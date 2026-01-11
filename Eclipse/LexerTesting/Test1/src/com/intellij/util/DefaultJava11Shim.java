
// Her er koden oversatt fra Kotlin til Java:

//Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util;

import com.intellij.util.containers.ConcurrentLongObjectMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

class DefaultJava11Shim extends Java11Shim {
 @Override
 public <K, V> Map<K, V> copyOf(@NotNull Map<? extends K, ? extends V> map) {
     return Collections.unmodifiableMap(map);
 }

 @Override
 public <K, V> Map<K, V> mapOf(@NotNull K k, V v) {
     return Collections.singletonMap(k, v);
 }

 @Override
 public <K, V> Map<K, V> mapOf(@NotNull K k, V v, @NotNull K k2, V v2) {
     Map<K, V> map = new HashMap<>(2);
     map.put(k, v);
     map.put(k2, v2);
     return map;
 }

 @Override
 public <E> Set<E> copyOf(@NotNull Collection<? extends E> collection) {
     return Collections.unmodifiableSet(new HashSet<>(collection));
 }

 @Override
 public <V> ConcurrentLongObjectMap<V> createConcurrentLongObjectMap() {
     return new ConcurrentLongObjectHashMap<>();
 }

 @Override
 public <K, V> Map<K, V> mapOf() {
     return Collections.emptyMap();
 }

 @Override
 public <E> List<E> listOf() {
     return Collections.emptyList();
 }

 @Override
 public <E> List<E> listOf(E element) {
     return Collections.singletonList(element);
 }

 @Override
 public <E> List<E> copyOfList(@NotNull Collection<? extends E> collection) {
     return Collections.unmodifiableList(new ArrayList<>(collection));
 }

 @Override
 public <E> List<E> listOf(E e1, E e2) {
     return Arrays.asList(e1, e2);
 }

 @Override
 public <E> List<E> listOf(@NotNull E[] array, int size) {
     if (array.length == size) {
         return Arrays.asList(array);
     } else {
         return Arrays.asList(array).subList(0, size);
     }
 }

 @Nullable
 @Override
 public Class<?> getCallerClass(int stackFrameIndex) {
     // +1 for å kompensere for den nåværende stack-rammen
     return ReflectionUtil.getCallerClass(stackFrameIndex + 1);
 }
}
