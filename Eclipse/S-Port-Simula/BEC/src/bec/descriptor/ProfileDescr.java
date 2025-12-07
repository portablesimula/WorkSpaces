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
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.instruction.SVM_CALL_SYS;
import svm.segment.DataSegment;
import svm.value.ObjectAddress;
import svm.value.Value;

/// Profile descriptor.
///
/// S-CODE:
/// <pre>
///	routine_profile
///		 ::= profile profile:newtag < peculiar >?
///			   < import_definition >* < export or exit >? endprofile
///
///		peculiar
///			::= known body:newtag kid:string
///			::= system body:newtag sid:string
///			::= external body:newtag nature:string xid:string
///			::= interface pid:string
///
///		import_definition
///			::= import parm:newtag quantity_descriptor
///
///		export_or_exit
///			::= export parm:newtag resolved_type
///			::= exit return:newtag
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/descriptor/ProfileDescr.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class ProfileDescr extends Descriptor {
	
	/// Peculiar Profile Kind
	public int pKind;
	
	/// The parameters
	public Vector<Tag> params;
	
	/// The Export tag
	private Tag exportTag;
	
	/// The retur value slot address
	public ObjectAddress returSlot;
	
	/// The data segment for the routine code
	public DataSegment DSEG;
	
	/// The size of Export slot
	public int exportSize;
	
	/// Size of Frame on RTStack
	public int frameSize; // Size of Frame
	
//	private static final boolean DEBUG = false;
	
	//	NOT SAVED:
//	private Vector<Variable> imports;
//	public Variable export;
//	public Variable exit;
	
	/// Peculiar body tag
	public int bodyTag;
	
	/// Peculiar nature
	private String nature;
	
	/// Peculiar ident
	public String ident;
	
	/// Create a new ProfileDescr with the given 'tag'
	/// @param tag used to lookup descriptors
	private ProfileDescr(final Tag tag) {
		super(Kind.K_ProfileDescr, tag);
	}
	
	/// Returns the simple name
	/// @return the simple name
	public String getSimpleName() {
		return tag.ident();
	}
	
	/// Returns the export Variable
	/// @return the export Variable
	public Variable getExport() {
		if(exportTag == null) return null;
		Variable export = (Variable) exportTag.getMeaning();
		return export;
	}
	
	/// Returns the DataSegment ident
	/// @return the DataSegment ident
	public String dsegIdent() {
		return "DSEG_" + Global.moduleID + '_' + tag.ident();

	}

	/// Scans the remaining S-Code (if any) belonging to this descriptor.
	/// Then construct a new ProfileDescr instance.
	/// @return an ProfileDescr instance.
	public static ProfileDescr ofScode() {
		Tag ptag = Tag.ofScode();
		ProfileDescr prf = new ProfileDescr(ptag);
		if(Scode.nextByte() == Sinstr.S_EXTERNAL) {
			 // peculiar ::= external body:newtag nature:string xid:string
			Scode.inputInstr();
			Tag.ofScode();
			Scode.inString();
			Util.IERR("External Routines is not part of this implementation");
		} else if(Scode.nextByte() == Sinstr.S_INTERFACE) {
			 // peculiar ::= interface pid:string
			Scode.inputInstr();
			String xid = Scode.inString();
			prf.pKind = SVM_CALL_SYS.getSysKind(xid);
		} else if(Scode.nextByte() == Sinstr.S_KNOWN) {
			// peculiar	::= known body:newtag kid:string
			Scode.inputInstr();
			Tag rtag = Tag.ofScode();
			String xid = Scode.inString();
			@SuppressWarnings("unused")
			RoutineDescr rut = new RoutineDescr(rtag, null);
			prf.pKind = SVM_CALL_SYS.getKnownKind(xid);
		} else if(Scode.nextByte() == Sinstr.S_SYSTEM) {
			 //	peculiar ::= system body:newtag sid:string
			Scode.inputInstr();
			Tag rtag = Tag.ofScode();
			String xid = Scode.inString();
			@SuppressWarnings("unused")
			RoutineDescr rut = new RoutineDescr(rtag, null);
			prf.pKind = SVM_CALL_SYS.getSysKind(xid);
		}
		
		Vector<Variable> imports;
		Variable export = null;
		Variable exit = null;

		imports = new Vector<Variable>();
		prf.params = new Vector<Tag>();

		Scode.inputInstr();
		while(Scode.curinstr == Sinstr.S_IMPORT) {
			Variable par = null;
			par = Variable.ofIMPORT();				
			imports.add(par);
			prf.params.add(par.tag);
			Scode.inputInstr();
		}
		if(Scode.curinstr == Sinstr.S_EXIT) {
			exit = Variable.ofEXIT();
			Scode.inputInstr();
		} else if(Scode.curinstr == Sinstr.S_EXPORT) {
			export = Variable.ofEXPORT();				
			prf.exportTag = export.tag;
			Scode.inputInstr();
		}
		if(exit == null) exit = Variable.ofRETUR(prf.returSlot);
		if(Scode.curinstr != Sinstr.S_ENDPROFILE)
			Util.IERR("Missing ENDPROFILE. Got " + Sinstr.edInstr(Scode.curinstr));
		
		// Allocate StackFrame
		int rela = 0;
		if(export != null) {
			export.address = ObjectAddress.ofRelFrameAddr(rela);
			prf.exportSize = export.type.size();
			rela += prf.exportSize;
		}
		for(Variable par:imports) {
			par.address = ObjectAddress.ofRelFrameAddr(rela);
			rela += par.type.size() * par.repCount;
		}
		// Allocate Return address
		prf.returSlot = ObjectAddress.ofRelFrameAddr(rela++);
		
		if(exit != null) {
			exit.address = prf.returSlot;
		}
		
//		if(DEBUG) {
//			prf.print("ProfileDescr.ofProfile: PROFILE: ");
//		}
		prf.frameSize = rela;
		return prf;
	}

	@Override
	public void print(final String indent) {
		String profile = "PROFILE " + tag;
		switch(kind) {
		case Sinstr.S_KNOWN ->     profile += " KNOWN "     + Scode.edTag(bodyTag) + " \"" + ident + '"';
		case Sinstr.S_SYSTEM ->    profile += " SYSTEM "    + Scode.edTag(bodyTag) + " " + ident;
		case Sinstr.S_EXTERNAL ->  profile += " EXTERNAL "  + Scode.edTag(bodyTag) + " " + nature + " " + ident;
		case Sinstr.S_INTERFACE -> profile += " INTERFACE " + ident;
		}
		IO.println(indent + profile);
		if(exportTag != null) IO.println(indent + "   " + exportTag.getMeaning());
		if(params != null) for(Tag ptag:params) IO.println(indent + "   " + ptag.getMeaning());
		if(returSlot != null) IO.println(indent + "   ReturSlot = " + returSlot);
		if(DSEG != null) IO.println(indent + "   DSEG = " + DSEG);
		IO.println(indent + "ENDPROFILE  FrameHeadSize="+frameSize);	
	}
	
	@Override
	public String toString() {
		String profile = "PROFILE " + tag;
		switch(kind) {
			case Sinstr.S_KNOWN ->     profile += " KNOWN " + Scode.edTag(bodyTag) + " \"" + ident + '"';
			case Sinstr.S_SYSTEM ->    profile += " SYSTEM " + Scode.edTag(bodyTag) + " " + ident;
			case Sinstr.S_EXTERNAL ->  profile += " EXTERNAL " + Scode.edTag(bodyTag) + " " + nature + " " + ident;
			case Sinstr.S_INTERFACE -> profile += " INTERFACE " + ident;
		}
		profile += " DSEG=" + DSEG;
		if(params != null) {
			String cc = "( ";
			for(Tag ptag:params) {
				profile += cc +ptag;
				cc = ", ";
			}
			profile += ")";
		}
		if(exportTag != null) profile += " ==> exportTag=" + exportTag;
		profile += " returSlot=" + returSlot;
		return profile;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(final AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("ProfileDescr.Write: " + this);
		oupt.writeByte(kind);
		tag.write(oupt);
		oupt.writeShort(pKind);
		oupt.writeString(this.dsegIdent());
		oupt.writeShort(frameSize);
		oupt.writeShort(params.size());
		for(Tag par:params) par.write(oupt);
		returSlot.write(oupt);
//		if(export != null) {
		if(exportTag != null) {
			oupt.writeBoolean(true);
			exportTag.write(oupt);
			oupt.writeShort(exportSize);
		} else oupt.writeBoolean(false);
	}

	/// Reads a ProfileDescr from the given input.
	/// @param inpt the input stream
	/// @return The ProfileDescr read
	/// @throws IOException if IOException occur
	public static ProfileDescr read(final AttributeInputStream inpt) throws IOException {
		ProfileDescr prf = new ProfileDescr(Tag.read(inpt));
		if(Option.ATTR_INPUT_TRACE) IO.println("BEGIN ProfileDescr.Read: " + prf);
		prf.pKind = inpt.readShort();
		String segID = inpt.readString();
		prf.frameSize = inpt.readShort();
		prf.params = new Vector<Tag>();
		int n = inpt.readShort();
		for(int i=0;i<n;i++) {
			prf.params.add(Tag.read(inpt));
		}
		prf.returSlot = (ObjectAddress) Value.read(inpt);
		boolean present = inpt.readBoolean();
		if(present) {
			prf.exportTag = Tag.read(inpt);
			prf.exportSize = inpt.readShort();
		}
		
		if(Option.ATTR_INPUT_TRACE) IO.println("ProfileDescr.Read: " + prf);
		return prf;
	}


}
