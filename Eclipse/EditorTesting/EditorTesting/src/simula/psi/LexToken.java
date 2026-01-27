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
		IO.println("NEW LexToken(1) "+this);
	}

	public LexToken(int tokenStartLine, int keyWord) {
		super(KeyWord.edit(keyWord));
		this.lineNumber = tokenStartLine;
		this.keyWord = keyWord;
//		this.sourceText = sourceText;
//		this.startOffset = startOffset;
//		this.endOffset = endOffset;
	}

	@Override public int getLineNumber() {
		return lineNumber;
	}

	public String getText() {
		CharSequence txt = sourceText.subSequence(startOffset, endOffset);
		String str = txt.toString();
		return str;
	}

	public String edText() {
		CharSequence txt = sourceText.subSequence(startOffset, endOffset);
		String str = txt.toString().replace("\r", "\\r").replace("\n", "\\n");
		return str;
	}
	
	@Override
	public String toString() {
		return "Line-" + lineNumber + ':' + KeyWord.edit(keyWord) + '[' + startOffset + ':' + endOffset + "]=\"" + edText() + '"';
	}

}
