/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec;

import java.io.FileOutputStream;
import java.io.IOException;

import bec.descriptor.Descriptor;
import bec.descriptor.Display;
import bec.descriptor.Kind;
import bec.descriptor.LabelDescr;
import bec.descriptor.ProfileDescr;
import bec.descriptor.RoutineDescr;
import bec.descriptor.Variable;
import bec.instruction.Instruction;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import bec.statement.IfConstrction;
import bec.statement.InsertStatement;
import bec.statement.ProtectConstruction;
import bec.statement.SkipifConstruction;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.instruction.SVM_LINE;
import svm.segment.Segment;

/// This is an implementation of S-Code Module.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/S_Module.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class S_Module {

	/** Default Constructor */ public S_Module() {} 

	/// 	program_element
	/// 		::= instruction
	/// 		::= label_declaration
	/// 		::= routine_profile | routine_definition
	/// 		::= skip_statement | if_statement
	/// 		::= protect_statement
	/// 		::= goto_statement | insert_statement
	/// 		::= delete_statement
	///
	public static void programElements() {
		LOOP: while(true) {
			switch(Scode.curinstr) {
				case Sinstr.S_LABELSPEC ->	LabelDescr.ofLabelSpec();
				case Sinstr.S_LABEL ->		LabelDescr.ofLabelDef(Tag.ofScode());
				case Sinstr.S_PROFILE ->		ProfileDescr.ofScode();
				case Sinstr.S_ROUTINE ->		RoutineDescr.ofRoutineDef();
				case Sinstr.S_IF ->			IfConstrction.ofScode();
				case Sinstr.S_SKIPIF ->		SkipifConstruction.ofScode();
				case Sinstr.S_SAVE ->		ProtectConstruction.ofStatement();
				case Sinstr.S_INSERT ->		new InsertStatement(false);
				case Sinstr.S_SYSINSERT ->	new InsertStatement(true);
				case Sinstr.S_LOCAL ->		Variable.ofGlobal(Global.DSEG);
				default -> { if(! Instruction.inInstruction()) break LOOP; }
			}
			Scode.inputInstr();
		}
	}

	/// setSwitch.
	///
	///		info_setting
	///			::= setswitch switch:byte setting:byte
	///
	/// setSwitch is intended to control various aspects of the working
	/// of the S-compiler such as the production of debugging information.
	///
	/// NOT Implemented.
	protected static void setSwitch() {
		Util.IERR("NOT IMPL");
	}

	/// setLine.
	///
	///		info_setting
	///			::= decl line:number
	///			::= line line:number
	///			::= stmt line:number
	///
	/// The line, decl and stmt instructions are used to inform
	/// about a mapping between a source program and its S-Code.
	///
	/// The argument of the instructions refer to the numbering of
	/// the lines of the program listing from the S-Code producer.
	///
	/// decl informs that the code following is for a SIMULA
	/// declaration starting on the line with the supplied number.
	///
	/// stmt informs that the code following is for a SIMULA
	/// statement starting on the line with the supplied number.
	///
	/// line informs that the current point in the S-Code
	/// corresponds to the start of the source program line with the given number.
	///
	/// The intention is that the Front End Compiler will produce decl and stmt
	/// instructions, while line instructions will occur in the code for the Run-Time System.
	///
	/// @param type one of: decl, stmt, line
	protected static void setLine(final int type) {
//		Global.curline = Scode.inNumber();
//		Global.PSEG.emit(new SVM_LINE(type, Global.curline));
		Scode.curline = Scode.inNumber();
		Global.PSEG.emit(new SVM_LINE(type, Scode.curline));
	}

	/// Output Module.
	/// @param nXtag number of external tags
	protected static void outputModule(final int nXtag) {
		if(Option.ATTR_OUTPUT_TRACE)
			IO.println("**************   Begin  -  Output-module  " + Global.modident + "  " + Global.modcheck + "   **************");
		if(Global.outputDIR==null || Global.outputDIR.isEmpty()) Util.IERR("No Output Directory Specified");
		try {
			AttributeOutputStream modoupt = new AttributeOutputStream(new FileOutputStream(Global.outputDIR+Global.modident+".svm"));
			modoupt.writeByte(Kind.K_Module);
			modoupt.writeString(Global.modident);
			modoupt.writeString(Global.modcheck);
	
			Type.writeRECTYPES(modoupt);
			
			for(Segment seg:Global.routineSegments) seg.write(modoupt);

			for(int i=0;i<=nXtag;i++) {
				int tx = Global.iTAGTAB.get(i);
				Descriptor d = Display.get(tx);
				if(d == null) Util.IERR("External tag " + i + " = Tag " + tx + " is not defined (OutModule)");
				d.write(modoupt);
			}
			
			Global.CSEG.write(modoupt);
			Global.TSEG.write(modoupt);
			Global.DSEG.write(modoupt);
			Global.PSEG.write(modoupt);
	
			modoupt.writeByte(Kind.K_EndModule);
			modoupt.flush(); modoupt.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		if(Option.ATTR_OUTPUT_TRACE)
			IO.println("**************   Endof  -  Output-module  " + Global.modident + "  " + Global.modcheck + "   **************");
	}

}
