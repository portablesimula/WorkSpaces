package simula.lsp.builder.token;

import simula.lsp.builder.SimulaBuilder;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.Option;

public class LongRealConst extends LexToken {
	public final double value;

	public LongRealConst(SimulaBuilder simBuilder, int tokenStartLine, int column, int length, double value) {
		super(simBuilder, tokenStartLine, column, length, KeyWord.LONGREALKONST, SemanticTokenTypes.Number);
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
