/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.ConstItem;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.Util;
import svm.instruction.SVM_LOADC;
import svm.value.BooleanValue;
import svm.value.DotAddress;
import svm.value.GeneralAddress;
import svm.value.IntegerValue;
import svm.value.LongRealValue;
import svm.value.ObjectAddress;
import svm.value.ProgramAddress;
import svm.value.RealValue;
import svm.value.RecordValue;
import svm.value.StringValue;
import svm.value.TextValue;
import svm.value.Value;

/// S-INSTRUCTION: PUSHC.
/// <pre>
/// stack_instruction ::= pushc value
/// 
///	 value
///		::= boolean_value | character_value
///		::= integer_value | size_value
///		::= real_value | longreal_value
///		::= attribute_address | object_address
///		::= general_address | program_address
///		::= routine_address | record_value
/// 
/// text_value      ::= text long_string
/// boolean_value   ::= true | false 
/// character_value ::= c-char byte
/// integer_value   ::= c-int integer_literal:string
/// real_value      ::= c-real real_literal:string 
/// longreal_value  ::= c-lreal real_literal:string
/// size_value      ::= c-size type | nosize
/// 
/// attribute_address
/// 		::= < c-dot attribute:tag >* c-aaddr attribute:tag
/// 		::= anone
/// 
/// object_address
/// 		::= c-oaddr global_or_const:tag
/// 		::= onone
/// 
/// general_address
/// 		::= < c-dot attr:tag >* c-gaddr global_or_const:tag
/// 		::= gnone
/// 
/// program_address ::= c-paddr label:tag | nowhere
/// routine_address ::= c-raddr body:tag | nobody
/// 
/// record_value
/// 		::= c-record structured_type
/// 			<attribute_value>+ endrecord
/// 
/// 
/// pushc constant:value
/// push( VAL, constant.TYPE, "value" );
/// </pre>
/// A descriptor of the given value is pushed onto the stack.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/PUSHC.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class PUSHC extends Instruction {
	
	/** Default Constructor */ public PUSHC() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Emit an SVM_LOADC instruction.
	public static void ofScode() {
		Type type = null;
		Value value = null;
		Scode.inputInstr();
		switch(Scode.curinstr) {
		    case Sinstr.S_C_INT:    type = Type.T_INT; value = IntegerValue.ofScode_INT(); break;
		    case Sinstr.S_C_REAL:   type = Type.T_REAL; value = RealValue.ofScode(); break;
		    case Sinstr.S_C_LREAL:  type = Type.T_LREAL; value = LongRealValue.ofScode(); break;
		    case Sinstr.S_C_CHAR:   type = Type.T_CHAR; value = IntegerValue.ofScode_CHAR(); break;
		    case Sinstr.S_NOSIZE:   type = Type.T_SIZE; value = null; break;
		    case Sinstr.S_C_SIZE:   type = Type.T_SIZE; value = IntegerValue.ofScode_SIZE(); break;
		    case Sinstr.S_TRUE:     type = Type.T_BOOL; value = BooleanValue.of(true); break;
		    case Sinstr.S_FALSE:    type = Type.T_BOOL; value = BooleanValue.of(false); break;
		    case Sinstr.S_ANONE:    type = Type.T_AADDR; value = null; break;
		    case Sinstr.S_C_AADDR:  type = Type.T_AADDR; value = IntegerValue.ofScode_AADDR(); break;
		    case Sinstr.S_NOWHERE:  type = Type.T_PADDR; value = null; break;
		    case Sinstr.S_C_PADDR:  type = Type.T_PADDR; value = ProgramAddress.ofScode(Type.T_PADDR); break;
			case Sinstr.S_NOBODY:   type = Type.T_RADDR; value = null; break;
		    case Sinstr.S_C_RADDR:  type = Type.T_RADDR; value = ProgramAddress.ofScode(Type.T_RADDR); break;
		    case Sinstr.S_ONONE:    type = Type.T_OADDR; value = null; break;
		    case Sinstr.S_C_OADDR:  type = Type.T_OADDR; value = ObjectAddress.ofScode(); break;
		    case Sinstr.S_GNONE:    type = Type.T_GADDR; value = null; break;
		    case Sinstr.S_C_GADDR:  type = Type.T_GADDR; value = GeneralAddress.ofScode(); break;
		    case Sinstr.S_C_DOT:	   value = DotAddress.ofScode(); type = value.type; break;
		    case Sinstr.S_C_RECORD: value = RecordValue.ofScode(); type = value.type; break;
		    case Sinstr.S_TEXT:   { type = Type.T_TEXT; TextValue txtval = TextValue.ofScode();
		    					   ObjectAddress addr = txtval.emitChars(Global.TSEG);
		    					   value = new StringValue(addr, txtval.textValue.length());
		    					   type = Type.T_STRING; break;
		    					 }
		    default: Util.IERR("NOT IMPLEMENTED: " + Sinstr.edInstr(Scode.curinstr));
		}
		CTStack.push(new ConstItem(type, value));
		Global.PSEG.emit(new SVM_LOADC(type, value));
	}

}
