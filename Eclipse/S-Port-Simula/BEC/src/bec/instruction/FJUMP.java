/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Scode;
import bec.util.Util;
import svm.instruction.SVM_JUMP;

/// S-INSTRUCTION: FJUMP.
/// <pre>
/// forward_jump ::= fjump destination:newindex
/// 
/// check stack empty;
/// </pre>
/// The destination must be undefined,otherwise: error.
///
/// A jump to the (as yet unknown) program point is generated, and the destination becomes defined.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/fjump.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class FJUMP extends Instruction {

	/** Default Constructor */ public FJUMP() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Update the destination table with the address of the forthcoming SVM_JUMP instruction.
	/// This info is used to fixup the jump address when processing the FDEST instruction.
	/// Finally: Emit an SVM_JUMP instruction with a null address.
	public static void ofScode() {
		int destination = Scode.inByte();
		CTStack.checkStackEmpty();
		if(Global.DESTAB[destination] != null) Util.IERR("Destination is already defined");
		Global.DESTAB[destination] = Global.PSEG.nextAddress();
		
		Global.PSEG.emit(new SVM_JUMP(null));
	}

}
