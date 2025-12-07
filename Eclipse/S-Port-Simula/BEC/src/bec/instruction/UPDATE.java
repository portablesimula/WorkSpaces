/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.AddressItem;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import svm.instruction.SVM_ASSIGN;

/// S-INSTRUCTION: UPDATE.
/// <pre>
///  assign_instruction ::= update (dyadic)
///
///  force TOS value;
///  check SOS ref; check types identical;
///  force SOS value;
///  pop;
/// </pre>
/// Code is generated to transfer the value described by TOS to the location designated by SOS.
/// TOS must be evaluated and any deferred code generation involving TOS must take place before
/// the update code is generated.
///
/// Note that only TOS is popped and the new TOS is modified to describe the value assigned.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/UPDATE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class UPDATE extends Instruction {
	
	/** Default Constructor */ public UPDATE() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_ASSIGN instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkSosRef(); CTStack.checkTypesEqual();
		CTStackItem tos = CTStack.pop();
		AddressItem sos = (AddressItem) CTStack.pop();
		CTStack.pushTempItem(tos.type);
		
		Global.PSEG.emit(new SVM_ASSIGN(true, sos.objadr.addOffset(sos.offset), sos.type.size())); // Store into adr
	}

}
