/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression;

import simuletta.type.Type;

/**
 * Qua Operation.
 * 
 * <pre>
 * Syntax:
 * 
 *	type_conversion 
 *		::= expression qua type
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class QuaOperation extends Expression {
//	public int lineNumber;    // From SyntaxClass
	public Expression x;
	Type type;
	
	public QuaOperation(Expression x,Type type) {
		this.x=x; this.type=type;
	}
	
	public String toString() {
		return(""+x+" QUA "+type);
	}

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect() {
		enterLine();
        Type t2=x.doSCodingDirect();
        if(t2 != type) Type.convert(t2,type);
		exitLine();
        return(type);
	}
}
