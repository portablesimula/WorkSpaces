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

/// SVM-INSTRUCTION: LINE lineType lineNumber
/// 
/// 	Runtime Stack
/// 	   ... →
/// 	   ...
///
/// The Global variable sourceLineNumber := sourceLine;
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_LINE.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_LINE extends SVM_Instruction {
	
	/// The instruction type: 0, DCL, STM
	private final int type; // 0, DCL, STM
	
	/// The sourceLine number
	private final int sourceLine;

	/// Construct a new SVM_LINE instruction
	/// @param type the instruction type: 0, DCL, STM
	/// @param sourceLine the sourceLine number
	public SVM_LINE(int type, int sourceLine) {
		this.type = type;
		this.sourceLine = sourceLine;
	}

	@Override
	public void execute() {
		Global.sourceLineNumber = sourceLine;
		Global.PSC.ofst++;
	}
	
	@Override	
	public String toString() {
		return "LINE     " + sourceLine;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(iLINE);
		oupt.writeByte(type+1);
		oupt.writeShort(sourceLine);
	}

	/// Reads an SVM_LINE instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_LINE instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		int type = inpt.readUnsignedByte() - 1;
		int sourceline = inpt.readShort();
		SVM_LINE instr = new SVM_LINE(type, sourceline);
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
