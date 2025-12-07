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
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.type.Type;
import simuletta.utilities.Util;

/**
 * ObjectAddress.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		ObjectReferenceValue ::=  NONE  |  REF  (  Object'Identifier  ) 
 * 
 *  S-Code:
 *  
 *  	ObjectAddress ::= ONONE  |  C_OADDR global_or_const:tag
 * 
 * </pre>
 * 
 * The value is the object address of the global or constant quantity given.
 * 
 * ONONE refers to no object unit.
 * <p>
 *            c-dot T1 c-dot T2 c-gaddr T3    is    "T3.T2.T1".GADDR
 * 
 * 
 * @author Øystein Myhre Andersen
 */
public class ObjectAddress extends Value {
	public final String ident;
	
	public ObjectAddress(String ident) {
		this.ident=ident;
	}

	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
	public Type doOutConst() {
		enterLine();
			if(ident==null) {
				sCode.outinst(S_ONONE);	sCode.outcode();
//				IO.println("ObjectAddress.doOutConst: ONONE");
				return(Type.Ref);
			}
			VariableDeclaration q=(VariableDeclaration) Declaration.findMeaning(ident);
			if(q==null) Util.IERR("Missing declaration of "+ident);
			sCode.outinst(S_C_OADDR); sCode.outtag(q.getTag()); sCode.outcode();
//			IO.println("ObjectAddress.doOutConst: q="+q);
		exitLine();
		return(Type.Ref(q.identifier));
	}
	
	public String toString() {
		return ("C_OADDR: "+((ident==null)?"NULL":ident));
	}


}
