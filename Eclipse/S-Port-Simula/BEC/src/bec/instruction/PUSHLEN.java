/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_PUSHLEN;

/// S-INSTRUCTION: PUSHLEN.
/// <pre>
/// stack_instruction ::= pushlen
/// 
/// pushlen
/// 
/// push( VAL, SIZE, "temporary area.LENGTH" );
/// </pre>
///
///	An implicit eval is performed.
///
///	The SIZE needed for the following save, that is the sum of the current value of ALLOCATED
///	and the number of object units, which is needed for SAVE-MARKS and possibly other
///	implementation-dependant information, is computed and the value is pushed onto the stack.
///
///	For optimisation purposes, it is set to nosize in case ALLOCATED = nosize (i.e. if the temporary
///	area is empty). In this case the accompaning save and corresponding restore will receive onone
///	as parameter.
///
///	An S-compiler may choose to skip code generation for the complete sequence pushlen, asscall,
///	call, and save in the case ALLOCATED = nosize. In that case the processing of restore is
///	changed, see below.	public static void ofScode() {
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/PUSHLEN.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class PUSHLEN extends Instruction {
	
	/** Default Constructor */ public PUSHLEN() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_PUSHLEN instruction.
	public static void ofScode() {
		CTStack.pushTempItem(Type.T_SIZE);
		
		Global.PSEG.emit(new SVM_PUSHLEN());
	}

}
