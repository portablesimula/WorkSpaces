/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec;

import java.util.Vector;

import bec.compileTimeStack.CTStack;
import bec.descriptor.ConstDescr;
import bec.descriptor.Kind;
import bec.descriptor.ProfileDescr;
import bec.descriptor.RecordDescr;
import bec.descriptor.RoutineDescr;
import bec.descriptor.Variable;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.util.Array;
import bec.util.Util;
import svm.segment.DataSegment;
import svm.segment.ProgramSegment;
import svm.segment.Segment;

/// This is an implementation of S-Code Interface Module definition.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/InterfaceModule.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public final class InterfaceModule extends S_Module {
	
	/// 	interface_module
	/// 		::= global module module_id:string checkcode:string
	/// 					<global interface>* tag_list
	/// 					body < init global:tag type repetition_value >*
	/// 			endmodule
	/// 
	/// 		global_interface
	/// 			::= record_descriptor
	/// 			::= constant_definition < system sid:string >?
	/// 			::= global_definition < system sid:string >?
	/// 			::= routine_profile
	/// 			::= info_setting
	/// 
	/// 				global_definition ::= global internal:newtag quantity_descriptor
	/// 
	/// 		tag_list ::= < tag internal:tag external:number >+
	/// 
	///
	public InterfaceModule() {
		Global.currentModule = this;
		Scode.inputInstr();
		if(Scode.curinstr != Sinstr.S_MODULE) Util.IERR("Missing - MODULE");
		Global.modident = Scode.inString();
		Global.modcheck = Scode.inString();
		Global.moduleID = Global.modident.toUpperCase();
		String sourceID = Global.getSourceID();
		Global.CSEG = new DataSegment("CSEG_" + sourceID, Kind.K_SEG_CONST);
		Global.TSEG = new DataSegment("TSEG_" + sourceID, Kind.K_SEG_CONST);
		Global.DSEG = new DataSegment("DSEG_" + sourceID, Kind.K_SEG_DATA);
		Global.PSEG = new ProgramSegment("PSEG_" + sourceID);
		if(Global.PROGID == null) Global.PROGID = Global.modident;
		Global.routineSegments = new Vector<Segment>();
		LOOP: while(true) {
			Scode.inputInstr();
			switch(Scode.curinstr) {
				case Sinstr.S_GLOBAL:	Variable.ofGlobal(Global.DSEG); break;
				case Sinstr.S_CONSTSPEC: ConstDescr.ofConstSpec(); break;
				case Sinstr.S_CONST:		ConstDescr.ofConstDef(); break;
				case Sinstr.S_RECORD:	RecordDescr.ofScode(); break;
				case Sinstr.S_PROFILE:   ProfileDescr.ofScode(); break;
				case Sinstr.S_ROUTINE:	RoutineDescr.ofRoutineDef();	break;
				case Sinstr.S_LINE:		setLine(0); break;
				case Sinstr.S_DECL:		CTStack.checkStackEmpty(); setLine(Kind.qDCL); break;
				case Sinstr.S_STMT:		CTStack.checkStackEmpty(); setLine(Kind.qSTM); break;
				case Sinstr.S_SETSWITCH:	setSwitch(); break;
				case Sinstr.S_INFO:		Util.WARNING("Unknown info: " + Scode.inString());
				default: break LOOP;
			}
		}

		// tag_list ::= < tag internal:tag external:number >+
		Global.iTAGTAB = new Array<Integer>();
		Global.xTAGTAB = new Array<Integer>();
		int nXtag = 0;
		while(Scode.curinstr == Sinstr.S_TAG) {
			int itag = Scode.inTag();
			int xtag = Scode.inNumber();
			Global.iTAGTAB.set(xtag, itag); // Index xTag --> value iTag
			Global.xTAGTAB.set(itag, xtag); // Index iTag --> value xTag
			Scode.inputInstr();
			if(xtag > nXtag) nXtag = xtag;
		}
			
		outputModule(nXtag);
		if(Scode.curinstr != Sinstr.S_BODY) Util.IERR("Illegal termination of module head");
		Scode.inputInstr();
		if(Scode.curinstr != Sinstr.S_ENDMODULE) Util.IERR("Improper termination of module");
	}
	
}
