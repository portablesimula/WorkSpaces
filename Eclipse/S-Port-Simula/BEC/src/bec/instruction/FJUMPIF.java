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

/// S-INSTRUCTION: FJUMPIF.
/// <pre>
/// forward_jump ::= fjumpif relation destination:newindex
/// 
/// relation ::= ?lt | ?le | ?eq | ?ge | ?gt | ?ne
/// 
/// force TOS value; force SOS value;
/// check relation;
/// pop; pop;
/// </pre>
/// The destination must be undefined, and TOS and SOS must be of the same permissible resolved type
/// with regard to the relation given, otherwise: error.
///
/// A conditional forward jump sequence will be generated, branching only if the relation (see chapter 9)
/// evaluates true. The destination will refer to an undefined program point to be located later (by fdest).
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/FJUMPIF.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class FJUMPIF extends Instruction {

	/** Default Constructor */ public FJUMPIF() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Update the destination table with the address of the forthcoming SVM_JUMP instruction.
	/// This info is used to fixup the jump address when processing the FDEST instruction.
	/// Finally: Emit an SVM_JUMP instruction with a null address.
	public static void ofScode() {
		Relation relation = Relation.ofScode();
		int destination = Scode.inByte();

		FETCH.doFetch();
		CTStack.checkSosValue(); CTStack.checkTypesEqual();
		CTStackItem tos = CTStack.pop();
		CTStack.pop();
		
		if(Global.DESTAB[destination] != null) Util.IERR("Destination is already defined");
		Global.DESTAB[destination] = Global.PSEG.nextAddress();
		Global.PSEG.emit(new SVM_JUMPIF(relation, tos.type.size(), null));
	}

}
