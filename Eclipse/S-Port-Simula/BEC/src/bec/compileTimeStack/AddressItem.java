/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.compileTimeStack;

import bec.scode.Type;
import svm.value.ObjectAddress;

/// Address Item.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/compileTimeStack/AddressItem.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class AddressItem extends CTStackItem {
	
	/// The object address
	public ObjectAddress objadr;
	
	/// The additional offset
	public int offset;
	
	/// Address item to be pushed onto the Compile-time Stack.
	/// @param type the type of the object addressed
	/// @param offset an extra offset
	/// @param objadr object address
	///
	public AddressItem(final Type type, final int offset, final ObjectAddress objadr) {
		this.type = type;
		this.objadr = objadr;
		this.offset = offset;
	}

	@Override
	public CTStackItem copy() {
		return new AddressItem(type, offset, objadr);
	}

	@Override
	public String toString() {
		String s = "" + type + " AT " + objadr + "[" + offset;
		if(objadr.indexed) s += "+IDX";
		s =  s  + "]";
		if(objadr.kind == ObjectAddress.REMOTE_ADDR) s = s + " withRemoteBase";
		return "ADDR: " + s;
	}

}
