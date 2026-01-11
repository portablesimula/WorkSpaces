package com.intellij.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public interface TokenIterator {
    int getStartOffset(int var1);

    int getEndOffset(int var1);

    @NotNull IElementType getType(int var1);

    int getState(int var1);

    int getTokenCount();

    int initialTokenIndex();
}