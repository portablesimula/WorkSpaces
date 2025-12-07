/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.designator;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;
import static simuletta.compiler.expression.Expression.*;

import java.util.Vector;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.Profile;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;
import simuletta.utilities.VarSet;

/**
 * CallExpression.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		CallExpression ::=  CALL  Profile'Identifier ( Entry'Expression )  <  ArgumentList  >? 
 *
 * 			ArgumentList ::=  (  Argument  < , Argument >*  )
 * 
 *				Argument ::=  Expression  |  ( Expression  <  ,  Expression  >*  ) 				
 * 
 * </pre>
 * @author Øystein Myhre Andersen
 */
public class CallExpression extends SimpleDesignator {
//	public int lineNumber;     // From SyntaxClass
//	Vector<Expression> argset; // From SimpleDesignator
	final Expression entry;    // Entry'Expression
	final String profileIdent; // Profile'Identifier
	
	private CallExpression(String profileIdent, Expression entry, Vector<Expression> argset) {
		super(argset);
		this.entry=entry; this.profileIdent=profileIdent;
	}

	static void parseVarcall(VarSet varset) {
		//	CallStatement ::=  CALL  Profile'Identifier ( Entry'Expression )  <  ArgumentList  >? 
		//		ArgumentList ::=  (  Argument  < , Argument >*  )
		//			Argument ::=  Expression  |  ( Expression  <  ,  Expression  >*  ) 				
		Parser.TRACE("CallExpression.parseVarcall: CALL");
		String prf=Parser.expectIdentifier();
		Parser.TRACE("CallExpression.parseVarcall: CALL  prf="+prf);
		Parser.expect(KeyWord.BEGPAR);
		Parser.TRACE("CallExpression.parseVarcall: CALL 2");
		Expression entry=scan_expr(getprim());
		Parser.expect(KeyWord.ENDPAR);
		Vector<Expression> argset=parseArgumentSet();
		CallExpression vc=new CallExpression(prf,entry,argset);
		Parser.TRACE("CallExpression.parseVarcall: END CALL: Varcall="+vc);
		varset.add(vc);
	}
	
	public Profile findProfile() {
		Profile profile=(Profile)Declaration.findMeaning(profileIdent);
		Util.TRACE("CallExpression.findProfile: prf="+profile);
		return(profile);
	}

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect(boolean target,boolean simple,Type prevType) {
		enterLine();
		CallExpression var=this;//(Varcall)v;
		Util.TRACE("CallExpression.doSCoding: S_VARCALL  simple="+simple+", v="+this);
    	Profile prf=findProfile();
//    	IO.println("CallExpression.doSCodingDirect: prf="+prf);
    	sCode.outinst(S_PRECALL); sCode.outtag(prf.getTag());
        sCode.outcode(+1);
        	Type vartype=rutcall(prf.signatur,var.argset);
        	Type typ=var.entry.doSCodingDirect(); // Outexpr   typ=Expression.expression(bbyte);
        	Type t2=Type.Entry(prf.identifier);
        	if(typ != t2) Type.tstconv(typ,t2);
        	sCode.outinst(S_CALL_TOS);
        sCode.outcode(-1);
		exitLine();
 		return(vartype);
	}

	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append("CALL ").append(profileIdent).append('(').append(entry).append(')');
		s.append(edArgset());
		return(s.toString());
	}

}
