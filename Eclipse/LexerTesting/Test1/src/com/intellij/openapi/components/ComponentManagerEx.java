
// Her er Kotlin-grensesnittet oversatt til Java.
// Merk at Kotlin-spesifikke konsepter som suspend, Sequence og
// standardverdier i parametere krever endringer i metodestrukturen for å fungere i Java.

// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.components;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.objectTree.ReferenceDelegatingDisposableInternal;
import com.intellij.openapi.client.ClientKind;
import com.intellij.openapi.extensions.PluginDescriptor;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.sequences.Sequence;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.picocontainer.ComponentAdapter;

import java.util.List;

@ApiStatus.Internal
public interface ComponentManagerEx extends ComponentManager, ReferenceDelegatingDisposableInternal {

  @ApiStatus.Experimental
  @ApiStatus.Internal
  @Nullable
  default <T> Object getServiceAsync(@NotNull Class<T> keyClass, @NotNull Continuation<? super T> $completion) {
    throw new AbstractMethodError();
  }

  @Nullable
  default <T> Object getServiceAsyncIfDefined(@NotNull Class<T> keyClass, @NotNull Continuation<? super T> $completion) {
    throw new AbstractMethodError();
  }

  @ApiStatus.Obsolete
  @ApiStatus.Internal
  @NotNull
  CoroutineScope getCoroutineScope();

  @ApiStatus.Internal
  @NotNull
  default ComponentManager getMutableComponentContainer() {
    return this;
  }

  @ApiStatus.Internal
  @NotNull
  @Override
  default Disposable getDisposableDelegate() {
    return this;
  }

  @ApiStatus.Internal
  @NotNull
  CoroutineScope instanceCoroutineScope(@NotNull Class<?> pluginClass);

  @ApiStatus.Internal
  @Nullable
  ComponentAdapter unregisterComponent(@NotNull Class<?> componentKey);

  @TestOnly
  @ApiStatus.Internal
  <T> void replaceServiceInstance(@NotNull Class<T> serviceInterface, @NotNull T instance, @NotNull Disposable parentDisposable);

  @ApiStatus.Internal
  @NotNull
  Sequence<Object> instances(boolean createIfNeeded, @Nullable Function1<? super Class<?>, Boolean> filter);

  // Overlastet metode for å støtte Kotlins standardverdi: filter = null
  @ApiStatus.Internal
  @NotNull
  default Sequence<Object> instances(boolean createIfNeeded) {
    return instances(createIfNeeded, null);
  }

  @ApiStatus.Internal
  void processAllImplementationClasses(@NotNull Function2<? super Class<?>, ? super PluginDescriptor, Unit> processor);

  /**
   * Use only if approved by core team.
   */
  @ApiStatus.Internal
  void registerService(
    @NotNull Class<?> serviceInterface,
    @NotNull Class<?> implementation,
    @NotNull PluginDescriptor pluginDescriptor,
    boolean override,
    @Nullable ClientKind clientKind
  );

  @ApiStatus.Internal
  @Nullable
  <T> T getServiceByClassName(@NotNull String serviceClassName);

  @ApiStatus.Internal
  void unloadServices(@NotNull IdeaPluginDescriptor module, @NotNull List<ServiceDescriptor> services);

  @ApiStatus.Internal
  void processAllHolders(@NotNull Function3<? super String, ? super Class<?>, ? super PluginDescriptor, Unit> processor);

  @ApiStatus.Internal
  @NotNull
  CoroutineScope pluginCoroutineScope(@NotNull ClassLoader pluginClassloader);

  @ApiStatus.Internal
  void stopServicePreloading();

  @ApiStatus.Internal
  @NotNull
  <T> List<T> collectInitializedComponents(@NotNull Class<T> aClass);

  @ApiStatus.Internal
  @NotNull
  String debugString();

  @ApiStatus.Internal
  boolean isServiceSuitable(@NotNull ServiceDescriptor descriptor);

  @ApiStatus.Internal
  <T> void registerServiceInstance(
    @NotNull Class<T> serviceInterface,
    @NotNull T instance,
    @NotNull PluginDescriptor pluginDescriptor
  );

  @ApiStatus.Internal
  @Nullable
  Class<?> getServiceImplementation(@NotNull Class<?> key);

  @ApiStatus.Internal
  <T> void replaceComponentInstance(@NotNull Class<T> componentKey, @NotNull T componentImplementation, @Nullable Disposable parentDisposable);

  @TestOnly
  @ApiStatus.Internal
  void registerComponentInstance(@NotNull Class<?> key, @NotNull Object instance);

  @ApiStatus.Internal
  void unregisterService(@NotNull Class<?> serviceInterface);

  @ApiStatus.Internal
  <T> void replaceRegularServiceInstance(@NotNull Class<T> serviceInterface, @NotNull T instance);
}
