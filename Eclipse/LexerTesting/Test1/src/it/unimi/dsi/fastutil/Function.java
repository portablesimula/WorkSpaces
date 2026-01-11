//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil;

@FunctionalInterface
public interface Function<K, V> extends java.util.function.Function<K, V> {
    default V apply(K key) {
        return (V)this.get(key);
    }

    V get(Object var1);

    default boolean containsKey(Object key) {
        return true;
    }

    default int size() {
        return -1;
    }

    default void clear() {
        throw new UnsupportedOperationException();
    }
}
