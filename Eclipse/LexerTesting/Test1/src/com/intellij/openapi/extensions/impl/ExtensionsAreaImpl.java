
// Her er koden oversatt fra Kotlin til Java.
// Vær oppmerksom på at Kotlin-funksjoner som ligger på fil-nivå (som createExtensionPoints)
// må plasseres i en hjelpeklasse eller som statiske metoder i Java.

//Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.extensions.impl;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.*;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.containers.ContainerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.*;

@Internal
public final class ExtensionsAreaImpl implements ExtensionsArea {
 private static final Logger LOG = Logger.getInstance(ExtensionsAreaImpl.class);
 private static final boolean DEBUG_REGISTRATION = false;

 private final ComponentManager componentManager;
 private final Object lock = new Object();

 @SuppressWarnings("FieldMayBeFinal")
 private volatile Map<String, ExtensionPointImpl<?>> extensionPoints = Collections.emptyMap();
 private final Map<String, Throwable> epTraces = DEBUG_REGISTRATION ? new HashMap<>() : null;

 public ExtensionsAreaImpl(@NotNull ComponentManager componentManager) {
     this.componentManager = componentManager;
 }

 @Internal
 public static void createExtensionPoints(@NotNull List<ExtensionPointDescriptor> points,
                                          @NotNull ComponentManager componentManager,
                                          @NotNull Map<String, ExtensionPointImpl<?>> result,
                                          @NotNull PluginDescriptor pluginDescriptor) {
     for (ExtensionPointDescriptor descriptor : points) {
         String name = descriptor.getQualifiedName(pluginDescriptor);
         ExtensionPointImpl<Object> point;
         if (descriptor.isBean) {
             point = new BeanExtensionPoint<>(
                     name,
                     descriptor.className,
                     pluginDescriptor,
                     componentManager,
                     descriptor.isDynamic
             );
         } else {
             point = new InterfaceExtensionPoint<>(
                     name,
                     descriptor.className,
                     pluginDescriptor,
                     componentManager,
                     null,
                     descriptor.hasAttributes,
                     descriptor.isDynamic
             );
         }

         ExtensionPointImpl<?> old = result.putIfAbsent(point.getName(), point);
         if (old != null) {
             PluginDescriptor oldPluginDescriptor = old.getPluginDescriptor();
             throw componentManager.createError(
                     "Duplicate registration for EP " + point.getName() + " first in " + oldPluginDescriptor + ", second in " + pluginDescriptor,
                     pluginDescriptor.getPluginId()
             );
         }
     }
 }

 @NotNull
 @Override
 public Map<String, ExtensionPointImpl<?>> getNameToPointMap() {
     return extensionPoints;
 }

 public void reset(@NotNull Map<String, ExtensionPointImpl<?>> nameToPointMap) {
     this.extensionPoints = nameToPointMap;
 }

 @TestOnly
 public void notifyAreaReplaced(@Nullable ExtensionsAreaImpl newArea) {
     Set<String> processedEPs = new HashSet<>(extensionPoints.size());
     for (ExtensionPointImpl<?> point : extensionPoints.values()) {
         point.notifyAreaReplaced(this);
         processedEPs.add(point.getName());
     }

     if (newArea == null) {
         return;
     }

     for (ExtensionPointImpl<?> point : newArea.extensionPoints.values()) {
         if (!processedEPs.contains(point.getName())) {
             point.notifyAreaReplaced(this);
         }
     }
 }

 @TestOnly
 public void registerExtensionPoints(@NotNull PluginDescriptor pluginDescriptor, @NotNull List<Element> extensionPointElements) {
     for (Element element : extensionPointElements) {
         String pointName = element.getAttributeValue("qualifiedName");
         if (pointName == null) {
             String name = element.getAttributeValue("name");
             if (name == null) {
                 throw componentManager.createError("'name' attribute not specified for extension point in '" + pluginDescriptor + "' plugin",
                         pluginDescriptor.getPluginId());
             }
             pointName = pluginDescriptor.getPluginId().getIdString() + '.' + name;
         }

         String beanClassName = element.getAttributeValue("beanClass");
         String interfaceClassName = element.getAttributeValue("interface");

         if (beanClassName == null && interfaceClassName == null) {
             throw componentManager.createError(
                     "Neither 'beanClass' nor 'interface' attribute is specified for extension point '" + pointName + "' in '" + pluginDescriptor + "' plugin",
                     pluginDescriptor.getPluginId()
             );
         }
         if (beanClassName != null && interfaceClassName != null) {
             throw componentManager.createError(
                     "Both 'beanClass' and 'interface' attributes are specified for extension point '" + pointName + "' in '" + pluginDescriptor + "' plugin",
                     pluginDescriptor.getPluginId()
             );
         }

         boolean dynamic = Boolean.parseBoolean(element.getAttributeValue("dynamic"));
         doRegisterExtensionPoint(
                 pointName,
                 interfaceClassName != null ? interfaceClassName : beanClassName,
                 pluginDescriptor,
                 interfaceClassName != null,
                 dynamic
         );
     }
 }

