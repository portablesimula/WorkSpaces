package simula.lsp.builder.token;

import simula.lsp.builder.SimulaBuilder;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.Option;

public class CharacterConst extends LexToken {
	public final Character value;

	public CharacterConst(SimulaBuilder simBuilder, int tokenStartLine, int column, int length, int value) {
		super(simBuilder, tokenStartLine, column, length, KeyWord.CHARACTERKONST, SemanticTokenTypes.String);
		this.value = Character.valueOf((char) value);
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
