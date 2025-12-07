/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.value;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;

import simuletta.type.Type;

import static simuletta.compiler.common.S_Instructions.*;

/**
 * SimpleValue.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		SimpleValue ::=  IntegerValue  |  RealValue  |  LongRealValue  |  CharacterValue  |  TextValue
 * 
 *  S-Code:
 *  
 * 		SimpleValue ::=  IntegerValue  |  RealValue  |  LongRealValue  |  CharacterValue  |  TextValue
 * 
 * 			IntegerValue   ::=  C_INT   value
 * 			RealValue      ::=  C_REAL  value
 * 			LongRealValue  ::=  C_LRAL  value
 * 			CharacterValue ::=  C_CHAR  value
 * 			TextValue      ::=  C_TEXT  value
 * 			
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class SimpleValue extends Value {
	public final int instr;
	public final String value;
	
	public SimpleValue(int instr, String value) {
		this.instr=instr;
		this.value=value;
	}
	
	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
	public Type doOutConst() {
		enterLine();
			switch(this.instr) {
				case S_C_INT:   sCode.outinst(S_C_INT);   sCode.outstring(value);        sCode.outcode(); return(Type.Integer);
				case S_C_REAL:  sCode.outinst(S_C_REAL);  sCode.outstring(value);        sCode.outcode(); return(Type.Real);
				case S_C_LREAL: sCode.outinst(S_C_LREAL); sCode.outstring(value);        sCode.outcode(); return(Type.LongReal);
				case S_C_CHAR:  sCode.outinst(S_C_CHAR);  sCode.outbyt(value.charAt(0)); sCode.outcode(); return(Type.Character);
				case S_TEXT:    sCode.outinst(S_TEXT);    sCode.outlongstring(value);    sCode.outcode(); return(Type.StringType);
				default: IERR();
			}
			sCode.outcode();
		exitLine();
		return(null);
	}	
	
	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append(edSymbol(instr)).append(':').append(value);
		return(s.toString());
	}


}
