/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.compileTimeStack.AddressItem;
import bec.compileTimeStack.CTStack;
import bec.descriptor.ConstDescr;
import bec.descriptor.Descriptor;
import bec.descriptor.Variable;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.util.Util;
import svm.value.ObjectAddress;

/// S-INSTRUCTION: PUSH.
/// <pre>
/// stack_instruction ::= push obj:tag | pushv obj:tag
///
/// * push( REF, obj.TYPE, obj.BASE, obj.OFFSET );
///
/// If instruction pushv: force TOS value.
/// </pre>
/// Obj must refer to a defined global, local or constant quantity, otherwise: error.
///
/// Observe that routine parameters and exit tags are local quantities.
///
/// A copy of the descriptor is pushed onto the stack.
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/PUSH.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class PUSH extends Instruction {

	/** Default Constructor */ public PUSH() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// @param instr which instruction
	public static void ofScode(int instr) {
		Tag tag = Tag.ofScode();
		Descriptor x = tag.getMeaning();
		if(x instanceof Variable var) {
				ObjectAddress oaddr = (ObjectAddress) var.address.copy();
				CTStack.push(new AddressItem(var.type,0,oaddr));
		} else if(x instanceof ConstDescr cns) {
				ObjectAddress oaddr = (ObjectAddress) cns.getAddress().copy();
				CTStack.push(new AddressItem(cns.type,0,oaddr));
		} else Util.IERR("");
        if(instr == Sinstr.S_PUSHV) FETCH.doFetch();
	}

}
