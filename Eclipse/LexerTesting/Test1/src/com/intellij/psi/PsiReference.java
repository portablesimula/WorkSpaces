package com.intellij.psi;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ArrayFactory;
import com.intellij.util.ArrayUtilRt;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PsiReference {
    PsiReference[] EMPTY_ARRAY = new PsiReference[0];
    ArrayFactory<PsiReference> ARRAY_FACTORY = (count) -> count == 0 ? EMPTY_ARRAY : new PsiReference[count];

    @NotNull PsiElement getElement();

    @NotNull TextRange getRangeInElement();

    default @NotNull TextRange getAbsoluteRange() {
        return this.getRangeInElement().shiftRight(this.getElement().getTextRange().getStartOffset());
    }

    @Nullable PsiElement resolve();

    @NotNull @NlsSafe String getCanonicalText();

    PsiElement handleElementRename(@NotNull String var1) throws IncorrectOperationException;

    PsiElement bindToElement(@NotNull PsiElement var1) throws IncorrectOperationException;

    boolean isReferenceTo(@NotNull PsiElement var1);

    default Object @NotNull [] getVariants() {
        Object[] var10000 = ArrayUtilRt.EMPTY_OBJECT_ARRAY;
        if (var10000 == null) {
            $$$reportNull$$$0(1);
        }

        return var10000;
    }

    boolean isSoft();
}
