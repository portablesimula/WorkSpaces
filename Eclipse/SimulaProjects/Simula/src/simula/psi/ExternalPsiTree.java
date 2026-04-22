package simula.psi;

import simula.compiler.utilities.Util;

//public class ExternalPsiTree extends PsiElement {
public class ExternalPsiTree extends PsiTree {
	int lastLine;
	
	public ExternalPsiTree(String debugName, int firstLineNumber, int lastLineNumber) {
//		super(debugName, firstLineNumber, null);
//		super(PsiTree parent, int tokenStartLine, CharSequence sourceText, int startOffset, String debugName) {
		super(null, firstLineNumber, null, 0, debugName);
//		this.lineNumber = firstLineNumber;
		this.lastLine = lastLineNumber;
		Util.IERR("DETTE MÅ SJEKKES");
	}

	@Override
	public int firstLineNumber() {
		// TODO Auto-generated method stub
		return lineNumber;
	}

	@Override
	public int lastLineNumber() {
		return lastLine;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

}
