/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.statement;

import bec.Global;
import bec.S_Module;
import bec.compileTimeStack.CTStack;
import bec.instruction.FETCH;
import bec.instruction.Instruction;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.scode.Type;
import bec.util.Util;
import svm.instruction.SVM_RESTORE;
import svm.instruction.SVM_SAVE;

/// S-INSTRUCTION: SAVE - RESTORE.
/// <pre>
/// protect_statement ::= save < program_element >* restore
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/statement/ProtectConstruction.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class ProtectConstruction {

	/** Default Constructor */ public ProtectConstruction() {} 

	/// Scans the remaining S-Code belonging to this statement.
	/// Perform the specified operations (which may result in code generation).
	public static void ofStatement() {
		doSAVE();
			Scode.inputInstr();
			S_Module.programElements();
			if(Scode.curinstr != Sinstr.S_RESTORE)
				Util.IERR("Improper termination of protect-construction");
		doRESTORE();
	}
	
	/// doSAVE.
	/// <pre>
	/// save
	/// * force TOS value; check TOS type(OADDR);
	/// * pop;
	/// * remember stack;
	/// * purge stack;
	/// </pre>
	/// TOS describes the address of a save-object. The size of this object is as determined by the
	/// preceding pushlen. The complete state of the stack is remembered (together with the values of
	/// ALLOCATED and MARKS) and the compilation continues with an empty stack.
	///
	/// Code is generated, which - if TOS.VALUE <> onone (see note below) - at run time will save the
	/// used part of the temporary area, and set the SAVE-MARKS attribute.
	///
	/// TOS is popped.
	private static void doSAVE() {
		CTStack.checkTosType(Type.T_OADDR);
		
		FETCH.doFetch();
		CTStack.pop();
		CTStack.SAVE("SAVE");
		
		Global.PSEG.emit(new SVM_SAVE());
	}
	
	/// doRESTORE.
	/// <pre>
	/// restore
	/// * check TOS ref; check TOS type(OADDR);
	/// * push(onone); perform assign;
	/// * check stack empty;
	/// * reestablish stack remembered at corresponding save;
	/// </pre>
	///
	/// The stack remembered by the corresponding save is reestablished (together with the attributes
	/// ALLOCATED and MARKS).
	///
	/// Code is generated, which - if TOS.VALUE <> onone (see note below) - at run time will copy the
	/// content of the specified save-object into the temporary area (the save-object will be the one
	/// generated at the corresponding save). After the copy has been taken, onone is assigned to what is
	/// referred by TOS.
	///
	/// TOS is popped.
	private static void doRESTORE() {
		CTStack.checkTosRef(); CTStack.checkTosType(Type.T_OADDR);
		FETCH.doFetch();
		CTStack.pop();
		CTStack.checkStackEmpty();
		CTStack.RESTORE();
		
		Global.PSEG.emit(new SVM_RESTORE());
	}

	/// Scans the remaining S-Code belonging to this instruction.
	/// Perform the specified operations (which may result in code generation).
	public static void ofInstruction() {
		doSAVE();
			do Scode.inputInstr(); while(Instruction.inInstruction());
			if(Scode.curinstr != Sinstr.S_RESTORE)
				Util.IERR("Improper termination of protect-construction");
		doRESTORE();
	}
	
	@Override
	public String toString() {
		return "SAVE "; // + lab;
	}

}
