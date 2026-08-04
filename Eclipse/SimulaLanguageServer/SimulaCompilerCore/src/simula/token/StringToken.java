package simula.token;

import simula.Option;
import simula.builder.SimulaLexer;
import simula.compiler.TokenManager;
import simula.compiler.utilities.KeyWord;

public class StringToken extends LexToken {
	public final String value;

	public StringToken(int tokenStartLine, CharSequence sourceText, int column, int length, String value, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.TEXTKONST);
		super(tokenStartLine, sourceText, column, length, KeyWord.TEXTKONST, lexer);
		this.value = value;
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return value;
	}
	
	public int getLspTokenType() {
		return TokenManager.STRING.index; 
	}

	@Override
	public String toString() {
		return super.toString() + ", Value: \"" + value + '"';
	}
}
