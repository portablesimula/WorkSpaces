/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.instruction.CALL;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;

/// SVM-INSTRUCTION
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_Instruction.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
@SuppressWarnings("unused")
public class SVM_Instruction {

	/** Default Constructor */ public SVM_Instruction() {} 

	/// The opcode
	public int opcode;

	/** SVM-Instruction */ public final static int iADD = 1;
	/** SVM-Instruction */ public final static int iAND = 2;
	/** SVM-Instruction */ public final static int iOR = 3;
	/** SVM-Instruction */ public final static int iCALL = 4;
	/** SVM-Instruction */ public final static int iCOMPARE = 5;
	/** SVM-Instruction */ public final static int iCONVERT = 6;
	/** SVM-Instruction */ public final static int iDIV = 7;
	/** SVM-Instruction */ public final static int iJUMP = 8;
	/** SVM-Instruction */ public final static int iJUMPIF = 9;
	/** SVM-Instruction */ public final static int iMULT = 10;
	/** SVM-Instruction */ public final static int iNEG = 11;
	/** SVM-Instruction */ public final static int iLOAD = 12;
	/** SVM-Instruction */ public final static int iPUSHC = 13;
	/** SVM-Instruction */ public final static int iRETURN = 14;
//	/** SVM-Instruction */ public final static int iSTORE2REG = 15;
	/** SVM-Instruction */ public final static int iSTORE = 16;
	/** SVM-Instruction */ public final static int iREM = 17;
	/** SVM-Instruction */ public final static int iSUB = 18;
	/** SVM-Instruction */ public final static int iSWITCH = 19;
	/** SVM-Instruction */ public final static int iCALLSYS = 20;
	/** SVM-Instruction */ public final static int iLINE = 21;
	/** SVM-Instruction */ public final static int iNOOP = 22;
	/** SVM-Instruction */ public final static int iNOT = 23;
	/** SVM-Instruction */ public final static int iGOTO = 24;
	/** SVM-Instruction */ public final static int iPUSHR = 25;
	/** SVM-Instruction */ public final static int iPRECALL = 26;
	/** SVM-Instruction */ public final static int iPOPK = 27;
	/** SVM-Instruction */ public final static int iENTER = 28;
//	/** SVM-Instruction */ public final static int iREFER_NOT_USED = 29;
	/** SVM-Instruction */ public final static int iADDREG = 30;
	/** SVM-Instruction */ public final static int iXOR = 31;
	/** SVM-Instruction */ public final static int iINITO = 32;
	/** SVM-Instruction */ public final static int iGETO = 33;
	/** SVM-Instruction */ public final static int iSETO = 34;
	/** SVM-Instruction */ public final static int iINCO = 35;
	/** SVM-Instruction */ public final static int iDECO = 36;
	/** SVM-Instruction */ public final static int iDIST = 37;
	/** SVM-Instruction */ public final static int iEQV = 38;
	/** SVM-Instruction */ public final static int iIMP = 39;
//	/** SVM-Instruction */ public final static int iDUPA = 40;
	/** SVM-Instruction */ public final static int iASSIGN = 41;
	/** SVM-Instruction */ public final static int iPUSHLEN = 42;
	/** SVM-Instruction */ public final static int iSAVE = 43;
	/** SVM-Instruction */ public final static int iRESTORE = 44;
	/** SVM-Instruction */ public final static int iSHIFT = 45;
	/** SVM-Instruction */ public final static int iDUP = 46;
	/** SVM-Instruction */ public final static int iLOADA = 47;
	
	/** SVM-Instruction */ public final static int iMax = 99;
	

	/// Execute this SVM instruction
	public void execute() {
		Util.IERR("Method execute need a redefinition in "+this.getClass().getSimpleName());
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Write this SVM instruction to the given output.
	/// @param oupt the output stream
	/// @throws IOException if IOException occur
	public void write(AttributeOutputStream oupt) throws IOException {
		Util.IERR("Method 'write' need a redefinition in " + this.getClass().getSimpleName());
	}

	/// Reads an SVM instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		Util.IERR("Method 'read' need a redefinition"); // in " + this.getClass().getSimpleName());
		return null;
	}
	
