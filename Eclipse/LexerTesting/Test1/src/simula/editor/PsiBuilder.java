package simula.editor;

import simula.lexer.SimulaLexer;

public class PsiBuilder {

	public void start(CharSequence txt) {
        SimulaLexer lexer = new SimulaLexer();
		CharSequence buffer = txt;
		int startOffset = 0;
		int endOffset = buffer.length();
		int initialState = 0;
	    lexer.start(buffer, startOffset, endOffset, initialState);
		
	}

}
