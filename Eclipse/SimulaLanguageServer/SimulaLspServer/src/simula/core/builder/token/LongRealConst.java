package simula.core.builder.token;

import java.util.List;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.utilities.KeyWord;

public class LongRealConst extends LexToken {
	public final double value;

	public LongRealConst(int tokenStartLine, List<String> sourceLines, int column, int length, double value, SimulaLexer lexer) {
		super(tokenStartLine, sourceLines, column, length, KeyWord.LONGREALKONST, SemanticTokenTypes.Number, lexer);
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
