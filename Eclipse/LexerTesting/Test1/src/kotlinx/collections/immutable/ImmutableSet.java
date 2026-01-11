
// Her er den tilsvarende koden i Java.
// Merk at Java ikke har direkte støtte for varians (out) på grensesnitt-nivå eller
// arv fra flere grensesnitt med samme type-parameter på nøyaktig samme måte som Kotlin,
// men den funksjonelle oversettelsen er som følger:

package kotlinx.collections.immutable;

import java.util.Set;

public interface ImmutableSet<E> extends Set<E>, ImmutableCollection<E> {
    // Implementasjon av metoder er ikke tilgjengelig
}
