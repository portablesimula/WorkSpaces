//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntSpliterator extends Spliterator.OfInt {
    /** @deprecated */
    @Deprecated
    default boolean tryAdvance(Consumer<? super Integer> action) {
        IntConsumer var10001;
        if (action instanceof IntConsumer) {
            var10001 = (IntConsumer)action;
        } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
        }

        return this.tryAdvance(var10001);
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

    IntSpliterator trySplit();

    default IntComparator getComparator() {
        throw new IllegalStateException();
    }
}
