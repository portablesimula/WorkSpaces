package simula.core.builder.util;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.builder.export.LexToken;
import simula.core.builder.export.SimulaTokenTypes;
import simula.core.utilities.KeyWord;

public class WhiteSpaceToken extends LexToken {
	String value;

	public WhiteSpaceToken(int tokenStartLine, CharSequence sourceText, int column, int length, SimulaLexer lexer) {
		super(tokenStartLine, sourceText, column, length, KeyWord.WHITESPACES, SimulaTokenTypes.WhiteSpace, lexer);
		this.value = this.edTokenText(lexer);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return value;
	}

}
