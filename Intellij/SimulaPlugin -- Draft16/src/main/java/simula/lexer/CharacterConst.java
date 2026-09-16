package simula.lexer;

import simula.compiler.utilities.KeyWord;

public class CharacterConst extends SimulaToken {
	public final int value;

	public CharacterConst(CharSequence sourceText, int startOffset, int endOffset, int value) {
		super(sourceText, startOffset, endOffset, KeyWord.CHARACTERKONST, "CHARACTERKONST");
		this.value = value;
	}

}
