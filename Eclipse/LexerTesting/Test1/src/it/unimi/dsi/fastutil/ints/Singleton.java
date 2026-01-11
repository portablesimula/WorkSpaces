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


    public class Singleton extends AbstractIntSet implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int element;

        protected Singleton(int element) {
            this.element = element;
        }

        public boolean contains(int k) {
            return k == this.element;
        }

        public boolean remove(int k) {
            throw new UnsupportedOperationException();
        }

        public IntListIterator iterator() {
            return IntIterators.singleton(this.element);
        }

        public IntSpliterator spliterator() {
            return IntSpliterators.singleton(this.element);
        }

        public int size() {
            return 1;
        }

        public int[] toIntArray() {
            return new int[]{this.element};
        }

        /** @deprecated */
        @Deprecated
        public void forEach(Consumer<? super Integer> action) {
            action.accept(this.element);
        }

        public boolean addAll(Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        /** @deprecated */
        @Deprecated
        public boolean removeIf(Predicate<? super Integer> filter) {
            throw new UnsupportedOperationException();
        }

        public void forEach(IntConsumer action) {
            action.accept(this.element);
        }

        public boolean addAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(IntCollection c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeIf(IntPredicate filter) {
            throw new UnsupportedOperationException();
        }

        /** @deprecated */
        @Deprecated
        public Object[] toArray() {
            return new Object[]{this.element};
        }

        public Object clone() {
            return this;
        }
    }

}
