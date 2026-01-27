package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.psi.LexToken;

public class Identifier extends LexToken {
	public final String value;

	public Identifier(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, String value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.IDENTIFIER);
		this.value = value;
	}

}
