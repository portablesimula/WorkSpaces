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

/// SVM-INSTRUCTION: PRECALL rutIdent nParSlots exportSize importSize
/// 
///		Runtime Stack
///			..., arg1, arg2, ... , arg'n →
///			..., value1, value2, ... , value'size
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_PRECALL.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_PRECALL extends SVM_Instruction {
	
	/// The Routine ident
	private final String rutIdent;
	
	/// The number of Parameter Slots
	private final int nParSlots;
	
	/// The Export size
	private final int exportSize;
	
	/// The Import size
	private final int importSize;

	/// Construct a new SVM_PRECALL instruction
	/// @param rutIdent the Routine ident
	/// @param nParSlots the number of Parameter Slots
	/// @param exportSize the Export size
	/// @param importSize the Import size
	public SVM_PRECALL(String rutIdent, int nParSlots, int exportSize, int importSize) {
		this.opcode = SVM_Instruction.iPRECALL;
		this.rutIdent = rutIdent;
		this.nParSlots = nParSlots;
		this.exportSize = exportSize;
		this.importSize = importSize;
	}

	@Override
	public void execute() {
		RTStack.precallStack.push(new CallStackFrame(rutIdent, RTStack.size() - nParSlots, exportSize, importSize));
		if(exportSize > 0) {
			if(nParSlots > 0) {
				RTStack.addExport(nParSlots, exportSize);
			} else {
				for(int i=0;i<exportSize;i++) {
					RTStack.push(null); // Export slots		
				}
			}
		}
		Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "PRECALL   " + rutIdent + ", nParSlots=" + nParSlots + ", exportSize=" + exportSize + ", importSize=" + importSize;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override	
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeString(rutIdent);
		oupt.writeShort(nParSlots);
		oupt.writeShort(exportSize);
		oupt.writeShort(importSize);
	}

	/// Reads an SVM_PRECALL instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_PRECALL instruction read
	/// @throws IOException if IOException occur
	public static SVM_PRECALL read(AttributeInputStream inpt) throws IOException {
		String rutIdent = inpt.readString();
		int nParSlots = inpt.readShort();
		int exportSize = inpt.readShort();
		int importSize = inpt.readShort();
		SVM_PRECALL instr = new SVM_PRECALL(rutIdent, nParSlots, exportSize, importSize);
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
