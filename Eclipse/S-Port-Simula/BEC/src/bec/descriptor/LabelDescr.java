/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.descriptor;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.compileTimeStack.CTStack;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.value.ProgramAddress;
import svm.value.Value;

/// Label descriptor.
///
/// S-CODE:
/// <pre>
/// label_declaration
///		::= label_specification | label_definition
///
/// 	label_specification
///			::= labelspec label:newtag
///
/// 	label_definition
///			::= label label:spectag
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/LabelDescr.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class LabelDescr extends Descriptor {
	
	/// The address of this LabelDescr
	private ProgramAddress adr;

	/// Create a new LabelDescr with the given 'tag'
	/// @param tag used to lookup descriptors
	/// @param adr the label address
	private LabelDescr(Tag tag, ProgramAddress adr) {
		super(Kind.K_IntLabel, tag);
		this.adr = adr;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new LabelDescr instance.
	/// @return an LabelDescr instance.
	public static LabelDescr ofLabelSpec() {
		Tag tag = Tag.ofScode();
		LabelDescr lab = (LabelDescr) Display.get(tag.val);
		if(lab != null) Util.IERR("");
		lab = new LabelDescr(tag, null);
		return lab;
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct or update the LabelDescr.
	/// @param tag the label tag
	/// @return the LabelDescr instance.
	public static LabelDescr ofLabelDef(final Tag tag) {
		LabelDescr lab = (LabelDescr) Display.get(tag.val);
		ProgramAddress paddr = Global.PSEG.nextAddress();
		if(lab == null) {
			lab = new LabelDescr(tag, paddr);
		} else {
			if(lab.adr != null) {
				lab.adr.fixupAddress(paddr.segID, paddr.ofst);
			} else lab.adr = paddr;
	}
//     	Global.PSEG.emit(new SVM_NOOP());
		CTStack.checkStackEmpty();
		return lab;
	}
	
	/// Returns the address of this LabelDescr
	/// @return the address of this LabelDescr
	public ProgramAddress getAddress() {
		if(adr == null)	adr = ProgramAddress.ofFixup(Type.T_PADDR);
		return adr;
	}
	
	@Override
	public String toString() {
		return "LabelDescr " + tag + " " + adr;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("IntDescr.Write: " + this);
		oupt.writeByte(kind);
		tag.write(oupt);
		if(adr != null) {
			oupt.writeBoolean(true);
			adr.write(oupt);
		} else {
			oupt.writeBoolean(false);
		}
	}

	/// Reads a LabelDescr from the given input.
	/// @param inpt the input stream
	/// @return The LabelDescr read
	/// @throws IOException if IOException occur
	public static LabelDescr read(final AttributeInputStream inpt) throws IOException {
		Tag tag = Tag.read(inpt);
		LabelDescr lab = new LabelDescr(tag, null);
		boolean present = inpt.readBoolean();
		if(present) lab.adr = (ProgramAddress) Value.read(inpt);
		if(Option.ATTR_INPUT_TRACE) IO.println("LabelDescr.Read: " + lab);
		return(lab);
	}


}
