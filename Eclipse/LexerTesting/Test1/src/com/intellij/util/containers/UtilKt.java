
// Her er en oversettelse av Kotlin-koden til Java. Vær oppmerksom på at mange av funksjonene i
// originalkoden bruker Kotlin-spesifikke funksjoner som inline, contract og reified typer, som ikke har direkte motstykker i Java.

// Java-versjonen bruker standard funksjonelle grensesnitt fra java.util.function.

// Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.containers;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.lang.Language;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class UtilKt {
    private UtilKt() { }

    /**
     * Tilsvarer asSafely<T>(). Returnerer objektet hvis det er av type T, ellers null.
     */
    @Nullable
    public static <T> T asSafely(@Nullable Object obj, @NotNull Class<T> clazz) {
        return clazz.isInstance(obj) ? clazz.cast(obj) : null;
    }

    @Nullable
    public static <T> T runIf(boolean condition, @NotNull Supplier<T> block) {
        return condition ? block.get() : null;
    }

    @ApiStatus.ScheduledForRemoval
    @Deprecated
    public static <T> T alsoIfNull(@Nullable T value, @NotNull Runnable block) {
        if (value == null) {
            block.run();
        }
        return value;
    }

    public static <T> T applyIf(@NotNull T receiver, boolean condition, @NotNull Function<T, T> body) {
        return condition ? body.apply(receiver) : receiver;
    }

    // Java har ikke innebygd støtte for 'operator' delegates for AtomicReference.
    // Man må bruke .get() og .set() direkte på AtomicReference-objektet i Java.

    /**
     * Tilsvarer Pair.use i Kotlin for AutoCloseables.
     */
    public static <T1 extends AutoCloseable, T2 extends AutoCloseable, R> R use(
            @NotNull Supplier<T1> factory1,
            @NotNull Supplier<T2> factory2,
            @NotNull BiFunctionWithException<T1, T2, R> block) throws Exception {
        try (T1 o1 = factory1.get(); T2 o2 = factory2.get()) {
            return block.apply(o1, o2);
        }
    }

    @FunctionalInterface
    public interface BiFunctionWithException<T1, T2, R> {
        R apply(T1 t1, T2 t2) throws Exception;
    }

    /**
     * Implementasjon av takeWhileInclusive for Java Stream.
     */
    public static <T> Stream<T> takeWhileInclusive(Stream<T> stream, Predicate<? super T> predicate) {
        return IterableScanner.toStream(new TakeWhileInclusiveIterator<>(stream.iterator(), predicate));
    }

    private static class TakeWhileInclusiveIterator<T> implements Iterator<T> {
        private final Iterator<T> source;
        private final Predicate<? super T> predicate;
        private T nextItem = null;
        private boolean done = false;
        private boolean hasNextCalculated = false;

        public TakeWhileInclusiveIterator(Iterator<T> source, Predicate<? super T> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            return !done && (hasNextCalculated || calculateNext());
        }

        private boolean calculateNext() {
            if (source.hasNext()) {
                nextItem = source.next();
                hasNextCalculated = true;
                return true;
            }
            done = true;
            return false;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T result = nextItem;
            hasNextCalculated = false;
            nextItem = null;
            if (!predicate.test(result)) {
                done = true;
            }
            return result;
        }
    }

    // Hjelpeklasse for å konvertere iterator til stream (siden Java ikke har Sequence)
    private static class IterableScanner {
        public static <T> Stream<T> toStream(Iterator<T> iterator) {
            Iterable<T> iterable = () -> iterator;
            return java.util.stream.StreamSupport.stream(iterable.spliterator(), false);
        }
    }

	public static Map<Class<? extends Language>, @NotNull Language> with(
			Map<Class<? extends Language>, @NotNull Language> registeredLanguages, Class<? extends Language> langClass,
			Language language) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Map<String, Language> with(Map<String, Language> registeredIds, @NonNls @NotNull String iD,
			Language language) {
		// TODO Auto-generated method stub
		return null;
	}
}
