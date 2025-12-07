/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.statement;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.expression.Expression.getprim;
import static simuletta.compiler.expression.Expression.scan_expr;

import java.util.Vector;

import simuletta.compiler.common.Condition;
import simuletta.compiler.common.ProgramPoint;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.parsing.Parser;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * <pre>
 * 
 * Syntax:
 * 
 *   RepeatStatement = REPEAT < Statement >* WHILE BooleanExpression DO < Statement >* ENDREPEAT
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class RepeatStatement extends Statement {
//	public int lineNumber;    // From SyntaxClass
	public final Vector<Statement> statements1 = new Vector<Statement>();
	private Expression condition;
	public final Vector<Statement> statements2 = new Vector<Statement>();

	public static RepeatStatement parse() {
		Parser.TRACE("Statements.parseStatements: REPEAT");
		RepeatStatement stm=new RepeatStatement();
		Statement.parseStatements(stm.statements1);
		Parser.expect(KeyWord.WHILE);
		stm.condition=scan_expr(getprim());
		Parser.expect(KeyWord.DO);
		Statement.parseStatements(stm.statements2);
		Parser.expect(KeyWord.ENDREPEAT);
		return(stm);
	}

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterLine();
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
			SET_SEMANTICS_CHECKED();
		exitLine();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeStatement
	// ***********************************************************************************************
	public void doSCodeStatement() {
		enterLine();
			ProgramPoint beglab=new ProgramPoint("Repeat'Begin"); beglab.define();
			sCode.outcode(+1);
			for(Statement stm:statements1) stm.doSCodeStatement();
			Condition cond=Expression.condition(condition);
			if(statements2.isEmpty()) beglab.jumpif(cond);
			else {
				ProgramPoint dolab=new ProgramPoint("Repeat:doLab");
				dolab.jumpif(cond.inverse());
				for(Statement stm:statements2) stm.doSCodeStatement();
				beglab.go_to();
				dolab.define();
			}
			sCode.outcode(-1);
		exitLine();
	}
	
}
