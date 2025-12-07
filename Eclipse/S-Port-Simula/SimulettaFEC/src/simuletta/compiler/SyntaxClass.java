/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.S_LINE;

import java.util.Stack;

import simuletta.utilities.Util;

/**
 * A NonTerminal object represents non terminal symbol in the formal syntax.
 *
 * Parsing descends in a top-down manner, until the final nonterminal has been processed.
 * The parsing process depends on a global variable, currentToken, which contains the current symbol
 * from the input, and the function nextSymb, which updates currentToken when called.
 * 
 * For further description of Recursive Descent Parsing
 * see <a href="https://en.wikipedia.org/wiki/Recursive_descent_parser">Wikipedia</a>.
 *
 * <pre>
 * 
 * ***********************************************************************	
 *  META-SYNTAX:
 *  
 *       MetaSymbol                    Meaning
 *           =                     is defined to be
 *           |                     alternatively
 *         [ x ]                   0 or 1 instance of x
 *         { x }                   0 or more instances of x
 *       ( x | y )                 grouping: either x or y
 *          xyz                    the terminal symbol xyz
 *     MetaIdentifier              a non terminal symbol
 * ***********************************************************************	
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */

public abstract class SyntaxClass { 
	private boolean CHECKED = false; // Set true when doChecking(), put or get is called
	private boolean EMITTED = false; // Set true when doChecking(), put or get is called
	public int lineNumber;

	protected SyntaxClass() {
		lineNumber = Global.sourceLineNumber;
	}
	private static Stack<Integer> lineStack=new Stack<Integer>();
	
	protected void enterLine() {
//		Util.BREAK("Global.enterScope: currentScope <== "+scope);
		lineStack.push(Global.sourceLineNumber); Global.sourceLineNumber=lineNumber;
	}
	protected void exitLine() {
		Global.sourceLineNumber=lineStack.pop();
//		Util.BREAK("Global.exitScope: currentScope <== "+currentScope);
	}

	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterLine();//Global.sourceLineNumber = lineNumber;
		String name = this.getClass().getSimpleName();
		Util.NOT_IMPLEMENTED("" + name + ".doChecking");
		exitLine();
	}

	// Should be called from all doChecking,put,get methods
	// to signal that semantic checking is done.
	protected void SET_SEMANTICS_CHECKED() {
		CHECKED = true;
	}

	protected boolean IS_SEMANTICS_CHECKED() {
		return (CHECKED);
	}

	protected void ASSERT_SEMANTICS_CHECKED(final Object obj) {
		if (!CHECKED) Util.FATAL_ERROR("Semantic checker not called: " + obj.getClass().getName() + ", " + obj);
	}

	private static int lastLineEmitted=0;
	protected void output_S_LINE() {
		if(lineNumber==lastLineEmitted) return;
		sCode.outinst(S_LINE); sCode.outnumber(lineNumber);	sCode.outcode();
	}

	protected void SET_SCODE_EMITTED() {
		EMITTED = true;
	}

	public boolean IS_SCODE_EMITTED() {
		return (EMITTED);
	}

	public void print(final String lead,final int indent) {
		Util.println(Util.edIndent(indent) + lead + this);
	}

}
