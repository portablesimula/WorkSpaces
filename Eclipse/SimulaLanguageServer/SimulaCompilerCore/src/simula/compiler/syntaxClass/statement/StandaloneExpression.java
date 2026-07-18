/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import simula.builder.SimulaBuilder;
import simula.Option;
import simula.builder.Parse;
import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.Type;
import simula.compiler.syntaxClass.expression.AssignmentOperation;
import simula.compiler.syntaxClass.expression.Expression;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Util;
import simula.token.LexToken;

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
	StandaloneExpression(final SimulaBuilder simBuilder, final Expression expression) {
		super(simBuilder);
//		IO.println("\nNEW StandaloneExpression: expr="+expression);
//		simBuilder.printPSI("NEW StandaloneExpression: expr="+expression);
		this.lastParserToken = expression.lastParserToken;
		this.expression = expression;
		if (Option.internal.TRACE_PARSE) {
			Util.TRACE("Line "+firstLineNumber()+": StandaloneExpression: "+this);
			IO.println("Line "+firstLineNumber()+": StandaloneExpression: "+this+"   "+simBuilder.getCurrentParserToken());
		}
		LexToken prevToken = null;
		while ((prevToken = Parse.acceptParserToken(simBuilder, KeyWord.ASSIGNVALUE, KeyWord.ASSIGNREF)) != null) { 
//			IO.println("NEW StandaloneExpression: prevToken="+prevToken);
			this.expression = new AssignmentOperation(simBuilder, this.expression, prevToken.keyWord, expectStandaloneExpression(simBuilder));
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
			retExpr=new AssignmentOperation(simBuilder, retExpr, opr, expectStandaloneExpression(simBuilder));
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
	public void doJavaCoding() {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		JavaSourceFileCoder.code(toJavaCode() + ';');
	}

	@Override
	public String toJavaCode() {
		ASSERT_SEMANTICS_CHECKED();
		String result=expression.toJavaCode();
		return (result);
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		expression.buildEvaluation(null,codeBuilder);
	}

	@Override
	public void print(final int indent) {
		expression.print(indent);
	}
	
	@Override
	public void printTree(final int indent, final Object head) {
		expression.printTree(indent,this);
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
	private StandaloneExpression() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeStandaloneExpression: " + this);
		oupt.writeKind(ObjectKind.StandaloneExpression);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** StandaloneExpression
		oupt.writeObj(expression);
	}

	/// Read and return a StandaloneExpression object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the StandaloneExpression object read from the stream.
	/// @throws IOException if something went wrong.
	public static StandaloneExpression readObject(AttributeInputStream inpt) throws IOException {
		StandaloneExpression stm = new StandaloneExpression();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		stm.astData = readAstData(inpt);
		// *** StandaloneExpression
		stm.expression = (Expression) inpt.readObj();
		Util.TRACE_INPUT("StandaloneExpression: " + stm);
		return(stm);
	}

}
