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

/// S-INSTRUCTION: ASSIGN.
/// <pre>
/// assign_instruction ::= assign (dyadic)
/// 
/// - force TOS value;
/// - check SOS ref; check types identical;
/// - pop; pop;
/// </pre>
/// Code is generated to transfer the value described by TOS to the location designated by SOS.
/// This implies that the stack elements must be evaluated, and that any code generation involving
/// TOS or SOS, that has been deferred for optimisation purposes, must take place before the
/// assignment code is generated. SOS and TOS are popped from the stack.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/ASSIGN.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class ASSIGN extends Instruction {
	
	/** Default Constructor */ public ASSIGN() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_ASSIGN instruction.
	public static void ofScode() {
		CTStack.checkSosRef(); CTStack.checkTypesEqual(); FETCH.doFetch();
		@SuppressWarnings("unused")
		CTStackItem tos = CTStack.pop();
		AddressItem sos = (AddressItem) CTStack.pop();
		Global.PSEG.emit(new SVM_ASSIGN(false, sos.objadr.addOffset(sos.offset), sos.type.size())); // Store into adr
	}

}
