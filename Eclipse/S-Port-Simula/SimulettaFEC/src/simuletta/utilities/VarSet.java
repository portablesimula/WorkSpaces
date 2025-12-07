package simuletta.utilities;

import static simuletta.utilities.Util.*;

import java.util.Vector;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.expression.designator.DesignatorElement;
import simuletta.compiler.expression.designator.Variable;
import simuletta.type.InfixType;
import simuletta.type.Type;

public class VarSet {
	public Vector<DesignatorElement> set;

	public VarSet() {
		set=new Vector<DesignatorElement>();
	}
	
	public int size() { return(set.size()); }	
	public boolean isEmpty() { return(set.isEmpty()); }
	public DesignatorElement elementAt(int i) { return(set.elementAt(i)); }
	public DesignatorElement firstElement() { return(set.firstElement()); }
	public DesignatorElement lastElement() { return(set.lastElement()); }
	public void add(DesignatorElement elt) { set.add(elt); }
	public void removeElementAt(int i) { set.removeElementAt(i); }


	static boolean TESTING=false;//true;

	public boolean isLegalDotList() {   // TODO:NAME
		if(TESTING) IO.println("VarSet.isLegalDotList("+this+")");
		boolean res=isLegalDotList2();
//		IO.println("VarSet.isLegalDotList("+this+")  ==>  "+res);
//		Util.STOP();
		
		return(res);
	}
	
	public boolean isLegalDotList2() {   // TODO:NAME
//		if(Global.currentScope.isRoutine()) return(false);
		Type encType=null;
		DesignatorElement first=set.firstElement();
		if(TESTING) IO.println("VarSet.isLegalDotList: first="+first.getClass().getSimpleName()+"  "+first);
		if(first instanceof Variable var) {
			if(var.argset!=null && var.argset.size()!=0) return(false);
			VariableDeclaration decl=(VariableDeclaration) Declaration.findMeaning(var.identifier);
			if(decl==null) IERR("Missing declaration of "+var.identifier);
			if(TESTING) IO.println("VarSet.isLegalDotList: decl="+decl.getClass().getSimpleName()+"  "+decl);
			encType=decl.type;
			if(TESTING) IO.println("VarSet.isLegalDotList: type="+encType+", keyWord="+encType.getKeyWord());
		} else return(false);
		
		int n=set.size();
		for(int i=1;i<n;i++) {
			if(!(encType instanceof InfixType)) return(false);
			DesignatorElement elt=set.elementAt(i);
			if(elt instanceof Variable xvar) {
				if(xvar.argset!=null && xvar.argset.size()!=0) return(false);
				Record rec=encType.getQualifyingRecord();
				VariableDeclaration attr=rec.findAttribute(xvar.identifier);
				if(TESTING) IO.println("VarSet.isLegalDotList: attr="+attr.getClass().getSimpleName()+"  "+attr);
				encType=attr.type;
				if(TESTING) IO.println("VarSet.isLegalDotList: type="+encType+", keyWord="+encType.getKeyWord());
			} else return(false);
			
		}
		return(true);
	}
	
//	private VarSet splitVarSet() {
//		VarSet head=new VarSet();
//		Type encType=null;
//	LOOP:while(set.size()>0) {
//			IO.println("UnaryOperation.splitVarSet: head="+head+", rest="+set);
//			DesignatorElement elt=set.firstElement();
//			if(elt instanceof Variable var) {
//				if(encType!=null) {
//					Record rec=encType.getQualifyingRecord();
//				}
//				VariableDeclaration decl=(VariableDeclaration) Declaration.findMeaning(var.identifier);
//				if(decl==null) IERR("Missing declaration of "+var.identifier);
//				IO.println("UnaryOperation.splitVarSet: decl="+decl.getClass().getSimpleName()+"  "+decl);
//				encType=decl.type;
//				IO.println("UnaryOperation.splitVarSet: type="+encType+", keyWord="+encType.getKeyWord());
//				if(! (encType instanceof InfixType)) break LOOP;
//				head.add(elt); set.removeElementAt(0);
//				IO.println("UnaryOperation.splitVarSet: head="+head+", rest="+set);
//				Util.STOP();
//			} else IERR("ANDRE ALTERNATIVER-1 ???");
//		}
//		return(head);
//	}

	public String toString() {
		StringBuilder s=new StringBuilder();
		boolean first=true;
		if(!set.isEmpty()) {
			for(DesignatorElement elt:set) {
				if(!first) s.append('.'); first=false;
				s.append(elt);
			}
		}
		return(s.toString());
	}

}
