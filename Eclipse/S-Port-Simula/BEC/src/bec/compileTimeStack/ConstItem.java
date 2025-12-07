/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.compileTimeStack;

import bec.scode.Type;
import svm.value.Value;

/// Constant Item.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/compileTimeStack/ConstItem.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class ConstItem extends TempItem {
	
	/// The Constant value
	public Value value;

	/// Value item to be pushed onto the Compile-time Stack.
	/// @param type the type of the Constant value
	/// @param value the Constant value
	public ConstItem(final Type type, final Value value) {
		super(type);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "CNST: " +type + " " + value;
	}
}
