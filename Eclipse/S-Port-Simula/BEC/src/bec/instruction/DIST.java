/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_DIST;

/// S-INSTRUCTION: DIST.
/// <pre>
/// addressing_instruction ::= dist (dyadic)
///
/// force TOS value; check TOS type(OADDR);
/// force SOS value; check SOS type(OADDR);
/// pop; pop;
/// push( VAL, SIZE, "value(SOS) - value(TOS)" );
/// </pre>
/// TOS and SOS are replaced by a description of the signed distance from TOS to SOS.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/DIST.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class DIST extends Instruction {
	
	/** Default Constructor */ public DIST() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_DIST instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosType(Type.T_OADDR);
		CTStack.checkSosValue(); CTStack.checkSosType(Type.T_OADDR);
		CTStack.pop(); CTStack.pop();
	    CTStack.pushTempItem(Type.T_SIZE);
	    
		Global.PSEG.emit(new SVM_DIST());
	}

}
