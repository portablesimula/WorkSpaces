//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Size64;
import simula.compiler.utilities.LOG;

import java.util.Set;

public interface IntSet extends IntCollection, Set<Integer> {
    IntIterator iterator();

    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 321);
    }

    boolean remove(int var1);

    /** @deprecated */
    @Deprecated
    default boolean remove(Object o) {
//        return super.remove(o);
    	LOG.error("NOT IMPL");
    	return false;
    }

    /** @deprecated */
    @Deprecated
    default boolean add(Integer o) {
//        return super.add(o);
    	LOG.error("NOT IMPL");
    	return false;
    }

    /** @deprecated */
    @Deprecated
    default boolean contains(Object o) {
//        return super.contains(o);
    	LOG.error("NOT IMPL");
    	return false;
    }

    /** @deprecated */
    @Deprecated
    default boolean rem(int k) {
        return this.remove(k);
    }

    static IntSet of() {
        return IntSets.UNMODIFIABLE_EMPTY_SET;
    }

    static IntSet of(int e) {
        return IntSets.singleton(e);
    }

    static IntSet of(int e0, int e1) {
        IntArraySet innerSet = new IntArraySet(2);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        } else {
            return IntSets.unmodifiable(innerSet);
        }
    }

    static IntSet of(int e0, int e1, int e2) {
        IntArraySet innerSet = new IntArraySet(3);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        } else if (!innerSet.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        } else {
            return IntSets.unmodifiable(innerSet);
        }
    }

    static IntSet of(int... a) {
        switch (a.length) {
            case 0:
                return of();
            case 1:
                return of(a[0]);
            case 2:
                return of(a[0], a[1]);
            case 3:
                return of(a[0], a[1], a[2]);
            default:
                IntSet innerSet = (IntSet)(a.length <= 4 ? new IntArraySet(a.length) : new IntOpenHashSet(a.length));

                for(int element : a) {
                    if (!innerSet.add(element)) {
                        throw new IllegalArgumentException("Duplicate element: " + element);
                    }
                }

                return IntSets.unmodifiable(innerSet);
        }
    }
}
