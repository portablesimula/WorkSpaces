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
import bec.util.Util;
import svm.instruction.SVM_DUP;
import svm.value.ObjectAddress;

/// S-INSTRUCTION: DUP.
/// <pre>
/// stack_instruction ::= dup
/// 
/// push( TOS );
/// force TOS value;
/// </pre>
/// A duplicate of TOS is pushed onto the stack and forced into value mode.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/DUP.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class DUP extends Instruction {
	
	/** Default Constructor */ public DUP() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_DUP instruction.
	public static void ofScode() {
		CTStack.dup();
		CTStackItem tos = CTStack.TOS();
		
		if(tos instanceof AddressItem addr) {
			ObjectAddress oaddr = addr.objadr.addOffset(addr.offset);
			Global.PSEG.emit(new SVM_DUP(sizeOnStack(oaddr)));
			FETCH.doFetch();
		} else {
			Global.PSEG.emit(new SVM_DUP(tos.type.size()));
		}
	}
	
	/// Returns the size on Runtime stack
	/// @param oaddr the ObjectAddress
	/// @return the size on Runtime stack
	private static int sizeOnStack(final ObjectAddress oaddr) {
		int size =(oaddr.indexed)? 1 : 0;
		switch(oaddr.kind) {
			case ObjectAddress.SEGMNT_ADDR: // Stack: nothing + index(?)
			case ObjectAddress.REL_ADDR:    // Stack: nothing + index(?) 
			case ObjectAddress.STACK_ADDR:  // Stack: nothing + index(?)
				break;
			case ObjectAddress.REMOTE_ADDR: // Stack: oaddr + index(?)
				size++;
				break;
			case ObjectAddress.REFER_ADDR:  // Stack: ofst oaddr + index(?)
				size = size + 2;
				break;
			default: Util.IERR("");
		}
		return size;
	}

}
