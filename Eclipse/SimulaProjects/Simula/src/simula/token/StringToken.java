package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.psi.LexToken;

public class StringToken extends LexToken {
	public final String value;

	public StringToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, String value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.TEXTKONST);
		this.value = value;
//		IO.println("NEW " + this);
	}

//	public String toString() {
//		return "StringToken: \"" + value + '"';
//	}
}
