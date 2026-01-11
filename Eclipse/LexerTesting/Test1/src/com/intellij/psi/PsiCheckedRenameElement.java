package com.intellij.psi;

import com.intellij.util.IncorrectOperationException;

public interface PsiCheckedRenameElement extends PsiNamedElement {
    void checkSetName(String var1) throws IncorrectOperationException;
}
