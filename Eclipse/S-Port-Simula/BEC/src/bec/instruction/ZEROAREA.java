/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_CALL_SYS;

/// S-INSTRUCTION: ZEROAREA.
/// <pre>
/// area_initialisation ::= zeroarea
/// 
/// zeroarea (dyadic)
/// force TOS value; check TOS type(OADDR);
/// force SOS value; check SOS type(OADDR);
/// pop;
/// </pre>
/// TOS and SOS must be OADDR, otherwise error.
/// <br>The area between SOS and TOS (SOS included, TOS not) is to be zero-filled, and TOS is popped.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/ZEROAREA.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class ZEROAREA extends Instruction {
	
	/** Default Constructor */ public ZEROAREA() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_CALL_SYS(ZEROAREA) instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosType(Type.T_OADDR);
		CTStack.checkSosValue(); CTStack.checkSosType(Type.T_OADDR);
		Global.PSEG.emit(new SVM_CALL_SYS(SVM_CALL_SYS.P_ZEROAREA));
		CTStack.pop();
	}

}
