/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import svm.RTStack;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;

/// SVM-INSTRUCTION: LOADA objadr size
/// 
///		Runtime Stack
///			..., oaddr(?), offset(?), index(?) →
///			..., resadr, ofst
///
/// First, if the 'objadr.indexed' flag is set, the 'index' is popped off the Runtime stack and added to 'offset'.
///
/// Then Force 'objadr' unstacked. Ie. pop off any stacked part and form the resulting address 'resadr'.
/// 
/// The unstacking of the 'objadr' depend on its address kind:
/// <pre>
/// - REMOTE_ADDR: object address 'oaddr' is popped of the Runtime stack.
///                resadr := oaddr + objadr.offset
/// - REFER_ADDR:  'offset' and object address 'oaddr' is popped of the Runtime stack.
///                resadr := oaddr + objadr.offset + offset
/// - Otherwise:   resadr := objadr
/// </pre>
/// Finally, 'resadr' and 'offset' are pushed onto the Runtime stack.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_LOADA.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_LOADA extends SVM_Instruction {
	
	/// The object address
	private final ObjectAddress objadr;
	
	/// The extra offset
	private final int offset;

	/// Construct a new SVM_LOADA instruction
	/// @param objadr the object address
	/// @param offset the extra offset
	public SVM_LOADA(ObjectAddress objadr, int offset) {
		this.opcode = SVM_Instruction.iLOADA;
		this.objadr = objadr;
		this.offset = offset;
	}

	@Override
	public void execute() {
		int ofst = (! objadr.indexed)? offset : offset + RTStack.popInt();
		ObjectAddress resadr = objadr.toRTMemAddr();
		RTStack.push(resadr);
		RTStack.push(IntegerValue.of(Type.T_INT, ofst));
		
		Global.PSC.ofst++;
	}

	@Override	
	public String toString() {
		String tail = "";
	    if(objadr.kind == ObjectAddress.REMOTE_ADDR) tail = tail +" withRemoteBase";
	    tail += " stackedPart";
		return "LOADA    " + objadr + tail;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Construct an SVM_LOADA instruction from the given input.
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private SVM_LOADA(AttributeInputStream inpt) throws IOException {
		this.opcode = SVM_Instruction.iLOADA;
		this.objadr = ObjectAddress.read(inpt);
		this.offset = inpt.readShort();
	}

	@Override	
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		objadr.writeBody(oupt);
		oupt.writeShort(offset);
	}

	/// Reads an SVM_LOADA instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_LOADA instruction read
	/// @throws IOException if IOException occur
	public static SVM_LOADA read(AttributeInputStream inpt) throws IOException {
		SVM_LOADA instr = new SVM_LOADA(inpt);
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
