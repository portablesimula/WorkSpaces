/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;

import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.segment.DataSegment;

/// Value.
/// 
///		value
///			::= boolean_value | character_value
///			::= integer_value | size_value
///			::= real_value | longreal_value
///			::= attribute_address | object_address
///			::= general_address | program_address
///			::= routine_address | record_value
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/Value.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class Value {

	/** Default Constructor */ public Value() {} 

	/// The type of the value
	public Type type;

	/// Utility print method.
	/// @param indent number of spaces leading the lines
	public void print(final String indent) {
		IO.println(this.getClass().getSimpleName() + ".print: " +  toString());
	}

	/// Add this value to the given DataSegment
	/// @param dseg the DataSegment
	public void emit(DataSegment dseg) {
		Util.IERR("Method 'emit' need a redefinition in " + this.getClass().getSimpleName());
	}

	/// Returns the float value of this Value object.
	/// @return the float value of this Value object.
	public float toFloat() {
		Util.IERR("Method 'toFloat' need a redefinition in " + this.getClass().getSimpleName());
		return 0;
	}

	/// Returns the double value of this Value object.
	/// @return the double value of this Value object.
	public double toDouble() {
		Util.IERR("Method 'toDouble' need a redefinition in " + this.getClass().getSimpleName());
		return 0;
	}

	/// Returns a copy of this Value object.
	/// @return a copy of this Value object.
	public Value copy() {
		Util.IERR("Method 'copy' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the negative value of this Value object.
	/// @return the negative value of this Value object.
	public Value neg() {
		Util.IERR("Method 'neg' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value + other value
	/// @param other the other value
	/// @return the result of: this value + other value
	public Value add(Value other) {
		Util.IERR("Method 'add' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value - other value
	/// @param other the other value
	/// @return the result of: this value - other value
	public Value sub(Value other) {
		Util.IERR("Method 'sub' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value * other value
	/// @param other the other value
	/// @return the result of: this value * other value
	public Value mult(Value other) {
		Util.IERR("Method 'mult' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value / other value
	/// @param other the other value
	/// @return the result of: this value / other value
	public Value div(Value other) {
		Util.IERR("Method 'div' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value rem other value
	/// @param other the other value
	/// @return the result of: this value rem other value
	public Value rem(Value other) {
		Util.IERR("Method 'rem' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value and other value
	/// @param other the other value
	/// @return the result of: this value and other value
	public Value and(Value other) {
		Util.IERR("Method 'and' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value or other value
	/// @param other the other value
	/// @return the result of: this value or other value
	public Value or(Value other) {
		Util.IERR("Method 'or' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value xor other value
	/// @param other the other value
	/// @return the result of: this value xor other value
	public Value xor(Value other) {
		Util.IERR("Method 'xor' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value imp other value
	/// @param other the other value
	/// @return the result of: this value imp other value
	public Value imp(Value other) {
		Util.IERR("Method 'imp' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Returns the result of: this value eqv other value
	/// @param other the other value
	/// @return the result of: this value eqv other value
	public Value eqv(Value other) {
		Util.IERR("Method 'eqv' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	/// Compare this Value with the given Value according to the given relation.
	/// @param relation one of: LT, LE, EQ, GE, GT, NE
	/// @param other the other Value
	/// @return true: if the relation holds
	public boolean compare(int relation, Value other) {
		Util.IERR("Method 'compare' need a redefinition in " + this.getClass().getSimpleName());
		return false;
	}

	/// Returns the result of: this value shiftInstr other value
	///	S_LSHIFTA: Signed Left Shift	<<	The left shift operator moves all bits by a given number of bits to the left.
	///	S_LSHIFTL: Unsigned Left Shift	<<	The left shift operator moves all bits by a given number of bits to the left.
	/// S_RSHIFTA: Signed Right Shift	>>	The right shift operator moves all bits by a given number of bits to the right.
	/// S_RSHIFTL: Unsigned Right Shift	>>>	It is the same as the signed right shift, but the vacant leftmost position is filled with 0 instead of the sign bit.
	/// @param shiftInstr the actual shift instruction
	/// @param other the other value
	/// @return the result of: this value shiftInstr other value
	public Value shift(int shiftInstr, Value other) {
		Util.IERR("Method 'shift' need a redefinition in " + this.getClass().getSimpleName());
		return null;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Writes this Value to the given output.
	/// @param oupt the output stream
	/// @throws IOException if IOException occur
	public void write(final AttributeOutputStream oupt) throws IOException {
		Util.IERR("Method 'write' needs a redefinition in "+this.getClass().getSimpleName());
	}

	/// Reads a Value from the given input.
	/// @param inpt the input stream
	/// @return the Value read
	/// @throws IOException if IOException occur
	public static Value read(final AttributeInputStream inpt) throws IOException {
		int kind = inpt.readUnsignedByte();
		return read(kind, inpt);
	}
	
	/// Reads a Value of the given kind from the given input.
	/// @param kind the value kind
	/// @param inpt the input stream
	/// @return the Value read
	/// @throws IOException if IOException occur
	public static Value read(final int kind, final AttributeInputStream inpt) throws IOException {
		// IO.println("Value.read: kind="+Sinstr.edInstr(kind));
		switch(kind) {
			case Sinstr.S_NULL:		return null;
			case Sinstr.S_TRUE:		return BooleanValue.of(true);
			case Sinstr.S_FALSE:	return BooleanValue.of(false);
			case Sinstr.S_C_INT, Sinstr.S_C_CHAR, Sinstr.S_C_SIZE, Sinstr.S_C_AADDR: return IntegerValue.read(inpt);
			case Sinstr.S_C_REAL:	return RealValue.read(inpt);
			case Sinstr.S_C_LREAL:	return LongRealValue.read(inpt);
			case Sinstr.S_TEXT:		return TextValue.read(inpt);
			case Sinstr.S_STRING:	return StringValue.read(inpt);
			case Sinstr.S_C_RECORD:	return RecordValue.read(inpt);
			case Sinstr.S_C_OADDR:	return ObjectAddress.read(inpt);
			case Sinstr.S_C_GADDR:	return GeneralAddress.read(inpt);
			case Sinstr.S_C_PADDR, Sinstr.S_C_RADDR: return ProgramAddress.read(inpt);
//			case Sinstr.S_C_DOT:	return DotAddress.read(inpt);
			default: Util.IERR("MISSING: " + Sinstr.edInstr(kind)); return null;
		}
	}


}
