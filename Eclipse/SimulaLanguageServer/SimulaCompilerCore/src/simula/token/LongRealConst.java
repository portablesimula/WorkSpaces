package simula.token;

import simula.Option;
import simula.builder.SimulaLexer;
import simula.compiler.TokenManager;
import simula.compiler.utilities.KeyWord;

public class LongRealConst extends LexToken {
	public final double value;

	public LongRealConst(int tokenStartLine, CharSequence sourceText, int column, int length, double value, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.LONGREALKONST);
		super(tokenStartLine, sourceText, column, length, KeyWord.LONGREALKONST, lexer);
		this.value = value;
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return ""+value;
	}
	
	public int getLspTokenType() {
		return TokenManager.NUMBER.index; 
	}

	@Override
	public String toString() {
		return super.toString() + ", Value: " + value;
	}

}
