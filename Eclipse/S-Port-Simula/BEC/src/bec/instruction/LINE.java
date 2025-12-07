/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Scode;
import svm.instruction.SVM_LINE;

/// S-INSTRUCTION: LINE.
/// <pre>
/// 	info_setting
/// 		::= decl line:number
/// 		::= line line:number
/// 		::= stmt line:number
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/LINE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class LINE extends Instruction {

	/** Default Constructor */ public LINE() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_LINE instruction.
	/// @param kind the kind: line|decl|stmt
	public static void ofScode(final int kind) {
		Scode.curline = Scode.inNumber();	
		
		if(kind==2)
			CTStack.checkStackEmpty();
		
		Global.PSEG.emit(new SVM_LINE(0, Scode.curline));
	}

}
