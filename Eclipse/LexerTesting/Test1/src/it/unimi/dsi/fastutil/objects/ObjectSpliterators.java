package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;

public final class ObjectSpliterators {
    public static final EmptySpliterator EMPTY_SPLITERATOR = new EmptySpliterator();

    public static <K> ObjectSpliterator<K> singleton(K element) {
        return new SingletonSpliterator<K>(element);
    }

    public static <K> ObjectSpliterator<K> wrap(K[] array, int offset, int length, int additionalCharacteristics) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator<K>(array, offset, length, additionalCharacteristics);
    }

    public static <K> ObjectSpliterator<K> wrapPreSorted(K[] array, int offset, int length, int additionalCharacteristics, Comparator<? super K> comparator) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliteratorWithComparator<K>(array, offset, length, additionalCharacteristics, comparator);
    }

    public static <K> ObjectSpliterator<K> asSpliterator(ObjectIterator<? extends K> iter, long size, int additionalCharacterisitcs) {
        return new SpliteratorFromIterator<K>(iter, size, additionalCharacterisitcs);
    }

    public static <K> ObjectSpliterator<K> asSpliteratorFromSorted(ObjectIterator<? extends K> iter, long size, int additionalCharacterisitcs, Comparator<? super K> comparator) {
        return new SpliteratorFromIteratorWithComparator<K>(iter, size, additionalCharacterisitcs, comparator);
    }

    public static <K> ObjectSpliterator<K> asSpliteratorUnknownSize(ObjectIterator<? extends K> iter, int characterisitcs) {
        return new SpliteratorFromIterator<K>(iter, characterisitcs);
    }

    public static class EmptySpliterator<K> implements ObjectSpliterator<K>, Serializable, Cloneable {
        private static final long serialVersionUID = 8379247926738230492L;

        protected EmptySpliterator() {
        }

        public boolean tryAdvance(Consumer<? super K> action) {
            return false;
        }

        public ObjectSpliterator<K> trySplit() {
            return null;
        }

        public long estimateSize() {
            return 0L;
        }

        public int characteristics() {
            return 16448;
        }

        public void forEachRemaining(Consumer<? super K> action) {
        }

        public Object clone() {
            return ObjectSpliterators.EMPTY_SPLITERATOR;
        }

        private Object readResolve() {
            return ObjectSpliterators.EMPTY_SPLITERATOR;
        }
    }

    private static class SingletonSpliterator<K> implements ObjectSpliterator<K> {
        private final K element;
        private final Comparator<? super K> comparator;
        private boolean consumed;

        public SingletonSpliterator(K element) {
            this(element, (Comparator)null);
        }

        public SingletonSpliterator(K element, Comparator<? super K> comparator) {
            this.consumed = false;
            this.element = element;
            this.comparator = comparator;
        }

        public boolean tryAdvance(Consumer<? super K> action) {
            Objects.requireNonNull(action);
            if (this.consumed) {
                return false;
            } else {
                this.consumed = true;
                action.accept(this.element);
                return true;
            }
        }

        public ObjectSpliterator<K> trySplit() {
            return null;
        }

        public long estimateSize() {
            return this.consumed ? 0L : 1L;
        }

        public int characteristics() {
            return 17493 | (this.element != null ? 256 : 0);
        }

        public void forEachRemaining(Consumer<? super K> action) {
            Objects.requireNonNull(action);
            if (!this.consumed) {
                this.consumed = true;
                action.accept(this.element);
            }

        }

        public Comparator<? super K> getComparator() {
            return this.comparator;
        }
    }

    private static class ArraySpliterator<K> implements ObjectSpliterator<K> {
        final K[] array;
        private final int offset;
        private int length;
        private int curr;
        final int characteristics;

        public ArraySpliterator(K[] array, int offset, int length, int additionalCharacteristics) {
            this.array = array;
            this.offset = offset;
            this.length = length;
            this.characteristics = 16464 | additionalCharacteristics;
        }

        public boolean tryAdvance(Consumer<? super K> action) {
            if (this.curr >= this.length) {
                return false;
            } else {
                Objects.requireNonNull(action);
                action.accept(this.array[this.offset + this.curr++]);
                return true;
            }
        }

        public long estimateSize() {
            return (long)(this.length - this.curr);
        }

        public int characteristics() {
            return this.characteristics;
        }

        protected ArraySpliterator<K> makeForSplit(int newOffset, int newLength) {
            return new ArraySpliterator<K>(this.array, newOffset, newLength, this.characteristics);
        }

        public ObjectSpliterator<K> trySplit() {
            int retLength = this.length - this.curr >> 1;
            if (retLength <= 1) {
                return null;
            } else {
                int myNewCurr = this.curr + retLength;
                int retOffset = this.offset + this.curr;
                this.curr = myNewCurr;
                return this.makeForSplit(retOffset, retLength);
            }
        }

        public void forEachRemaining(Consumer<? super K> action) {
            Objects.requireNonNull(action);

            for(K[] array = this.array; this.curr < this.length; ++this.curr) {
                action.accept(array[this.offset + this.curr]);
            }

        }
    }

    private static class ArraySpliteratorWithComparator<K> extends ArraySpliterator<K> {
        private final Comparator<? super K> comparator;

        public ArraySpliteratorWithComparator(K[] array, int offset, int length, int additionalCharacteristics, Comparator<? super K> comparator) {
            super(array, offset, length, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        protected ArraySpliteratorWithComparator<K> makeForSplit(int newOffset, int newLength) {
            return new ArraySpliteratorWithComparator<K>(this.array, newOffset, newLength, this.characteristics, this.comparator);
        }

        public Comparator<? super K> getComparator() {
            return this.comparator;
        }
    }

    public abstract static class AbstractIndexBasedSpliterator<K> extends AbstractObjectSpliterator<K> {
        protected int pos;

        protected AbstractIndexBasedSpliterator(int initialPos) {
            this.pos = initialPos;
        }

        protected abstract K get(int var1);

        protected abstract int getMaxPos();

        protected abstract ObjectSpliterator<K> makeForSplit(int var1, int var2);

        protected int computeSplitPoint() {
            return this.pos + (this.getMaxPos() - this.pos) / 2;
        }

        private void splitPointCheck(int splitPoint, int observedMax) {
            if (splitPoint < this.pos || splitPoint > observedMax) {
                throw new IndexOutOfBoundsException("splitPoint " + splitPoint + " outside of range of current position " + this.pos + " and range end " + observedMax);
            }
        }

        public int characteristics() {
            return 16464;
        }

        public long estimateSize() {
            return (long)this.getMaxPos() - (long)this.pos;
        }

        public boolean tryAdvance(Consumer<? super K> action) {
            if (this.pos >= this.getMaxPos()) {
                return false;
            } else {
                action.accept(this.get(this.pos++));
                return true;
            }
        }

        public void forEachRemaining(Consumer<? super K> action) {
            for(int max = this.getMaxPos(); this.pos < max; ++this.pos) {
                action.accept(this.get(this.pos));
            }

        }

        public ObjectSpliterator<K> trySplit() {
            int max = this.getMaxPos();
            int splitPoint = this.computeSplitPoint();
            if (splitPoint != this.pos && splitPoint != max) {
                this.splitPointCheck(splitPoint, max);
                int oldPos = this.pos;
                ObjectSpliterator<K> maybeSplit = this.makeForSplit(oldPos, splitPoint);
                if (maybeSplit != null) {
                    this.pos = splitPoint;
                }

                return maybeSplit;
            } else {
                return null;
            }
        }
    }

    public abstract static class EarlyBindingSizeIndexBasedSpliterator<K> extends AbstractIndexBasedSpliterator<K> {
        protected final int maxPos;

        protected EarlyBindingSizeIndexBasedSpliterator(int initialPos, int maxPos) {
            super(initialPos);
            this.maxPos = maxPos;
        }

        protected final int getMaxPos() {
            return this.maxPos;
        }
    }

    public abstract static class LateBindingSizeIndexBasedSpliterator<K> extends AbstractIndexBasedSpliterator<K> {
        protected int maxPos = -1;
        private boolean maxPosFixed;

        protected LateBindingSizeIndexBasedSpliterator(int initialPos) {
            super(initialPos);
            this.maxPosFixed = false;
        }

        protected LateBindingSizeIndexBasedSpliterator(int initialPos, int fixedMaxPos) {
            super(initialPos);
            this.maxPos = fixedMaxPos;
            this.maxPosFixed = true;
        }

        protected abstract int getMaxPosFromBackingStore();

        protected final int getMaxPos() {
            return this.maxPosFixed ? this.maxPos : this.getMaxPosFromBackingStore();
        }

        public ObjectSpliterator<K> trySplit() {
            ObjectSpliterator<K> maybeSplit = super.trySplit();
            if (!this.maxPosFixed && maybeSplit != null) {
                this.maxPos = this.getMaxPosFromBackingStore();
                this.maxPosFixed = true;
            }

            return maybeSplit;
        }
    }

    private static class SpliteratorFromIterator<K> implements ObjectSpliterator<K> {
        private final ObjectIterator<? extends K> iter;
        final int characteristics;
        private final boolean knownSize;
        private long size = Long.MAX_VALUE;
        private int nextBatchSize = 1024;
        private ObjectSpliterator<K> delegate = null;

        SpliteratorFromIterator(ObjectIterator<? extends K> iter, int characteristics) {
            this.iter = iter;
            this.characteristics = 0 | characteristics;
            this.knownSize = false;
        }

        SpliteratorFromIterator(ObjectIterator<? extends K> iter, long size, int additionalCharacteristics) {
            this.iter = iter;
            this.knownSize = true;
            this.size = size;
            if ((additionalCharacteristics & 4096) != 0) {
                this.characteristics = 0 | additionalCharacteristics;
            } else {
                this.characteristics = 16448 | additionalCharacteristics;
            }

        }

        public boolean tryAdvance(Consumer<? super K> action) {
            if (this.delegate != null) {
                boolean hadRemaining = this.delegate.tryAdvance(action);
                if (!hadRemaining) {
                    this.delegate = null;
                }

                return hadRemaining;
            } else if (!this.iter.hasNext()) {
                return false;
            } else {
                --this.size;
                action.accept(this.iter.next());
                return true;
            }
        }

        public void forEachRemaining(Consumer<? super K> action) {
            if (this.delegate != null) {
                this.delegate.forEachRemaining(action);
                this.delegate = null;
            }

            this.iter.forEachRemaining(action);
            this.size = 0L;
        }

        public long estimateSize() {
            if (this.delegate != null) {
                return this.delegate.estimateSize();
            } else if (!this.iter.hasNext()) {
                return 0L;
            } else {
                return this.knownSize && this.size >= 0L ? this.size : Long.MAX_VALUE;
            }
        }

        public int characteristics() {
            return this.characteristics;
        }

        protected ObjectSpliterator<K> makeForSplit(K[] batch, int len) {
            return ObjectSpliterators.<K>wrap(batch, 0, len, this.characteristics);
        }

        public ObjectSpliterator<K> trySplit() {
            if (!this.iter.hasNext()) {
                return null;
            } else {
                int batchSizeEst = this.knownSize && this.size > 0L ? (int)Math.min((long)this.nextBatchSize, this.size) : this.nextBatchSize;
                K[] batch = (K[])(new Object[batchSizeEst]);

                int actualSeen;
                for(actualSeen = 0; actualSeen < batchSizeEst && this.iter.hasNext(); --this.size) {
                    batch[actualSeen++] = this.iter.next();
                }

                if (batchSizeEst < this.nextBatchSize && this.iter.hasNext()) {
                    for(batch = (K[])Arrays.copyOf(batch, this.nextBatchSize); this.iter.hasNext() && actualSeen < this.nextBatchSize; --this.size) {
                        batch[actualSeen++] = this.iter.next();
                    }
                }

                this.nextBatchSize = Math.min(33554432, this.nextBatchSize + 1024);
                ObjectSpliterator<K> split = this.makeForSplit(batch, actualSeen);
                if (!this.iter.hasNext()) {
                    this.delegate = split;
                    return split.trySplit();
                } else {
                    return split;
                }
            }
        }
    }

    private static class SpliteratorFromIteratorWithComparator<K> extends SpliteratorFromIterator<K> {
        private final Comparator<? super K> comparator;

        SpliteratorFromIteratorWithComparator(ObjectIterator<? extends K> iter, long size, int additionalCharacteristics, Comparator<? super K> comparator) {
            super(iter, size, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        public Comparator<? super K> getComparator() {
            return this.comparator;
        }

        protected ObjectSpliterator<K> makeForSplit(K[] array, int len) {
            return ObjectSpliterators.<K>wrapPreSorted(array, 0, len, this.characteristics, this.comparator);
        }
    }
}
