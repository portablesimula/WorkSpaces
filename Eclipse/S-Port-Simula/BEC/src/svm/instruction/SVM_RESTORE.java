/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import svm.RTStack;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;
import svm.value.Value;

/// SVM-INSTRUCTION: SAVE
/// 
///		Runtime Stack
///			oaddr →
///			value1, value2, ... , value'size  
///
/// The oaddr of a save-object is popped off the Runtime stack.
/// Then the complete Runtime stack is restored from the save-object.
///
/// See: SVM_PUSHLEN and SVM_RERSTORE
/// See also S-Port - Definition of S-code - sect. 7. INTERMEDIATE RESULTS.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_RESTORE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_RESTORE extends SVM_Instruction {

//	private static final boolean DEBUG = false;

	/// Construct a new SVM_RESTORE instruction
	public SVM_RESTORE() {
		this.opcode = SVM_Instruction.iRESTORE;
	}
	
	@Override
	public void execute() {
		ObjectAddress savePos = RTStack.popOADDR();
		if(savePos != null)	restoreStack(savePos);
		Global.PSC.ofst++;
	}
	
	/// Restore the Runtime stack from the SAVENT instance
	/// @param savePos the object address to the saved stack within a SAVENT instance
	private static void restoreStack(ObjectAddress savePos) {
		IntegerValue entitySize = (IntegerValue) savePos.addOffset(SVM_SAVE.sizeOffset - SVM_SAVE.saveEntityHead).load(0);
		int size = entitySize.value - SVM_SAVE.saveEntityHead;
		
//		if(DEBUG) {
//			IO.println("RTStack.restoreStack: BEGIN RESTORE ++++++++++++++++++++++++++++++++++++++++");
//			ObjectAddress saveObj = savePos.addOffset(-SVM_SAVE.saveEntityHead);
//			RTUtil.dumpEntity(saveObj);
//			IO.println("RTStack.restoreStack: RESTORE  entitySize = " + entitySize);
//			IO.println("RTStack.restoreStack: RESTORE  Size = " + size);
//		}

		for(int i=size-1;i>=0;i--) {
			Value item = savePos.load(i);
//			if(DEBUG) {
//				IO.println("RTStack.saveStack:    SAVE-RESTORE " + item + " <=== saveObj("+(SVM_SAVE.saveEntityHead + i)+")");
//			}
			RTStack.push(item);
		}

//		if(DEBUG) {
//			RTStack.dumpRTStack("RTStack.restoreStack: ");
//			Util.IERR("");
//		}
	}
	
	@Override
	public String toString() {
		return "RESTORE  ";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_RESTORE instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_RESTORE instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_RESTORE instr = new SVM_RESTORE();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
