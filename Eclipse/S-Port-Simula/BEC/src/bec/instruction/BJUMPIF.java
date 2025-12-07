/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import bec.scode.Relation;
import bec.scode.Scode;
import bec.util.Util;
import svm.instruction.SVM_JUMPIF;
import svm.value.ProgramAddress;

/// S-INSTRUCTION: BJUMPIF.
/// <pre>
/// backward_jump ::= bjumpif relation destination:index (dyadic)
/// 
/// relation ::= ?lt | ?le | ?eq | ?ge | ?gt | ?ne
///
/// force TOS value; force SOS value;
/// check relation;
/// pop; pop;
/// </pre>
/// The destination must be defined by a bdest instruction, and TOS and SOS must be of the same
/// permissible resolved types with regard to relation, otherwise: error.
/// <br>A conditional jump sequence will be generated, branching only if the relation evaluates true.
/// <br>The destination becomes undefined.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/BJUMPIF.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class BJUMPIF extends Instruction {

	/** Default Constructor */ public BJUMPIF() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Update the destination table.
	/// Finally: Emit an SVM_JUMPIF instruction.
	public static void ofScode() {
		Relation relation = Relation.ofScode();
		int destination = Scode.inByte();

		FETCH.doFetch();
		CTStack.checkSosValue(); CTStack.checkTypesEqual();
		CTStackItem tos = CTStack.pop();
		CTStack.pop();
		
		ProgramAddress addr = Global.DESTAB[destination];
		if(addr == null) Util.IERR("Destination is undefined");
		Global.DESTAB[destination] = null;
		
		Global.PSEG.emit(new SVM_JUMPIF(relation, tos.type.size(), addr));
	}

}
