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
import svm.segment.DataSegment;

/// LongRealValue.
/// 
///		longreal_value ::= c-lreal real_literal:string
///
///		A real literal is a string:
///
///			<sign>? <digit>* < . <digit>+ >? < & <sign>? <digit>+ >?
///
///		Note that a real literal may not contain spaces.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/LongRealValue.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class LongRealValue extends Value {

	/** Default Constructor */ public LongRealValue() {} 

	/// The double value
	public double value;
	
	/// Construct a new LongRealValue
	/// @param value the double value
	private LongRealValue(final double value) {
		this.type = Type.T_LREAL;
		this.value = value;
	}
	
	/// Create a new LongRealValue with the given parameter
	/// @param value the double value
	/// @return an LongRealValue or null
	public static LongRealValue of(final double value) {
		if(value != 0) return new LongRealValue(value);
		return null;
	}

	/// Scans the remaining S-Code belonging to this longreal_value.
	/// Then construct a new LongRealValue object.
	/// @return that LongRealValue object or null.
	public static LongRealValue ofScode() {
		String r = Scode.inString();
		if(r.startsWith("-")) {
			r = r.substring(1).trim();
			r = (r.startsWith("&"))? "-1" + r : "-" + r;
		}
		if(r.startsWith("&")) r = "1" + r;
		else if(r.startsWith("-&")) r = "-0" + r.substring(1);
		return LongRealValue.of(Double.valueOf(r.replace('&', 'E')));
	}
	
	@Override
	public void emit(final DataSegment dseg) {
		dseg.emit(this);
	}

	@Override
	public float toFloat() {
		return (float) value;
	}

	@Override
	public double toDouble() {
		return (double) value;
	}

	@Override
	public Value neg() {
		return LongRealValue.of(- value);
	}

	@Override
	public Value add(final Value other) {
		if(other == null) return this;
		double val2 = other.toDouble();
		double res = value + val2;
		if(res == 0) return null;
		return LongRealValue.of(res);
	}

	@Override
	public Value sub(final Value other) {
		double res = 0;
		if(other != null) {
			double val2 = other.toDouble();
			res = this.value - val2;
		} else res = this.value;
		if(res == 0) return null;
		return LongRealValue.of(res);
	}

	@Override
	public Value mult(final Value other) {
		if(other == null) return null;
		double val2 = other.toDouble();
		double res = value * val2;
		if(res == 0) return null;
		return LongRealValue.of(res);
	}

	@Override
	public Value div(final Value other) {
		double res = 0;
		if(other != null) {
			double val2 = other.toDouble();
			res = val2 / this.value;
		} else res = 0;
		if(res == 0) return null;
		return LongRealValue.of(res);
	}

	@Override
	public boolean compare(int relation, final Value other) {
		double LHS = this.value;
		double RHS = (other == null)? 0 : other.toDouble();
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
		return "C-LREAL " + value;
	}
	

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_C_LREAL);
		oupt.writeDouble(value);
	}

	/// Reads a BooleanValue from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the BooleanValue read
	/// @throws IOException if IOException occur
	public static LongRealValue read(final AttributeInputStream inpt) throws IOException {
		return new LongRealValue(inpt.readDouble());
	}


}
