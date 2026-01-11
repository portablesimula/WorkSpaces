package com.intellij.util;

import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

public interface CharTable {
    Key<CharTable> CHAR_TABLE_KEY = new Key("Char table");

    @NotNull CharSequence intern(@NotNull CharSequence var1);

    @NotNull CharSequence intern(@NotNull CharSequence var1, int var2, int var3);
}
