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
import static simuletta.compiler.common.S_Instructions.*;
import static simuletta.compiler.expression.Expression.getprim;
import static simuletta.compiler.expression.Expression.scan_expr;

import simuletta.compiler.expression.Expression;
import simuletta.compiler.expression.QuaOperation;
import simuletta.compiler.expression.designator.Designator;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * Assigment Statement.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		assignment_statement
 * 			::= < variable := >+ expression
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class AssignmentStatement extends Statement {
//	public int lineNumber;    // From SyntaxClass
	Designator target[]=new Designator[10]; // used in ASSIGN-stat only
	int nassign=0;
	Expression x;

	public static AssignmentStatement parse(Designator d) {
		Expression dx;
		Parser.TRACE("Statements.parseStatements: ASSIGN");
		AssignmentStatement stm=new AssignmentStatement();
		LOOP:while(true) {
			stm.target[stm.nassign]=d; stm.nassign=stm.nassign+1;
			if(Parser.accept(KeyWord.IDENTIFIER,KeyWord.VAR)) {
				dx=Designator.parseVarCallIdentifier();
				if(Option.TRACE_PARSE) dx.print("",10);
				if(Parser.accept(KeyWord.ASSIGN)) {
					if(dx instanceof Designator) d=(Designator)dx;
					else { ERROR("Misplaced qua"); d=(Designator) ((QuaOperation)dx).x; };
					continue LOOP;
				};
				stm.x=scan_expr(dx);
			} else {
				Parser.TRACE("Statements.parseStatements: ASSIGN: Before stm.x=scan_expr(getprim());");
				stm.x=scan_expr(getprim());
			}
			Parser.TRACE("Statements.parseStatements: ASSIGN: x="+stm.x);
			break LOOP;
		}
		Parser.TRACE("Statements.parseStatements: END ASSIGN: "+stm);
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
			//IO.println("AssignmentStatement.doSCodeStatement QUAL="+x.getClass().getSimpleName()+", x="+x);
        	Type xtype=x.doSCodingDirect();
        	while(nassign != 0) {
        		nassign=nassign-1;
        		Type tp=target[nassign].doSCodingDirect(true);
        		Util.BREAK("AssignmentStatement.doSCodeStatement xtype="+xtype+", tp="+tp);
        		if(!xtype.equals(tp)) Type.tstconv(xtype,tp);
        		sCode.outinst(S_RUPDATE); sCode.outcode();
        	}
        	sCode.outinst(S_POP); sCode.outcode();
        exitLine();
	}

	public String toString() {
		StringBuilder s=new StringBuilder();
		for(int i=0;i<nassign;i++) {
			s.append(target[i]).append(" := ");
		}
		s.append(x);
		return(s.toString());
	}
	
}
