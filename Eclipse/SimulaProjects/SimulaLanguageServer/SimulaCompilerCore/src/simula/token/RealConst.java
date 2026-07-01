package simula.token;

import simula.builder.SimulaLexer;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.lsp.compiler.TokenManager;

public class RealConst extends LexToken {
	public final float value;

	public RealConst(int tokenStartLine, CharSequence sourceText, int column, int length, float value, SimulaLexer lexer) {
//	public RealConst(int tokenStartLine, CharSequence sourceText, int column, int length, double value, SimulaLexer lexer) {
		super(tokenStartLine, sourceText, column, length, KeyWord.REALKONST, lexer);
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
