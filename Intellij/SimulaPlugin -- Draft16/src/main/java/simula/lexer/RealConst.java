package simula.lexer;

import simula.compiler.utilities.KeyWord;

public class RealConst extends SimulaToken {
	public final double value;

	public RealConst(CharSequence sourceText, int startOffset, int endOffset, double value) {
		super(sourceText, startOffset, endOffset, KeyWord.REALKONST, "REALKONST");
		this.value = value;
	}

}
