package com.intellij.openapi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UserDataHolderEx extends UserDataHolder {
    <T> @NotNull T putUserDataIfAbsent(@NotNull Key<T> var1, @NotNull T var2);

    <T> boolean replace(@NotNull Key<T> var1, @Nullable T var2, @Nullable T var3);
}
