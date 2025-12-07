/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.segment;

import java.io.IOException;
import java.util.Vector;

import bec.Option;
import bec.descriptor.Kind;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import svm.instruction.SVM_Instruction;
import svm.value.ProgramAddress;

/// Program Segment.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/segment/ProgramSegment.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class ProgramSegment extends Segment {
	
	/// The set of instructions in this ProgramSegment.
	public Vector<SVM_Instruction> instructions;

	/// ProgramSegment constructor.
	/// @param ident the ProgramSegment ident
	public ProgramSegment(final String ident) {
		super(ident, Kind.K_SEG_CODE);
		this.ident = ident.toUpperCase();
		instructions = new Vector<SVM_Instruction>();
	}
	
	/// Returns the next ProgramAddress
	/// @return the next ProgramAddress
	public ProgramAddress nextAddress() {
		return new ProgramAddress(Type.T_PADDR, this.ident, instructions.size());
	}
	
//	public SVM_Instruction load(int index) {
//		return instructions.get(index);
//	}
	
	/// Emit an instruction by adding it to this ProgramSegment.
	/// @param instr instr to be added
	public void emit(final SVM_Instruction instr) {
		instructions.add(instr);
	}
	
	/// Listing utility: index to the last listed instruction.
	private int lastListed;
	
	/// Listing utility: list all instructions not yet listed.
	public void listInstructions() {
		while(lastListed < instructions.size()) {
			listIntruction("                                 ==> ",lastListed++);
		}
	}
	
	/// Listing utility: List an instruction
	/// @param indent indentation String
	/// @param idx index to the instruction
	public void listIntruction(final String indent, final int idx) {
		String line = ident + "[" + idx + "] ";
		while(line.length() < 8) line = " " +line;
		String value = ""+instructions.get(idx);
		IO.println(indent + line + value);
		
	}
	
	@Override
	public void dump(final String title) {
		dump(title,0,instructions.size());
	}
	
	@Override
	public void dump(final String title, final int from, final int to) {
		IO.println("========================== " + title + ident + " DUMP ==========================");
		for(int i=from;i<to;i++) {
			listIntruction("",i);
		}
		IO.println("========================== " + title + ident + " END  ==========================");
	}

	@Override
	public String toString() {
		return "ProgramSegment \"" + ident + '"';
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE)
			IO.println("ProgramSegment.Write: " + this + ", Size=" + instructions.size());
		if(instructions.size() == 0) return;
		oupt.writeByte(segmentKind);
		oupt.writeString(ident);
		oupt.writeShort(instructions.size());
		for(int i=0;i<instructions.size();i++) {
			SVM_Instruction val = instructions.get(i);
			if(val == null)
				 oupt.writeByte(Sinstr.S_NULL);
			else val.write(oupt);
		}
	}

	/// Returns a ProgramSegment read from the given AttributeInputStream
	/// @param inpt the AttributeInputStream
	/// @return a ProgramSegment read from the given AttributeInputStream
	/// @throws IOException if IOException occur
	public static ProgramSegment readObject(final AttributeInputStream inpt) throws IOException {
		String ident = inpt.readString();
		ProgramSegment seg = new ProgramSegment(ident);
		int n = inpt.readShort();
		for(int i=0;i<n;i++) {
			seg.instructions.add(SVM_Instruction.readObject(inpt));
		}
		if(Option.ATTR_INPUT_TRACE) IO.println("ProgramSegment.Read: " + seg);
		if(Option.ATTR_INPUT_DUMP) seg.dump("ProgramSegment.readObject: ");
		return seg;
	}
	

}
