/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.designator;

import simuletta.compiler.SyntaxClass;
import simuletta.type.Type;

public abstract class DesignatorElement extends SyntaxClass {
//	public int lineNumber;    // From SyntaxClass

	
	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	protected abstract Type doSCodingDirect(boolean target, boolean isLast,Type prevType);

}
