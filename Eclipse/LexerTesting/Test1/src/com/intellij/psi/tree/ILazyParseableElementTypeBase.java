package com.intellij.psi.tree;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public interface ILazyParseableElementTypeBase {
    ASTNode parseContents(@NotNull ASTNode var1);

    default boolean reuseCollapsedTokens() {
        return false;
    }
}
