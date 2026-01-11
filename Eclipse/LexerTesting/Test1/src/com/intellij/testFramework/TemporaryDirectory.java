
// Her er koden oversatt til Java.
// Merk at enkelte Kotlin-spesifikke funksjoner (som Delegates.notNull og
// extension functions) er skrevet om til standard Java-mønstre.

//Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.testFramework;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.util.io.NioFiles;
import com.intellij.openapi.util.io.VfsUtilCore;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.common.ThreadUtil; // runAllCatching tilsvarende
import com.intellij.testFramework.common.TestFrameworkUtil;
import com.intellij.util.SmartList;
import com.intellij.util.io.Ksuid;
import com.intellij.util.io.sanitizeFileName;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class TemporaryDirectory extends ExternalResource {
 private final List<Path> paths = new SmartList<>();
 private String sanitizedName;
 private VirtualFile virtualFileRoot = null;
 private Path root = null;

 public static Path generateTemporaryPath(@NotNull String fileName) {
     return generateTemporaryPath(fileName, Paths.get(FileUtilRt.getTempDirectory()));
 }

 public static Path generateTemporaryPath(@NotNull String fileName, @NotNull Path root) {
     Path path = root.resolve(generateName(fileName));
     if (Files.exists(path)) {
         throw new IllegalStateException("Path " + path + " must be unique but already exists");
     }
     return path;
 }

 public static String testNameToFileName(@NotNull String name) {
     String trimmed = name.startsWith("test") ? name.substring(4).stripLeading() : name.stripLeading();
     return com.intellij.util.io.IOUtilKt.sanitizeFileName(trimmed, false, c -> c == ' ' || c == '\'');
 }

 @ApiStatus.Internal
 public static VirtualFile createVirtualFile(@NotNull VirtualFile parent, @NotNull String exactFileName, @Nullable String data) {
     try {
         return WriteAction.computeAndWait(() -> {
             VirtualFile result = parent.createChildData(TemporaryDirectory.class, exactFileName);
             if (data != null && !data.isEmpty()) {
                 result.setBinaryContent(data.getBytes(StandardCharsets.UTF_8));
             }
             return result;
         });
     } catch (IOException e) {
         throw new RuntimeException(e);
     }
 }

 @Override
 public Statement apply(Statement base, Description description) {
     String methodName = description.getMethodName();
     before(methodName != null ? methodName : description.getClassName());
     return super.apply(base, description);
 }

 protected void before(@NotNull String testName) {
     this.sanitizedName = testNameToFileName(testName);
     this.root = Paths.get(FileUtilRt.getTempDirectory());
 }

 @ApiStatus.Internal
 public void init(@NotNull String commonPrefix, @NotNull Path root) {
     if (this.root != null) {
         throw new IllegalStateException("Already initialized (root=" + this.root + ")");
     }
     this.sanitizedName = commonPrefix;
     this.root = root;
 }

 private VirtualFile getVirtualRoot() {
     if (virtualFileRoot == null) {
         if (root == null) throw new IllegalStateException("Not initialized");
         try {
             Files.createDirectories(root);
             virtualFileRoot = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(root);
             if (virtualFileRoot == null) {
                 throw new IllegalStateException("Cannot find virtual file by " + root);
             }
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }
     return virtualFileRoot;
 }

 @Override
 protected void after() {
     virtualFileRoot = null;
     root = null;
     if (paths.isEmpty()) return;

     List<Path> reversedPaths = new SmartList<>(paths);
     Collections.reverse(reversedPaths);

     // Forenklet feilhåndtering tilsvarende runAllCatching
     Throwable firstError = null;
     for (Path path : reversedPaths) {
         try {
             NioFiles.deleteRecursively(path);
         } catch (Throwable t) {
             if (firstError == null) firstError = t;
         }
     }

     paths.clear();
     if (firstError != null) {
         throw new RuntimeException(firstError);
     }
 }

 public Path newPath() {
     return newPath(null, false);
 }

 public Path newPath(@Nullable String fileName, boolean refreshVfs) {
     Path path = generatePath(fileName);
     if (refreshVfs) {
         refreshVfs(path);
     }
     return path;
 }

 public VirtualFile createVirtualFile() {
     return createVirtualFile(null, null);
 }

 public VirtualFile createVirtualFile(@Nullable String fileName, @Nullable String data) {
     VirtualFile result = createVirtualFile(getVirtualRoot(), generateName(fileName != null ? fileName : ""), data);
     paths.add(result.toNioPath());
     return result;
 }

 public VirtualFile createVirtualDir() {
     return createVirtualDir(null);
 }

 public VirtualFile createVirtualDir(@Nullable String dirName) {
     VirtualFile virtualRoot = getVirtualRoot();
     try {
         return WriteAction.computeAndWait(() -> {
             String name = generateName(dirName != null ? dirName : "");
             VirtualFile result = virtualRoot.createChildDirectory(TemporaryDirectory.class, name);
             paths.add(result.toNioPath());
             return result;
         });
     } catch (IOException e) {
         throw new RuntimeException(e);
     }
 }

 public Path createDir() {
     Path file = newPath();
     try {
         Files.createDirectories(file);
     } catch (IOException e) {
         throw new RuntimeException(e);
     }
     return file;
 }

 @Deprecated
 public void scheduleDelete(@NotNull Path path) {
     paths.add(path);
 }

 private Path generatePath(@Nullable String suffix) {
     String fileName = sanitizedName;
     if (suffix != null) {
         fileName = fileName.isEmpty() ? suffix : fileName + "_" + suffix;
     }

     if (root == null) throw new IllegalStateException("not initialized yet");
     Path path = generateTemporaryPath(fileName, root);
     paths.add(path);
     return path;
 }

 private static String generateName(@NotNull String fileName) {
     StringBuilder nameBuilder = new StringBuilder(fileName.length() + 1 + Ksuid.MAX_ENCODED_LENGTH);
     int extIndex = fileName.lastIndexOf('.');
     if (!fileName.isEmpty() && extIndex != 0) {
         if (extIndex == -1) {
             nameBuilder.append(fileName);
         } else {
             nameBuilder.append(fileName, 0, extIndex);
         }
         nameBuilder.append('_');
     }
     nameBuilder.append(Ksuid.generate());
     if (extIndex != -1) {
         nameBuilder.append(fileName, extIndex, fileName.length());
     }
     return nameBuilder.toString();
 }

 // Helper-metoder som erstatter extension functions
 public static void refreshVfs(Path path) {
     VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(path);
     if (virtualFile != null) {
         VfsUtil.markDirtyAndRefresh(false, true, true, virtualFile);
     }
 }
}

class TemporaryDirectoryExtension extends TemporaryDirectory implements BeforeEachCallback, AfterEachCallback {
 @Override
 public void afterEach(ExtensionContext context) {
     after();
 }

 @Override
 public void beforeEach(ExtensionContext context) {
     String testName = context.getTestMethod()
             .map(java.lang.reflect.Method::getName)
             .orElse(context.getDisplayName());
     before(testName);
 }
}
