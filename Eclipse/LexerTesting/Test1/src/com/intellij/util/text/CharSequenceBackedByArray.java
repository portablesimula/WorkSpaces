
// Her er koden oversatt til Java.
// Merk at Kotlin-egenskapen chars i et interface oversettes til en get-metode i Java: 

//Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
* A char sequence based on a char array. May be used for performance optimizations.
*
* @author Maxim.Mossienko
* @see CharArrayExternalizable
*
* @see CharArrayUtil#getChars(CharSequence) 
* @see CharArrayUtil#fromSequenceWithoutCopying(CharSequence)
*/
@ApiStatus.Internal
public interface CharSequenceBackedByArray extends CharSequence {

/**
* NOT guaranteed to return the array of the length of the original charSequence.length() - 
* may be more for performance reasons.
*/
@NotNull
char[] getChars();

void getChars(@NotNull char[] dst, int dstOffset);
}
