package com.intellij.psi;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.PsiElementProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PsiFileSystemItem extends PsiCheckedRenameElement, NavigatablePsiElement {
    boolean isDirectory();

    @Nullable PsiFileSystemItem getParent();

    VirtualFile getVirtualFile();

    @NotNull @NlsSafe String getName();

    boolean processChildren(@NotNull PsiElementProcessor<? super PsiFileSystemItem> var1);
}
