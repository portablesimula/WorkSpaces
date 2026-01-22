package simula.lexer;

import simula.compiler.utilities.KeyWord;

public class Identifier extends SimulaToken {
	public final String value;

	public Identifier(CharSequence sourceText, int startOffset, int endOffset, String value) {
		super(sourceText, startOffset, endOffset, KeyWord.IDENTIFIER, "IDENTIFIER");
		this.value = value;
	}

}
