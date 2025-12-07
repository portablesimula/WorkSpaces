/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import svm.instruction.SVM_ADD;

/// S-INSTRUCTION: ADD.
/// <pre>
/// arithmetic_instruction ::= add (dyadic)
/// 
/// force TOS value; check TOS type(INT,REAL,LREAL);
/// force SOS value; check SOS type(INT,REAL,LREAL);
/// check types equal;
/// 
/// pop; pop;
/// push( VAL, type, "value(SOS) + value(TOS)" );
/// </pre>
/// SOS and TOS are replaced by a description of the value of the application of the operator.
/// <br>The type of the result is the same as the type of the operands.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/ADD.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class ADD extends Instruction {

	/** Default Constructor */ public ADD() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_ADD instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosArith();
		CTStack.checkSosValue(); CTStack.checkTypesEqual();
		CTStackItem tos = CTStack.pop(); CTStack.pop();
	    CTStack.pushTempItem(tos.type);
	    
		Global.PSEG.emit(new SVM_ADD());
	}
	
}
