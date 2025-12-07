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
import svm.CallStackFrame;
import svm.RTStack;
import svm.value.ObjectAddress;
import svm.value.ProgramAddress;
import svm.value.Value;

/// SVM-INSTRUCTION: RETURN rutID returAddr
/// 
///		Runtime Stack
///			..., ??? →
///			..., ???
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_RETURN.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_RETURN extends SVM_Instruction {
	
	/// The Routine ident
	private final String rutID;
	
	/// The retur address
	private final ObjectAddress returAddr;

	/// Construct a new SVM_RETURN instruction
	/// @param rutID the Routine ident
	/// @param returAddr the retur address
	public SVM_RETURN(String rutID, ObjectAddress returAddr) {
		this.opcode = SVM_Instruction.iRETURN;
		this.rutID = rutID;
		this.returAddr = returAddr;
		if(rutID == null) Util.IERR("");
		if(returAddr == null) Util.IERR("");
	}
	
	@Override	
	public String toString() {
		return "RETURN   " + rutID + " " + returAddr;
	}
	
	@Override	
	public void execute() {
		ProgramAddress padr = (ProgramAddress) returAddr.load(0);
		if(Option.CALL_TRACE_LEVEL > 0) {
			IO.println("SVM_RETURN.execute: RETURN From "+ rutID + " and Continue at " + padr);
			RTStack.printCallTrace("SVM_RETURN.execute: RETURN From "+ rutID + " and Continue at " + padr);
		}
		RTStack.checkStackEmpty();
		CallStackFrame callStackTop = RTStack.callStack_TOP();
		if(Option.EXEC_TRACE > 0) {
			ProgramAddress.printInstr(this,false);
		}
		
		int n = RTStack.size() - callStackTop.rtStackIndex - callStackTop.exportSize;
		for(int i=0;i<n;i++) RTStack.pop();	
		
		RTStack.callStack.pop();
		
		if(Option.EXEC_TRACE > 1) {
			callStackTop = RTStack.callStack_TOP();
			if(callStackTop == null) {
				RTStack.dumpRTStack("SVM_RETURN: From "+ rutID + " and Continue at " + padr);
			} else {
				callStackTop.dump("SVM_RETURN: From "+ rutID + " and Continue at " + padr);
			}
		}
		Global.PSC = padr.copy();
	}

	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Construct an SVM_RETURN instruction from the given input.
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private SVM_RETURN(AttributeInputStream inpt) throws IOException {
		this.opcode = SVM_Instruction.iRETURN;
		this.rutID = inpt.readString();
		this.returAddr = (ObjectAddress) Value.read(inpt);
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + this);
	}

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeString(rutID);
		returAddr.write(oupt);
	}

	/// Reads an SVM_RETURN instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_RETURN instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		return new SVM_RETURN(inpt);
	}

}
