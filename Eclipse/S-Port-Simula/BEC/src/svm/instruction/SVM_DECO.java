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

/// SVM-INSTRUCTION: DECO
/// 
/// 	Runtime Stack
/// 	   ..., sos, tos →
/// 	   ..., result
///
/// The size 'tos' and the oaddr 'sos' are popped off the Runtime stack.
/// <br>The 'result' is calculated as result = new ObjectAddress(sos.segID, sos.offset - tos)
/// <br>Then the 'result' is pushed onto the Runtime Stack.
/// 
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_DECO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_DECO extends SVM_Instruction {

	/// Construct a new SVM_DECO instruction
	public SVM_DECO() {
		this.opcode = SVM_Instruction.iDECO;
	}

	@Override
	public void execute() {
		int tos = RTStack.popInt();
		ObjectAddress sos = RTStack.popOADDR();
		ObjectAddress res = (sos == null)? ObjectAddress.ofRelFrameAddr( -tos) : sos.addOffset(-tos);
		RTStack.push(res);
		Global.PSC.ofst++;
	}

	@Override	
	public String toString() {
		return "DECO     ";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override	
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_DECO instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_DECO instruction read
	/// @throws IOException if IOException occur
	public static SVM_DECO read(AttributeInputStream inpt) throws IOException {
		SVM_DECO instr = new SVM_DECO();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
