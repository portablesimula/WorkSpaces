package com.intellij.lexer;

import org.jetbrains.annotations.NotNull;

public abstract class LexerBase extends Lexer {
    public @NotNull LexerPosition getCurrentPosition() {
        int offset = this.getTokenStart();
        int intState = this.getState();
        return new LexerPositionImpl(offset, intState);
    }

    public void restore(@NotNull LexerPosition position) {
        this.start(this.getBufferSequence(), position.getOffset(), this.getBufferEnd(), position.getState());
    }
}
