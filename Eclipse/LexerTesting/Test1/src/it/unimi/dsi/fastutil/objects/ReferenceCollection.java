//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Size64;
import java.util.Collection;

public interface ReferenceCollection<K> extends ObjectIterable<K>, Collection<K> {
    ObjectIterator<K> iterator();

    default ObjectSpliterator<K> spliterator() {
        return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 64);
    }
}
