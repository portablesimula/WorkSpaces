package com.intellij.lang;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ASTNode extends UserDataHolder {
    ASTNode[] EMPTY_ARRAY = new ASTNode[0];

    @NotNull IElementType getElementType();

    @NotNull String getText();

    @NotNull CharSequence getChars();

    boolean textContains(char var1);

    int getStartOffset();

    default int getStartOffsetInParent() {
        ASTNode parent = this.getTreeParent();
        return this.getStartOffset() - (parent == null ? 0 : parent.getStartOffset());
    }

    int getTextLength();

    TextRange getTextRange();

    ASTNode getTreeParent();

    ASTNode getFirstChildNode();

    ASTNode getLastChildNode();

    ASTNode getTreeNext();

    ASTNode getTreePrev();

    ASTNode @NotNull [] getChildren(@Nullable TokenSet var1);

    void addChild(@NotNull ASTNode var1);

    void addChild(@NotNull ASTNode var1, @Nullable ASTNode var2);

    void addLeaf(@NotNull IElementType var1, @NotNull CharSequence var2, @Nullable ASTNode var3);

    void removeChild(@NotNull ASTNode var1);

    void removeRange(@NotNull ASTNode var1, @Nullable ASTNode var2);

    void replaceChild(@NotNull ASTNode var1, @NotNull ASTNode var2);

    void replaceAllChildrenToChildrenOf(@NotNull ASTNode var1);

    void addChildren(@NotNull ASTNode var1, @Nullable ASTNode var2, @Nullable ASTNode var3);

    @NotNull Object clone();

    ASTNode copyElement();

    @Nullable ASTNode findLeafElementAt(int var1);

    <T> @Nullable T getCopyableUserData(@NotNull Key<T> var1);

    <T> void putCopyableUserData(@NotNull Key<T> var1, @Nullable T var2);

    @Nullable ASTNode findChildByType(@NotNull IElementType var1);

    @Nullable ASTNode findChildByType(@NotNull IElementType var1, @Nullable ASTNode var2);

    @Nullable ASTNode findChildByType(@NotNull TokenSet var1);

    @Nullable ASTNode findChildByType(@NotNull TokenSet var1, @Nullable ASTNode var2);

    PsiElement getPsi();

    <T extends PsiElement> T getPsi(@NotNull Class<T> var1);
}
