/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.compileTimeStack;

import bec.scode.Scode;
import bec.scode.Type;

/// Temp Item.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/compileTimeStack/TempItem.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class TempItem extends CTStackItem {
	
	/// Temp item to be pushed onto the Compile-time Stack.
	/// @param type the type of the object addressed
	public TempItem(final Type type) {
		this.type = type;
	}

	@Override
	public CTStackItem copy() {
		return new TempItem(type);
	}

	@Override
	public String toString() {
		return "TEMP: " + Scode.edTag(type.tag);
	}
}
