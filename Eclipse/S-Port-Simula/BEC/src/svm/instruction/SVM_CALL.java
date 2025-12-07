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
import svm.value.ProgramAddress;
import svm.value.Value;

/// SVM-INSTRUCTION: CALL rutadr returSlot
///
///	  Runtime Stack
///		..., value1, value2, ... , value'nPar →
///		..., value1, value2, ... , value'exportSize
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_CALL.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_CALL extends SVM_Instruction {
	
	/// The routine address
	private final ProgramAddress rutAddr;
	
	/// The frame slot for the retur address
	private final ObjectAddress returSlot;

	/// Construct a new SVM_CALL_SYS instruction
	/// @param rutAddr the routine address
	/// @param returSlot the frame slot for the return address
	public SVM_CALL(ProgramAddress rutAddr, ObjectAddress returSlot) {
		this.opcode = SVM_Instruction.iCALL;
		this.rutAddr = rutAddr;
		this.returSlot = returSlot;
	}
	
	/// Returns a new SVM_CALL with the given 'returSlot'
	/// @param returSlot the frame slot for the return address
	/// @return a new SVM_CALL with the given 'returSlot'
	public static SVM_CALL ofTOS(ObjectAddress returSlot) {
		return new SVM_CALL(null, returSlot);
	}
	
	@Override	
	public void execute() {
		ProgramAddress retur = Global.PSC.copy();
		retur.ofst++;
		if(Option.EXEC_TRACE > 0) {
			ProgramAddress.printInstr(this,false);
		}
		if(rutAddr == null) {
			// CALL-TOS			
			Global.PSC = (ProgramAddress) RTStack.pop().copy();
		} else {
			Global.PSC = rutAddr.copy();
		}
		RTStack.push(retur);
	}
	
	@Override	
	public String toString() {
		String tail = " Return=" + returSlot;
		if(rutAddr == null)
		return "CALL     TOS" + tail;
		return "CALL     " + rutAddr + tail;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Construct an SVM_CALL instruction from the given input.
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private SVM_CALL(AttributeInputStream inpt) throws IOException {
		this.opcode = SVM_Instruction.iCALL;
		this.returSlot = (ObjectAddress) Value.read(inpt);
		boolean present = inpt.readBoolean();
		this.rutAddr = (!present)? null : (ProgramAddress) Value.read(inpt);
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + this);
	}

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		returSlot.write(oupt);
		if(rutAddr != null) {
			oupt.writeBoolean(true);
			rutAddr.write(oupt);
		} else oupt.writeBoolean(false);
	}

	/// Reads an SVM_CALL instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_CALL instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		return new SVM_CALL(inpt);
	}


}
