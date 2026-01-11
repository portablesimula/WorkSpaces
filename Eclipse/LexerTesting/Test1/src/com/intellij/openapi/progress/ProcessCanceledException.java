
// Her er Kotlin-koden oversatt til Java.
// Siden Java ikke har innebygd støtte for kotlin.coroutines.cancellation.CancellationException,
// brukes vanligvis java.util.concurrent.CancellationException i IntelliJ-plattformen for Java-kompatibilitet. 
//Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package com.intellij.openapi.progress;

import com.intellij.openapi.diagnostic.ControlFlowException;
import java.util.concurrent.CancellationException;
import org.jetbrains.annotations.Nullable;

/**
* An exception indicating that the currently running operation was terminated and should finish as soon as possible.
* <p>
* Usually, this exception should not be caught, swallowed, logged, or handled in any way.
* Instead, it should be rethrown so that the infrastructure can handle it correctly.
* <p>
* This exception can happen during almost any IDE activity, e.g. any PSI query,
* {@link com.intellij.openapi.extensions.ExtensionPointName#getExtensions()},
* {@link com.intellij.openapi.actionSystem.AnAction#update}, etc.
*
* @see com.intellij.openapi.progress.ProgressIndicator#checkCanceled
* @see <a href="https://plugins.jetbrains.com/docs/intellij/threading-model.html">Threading Model</a>
*/
public class ProcessCanceledException extends CancellationException implements ControlFlowException {
private final Throwable myCause;

public ProcessCanceledException() {
 super();
 myCause = null;
}

public ProcessCanceledException(Throwable cause) {
 super(cause == null ? null : cause.toString());
 if (cause instanceof ProcessCanceledException) {
   throw new IllegalArgumentException("Must not self-wrap ProcessCanceledException: ", cause);
 }
 myCause = cause;
}

protected ProcessCanceledException(String message) {
 super(message);
 myCause = null;
}

@Override
@Nullable
public Throwable getCause() {
 return myCause;
}
}
