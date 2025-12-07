/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import svm.segment.DataSegment;

/// TextValue.
/// 
///		text_value ::= text long_string
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/TextValue.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class TextValue extends Value {
	
	/** Default Constructor */ public TextValue() {} 

	/// The text value string
	public String textValue;
	
	/// Construct a new TextValue with the given parameter string
	/// @param textValue the textValue value string
	private TextValue(final String textValue) {
		this.type = Type.T_TEXT;
		this.textValue = textValue;
	}
	
	/// Scans the remaining S-Code belonging to this TextValue.
	/// Then construct a new TextValue instance.
	/// @return that TextValue instance.
	public static TextValue ofScode() {
		return new TextValue(Scode.inLongString());
	}
	
	/// Emit the characters of this TextValue
	/// @param dseg the DataSegment to receive the chars
	/// @return the address of the emitted value
	public ObjectAddress emitChars(final DataSegment dseg) {
		return dseg.emitChars(textValue);			
	}
	
	@Override
	public void emit(final DataSegment dseg) {
		ObjectAddress addr = emitChars(Global.TSEG);
		dseg.emit(addr);
		dseg.emit(null);
		dseg.emit(IntegerValue.of(Type.T_INT, textValue.length()));
	}
	
	@Override
	public String toString() {
		return "TEXT: " + textValue;
	}
	

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_TEXT);
		oupt.writeString(textValue);
	}

	/// Reads a TextValue from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the TextValue read
	/// @throws IOException if IOException occur
	public static TextValue read(final AttributeInputStream inpt) throws IOException {
		return new TextValue(inpt.readString());
	}


}
