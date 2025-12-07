/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.compileTimeStack.CTStack;
import bec.scode.Scode;

/// S-INSTRUCTION: POPALL.
/// <pre>
/// popall N:byte
///  - perform pop n times;
///  - check stack empty;
/// </pre>
/// Pop N items off the stack. The stack should then be empty, otherwise: error.
///
/// This instruction gives a short way of emptying the stack, together with the control of the
/// number of elements that was on the stack.
///
/// Profiles cannot be deleted from the stack by pop, only by deleting the complete stack through popall.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/POPALL.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class POPALL extends Instruction {
	
	/** Default Constructor */ public POPALL() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations.
	public static void ofScode() {
		int n = Scode.inByte();
		
		for (int i = 0; i < n; i++)
			CTStack.pop();
		CTStack.checkStackEmpty();
	}

}
