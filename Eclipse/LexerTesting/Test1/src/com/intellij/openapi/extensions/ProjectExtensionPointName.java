
// Her er Kotlin-koden oversatt til Java.
// Merk at jeg har brukt JetBrains-annotasjoner som @NotNull og @Nullable
// for å opprettholde null-sikkerheten fra den opprinnelige Kotlin-koden.

//Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.extensions;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.extensions.impl.ExtensionProcessingHelper;
import com.intellij.util.ThreeState;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
* Do not use.
*
* Provides access to a project-level or module-level extension point. Since extensions are supposed to be stateless, storing different
* instances of an extension for each project or module just wastes the memory and complicates code, so **it's strongly recommended not
* to introduce new project-level and module-level extension points**. If you need to have {@link com.intellij.openapi.project.Project}
* or {@link com.intellij.openapi.module.Module} instance in some extension's method, pass it as a parameter and use the default
* application-level extension point.
*/
public non-sealed class ProjectExtensionPointName<T> extends BaseExtensionPointName<T> {

 public ProjectExtensionPointName(@NotNull @NonNls String name) {
     super(name);
 }

 @NotNull
 public ExtensionPoint<T> getPoint(@NotNull AreaInstance areaInstance) {
     return getPointImpl(areaInstance);
 }

 @NotNull
 public List<T> getExtensions(@NotNull AreaInstance areaInstance) {
     return getPointImpl(areaInstance).getExtensionList();
 }

 @Nullable
 public <V extends T> V findExtension(@NotNull Class<V> instanceOf, @NotNull AreaInstance areaInstance) {
     return getPointImpl(areaInstance).findExtension(instanceOf, false, ThreeState.UNSURE);
 }

 @NotNull
 public <V extends T> V findExtensionOrFail(@NotNull Class<V> instanceOf, @NotNull AreaInstance areaInstance) {
     V result = getPointImpl(areaInstance).findExtension(instanceOf, true, ThreeState.UNSURE);
     assert result != null;
     return result;
 }

 public boolean hasAnyExtensions(@NotNull AreaInstance areaInstance) {
     return getPointImpl(areaInstance).size() != 0;
 }

 @Nullable
 public T findFirstSafe(@NotNull AreaInstance areaInstance, @NotNull Predicate<? super T> predicate) {
     return ExtensionProcessingHelper.findFirstSafe(predicate, getPointImpl(areaInstance).asSequence());
 }

 @Nullable
 public <R> R computeSafeIfAny(@NotNull AreaInstance areaInstance, @NotNull Function<T, @Nullable R> processor) {
     return ExtensionProcessingHelper.computeSafeIfAny(processor::apply, getPointImpl(areaInstance).asSequence());
 }

 public void addExtensionPointListener(@NotNull AreaInstance areaInstance, @NotNull ExtensionPointListener<T> listener, @Nullable Disposable parentDisposable) {
     getPointImpl(areaInstance).addExtensionPointListener(listener, false, parentDisposable);
 }

 public void addChangeListener(@NotNull AreaInstance areaInstance, @NotNull Runnable listener, @Nullable Disposable parentDisposable) {
     getPointImpl(areaInstance).addChangeListener(listener, parentDisposable);
 }

 @ApiStatus.Experimental
 @NotNull
 public kotlin.sequences.Sequence<T> asSequence(@NotNull AreaInstance areaInstance) {
     return getPointImpl(areaInstance).asSequence();
 }
}
