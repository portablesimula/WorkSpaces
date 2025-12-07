package simula.plugin.extensions.highlighterExtension.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.lexer.LexerPosition;
import com.intellij.psi.tree.IElementType;
import simula.plugin.extensions.lang.scanner.TestToken;
import simula.plugin.extensions.lang.scanner.Tester;
import simula.plugin.util.Global;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimulaLexer_draft1 extends LexerBase {

//    CharSequence buffer; // character data for lexing.
//    int startOffset;     // offset to start lexing from
//    int endOffset;       // offset to stop lexing at
    int state;           // the initial state of the lexer.

    TestToken current;

    public SimulaLexer_draft1() {
        System.out.println("NEW SimulaLexer: ");
        Global.popUpMessage("NEW SimulaLexer:");
//        throw new RuntimeException("NEW SimulaLexer: start="+startOffset+", end="+endOffset+", state="+state+", chars=\""+buffer+'"');
        this.current = Tester.nullToken();
    }

    /*
    BTW, there is an ambiguity in the API documentation.  For method Lexer.start(), it says:

        * @param endOffset    offset to stop lexing at

    But I have found that I expected it to be the last good offset when in fact it is the first bad offset ;).
    In other words it is the length of the buffer not the last offset.
    it is not incorrect, just ambiguous.
    It should say something like "The last valid index + 1" or something, which is awkward but unambiguous.
    I would also like to know when this is ever called with something other than a start offset
    of zero and end offset of len(buffer).
     */
    /**
     * Prepare for lexing character data from {@code buffer} passed. Internal lexer state is supposed to be {@code initialState}. It is guaranteed
     * that the value of initialState is the same as returned by {@link #getState()} method of this lexer at condition {@code startOffset=getTokenStart()}.
     * This method is used to incrementally re-lex changed characters using lexing data acquired from this particular lexer sometime in the past.
     *
     * @param buffer       character data for lexing.
     * @param startOffset  offset to start lexing from
     * @param endOffset    offset to stop lexing at
     * @param initialState the initial state of the lexer.
     */
    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        System.out.println("SimulaLexer.start: start="+startOffset+", end="+endOffset+", state="+initialState+", chars="+buffer);
        Global.popUpMessage("SimulaLexer.start: start="+startOffset+", end="+endOffset+", state="+initialState+", chars="+buffer);
//        this.buffer=buffer;
//        this.startOffset = startOffset;
//        this.endOffset = endOffset;
        this.state = initialState;
        if(buffer.length() > 0) {
//            throw new RuntimeException("SimulaLexer.start: start="+startOffset+", end="+endOffset+", state="+initialState+", chars=\""+buffer+'"');
            Tester.addSource(buffer, startOffset, endOffset);
        }
        this.current = Tester.nextToken();
    }

    /**
     * Returns the current state of the lexer.
     *
     * @return the lexer state.
     */
    @Override
    public int getState() {
        System.out.println("SimulaLexer.getState: state="+state);
        Global.popUpMessage("SimulaLexer.getState: state="+state);
//        throw new RuntimeException("SimulaLexer.getState: start="+startOffset+", end="+endOffset+", state="+state+", chars=\""+buffer+'"');
        return state;
    }

    /**
     * Returns the token at the current position of the lexer or {@code null} if lexing is finished.
     *
     * @return the current token.
     */
    @Override
    public @Nullable IElementType getTokenType() {
//        throw new RuntimeException("SimulaLexer.getTokenType: start="+startOffset+", end="+endOffset+", state="+state+", chars=\""+buffer+'"');
//       return null;
//        return Tester.getElement();
        if(current == null) current = Tester.nullToken();
        Global.popUpMessage("SimulaLexer.getTokenType: "+current.type);
        return current.type;
    }

    @Override
    public int getTokenStart() {
        System.out.println("SimulaLexer.getTokenStart: ");
        Global.popUpMessage("SimulaLexer.getTokenStart: start="+current.start);
//        throw new RuntimeException("SimulaLexer.getTokenStart: start="+startOffset+", end="+endOffset+", state="+state+", chars=\""+buffer+'"');
//        return startOffset;
        return current.start;
    }

    @Override
    public int getTokenEnd() {
        System.out.println("SimulaLexer.getTokenEnd: ");
        Global.popUpMessage("SimulaLexer.getTokenEnd: "+current.end);
//        throw new RuntimeException("SimulaLexer.getTokenEnd: start="+startOffset+", end="+endOffset+", state="+state+", chars=\""+buffer+'"');
//        return endOffset;
        return current.end;
   }

    /**
     * Advances the lexer to the next token.
     */
    @Override
    public void advance() {
//       throw new RuntimeException("SimulaLexer.ZZ_advance: start="+startOffset+", end="+endOffset+", state="+state+", chars=\""+buffer+'"');
        Global.popUpMessage("SimulaLexer.ZZ_advance: FROM "+current);
        current = Tester.nextToken();
        Global.popUpMessage("SimulaLexer.ZZ_advance: TO "+current);

    }

    /**
     * Returns the current position and state of the lexer.
     *
     * @return the lexer position and state.
     */
    @Override
    public @NotNull LexerPosition getCurrentPosition() {
        throw new RuntimeException("SimulaLexer.getCurrentPosition: ");
//        return null;
    }

    /**
     * Restores the lexer to the specified state and position.
     *
     * @param position the state and position to restore to.
     */
    @Override
    public void restore(@NotNull LexerPosition position) {
        throw new RuntimeException("SimulaLexer.restore: ");

    }

    /**
     * Returns the buffer sequence over which the lexer is running. This method should return the
     * same buffer instance which was passed to the {@code start()} method.
     *
     * @return the lexer buffer.
     */
    @Override
    public @NotNull CharSequence getBufferSequence() {
        throw new RuntimeException("SimulaLexer.getBufferSequence: ");
//       return buffer;
    }

    /**
     * Returns the offset at which the lexer will stop lexing. This method should return
     * the length of the buffer or the value passed in the {@code endOffset} parameter
     * to the {@code start()} method.
     *
     * @return the lexing end offset
     */
    @Override
    public int getBufferEnd() {
        throw new RuntimeException("SimulaLexer.getBufferEnd: ");
//        return endOffset;
    }

}
