
// Her er koden oversatt til Java:

//Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.platform.backend.navigation;

import com.intellij.codeInsight.multiverse.CodeInsightContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresReadLock;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

/**
* This interface isn't supposed to be used from plugins, call methods from NavigationRequest instead.
*/
@Internal
public interface NavigationRequests {

 static NavigationRequests getInstance() {
     return ApplicationManager.getApplication().getService(NavigationRequests.class);
 }

 @RequiresReadLock
 @RequiresBackgroundThread
 @Nullable
 NavigationRequest sourceNavigationRequest(Project project, VirtualFile file, int offset, @Nullable TextRange elementRange);

 // todo IJPL-339 design request
 @RequiresReadLock
 @RequiresBackgroundThread
 @Nullable
 NavigationRequest sharedSourceNavigationRequest(Project project, VirtualFile file, CodeInsightContext context, int offset, @Nullable TextRange elementRange);

 @RequiresReadLock
 @RequiresBackgroundThread
 @Nullable
 NavigationRequest directoryNavigationRequest(PsiDirectory directory);

 /**
  * An adapted version of {@link com.intellij.ide.util.EditSourceUtil#getDescriptor}.
  */
 @Internal
 @Deprecated
 @RequiresReadLock
 @RequiresBackgroundThread
 @Nullable
 NavigationRequest psiNavigationRequest(PsiElement element);

 /**
  * @return a request to execute an {@link Navigatable#navigate arbitrary code},
  * or {@code null} if the navigation is not possible for any reason
  */
 @Internal
 @Deprecated
 @RequiresReadLock
 @RequiresBackgroundThread
 @Nullable
 NavigationRequest rawNavigationRequest(Navigatable navigatable);
}
