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
import svm.value.Value;

/// SVM-INSTRUCTION: NEG
/// 
/// 	Runtime Stack
/// 	   ..., tos →
/// 	   ..., result
///
/// The 'tos' is popped off the Runtime stack.
/// The 'result' is calculated as result = - tos.
/// Then the 'result' is pushed onto the Runtime Stack.
/// 
/// 'tos' must be of the arithmetic type, i.e. int, float or double.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_NEG.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_NEG extends SVM_Instruction {

	/// Construct a new SVM_NEG instruction
	public SVM_NEG() {
		this.opcode = SVM_Instruction.iNEG;
	}

	@Override
	public void execute() {
		Value tos = RTStack.pop();
		Value res = (tos == null)? null : tos.neg();
		RTStack.push(res);
		Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "NEG      ";
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_NEG instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_NEG instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_NEG instr = new SVM_NEG();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
