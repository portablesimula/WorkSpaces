
// Her er koden oversatt til Java:
	
//Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.platform.syntax.impl.builder;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

/**
* Interface describes a probe that can be set to the {@link SyntaxBuildingDiagnosticsKt#DIAGNOSTICS} for processing different
* building events, e.g. parser rollbacks.
*/
@ApiStatus.Experimental
public interface SyntaxBuildingDiagnostics {
 /**
  * Invoked on builder creation
  * @param charLength length of the text to parse in characters
  * @param tokensLength length of the text to parse in tokens
  */
 void registerPass(int charLength, int tokensLength);

 /**
  * Invoked on marker rollback
  * @param tokens number of tokens rolled back with this marker
  */
 void registerRollback(int tokens);
}

@ApiStatus.Experimental
interface DiagnosticAwareBuilder {
 long getLexingTimeNs();
}

/**
* Hjelpeklasse for å håndtere globale variabler og statiske metoder fra Kotlin-filen.
*/
@ApiStatus.Experimental
class SyntaxBuildingDiagnosticsKt {
 
 @Nullable
 private static SyntaxBuildingDiagnostics DIAGNOSTICS = null;

 @Nullable
 public static SyntaxBuildingDiagnostics getDIAGNOSTICS() {
     return DIAGNOSTICS;
 }

 public static void setDIAGNOSTICS(@Nullable SyntaxBuildingDiagnostics diagnostics) {
     DIAGNOSTICS = diagnostics;
 }

 @ApiStatus.Experimental
 public static <T> T computeWithDiagnostics(@Nullable SyntaxBuildingDiagnostics diagnostics, Supplier<T> block) {
     SyntaxBuildingDiagnostics oldDiagnostics = DIAGNOSTICS;
     try {
         DIAGNOSTICS = diagnostics;
         return block.get();
     } finally {
         DIAGNOSTICS = oldDiagnostics;
     }
 }
}
