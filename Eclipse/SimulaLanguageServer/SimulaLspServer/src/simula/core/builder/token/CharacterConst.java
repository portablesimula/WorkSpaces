package simula.core.builder.token;

import java.util.List;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.utilities.KeyWord;

public class CharacterConst extends LexToken {
	public final Character value;

	public CharacterConst(int tokenStartLine, List<String> sourceLines, int column, int length, int value, SimulaLexer lexer) {
		super(tokenStartLine, sourceLines, column, length, KeyWord.CHARACTERKONST, SimulaTokenTypes.Character, lexer);
		this.value = Character.valueOf((char) value);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	@Override
	public String edText() {
		return ""+value;
	}

	@Override
	public String toString() {
		return super.toString() + ", Value: " + value;
	}

}
