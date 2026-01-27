package simula.psi;

import simula.compiler.syntaxClass.SyntaxClass;

public class REMOVED_PsiMarker extends REMOVED_PsiNode {
	
	public REMOVED_PsiMarker(String debugName) {
		super(debugName);
	}

	public void error(String string) {
		// TODO Auto-generated method stub
		
	}

	public void done(SyntaxClass cls) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return "PsiMarker("+debugName+")";
	}


}
