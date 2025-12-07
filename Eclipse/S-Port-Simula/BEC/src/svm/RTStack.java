/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm;

import java.util.Stack;
import java.util.Vector;

import bec.Global;
import bec.Option;
import bec.util.Util;
import svm.value.GeneralAddress;
import svm.value.IntegerValue;
import svm.value.LongRealValue;
import svm.value.ObjectAddress;
import svm.value.Value;

/// Runtime Stack.
/// <pre>
///		STACK -----------.  OUTMOST STACK
///		...              |
///		STACK -----------'
///	FRAME:
///		EXPORT ?  -------.  FRAME HEAD
///		IMPORT           |
///		...              |
///		IMPORT           |
///		RETURN ADDRESS   |
///		LOCAL            |
///		...              |
///		LOCAL -----------'
///		STACK -----------.  LOCAL STACK
///		...              |
///		STACK -----------'
/// MORE FRAMES ?
/// </pre>
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/RTStack.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public abstract class RTStack {
	
	/** Default Constructor */ public RTStack() {} 

	/// The Runtime Stack
	private static Stack<Value> stack = new Stack<Value>();
	
	/// The PRECALL Stack
	public static Stack<CallStackFrame> precallStack = new Stack<CallStackFrame>();
	
	/// The CALL Stack
	public static Stack<CallStackFrame> callStack = new Stack<CallStackFrame>();
	
	/// Check RTStack empty
	public static void checkStackEmpty() {
		if(curSize() != 0) {
			CallStackFrame callStackTop = callStack_TOP();
			int frameHeadSize = (callStackTop == null)? 0 : callStackTop.headSize();
			int idx = (callStackTop == null)? 0 : callStackTop.rtStackIndex;
			dumpRTStack("Check RTStack Empty - FAILED: ");
			if(callStackTop != null) callStackTop.dump("Check RTStack Empty - FAILED: ");
//			printCallStack("Check RTStack Empty - FAILED: ");
			Util.IERR("Check RTStack Empty - FAILED: size="+size()+"  rtStackIndex=" + idx + "  frameHeadSize=" + frameHeadSize );
		}
	}
	
	/// Returns the CALL Stack TOP
	/// @return the CALL Stack TOP
	public static CallStackFrame callStack_TOP() {
		if(RTStack.callStack.empty()) return null;
		return RTStack.callStack.peek();
	}
	
	
//	private static void printCallStack(String title) {
//		IO.println("CallStack["+callStack.size()+"]: " +title);
//		IO.println("     at "+Global.PSC);
//		for(int i=callStack.size()-1;i>=0;i--) {
//			CallStackFrame frame = callStack.get(i);
//			ProgramAddress curAddr =  frame.curAddr;
//			IO.println("     at "+curAddr);	
//			frame.print();
//		}
//	}
	
	/// Print the entire Call Stack
	/// @param title the title of the printout
	public static void printCallTrace(String title) {
		IO.println("CallStack["+callStack.size()+"]: " +title);
		int n = callStack.size()-1;
		for(int i=n;i>=0;i--) {
			CallStackFrame frame = callStack.get(i);
			if(i != n)
				IO.println("     called from " + frame.ident);
			if(Option.CALL_TRACE_LEVEL > 1)
				frame.print();
		}
	}
	
	/// Returns Stack size within the current Frame
	/// @return Stack size within the current Frame
	public static int curSize() {
		CallStackFrame callStackTop = callStack_TOP();
		if(callStackTop == null) return size();
		int frameHeadSize = callStackTop.headSize();
		int idx = callStackTop.rtStackIndex;
		return size() - (idx + frameHeadSize);
	}
	
	/// Returns The total Stack size
	/// @return The total Stack size
	public static int size() {
		return stack.size();
	}
	
	/// Returns the value at the given index
	/// @param index the index
	/// @return the value at the given index
	public static Value load(int index) {
		try { return stack.get(index); } catch(Exception e) { return null; }
	}
	
	/// Store a value at the given index
	/// @param index the index
	/// @param value the value
	public static void store(int index, Value value) {
//		if(index == GUARD) {
//			Global.PSC.segment().dump("STACK GUARD TRAPPED: " + index, 0, 10);
//			RTStack.dumpRTStack("STACK GUARD TRAPPED: " + index);
//			Util.IERR("Attempt to store " + value + " into guarded location RTStack[" + index + ']');
//		}
		stack.set(index, value);
	}
	
	/// Duplicate the 'n' top values on the stack
	/// @param n number of values to be duplicated
	public static void dup(int n) {
		Vector<Value> values = new Vector<Value>();
		int idx = stack.size()-1;
		for(int i=0;i<n;i++) {
			Value val = stack.get(idx-i);
			values.add(val);
		}
		for(int i=n-1;i>=0;i--) {
			stack.push(values.get(i));
		}		
	}
	
//	/// Debug Utility
//	private static int GUARD = -1;
//
//	/// Debug Utility
//	public static void guard(int index) {
//		RTStack.dumpRTStack("RTStack.guard: "+index);
//		GUARD = index;
//	}

	/// Push a value onto the stack
	/// @param value the value
	public static void push(Value value) {
		stack.push(value);
	}

	/// Add EXPORT slots to the Current Stack Frame.
	/// <pre>
	/// Current Stack;
	///
	///      IMPORT	idx = last - (nSlotStacked - 1)
	///      ...    nSlotStacked
	/// TOP: IMPORT	idx = last
	/// </pre>
	/// ===>
	/// <pre>
	///      EXPORT
	///	     ...    nExportSlots
	///      EXPORT
	///      IMPORT
	///      ...    nSlotStacked
	/// TOP: IMPORT
	/// </pre>
	/// @param nSlotStacked number of slots stacked
	/// @param nExportSlots number of Exports slots
	public static void addExport(int nSlotStacked, int nExportSlots) {
		int idx = RTStack.size() - nSlotStacked;
		for(int i=0; i<nExportSlots;i++)
			stack.add(idx, null);
	}
	
	/// Pop the top off this stack and returns that value.
	/// @return the value popped off the stack
	public static Value pop() {
		Value itm = stack.pop();
		return itm;
	}
	
	/// Returns the value at the top of this stack 
	/// @return the value at the top of this stack 
	public static Value peek() {
		return stack.peek();
	}

	/// Push a set of values onto the stack
	/// @param values the values
	public static void pushx(Vector<Value> values) {
		for(int i=values.size()-1;i>=0;i--) {
			Value value = values.get(i);
			stack.push(value);
		}
	}
	
	/// Pop the top 'n' values off this stack.
	/// @param n number of pop
	/// @return the set of values popped off the stack
	public static Vector<Value> popx(int n) {
		Vector<Value> values = new Vector<Value>();
		for(int i=0;i<n;i++) {
			Value value = RTStack.pop();
			values.add(value);
		}
		return values;
	}
	
	/// Returns the value at the top of this stack as n int
	/// @return the value at the top of this stack as an int 
	public static int peekInt() {
		IntegerValue ival = (IntegerValue) peek();
		return (ival==null)? 0 : ival.value;
	}
	
	/// Pop the top off this stack and returns that value as an int
	/// @return the int value popped off the stack
	public static int popInt() {
		Value value = RTStack.pop();
		if(value == null) return 0;
		if(value instanceof IntegerValue ival) {
			return (ival==null)? 0 : ival.value;
		}
		Util.IERR(""+value.getClass().getSimpleName());
		return 0;
	}
	
	/// Pop the top off this stack and returns that value as a float
	/// @return the floar value popped off the stack
	public static float popReal() {		
		Value val = pop();
		return (val==null)? 0 : val.toFloat();
		
	}
	
	/// Pop the top off this stack and returns that value as a double
	/// @return the double value popped off the stack
	public static double popLongReal() {
		LongRealValue rval = (LongRealValue) pop();
		return (rval==null)? 0 : rval.value;
	}
	
	/// Pop the top two values off this stack and return it as a GeneralAddress.
	/// @return the GeneralAddress value popped off the stack
	public static GeneralAddress popGADDR() {
		int ofst = RTStack.popInt();
		ObjectAddress base = (ObjectAddress) RTStack.pop();
		return new GeneralAddress(base,ofst);
	}
	
	/// Pop the top two values off this stack and return it as an ObjectAddress.
	/// @return the ObjectAddress value popped off the stack
	public static ObjectAddress popGADDRasOADDR() {
		int ofst = RTStack.popInt();
		ObjectAddress chradr = (ObjectAddress) RTStack.pop();
		if(chradr == null) {
			if(ofst != 0) Util.IERR("");
			return null;
		}
		if(ofst != 0) chradr = chradr.addOffset(ofst);
		return chradr;
	}
	
	/// Returns the current Frame index
	/// @return the current Frame index
	public static int frameIndex() {
		CallStackFrame top = RTStack.callStack_TOP();
		int frmx = (top == null)? 0 : top.rtStackIndex;
		return frmx;
	}
	
	/// Pop the top off this stack and returns that value as an ObjectAddress value.
	/// @return the ObjectAddress value popped off the stack
	public static ObjectAddress popOADDR() {
		ObjectAddress oadr = (ObjectAddress) RTStack.pop();
		return oadr;
	}
	
	/// Pop the top three items off this stack as an infix(string).
	/// Then, use it to form a Java String value.
	/// @return the Java String formed
	public static String popString() {
		int nchr = RTStack.popInt();
		ObjectAddress oaddr = RTStack.popGADDRasOADDR();
		if(oaddr == null) {
			if(nchr != 0) Util.IERR("");
			return null;
		}
		ObjectAddress x = (ObjectAddress) oaddr.copy();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<nchr;i++) {
			IntegerValue ival = (IntegerValue) x.load(i);
			char c = (ival==null)? '.' : (char) ival.value;
			sb.append(c);
		}
		return sb.toString();
	}
	
//	public static void listRTStack() {
//		String s = "     RTStack ===> ";
//		for(Value item:stack) {
//			s += ("   " + item);
//		}
//		IO.println(s);
//	}
	
	
	/// Listing utility: Edit the RTStack as a single line
	/// @return the RTStack as a single line
	public static String toLine() {
		StringBuilder sb = new StringBuilder();
		int n = stack.size();
		sb.append("Stack["+n+"]: ");
		boolean first = true;
		for(int i=0;i<n;i++) {
			Value item = stack.get(i);
			if(! first) sb.append(", "); first = false;
			sb.append(item);
		}
		String s = sb.toString();
		while(s.length() < 30) s = s + ' ';
		return s;
	}
	
	/// Dump the RTStack
	/// @param title the title of the dump printout
	public static void dumpRTStack(String title) {
		IO.println("==== RTStack ================ " + title + " RTStack'DUMP ====================");
		int n = stack.size();
		for(int i=0;i<n;i++) {
			Value item = stack.get(i);
			IO.println("   " + i + ": " + item);
		}
		IO.println("==== RTStack ================ " + title + " RTStack' END  ====================");
	}
}
