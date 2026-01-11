//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Predicate;

import testing.util.LOG;

@FunctionalInterface
public interface IntPredicate extends java.util.function.IntPredicate, Predicate<Integer> {
    /** @deprecated */
    @Deprecated
    default boolean test(Integer t) {
        return this.test(t);
    }

    default IntPredicate and(java.util.function.IntPredicate other) {
        Objects.requireNonNull(other);
        return (t) -> this.test(t) && other.test(t);
    }

    /** @deprecated */
    @Deprecated
    default Predicate<Integer> and(Predicate<? super Integer> other) {
//        return super.and(other);
    	LOG.error("NOT IMPL");
    	return null;
    }

    default IntPredicate negate() {
        return (t) -> !this.test(t);
    }

    default IntPredicate or(java.util.function.IntPredicate other) {
        Objects.requireNonNull(other);
        return (t) -> this.test(t) || other.test(t);
    }

    /** @deprecated */
    @Deprecated
    default Predicate<Integer> or(Predicate<? super Integer> other) {
//        return super.or(other);
    	LOG.error("NOT IMPL");
    	return null;
    }
}
