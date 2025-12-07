/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.value;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.LabelDeclaration;
import simuletta.type.Type;

/**
 * ProgramAddress.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		LabelValue ::=  NOWHERE  |  Label'Identifier 
 * 
 *  S-Code:
 *  
 *  	ProgramAddress ::= NOWHERE  |  C_PADDR label:tag
 * 
 * </pre>
 * 
 * The value is the program point designated by the label.
 * 
 * NOWHERE designates no program point.
 * 
 * @author Øystein Myhre Andersen
 */
public class ProgramAddress extends Value {
	public final String ident;
	
	public ProgramAddress(String ident) {
		this.ident=ident;
	}

	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
	public Type doOutConst() {
		enterLine();
			if(ident==null) {
				sCode.outinst(S_NOWHERE); sCode.outcode();
				return(Type.Label);
			}
			LabelDeclaration lab=(LabelDeclaration) Declaration.findMeaning(ident);
			sCode.outinst(S_C_PADDR); sCode.outtag(lab.getTag()); sCode.outcode();
		exitLine();
		return(Type.Label);
	}
	
	public String toString() {
		return("S_C_PADDR: "+((ident==null)?"NULL":ident));
	}


}
