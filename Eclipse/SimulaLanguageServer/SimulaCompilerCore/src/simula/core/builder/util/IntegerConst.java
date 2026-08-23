package simula.core.builder.util;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.builder.export.LexToken;
import simula.core.builder.export.SimulaTokenTypes;
import simula.core.utilities.KeyWord;

public class IntegerConst extends LexToken {
	public final long value;

	public IntegerConst(int tokenStartLine, CharSequence sourceText, int column, int length, long value, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.INTEGERKONST);
		super(tokenStartLine, sourceText, column, length, KeyWord.INTEGERKONST, SimulaTokenTypes.Number, lexer);
		this.value = value;
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return ""+value;
	}

	@Override
	public String toString() {
		return super.toString() + ", Value: " + value;
	}

}
