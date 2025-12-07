/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import bec.scode.Type;
import svm.instruction.SVM_CONVERT;

/// S-INSTRUCTION: CONVERT.
/// <pre>
/// convert_instruction ::= convert simple_type
/// 
/// TOS must be of simple type, otherwise: error.
/// 
/// The TYPE of TOS is changed to the type specified in the instruction,
/// this may imply code generation.
/// </pre> 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/CONVERT.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class CONVERT extends Instruction {

	/** Default Constructor */ public CONVERT() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Call 'doConvert' to test and possibly Emit an SVM_COMPARE instruction.
	public static void ofScode() {
		Type toType = Type.ofScode();
		FETCH.doFetch();
		doConvert(toType);
	}
	
	/// Test and possibly Emit an SVM_CONVERT instruction.
	/// @param totype the expected result type 
	public static void doConvert(final Type totype) {
		CTStackItem TOS = CTStack.TOS();
		Type fromtype = TOS.type;
		if(totype != fromtype) {
			Global.PSEG.emit(new SVM_CONVERT(fromtype.tag, totype.tag));
			CTStack.pop(); CTStack.pushTempItem(totype);
			TOS.type = totype;
		}
	}

}
