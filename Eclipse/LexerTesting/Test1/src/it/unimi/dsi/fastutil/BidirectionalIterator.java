//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil;

import java.util.Iterator;

public interface BidirectionalIterator<K> extends Iterator<K> {
    K previous();

    boolean hasPrevious();
}
