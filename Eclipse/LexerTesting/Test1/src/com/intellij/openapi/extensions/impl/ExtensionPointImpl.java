
// Her er en oversettelse av den oppgitte Kotlin-koden til Java.
// Siden originalen bruker moderne JetBrains-interne biblioteker (som persistentListOf og Java11Shim),
// må man i Java manuelt håndtere trådsikkerhet og immutable lister, ofte ved bruk av AtomicReferenceFieldUpdater.

//Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.extensions.impl;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.*;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.Java11Shim;
import kotlinx.collections.immutable.PersistentList;
import kotlinx.collections.immutable.PersistentCollectionsKt;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@ApiStatus.Internal
public abstract class ExtensionPointImpl<T> implements ExtensionPoint<T>, Iterable<T> {
 private static final Logger LOG = Logger.getInstance(ExtensionPointImpl.class);

 public final String name;
 public final String className;
 private final PluginDescriptor extensionPointPluginDescriptor;
 public final ComponentManager componentManager;
 private Class<T> extensionClass;
 private final boolean isDynamic;

 private volatile List<T> cachedExtensions;
 private volatile T[] cachedExtensionsAsArray;
 private volatile List<ExtensionComponentAdapter> adapters = Java11Shim.INSTANCE.listOf();
 private volatile boolean adaptersAreSorted = true;

 // Bruker kotlinx persistent list for å matche Kotlin-oppførsel for listeners
 private volatile PersistentList<ExtensionPointListener<T>> listeners = PersistentCollectionsKt.persistentListOf();

 private volatile ConcurrentMap<?, Map<?, ?>> keyMapperToCache;

 private static final AtomicReferenceFieldUpdater<ExtensionPointImpl, PersistentList> listenerUpdater =
         AtomicReferenceFieldUpdater.newUpdater(ExtensionPointImpl.class, PersistentList.class, "listeners");

 private static final AtomicReferenceFieldUpdater<ExtensionPointImpl, ConcurrentMap> keyMapperToCacheUpdater =
         AtomicReferenceFieldUpdater.newUpdater(ExtensionPointImpl.class, ConcurrentMap.class, "keyMapperToCache");

 private static Consumer<StackTraceElement[]> CHECK_CANCELED = (stackTrace) -> {};

 public ExtensionPointImpl(String name,
                           String className,
                           PluginDescriptor extensionPointPluginDescriptor,
                           ComponentManager componentManager,
                           Class<T> extensionClass,
                           boolean isDynamic) {
     this.name = name;
     this.className = className;
     this.extensionPointPluginDescriptor = extensionPointPluginDescriptor;
     this.componentManager = componentManager;
     this.extensionClass = extensionClass;
     this.isDynamic = isDynamic;
 }

 public static void setCheckCanceledAction(Runnable checkCanceled) {
     CHECK_CANCELED = (stackTrace) -> {
         try {
             checkCanceled.run();
         } catch (ProcessCanceledException e) {
             if (!isInsideClassInitializer(e.getStackTrace())) {
                 throw e;
             }
         }
     };
 }

 @SuppressWarnings("unchecked")
 public <CACHE_KEY, V> ConcurrentMap<CACHE_KEY, V> getCacheMap() {
     ConcurrentMap<?, Map<?, ?>> map = keyMapperToCache;
     if (map == null) {
         keyMapperToCacheUpdater.compareAndSet(this, null, new ConcurrentHashMap<Object, Map<?, ?>>());
         map = keyMapperToCache;
     }
     return (ConcurrentMap<CACHE_KEY, V>) map;
 }

 @Override
 public final boolean isDynamic() {
     return isDynamic;
 }

 @Override
 public final void registerExtension(@NotNull T extension) {
     doRegisterExtension(extension, LoadingOrder.ANY, extensionPointPluginDescriptor, null);
 }

 @Override
 public final void registerExtension(@NotNull T extension, @NotNull Disposable parentDisposable) {
     registerExtension(extension, extensionPointPluginDescriptor, parentDisposable);
 }

 @Override
 public final void registerExtension(@NotNull T extension, @NotNull PluginDescriptor pluginDescriptor, @NotNull Disposable parentDisposable) {
     doRegisterExtension(extension, LoadingOrder.ANY, pluginDescriptor, parentDisposable);
 }

 @Override
 public final PluginDescriptor getPluginDescriptor() {
     return extensionPointPluginDescriptor;
 }

