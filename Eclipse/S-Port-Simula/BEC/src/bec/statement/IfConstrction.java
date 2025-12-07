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

/// S-INSTRUCTION: IF.
/// <pre>
///	if_statement
///		::= if relation < program_element >* else_part
///
///		relation ::= ?lt | ?le | ?eq | ?ge | ?gt | ?ne
///
///		else_part
///			::= else < program_element >* endif
///			::= endif
///
///	if_instruction
///		::= if relation < instruction >* i_else_part
///
///
///		i_else_part
///			::= else < instruction >* endif
///			::= endif
/// </pre>
///
/// <pre>
/// if relation
///   * force TOS value; force SOS value;
///   * check relation;
///   * pop; pop;
///   * remember stack as "if-stack";
/// </pre>
/// The generated code will compute the value of the relation, and transfer control to an "else-label" (to be
/// defined later) if the relation is false. A copy of the complete state of the S-compiler's stack is saved as
/// the "if-stack".
///
/// <pre>
/// else
///   * force TOS value;
///   * remember stack as "else-stack";
///   * reestablish stack saved as "if-stack";
/// </pre>
///	An unconditional forward branch is generated to an "end-label" (to be defined later). A copy is made of
///	the complete state of the stack and this is saved as the "else-stack", then the stack is restored to the state
///	saved as the "if-stack". Finally the "else-label" (used by if) is located at the current program point.
///
/// <pre>
/// endif
///   * force TOS value;
///   * merge current stack with "else-stack" if it exists, otherwise "if-stack";
/// </pre>
/// The current stack and the saved stack are merged. The saved stack will be the "if-stack" if no else-part
/// has been processed, otherwise it will be the "else-stack". The merge takes each corresponding pair of
/// stack items and forces them to be identical by applying fetch operations when necessary - this process
/// will generally involve inserting code sequences into the if-part and the else-part. It is an error if the two
/// stacks do not contain the same number of elements or if any pair of stack items cannot be made
/// identical. After the merge the saved stack is deleted.
/// If no else-part was processed the "else-label", otherwise the "end-label", is located at the current
/// program point.
///
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/statement/IfConstrction.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class IfConstrction {

	/** Default Constructor */ public IfConstrction() {} 

//	private static final boolean DEBUG = false;

	/// Treat a complete If Statement.
	public static void ofScode() {
		// if relation
		// * force TOS value; force SOS value;
		// * check relation;
		// * pop; pop;
		// * remember stack as "if-stack";
		//
		// The generated code will compute the value of the relation, and transfer control to an "else-label" (to be
		// defined later) if the relation is false. A copy of the complete state of the S-compiler's stack is saved as
		// the "if-stack".
		
		FETCH.doFetch();
		CTStack.checkTypesEqual();
		CTStack.checkSosValue();
		Relation relation = Relation.ofScode();
		Global.ifDepth++;
		CTStackItem tos = CTStack.pop();
		CTStack.pop();
		NamedStack<CTStackItem> IF_Stack = CTStack.copy("IF-Stack-Copy-"+Global.ifDepth);
		String indent = "";
//		if(DEBUG) {
//			for(int i=0;i<Global.ifDepth;i++) indent = indent + "      ";
//			IF_Stack.dumpStack(Global.ifDepth, "IF: SAVE IF_Stack");
//		}
		
		ProgramAddress IF_LABEL = Global.PSEG.nextAddress();
		ProgramAddress ELSE_LABEL = null;
		Global.PSEG.emit(new SVM_JUMPIF(relation.not(), tos.type.size(), null));
		
		NamedStack<CTStackItem> ELSE_Stack = null;
		
		Scode.inputInstr();
		S_Module.programElements();
		if(Scode.curinstr == Sinstr.S_ELSE) {
			// else
			// 	* force TOS value;
			// 	* remember stack as "else-stack";
			// 	* reestablish stack saved as "if-stack";
			//
			// 	An unconditional forward branch is generated to an "end-label" (to be defined later). A copy is made of
			// 	the complete state of the stack and this is saved as the "else-stack", then the stack is restored to the state
			// 	saved as the "if-stack". Finally the "else-label" (used by if) is located at the current program point.
			FETCH.doFetch();
			ELSE_Stack = CTStack.copy("ELSE-Stack-Copy-"+Global.ifDepth);
			CTStack.reestablish(IF_Stack);
//			if(DEBUG) {
//				ELSE_Stack.dumpStack(Global.ifDepth, "ELSE: SAVE ELSE_Stack");
//				IF_Stack.dumpStack(Global.ifDepth, "ELSE: reestablish IF_Stack");
//			}
			
			ELSE_LABEL = Global.PSEG.nextAddress();
			Global.PSEG.emit(new SVM_JUMP(null));
			
			// FIXUP:
			SVM_JUMP instr = (SVM_JUMP) Global.PSEG.instructions.get(IF_LABEL.ofst);
			instr.setDestination(Global.PSEG.nextAddress());
//	      	Global.PSEG.emit(new SVM_NOOP());

			Scode.inputInstr();
			S_Module.programElements();
		}
		if(Scode.curinstr != Sinstr.S_ENDIF) Util.IERR("Missing ENDIF: " + Sinstr.edInstr(Scode.curinstr));
		// endif
		// * force TOS value;
		// * merge current stack with "else-stack" if it exists, otherwise "if-stack";
		//
		// The current stack and the saved stack are merged. The saved stack will be the "if-stack" if no else-part
		// has been processed, otherwise it will be the "else-stack". The merge takes each corresponding pair of
		// stack items and forces them to be identical by applying fetch operations when necessary - this process
		// will generally involve inserting code sequences into the if-part and the else-part. It is an error if the two
		// stacks do not contain the same number of elements or if any pair of stack items cannot be made
		// identical. After the merge the saved stack is deleted.
		// If no else-part was processed the "else-label", otherwise the "end-label", is located at the current
		// program point.
		FETCH.doFetch();
		if(ELSE_LABEL != null) {
			// FIXUP:
			SVM_JUMP instr = (SVM_JUMP) Global.PSEG.instructions.get(ELSE_LABEL.ofst);
			instr.setDestination(Global.PSEG.nextAddress());
//	      	Global.PSEG.emit(new SVM_NOOP());		
		} else {
			// FIXUP:
			SVM_JUMP instr = (SVM_JUMP) Global.PSEG.instructions.get(IF_LABEL.ofst);
			instr.setDestination(Global.PSEG.nextAddress());
//	      	Global.PSEG.emit(new SVM_NOOP());			
		}
		
		if(ELSE_Stack != null) {
//			if(DEBUG) {
//				ELSE_Stack.dumpStack(Global.ifDepth, "ENDIF: resulting ELSE_Stack");
//				CTStack.current().dumpStack(Global.ifDepth, "ENDIF: current Stack");
//			}
			if(! CTStack.equals(CTStack.current(), ELSE_Stack)) {
				Util.IERR("Merge IF-Stack and ELSE-Stack FAILED !");
			}
		}
		
		Global.ifDepth--;
	
	}

}
