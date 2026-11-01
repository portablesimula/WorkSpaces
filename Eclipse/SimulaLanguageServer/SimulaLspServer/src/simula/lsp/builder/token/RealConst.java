package simula.lsp.builder.token;

import simula.lsp.builder.SimulaBuilder;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.Option;

public class RealConst extends LexToken {
	public final float value;

	public RealConst(SimulaBuilder simBuilder, int tokenStartLine, int column, int length, float value) {
		super(simBuilder, tokenStartLine, column, length, KeyWord.REALKONST, SemanticTokenTypes.Number);
		this.value = value;
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return ""+value;
	}

}
