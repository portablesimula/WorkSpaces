//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntIterable extends Iterable<Integer> {
    IntIterator iterator();

    default IntIterator intIterator() {
        return this.iterator();
    }

    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliteratorUnknownSize(this.iterator(), 0);
    }

    default IntSpliterator intSpliterator() {
        return this.spliterator();
    }

    default void forEach(IntConsumer action) {
        Objects.requireNonNull(action);
        this.iterator().forEachRemaining(action);
    }

    default void forEach(it.unimi.dsi.fastutil.ints.IntConsumer action) {
        this.forEach((IntConsumer)action);
    }

    /** @deprecated */
    @Deprecated
    default void forEach(Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        IntConsumer var10001;
        if (action instanceof IntConsumer) {
            var10001 = (IntConsumer)action;
        } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
        }

        this.forEach(var10001);
    }
}
