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

/// S-INSTRUCTION: SELECT.
/// <pre>
///  addressing_instruction ::= select attribute:tag | selectv attribute:tag
///  
///  check TOS ref;
///  TOS.TYPE := attr.TYPE;
///  "TOS.OFFSET := TOS.OFFSET ++ attr.OFFSET";
///  
///  (note that the BASE component of TOS is unchanged)
///
///  If instruction selectv: force TOS value.
/// </pre>
///  The area described by TOS is considered to be holding a record of the type, say 'REC', in which
///  the instruction argument attr is an attribute. TOS is modified to describe the designated
///  component of that record. Note that no qualification check is implied, i.e. TOS.TYPE may be
///  different from 'REC'.
///<pre>
///  
///        BASE ---------------------------> .============================.
///                                          |-----.                      |
///                                          |     |                      |
///                                          |     | TOS.OFFSET           |
///                                          |     |                      |
///                       REF                |     V                      |
///        (TOS) ----------------------------|---> .==================.   |
///                                          |     |----              |   |
///                                          |     |    |             |   |
///                                          |     |    | attr.OFFSET |   |
///     The resulting         REF            |     |    V             |   |
///           TOS ---------------------------|-----|--> .======.      |   |
///      after select                        |     |    | attr |      |   |
///                                          |     |    .======.      |   |
///                                          |     |                  |   |
///                                          |     .==================.   |
///                                          |                            |
///                                          .============================.
/// </pre>
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/SELECT.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class SELECT extends Instruction {

	/** Default Constructor */ public SELECT() {} 

	/// Scans the remaining S-Code (if any) belonging to this instruction.
	/// Perform the specified stack operations (which may result in code generation).
	/// @param instr which instruction
	public static void ofScode(int instr) {
		Tag tag = Tag.ofScode();
		CTStack.checkTosRef();
		Attribute attr = (Attribute) tag.getMeaning();
		CTStack.TOS().type = attr.type;
		AddressItem adr = (AddressItem) CTStack.TOS();
		adr.offset = adr.offset + attr.rela;
		adr.type = attr.type;
		if(instr == Sinstr.S_SELECTV) FETCH.doFetch();
	}

}
