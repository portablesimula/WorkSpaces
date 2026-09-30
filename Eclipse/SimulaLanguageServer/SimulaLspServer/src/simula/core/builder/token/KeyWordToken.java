package simula.core.builder.token;

import java.util.List;

import simula.Option;
import simula.core.builder.SimulaLexer;

public class KeyWordToken extends LexToken {
	
	public KeyWordToken(int tokenStartLine, List<String> sourceLines, int column, int length, int keyWord, SimulaLexer lexer) {
		super(tokenStartLine, sourceLines, column, length, keyWord, SemanticTokenTypes.Keyword, lexer);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

//	@Override
//	public String toString() {
//		return super.toString() + ", KeyWord: " + KeyWord.edit(keyWord);
//	}

}
