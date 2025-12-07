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
import svm.value.ProgramAddress;
import svm.value.Value;

/// SVM-INSTRUCTION: SWITCH destab
/// 
/// 	Runtime Stack
/// 	   index →
/// 	   - empty
///
/// The 'index' is popped of the Runtim stack.
/// Then Program Sequence Control PCS := destab(index)
///
/// The 'destab' is an array of Program Addresses.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_SWITCH.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_SWITCH extends SVM_Instruction {
	
	/// The destination table
	private final ProgramAddress[] DESTAB;

	/// Construct a new SVM_STORE instruction
	/// @param DESTAB the destination table
	public SVM_SWITCH(ProgramAddress[] DESTAB) {
		this.opcode = SVM_Instruction.iSWITCH;
		this.DESTAB = DESTAB;
	}

	@Override
	public void execute() {
		int idx = RTStack.popInt();
		Global.PSC = DESTAB[idx].copy();
	}
	
	@Override	
	public String toString() {
		String s = "SWITCH   " + DESTAB.length;
		for(int i=0;i<DESTAB.length;i++) {
			s = s + " " + DESTAB[i];
		}
		return s;
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Construct SVM_SWITCH instruction from the given input.
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private SVM_SWITCH(AttributeInputStream inpt) throws IOException {
		this.opcode = SVM_Instruction.iSWITCH;
		int n = inpt.readShort();
		DESTAB = new ProgramAddress[n];
		for(int i=0;i<n;i++) {
			DESTAB[i] = (ProgramAddress) Value.read(inpt);
		}		
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + this);
	}

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		int n = DESTAB.length;
		oupt.writeShort(n);
		for(int i=0;i<n;i++) {
			DESTAB[i].write(oupt);
		}
	}

	/// Reads an SVM_SWITCH instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_SWITCH instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		return new SVM_SWITCH(inpt);
	}

}
