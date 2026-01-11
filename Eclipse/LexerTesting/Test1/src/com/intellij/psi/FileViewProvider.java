package com.intellij.psi;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.UserDataHolderEx;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public interface FileViewProvider extends Cloneable, UserDataHolderEx {
    @NotNull PsiManager getManager();

    Document getDocument();

    @NotNull CharSequence getContents();

    @NotNull VirtualFile getVirtualFile();

    @NotNull Language getBaseLanguage();

    @NotNull @Unmodifiable Set<Language> getLanguages();

    default boolean hasLanguage(@NotNull Language language) {
        return this.getLanguages().contains(language);
    }

    PsiFile getPsi(@NotNull Language var1);

    @NotNull @Unmodifiable List<@NotNull PsiFile> getAllFiles();

    boolean isEventSystemEnabled();

    boolean isPhysical();

    long getModificationStamp();

    boolean supportsIncrementalReparse(@NotNull Language var1);

    void rootChanged(@NotNull PsiFile var1);

    void beforeContentsSynchronized();

    void contentsSynchronized();

    FileViewProvider clone();

    @Nullable PsiElement findElementAt(int var1);

    @Nullable PsiReference findReferenceAt(int var1);

    @Nullable PsiElement findElementAt(int var1, @NotNull Language var2);

    @Nullable PsiElement findElementAt(int var1, @NotNull Class<? extends Language> var2);

    @Nullable PsiReference findReferenceAt(int var1, @NotNull Language var2);

    @NotNull FileViewProvider createCopy(@NotNull VirtualFile var1);

    @NotNull PsiFile getStubBindingRoot();

    @NotNull FileType getFileType();
}
