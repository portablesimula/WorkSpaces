package simula.psi;

import simula.compiler.utilities.KeyWord;

public class LexToken extends PsiElement {
	public int keyWord;

    CharSequence sourceText; // Pointer to the Whole FILE
    int startOffset;
    int endOffset;
    
    public int lineNumber;

	public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
		super(KeyWord.edit(keyWord));
		this.lineNumber = tokenStartLine;
		this.keyWord = keyWord;
		this.sourceText = sourceText;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
//		IO.println("NEW LexToken: "+this);
//		IO.println("NEW LexToken: "+this+"  CALLED FROM: "+calledFrom(3, 25));
//		Thread.dumpStack();
	}

//	public LexToken(int tokenStartLine, int keyWord) {
//		super(KeyWord.edit(keyWord));
//		this.lineNumber = tokenStartLine;
//		this.keyWord = keyWord;
////		this.sourceText = sourceText;
////		this.startOffset = startOffset;
////		this.endOffset = endOffset;
//	}

	@Override public int getLineNumber() {
		return lineNumber;
	}

	public String getText() {
		CharSequence txt = sourceText.subSequence(startOffset, endOffset);
		String str = txt.toString();
		return str;
	}

	public String edText() {
		try {
			CharSequence txt = sourceText.subSequence(startOffset, endOffset);
			String str = txt.toString().replace("\r", "\\r").replace("\n", "\\n");
		return str;
		} catch(Exception e) {
			return "EOF";
		}
	}
	
	@Override
	public String toString() {
		return "Line-" + lineNumber + ':' + KeyWord.edit(keyWord) + '[' + startOffset + ':' + endOffset + "]=\"" + edText() + '"';
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
