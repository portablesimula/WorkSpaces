package simula.core.builder.util;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.builder.TokenManager;
import simula.core.builder.export.LexToken;
import simula.core.utilities.KeyWord;

public class CharacterConst extends LexToken {
	public final Character value;

	public CharacterConst(int tokenStartLine, CharSequence sourceText, int column, int length, int value, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.CHARACTERKONST);
		super(tokenStartLine, sourceText, column, length, KeyWord.CHARACTERKONST, lexer);
		this.value = Character.valueOf((char) value);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return ""+value;
	}
	
	public int getLspTokenType() {
		return TokenManager.STRING.index; 
	}

	@Override
	public String toString() {
		return super.toString() + ", Value: " + value;
	}

}
