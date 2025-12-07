/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/// Attribute Input Stream.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/util/AttributeInputStream.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class AttributeInputStream extends DataInputStream {

	/// Create a new AttributeInputStream
	/// @param inpt the underlying input stream
	/// @throws IOException if IOException occur
   public AttributeInputStream(final InputStream inpt) throws IOException {
    	super(inpt);
    }

    /// Read String or null
    /// @return String or null
	/// @throws IOException if IOException occur
    public String readString() throws IOException {
    	int lng = super.readShort();
    	if(lng < 0) return null;
    	    	
    	char[] chars = new char[lng];
		for (int i = 0; i < lng; i++) chars[i] = super.readChar();
		return new String(chars);
    }


}
