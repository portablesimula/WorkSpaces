/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.value;

import java.io.IOException;

import bec.Option;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;

/// StringValue.
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/value/StringValue.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class StringValue extends Value {
	
	/** Default Constructor */ public StringValue() {} 

	/// The address to the characters
	public ObjectAddress addr;
	
	/// The length of the string
	public int lng;
	
	/// Construct a new StringValue with the given parameters
	/// @param addr the address to the characters
	/// @param lng the length of the string
	public StringValue(final ObjectAddress addr, final int lng) {
		this.type = Type.T_STRING;
		this.addr = addr;
		this.lng = lng;
	}
	
	@Override
	public String toString() {
		return "String: " + addr + ", lng="+lng;
	}
	

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("Value.write: " + this);
		oupt.writeByte(Sinstr.S_STRING);
		oupt.writeString(addr.segID);
		oupt.writeShort(addr.ofst);
		oupt.writeShort(lng);
	}

	/// Reads a StringValue from the given input.
	/// @param inpt the AttributeInputStream
	/// @return the StringValue read
	/// @throws IOException if IOException occur
	public static StringValue read(final AttributeInputStream inpt) throws IOException {
		String segID = inpt.readString();
		int ofst = inpt.readShort();
		ObjectAddress addr = ObjectAddress.ofSegAddr(segID,	ofst);
		int lng = inpt.readShort();
		return new StringValue(addr, lng);
	}


}
