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
import svm.segment.Segment;

/// GeneralAddress.
/// 
///		general_address	::= c-gaddr global_or_const:tag
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/GeneralAddress.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class GeneralAddress extends Value {
	
	/** Default Constructor */ public GeneralAddress() {} 

	/// Base Object address
	public ObjectAddress base;
	
	/// The offset
	public int ofst;
	
	/// Construct a new GeneralAddress with the given base and offset
	/// @param base the Base Object address
	/// @param ofst The offset
	public GeneralAddress(final ObjectAddress base, final int ofst) {
		this.type = Type.T_GADDR;
		this.base = base;
		this.ofst = ofst;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this GeneralAddress Value.
	/// Then construct a new GeneralAddress instance.
	/// @return that GeneralAddress instance.
	public static GeneralAddress ofScode() {
		Tag tag = Tag.ofScode();
		Descriptor descr = tag.getMeaning();
		if(descr == null) Util.IERR("IMPOSSIBLE: TESTING FAILED");
		if(descr instanceof Variable var) {
			return new GeneralAddress(var.address, 0);
		} else if(descr instanceof ConstDescr cns) {
			return new GeneralAddress(cns.getAddress(), 0);
		}
		Util.IERR("NOT IMPL: " + descr.getClass().getSimpleName());
		return null;
	}

	@Override
	public boolean compare(final int relation, final Value other) {
		String RHSegID = null; //(other == null)? null : ((GeneralAddress)other).segID;
		int rhs = 0;           //(other == null)? 0 : ((GeneralAddress)other).ofst;
		if(other != null) {
			GeneralAddress gaddr = (GeneralAddress) other;
			ObjectAddress base = gaddr.base;
			RHSegID = base.segID;
			rhs = base.ofst + gaddr.ofst;
		}
		return Segment.compare(this.base.segID, base.ofst + ofst, relation, RHSegID, rhs);
	}

	@Override
	public Value add(final Value other) {
		if(other == null) return this;
		if(other instanceof IntegerValue ival) {
			return new GeneralAddress(this.base, this.ofst + ival.value);
		} else {
			Util.IERR(""+other.getClass().getSimpleName()+"  "+other);
			return null;
		}
	}
	
	@Override
	public String toString() {
		return (base == null)? "GNONE" : base.toString() + '[' + ofst + ']';
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_C_GADDR);
		base.write(oupt);
		oupt.writeShort(ofst);
	}

	/// Reads a GeneralAddress from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the GeneralAddress read
	/// @throws IOException if IOException occur
	public static GeneralAddress read(final AttributeInputStream inpt) throws IOException {
		ObjectAddress base = (ObjectAddress) Value.read(inpt);
		int ofst = inpt.readShort();
		return new GeneralAddress(base, ofst);
	}

}
