package simula.lsp.builder.token;

import java.util.List;

import simula.lsp.builder.SimulaLexer;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.Option;

public class WhiteSpaceToken extends LexToken {
	String value;

	public WhiteSpaceToken(int tokenStartLine, List<String> sourceLines, int column, int length, SimulaLexer lexer) {
		super(tokenStartLine, sourceLines, column, length, KeyWord.WHITESPACES, "UNKNOWN", lexer);
		this.value = this.edTokenText(lexer);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return value;
	}

}
