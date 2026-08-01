package simula.builder;

import java.util.Vector;

import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.utilities.Util;
import simula.token.LexToken;

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
