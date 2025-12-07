/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;

import bec.Option;
import bec.descriptor.Attribute;
import bec.scode.Relation;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.segment.DataSegment;

/// IntegerValue.
/// 
///		integer_value ::= c-int integer_literal:string
///
///		character_value ::= c-char byte
/// 
///		size_value
/// 		::= c-size type
/// 		::= NOSIZE
/// 
///		attribute_address	::= c-aaddr attribute:tag
///
///
///		An integer literal is a string:
///
///			< <radix> R>? <sign>? <digit>+
///
///		where digit is one of the (ISO coded) decimal digits, and sign may be + or -. The letter R, if included,
///		signals that the integer is specified with a radix preceding R. The only legal radices are 2, 4, 8 and 16.
///		If the radix is 16, <digit> may also be one of the (ISO-coded) letters A-F, with the obvious meaning.
///
///		Note that an integer literal may not contain spaces.
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/IntegerValue.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class IntegerValue extends Value {
	
	/** Default Constructor */ public IntegerValue() {} 

	/// The int value
	public int value;
	
	/// Construct a new IntegerValue
	/// @param type the s-type: INT, CHAR, SIZE, AADDR
	/// @param value the int value
	private IntegerValue(final Type type, final int value) {
		this.type = type;
		this.value = value;
	}
	
	/// Create a new IntegerValue with the given parameters
	/// @param type the s-type: INT, CHAR, SIZE, AADDR
	/// @param value the int value
	/// @return an IntegerValue or null
	public static IntegerValue of(final Type type, final int value) {
		if(value != 0) return new IntegerValue(type, value);
		return null;
	}

	/// Scans the remaining S-Code belonging to this integer_value.
	/// Then construct a new IntegerValue with type INT.
	/// @return that IntegerValue instance.
	public static IntegerValue ofScode_INT() {
		return IntegerValue.of(Type.T_INT, Integer.valueOf(Scode.inString()));
	}
	
	/// Scans the remaining S-Code belonging to this character_value.
	/// Then construct a new IntegerValue with type CHAR.
	/// @return that IntegerValue instance.
	public static IntegerValue ofScode_CHAR() {
		return IntegerValue.of(Type.T_CHAR, Scode.inByte());
	}
	
	/// Scans the remaining S-Code belonging to this size_value.
	/// Then construct a new IntegerValue with type SIZE.
	/// @return that IntegerValue instance.
	public static IntegerValue ofScode_SIZE() {
		Type type = Type.ofScode();
		return IntegerValue.of(Type.T_SIZE, type.size());
	}

	/// Scans the remaining S-Code belonging to this attribute_address.
	/// Then construct a new IntegerValue with type AADDR.
	/// @return that IntegerValue instance.
	public static IntegerValue ofScode_AADDR() {
		Tag tag = Tag.ofScode();
		Attribute var = (Attribute) tag.getMeaning();
		if(var == null) Util.IERR("IMPOSSIBLE: TESTING FAILED");
		return IntegerValue.of(Type.T_AADDR, var.rela);
	}
	
	/// Returns the int value of the given IntegerValue
	/// @param val the IntegerValue
	/// @return the int value of the given IntegerValue
	public static int intValue(final IntegerValue val) {
		if(val == null) return 0;
		return val.value;
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
		return IntegerValue.of(this.type,- value);
	}

	@Override
	public Value add(final Value other) {
		if(other == null) return this;
		if(other instanceof GeneralAddress gaddr) {
			return gaddr.add(this);
		} else {
			IntegerValue val2 = (IntegerValue) other;
			int res = value + val2.value;
			if(res == 0) return null;
			return IntegerValue.of(this.type, res);
		}
	}

	@Override
	public Value sub(final Value other) {
		int res = 0;
		if(other != null) {
			IntegerValue val2 = (IntegerValue) other;
			res = this.value - val2.value;
		} else res = this.value;
		if(res == 0) return null;
		return IntegerValue.of(this.type, res);
	}

	@Override
	public Value mult(final Value other) {
		if(other == null) return null;
		IntegerValue val2 = (IntegerValue) other;
		int res = value * val2.value;
		if(res == 0) return null;
		return IntegerValue.of(this.type, res);
	}

	@Override
	public Value div(final Value other) {
		int res = 0;
		if(other != null) {
			IntegerValue val2 = (IntegerValue) other;
			res = val2.value / this.value;
		} else res = 0;
		if(res == 0) return null;
		return IntegerValue.of(this.type, res);
	}

	@Override
	public Value rem(final Value other) {
		int res = 0;
		if(other != null) {
			IntegerValue val2 = (IntegerValue) other;
			res = val2.value % this.value;
		} else res = 0;
		if(res == 0) return null;
		return IntegerValue.of(this.type, res);
	}

	@Override
	public Value and(final Value other) {
		int val2 = (other == null)? 0 : ((IntegerValue) other).value;
		return IntegerValue.of(this.type, value & val2);
	}

	@Override
	public Value or(final Value other) {
		int val2 = (other == null)? 0 : ((IntegerValue) other).value;
		return IntegerValue.of(this.type, value | val2);
	}

	@Override
	public Value xor(final Value other) {
		int val2 = (other == null)? 0 : ((IntegerValue) other).value;
		return IntegerValue.of(this.type, value ^ val2);
	}

	@Override
	public boolean compare(final int relation, final Value other) {
		int LHS = this.value;
		int RHS = (other == null)? 0 : ((IntegerValue)other).value;
		return Relation.compare(LHS, relation, RHS);
	}

	@Override
	public Value shift(final int instr, final Value other) {
		int LHS = this.value;
		int RHS = (other == null)? 0 : ((IntegerValue)other).value;
		int res = 0;
		switch(instr) {
		case Sinstr.S_LSHIFTA,
			 Sinstr.S_LSHIFTL: res = LHS << RHS; break;
		case Sinstr.S_RSHIFTA: res = LHS >> RHS; break;
		case Sinstr.S_RSHIFTL: res = LHS >>> RHS; break;
		default: Util.IERR("");
		}
		return IntegerValue.of(Type.T_INT, res);
	}

	@Override
	public String toString() {
		if(type == null) return ""+value;
		switch(type.tag) {
			case Tag.TAG_INT:   return "INT:"   + value;
			case Tag.TAG_CHAR:  return "CHAR:"  + (char)value;
			case Tag.TAG_SIZE:  return "SIZE:"  + value;
			case Tag.TAG_AADDR: return "FIELD:" + value;
			default: return "C-" +type + " " + value; 
		}
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_C_INT);
		type.write(oupt);
		oupt.writeInt(value);
	}

	/// Reads a IntegerValue from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the IntegerValue read
	/// @throws IOException if IOException occur
	public static IntegerValue read(final AttributeInputStream inpt) throws IOException {
		Type type = Type.read(inpt);
		int value = inpt.readInt();
		return new IntegerValue(type, value);
	}


}
