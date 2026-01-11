// Her er koden oversatt til Java:
	
// Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.util.containers.ConcurrentIntObjectMap;
import com.intellij.util.containers.ConcurrentLongObjectMap;
import com.intellij.util.containers.ConcurrentIntObjectHashMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A factory of various containers which implementations are different in pre/after jdk9.
 * Used to simplify porting jdk9+ collections to jdk8-modules
 */
@ApiStatus.Internal
public abstract class Java11Shim {
    public static Java11Shim INSTANCE = new DefaultJava11Shim();

    @NotNull
    public static <V> ConcurrentLongObjectMap<V> createConcurrentLongObjectMap() {
        return new ConcurrentLongObjectHashMap<>();
    }

    @NotNull
    public static <V> ConcurrentIntObjectMap<V> createConcurrentIntObjectMap() {
        return new ConcurrentIntObjectHashMap<>();
    }

    @NotNull
    public static <V> ConcurrentIntObjectMap<V> createConcurrentIntObjectMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        return new ConcurrentIntObjectHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    @NotNull
    public static <V> ConcurrentIntObjectMap<V> createConcurrentIntObjectSoftValueMap() {
        return new ConcurrentIntKeySoftValueHashMap<>();
    }

    @NotNull
    public static <V> ConcurrentIntObjectMap<V> createConcurrentIntObjectWeakValueMap() {
        return new ConcurrentIntKeyWeakValueHashMap<>();
    }

    /**
     * The implementation of `copyOf` is allowed to not do copy - it can return the same map, read `copyOf` as `immutable`.
     */
    @NotNull
    public abstract <K, V> Map<K, V> copyOf(@NotNull Map<K, V> map);

    @NotNull
    public abstract <K, V> Map<K, V> mapOf(@NotNull K k, @NotNull V v);

    @NotNull
    public abstract <K, V> Map<K, V> mapOf(@NotNull K k, @NotNull V v, @NotNull K k2, @NotNull V v2);

    @NotNull
    public abstract <K, V> Map<K, V> mapOf();

    @NotNull
    public abstract <E> Set<E> copyOf(@NotNull Collection<E> collection);

    @NotNull
    public abstract <E> List<E> copyOfList(@NotNull Collection<E> collection);

    @NotNull
    public abstract <E> List<E> listOf();

    @NotNull
    public abstract <E> List<E> listOf(@NotNull E element);

    @NotNull
    public abstract <E> List<E> listOf(@NotNull E e1, @NotNull E e2);

    @NotNull
    public abstract <E> List<E> listOf(@NotNull E[] array, int size);

    @Nullable
    public abstract Class<?> getCallerClass(int stackFrameIndex);
}