	/// Reads an SVM instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction readObject(AttributeInputStream inpt) throws IOException {
		int opcode = inpt.readUnsignedByte();
//		IO.println("SVM_Instruction.read: opcode="+edOpcode(opcode));
		switch(opcode) {
			case iADD:		return SVM_ADD.read(inpt);
			case iAND:		return SVM_AND.read(inpt);
			case iOR:		return SVM_OR.read(inpt);
			case iXOR:		return SVM_XOR.read(inpt);
			case iEQV:		return SVM_EQV.read(inpt);
			case iIMP:		return SVM_IMP.read(inpt);
			case iCALL:		return SVM_CALL.read(inpt);
			case iDUP:		return SVM_DUP.read(inpt);
			case iCOMPARE:	return SVM_COMPARE.read(inpt);
			case iCONVERT:	return SVM_CONVERT.read(inpt);
			case iDIV:		return SVM_DIV.read(inpt);
			case iJUMP:		return SVM_JUMP.read(inpt);
			case iJUMPIF:	return SVM_JUMPIF.read(inpt);
			case iMULT:		return SVM_MULT.read(inpt);
			case iNEG:		return SVM_NEG.read(inpt);
			case iLOAD:		return SVM_LOAD.read(inpt);
			case iPUSHC:	return SVM_LOADC.read(inpt);
//			case iPUSHR:	return DELETED_SVM_PUSHR.read(inpt);
			case iPUSHLEN:	return SVM_PUSHLEN.read(inpt);
			case iSAVE:		return SVM_SAVE.read(inpt);
			case iRESTORE:	return SVM_RESTORE.read(inpt);
			case iRETURN:	return SVM_RETURN.read(inpt);
//			case iSTORE2REG:	return DELETED_SVM_STORE2REG.read(inpt);
			case iSTORE:	return SVM_STORE.read(inpt);
			case iREM:		return SVM_REM.read(inpt);
			case iSUB:		return SVM_SUB.read(inpt);
			case iSWITCH:	return SVM_SWITCH.read(inpt);
			case iLINE:		return SVM_LINE.read(inpt);
			case iCALLSYS:	return SVM_CALL_SYS.read(inpt);
//			case iNOOP:		return SVM_NOOP.read(inpt);
			case iNOT:		return SVM_NOT.read(inpt);
			case iGOTO:		return SVM_GOTO.read(inpt);
			case iPRECALL:	return SVM_PRECALL.read(inpt);
			case iPOPK:		return SVM_POPK.read(inpt);
			case iENTER:	return SVM_ENTER.read(inpt);
//			case iREFER_NOT_USED:	return DELETED_SVM_REFER.read(inpt);
//			case iADDREG:	return DELETED_SVM_ADDREG.read(inpt);
			case iINITO:	return SVM_INITO.read(inpt);
			case iGETO:		return SVM_GETO.read(inpt);
			case iSETO:		return SVM_SETO.read(inpt);
			case iINCO:		return SVM_INCO.read(inpt);
			case iDECO:		return SVM_DECO.read(inpt);
			case iDIST:		return SVM_DIST.read(inpt);
//			case iDUPA:		return REMOVED_SVM_DUPA.read(inpt);
			case iASSIGN:	return SVM_ASSIGN.read(inpt);
			case iSHIFT:	return SVM_SHIFT.read(inpt);
			case iLOADA:	return SVM_LOADA.read(inpt);
			
			default: Util.IERR("MISSING: " + edOpcode(opcode));
		}
		return null;
	}
	
	/// Edit the given 'opcode'
	/// @param opcode the opcode
	/// @return the edited string
	public static String edOpcode(int opcode) {
		switch(opcode) {
			case iADD:		return "iADD";
			case iAND:		return "iAND";
			case iOR:		return "iOR";
			case iXOR:		return "iXOR";
			case iEQV:		return "iEQV";
			case iIMP:		return "iIMP";
			case iCALL:		return "iCALL";
			case iDUP:		return "iDUP";
			case iCOMPARE:	return "iCOMPARE";
			case iCONVERT:	return "iCONVERT";
			case iDIV:		return "iDIV";
			case iJUMP:		return "iJUMP";
			case iJUMPIF:	return "iJUMPIF";
			case iMULT:		return "iMULT";
			case iNEG:		return "iNEG";
			case iLOAD:		return "iLOAD";
			case iPUSHC:	return "iPUSHC";
			case iPUSHR:	return "iPUSHR";
			case iPUSHLEN:	return "iPUSHLEN";
			case iSAVE:		return "iSAVE";
			case iRESTORE:	return "iRESTORE";
			case iRETURN:	return "iRETURN";
//			case iSTORE2REG:	return "iSTORE2REG";
			case iSTORE:	return "iSTORE";
			case iREM:	 	return "iSTOREC";
			case iSUB:		return "iSUB";
			case iSWITCH:	return "iSWITCH";
			case iCALLSYS:	return "iCALLSYS";
			case iLINE:		return "iNOOP";
			case iNOOP:		return "iNOOP";
			case iNOT:		return "iNOT";
			case iGOTO:		return "iGOTO";
			case iPRECALL:	return "iPRECALL";
			case iPOPK:		return "iPOPK";
			case iENTER:	return "iENTER";
//			case iREFER_NOT_USED:	return "iREFER_NOT_USED";
			case iADDREG:	return "iADDREG";
			case iINITO:	return "iINITO";
			case iGETO:		return "iGETO";
			case iSETO:		return "iSETO";
			case iINCO:		return "iINCO";
			case iDECO:		return "iDECO";
			case iDIST:		return "iDIST";
//			case iDUPA:		return "iDUPA";
			case iASSIGN:	return "iASSIGN";
			case iSHIFT:	return "iSHIFT";
			case iLOADA:	return "iLOADA";

			default:		return "UNKNOWN:" + opcode;
		}
	}

}
