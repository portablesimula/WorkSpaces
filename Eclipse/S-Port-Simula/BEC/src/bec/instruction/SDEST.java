/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.descriptor.SwitchDescr;
import bec.scode.Scode;
import bec.scode.Tag;
import bec.util.Util;

/// S-INSTRUCTION: SDEST.
/// <pre>
///  forward_destination ::= sdest switch:tag which:number
///  
///  check stack empty;
///  </pre>
///  The tag must have been defined in a switch instruction, and the number must be within the range
///  defined by the corresponding switch instruction, otherwise: error.
///
///  The destination "D(which)" of the switch instruction defining the tag is located at the current program
///  point.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/SDEST.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class SDEST extends Instruction {
	
	/** Default Constructor */ public SDEST() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Update the switch destination table.
	public static void ofScode() {
		Tag tag = Tag.ofScode();
		int which = Scode.inNumber();
		
		CTStack.checkStackEmpty();
		
		SwitchDescr swt = (SwitchDescr) tag.getMeaning();
		if(swt.DESTAB[which] != null) Util.IERR("SWITCH dest["+which+"]. dest != null");

		swt.DESTAB[which] = Global.PSEG.nextAddress();
//     	Global.PSEG.emit(new SVM_NOOP());
	}

}
