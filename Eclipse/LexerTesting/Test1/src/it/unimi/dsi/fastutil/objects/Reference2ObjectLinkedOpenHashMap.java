package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Reference2ObjectLinkedOpenHashMap<K, V> extends AbstractReference2ObjectSortedMap<K, V> implements Hash, Serializable, Cloneable {
    private static final long serialVersionUID = 0L;
    protected transient K[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Reference2ObjectSortedMap.FastSortedEntrySet<K, V> entries;
    protected transient ReferenceSortedSet<K> keys;
    protected transient ObjectCollection<V> values;

    public Reference2ObjectLinkedOpenHashMap(int expected, float f) {
        this.first = -1;
        this.last = -1;
        if (!(f <= 0.0F) && !(f >= 1.0F)) {
            if (expected < 0) {
                throw new IllegalArgumentException("The expected number of elements must be nonnegative");
            } else {
                this.f = f;
                this.minN = this.n = HashCommon.arraySize(expected, f);
                this.mask = this.n - 1;
                this.maxFill = HashCommon.maxFill(this.n, f);
                this.key = (K[])(new Object[this.n + 1]);
                this.value = (V[])(new Object[this.n + 1]);
                this.link = new long[this.n + 1];
            }
        } else {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
        }
    }

    public Reference2ObjectLinkedOpenHashMap(int expected) {
        this(expected, 0.75F);
    }

    public Reference2ObjectLinkedOpenHashMap() {
        this(16, 0.75F);
    }

    public Reference2ObjectLinkedOpenHashMap(Map<? extends K, ? extends V> m, float f) {
        this(m.size(), f);
        this.putAll(m);
    }

    public Reference2ObjectLinkedOpenHashMap(Map<? extends K, ? extends V> m) {
        this(m, 0.75F);
    }

    public Reference2ObjectLinkedOpenHashMap(Reference2ObjectMap<K, V> m, float f) {
        this(m.size(), f);
        this.putAll(m);
    }

    public Reference2ObjectLinkedOpenHashMap(Reference2ObjectMap<K, V> m) {
        this(m, 0.75F);
    }

    public Reference2ObjectLinkedOpenHashMap(K[] k, V[] v, float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        } else {
            for(int i = 0; i < k.length; ++i) {
                this.put(k[i], v[i]);
            }

        }
    }

    public Reference2ObjectLinkedOpenHashMap(K[] k, V[] v) {
        this(k, v, 0.75F);
    }

    private int realSize() {
        return this.containsNullKey ? this.size - 1 : this.size;
    }

    public void ensureCapacity(int capacity) {
        int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }

    }

    private void tryCapacity(long capacity) {
        int needed = (int)Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((double)((float)capacity / this.f)))));
        if (needed > this.n) {
            this.rehash(needed);
        }

    }

    private V removeEntry(int pos) {
        V oldValue = (V)this.value[pos];
        this.value[pos] = null;
        --this.size;
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }

        return oldValue;
    }

    private V removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        V oldValue = (V)this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }

        return oldValue;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        if ((double)this.f <= (double)0.5F) {
            this.ensureCapacity(m.size());
        } else {
            this.tryCapacity((long)(this.size() + m.size()));
        }

        super.putAll(m);
    }

    private int find(K k) {
        if (k == null) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        } else {
            K[] key = this.key;
            K curr;
            int pos;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null) {
                return -(pos + 1);
            } else if (k == curr) {
                return pos;
            } else {
                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (k == curr) {
                        return pos;
                    }
                }

                return -(pos + 1);
            }
        }
    }

    private void insert(int pos, K k, V v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }

        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            long[] var10000 = this.link;
            int var10001 = this.last;
            var10000[var10001] ^= (this.link[this.last] ^ (long)pos & 4294967295L) & 4294967295L;
            this.link[pos] = ((long)this.last & 4294967295L) << 32 | 4294967295L;
            this.last = pos;
        }

        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }

    }

    public V put(K k, V v) {
        int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return (V)this.defRetValue;
        } else {
            V oldValue = (V)this.value[pos];
            this.value[pos] = v;
            return oldValue;
        }
    }

    protected final void shiftKeys(int pos) {
        K[] key = this.key;
        V[] value = this.value;

        while(true) {
            int last = pos;
            pos = pos + 1 & this.mask;

            K curr;
            while(true) {
                if ((curr = (K)key[pos]) == null) {
                    key[last] = null;
                    value[last] = null;
                    return;
                }

                int slot = HashCommon.mix(System.identityHashCode(curr)) & this.mask;
                if (last <= pos) {
                    if (last >= slot || slot > pos) {
                        break;
                    }
                } else if (last >= slot && slot > pos) {
                    break;
                }

                pos = pos + 1 & this.mask;
            }

            key[last] = curr;
            value[last] = value[pos];
            this.fixPointers(pos, last);
        }
    }

    public V remove(Object k) {
        if (k == null) {
            return (V)(this.containsNullKey ? this.removeNullEntry() : this.defRetValue);
        } else {
            K[] key = this.key;
            K curr;
            int pos;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null) {
                return (V)this.defRetValue;
            } else if (k == curr) {
                return (V)this.removeEntry(pos);
            } else {
                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (k == curr) {
                        return (V)this.removeEntry(pos);
                    }
                }

                return (V)this.defRetValue;
            }
        }
    }

    private V setValue(int pos, V v) {
        V oldValue = (V)this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    public V removeFirst() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        } else {
            int pos = this.first;
            if (this.size == 1) {
                this.first = this.last = -1;
            } else {
                this.first = (int)this.link[pos];
                if (0 <= this.first) {
                    long[] var10000 = this.link;
                    int var10001 = this.first;
                    var10000[var10001] |= -4294967296L;
                }
            }

            --this.size;
            V v = (V)this.value[pos];
            if (pos == this.n) {
                this.containsNullKey = false;
                this.key[this.n] = null;
                this.value[this.n] = null;
            } else {
                this.shiftKeys(pos);
            }

            if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
                this.rehash(this.n / 2);
            }

            return v;
        }
    }

    public V removeLast() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        } else {
            int pos = this.last;
            if (this.size == 1) {
                this.first = this.last = -1;
            } else {
                this.last = (int)(this.link[pos] >>> 32);
                if (0 <= this.last) {
                    long[] var10000 = this.link;
                    int var10001 = this.last;
                    var10000[var10001] |= 4294967295L;
                }
            }

            --this.size;
            V v = (V)this.value[pos];
            if (pos == this.n) {
                this.containsNullKey = false;
                this.key[this.n] = null;
                this.value[this.n] = null;
            } else {
                this.shiftKeys(pos);
            }

            if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
                this.rehash(this.n / 2);
            }

            return v;
        }
    }

    private void moveIndexToFirst(int i) {
        if (this.size != 1 && this.first != i) {
            if (this.last == i) {
                this.last = (int)(this.link[i] >>> 32);
                long[] var10000 = this.link;
                int var10001 = this.last;
                var10000[var10001] |= 4294967295L;
            } else {
                long linki = this.link[i];
                int prev = (int)(linki >>> 32);
                int next = (int)linki;
                long[] var6 = this.link;
                var6[prev] ^= (this.link[prev] ^ linki & 4294967295L) & 4294967295L;
                var6 = this.link;
                var6[next] ^= (this.link[next] ^ linki & -4294967296L) & -4294967296L;
            }

            long[] var8 = this.link;
            int var9 = this.first;
            var8[var9] ^= (this.link[this.first] ^ ((long)i & 4294967295L) << 32) & -4294967296L;
            this.link[i] = -4294967296L | (long)this.first & 4294967295L;
            this.first = i;
        }
    }

    private void moveIndexToLast(int i) {
        if (this.size != 1 && this.last != i) {
            if (this.first == i) {
                this.first = (int)this.link[i];
                long[] var10000 = this.link;
                int var10001 = this.first;
                var10000[var10001] |= -4294967296L;
            } else {
                long linki = this.link[i];
                int prev = (int)(linki >>> 32);
                int next = (int)linki;
                long[] var6 = this.link;
                var6[prev] ^= (this.link[prev] ^ linki & 4294967295L) & 4294967295L;
                var6 = this.link;
                var6[next] ^= (this.link[next] ^ linki & -4294967296L) & -4294967296L;
            }

            long[] var8 = this.link;
            int var9 = this.last;
            var8[var9] ^= (this.link[this.last] ^ (long)i & 4294967295L) & 4294967295L;
            this.link[i] = ((long)this.last & 4294967295L) << 32 | 4294967295L;
            this.last = i;
        }
    }

    public V getAndMoveToFirst(K k) {
        if (k == null) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return (V)this.value[this.n];
            } else {
                return (V)this.defRetValue;
            }
        } else {
            K[] key = this.key;
            K curr;
            int pos;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null) {
                return (V)this.defRetValue;
            } else if (k == curr) {
                this.moveIndexToFirst(pos);
                return (V)this.value[pos];
            } else {
                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (k == curr) {
                        this.moveIndexToFirst(pos);
                        return (V)this.value[pos];
                    }
                }

                return (V)this.defRetValue;
            }
        }
    }

    public V getAndMoveToLast(K k) {
        if (k == null) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return (V)this.value[this.n];
            } else {
                return (V)this.defRetValue;
            }
        } else {
            K[] key = this.key;
            K curr;
            int pos;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null) {
                return (V)this.defRetValue;
            } else if (k == curr) {
                this.moveIndexToLast(pos);
                return (V)this.value[pos];
            } else {
                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (k == curr) {
                        this.moveIndexToLast(pos);
                        return (V)this.value[pos];
                    }
                }

                return (V)this.defRetValue;
            }
        }
    }

    public V putAndMoveToFirst(K k, V v) {
        int pos;
        if (k == null) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return (V)this.setValue(this.n, v);
            }

            this.containsNullKey = true;
            pos = this.n;
        } else {
            K[] key = this.key;
            K curr;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) != null) {
                if (curr == k) {
                    this.moveIndexToFirst(pos);
                    return (V)this.setValue(pos, v);
                }

                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (curr == k) {
                        this.moveIndexToFirst(pos);
                        return (V)this.setValue(pos, v);
                    }
                }
            }
        }

        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            long[] var10000 = this.link;
            int var10001 = this.first;
            var10000[var10001] ^= (this.link[this.first] ^ ((long)pos & 4294967295L) << 32) & -4294967296L;
            this.link[pos] = -4294967296L | (long)this.first & 4294967295L;
            this.first = pos;
        }

        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }

        return (V)this.defRetValue;
    }

    public V putAndMoveToLast(K k, V v) {
        int pos;
        if (k == null) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return (V)this.setValue(this.n, v);
            }

            this.containsNullKey = true;
            pos = this.n;
        } else {
            K[] key = this.key;
            K curr;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) != null) {
                if (curr == k) {
                    this.moveIndexToLast(pos);
                    return (V)this.setValue(pos, v);
                }

                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (curr == k) {
                        this.moveIndexToLast(pos);
                        return (V)this.setValue(pos, v);
                    }
                }
            }
        }

        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            long[] var10000 = this.link;
            int var10001 = this.last;
            var10000[var10001] ^= (this.link[this.last] ^ (long)pos & 4294967295L) & 4294967295L;
            this.link[pos] = ((long)this.last & 4294967295L) << 32 | 4294967295L;
            this.last = pos;
        }

        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }

        return (V)this.defRetValue;
    }

    public V get(Object k) {
        if (k == null) {
            return (V)(this.containsNullKey ? this.value[this.n] : this.defRetValue);
        } else {
            K[] key = this.key;
            K curr;
            int pos;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null) {
                return (V)this.defRetValue;
            } else if (k == curr) {
                return (V)this.value[pos];
            } else {
                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (k == curr) {
                        return (V)this.value[pos];
                    }
                }

                return (V)this.defRetValue;
            }
        }
    }

    public boolean containsKey(Object k) {
        if (k == null) {
            return this.containsNullKey;
        } else {
            K[] key = this.key;
            K curr;
            int pos;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null) {
                return false;
            } else if (k == curr) {
                return true;
            } else {
                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (k == curr) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public boolean containsValue(Object v) {
        K[] key = this.key;
        V[] value = this.value;
        if (this.containsNullKey && Objects.equals(value[this.n], v)) {
            return true;
        } else {
            int i = this.n;

            while(i-- != 0) {
                if (key[i] != null && Objects.equals(value[i], v)) {
                    return true;
                }
            }

            return false;
        }
    }

    public V getOrDefault(Object k, V defaultValue) {
        if (k == null) {
            return (V)(this.containsNullKey ? this.value[this.n] : defaultValue);
        } else {
            K[] key = this.key;
            K curr;
            int pos;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null) {
                return defaultValue;
            } else if (k == curr) {
                return (V)this.value[pos];
            } else {
                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (k == curr) {
                        return (V)this.value[pos];
                    }
                }

                return defaultValue;
            }
        }
    }

    public V putIfAbsent(K k, V v) {
        int pos = this.find(k);
        if (pos >= 0) {
            return (V)this.value[pos];
        } else {
            this.insert(-pos - 1, k, v);
            return (V)this.defRetValue;
        }
    }

    public boolean remove(Object k, Object v) {
        if (k == null) {
            if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
                this.removeNullEntry();
                return true;
            } else {
                return false;
            }
        } else {
            K[] key = this.key;
            K curr;
            int pos;
            if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null) {
                return false;
            } else if (k == curr && Objects.equals(v, this.value[pos])) {
                this.removeEntry(pos);
                return true;
            } else {
                while((curr = (K)key[pos = pos + 1 & this.mask]) != null) {
                    if (k == curr && Objects.equals(v, this.value[pos])) {
                        this.removeEntry(pos);
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public boolean replace(K k, V oldValue, V v) {
        int pos = this.find(k);
        if (pos >= 0 && Objects.equals(oldValue, this.value[pos])) {
            this.value[pos] = v;
            return true;
        } else {
            return false;
        }
    }

    public V replace(K k, V v) {
        int pos = this.find(k);
        if (pos < 0) {
            return (V)this.defRetValue;
        } else {
            V oldValue = (V)this.value[pos];
            this.value[pos] = v;
            return oldValue;
        }
    }

    public V computeIfAbsent(K key, Reference2ObjectFunction<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(key);
        if (pos >= 0) {
            return (V)this.value[pos];
        } else if (!mappingFunction.containsKey(key)) {
            return (V)this.defRetValue;
        } else {
            V newValue = (V)mappingFunction.get(key);
            this.insert(-pos - 1, key, newValue);
            return newValue;
        }
    }

    public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        if (pos < 0) {
            return (V)this.defRetValue;
        } else if (this.value[pos] == null) {
            return (V)this.defRetValue;
        } else {
            V newValue = (V)remappingFunction.apply(k, this.value[pos]);
            if (newValue == null) {
                if (k == null) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(pos);
                }

                return (V)this.defRetValue;
            } else {
                return (V)(this.value[pos] = newValue);
            }
        }
    }

    public V compute(K k, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        V newValue = (V)remappingFunction.apply(k, pos >= 0 ? this.value[pos] : null);
        if (newValue == null) {
            if (pos >= 0) {
                if (k == null) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(pos);
                }
            }

            return (V)this.defRetValue;
        } else if (pos < 0) {
            this.insert(-pos - 1, k, newValue);
            return newValue;
        } else {
            return (V)(this.value[pos] = newValue);
        }
    }

    public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(v);
        int pos = this.find(k);
        if (pos >= 0 && this.value[pos] != null) {
            V newValue = (V)remappingFunction.apply(this.value[pos], v);
            if (newValue == null) {
                if (k == null) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(pos);
                }

                return (V)this.defRetValue;
            } else {
                return (V)(this.value[pos] = newValue);
            }
        } else {
            if (pos < 0) {
                this.insert(-pos - 1, k, v);
            } else {
                this.value[pos] = v;
            }

            return v;
        }
    }

    public void clear() {
        if (this.size != 0) {
            this.size = 0;
            this.containsNullKey = false;
            Arrays.fill(this.key, (Object)null);
            Arrays.fill(this.value, (Object)null);
            this.first = this.last = -1;
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    protected void fixPointers(int i) {
        if (this.size == 0) {
            this.first = this.last = -1;
        } else if (this.first == i) {
            this.first = (int)this.link[i];
            if (0 <= this.first) {
                long[] var8 = this.link;
                int var9 = this.first;
                var8[var9] |= -4294967296L;
            }

        } else if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            if (0 <= this.last) {
                long[] var7 = this.link;
                int var10001 = this.last;
                var7[var10001] |= 4294967295L;
            }

        } else {
            long linki = this.link[i];
            int prev = (int)(linki >>> 32);
            int next = (int)linki;
            long[] var10000 = this.link;
            var10000[prev] ^= (this.link[prev] ^ linki & 4294967295L) & 4294967295L;
            var10000 = this.link;
            var10000[next] ^= (this.link[next] ^ linki & -4294967296L) & -4294967296L;
        }
    }

    protected void fixPointers(int s, int d) {
        if (this.size == 1) {
            this.first = this.last = d;
            this.link[d] = -1L;
        } else if (this.first == s) {
            this.first = d;
            long[] var9 = this.link;
            int var10 = (int)this.link[s];
            var9[var10] ^= (this.link[(int)this.link[s]] ^ ((long)d & 4294967295L) << 32) & -4294967296L;
            this.link[d] = this.link[s];
        } else if (this.last == s) {
            this.last = d;
            long[] var8 = this.link;
            int var10001 = (int)(this.link[s] >>> 32);
            var8[var10001] ^= (this.link[(int)(this.link[s] >>> 32)] ^ (long)d & 4294967295L) & 4294967295L;
            this.link[d] = this.link[s];
        } else {
            long links = this.link[s];
            int prev = (int)(links >>> 32);
            int next = (int)links;
            long[] var10000 = this.link;
            var10000[prev] ^= (this.link[prev] ^ (long)d & 4294967295L) & 4294967295L;
            var10000 = this.link;
            var10000[next] ^= (this.link[next] ^ ((long)d & 4294967295L) << 32) & -4294967296L;
            this.link[d] = links;
        }
    }

    public K firstKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        } else {
            return (K)this.key[this.first];
        }
    }

    public K lastKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        } else {
            return (K)this.key[this.last];
        }
    }

    public Reference2ObjectSortedMap<K, V> tailMap(K from) {
        throw new UnsupportedOperationException();
    }

    public Reference2ObjectSortedMap<K, V> headMap(K to) {
        throw new UnsupportedOperationException();
    }

    public Reference2ObjectSortedMap<K, V> subMap(K from, K to) {
        throw new UnsupportedOperationException();
    }

    public Comparator<? super K> comparator() {
        return null;
    }

    public Reference2ObjectSortedMap.FastSortedEntrySet<K, V> reference2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }

        return this.entries;
    }

    public ReferenceSortedSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }

        return this.keys;
    }

    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() {
                public ObjectIterator<V> iterator() {
                    return Reference2ObjectLinkedOpenHashMap.this.new ValueIterator();
                }

                public ObjectSpliterator<V> spliterator() {
                    return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(Reference2ObjectLinkedOpenHashMap.this), 80);
                }

                public void forEach(Consumer<? super V> consumer) {
                    long[] link = Reference2ObjectLinkedOpenHashMap.this.link;
                    V[] value = Reference2ObjectLinkedOpenHashMap.this.value;
                    int i = Reference2ObjectLinkedOpenHashMap.this.size;
                    int next = Reference2ObjectLinkedOpenHashMap.this.first;

                    while(i-- != 0) {
                        int curr = next;
                        next = (int)link[next];
                        consumer.accept(value[curr]);
                    }

                }

                public int size() {
                    return Reference2ObjectLinkedOpenHashMap.this.size;
                }

                public boolean contains(Object v) {
                    return Reference2ObjectLinkedOpenHashMap.this.containsValue(v);
                }

                public void clear() {
                    Reference2ObjectLinkedOpenHashMap.this.clear();
                }
            };
        }

        return this.values;
    }

    public boolean trim() {
        return this.trim(this.size);
    }

    public boolean trim(int n) {
        int l = HashCommon.nextPowerOfTwo((int)Math.ceil((double)((float)n / this.f)));
        if (l < this.n && this.size <= HashCommon.maxFill(l, this.f)) {
            try {
                this.rehash(l);
                return true;
            } catch (OutOfMemoryError var4) {
                return false;
            }
        } else {
            return true;
        }
    }

    protected void rehash(int newN) {
        K[] key = this.key;
        V[] value = this.value;
        int mask = newN - 1;
        K[] newKey = (K[])(new Object[newN + 1]);
        V[] newValue = (V[])(new Object[newN + 1]);
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        long[] link = this.link;
        long[] newLink = new long[newN + 1];
        this.first = -1;

        int t;
        for(int j = this.size; j-- != 0; prev = t) {
            int pos;
            if (key[i] == null) {
                pos = newN;
            } else {
                for(pos = HashCommon.mix(System.identityHashCode(key[i])) & mask; newKey[pos] != null; pos = pos + 1 & mask) {
                }
            }

            newKey[pos] = key[i];
            newValue[pos] = value[i];
            if (prev != -1) {
                newLink[newPrev] ^= (newLink[newPrev] ^ (long)pos & 4294967295L) & 4294967295L;
                newLink[pos] ^= (newLink[pos] ^ ((long)newPrev & 4294967295L) << 32) & -4294967296L;
                newPrev = pos;
            } else {
                newPrev = this.first = pos;
                newLink[pos] = -1L;
            }

            t = i;
            i = (int)link[i];
        }

        this.link = newLink;
        this.last = newPrev;
        if (newPrev != -1) {
            newLink[newPrev] |= 4294967295L;
        }

        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }

    public Reference2ObjectLinkedOpenHashMap<K, V> clone() {
        Reference2ObjectLinkedOpenHashMap<K, V> c;
        try {
            c = (Reference2ObjectLinkedOpenHashMap)super.clone();
        } catch (CloneNotSupportedException var3) {
            throw new InternalError();
        }

        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = (K[])((Object[])this.key.clone());
        c.value = (V[])((Object[])this.value.clone());
        c.link = (long[])this.link.clone();
        return c;
    }

    public int hashCode() {
        int h = 0;
        K[] key = this.key;
        V[] value = this.value;
        int j = this.realSize();
        int i = 0;

        for(int t = 0; j-- != 0; ++i) {
            while(key[i] == null) {
                ++i;
            }

            if (this != key[i]) {
                t = System.identityHashCode(key[i]);
            }

            if (this != value[i]) {
                t ^= value[i] == null ? 0 : value[i].hashCode();
            }

            h += t;
        }

        if (this.containsNullKey) {
            h += value[this.n] == null ? 0 : value[this.n].hashCode();
        }

        return h;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        K[] key = this.key;
        V[] value = this.value;
        Reference2ObjectLinkedOpenHashMap<K, V>.EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        int j = this.size;

        while(j-- != 0) {
            int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeObject(value[e]);
        }

    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        K[] key = this.key = (K[])(new Object[this.n + 1]);
        V[] value = this.value = (V[])(new Object[this.n + 1]);
        long[] link = this.link = new long[this.n + 1];
        int prev = -1;
        this.first = this.last = -1;
        int i = this.size;

        while(i-- != 0) {
            K k = (K)s.readObject();
            V v = (V)s.readObject();
            int pos;
            if (k == null) {
                pos = this.n;
                this.containsNullKey = true;
            } else {
                for(pos = HashCommon.mix(System.identityHashCode(k)) & this.mask; key[pos] != null; pos = pos + 1 & this.mask) {
                }
            }

            key[pos] = k;
            value[pos] = v;
            if (this.first != -1) {
                link[prev] ^= (link[prev] ^ (long)pos & 4294967295L) & 4294967295L;
                link[pos] ^= (link[pos] ^ ((long)prev & 4294967295L) << 32) & -4294967296L;
                prev = pos;
            } else {
                prev = this.first = pos;
                link[pos] |= -4294967296L;
            }
        }

        this.last = prev;
        if (prev != -1) {
            link[prev] |= 4294967295L;
        }

    }

    final class MapEntry implements Reference2ObjectMap.Entry<K, V>, ReferenceObjectPair<K, V>, Map.Entry<K, V> {
        int index;

        MapEntry(final int index) {
            this.index = index;
        }

        MapEntry() {
        }

        public K getKey() {
            return (K)Reference2ObjectLinkedOpenHashMap.this.key[this.index];
        }

        public K left() {
            return (K)Reference2ObjectLinkedOpenHashMap.this.key[this.index];
        }

        public V getValue() {
            return (V)Reference2ObjectLinkedOpenHashMap.this.value[this.index];
        }

        public V right() {
            return (V)Reference2ObjectLinkedOpenHashMap.this.value[this.index];
        }

        public V setValue(V v) {
            V oldValue = (V)Reference2ObjectLinkedOpenHashMap.this.value[this.index];
            Reference2ObjectLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            } else {
                Map.Entry<K, V> e = (Map.Entry)o;
                return Reference2ObjectLinkedOpenHashMap.this.key[this.index] == e.getKey() && Objects.equals(Reference2ObjectLinkedOpenHashMap.this.value[this.index], e.getValue());
            }
        }

        public int hashCode() {
            return System.identityHashCode(Reference2ObjectLinkedOpenHashMap.this.key[this.index]) ^ (Reference2ObjectLinkedOpenHashMap.this.value[this.index] == null ? 0 : Reference2ObjectLinkedOpenHashMap.this.value[this.index].hashCode());
        }

        public String toString() {
            return Reference2ObjectLinkedOpenHashMap.this.key[this.index] + "=>" + Reference2ObjectLinkedOpenHashMap.this.value[this.index];
        }
    }

    private abstract class MapIterator<ConsumerType> {
        int prev = -1;
        int next = -1;
        int curr = -1;
        int index = -1;

        abstract void acceptOnIndex(ConsumerType var1, int var2);

        protected MapIterator() {
            this.next = Reference2ObjectLinkedOpenHashMap.this.first;
            this.index = 0;
        }

        public boolean hasNext() {
            return this.next != -1;
        }

        public boolean hasPrevious() {
            return this.prev != -1;
        }

        private final void ensureIndexKnown() {
            if (this.index < 0) {
                if (this.prev == -1) {
                    this.index = 0;
                } else if (this.next == -1) {
                    this.index = Reference2ObjectLinkedOpenHashMap.this.size;
                } else {
                    int pos = Reference2ObjectLinkedOpenHashMap.this.first;

                    for(this.index = 1; pos != this.prev; ++this.index) {
                        pos = (int)Reference2ObjectLinkedOpenHashMap.this.link[pos];
                    }

                }
            }
        }

        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }

        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }

        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                this.curr = this.next;
                this.next = (int)Reference2ObjectLinkedOpenHashMap.this.link[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    ++this.index;
                }

                return this.curr;
            }
        }

        public int previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            } else {
                this.curr = this.prev;
                this.prev = (int)(Reference2ObjectLinkedOpenHashMap.this.link[this.curr] >>> 32);
                this.next = this.curr;
                if (this.index >= 0) {
                    --this.index;
                }

                return this.curr;
            }
        }

        public void forEachRemaining(ConsumerType action) {
            for(; this.hasNext(); this.acceptOnIndex(action, this.curr)) {
                this.curr = this.next;
                this.next = (int)Reference2ObjectLinkedOpenHashMap.this.link[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    ++this.index;
                }
            }

        }

        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            } else {
                if (this.curr == this.prev) {
                    --this.index;
                    this.prev = (int)(Reference2ObjectLinkedOpenHashMap.this.link[this.curr] >>> 32);
                } else {
                    this.next = (int)Reference2ObjectLinkedOpenHashMap.this.link[this.curr];
                }

                --Reference2ObjectLinkedOpenHashMap.this.size;
                if (this.prev == -1) {
                    Reference2ObjectLinkedOpenHashMap.this.first = this.next;
                } else {
                    long[] var7 = Reference2ObjectLinkedOpenHashMap.this.link;
                    int var10001 = this.prev;
                    var7[var10001] ^= (Reference2ObjectLinkedOpenHashMap.this.link[this.prev] ^ (long)this.next & 4294967295L) & 4294967295L;
                }

                if (this.next == -1) {
                    Reference2ObjectLinkedOpenHashMap.this.last = this.prev;
                } else {
                    long[] var8 = Reference2ObjectLinkedOpenHashMap.this.link;
                    int var9 = this.next;
                    var8[var9] ^= (Reference2ObjectLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 4294967295L) << 32) & -4294967296L;
                }

                int pos = this.curr;
                this.curr = -1;
                if (pos == Reference2ObjectLinkedOpenHashMap.this.n) {
                    Reference2ObjectLinkedOpenHashMap.this.containsNullKey = false;
                    Reference2ObjectLinkedOpenHashMap.this.key[Reference2ObjectLinkedOpenHashMap.this.n] = null;
                    Reference2ObjectLinkedOpenHashMap.this.value[Reference2ObjectLinkedOpenHashMap.this.n] = null;
                } else {
                    K[] key = Reference2ObjectLinkedOpenHashMap.this.key;
                    V[] value = Reference2ObjectLinkedOpenHashMap.this.value;

                    while(true) {
                        int last = pos;
                        pos = pos + 1 & Reference2ObjectLinkedOpenHashMap.this.mask;

                        K curr;
                        while(true) {
                            if ((curr = (K)key[pos]) == null) {
                                key[last] = null;
                                value[last] = null;
                                return;
                            }

                            int slot = HashCommon.mix(System.identityHashCode(curr)) & Reference2ObjectLinkedOpenHashMap.this.mask;
                            if (last <= pos) {
                                if (last >= slot || slot > pos) {
                                    break;
                                }
                            } else if (last >= slot && slot > pos) {
                                break;
                            }

                            pos = pos + 1 & Reference2ObjectLinkedOpenHashMap.this.mask;
                        }

                        key[last] = curr;
                        value[last] = value[pos];
                        if (this.next == pos) {
                            this.next = last;
                        }

                        if (this.prev == pos) {
                            this.prev = last;
                        }

                        Reference2ObjectLinkedOpenHashMap.this.fixPointers(pos, last);
                    }
                }
            }
        }

        public void set(Reference2ObjectMap.Entry<K, V> ok) {
            throw new UnsupportedOperationException();
        }

        public void add(Reference2ObjectMap.Entry<K, V> ok) {
            throw new UnsupportedOperationException();
        }
    }

    private final class EntryIterator extends MapIterator implements ObjectListIterator {
        private Reference2ObjectLinkedOpenHashMap<K, V>.MapEntry entry;

        public EntryIterator() {
        }

        final void acceptOnIndex(Consumer<? super Reference2ObjectMap.Entry<K, V>> action, int index) {
            action.accept(Reference2ObjectLinkedOpenHashMap.this.new MapEntry(index));
        }

        public Reference2ObjectLinkedOpenHashMap<K, V>.MapEntry next() {
            return this.entry = Reference2ObjectLinkedOpenHashMap.this.new MapEntry(this.nextEntry());
        }

        public Reference2ObjectLinkedOpenHashMap<K, V>.MapEntry previous() {
            return this.entry = Reference2ObjectLinkedOpenHashMap.this.new MapEntry(this.previousEntry());
        }

        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    private final class FastEntryIterator extends MapIterator implements ObjectListIterator {
        final Reference2ObjectLinkedOpenHashMap<K, V>.MapEntry entry = Reference2ObjectLinkedOpenHashMap.this.new MapEntry();

        public FastEntryIterator() {
        }

        final void acceptOnIndex(Consumer<? super Reference2ObjectMap.Entry<K, V>> action, int index) {
            this.entry.index = index;
            action.accept(this.entry);
        }

        public Reference2ObjectLinkedOpenHashMap<K, V>.MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }

        public Reference2ObjectLinkedOpenHashMap<K, V>.MapEntry previous() {
            this.entry.index = this.previousEntry();
            return this.entry;
        }
    }

    private final class MapEntrySet extends AbstractObjectSortedSet<Reference2ObjectMap.Entry<K, V>> implements Reference2ObjectSortedMap.FastSortedEntrySet<K, V> {
        private MapEntrySet() {
        }

        public ObjectBidirectionalIterator<Reference2ObjectMap.Entry<K, V>> iterator() {
            return Reference2ObjectLinkedOpenHashMap.this.new EntryIterator();
        }

        public ObjectSpliterator<Reference2ObjectMap.Entry<K, V>> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(Reference2ObjectLinkedOpenHashMap.this), 81);
        }

        public Comparator<? super Reference2ObjectMap.Entry<K, V>> comparator() {
            return null;
        }

        public ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> subSet(Reference2ObjectMap.Entry<K, V> fromElement, Reference2ObjectMap.Entry<K, V> toElement) {
            throw new UnsupportedOperationException();
        }

        public ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> headSet(Reference2ObjectMap.Entry<K, V> toElement) {
            throw new UnsupportedOperationException();
        }

        public ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> tailSet(Reference2ObjectMap.Entry<K, V> fromElement) {
            throw new UnsupportedOperationException();
        }

        public Reference2ObjectMap.Entry<K, V> first() {
            if (Reference2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            } else {
                return Reference2ObjectLinkedOpenHashMap.this.new MapEntry(Reference2ObjectLinkedOpenHashMap.this.first);
            }
        }

        public Reference2ObjectMap.Entry<K, V> last() {
            if (Reference2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            } else {
                return Reference2ObjectLinkedOpenHashMap.this.new MapEntry(Reference2ObjectLinkedOpenHashMap.this.last);
            }
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            } else {
                Map.Entry<?, ?> e = (Map.Entry)o;
                K k = (K)e.getKey();
                V v = (V)e.getValue();
                if (k == null) {
                    return Reference2ObjectLinkedOpenHashMap.this.containsNullKey && Objects.equals(Reference2ObjectLinkedOpenHashMap.this.value[Reference2ObjectLinkedOpenHashMap.this.n], v);
                } else {
                    K[] key = Reference2ObjectLinkedOpenHashMap.this.key;
                    K curr;
                    int pos;
                    if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & Reference2ObjectLinkedOpenHashMap.this.mask]) == null) {
                        return false;
                    } else if (k == curr) {
                        return Objects.equals(Reference2ObjectLinkedOpenHashMap.this.value[pos], v);
                    } else {
                        while((curr = (K)key[pos = pos + 1 & Reference2ObjectLinkedOpenHashMap.this.mask]) != null) {
                            if (k == curr) {
                                return Objects.equals(Reference2ObjectLinkedOpenHashMap.this.value[pos], v);
                            }
                        }

                        return false;
                    }
                }
            }
        }

        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            } else {
                Map.Entry<?, ?> e = (Map.Entry)o;
                K k = (K)e.getKey();
                V v = (V)e.getValue();
                if (k == null) {
                    if (Reference2ObjectLinkedOpenHashMap.this.containsNullKey && Objects.equals(Reference2ObjectLinkedOpenHashMap.this.value[Reference2ObjectLinkedOpenHashMap.this.n], v)) {
                        Reference2ObjectLinkedOpenHashMap.this.removeNullEntry();
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    K[] key = Reference2ObjectLinkedOpenHashMap.this.key;
                    K curr;
                    int pos;
                    if ((curr = (K)key[pos = HashCommon.mix(System.identityHashCode(k)) & Reference2ObjectLinkedOpenHashMap.this.mask]) == null) {
                        return false;
                    } else if (curr == k) {
                        if (Objects.equals(Reference2ObjectLinkedOpenHashMap.this.value[pos], v)) {
                            Reference2ObjectLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        while((curr = (K)key[pos = pos + 1 & Reference2ObjectLinkedOpenHashMap.this.mask]) != null) {
                            if (curr == k && Objects.equals(Reference2ObjectLinkedOpenHashMap.this.value[pos], v)) {
                                Reference2ObjectLinkedOpenHashMap.this.removeEntry(pos);
                                return true;
                            }
                        }

                        return false;
                    }
                }
            }
        }

        public int size() {
            return Reference2ObjectLinkedOpenHashMap.this.size;
        }

        public void clear() {
            Reference2ObjectLinkedOpenHashMap.this.clear();
        }

        public ObjectListIterator<Reference2ObjectMap.Entry<K, V>> fastIterator() {
            return Reference2ObjectLinkedOpenHashMap.this.new FastEntryIterator();
        }

        public void forEach(Consumer<? super Reference2ObjectMap.Entry<K, V>> consumer) {
            long[] link = Reference2ObjectLinkedOpenHashMap.this.link;
            int i = Reference2ObjectLinkedOpenHashMap.this.size;
            int next = Reference2ObjectLinkedOpenHashMap.this.first;

            while(i-- != 0) {
                int curr = next;
                next = (int)link[next];
                consumer.accept(Reference2ObjectLinkedOpenHashMap.this.new MapEntry(curr));
            }

        }

        public void fastForEach(Consumer<? super Reference2ObjectMap.Entry<K, V>> consumer) {
            Reference2ObjectLinkedOpenHashMap<K, V>.MapEntry entry = Reference2ObjectLinkedOpenHashMap.this.new MapEntry();
            long[] link = Reference2ObjectLinkedOpenHashMap.this.link;
            int i = Reference2ObjectLinkedOpenHashMap.this.size;
            int next = Reference2ObjectLinkedOpenHashMap.this.first;

            while(i-- != 0) {
                entry.index = next;
                next = (int)link[next];
                consumer.accept(entry);
            }

        }
    }

    private final class KeyIterator extends MapIterator implements ObjectListIterator {
        public K previous() {
            return (K)Reference2ObjectLinkedOpenHashMap.this.key[this.previousEntry()];
        }

        public KeyIterator() {
        }

        final void acceptOnIndex(Consumer<? super K> action, int index) {
            action.accept(Reference2ObjectLinkedOpenHashMap.this.key[index]);
        }

        public K next() {
            return (K)Reference2ObjectLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }

    private final class KeySet extends AbstractReferenceSortedSet<K> {
        private KeySet() {
        }

        public ObjectListIterator<K> iterator() {
            return Reference2ObjectLinkedOpenHashMap.this.new KeyIterator();
        }

        public ObjectSpliterator<K> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(Reference2ObjectLinkedOpenHashMap.this), 81);
        }

        public void forEach(Consumer<? super K> consumer) {
            long[] link = Reference2ObjectLinkedOpenHashMap.this.link;
            K[] key = Reference2ObjectLinkedOpenHashMap.this.key;
            int i = Reference2ObjectLinkedOpenHashMap.this.size;
            int next = Reference2ObjectLinkedOpenHashMap.this.first;

            while(i-- != 0) {
                int curr = next;
                next = (int)link[next];
                consumer.accept(key[curr]);
            }

        }

        public int size() {
            return Reference2ObjectLinkedOpenHashMap.this.size;
        }

        public boolean contains(Object k) {
            return Reference2ObjectLinkedOpenHashMap.this.containsKey(k);
        }

        public boolean remove(Object k) {
            int oldSize = Reference2ObjectLinkedOpenHashMap.this.size;
            Reference2ObjectLinkedOpenHashMap.this.remove(k);
            return Reference2ObjectLinkedOpenHashMap.this.size != oldSize;
        }

        public void clear() {
            Reference2ObjectLinkedOpenHashMap.this.clear();
        }

        public K first() {
            if (Reference2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            } else {
                return (K)Reference2ObjectLinkedOpenHashMap.this.key[Reference2ObjectLinkedOpenHashMap.this.first];
            }
        }

        public K last() {
            if (Reference2ObjectLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            } else {
                return (K)Reference2ObjectLinkedOpenHashMap.this.key[Reference2ObjectLinkedOpenHashMap.this.last];
            }
        }

        public Comparator<? super K> comparator() {
            return null;
        }

        public ReferenceSortedSet<K> tailSet(K from) {
            throw new UnsupportedOperationException();
        }

        public ReferenceSortedSet<K> headSet(K to) {
            throw new UnsupportedOperationException();
        }

        public ReferenceSortedSet<K> subSet(K from, K to) {
            throw new UnsupportedOperationException();
        }
    }

    private final class ValueIterator extends MapIterator implements ObjectListIterator {
        public V previous() {
            return (V)Reference2ObjectLinkedOpenHashMap.this.value[this.previousEntry()];
        }

        public ValueIterator() {
        }

        final void acceptOnIndex(Consumer<? super V> action, int index) {
            action.accept(Reference2ObjectLinkedOpenHashMap.this.value[index]);
        }

        public V next() {
            return (V)Reference2ObjectLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
