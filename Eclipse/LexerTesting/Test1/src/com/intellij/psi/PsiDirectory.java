package com.intellij.psi;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import java.util.Arrays;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PsiDirectory extends PsiFileSystemItem {
    PsiDirectory[] EMPTY_ARRAY = new PsiDirectory[0];

    @NotNull VirtualFile getVirtualFile();

    @NotNull String getName();

    @NotNull PsiElement setName(@NotNull String var1) throws IncorrectOperationException;

    @Nullable PsiDirectory getParentDirectory();

    @Nullable PsiDirectory getParent();

    PsiDirectory @NotNull [] getSubdirectories();

    PsiFile @NotNull [] getFiles();

    default PsiFile @NotNull [] getFiles(@NotNull GlobalSearchScope scope) {
        PsiFile[] result = (PsiFile[])Arrays.stream(this.getFiles()).filter((psiFile) -> scope.contains(psiFile.getVirtualFile())).toArray((x$0) -> new PsiFile[x$0]);
        PsiFile[] var10000 = result.length == 0 ? PsiFile.EMPTY_ARRAY : result;
        if (var10000 == null) {
            //$$$reportNull$$$0(1);
        }

        return var10000;
    }

    @Nullable PsiDirectory findSubdirectory(@NonNls @NotNull String var1);

    @Nullable PsiFile findFile(@NotNull @NonNls String var1);

    @NotNull PsiDirectory createSubdirectory(@NotNull String var1) throws IncorrectOperationException;

    void checkCreateSubdirectory(@NotNull String var1) throws IncorrectOperationException;

    @NotNull PsiFile createFile(@NotNull @NonNls String var1) throws IncorrectOperationException;

    @NotNull PsiFile copyFileFrom(@NotNull String var1, @NotNull PsiFile var2) throws IncorrectOperationException;

    void checkCreateFile(@NotNull String var1) throws IncorrectOperationException;
}
