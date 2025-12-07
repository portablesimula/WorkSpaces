/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.util.Vector;

import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.Util;
import svm.segment.DataSegment;

/// RepetitionValue.
/// 
///		record_value
///		 repetition_value
/// 		::= <boolean_value>+
/// 		::= <character_value>+ | text_value
/// 		::= <integer_value>+ | <size_value>+
/// 		::= <real_value>+ | <longreal_value>+
/// 		::= <attribute_address>+ | <object_address>+
/// 		::= <general_address>+ | <program_address>+
/// 		::= <routine_address>+ | <record_value>+
/// 
///		text_value      ::= text long_string
///		boolean_value   ::= true | false 
///		character_value ::= c-char byte
///		integer_value   ::= c-int integer_literal:string
///		real_value      ::= c-real real_literal:string 
///		longreal_value  ::= c-lreal real_literal:string
///		size_value      ::= c-size type | nosize
/// 
///		attribute_address
/// 		::= < c-dot attribute:tag >* c-aaddr attribute:tag
/// 		::= anone
///	 
///		object_address
/// 		::= c-oaddr global_or_const:tag
/// 		::= onone
/// 
///		general_address
/// 		::= < c-dot attr:tag >* c-gaddr global_or_const:tag
/// 		::= gnone
/// 
///		program_address ::= c-paddr label:tag | nowhere
///		routine_address ::= c-raddr body:tag | nobody
/// 
///		record_value
/// 		::= c-record structured_type
/// 			<attribute_value>+ endrecord
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/RepetitionValue.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class RepetitionValue extends Value {
	
	/** Default Constructor */ public RepetitionValue() {} 

	/// The values
	public Vector<Value> values;
	
	/// Construct a new RepetitionValue with the given parameters.
	/// @param type the value type
	/// @param values the values
	private RepetitionValue(final Type type, final Vector<Value> values) {
		this.type = type;
		this.values = values;
	}

	/// Create a new RepetitionValue of the given value.
	/// @param value the value
	/// @return a new RepetitionValue with the value
	public static RepetitionValue ofValue(final Value value) {
		Vector<Value> values = new Vector<Value>();
		values.add(value);
		return new RepetitionValue(value.type, values);
	}
	
	/// Scans the remaining S-Code belonging to this RepetitionValue.
	/// Then construct a new RepetitionValue instance.
	/// @return that RepetitionValue instance.
	public static RepetitionValue ofScode() {
		Type type = null;
		Value value = null;
		Vector<Value> values = new Vector<Value>();
		LOOP:while(true) {
			switch(Scode.nextByte()) {
				case Sinstr.S_TEXT:     Scode.inputInstr(); type = Type.T_TEXT;  values.add(TextValue.ofScode()); break; // LOOP;
				case Sinstr.S_C_INT:    Scode.inputInstr(); type = Type.T_INT;   values.add(IntegerValue.ofScode_INT()); break;
				case Sinstr.S_C_CHAR:   Scode.inputInstr(); type = Type.T_CHAR;  values.add(IntegerValue.ofScode_CHAR()); break;
				case Sinstr.S_C_SIZE:   Scode.inputInstr(); type = Type.T_SIZE;  values.add(IntegerValue.ofScode_SIZE()); break;
				case Sinstr.S_C_REAL:   Scode.inputInstr(); type = Type.T_REAL;  values.add(RealValue.ofScode()); break;
				case Sinstr.S_C_LREAL:  Scode.inputInstr(); type = Type.T_LREAL; values.add(LongRealValue.ofScode()); break;
				case Sinstr.S_TRUE:     Scode.inputInstr(); type = Type.T_BOOL;  values.add(BooleanValue.of(true)); break;
				case Sinstr.S_FALSE:    Scode.inputInstr(); type = Type.T_BOOL;  values.add(BooleanValue.of(false)); break;
				case Sinstr.S_NOSIZE:   Scode.inputInstr(); type = Type.T_SIZE;  values.add(null); break;
				case Sinstr.S_ANONE:    Scode.inputInstr(); type = Type.T_AADDR; values.add(null); break;
				case Sinstr.S_NOWHERE:  Scode.inputInstr(); type = Type.T_PADDR; values.add(null); break;
				case Sinstr.S_NOBODY:   Scode.inputInstr(); type = Type.T_RADDR; values.add(null); break;
				case Sinstr.S_ONONE:    Scode.inputInstr(); type = Type.T_OADDR; values.add(null); break;
				case Sinstr.S_GNONE:    Scode.inputInstr(); type = Type.T_GADDR; values.add(null); values.add(null); break;
				case Sinstr.S_C_AADDR:  Scode.inputInstr(); type = Type.T_AADDR; values.add(IntegerValue.ofScode_AADDR()); break;
				case Sinstr.S_C_PADDR:  Scode.inputInstr(); type = Type.T_PADDR; values.add(ProgramAddress.ofScode(Type.T_PADDR)); break;
				case Sinstr.S_C_RADDR:  Scode.inputInstr(); type = Type.T_RADDR; values.add(ProgramAddress.ofScode(Type.T_RADDR)); break;
				case Sinstr.S_C_OADDR:  Scode.inputInstr(); type = Type.T_OADDR; values.add(ObjectAddress.ofScode()); break;
				case Sinstr.S_C_RECORD: Scode.inputInstr(); value = RecordValue.ofScode(); values.add(value); type = value.type; break;
				case Sinstr.S_C_DOT:
					Scode.inputInstr();
					Value dotAddr = DotAddress.ofScode();
					if(dotAddr instanceof GeneralAddress gaddr) {
						type = Type.T_GADDR;
						values.add(gaddr.base);
						values.add(IntegerValue.of(Type.T_INT, gaddr.ofst));						
					} else if(dotAddr instanceof ObjectAddress oaddr) {
						type = Type.T_OADDR;
						values.add(oaddr);
					} else if(dotAddr instanceof IntegerValue aaddr) {
						type = Type.T_AADDR;
						values.add(aaddr);
					} else Util.IERR(""+dotAddr.getClass().getSimpleName());
					break;
				case Sinstr.S_C_GADDR:
					type = Type.T_GADDR;
					Scode.inputInstr();
					GeneralAddress gaddr = GeneralAddress.ofScode();
					values.add(gaddr.base);
					values.add(IntegerValue.of(Type.T_INT, gaddr.ofst));
					break;
				default:
//					IO.println("RepetitionValue.ofScode: TERMINATED BY "+Sinstr.edInstr(Scode.nextByte()));
					break LOOP;
			}
		}
		return new RepetitionValue(type, values);
	}
		
	@Override
	public void emit(final DataSegment dseg) {
		for(Value value:values) {
			if(value == null)
				 dseg.emit(null);
			else value.emit(dseg);
		}
	}
	
	@Override
	public void print(final String indent) {
		for(Value value:values) value.print(indent);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Value value:values) {
			sb.append(value).append(" ");
		}
		return sb.toString();
	}

}
