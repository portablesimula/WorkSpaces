//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.util.function.Function;
import java.util.function.IntFunction;

@FunctionalInterface
public interface Int2ObjectFunction<V> extends it.unimi.dsi.fastutil.Function<Integer, V>, IntFunction<V> {
    default V apply(int operand) {
        return (V)this.get(operand);
    }

    default V put(int key, V value) {
        throw new UnsupportedOperationException();
    }

    V get(int var1);

    default V getOrDefault(int key, V defaultValue) {
        V v;
        return (V)((v = (V)this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v);
    }

    default V remove(int key) {
        throw new UnsupportedOperationException();
    }

    /** @deprecated */
    @Deprecated
    default V put(Integer key, V value) {
        int k = key;
        boolean containsKey = this.containsKey(k);
        V v = (V)this.put(k, value);
        return (V)(containsKey ? v : null);
    }

    /** @deprecated */
    @Deprecated
    default V get(Object key) {
        if (key == null) {
            return null;
        } else {
            int k = (Integer)key;
            V v;
            return (V)((v = (V)this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v);
        }
    }

    /** @deprecated */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key == null) {
            return defaultValue;
        } else {
            int k = (Integer)key;
            V v = (V)this.get(k);
            return (V)(v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v);
        }
    }

    /** @deprecated */
    @Deprecated
    default V remove(Object key) {
        if (key == null) {
            return null;
        } else {
            int k = (Integer)key;
            return (V)(this.containsKey(k) ? this.remove(k) : null);
        }
    }

    default boolean containsKey(int key) {
        return true;
    }

    /** @deprecated */
    @Deprecated
    default boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Integer)key);
    }

    default void defaultReturnValue(V rv) {
        throw new UnsupportedOperationException();
    }

    default V defaultReturnValue() {
        return null;
    }

    /** @deprecated */
    @Deprecated
    default <T> Function<T, V> compose(Function<? super T, ? extends Integer> before) {
        return super.compose(before);
    }
}
