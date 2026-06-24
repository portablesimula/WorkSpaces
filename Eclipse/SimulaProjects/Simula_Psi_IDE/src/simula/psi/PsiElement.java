package simula.psi;

import java.util.List;
import javax.swing.text.Style;
import simula.editor.PsiTextPanel;

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

	public List<PsiElement> getChildren() {
		return null;
	}
	
	public String getText() { return null;}


	public String edText() { return null; }

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

	/// Returns the style for this PsiElement.
	/// @return the style for this PsiElement
	public Style getStyle(final PsiTextPanel psiText) {
		return psiText.styleRegular;
	}

}