
// Her er koden oversatt fra Kotlin til Java:

//Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.text;

import com.intellij.openapi.util.text.CharSequenceWithStringHash;
import com.intellij.openapi.util.text.StringHash; // Tilsvarer stringHashCode i Kotlin-eksempelet
import com.intellij.util.text.CharArrayUtil; // Antatt lokasjon for regionMatches i Java
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CharArrayCharSequence implements CharSequenceBackedByArray, CharSequenceWithStringHash {

 protected final char[] myChars;
 protected final int myStart;
 protected final int myEnd;

 private transient int hash = 0;

 public CharArrayCharSequence(char... chars) {
     this(chars, 0, chars.length);
 }

 public CharArrayCharSequence(char[] chars, int start, int end) {
     if (start < 0 || end > chars.length || start > end) {
         throw new IndexOutOfBoundsException("chars.length:" + chars.length + ", start:" + start + ", end:" + end);
     }
     this.myChars = chars;
     this.myStart = start;
     this.myEnd = end;
 }

 @Override
 public int length() {
     return myEnd - myStart;
 }

 @Override
 public char charAt(int index) {
     if (index < 0 || index >= length()) {
         throw new IndexOutOfBoundsException(String.valueOf(index));
     }
     return myChars[index + myStart];
 }

 @NotNull
 @Override
 public CharSequence subSequence(int start, int end) {
     if (start == 0 && end == length()) {
         return this;
     }
     return new CharArrayCharSequence(myChars, myStart + start, myStart + end);
 }

 @NotNull
 @Override
 public String toString() {
     return new String(myChars, myStart, length());
 }

 @Override
 public char[] getChars() {
     if (myStart == 0 && myEnd == myChars.length) {
         return myChars;
     }
     char[] chars = new char[length()];
     getChars(chars, 0);
     return chars;
 }

 @Override
 public void getChars(char[] dst, int dstOffset) {
     System.arraycopy(myChars, myStart, dst, dstOffset, length());
 }

 @Override
 public boolean equals(Object anObject) {
     if (this == anObject) {
         return true;
     }
     if (anObject == null || getClass() != anObject.getClass()) {
         return false;
     }
     CharSequence that = (CharSequence) anObject;
     if (length() != that.length()) {
         return false;
     }
     // Bruker IntelliJ utility for region-sammenligning
     return CharArrayUtil.regionMatches(myChars, myStart, myEnd, that);
 }

 /**
  * See {@link java.io.Reader#read(char[], int, int)}
  */
 public int readCharsTo(int start, char[] cbuf, int off, int len) {
     int readChars = Math.min(len, length() - start);
     if (readChars <= 0) {
         return -1;
     }

     System.arraycopy(myChars, myStart + start, cbuf, off, readChars);
     return readChars;
 }

 @Override
 public int hashCode() {
     int h = hash;
     if (h == 0 && length() > 0) {
         // Tilsvarer extension function stringHashCode i Kotlin
         h = StringHash.hashCode(myChars, myStart, myEnd);
         hash = h;
     }
     return h;
 }
}
