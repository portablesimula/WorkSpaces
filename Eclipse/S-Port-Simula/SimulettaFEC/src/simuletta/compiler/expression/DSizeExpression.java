/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.scope.Record;
import simuletta.type.Type;

/**
 * SizeValue.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 *		DSizeExpression  ::= SIZE ( record'Identifier : integer'Expression )
 * 
 * 
 *  S-Code:
 *  
 *  	DSizeExpression ::= DSIZE type
 * 
 * </pre>
 * 
 * If the type contains an indefinite repetition the size is measured as if this
 * attribute is absent, i.e. only the part(s) of the type preceding the
 * indefinite repetition is measured.
 * <p>
 * The size of the type is measured as the distance (see dist chapter 6) from
 * the first object unit allocated to a record of the type to the first object
 * unit following the record, i.e.
 * <p>
 * size = dist(first,next)
 * 
 * @author Øystein Myhre Andersen
 */
public class DSizeExpression extends Expression {
//	public int lineNumber;      // From SyntaxClass
	private final String ident; // Record Identifier
	private final Expression fixrep;
	
	public DSizeExpression(String ident,Expression fixrep) {
		this.ident=ident;
		this.fixrep=fixrep;
	}

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect() {
		enterLine();
			Type t2=fixrep.doSCodingDirect();
			if(!t2.isIntegerType()) Type.convert(t2,Type.Integer);			
			Record rec=(Record) Declaration.findMeaning(this.ident);
			if(!rec.indefinite) ERROR("Illegal record in size(Record:fixrep): Record in not indefinite");
			sCode.outinst(S_DSIZE); sCode.outtag(rec.getTag());
			sCode.outcode();
		exitLine();
		return(Type.Size);
	}
	
	public String toString() {
		return("SIZE("+ident+':'+fixrep+')');
	}


}
