/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.value;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.util.Stack;

import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;
import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.expression.UnaryOperation;
import simuletta.compiler.expression.designator.Designator;
import simuletta.compiler.expression.designator.DesignatorElement;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Tag;

/**
 * GeneralAddress.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		GeneralReferenceValue ::= NONAME  |  NAME ( Variable )
 * 
 * 
 * 		GeneralReferenceValue ::= NONAME  |  NAME ( record'Identifier < . Identifier >+ )    TODO: ?????
 * 
 * 
 *  S-Code:
 *  
 *  	GeneralAddress ::= GNONE  |  C_GADDR global_or_const:tag
 * 
 * </pre>
 * 
 * The value is the general address of the defined global or constant quantity
 * designated by the lexically first tag. The general address of a sub-component
 * of a structure component may be given by means of the c-dot construction;
 * this is interpreted as for attribute addresses. gnone is the address of no
 * atomic unit. The interpretation of the construction
 * <p>
 * c-dot T1 c-dot T2 c-gaddr T3 is "T3.T2.T1".GADDR
 * 
 * 
 * @author Øystein Myhre Andersen
 */
public class GeneralAddress extends Value {
	public Designator dotList;

	public GeneralAddress() {}

	public GeneralAddress(Designator dotList) {
		this.dotList=dotList;
        if(TESTING) IO.println("NEW GeneralAddress:"+this+" ***********************************************************");
//		try { Thread.sleep(10);	Thread.dumpStack(); } catch (InterruptedException e) {}
	}
	
	public static boolean TESTING=false;//true;

	public static Expression parse() {
        if(TESTING) IO.println("GeneralAddress.parse:");
        Parser.expect(KeyWord.BEGPAR);
        
        Designator designator=(Designator) Designator.parse();
        if(TESTING) IO.println("GeneralAddress.parse: designator="+designator.getClass().getSimpleName()+"  "+designator);
        Parser.expect(KeyWord.ENDPAR);
        if(designator.varset.isLegalDotList()) {   // TODO:NAME
        	GeneralAddress gaddr=new GeneralAddress((Designator)designator);
            if(TESTING) IO.println("GeneralAddress.parse: gaddr="+gaddr);
            return(gaddr);
       } else {    	   
    	   Expression result=new UnaryOperation(S_NAME,designator);
    	   if(TESTING) IO.println("GeneralAddress.parse: result="+result);
    	   return(result);
       }
	}


	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
//	public Type doOutConst() {
//        if(TESTING) IO.println("GeneralAddress.doOutConst:"+this+" ***********************************************************");
//		enterLine();
//		if(dotList==null) {			
//			sCode.outinst(S_GNONE); sCode.outcode(); 
//			return(Type.Name);
//		} 
//		String ident=dotList.getIdent(0);
//		VariableDeclaration q=(VariableDeclaration) Declaration.findMeaning(ident);
//		Tag tag=q.getTag();
//		for(int i=1;i<dotList.varset.size();i++) {
//			ident=dotList.getIdent(i);
//            if(q!=null) {
//            	sCode.outinst(S_C_DOT);
//            	sCode.outtag(tag);
//            }
//			String refIdent=q.type.getRefIdent();
//			Record rec=(Record) Declaration.findMeaning(refIdent);
//			q=rec.findAttribute(ident);
//			tag=q.getTag();
//			rec=q.type.getQualifyingRecord();
//		}
//		sCode.outinst(S_C_GADDR); sCode.outtag(tag); sCode.outcode();
//		exitLine();
//		return(Type.Name(q.type));
//	}
	public Type doOutConst() {
        if(TESTING) IO.println("GeneralAddress.doOutConst:"+this+" ***********************************************************");
		enterLine();
		if(dotList==null) {			
			sCode.outinst(S_GNONE); sCode.outcode(); 
			return(Type.Name);
		} 
		String ident=dotList.getIdent(0);
		VariableDeclaration q=(VariableDeclaration) Declaration.findMeaning(ident);
		Tag tag=q.getTag();
//		IO.println("GeneralAddress.doOutConst: "+ident+"  q="+q);
		Stack<Tag> qStack = new Stack<Tag>();
		qStack.push(tag);
		for(int i=1;i<dotList.varset.size();i++) {
			ident=dotList.getIdent(i);
			String refIdent=q.type.getRefIdent();
			Record rec=(Record) Declaration.findMeaning(refIdent);
			q=rec.findAttribute(ident);
			tag=q.getTag();
			qStack.push(tag);
		}
		while(!qStack.empty()) {
			tag = qStack.pop();
			if(qStack.empty()) {
				sCode.outinst(S_C_GADDR); sCode.outtag(tag); sCode.outcode();				
			} else {
            	sCode.outinst(S_C_DOT); sCode.outtag(tag);
			}
		}
		exitLine();
		return(Type.Name(q.type));
	}
	
	public String toString() {
		if(dotList==null) return("GNONE");
		StringBuilder s=new StringBuilder();
		s.append("NAME(").append(dotList).append(')');
		return(s.toString());
	}


}
