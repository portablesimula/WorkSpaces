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

/// SVM-INSTRUCTION: AND
///
/// 	Runtime Stack
/// 	   ..., sos, tos →
/// 	   ..., result
///
/// The 'tos' and 'sos' are popped off the Runtime stack.
/// <br>The 'result' is calculated as result = sos and tos.
/// <br>Then the 'result' is pushed onto the Runtime Stack.
/// 
/// 'tos' and 'sos' must be of the same type, boolean or int.
/// 
/// The 'and' operation is defined by the following matrix:
/// <pre>
///       AND
///     a  \  b  true   false
///     true     true   false
///     false    false  false
/// </pre>
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_AND.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_AND extends SVM_Instruction {

	/// Construct a new SVM_AND instruction
	public SVM_AND() {
		this.opcode = SVM_Instruction.iAND;
	}

	@Override
	public void execute() {
		Value tos = RTStack.pop();
		Value sos = RTStack.pop();
		Value res = (tos == null)? null : tos.and(sos);
		RTStack.push(res);
		Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "AND      ";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_AND instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_AND instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_AND instr = new SVM_AND();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}


}
