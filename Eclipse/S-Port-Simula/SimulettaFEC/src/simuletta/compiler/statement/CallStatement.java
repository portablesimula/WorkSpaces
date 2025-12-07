/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.statement;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.S_POP;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.Profile;
import simuletta.compiler.declaration.InlineRoutine;
import simuletta.compiler.declaration.scope.RoutineDeclaration;
import simuletta.compiler.expression.designator.CallExpression;
import simuletta.compiler.expression.designator.Designator;
import simuletta.compiler.expression.designator.DesignatorElement;
import simuletta.compiler.expression.designator.Variable;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * Routine Activation.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		CallStatement ::=  CALL  Profile'Identifier ( Entry'Expression )  <  ArgumentList  >?
 * 
 * 			ArgumentList ::=  (  Argument  < , Argument >*  )
 * 
 *				Argument ::=  Expression  |  ( Expression  <  ,  Expression  >*  ) 				
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class CallStatement extends Statement {
//	public int lineNumber;    // From SyntaxClass
	private Designator designator;
	
	public static CallStatement parse(Designator designator) {
		CallStatement stm=new CallStatement(); stm.designator=designator;
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
		designator.doSCodingDirect();

//		IO.println("CallStatement.doSCodeStatement: designator="+designator.getClass().getSimpleName()+"  "+designator);
		Util.ASSERT(designator.varset.size()==1,"designator.varset.size()==1");
		DesignatorElement elt=designator.varset.firstElement();
		if(elt instanceof Variable var) {
//			Declaration meaning=getMeaning(var.identifier);
			Declaration meaning=Declaration.findMeaning(var.identifier);
//			IO.println("CallStatement.doSCodeStatement: meaning="+meaning.getClass().getSimpleName()+"  "+meaning);
			if(meaning instanceof RoutineDeclaration rut) {
				if(rut.signatur.export!=null) {
					sCode.outinst(S_POP); sCode.outcode(); // POP off export value
				}
			} else if(meaning instanceof InlineRoutine rut) {
				if(rut.isFunction()) {
					sCode.outinst(S_POP); sCode.outcode(); // POP off export value
				}
			}
		} else if(elt instanceof CallExpression call) {
			Profile prf=call.findProfile();
//			IO.println("CallStatement.doSCodeStatement: elt="+elt.getClass().getSimpleName()+"  "+elt);
			if(prf.signatur.export!=null) {
				sCode.outinst(S_POP); sCode.outcode(); // POP off export value
			}
		}
		exitLine();
//		Util.STOP();
	}
	
}
