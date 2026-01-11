package com.intellij.lexer;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Lexer {
//    private static final Logger LOG = Logger.getInstance(Lexer.class);
    private static final long LEXER_START_THRESHOLD = 500L;

    public abstract void start(@NotNull CharSequence var1, int var2, int var3, int var4);

    private void startMeasured(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
//        if (!LOG.isDebugEnabled()) {
//            this.start(buffer, startOffset, endOffset, initialState);
//        } else {
            long start = System.currentTimeMillis();
            this.start(buffer, startOffset, endOffset, initialState);
            long startDuration = System.currentTimeMillis() - start;
//            if (startDuration > 500L) {
//                LOG.debug("Starting lexer took: ", new Object[]{startDuration, "; at ", startOffset, " - ", endOffset, "; state: ", initialState, "; text: ", StringUtil.shortenTextWithEllipsis(buffer.toString(), 1024, 500)});
//            }

        }
//    }

    public final void start(@NotNull CharSequence buf, int start, int end) {
        this.startMeasured(buf, start, end, 0);
    }

    public final void start(@NotNull CharSequence buf) {
        this.startMeasured(buf, 0, buf.length(), 0);
    }

    public @NotNull CharSequence getTokenSequence() {
        return this.getBufferSequence().subSequence(this.getTokenStart(), this.getTokenEnd());
    }

    public @NotNull String getTokenText() {
        return this.getTokenSequence().toString();
    }

    public abstract int getState();

    public abstract @Nullable IElementType getTokenType();

    public abstract int getTokenStart();

    public abstract int getTokenEnd();

    public abstract void advance();

    public abstract @NotNull LexerPosition getCurrentPosition();

    public abstract void restore(@NotNull LexerPosition var1);

    public abstract @NotNull CharSequence getBufferSequence();

    public abstract int getBufferEnd();
}
