/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.compileTimeStack.CTStack;
import bec.descriptor.Display;
import bec.scode.Tag;

/// S-INSTRUCTION: DELETE.
/// <pre>
/// delete_statement ::= delete from:tag
/// 
/// check stacks empty;
/// </pre>
///
/// All tags defined with values greater than or equal to from:tag are made undefined, i.e. the
/// corresponding descriptors may be released. The tags become available for reuse. The stack and all
/// saved stacks must be empty, otherwise: error.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/DELETE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class DELETE extends Instruction {

	/** Default Constructor */ public DELETE() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: Delete the specified tags.
	public static void ofScode() {
		Tag tag = Tag.ofScode();
		CTStack.checkStackEmpty();
		Display.delete(tag.val);
	}

}
