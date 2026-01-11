package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Size64;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractReference2ObjectMap<K, V> extends AbstractReference2ObjectFunction<K, V> implements Reference2ObjectMap<K, V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractReference2ObjectMap() {
    }

    public boolean containsKey(Object k) {
        ObjectIterator<Reference2ObjectMap.Entry<K, V>> i = this.reference2ObjectEntrySet().iterator();

        while(i.hasNext()) {
            if (((Reference2ObjectMap.Entry)i.next()).getKey() == k) {
                return true;
            }
        }

        return false;
    }

    public boolean containsValue(Object v) {
        ObjectIterator<Reference2ObjectMap.Entry<K, V>> i = this.reference2ObjectEntrySet().iterator();

        while(i.hasNext()) {
            if (((Reference2ObjectMap.Entry)i.next()).getValue() == v) {
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>() {
            public boolean contains(Object k) {
                return AbstractReference2ObjectMap.this.containsKey(k);
            }

            public int size() {
                return AbstractReference2ObjectMap.this.size();
            }

            public void clear() {
                AbstractReference2ObjectMap.this.clear();
            }

            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Reference2ObjectMap.Entry<K, V>> i = Reference2ObjectMaps.fastIterator(AbstractReference2ObjectMap.this);

                    public K next() {
                        return (K)((Reference2ObjectMap.Entry)this.i.next()).getKey();
                    }

                    public boolean hasNext() {
                        return this.i.hasNext();
                    }

                    public void remove() {
                        this.i.remove();
                    }

                    public void forEachRemaining(Consumer<? super K> action) {
                        this.i.forEachRemaining((entry) -> action.accept(entry.getKey()));
                    }
                };
            }

            public ObjectSpliterator<K> spliterator() {
                return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractReference2ObjectMap.this), 65);
            }
        };
    }

    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            public boolean contains(Object k) {
                return AbstractReference2ObjectMap.this.containsValue(k);
            }

            public int size() {
                return AbstractReference2ObjectMap.this.size();
            }

            public void clear() {
                AbstractReference2ObjectMap.this.clear();
            }

            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Reference2ObjectMap.Entry<K, V>> i = Reference2ObjectMaps.fastIterator(AbstractReference2ObjectMap.this);

                    public V next() {
                        return (V)((Reference2ObjectMap.Entry)this.i.next()).getValue();
                    }

                    public boolean hasNext() {
                        return this.i.hasNext();
                    }

                    public void remove() {
                        this.i.remove();
                    }

                    public void forEachRemaining(Consumer<? super V> action) {
                        this.i.forEachRemaining((entry) -> action.accept(entry.getValue()));
                    }
                };
            }

            public ObjectSpliterator<V> spliterator() {
                return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractReference2ObjectMap.this), 64);
            }
        };
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        if (m instanceof Reference2ObjectMap) {
            ObjectIterator<Reference2ObjectMap.Entry<K, V>> i = Reference2ObjectMaps.fastIterator((Reference2ObjectMap)m);

            while(i.hasNext()) {
                Reference2ObjectMap.Entry<? extends K, ? extends V> e = (Reference2ObjectMap.Entry)i.next();
                this.put(e.getKey(), e.getValue());
            }
        } else {
            int n = m.size();
            Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m.entrySet().iterator();

            while(n-- != 0) {
                Map.Entry<? extends K, ? extends V> e = (Map.Entry)i.next();
                this.put(e.getKey(), e.getValue());
            }
        }

    }

    public int hashCode() {
        int h = 0;
        int n = this.size();

        for(ObjectIterator<Reference2ObjectMap.Entry<K, V>> i = Reference2ObjectMaps.fastIterator(this); n-- != 0; h += ((Reference2ObjectMap.Entry)i.next()).hashCode()) {
        }

        return h;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Map)) {
            return false;
        } else {
            Map<?, ?> m = (Map)o;
            return m.size() != this.size() ? false : this.reference2ObjectEntrySet().containsAll(m.entrySet());
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Reference2ObjectMap.Entry<K, V>> i = Reference2ObjectMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");

        while(n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }

            Reference2ObjectMap.Entry<K, V> e = (Reference2ObjectMap.Entry)i.next();
            if (this == e.getKey()) {
                s.append("(this map)");
            } else {
                s.append(String.valueOf(e.getKey()));
            }

            s.append("=>");
            if (this == e.getValue()) {
                s.append("(this map)");
            } else {
                s.append(String.valueOf(e.getValue()));
            }
        }

        s.append("}");
        return s.toString();
    }
}
