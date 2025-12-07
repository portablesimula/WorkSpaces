/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_GETO;

/// S-INSTRUCTION: GETO.
/// <pre>
/// temp_control ::= t-geto
///
/// push( VAL, OADDR, "value of current pointer" );
/// </pre>
/// Code is generated, which in case SAVE-INDEX refers to the "last" pointer of the save object
/// refered by SAVE-OBJECT or no pointer exists in the object, the value onone is returned to
/// signal that the scan of the object should be terminated. Otherwise SAVE-INDEX is updated to
/// describe the "next" pointer of the save object. In case the value of the "next" pointer is onone,
/// the pointer is skipped, i.e. iterate this description, otherwise the value of the refered pointer is
/// returned.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/GETO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class GETO extends Instruction {
	
	/** Default Constructor */ public GETO() {} 
	
	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_GETO instruction.
	public static void ofScode() {
		CTStack.pushTempItem(Type.T_OADDR);
		Global.PSEG.emit(new SVM_GETO());
	}

}
