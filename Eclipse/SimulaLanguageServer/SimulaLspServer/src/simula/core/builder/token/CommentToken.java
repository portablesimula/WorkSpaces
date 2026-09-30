package simula.core.builder.token;

import java.util.List;

import simula.Option;
import simula.core.builder.SimulaLexer;
import simula.core.utilities.KeyWord;

public class CommentToken extends LexToken {

	public CommentToken(int tokenStartLine, List<String> sourceLines, int column, int length, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.TEXTKONST);
		super(tokenStartLine, sourceLines, column, length, KeyWord.COMMENT_TEXT, SemanticTokenTypes.Comment, lexer);
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
