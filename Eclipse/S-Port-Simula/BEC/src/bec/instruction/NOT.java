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
import svm.instruction.SVM_NOT;

/// S-INSTRUCTION: NOT.
/// <pre>
/// arithmetic_instruction ::= not
/// 
/// force TOS value; check TOS type(BOOL,INT);
/// 
/// value(TOS) := not value(TOS);
/// </pre>
/// TOS is replaced by a description of the negated TOS value.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/NOT.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class NOT extends Instruction {
	
	/** Default Constructor */ public NOT() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_NOT instruction.
	public static void ofScode() {
		FETCH.doFetch();			
		CTStackItem tos = CTStack.TOS();
	    
	    Type at = tos.type;
	    if(at != Type.T_BOOL)
	    	CTStack.checkTosType(Type.T_INT);
		CTStack.pop();
	    CTStack.pushTempItem(at);
	    
		Global.PSEG.emit(new SVM_NOT());
	}

}
