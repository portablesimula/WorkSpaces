package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.psi.LexToken;

public class RealConst extends LexToken {
	public final float value;

	public RealConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, float value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.REALKONST);
		this.value = value;
	}

}
