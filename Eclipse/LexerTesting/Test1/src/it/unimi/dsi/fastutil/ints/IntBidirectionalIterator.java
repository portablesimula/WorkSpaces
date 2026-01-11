//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface IntBidirectionalIterator extends IntIterator, ObjectBidirectionalIterator<Integer> {
    int previousInt();

    /** @deprecated */
    @Deprecated
    default Integer previous() {
        return this.previousInt();
    }
}
