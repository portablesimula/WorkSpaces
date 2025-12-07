/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Type;
import svm.instruction.SVM_POPK;

/// S-INSTRUCTION: DINITAREA.
/// <pre>
/// dinitarea structured_type (dyadic)
///
/// force TOS value; check TOS type(INT);
/// force SOS value; check SOS type(OADDR);
/// pop;
/// </pre>
/// TOS.TYPE must be INT, SOS.TYPE must be OADDR, and the structured type must contain an
/// indefinite repetition, otherwise: error.
///
/// The value of TOS is used to resolve the type, i.e fixing the number of elements in the indefinite
/// repetition, following that the evaluation proceeds exactly as for initarea.
/// 
/// NOTE: In this implementation  DINITAREA == NOOP
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/DINITAREA.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class DINITAREA extends Instruction {
	
	/** Default Constructor */ public DINITAREA() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_POPK instruction.
	@SuppressWarnings("unused")
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosType(Type.T_OADDR);
		CTStack.checkSosValue(); CTStack.checkSosType(Type.T_INT);
		Type type = Type.ofScode();
		Scode.expect(Sinstr.S_FIXREP);
		int fixrep = Scode.inNumber();
		CTStack.pop();
		
		Global.PSEG.emit(new SVM_POPK(1));
	}

}
