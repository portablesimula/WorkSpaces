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
import svm.RTUtil;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;
import svm.value.Value;

/// SVM-INSTRUCTION: INITO
/// 
/// 	Runtime Stack
/// 	   ..., oaddr →
/// 	   ...
///
/// The oaddr of a save-object is popped off the Runtime stack.
/// It points to the first value after the Object head, and is
/// reduced and saved in the SAVE-OBJECT pointer.
///
///
/// 
///          SAVE-OBJECT ---------->.=====================.
///                                 |                     |
///                                 |     Object head     |
///                                 |                     |
///          TOS oaddr ------------>|=====================|
///                                 |                     |
///                NEXT-------------|->.================. |
///                                 |  |     oaddr      | |
///                                 |  '================' |
///                                 |                     |
///                                 '====================='
///
/// See: SVM_GETO and SVM_SETO.
/// See also S-Port - Definition of S-code - sect. 7. INTERMEDIATE RESULTS.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_INITO.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_INITO extends SVM_Instruction {
	
	/// The object address to the SAVE_OBJECT
	private static ObjectAddress SAVE_OBJECT;
	
	/// The SAVE_INDEX so far
	private static int SAVE_INDEX;
	
	/// The SAVE_LENGTH
	private static int SAVE_LENGTH;
	
//	private static final boolean DEBUG = false;

	/// Construct a new SVM_INITO instruction
	public SVM_INITO() {
		this.opcode = SVM_Instruction.iINITO;
	}
	
//	 Visible record entity;  info "DYNAMIC";
//	 begin ref(inst)                sl;   -- during GC used as GCL!!!!
//	       range(0:MAX_BYT)         sort;
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

	/// Used by SVM_GETO
	/// @return the object address saved according to SAVE_INDEX
	static ObjectAddress get() {
		while(SAVE_INDEX < SAVE_LENGTH) {
			Value value = SAVE_OBJECT.addOffset(SAVE_INDEX++).load(0);
//			if(DEBUG) IO.println("SVM_INITO.get: SAVE_OBJECT["+(SAVE_INDEX-1)+"] = "+value);
			if(value instanceof ObjectAddress oaddr) return oaddr;
		}
//		if(DEBUG) IO.println("SVM_INITO.get: FINISHED !");
		return null;
	}
	
	/// Used by SVM_SETO
	/// @param oaddr the object address to save according to SAVE_INDEX
	static void set(ObjectAddress oaddr) {
//		if(DEBUG) IO.println("SVM_INITO.set: SAVE_OBJECT["+(SAVE_INDEX-1)+"] = "+oaddr);
		SAVE_OBJECT.store(SAVE_INDEX - 1, oaddr);
	}

	@Override
	public void execute() {
		try {
			SAVE_OBJECT = RTStack.popOADDR().addOffset(-7); // 7 == Size of Object Head
			SAVE_INDEX = 0+6;
//			if(DEBUG) {
//				IO.println("SVM_INITO: " + SAVE_OBJECT.getClass().getSimpleName() + " " + SAVE_OBJECT);
//				RTUtil.dumpEntity(SAVE_OBJECT);
//			}
			IntegerValue sort = (IntegerValue) SAVE_OBJECT.load(1);
			if(sort.value != RTUtil.S_SAV) Util.IERR("NOT A SAVE OBJECT");
			IntegerValue lng = (IntegerValue) SAVE_OBJECT.load(3);
//			if(DEBUG) IO.println("SVM_INITO: sort="+sort+", lng="+lng);
			SAVE_LENGTH = lng.value;
		} catch(Exception e) {
			IO.println("SVM_INITO: FAILED: " + e);
			e.printStackTrace();
		}
		
		Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "INITO    ";
	}
	
	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
	}

	/// Reads an SVM_INITO instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_INITO instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_INITO instr = new SVM_INITO();
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
