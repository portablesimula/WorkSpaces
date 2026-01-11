
// Her er koden oversatt fra Kotlin til Java.
// Siden Kotlin-koden bruker utvidselsesfunksjoner (extension functions) og et object (singleton),
// er disse i Java implementert som statiske metoder i en final class.
//
// Jeg har også inkludert nødvendige hjelpemetoder for å håndtere Kotlins standardfunksjonalitet som any, all og linkToActual.

//Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@ApiStatus.Experimental
public final class CharArrayUtilKmp {
 private static final int GET_CHARS_THRESHOLD = 32; // Antatt verdi basert på IntelliJ-standarder

 private CharArrayUtilKmp() {}

 @Nullable
 public static char[] fromSequenceWithoutCopying(@Nullable CharSequence seq) {
     if (seq instanceof CharSequenceBackedByArray) {
         return ((CharSequenceBackedByArray) seq).getChars();
     }
     return fromSequenceWithoutCopyingPlatformSpecific(seq);
 }

 public static boolean containLineBreaks(@Nullable CharSequence seq) {
     return containLineBreaks(seq, 0, seq != null ? seq.length() : 0);
 }

 public static boolean containLineBreaks(@Nullable CharSequence seq, int fromOffset, int endOffset) {
     if (seq == null) return false;
     for (int i = fromOffset; i < endOffset; i++) {
         char c = seq.charAt(i);
         if (c == '\n' || c == '\r') return true;
     }
     return false;
 }

 /**
  * @return the underlying char[] array if any, or a new char array if not
  */
 @NotNull
 public static char[] fromSequence(@NotNull CharSequence seq) {
     char[] underlying = fromSequenceWithoutCopying(seq);
     if (underlying != null) {
         return Arrays.copyOf(underlying, underlying.length);
     }
     return fromSequence(seq, 0, seq.length());
 }

 /**
  * @return a new char array containing the subsequence's chars
  */
 @NotNull
 public static char[] fromSequence(@NotNull CharSequence seq, int start, int end) {
     char[] result = new char[end - start];
     getChars(seq, result, start, 0, end - start);
     return result;
 }

 public static void getChars(@NotNull CharSequence seq, char[] dst, int dstOffset) {
     getChars(seq, dst, 0, dstOffset, seq.length());
 }

 public static void getChars(@NotNull CharSequence seq, char[] dst, int srcOffset, int dstOffset, int len) {
     if (seq instanceof CharArrayExternalizable) {
         ((CharArrayExternalizable) seq).getChars(srcOffset, srcOffset + len, dst, dstOffset);
         return;
     }

     if (len >= GET_CHARS_THRESHOLD) {
         if (seq instanceof String) {
             ((String) seq).getChars(srcOffset, srcOffset + len, dst, dstOffset);
             return;
         }
         if (seq instanceof CharSequenceBackedByArray) {
             ((CharSequenceBackedByArray) seq.subSequence(srcOffset, srcOffset + len)).getChars(dst, dstOffset);
             return;
         }
         if (seq instanceof StringBuilder) {
             ((StringBuilder) seq).getChars(srcOffset, srcOffset + len, dst, dstOffset);
             return;
         }

         if (getCharsPlatformSpecific(seq, srcOffset, dst, dstOffset, len)) {
             return;
         }
     }

     for (int i = 0, j = srcOffset; j < srcOffset + len && i + dstOffset < dst.length; i++, j++) {
         dst[i + dstOffset] = seq.charAt(j);
     }
 }

 public static int shiftForward(@NotNull CharSequence seq, @NotNull String chars, int startOffset) {
     return shiftForward(seq, chars, startOffset, seq.length());
 }

 public static int shiftForward(@NotNull CharSequence seq, @NotNull String chars, int startOffset, int endOffset) {
     int offset = startOffset;
     int limit = Math.min(endOffset, seq.length());
     while (offset < limit) {
         char c = seq.charAt(offset);
         int i = 0;
         while (i < chars.length()) {
             if (c == chars.charAt(i)) break;
             i++;
         }
         if (i >= chars.length()) {
             return offset;
         }
         offset++;
     }
     return endOffset;
 }

 public static int shiftBackward(char[] array, int offset, @NotNull String chars) {
     return shiftBackward(new CharArrayCharSequence(array), offset, chars);
 }

 public static int shiftBackward(@NotNull CharSequence seq, int offset, @NotNull String chars) {
     return shiftBackward(seq, 0, offset, chars);
 }

 public static int shiftBackward(@NotNull CharSequence seq, int minOffset, int maxOffset, @NotNull String chars) {
     if (maxOffset >= seq.length()) return maxOffset;

     int offset = maxOffset;
     while (true) {
         if (offset < minOffset) break;
         char c = seq.charAt(offset);
         int i = 0;
         while (i < chars.length()) {
             if (c == chars.charAt(i)) break;
             i++;
         }
         if (i == chars.length()) break;
         offset--;
     }
     return offset;
 }

 public static int shiftForwardUntil(@NotNull CharSequence seq, int offset, @NotNull String chars) {
     while (true) {
         if (offset >= seq.length()) break;
         char c = seq.charAt(offset);
         int i = 0;
         while (i < chars.length()) {
             if (c == chars.charAt(i)) break;
             i++;
         }
         if (i < chars.length()) break;
         offset++;
     }
     return offset;
 }

 public static int shiftBackwardUntil(@NotNull CharSequence seq, int offset, @NotNull String chars) {
     if (offset >= seq.length()) return offset;
     while (true) {
         if (offset < 0) break;
         char c = seq.charAt(offset);
         int i = 0;
         while (i < chars.length()) {
             if (c == chars.charAt(i)) break;
             i++;
         }
         if (i < chars.length()) break;
         offset--;
     }
     return offset;
 }

 public static boolean regionMatches(char[] array, int start, int end, @NotNull CharSequence s) {
     int len = s.length();
     if (start + len > end || start < 0) return false;
     for (int i = 0; i < len; i++) {
         if (array[start + i] != s.charAt(i)) return false;
     }
     return true;
 }

 public static boolean regionMatches(@NotNull CharSequence seq, int offset, @NotNull CharSequence s) {
     if (offset < 0 || offset + s.length() > seq.length()) return false;
     for (int i = 0; i < s.length(); i++) {
         if (seq.charAt(offset + i) != s.charAt(i)) return false;
     }
     return true;
 }

 public static boolean regionMatches(@NotNull CharSequence seq, int start, int end, @NotNull CharSequence s) {
     int len = s.length();
     if (start < 0 || start + len > end) return false;
     for (int i = 0; i < len; i++) {
         if (seq.charAt(start + i) != s.charAt(i)) return false;
     }
     return true;
 }

 // Platform-spesifikke implementasjoner (må kobles til faktiske Java-filer i prosjektet)
 private static boolean getCharsPlatformSpecific(CharSequence sequence, int srcOffset, char[] dst, int dstOffset, int len) {
     // Denne metoden tilsvarer linkToActual() i Kotlin
     return false; 
 }

 private static char[] fromSequenceWithoutCopyingPlatformSpecific(CharSequence seq) {
     // Denne metoden tilsvarer linkToActual() i Kotlin
     return null;
 }
}
