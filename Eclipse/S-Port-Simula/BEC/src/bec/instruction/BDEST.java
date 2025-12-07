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

/// S-INSTRUCTION: BDEST.
/// <pre>
/// backward_destination ::= bdest destination:newindex
/// 
/// check stack empty;
/// </pre>
/// The destination must be undefined, otherwise: error.
/// <br>The destination is defined to refer to the current program point.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/BDEST.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class BDEST extends Instruction {

	/** Default Constructor */ public BDEST() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Update the destination table.
	public static void ofScode() {
		CTStack.checkStackEmpty();
		int destination = Scode.inByte();
		if(Global.DESTAB[destination] != null) Util.IERR("BJUMP dest. dest == null");
		Global.DESTAB[destination] = Global.PSEG.nextAddress();
//     	Global.PSEG.emit(new SVM_NOOP());
	}

}
