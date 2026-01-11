package com.intellij.lang;

import com.intellij.psi.tree.IElementType;

public interface LighterASTNode {
    LighterASTNode[] EMPTY_ARRAY = new LighterASTNode[0];

    IElementType getTokenType();

    int getStartOffset();

    int getEndOffset();

    default int getTextLength() {
        return this.getEndOffset() - this.getStartOffset();
    }
}
