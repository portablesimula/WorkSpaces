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
import simuletta.compiler.declaration.scope.DeclaredBody;
import simuletta.type.Type;
import simuletta.utilities.Util;

/**
 * RoutineAddress.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		EntryValue ::=  NOBODY  |  ENTRY( Routine'Identifier ) 
 * 
 *  S-Code:
 *  
 *  	RoutineAddress ::= NOBODY  |  C_RADDR body:tag
 * 
 * </pre>
 * 
 * The value is (the entry point of) the routine specified; a peculiar routine cannot occur.
 * NOBODY designates no routine body.
 * 
 * @author Øystein Myhre Andersen
 */
public class RoutineAddress extends Value {
	public final String ident;
	
	public RoutineAddress(String ident) {
		this.ident=ident;
	}

	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
	public Type doOutConst() {
		enterLine();
			if(ident==null) {
				sCode.outinst(S_NOBODY); sCode.outcode();
				return(Type.Entry);
			}
			DeclaredBody rut=(DeclaredBody) Declaration.findMeaning(ident);
			if(rut==null) Util.ERROR("Missing Declaration of "+ident);
			sCode.outinst(S_C_RADDR); sCode.outtag(rut.getTag()); sCode.outcode();
		exitLine();
		return(Type.Entry(rut.profileIdentifier));
	}
	
	public String toString() {
		return ("S_C_RADDR:"+((ident==null)?"NULL":ident));
	}


}
