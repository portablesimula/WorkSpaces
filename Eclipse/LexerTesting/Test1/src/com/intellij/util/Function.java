package com.intellij.util;

import org.jetbrains.annotations.ApiStatus.Obsolete;

@FunctionalInterface
@Obsolete
public interface Function<Param, Result> extends java.util.function.Function<Param, Result> {
    Result fun(Param var1);

    default Result apply(Param param) {
        return (Result)this.fun(param);
    }

    @Obsolete
    public interface Mono<T> extends Function<T, T> {
    }
}
