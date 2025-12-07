/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.statement;

import bec.Global;
import bec.S_Module;
import bec.compileTimeStack.CTStack;
import bec.compileTimeStack.CTStackItem;
import bec.instruction.FETCH;
import bec.scode.Relation;
import bec.scode.Scode;
import bec.scode.Sinstr;
import bec.util.NamedStack;
import bec.util.Util;
import svm.instruction.SVM_JUMP;
import svm.instruction.SVM_JUMPIF;
import svm.value.ProgramAddress;

/// S-INSTRUCTION: SKIPIF.
/// <pre>
/// skip_statement ::= skipif relation < program_element >* endskip
/// 
/// skip_instruction ::= skipif relation < instruction >* endskip
/// 
///		relation ::= ?lt | ?le | ?eq | ?ge | ?gt | ?ne
/// </pre>
/// The skip_statement is intended to be used where a transfer of control is to be generated without altering
/// the state of the stack, commonly to report error conditions during expression evaluation. The skip
/// instruction is the form the statement takes inside routine bodies.
/// 
/// <pre>
/// skipif relation
/// * force TOS value; force SOS value;
/// * check relation;
/// * pop; pop;
/// * save skip-stack;
/// </pre>
///
/// The generated code will compute the relation, and control is transferred to an "end-label" (to be defined
/// later), if the relation is true. A copy of the complete state of the S- compiler`s stack is saved as the
/// "skip-stack".
/// 
///<pre>
/// endskip
/// * check stack empty; restore skip-stack;
/// </pre>
///
/// The "end-label" is located at the current program point, and the "skip-stack" is restored as the current
/// stack.
///
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/statement/SkipifConstruction.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class SkipifConstruction {

	/** Default Constructor */ public SkipifConstruction() {} 

	/// Treat a complete Skipif Statement.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTypesEqual(); CTStack.checkSosValue();
		Relation relation = Relation.ofScode();
		CTStackItem tos = CTStack.pop();
		CTStack.pop();
		
		Global.ifDepth++;
		NamedStack<CTStackItem> SKIP_Stack = CTStack.copy("IF-Stack-Copy-"+Global.ifDepth);
		ProgramAddress END_LABEL = Global.PSEG.nextAddress();
		Global.PSEG.emit(new SVM_JUMPIF(relation, tos.type.size(), null));
		
		Scode.inputInstr();
		S_Module.programElements();

		if(Scode.curinstr != Sinstr.S_ENDSKIP) Util.IERR("Missing ENDSKIP: " + Sinstr.edInstr(Scode.curinstr));
		
		// * endskip
		// * check stack empty; restore skip-stack;
		CTStack.reestablish(SKIP_Stack);
		
		// FIXUP:
		SVM_JUMP instr = (SVM_JUMP) Global.PSEG.instructions.get(END_LABEL.ofst);
		instr.setDestination(Global.PSEG.nextAddress());
//     	Global.PSEG.emit(new SVM_NOOP());			
		Global.ifDepth--;
	}
}
