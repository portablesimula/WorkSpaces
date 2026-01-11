
// Her er en oversettelse av Kotlin-filen til en tilsvarende Java-struktur.
// Fordi kildefilen består av frittstående funksjoner (top-level functions)
// og utvidelsesfunksjoner (extension functions), vil Java-versjonen kreve
// en hjelpeklasse (f.eks. ImmutableKt) med statiske metoder.
//
// Vær oppmerksom på at Kotlin-spesifikke typer som Pair, Iterable og Sequence
// her er referert fra Kotlins standardbibliotek.

package kotlinx.collections.immutable;

//import kotlin.Pair;
//import kotlin.Unit;
//import kotlin.jvm.functions.Function1;
//import kotlin.sequences.Sequence;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class ExtensionsKt {

    // --- Deklarasjoner av utdaterte (Deprecated) fabrikkmetoder ---

    @Deprecated
    public static <K, V> PersistentMap<K, V> immutableHashMapOf(Pair<K, V>... pairs) {
        throw new UnsupportedOperationException("Compiled code");
    }

    @Deprecated
    public static <E> PersistentSet<E> immutableHashSetOf(E... elements) {
        throw new UnsupportedOperationException("Compiled code");
    }

    @Deprecated
    public static <E> PersistentList<E> immutableListOf() {
        throw new UnsupportedOperationException("Compiled code");
    }

    @Deprecated
    public static <E> PersistentList<E> immutableListOf(E... elements) {
        throw new UnsupportedOperationException("Compiled code");
    }

    @Deprecated
    public static <K, V> PersistentMap<K, V> immutableMapOf(Pair<K, V>... pairs) {
        throw new UnsupportedOperationException("Compiled code");
    }

    @Deprecated
    public static <E> PersistentSet<E> immutableSetOf() {
        throw new UnsupportedOperationException("Compiled code");
    }

    @Deprecated
    public static <E> PersistentSet<E> immutableSetOf(E... elements) {
        throw new UnsupportedOperationException("Compiled code");
    }

    // --- Gjeldende (Persistent) fabrikkmetoder ---

    public static <K, V> PersistentMap<K, V> persistentHashMapOf() {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <K, V> PersistentMap<K, V> persistentHashMapOf(Pair<K, V>... pairs) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentSet<E> persistentHashSetOf() {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentSet<E> persistentHashSetOf(E... elements) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentList<E> persistentListOf() {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentList<E> persistentListOf(E... elements) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <K, V> PersistentMap<K, V> persistentMapOf() {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <K, V> PersistentMap<K, V> persistentMapOf(Pair<K, V>... pairs) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentSet<E> persistentSetOf() {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentSet<E> persistentSetOf(E... elements) {
        throw new UnsupportedOperationException("Compiled code");
    }

    // --- Utvidelsesfunksjoner (Extension Functions) oversatt til statiske metoder ---

    public static <E> PersistentSet<E> intersect(PersistentCollection<E> receiver, Iterable<? extends E> elements) {
        throw new UnsupportedOperationException("Compiled code");
    }

    // Operatører (plus/minus) må kalles som vanlige metoder i Java

    public static <E> PersistentCollection<E> minus(PersistentCollection<E> receiver, E element) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentCollection<E> minus(PersistentCollection<E> receiver, E[] elements) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentList<E> minus(PersistentList<E> receiver, E element) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <K, V> PersistentMap<K, V> minus(PersistentMap<? extends K, V> receiver, K key) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentSet<E> minus(PersistentSet<E> receiver, E element) {
        throw new UnsupportedOperationException("Compiled code");
    }

    // Mutate-funksjoner (bruker Kotlins Function1 for lambda-støtte)

    public static <T> PersistentList<T> mutate(PersistentList<T> receiver, Function1<? super java.util.List<T>, Unit> mutator) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <K, V> PersistentMap<K, V> mutate(PersistentMap<? extends K, V> receiver, Function1<? super Map<K, V>, Unit> mutator) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <T> PersistentSet<T> mutate(PersistentSet<T> receiver, Function1<? super Set<T>, Unit> mutator) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentCollection<E> plus(PersistentCollection<E> receiver, E element) {
        throw new UnsupportedOperationException("Compiled code");
    }

    public static <E> PersistentList<E> plus(PersistentList<E> receiver, E element) {
        throw new UnsupportedOperationException("Compiled code");
    }
}
