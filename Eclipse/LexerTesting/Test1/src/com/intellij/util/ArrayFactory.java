package com.intellij.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ArrayFactory<T> {
    T @NotNull [] create(int var1);
}
