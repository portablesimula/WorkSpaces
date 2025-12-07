/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.designator;

import simuletta.type.Type;

public class Qualification extends DesignatorElement {
//	public int lineNumber;    // From SyntaxClass
	Type type;
	
	public Qualification(Type type) {
		this.type=type;
	}

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	protected Type doSCodingDirect(boolean target, boolean isLast, Type prevType) {
		Type.convert(prevType,this.type);
		return(this.type);
	}

	
	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append(" QUA ").append(type);
		return(s.toString());
	}

}
