package it.unimi.dsi.fastutil.objects;

import java.util.Spliterator;

public interface ObjectSpliterator<K> extends Spliterator<K> {
    ObjectSpliterator<K> trySplit();
}
