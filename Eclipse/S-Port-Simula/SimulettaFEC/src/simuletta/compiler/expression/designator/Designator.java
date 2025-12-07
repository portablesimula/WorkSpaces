/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.designator;

import static simuletta.utilities.Util.*;

import simuletta.compiler.expression.Expression;
import simuletta.compiler.expression.QuaOperation;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;
import simuletta.utilities.VarSet;

/**
 * Designator.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		Designator ::=  SimpleDesignator  < ( Index'Expression ) >? 
 * 				    |   SimpleDesignator  <  ArgumentList  >? 
 * 				   
 *			SimpleDesignator  ::=  Identifier  |  VarExpression  |  CallExpression
 *
 *				VarExpression  ::=  VAR  (  GeneralReference'Expression  )
 *
 * 				CallExpression ::=  CALL  Profile'Identifier ( Entry'Expression )
 *
 *
 * 				ArgumentList ::=  (  Argument  < , Argument >*  )
 * 
 *					Argument ::=  Expression  |  ( Expression  <  ,  Expression  >*  ) 				
 * 
 * </pre>
 * @author Øystein Myhre Andersen
 */
public class Designator extends Expression {
//	public int lineNumber;    // From SyntaxClass
	public VarSet varset;
	
	public Designator(VarSet varset) {
		this.varset=varset;
	}

	public static Expression parseVarCallIdentifier() {
		Parser.TRACE("Designator.parseVarCallIdentifier: INIT-1");
		Parser.saveCurrentToken();
		Parser.TRACE("Designator.parseVarCallIdentifier: INIT-2");
		return(parse());
	}
	
	static boolean TESTING=true;//false;//true;
	
//	public boolean isLegalDotList() { // Used for dotList    // TODO:NAME
//		if(currentScope.isRoutine()) return(false);
//		DesignatorElement first=varset.firstElement();
//		if(TESTING) IO.println("Designator.isLegalDotList: first="+first.getClass().getSimpleName()+"  "+first);
//		for(DesignatorElement elt:varset)
//			if(!(elt instanceof Variable var)) return(false);
//		if(first instanceof Variable var) {
//			VariableDeclaration decl=(VariableDeclaration) Declaration.findMeaning(var.identifier);
//			if(decl==null) IERR("Missing declaration of "+var.identifier);
//			if(TESTING) IO.println("Designator.isLegalDotList: decl="+decl.getClass().getSimpleName()+"  "+decl);
//			Type type=decl.type;
//			if(TESTING) IO.println("Designator.isLegalDotList: type="+type+", keyWord="+type.getKeyWord());
//			if(type.getKeyWord()==KeyWord.REF) return(false);
//		} else return(false);
//		return(true);
//	}
	
	public String getIdent(int i) { // Used for dotList
		DesignatorElement elt=varset.elementAt(i);
		if(elt instanceof Variable var) {
			if(var.argset!=null && var.argset.size() > 1) IERR("");
			return(var.identifier);
		} IERR("Illegal element in dotList: "+elt.getClass().getSimpleName()+"  "+elt);
		return(null);
	}
	
	public static Expression parse() {
		VarSet varset;
		Type type = null;
		varset=new VarSet();
		Expression designator=new Designator(varset);
  LOOP: while(true) {
			Parser.TRACE("Designator.parse: LOOP: varset="+varset);
			if(Parser.accept(KeyWord.VAR)) VarExpression.parseVarexpr(varset);
			else if(Parser.accept(KeyWord.CALL)) CallExpression.parseVarcall(varset);
			else Variable.parseIDENTIFIER(varset);

			if(Parser.accept(KeyWord.DOT)) {
				Parser.TRACE("Designator.parse: DOT");
				Parser.checkNext(KeyWord.IDENTIFIER);
				continue LOOP; //goto NXTID
			}
			if(Parser.accept(KeyWord.QUA)) {
//				Parser.TRACE("Designator.parse: QUA");
				type=Parser.acceptType(); //inType();
				if(type==null) {
					String qual=Parser.expectIdentifier();
					type=Type.Ref(qual);
				}
				if(type == null) ERROR("Illegal syntax after qua");
				if(Parser.accept(KeyWord.DOT)) {
//					Parser.TRACE("Designator.parse: QUA-DOT: Type="+type);
					varset.add(new Qualification(type));
					Parser.checkNext(KeyWord.IDENTIFIER);
					continue LOOP; //goto NXTID;
				}
				designator=new QuaOperation(designator,type);
			}
			Parser.TRACE("Designator.parse: RESULT="+designator);
//        	IO.println("Designator.parse: RESULT="+designator);
//        	if(designator.toString().equals("bio.simid.ent.cha")) {
//        		try { Thread.sleep(10);	Thread.dumpStack(); } catch (InterruptedException e) {}
//        	}
			return(designator);
		}
	}

	

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect() {
		return(doSCodingDirect(false));
	}
	
	public Type doSCodingDirect(boolean target) {
		enterLine();
		Util.BREAK("Designator.doSCodingDirect");
		Type type=null;
		if(!varset.isEmpty()) {
			for(DesignatorElement elt:varset.set) {
				boolean isLast=(elt==varset.lastElement());
				//Util.println("Designator.doSCodingDirect: "+elt.getClass().getSimpleName()+this+"  elt="+elt+", isLast="+isLast);
				type=elt.doSCodingDirect(target,isLast,type);
			}
		}
		exitLine();
		return(type);	
	}
	
	public void print(String title) {
		IO.println("Designator.print: "+title);
		if(!varset.isEmpty()) {
			for(DesignatorElement elt:varset.set) {
				IO.println("Designator.print: "+elt);
			}
		}
	}

	public String toString() {
//		StringBuilder s=new StringBuilder();
//		boolean first=true;
//		if(!varset.isEmpty()) {
//			for(DesignatorElement elt:varset.set) {
//				if(!first) s.append('.'); first=false;
//				s.append(elt);
//			}
//		}
//		return(s.toString());
		return(varset.set.toString());
	}

	
}
