//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface Object2IntFunction<K> extends it.unimi.dsi.fastutil.Function<K, Integer>, ToIntFunction<K> {
    default int applyAsInt(K operand) {
        return this.getInt(operand);
    }

    default int put(K key, int value) {
        throw new UnsupportedOperationException();
    }

    int getInt(Object var1);

    default int getOrDefault(Object key, int defaultValue) {
        int v;
        return (v = this.getInt(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
    }

    default int removeInt(Object key) {
        throw new UnsupportedOperationException();
    }

    /** @deprecated */
    @Deprecated
    default Integer put(K key, Integer value) {
        boolean containsKey = this.containsKey(key);
        int v = this.put(key, value);
        return containsKey ? v : null;
    }

    /** @deprecated */
    @Deprecated
    default Integer get(Object key) {
        int v;
        return (v = this.getInt(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
    }

    /** @deprecated */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        int v = this.getInt(key);
        return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
    }

    /** @deprecated */
    @Deprecated
    default Integer remove(Object key) {
        return this.containsKey(key) ? this.removeInt(key) : null;
    }

    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default int defaultReturnValue() {
        return 0;
    }

    /** @deprecated */
    @Deprecated
    default <T> Function<K, T> andThen(Function<? super Integer, ? extends T> after) {
        return super.andThen(after);
    }
}
