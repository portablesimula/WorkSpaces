/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.S_Module;
import bec.compileTimeStack.CTStack;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.util.Util;
import svm.segment.ProgramSegment;

/// S-INSTRUCTION: BSEG.
/// <pre>
///   * remember stack;
///   * purge stack;
/// </pre>
/// The current program point is remembered together with the complete state of the stack, the "bsegstack".
/// Some new segment is designated the current program point, together with a new, empty stack.
///
/// S-INSTRUCTION: ESEG.
/// <pre>
///  * check stack empty;
///  * reestablish stack remembered at corresponding bseg;
/// </pre>
/// The "bseg-stack" is restored together with the saved program point.
///
///
/// SEGMENTATION CONTROL.
/// <pre>
/// segment_instruction
///		::= bseg < program_element >* eseg
/// </pre>
/// This instruction specifies that the enclosed program elements are out of sequence, i.e. the code
/// generated must either be located elsewhere or it must be preceded by an unconditional jump instruction
/// leading to the program point following eseg. The segment instruction is illegal within routine bodies.
///
/// Note that all jump/destination sets must be fully enclosed within a segment.
/// <br>The bseg and eseg have similar effects on the stack as save and restore (chapter 7).
///
/// More info in: S-Port: Definition of S-code sectn. 12. SEGMENTATION CONTROL
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/BSEG.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class BSEG extends Instruction {
	
	/// BSEG sequence number
	private static int SEQU;
	
	/** Default Constructor */ public BSEG() {} 

	/// Treat a complete Segment Instruction by opening a new Program Segment and
	/// process all Program Elements in it and finally expect an ESEG instruction.
	/// Then the Program Segment is closed and we continue with the 'old' segment.
	///
	/// End-Condition: Scode'nextByte = First byte after ESEG
	public static void ofScode() {
		Scode.inputInstr();
		
		ProgramSegment prevPSEG = Global.PSEG;
		Global.PSEG = new ProgramSegment(Global.getSourceID()+"_BSEG_" + SEQU++);

		CTStack.BSEG("BSEG");
			S_Module.programElements();
			if(Scode.curinstr != Sinstr.S_ESEG) Util.IERR("Missing ESEG, Got " + Sinstr.edInstr(Scode.curinstr));
		CTStack.ESEG();
		
		Global.routineSegments.add(Global.PSEG);
		Global.PSEG = prevPSEG;
	}

}
