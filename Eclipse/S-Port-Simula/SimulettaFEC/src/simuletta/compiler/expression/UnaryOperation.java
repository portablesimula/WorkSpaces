/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;

import static simuletta.compiler.common.S_Instructions.*;

import simuletta.compiler.common.S_Instructions;
import simuletta.type.Type;
import simuletta.utilities.Option;

/**
 * Unary Operation.
 * 
 * <pre>
 * Syntax:
 * 
 *	unary_operation
 * 		 ::= unary_operator factor
 * 
 *		unary_operator 
 *			::= + | - | not
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class UnaryOperation extends Expression {
//	public int lineNumber;    // From SyntaxClass
	int opr; Expression x;
	
	public UnaryOperation(int opr, Expression x) {
		this.opr=opr; this.x=x;
	}

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect() {
		enterLine();
			Type xtype=null;
			switch(opr) {

				case S_NOT:
					xtype=x.doSCodingDirect();
					if(!Option.sportOk && xtype!=Type.Boolean) {
						Type.tstconv(xtype,Type.Boolean); xtype=Type.Boolean;
					}
					sCode.outinst(S_NOT); sCode.outcode();
					break;

				case S_NEG:
					xtype=x.doSCodingDirect();//expression(ininstr());
					xtype=xtype.arith_type();
					sCode.outinst(S_NEG); sCode.outcode();
					break;
        	
				case S_NAME:  // NAME  and  @-operation
					Type type=x.doSCodingDirect(true);
					xtype=Type.Name(type);
//					IO.println("UnaryOperation.doSCodingDirect: x="+x.getClass().getSimpleName()+"  "+x);
					sCode.outinst(S_DEREF); sCode.outcode();
					break;
        	
				default: FATAL_ERROR("Unopr: "+S_Instructions.edSymbol(opr));
			}
		exitLine();
        return(xtype);
	}
	
	
	public String toString() {
		return("Unopr: "+edSymbol(opr)+" "+x);
	}

}
