// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.application;

import com.intellij.openapi.diagnostic.Logger;

import testing.util.LOG;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public final class PathManagerEx {
    private PathManagerEx() {
    }

    /**
     * Absolute canonical path to system cache dir.
     */
    @NotNull
    public static Path getAppSystemDir() {
        Path path = PathManager.getSystemDir();
        try {
            return path.toRealPath();
        }
        catch (NoSuchFileException ignore) {
        }
        catch (IOException e) {
//            Logger.getInstance(PathManager.class).warn(e);
            LOG.warn(e);
        }
        return path;
    }
    
// Kotlin Code:    
 // Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
//    @file:JvmName("PathManagerEx")
//    package com.intellij.openapi.application
//
//    import com.intellij.openapi.diagnostic.logger
//    import java.io.IOException
//    import java.nio.file.NoSuchFileException
//    import java.nio.file.Path
//
//    /**
//     * Absolute canonical path to system cache dir.
//     */
//    val appSystemDir: Path
//      get() {
//        val path = PathManager.getSystemDir()
//        try {
//          return path.toRealPath()
//        }
//        catch (ignore: NoSuchFileException) {
//        }
//        catch (e: IOException) {
//          logger<PathManager>().warn(e)
//        }
//        return path
//      }    
}
