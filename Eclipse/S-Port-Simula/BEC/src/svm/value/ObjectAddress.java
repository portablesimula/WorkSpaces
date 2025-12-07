/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;

import bec.Option;
import bec.descriptor.ConstDescr;
import bec.descriptor.Descriptor;
import bec.descriptor.Variable;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.CallStackFrame;
import svm.RTStack;
import svm.segment.DataSegment;
import svm.segment.Segment;

/// ObjectAddress.
/// 
///		object_address ::= c-oaddr global_or_const:tag
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/ObjectAddress.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author /** Address kind */ ystein Myhre Andersen
public class ObjectAddress extends Value {

	/** Default Constructor */ public ObjectAddress() {} 

	/// Address kind
	public int kind;
	
	/// Segment ident when kind SEGMNT_ADDR
	public String segID;
	
	/// Address offset
	public int ofst;
	
	/// Signals 'indexed' address. In that case an index offset is pushed to the runtime stack.
	public boolean indexed;
	
	/** Address kind */ public static final int SEGMNT_ADDR = 1; // Segment Address
	/** Address kind */ public static final int REMOTE_ADDR = 2; // Remote Address
	/** Address kind */ public static final int REFER_ADDR  = 3; // Refer Address
	/** Address kind */ public static final int REL_ADDR    = 4; // Frame relative Addtess
	/** Address kind */ public static final int STACK_ADDR  = 5; // Stack relative Address
	
