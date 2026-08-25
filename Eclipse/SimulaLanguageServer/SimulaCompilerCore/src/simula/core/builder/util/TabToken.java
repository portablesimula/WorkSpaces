package simula.core.builder.util;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.builder.export.LexToken;
import simula.core.builder.export.SimulaTokenTypes;
import simula.core.utilities.KeyWord;

public class TabToken extends LexToken {
	public final Character value;

	public TabToken(int tokenStartLine, CharSequence sourceText, int column, int length, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.CHARACTERKONST);
		super(tokenStartLine, sourceText, column, length, KeyWord.CHARACTERKONST, SimulaTokenTypes.Character, lexer);
		this.value = '\t';
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
