/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.descriptor;

import java.io.IOException;
import java.util.Vector;

import bec.Option;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;

/// Record descriptor.
///
/// S-CODE:
/// <pre>
///	record_descriptor
///		::= record record_tag:newtag < record_info >?
///			< prefix_part >? common_part
///			< alternate_part >*
///			endrecord 
///
///			record_info	::= info "TYPE" | info "DYNAMIC"
///			prefix_part	::= prefix resolved_structure
///			common_part	::= < attribute_definition >*
///			alternate_part ::= alt < attribute_definition >*
///				attribute_definition ::= attr attr:newtag quantity_descriptor
///				resolved_structure ::= structured_type < fixrep count:ordinal >?
///					structured_type ::= record_tag:tag
///
///				quantity_descriptor ::= resolved_type < Rep count:number >?
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/RecordDescr.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class RecordDescr extends Descriptor {
	
    /// Record size information
	public int size;
	
    /// Size of rep(0) attribute
	public int rep0size;
	
	/// true: INFO TYPE
	boolean infoType;

	// NOT SAVED
	
	/// The prefix tag
	private int prefixTag;
	
	/// The set of attributes
	private Vector<Attribute> attributes;
	
	/// The set of alternateParts
	private Vector<AlternatePart> alternateParts;
	
	/// Create a new RecordDescr with the given 'tag'
	/// @param tag used to lookup descriptors
	private RecordDescr(final Tag tag) {
		super(Kind.K_RecordDescr, tag);
	}
	
	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new RecordDescr instance.
	/// @return an Attribute instance.
	public static RecordDescr ofScode() {
		RecordDescr rec = new RecordDescr(Tag.ofScode());
		int comnSize = 0;
			
		if(Scode.accept(Sinstr.S_INFO)) {
			@SuppressWarnings("unused")
			String info = Scode.inString();
			rec.infoType = true;
		}
		if(Scode.accept(Sinstr.S_PREFIX)) {
			rec.prefixTag = Scode.inTag();
			RecordDescr prefix = rec.getPrefix(rec.prefixTag);
			comnSize = prefix.size;
		}
		rec.attributes = new Vector<Attribute>();
		while(Scode.accept(Sinstr.S_ATTR)) {
			Attribute attr = Attribute.ofScode(comnSize);
			comnSize = comnSize + attr.allocSize();
			if(attr.repCount == 0) rec.rep0size = attr.size();
			rec.attributes.add(attr);
		}
		rec.size = comnSize;
		while(Scode.accept(Sinstr.S_ALT)) {
			if(rec.alternateParts == null) rec.alternateParts = new Vector<AlternatePart>();
			AlternatePart alt = rec.new AlternatePart();
			rec.alternateParts.add(alt);
			int altSize = comnSize;
			while(Scode.accept(Sinstr.S_ATTR)) {
				Attribute attr = Attribute.ofScode(altSize);
				altSize = altSize + attr.allocSize();
				alt.attributes.add(attr);
			}
			rec.size = Math.max(rec.size, altSize);
		}
		Scode.expect(Sinstr.S_ENDRECORD);
		Type.newRecType(rec);
		return rec;
	}
	
	/// Returns the prefix RecordDescr
	/// @param prefixTag the prefixTag
	/// @return the prefix RecordDescr
	private RecordDescr getPrefix(final int prefixTag) {
		RecordDescr prefix = (RecordDescr) Display.get(prefixTag);
		return prefix;
	}
		
	@Override
	public void print(final String indent) {
		String head = "RECORD " + tag + " Size=" + size;
		if(infoType)  head = head + " INFO TYPE";
		if(prefixTag > 0) head = head + " PREFIX " + Scode.edTag(prefixTag);
		IO.println(indent + head);
		if(attributes != null) for(Attribute attr:attributes) {
			IO.println(indent + "   " + attr.toString());
		}
		if(alternateParts != null) {
			for(AlternatePart alt:alternateParts) {
				alt.print(indent + "   ");
			}
		}
		IO.println("   " + "ENDRECORD");
	}
		
	@Override
	public String toString() {
		String head = "RECORD " + tag + " Size=" + size;
		if(infoType)  head = head + " INFO TYPE";
		if(prefixTag > 0) head = head + " PREFIX " + Scode.edTag(prefixTag);
		return head + " ...";
	}


	/// AlternatePart.
	///
	///	alternate_part ::= alt <attribute_definition>*
	///		attribute_definition ::= attr attr:newtag quantity_descriptor
	///		resolved_structure ::= structured_type < fixrep count:ordinal >?
	///			structured_type ::= record_tag:tag
	///
	///		quantity_descriptor ::= resolved_type < Rep count:number >?
	/// 
	///
	class AlternatePart {
		/// The set of attributes
		Vector<Attribute> attributes;
		
		/// Construct a new AlternatePart
		public AlternatePart() {
			attributes = new Vector<Attribute>();
		}
	
		/// Returns the Alternate size
		/// @return the Alternate size
		public int size() {
			int n = 0;
			for(Attribute attr:attributes) n = n + attr.size();
			return n;
		}
		
		/// Utility: print
		/// @param indent the indent String
		public void print(final String indent) {
			boolean first = true;
			for(Attribute attr:attributes) {
				if(first) {
					IO.println(indent + "ALT " + attr);
					first = false;
				}
				else IO.println(indent + "    " + attr);
			}
		}
	}	

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("RecordDescr.Write: " + this);
		oupt.writeByte(kind);
		tag.write(oupt);
		oupt.writeShort(size);
		oupt.writeShort(rep0size);
		oupt.writeBoolean(infoType);
	}

	/// Reads a RecordDescr from the given input.
	/// @param inpt the input stream
	/// @return The RecordDescr read
	/// @throws IOException if IOException occur
	public static RecordDescr read(final AttributeInputStream inpt) throws IOException {
		RecordDescr rec = new RecordDescr(Tag.read(inpt));
		rec.size = inpt.readShort();
		rec.rep0size = inpt.readShort();
		rec.infoType = inpt.readBoolean();
		if(Option.ATTR_INPUT_TRACE) IO.println("RecordDescr.Read: " + rec);
		return rec;
	}

}
