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

import simuletta.compiler.common.ProgramPoint;
import simuletta.compiler.declaration.LabelDeclaration;
import simuletta.compiler.expression.designator.Designator;
import simuletta.compiler.expression.designator.Variable;
import simuletta.compiler.parsing.Parser;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * Label Definition.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		label_definition ::= label'identifier :
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class LabelDefinition extends Statement {
//	public int lineNumber;    // From SyntaxClass
	public LabelDeclaration label;
	
	public static LabelDefinition parse(Designator d) {
		LabelDefinition lab=new LabelDefinition();
		Parser.TRACE("LabelDefinition.parse: COLON");
		if(d.varset.size() > 1) ERROR("Illegal label identifier");
		Object firstVar=d.varset.firstElement();
		if(firstVar instanceof Variable) {
			Variable var=(Variable)firstVar;
			if(var.argset!=null && var.argset.size() > 1) ERROR("Illegal label identifier");
			if(!currentScope.isRoutine()) {
				lab.label=new LabelDeclaration(allVisible,var.identifier);
				lab.label.global=true;
				Util.ASSERT(currentModule==currentScope,"Interesting");
			} else lab.label=new LabelDeclaration(false,var.identifier);
			currentScope.add(lab.label);
		} else IERR();
		return(lab);
	}

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterLine();//Global.sourceLineNumber = lineNumber;
//		Comn.enterScope(this);
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
//		Comn.exitScope();
		SET_SEMANTICS_CHECKED();
		exitLine();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeStatement
	// ***********************************************************************************************
	public void doSCodeStatement() {
		enterLine(); //Global.sourceLineNumber = lineNumber;
			Util.TRACE("currentScope="+currentScope);
			if(label.global) {
				sCode.outinst(S_LABEL); sCode.outtag(label.getTag()); sCode.outcode();			
			} else {
				if(label.destinationIndex == 0) ProgramPoint.grabDestTabEntry(label);
				ProgramPoint.defineDestination(label.destinationIndex);
			}
		exitLine();
	}
	
	
}
