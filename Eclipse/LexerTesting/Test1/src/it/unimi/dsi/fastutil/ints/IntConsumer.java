//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;

import testing.util.LOG;

@FunctionalInterface
public interface IntConsumer extends Consumer<Integer>, java.util.function.IntConsumer {
    /** @deprecated */
    @Deprecated
    default void accept(Integer t) {
        this.accept(t);
    }

    default IntConsumer andThen(java.util.function.IntConsumer after) {
        Objects.requireNonNull(after);
        return (t) -> {
            this.accept(t);
            after.accept(t);
        };
    }

    default IntConsumer andThen(IntConsumer after) {
        return this.andThen((java.util.function.IntConsumer)after);
    }

    /** @deprecated */
    @Deprecated
    default Consumer<Integer> andThen(Consumer<? super Integer> after) {
//        return super.andThen(after);
    	LOG.error("NOT IMPL");
    	return null;
    }
}