 private <T> void doRegisterExtensionPoint(@NotNull String name,
                                           @NotNull String extensionClass,
                                           @NotNull PluginDescriptor pluginDescriptor,
                                           boolean isInterface,
                                           boolean dynamic) {
     // Implementasjon av doRegisterExtensionPoint avhenger av resten av klassen/systemet
 }

 public boolean unregisterExtensions(@NotNull String extensionPointName,
                                     @NotNull PluginDescriptor pluginDescriptor,
                                     @NotNull List<Runnable> priorityListenerCallbacks,
                                     @NotNull List<Runnable> listenerCallbacks) {
     ExtensionPointImpl<?> point = extensionPoints.get(extensionPointName);
     if (point == null) return false;

     point.unregisterExtensions(componentManager, pluginDescriptor, priorityListenerCallbacks, listenerCallbacks);
     return true;
 }

 public void resetExtensionPoints(@NotNull List<ExtensionPointDescriptor> descriptors, @NotNull PluginDescriptor pluginDescriptor) {
     for (ExtensionPointDescriptor descriptor : descriptors) {
         ExtensionPointImpl<?> point = extensionPoints.get(descriptor.getQualifiedName(pluginDescriptor));
         if (point != null) {
             point.reset();
         }
     }
 }

 public void clearUserCache() {
     for (ExtensionPointImpl<?> point : extensionPoints.values()) {
         point.clearUserCache();
     }
 }

 public void unregisterExtensionPoints(@NotNull List<ExtensionPointDescriptor> descriptors, @NotNull PluginDescriptor pluginDescriptor) {
     if (descriptors.isEmpty()) return;

     synchronized (lock) {
         Map<String, ExtensionPointImpl<?>> newMap = new HashMap<>(extensionPoints);
         for (ExtensionPointDescriptor descriptor : descriptors) {
             newMap.remove(descriptor.getQualifiedName(pluginDescriptor));
         }
         extensionPoints = Collections.unmodifiableMap(newMap);
     }
 }

 @TestOnly
 public void registerExtensionPoint(@NotNull BaseExtensionPointName<?> extensionPoint,
                                    @NotNull String extensionPointBeanClass,
                                    @NotNull ExtensionPoint.Kind kind,
                                    @NotNull Disposable parentDisposable) {
     String extensionPointName = extensionPoint.getName();
     registerExtensionPoint(extensionPointName, extensionPointBeanClass, kind, false);
     Disposer.register(parentDisposable, () -> unregisterExtensionPoint(extensionPointName));
 }

 @TestOnly
 @Override
 public void registerExtensionPoint(@NotNull String extensionPointName,
                                    @NotNull String extensionPointBeanClass,
                                    @NotNull ExtensionPoint.Kind kind,
                                    boolean isDynamic) {
     PluginDescriptor pluginDescriptor = new DefaultPluginDescriptor(PluginId.getId("fakeIdForTests"));
     doRegisterExtensionPoint(
             extensionPointName,
             extensionPointBeanClass,
             pluginDescriptor,
             kind == ExtensionPoint.Kind.INTERFACE,
             isDynamic
     );
 }
 
 // Merk: unregisterExtensionPoint må også implementeres for å matche Disposer-kallet.
 @TestOnly
 public void unregisterExtensionPoint(@NotNull String extensionPointName) {
     synchronized (lock) {
         Map<String, ExtensionPointImpl<?>> newMap = new HashMap<>(extensionPoints);
         newMap.remove(extensionPointName);
         extensionPoints = Collections.unmodifiableMap(newMap);
     }
 }
}
