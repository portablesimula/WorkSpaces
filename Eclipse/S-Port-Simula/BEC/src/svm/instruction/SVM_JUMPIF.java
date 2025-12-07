/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.scode.Relation;
import bec.scode.Sinstr;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.RTStack;
import svm.value.ProgramAddress;
import svm.value.Value;

/// SVM-INSTRUCTION: JUMPIF relation size paddr
/// 
/// 	Runtime Stack
/// 	   ..., sos, tos →
/// 	   ...
///
/// The 'tos' and 'sos' are popped off the Runtime stack.
/// The 'result' is calculated as result = sos relation tos.
/// Note: Both 'tos' abd 'sos' may be multi-sized.
///
/// Conditional Jump to paddr.
///
/// A conditional jump is executed, branching only if the relation evaluates true.
/// Ie. The Program Sequence Control PCS := paddr
/// otherwise PCS is incremented by one.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_JUMPIF.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_JUMPIF extends SVM_JUMP {
	
	/// The relation
	private final Relation relation;
	
	/// The test value size
	private final int size;
	
	/// Construct a new SVM_JUMP instruction
	/// @param relation the relation
	/// @param size the test value size
	/// @param destination the jump destination address
	public SVM_JUMPIF(Relation relation, int size, ProgramAddress destination) {
		super(destination);
		this.opcode = SVM_Instruction.iJUMPIF;
		this.relation = relation;
		this.size =  size;
	}

	@Override
	public void execute() {
		boolean doJump = false;
		if(size == 1) {
			Value tos = RTStack.pop();
			Value sos = RTStack.pop();
			doJump = relation.compare(sos, tos);
		} else {
			Value[] TOS = new Value[size];
			Value[] SOS = new Value[size];
			for(int i=0;i<size;i++) TOS[i] = RTStack.pop();
			for(int i=0;i<size;i++) SOS[i] = RTStack.pop();
			boolean equals = true;
			Relation eqRel = new Relation(Sinstr.S_EQ);
			LOOP:for(int i=0;i<size;i++) {
				if(! eqRel.compare(SOS[i], TOS[i])) {
					equals = false; break LOOP;
				}
			}
			switch(relation.relation) {
				case Sinstr.S_EQ: doJump = equals; break;
				case Sinstr.S_NE: doJump = ! equals; break;
				default: Util.IERR("");
			}
		}
		
		if(doJump) Global.PSC = destination.copy();
		else Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "JUMPIF   " + relation + " " + destination;
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Construct an SVM_JUMPIF instruction from the given input.
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private SVM_JUMPIF(AttributeInputStream inpt) throws IOException {
		super(inpt);
		this.opcode = SVM_Instruction.iJUMPIF;
		this.relation = Relation.read(inpt);
		this.size = inpt.readUnsignedByte();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + this);
	}

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		destination.write(oupt);
		relation.write(oupt);
		oupt.writeByte(size);
	}

	/// Reads an SVM_JUMPIF instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_JUMPIF instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		return new SVM_JUMPIF(inpt);
	}

}
