/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.segment;

import java.io.IOException;
import java.util.Vector;

import bec.Option;
import bec.descriptor.Kind;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;
import svm.value.Value;

/// Data Segment.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/segment/DataSegment.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class DataSegment extends Segment {
	
	/// The set of values in this DataSegment.
	Vector<Value> values;
	
//	private int guard = -1;

	/// DataSegment constructor.
	/// @param ident the Segment ident
	/// @param segmentKind K_SEG_DATA, K_SEG_CONST
	public DataSegment(final String ident, final int segmentKind) {
		super(ident, segmentKind);
		this.ident = ident.toUpperCase();
		this.segmentKind = segmentKind;
		values = new Vector<Value>();
	}
	
//	public void addGuard(int ofst) {
//		guard = ofst;
//	}
	
	/// Returns the ObjectAddress with the given offset
	/// @param ofst the offset
	/// @return the ObjectAddress with the given offset
	public ObjectAddress ofOffset(final int ofst) {
		return ObjectAddress.ofSegAddr(this,ofst);
	}
	
	/// Returns the next ObjectAddress
	/// @return the next ObjectAddress
	public ObjectAddress nextAddress() {
		return ObjectAddress.ofSegAddr(this,values.size());
	}
	
	/// Returns the size of this DataSegment
	/// @return the size of this DataSegment
	public int size() {
		return values.size();
	}
	
	/// Store a value into this DataSegment at the given index
	/// @param index the index
	/// @param value the value
	public void store(final int index, final Value value) {
//		if(index == guard) Util.IERR("FATAL ERROR: Attempt to change Guarded location: "+ObjectAddress.ofSegAddr(this, index)+" from "+values.get(index)+" to "+value);
		values.set(index, value);
	}
	
	/// Load a value from this DataSegment at the given index
	/// @param index the index
	/// @return the value at the given index
	public Value load(final int index) {
		return values.get(index);
	}
	
	/// Emit a value by adding it to this DataSegment.
	/// @param value value to be added
	/// @return the address of the value emitted
	public ObjectAddress emit(final Value value) {
		ObjectAddress addr = nextAddress();
		values.add(value);
		if(Option.PRINT_GENERATED_SVM_DATA)
			listData("                                 ==> ", addr.ofst);
		return addr;
	}

	/// Listing utility: List a DataSegment entry
	/// @param indent indentation String
	/// @param idx index to the value
	private void listData(final String indent, final int idx) {
		String line = ident + "[" + idx + "] ";
		while(line.length() < 8) line = " " +line;
		String val = ""+values.get(idx);
		IO.println(indent + line + val);
		
	}

	/// Emit default value by emiting the necessary number of null values.
	/// @param size the value size
	/// @param repCount the repetition count
	public void emitDefaultValue(final int size, final int repCount) {
		if(repCount < 1) Util.IERR("");
		boolean option = Option.PRINT_GENERATED_SVM_DATA;
		int LIMIT = 30;
		int n = size * repCount;
		for(int i=0;i<n;i++) {
			if(Option.PRINT_GENERATED_SVM_DATA && i == LIMIT) {
				IO.println("                                 ==> ... " + (n-LIMIT) + " more truncated");
				Option.PRINT_GENERATED_SVM_DATA = false;
			}
			emit(null);
		}
		Option.PRINT_GENERATED_SVM_DATA = option;
	}
	
	/// Emit the characters in the given String
	/// @param chars the String
	/// @return the address of the first character emitted
	public ObjectAddress emitChars(final String chars) {
		ObjectAddress addr = nextAddress();
		int n = chars.length();
		for(int i=0;i<n;i++) {
			emit(IntegerValue.of(Type.T_CHAR, chars.charAt(i)));
		}
		return addr;
	}

	@Override
	public void dump(final String title) {
		dump(title,0,values.size());
	}
	
	@Override
	public void dump(final String title,int from,int to) {
		if(values.size() == 0) return;
		IO.println("==================== " + title + ident + " DUMP ====================" + this.hashCode());
		for(int i=from;i<to;i++) {
			String line = "" + i + ": ";
			while(line.length() < 8) line = " " +line;
			String value = ""+values.get(i);
			while(value.length() < 25) value = value + ' ';
			IO.println(line + value);
		}
		IO.println("==================== " + title + ident + " END  ====================");
	}
	
	@Override
	public String toString() {
		if(segmentKind == Kind.K_SEG_CONST)
			return "ConstSegment \"" + ident + '"';
		return "DataSegment \"" + ident + '"';
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("DataSegment.Write: " + this + ", Size=" + values.size());
		if(values.size() == 0) return;
		oupt.writeByte(segmentKind);
		oupt.writeString(ident);
		oupt.writeShort(values.size());
		for(int i=0;i<values.size();i++) {
			Value val = values.get(i);
			if(val == null)
				 oupt.writeByte(Sinstr.S_NULL);
			else val.write(oupt);
		}
	}

	/// Returns a DataSegment read from the given AttributeInputStream
	/// @param inpt the AttributeInputStream
	/// @param segmentKind the Segment kind code
	/// @return a DataSegment read from the given AttributeInputStream
	/// @throws IOException if IOException occur
	public static DataSegment readObject(final AttributeInputStream inpt, final int segmentKind) throws IOException {
		String ident = inpt.readString();
		DataSegment seg = new DataSegment(ident, segmentKind);

		int n = inpt.readShort();
		for(int i=0;i<n;i++) {
			seg.values.add(Value.read(inpt));
		}
		
		if(Option.ATTR_INPUT_TRACE) IO.println("DataSegment.Read: " + seg);
		if(Option.ATTR_INPUT_DUMP) seg.dump("DataSegment.readObject: ");
		return seg;
	}
	

}
