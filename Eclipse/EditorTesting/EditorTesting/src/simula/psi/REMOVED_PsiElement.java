package simula.psi;

import java.util.List;

//Base interface for all PSI elements
public interface REMOVED_PsiElement {
	REMOVED_PsiElement getParent();
	List<REMOVED_PsiElement> getChildren();
	String getText();
}
