/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm;

import svm.value.ProgramAddress;
import svm.value.Value;

/// CallStackFrame.
/// <pre>
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
/// </pre>
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/CallStackFrame.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class CallStackFrame {
	
	/// Routine ident
	public String ident;
	
	/// The Routine Address
	public ProgramAddress rutAddr;
	
	/// The Runtime Stack index
	public int rtStackIndex;
	
	/// The Export size
	public int exportSize;
	
	/// The Inport size
	public int importSize;
	
	/// The locals size
	public int localSize;
	
	/// Construct a new CallStackFrame with the given parameters
	/// @param ident The Routine ident
	/// @param rtStackIndex The Runtime Stack index
	/// @param exportSize The Export size
	/// @param importSize The Inport size
	public CallStackFrame(final String ident, int rtStackIndex, int exportSize, int importSize) {
		this.ident = ident;
		this.rtStackIndex = rtStackIndex;
		this.exportSize = exportSize;
		this.importSize = importSize;
	}

	/// Returns the Frame Head size
	/// @return the Frame Head size
	public int headSize() {
		return exportSize + importSize + 1 + localSize; 
	}
	
	/// Returns the return address
	/// @return the return address
	public ProgramAddress returnAddress() {
		int idx = rtStackIndex + exportSize + importSize;
		return (ProgramAddress) RTStack.load(idx);
	}

	@Override
	public String toString() {
		return ident + ": rtStackIndex=" + rtStackIndex + ", exportSize=" + exportSize + ", importSize=" + importSize + ", localSize=" + localSize;
	}

	/// Listing utility: Edit the CallStackFrame as a single line
	/// @return the CallStackFrame as a single line
	public String toLine() {
		StringBuilder sb = new StringBuilder();
		int idx = rtStackIndex;
		int stx = idx + exportSize + importSize + 1 + localSize;
		boolean first = true;
		sb.append("Frame["+(stx-idx)+"]: ");
		while(idx < stx) {
			Value item = RTStack.load(idx++);
			if(! first) sb.append(", "); first = false;
			sb.append((item == null)? null : item);
		}
		sb.append("  Stack["+(RTStack.size()-idx)+"]: ");
		first = true;
		while(idx < RTStack.size()) {
			Value item = RTStack.load(idx++);
			if(! first) sb.append(", "); first = false;
			sb.append(item);
		}
		return sb.toString();
	}

	/// Print the CallStackFrame
	public void print() {
		String indent = "            ";
		try {
			int idx = rtStackIndex;
			IO.println("    "+ident + ": callStackTop.rtStackIndex=" + idx);
			if(exportSize > 0) {
				for(int i=0;i<exportSize;i++) {
					Value item = RTStack.load(idx);
					IO.println(indent+"EXPORT: " + idx + ": " + item); idx++;
				}
			}
			for(int i=0;i<importSize;i++) {
				Value item = RTStack.load(idx);
				IO.println(indent+"IMPORT: " + idx + ": " + item); idx++;
			}
			IO.println(indent+"RETURN: " + idx + ": " + RTStack.load(idx)); idx++;
			for(int i=0;i<localSize;i++) {
				Value item = RTStack.load(idx);
				IO.println(indent+"LOCAL:  " + idx + ": " + item); idx++;
			}
		} catch(Exception e) {}
	}

	/// Dump the CallStackFrame
	/// @param title the title of the dump printout
	public void dump(String title) {
		CallStackFrame callStackTop = RTStack.callStack_TOP();
		IO.println("==================== " + title + " RTFrame'DUMP ====================");
		IO.println("   ROUTINE: " + rutAddr + " callStackTop.rtStackIndex=" + callStackTop.rtStackIndex);
		print();
		IO.println("==================== " + title + " RTFrame' END  ====================");
		if(rutAddr != null) rutAddr.segment().dump(title);
	}

}
