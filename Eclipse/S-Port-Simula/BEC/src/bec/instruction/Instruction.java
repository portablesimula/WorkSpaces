/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.descriptor.ConstDescr;
import bec.descriptor.RecordDescr;
import bec.descriptor.RoutineDescr;
import bec.descriptor.SwitchDescr;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.statement.IfConstrction;
import bec.statement.ProtectConstruction;
import bec.statement.SkipifConstruction;
import bec.util.Util;

/// S-INSTRUCTION.
/// <pre>
/// instruction
/// 		::= constant_declaration
/// 		::= record_descriptor | routine_specification
/// 		::= stack_instruction | assign_instruction
/// 		::= addressing_instruction | protect_instruction
/// 		::= temp_control | access_instruction
/// 		::= arithmetic_instruction | convert_instruction
/// 		::= jump_instruction | goto_instruction
/// 		::= if_instruction | skip_instruction
/// 		::= segment_instruction | call_instruction
/// 		::= area_initialisation | eval_instruction
/// 		::= info_setting | macro_call
/// </pre> 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/Instruction.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class Instruction {
		
	/** Default Constructor */ public Instruction() {} 

	/// Read S-Code while encountering instruction
	public static void inInstructions() {
		LOOP:while(true) {
			if(! inInstruction()) break LOOP;
			Scode.inputInstr();
		}
	}

	/// Returns true if a 'visible' was encountered
	/// @return true if a 'visible' was encountered
	public static boolean inInstruction() {
		switch(Scode.curinstr) {
			case Sinstr.S_CONSTSPEC ->   ConstDescr.ofConstSpec();
			case Sinstr.S_CONST ->	    ConstDescr.ofConstDef();
			case Sinstr.S_ROUTINESPEC -> RoutineDescr.ofRoutineSpec();
			case Sinstr.S_RECORD ->      RecordDescr.ofScode();
			case Sinstr.S_SETOBJ ->      Util.IERR("SETOBJ is not implemented");
			case Sinstr.S_GETOBJ ->      Util.IERR("GETOBJ is not implemented");
			case Sinstr.S_ACCESS, Sinstr.S_ACCESSV -> Util.IERR("ACCESS is not implemented");
			case Sinstr.S_PUSH ->        PUSH.ofScode(Sinstr.S_PUSH);
			case Sinstr.S_PUSHV ->       PUSH.ofScode(Sinstr.S_PUSHV);
			case Sinstr.S_PUSHC ->       PUSHC.ofScode();
			case Sinstr.S_INDEX, Sinstr.S_INDEXV -> INDEX.ofScode(Scode.curinstr);
			case Sinstr.S_FETCH ->       FETCH.ofScode();
			case Sinstr.S_SELECT ->      SELECT.ofScode(Sinstr.S_SELECT);
			case Sinstr.S_SELECTV ->     SELECT.ofScode(Sinstr.S_SELECTV);
			case Sinstr.S_REMOTE ->      REMOTE.ofScode(Sinstr.S_REMOTE);
			case Sinstr.S_REMOTEV ->     REMOTE.ofScode(Sinstr.S_REMOTEV);
			case Sinstr.S_REFER ->       REFER.ofScode();
			case Sinstr.S_DSIZE ->       DSIZE.ofScode();
			case Sinstr.S_DUP ->         DUP.ofScode();
			case Sinstr.S_POP ->         POP.ofScode();
			case Sinstr.S_POPALL ->      POPALL.ofScode();
			case Sinstr.S_ASSIGN ->      ASSIGN.ofScode();
			case Sinstr.S_UPDATE ->      UPDATE.ofScode();
			case Sinstr.S_RUPDATE ->     RUPDATE.ofScode();
			case Sinstr.S_BSEG ->        BSEG.ofScode();
			case Sinstr.S_IF ->          IfConstrction.ofScode();
			case Sinstr.S_SKIPIF ->      SkipifConstruction.ofScode();
			case Sinstr.S_PRECALL ->     CALL.ofScode(0);
			case Sinstr.S_ASSCALL ->     CALL.ofScode(1);
			case Sinstr.S_REPCALL ->     CALL.ofScode(Scode.inByte());
			case Sinstr.S_GOTO ->        GOTO.ofScode();
			case Sinstr.S_PUSHLEN ->     PUSHLEN.ofScode();
			case Sinstr.S_SAVE ->        ProtectConstruction.ofInstruction();
			case Sinstr.S_T_INITO ->     INITO.ofScode();
			case Sinstr.S_T_GETO ->      GETO.ofScode();
			case Sinstr.S_T_SETO ->      SETO.ofScode();
			case Sinstr.S_DECL ->        LINE.ofScode(1);
			case Sinstr.S_STMT ->        LINE.ofScode(2);
			case Sinstr.S_LINE ->        LINE.ofScode(0);
			case Sinstr.S_EMPTY ->       EMPTY.ofScode();
			case Sinstr.S_SETSWITCH ->   Util.IERR("NOT IMPLEMENTED: " + Sinstr.edInstr(Scode.curinstr));
			case Sinstr.S_INFO ->        Util.IERR("NOT IMPLEMENTED: " + Sinstr.edInstr(Scode.curinstr));
			case Sinstr.S_DELETE ->      DELETE.ofScode();
			case Sinstr.S_ZEROAREA ->    ZEROAREA.ofScode();
			case Sinstr.S_INITAREA ->    INITAREA.ofScode();
			case Sinstr.S_DINITAREA ->   DINITAREA.ofScode();
			case Sinstr.S_EVAL ->        EVAL.ofScode();
			case Sinstr.S_FJUMPIF ->     FJUMPIF.ofScode();
			case Sinstr.S_FJUMP ->       FJUMP.ofScode();
			case Sinstr.S_FDEST ->       FDEST.ofScode();
			case Sinstr.S_BDEST ->       BDEST.ofScode();
			case Sinstr.S_BJUMP ->       BJUMP.ofScode();
			case Sinstr.S_BJUMPIF ->     BJUMPIF.ofScode();
			case Sinstr.S_SWITCH ->      SwitchDescr.ofScode();
			case Sinstr.S_SDEST ->       SDEST.ofScode();
			case Sinstr.S_CONVERT ->     CONVERT.ofScode();
			case Sinstr.S_NEG ->	        NEG.ofScode();
			case Sinstr.S_ADD ->         ADD.ofScode();
			case Sinstr.S_SUB ->         SUB.ofScode();
			case Sinstr.S_MULT ->        MULT.ofScode();
			case Sinstr.S_DIV ->         DIV.ofScode();
			case Sinstr.S_REM ->         REM.ofScode();
			case Sinstr.S_NOT ->         NOT.ofScode();
			case Sinstr.S_AND ->         AND.ofScode();
			case Sinstr.S_OR ->          OR.ofScode();
			case Sinstr.S_XOR ->         XOR.ofScode();
			case Sinstr.S_EQV ->         EQV.ofScode();
			case Sinstr.S_IMP ->         IMP.ofScode();
			case Sinstr.S_LSHIFTL ->     SHIFT.ofScode(Sinstr.S_LSHIFTL); // Extension to S-Code: Left shift logical
			case Sinstr.S_LSHIFTA ->     SHIFT.ofScode(Sinstr.S_LSHIFTA); // Extension to S-Code: Left shift arithm.
			case Sinstr.S_RSHIFTL ->     SHIFT.ofScode(Sinstr.S_RSHIFTL); // Extension to S-Code: Right shift logical
			case Sinstr.S_RSHIFTA ->     SHIFT.ofScode(Sinstr.S_RSHIFTA); // Extension to S-Code: Right shift arithm.
			case Sinstr.S_LOCATE ->      LOCATE.ofScode();
			case Sinstr.S_INCO ->        INCO.ofScode();
			case Sinstr.S_DECO ->        DECO.ofScode();
			case Sinstr.S_DIST ->        DIST.ofScode();
			case Sinstr.S_COMPARE ->     COMPARE.ofScode();
			case Sinstr.S_DEREF ->       DEREF.ofScode();
			default -> { return false; }
		}
		return true;
	}
	

}
