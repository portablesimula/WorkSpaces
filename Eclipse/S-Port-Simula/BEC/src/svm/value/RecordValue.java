/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;
import java.util.Vector;

import bec.Global;
import bec.Option;
import bec.descriptor.Attribute;
import bec.descriptor.ConstDescr;
import bec.descriptor.Display;
import bec.descriptor.RecordDescr;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.segment.DataSegment;

/// RecordValue.
/// 
///		record_value
/// 		::= c-record structured_type
/// 			<attribute_value>+ endrecord
/// 
/// 		structured_type ::= record_tag:tag
/// 
/// 		attribute_value
/// 			::= attr attribute:tag type repetition_value
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/RecordValue.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class RecordValue extends Value {

	/// The Record Tag
	public Tag tag;
	
	/// The Record Attributes
	public Vector<Value> attrValues;
	
	/// Construct a new RecordValue.
	private RecordValue() {
		attrValues = new Vector<Value>();
	}
	
	/// Scans the remaining S-Code belonging to this RecordValue.
	/// Then construct a new RecordValue instance.
	/// @return that RecordValue instance.
	public static RecordValue ofScode() {
		RecordValue recValue = new RecordValue();
		recValue.tag = Tag.ofScode();
		RecordDescr rec = (RecordDescr) recValue.tag.getMeaning();
		
		for(int i=0;i<rec.size;i++)
			 recValue.attrValues.add(null);
		
		while(Scode.accept(Sinstr.S_ATTR)) {
			Tag tag = Tag.ofScode();
			@SuppressWarnings("unused")
			Type type = Type.ofScode();
			RepetitionValue atrvalue = RepetitionValue.ofScode();
			Attribute attr = (Attribute) tag.getMeaning();
			
			if(attr.repCount == 0) {
				int n = 0;
				for(Value val:atrvalue.values) {
					if(val != null && val.type == Type.T_TEXT && attr.type == Type.T_CHAR) {
						n += recValue.addChars((TextValue) val);
					} else n += recValue.addValue(val);
				}
				if(ConstDescr.fixrepTail > 0 && ConstDescr.fixrepTail < n) Util.IERR("Too many elements in repetition: " + n + " > " + ConstDescr.fixrepTail);
				for(int i=n;i<ConstDescr.fixrepTail;i++)
					recValue.addValue(null);
			} else {
				int idx = attr.rela;
				for(Value val:atrvalue.values) {
					idx = setValue(recValue.attrValues, idx, val);
				}
			}
		}
		Scode.expect(Sinstr.S_ENDRECORD);
		
		RecordDescr recordDescr = (RecordDescr) Display.get(recValue.tag.val);
		recValue.type = Type.lookupType(recordDescr);
		return recValue;
	}
	
	/// Update the 'attrValues(idx)' with the given value.
	/// @param attrValues a set of values
	/// @param idx the index in attrValues
	/// @param value the value
	/// @return the index updated with the number of basic values treated
	private static int setValue(final Vector<Value> attrValues, int idx, final Value value) {
		if(value instanceof TextValue txt) {
			idx = setValue(attrValues, idx, txt.emitChars(Global.TSEG));
			idx = setValue(attrValues, idx, null);
			idx = setValue(attrValues, idx, IntegerValue.of(Type.T_INT, txt.textValue.length()));
			return idx;
		} else if(value instanceof RecordValue recval) {
			for(Value atrval:recval.attrValues) {
				idx = setValue(attrValues, idx, atrval);
			}
			return idx;
		}
		
		else if(value instanceof GeneralAddress)  Util.IERR("");
		else if(value instanceof StringValue)     Util.IERR("");
		else if(value instanceof RepetitionValue) Util.IERR("");
		
		attrValues.set(idx, value);
		return idx+1;
	}
	
	/// Add the given value to 'attrValues'.
	/// Note: Some values consists of several basic values
	/// @param value the value to be added
	/// @return the number of basic values added
	private int addValue(final Value value) {
		if(value instanceof TextValue txt) {
			addValue(txt.emitChars(Global.TSEG));
			addValue(null);
			addValue(IntegerValue.of(Type.T_INT, txt.textValue.length()));
			return 3;
		} else if(value instanceof RecordValue rval) {
			int n = 0;
			for(Value val:rval.attrValues) n = n + addValue(val);
			return n;
		}

		else if(value instanceof GeneralAddress)  Util.IERR("");
		else if(value instanceof StringValue)     Util.IERR("");
		else if(value instanceof RepetitionValue) Util.IERR("");

		attrValues.add(value);
		return 1;
	}

	/// Add characters to 'attrValues'.
	/// @param txt the text value to be added
	/// @return the number of basic values added
	private int addChars(final TextValue txt) {
		int n = txt.textValue.length();
		for(int i=0;i<n;i++) {
			addValue(IntegerValue.of(Type.T_CHAR, txt.textValue.charAt(i)));			
		}
		return n;
	}
	
	@Override
	public void emit(final DataSegment dseg) {
		for(Value value:attrValues) {
			if(value == null)
				 dseg.emit(null);
			else value.emit(dseg);
		}
	}

	@Override
	public void print(final String indent) {
		IO.println(indent + "C-RECORD " + tag);
		for(Value value:attrValues) {
			IO.println(indent + "   "+value);
		}
		IO.println(indent + "ENDRECORD");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("C-RECORD " + tag);
		for(Value value:attrValues) {
			sb.append(" ATTR " + value);
		}
		sb.append(" ENDRECORD");
		return sb.toString();
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	
	/// Construct a RecordValue from reading the given input
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private RecordValue(final AttributeInputStream inpt) throws IOException {
		int expectedSize = inpt.readShort();
		tag = Tag.read(inpt);
		attrValues = new Vector<Value>();
		int kind = inpt.readUnsignedByte();
		while(kind != Sinstr.S_ENDRECORD) {
			Value value = Value.read(kind, inpt);
			attrValues.add(value);
			kind = inpt.readUnsignedByte();
		}
		if(attrValues.size() != expectedSize) Util.IERR("TRAP: expected size="+expectedSize+"  read size="+attrValues.size());
	}

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_C_RECORD);
		oupt.writeShort(attrValues.size());
		tag.write(oupt);
		for(Value value:attrValues) {
			if(value != null) {
				value.write(oupt);
			} else {
				oupt.writeByte(Sinstr.S_NULL);
			}
		}
		oupt.writeByte(Sinstr.S_ENDRECORD);
	}

	/// Reads a RecordValue from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the RecordValue read
	/// @throws IOException if IOException occur
	public static RecordValue read(final AttributeInputStream inpt) throws IOException {
		return new RecordValue(inpt);
	}

	
}
