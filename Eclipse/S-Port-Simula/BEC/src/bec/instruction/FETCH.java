/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.AddressItem;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_LOAD;

/// S-INSTRUCTION: FETCH.
/// <pre>
/// addressing_instruction ::= fetch
/// 
/// force TOS value;
/// 
/// TOS.MODE should be REF, otherwise fetch has no effect.
/// TOS is modified to describe the contents of the area previously described.
/// 
///      (TOS) -------------------,
///                               |
///                               V
///      The resulting            .============.
///          TOS -----------------|---> VALUE  |
///      after fetch              '============'
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/FETCH.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class FETCH extends Instruction {

	/** Default Constructor */ public FETCH() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: If TOS mode = address, Emit an SVM_LOAD instruction.
	public static void ofScode() {
		doFetch();
	}

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: If TOS mode = address, Emit an SVM_LOAD instruction.
	public static void doFetch() {
		if(CTStack.TOS() instanceof AddressItem addr) {
			Type type = addr.type;
			CTStack.pop(); CTStack.pushTempItem(type);
			Global.PSEG.emit(new SVM_LOAD(addr.objadr.addOffset(addr.offset), type.size()));				
		}
	}

}
