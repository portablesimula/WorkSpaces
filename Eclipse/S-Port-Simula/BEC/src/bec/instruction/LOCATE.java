/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.Global;
import bec.compileTimeStack.CTStack;
import bec.scode.Type;
import svm.instruction.SVM_ADD;

/// S-INSTRUCTION: LOCATE.
/// <pre>
/// addressing_instruction ::= locate (dyadic)
///
/// force TOS value; check TOS type(AADDR);
/// force SOS value; check SOS type(OADDR,GADDR);
/// pop; pop;
/// push( VAL, GADDR, "value(SOS).BASE, value(SOS).OFFSET++value(TOS)" );
/// </pre>
/// SOS and TOS are replaced by a description of the general address value
/// formed by "addition" of the two original addresses.
/// <pre>
///                               .===========================.
///                               |                           |
///                               |                           |
///                               |                           |
///                               |                           |
///      (SOS) -------------------|-------->.=============.   |
///                               |         |   |         |   |
///                               |         |   | (TOS)   |   |
///                               |         |   V         |   |
///    The resulting              |         |   .=====,   |   |
///         TOS ------------------|---------|-->| : : |   |   |
///    after locate               |         |   '====='   |   |
///                               |         |             |   |
///                               |         |             |   |
///                               |         '============='   |
///                               |                           |
///                               |                           |
///                               |                           |
///                               '==========================='
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/LOCATE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class LOCATE extends Instruction {
	
	/** Default Constructor */ public LOCATE() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// Finally: If TOS.type = GADDR then emit an SVM_ADD instruction.
	public static void ofScode() {
		FETCH.doFetch(); CTStack.checkTosType(Type.T_AADDR); CTStack.checkSosValue();
		Type sosType = CTStack.checkSosType2(Type.T_OADDR,Type.T_GADDR);
		CTStack.pop(); CTStack.pop();
		CTStack.pushTempItem(Type.T_GADDR);
		
		if(sosType == Type.T_GADDR)
			Global.PSEG.emit(new SVM_ADD());
	}

}
