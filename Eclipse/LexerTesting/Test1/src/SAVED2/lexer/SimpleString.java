package simula.lexer;

import simula.compiler.utilities.KeyWord;

public class SimpleString extends SimulaToken {
	public final String value;

	public SimpleString(CharSequence sourceText, int startOffset, int endOffset, String value) {
		super(sourceText, startOffset, endOffset, KeyWord.TEXTKONST, "TEXTKONST");
		this.value = value;
		System.out.println("NEW " + this);
	}

	public String toString() {
		return "SimpleString: \"" + value + '"';
	}
}
