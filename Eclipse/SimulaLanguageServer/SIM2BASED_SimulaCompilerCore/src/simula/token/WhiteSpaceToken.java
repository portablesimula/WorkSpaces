package simula.token;

import simula.Option;
import simula.builder.SimulaLexer;
import simula.compiler.utilities.KeyWord;
import simula.lsp.compiler.TokenManager;

public class WhiteSpaceToken extends LexToken {
	String value;

	public WhiteSpaceToken(int tokenStartLine, CharSequence sourceText, int column, int length, SimulaLexer lexer) {
		super(tokenStartLine, sourceText, column, length, KeyWord.WHITESPACES, lexer);
		this.value = this.edTokenText(lexer);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return value;
	}
	
	public int getLspTokenType() {
		return TokenManager.OTHER.index; 
	}

}
