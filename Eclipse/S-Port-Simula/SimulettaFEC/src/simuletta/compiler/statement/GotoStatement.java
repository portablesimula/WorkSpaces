/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.statement;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.S_GOTO;
import static simuletta.compiler.expression.Expression.getprim;
import static simuletta.compiler.expression.Expression.scan_expr;

import simuletta.compiler.common.ProgramPoint;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * Goto Statement.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		goto_statement 
 * 			::= goto label'expression
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class GotoStatement extends Statement {
	Expression x;

	public static GotoStatement parse() {
//		public int lineNumber;    // From SyntaxClass
		GotoStatement stm=new GotoStatement();
		Parser.TRACE("Statements.parseStatements: GOTO");
		stm.x=scan_expr(getprim());
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
			Type type=x.doSCodingDirect();
			if(type == Type.Destination) ProgramPoint.gotoCurrentDestination();
			else if(type.getKeyWord() == KeyWord.LABEL) { sCode.outinst(S_GOTO); sCode.outcode(); }
			else ERROR("Illegal type - goto: "+type);
		exitLine();
	}
	
}
