package com.intellij.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.model.psi.PsiSymbolDeclaration;
import com.intellij.model.psi.PsiSymbolReference;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.ArrayFactory;
import com.intellij.util.IncorrectOperationException;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;

public interface PsiElement extends UserDataHolder, Iconable {
    PsiElement[] EMPTY_ARRAY = new PsiElement[0];
    ArrayFactory<PsiElement> ARRAY_FACTORY = (count) -> count == 0 ? EMPTY_ARRAY : new PsiElement[count];

    @Contract(
        pure = true
    )
    @NotNull Project getProject() throws PsiInvalidElementAccessException;

    @Contract(
        pure = true
    )
    @NotNull Language getLanguage();

    @Contract(
        pure = true
    )
    PsiManager getManager();

    @Contract(
        pure = true
    )
    @NotNull PsiElement @NotNull [] getChildren();

    @Contract(
        pure = true
    )
    PsiElement getParent();

    @Contract(
        pure = true
    )
    PsiElement getFirstChild();

    @Contract(
        pure = true
    )
    PsiElement getLastChild();

    @Contract(
        pure = true
    )
    PsiElement getNextSibling();

    @Contract(
        pure = true
    )
    PsiElement getPrevSibling();

    @Contract(
        pure = true
    )
    PsiFile getContainingFile() throws PsiInvalidElementAccessException;

    @Contract(
        pure = true
    )
    TextRange getTextRange();

    @Contract(
        pure = true
    )
    default @NotNull TextRange getTextRangeInParent() {
        return TextRange.from(this.getStartOffsetInParent(), this.getTextLength());
    }

    @Contract(
        pure = true
    )
    int getStartOffsetInParent();

    @Contract(
        pure = true
    )
    int getTextLength();

    @Contract(
        pure = true
    )
    @Nullable PsiElement findElementAt(int var1);

    @Contract(
        pure = true
    )
    @Nullable PsiReference findReferenceAt(int var1);

    @Contract(
        pure = true
    )
    int getTextOffset();

    @Contract(
        pure = true
    )
    @NlsSafe String getText();

    @Contract(
        pure = true
    )
    char @NotNull [] textToCharArray();

    @Contract(
        pure = true
    )
    PsiElement getNavigationElement();

    @Contract(
        pure = true
    )
    PsiElement getOriginalElement();

    @Contract(
        pure = true
    )
    boolean textMatches(@NotNull @NonNls CharSequence var1);

    @Contract(
        pure = true
    )
    boolean textMatches(@NotNull PsiElement var1);

    @Contract(
        pure = true
    )
    boolean textContains(char var1);

    void accept(@NotNull PsiElementVisitor var1);

    void acceptChildren(@NotNull PsiElementVisitor var1);

    PsiElement copy();

    PsiElement add(@NotNull PsiElement var1) throws IncorrectOperationException;

    PsiElement addBefore(@NotNull PsiElement var1, @Nullable PsiElement var2) throws IncorrectOperationException;

    PsiElement addAfter(@NotNull PsiElement var1, @Nullable PsiElement var2) throws IncorrectOperationException;

    /** @deprecated */
    @Deprecated
    void checkAdd(@NotNull PsiElement var1) throws IncorrectOperationException;

    PsiElement addRange(PsiElement var1, PsiElement var2) throws IncorrectOperationException;

    PsiElement addRangeBefore(@NotNull PsiElement var1, @NotNull PsiElement var2, PsiElement var3) throws IncorrectOperationException;

    PsiElement addRangeAfter(PsiElement var1, PsiElement var2, PsiElement var3) throws IncorrectOperationException;

    void delete() throws IncorrectOperationException;

    /** @deprecated */
    @Deprecated
    void checkDelete() throws IncorrectOperationException;

    void deleteChildRange(PsiElement var1, PsiElement var2) throws IncorrectOperationException;

    PsiElement replace(@NotNull PsiElement var1) throws IncorrectOperationException;

    @Contract(
        pure = true
    )
    boolean isValid();

    @Contract(
        pure = true
    )
    boolean isWritable();

    @Experimental
    @OverrideOnly
    default @NotNull Collection<? extends @NotNull PsiSymbolDeclaration> getOwnDeclarations() {
        return Collections.emptyList();
    }

    @Experimental
    @OverrideOnly
    default @NotNull Collection<? extends @NotNull PsiSymbolReference> getOwnReferences() {
        return Collections.emptyList();
    }

    @Contract(
        pure = true
    )
    @Nullable PsiReference getReference();

    @Contract(
        pure = true
    )
    PsiReference @NotNull [] getReferences();

    @Contract(
        pure = true
    )
    <T> @Nullable T getCopyableUserData(@NotNull Key<T> var1);

    <T> void putCopyableUserData(@NotNull Key<T> var1, @Nullable T var2);

    boolean processDeclarations(@NotNull PsiScopeProcessor var1, @NotNull ResolveState var2, @Nullable PsiElement var3, @NotNull PsiElement var4);

    @Contract(
        pure = true
    )
    @Nullable PsiElement getContext();

    @Contract(
        pure = true
    )
    boolean isPhysical();

    @Contract(
        pure = true
    )
    @NotNull GlobalSearchScope getResolveScope();

    @Contract(
        pure = true
    )
    @NotNull SearchScope getUseScope();

    @Contract(
        pure = true
    )
    ASTNode getNode();

    @Contract(
        pure = true
    )
    @NonNls String toString();

    @Contract(
        pure = true
    )
    boolean isEquivalentTo(PsiElement var1);
}
