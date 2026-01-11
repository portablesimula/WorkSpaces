// Her er PersistentList oversatt til Java.
// Merk at Kotlin-spesifikke typer som Int og Boolean oversettes til primitive typer,
// mens funksjonstypen (E) -> Boolean oversettes til java.util.function.Predicate. 

package kotlinx.collections.immutable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface PersistentList<E> extends ImmutableList<E>, PersistentCollection<E> {

    PersistentList<E> add(E element);

    PersistentList<E> add(int index, E element);

    PersistentList<E> addAll(int index, Collection<? extends E> c);

    PersistentList<E> addAll(Collection<? extends E> elements);

    Builder<E> builder();

    PersistentList<E> clear();

    PersistentList<E> remove(E element);

    PersistentList<E> removeAll(Predicate<? super E> predicate);

    PersistentList<E> removeAll(Collection<? extends E> elements);

    PersistentList<E> removeAt(int index);

    PersistentList<E> retainAll(Collection<? extends E> elements);

    PersistentList<E> set(int index, E element);

    interface Builder<E> extends List<E>, PersistentCollection.Builder<E> {
        PersistentList<E> build();
    }
}
