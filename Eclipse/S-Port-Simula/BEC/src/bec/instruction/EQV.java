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
import svm.instruction.SVM_EQV;

/// S-INSTRUCTION: EQV.
/// <pre>
/// arithmetic_instruction ::= eqv (dyadic)
/// 
/// force TOS value; check TOS type(BOOL,INT);
/// force SOS value; check SOS type(BOOL,INT);
/// check types equal;
/// pop; pop;
/// push( VAL, BOOL, "value(SOS) eqv value(TOS)" );
/// </pre>
/// TOS and SOS are replaced by a description of the result of applying the operator.
/// <br>Note that SOS is the left operand.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/EQV.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class EQV extends Instruction {
	
	/** Default Constructor */ public EQV() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_EQV instruction.
	public static void ofScode() {
		FETCH.doFetch();
		CTStackItem tos = CTStack.TOS();
	    CTStack.checkSosValue(); CTStack.checkTypesEqual();
	    Type at = tos.type;
	    if(at != Type.T_BOOL)
	    	CTStack.checkTosType(Type.T_INT);
		CTStack.pop(); CTStack.pop();
	    CTStack.pushTempItem(at);
	    
		Global.PSEG.emit(new SVM_EQV());
	}

}
