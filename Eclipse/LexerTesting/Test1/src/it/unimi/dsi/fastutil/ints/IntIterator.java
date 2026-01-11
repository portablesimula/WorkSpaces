//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntIterator extends PrimitiveIterator.OfInt {
    int nextInt();

    /** @deprecated */
    @Deprecated
    default Integer next() {
        return this.nextInt();
    }

    /** @deprecated */
    @Deprecated
    default void forEachRemaining(Consumer<? super Integer> action) {
        IntConsumer var10001;
        if (action instanceof IntConsumer) {
            var10001 = (IntConsumer)action;
        } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
        }

        this.forEachRemaining(var10001);
    }
}
