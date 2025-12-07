/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;

import bec.Option;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.segment.DataSegment;

/// RealValue.
/// 
///		real_value ::= c-real real_literal:string
///
///		A real literal is a string:
///
///			<sign>? <digit>* < . <digit>+ >? < & <sign>? <digit>+ >?
///
///		Note that a real literal may not contain spaces.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/RealValue.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class RealValue extends Value {

	/** Default Constructor */ public RealValue() {} 

	/// The float value
	public float value;
	
	/// Construct a new LongRealValue
	/// @param value the float value
	private RealValue(final float value) {
		this.type = Type.T_REAL;
		this.value = value;
	}
	
	/// Create a new RealValue with the given parameter
	/// @param value the double value
	/// @return an RealValue or null
	public static RealValue of(final float value) {
		if(value != 0) return new RealValue(value);
		return null;
	}

	/// Scans the remaining S-Code belonging to this real_value.
	/// Then construct a new RealValue object.
	/// @return that RealValue object or null.
	public static RealValue ofScode() {
		String r = Scode.inString();
		if(r.startsWith("-")) {
			r = r.substring(1).trim();
			r = (r.startsWith("&"))? "-1" + r : "-" + r;
		}
		if(r.startsWith("&")) r = "1" + r;
		else if(r.startsWith("-&")) r = "-0" + r.substring(1);
		return RealValue.of(Float.valueOf(r.replace('&', 'E')));
	}
	
	@Override
	public void emit(final DataSegment dseg) {
		dseg.emit(this);
	}

	@Override
	public Value neg() {
		return RealValue.of(- value);
	}

	@Override
	public float toFloat() {
		return value;
	}

	@Override
	public double toDouble() {
		return (double) value;
	}

	@Override
	public Value add(final Value other) {
		if(other == null) return this;
		float val2 = other.toFloat();
		float res = value + val2;
		if(res == 0) return null;
		return RealValue.of(res);
	}

	@Override
	public Value sub(final Value other) {
		float res = 0;
		if(other != null) {
			float val2 = other.toFloat();
			res = this.value - val2;
		} else res = this.value;
		if(res == 0) return null;
		return RealValue.of(res);
	}

	@Override
	public Value mult(final Value other) {
		if(other == null) return null;
		float val2 = other.toFloat();
		float res = value * val2;
		if(res == 0) return null;
		return RealValue.of(res);
	}

	@Override
	public Value div(final Value other) {
		float res = 0;
		if(other != null) {
			float val2 = other.toFloat();
			res = val2 / this.value;
		} else res = 0;
		if(res == 0) return null;
		return RealValue.of(res);
	}

	@Override
	public boolean compare(final int relation, final Value other) {
		double LHS = this.value;
		double RHS = 0;
		if(other == null); // Nothing
		else if(other instanceof RealValue rval) RHS = rval.value;
		else if(other instanceof LongRealValue lrval) RHS = lrval.value;
		else Util.IERR("");
		boolean res = false;
		switch(relation) {
			case Sinstr.S_LT: res = LHS <  RHS; break;
			case Sinstr.S_LE: res = LHS <= RHS; break;
			case Sinstr.S_EQ: res = LHS == RHS; break;
			case Sinstr.S_GE: res = LHS >= RHS; break;
			case Sinstr.S_GT: res = LHS >  RHS; break;
			case Sinstr.S_NE: res = LHS != RHS; break;
		}
		return res;
	}

	@Override
	public String toString() {
		return "C-REAL " + value;
	}
	

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_C_REAL);
		oupt.writeFloat(value);
	}

	/// Reads a BooleanValue from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the BooleanValue read
	/// @throws IOException if IOException occur
	public static RealValue read(final AttributeInputStream inpt) throws IOException {
		return new RealValue(inpt.readFloat());
	}


}
