/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import svm.instruction.SVM_REM;

/// S-INSTRUCTION: REM.
/// <pre>
/// arithmetic_instruction ::= rem (dyadic)
///
/// Remainder, defined as "SOS - (SOS//TOS)*TOS".
/// Syntax and semantics as for mult except that INT is the only legal type.
/// </pre>
/// Note that SIMULA demands "truncation towards zero" for integer division. Thus (except for a
/// zero remainder) the result of rem has the same sign as the result of the division.
/// In more formal terms:
/// 
///	 i div j = sign(i/j) * entier(abs(i/j))
/// 
///	 i rem j = i - (i div j) * j
/// 
/// where '/' represents the exact mathematical division within the space of real numbers.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/REM.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class REM extends Instruction {
	
	/** Default Constructor */ public REM() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_REM instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosArith();
		CTStack.checkSosValue(); CTStack.checkTypesEqual();
		CTStackItem tos = CTStack.pop(); CTStack.pop();
	    CTStack.pushTempItem(tos.type);

		Global.PSEG.emit(new SVM_REM());
	}

}
