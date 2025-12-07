/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import svm.RTStack;
import svm.value.IntegerValue;


/// SVM-INSTRUCTION: PUSHLEN
/// 
///		Runtime Stack
///			... →
///			..., size
///
///	The size of the Runtime stack is pushed onto the Runtime stack.
///
///	For optimisation purposes, if it is set to nosize the accompaning save
/// and corresponding restore will receive onone as parameter.
///
/// See: SVM_SAVE and SVM_RERSTORE
/// See also S-Port - Definition of S-code - sect. 7. INTERMEDIATE RESULTS.
///	
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_PUSHLEN.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_PUSHLEN extends SVM_Instruction {
	
	/// Construct a new SVM_PUSHLEN instruction
	public SVM_PUSHLEN() {
		this.opcode = SVM_Instruction.iPUSHLEN;
	}
	
	@Override
	public void execute() {
		IntegerValue size = IntegerValue.of(Type.T_SIZE, RTStack.size());
		RTStack.push(size);
		Global.PSC.ofst++;
	}
	
	@Override
	public String toString() {
		return "PUSHLEN  ";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_PUSHLEN instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_PUSHLEN instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_PUSHLEN instr = new SVM_PUSHLEN();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
