
// Her er koden oversatt til Java.
//Siden Kotlin-koden bruker @JvmOverloads, er den oversatt med flere konstruktører for å beholde den samme kompatibiliteten i Java.

//Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.platform.testFramework.core;

import com.intellij.rt.execution.junit.FileComparisonData;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.FileInfo;
import org.opentest4j.ValueWrapper;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class FileComparisonFailedError extends AssertionFailedError implements FileComparisonData {

 public FileComparisonFailedError(String message, String expected, String actual) {
     this(message, expected, actual, null, null);
 }

 public FileComparisonFailedError(String message, String expected, String actual, String expectedFilePath) {
     this(message, expected, actual, expectedFilePath, null);
 }

 public FileComparisonFailedError(
         String message,
         String expected,
         String actual,
         String expectedFilePath,
         String actualFilePath
 ) {
     super(
             message,
             createFileInfo(expected, expectedFilePath),
             createFileInfo(actual, actualFilePath)
     );

     if (!(expectedFilePath == null || new File(expectedFilePath).isFile())) {
         throw new IllegalArgumentException("'expectedFilePath' should point to the existing file or be null");
     }
     if (!(actualFilePath == null || new File(actualFilePath).isFile())) {
         throw new IllegalArgumentException("'actualFilePath' should point to the existing file or be null");
     }
 }

 @Override
 public String getFilePath() {
     return getFilePath(getExpected());
 }

 @Override
 public String getActualFilePath() {
     return getFilePath(getActual());
 }

 @Override
 public String getActualStringPresentation() {
     return getFileText(getActual());
 }

 @Override
 public String getExpectedStringPresentation() {
     return getFileText(getExpected());
 }

 private static ValueWrapper createFileInfo(String text, String path) {
     byte[] contents = text.getBytes(StandardCharsets.UTF_8);
     if (path == null) {
         return ValueWrapper.create(text);
     } else {
         FileInfo fileInfo = new PresentableFileInfo(path, contents);
         return ValueWrapper.create(fileInfo);
     }
 }

 private static String getFileText(ValueWrapper valueWrapper) {
     Object value = valueWrapper.getValue();
     if (value instanceof FileInfo) {
         return ((FileInfo) value).getContentsAsString(StandardCharsets.UTF_8);
     } else {
         return (String) value;
     }
 }

 private static String getFilePath(ValueWrapper valueWrapper) {
     Object value = valueWrapper.getValue();
     if (value instanceof FileInfo) {
         return ((FileInfo) value).getPath();
     }
     return null;
 }

 private static class PresentableFileInfo extends FileInfo {
     public PresentableFileInfo(String path, byte[] contents) {
         super(path, contents);
     }

     @Override
     public String toString() {
         return getContentsAsString(StandardCharsets.UTF_8);
     }
 }
}
