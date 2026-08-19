/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.util.Identifier;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.ConnectionBlock;
import simula.core.syntaxClass.expression.AssignmentOperation;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// Utility class to hold the single Connection do-part.
///
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/ConnectionDoPart.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class ConnectionDoPart extends SyntaxElement {
	
	/// The associated connection statement.
	ConnectionStatement connectionStatement;
	
	/// The associated connection block.
	public ConnectionBlock connectionBlock;

	/// Create a new do-part.
	/// @param connectionStatement The owner.
	/// @param connectionBlock The associated connection block
	/// @param statement the statement after DO
	ConnectionDoPart(final DocumentManager documentManager, final ConnectionStatement connectionStatement, final ConnectionBlock connectionBlock,final Statement statement) {
		super(documentManager);
//		SimulaBuilder simBuilder = documentManager.simBuilder;
		this.connectionStatement = connectionStatement;
		this.connectionBlock = connectionBlock; // this.statement=statement;
		connectionBlock.setStatement(statement);
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("NEW ConnectionDoPart: " + toString());
	}

	/// Perform semantic checking.
	public void doChecking() {
		Type type = connectionStatement.inspectVariableDeclaration.type;
		Identifier refIdentifier = type.getRefIdent();
		if (refIdentifier == null) {
			Util.semanticError(this, "The Variable " + connectionStatement.inspectedVariable + " is not ref() type");
		}
		ClassDeclaration classDeclaration = AssignmentOperation.getQualification(refIdentifier);
		if(classDeclaration != null) {
			connectionBlock.setClassDeclaration(classDeclaration);
		} else {
			Util.semanticError(this, "Illegal: " + refIdentifier + " is not a class");			
		}
		
		connectionBlock.doChecking();
		SET_SEMANTICS_CHECKED();
	}

	/// Perform Java coding.
	/// @param first true if coding the first when-part
	public void doCoding(final SimulaCoder simCoder, final boolean first) {
		ASSERT_SEMANTICS_CHECKED();
		connectionBlock.doJavaCoding(simCoder);
	}

	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		connectionBlock.buildByteCode(simCoder, codeBuilder);
		codeBuilder.goto_(connectionStatement.endLabel);
	}

	/// Utility print method.
	/// @param indent the indent
	@Override
	public void printTree(final int indent) {
    	String spc=edTreeIndent(indent);
		IO.println(spc + "DO " + connectionBlock.statement);
		connectionBlock.printTree(indent);
	}

	@Override
	public String toString() {
		return (connectionBlock.toString());
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	protected ConnectionDoPart(final DocumentManager documentManager) {
		super(documentManager);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeDoPart: " + this);
		oupt.writeKind(ObjectKind.ConnectionDoPart);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		
		// *** ConnectionDoPart
		oupt.writeObj(connectionStatement);
		oupt.writeObj(connectionBlock);
	}
	
	/// Read and return a ConnectionDoPart object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the ConnectionDoPart object read from the stream.
	/// @throws IOException if something went wrong.
	public static ConnectionDoPart readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		ConnectionDoPart dop = new ConnectionDoPart(documentManager);
		dop.OBJECT_SEQU = inpt.readSEQU(dop);
		// *** SyntaxElement

		// *** ConnectionDoPart
		dop.connectionStatement = (ConnectionStatement) inpt.readObj(documentManager);
		dop.connectionBlock = (ConnectionBlock) inpt.readObj(documentManager);
		Util.TRACE_INPUT("ConnectionDoPart: " + dop);
		return(dop);
	}
	
}

