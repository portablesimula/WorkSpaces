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

/**
 * Code is generated that inserts the value described by TOS into the pointer variable refered by
 * SAVE-OBJECT and SAVE-INDEX. Note that t-seto does not update SAVE-INDEX.
 * TOS is popped.
 */
/// SVM-INSTRUCTION: GETO
/// 
/// 	Runtime Stack
/// 	   ..., oaddr →
/// 	   ...
///
/// An oaddr is popped of the Runtime stack and inserted
/// into the pointer variable refered by SAVE-OBJECT and SAVE-INDEX.
/// Note that t-seto does not update SAVE-INDEX.
///
/// See: SVM_INITO and SVM_GETO.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_SETO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_SETO extends SVM_Instruction {

	/// Construct a new SVM_SETO instruction
	public SVM_SETO() {
		this.opcode = SVM_Instruction.iSETO;
	}

	@Override
	public void execute() {
		ObjectAddress oaddr = RTStack.popOADDR();
		SVM_INITO.set(oaddr);
		Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "SETO      ";
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_SETO instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_SETO instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_SETO instr = new SVM_SETO();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
