/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.scode.Sinstr;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import svm.RTStack;
import svm.value.Value;

/// SVM-INSTRUCTION: SHIFT opr
/// 
/// 	Runtime Stack
/// 	   ..., sos, tos →
/// 	   ..., result
///
/// The 'tos' and 'sos' are popped off the Runtime stack.
/// The 'result' is calculated as result = sos opr tos.
/// Then the 'result' is pushed onto the Runtime Stack.
/// 
/// 'tos' and 'sos' must be of type int.
/// 
/// The operation depends on the 'opr' parameter:
/// <pre>
///	LSHIFTA: Signed Left Shift     << The left shift operator moves all bits by a given number of bits to the left.
/// LSHIFTL: Unsigned Left Shift   << The left shift operator moves all bits by a given number of bits to the left.
/// RSHIFTA: Signed Right Shift    >> The right shift operator moves all bits by a given number of bits to the right.
/// RSHIFTL: Unsigned Right Shift  >>> It is the same as the signed right shift, but the vacant leftmost position is filled with 0 instead of the sign bit.
/// </pre>
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_SHIFT.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_SHIFT extends SVM_Instruction {
	
	/// Which shift instruction
	int instr;

	/// Construct a new SVM_SHIFT instruction
	/// @param instr which shift instruction
	public SVM_SHIFT(int instr) {
		this.opcode = SVM_Instruction.iSHIFT;
		this.instr = instr;
	}

	@Override
	public void execute() {
		Value tos = RTStack.pop();
		Value sos = RTStack.pop();
		Value res = (sos == null)? null : sos.shift(instr, tos);
		RTStack.push(res);
		Global.PSC.ofst++;
	}

	@Override	
	public String toString() {
		switch(instr) {
			case Sinstr.S_LSHIFTA: return("LSHIFTA");
			case Sinstr.S_LSHIFTL: return("LSHIFTL");
			case Sinstr.S_RSHIFTA: return("RSHIFTA");
			case Sinstr.S_RSHIFTL: return("RSHIFTL");
		}
		return "SHIFT    ";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override	
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeByte(instr);
	}

	/// Reads an SVM_SHIFT instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_SHIFT instruction read
	/// @throws IOException if IOException occur
	public static SVM_SHIFT read(AttributeInputStream inpt) throws IOException {
		SVM_SHIFT instr = new SVM_SHIFT(inpt.readUnsignedByte());
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
