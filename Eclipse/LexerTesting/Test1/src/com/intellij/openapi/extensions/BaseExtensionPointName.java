// Her er den tilsvarende Java-koden for IntelliJ-plattformklassen.
// Siden sealed i Kotlin tilsvarer sealed i moderne Java (fra versjon 17+),
// er dette den mest nøyaktige oversettelsen:
package com.intellij.openapi.extensions;

import com.intellij.openapi.extensions.impl.ExtensionPointImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed class BaseExtensionPointName<T> permits ExtensionPointName, ProjectExtensionPointName {
//public class BaseExtensionPointName<T> ExtensionPointName, ProjectExtensionPointName {
    private final String name;

    protected BaseExtensionPointName(@NonNls @NotNull String name) {
        this.name = name;
    }

    @NonNls
    @NotNull
    public final String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @NotNull
    final ExtensionPointImpl<T> getPointImpl(@Nullable AreaInstance areaInstance) {
        // Implementasjon ikke tilgjengelig i stub
        throw new UnsupportedOperationException();
    }
}


//// Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
//package com.intellij.openapi.extensions;
//
//import com.intellij.openapi.extensions.impl.ExtensionPointImpl;
//import com.intellij.openapi.extensions.impl.ExtensionsAreaImpl;
//import org.jetbrains.annotations.NonNls;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Objects;
//
//public abstract class BaseExtensionPointName<T> {
//    private final String name;
//
//    BaseExtensionPointName(@NotNull @NonNls String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        return name;
//    }
//
//    @NotNull
//    ExtensionPointImpl<T> getPointImpl(@Nullable AreaInstance areaInstance) {
//        ExtensionsArea area;
//        if (areaInstance != null && areaInstance.getExtensionArea() != null) {
//            area = areaInstance.getExtensionArea();
//        } else {
//            area = Extensions.getRootArea();
//        }
//
//        if (area == null) {
//            throw new IllegalStateException(
//                "Can't get extension point. If you're running a JUnit5 test, make sure the test class is annotated with `@TestApplication`.\n" +
//                "Check out `com.intellij.testFramework.junit5.showcase.JUnit5ApplicationTest` for an example."
//            );
//        }
//
//        return ((ExtensionsAreaImpl) area).getExtensionPoint(name);
//    }
//
//    @NotNull
//    ExtensionPointImpl<T> getRootPoint() {
//        ExtensionsArea area = Extensions.getRootArea();
//        if (area == null) {
//            throw new IllegalStateException(
//                "Can't get extension point. If you're running a JUnit5 test, make sure the test class is annotated with `@TestApplication`.\n" +
//                "Check out `com.intellij.testFramework.junit5.showcase.JUnit5ApplicationTest` for an example."
//            );
//        }
//
//        return ((ExtensionsAreaImpl) area).getExtensionPoint(name);
//    }
//
//    @NotNull
//    public String getName() {
//        return name;
//    }
//}
