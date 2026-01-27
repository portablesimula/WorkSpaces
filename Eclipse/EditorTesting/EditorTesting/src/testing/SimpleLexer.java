package testing;

import simula.psi.PsiTree;
import simula.psi.PsiElement;
import simula.psi.REMOVED_PsiElement;

//Simplified Lexer
class SimpleLexer {
	private final String input;
	private int pos = 0;

	public SimpleLexer(String input) { this.input = input; }

	public String nextToken() {
		while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) pos++;
		if (pos >= input.length()) return null;
		int start = pos;
		if (Character.isLetterOrDigit(input.charAt(pos))) {
			while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) pos++;
		} else {
			pos++;
		}
		return input.substring(start, pos);
	}
}

//Specific PSI Nodes
class IdentifierPsi extends PsiElement {
	public IdentifierPsi(String text) { super(text); }
}

class ExpressionPsi extends PsiTree {
	@Override public String getText() {
		return children.stream().map(REMOVED_PsiElement::getText).reduce("", (a, b) -> a + " " + b);
	}
}
