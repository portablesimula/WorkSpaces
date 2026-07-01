package simula.token;

import simula.builder.SimulaLexer;
import simula.compiler.utilities.Option;
import simula.lsp.compiler.TokenManager;

public class KeyWordToken extends LexToken {
	
	public KeyWordToken(int tokenStartLine, CharSequence sourceText, int column, int length, int keyWord, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, keyWord);
		super(tokenStartLine, sourceText, column, length, keyWord, lexer);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

	
	public int getLspTokenType() {
		return TokenManager.KEYWORD.index; 
	}

//	@Override
//	public String toString() {
//		return super.toString() + ", KeyWord: " + KeyWord.edit(keyWord);
//	}

}
