//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.psi.impl.source.tree;

import com.intellij.lang.ASTFactory;
import com.intellij.openapi.util.NlsContexts.DetailedDescription;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.DummyHolder;
import com.intellij.psi.impl.source.DummyHolderFactory;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.CharTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Factory {
    private Factory() {
    }

    public static @NotNull LeafElement createSingleLeafElement(@NotNull IElementType type, @NotNull CharSequence buffer, int startOffset, int endOffset, CharTable table, @NotNull PsiManager manager, PsiFile originalFile) {
        if (manager == null) {
            $$$reportNull$$$0(2);
        }

        DummyHolder dummyHolder = DummyHolderFactory.createHolder(manager, table, type.getLanguage());
        dummyHolder.setOriginalFile(originalFile);
        FileElement holderElement = dummyHolder.getTreeElement();
        LeafElement newElement = ASTFactory.leaf(type, holderElement.getCharTable().intern(buffer, startOffset, endOffset));
        holderElement.rawAddChildren(newElement);
        CodeEditUtil.setNodeGenerated(newElement, true);
        return newElement;
    }

    public static @NotNull LeafElement createSingleLeafElement(@NotNull IElementType type, @NotNull CharSequence buffer, int startOffset, int endOffset, CharTable table, @NotNull PsiManager manager, boolean generatedFlag) {
        if (manager == null) {
            $$$reportNull$$$0(6);
        }

        FileElement holderElement = DummyHolderFactory.createHolder(manager, table, type.getLanguage()).getTreeElement();
        LeafElement newElement = ASTFactory.leaf(type, holderElement.getCharTable().intern(buffer, startOffset, endOffset));
        holderElement.rawAddChildren(newElement);
        if (generatedFlag) {
            CodeEditUtil.setNodeGenerated(newElement, true);
        }

        return newElement;
    }

    public static @NotNull LeafElement createSingleLeafElement(@NotNull IElementType type, @NotNull CharSequence buffer, CharTable table, @NotNull PsiManager manager) {
        if (manager == null) {
            $$$reportNull$$$0(10);
        }

        return createSingleLeafElement(type, buffer, 0, buffer.length(), table, manager);
    }

    public static @NotNull LeafElement createSingleLeafElement(@NotNull IElementType type, @NotNull CharSequence buffer, int startOffset, int endOffset, @Nullable CharTable table, @NotNull PsiManager manager) {
        if (manager == null) {
            $$$reportNull$$$0(13);
        }

        return createSingleLeafElement(type, buffer, startOffset, endOffset, table, manager, true);
    }

    public static @NotNull CompositeElement createErrorElement(@NotNull @DetailedDescription String description) {
        return new PsiErrorElementImpl(description);
    }

    public static @NotNull CompositeElement createCompositeElement(@NotNull IElementType type, CharTable charTableByTree, @NotNull PsiManager manager) {
        FileElement treeElement = DummyHolderFactory.createHolder(manager, (PsiElement)null, charTableByTree).getTreeElement();
        CompositeElement composite = ASTFactory.composite(type);
        treeElement.rawAddChildren(composite);
        return composite;
    }
}
