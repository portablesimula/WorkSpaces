/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_INITO;

/// S-INSTRUCTION: INITO.
/// <pre>
/// temp_control ::= t-inito
/// 
/// force TOS value; check TOS type(OADDR);
/// pop;
/// </pre>
/// Code is generated to initialise a scan of the save-object described by TOS,
/// i.e. SAVE-OBJECT is set to refer to the object, and SAVE-INDEX is initialized.
/// TOS is popped.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/INITO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class INITO extends Instruction {
	
	/** Default Constructor */ public INITO() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_INITO instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosType(Type.T_OADDR);
		CTStack.pop();
		
		Global.PSEG.emit(new SVM_INITO());
	}

}
