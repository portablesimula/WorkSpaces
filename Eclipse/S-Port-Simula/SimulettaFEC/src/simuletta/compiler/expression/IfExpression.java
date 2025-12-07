/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;

import simuletta.compiler.common.Condition;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;

/**
 * If Expression.
 * 
 * <pre>
 * Syntax:
 * 
 *	if_expression
 *		 ::= if Boolean'expression then expression else expression
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class IfExpression extends Expression {
//	public int lineNumber;    // From SyntaxClass
	Expression cond;
	Expression x;
	Expression y;

	public static Expression parse() {
		IfExpression ifx=new IfExpression();
		ifx.cond=scan_expr(getprim());
		Parser.expect(KeyWord.THEN); 
		ifx.x=scan_expr(getprim());
		Parser.expect(KeyWord.ELSE);
		ifx.y=scan_expr(getprim());
		Parser.TRACE("Ifexpr.parse: RESULT="+ifx);
		return(ifx);
	}

	
	public Type doSCodingDirect() {
		enterLine();
        	Condition c=condition(cond);
        	sCode.outinst(S_IF); sCode.outinst(c.instr); sCode.outcode();
        	Type xtype=x.doSCodingDirect();
        	sCode.outinst(S_ELSE); sCode.outcode();
        	Type t2=y.doSCodingDirect();
        	if(xtype != t2) Type.tstconv(xtype,t2);
        	sCode.outinst(S_ENDIF); sCode.outcode();
		exitLine();
		return(xtype);
	}

	public String toString() {
		return("IF "+cond+" THEN "+x+" ELSE "+y);
	}
}
