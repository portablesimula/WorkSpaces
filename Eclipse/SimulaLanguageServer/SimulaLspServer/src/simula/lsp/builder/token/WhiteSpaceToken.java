package simula.lsp.builder.token;

import simula.lsp.builder.SimulaBuilder;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.Option;

public class WhiteSpaceToken extends LexToken {
	String value;

	public WhiteSpaceToken(SimulaBuilder simBuilder, int tokenStartLine, int column, int length) {
		super(simBuilder, tokenStartLine, column, length, KeyWord.WHITESPACES, "UNKNOWN");
		this.value = this.edTokenText(simBuilder.lexer);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return value;
	}

}
