package simula.psi;

import simula.compiler.utilities.Util;

public class REMOVED_PsiNode {
	String debugName;
	REMOVED_PsiNode suc;
	REMOVED_PsiNode prv;
	
	public REMOVED_PsiNode(String debugName) {
		this.debugName = debugName;
	}
	
	public void follow(REMOVED_PsiNode x) {
		prv = x.prv;
		if(x.suc != null) {
			x.suc.prv = x;
			x.suc = this;
		}
		
		IO.println("PsiNode.follow: x.suc="+x.suc.debugName+", x.prv="+x.prv.debugName);
		IO.println("PsiNode.follow: this.suc="+this.suc.debugName+", this.prv="+this.prv.debugName);
		Util.IERR("");
	}
	
	public void precede(REMOVED_PsiNode x) {
		suc = x.suc;
		if(x.prv != null) {
			x.prv.suc = x;
			x.prv = this;
		}
		
		IO.println("PsiNode.precede: x.suc="+x.suc.debugName+", x.prv="+x.prv.debugName);
		IO.println("PsiNode.precede: this.suc="+this.suc.debugName+", this.prv="+this.prv.debugName);
		Util.IERR("");
	}

}
