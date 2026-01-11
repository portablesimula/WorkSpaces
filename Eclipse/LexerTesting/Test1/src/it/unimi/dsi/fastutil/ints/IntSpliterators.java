//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class IntSpliterators {
    public static final EmptySpliterator EMPTY_SPLITERATOR = new EmptySpliterator();

    public static IntSpliterator singleton(int element) {
        return new SingletonSpliterator(element);
    }

    public static IntSpliterator singleton(int element, IntComparator comparator) {
        return new SingletonSpliterator(element, comparator);
    }

    public static IntSpliterator wrap(int[] array, int offset, int length, int additionalCharacteristics) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator(array, offset, length, additionalCharacteristics);
    }

    public static IntSpliterator wrapPreSorted(int[] array, int offset, int length, int additionalCharacteristics, IntComparator comparator) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliteratorWithComparator(array, offset, length, additionalCharacteristics, comparator);
    }

    public static IntSpliterator asSpliterator(IntIterator iter, long size, int additionalCharacterisitcs) {
        return new SpliteratorFromIterator(iter, size, additionalCharacterisitcs);
    }

    public static IntSpliterator asSpliteratorFromSorted(IntIterator iter, long size, int additionalCharacterisitcs, IntComparator comparator) {
        return new SpliteratorFromIteratorWithComparator(iter, size, additionalCharacterisitcs, comparator);
    }

    public static IntSpliterator asSpliteratorUnknownSize(IntIterator iter, int characterisitcs) {
        return new SpliteratorFromIterator(iter, characterisitcs);
    }

    public static class EmptySpliterator implements IntSpliterator, Serializable, Cloneable {
        private static final long serialVersionUID = 8379247926738230492L;

        protected EmptySpliterator() {
        }

        public boolean tryAdvance(IntConsumer action) {
            return false;
        }

        /** @deprecated */
        @Deprecated
        public boolean tryAdvance(Consumer<? super Integer> action) {
            return false;
        }

        public IntSpliterator trySplit() {
            return null;
        }

        public long estimateSize() {
            return 0L;
        }

        public int characteristics() {
            return 16448;
        }

        public void forEachRemaining(IntConsumer action) {
        }

        /** @deprecated */
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
        }

        public Object clone() {
            return IntSpliterators.EMPTY_SPLITERATOR;
        }

        private Object readResolve() {
            return IntSpliterators.EMPTY_SPLITERATOR;
        }
    }

    private static class SingletonSpliterator implements IntSpliterator {
        private final int element;
        private final IntComparator comparator;
        private boolean consumed;

        public SingletonSpliterator(int element) {
            this(element, (IntComparator)null);
        }

        public SingletonSpliterator(int element, IntComparator comparator) {
            this.consumed = false;
            this.element = element;
            this.comparator = comparator;
        }

        public boolean tryAdvance(IntConsumer action) {
            Objects.requireNonNull(action);
            if (this.consumed) {
                return false;
            } else {
                this.consumed = true;
                action.accept(this.element);
                return true;
            }
        }

        public IntSpliterator trySplit() {
            return null;
        }

        public long estimateSize() {
            return this.consumed ? 0L : 1L;
        }

        public int characteristics() {
            return 17749;
        }

        public void forEachRemaining(IntConsumer action) {
            Objects.requireNonNull(action);
            if (!this.consumed) {
                this.consumed = true;
                action.accept(this.element);
            }

        }

        public IntComparator getComparator() {
            return this.comparator;
        }
    }

    private static class ArraySpliterator implements IntSpliterator {
        final int[] array;
        private final int offset;
        private int length;
        private int curr;
        final int characteristics;

        public ArraySpliterator(int[] array, int offset, int length, int additionalCharacteristics) {
            this.array = array;
            this.offset = offset;
            this.length = length;
            this.characteristics = 16720 | additionalCharacteristics;
        }

        public boolean tryAdvance(IntConsumer action) {
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

        protected ArraySpliterator makeForSplit(int newOffset, int newLength) {
            return new ArraySpliterator(this.array, newOffset, newLength, this.characteristics);
        }

        public IntSpliterator trySplit() {
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

        public void forEachRemaining(IntConsumer action) {
            Objects.requireNonNull(action);

            for(int[] array = this.array; this.curr < this.length; ++this.curr) {
                action.accept(array[this.offset + this.curr]);
            }

        }
    }

    private static class ArraySpliteratorWithComparator extends ArraySpliterator {
        private final IntComparator comparator;

        public ArraySpliteratorWithComparator(int[] array, int offset, int length, int additionalCharacteristics, IntComparator comparator) {
            super(array, offset, length, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        protected ArraySpliteratorWithComparator makeForSplit(int newOffset, int newLength) {
            return new ArraySpliteratorWithComparator(this.array, newOffset, newLength, this.characteristics, this.comparator);
        }

        public IntComparator getComparator() {
            return this.comparator;
        }
    }

    public abstract static class AbstractIndexBasedSpliterator extends AbstractIntSpliterator {
        protected int pos;

        protected AbstractIndexBasedSpliterator(int initialPos) {
            this.pos = initialPos;
        }

        protected abstract int get(int var1);

        protected abstract int getMaxPos();

        protected abstract IntSpliterator makeForSplit(int var1, int var2);

        protected int computeSplitPoint() {
            return this.pos + (this.getMaxPos() - this.pos) / 2;
        }

        private void splitPointCheck(int splitPoint, int observedMax) {
            if (splitPoint < this.pos || splitPoint > observedMax) {
                throw new IndexOutOfBoundsException("splitPoint " + splitPoint + " outside of range of current position " + this.pos + " and range end " + observedMax);
            }
        }

        public int characteristics() {
            return 16720;
        }

        public long estimateSize() {
            return (long)this.getMaxPos() - (long)this.pos;
        }

        public boolean tryAdvance(IntConsumer action) {
            if (this.pos >= this.getMaxPos()) {
                return false;
            } else {
                action.accept(this.get(this.pos++));
                return true;
            }
        }

        public void forEachRemaining(IntConsumer action) {
            for(int max = this.getMaxPos(); this.pos < max; ++this.pos) {
                action.accept(this.get(this.pos));
            }

        }

        public IntSpliterator trySplit() {
            int max = this.getMaxPos();
            int splitPoint = this.computeSplitPoint();
            if (splitPoint != this.pos && splitPoint != max) {
                this.splitPointCheck(splitPoint, max);
                int oldPos = this.pos;
                IntSpliterator maybeSplit = this.makeForSplit(oldPos, splitPoint);
                if (maybeSplit != null) {
                    this.pos = splitPoint;
                }

                return maybeSplit;
            } else {
                return null;
            }
        }
    }

    public abstract static class EarlyBindingSizeIndexBasedSpliterator extends AbstractIndexBasedSpliterator {
        protected final int maxPos;

        protected EarlyBindingSizeIndexBasedSpliterator(int initialPos, int maxPos) {
            super(initialPos);
            this.maxPos = maxPos;
        }

        protected final int getMaxPos() {
            return this.maxPos;
        }
    }

    public abstract static class LateBindingSizeIndexBasedSpliterator extends AbstractIndexBasedSpliterator {
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

        public IntSpliterator trySplit() {
            IntSpliterator maybeSplit = super.trySplit();
            if (!this.maxPosFixed && maybeSplit != null) {
                this.maxPos = this.getMaxPosFromBackingStore();
                this.maxPosFixed = true;
            }

            return maybeSplit;
        }
    }

    private static class SpliteratorFromIterator implements IntSpliterator {
        private final IntIterator iter;
        final int characteristics;
        private final boolean knownSize;
        private long size = Long.MAX_VALUE;
        private int nextBatchSize = 1024;
        private IntSpliterator delegate = null;

        SpliteratorFromIterator(IntIterator iter, int characteristics) {
            this.iter = iter;
            this.characteristics = 256 | characteristics;
            this.knownSize = false;
        }

        SpliteratorFromIterator(IntIterator iter, long size, int additionalCharacteristics) {
            this.iter = iter;
            this.knownSize = true;
            this.size = size;
            if ((additionalCharacteristics & 4096) != 0) {
                this.characteristics = 256 | additionalCharacteristics;
            } else {
                this.characteristics = 16704 | additionalCharacteristics;
            }

        }

        public boolean tryAdvance(IntConsumer action) {
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
                action.accept(this.iter.nextInt());
                return true;
            }
        }

        public void forEachRemaining(IntConsumer action) {
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

        protected IntSpliterator makeForSplit(int[] batch, int len) {
            return IntSpliterators.wrap(batch, 0, len, this.characteristics);
        }

        public IntSpliterator trySplit() {
            if (!this.iter.hasNext()) {
                return null;
            } else {
                int batchSizeEst = this.knownSize && this.size > 0L ? (int)Math.min((long)this.nextBatchSize, this.size) : this.nextBatchSize;
                int[] batch = new int[batchSizeEst];

                int actualSeen;
                for(actualSeen = 0; actualSeen < batchSizeEst && this.iter.hasNext(); --this.size) {
                    batch[actualSeen++] = this.iter.nextInt();
                }

                if (batchSizeEst < this.nextBatchSize && this.iter.hasNext()) {
                    for(batch = Arrays.copyOf(batch, this.nextBatchSize); this.iter.hasNext() && actualSeen < this.nextBatchSize; --this.size) {
                        batch[actualSeen++] = this.iter.nextInt();
                    }
                }

                this.nextBatchSize = Math.min(33554432, this.nextBatchSize + 1024);
                IntSpliterator split = this.makeForSplit(batch, actualSeen);
                if (!this.iter.hasNext()) {
                    this.delegate = split;
                    return split.trySplit();
                } else {
                    return split;
                }
            }
        }
    }

    private static class SpliteratorFromIteratorWithComparator extends SpliteratorFromIterator {
        private final IntComparator comparator;

        SpliteratorFromIteratorWithComparator(IntIterator iter, long size, int additionalCharacteristics, IntComparator comparator) {
            super(iter, size, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        public IntComparator getComparator() {
            return this.comparator;
        }

        protected IntSpliterator makeForSplit(int[] array, int len) {
            return IntSpliterators.wrapPreSorted(array, 0, len, this.characteristics, this.comparator);
        }
    }
}
