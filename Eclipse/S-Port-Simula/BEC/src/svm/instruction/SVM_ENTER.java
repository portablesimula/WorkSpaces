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
import svm.CallStackFrame;
import svm.RTStack;
import svm.value.ProgramAddress;

/// SVM-INSTRUCTION: ENTER
/// 
/// Enter a Routine by pushing local variables onto the runtime stack.
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_ENTER.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_ENTER extends SVM_Instruction {
	
	/// The Routine ident
	private final String rutIdent;
	
	/// The total size of the local variables
	private final int localSize;

	/// Construct a new SVM_ENTER instruction
	/// @param rutIdent the Routine ident
	/// @param localSize the total size of the local variables
	public SVM_ENTER(String rutIdent, int localSize) {
		this.opcode = SVM_Instruction.iENTER;
		this.rutIdent = rutIdent;
		this.localSize = localSize;
	}

	@Override
	public void execute() {
		CallStackFrame callStackItem = RTStack.precallStack.pop();
		ProgramAddress rutAddr = Global.PSC.copy();
		callStackItem.rutAddr = rutAddr;
		callStackItem.localSize = localSize;
		RTStack.callStack.push(callStackItem);
		for(int i=0;i<localSize;i++) {
			RTStack.push(null);
		}
		if(Option.EXEC_TRACE > 2)
			callStackItem.dump(""+rutAddr+": ENTER: " + rutIdent + ", localSize=" + localSize);
		if(Option.CALL_TRACE_LEVEL > 0) {
			RTStack.printCallTrace("SVM_ENTER.execute: ENTER " + rutIdent + ", localSize=" + localSize);
		}
		Global.PSC.ofst++;
	}

	@Override	
	public String toString() {
		return "ENTER    nLOCAL=" + localSize;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override	
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeString(rutIdent);
		oupt.writeShort(localSize);
	}

	/// Reads an SVM_ENTER instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_ENTER instruction read
	/// @throws IOException if IOException occur
	public static SVM_ENTER read(AttributeInputStream inpt) throws IOException {
		SVM_ENTER instr = new SVM_ENTER(inpt.readString(), inpt.readShort());
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