 @Override
 public final void registerExtension(@NotNull T extension, @NotNull LoadingOrder order, @NotNull Disposable parentDisposable) {
     doRegisterExtension(extension, order, getPluginDescriptor(), parentDisposable);
 }

 private void doRegisterExtension(@NotNull T extension,
                                  @NotNull LoadingOrder order,
                                  @NotNull PluginDescriptor pluginDescriptor,
                                  @Nullable Disposable parentDisposable) {
     checkExtensionType(extension, getExtensionClass(), null);

     ObjectComponentAdapter adapter = new ObjectComponentAdapter(extension, pluginDescriptor, order);
     synchronized (this) {
         for (ExtensionComponentAdapter a : adapters) {
             if (a instanceof ObjectComponentAdapter && ((ObjectComponentAdapter<?>) a).getInstance() == extension) {
                 LOG.error("Extension was already added: " + extension);
                 return;
             }
         }

         assertNotReadOnlyMode();
         addExtensionAdapter(adapter);
     }

     notifyListeners(false, Java11Shim.INSTANCE.listOf(adapter), listeners);

     if (parentDisposable != null) {
         Disposer.register(parentDisposable, () -> {
             synchronized (this) {
                 int index = -1;
                 for (int i = 0; i < adapters.size(); i++) {
                     if (adapters.get(i) == adapter) {
                         index = i;
                         break;
                     }
                 }

                 if (index < 0) {
                     LOG.error("Extension to be removed not found: " + adapter.getInstance());
                 } else {
                     int finalIndex = index;
                     adapters = mutateAdapters(adapters, l -> l.remove(finalIndex));
                     clearCache();
                 }
             }
             notifyListeners(true, Java11Shim.INSTANCE.listOf(adapter), listeners);
         });
     }
 }

 public void registerExtensions(@NotNull List<T> extensions) {
     for (ExtensionComponentAdapter adapter : adapters) {
         if (adapter instanceof ObjectComponentAdapter) {
             Object instance = ((ObjectComponentAdapter<?>) adapter).getInstance();
             for (T ext : extensions) {
                 if (ext == instance) {
                     LOG.error("Extension was already added: " + instance);
                     return;
                 }
             }
         }
     }

     List<ExtensionComponentAdapter> newAdapters = doRegisterExtensions(extensions);
     notifyListeners(false, newAdapters, listeners);
 }

 private List<ExtensionComponentAdapter> mutateAdapters(
         List<ExtensionComponentAdapter> l,
         Consumer<List<ExtensionComponentAdapter>> operation
 ) {
     List<ExtensionComponentAdapter> result = new ArrayList<>(l);
     operation.accept(result);
     return Java11Shim.INSTANCE.copyOfList(result);
 }

 private synchronized List<ExtensionComponentAdapter> doRegisterExtensions(List<T> extensions) {
     List<ExtensionComponentAdapter> newAdapters = extensions.stream()
             .map(it -> new ObjectComponentAdapter(it, getPluginDescriptor(), LoadingOrder.ANY))
             .collect(Collectors.toList());

     List<ExtensionComponentAdapter> oldAdapters = adapters;
     adapters = mutateAdapters(oldAdapters, l -> {
         l.addAll(findInsertionIndexForAnyOrder(oldAdapters), newAdapters);
     });
     clearCache();
     return newAdapters;
 }

 // Abstrakt/hjelpemetoder som antas eksistere i baseklassen eller andre steder i kildekoden:
 protected abstract Class<T> getExtensionClass();
 protected abstract void checkExtensionType(T extension, Class<T> extensionClass, ExtensionComponentAdapter adapter);
 protected abstract void assertNotReadOnlyMode();
 protected abstract void addExtensionAdapter(ExtensionComponentAdapter adapter);
 protected abstract void clearCache();
 protected abstract int findInsertionIndexForAnyOrder(List<ExtensionComponentAdapter> adapters);
 protected abstract void notifyListeners(boolean isRemoved, List<ExtensionComponentAdapter> adapters, List<ExtensionPointListener<T>> listeners);
 private static boolean isInsideClassInitializer(StackTraceElement[] stackTrace) { return false; }

 @NotNull
 @Override
 public Iterator<T> iterator() {
     // Implementasjon av Sequence<T> / Iterable<T>
     return getExtensionsList().iterator();
 }

 private List<T> getExtensionsList() {
     // Forenklet for eksempelets skyld
     return cachedExtensions != null ? cachedExtensions : Collections.emptyList();
 }
}
