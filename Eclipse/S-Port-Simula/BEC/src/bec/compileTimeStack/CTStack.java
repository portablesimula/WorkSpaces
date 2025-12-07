/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.compileTimeStack;

import java.util.Stack;

import bec.Global;
import bec.Option;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.NamedStack;
import bec.util.Util;
import svm.value.Value;

/// Compile time Stack.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/compileTimeStack/CTStack.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class CTStack {
	
	/** Default Constructor */ public CTStack() {} 

	/// Current Compile-time Stack.
	private static NamedStack<CTStackItem> stack = new NamedStack<CTStackItem>("MAIN");
	
	/// Stack of saved SAVE-RESTORE Stacks
	private static Stack<NamedStack<CTStackItem>> saveStack = new Stack<NamedStack<CTStackItem>>();
	
	/// Stack of saved BSEG-ESEG Stacks
	private static Stack<NamedStack<CTStackItem>> bsegStack = new Stack<NamedStack<CTStackItem>>();
	
	/// Returns the Stack ident
	/// @return the Stack ident
	public static String ident() {
		return stack.ident();
	}

	/// Returns the current Stack
	/// @return the current Stack
	public static NamedStack<CTStackItem> current() {
		return stack;
	}
	
	/// Returns copy of the current Stack
	/// @param ident the NamedStack ident
	/// @return copy of the current Stack
	public static NamedStack<CTStackItem> copy(final String ident) {
		NamedStack<CTStackItem> copy = new NamedStack<CTStackItem>(ident);
		for(CTStackItem item:stack) {
			copy.add(item.copy());
		}
		return copy;
	}

	/// Reestablish the current Stack.
	/// @param saved the saved Stack to be reestablished
	public static void reestablish(final NamedStack<CTStackItem> saved) {
		stack = new NamedStack<CTStackItem>(saved.ident());
		for(CTStackItem item:saved) {
			stack.add(item.copy());
		}
	}
	
	/// Returns true if the two specified Stacks are equal to one another.
	///
	/// Two Stacks are considered equal if they contain the same number of elements,
	/// and all corresponding pairs of elements in the two arrays are equal.
	///
	/// @param stack1 one Stack to be tested for equality
	/// @param stack2 the other Stack to be tested for equality
	/// @return true if the two Stacks are equal
	public static boolean equals(final NamedStack<CTStackItem> stack1, final NamedStack<CTStackItem> stack2) {
		if(stack1.size() != stack2.size()) return false;
		for(int i=0;i<stack1.size();i++) {
			CTStackItem itm1 = stack1.get(i);
			CTStackItem itm2 = stack2.get(i);
			if(itm1.type == null) {
				if(itm2.type != null) return false;				
			} else {
				if(itm1.type.tag != itm2.type.tag) return false;
			}
		}
		return true;
	}

	/// Utility for the protect_statement.
	///
	/// * remember stack;
	/// * purge stack;
	/// @param ident the SAVE ident
	public static void SAVE(final String ident) {
		saveStack.push(stack);
		stack = new NamedStack<CTStackItem>(ident);
	}

	/// Utility for the protect_statement.
	///
	///  * check stack empty;
	///  * reestablish stack remembered at corresponding SAVE;
	public static void RESTORE() {
		stack = saveStack.pop();
	}

	/// Utility for the segment_instruction.
	///
	/// * remember stack;
	/// * purge stack;
	/// @param ident the Segment ident
	public static void BSEG(final String ident) {
		bsegStack.push(stack);
		stack = new NamedStack<CTStackItem>(ident);
	}

	/// Utility for the segment_instruction.
	///
	///  * check stack empty;
	///  * reestablish stack remembered at corresponding BSEG;
	public static void ESEG() {
		checkStackEmpty();
		stack = bsegStack.pop();
	}
	
	/// Returns the Top of the Compile-time stack
	/// @return the Top of the Compile-time stack
	public static CTStackItem TOS() {
		if(stack.size() < 1) {
			stack.dumpStack("CTSTACK UNDERFLOW at TOS");
			Util.IERR("CTSTACK UNDERFLOW at TOS");
		}
		return stack.getLast();
	}
	
	/// Returns the Second of the Compile-time stack
	/// @return the Second of the Compile-time stack
	public static CTStackItem SOS() {
		if(stack.size() < 2) {
			stack.dumpStack("CTSTACK UNDERFLOW at SOS");
			Util.IERR("CTSTACK UNDERFLOW at SOS");
		}
		return stack.get(stack.size()-2);
	}
	
	/// Returns the indexed item of the Compile-time stack
	/// @param i the index
	/// @return the indexed item of the Compile-time stack
	public static CTStackItem getItem(final int i) {
		return stack.get(i);
	}
	
	/// Returns the size of the Compile-time stack
	/// @return the size of the Compile-time stack
	public static int size() {
		return stack.size();
	}
	
	/// Push a Compile-time stack item onto the Compile-time stack.
	/// @param item a Compile-time stack item
	public static void push(final CTStackItem item) {
		stack.push(item);
	}
	
	/// Push a TempItem onto the Compile-time stack.
	/// @param type the type
	public static void pushTempItem(final Type type) {
		push(new TempItem(type));
	}
	
	/// Push a ConstItem onto the Compile-time stack.
	/// @param type the type
	/// @param value the constant value
	public static void pushCoonst(final Type type, final Value value) {
		push(new ConstItem(type, value));
	}
	
	/// Pop an item off the Compiler-time stack.
	/// @return the item popped
	public static CTStackItem pop() {
		return stack.pop();
	}
	
	/// Push a copy of TOS onto the Compile-time stack.
	public static void dup() {
		stack.push(TOS().copy());
	}
	
	/// Utility: Print Stack related error message and exit.
	/// @param msg a descriptive message
	private static void STKERR(final String msg) {
		IO.println("\nERROR: " + msg + " ================================================");
		CTStack.dumpStack("STKERR: ");
		Global.PSEG.dump("STKERR: ");
		Util.IERR("FORCED EXIT: " + msg);
	}

	/// Convenient method: checkTosRef
	public static void checkTosRef() {
		if(Option.debugMode)
			if(! (TOS() instanceof AddressItem)) STKERR("CheckTosRef fails: " + TOS());
	}

	/// Convenient method: checkSosRef
	public static void checkSosRef() {
		if(Option.debugMode)
			if(! (SOS() instanceof AddressItem)) STKERR("CheckTosRef fails: " + SOS());
	}

	/// Convenient method: checkSosValue
	public static void checkSosValue() {
		if(Option.debugMode) if(SOS() instanceof AddressItem) STKERR("CheckSosValue fails");
	}

	/// Convenient method: checkTosType
	/// @param t the type to be checked for
	public static void checkTosType(final Type t) {
		if(Option.debugMode) if(TOS().type != t) STKERR("Illegal type of TOS: " + TOS().type + " expected: " + t);
	}

	/// Convenient method: checkSosType
	/// @param t the type to be checked for
	public static void checkSosType(Type t) {
		if(Option.debugMode) 
			if(SOS().type != t) STKERR("Illegal type of TOS");
	}

	/// Convenient method: checkTosInt
	public static void checkTosInt() {
		if(Option.debugMode) switch(TOS().type.tag) {
			case Tag.TAG_INT, Tag.TAG_SINT: break; 
			default: STKERR("Illegal type of TOS");
		}
	}

	/// Convenient method: checkTosArith
	public static void checkTosArith() {
		if(Option.debugMode) switch(TOS().type.tag) {
			case Tag.TAG_INT, Tag.TAG_SINT, Tag.TAG_REAL, Tag.TAG_LREAL: break; 
			default: STKERR("Illegal type of TOS");
		}
	}

	/// Convenient method: checkSosInt
	public static void checkSosInt() {
		if(Option.debugMode) switch(SOS().type.tag) {
			case Tag.TAG_INT, Tag.TAG_SINT: break; 
			default: STKERR("Illegal type of xTOS");
		}
	}

	/// Convenient method: checkSosArith
	public static void checkSosArith() {
		if(Option.debugMode) {
			Type type = SOS().type;
			switch(type.tag) {
				case Tag.TAG_INT, Tag.TAG_SINT, Tag.TAG_REAL, Tag.TAG_LREAL: break; 
				default: STKERR("Illegal type of SOS: " + type);
			}
		}
	}

	/// Convenient method: checkSosType2
	/// @param t1 a Type
	/// @param t2 another Type
	/// @return SOS.type
	public static Type checkSosType2(final Type t1, final Type t2) {
		Type type = SOS().type;
		if(type == t1) ; // OK
		else if(type == t2) ; // OK
		else STKERR("Illegal type of SOS");
		return type;
	}

	/// Convenient method: checkTypesEqual
	public static void checkTypesEqual() {
		if(Option.debugMode) {
			Type t1 = TOS().type;
			Type t2 = SOS().type;
			if(t1 == t2) return;
			switch(t1.tag) {
		      case Tag.TAG_INT, Tag.TAG_SINT:
		    	  switch(t2.tag) {
			    	  case Tag.TAG_INT, Tag.TAG_SINT:  return;
		    	  }
			}
			Type.dumpTypes("checkTypesEqual: ");
			STKERR("Different types of TOS=" + t1 + " and SOS=" + t2);
		}
	}

	/// Convenient method: checkStackEmpty
	public static void checkStackEmpty() {
		if(Option.debugMode) if(stack.size() != 0) {
			STKERR("Stack should be empty");
			stack = new NamedStack<CTStackItem>("ERR");
		}
	}

	/// Debug utility: dumpStack
	/// @param title the title of the dump printout
	public static void dumpStack(final String title) {
		stack.dumpStack(0, title);
	}

	/// Debug utility: dumpStack
	/// @param level the indent level of the dump printout
	/// @param title the title of the dump printout
	public static void dumpStack(final int level, final String title) {
		stack.dumpStack(level, title);
	}

}
