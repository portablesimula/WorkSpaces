package com.intellij.lang;

import com.intellij.openapi.util.NlsContexts.DetailedDescription;
import com.intellij.openapi.util.NlsContexts.ParsingError;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.List;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SyntaxTreeBuilder {
    @NotNull CharSequence getOriginalText();

    void advanceLexer();

    @Nullable IElementType getTokenType();

    void setTokenTypeRemapper(@Nullable ITokenTypeRemapper var1);

    void remapCurrentToken(@NotNull IElementType var1);

    void setWhitespaceSkippedCallback(@Nullable WhitespaceSkippedCallback var1);

    @Nullable IElementType lookAhead(int var1);

    @Nullable IElementType rawLookup(int var1);

    int rawTokenTypeStart(int var1);

    int rawTokenIndex();

    @NonNls @Nullable String getTokenText();

    default void rawAdvanceLexer(int steps) {
        if (steps < 0) {
            throw new IllegalArgumentException("Steps must be a positive integer - lexer can only be advanced. Use Marker.rollbackTo if you want to rollback PSI building.");
        } else if (steps != 0) {
            int offset = this.rawTokenTypeStart(steps);

            while(!this.eof() && this.getCurrentOffset() < offset) {
                this.advanceLexer();
            }

        }
    }

    int getCurrentOffset();

    @NotNull Marker mark();

    void error(@NotNull @ParsingError String var1);

    boolean eof();

    void setDebugMode(boolean var1);

    void enforceCommentTokens(@NotNull TokenSet var1);

    @Nullable LighterASTNode getLatestDoneMarker();

    default @NotNull List<? extends Production> getProductions() {
        throw new UnsupportedOperationException("not implemented for this kind of Builder");
    }

    default boolean isWhitespaceOrComment(@NotNull IElementType elementType) {
        return false;
    }

    public interface Production extends LighterASTNode {
        default IElementType getTokenType() {
            throw new UnsupportedOperationException("not implemented for this kind of markers");
        }

        default int getStartOffset() {
            throw new UnsupportedOperationException("not implemented for this kind of markers");
        }

        default int getEndOffset() {
            throw new UnsupportedOperationException("not implemented for this kind of markers");
        }

        default int getStartIndex() {
            throw new UnsupportedOperationException("not implemented for this kind of markers");
        }

        default int getEndIndex() {
            throw new UnsupportedOperationException("not implemented for this kind of markers");
        }

        default @DetailedDescription @Nullable String getErrorMessage() {
            return null;
        }

        default boolean isCollapsed() {
            return false;
        }
    }

    public interface Marker extends Production {
        @NotNull Marker precede();

        void drop();

        void rollbackTo();

        void done(@NotNull IElementType var1);

        void collapse(@NotNull IElementType var1);

        void doneBefore(@NotNull IElementType var1, @NotNull Marker var2);

        void doneBefore(@NotNull IElementType var1, @NotNull Marker var2, @NotNull @ParsingError String var3);

        void error(@NotNull @ParsingError String var1);

        void errorBefore(@NotNull @ParsingError String var1, @NotNull Marker var2);

        void setCustomEdgeTokenBinders(@Nullable WhitespacesAndCommentsBinder var1, @Nullable WhitespacesAndCommentsBinder var2);
    }
}
