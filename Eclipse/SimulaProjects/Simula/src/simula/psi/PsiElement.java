package simula.psi;

public class PsiElement {// implements PsiElement {
    CharSequence sourceText; // Pointer to the Whole FILE
    int startOffset;
    int endOffset;
    
	public PsiTree parent;
	public String debugName;
    public int lineNumber;
	
	public PsiElement(String debugName, CharSequence sourceText) {
		this.debugName = debugName;
		this.sourceText = sourceText;
	}
	
	public String getText() { return null;}
	
	public String getOriginalText() {
		CharSequence txt = sourceText.subSequence(startOffset, endOffset);
		return txt.toString();
	}
	
	public String edPsiLine() { return null;}
	public String edHtmlLine() { return null;}
	
	public int firstLineNumber() {
		return lineNumber;
	}

	public int lastLineNumber() {
		return lineNumber;
	}

}