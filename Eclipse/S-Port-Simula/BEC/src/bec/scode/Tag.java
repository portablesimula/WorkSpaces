/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.scode;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.descriptor.Descriptor;
import bec.descriptor.Display;
import bec.statement.InsertStatement;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;

/// Tag.
///
/// 
/// 	tag
///			::= An ordinal (the "tag-value") associated with a descriptor. See section 2.4.
///			::= The number zero followed by an ordinal (the "tag value") and an identifying string.
///
///	The second form is intended for debugging purposes and is used to associate an identification
///	with the tag.
///
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/scode/Tag.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class Tag {
	
	/// Tag's value
	public int val;

	/** Basic Tag */ public final static int TAG_VOID  = 0;
	/** Basic Tag */ public final static int TAG_BOOL  = 1;
	/** Basic Tag */ public final static int TAG_CHAR  = 2;
	/** Basic Tag */ public final static int	TAG_INT   = 3;
	/** Basic Tag */ public final static int TAG_SINT  = 4;
	/** Basic Tag */ public final static int TAG_REAL  = 5;
	/** Basic Tag */ public final static int TAG_LREAL = 6;
	/** Basic Tag */ public final static int TAG_AADDR = 7;
	/** Basic Tag */ public final static int TAG_OADDR = 8;
	/** Basic Tag */ public final static int TAG_GADDR = 9;
	/** Basic Tag */ public final static int TAG_PADDR = 10;
	/** Basic Tag */ public final static int TAG_RADDR = 11;
	/** Basic Tag */ public final static int TAG_SIZE  = 12;
	/** Basic Tag */ public final static int TAG_TEXT  = 13;
	/** Basic Tag */ public final static int T_max=13; // Max value of predefined type
	/** Basic Tag */ public final static int TAG_STRING  = 32;

	/// Create a new Tag with the given Tag val
	/// @param val the Tag value
	public Tag(int val) {
		this.val = val;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Finally: Return a new Tag.
	/// @return the new Tag read
	public static Tag ofScode() {
		return new Tag(Scode.inTag());
	}
	
	/// Returns the Tag ident
	/// @return the Tag ident
	public String ident() {
		return Scode.TAGIDENT.get(val);
	}
	
	/// Returns the Meaning of this Tag
	/// @return the Meaning of this Tag
	public Descriptor getMeaning() {
		return Display.getMeaning(this.val);
	}
	
	/// Returns the external tag
	/// @return the external tag
	/// @param t the internal tag index
	private static int xTag(final int t) { // export range(0:MaxType) tx;
		Integer xx = Global.xTAGTAB.get(t);
		int tx = (xx == null)? 0 : xx;
		return tx + T_max + 1;
	}
	
	/// Debug utility: dumpITAGTABLE
	/// @param title the title of the dump printout
	public static void dumpITAGTABLE(String title) {
		IO.println("============ "+title+" BEGIN Dump iTAGTABLE ================");
		for(int i=0;i<Global.iTAGTAB.size();i++) {
			Integer tx = Global.iTAGTAB.get(i);
			int xx = (tx==null)? 0 : tx;
			IO.println("iTAGTABLE["+i+"]  iTag:" + Scode.edTag(xx) + "  ==> xTag:" + i);
		}
		IO.println("============ "+title+" ENDOF Dump iTAGTABLE ================");
	}
	
	/// Debug utility: dumpXTAGTABLE
	/// @param title the title of the dump printout
	public static void dumpXTAGTABLE(String title) {
		IO.println("============ "+title+" BEGIN Dump xTAGTABLE ================");
		for(int i=32;i<Global.xTAGTAB.size();i++) {
			IO.println("xTAGTABLE["+i+"]  xTag:" + xTag(i) + "  ==> iTag:" + Scode.edTag(i));
		}
		IO.println("============ "+title+" ENDOF Dump xTAGTABLE ================");
	}
	
	/// Utility: chgInType - used by Tag.read
	/// @param tx external Tag index
	/// @return the corresponding internal Tag index
	private static int chgInType(final int tx) {
		int t = 0;
		if(tx <= T_max) t = tx; else {
			t = tx - T_max + InsertStatement.current.bias - 1;
		}
		if(Option.ATTR_INPUT_TRACE)
			IO.println("chgInType xTag:" + tx + " ==> " + Scode.edTag(t));
		return t;
	}

	@Override
	public String toString() {
		return Scode.edTag(val);
	}
	

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Writes a Tag to the given output.
	/// @param oupt the output stream
	/// @throws IOException if IOException occur
	public void write(final AttributeOutputStream oupt) throws IOException {
		oupt.writeString(Scode.TAGIDENT.get(val));
		oupt.writeShort(xTag(val));
	}
	
	/// Reads a Tag from the given input.
	/// @param inpt the input stream
	/// @return the Tag read
	/// @throws IOException if IOException occur
	public static Tag read(AttributeInputStream inpt) throws IOException {
		String ident = inpt.readString();
		int tag = inpt.readShort();
		tag = chgInType(tag);
		Scode.TAGIDENT.set(tag, ident);
		return new Tag(tag);
	}
	
}
