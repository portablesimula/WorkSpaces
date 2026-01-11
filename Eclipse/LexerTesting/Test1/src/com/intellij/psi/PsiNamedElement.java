package com.intellij.psi;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PsiNamedElement extends PsiElement {
    PsiNamedElement[] EMPTY_ARRAY = new PsiNamedElement[0];

    @Nullable @NlsSafe String getName();

    PsiElement setName(@NlsSafe @NotNull String var1) throws IncorrectOperationException;
}
