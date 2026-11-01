package simula.lsp.builder.token;

import simula.lsp.builder.SimulaBuilder;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.Option;

public class CommentToken extends LexToken {

	public CommentToken(SimulaBuilder simBuilder, int tokenStartLine, int column, int length) {
		super(simBuilder, tokenStartLine, column, length, KeyWord.COMMENT_TEXT, SemanticTokenTypes.Comment);
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
	}

//	@Override
//	public String edText() {
//		return value;
//	}
//
//	@Override
//	public String toString() {
//		return super.toString() + ", Value: \"" + value + '"';
//	}
}
