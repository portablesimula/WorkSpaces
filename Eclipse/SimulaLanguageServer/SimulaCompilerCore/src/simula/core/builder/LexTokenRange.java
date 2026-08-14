package simula.core.builder;

import java.util.Vector;

import simula.core.builder.token.LexToken;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.utilities.Util;

public class LexTokenRange {
	LexTokenRange parent;
	
	Vector<LexToken> lexTokenRange;

	public Vector<SyntaxElement> syntaxElements;
	
	public LexTokenRange(LexTokenRange parent) {
		this.parent = parent;
		lexTokenRange = new	Vector<LexToken>();
	}

	public LexToken getFirstLexToken() {
		return lexTokenRange.firstElement();
	}

	public LexToken getLastLexToken() {
		return lexTokenRange.lastElement();
	}

	public void addChild(LexToken token) {
		lexTokenRange.add(token);
	}
	
	/// NOTE: DEBUG ONLY
	public String getDebugText() {
		StringBuilder sb = new StringBuilder();
		for(LexToken token:lexTokenRange) {
			sb.append(token.getText());
		}
		return Util.printable(sb.toString());
	}
	
	@Override
	public String toString() {
		return "LexTokenRange[" + getFirstLexToken() + " : "  + getLastLexToken() + "]";
	}
}
