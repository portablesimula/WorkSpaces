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
import static simuletta.compiler.common.S_Instructions.S_FETCH;
import static simuletta.compiler.common.S_Instructions.S_INDEX;
import static simuletta.compiler.common.S_Instructions.S_INDEXV;
import static simuletta.compiler.common.S_Instructions.S_REFER;
import static simuletta.compiler.expression.Expression.*;

import java.util.Vector;

import simuletta.compiler.expression.Expression;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;
import simuletta.utilities.VarSet;

/**
 * VarExpression.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 *		VarExpression  ::=  VAR  (  GeneralReference'Expression  )  <  ArgumentList  >? 
 *
 * 			ArgumentList ::=  (  Argument  < , Argument >*  )
 * 
 *				Argument ::=  Expression  |  ( Expression  <  ,  Expression  >*  ) 				
 * 
 * </pre>
 * @author Øystein Myhre Andersen
 */
public class VarExpression extends SimpleDesignator {
//	public int lineNumber;     // From SyntaxClass
//	Vector<Expression> argset; // From SimpleDesignator
	final Expression generalReference;
	
	private VarExpression(Expression generalReference,Vector<Expression> argset) {
		super(argset);
		this.generalReference=generalReference;
	}

	static void parseVarexpr(VarSet varset) {
		// VarExpression  ::=  VAR  (  GeneralReference'Expression  )
		Parser.TRACE("VarExpression.parseVarexpr: VAR Expression");
		Parser.expect(KeyWord.BEGPAR);
		Expression generalReference=scan_expr(getprim());
		Parser.expect(KeyWord.ENDPAR);
		Vector<Expression> argset=parseArgumentSet();
		VarExpression varexpr=new VarExpression(generalReference,argset);
		Parser.TRACE("Designator.parseVarexpr: END VAR: Varexpr="+varexpr);
		varset.add(varexpr);
	}

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect(boolean target,boolean isLast,Type prevType) {
		VarExpression var=this;
		enterLine();
			Util.TRACE("S_VAREXPR  isLast="+isLast+", v="+this);
			Type vartype=var.generalReference.doSCodingDirect(); // Outexpr
			Util.TRACE("S_VAREXPR  vartype="+vartype);
			if(vartype.isNameType()) {
				Util.TRACE("S_VAREXPR  nameType="+vartype);
				vartype=vartype.qualifyingType();
				sCode.outinst(S_REFER); sCode.outtype(vartype); sCode.outcode();
			} else ERROR("var(-- not name --)");
// 			boolean valueMode=(isLast)?(!target):vartype.isReferenceType();  // TODO: ER DETTE RIKTIG ?
 			boolean valueMode=(isLast)?(!target):vartype.isRefType();
//			Util.println("VarExpression.doSCodingDirect: "+this+"   isLast="+isLast+", valueMode="+valueMode+", vartype="+vartype+", isReferenceType="+vartype.isReferenceType());
			if(var.argset != null) {
				int n=var.argset.size();
				if(n>1) ERROR("Only one index allowed");
 				Expression expr=var.argset.firstElement();
				Type type=expr.doSCodingDirect();
				if(!type.isIntegerType()) ERROR("Index is not integer");
				sCode.outinst((valueMode)?S_INDEXV:S_INDEX); sCode.outcode();
			} else if(valueMode) { sCode.outinst(S_FETCH); sCode.outcode();
//			STOP();
			}
		exitLine();
    	return(vartype);
	}
	
	
	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append("VAR(").append(generalReference).append(')').append(edArgset());
		return(s.toString());
	}

}
