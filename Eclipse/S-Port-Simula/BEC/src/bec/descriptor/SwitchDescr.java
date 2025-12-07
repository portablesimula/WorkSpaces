/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.descriptor;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Scode;
import bec.scode.Tag;
import svm.instruction.SVM_SWITCH;
import svm.value.ProgramAddress;

/// Switch.
///
/// S-CODE:
/// <pre>
///	 forward_jump ::= switch switch:newtag size:number
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/SwitchDescr.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SwitchDescr extends Descriptor {
	
	/// The size of this Switch
	int size;
	
	/// The Destination Table of this Switch
	public ProgramAddress[] DESTAB;
	
	/// Create a new SwitchDescr with the given 'tag'
	/// @param tag used to lookup descriptors
	private SwitchDescr(final Tag tag) {
		super(Kind.K_SwitchDescr, tag);
		size = Scode.inNumber();
		DESTAB = new ProgramAddress[size];
		CTStack.checkTosInt();
		CTStack.pop();
    	Global.PSEG.emit(new SVM_SWITCH(DESTAB));
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new Attribute instance.
	/// @return an SwitchDescr instance.
	public static SwitchDescr ofScode() {
		Tag tag = Tag.ofScode();
		SwitchDescr sw = new SwitchDescr(tag);
		return sw;
	}
	
	public String toString() {
		return "SWITCH " + tag + " " + size;
	}
	

}
