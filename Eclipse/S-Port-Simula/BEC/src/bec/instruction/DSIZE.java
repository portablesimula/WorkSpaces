/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.descriptor.RecordDescr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.Util;
import svm.instruction.SVM_ADD;
import svm.instruction.SVM_LOADC;
import svm.instruction.SVM_MULT;
import svm.value.IntegerValue;

/// S-INSTRUCTION: DSIZE.
/// <pre>
/// addressing_instruction ::= dsize structured_type
/// 
///		structured_type ::= record_tag:tag
///
/// dsize structured_type
/// 
/// force TOS value; check TOS type(INT);
/// pop;
/// push( VAL, SIZE, "size(type with mod. rep.count)" );
/// </pre>
/// The structured type must be prefixed with a "DYNAMIC" type (see 4.3.6),
/// <br>and it must contain an indefinite repetition, otherwise: error.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/DSIZE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class DSIZE extends Instruction {
	
	/** Default Constructor */ public DSIZE() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit instructions to evaluate the DSIZE.
	public static void ofScode() {
		Tag tag = Tag.ofScode();
		RecordDescr fixrec = (RecordDescr) tag.getMeaning();
		int n = fixrec.rep0size;
		if(n == 0) Util.IERR("Illegal DSIZE on: " + fixrec);
		FETCH.doFetch(); CTStack.checkTosInt();
		CTStack.pop();
		CTStack.pushTempItem(Type.T_SIZE);
		
		if(n > 1) {
			Global.PSEG.emit(new SVM_LOADC(Type.T_INT, IntegerValue.of(Type.T_INT, n)));
			Global.PSEG.emit(new SVM_MULT());
		}
		IntegerValue fixValue = IntegerValue.of(Type.T_INT, fixrec.size);
		Global.PSEG.emit(new SVM_LOADC(Type.T_INT, fixValue));
		Global.PSEG.emit(new SVM_ADD());
	}

}
