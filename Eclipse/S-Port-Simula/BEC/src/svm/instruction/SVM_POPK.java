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

/// SVM-INSTRUCTION: POPK n
/// 
///		Runtime Stack
///			..., value1, value2, ... , value'n →
///			...,
///
/// The 'n' values on the top of the Runtime stack is popped off and forgotten.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_POPK.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_POPK extends SVM_Instruction {
	
	/// The number of stack items to pop
	private final int n;
	
	/// Construct a new SVM_POPK instruction
	/// @param n the number of stack items to pop
	public SVM_POPK(int n) {
		this.opcode = SVM_Instruction.iPOPK;
		this.n = n;
	}

	@Override
	public void execute() {
		for(int i=0;i<n;i++) {
			if(RTStack.curSize() <= 0) Util.IERR("RTStack underflow");
			RTStack.pop();
		}
		Global.PSC.ofst++;
	}

	public String toString() {
		return "POPK     " + n;
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Construct an SVM_POPK instruction from the given input.
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private SVM_POPK(AttributeInputStream inpt) throws IOException {
		this.opcode = SVM_Instruction.iPOPK;
		this.n = inpt.readShort();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + this);
	}

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeShort(n);
	}

	/// Reads an SVM_POPK instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_POPK instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		return new SVM_POPK(inpt);
	}

}
