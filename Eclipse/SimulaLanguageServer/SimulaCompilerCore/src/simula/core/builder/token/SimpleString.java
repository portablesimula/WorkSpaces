package simula.core.builder.token;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.builder.TokenManager;
import simula.core.utilities.KeyWord;

public class SimpleString extends LexToken {
	public final String value;

	public SimpleString(int tokenStartLine, CharSequence sourceText, int column, int length, String value, SimulaLexer lexer) {
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
