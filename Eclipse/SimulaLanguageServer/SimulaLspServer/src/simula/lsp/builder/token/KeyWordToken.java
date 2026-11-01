package simula.lsp.builder.token;

import simula.lsp.builder.SimulaBuilder;
import simula.lsp.utilities.Option;

public class KeyWordToken extends LexToken {
	
	public KeyWordToken(SimulaBuilder simBuilder, int tokenStartLine, int column, int length, int keyWord) {
		super(simBuilder, tokenStartLine, column, length, keyWord, SemanticTokenTypes.Keyword);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

//	@Override
//	public String toString() {
//		return super.toString() + ", KeyWord: " + KeyWord.edit(keyWord);
//	}

}
