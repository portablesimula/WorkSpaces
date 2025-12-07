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
import svm.value.LongRealValue;
import svm.value.ObjectAddress;
import svm.value.RealValue;
import svm.value.Value;

/// SVM-INSTRUCTION: CONVERT fromType toType
/// 
/// 	Runtime Stack
/// 	   ..., tos →
/// 	   ..., result
///
/// The 'tos' is popped off the Runtime stack.
/// <br>The 'result' is calculated by converting tos 'toType'.
/// <br>Then the 'result' is pushed onto the Runtime Stack.
/// 
/// Not all conversions are valid, see the table below.
/// An attempt to perform an invalid conversion is an error.
///
/// The conversion performed will in some cases be illegal because of the actual value;
///
/// Conversion from a GADDR to OADDR (AADDR) means: take the object address (attribute address)
/// part of the general address and return as result. An object address OADDR may be converted
/// to a general address GADDR. In that case the object address is extended with an empty attribute
/// address and the pair comprises the result.
///
/// REAL (LREAL) to INT conversion is performed after the rule: INT = entier( REAL + 0.5 ).
///
/// <pre>
///                             L         A    O    G    P    R
///    to:  B    C         R    R    S    A    A    A    A    A
///         O    H    I    E    E    I    D    D    D    D    D
///         O    A    N    A    A    Z    D    D    D    D    D
///  from:  L    R    T    L    L    E    R    R    R    R    R
///  BOOL   .
///  CHAR        .    +
///  INT         ?    .    ?    ?
///  REAL             ?    .    ?
///  LREAL            ?    ?    .
///  SIZE                            .
///  AADDR                                .
///  OADDR                                     .    +
///  GADDR                                +    +    .
///  PADDR                                               .
///  RADDR                                                    .
///
///                        Table of legal conversions
///
///
///  	. - always a legal conversion, but a null operation
///
///  	+ - always legal and exact
///
///  	? - the legality depends on the actual value being converted.
///  		Loss of accuracy is not considered an error when converting
///  		from integer values to real values. In other cases execution
///  		time checks may have to be inserted in order to avoid loss of
///  		information due to truncation.
///
///  	blank - always illegal.
/// </pre>
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_CONVERT.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_CONVERT extends SVM_Instruction {
	
	/// The fromType
	private final int fromType;
	
	/// The toType
	private final int toType;
		
	/// Construct a new SVM_CONVERT instruction
	/// @param fromType the fromType
	/// @param toType the toType
	public SVM_CONVERT(int fromType, int toType) {
		this.opcode = SVM_Instruction.iCONVERT;
		this.fromType = fromType;
		this.toType = toType;
	}
	
	@Override
	public void execute() {
		Value fromValue = null;
		switch(fromType) {
			case Tag.TAG_OADDR: fromValue = RTStack.popOADDR(); break;
			case Tag.TAG_GADDR: fromValue = RTStack.popGADDR(); break;
			default:			fromValue = RTStack.pop();
		}
		Value toValue = convValue(fromValue, fromType, toType);
		RTStack.push(toValue);
		Global.PSC.ofst++;
	}
	

	/// Convert a Value
	/// @param fromValue the Value to convert
	/// @param fromtype the fromType
	/// @param totype the toType
	/// @return the converted Value
	private static Value convValue(Value fromValue, int fromtype, int totype) {
		Value toValue = null;
		boolean ILL = false;
		switch(fromtype) {
			case Tag.TAG_CHAR: {
				IntegerValue fromval = (IntegerValue)fromValue;
				int val = (fromval == null)? 0 : fromval.value;
				switch(totype) {
					case Tag.TAG_SINT:  toValue = IntegerValue.of(Type.T_SINT, val); break;
					case Tag.TAG_INT:   toValue = IntegerValue.of(Type.T_INT, val); break;
					case Tag.TAG_REAL:  toValue = RealValue.of(val); break;
					case Tag.TAG_LREAL: toValue = LongRealValue.of(val); break;
					default: ILL = true;
				}
				break;
			}
			case Tag.TAG_SINT: {
				IntegerValue fromval = (IntegerValue)fromValue;
				int val = (fromval == null)? 0 : fromval.value;
				switch(totype) {
					case Tag.TAG_CHAR:  toValue = IntegerValue.of(Type.T_CHAR, val); break;
					case Tag.TAG_INT:   toValue = IntegerValue.of(Type.T_INT, val); break;
					case Tag.TAG_SIZE:  toValue = IntegerValue.of(Type.T_SIZE, val); break;
					case Tag.TAG_REAL:  toValue = RealValue.of(val); break;
					case Tag.TAG_LREAL: toValue = LongRealValue.of(val); break;
					default: ILL = true;
				}
				break;
			}
			case Tag.TAG_INT: {
				IntegerValue fromval = (IntegerValue)fromValue;
				int val = (fromval == null)? 0 : fromval.value;
				switch(totype) {
					case Tag.TAG_CHAR:  toValue = IntegerValue.of(Type.T_CHAR, val); break;
					case Tag.TAG_SINT:  toValue = IntegerValue.of(Type.T_SINT, val); break;
					case Tag.TAG_SIZE:  toValue = IntegerValue.of(Type.T_SIZE, val); break;
					case Tag.TAG_REAL:  toValue = RealValue.of(val); break;
					case Tag.TAG_LREAL: toValue = LongRealValue.of(val); break;
					default: ILL = true;
				}
				break;
			}
			case Tag.TAG_REAL: {
				float val = (fromValue == null)? 0 : fromValue.toFloat();
				if(val >0) {
					int newVal = (int)(val+0.5);
					switch(totype) {
						case Tag.TAG_CHAR:  toValue = IntegerValue.of(Type.T_CHAR, newVal); break;
						case Tag.TAG_SINT:  toValue = IntegerValue.of(Type.T_SINT, newVal); break;
						case Tag.TAG_INT:   toValue = IntegerValue.of(Type.T_INT, newVal); break;
						case Tag.TAG_LREAL: toValue = LongRealValue.of(val); break;
						default: ILL = true;
					}
				} else {
					int newVal = - (int)((-val)+0.5);
					switch(totype) {
						case Tag.TAG_CHAR:  toValue = IntegerValue.of(Type.T_CHAR, newVal); break;
						case Tag.TAG_SINT:  toValue = IntegerValue.of(Type.T_SINT, newVal); break;
						case Tag.TAG_INT:   toValue = IntegerValue.of(Type.T_INT, newVal); break;
						case Tag.TAG_LREAL: toValue = LongRealValue.of(val); break;
						default: ILL = true;
					}
				}
				break;
			}
			case Tag.TAG_LREAL: {
				LongRealValue fromval = (LongRealValue)fromValue;
				double val = (fromval == null)? 0 : fromval.value;
				if(val > 0) {
				int newVal = (int)(val+0.5);
					switch(totype) {
						case Tag.TAG_CHAR: toValue = IntegerValue.of(Type.T_CHAR, newVal); break;
						case Tag.TAG_SINT: toValue = IntegerValue.of(Type.T_SINT, newVal); break;
						case Tag.TAG_INT:  toValue = IntegerValue.of(Type.T_INT, newVal); break;
						case Tag.TAG_REAL: toValue = RealValue.of((float)val); break;
						default: ILL = true;
					}
				} else {
					int newVal = - (int)((-val)+0.5);
					switch(totype) {
						case Tag.TAG_CHAR: toValue = IntegerValue.of(Type.T_CHAR, newVal); break;
						case Tag.TAG_SINT: toValue = IntegerValue.of(Type.T_SINT, newVal); break;
						case Tag.TAG_INT:  toValue = IntegerValue.of(Type.T_INT, newVal); break;
						case Tag.TAG_REAL: toValue = RealValue.of((float)val); break;
						default: ILL = true;
					}	
				}
				break;
			}
			case Tag.TAG_OADDR: {
				// An object address OADDR may be converted to a general address GADDR.
				// In that case the object address is extended with an empty attribute address
				// and the pair comprises the result.
				if(totype == Tag.TAG_GADDR) {
					toValue = new GeneralAddress((ObjectAddress) fromValue, 0);
				} else ILL = true;
	
			} break;
			case Tag.TAG_GADDR: {
				// Conversion from a GADDR to OADDR (AADDR) means: take the object address (attribute address)
				// part of the general address and return as result.			
				GeneralAddress gaddr = (GeneralAddress) fromValue;
				switch(totype) {
					case Tag.TAG_OADDR:  toValue = gaddr.base; break;
					case Tag.TAG_AADDR:  toValue = IntegerValue.of(Type.T_AADDR, gaddr.ofst); break;
					default: ILL = true;
				}			
			} break;
			default: toValue = null;
		}
		if(ILL) {
			Util.ERROR("convValue: conversion is undefined: " + Scode.edTag(fromtype) + " ==> " + Scode.edTag(totype));
			Util.IERR("");
		}
		return toValue;
	}

	
	public String toString() {
		return "CONVERT  " + Scode.edTag(fromType) + " ==> " + Scode.edTag(toType);
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeShort(fromType);
		oupt.writeShort(toType);
	}

	/// Reads an SVM_CONVERT instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_CONVERT instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_CONVERT instr = new SVM_CONVERT(inpt.readShort(), inpt.readShort());
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}

}
