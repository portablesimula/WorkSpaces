/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;

import bec.Option;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.segment.DataSegment;

/// BooleanValue.
/// 
///		boolean_value ::= true | false
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/BooleanValue.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class BooleanValue extends Value {
	
	/** Default Constructor */ public BooleanValue() {} 

	/// The value
	public boolean value;

	/// Construct a new BooleanValue
	/// @param value true or false
	private BooleanValue(final boolean value) {
		this.type = Type.T_BOOL;
		this.value = value;
	}
	
	/// Create an Object to represent a boolean value.
	/// true: new BooleanValue, otherwise null
	/// @param value true or false
	/// @return the newly created BooleanValue
	public static BooleanValue of(final boolean value) {
		if(value) return new BooleanValue(true);
		return null;
	}
	
	@Override
	public void emit(final DataSegment dseg) {
		dseg.emit(this);
	}
	
	@Override
	public Value and(final Value other) {
		if(other == null) return BooleanValue.of(value == false);
//		if(other == null) return this;
		BooleanValue val2 = (BooleanValue) other;
		boolean res = value & val2.value;
		if(!res) return null;
		return BooleanValue.of(res);
	}

	@Override
	public BooleanValue or(final Value other) {
		if(other == null) return this;
		BooleanValue val2 = (BooleanValue) other;
		boolean res = value | val2.value;
		if(!res) return null;
		return BooleanValue.of(res);
	}

	@Override
	public BooleanValue xor(final Value other) {
		if(other == null) return this;
		BooleanValue val2 = (BooleanValue) other;
		boolean res = value ^ val2.value;
		if(!res) return null;
		return BooleanValue.of(res);
	}

	@Override
	public BooleanValue imp(final Value other) {
		// true imp false  ==> false  otherwise  true
		boolean res = true;
		if(this.value) {
			if(other == null || !((BooleanValue)other).value) res = false;
		}
		if(!res) return null;
		return BooleanValue.of(res);
	}

	@Override
	public BooleanValue eqv(final Value other) {
		if(other == null) return this;
		BooleanValue val2 = (BooleanValue) other;
		boolean res = value == val2.value;
		if(!res) return null;
		return BooleanValue.of(res);
	}

	@Override
	public boolean compare(final int relation, final Value other) {
		boolean LHS = this.value;
		boolean RHS = (other == null)? false : ((BooleanValue)other).value;
		boolean res = false;
		switch(relation) {
			case Sinstr.S_EQ: res = LHS == RHS; break;
			case Sinstr.S_NE: res = LHS != RHS; break;
			default: Util.IERR("Undefined relation");
		}
		return res;
	}

	@Override
	public String toString() {
		return "" + value;
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte((value)?Sinstr.S_TRUE:Sinstr.S_FALSE);
	}

	/// Reads a BooleanValue from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the BooleanValue read
	/// @throws IOException if IOException occur
	public static BooleanValue read(final AttributeInputStream inpt) throws IOException {
		return new BooleanValue(inpt.readUnsignedByte() == Sinstr.S_TRUE);
	}


}
