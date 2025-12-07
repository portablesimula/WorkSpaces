/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.statement;

import static simuletta.utilities.Util.*;
import java.util.Vector;

import simuletta.compiler.SyntaxClass;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.expression.designator.Designator;
import simuletta.compiler.parsing.Parser;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * Statement.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		Statement 
 * 			::= if_statement 
 * 			::= assignment_statement
 * 			::= goto_statement
 * 			::= routine_activation
 * 			::= built_in_routine
 * 			::= repeat_statement
 * 			::= case_statement
 * 			::= label'identifier :
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public abstract class Statement extends SyntaxClass {
//	public int lineNumber;    // From SyntaxClass
	
	public static void parseStatements(Vector<Statement> statements) {
		Parser.TRACE("Statements.parseStatements");
		while(true) {
			Statement stm=null;
			if(Parser.accept(KeyWord.IF)) 			stm=ConditionalStatement.parse();
			else if(Parser.accept(KeyWord.ASSERT))	stm=AssertStatement.parse();
			else if(Parser.accept(KeyWord.REPEAT))	stm=RepeatStatement.parse();
			else if(Parser.accept(KeyWord.CASE))	stm=CaseStatement.parse();
			else if(Parser.accept(KeyWord.GOTO))	stm=GotoStatement.parse();
			else if(Parser.accept(KeyWord.VAR,KeyWord.CALL,KeyWord.IDENTIFIER)) {
				Parser.TRACE("Statements.parseStatements: VAR/CALL/IDENTIFIER");
				Expression dx=Designator.parseVarCallIdentifier();
				if(Option.TRACE_PARSE) { dx.print("",10); }
				if(dx instanceof Designator) {
					Designator d=(Designator)dx;
					if(Parser.accept(KeyWord.ASSIGN))		stm=AssignmentStatement.parse(d);
					else if(Parser.accept(KeyWord.COLON))	stm=LabelDefinition.parse(d);
					else									stm=CallStatement.parse(d);;
				} else ERROR("Misplaced qua");
			} else {
				Parser.TRACE("Statements.parseStatements: NO MORE STEMENTS");
				return;
			}
			statements.add(stm);
		}
	}


	// ***********************************************************************************************
	// *** Coding: doSCodeStatement
	// ***********************************************************************************************
	public void doSCodeStatement() {		
		Util.FATAL_ERROR("doSCodeStatement should been REDEFINED !  QUAL="+this.getClass().getSimpleName());
	}	
	
}

