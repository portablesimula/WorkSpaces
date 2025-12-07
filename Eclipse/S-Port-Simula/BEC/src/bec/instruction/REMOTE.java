/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

import bec.compileTimeStack.AddressItem;
import bec.compileTimeStack.CTStack;
import bec.descriptor.Attribute;
import bec.scode.Sinstr;
import bec.scode.Tag;
import bec.scode.Type;
import svm.value.ObjectAddress;

/// S-INSTRUCTION: REMOTE.
/// <pre>
///  addressing_instruction ::= remote attribute:tag | remotev attribute:tag
///  
///  remote attr:tag
///  force TOS value; check TOS type(OADDR);
///  pop;
///  push( REF, attr.TYPE, BASE = value(TOS), OFFSET = attr.OFFSET" );
///  
///  If instruction remotev: force TOS value.
/// </pre>
///  This instruction uses one step of indirection. The value is considered to be the address of an
///  object of the type 'REC' in which attr is an attribute. TOS is replaced by a descriptor of the
///  designated component of that object. Note again that no qualification check is implied (neither
///  could it be done).
///  <pre>
///          (TOS) -------- > OADDR VALUE ---------->.=====================.
///                                                  |-----.
///                                                  |     |               |
///                                                  |     | attribute     |
///                                                  |     |  OFFSET       |
///          The resulting           REF             |     V               |
///                TOS ------------------------------|----->.======.       |
///           after remote                           |      | attr |       |
///                                                  |      '======'       |
///                                                  |                     |
///                                                  '====================='
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/REMOTE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class REMOTE extends Instruction {
	
	/** Default Constructor */ public REMOTE() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// @param instr which instruction
	public static void ofScode(int instr) {
		Tag tag = Tag.ofScode();
		Attribute attr = (Attribute) tag.getMeaning();
		
		FETCH.doFetch();			
		CTStack.checkTosType(Type.T_OADDR);
		CTStack.pop();
		
		ObjectAddress memAddr = ObjectAddress.ofRemoteAddr();
		AddressItem adr = new AddressItem(attr.type, attr.rela, memAddr);
		CTStack.push(adr);
		
        if(instr == Sinstr.S_REMOTEV) {
        	FETCH.doFetch();
        }
	}

}
