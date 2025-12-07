/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.instruction;

/// S-INSTRUCTION: EVAL.
/// <pre>
/// eval_instruction ::= eval
/// </pre>
/// NOTE: In this implementation  EVAL == NOOP
/// 
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/instruction/EVAL.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public abstract class EVAL extends Instruction {

	/** Default Constructor */ public EVAL() {} 

	/// NOTE: In this implementation  EVAL == NOOP
	public static void ofScode() {}

}
