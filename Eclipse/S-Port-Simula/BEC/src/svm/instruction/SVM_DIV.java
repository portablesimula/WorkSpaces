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
import bec.util.Util;
import svm.RTStack;
import svm.value.Value;

/// SVM-INSTRUCTION: DIV
/// 
/// 	Runtime Stack
/// 	   ..., sos, tos →
/// 	   ..., result
///
/// The 'tos' and 'sos' are popped off the Runtime stack.
/// <br>The 'result' is calculated as result = sos / tos.
/// <br>Then the 'result' is pushed onto the Runtime Stack.
/// 
/// 'tos' and 'sos' must be of the same arithmetic type, i.e. int, float or double.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_DIV.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_DIV extends SVM_Instruction {

	/// Construct a new SVM_DIV instruction
	public SVM_DIV() {
		this.opcode = SVM_Instruction.iDIV;
	}

	@Override
	public void execute() {
		Value tos = RTStack.pop();
		Value sos = RTStack.pop();
		if(tos == null) Util.IERR("DIV by zero: " + sos + " / 0");
		Value res = (tos == null)? null : tos.div(sos);
		RTStack.push(res);
		Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "DIV      ";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override	
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_DIV instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_DIV instruction read
	/// @throws IOException if IOException occur
	public static SVM_DIV read(AttributeInputStream inpt) throws IOException {
		SVM_DIV instr = new SVM_DIV();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
