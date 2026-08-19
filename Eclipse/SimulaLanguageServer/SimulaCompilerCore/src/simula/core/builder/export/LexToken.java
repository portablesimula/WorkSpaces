package simula.core.builder.export;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.builder.TokenManager;
import simula.core.builder.util.LexPosition;
import simula.core.utilities.KeyWord;
import simula.core.utilities.Util;

public class LexToken {
    CharSequence sourceText; // Pointer to the Whole FILE
    public int lineNumber;
//    public int startOffset;
//    public int endOffset;
    public int column; // Zero based column within current line
    public int length;
	public int keyWord;
	public String styleName;
	
	public String tokenText; // Copied from an interval of SourceText
	
	public static LexToken prevToken;
	public static int lineNumberBeforeScanBasic; // TESTING SCANNER

	public LexToken(int tokenStartLine, CharSequence sourceText, int column, int length, int keyWord, SimulaLexer lexer) {
		this(tokenStartLine, sourceText, column, length, keyWord, null, lexer);
	}
	
	public LexPosition getPosition() {
		return new LexPosition(lineNumber, column);
	}
	
	public LexToken(int tokenStartLine, CharSequence sourceText, int column, int length, int keyWord, String styleName, SimulaLexer lexer) {
		this.lineNumber = tokenStartLine;
		this.sourceText = sourceText;
		this.column = column;
		this.length = length;
		this.keyWord = keyWord;
		this.styleName = styleName;
		
//		IO.println("NEW LexToken: lineNumber: "+lineNumber+", column:"+column+", length:"+length);
		
		if(Option.LEX_VERIFY) {
	    	if(length == 0 && keyWord == KeyWord.EOF) {
	    		prevToken = null;
	    	} else lexer.verifyToken(this, lineNumber, column, length);
		}
		
		this.tokenText = edTokenText(lexer);

//		if(endOffset <= startOffset) {
//			if(keyWord != KeyWord.EOF)
//				Util.IERR("NEW LexToken: Illegal Token: " + this.getClass().getSimpleName()+ " " + this);
//		}
				
		if(Option.LEX_VERIFY) {
			if(keyWord != KeyWord.NEWLINE && this.getText().contains("\r"))
				Util.IERR("NEW LexToken: LEX_VERIFY FAILD: Token text contais CR: " + this);
			if(keyWord != KeyWord.NEWLINE && this.getText().contains("\n"))
				Util.IERR("NEW LexToken: LEX_VERIFY FAILD: Token text contais NEWLINE: " + this);
			if(prevToken != null) {
				if(keyWord != KeyWord.EOF && column != ( prevToken.column + prevToken.length )) {
					System.err.println("\nNEW LexToken: Illegal gap between tokens: ");
					System.err.println("NEW LexToken: Prev Token: " + prevToken.getClass().getSimpleName()+ " " + prevToken);
					System.err.println("NEW LexToken: This Token: " + this.getClass().getSimpleName()+ " " + this);
					Util.IERR("NEW LexToken: Illegal gap between tokens: ");
					Util.STOP();
				}
			}
			prevToken = (keyWord != KeyWord.NEWLINE && keyWord != KeyWord.EOF)? this : null;
		}
		
//		Util.IERR("");
//		Thread.dumpStack();
		
	}
	
	public LexToken(int keyWord) {
		this.keyWord = keyWord;
	}

	protected void TRACE_NEW_LEXTOKEN() {
		if(Option.internal.TRACE_NEW_LEXTOKEN > 1) {
			IO.println("NEW LexToken: "+this);
//			IO.println("NEW LexToken: "+this+"  CALLED FROM: " + Util.calledFrom(3, 25));
		} else {
			if(keyWord == KeyWord.WHITESPACES) ; // Nothing
			else if(keyWord == KeyWord.NEWLINE) ; // Nothing
			else {
				IO.println("NEW LexToken: " + this);
//				IO.println("NEW LexToken: " + this+"  CALLED FROM: " + Util.calledFrom(3, 25));
			}
		}
	}
	
	public String edText() {
		return KeyWord.edit(keyWord);
	}

	public boolean isParserToken() {
		if(keyWord == KeyWord.NEWLINE) return false;
		if(keyWord == KeyWord.WHITESPACES) return false;
		if(keyWord == KeyWord.COMMENT_KEY) return false;
		if(keyWord == KeyWord.COMMENT_TEXT) return false;
		return true;
	}
	
	public int getLspTokenType() {
		return TokenManager.OTHER.index; 
	}

//	public String getOriginalText() {
//		CharSequence txt = sourceText.subSequence(startOffset, endOffset);
//		return txt.toString();
//	}
	
	public int firstLineNumber() {
		return lineNumber;
	}

	public int lastLineNumber() {
		return lineNumber;
	}

	public String getText() {
		return tokenText;
	}
	
	public String edTokenText(SimulaLexer lexer) {
		int startOfLine = lexer.getLineStartPos(lineNumber);
		int tokenStartPos = startOfLine + column;
		CharSequence txt = sourceText.subSequence(tokenStartPos, tokenStartPos + length);
		String debugText=txt.toString();
		return debugText;
	}
	
	public String edToken(SimulaLexer lexer) {
		String str = Util.printable(edTokenText(lexer));
		return "Line " + lineNumber + ": " + KeyWord.edit(keyWord) + "[col:" + column + ", lng:" + length + "]=\"" + str + '"';
	}

	@Override
	public String toString() {
		if(tokenText == null) Util.IERR("");
		String str = (tokenText == null)? "UNKNOWN" :  Util.printable(tokenText);
		return "Line " + lineNumber + ": " + KeyWord.edit(keyWord) + "[col:" + column + ", lng:" + length + "] Text: \"" + str + '"';
	}


}
