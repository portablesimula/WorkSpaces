/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_DECO;

/// S-INSTRUCTION: DECO.
/// <pre>
/// addressing_instruction ::= deco (dyadic)
///
/// force TOS value; check TOS type(SIZE);
/// force SOS value; check SOS type(OADDR);
/// pop; pop;
/// push( VAL, OADDR, "value(SOS) +/- value(TOS)" );
/// </pre>
/// The two top elements are replaced by a descriptor of
/// <br>the object address RESULT defined through the equation
///
/// 	dist(RESULT,value(SOS)) = - value(TOS)
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/DECO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class DECO extends Instruction {
	
	/** Default Constructor */ public DECO() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_DECO instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosType(Type.T_SIZE);
		CTStack.checkSosValue(); CTStack.checkSosType(Type.T_OADDR);
		CTStack.pop(); CTStack.pop();
	    CTStack.pushTempItem(Type.T_OADDR);
	    
		Global.PSEG.emit(new SVM_DECO());
	}

}
