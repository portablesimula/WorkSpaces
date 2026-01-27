package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.psi.LexToken;

public class CharacterConst extends LexToken {
	public final int value;

	public CharacterConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.CHARACTERKONST);
		this.value = value;
	}

}
