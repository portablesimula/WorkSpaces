/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.expression.AssignmentOperation;
import simula.compiler.syntaxClass.expression.Expression;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.LexToken;
import simula.psi.PsiBuilder;
import simula.psi.PsiParse;
import simula.psi.PsiTree;

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
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaCompiler2/Simula/src/simula/compiler/syntaxClass/statement/StandaloneExpression.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class StandaloneExpression extends Statement {
	
	/// The expression.
	private Expression expression;

	/// Create a new StandaloneExpression.
	/// @param line the source line number
	/// @param expression the expression
//	StandaloneExpression(final PsiBuilder psiBuilder, final int line,final Expression expression) {
//		super(line);
	StandaloneExpression(final PsiBuilder psiBuilder, final Expression expression) {
		
//		IO.println("\nNEW StandaloneExpression: expr="+expression);
//		psiBuilder.printPSI("NEW StandaloneExpression: expr="+expression);

		this.expression = expression;
		if (Option.internal.TRACE_PARSE) {
			Util.TRACE("Line "+firstLineNumber()+": StandaloneExpression: "+this);
			IO.println("Line "+firstLineNumber()+": StandaloneExpression: "+this+"   "+psiBuilder.getCurrentLexerToken());
		}
		LexToken prevToken = null;
		while ((prevToken = PsiParse.acceptParserToken(psiBuilder, KeyWord.ASSIGNVALUE, KeyWord.ASSIGNREF)) != null) { 
//			IO.println("NEW StandaloneExpression: prevToken="+prevToken);
			psiBuilder.startSubtree("AssignmentOperation");
			this.expression = new AssignmentOperation(this.expression, prevToken.keyWord, expectStandaloneExpression(psiBuilder));
			psiBuilder.doneSubtree(this);
		}		
		
//		IO.println("\nEND NEW StandaloneExpression: expr="+expression);
//		psiBuilder.printPSI("END NEW StandaloneExpression: expr="+expression);

	}

	@Override
	public PsiTree getPsiTree() {
		if(psiTree == null) psiTree = expression.getPsiTree();
		return psiTree;
	}

	/// Parse a standalone expression.
	/// <pre>
	/// Syntax:
	/// 
	///    standalone-expression  =  expression  { assignment-operator  expression }
	/// </pre>
	/// Pre-Condition: First expression is already read.
	/// @return the resulting StandaloneExpression
	private static Expression expectStandaloneExpression(PsiBuilder psiBuilder) { 
		Expression retExpr=Expression.expectExpression(psiBuilder);
		LexToken prevToken = null;
		while ((prevToken = PsiParse.acceptParserToken(psiBuilder, KeyWord.ASSIGNVALUE,KeyWord.ASSIGNREF)) != null) {
			int opr=prevToken.keyWord;
			retExpr=new AssignmentOperation(retExpr,opr,expectStandaloneExpression(psiBuilder));
		}
//		IO.println("StandaloneExpression.expectStandaloneExpression: RETURN: "+retExpr+" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return retExpr;
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		Global.sourceLineNumber=firstLineNumber();
		if (Option.internal.TRACE_CHECKER) Util.TRACE("StandaloneExpression("+expression+").doChecking - Current Scope Chain: "+Global.getCurrentScope().edScopeChain());
		expression.doChecking();
		if(!expression.maybeStatement()) Util.error("Illegal/Missplaced Expression: "+expression);
		if (Option.internal.TRACE_CHECKER) Util.TRACE("END StandaloneExpression(" + expression+ ").doChecking:");
		SET_SEMANTICS_CHECKED();
	}
	
	@Override
	public void doJavaCoding() {
		Global.sourceLineNumber=firstLineNumber();
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
	
	@Override
	public int firstLineNumber() {
//		IO.println("StandaloneExpression.firstLineNumber: psiTree: "+getPsiTree()+", expr="+expression.getClass().getSimpleName()+"  "+expression);
		if(getPsiTree() != null) return getPsiTree().firstLineNumber();
		return -105;
	}
	
	@Override
	public int lastLineNumber() {
//		IO.println("StandaloneExpression.lastLineNumber: psiTree: "+getPsiTree());
		if(getPsiTree() != null) return getPsiTree().lastLineNumber();
		return -106;
	}

	@Override
    public void addSyntaxNodes(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(expression.edPsi(expression.toString()));
        model.insertNodeInto(newNode, parent, parent.getChildCount());
        expression.addSyntaxNodes(tree, model, newNode);
   }

	@Override
	public String toString() {
		return expression.toString();
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private StandaloneExpression() {
//		super(0);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeStandaloneExpression: " + this);
		oupt.writeKind(ObjectKind.StandaloneExpression);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxClass
//		oupt.writeShort(firstLineNumber());
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
		// *** SyntaxClass
//		stm.OLD_lineNumber = inpt.readShort();
		// *** StandaloneExpression
		stm.expression = (Expression) inpt.readObj();
		Util.TRACE_INPUT("StandaloneExpression: " + stm);
		return(stm);
	}

}