	/// Construct a new ObjectAddress with the given parameters
	/// @param kind the address kind
	/// @param segID the Segment ident when kind SEGMNT_ADDR
	/// @param ofst The offset
	/// @param indexed Signals 'indexed' address. In that case an index offset is pushed to the runtime stack.
	protected ObjectAddress(final int kind, final String segID,	final int ofst, final boolean indexed) {
		this.type = Type.T_OADDR;
		this.kind = kind;
		this.segID = segID;
		this.ofst = ofst;
		this.indexed = indexed;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this ObjectAddress Value.
	/// Then construct a new ObjectAddress instance.
	/// @return that ObjectAddress instance.
	public static ObjectAddress ofScode() {
		Tag tag = Tag.ofScode();
		Descriptor descr = tag.getMeaning();
		if(descr == null) Util.IERR("IMPOSSIBLE: TESTING FAILED");
		if(descr instanceof Variable var) return var.address;
		if(descr instanceof ConstDescr cns) return cns.getAddress();
		Util.IERR("MISSING: " + descr);
		return null;
	}
	
	/// Create a new Segment address with the given parameters
	/// @param segID the Segment ident
	/// @param ofst The offset
	/// @return an ObjectAddress
	public static ObjectAddress ofSegAddr(final String segID, final int ofst) {
		return new ObjectAddress(SEGMNT_ADDR, segID, ofst, false);
	}
	
	/// Create a new Segment address with the given parameters
	/// @param seg the Segment
	/// @param ofst The offset
	/// @return an ObjectAddress
	public static ObjectAddress ofSegAddr(final DataSegment seg, final int ofst) {
		String segID = (seg == null)? null : seg.ident;
		return new ObjectAddress(SEGMNT_ADDR, segID, ofst, false);
	}

	/// Create a new Remote address.
	/// @return an ObjectAddress
	public static ObjectAddress ofRemoteAddr() {
		return new ObjectAddress(REMOTE_ADDR, null, 0, false);
	}

	/// Create a new Refer address.
	/// @return an ObjectAddress
	public static ObjectAddress ofReferAddr() {
		return new ObjectAddress(REFER_ADDR, null, 0, false);
	}

	/// Create a new Relative address with the given offset
	/// @param ofst The offset
	/// @return an ObjectAddress
	public static ObjectAddress ofRelFrameAddr(final int ofst) {
		return new ObjectAddress(REL_ADDR, null, ofst, false);
	}

	/// Create a new Fixup address.
	/// @return an ObjectAddress
	public static ObjectAddress ofFixup() {
		return new ObjectAddress(SEGMNT_ADDR, null, 0, false);
	}
	
	/// Returns the DataSegment part of a Segment Address
	/// @return the DataSegment part of a Segment Address
	public DataSegment segment() {
		if(segID == null) return null;
		return (DataSegment) Segment.lookup(segID);
	}
		
	/// Returns a copy of this ObjectAddress
	/// @return a copy of this ObjectAddress
	public Value copy() {
		return new ObjectAddress(kind, segID, ofst, indexed);
	}
		
	/// Increment the offset part by the given ofst
	/// @param ofst the ofst to be added
	/// @return the resulting ObjectAddress
	public ObjectAddress addOffset(final int ofst) {
		return new ObjectAddress(this.kind, segID, this.ofst + ofst, indexed);
	}
	
	/// Fixup this ObjectAddress by the given address
	/// @param oaddr an Object address
	public void fixupAddress(final ObjectAddress oaddr) {
		this.segID = oaddr.segID;
		this.ofst = oaddr.ofst;
	}

	/// If this ObjectAddress is a Relative Address it is converted to a absolute Stack Address.
	/// @return an ObjectAddress possibly converted 
	public ObjectAddress toStackAddress() {
		ObjectAddress oaddr = this;
		if(this.kind == ObjectAddress.REL_ADDR) {
			CallStackFrame callStackTop = RTStack.callStack_TOP();
			int bias = (callStackTop == null)? 0 : callStackTop.rtStackIndex;
			oaddr = new ObjectAddress(STACK_ADDR, segID, bias + ofst, indexed);
		}
		return oaddr;
	}
	
	/// Force 'objadr' unstacked.
	/// Ie. pop off any stacked part and form the resulting address 'resadr'.
	/// 
	/// The unstacking of the 'objadr' depend on its address kind:
	///
	/// - REMOTE_ADDR: object address 'oaddr' is popped of the Runtime stack.
	///                resadr := oaddr + objadr.offset
	/// - REFER_ADDR:  'offset' and object address 'oaddr' is popped of the Runtime stack.
	///                resadr := oaddr + objadr.offset + offset
	/// - Otherwise:   resadr := objadr
	///
	/// @return resadr as calculated
	///
	public ObjectAddress toRTMemAddr() {
		switch(this.kind) {
			case ObjectAddress.SEGMNT_ADDR: return this; // Nothing
			case ObjectAddress.REL_ADDR:    return this; // Nothing
			case ObjectAddress.STACK_ADDR:  Util.IERR("NOT IMPL"); return this;
			case ObjectAddress.REMOTE_ADDR:	return RTStack.popOADDR().addOffset(this.ofst);
			case ObjectAddress.REFER_ADDR:
				int ofset = RTStack.popInt();
				return RTStack.popOADDR().addOffset(this.ofst + ofset);
			default: Util.IERR(""); return null;
		}
	}
	
	@Override
	public void emit(final DataSegment dseg) {
		dseg.emit(this);
	}
	
	/// Store the given value into memory address: this ObectAddress + idx
	/// @param idx the index to use
	/// @param value the value to be stored
	public void store(final int idx, final Value value) {
		switch(kind) {
			case SEGMNT_ADDR:
				DataSegment dseg = segment();
				int reladdr = ofst + idx;
				dseg.store(reladdr, value);
				break;
			case REL_ADDR:
				int frmx = RTStack.frameIndex();
				RTStack.store(frmx + ofst + idx, value);
				break;
			case STACK_ADDR:
				RTStack.store(ofst + idx, value);
				break;
			default: Util.IERR(""+kind);
		}
	}
	
	/// Load a value from memory address: this ObectAddress + idx
	/// @param idx the index to use
	/// @return value the value loaded
	public Value load(final int idx) {
		switch(kind) {
			case SEGMNT_ADDR:
				DataSegment dseg = segment();
				int reladdr = ofst + idx;				
				return dseg.load(reladdr);
			case REL_ADDR:
//				// load rel-addr  callStackTop + ofst
//				CallStackFrame callStackTop = RTStack.callStack_TOP();
//				int bias = (callStackTop == null)? 0 : callStackTop.rtStackIndex;
//				Value value = RTStack.load(bias + ofst + idx);

				int frmx = RTStack.frameIndex();
				Value value = RTStack.load(frmx + ofst + idx);
				return value;
			case STACK_ADDR:
				value = RTStack.load(ofst + idx);
				return value;
			default: Util.IERR(""+kind); return null;
		}
	}

	@Override
	public Value add(final Value other) {
		if(other == null) return this;
		if(other instanceof IntegerValue ival) {
			return new ObjectAddress(this.kind, this.segID, this.ofst + ival.value, indexed);
		} else if(other instanceof ObjectAddress oaddr) {
			IO.println("ObjectAddress.add: this="+this);
			IO.println("ObjectAddress.add: other="+other);
			if(!oaddr.segID.equals(segID))
				Util.IERR("Illegal ObjectAddress'add operation: "+oaddr.segID+" != "+segID);
			return new ObjectAddress(this.kind, this.segID, this.ofst + oaddr.ofst, indexed);
		} else {
			Util.IERR(""+other.getClass().getSimpleName()+"  "+other);
			return null;
		}
	}

	@Override
	public Value sub(final Value other) {
		if(other == null) return this;
		if(other instanceof IntegerValue ival) {
			return new ObjectAddress(this.kind, this.segID, this.ofst - ival.value, indexed);
		} else if(other instanceof ObjectAddress oaddr) {
			if(!oaddr.segID.equals(segID)) {
				RTStack.dumpRTStack("ObjectAddress.sub: ");
				Util.IERR("Illegal ObjectAddress'sub operation: "+oaddr.segID+" != "+segID);
			}
			return IntegerValue.of(Type.T_SIZE, this.ofst - oaddr.ofst);
		} else {
			Util.IERR(""+other.getClass().getSimpleName()+"  "+other);
			return null;
		}
	}

	@Override
	public boolean compare(final int relation, final Value other) {
		String RHSegID = (other == null)? null : ((ObjectAddress)other).segID;
		int rhs = (other == null)? 0 : ((ObjectAddress)other).ofst;
		return Segment.compare(segID, ofst, relation, RHSegID, rhs);
	}
	
	/// Debug utility: dumpArea
	/// @param title the title of the dump printout
	/// @param lng the length of the dump
	public void dumpArea(final String title, final int lng) {
		if(this.segID != null) {
			segment().dump("\nRTAddress.dumpArea:", ofst, ofst+lng);
		} else {
			IO.println("\nRTAddress.dumpArea: BEGIN " + title + " +++++++++++++++++++++++++++++++++++++");
			for(int i=0;i<lng;i++) {
				ObjectAddress rtadr = this.addOffset(i);
				IO.println(""+rtadr+": " + load(ofst+i));
			}
			IO.println("RTAddress.dumpArea: ENDOF " + title + " +++++++++++++++++++++++++++++++++++++");
		}
	}
	
	@Override
	public String toString() {
		switch(kind) {
			case SEGMNT_ADDR: return segID + '[' + ofst + ']';
			case REMOTE_ADDR: return "REMOTE_ADDR[RTStackTop+" + ofst + ']';
			case REFER_ADDR:  return "REFER_ADDR[" + ofst + ']';
			case REL_ADDR:    return "REL_ADR[callStackTop+" + ofst + ']';
			case STACK_ADDR:  return "STACK_ADR[RTStack(" + ofst + ")]";
			default: return "UNKNOWN";
		}
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_C_OADDR);
		writeBody(oupt);
	}

	/// Writes the body of this ObjectAddress to the given output.
	/// @param oupt the output stream
	/// @throws IOException if IOException occur
	public void writeBody(final AttributeOutputStream oupt) throws IOException {
		oupt.writeByte(kind);
		oupt.writeString(segID);
		oupt.writeShort(ofst);
		oupt.writeBoolean(indexed);
	}
	
	/// Reads a ObjectAddress from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the ObjectAddress read
	/// @throws IOException if IOException occur
	public static ObjectAddress read(final AttributeInputStream inpt) throws IOException {
		int kind = inpt.readUnsignedByte();
		String segID = inpt.readString();
		int ofst = inpt.readShort();
		boolean indexed = inpt.readBoolean();
		return new ObjectAddress(kind, segID, ofst, indexed);
	}

}
