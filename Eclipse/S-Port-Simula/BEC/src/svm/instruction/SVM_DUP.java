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

/// SVM-INSTRUCTION: DUP n
/// 
///		Runtime Stack
///			..., value1, value2, ... , value'n →
///			..., value1, value2, ... , value'n, value1, value2, ... , value'n
///
/// A duplicate of the 'n' top values are is pushed onto the Runtime stack.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_DUP.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_DUP extends SVM_Instruction {
	
	/// The number of stack items to duplicate
	private final int n;
	
	/// Construct a new SVM_DUP instruction
	/// @param n the number of stack items to duplicate
	public SVM_DUP(int n) {
		this.opcode = SVM_Instruction.iDUP;
		this.n = n;
	}
	
	@Override
	public void execute() {
		if(n == 1) {
			RTStack.push(RTStack.peek());				
		} else {
			RTStack.dup(n);
		}
		Global.PSC.ofst++;
	}

	@Override	
	public String toString() {
		return "DUP      " + n;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override	
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeShort(n);
	}

	/// Reads an SVM_DUP instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_DUP instruction read
	/// @throws IOException if IOException occur
	public static SVM_DUP read(AttributeInputStream inpt) throws IOException {
		int n = inpt.readShort();
		SVM_DUP instr = new SVM_DUP(n);
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
