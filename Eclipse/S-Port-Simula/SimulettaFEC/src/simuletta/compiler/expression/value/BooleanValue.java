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

import simuletta.type.Type;

/**
 * BooleanValue.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		BooleanValue ::=  TRUE  |  FALSE 
 * 
 *  S-Code:
 *  
 *  	BooleanValue ::=  TRUE  |  FALSE
 * 
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class BooleanValue extends Value {
	public final boolean value;
	
	public BooleanValue(boolean value) {
		this.value=value;
	}

	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
	public Type doOutConst() {
		enterLine();
        	sCode.outinst(value?S_TRUE:S_FALSE); sCode.outcode();
		exitLine();
		return(Type.Boolean);
	}
	
	public String toString() {
		return(""+value);
	}

}
