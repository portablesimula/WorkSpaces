/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.scode.Relation;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import svm.RTStack;
import svm.value.BooleanValue;
import svm.value.Value;

/// SVM-INSTRUCTION: COMPARE relation
/// 
///	relation ::= ?lt | ?le | ?eq | ?ge | ?gt | ?ne
/// 
/// 	Runtime Stack
/// 	   ..., sos, tos →
/// 	   ..., result
///
/// The 'tos' and 'sos' are popped off the Runtime stack.
/// <br>The 'result' is calculated as result = sos relation tos.
/// <br>Then the boolean 'result' is pushed onto the Runtime Stack.
/// 
/// 'tos' and 'sos' must be of the same arithmetic type, i.e. int, float or double,
/// <br>or boolean, in which case: relation ::= ?eq | ?ne
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_COMPARE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_COMPARE extends SVM_Instruction {
	
	/// The relation
	private final Relation relation;
	
	/// Construct a new SVM_CALL_COMPARE instruction
	/// @param relation the relation
	public SVM_COMPARE(Relation relation) {
		this.opcode = SVM_Instruction.iCOMPARE;
		this.relation = relation;
	}

	@Override
	public void execute() {
		Value tos = RTStack.pop();
		Value sos = RTStack.pop();
		boolean res = relation.compare(sos, tos);
		RTStack.push(BooleanValue.of(res));
		Global.PSC.ofst++;
	}
	
	public String toString() {
		return "COMPARE  " + relation;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		relation.write(oupt);
	}

	/// Reads an SVM_COMPARE instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_COMPERE instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_COMPARE instr = new SVM_COMPARE(Relation.read(inpt));
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}
}
