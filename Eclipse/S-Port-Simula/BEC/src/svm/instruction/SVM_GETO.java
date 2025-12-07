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
import svm.value.ObjectAddress;

/// SVM-INSTRUCTION: GETO
/// 
/// 	Runtime Stack
/// 	   ... →
/// 	   ..., next
///
/// Push 'next' pointer of an earlier registered save-object.
/// <br>In case the 'next' being onone, no more pointers and the scan 
/// <br>of the save-object should be terminated.
///
/// If more pointers the SAVE-INDEX is updated.
/// <br>In case the "next" pointer is onone, the pointer is skipped.
///
/// See: SVM_INITO and SVM_SETO.
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_GETO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_GETO extends SVM_Instruction {

	/// Construct a new SVM_GETO instruction
	public SVM_GETO() {
		this.opcode = SVM_Instruction.iGETO;
	}

	@Override
	public void execute() {
		ObjectAddress next = SVM_INITO.get();
		RTStack.push(next);
		Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "GETO     ";
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_GETO instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_GETO instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_GETO instr = new SVM_GETO();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
