
// Her er koden oversatt til Java:
	
//Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.concurrency.annotations;

import org.jetbrains.annotations.ApiStatus;
import java.lang.annotation.*;

/**
* Functions annotated with {@code RequiresBlockingContext} are not designed to be called in suspend context
* (where {@code kotlinx.coroutines.currentCoroutineContext} is available).
* <p>
* A function should be annotated if there exists an analog of that function which is tailored for the suspending world.
* For example:
* <pre>{@code
* @RequiresBlockingContext
* public void writeActionBlocking(Runnable action) {
*   ...
* }
*
* // Kotlin equivalent: suspend fun writeAction(action: () -> Unit)
*
* // In Kotlin suspend context:
* // writeActionBlocking(() -> { ... }); // highlighted because the function is annotated
* // writeAction(() -> { ... });         // a proper function to call in suspend context
* }</pre>
* <p>
* The annotation shall not be propagated to outer functions by default.
* If there is no analog of the function for the suspending world,
* then don't annotate it.
*/
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
@ApiStatus.Experimental
public @interface RequiresBlockingContext {
}
