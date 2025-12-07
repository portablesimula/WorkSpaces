/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.compileTimeStack;

import bec.descriptor.ProfileDescr;

/// Profile Item.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/compileTimeStack/ProfileItem.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class ProfileItem extends CTStackItem {
	
	/// The associated ProfileDescr
	public ProfileDescr spc;
	
	/// Number of parameters treated so far.
	public int nasspar;
	
	/// Profile item to be pushed onto the Compile-time Stack.
	/// @param spec the associated ProfileDescr
	public ProfileItem(final ProfileDescr spec) {
		this.spc = spec;
		this.nasspar = 0;
	}

	@Override
	public CTStackItem copy() {
		ProfileItem addr = new ProfileItem(spc);
		addr.nasspar = nasspar;
		return addr;
	}

	@Override
	public String toString() {
		return "PROF: " + spc + ", nasspar=" + nasspar;
	}

}
