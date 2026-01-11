
// Her er PersistentSet oversatt til Java.
// Merk at Kotlin-spesifikke typer som (E) -> Boolean oversettes til Predicate<E>,
// og out E (varians) fjernes da Java håndterer dette annerledes gjennom wildcards ved bruk.

package kotlinx.collections.immutable;

import java.util.Collection;
import java.util.function.Predicate;

public interface PersistentSet<E> extends ImmutableSet<E>, PersistentCollection<E> {

    @Override
    PersistentSet<E> add(E element);

    @Override
    PersistentSet<E> addAll(Collection<? extends E> elements);

    @Override
    PersistentSet<E> remove(E element);

    @Override
    PersistentSet<E> removeAll(Collection<? extends E> elements);

    @Override
    PersistentSet<E> removeAll(Predicate<? super E> predicate);

    @Override
    PersistentSet<E> retainAll(Collection<? extends E> elements);

    @Override
    PersistentSet<E> clear();

    @Override
    Builder<E> builder();

    interface Builder<E> extends java.util.Set<E>, PersistentCollection.Builder<E> {
        @Override
        PersistentSet<E> build();
    }
}
