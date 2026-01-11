package simula.lexer;

import simula.compiler.utilities.KeyWord;

public class IntegerConst extends SimulaToken {
	public final long value;

	public IntegerConst(CharSequence sourceText, int startOffset, int endOffset, long value) {
		super(sourceText, startOffset, endOffset, KeyWord.INTEGERKONST, "INTEGERKONST");
		this.value = value;
	}

}
