package com.intellij.openapi.fileTypes;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.Arrays;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SyntaxHighlighterBase implements SyntaxHighlighter {
    private static final Logger LOG = Logger.getInstance(SyntaxHighlighterBase.class);
    /** @deprecated */
    @Deprecated
    protected static final TextAttributesKey[] EMPTY;

    public static TextAttributesKey @NotNull [] pack(@Nullable TextAttributesKey key) {
        TextAttributesKey[] var10000 = key == null ? TextAttributesKey.EMPTY_ARRAY : new TextAttributesKey[]{key};
        if (var10000 == null) {
            $$$reportNull$$$0(0);
        }

        return var10000;
    }

    public static TextAttributesKey @NotNull [] pack(@Nullable TextAttributesKey key1, @Nullable TextAttributesKey key2) {
        if (key1 == null) {
            return pack(key2);
        } else if (key2 == null) {
            return pack(key1);
        } else {
            TextAttributesKey[] var10000 = new TextAttributesKey[]{key1, key2};
            if (var10000 == null) {
                $$$reportNull$$$0(1);
            }

            return var10000;
        }
    }

    public static TextAttributesKey @NotNull [] pack(TextAttributesKey @NotNull [] base, @Nullable TextAttributesKey key) {
        if (base == null) {
            $$$reportNull$$$0(2);
        }

        if (key == null) {
            if (base == null) {
                $$$reportNull$$$0(3);
            }

            return base;
        } else {
            TextAttributesKey[] result = (TextAttributesKey[])Arrays.copyOf(base, base.length + 1);
            result[base.length] = key;
            if (result == null) {
                $$$reportNull$$$0(4);
            }

            return result;
        }
    }

    public static @NotNull TextAttributesKey @NotNull [] pack(@Nullable TextAttributesKey key, @NotNull TextAttributesKey @NotNull [] base) {
        if (key == null) {
            return base;
        } else {
            TextAttributesKey[] result = new TextAttributesKey[base.length + 1];
            System.arraycopy(base, 0, result, 1, base.length);
            result[0] = key;
            return result;
        }
    }

    public static @NotNull TextAttributesKey @NotNull [] pack(TextAttributesKey @NotNull [] base, @Nullable TextAttributesKey t1, @Nullable TextAttributesKey t2) {
        if (base == null) {
            $$$reportNull$$$0(8);
        }

        int add = 0;
        if (t1 != null) {
            ++add;
        }

        if (t2 != null) {
            ++add;
        }

        if (add == 0) {
            return base;
        } else {
            TextAttributesKey[] result = (TextAttributesKey[])Arrays.copyOf(base, base.length + add);
            add = base.length;
            if (t1 != null) {
                result[add++] = t1;
            }

            if (t2 != null) {
                result[add] = t2;
            }

            return result;
        }
    }

    public static void fillMap(@NotNull Map<? super IElementType, ? super TextAttributesKey> map, @NotNull TokenSet keys, @NotNull TextAttributesKey value) {
        if (value == null) {
            $$$reportNull$$$0(13);
        }

        fillMap(map, value, keys.getTypes());
    }

    protected static void fillMap(@NotNull Map<? super IElementType, ? super TextAttributesKey> map, @NotNull TextAttributesKey value, @NotNull IElementType... types) {
        if (types == null) {
            $$$reportNull$$$0(16);
        }

        for(IElementType type : types) {
            map.put(type, value);
        }

    }

    protected static void safeMap(@NotNull Map<IElementType, TextAttributesKey> map, @NotNull TokenSet keys, @NotNull TextAttributesKey value) {
        if (value == null) {
            $$$reportNull$$$0(19);
        }

        for(IElementType type : keys.getTypes()) {
            safeMap(map, type, value);
        }

    }

    protected static void safeMap(@NotNull Map<IElementType, TextAttributesKey> map, @NotNull IElementType type, @NotNull TextAttributesKey value) {
        if (value == null) {
            $$$reportNull$$$0(22);
        }

        TextAttributesKey oldVal = (TextAttributesKey)map.put(type, value);
        if (oldVal != null && !oldVal.equals(value)) {
            LOG.error("Remapping highlighting for \"" + String.valueOf(type) + "\" val: old=" + String.valueOf(oldVal) + " new=" + String.valueOf(value));
        }

    }

    static {
        EMPTY = TextAttributesKey.EMPTY_ARRAY;
    }
}
