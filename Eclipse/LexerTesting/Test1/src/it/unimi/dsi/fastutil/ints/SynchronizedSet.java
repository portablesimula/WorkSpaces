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


    public class SynchronizedSet extends IntCollections.SynchronizedCollection implements IntSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected SynchronizedSet(IntSet s, Object sync) {
            super(s, sync);
        }

        protected SynchronizedSet(IntSet s) {
            super(s);
        }

        public boolean remove(int k) {
            synchronized(this.sync) {
                return this.collection.rem(k);
            }
        }

        /** @deprecated */
        @Deprecated
        public boolean rem(int k) {
            return super.rem(k);
        }
    }
}
