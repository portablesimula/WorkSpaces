/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.descriptor;

import bec.Option;
import bec.scode.Scode;
import bec.scode.Type;
import bec.util.Array;
import bec.util.Util;

/// Display.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/Display.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class Display {

	/** Default Constructor */ public Display() {} 

	/// The Descriptor Display
	private static Array<Descriptor> DISPL;
	
	/// Init the Descriptor Display
	public static void init() {
		DISPL = new Array<Descriptor>();		
	}

	/// Returns the Descriptor at the specified tag index
	/// @param tag the tag index
	/// @return the Descriptor at the specified tag index
	public static Descriptor get(int tag) {
		return DISPL.get(tag);
	}
	
	/// Returns the non null Descriptor at the specified tag index.
	/// @param tag the tag index
	/// @return the non null Descriptor at the specified tag index
	public static Descriptor getMeaning(int tag) {
		Descriptor x = DISPL.get(tag);
		if(x == null) Util.IERR("Missing meaning: " + tag);
		return(x);
	}
	
	/// Set the element at the specified position in the Display to the specified Descriptor.
	/// Update the Display at tag index with the given Descriptor.
	/// The Display-entry being already defined is an error.
	/// @param d the Descriptor
	/// @param tag the tag index
	public static void intoDisplay(Descriptor d, int tag) {
		if(tag != 0) {
			Descriptor prev = DISPL.get(tag);
			if(prev == null) ; // OK
			else if(prev != d) {
				Util.IERR("Display-entry is already defined: " + Scode.edTag(tag) + "  " + prev);
			}
			DISPL.set(tag, d);
			if(Option.traceMode != 0) IO.println("Display(" + Scode.edTag(tag) + ") = " +d);
		}
		if(d == null) Util.IERR("");
	}

	/// Delete Display entries.
	/// All tag s defined with values greater than or equal to from:tag are made undefined.
	/// @param fromTag the fromTag index
	public static void delete(int fromTag) {
		for (int t = fromTag; t < Display.DISPL.size(); t++) {
			Display.DISPL.set(t, null);
			Type.removeFromTMAP(t);
		}		
	}

}
