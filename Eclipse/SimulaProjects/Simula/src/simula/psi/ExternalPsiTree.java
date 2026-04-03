package simula.psi;

//public class ExternalPsiTree extends PsiElement {
public class ExternalPsiTree extends PsiTree {
	int lastLine;
	
	public ExternalPsiTree(String debugName, int firstLineNumber, int lastLineNumber) {
		super(debugName, firstLineNumber, null);
//		this.lineNumber = firstLineNumber;
		this.lastLine = lastLineNumber;
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
