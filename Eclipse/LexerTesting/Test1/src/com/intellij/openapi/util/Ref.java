//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.openapi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Ref<T> {
    private T myValue;

    public Ref() {
    }

    public Ref(@Nullable T value) {
        this.myValue = value;
    }

    public final boolean isNull() {
        return this.myValue == null;
    }

    public final T get() {
        return this.myValue;
    }

    public final void set(@Nullable T value) {
        this.myValue = value;
    }

    public final boolean setIfNull(@Nullable T value) {
        boolean result = this.myValue == null && value != null;
        if (result) {
            this.myValue = value;
        }

        return result;
    }

    @NotNull
    public static <T> Ref<T> create() {
        return new Ref<T>();
    }

    public static <T> Ref<T> create(@Nullable T value) {
        return new Ref<T>(value);
    }

    @Nullable
    public static <T> T deref(@Nullable Ref<T> ref) {
        return (T)(ref == null ? null : ref.get());
    }

    public String toString() {
        return String.valueOf(this.myValue);
    }
}
