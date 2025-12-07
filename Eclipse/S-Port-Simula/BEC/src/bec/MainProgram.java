/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec;

import java.util.Vector;

import bec.descriptor.Kind;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.util.EndProgram;
import bec.util.Util;
import svm.RTUtil;
import svm.segment.DataSegment;
import svm.segment.ProgramSegment;
import svm.segment.Segment;
import svm.value.ProgramAddress;

/// This is an implementation of S-Code Main Program definition.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/MainProgram.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public final class MainProgram extends S_Module {
	
	/// 	MainPprogram ::= main <local_quantity>* < program_element >*
	/// 
	/// 		local_quantity ::= local var:newtag quantity_descriptor
	/// 
	///			quantity_descriptor ::= resolved_type < Rep count:number >?
	///
	///		program_element
	///			::= instruction
	///			::= label_declaration
	///			::= routine_profile
	///			::= routine_definition
	///			::= if_statement
	///			::= skip_statement
	///			::= insert_statement
	///			::= protect_statement
	///			::= delete_statement
	///
	public MainProgram() {
		Global.currentModule = this;
		String sourceID = Global.getSourceID();
		Global.CSEG = new DataSegment("CSEG_" + sourceID, Kind.K_SEG_CONST);
		Global.TSEG = new DataSegment("TSEG_" + sourceID, Kind.K_SEG_CONST);
		Global.DSEG = new DataSegment("DSEG_" + sourceID, Kind.K_SEG_DATA);
		Global.PSEG = new ProgramSegment("PSEG_" + sourceID);
		ProgramAddress mainEntry = Global.PSEG.nextAddress();
		if(Global.PROGID == null) Global.PROGID = Global.modident;
		Global.routineSegments = new Vector<Segment>();

		while(Scode.nextByte() == Sinstr.S_LOCAL) {
			Scode.inputInstr(); 
			Util.IERR("NOT IMPL");
		}
		Scode.inputInstr(); 
		programElements();
		
		if(Option.PRINT_GENERATED_SVM_CODE) {
			Global.CSEG.dump("END MainProgram: ");
			Global.TSEG.dump("END MainProgram: ");
			Global.DSEG.dump("END MainProgram: ");
			Global.PSEG.dump("END MainProgram: ");
		}
	
		if(Scode.curinstr != Sinstr.S_ENDPROGRAM)
			Util.IERR("Illegal termination of program");
		
		try {
			if(Option.verbose) Util.println("\n\nBEC: NEW MainProgram: BEGIN EXECUTE: " + mainEntry);
			RTUtil.init();
			Global.PSC = mainEntry;
			while(true) {
				Global.PSC.execute();
			}
		} catch(EndProgram eprog) {
			if(Option.verbose)
				Util.println("BEC: MainProgram - Exit: " + eprog.exitCode);
			return;
		}	
	}
	
}
