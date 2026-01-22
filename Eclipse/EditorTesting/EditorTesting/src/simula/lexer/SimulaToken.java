package simula.lexer;

import simula.compiler.utilities.KeyWord;

public class SimulaToken {// extends SyntaxClass {
	public int keyWord;

    CharSequence sourceText; // Pointer to the Whole FILE
    int startOffset;
    int endOffset;
    
    public int lineNumber;

	public SimulaToken(CharSequence sourceText, int startOffset, int endOffset, int keyWord, String debugName) {
//		super(debugName, SimulaLanguage.INSTANCE);
		this.keyWord = keyWord;
		this.sourceText = sourceText;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public SimulaToken(int keyWord, String debugName) {
//		super(debugName, SimulaLanguage.INSTANCE);
		this.keyWord = keyWord;
//		this.sourceText = sourceText;
//		this.startOffset = startOffset;
//		this.endOffset = endOffset;
	}

	public String text() {
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
		return KeyWord.edit(keyWord) + '[' + startOffset + ':' + endOffset + "]=\"" + edText() + '"';
	}

}
