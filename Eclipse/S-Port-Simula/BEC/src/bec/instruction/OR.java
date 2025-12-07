/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import bec.scode.Type;
import svm.instruction.SVM_OR;

/// S-INSTRUCTION: OR.
/// <pre>
/// arithmetic_instruction ::= or (dyadic)
/// 
/// force TOS value; check TOS type(BOOL,INT);
/// force SOS value; check SOS type(BOOL,INT);
/// check types equal;
/// 
/// pop; pop;
/// push( VAL, type, "value(SOS) or value(TOS)" );
/// </pre>
/// SOS and TOS are replaced by a description of the value of the application of the operator.
/// <br>The type of the result is the same as the type of the operands.
/// <br>SOS is always the left operand, i.e. Result := SOS or TOS.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/OR.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class OR extends Instruction {
	
	/** Default Constructor */ public OR() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_OR instruction.
	public static void ofScode() {
		FETCH.doFetch();
		CTStackItem tos = CTStack.TOS();
	    CTStack.checkSosValue(); CTStack.checkTypesEqual();
	    Type at = tos.type;
	    if(at != Type.T_BOOL)
	    	CTStack.checkTosType(Type.T_INT);
		CTStack.pop(); CTStack.pop();
	    CTStack.pushTempItem(at);
	    
		Global.PSEG.emit(new SVM_OR());
	}

}
