/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.scode;

import bec.Global;
import bec.Option;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import bec.util.Array;
import bec.util.Util;

import java.io.FileInputStream;
import java.io.IOException;

/// Global variables.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/scode/Scode.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class Scode {

	/** Default Constructor */ public Scode() {} 

	/// All input bytes
	private static byte[] SBUF;
	
	/// Next pointer into SBUF
	private static int SBUF_nxt;
	
	/// Current instr-byte read from scodeSource
	public static int curinstr;
	
    /// Input Trace's StringBuilder
	private static StringBuilder traceBuff;
	
	/// Current source line number
	public static int curline;
	
	/// The Tag-identifiers
	public static Array<String> TAGIDENT;


	/// Initiate Scode data
	public static void init() {
		Scode.TAGIDENT = new Array<String>();
        Scode.TAGIDENT.set(1, "BOOL");
        Scode.TAGIDENT.set(2, "CHAR");
        Scode.TAGIDENT.set(3, "INT");
        Scode.TAGIDENT.set(4, "SINT");
        Scode.TAGIDENT.set(5, "REAL");
        Scode.TAGIDENT.set(6, "LREAL");
        Scode.TAGIDENT.set(7, "AADDR");
        Scode.TAGIDENT.set(8, "OADDR");
        Scode.TAGIDENT.set(9, "GADDR");
        Scode.TAGIDENT.set(10, "PADDR");
        Scode.TAGIDENT.set(11, "RADDR");
        Scode.TAGIDENT.set(12, "SIZE");
        Scode.TAGIDENT.set(13, "TEXT");

		String fileName = Global.scodeSource;
		try (FileInputStream scode = new FileInputStream(fileName)) {
			SBUF = scode.readAllBytes();
			SBUF_nxt = 0;
			if(Option.verbose) {
				IO.println("\nOpen SCode file: " + fileName + "   size = " + SBUF.length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/// Debug utility: dumpTAGIDENTS
	/// @param title the title of the dump printout
	public static void dumpTAGIDENTS(final String title) {
		IO.println("============ "+title+" BEGIN Dump TAGIDENT ================");
		for(int i=32;i<TAGIDENT.size();i++) {
			String elt =TAGIDENT.get(i);
			IO.println("  " + i + ": " + elt);
		}
		IO.println("============ "+title+"ENDOF Dump TAGIDENT ================");
	}

	/// Trace utility: initTraceBuff
	/// @param head the initial string
	public static void initTraceBuff(final String head) {
		Scode.traceBuff = new StringBuilder(head);		
	}

	/// Trace utility: flushTraceBuff
	public static void flushTraceBuff() {
		if(Option.SCODE_INPUT_TRACE) {
			String ctstk = (CTStack.size() == 0)? "" : " "+CTStack.ident()+"-STACK"+"["+CTStack.size()+"]:";
			
			String line = traceBuff.toString();
			while(line.length() < 60) line = line + " ";
			IO.println(line + ctstk);
			if(CTStack.size() > 0) {
				line = "";
				while(line.length() < 71) line = line + " ";
				for(int i=CTStack.size()-1;i>=0;i--) {
					CTStackItem item = CTStack.getItem(i);
					IO.println(line + item);
				}
			}
		}
		if(Option.PRINT_GENERATED_SVM_CODE) {
			if(Global.PSEG != null) Global.PSEG.listInstructions();
		}
	}

	/// Close Scode reading
	public static void close() {
		flushTraceBuff();
		traceBuff = null;
	}
	
	/// Returns the next byte from the SBUF or null if outside bounds
	/// @return the next byte from the SBUF or null if outside bounds
	public static int nextByte() {
		try {
			return SBUF[SBUF_nxt] & 0xff;
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0xff;
		}
	}
	
	/// Read a byte from SBUF
	/// @return The byte read
	private static int readByte() {
		return SBUF[SBUF_nxt++] & 0xff;
	}
	
	/// Read 2 bytes from SBUF
	/// @return The 2 bytes read as a word
	private static int read2Bytes() {
		int HI = readByte();
		int LO = readByte();
		return (HI << 8) | LO; 
	}

	/// Read an instruction and update 'curinstr'
	public static void inputInstr() {
		Scode.curinstr = readByte();
		if(Scode.curinstr < 1 || Scode.curinstr > Sinstr.S_max) {
			if(traceBuff != null) IO.println(traceBuff);
			Util.IERR("Illegal instruction["+(SBUF_nxt -1)+"]: " + Scode.curinstr);
		}
		if(Option.SCODE_INPUT_TRACE) {
			if(traceBuff != null) {
				flushTraceBuff();
			}
			String instr = Sinstr.edInstr(Scode.curinstr);
			while(instr.length() < 10) instr = instr + ' ';
			Util.ITRC("Ininstr["+(SBUF_nxt-1)+",CTStack="+CTStack.size()+']', instr);
		}
	}
	
	/// Expect next instruction == instr.
	/// Otherwise: Error
	/// @param instr S-code instruction
	public static void expect(final int instr) {
		if(nextByte() == instr) {
			Scode.inputInstr();
		} else Util.IERR("Missing " + Sinstr.edInstr(instr) + " - Got " + Sinstr.edInstr(nextByte()));
	}
	
	/// Expect 'curinstr' == instr.
	/// Otherwise: Error
	/// @param instr S-code instruction
	public static void checkEqual(final int instr) {
		if(curinstr != instr)
			Util.IERR("Missing " + Sinstr.edInstr(instr) + " - Got " + Sinstr.edInstr(curinstr));
	}
	
	/// Check if next instruction == 'instr'
	/// If so: read it and return true,
	/// Otherwise return false
	/// @param instr S-code instruction
	/// @return true if it matched
	public static boolean accept(final int instr) {
		if(nextByte() == instr) {
			Scode.inputInstr();
			return true;
		}
		return false;
	}
	
	/// Returns a String read from SBUF
	/// @return a String read from SBUF
	public static String getString() {
		StringBuilder sb = new StringBuilder();
		int n = readByte();
		for(int i=0;i<n;i++) sb.append((char)readByte());
		return sb.toString();
	}
	
	/// Returns a String read from SBUF
	/// @return a String read from SBUF
	public static String inString() {
		String s = getString();
		if(Option.SCODE_INPUT_TRACE) {
			traceBuff.append(" \"").append(s).append('"');
		}
		return s;
	}
	
	/// Returns a long String read from SBUF
	/// @return a String read from SBUF
	public static String inLongString() {
		StringBuilder sb = new StringBuilder();
		int n = read2Bytes();
		for(int i=0;i<n;i++) sb.append((char)readByte());
		String s = sb.toString();
		if(Option.SCODE_INPUT_TRACE) {
			traceBuff.append(" \"").append(s).append('"');
		}
		return s;
	}
	
	/// Returns a integer Number read from SBUF
	/// @return a integer Number read from SBUF
	public static int inNumber() {
		int n = read2Bytes();
		if(Option.SCODE_INPUT_TRACE) {
			traceBuff.append(" ").append(n);
		}
		return n;
	}	
	
	/// Returns a byte read from SBUF
	/// @return a byte read from SBUF
	public static int inByte() {
		int n = readByte();
		if(Option.SCODE_INPUT_TRACE) {
			traceBuff.append(" ").append(n);
		}
		return n;
	}	
	
	/// Returns a Tag read from SBUF
	/// @return a Tag read from SBUF
	public static int inTag() {
		int t = read2Bytes();
		if(t == 0) {
			t = read2Bytes();
			String id = getString();
			TAGIDENT.set(t, id);
		}
		if(Option.SCODE_INPUT_TRACE) {
			traceBuff.append(" "+edTag(t));
		}
		return t;
	}
	
	/// Returns a edited Tag 
	/// @param t the Tag index
	/// @return the edited Tag 
	public static String edTag(final int t) {
		return "T[" + t + ':' + TAGIDENT.get(t) + ']';		
	}

}
