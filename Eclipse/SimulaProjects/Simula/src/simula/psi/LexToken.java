package simula.psi;

import java.util.Vector;

import javax.lang.model.SourceVersion;
import javax.swing.text.Style;
import simula.compiler.utilities.Html;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.editor.PsiTextPanel;

public class LexToken extends PsiElement {
	public int keyWord;
	public Vector<String> errors;
	
	public static LexToken prevToken;
	
	public void addError(String err) {
		if(errors == null) errors = new Vector<String>();
		errors.add(err);
	}


	public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
		super(KeyWord.edit(keyWord), sourceText);
		this.lineNumber = tokenStartLine;
		this.keyWord = keyWord;
//		this.sourceText = sourceText;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		if(endOffset <= startOffset) {
			if(keyWord != KeyWord.EOF)
				Util.IERR("NEW LexToken: Illegal Token: " + this.getClass().getSimpleName()+ " " + this);
		}
//		if(Option.TESTING_SUBSEQUENT_TOKENS) {
//			if(prevToken != null) {
//				if(startOffset != prevToken.endOffset) {
//					System.err.println("NEW LexToken: Prev Token: " + prevToken.getClass().getSimpleName()+ " " + prevToken);
//					System.err.println("NEW LexToken: This Token: " + this.getClass().getSimpleName()+ " " + this);
//					Util.IERR("\"NEW LexToken: Illegal gap between tokens: ");
//					Util.STOP();
//				}
//			}
//			prevToken = this;
//		}
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) {
			if(Option.internal.TRACE_NEW_LEXTOKEN > 1) {
//				IO.println("NEW LexToken: Line: "+lineNumber+": "+this); //+"  CALLED FROM: " + Util.calledFrom(3, 25));
				IO.println("NEW LexToken: Line: "+lineNumber+": "+this+"  CALLED FROM: " + Util.calledFrom(3, 25));
			} else {
				if(keyWord == KeyWord.WHITESPACES) ; // Nothing
				else if(keyWord == KeyWord.NEWLINE) ; // Nothing
				else {
//					IO.println("NEW LexToken: Line: "+lineNumber+": "+this); //+"  CALLED FROM: " + Util.calledFrom(3, 25));
					IO.println("NEW LexToken: Line: "+lineNumber+": "+this+"  CALLED FROM: " + Util.calledFrom(3, 25));
//					Thread.dumpStack();
				}
			}
//			IO.println("NEW LexToken: "+this+"  CALLED FROM: " + Util.calledFrom(3, 25));
//			Thread.dumpStack();
		}
//		if(startOffset == 63) {
//			Util.IERR("STOP ON TOKEN: " + this);
//		}
	}

//	public LexToken(int tokenStartLine, int keyWord) {
//		super(KeyWord.edit(keyWord));
//		this.lineNumber = tokenStartLine;
//		this.keyWord = keyWord;
////		this.sourceText = sourceText;
////		this.startOffset = startOffset;
////		this.endOffset = endOffset;
//	}

//	@Override public int firstLineNumber() {
//		return lineNumber;
//	}
//
//	@Override public int lastLineNumber() {
//		return lineNumber;
//	}

	public String getText() {
		CharSequence txt = sourceText.subSequence(startOffset, endOffset);
		String str = txt.toString();
//		if(keyWord == KeyWord.IDENTIFIER) {
//			if(SourceVersion.isKeyword(str)) { // Check for Java keyWord
//				str = "_" + str;
//				Util.IERR("LexToken.getText: Illegal use of KeyWord: " + str);
//			}
//		}
		return str;
	}

	@Override
	public String edText() {
		try {
			CharSequence txt = sourceText.subSequence(startOffset, endOffset);
			String str = txt.toString();
			if(keyWord == KeyWord.IDENTIFIER && SourceVersion.isKeyword(str)) // Check for Java keyWord
				 str = "_" + str;
			else str = str.replace("\r", "\\r").replace("\n", "\\n");
			return str;
		} catch(Exception e) {
			return "EOF";
		}
	}

	public String edHtml() {
//		return KeyWord.edit(keyWord) + '[' + startOffset + ':' + endOffset + "]=\"" + edText() + '"';
//		String ID = KeyWord.edit(keyWord) + '[' + startOffset + ':' + endOffset + "]=\"" + edText() + '"';
		int lno = this.firstLineNumber();
		int lastLine = this.lastLineNumber();
        return Html.edPsi(lno, lastLine, toString());
	}

	public String edPsiLine() {
		return "Line " + lineNumber + ": " + KeyWord.edit(keyWord) + '[' + startOffset + ':' + endOffset + "]=\"" + edText() + '"';		
	}
	public String edHtmlLine() {
		return edHtml();
	}

	@Override
	public String toString() {
		return "Line " + lineNumber + ": " + KeyWord.edit(keyWord) + '[' + startOffset + ':' + endOffset + "]=\"" + edText() + '"';
//		String ID = KeyWord.edit(keyWord) + '[' + startOffset + ':' + endOffset + "]=\"" + edText() + '"';
//		int lno = this.firstLineNumber();
//		int lastLine = this.lastLineNumber();
//        return Html.edPsi(lno, lastLine, ID);
	}

	public boolean isParserToken() {
		if(keyWord == KeyWord.NEWLINE) return false;
		if(keyWord == KeyWord.WHITESPACES) return false;
		if(keyWord == KeyWord.COMMENT) return false;
		return true;
	}

//	public boolean isWhiteSpaces() {
//		if(keyWord == KeyWord.NEWLINE) return true;
//		if(keyWord == KeyWord.WHITESPACES) return true;
//		return false;
//	}

	/// Returns the style for this Token's keyword.
	/// @return the style for this Token's keyword
	@Override
	public Style getStyle(final PsiTextPanel psiText) {
		if(errors != null) return psiText.styleError;
		switch(keyWord) {
		    case KeyWord.ASSIGNVALUE, KeyWord.ASSIGNREF, KeyWord.COMMA, KeyWord.COLON, KeyWord.SEMICOLON,
		    	 KeyWord.BEGPAR, KeyWord.ENDPAR, KeyWord.BEGBRACKET, KeyWord.ENDBRACKET, KeyWord.EQR, KeyWord.NER,
			     KeyWord.EQ, KeyWord.GE, KeyWord.GT, KeyWord.LE, KeyWord.LT, KeyWord.NE,
			     KeyWord.PLUS, KeyWord.MINUS, KeyWord.MUL, KeyWord.DIV, KeyWord.INTDIV, KeyWord.EXP,
			     KeyWord.IDENTIFIER, KeyWord.DOT:
		    	 return psiText.styleRegular;
		    	 
		    case KeyWord.BOOLEANKONST, KeyWord.INTEGERKONST, KeyWord.CHARACTERKONST,
		    	 KeyWord.REALKONST, KeyWord.TEXTKONST, KeyWord.STRING:
		    	 return psiText.styleConstant;
		    	 
		    case KeyWord.COMMENT:
		    	 return psiText.styleComment;
		    	 
		    default: return psiText.styleKeyword;
		}
	}

}
