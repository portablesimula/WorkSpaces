/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env;

import bec.Option;
import bec.scode.Type;
import bec.util.Util;
import svm.RTStack;
import svm.instruction.SVM_CALL_SYS;
import svm.segment.Segment;
import svm.value.IntegerValue;

/// Information from/to the user of the environment
///
/// The routines correspond to the similarly named SIMULA Standard functions.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/SysInfo.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public abstract class SysInfo {

	/** Default Constructor */ public SysInfo() {} 

	/// Get integer valued information from the environment
	///
	///		Visible sysroutine("GINTIN") GINTIN;
	///		import range(0:127) index; export integer result  end;
	///
	/// 	Runtime Stack
	/// 	   ..., index →
	/// 	   ..., result
	///
	/// The parameter index is an integer specifying what information is requested.
	/// The result will be an integer whose interpretation gives the specified information.
	/// The result, which is pushed onto the Runtime stack, is given for each value of index as follows:
	///
	/// <pre>
	/// Index: Interpretation:
	///
	///   19   Should the symbolic debugger SIMOB be entered prior to the execution of the
	///        program, and at program termination? An answer greater than zero will give this effect.
	///
	///   24   How many work areas may be requested (see chapter 5)?
	///
	///   33   Result: 0 - no, this is not an interactive execution
	///                1 - yes, this is an interactive execution
	/// </pre>
	public static void getIntInfo() {
		SVM_CALL_SYS.ENTER("GINTIN: ", 1, 1); // exportSize, importSize
		int index = RTStack.popInt();
//		IO.println("SVM_SYSCALL.getIntinfo: "+index);
		int result=0;
		switch(index) {
			case 19: result = 0; break; // 19 Should the symbolic debugger SIMOB be entered prior to the execution of the
			                            //    program, and at program termination? An answer greater than zero will give this effect.
			case 24: result = 1; break; // 24 How many work areas may be requested (see chapter 5)?
			case 33: result = 0; break; // 33 Result: 0 - no, this is not an interactive execution
			                            //            1 - yes, this is an interactive execution
			case 99: Segment.lookup("DSEG_RT").dump("",0,100); break; // AD'HOC DUMP UTILITY
			default: Util.IERR("");
		}
		RTStack.push(IntegerValue.of(Type.T_INT, result));
		SVM_CALL_SYS.EXIT("GINTIN: ");
	}
	
	/// Get size valued information from the environment
	///
	///		Visible sysroutine ("SIZEIN") SIZEIN;
	///		import range(0:127) index; range(0:255) warea;
	///		export size result  end;
	///
	/// 	Runtime Stack
	/// 	   ..., index, warea →
	/// 	   ..., result
	///
	///		index: Specifies the information requested (se below).
	///		warea: Identifies the work area in question.
	///		result: The wanted SIZE, according to the value of Index:
	///
	/// <pre>
	/// Index Result
	///
	///   1   The minimum size of this work area.
	///   2   The extension/contraction step size.
	///   3   The minimum gap left in this work area after a garbage collection,
	///       if the area is the current work area.
	/// </pre>
	public static void getSizeInfo() {
		SVM_CALL_SYS.ENTER("SIZEIN: ", 1, 2); // exportSize, importSize
		int warea = RTStack.popInt();
		int index = RTStack.popInt();
		int result = 0;
		switch(index) {
			case 1: // The minimum size of this work area.
//				result = 1500000; break;
				result = 150000; break;
//				result = 1500; break;
			case 2: // The extension/contraction step size.
//				Util.IERR("The extension/contraction step size.");
				break;
			case 3: // The minimum gap left in this work area after a garbage collection if the area is the current work area.
//				Util.IERR("The minimum gap left in this work area after a garbage collection if the area is the current work area.");
				break;
			default: Util.IERR("");
		}
		if(Option.verbose) IO.println("SVM_SYSCALL.sizein: index=" + index + ", warea=" + warea + ", result=" +result);
		RTStack.push(IntegerValue.of(Type.T_SIZE, result));
		SVM_CALL_SYS.EXIT("SIZEIN: ");
	}

	/// Information to the environment
	///
	///	The routine give intinfo is defined to submit information from the front-end compiler or the runtime
	///	system to the environment. This information is gathered from the source input under
	///
	///		Visible sysroutine("GVIINF")  GVIINF;
	///		import range(0:127) index; integer inform  end;
	///
	/// 	Runtime Stack
	/// 	   ..., index, inform →
	/// 	   ...
	///
	///	The parameter index is an integer that specifies what information follows.
	/// Info will be an integer	carrying the following interpretation:
	///
	/// <pre>
	/// Index Interpretation
	///
	///   6   Garbage collection information.
	///          Info=0 signals the start of a garbage collection,
	///          Info=1 signals termination of g.c. (see 5.2).
	/// </pre>
	public static void giveIntInfo() {
		SVM_CALL_SYS.ENTER("GVIINF: ", 0, 2); // exportSize, importSize
		int inform = RTStack.popInt();
		int index = RTStack.popInt();
//		IO.println("SVM_SYSCALL.gviinf: index=" + index + ", inform=" + inform);
		switch(index) {
			case 6: // Garbage collection information. Info=0 signals the start of a garbage collection,
				    // Info=1 signals termination of g.c. (see 5.2).
				if(Option.execVerbose) {
					String more = (inform == 0)? " Begin" : " Endof";
					Util.println("SVM_SYSCALL.gviinf: index=" + index + ", inform=" + inform + more + " Garbage Collection");
				}
				break;
			default: Util.IERR(""+index);
		}
		SVM_CALL_SYS.EXIT("GVIINF: ");
	}
	
}
