package simula.plugin.extensions.highlighterExtension.lexer;


import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
//import com.intellij.util.text.CharSequenceReader;
import simula.plugin.extensions.lang.SimulaLanguage;
//import org.jetbrains.annotations.NotNull;

public class SimpleManualLexer extends LexerBase {
    // Define custom token types (for a real plugin, these would be in a custom *TokenTypes class)
    public static final IElementType WORD = new IElementType("WORD", SimulaLanguage.INSTANCE);

    private static final int DEBUG = 1;
    
    private CharSequence myBuffer;
    private int myEndOffset;
    private int tokenStartOffset;
    private int tokenEndOffset;
    private int myPosition;
    private IElementType tokenType;

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
        myEndOffset = endOffset;
        
        tokenStartOffset = startOffset;
        tokenEndOffset = startOffset;
        myPosition = startOffset;
        tokenType = null;
        // In a real incremental lexer, initialState would be used to restore context
        // For this simple example, we assume we always start from the beginning (initialState 0)
        advance();
    }

    @Override
    public void advance() {
    	if(DEBUG > 1) System.out.println("\nSimpleManualLexer.advance: myPosition="+myPosition+", myEndOffset="+myEndOffset+", tokenStartOffset="+tokenStartOffset+", tokenEndOffset="+tokenEndOffset);
	    if(DEBUG > 0) if(tokenType != null) {
    		CharSequence txt = myBuffer.subSequence(tokenStartOffset, tokenEndOffset);
        	System.out.println("SimpleManualLexer.advance: "+tokenType+'['+tokenStartOffset+':'+tokenEndOffset+"]=\""+txt+'"');
    	}
    	
    	if (myPosition >= myEndOffset) {
            tokenType = null;
            return;
        }

        tokenStartOffset = myPosition;
        char currentChar = myBuffer.charAt(myPosition);

        if (Character.isWhitespace(currentChar)) {
            while (myPosition < myEndOffset && Character.isWhitespace(myBuffer.charAt(myPosition))) {
                myPosition++;
            }
            tokenType = TokenType.WHITE_SPACE;
        } else if (Character.isLetterOrDigit(currentChar)) {
            while (myPosition < myEndOffset && Character.isLetterOrDigit(myBuffer.charAt(myPosition))) {
                myPosition++;
            }
            tokenType = WORD;
        } else {
            // Handle single character "invalid" tokens for anything else
            myPosition++;
            tokenType = TokenType.BAD_CHARACTER;
        }
        tokenEndOffset = myPosition; // Update end offset to current token end
    }

    /**
     * Returns the buffer sequence over which the lexer is running. This method should return the
     * same buffer instance which was passed to the {@code start()} method.
     *
     * @return the lexer buffer.
     */
    @Override
    public CharSequence getBufferSequence() {
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
    	if(DEBUG > 1) System.out.println("SimpleManualLexer.getTokenType: "+tokenType);
        return tokenType;
    }

    @Override
    public int getTokenStart() {
    	if(DEBUG > 1) System.out.println("SimpleManualLexer.getTokenStart: "+tokenStartOffset);
        return tokenStartOffset;
    }

    @Override
    public int getTokenEnd() {
    	if(DEBUG > 1) System.out.println("SimpleManualLexer.getTokenEnd: "+tokenEndOffset);
        return tokenEndOffset;
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

