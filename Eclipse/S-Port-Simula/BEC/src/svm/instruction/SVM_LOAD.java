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
import svm.value.Value;

/// SVM-INSTRUCTION: LOAD objadr size
/// 
///		Runtime Stack
///			..., oaddr(?), offset(?), index(?) →
///			..., value1, value2, ... , value'size
///
///
/// First, if the 'objadr.indexed' flag is set, the 'index' is popped off and added to 'objadr'.
/// <br>Then Force 'objadr' unstacked. Ie. pop off any stacked part and form the resulting address 'resadr'.
/// 
/// The unstacking of the 'objadr' depend on its address kind:
/// <pre>
/// - REMOTE_ADDR: object address 'oaddr' is popped of the Runtime stack.
///                resadr := oaddr + objadr.offset
/// - REFER_ADDR:  'offset' and object address 'oaddr' is popped of the Runtime stack.
///                resadr := oaddr + objadr.offset + offset
/// - Otherwise:   resadr := objadr
/// </pre>
/// Finally, the values in address resadr, resadr+1, ... and upwards are loaded and pushed onto the Runtime stack.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_LOAD.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_LOAD extends SVM_Instruction {
	
	/// The object address
	private final ObjectAddress objadr;
	
	/// The value size
	private final int size;

	/// Construct a new SVM_LOAD instruction
	/// @param objadr the object address
	/// @param size the value size
	public SVM_LOAD(ObjectAddress objadr, int size) {
		this.opcode = SVM_Instruction.iLOAD;
		this.objadr = objadr;
		this.size = size;
	}
	
	@Override
	public void execute() {
		int idx =(! objadr.indexed)? 0 :RTStack.popInt();
		ObjectAddress addr = objadr.toRTMemAddr();
		for(int i=0;i<size;i++) {
			Value value = addr.load(idx + i);
			RTStack.push(value);
		}
		Global.PSC.ofst++;
	}
	
	@Override
	public String toString() {
		String s = "LOAD     " + objadr;
		if(size > 1) s += ", " + size;
		return s;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Construct an SVM_LOAD instruction from the given input.
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private SVM_LOAD(AttributeInputStream inpt) throws IOException {
		this.opcode = SVM_Instruction.iLOAD;
		this.objadr = ObjectAddress.read(inpt);
		this.size = inpt.readShort();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + this);
	}

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		objadr.writeBody(oupt);
		oupt.writeShort(size);
	}

	/// Reads an SVM_LOAD instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_LOAD instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		return new SVM_LOAD(inpt);
	}

}
