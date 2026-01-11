package com.intellij.lang;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public interface PsiParser {
    @NotNull ASTNode parse(@NotNull IElementType var1, @NotNull PsiBuilder var2);
}
