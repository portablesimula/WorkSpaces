/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.scode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import bec.Option;
import bec.descriptor.Kind;
import bec.descriptor.RecordDescr;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;

/// Type.
/// 
///		type
///			::= structured_type | simple_type | range_type
///
///			simple_type ::= BOOL | CHAR | INT | REAL | LREAL | SIZE | OADDR | AADDR | GADDR | PADDR | RADDR
/// 
///			structured_type ::= record_tag:tag
///
///			range_type
///				::= INT range lower:number upper:number  -- NOTE: DETTE ER NYTT
///				::= SINT                                 -- NOTE: DETTE ER NYTT
///
///
///		resolved_type
///			::= resolved_structure
///			::= simple_type
///			::= INT Range lower:number upper:number
///			::= SINT
///
/// Any data quantity must belong to some type. The type will define the internal structure of the quantity
/// as well as the operations that may be performed upon it. Types are used as generators in global,
/// constant, local or parameter definitions and as specificators in quantity descriptors. Each type defines a
/// descriptor (of the same type), this descriptor cannot be used on the stack, thus types cannot be used
/// dynamically as e.g. parameters.
///
/// The distinction between resolved and non-resolved type is made because of the indefinite repetition,
/// which may occur in structured types. Such a type cannot be used as a generator, or in further type
/// definition, without determining the actual number of elements in the repetition.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/scode/Type.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class Type {
	
	/// The type tag index
	public  int tag;
	
	/// The size of type in basic cells
	private int size;
	
	/// The size of the 'REP 0' attribute in basic cells
	private int rep0size;

	/// The Type Map
	private static HashMap<Integer, Type> TMAP;
	
	/// The set of Record Types
	private static Vector<Type> RECTYPES;
	
	/** Basic Type */ public static Type T_VOID;
	/** Basic Type */ public static Type T_TEXT;
	/** Basic Type */ public static Type T_STRING;
	/** Basic Type */ public static Type T_BOOL;
	/** Basic Type */ public static Type T_CHAR;
	/** Basic Type */ public static Type T_INT;
	/** Basic Type */ public static Type T_SINT;
	/** Basic Type */ public static Type T_REAL;
	/** Basic Type */ public static Type T_LREAL;
	/** Basic Type */ public static Type T_SIZE;
	/** Basic Type */ public static Type T_OADDR;
	/** Basic Type */ public static Type T_AADDR;
	/** Basic Type */ public static Type T_GADDR;
	/** Basic Type */ public static Type T_PADDR;
	/** Basic Type */ public static Type T_RADDR;
	
	/// Construct a new Type
	/// @param tag the type tag index
	/// @param size the size of type in basic cells
	private Type(final int tag, final int size) {
		this.tag = tag;
		this.size = size;
	}

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Finally: Return a new Type.
	/// @return the new Type
	public static Type ofScode() {
		int tag = Scode.inTag();
		if(tag == Tag.TAG_INT) {
			if(Scode.accept(Sinstr.S_RANGE)) {
				Scode.inNumber(); // low
				Scode.inNumber(); // high
			}
		}
		Type type = TMAP.get(tag);
		if(type == null) {
			Util.IERR("Illegal type: " + Scode.edTag(tag));
		}
		return type;
	}

	/// Returns the Type corresponding to the given RecordDescr
	/// @param rec a RecordDescr
	/// @return the Type corresponding to the given RecordDescr
	public static Type lookupType(final RecordDescr rec) {
		Type type = TMAP.get(rec.tag.val);
		if(type == null) Util.IERR("Type.recType: UNKNOWN: " + rec);
		return type;
	}

	/// Define a new Record Type
	/// @param rec a RecordDescr
	public static void newRecType(final RecordDescr rec) {
		Type type = new Type(rec.tag.val, rec.size);
		type.rep0size = rec.rep0size;
		TMAP.put(rec.tag.val, type);
		RECTYPES.add(type);
	}
	
	/// Utility: removeFromTMAP
	/// @param tag the tag
	public static void removeFromTMAP(final int tag) {
		TMAP.remove(tag);
	}

	/// Returns true when Simple Type
	/// @return true when Simple Type
	public boolean isSimple() {
		return tag <= Tag.TAG_SIZE;
	}

	/// Returns true when RecordTag
	/// @return true when RecordTag
	public boolean isRecordType() {
		return tag > Tag.T_max;
	}
	
	/// Returns true when INT, REAL, LREAL
	/// @return true when INT, REAL, LREAL
	public boolean isArithmetic() {
		switch(tag) {
		case Tag.TAG_INT, Tag.TAG_REAL, Tag.TAG_LREAL: return true;
		}
		return false;
	}
	
	/// Returns the size of type in basic cells
	/// @return the size of type in basic cells
	public int size() {
		return size;
	}
	
	
	/// Initiate Type data
	public static void init() {
		TMAP = new HashMap<Integer, Type>();
		RECTYPES = new Vector<Type>();		

		// type                    tag         size )
		T_VOID   = newBasType(Tag.TAG_VOID,   0   );
		T_TEXT   = newBasType(Tag.TAG_TEXT,   3   );
		T_STRING = newBasType(Tag.TAG_STRING, 3   );
		T_BOOL   = newBasType(Tag.TAG_BOOL,   1   );
		T_CHAR   = newBasType(Tag.TAG_CHAR,   1   );
		T_INT    = newBasType(Tag.TAG_INT,    1   );
		T_SINT   = newBasType(Tag.TAG_SINT,   1   );
		T_REAL   = newBasType(Tag.TAG_REAL,   1   );
		T_LREAL  = newBasType(Tag.TAG_LREAL,  1   );
		T_SIZE   = newBasType(Tag.TAG_SIZE,   1   );
		T_OADDR  = newBasType(Tag.TAG_OADDR,  1   );
		T_AADDR  = newBasType(Tag.TAG_AADDR,  1   );
		T_GADDR  = newBasType(Tag.TAG_GADDR,  2   );
		T_PADDR  = newBasType(Tag.TAG_PADDR,  1   );
		T_RADDR  = newBasType(Tag.TAG_RADDR,  1   );
	}

	/// Create a new Base Type
	/// @param tag the type tag index
	/// @param size the size of type in basic cells
	/// @return a new Type
	private static Type newBasType(final int tag, final int size) {
		Type type = new Type(tag, size);
		TMAP.put(tag, type);
		return type;
	}

	/// Debug utility: dumpTypes
	/// @param title the title of the dump printout
	public static void dumpTypes(final String title) {
		IO.println("============ "+title+" BEGIN Dump Types ================");
		for(Integer type:TMAP.keySet()) {
			IO.println("TTAB["+type+"] = " + TMAP.get(type));
		}
		for(Type type:RECTYPES) {
			IO.println("Record TYPE = " + type);
			
		}
		IO.println("============ "+title+" ENDOF Dump Types ================");
	}
	
	@Override
	public String toString() {
		return Scode.edTag(tag) + " size=" + size + ", rep0size=" + rep0size;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Writes the Record Types to the given output.
	/// @param oupt the output stream
    /// @throws IOException if an I/O error occurs
	public static void writeRECTYPES(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("writeRECTYPES: ");
		oupt.writeByte(Kind.K_RECTYPES);
		oupt.writeShort(RECTYPES.size());
		for(Type type:RECTYPES) {
			oupt.writeString(Scode.TAGIDENT.get(type.tag));
			oupt.writeShort(type.tag);
			oupt.writeShort(type.size);
		}
	}

	/// Reads the Record Types from the given input.
	/// @param inpt the input stream
    /// @throws IOException if an I/O error occurs
	public static void readRECTYPES(final AttributeInputStream inpt) throws IOException {
		int n = inpt.readShort();
		for(int i=0;i<n;i++) {
	    	String ident = inpt.readString();
	    	int tag = inpt.readShort();
	    	Scode.TAGIDENT.set(tag, ident);
			int size = inpt.readShort();
			Type type = new Type(tag, size);
			
			if(tag == Tag.TAG_STRING) ; // OK Predefinert
			else if(TMAP.get(tag) ==null) {
				TMAP.put(tag, type);
				RECTYPES.add(type);
			}
		}
	}

	/// Writes this Type to the given output.
	/// @param oupt the output stream
    /// @throws IOException if an I/O error occurs
	public void write(final AttributeOutputStream oupt) throws IOException {
		oupt.writeShort(tag);
	}

	/// Reads a Type from the given input.
	/// @param inpt the input stream
	/// @return the Type read
    /// @throws IOException if an I/O error occurs
	public static Type read(final AttributeInputStream inpt) throws IOException {
		int tag = inpt.readShort();
		Type type = TMAP.get(tag);
		if(type == null) Util.IERR("SJEKK DETTE");
		return type;
	}


}
