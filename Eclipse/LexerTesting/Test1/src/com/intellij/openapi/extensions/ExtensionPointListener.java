
// Her er koden oversatt til Java.
// Siden Kotlin-grensesnittet bruker standardimplementasjoner (tomme krøllparenteser),
// oversettes disse til default-metoder i Java:

// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.extensions;

import org.jetbrains.annotations.NotNull;

public interface ExtensionPointListener<T> {
    default void extensionAdded(@NotNull T extension, @NotNull PluginDescriptor pluginDescriptor) {
    }

    default void extensionRemoved(@NotNull T extension, @NotNull PluginDescriptor pluginDescriptor) {
    }
}
