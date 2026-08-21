package simula.core.builder.util;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.builder.export.LexToken;
import simula.core.builder.export.TokenManager;

public class KeyWordToken extends LexToken {
	
	public KeyWordToken(int tokenStartLine, CharSequence sourceText, int column, int length, int keyWord, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, keyWord);
		super(tokenStartLine, sourceText, column, length, keyWord, "symbol", lexer);
		if(tokenText.length() > 0 && Character.isLetter(tokenText.charAt(0))) tokenTypeIndex = TokenManager.tokenTypes.indexOf("keyword");
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

//	@Override
//	public String toString() {
//		return super.toString() + ", KeyWord: " + KeyWord.edit(keyWord);
//	}

}
