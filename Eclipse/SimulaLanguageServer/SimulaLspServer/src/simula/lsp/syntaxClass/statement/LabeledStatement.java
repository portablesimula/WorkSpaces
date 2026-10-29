/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.lsp.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import simula.lsp.DocumentManager;
import simula.lsp.builder.AttributeInputStream;
import simula.lsp.builder.AttributeOutputStream;
import simula.lsp.builder.JavaSourceFileCoder;
import simula.lsp.coder.SimulaCoder;
import simula.lsp.syntaxClass.declaration.LabelDeclaration;
import simula.lsp.utilities.LspGlobal;
import simula.lsp.utilities.ObjectKind;
import simula.lsp.utilities.ObjectList;
import simula.lsp.utilities.Option;
import simula.lsp.utilities.Util;

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
	LabeledStatement(final DocumentManager documentManager, final ObjectList<LabelDeclaration> labels, final Statement statement) {
		super(documentManager);
//		SimulaBuilder simBuilder = documentManager.simBuilder;
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
	public void doJavaCoding(final SimulaCoder simCoder) {
		LspGlobal.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(simCoder,"{");
		for (LabelDeclaration decl:labels) {
			String comment = "DeclaredIn: "+decl.declaredIn.identifier;
			if(decl.movedTo != null) comment = comment+" -> "+decl.movedTo;
			String labelcode;
			labelcode="_SIM_LABEL("+decl.index+");";
			if(statement instanceof BlockStatement stat && stat.isCompoundStatement())
				     stat.addLeadingLabel(labelcode);
				else JavaSourceFileCoder.code(simCoder,labelcode,comment);
		}
		statement.doJavaCoding(simCoder);
		JavaSourceFileCoder.code(simCoder,"}");
	}

	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		LspGlobal.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		for (LabelDeclaration lab:labels)
			lab.doBind(codeBuilder); // Bind Label
		statement.buildByteCode(simCoder, codeBuilder);
	}

	@Override
	public void printTree(final int indent) {
		StringBuilder sb = new StringBuilder();
		sb.append(edTreeIndent(indent)+"LABELED_STATEMENT ");
		for (LabelDeclaration lab:labels) sb.append(" "+lab+":");
		Util.println(""+sb);
		statement.printTree(indent + 1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(LabelDeclaration lab:labels) {
			if(! first) sb.append("Line").append(lab.firstLineNumber()).append(": "); first = false;
			sb.append(lab.identifierValue()).append(": ");
		}
		
		return sb.toString() + statement;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private LabeledStatement(final DocumentManager documentManager) {
		super(documentManager);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeLabeledStatement: " + this);
		oupt.writeKind(ObjectKind.LabeledStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		
		// *** LabeledStatement
		oupt.writeObj(statement);
		oupt.writeObjectList(labels);
	}

	/// Read and return a LabeledStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the LabeledStatement object read from the stream.
	/// @throws IOException if something went wrong.
	@SuppressWarnings("unchecked")
	public static LabeledStatement readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		LabeledStatement stm = new LabeledStatement(documentManager);
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		
		// *** LabeledStatement
		stm.statement = (Statement) inpt.readObj(documentManager);
		stm.labels = (ObjectList<LabelDeclaration>) inpt.readObjectList(documentManager);
		Util.TRACE_INPUT("LabeledStatement: " + stm);
		return(stm);
	}

}
