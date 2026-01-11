//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface IntUnaryOperator extends java.util.function.IntUnaryOperator, UnaryOperator<Integer> {
    int apply(int var1);

    /** @deprecated */
    @Deprecated
    default int applyAsInt(int x) {
        return this.apply(x);
    }

    /** @deprecated */
    @Deprecated
    default Integer apply(Integer x) {
        return this.apply(x);
    }
}
