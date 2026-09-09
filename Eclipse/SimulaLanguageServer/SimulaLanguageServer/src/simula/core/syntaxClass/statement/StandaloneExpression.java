/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.export.LexToken;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.expression.AssignmentOperation;
import simula.core.syntaxClass.expression.Expression;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// Standalone Expression Statement.
/// 
/// <pre>
/// 
/// Syntax:
/// 
///   standalone-expression = expression | assignment-statement
/// 
///      assignment-statement
///           = expression { assignment-operator expression }
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/StandaloneExpression.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class StandaloneExpression extends Statement {
	
	/// The expression.
	private Expression expression;

	/// Create a new StandaloneExpression.
	/// @param line the source line number
	/// @param expression the expression
//	StandaloneExpression(final PsiBuilder simBuilder, final int line,final Expression expression) {
//		super(line);
	StandaloneExpression(final DocumentManager documentManager, final Expression expression) {
		super(documentManager);
		SimulaBuilder simBuilder = documentManager.simBuilder;
//		IO.println("\nNEW StandaloneExpression: expr="+expression);
//		simBuilder.printPSI("NEW StandaloneExpression: expr="+expression);
//		this.lastParserToken = expression.lastParserToken;
		this.lexRange = expression.lexRange;
		this.expression = expression;
		if (Option.internal.TRACE_PARSE) {
			Util.TRACE("Line "+firstLineNumber()+": StandaloneExpression: "+this);
			IO.println("Line "+firstLineNumber()+": StandaloneExpression: "+this+"   "+simBuilder.getCurrentParserToken());
		}
		LexToken prevToken = null;
		while ((prevToken = Parse.acceptParserToken(simBuilder, KeyWord.ASSIGNVALUE, KeyWord.ASSIGNREF)) != null) { 
//			IO.println("NEW StandaloneExpression: prevToken="+prevToken);
			this.expression = new AssignmentOperation(simBuilder.documentManager, this.expression, prevToken.keyWord, expectStandaloneExpression(simBuilder));
		}		
		
//		IO.println("\nEND NEW StandaloneExpression: expr="+expression);
//		simBuilder.printPSI("END NEW StandaloneExpression: expr="+expression);

	}

	/// Parse a standalone expression.
	/// <pre>
	/// Syntax:
	/// 
	///    standalone-expression  =  expression  { assignment-operator  expression }
	/// </pre>
	/// Pre-Condition: First expression is already read.
	/// @return the resulting StandaloneExpression
	private static Expression expectStandaloneExpression(SimulaBuilder simBuilder) { 
		Expression retExpr=Expression.expectExpression(simBuilder, "standalone");
		LexToken prevToken = null;
		while ((prevToken = Parse.acceptParserToken(simBuilder, KeyWord.ASSIGNVALUE,KeyWord.ASSIGNREF)) != null) {
			int opr=prevToken.keyWord;
			retExpr=new AssignmentOperation(simBuilder.documentManager, retExpr, opr, expectStandaloneExpression(simBuilder));
		}
//		IO.println("StandaloneExpression.expectStandaloneExpression: RETURN: "+retExpr+" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return retExpr;
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		CoreGlobal.sourceLineNumber=firstLineNumber();
		if (Option.internal.TRACE_CHECKER) Util.TRACE("StandaloneExpression("+expression+").doChecking - Current Scope Chain: "+CoreGlobal.getCurrentScope().edScopeChain());
		expression.doChecking();
		if(!expression.maybeStatement() && expression.type != Type.Undef) {
			Util.semanticError(expression, "Illegal/Missplaced Expression: "+expression);
		}
		if (Option.internal.TRACE_CHECKER) Util.TRACE("END StandaloneExpression(" + expression+ ").doChecking:");
		SET_SEMANTICS_CHECKED();
	}
	
	@Override
	public void doJavaCoding(final SimulaCoder simCoder) {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		JavaSourceFileCoder.code(simCoder,toJavaCode() + ';');
	}

	@Override
	public String toJavaCode() {
		ASSERT_SEMANTICS_CHECKED();
		String result=expression.toJavaCode();
		return (result);
	}

	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		expression.buildEvaluation(simCoder, null, codeBuilder);
	}

	@Override
	public void print(final int indent) {
		expression.print(indent);
	}
	
	@Override
	public void printTree(final int indent) {
		expression.printTree(indent);
	}
	
//	@Override
//	public int firstLineNumber() {
////		IO.println("StandaloneExpression.firstLineNumber: psiTree: "+getPsiTree()+", expr="+expression.getClass().getSimpleName()+"  "+expression);
////		if(getPsiTree() != null) return getPsiTree().firstLineNumber();
//		LexToken token = lexTokenRange.getFirstLexToken();
//		if(token != null) return token.firstLineNumber();
//		return -105;
//	}
//	
//	@Override
//	public int lastLineNumber() {
////		IO.println("StandaloneExpression.lastLineNumber: psiTree: "+getPsiTree());
////		if(getPsiTree() != null) return getPsiTree().lastLineNumber();
//		LexToken token = lexTokenRange.getLastLexToken();
//		if(token != null) return token.lastLineNumber();
//		return -106;
//	}

	@Override
	public String toString() {
		return expression.toString();
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private StandaloneExpression(final DocumentManager documentManager) {
		super(documentManager);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeStandaloneExpression: " + this);
		oupt.writeKind(ObjectKind.StandaloneExpression);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		
		// *** StandaloneExpression
		oupt.writeObj(expression);
	}

	/// Read and return a StandaloneExpression object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the StandaloneExpression object read from the stream.
	/// @throws IOException if something went wrong.
	public static StandaloneExpression readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		StandaloneExpression stm = new StandaloneExpression(documentManager);
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement

		// *** StandaloneExpression
		stm.expression = (Expression) inpt.readObj(documentManager);
		Util.TRACE_INPUT("StandaloneExpression: " + stm);
		return(stm);
	}

}
