/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.AddressItem;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.TempItem;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.Util;
import svm.instruction.SVM_LOADC;
import svm.instruction.SVM_MULT;
import svm.value.IntegerValue;

/// S-INSTRUCTION: INDEX.
/// <pre>
/// addressing_instruction ::= ::= index | indexv
/// 
/// force TOS value; check TOS type(INT);
/// check SOS ref;
/// pop;
/// 
/// TOS.OFFSET := SOS.OFFSET + "SOS.SIZE * value(TOS)"
///
/// If instruction indexv: force TOS value.
/// </pre>
/// SOS is considered to describe an element of a repetition, and the purpose of the instruction is to
/// select one of the components of the repetition by indexing relative to the current position. The
/// effect may perhaps best be understood by considering an infinite array A with elements of
/// SOS.TYPE. The array is placed so that element A(0) is the quantity described by SOS. After
/// index the stack top will describe A(N), where N is the value of TOS. No bounds checking should
/// be performed.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/INDEX.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class INDEX extends Instruction {

	/** Default Constructor */ public INDEX() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit SVM instructions.
	/// @param instr which S-Code instruction
	public static void ofScode(int instr) {
		FETCH.doFetch();			
		CTStack.checkTosInt(); CTStack.checkSosRef();
		if(! (CTStack.TOS() instanceof TempItem)) Util.IERR("");
		CTStack.pop();
		AddressItem adr = (AddressItem) CTStack.TOS();
		int size = adr.type.size();
		if(adr.objadr.indexed) {
			Util.IERR("IMPOSSIBLE");
		} else {
			if(size > 1) {
				Global.PSEG.emit(new SVM_LOADC(Type.T_INT, IntegerValue.of(Type.T_INT, size)));
				Global.PSEG.emit(new SVM_MULT());
			}
			adr.objadr.indexed = true;
		}			
		if(instr == Sinstr.S_INDEXV) FETCH.doFetch();
	}
	

}
