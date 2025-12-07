/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import svm.RTStack;
import svm.value.ProgramAddress;

/// SVM-INSTRUCTION: GOTO
/// 
/// 	Runtime Stack
/// 	   paddr →
/// 	   - empty
///
/// The paddr is popped of the Runtim stack.
/// <br>Then Program Sequence Control PCS := paddr
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_GOTO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_GOTO extends SVM_Instruction {

	/// Construct a new SVM_GOTO instruction
	public SVM_GOTO() {
		this.opcode = SVM_Instruction.iGOTO;
	}

	@Override
	public void execute() {
		ProgramAddress target = (ProgramAddress) RTStack.pop();
		Global.PSC = target.copy();
		RTStack.checkStackEmpty();
	}
	
	@Override	
	public String toString() {
		return "GOTO     ";
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_GOTO instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_GOTO instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_GOTO instr = new SVM_GOTO();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
