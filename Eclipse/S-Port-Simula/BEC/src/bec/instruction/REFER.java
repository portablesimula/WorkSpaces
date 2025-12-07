/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.compileTimeStack.AddressItem;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.value.ObjectAddress;

/// S-INSTRUCTION: REFER.
/// <pre>
///  refer resolved_type
///  
///  force TOS value; check TOS type(GADDR);
///  TOS.MODE := REF; TOS.TYPE := type;
///  </pre>
///  TOS is modified to describe a quantity of the given type, at the address described by TOS.
///  <pre>
///                            =================
///        (TOS) ==============|==> GADDR VALUE –|----------.
///                            =================            |
///                                                         |
///                                                         |
///   The resulting                                         V
///        TOS -------------------------------------------->.==========.
///    after refer                  REF                     |  object  |
///                                                         |    of    |
///                                                         |  "type"  |
///                                                         '=========='
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/REFER.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class REFER extends Instruction {

	/** Default Constructor */ public REFER() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	public static void ofScode() {
		Type type = Type.ofScode();
		
		FETCH.doFetch();			
		CTStack.checkTosType(Type.T_GADDR);
        CTStack.pop(); 
        CTStack.push(new AddressItem(type, 0, ObjectAddress.ofReferAddr()));			
	}

}
