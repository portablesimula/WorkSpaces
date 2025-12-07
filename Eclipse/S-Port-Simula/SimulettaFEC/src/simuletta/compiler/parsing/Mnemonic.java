/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.parsing;

import java.io.IOException;
import java.io.ObjectOutput;

import simuletta.utilities.Util;

/**
 * Mnemonic.
 * 
 * <pre>
 * Syntax:
 * 
 * mnmonic_definition
 * 		::= macro_definition
 * 		::= literal_definition
 * 
 * 	macro_definition
 * 		::= macro macro'mnemonic ( parcount'integer_number )
 * 			begin < macro_element >* end
 * 
 * 		macro macro_element ::= any basic_symbol except endmacro and %
 * 
 *  literal_definition ::= define literal'mnemonic = < literal_value >+ < , literal'mnemonic = < literal_value >+ >*
 *  
 *   	literal_value ::= basic_symbol
 * 
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public abstract class Mnemonic {
	public String identifier;

	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.IERR("Missing redefinition of 'write' in "+this.getClass().getSimpleName());
//	}
//
//	public static Mnemonic createAndRead(AttributeInput inpt) throws ClassNotFoundException, IOException {
//		int kind=inpt.readUnsignedByte();
//		switch(kind) {
//			case Kind.kLiteral: return(LiteralMnemonic.readLiteral(inpt));
//			case Kind.kMacro:   return(MacroDefinition.readMacro(inpt));
//		}
//		Util.IERR("");
//		return(null);
//	}
	
	public void write(ObjectOutput oupt) throws IOException {
		Util.IERR("Missing redefinition of 'write' in "+this.getClass().getSimpleName());
	}

}
