/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.designator;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.util.Vector;

import simuletta.compiler.common.ProgramPoint;
import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.LabelDeclaration;
import simuletta.compiler.declaration.Profile;
import simuletta.compiler.declaration.InlineRoutine;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.declaration.scope.*;
import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.expression.value.Value;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.Util;
import simuletta.utilities.VarSet;

/**
 * Variable.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 *		Variable  ::=  Identifier   <  ArgumentList  >? 
 *
 * 			ArgumentList ::=  (  Argument  < , Argument >*  )
 * 
 *				Argument ::=  Expression  |  ( Expression  <  ,  Expression  >*  ) 				
 * 
 * </pre>
 * @author Øystein Myhre Andersen
 */
public class Variable extends SimpleDesignator {
//	public int lineNumber;     // From SyntaxClass
//	Vector<Expression> argset; // From SimpleDesignator
	public final String identifier;
	
	private Variable(String identifier,Vector<Expression> argset) {
		super(argset);
		this.identifier=identifier;
	}
	
	static void parseIDENTIFIER(VarSet varset) {
//		Parser.TRACE("Variable.parse: IDENTIFIER");
		String id=Parser.expectIdentifier();
		Vector<Expression> argset=parseArgumentSet();
		Variable varvar=new Variable(id,argset);
		Parser.TRACE("Variable.parseIDENTIFIER: END IDENTIFIER: Varvar="+varvar+", argset="+argset);
		varset.add(varvar);
	}

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect(boolean target,boolean isLast,Type prevType) {
		Variable var=this;//(Variable)v;
		Type vartype;
		
		enterLine();
		
		Util.TRACE("S_VARID  isLast="+isLast+", v="+this);
		
		Declaration meaning;
		if(prevType!=null) {
//			IO.println("Variable.doSCodingDirect: prevType="+prevType);
			Record remote=prevType.getQualifyingRecord();
			Util.TRACE("remote="+remote+", QUAL="+remote.getClass().getSimpleName());
			meaning=remote.findAttribute(var.identifier);
		} else meaning=Declaration.findMeaning(var.identifier);
		if(meaning==null) Util.ERROR("Missing Declaration of "+var.identifier);
    	if(meaning instanceof InlineRoutine) vartype=((InlineRoutine)meaning).callSystemFunction(var.argset);
    	else if(meaning instanceof LabelDeclaration) {
    		LabelDeclaration lab=(LabelDeclaration)meaning;
    		if(lab.global) {
    			vartype=Type.Label;
    			sCode.outinst(S_PUSHC); sCode.outinst(S_C_PADDR);
    			sCode.outtag(lab.getTag()); sCode.outcode();
    		} else {
    			if(lab.destinationIndex==0) ProgramPoint.grabDestTabEntry(lab);
    			ProgramPoint.curdest=lab.destinationIndex; vartype=Type.Destination;
    		}
    	}
    	else if(meaning instanceof RoutineDeclaration routine) {
    		sCode.outinst(S_PRECALL); sCode.outtag(routine.getTag());
    		sCode.outcode(+1);
    		vartype=rutcall(routine.signatur,var.argset);
    		sCode.outinst(S_CALL); sCode.outtag(routine.getBodyTag());
    		sCode.outcode(-1);
    	}
    	else if(meaning instanceof DeclaredBody) {
    		DeclaredBody rut=(DeclaredBody)meaning;
    		Profile prf=(Profile) Declaration.findMeaning(rut.profileIdentifier);

    		sCode.outinst(S_PRECALL); sCode.outtag(prf.getTag());
    		sCode.outcode(+1);
    		vartype=rutcall(prf.signatur,var.argset);
    		if(rut.getTag()==null) {
    			sCode.outinst(S_ROUTINESPEC);
    			sCode.outtagid(rut.getTag());
    			sCode.outtagid(prf.getTag()); sCode.outcode();
    		}
    		sCode.outinst(S_CALL); sCode.outtag(rut.getTag()); sCode.outcode();
    	}
    	else { // Ordinary Variable-Quant
    		Util.TRACE("Ordinary Variable-Quant -- meaning="+meaning);
    		VariableDeclaration quant=(VariableDeclaration)meaning;
    		vartype=quant.type;
    		
    		boolean vmode=(isLast)?(!target):(!vartype.isReferenceType());
//    		boolean vmode=(isLast)?(!target):(vartype.isInfixType());
//    		boolean vmode=(isLast)?(!target):(vartype.isReferenceType());
//    		boolean vmode=(isLast)?(!target):true;
    		
            Util.TRACE("Variable type: "+vartype);
            boolean valueMode=vmode && argset==null;
//            IO.println("Variable.doSCodingDirect: "+var+", prefType="+prevType+", valueMode="+valueMode+", vartype="+vartype);
            
            boolean pushc_done=false;
            
			if(prevType!=null) {
//	            IO.println("Variable.doSCodingDirect: "+var+", prefType="+prevType+", valueMode="+valueMode+", vartype="+vartype);
//	            IO.println("Variable.doSCodingDirect: prefType="+prevType+", islaste="+isLast);
				
				if(prevType.isInfixType() && !isLast) {
//		            IO.println("Variable.doSCodingDirect: prefType="+prevType+", islaste="+isLast+", valueMode="+valueMode);
//		            valueMode=true;
				}
				
	             if(prevType.isInfixType()) {
	            	 sCode.outinst((valueMode)?S_SELECTV:S_SELECT);
	             } else if(prevType.isRefType()) {
	            	 sCode.outinst((valueMode)?S_REMOTEV:S_REMOTE);
	             }
	             else { ERROR("Remote access through non-ref variable"); return(null); } 
			} else {
//				sCode.outinst((valueMode)?S_PUSHV:S_PUSH);
				if(!valueMode) sCode.outinst(S_PUSH);
				else
//					if(!quant.read_only)
						sCode.outinst(S_PUSHV);   // TODO: TESTING PUSHC
//				else {
//					sCode.outinst(S_PUSHC);
//					//quant.outConstant();
//					if(quant.sysid != null) Util.NOTIMPL("PUSHC sysid"); // Nothing
//					else if(quant.initval == null) {
//						quant.type.toDefaultSCode();
//					} else {
//						Value val=quant.initval.firstElement();
//						//IO.println("Variable.doSCodingDirect: "+val);
//						val.doOutConst();
//					}
//					//Util.FORCED_EXIT();
//					pushc_done=true;
//				}
			}
            if(!pushc_done) {
            	sCode.outtag(quant.getTag()); sCode.outcode();
            	if(var.argset != null) {
            		if(var.argset.size() != 1) ERROR("Only one index allowed");
            		Type type=var.argset.firstElement().doSCodingDirect();
            		if(!type.isIntegerType()) ERROR("Index is not integer");
            		sCode.outinst((vmode)?S_INDEXV:S_INDEX); sCode.outcode();
            	}
            }
    	}

		Util.TRACE("vartype="+vartype+", var="+var+", QUAL="+var.getClass().getSimpleName());

		exitLine();
		
    	return(vartype);
	}
	
	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append((identifier==null)?"UNDEFINED_VARIABLE":identifier);
		s.append(edArgset());
		return(s.toString());
	}

    
}
