/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/// Attribute Output Stream.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/util/AttributeOutputStream.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class AttributeOutputStream extends DataOutputStream{

	/// Create a new AttributeOutputStream
	/// @param oupt the underlying output stream
	/// @throws IOException if IOException occur
    public AttributeOutputStream(final OutputStream oupt) throws IOException {
    	super(oupt);
    }

    /// Write a String to the underlying OutputStream.
    /// If argument 's' is null a length -1 is written informing
    /// AttributeInputStream.readString to return null.
    /// @param s the string to write
	/// @throws IOException if IOException occur
    public void writeString(final String s) throws IOException {
		if(s == null) {
			super.writeShort(-1);
		} else {
			int lng = s.length();
			if(lng > Short.MAX_VALUE) Util.IERR("");
			super.writeShort(lng);
			super.writeChars(s);
		}
	}

}