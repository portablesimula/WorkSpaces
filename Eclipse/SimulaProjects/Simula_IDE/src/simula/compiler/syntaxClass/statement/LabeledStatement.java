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
import simula.compiler.syntaxClass.declaration.LabelDeclaration;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.ObjectList;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.PsiBuilder;

/// Labeled Statement.
/// 
/// <pre>
/// 
/// Syntax:
/// 
///   label-statement =  label : { label : } statement 
/// 
/// 	    label = identifier
///  
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/LabeledStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class LabeledStatement extends Statement {
	
	/// The list of labels.
	private ObjectList<LabelDeclaration> labels;
	
	/// The statement
	private Statement statement;

	/// Create a new LabeledStatement.
	/// @param line the source line number
	/// @param labels the label identifiers
	/// @param statement the labeled statement
//	LabeledStatement(final int line,final ObjectList<LabelDeclaration> labels,final Statement statement) {
	LabeledStatement(final PsiBuilder psiBuilder, final ObjectList<LabelDeclaration> labels, final Statement statement) {
		super(psiBuilder);
		this.labels = labels;
		this.statement = statement;
		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": LabeledStatement: "+this);
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		statement.doChecking();
		for (LabelDeclaration decl:labels) {
			decl.doChecking();
		}
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doJavaCoding() {
		Global.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code("{");
		for (LabelDeclaration decl:labels) {
			String comment = "DeclaredIn: "+decl.declaredIn.identifier;
			if(decl.movedTo != null) comment = comment+" -> "+decl.movedTo;
			String labelcode;
			labelcode="_SIM_LABEL("+decl.index+");";
			if(statement instanceof BlockStatement stat && stat.isCompoundStatement())
				     stat.addLeadingLabel(labelcode);
				else JavaSourceFileCoder.code(labelcode,comment);
		}
		statement.doJavaCoding();
		JavaSourceFileCoder.code("}");
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		Global.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		for (LabelDeclaration lab:labels)
			lab.doBind(codeBuilder); // Bind Label
		statement.buildByteCode(codeBuilder);
	}

	@Override
	public void printTree(final int indent, final Object head) {
		System.out.print(edTreeIndent(indent)+"LABELED_STATEMENT ");
		for (LabelDeclaration lab:labels)
			System.out.print(" "+lab+":");
		IO.println("");
		statement.printTree(indent+1,this);
	}

	@Override
    public void addSyntaxNodes(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(edPsi(toString()));
        model.insertNodeInto(newNode, parent, parent.getChildCount());
		for (LabelDeclaration lab:labels) 
			lab.addSyntaxNodes(tree, model, newNode);
       statement.addSyntaxNodes(tree, model, newNode);
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(LabelDeclaration lab:labels) {
			if(! first) sb.append("Line").append(lab.firstLineNumber()).append(": "); first = false;
			sb.append(lab.identifier).append(": ");
		}
		
		return sb.toString() + statement;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private LabeledStatement() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeLabeledStatement: " + this);
		oupt.writeKind(ObjectKind.LabeledStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writePsiTree(oupt);
		// *** LabeledStatement
		oupt.writeObj(statement);
		oupt.writeObjectList(labels);
	}

	/// Read and return a LabeledStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the LabeledStatement object read from the stream.
	/// @throws IOException if something went wrong.
	@SuppressWarnings("unchecked")
	public static LabeledStatement readObject(AttributeInputStream inpt) throws IOException {
		LabeledStatement stm = new LabeledStatement();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		stm.psiTree = readPsiTree(inpt);
		// *** LabeledStatement
		stm.statement = (Statement) inpt.readObj();
		stm.labels = (ObjectList<LabelDeclaration>) inpt.readObjectList();
		Util.TRACE_INPUT("LabeledStatement: " + stm);
		return(stm);
	}

}
