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
 * Conditional Statement.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		IfStatement ::= IF Boolean'Expression THEN < Statement >*
 * 						< ELSIF Boolean'Expression THEN < Statement >*  >*
 * 						< ELSE < Statement >* >? ENDIF
 * 
 *   ConditionalStatement = Ifclause { Label : } ForStatement
 *                        | Ifclause { Label : } UnconditionalStatement  [ ELSE Statement ]
 *                        
 *     Ifclause = IF BooleanExpression THEN
 *
 *
 * S-Code:
 * 
 * 		IfStatement ::= 
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class ConditionalStatement extends Statement {
//	public int lineNumber;    // From SyntaxClass
	private Expression condition;
	private final Vector<Statement> thenStatements=new Vector<Statement>();
	private final Vector<ElsifClause> elsifClauses=new Vector<ElsifClause>();
	private final Vector<Statement> elseStatements=new Vector<Statement>();
	
	class ElsifClause {
		Expression condition;
		Vector<Statement> statements=new Vector<Statement>();
	}

	public static ConditionalStatement parse() {
		Parser.TRACE("Statements.parseStatements: IF");
		ConditionalStatement stm=new ConditionalStatement();
		int n=0;
		Vector<Statement> statements=stm.thenStatements;
		do { ElsifClause elsif=null;
			if(statements==null) {
				elsif=stm.new ElsifClause();
				stm.elsifClauses.add(elsif);
				statements=elsif.statements;
			}
			n=n+1;
			Expression cond=scan_expr(getprim());
			if(elsif!=null) elsif.condition=cond; else stm.condition=cond;
			
			Parser.expect(KeyWord.THEN);
			Statement.parseStatements(statements);
			statements=null;
		} while(Parser.accept(KeyWord.ELSIF)); // goto L;

		if(Parser.accept(KeyWord.ELSE)) Statement.parseStatements(stm.elseStatements);
		Parser.expect(KeyWord.ENDIF);
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
	/*
	 * 		IfStatement ::=
	 */
	public void doSCodeStatement() {
		enterLine();
		
		ProgramPoint elslab=new ProgramPoint("Else#0");
		int nBrance=elsifClauses.size()+1; int n=0;
    	ProgramPoint[] xdest=new ProgramPoint[nBrance];
		Util.TRACE("condition="+condition+", QUAL="+condition.getClass().getSimpleName());
		Condition cond=Expression.condition(condition);
		Util.TRACE("condition="+condition+", cond="+cond);
		elslab.jumpif(cond.inverse());
		sCode.outcode(+1);
		
		for(Statement stm:thenStatements) stm.doSCodeStatement();
		ProgramPoint endif=new ProgramPoint("Endif#0");
		xdest[n++]=endif;
		endif.go_to();
		
		for(ElsifClause elsif:elsifClauses) {
			sCode.outcode(-1);
			elslab.define();
			elslab=new ProgramPoint("Else#"+n);
			Util.TRACE("condition="+condition+", QUAL="+condition.getClass().getSimpleName());
			cond=Expression.condition(elsif.condition);
			elslab.jumpif(cond.inverse() );
			sCode.outcode(+1);
			for(Statement stm:elsif.statements) stm.doSCodeStatement();
			endif=new ProgramPoint("Endif#"+n);
			xdest[n++]=endif;
			//Util.println("XDEST["+(n-1)+"]="+xdest[n-1]);
			endif.go_to();
		}
		
		if(elseStatements.isEmpty()) {
			elslab.define();
		}
		else {
			elslab.define();
			for(Statement stm:elseStatements) stm.doSCodeStatement();
			
		}
		sCode.outcode(-1);
		for(int i=0;i<n;i++) {
			//Util.println("XDEST["+i+"="+xdest[i]);
			xdest[i].define();
		}
		sCode.outcode(-1);
		
		exitLine();
	}

	public void print(final String lead,final int indent) {
    	String spc=Util.edIndent(indent);
		StringBuilder s = new StringBuilder(spc);
		s.append("IF ").append(condition);
		Util.println(s.toString());
		Util.println(spc + "THEN "+thenStatements);
		if (elsifClauses != null) {
			Util.println(spc + elsifClauses);
		}
		if (elseStatements != null) {
			Util.println(spc + "ELSE "+elseStatements);
		}
	}
	
	public String toString() {
		String s="IF " + condition + " THEN " + thenStatements;
		s=s+elsifClauses;
	    s=s+" ELSE " + elseStatements;
		return(s+" ENDIF");
	}

}
