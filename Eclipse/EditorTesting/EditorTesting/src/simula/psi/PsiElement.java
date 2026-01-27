package simula.psi;

//Base class for leaf nodes (tokens)
//public class LeafPsiElement implements PsiElement {
public abstract class PsiElement {// implements PsiElement {
//	private final String text;
	public String debugName;
	public PsiTree parent;
	
	public PsiElement(String debugName) {
		this.debugName = debugName;
	}

//	public PsiElement getParent() { return parent; }
//	public void setParent(PsiTree parent) { this.parent = parent; }
	public abstract int getLineNumber();
	public abstract String getText();
}