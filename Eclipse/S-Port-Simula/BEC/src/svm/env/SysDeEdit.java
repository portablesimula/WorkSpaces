/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/

package svm.env;

import bec.scode.Type;
import svm.RTStack;
import svm.RTUtil;
import svm.instruction.SVM_CALL_SYS;
import svm.value.IntegerValue;
import svm.value.LongRealValue;

/// De-Editing Routines
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/SysDeEdit.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public abstract class SysDeEdit {

	/** Default Constructor */ public SysDeEdit() {} 

	/// Scan the input text for an integer item.
	/// <pre>
	/// INTEGER-ITEM = SIGN-PART DIGITS
	///    SIGN-PART = BLANKS [ SIGN ] BLANKS
	///       SIGN = + | -
	///    DIGITS = DIGIT { DIGIT }
	///       DIGIT = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
	/// </pre>
	/// @param T string to be decoded
	/// @return the INTEGER ITEM string
	private static String getIntegerItem(String T) {
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		char c = 0;
		LOOP1:while(pos < T.length()) { // SKIP BLANKS
			c = T.charAt(pos);
			if (c != ' ') break LOOP1;
			pos++;
		}
		c = T.charAt(pos);
		if (c == '+' || c == '-') {
			sb.append(c);
			pos++;
			LOOP2:while(pos < T.length()) { // SKIP BLANKS
				c = T.charAt(pos);
				if (c != ' ') break LOOP2;
				pos++;
			}
		}
		LOOP3:while(pos < T.length()) { // KEEP DIGITS
			c = T.charAt(pos);
			if (!Character.isDigit(c)) break LOOP3;
			sb.append(c);
			pos++;
		}
		RTUtil.set_ITEM_SIZE(pos);
		return (sb.toString());
	}

	/// Procedure getint.
	///
	///		Visible sysroutine("GETINT") GETINT;
	///		import infix (string) item; export integer res end;
	///
	/// 	Runtime Stack
	/// 	   ..., item'addr, item'nchr →
	/// 	   ..., res
	///
	/// The argument item String is popped of the Runtime stack.
	/// <br>An INTEGER ITEM is located in that String.
	/// <br>Finally: That ITEM is decoded and pushed onto the Runtime stack.
	/// <pre>
	/// INTEGER-ITEM = SIGN-PART DIGITS
	///    SIGN-PART = BLANKS [ SIGN ] BLANKS
	///       SIGN = + | -
	///    DIGITS = DIGIT { DIGIT }
	///       DIGIT = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
	/// </pre>
	/// 
	public static void getint() {
		SVM_CALL_SYS.ENTER("GETINT: ", 1, 3); // exportSize, importSize
		String arg = RTStack.popString();
		String item = getIntegerItem(arg);
		int res = Integer.parseInt(item);
		RTStack.push(IntegerValue.of(Type.T_INT, res));
		SVM_CALL_SYS.EXIT("GETINT: ");
	}


	/// Scan the input text for a real item.
	/// <pre>
	/// REAL-ITEM = DECIMAL-ITEM [ EXPONENT ] | SIGN-PART EXPONENT
	///    DECIMAL-ITEM = INTEGER-ITEM [ FRACTION ] | SIGN-PART FRACTION
	///       INTEGER-ITEM = SIGN-PART DIGITS
	///       FRACTION = DECIMAL-MARK DIGITS
	///       SIGN-PART = BLANKS [ SIGN ] BLANKS
	///    EXPONENT = LOWTEN-CHARACTER INTEGER-ITEM
	///          SIGN = + | -
	///          DIGITS = DIGIT { DIGIT }
	///          DIGIT = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
	///          LOWTEN-CHARACTER = & | ...
	///          DECIMAL-MARK = . | ,
	///          BLANKS = { BLANK | TAB }
	/// </pre>
	/// @param T string to be decoded
	/// @return the REAL ITEM string
	private static String getRealItem(String T) {
		StringBuilder sb = new StringBuilder();
		char c = 0;
		int pos = 0;
		LOOP1:while(pos < T.length()) { // SKIP BLANKS
			c = T.charAt(pos);
			if (c != ' ') break LOOP1;
			pos++;
		}
		if (c == '+' || c == '-') {
			sb.append(c);
			pos++;
			LOOP2:while(pos < T.length()) { // SKIP BLANKS
				c = T.charAt(pos);
				if (c != ' ') break LOOP2;
				pos++;
			}
		}
		int lastDigPos = pos;
		LOOP3:while(pos < T.length()) { // KEEP DIGITS
			c = T.charAt(pos);
			if (Character.isDigit(c)) ; // OK
			else if (c == RTUtil.CURRENTDECIMALMARK) c = '.'; // OK NOTE: THIS WAS WRONG IN PC-SIMULA
			else if (c == '+') ; // OK
			else if (c == '-') ; // OK
			else if (c == RTUtil.CURRENTLOWTEN) c = 'E'; // OK
			else { pos = lastDigPos; break LOOP3;}
			sb.append(c);
			lastDigPos = pos;
			pos++;
		}
		RTUtil.set_ITEM_SIZE(lastDigPos);
		return (sb.toString());
	}

	/// Procedure getreal.
	///
	///		Visible sysroutine("GTREAL") GTREAL;
	///		import infix (string) item; export long real res end;
	///
	/// 	Runtime Stack
	/// 	   ..., item'addr, item'nchr →
	/// 	   ..., res
	///
	/// The argument item String is popped of the Runtime stack.
	/// <br>An REAL ITEM is located in that String.
	/// <br>Finally: That ITEM is decoded and pushed onto the Runtime stack.
	/// <pre>
	/// REAL-ITEM = DECIMAL-ITEM [ EXPONENT ] | SIGN-PART EXPONENT
	///    DECIMAL-ITEM = INTEGER-ITEM [ FRACTION ] | SIGN-PART FRACTION
	///       INTEGER-ITEM = SIGN-PART DIGITS
	///       FRACTION = DECIMAL-MARK DIGITS
	///       SIGN-PART = BLANKS [ SIGN ] BLANKS
	///    EXPONENT = LOWTEN-CHARACTER INTEGER-ITEM
	///          SIGN = + | -
	///          DIGITS = DIGIT { DIGIT }
	///          DIGIT = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
	///          LOWTEN-CHARACTER = & | ...
	///          DECIMAL-MARK = . | ,
	///          BLANKS = { BLANK | TAB }
	/// </pre>
	public static void getreal() {
		SVM_CALL_SYS.ENTER("GTREAL: ", 1, 3); // exportSize, importSize
		String arg = RTStack.popString();
		String item = getRealItem(arg);
		double res = Double.parseDouble(item);
		RTStack.push(LongRealValue.of(res));
		SVM_CALL_SYS.EXIT("GTREAL: ");
	}

	/// Scan the input text for a fraction item.
	/// <pre>
	/// GROUPED-ITEM = SIGN-PART GROUPS [ DECIMAL-MARK GROUPS ]
	///              | SIGN-PART DECIMAL-MARK GROUPS
	/// 	SIGN-PART = BLANKS [ SIGN ] BLANKS
	/// 		SIGN = + | -
	/// 	GROUPS = DIGITS { BLANK DIGITS } DIGITS = DIGIT { DIGIT }
	/// 		DIGIT = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
	/// </pre>
	/// @param T string to be decoded
	/// @return the GROUPED ITEM string
	private static String getFracItem(String T) {
		StringBuilder sb = new StringBuilder();
		char c = 0;
		int pos = 0;
		LOOP1:while(pos < T.length()) { // SKIP BLANKS
			c = T.charAt(pos);
			if (c != ' ') break LOOP1;
			pos++;
		}
		if (c == '+' || c == '-') {
			sb.append(c);
			pos++;
			LOOP2:while(pos < T.length()) { // SKIP BLANKS
				c = T.charAt(pos);
				if (c != ' ') break LOOP2;
				pos++;
			}
		}
		int lastDigPos = pos;
		LOOP3:while(pos < T.length()) { // KEEP DIGITS
			c = T.charAt(pos);
			if (Character.isDigit(c)) {
				sb.append(c);
				lastDigPos = pos;
			} // OK
			else if (c == RTUtil.CURRENTDECIMALMARK) ; // OK NOTE: THIS WAS WRONG IN PC-SIMULA
			else if (c == ' ') ; // OK
			else break LOOP3;
			pos++;
		}
		RTUtil.set_ITEM_SIZE(lastDigPos);
		return (sb.toString());
	}

	/// Procedure getfrac.
	///
	///		Visible sysroutine("GTFRAC") GTFRAC;
	///		import infix (string) item; export integer res end;
	///
	/// 	Runtime Stack
	/// 	   ..., item'addr, item'nchr →
	/// 	   ..., res
	///
	/// The argument item String is popped of the Runtime stack.
	/// <br>A GROUPED ITEM is located in that String.
	/// <br>Finally: That ITEM is decoded and pushed onto the Runtime stack.
	///
	/// The digits of a GROUPED ITEM may be interspersed with BLANKS
	/// and a single DECIMAL MARK which are ignored.
	/// <pre>
	/// GROUPED-ITEM = SIGN-PART GROUPS [ DECIMAL-MARK GROUPS ]
	///              | SIGN-PART DECIMAL-MARK GROUPS
	/// 	SIGN-PART = BLANKS [ SIGN ] BLANKS
	/// 		SIGN = + | -
	/// 	GROUPS = DIGITS { BLANK DIGITS } DIGITS = DIGIT { DIGIT }
	/// 		DIGIT = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
	/// </pre>
	/// The procedure locates a GROUPED ITEM. The function value is
	/// equal to the resulting integer. The digits of a GROUPED ITEM
	/// may be interspersed with BLANKS and a single DECIMAL MARK which
	/// are ignored by the procedure.
	/// 
	public static void getfrac() {
		SVM_CALL_SYS.ENTER("GTFRAC: ", 1, 3); // exportSize, importSize
		String arg = RTStack.popString();
		String item = getFracItem(arg);
		int res = Integer.parseInt(item);
		RTStack.push(IntegerValue.of(Type.T_INT, res));
		SVM_CALL_SYS.EXIT("GTFRAC: ");
	}

}
