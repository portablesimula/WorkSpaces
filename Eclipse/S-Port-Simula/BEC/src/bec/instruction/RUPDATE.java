/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.AddressItem;
import bec.compileTimeStack.CTStack;
import svm.instruction.SVM_STORE;

/// S-INSTRUCTION: RUPDATE.
/// <pre>
///  assign_instruction ::= rupdate (dyadic)
///  
///  check TOS ref;
///  force SOS value; check types identical;
///  pop;
///  </pre>
///  This instruction (“reverse update”) works almost like update with the sole exception that the
///  roles of TOS and SOS are interchanged, i.e. the value transfer is from SOS to TOS.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/RUPDATE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class RUPDATE extends Instruction {
	
	/** Default Constructor */ public RUPDATE() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_STORE instruction.
	public static void ofScode() {
		CTStack.checkTosRef(); CTStack.checkSosValue(); CTStack.checkTypesEqual();
		AddressItem adr = (AddressItem) CTStack.pop();
		FETCH.doFetch();			
		Global.PSEG.emit(new SVM_STORE(adr.objadr.addOffset(adr.offset), adr.type.size())); // Store into adr			
	}

}
