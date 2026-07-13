package simula.token;

import simula.Option;
import simula.builder.SimulaLexer;
import simula.compiler.utilities.KeyWord;
import simula.lsp.compiler.TokenManager;

public class Identifier extends LexToken {
	public String value;

	public Identifier(int tokenStartLine, CharSequence sourceText, int column, int length, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.CHARACTERKONST);
		super(tokenStartLine, sourceText, column, length, KeyWord.IDENTIFIER, lexer);
		this.value = this.edTokenText(lexer);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}
	
	public Identifier(String value) {
		super(KeyWord.IDENTIFIER);
		this.value = value;
	}

	@Override
	public String edText() {
		return value;
	}

	public int getLspTokenType() {
		return TokenManager.OTHER.index; 
	}

	@Override
	public String toString() {
		return super.toString() + ", Value: \"" + value + '"';
	}
}
