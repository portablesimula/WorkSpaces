// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.extensions;

// Oversatt fra Kotlin:
import com.intellij.openapi.extensions.impl.ExtensionPointImpl;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.function.Consumer;

public interface ExtensionsArea {

    @TestOnly
    void registerExtensionPoint(@NonNls @NotNull String extensionPointName,
                                @NotNull String extensionPointBeanClass,
                                @NotNull ExtensionPoint.Kind kind,
                                boolean isDynamic);

    @TestOnly
    @Deprecated
    default void registerExtensionPoint(@NonNls @NotNull String extensionPointName,
                                        @NotNull String extensionPointBeanClass,
                                        @NotNull ExtensionPoint.Kind kind) {
        registerExtensionPoint(extensionPointName, extensionPointBeanClass, kind, false);
    }

    @TestOnly
    void unregisterExtensionPoint(@NonNls @NotNull String extensionPointName);

    boolean hasExtensionPoint(@NonNls @NotNull String extensionPointName);

    boolean hasExtensionPoint(@NotNull ExtensionPointName<?> extensionPointName);

    @NotNull
    <T> ExtensionPoint<T> getExtensionPoint(@NonNls @NotNull String extensionPointName);

    @Nullable
    <T> ExtensionPoint<T> getExtensionPointIfRegistered(@NotNull String extensionPointName);

    @NotNull
    <T> ExtensionPoint<T> getExtensionPoint(@NotNull ExtensionPointName<T> extensionPointName);

    @Internal
    @NotNull
    @Unmodifiable
    Map<String, ExtensionPointImpl<?>> getNameToPointMap();

    @TestOnly
    @Internal
    void processExtensionPoints(@NotNull Consumer<? super ExtensionPointImpl<?>> consumer);
}
