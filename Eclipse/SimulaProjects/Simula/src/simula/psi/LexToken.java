package simula.psi;

import javax.lang.model.SourceVersion;

import simula.compiler.utilities.Html;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;

public class LexToken extends PsiElement {
	public int keyWord;

//    CharSequence sourceText; // Pointer to the Whole FILE
//    int startOffset;
//    int endOffset;
    
//    public int lineNumber;

	public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
		super(KeyWord.edit(keyWord), sourceText);
		this.lineNumber = tokenStartLine;
		this.keyWord = keyWord;
//		this.sourceText = sourceText;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) {
			if(Option.internal.TRACE_NEW_LEXTOKEN > 1) {
				IO.println("NEW LexToken: Line: "+lineNumber+": "+this); //+"  CALLED FROM: " + Util.calledFrom(3, 25));
			} else {
				if(keyWord == KeyWord.WHITESPACES) ; // Nothing
				else if(keyWord == KeyWord.NEWLINE) ; // Nothing
				else {
					IO.println("NEW LexToken: Line: "+lineNumber+": "+this); //+"  CALLED FROM: " + Util.calledFrom(3, 25));
//					Thread.dumpStack();
				}
			}
//			IO.println("NEW LexToken: "+this+"  CALLED FROM: " + Util.calledFrom(3, 25));
//			Thread.dumpStack();
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

}
