package com.intellij.psi;

import com.intellij.lang.FileASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

public interface PsiFile extends PsiFileSystemItem {
    PsiFile[] EMPTY_ARRAY = new PsiFile[0];

    VirtualFile getVirtualFile();

    PsiDirectory getContainingDirectory();

    PsiDirectory getParent();

    long getModificationStamp();

    @NotNull PsiFile getOriginalFile();

    @NotNull FileType getFileType();

    /** @deprecated */
    @Deprecated
    PsiFile @NotNull [] getPsiRoots();

    @NotNull FileViewProvider getViewProvider();

    @NonExtendable
    default @NotNull Document getFileDocument() {
        Document document = this.getViewProvider().getDocument();
        if (document == null) {
            throw new UnsupportedOperationException("No document is available for file " + this.getClass() + "; virtualFile: " + PsiUtilCore.getVirtualFile(this));
        } else {
            return document;
        }
    }

    FileASTNode getNode();

    void subtreeChanged();

    default void clearCaches() {
    }

    default @Nullable IFileElementType getFileElementType() {
        return (IFileElementType)ObjectUtils.tryCast(PsiUtilCore.getElementType(this.getNode()), IFileElementType.class);
    }
}
