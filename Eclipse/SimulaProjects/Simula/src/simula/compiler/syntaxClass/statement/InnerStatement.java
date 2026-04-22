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

	public static InnerStatement ofExplicit(final PsiBuilder psiBuilder) {
		if(! Option.TESTING_STATEMENT) {
			psiBuilder.startSubtree(PsiTree.Kind.innerStatement, "InnerStatement");
		}
//		 psiBuilder.consume(KeyWord.INNER); //  (add it to 'current tree')
		 InnerStatement innerStatement = new InnerStatement(psiBuilder);		
		 if(! Option.TESTING_STATEMENT) {
			 psiBuilder.doneSubtree(PsiTree.Kind.innerStatement, innerStatement);
		 }
		 return innerStatement;
	}

	public static InnerStatement ofImplicit(final PsiBuilder psiBuilder) {
		if(! Option.TESTING_STATEMENT) {
			psiBuilder.startSubtree(PsiTree.Kind.innerStatement, "InnerStatement");
		}
		 InnerStatement innerStatement = new InnerStatement(psiBuilder);		
		 if(! Option.TESTING_STATEMENT) {
			 psiBuilder.doneSubtree(PsiTree.Kind.innerStatement, innerStatement);
		 }
		 return innerStatement;
	}
		
	/// Create a new InnerStatement.
	/// @param line the source line number
	private InnerStatement(final PsiBuilder psiBuilder) {
		super(psiBuilder.psiTree);
		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": InnerStatement: "+this);
		if(Global.getCurrentScope() instanceof ClassDeclaration cls) {
			cls.statements1 = cls.statements;
			cls.statements = new ObjectList<Statement>();
		} else Util.error("Missplaced Inner");
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
		// *** SyntaxClass
		writePsiTree(oupt);
	}

	/// Read and return an InnerStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the InnerStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static InnerStatement readObject(AttributeInputStream inpt) throws IOException {
		InnerStatement stm = new InnerStatement();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxClass
		stm.psiTree = readPsiTree(inpt);
		Util.TRACE_INPUT("InnerStatement: " + stm);
		return(stm);
	}	

}
