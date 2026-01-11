package com.intellij.openapi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UserDataHolder {
    <T> @Nullable T getUserData(@NotNull Key<T> var1);

    <T> void putUserData(@NotNull Key<T> var1, @Nullable T var2);
}
