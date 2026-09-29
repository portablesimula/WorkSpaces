package simula.core.builder.token;

import java.util.List;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.utilities.KeyWord;

public class RealConst extends LexToken {
	public final float value;

	public RealConst(int tokenStartLine, List<String> sourceLines, int column, int length, float value, SimulaLexer lexer) {
		super(tokenStartLine, sourceLines, column, length, KeyWord.REALKONST, SimulaTokenTypes.Number, lexer);
		this.value = value;
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return ""+value;
	}

}
