//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class EmptySet extends IntCollections.EmptyCollection implements IntSet, Serializable, Cloneable {
    private static final long serialVersionUID = -7046029254386353129L;

    protected EmptySet() {
    }

    public boolean remove(int ok) {
        throw new UnsupportedOperationException();
    }

    public Object clone() {
        return IntSets.EMPTY_SET;
    }

    public boolean equals(Object o) {
        return o instanceof Set && ((Set)o).isEmpty();
    }

    /** @deprecated */
    @Deprecated
    public boolean rem(int k) {
        return super.rem(k);
    }

    private Object readResolve() {
        return IntSets.EMPTY_SET;
    }
}

