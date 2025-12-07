/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.descriptor;

import java.io.IOException;
import java.util.Vector;

import bec.Global;
import bec.Option;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.value.ObjectAddress;
import svm.value.RepetitionValue;
import svm.value.Value;

/// Constant descriptor.
///
/// S-CODE:
/// <pre>
/// constant_declaration
/// 		::= constant_specification | constant_definition
/// 
///	constant_specification
///		::= constspec const:newtag quantity_descriptor
///
///	constant_definition
///		::= const const:spectag quantity_descriptor repetition_value
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
///
///		repetition_value
///			::= boolean_value+
///			::= character_value+ | text_value
///			::= integer_value+ | size_value+
///			::= real_value+ | longreal_value+
///			::= attribute_address+ | object_address+
///			::= general_address+ | program_address+
///			::= routine_address+ | record_value+
///
///			text_value
///				::= text long_string
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/ConstDescr.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class ConstDescr extends Descriptor {

	/// The type of this ConstDescr
	public Type type;
	
	/// The address of this ConstDescr
	private ObjectAddress address;
	
	/// The values of this ConstDescr
	private Vector<RepetitionValue> values;
	
	/// The fixrepTail of this ConstDescr
	public static int fixrepTail;
	
	/// Create a new ConstDescr with the given 'tag'
	/// @param tag used to lookup descriptors
	private ConstDescr(final Tag tag) {
		super(Kind.K_Coonst, tag);
		this.values = new Vector<RepetitionValue>();
	}

	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new ConstDescr instance.
	/// @return an ConstDescr instance.
	public static ConstDescr ofConstSpec() {
		Tag tag = Tag.ofScode();
		ConstDescr cnst = (ConstDescr) Display.get(tag.val);
		if(cnst != null) Util.IERR("New CONSPEC but cnst="+cnst);
		
		cnst = new ConstDescr(tag);
		cnst.type = Type.ofScode();
		
		if(Scode.accept(Sinstr.S_FIXREP)) {
			Scode.inNumber();
		}

		int repCount = (Scode.accept(Sinstr.S_REP)) ? Scode.inNumber() : 1;
		return cnst;
		
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new ConstDescr instance.
	/// @return an ConstDescr instance.
	public static ConstDescr ofConstDef() {
		Tag tag = Tag.ofScode();
		ConstDescr cnst = (ConstDescr) Display.get(tag.val);
		if(cnst == null) {
			cnst = new ConstDescr(tag);
		}
		cnst.type = Type.ofScode();
		
		fixrepTail = 0;
		if(Scode.accept(Sinstr.S_FIXREP)) {
			int fixrep = Scode.inNumber();
			RecordDescr rec = (RecordDescr) Display.getMeaning(cnst.type.tag);
			int count = rec.size + rec.rep0size * fixrep;
			fixrepTail = rec.rep0size * fixrep;
		}

		int repCount = (Scode.accept(Sinstr.S_REP)) ? Scode.inNumber() : 1;
		
		if(cnst.address != null) {
			cnst.address.fixupAddress(Global.CSEG.nextAddress());
		} else cnst.address = Global.CSEG.nextAddress();
		for(int i=0;i<repCount;i++) {
			RepetitionValue value = RepetitionValue.ofScode();
			cnst.values.add(value);
			value.emit(Global.CSEG);
		}
		return cnst;
	}
	
	/// Returns the address of this ConstDescr
	/// @return the address of this ConstDescr
	public ObjectAddress getAddress() {
		if(address == null)	address = ObjectAddress.ofFixup();
		return address;
	}
	
	@Override
	public void print(final String indent) {
		for(RepetitionValue value:values) {
			boolean done = false;
			if(value.values instanceof Vector<Value> vector) {
				if(vector instanceof Vector<?> elts) {
					boolean first = true;
					for(Object rVal:elts) {
						if(first) IO.println(indent + "CONST " + tag);
						first = false;
						((Value)rVal).print(indent + "   ");							
					} done = true;
				}
			}
			if(! done) IO.println(indent + "   " + toString());
		}
	}
	
	@Override
	public String toString() {
		if(address != null) {
			 return "CONST " + tag + " " + address;
		} else if(values != null) {
			 return "CONST " + tag + " " + values;
		} else return "CONSTSPEC " + tag;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("CONST.Write: " + this);
		oupt.writeByte(kind);
		tag.write(oupt);
		type.write(oupt);
		address.write(oupt);
	}

	/// Reads an ConstDescr from the given input.
	/// @param inpt the input stream
	/// @return The ConstDescr read
	/// @throws IOException if IOException occur
	public static ConstDescr read(final AttributeInputStream inpt) throws IOException {
		Tag tag = Tag.read(inpt);
		ConstDescr cns = new ConstDescr(tag);
		cns.type = Type.read(inpt);
		cns.address = (ObjectAddress) Value.read(inpt);
		return(cns);
	}


}
