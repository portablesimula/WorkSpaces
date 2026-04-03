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
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Util;
import simula.psi.PsiBuilder;
import simula.psi.PsiTree;

/// Dummy Statement.
/// 
/// <pre>
/// 
/// Simula Standard: 4.11 Dummy statement
/// 
///   dummy-statement = empty
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/DummyStatement.java"><b>Source File
/// </b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class DummyStatement extends Statement {
	
	/// Create a new DummyStatement.
	/// @param line the source line number
//	private DummyStatement(final int line) {
//		super(line);
//		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": DummyStatement: "+this);
//	}
	private DummyStatement(final PsiTree psiTree) {
		super(psiTree);
	}

	public static DummyStatement ofExplicit(final PsiBuilder psiBuilder) {
		 psiBuilder.startSubtree("DummyStatement");
		 psiBuilder.consume(KeyWord.SEMICOLON); //  (add it to 'current tree')
		 DummyStatement dummyStatement = new DummyStatement(psiBuilder.psiTree);		
		 psiBuilder.doneSubtree(dummyStatement);
		 return dummyStatement;
	}

	public static DummyStatement ofImplicit(final PsiBuilder psiBuilder) {
		 psiBuilder.startSubtree("DummyStatement");
		 DummyStatement dummyStatement = new DummyStatement(psiBuilder.psiTree);		
		 psiBuilder.doneSubtree(dummyStatement);
		 return dummyStatement;
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		// No Checking
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doJavaCoding() { /* No Coding */
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(";");
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
	}

	@Override
	public void print(final int indent) {
	}
	
	@Override
	public void printTree(final int indent, final Object head) {
	}
	
	@Override
    public void addSyntaxNodes(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(edPsi(toString()));
        model.insertNodeInto(newNode, parent, parent.getChildCount());
    }

	@Override
	public String toString() {
		return ";";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
//	private DummyStatement() { super(0); }

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeDummyStatement: " + this);
		oupt.writeKind(ObjectKind.DummyStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxClass
		writePsiTree(oupt);
	}

	/// Read and return a DummyStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the DummyStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static DummyStatement readObject(AttributeInputStream inpt) throws IOException {
		DummyStatement stm = new DummyStatement(null);
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxClass
		stm.psiTree = readPsiTree(inpt);
		Util.TRACE_INPUT("DummyStatement: " + stm);
		return(stm);
	}

}
