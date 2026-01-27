package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.psi.LexToken;

public class SimpleString extends LexToken {
	public final String value;

	public SimpleString(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, String value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.TEXTKONST);
		this.value = value;
		System.out.println("NEW " + this);
	}

	public String toString() {
		return "SimpleString: \"" + value + '"';
	}
}
