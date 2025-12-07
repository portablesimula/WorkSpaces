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
import svm.value.ProgramAddress;

/// S-INSTRUCTION: BJUMP.
/// <pre>
/// backward_jump ::= bjump destination:index
/// 
/// check stack empty;
/// </pre>
/// The destination must have been defined in a bdest instruction, otherwise: error.
/// <br>A jump to the referenced program point is generated, and the destination becomes undefined.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/BJUMP.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class BJUMP extends Instruction {

	/** Default Constructor */ public BJUMP() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_JUMP instruction and update the destination table
	public static void ofScode() {
		int destination = Scode.inByte();
		CTStack.checkStackEmpty();
		ProgramAddress addr = Global.DESTAB[destination];
		if(addr == null) Util.IERR("BJUMP dest. dest == null");
		Global.PSEG.emit(new SVM_JUMP(addr));
		Global.DESTAB[destination] = null;
	}

}
