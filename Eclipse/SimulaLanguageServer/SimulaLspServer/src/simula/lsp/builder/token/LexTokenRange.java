package simula.lsp.builder.token;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

import simula.lsp.DocumentManager;
import simula.lsp.builder.SimulaBuilder;
import simula.lsp.coder.SimulaCoder;
import simula.lsp.utilities.Util;

public class LexTokenRange {
	
	public LexToken firstParserToken;
	public LexToken lastParserToken;

	public LexTokenRange() {}

	public LexTokenRange(LexToken firstParserToken) {
		this.firstParserToken = firstParserToken;
		this.lastParserToken = firstParserToken;
	}
	
	public LexTokenRange(LexToken firstParserToken, LexToken lastParserToken) {
		this.firstParserToken = firstParserToken;
		this.lastParserToken = lastParserToken;
	}

	public DocumentManager getDocumentManager() {
		if(firstParserToken == null) return null;
		return firstParserToken.simBuilder.documentManager;
	}
	
	public SimulaBuilder getSimBuilder() {
		if(firstParserToken == null) return null;
		return firstParserToken.simBuilder;
	}

	public SimulaCoder getSimCoder() {
		if(firstParserToken == null) return null;
		return firstParserToken.simBuilder.documentManager.simCoder;
	}

	public void setFirstParserToken(LexToken lexToken) {
		firstParserToken = lexToken;
		if(lastParserToken == null) lastParserToken = lexToken;
	}
	public void setLastParserToken(LexToken lexToken) {
		lastParserToken = lexToken;
		if(firstParserToken == null) firstParserToken = lexToken;
	}
	public LexToken getFirstParserToken() {
		return firstParserToken;
	}
	public LexToken getLastParserToken() {
		return lastParserToken;
	}
	
	public Range getLexRange() {
		Position firstLexPosition;
		Position lastLexPosition;
		if(firstParserToken != null) {
			firstLexPosition = new Position(firstParserToken.lineNumber, firstParserToken.column);
			lastLexPosition = new Position(lastParserToken.lineNumber, lastParserToken.column + lastParserToken.length);
		} else {
			firstLexPosition = new Position(0, 0);
			lastLexPosition = new Position(0, 0);			
		}
		Range lexRange = new Range(firstLexPosition, lastLexPosition);
		return lexRange;
	}

	/// The first source line number
	public int firstLineNumber() {
		if(firstParserToken == null) return 0;
		return firstParserToken.firstLineNumber();			
	}

	/// The last source line number
	public int lastLineNumber() {
		if(lastParserToken == null) return 0;
		return lastParserToken.lastLineNumber();			
	}
	
	@Override
	public String toString() {
		return Util.edRange(getLexRange());
	}

}
