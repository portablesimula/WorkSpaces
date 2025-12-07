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

import simuletta.type.Type;

import static simuletta.compiler.common.S_Instructions.*;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

///  Binary Operation.
///  
///  <pre>
///  Syntax:
///  
/// 	binary_operation
///  		 ::= expression binary_operator factor
///  
/// 	binary_operator
/// 		::= + | - | * | / | rem
/// 		::= and | or | xor | imp | eqv
/// 		::= <> | < | <= | = | >= | > 
/// 
///  </pre>
///  
///  @author Øystein Myhre Andersen
///  
public class BinaryOperation extends Expression {
//	public int lineNumber;    // From SyntaxClass
	int opr;
	Expression x;
	Expression y;
	
	public BinaryOperation(int opr,Expression x,Expression y) {
		this.opr=opr; this.x=x; this.y=y;
	}

	// ***********************************************************************************************
	// *** Coding: doSCodingDirect
	// ***********************************************************************************************
	public Type doSCodingDirect() {
		enterLine();
        switch(opr) {

        case S_AND, S_OR, S_XOR, S_IMP, S_EQV:
        	Type xtype=x.doSCodingDirect();
        	Type t2=   y.doSCodingDirect();
        	if(!Option.sportOk || xtype!=t2) {
        		if(xtype != Type.Boolean) Type.tstconv(xtype,Type.Boolean);
        		if(t2    != Type.Boolean) Type.tstconv(t2   ,Type.Boolean);
        		xtype=Type.Boolean;
        	}
        	sCode.outinst(opr); sCode.outcode();
    		exitLine(); return(xtype);

        case S_ADD:
        	xtype=x.doSCodingDirect();//expression(ininstr());
        	if(xtype.isRefType()) {
        		t2=y.doSCodingDirect();//expression(ininstr());
        		Util.TRACE("Binpor.ADD: t2="+t2+", y="+y+", QUAL="+y.getClass().getSimpleName());
        		if(t2.isFieldType()) {
        			xtype=Type.Name(t2.qualifyingType());
        			sCode.outinst(S_LOCATE);
        		}
        		else {
        			if(t2 != Type.Size) Type.tstconv(t2,Type.Size);
        			xtype=Type.Ref;
        			sCode.outinst(S_INCO);
        		}
        	}
        	else if(xtype.isNameType()) {
        		t2=y.doSCodingDirect();//expression(ininstr());
        		if(t2.isFieldType()) {
        			sCode.outinst(S_LOCATE);
        			sCode.outcode();
        			xtype=Type.Name(t2.qualifyingType());
        		} else ERROR("Illegal type after +");
        	}
        	else {
        		xtype=xtype.arith_type(); t2=y.doSCodingDirect();//expression(ininstr());
        		if(t2 != xtype) Type.tstconv(t2,xtype);
        		sCode.outinst(S_ADD);
        	}
        	sCode.outcode();
        	exitLine(); return(xtype);

        case S_SUB:
        	xtype=x.doSCodingDirect();//expression(ininstr());
        	if(xtype.isRefType()) {
        		t2=y.doSCodingDirect();//expression(ininstr());
        		if(t2.isSizeType()) {
        			sCode.outinst(S_DECO); sCode.outcode();
        			xtype=Type.Ref;
        		}
        		else if(t2.isRefType()) {
        			sCode.outinst(S_DIST); sCode.outcode();
        			xtype=Type.Size;
        		}
        		else ERROR("Illegal type("+t2+") following -");
        	}
        	else {
        		xtype=xtype.arith_type(); t2=y.doSCodingDirect();//expression(ininstr());
        		if(t2 != xtype) Type.tstconv(t2,xtype);
        		sCode.outinst(S_SUB);
        	} 
        	sCode.outcode();
        	exitLine(); return(xtype);

        case S_MULT: case S_DIV: case S_LSHIFTL: case S_RSHIFTL: case S_LSHIFTA: case S_RSHIFTA:
        	t2=x.doSCodingDirect();//expression(ininstr());
        	xtype=t2.arith_type();
        	t2=y.doSCodingDirect();//expression(ininstr());
        	if(t2 != xtype) Type.tstconv(t2,xtype);
        	sCode.outinst(opr); sCode.outcode();
        	exitLine(); return(xtype);

        case S_REM:
        	xtype=Type.Integer;
        	t2=x.doSCodingDirect();//expression(ininstr());
        	if(t2 != Type.Integer) Type.tstconv(t2,Type.Integer);
        	t2=y.doSCodingDirect();//expression(ininstr());
        	if(t2 != Type.Integer) Type.tstconv(t2,Type.Integer);
        	sCode.outinst(S_REM); sCode.outcode();
        	exitLine(); return(xtype);

        default:
            xtype=Type.Boolean; condition(this);
            sCode.outinst(S_COMPARE); sCode.outinst(opr); sCode.outcode();
        	exitLine(); return(xtype);
        }
	}	
	
	public String toString() {
		return(""+x+" "+edSymbol(opr)+" "+y);
	}

}
