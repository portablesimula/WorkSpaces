/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.value;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.S_PUSHC;

import simuletta.compiler.expression.Expression;
import simuletta.type.Type;

/**
 * @author Øystein Myhre Andersen
 */
public abstract class Value extends Expression {
//	public int lineNumber;     // From SyntaxClass

	
	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect() {
		output_S_LINE();
		sCode.outinst(S_PUSHC); return(doOutConst());
	}	

	
	public Type doOutConst() {
		FATAL_ERROR("THIS SHOULD BE REDEFINED: "+this.getClass().getSimpleName());
		return(null);
	}

}
