package simula.token;

import simula.compiler.utilities.KeyWord;
import simula.psi.LexToken;

public class LongRealConst extends LexToken {
	public final double value;

	public LongRealConst(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, double value) {
		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.LONGREALKONST);
		this.value = value;
	}

}
