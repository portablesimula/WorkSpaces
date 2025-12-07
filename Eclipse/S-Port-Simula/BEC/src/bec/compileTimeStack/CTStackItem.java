/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.compileTimeStack;

import bec.scode.Type;

/// Compile time Stack Item.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/compileTimeStack/CTStackItem.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public abstract class CTStackItem {
	
	/** Default Constructor */ public CTStackItem() {} 

	/// The common type attribute
	public Type type;
	
	/// Returns a copy of this stack item
	/// @return a copy of this stack item
	public abstract CTStackItem copy();

}
