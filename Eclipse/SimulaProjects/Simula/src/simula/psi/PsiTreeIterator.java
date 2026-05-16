package simula.psi;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class PsiTreeIterator implements Iterator<PsiElement> {
	public static boolean TRACING = false;
	private final Stack<PsiElement> stack;

	public PsiTreeIterator(PsiElement root) {
		if(TRACING) IO.println("NEW PsiTreeIterator ROOT: "+root.edText());
		stack = new Stack<PsiElement>();
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

		// Hent neste node fra toppen av stakken
		PsiElement current = stack.pop();
		if(TRACING) IO.println(" Pop  "+current.edText() + " ==> STACK: " + edStack());

		if(current instanceof PsiTree psiTree) {
			// Legg til barn i omvendt rekkefølge på stakken
			// Dette sikrer at det første barnet behandles først (Pre-order)
			for (int i = psiTree.children.size() - 1; i >= 0; i--) {
				PsiElement elt = psiTree.children.get(i);
//				if(!(elt instanceof PsiTree)) {
					stack.push(elt);
					if(TRACING) IO.println(" Push "+elt.edText() + " ==> STACK: " + edStack());
//				}
			}
		}

		return current;
	}
	
	public String edStack() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<stack.size();i++) {
			sb.append(stack.get(i).edText()).append(", ");
		}
		return sb.toString();
	}
}
