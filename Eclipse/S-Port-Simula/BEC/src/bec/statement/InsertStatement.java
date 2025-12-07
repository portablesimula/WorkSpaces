/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.statement;

import java.io.FileInputStream;
import java.io.IOException;

import bec.descriptor.ConstDescr;
import bec.descriptor.Kind;
import bec.descriptor.LabelDescr;
import bec.Global;
import bec.Option;
import bec.descriptor.Attribute;
import bec.descriptor.ProfileDescr;
import bec.descriptor.RecordDescr;
import bec.descriptor.RoutineDescr;
import bec.descriptor.Variable;
import bec.scode.Scode;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.Util;
import svm.segment.DataSegment;
import svm.segment.ProgramSegment;

/// S-INSTRUCTION: INSERT.
/// <pre>
/// insert_statement
/// 		::= insert module_id:string check_code:string
/// 			external_id:string tagbase:newtag taglimit:newtag
/// 
/// 		::= sysinsert module_id:string check_code:string
/// 			external_id:string tagbase:newtag taglimit:newtag
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/statement/InsertStatement.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class InsertStatement {
	
	/** Default Constructor */ public InsertStatement() {} 

	/// The Module ident
    String modid;

    /// The tag bias
    public int bias;
    
    /// The current InsertStatement
    public static InsertStatement current;

    /// Construct a new InsertStatement with the given parameter.
    /// @param sysmod true: when SYSINSERT
    ///
	@SuppressWarnings("unused")
	public InsertStatement(final boolean sysmod) {
		modid = Scode.inString();
		String check = Scode.inString();
		String extid = Scode.inString();
		bias = Scode.inTag();
		int limit = Scode.inTag();

		if(Option.ATTR_INPUT_TRACE)
			IO.println("**************   Begin  -  Input-module  " + modid + "  " + check + "   **************");
		try {
			current = this;
			readDescriptors(sysmod);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Util.IERR("ERROR READING: Input-module  " + modid + "  " + check);
		}
		if(Option.ATTR_INPUT_TRACE)
			IO.println("**************   Endof  -  Input-module  " + modid + "  " + check + "   **************");
	}
	
	/// Read all Segments, Descriptors and Data Types
    /// @param sysmod true: when SYSINSERT
	/// @throws IOException if IOException occur
	private void readDescriptors(final boolean sysmod) throws IOException {
		String fileName = null;
		if(sysmod) {
			fileName = Global.rtsDir+modid+".svm";
		} else {
			fileName = Global.getAttrFileName(modid, ".svm");
		}
		if(Option.verbose) IO.println("INSERT " + fileName);
		if(Option.ATTR_INPUT_TRACE) IO.println("ATTRIBUTE INPUT: " + fileName);
		AttributeInputStream inpt = new AttributeInputStream(new FileInputStream(fileName));
		int kind = inpt.readUnsignedByte();
		if(kind != Kind.K_Module) Util.IERR("Missing MODULE");
		String modident = inpt.readString();
		@SuppressWarnings("unused")
		String modcheck = inpt.readString();
		if(! modident.equalsIgnoreCase(modid)) Util.IERR("WRONG modident");
		
	    //   ------ Read Descriptors ------
		LOOP:while(true) {
			int prevKind = kind;
			kind = inpt.readUnsignedByte();
			
			if(kind == Kind.K_EndModule) break LOOP;
			switch(kind) {
				case Kind.K_RECTYPES:		Type.readRECTYPES(inpt); break;
				case Kind.K_SEG_DATA:		DataSegment.readObject(inpt, kind); break;
				case Kind.K_SEG_CONST:		DataSegment.readObject(inpt, kind); break;
				case Kind.K_SEG_CODE:		ProgramSegment.readObject(inpt); break;
				case Kind.K_Coonst:			ConstDescr.read(inpt); break;
				case Kind.K_RecordDescr:	RecordDescr.read(inpt); break;
				case Kind.K_Attribute:		Attribute.read(inpt); break;
				case Kind.K_GlobalVar:		Variable.read(inpt, kind); break;
				case Kind.K_LocalVar:		Variable.read(inpt, kind); break;
				case Kind.K_ProfileDescr:	ProfileDescr.read(inpt); break;
				case Kind.K_Import:			Variable.read(inpt, kind); break;
				case Kind.K_Export:			Variable.read(inpt, kind); break;
				case Kind.K_Exit:			Variable.read(inpt, kind); break;
				case Kind.K_Retur:			Variable.read(inpt, kind); break;
				case Kind.K_IntRoutine:		RoutineDescr.read(inpt); break;
				case Kind.K_IntLabel:		LabelDescr.read(inpt); break;
				default: Util.IERR("MISSING: " + Kind.edKind(kind) + ", prevKind=" + Kind.edKind(prevKind));
			}
		}
	}
	
}
