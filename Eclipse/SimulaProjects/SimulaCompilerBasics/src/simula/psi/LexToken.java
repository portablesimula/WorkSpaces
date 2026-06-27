package simula.psi;

import javax.lang.model.SourceVersion;

import simula.compiler.utilities.Html;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;

public class LexToken extends PsiElement {
	public int keyWord;
	public String styleName;
	
//	/// All errors associated with this LexToken
//	private Set<String> errors;
	
	public static LexToken prevToken;
	
//	public void addError(String err) {
//		if(errors == null) errors = new HashSet<String>();
//		errors.add(err);
//	}
//
//	public Set<String> getErrors() {
//		return errors;
//	}

	public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
		this(tokenStartLine, sourceText, startOffset, endOffset, keyWord, null);
	}
	
	public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord, String styleName) {
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
		
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) {
			if(Option.internal.TRACE_NEW_LEXTOKEN > 1) {
				IO.println("NEW LexToken: Line: "+lineNumber+": "+this+"  CALLED FROM: " + Util.calledFrom(3, 25));
			} else {
				if(keyWord == KeyWord.WHITESPACES) ; // Nothing
				else if(keyWord == KeyWord.NEWLINE) ; // Nothing
				else {
					IO.println("NEW LexToken: Line: "+lineNumber+": "+this+"  CALLED FROM: " + Util.calledFrom(3, 25));
				}
			}
		}
		
		if(Option.PSI_VERIFY) {
			if(keyWord != KeyWord.NEWLINE && this.getText().contains("\n"))
				Util.IERR("NEW LexToken: PSI_VERIFY FAILD: Token text contais NEWLINE: " + this);
			if(prevToken != null) {
				if(keyWord != KeyWord.EOF && startOffset != prevToken.endOffset) {
					System.err.println("NEW LexToken: Prev Token: " + prevToken.getClass().getSimpleName()+ " " + prevToken);
					System.err.println("NEW LexToken: This Token: " + this.getClass().getSimpleName()+ " " + this);
					Util.IERR("\"NEW LexToken: Illegal gap between tokens: ");
					Util.STOP();
				}
			}
//			prevToken = this;
			prevToken = (keyWord != KeyWord.EOF)? this : null;
		}
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
		try {
			CharSequence txt = sourceText.subSequence(startOffset, endOffset);
			String str = txt.toString();
//			if(keyWord == KeyWord.IDENTIFIER) {
//				if(SourceVersion.isKeyword(str)) { // Check for Java keyWord
//					str = "_" + str;
//					Util.IERR("LexToken.getText: Illegal use of KeyWord: " + str);
//				}
//			}
			return str;
		} catch(Exception e) {
			return "EOF";
		}
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
		if(keyWord == KeyWord.COMMENT_KEY) return false;
		if(keyWord == KeyWord.COMMENT_TEXT) return false;
		return true;
	}

//	public boolean isWhiteSpaces() {
//		if(keyWord == KeyWord.NEWLINE) return true;
//		if(keyWord == KeyWord.WHITESPACES) return true;
//		return false;
//	}

//	/// Returns the style for this Token's keyword.
//	/// @return the style for this Token's keyword
//	@Override
//	public Style getStyle(final PsiTextPanel psiText) {
//		if(getErrors() != null) return psiText.styleError;
//		if(styleName != null) return psiText.doc.getStyle(styleName);
//		switch(keyWord) {
//		    case KeyWord.ASSIGNVALUE, KeyWord.ASSIGNREF, KeyWord.COMMA, KeyWord.COLON, KeyWord.SEMICOLON,
//		    	 KeyWord.BEGPAR, KeyWord.ENDPAR, KeyWord.BEGBRACKET, KeyWord.ENDBRACKET, KeyWord.EQR, KeyWord.NER,
//			     KeyWord.EQ, KeyWord.GE, KeyWord.GT, KeyWord.LE, KeyWord.LT, KeyWord.NE,
//			     KeyWord.PLUS, KeyWord.MINUS, KeyWord.MUL, KeyWord.DIV, KeyWord.INTDIV, KeyWord.EXP,
//			     KeyWord.IDENTIFIER,
//			     KeyWord.DOT:
//		    	 return psiText.styleRegular;
//		    	 
//		    case KeyWord.BOOLEANKONST, KeyWord.INTEGERKONST, KeyWord.CHARACTERKONST,
//		    	 KeyWord.REALKONST, KeyWord.TEXTKONST, KeyWord.STRING:
//		    	 return psiText.styleConstant;
//		    	 
//		    case KeyWord.COMMENT_TEXT:
//		    	 return psiText.styleComment;
//		    	 
//		    default: return psiText.styleKeyword;
//		}
//	}

    

//    public Set<String> accumErrors(Set<String> errorLines) {
//    	Set<String> errors = getErrors();
//    	if(errors != null) {
//    		if(errorLines == null) errorLines = new HashSet<String>();
//    		errorLines.addAll(errors);
//    	}
//    	return errorLines;
//    }
}
