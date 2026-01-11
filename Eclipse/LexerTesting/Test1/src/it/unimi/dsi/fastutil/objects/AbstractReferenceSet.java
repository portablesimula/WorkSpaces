//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.objects;

import java.util.Set;

public abstract class AbstractReferenceSet<K> extends AbstractReferenceCollection<K> implements ReferenceSet<K>, Cloneable {
    protected AbstractReferenceSet() {
    }

    public abstract ObjectIterator<K> iterator();

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Set)) {
            return false;
        } else {
            Set<?> s = (Set)o;
            return s.size() != this.size() ? false : this.containsAll(s);
        }
    }

    public int hashCode() {
        int h = 0;
        int n = this.size();

        K k;
        for(ObjectIterator<K> i = this.iterator(); n-- != 0; h += System.identityHashCode(k)) {
            k = (K)i.next();
        }

        return h;
    }
}
