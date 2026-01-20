//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.util.ListIterator;

import simula.compiler.utilities.LOG;

public interface IntListIterator extends IntBidirectionalIterator, ListIterator<Integer> {
    default void set(int k) {
        throw new UnsupportedOperationException();
    }

    default void add(int k) {
        throw new UnsupportedOperationException();
    }

    default void remove() {
        throw new UnsupportedOperationException();
    }

    /** @deprecated */
    @Deprecated
    default void set(Integer k) {
        this.set(k);
    }

    /** @deprecated */
    @Deprecated
    default void add(Integer k) {
        this.add(k);
    }

    /** @deprecated */
    @Deprecated
    default Integer next() {
//        return super.next();
    	LOG.error("NOT IMPL: IntListIterator.next");
    	return null;
    }

    /** @deprecated */
    @Deprecated
    default Integer previous() {
//        return super.previous();
    	LOG.error("NOT IMPL: IntListIterator.previous");
    	return null;
    }
}
