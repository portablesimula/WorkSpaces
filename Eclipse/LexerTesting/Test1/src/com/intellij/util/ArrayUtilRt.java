package com.intellij.util;

import java.io.File;
import java.util.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ArrayUtilRt {
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Collection[] EMPTY_COLLECTION_ARRAY = new Collection[0];
    public static final File[] EMPTY_FILE_ARRAY = new File[0];

    private ArrayUtilRt() {
    }

    @NotNull
    @Contract(
        pure = true
    )
    public static String[] toStringArray(@Nullable Collection<String> collection) {
        return collection != null && !collection.isEmpty() ? (String[])collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY;
    }

    @Contract(
        pure = true
    )
    public static <T> int find(@NotNull T[] src, @Nullable T obj) {
        return indexOf(src, obj, 0, src.length);
    }

    @Contract(
        pure = true
    )
    public static <T> int indexOf(@NotNull T[] src, @Nullable T obj, int start, int end) {
        if (obj == null) {
            for(int i = start; i < end; ++i) {
                if (src[i] == null) {
                    return i;
                }
            }
        } else {
            for(int i = start; i < end; ++i) {
                if (obj.equals(src[i])) {
                    return i;
                }
            }
        }

        return -1;
    }
}
