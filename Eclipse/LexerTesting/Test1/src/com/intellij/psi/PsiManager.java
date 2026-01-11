package com.intellij.psi;

import com.intellij.codeInsight.multiverse.CodeInsightContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import com.intellij.util.concurrency.annotations.RequiresReadLock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class PsiManager extends UserDataHolderBase {
    public static @NotNull PsiManager getInstance(@NotNull Project project) {
        return (PsiManager)project.getService(PsiManager.class);
    }

    public abstract @NotNull Project getProject();

    @RequiresReadLock
    @RequiresBackgroundThread(
        generateAssertion = false
    )
    public abstract @Nullable PsiFile findFile(@NotNull VirtualFile var1);

    /** @deprecated */
    @Deprecated
    @Internal
    @RequiresReadLock
    @RequiresBackgroundThread(
        generateAssertion = false
    )
    public abstract @Nullable PsiFile findFile(@NotNull VirtualFile var1, @NotNull CodeInsightContext var2);

    public abstract @Nullable FileViewProvider findViewProvider(@NotNull VirtualFile var1);

    /** @deprecated */
    @Deprecated
    @Internal
    @RequiresReadLock
    @RequiresBackgroundThread(
        generateAssertion = false
    )
    public abstract @Nullable FileViewProvider findViewProvider(@NotNull VirtualFile var1, @NotNull CodeInsightContext var2);

    @RequiresBackgroundThread(
        generateAssertion = false
    )
    public abstract @Nullable PsiDirectory findDirectory(@NotNull VirtualFile var1);

    public abstract boolean areElementsEquivalent(@Nullable PsiElement var1, @Nullable PsiElement var2);

    public abstract void reloadFromDisk(@NotNull PsiFile var1);

    /** @deprecated */
    @Deprecated
    public abstract void addPsiTreeChangeListener(@NotNull PsiTreeChangeListener var1);

    public abstract void addPsiTreeChangeListener(@NotNull PsiTreeChangeListener var1, @NotNull Disposable var2);

    public abstract void removePsiTreeChangeListener(@NotNull PsiTreeChangeListener var1);

    public abstract @NotNull PsiModificationTracker getModificationTracker();

    /** @deprecated */
    @Deprecated
    public abstract void startBatchFilesProcessingMode();

    /** @deprecated */
    @Deprecated
    public abstract void finishBatchFilesProcessingMode();

    public abstract <T> T runInBatchFilesMode(@NotNull Computable<T> var1);

    public abstract boolean isDisposed();

    public abstract void dropResolveCaches();

    @RequiresEdt
    public abstract void dropPsiCaches();

    public abstract boolean isInProject(@NotNull PsiElement var1);

    @Internal
    public abstract @Nullable FileViewProvider findCachedViewProvider(@NotNull VirtualFile var1);
}
