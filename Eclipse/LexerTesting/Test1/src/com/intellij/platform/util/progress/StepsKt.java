
// // Oversettelse av Kotlin-kode som bruker suspend-funksjoner og coroutines til Java
// krever bruk av Continuation-objekter (fra Kotlin-standardbiblioteket)
// eller biblioteker som Project Loom (Virtual Threads).
//
// Siden IntelliJ-plattformen er tungt basert på Kotlin Coroutines,
// vil den direkte Java-ekvivalenten kreve at man kaller Kotlin-runtime manuelt.
// Her er en oversettelse til Java slik koden ville sett ut for en Java-bruker i IntelliJ-økosystemet:
	
//Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.platform.util.progress;

import com.intellij.platform.util.progress.impl.EmptyProgressStep;
import com.intellij.platform.util.progress.impl.ProgressStepKt;
import com.intellij.platform.util.progress.impl.ProgressText;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
* Java-oversettelse av progress-verktøy.
* Merk: Siden originale funksjoner er 'suspend', krever de en Continuation i Java,
* eller må kalles fra en 'runBlocking' / 'BuildersKt' kontekst.
*/
public final class StepsKt {

 private StepsKt() {}

 public static <T> Object reportSequentialProgress(
         int size,
         @NotNull Function1<? super SequentialProgressReporter, ? extends T> action,
         @NotNull Continuation<? super T> $completion
 ) {
     return ProgressStepKt.internalCurrentStepAsSequential(size, $completion)
             .use((handle) -> action.invoke(handle.getReporter()));
 }

 public static <T> Object reportProgress(
         int size,
         @NotNull Function2<? super ProgressReporter, ? super Continuation<? super T>, ? extends Object> action,
         @NotNull Continuation<? super T> $completion
 ) {
     return ProgressStepKt.internalCurrentStepAsConcurrent(size, $completion)
             .use((handle) -> action.invoke(handle.getReporter(), $completion));
 }

 public static <T> Object reportRawProgress(
         @NotNull Function1<? super RawProgressReporter, ? extends T> action,
         @NotNull Continuation<? super T> $completion
 ) {
     return ProgressStepKt.internalCurrentStepAsRaw($completion)
             .use((handle) -> action.invoke(handle.getReporter()));
 }

 public static <T> Object withProgressText(
         @Nullable ProgressText text,
         @NotNull Function1<? super Continuation<? super T>, ? extends Object> action,
         @NotNull Continuation<? super T> $completion
 ) {
     if (text == null) {
         return BuildersKt.coroutineScope(action, $completion);
     }
     return ProgressStepKt.currentProgressStep($completion).withText(text, action, $completion);
 }

 public static <T> Object ignoreProgressReportingIn(
         @NotNull Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> action,
         @NotNull Continuation<? super T> $completion
 ) {
     CoroutineContext context = EmptyProgressStep.INSTANCE.asContextElement();
     return BuildersKt.withContext(context, action, $completion);
 }

 // Helper for Collections
 public static <T> void forEachWithProgress(
         @NotNull Collection<T> collection,
         @NotNull Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> action,
         @NotNull Continuation<? super Unit> $completion
 ) {
     reportSequentialProgress(collection.size(), reporter -> {
         for (T item : collection) {
             reporter.itemStep(() -> action.invoke(item, (Continuation<? super Unit>) $completion));
         }
         return Unit.INSTANCE;
     }, $completion);
 }
}
