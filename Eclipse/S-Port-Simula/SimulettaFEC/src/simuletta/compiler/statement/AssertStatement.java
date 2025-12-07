/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.statement;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;
import static simuletta.compiler.expression.Expression.getprim;
import static simuletta.compiler.expression.Expression.scan_expr;

import java.util.Vector;

import simuletta.compiler.common.Condition;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.parsing.Parser;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * Assert Statement.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		AssertStatement ::= ASSERT Boolean'Expression SKIP < Statement >* ENDSKIP
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class AssertStatement extends Statement {
//	public int lineNumber;    // From SyntaxClass
	public Expression condition;
	public final Vector<Statement> statements = new Vector<Statement>();

	public static AssertStatement parse() {
		Parser.TRACE("Statements.parseStatements: ASSERT");
		AssertStatement stm=new AssertStatement();
		stm.condition=scan_expr(getprim());
		Parser.expect(KeyWord.SKIP);
		Statement.parseStatements(stm.statements);
		Parser.expect(KeyWord.ENDSKIP);
		return(stm);
	}


	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterLine();//Global.sourceLineNumber = lineNumber;
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
			SET_SEMANTICS_CHECKED();
		exitLine();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeStatement
	// ***********************************************************************************************
	public void doSCodeStatement() {		
		enterLine();
			Condition cond=Expression.condition(this.condition);
			sCode.outinst(S_SKIPIF);
			sCode.outinst(cond.instr); sCode.outcode(+1);
			for(Statement stm:statements) stm.doSCodeStatement();
			sCode.outinst(S_ENDSKIP); sCode.outcode(-1);
        exitLine();
	}	

	
	public String toString() {
		String s="ASSER " + condition + " SKIP " + statements;
		return(s+" ENDSKIP");
	}

}
