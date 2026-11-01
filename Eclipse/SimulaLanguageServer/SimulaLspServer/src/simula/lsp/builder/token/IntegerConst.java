package simula.lsp.builder.token;

import simula.lsp.builder.SimulaBuilder;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.Option;

public class IntegerConst extends LexToken {
	public final long value;

	public IntegerConst(SimulaBuilder simBuilder, int tokenStartLine, int column, int length, long value) {
		super(simBuilder, tokenStartLine, column, length, KeyWord.INTEGERKONST, SemanticTokenTypes.Number);
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
