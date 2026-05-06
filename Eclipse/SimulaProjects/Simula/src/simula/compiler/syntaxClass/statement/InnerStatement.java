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
import simula.compiler.syntaxClass.declaration.ClassDeclaration;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.ObjectList;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.PsiBuilder;
import simula.psi.PsiParse;
import simula.psi.PsiTree;
import simula.psi.SyntaxTree;

/// Inner Statement.
/// 
/// <pre>
/// 
/// Syntax:
/// 
///   inner-statement = INNER
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/InnerStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class InnerStatement extends Statement {

	/// Create a new InnerStatement.
	/// @param line the source line number
//	public InnerStatement(final int line) {
////		super(line);
//		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": InnerStatement: "+this);
//		if(Global.getCurrentScope() instanceof ClassDeclaration cls) {
//			cls.statements1 = cls.statements;
//			cls.statements = new ObjectList<Statement>();
//		} else Util.error("Missplaced Inner");
//	}
		
	/// Create a new InnerStatement.
	/// @param line the source line number
	public InnerStatement(final PsiBuilder psiBuilder, boolean explicit) {
		super(psiBuilder.psiTree);
		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": InnerStatement: "+this);
		IO.println("NEW InnerStatement: Line "+firstLineNumber()+": InnerStatement: "+this+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		Thread.dumpStack();
		
		if(explicit) psiBuilder.consume(KeyWord.INNER); //  (add it to 'current tree')
		
		if(Global.getCurrentScope() instanceof ClassDeclaration cls) {
			if(cls.statements1 != null) {
				Util.error("Multiple Inner Statements: statements1'last: "+cls.statements1.lastElement());
				Util.IERR("SJEKK DETTE");
//				Util.STOP();
			} else {
				cls.statements1 = cls.statements;
				cls.statements = new ObjectList<Statement>();
			}
		} else Util.error("Missplaced Inner Statement");
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		Global.sourceLineNumber=firstLineNumber();
		SET_SEMANTICS_CHECKED();
	}
	
	@Override
	public void doJavaCoding() {
		Global.sourceLineNumber=firstLineNumber();
		// No code !
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		// No code !
	}

	@Override
	public void print(final int indent) {
    	String spc=edIndent(indent);
		Util.println(spc+"inner"); 
	}
	
	@Override
	public void printTree(final int indent, final Object head) {
		IO.println(edTreeIndent(indent)+"INNER ");
	}

	@Override
    public void addSyntaxNodes(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {
//        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(edPsi(toString()));
//        model.insertNodeInto(newNode, parent, parent.getChildCount());
//        
//        SyntaxTree.addKeyWordNode(tree, model, newNode, KeyWord.INNER);
        SyntaxTree.addKeyWordNode(tree, model, parent, KeyWord.INNER);
    }

	@Override
	public String toString() {
		return "INNER";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	public InnerStatement() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeInnerStatement: " + this);
		oupt.writeKind(ObjectKind.InnerStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writePsiTree(oupt);
	}

	/// Read and return an InnerStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the InnerStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static InnerStatement readObject(AttributeInputStream inpt) throws IOException {
		InnerStatement stm = new InnerStatement();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		stm.psiTree = readPsiTree(inpt);
		Util.TRACE_INPUT("InnerStatement: " + stm);
		return(stm);
	}	

}
