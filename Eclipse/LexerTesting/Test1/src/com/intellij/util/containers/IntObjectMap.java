package com.intellij.util.containers;

import java.util.Collection;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Debug.Renderer;

@Renderer(
    text = "\"size = \" + size()",
    hasChildren = "!isEmpty()",
    childrenArray = "entrySet().toArray()"
)
public interface IntObjectMap<V> {
    V put(int var1, @NotNull V var2);

    V get(int var1);

    V remove(int var1);

    boolean containsKey(int var1);

    void clear();

    int @NotNull [] keys();

    int size();

    boolean isEmpty();

    @NotNull Collection<@NotNull V> values();

    boolean containsValue(@NotNull V var1);

    @NotNull Set<@NotNull Entry<V>> entrySet();

    @Renderer(
        text = "getKey() + \" -> \\\"\" + getValue() + \"\\\"\""
    )
    public interface Entry<V> {
        int getKey();

        @NotNull V getValue();
    }
}
