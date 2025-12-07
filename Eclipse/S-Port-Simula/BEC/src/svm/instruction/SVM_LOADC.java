/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;

import bec.Global;
import bec.Option;
import bec.scode.Scode;
import bec.scode.Tag;
import bec.scode.Type;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.RTStack;
import svm.value.GeneralAddress;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;
import svm.value.RecordValue;
import svm.value.StringValue;
import svm.value.Value;

/// SVM-INSTRUCTION: LOAD typeTag value
/// 
///		Runtime Stack
///			... →
///			..., value1, value2, ... , value'size
///
/// The the values are loaded and pushed onto the Runtime stack.
/// The number of values are type dependent.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_LOADC.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_LOADC extends SVM_Instruction {
	
	/// The type tag
	private final int typeTag;
	
	/// The constant value
	private final Value value;

	/// Construct a new SVM_LOADC instruction
	/// @param type the type
	/// @param value the constant value
	public SVM_LOADC(Type type,Value value) {
		this.opcode = SVM_Instruction.iPUSHC;
		this.typeTag  = type.tag;
		this.value = value;
		if(value != null ) {
			if(value.type.tag != typeTag) {
				IO.println("NEW SVM_LOADC: value="+value+", type="+value.type);
				Util.IERR("INCONSISTENT: typeTag="+Scode.edTag(typeTag) + ", value'tag="+Scode.edTag(value.type.tag));
			}
		}
	}
	
	@Override
	public void execute() {
		switch(typeTag) {
			case Tag.TAG_BOOL, Tag.TAG_CHAR, Tag.TAG_INT, Tag.TAG_SINT, Tag.TAG_REAL, Tag.TAG_LREAL,
			     Tag.TAG_SIZE, Tag.TAG_AADDR, Tag.TAG_PADDR, Tag.TAG_RADDR:
					RTStack.push(value); break;
			case Tag.TAG_OADDR:
				
				ObjectAddress oaddr = (ObjectAddress) value;
				if(oaddr != null && oaddr.segID == null) {
					IO.println("SVM_LOADC.execute: OADDR: "+oaddr);
					RTStack.dumpRTStack("SVM_LOADC.execute: NOTE: ");
//					RTStack.callStack_TOP().dump("SVM_LOADC.execute: NOTE: ");
					Util.IERR("");
				}
				
				RTStack.push(value); break;
			case Tag.TAG_TEXT:
				Util.IERR("IMPOSSIBLE");
				break;
			case Tag.TAG_STRING:
				StringValue sval = (StringValue) value;
				IntegerValue lng = IntegerValue.of(Type.T_INT, sval.lng);
				RTStack.push(sval.addr);
				RTStack.push(null);
				RTStack.push(lng);
				break;
			case Tag.TAG_GADDR:
				if(value == null) {
					RTStack.push(null);
					RTStack.push(null);
				} else {
					GeneralAddress gaddr = (GeneralAddress) value;
					
					oaddr = gaddr.base;
					if(oaddr != null && oaddr.kind == ObjectAddress.REL_ADDR) {
						gaddr.base = oaddr.toStackAddress();
					}
					
					RTStack.push(gaddr.base);
					RTStack.push(IntegerValue.of(Type.T_INT, gaddr.ofst));
				} break;
			default:
				RecordValue rval = (RecordValue)value;
				for(int i=0;i<rval.attrValues.size();i++) {
					Value val = rval.attrValues.get(i);
					RTStack.push(val);				
				}
				break;
		}
		Global.PSC.ofst++;
	}
	
	@Override
	public String toString() {
		return "LOADC    " + Scode.edTag(typeTag) + " " + value;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Construct an SVM_LOADC instruction from the given input.
	/// @param inpt the input stream
	/// @throws IOException if IOException occur
	private SVM_LOADC(AttributeInputStream inpt) throws IOException {
		this.opcode = SVM_Instruction.iPUSHC;
		this.typeTag = inpt.readShort();
		boolean present = inpt.readBoolean();
		this.value = (! present)? null : Value.read(inpt);
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + this);
	}

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeShort(typeTag);
		if(value != null) {
			oupt.writeBoolean(true);
//			IO.println("SVM_LOADC.write: "+value);
			value.write(oupt);
		} else oupt.writeBoolean(false);
	}

	/// Reads an SVM_LOADC instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_LOADC instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		return new SVM_LOADC(inpt);
	}

}
