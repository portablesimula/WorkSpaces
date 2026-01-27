package simula.psi;

import java.util.ArrayList;
import java.util.List;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.Util;

//Base class for composite nodes (branching nodes)
public class PsiTree extends PsiElement {
	protected final List<PsiElement> children = new ArrayList<>();
	private String error;
	
	public PsiTree(String debugName, PsiTree parent) {
		super(debugName);
		this.parent = parent;
	}

	public void addChild(PsiElement child) {
//		if (child instanceof BasePsiElement) {
//			((BasePsiElement) child).setParent(this); 
//		}
		if(child == null) Util.IERR("addChild NULL !!");
		children.add(child);
	}

	public List<PsiElement> getChildren() { return children; }
	
	@Override public int getLineNumber() {
		try {
			PsiElement firstChild = children.getFirst();
			if(firstChild != null) return firstChild.getLineNumber();
		} catch(Exception e) { }
		return -1;
	}

	@Override public String getText() {
//		IO.println("PsiTree.getText: " + debugName);
		StringBuilder sb = new StringBuilder();
		for(PsiElement child:children) {
//			IO.println("PsiTree.getText: child: " + child);
			sb.append(child.getText());
		}
//		IO.println(("PsiTree.getText: " + debugName + " ==> \"" + sb + '"').replace("\n", "\\n").replace("\r", "\\r"));
		return sb.toString();
	}
	
    public void printTree(String title) {
    	IO.println("****** PrintTree: " + title + " ******");
    	printTree(this, 1);
    }

    private static void printTree(PsiElement element, int depth) {
    	int line = element.getLineNumber();
    	String text = element.getText().replace("\r", "\\r").replace("\n", "\\n");
    	System.out.println("  ".repeat(depth) + "Line " + line + ": " + element.getClass().getSimpleName() + "("+element.debugName+"): [" + text + "]");
        if(element instanceof PsiTree subTree) {
	        for (PsiElement child : subTree.getChildren()) {
	            printTree(child, depth + 1);
	        }
        }
    }

	@Override public String toString() {
		return "PsiTree(" + debugName + ") Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
	}

}