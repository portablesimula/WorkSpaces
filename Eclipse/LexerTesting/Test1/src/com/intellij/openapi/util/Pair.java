package com.intellij.openapi.util;

import java.util.Comparator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Pair<A, B> {
    public final A first;
    public final B second;
    private static final Pair EMPTY = create((Object)null, (Object)null);

    @NotNull
    public static <A, B> Pair<A, B> create(A first, B second) {
        return new Pair<A, B>(first, second);
    }

    @NotNull
    public static <A, B> NonNull<A, B> createNonNull(@NotNull A first, @NotNull B second) {
        return new NonNull<A, B>(first, second);
    }

    @NotNull
    public static <A, B> Pair<A, B> pair(A first, B second) {
        return new Pair<A, B>(first, second);
    }

    public static <T> T getFirst(@Nullable Pair<T, ?> pair) {
        return (T)(pair != null ? pair.first : null);
    }

    public static <T> T getSecond(@Nullable Pair<?, T> pair) {
        return (T)(pair != null ? pair.second : null);
    }

    @NotNull
    public static <A, B> Pair<A, B> empty() {
        return EMPTY;
    }

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public final A getFirst() {
        return this.first;
    }

    public final B getSecond() {
        return this.second;
    }

    public final boolean equals(Object o) {
        return o instanceof Pair && Comparing.equal(this.first, ((Pair)o).first) && Comparing.equal(this.second, ((Pair)o).second);
    }

    public int hashCode() {
        int result = this.first != null ? this.first.hashCode() : 0;
        result = 31 * result + (this.second != null ? this.second.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "<" + this.first + "," + this.second + ">";
    }

    public static <A extends Comparable<? super A>, B> Comparator<Pair<A, B>> comparingByFirst() {
        return new Comparator<Pair<A, B>>() {
            public int compare(Pair<A, B> o1, Pair<A, B> o2) {
                return ((Comparable)o1.first).compareTo(o2.first);
            }
        };
    }

    public static <A, B extends Comparable<? super B>> Comparator<Pair<A, B>> comparingBySecond() {
        return new Comparator<Pair<A, B>>() {
            public int compare(Pair<A, B> o1, Pair<A, B> o2) {
                return ((Comparable)o1.second).compareTo(o2.second);
            }
        };
    }

    public static class NonNull<A, B> extends Pair<A, B> {
        public NonNull(@NotNull A first, @NotNull B second) {
            super(first, second);
        }
    }
}
