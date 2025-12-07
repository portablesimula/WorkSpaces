/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import svm.instruction.SVM_DIV;

/// S-INSTRUCTION: DIV.
/// <pre>
/// arithmetic_instruction ::= div (dyadic)
/// 
/// force TOS value; check TOS type(INT,REAL,LREAL);
/// force SOS value; check SOS type(INT,REAL,LREAL);
/// check types equal;
/// 
/// pop; pop;
/// push( VAL, type, "value(SOS) / value(TOS)" );
/// </pre>
/// SOS and TOS are replaced by a description of the value of the application of the operator.
/// <br>The type of the result is the same as the type of the operands.
/// <br>SOS is always the left operand, i.e. Result := SOS / TOS.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/DIV.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class DIV extends Instruction {

	/** Default Constructor */ public DIV() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_DIV instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosArith();
		CTStack.checkSosValue(); CTStack.checkTypesEqual();
		CTStackItem tos = CTStack.pop(); CTStack.pop();
	    CTStack.pushTempItem(tos.type);
	    
		Global.PSEG.emit(new SVM_DIV());
	}

}
