/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.Type;
import simula.compiler.syntaxClass.expression.Constant;
import simula.compiler.syntaxClass.expression.Expression;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.PsiBuilder;
import simula.psi.PsiParse;
import simula.psi.SyntaxTree;

/// While Statement.
/// 
/// <pre>
/// 
/// Simula Standard: 4.3 While-statement
/// 
///   while-statement = WHILE Boolean-expression DO Statement
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaCompiler2/Simula/src/simula/compiler/syntaxClass/statement/WhileStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class WhileStatement extends Statement {
	/// The WHILE condition
	private Expression condition;
	
	/// The statement after DO
	private Statement doStatement;

	/// Create a new WhileStatement.
	/// @param line the source line number
	WhileStatement(final PsiBuilder psiBuilder) {
		psiBuilder.startSubtree("WhileStatement");
		psiBuilder.consume(KeyWord.WHILE); //  (add it to 'current tree')

//		if (Option.internal.TRACE_PARSE)
//			Util.TRACE("Parse WhileStatement: line="+line+", current=" + PsiParse.currentLexToken(psiBuilder));
		condition = Expression.expectExpression(psiBuilder);
		PsiParse.expect(psiBuilder, KeyWord.DO);
		doStatement = Statement.acceptStatement(psiBuilder);
		if (Option.internal.TRACE_PARSE)	Util.TRACE("Line "+firstLineNumber()+": WhileStatement: "+this);
		psiBuilder.doneSubtree(this);
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		Global.sourceLineNumber=firstLineNumber();
		condition.doChecking(); condition.backLink=this;
		if (condition.type == null || condition.type.keyWord != Type.T_BOOLEAN) Util.error("While condition is not Boolean");
		doStatement.doChecking();
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doJavaCoding() {
		Global.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code("while(" + condition.toJavaCode() + ") {");
		doStatement.doJavaCoding();
		if(isWhileTrueDo())
			JavaSourceFileCoder.code("if(_CTX==null) break; // Ad'Hoc to prevent JAVAC error: 'dead code' and terminate");
		JavaSourceFileCoder.code("}");
	}
	
	/// Check if this while statement is a 'while true do'.
	/// @return true if this while statement is a 'while true do'
	private boolean isWhileTrueDo() {
		// Check for:  while(true) do {}
		if(condition instanceof Constant cnst) return((boolean)cnst.value);
		else return(false);
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		Label whlLabel = codeBuilder.newLabel();
		Label endLabel = codeBuilder.newLabel();
		codeBuilder.labelBinding(whlLabel);
		condition.buildEvaluation(null,codeBuilder);
		codeBuilder.ifeq(endLabel);
		doStatement.buildByteCode(codeBuilder);
		codeBuilder.goto_(whlLabel);
		codeBuilder.labelBinding(endLabel);
	}

	@Override
	public void printTree(final int indent, final Object head) {
		IO.println(edTreeIndent(indent)+"WHILE " + condition + " DO");
		doStatement.printTree(indent+1,this);
	}

	@Override
    public void addSyntaxNodes(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(edPsi(toString()));
        model.insertNodeInto(newNode, parent, parent.getChildCount());
        
		SyntaxTree.addKeyWordNode(tree, model, newNode, KeyWord.WHILE);
		condition.addSyntaxNodes(tree, model, newNode);
		SyntaxTree.addKeyWordNode(tree, model, newNode, KeyWord.DO);
		doStatement.addSyntaxNodes(tree, model, newNode);
    }

	@Override
	public String toString() {
		return ("WHILE " + condition + " DO " + doStatement);
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private WhileStatement() {}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeWhileStatement: " + this);
		oupt.writeKind(ObjectKind.WhileStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxClass
//		oupt.writeShort(firstLineNumber());
		// *** WhileStatement
		oupt.writeObj(condition);
		oupt.writeObj(doStatement);
	}

	/// Read and return a WhileStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the WhileStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static WhileStatement readObject(AttributeInputStream inpt) throws IOException {
		WhileStatement stm = new WhileStatement();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxClass
//		stm.OLD_lineNumber = inpt.readShort();
		// *** WhileStatement
		stm.condition  = (Expression) inpt.readObj();
		stm.doStatement = (Statement) inpt.readObj();
		Util.TRACE_INPUT("WhileStatement: " + stm);
		return(stm);
	}
	
}
