/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.descriptor;

import java.io.IOException;

import bec.Option;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;

/// Attribute descriptor.
///
/// S-CODE:
/// <pre>
/// attribute_definition
///		::= attr attr:newtag quantity_descriptor
///
///		quantity_descriptor ::= resolved_type < Rep count:number >?
///
///			resolved_type
///				::= resolved_structure | simple_type
///				::= INT range lower:number upper:number
///				::= SINT
///
///				resolved_structure ::= structured_type < fixrep count:ordinal >?
///
///					structured_type ::= record_tag:tag
/// </pre>
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/Attribute.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public final class Attribute extends Descriptor {
	
	/// The type of this Attribute
	public Type type;
	
	/// The relative address of this attribute within a Record.
	public int rela;
	
	/// The repetition count
	public int repCount;

	/// Create a new Attribute with the given 'tag'
	/// @param tag used to lookup descriptors
	private Attribute(final Tag tag) {
		super(Kind.K_Attribute, tag);
	}

	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new Attribute instance.
	/// @param rela the relative address
	/// @return an Attribute instance.
	public static Attribute ofScode(final int rela) {
		Attribute attr = new Attribute(Tag.ofScode());
		attr.type = Type.ofScode();
		attr.rela = rela;
		attr.repCount = (Scode.accept(Sinstr.S_REP)) ? Scode.inNumber() : 1;
		return attr;
	}
	
	/// Returns the size of this attribute
	/// @return the size of this attribute
	public int size() {
		return type.size();
	}
	
	/// Returns size * repcount
	/// @return size * repcount
	public int allocSize() {
		return type.size() * repCount;
	}
	
	@Override
	public void print(final String indent) {
		IO.println(indent + this);
	}
	
	@Override
	public String toString() {
		return "Attribute: " + tag + " rela=" + rela + ", repCount=" + repCount;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("LocDescr.Write: " + this);
		oupt.writeByte(kind);
		tag.write(oupt);
		type.write(oupt);
		
		oupt.writeShort(rela);
		oupt.writeShort(repCount);
	}

	/// Reads an Attribute from the given input.
	/// @param inpt the input stream
	/// @return The Attribute read
	/// @throws IOException if IOException occur
	public static Attribute read(final AttributeInputStream inpt) throws IOException {
		Attribute attr = new Attribute(Tag.read(inpt));
		attr.type = Type.read(inpt);
		attr.rela = inpt.readShort();
		attr.repCount = inpt.readShort();
		if(Option.ATTR_INPUT_TRACE) IO.println("attrDescr.Read: " + attr);
		return attr;
	}


}
