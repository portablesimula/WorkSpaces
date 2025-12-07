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
import java.util.Vector;

import simuletta.compiler.SyntaxClass;
import simuletta.compiler.common.Condition;
import simuletta.compiler.expression.designator.Designator;
import simuletta.compiler.expression.designator.Variable;
import simuletta.compiler.expression.value.AttributeAddress;
import simuletta.compiler.expression.value.BooleanValue;
import simuletta.compiler.expression.value.GeneralAddress;
import simuletta.compiler.expression.value.ObjectAddress;
import simuletta.compiler.expression.value.ProgramAddress;
import simuletta.compiler.expression.value.RoutineAddress;
import simuletta.compiler.expression.value.SimpleValue;
import simuletta.compiler.expression.value.SizeValue;
import simuletta.compiler.expression.value.StructuredConst;
import simuletta.compiler.expression.value.Value;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Token;
import simuletta.utilities.Util;

/**
 * Expression.
 * 
 * <pre>
 * Syntax:
 * 
 *	expression
 *		::= factor
 *		::= unary_operator factor
 *		::= expression binary_operator factor
 *		::= if Boolean'expression then expression else expression
 *		::= type_conversion
 * 
 *		factor
 *			::= value
 *			::= variable
 *			::= routine_activation
 *			::= ( expression )
 *
 * 		unary_operator ::= + | - | not
 * 
 *		binary_operator
 *			::= + | - | * | / | rem
 *			::= and | or | xor
 *			::= <> | < | <= | = | >= | > * 
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class Expression extends SyntaxClass {
//	public int lineNumber;    // From SyntaxClass

	//!*** recursive ***;
	public static Expression scan_expr(Expression x) {
		Type type;
		//Parser.TRACE("Expression.scan_expr:");
		int key=Parser.currentToken.getKeyWord();
		switch(key) {
		case KeyWord.PLUS:  return(newBinopr(S_ADD,x));
		case KeyWord.MINUS: return(newBinopr(S_SUB,x));
		case KeyWord.MUL:   return(newBinopr(S_MULT,x));
		case KeyWord.DIV:   return(newBinopr(S_DIV,x));
		case KeyWord.REM:   return(newBinopr(S_REM,x));
		case KeyWord.NE:    return(newBinopr(S_NE,x));
		case KeyWord.EQ:    return(newBinopr(S_EQ,x));
		case KeyWord.LT:    return(newBinopr(S_LT,x));
		case KeyWord.GT:    return(newBinopr(S_GT,x));
		case KeyWord.LE:    return(newBinopr(S_LE,x));
		case KeyWord.GE:    return(newBinopr(S_GE,x));
		case KeyWord.OR:    return(newBinopr(S_OR,x));
		case KeyWord.XOR:   return(newBinopr(S_XOR,x));
		case KeyWord.AND:   return(newBinopr(S_AND,x));
		case KeyWord.IMP:   return(newBinopr(S_IMP,x));
		case KeyWord.EQV:   return(newBinopr(S_EQV,x));
		case KeyWord.LSHIFTL: return(newBinopr(S_LSHIFTL,x));
		case KeyWord.LSHIFTA: return(newBinopr(S_LSHIFTA,x));
		case KeyWord.RSHIFTL: return(newBinopr(S_RSHIFTL,x));
		case KeyWord.RSHIFTA: return(newBinopr(S_RSHIFTA,x));
		case KeyWord.QUA:
			Parser.nextSymb();
			Parser.TRACE("Expression.scan_expr: QUA ");
			type=Parser.expectType();
			Parser.TRACE("Expression.scan_expr: QUA  Type="+type);
			if(type == null) ERROR("Illegal syntax after qua");
			x=scan_expr(new QuaOperation(x,type));
			if(Option.TRACE_PARSE) x.print("",10);
			return(x);
		default: return(x);
		} 
	}
	
	private static Expression newBinopr(int opr,Expression x) {
		Parser.nextSymb();
		x=scan_expr(new BinaryOperation(opr,x,getprim()));
		if(Option.TRACE_PARSE) x.print("",10);
		return(x);
	}
	
	public static Expression getprim() { return(getprim(false)); }
	
	public static Expression getprim(boolean constflag) {
		Vector<Expression> exprset; //Expression designator;
	    @SuppressWarnings("unused")
		String ident = null;
	    Expression result=null;

	    if(Parser.accept(KeyWord.IDENTIFIER,KeyWord.VAR,KeyWord.CALL)) {
	    	result = Designator.parseVarCallIdentifier();
	    } else if(Parser.accept(KeyWord.SIMPLEVALUE)) {
			Token token = Parser.prevToken;
			Object value=token.getValue();
			if(value instanceof Long) result=new SimpleValue(S_C_INT,""+value);
			else if(value instanceof Short) result=new SimpleValue(S_C_INT,""+value);
			else if(value instanceof Integer) result=new SimpleValue(S_C_INT,""+value);
			else if(value instanceof Float) result=new SimpleValue(S_C_REAL,""+value);
			else if(value instanceof Double) result=new SimpleValue(S_C_LREAL,""+value);
			else if(value instanceof Boolean) result=new BooleanValue((Boolean)value);
			else if(value instanceof Character) result=new SimpleValue(S_C_CHAR,""+value);
			else if(value instanceof String) result= new SimpleValue(S_TEXT,""+value);
			else Util.FATAL_ERROR("SimpleValue.setType: IMPOSSIBLE !!!  -- "+value+", QUAL="+value.getClass().getSimpleName());
	    } else if(Parser.accept(KeyWord.TRUE)) result=new BooleanValue(true);
	    else if(Parser.accept(KeyWord.FALSE)) result=new BooleanValue(false);
	    else if(Parser.accept(KeyWord.SIZE)) {
	             Parser.expect(KeyWord.BEGPAR);
	             String id=Parser.expectIdentifier();
	             if(Parser.accept(KeyWord.ENDPAR)) result=new SizeValue(id);
	             else {
	                if(!Parser.accept(KeyWord.COMMA)) Parser.expect(KeyWord.COLON);
	                result=new DSizeExpression(id,scan_expr(getprim()));
	                Parser.expect(KeyWord.ENDPAR);
	             }
	    } else if(Parser.accept(KeyWord.NOSIZE)) result=new SizeValue(null);
	    else if(Parser.accept(KeyWord.REF)) {
	             Parser.expect(KeyWord.BEGPAR);
	             String id=Parser.expectIdentifier();
	             result=new ObjectAddress(id);
	             Parser.expect(KeyWord.ENDPAR);
	    } else if(Parser.accept(KeyWord.NONE)) result=new ObjectAddress(null);
	    else if(Parser.accept(KeyWord.FIELD))  result=AttributeAddress.parse();
	    else if(Parser.accept(KeyWord.NOFIELD)) result=new AttributeAddress();
	    else if(Parser.accept(KeyWord.NAME)) result=GeneralAddress.parse();
	    else if(Parser.accept(KeyWord.ADDR)) {
	    	Designator designator=(Designator) Designator.parse();
	    	if(designator.varset.isLegalDotList())    // TODO:NAME
	    		 result=new GeneralAddress(designator);
	    	else result=new UnaryOperation(S_NAME,designator);
	    }
	    else if(Parser.accept(KeyWord.NONAME)) result=new GeneralAddress(null);
	    else if(Parser.accept(KeyWord.ENTRY)) {
	             Parser.expect(KeyWord.BEGPAR);
	             String id=Parser.expectIdentifier();
	             result=new RoutineAddress(id);
	             //Parser.TRACE("Expression.getprim: ENTRY: result="+result+", currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);
	             Parser.expect(KeyWord.ENDPAR);
	    } else if(Parser.accept(KeyWord.NOWHERE)) result=new ProgramAddress(null);
	    else if(Parser.accept(KeyWord.NOBODY)) result=new RoutineAddress(null);
	    else if(Parser.accept(KeyWord.RECORD)) result=StructuredConst.parse(constflag);
	    else if(Parser.accept(KeyWord.IF)) result=IfExpression.parse();
	    else if(Parser.accept(KeyWord.NOT)) result=new UnaryOperation(S_NOT,getprim());
	    else if(Parser.accept(KeyWord.MINUS)) result=new UnaryOperation(S_NEG,getprim());
	    else if(Parser.accept(KeyWord.BEGPAR)) {
	             exprset=new Vector<Expression>();
	             do { exprset.add(scan_expr(getprim()));
	             } while(Parser.accept(KeyWord.COMMA));
	             result=new ExpressionList(exprset);
	             //Parser.TRACE("Expression.getprim: REPEXPR: result="+result+", currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);
	             Parser.expect(KeyWord.ENDPAR);
	    }
	    else {
	             ERROR("Getprim, symbol: " + Parser.currentToken);
	             result=new Expression(); 
	    } // *** case ***;
		Parser.TRACE("Expression.getprim: RESULT="+result);
	    return(result);
	}


	public static Value parseValue(boolean constflag) {
		Expression e=getprim(constflag);
//    	IO.println("Expression.parseValue: "+e);
	    if(e instanceof Value) return((Value)e);
	    if(e instanceof Designator) {
	    	Designator designator=(Designator)e;
	    	if(designator.varset.size() != 1) ERROR("Remote label is not allowed: "+designator);
	    	Object first=designator.varset.firstElement();
	        if(first instanceof Variable) {
	        	Variable var=(Variable)first;
	        	if(var.argset != null) ERROR("Indexed label is not allowed");
	        	return(new ProgramAddress(var.identifier));
	        } else ERROR("Illegal label");
	    } else if(e instanceof UnaryOperation unopr) {
//	    	IO.println("Expression.parseValue: "+unopr);
	    	if(unopr.opr==S_NAME && (unopr.x instanceof Designator des)) {
//		    	IO.println("Expression.parseValue: des="+des);
	        	GeneralAddress gaddr=new GeneralAddress((Designator)des);
		    	return(gaddr);
	    	}
	    } else {
	    	ERROR("Illegal constant: "+e);
	    	return(new SimpleValue(S_C_INT,"0"));
	    }
	    return(null);
	} 


	public Type doSCodingDirect() {
		FATAL_ERROR("doSCodingDirect should been REDEFINED !  QUAL="+this.getClass().getSimpleName());
		return(null);
	}
	
	
	public Type doSCodingDirect(boolean target) {
		FATAL_ERROR("doSCodingDirect should been REDEFINED !  QUAL="+this.getClass().getSimpleName());
		return(null);
	}

    //%title *********     C  o  n  d  i  t  i  o  n    *********

    //!*** recursive - calls 'expression' ***;
    public static Condition condition(Expression operation) {
    	Util.TRACE("Condition "+operation);
    	Condition cond=null;
    	boolean notseen;
    	Type t,t2;
    	if(operation instanceof BinaryOperation) {
    		BinaryOperation binopr=(BinaryOperation)operation;
    		int b=binopr.opr;
    		cond=Condition.relation(b);
        	Util.TRACE("Condition cond="+cond);
    		if((b==S_EQ) || (b==S_NE)) {
    			t=binopr.x.doSCodingDirect();
    			t2=binopr.y.doSCodingDirect();
    			if(t2 !=t) Type.tstconv(t2,t);
    		}
    		else if(cond.isTrueRelationship()) {
    			t=binopr.x.doSCodingDirect();
    			sCode.outcode();
    			if(t.isIntegerType()) t=Type.Integer;
    			else if(t.isRealType() || t.isCharacterType() || t.isSizeType()); // !nothing;
    			else if(!t.isRefType()) ERROR("Illegal type in relation: " + t);
    			t2=binopr.y.doSCodingDirect();
    			if(t2 != t) Type.tstconv(t2,t);
    		}
    		else {
        		t2=operation.doSCodingDirect();
        		if(t2 != Type.Boolean) Type.tstconv(t2,Type.Boolean); cond=Condition.relation(S_EQ); //.K_EQ;
        		sCode.outinst(S_PUSHC); sCode.outinst(S_TRUE);
    		}
    	}
    	else if(operation instanceof UnaryOperation) {
    		UnaryOperation unopr=(UnaryOperation)operation;
    		int b=unopr.opr;
    		cond=Condition.relation(b);
    		notseen=false;
    		if(unopr.opr==S_NOT) notseen=true;
    		t2=unopr.x.doSCodingDirect();
    		if(t2 != Type.Boolean) Type.tstconv(t2,Type.Boolean); cond=Condition.relation(S_EQ); //.K_EQ;
    		sCode.outinst(S_PUSHC); sCode.outinst(notseen?S_FALSE:S_TRUE);
    	} else{
    		t2=operation.doSCodingDirect();
    		if(t2 != Type.Boolean) Type.tstconv(t2,Type.Boolean); cond=Condition.relation(S_EQ); //.K_EQ;
    		sCode.outinst(S_PUSHC); sCode.outinst(S_TRUE);
    	}
    	sCode.outcode();
    	return(cond);
    }

    
}
