/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.util;

import bec.Global;
import bec.Option;
import bec.scode.Scode;

/// Utility Methods.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/util/Util.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class Util {

	/** Default Constructor */ public Util() {} 

	/// S-Code input Trace
	/// @param id trace ident
	/// @param msg trace message
	public static void ITRC(final String id, final String msg) {
		if(Option.SCODE_INPUT_TRACE) {
			Scode.initTraceBuff("Line " + Scode.curline + "  " + id + ": " + msg);
		}
	}
	
	/// Print on both IO.println and Global.console
	/// @param s the String to print
	public static void println(final String s) {
		if(Global.console != null)
			Global.console.write(s + '\n');
		IO.println(s);
	}

	/// Print WARNING Message
	/// @param msg the message
	public static void WARNING(final String msg) {
		if(Option.SCODE_INPUT_TRACE) {
			Scode.flushTraceBuff();
		}
		IO.println("WARNING: " + msg);
	}

	/// Print ERROR Message
	/// @param msg the message
	public static void ERROR(final String msg) {
		if(Option.SCODE_INPUT_TRACE) {
			Scode.flushTraceBuff();
		}
		IO.println("ERROR: " + msg);
	}

	/// Print Internal Error Message and exit
	/// @param msg the message
	public static void IERR(final String msg) {
		ERROR("Internal error: " + msg);
		Thread.dumpStack();
		System.exit(0);
	}

	/// Utility method: TRACE_OUTPUT
	/// @param msg the message to print
	///
	public static void TRACE_OUTPUT(final String msg) {
		if (Option.ATTR_OUTPUT_TRACE)
			IO.println("ATTR OUTPUT: " + msg);
	}

	/// Utility method: TRACE_INPUT
	/// @param msg the message to print
	///
	public static void TRACE_INPUT(final String msg) {
		if (Option.ATTR_INPUT_TRACE)
			IO.println("ATTR INPUT: " + msg);
	}
	
}
