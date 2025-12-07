/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.descriptor.Descriptor;
import bec.descriptor.LabelDescr;
import bec.descriptor.RoutineDescr;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.EndProgram;
import bec.util.Util;
import svm.CallStackFrame;
import svm.RTStack;
import svm.instruction.SVM_CALL;
import svm.instruction.SVM_CALL_SYS;
import svm.instruction.SVM_Instruction;
import svm.instruction.SVM_RETURN;
import svm.segment.DataSegment;
import svm.segment.ProgramSegment;
import svm.segment.Segment;

/// ProgramAddress.
/// 
///		program_address ::= c-paddr label:tag
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/ProgramAddress.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class ProgramAddress extends Value {
	
	/** Default Constructor */ public ProgramAddress() {} 

	/// Segment ident
	public String segID;
	
	/// Address offset
	public int ofst;
	
	/// Construct a new ProgramAddress with the given parameters
	/// @param type the address type: PADDR or RADDR
	/// @param segID the Segment ident when kind SEGMNT_ADDR
	/// @param ofst The offset
	public ProgramAddress(final Type type, final String segID, final int ofst) {
		this.type = type;
		this.segID = segID;
		this.ofst = ofst;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this ProgramAddress Value.
	/// Then construct a new ProgramAddress instance.
	/// @param type The address type
	/// @return that ProgramAddress instance.
	public static ProgramAddress ofScode(final Type type) {
		Tag tag = Tag.ofScode();
		Descriptor descr = tag.getMeaning();
		if(type == Type.T_RADDR) return ((RoutineDescr)descr).getAddress();
		if(type == Type.T_PADDR) return ((LabelDescr)descr).getAddress();
		Util.IERR("");
		return null;
	}
	
	/// Create a new Fixup address.
	/// @param type The address type
	/// @return an ProgramAddress
	public static ProgramAddress ofFixup(final Type type) {
		return new ProgramAddress(type, null, 0);
	}

	/// Fixup this ProgramAddress by the given parameters
	/// @param segID the segment ident
	/// @param ofst the offset
	public void fixupAddress(final String segID, final int ofst) {
		this.segID = segID;
		this.ofst = ofst;
	}

	/// Returns the Segment part of a Segment Address
	/// @return the Segment part of a Segment Address
	public Segment segment() {
		if(segID == null) return null;
		return Segment.lookup(segID);
	}
	
	/// Returns a copy of this ObjectAddress
	/// @return a copy of this ObjectAddress
	public ProgramAddress copy() {
		return new ProgramAddress(type, segID, ofst);
	}
	
	@Override
	public void emit(final DataSegment dseg) {
		dseg.emit(this);
	}

	@Override
	public boolean compare(final int relation, final Value other) {
		String RHSegID = (other == null)? null : ((ProgramAddress)other).segID;
		int rhs = (other == null)? 0 : ((ProgramAddress)other).ofst;
		return Segment.compare(segID, ofst, relation, RHSegID, rhs);
	}
	
	/// Execute this ProgramSegment
	public void execute() {
		ProgramSegment seg = (ProgramSegment) segment();
		int size = seg.instructions.size();
		if(size == 0) {
			throw new EndProgram(-1,"ProgramAddress.execute: " + seg.ident + " IS EMPTY -- NOTHING TO EXECUTE");
		}
		
		if(ofst >= size) {
			if(Option.DUMPS_AT_EXIT) {
//				Segment.lookup("DSEG_ADHOC02").dump("ProgramAddress.execute: ");
				Global.DSEG.dump("ProgramAddress.execute: FINAL DATA SEGMENT ");
				Global.CSEG.dump("ProgramAddress.execute: FINAL CONSTANT SEGMENT ");
				Global.TSEG.dump("ProgramAddress.execute: FINAL CONSTANT TEXT SEGMENT ");
//				Segment.lookup("DSEG_RT").dump("ProgramAddress.execute: BIOINS", 30, 82);
//				Segment.lookup("POOL_1").dump("ProgramAddress.execute: FINAL POOL_1", 0, 60);
//				RTUtil.printPool("POOL_1");
			}
			RTStack.checkStackEmpty();
			throw new EndProgram(0,"ProgramAddress.execute: " + seg.ident + " IS FINALIZED -- NOTHING MORE TO EXECUTE");
		} else {
			SVM_Instruction cur = seg.instructions.get(ofst);
			try {
				cur.execute();
			} catch(EndProgram e) { throw e; // RE-THROW
			} catch(Exception e) {
				Util.println("\n\nProgramAddress.execute: FATAL ERROR: EXECUTION FAILED !");
				printInstr(cur,false);
				e.printStackTrace();
				seg.dump("",ofst-20,ofst+20);
				throw new EndProgram(-1, "EXECUTION FAILED !");
			}

			if(Option.EXEC_TRACE > 0) {
				if(cur instanceof SVM_CALL)          ; // NOTHING
				else if(cur instanceof SVM_RETURN)   ; // NOTHING
				else if(cur instanceof SVM_CALL_SYS) ; // NOTHING
				else printInstr(cur,true);
			}
		}
	}
	
	/// Print a SVM-Instruction
	/// @param cur a SVM-Instruction
	/// @param decr signals decrement of paddr in the printout
	public static void printInstr(final SVM_Instruction cur, final boolean decr) {
		printInstr(cur.toString(), decr);
	}
	
	/// Print a SVM-Instruction
	/// @param cur a SVM-Instruction
	/// @param decr signals decrement of paddr in the printout
	public static void printInstr(final String cur, final boolean decr) {
		CallStackFrame callStackTop = RTStack.callStack_TOP();
		String tail = (callStackTop == null)? RTStack.toLine() : callStackTop.toLine();
		ProgramAddress paddr = Global.PSC.copy();
		if(decr) paddr.ofst--;
		String line = "EXEC: "+paddr+"  "+cur;
		while(line.length()<70) line=line+' ';
		Util.println(line+"   "+tail);
	}
	
	@Override
	public String toString() {
		String s = (segID == null) ? "RELADR" : segID;
		return s + '[' + ofst + ']';
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_C_PADDR);
		type.write(oupt);
		oupt.writeString(segID);
		oupt.writeShort(ofst);
	}

	/// Reads a ObjectAddress from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the ObjectAddress read
	/// @throws IOException if IOException occur
	public static ProgramAddress read(final AttributeInputStream inpt) throws IOException {
		Type type = Type.read(inpt);
		String segID = inpt.readString();
		int ofst = inpt.readShort();
		return new ProgramAddress(type, segID, ofst);
	}

}
