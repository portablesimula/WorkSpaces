package simula.psi;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PsiTreeIterator implements Iterator<PsiElement> {
	
	public static boolean TRACING = false;
	
	private final ArrayDeque<PsiElement> stack = new ArrayDeque<>();
	
	public PsiTreeIterator(PsiElement root) {
		if(TRACING) IO.println("NEW PsiTreeIterator ROOT: "+root.edText());
        if (root != null) {
            stack.push(root);
			if(TRACING) IO.println(" Push "+root.edText() + " ==> STACK: " + edStack());
        }
	}

	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	@Override
    public PsiElement next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        PsiElement current = stack.pop();
		if(TRACING) IO.println(" ".repeat(50) + " Pop  [" + current.firstLineNumber() + "]" + current.edText() + " ==> STACK: " + edStack());
        
        List<PsiElement> children = current.getChildren();
        if (children != null) {
            for (int i = children.size() - 1; i >= 0; i--) {
				PsiElement child = children.get(i);
                if (child != null) {
                    stack.push(child);
					if(TRACING) IO.println(" ".repeat(10) + " Push "+child + '\n' + " ".repeat(55) + " ==> STACK: " + edStack());
                }
            }
        }
        return current;
	}
	
	public String edStack() {
		StringBuilder sb = new StringBuilder();
		PsiElement[] arr = stack.toArray(new PsiElement[0]);
		for(PsiElement elt:arr) {
			sb.append("[" + elt.firstLineNumber() + "]" + elt.edText()).append(", ");			
		}
		return sb.toString();
	}
}
