//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.util;

import com.intellij.openapi.util.Pair;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Obsolete;

@Obsolete
public final class Functions {
    private static final Function.Mono<?> ID = new Function.Mono<Object>() {
        public Object fun(Object o) {
            return o;
        }

        public String toString() {
            return "Functions.ID";
        }
    };
    private static final Function<?, String> TO_STRING = new Function<Object, String>() {
        public String fun(Object o) {
            return String.valueOf(o);
        }

        public String toString() {
            return "Functions.TO_STRING";
        }
    };
    private static final Function<Pair<?, ?>, Object> PAIR_FIRST = (pair) -> Pair.getFirst(pair);
    private static final Function<Pair<?, ?>, Object> PAIR_SECOND = (pair) -> Pair.getSecond(pair);
    private static final Function<Object[], Iterable<Object>> WRAP_ARRAY = (t) -> t == null ? Collections.emptyList() : Arrays.asList(t);

    public static <A> Function.@NotNull Mono<A> id() {
        return (Function.Mono)identity();
    }

    public static <A, B> @NotNull Function<A, B> constant(B b) {
        return (a) -> b;
    }

    public static <A, B> @NotNull Function<A, B> identity() {
        return ID;
    }

    public static <A, B, C> @NotNull Function<A, C> compose(@NotNull Function<? super A, ? extends B> f1, @NotNull Function<? super B, ? extends C> f2) {
        if (f1 != identity() && f2 != identity()) {
            return (a) -> f2.fun(f1.fun(a));
        } else if (f1 == f2) {
            return identity();
        } else {
            return f1 == identity() ? f2 : f1;
        }
    }

    public static <A> @NotNull Function<A, String> TO_STRING() {
        return TO_STRING;
    }

    public static <A, B> @NotNull Function<A, B> fromMap(@NotNull Map<? super A, ? extends B> map) {
        Objects.requireNonNull(map);
        return map::get;
    }

    public static <A> @NotNull Function<Pair<A, ?>, A> pairFirst() {
        return PAIR_FIRST;
    }

    public static <B> @NotNull Function<Pair<?, B>, B> pairSecond() {
        return PAIR_SECOND;
    }

    public static <T> @NotNull Function<T[], Iterable<T>> wrapArray() {
        return WRAP_ARRAY;
    }
}
