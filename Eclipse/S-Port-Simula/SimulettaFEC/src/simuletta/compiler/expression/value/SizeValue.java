package simuletta.compiler.expression.value;

import static simuletta.compiler.Global.*;
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
 *		SizeValue  ::= NOSIZE  | SIZE ( record'Identifier )
 * 
 * 
 *  S-Code:
 *  
 *  	SizeValue ::= NOSIZE  |  C_SIZE type
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
public class SizeValue extends Value {
	private final String ident; // Record Identifier
	
	public SizeValue(String ident) {
		this.ident=ident;
	}

	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
	public Type doOutConst() {
		enterLine();
			if(ident!=null) {
				Record rec=(Record) Declaration.findMeaning(this.ident);
				sCode.outinst(S_C_SIZE); sCode.outtag(rec.getTag());
			} else sCode.outinst(S_NOSIZE);
			sCode.outcode();
		exitLine();
		return(Type.Size);
	}
	
	public String toString() {
		if(ident!=null) return("SIZE("+ident+')');
		return("NOSIZE");
	}


}
