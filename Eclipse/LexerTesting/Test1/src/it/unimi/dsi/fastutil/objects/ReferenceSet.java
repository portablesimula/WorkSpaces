//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Size64;
import java.util.Set;

public interface ReferenceSet<K> extends ReferenceCollection<K>, Set<K> {
    ObjectIterator<K> iterator();

    default ObjectSpliterator<K> spliterator() {
        return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 65);
    }
}
