package simula.plugin.extensions.highlighterExtension.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import simula.plugin.extensions.lang.SimulaLanguage;
import org.jetbrains.annotations.NotNull;

public class SimpleManualLexer_draft1 extends LexerBase {
    // Define custom token types (for a real plugin, these would be in a custom *TokenTypes class)
    public static final IElementType WORD = new IElementType("WORD", SimulaLanguage.INSTANCE);

    private CharSequence myBuffer;
    private int myStartOffset;
    private int myEndOffset;
    private int myPosition;
    private IElementType myCurrentToken;

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
    @Override
    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        myBuffer = buffer;
        myStartOffset = startOffset;
        myEndOffset = endOffset;
        myPosition = startOffset;
        myCurrentToken = null;
        // In a real incremental lexer, initialState would be used to restore context
        // For this simple example, we assume we always start from the beginning (initialState 0)
//        if(buffer.length() > 40) throw new RuntimeException("ZZZZZZZZZZZZZZZZZZZZZZZZZ");
        advance();
//        LexerEditorHighlighter xxx = null;
//        EditorHighlighter xxx;
//        PrioritizedDocumentListener zzz;
//        TokenIterator bbb;
//        SegmentArrayWithData mmm;
//        EditorColorsScheme aaa;
//        ImmutableCharSequence xxx;
//        CharSequenceSubSequence zzz;
//        CharArrayExternalizable aaa;
//        CharSequenceWithStringHash bbb;
//        CharArrayUtil ccc;
//        RestartableLexer ddd;
//        DataStorage eee;
//        SegmentArrayWithData fff;
//        IntBasedStorage ggg;
//        ShortBasedStorage hhh;
//        //ValidatingLexerWrapper iii;
//        ProgressManager jjj;
//        ProcessCanceledException kkk;
//        SegmentArray lll;
//        ApplicationManager mmm;
//        Application nnn;
//        UIUtil ooo;
    }
    @Override
    public void advance() {
        if (myPosition >= myEndOffset) {
            myCurrentToken = null;
            return;
        }

        myStartOffset = myPosition;
        char currentChar = myBuffer.charAt(myPosition);

        if (Character.isWhitespace(currentChar)) {
            while (myPosition < myEndOffset && Character.isWhitespace(myBuffer.charAt(myPosition))) {
                myPosition++;
            }
            myCurrentToken = TokenType.WHITE_SPACE;
        } else if (Character.isLetterOrDigit(currentChar)) {
            while (myPosition < myEndOffset && Character.isLetterOrDigit(myBuffer.charAt(myPosition))) {
                myPosition++;
            }
            myCurrentToken = WORD;
        } else {
            // Handle single character "invalid" tokens for anything else
            myPosition++;
            myCurrentToken = TokenType.BAD_CHARACTER;
        }
        myEndOffset = myPosition; // Update end offset to current token end
    }

    /**
     * Returns the buffer sequence over which the lexer is running. This method should return the
     * same buffer instance which was passed to the {@code start()} method.
     *
     * @return the lexer buffer.
     */
    @Override
    public @NotNull CharSequence getBufferSequence() {
        throw new RuntimeException("SimulaLexer.getBufferSequence: \""+myBuffer+'"');
//        return myBuffer;
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
//        return myEndOffset;
    }

    @Override
    public IElementType getTokenType() {
        return myCurrentToken;
    }

    @Override
    public int getTokenStart() {
        return myStartOffset;
    }

    @Override
    public int getTokenEnd() {
        return myEndOffset;
    }

    @Override
    public int getState() {
        // State is represented by a single integer number
        // For this simple, stateless lexer, we return 0
        return 0;
    }

//    // Dummy classes for demonstration purposes
//    private static class MyLanguage extends com.intellij.lang.Language {
//        public static final MyLanguage INSTANCE = new MyLanguage();
//        private MyLanguage() {
//            super("SimpleManual");
//        }
//    }
}

