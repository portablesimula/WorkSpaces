package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_INCO;

/// S-INSTRUCTION: INCO.
/// <pre>
/// addressing_instruction ::= inco (dyadic)
///
/// force TOS value; check TOS type(SIZE);
/// force SOS value; check SOS type(OADDR);
/// pop; pop;
/// push( VAL, OADDR, "value(SOS) +/- value(TOS)" );
/// </pre>
/// The two top elements are replaced by a descriptor of
/// the object address RESULT defined through the equation
///
/// 	dist(RESULT,value(SOS)) = + value(TOS)
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/INCO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class INCO extends Instruction {
	
	/** Default Constructor */ public INCO() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_INCO instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosType(Type.T_SIZE);
		CTStack.checkSosValue(); CTStack.checkSosType(Type.T_OADDR);
		CTStack.pop(); CTStack.pop();
	    CTStack.pushTempItem(Type.T_OADDR);
	    
		Global.PSEG.emit(new SVM_INCO());
	}

}
