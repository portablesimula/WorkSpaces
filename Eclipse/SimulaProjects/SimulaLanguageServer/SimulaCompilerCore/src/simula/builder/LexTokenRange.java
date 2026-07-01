package simula.builder;

import java.util.Vector;

import simula.compiler.syntaxClass.SyntaxElement;
import simula.token.LexToken;

public class LexTokenRange {
	LexTokenRange parent;
	
//	public LexToken firstLexToken;    // TESTING_WITHOUT_PSI
//	public LexToken lastLexToken;     // TESTING_WITHOUT_PSI
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
		return sb.toString().replace("\r", "\\r").replace("\n", "\\n");
	}
	
	@Override
	public String toString() {
		return "LexTokenRange[" + getFirstLexToken() + " : "  + getLastLexToken() + "]";
	}
}
