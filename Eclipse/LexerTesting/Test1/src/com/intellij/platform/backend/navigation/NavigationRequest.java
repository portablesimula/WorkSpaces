
// Her er koden oversatt fra Kotlin til Java.
// I Java flyttes innholdet i companion object til static metoder direkte i interfacet
// (siden Java 8+ tillater dette).

//Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.platform.backend.navigation;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresReadLock;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.Nullable;

/**
* Use static functions to create instances.
*
* @see #sourceNavigationRequest(Project, VirtualFile, int)
*/
@Experimental
@NonExtendable
public interface NavigationRequest {

 /**
  * @return a request for the navigation to a specified [offset] in a [file],
  * or {@code null} if the navigation is not possible for any reason
  */
 @RequiresReadLock
 @RequiresBackgroundThread
 @Nullable
 static NavigationRequest sourceNavigationRequest(Project project, VirtualFile file, int offset) {
     return NavigationRequests.getInstance().sourceNavigationRequest(project, file, offset, null);
 }

 /**
  * @param elementRange is used to determine whether
  * to preserve the caret if [preserveCaret] is set
  * @return a request for the navigation to the start offset of [elementRange],
  * or {@code null} if the navigation is not possible for any reason
  */
 @RequiresReadLock
 @RequiresBackgroundThread
 @Nullable
 static NavigationRequest sourceNavigationRequest(PsiFile file, TextRange elementRange) {
     VirtualFile virtualFile = file.getVirtualFile();
     if (virtualFile == null) {
         return null;
     }
     return NavigationRequests.getInstance().sourceNavigationRequest(
             file.getProject(),
             virtualFile,
             elementRange.getStartOffset(),
             elementRange
     );
 }

 /**
  * @return a request for the navigation to a specified [directory],
  * or {@code null} if the navigation is not possible for any reason
  */
 @RequiresReadLock
 @RequiresBackgroundThread
 @Nullable
 static NavigationRequest directoryNavigationRequest(PsiDirectory directory) {
     return NavigationRequests.getInstance().directoryNavigationRequest(directory);
 }
}
