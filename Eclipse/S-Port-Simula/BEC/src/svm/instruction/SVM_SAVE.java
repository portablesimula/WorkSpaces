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
import bec.util.Util;
import svm.RTStack;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;
import svm.value.Value;

/// SVM-INSTRUCTION: SAVE
/// 
///		Runtime Stack
///			value1, value2, ... , value'size, oaddr →
///			- empty
///
/// The oaddr of a save-object is popped off the Runtime stack.
/// Then the complete Runtime stack is saved within the save-object.
///
/// See: SVM_PUSHLEN and SVM_RERSTORE
/// See also S-Port - Definition of S-code - sect. 7. INTERMEDIATE RESULTS.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_SAVE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_SAVE extends SVM_Instruction {

//	private static final boolean DEBUG = false;

	/// Construct a new SVM_RETURN instruction
	public SVM_SAVE() {
		this.opcode = SVM_Instruction.iSAVE;
	}
	
	@Override
	public void execute() {
		saveStack();
//		Util.IERR("");
		Global.PSC.ofst++;
	}
//	private static Stack<Stack<Value>> saveStack = new Stack<Stack<Value>>();
	
//	 Visible record entity;  info "DYNAMIC";
//	 begin ref(inst)                sl;   -- during GC used as GCL!!!!
//	       range(0:MAX_BYT)         sort; -- S_SAV =  9 for Save Object
//	       range(0:MAX_BYT)         misc;
//	       variant ref(ptp)         pp;   -- used for instances
//	       variant range(0:MAX_TXT) ncha; -- used for text entities
//	       variant size             lng;  -- used for other entities
//	 end;
//
//	 Visible record inst:entity;
//	 begin ref(entity)              gcl;
//	       variant ref(inst)        dl;
//	               label            lsc;
//	       variant entry(Pmovit)    moveIt;
//	 end;
//	 Visible record savent:inst;
//	 begin  end;
	
	/// The head size of the SAVENT instance
	public final static int saveEntityHead = 7;
	
	/// The offset of the 'lng' attribute in the SAVENT instance
	public final static int sizeOffset = 3;
	
	/// Save the Runtime stack in the SAVENT instance
	private static void saveStack() {
		ObjectAddress savePos = RTStack.popOADDR();
		if(savePos == null) {
		} else {
			ObjectAddress saveObj = savePos.addOffset(-saveEntityHead);
			IntegerValue entitySize = (IntegerValue) saveObj.addOffset(sizeOffset).load(0);
			int size = entitySize.value - saveEntityHead;
			if(size != RTStack.size()) Util.IERR("");
			
			for(int i=0;i<size;i++) {
				Value item = RTStack.pop();
//				if(DEBUG) {
//					IO.println("RTStack.saveStack:    SAVE-RESTORE " + item + " ===> saveObj("+(saveEntityHead + i)+")");
//				}
				saveObj.store(saveEntityHead + i, item);
			}
//			if(DEBUG) {
//				RTUtil.dumpEntity(saveObj);
//			}
		}
	}
	
	@Override
	public String toString() {
		return "SAVE     ";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_SAVE instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_SAVE instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_SAVE instr = new SVM_SAVE();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
