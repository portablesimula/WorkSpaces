package com.intellij.lexer;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class LexerPositionImpl implements LexerPosition {
    private final int myOffset;
    private final int myState;

    public LexerPositionImpl(int offset, int state) {
        this.myOffset = offset;
        this.myState = state;
    }

    public int getOffset() {
        return this.myOffset;
    }

    public int getState() {
        return this.myState;
    }
}
