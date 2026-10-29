/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.lsp.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;

import simula.lsp.DocumentManager;
import simula.lsp.builder.AttributeInputStream;
import simula.lsp.builder.AttributeOutputStream;
import simula.lsp.builder.JavaSourceFileCoder;
import simula.lsp.builder.token.Identifier;
import simula.lsp.coder.SimulaCoder;
import simula.lsp.syntaxClass.Type;
import simula.lsp.syntaxClass.declaration.ClassDeclaration;
import simula.lsp.syntaxClass.declaration.ConnectionBlock;
import simula.lsp.syntaxClass.expression.AssignmentOperation;
import simula.lsp.utilities.ObjectKind;
import simula.lsp.utilities.Option;
import simula.lsp.utilities.Util;

/// Utility class to hold a when-part.
///
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/ConnectionWhenPart.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class ConnectionWhenPart extends ConnectionDoPart {
	///  The WHEN class-identifier
	Identifier classIdentifier;
	
	/// The class declaration correspondig to the class identifier.
	/// Set during checking.
	ClassDeclaration classDeclaration;
	
	/// Indicator to signal an impossible when-part.
	/// Set during checking if object-expression is not compatible with the WHEN class.
	boolean impossibleWhenPart; // Set by doChecking

	/// Create a new when-part.
	/// @param connectionStatement the connectionStatement
	/// @param classIdentifier the WHEN class-identifier
	/// @param connectionBlock The associated connection block
	/// @param statement the statement after DO
	public ConnectionWhenPart(final DocumentManager documentManager, final ConnectionStatement connectionStatement, final Identifier classIdentifier,final ConnectionBlock connectionBlock,final Statement statement) {
		super(documentManager, connectionStatement, connectionBlock, statement);
		this.classIdentifier = classIdentifier;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("NEW ConnectionDoPart: " + toString());
	}

	@Override
	public void doChecking() {
		if (classIdentifier == null) { // && objectExpression!=null)
			Type type = connectionStatement.inspectVariableDeclaration.type;
			classIdentifier = type.getRefIdent();
			if (classIdentifier == null)
				Util.semanticError(this, "The Variable " + connectionStatement.inspectedVariable + " is not ref() type");
		}
		if (classIdentifier != null) {
			classDeclaration = AssignmentOperation.getQualification(classIdentifier);
			if(classDeclaration == null) Util.semanticError(this, "Illegal WHEN part: " + classIdentifier.value + " is not a class");
			connectionBlock.setClassDeclaration(classDeclaration);
		}
		if (!AssignmentOperation.checkCompatibility(connectionStatement.objectExpression, classIdentifier)) {
			Util.warning(connectionStatement, "Impossible When Part: " + connectionStatement.objectExpression + " is not compatible with " + classIdentifier);
			impossibleWhenPart = true;
		}
		connectionBlock.doChecking();
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doCoding(final SimulaCoder simCoder, final boolean first) {
		ASSERT_SEMANTICS_CHECKED();
		String prfx = (first) ? "" : "else ";
		String cid = classDeclaration.getJavaIdentifier();
		if (impossibleWhenPart) {
			JavaSourceFileCoder.code(simCoder,prfx,"WHEN " + cid + " DO -- IMPOSSIBLE REMOVED");
		} else {
			String cvar = this.connectionBlock.connID;
			JavaSourceFileCoder.code(simCoder,prfx + "if(" + connectionStatement.inspectedVariable.toJavaCode() + " instanceof " + cid + "  " + cvar + ") {","WHEN "	+ cid + " DO ");
			connectionBlock.doJavaCoding(simCoder);
			JavaSourceFileCoder.code(simCoder,"}");				
		}
	}

	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		if (!impossibleWhenPart) {
			connectionStatement.inspectedVariable.buildEvaluation(simCoder, null, codeBuilder);
			Label elseLabel = codeBuilder.newLabel();
			codeBuilder
				.instanceOf(classDeclaration.getClassDesc())
				.ifeq(elseLabel);
			
			connectionBlock.buildByteCode(simCoder, codeBuilder);
			
			codeBuilder
				.goto_(connectionStatement.endLabel)
				.labelBinding(elseLabel);
		}
	}

	@Override
	public String toString() {
		return ("WHEN " + classIdentifier + " DO ..."); // +statement);
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private ConnectionWhenPart(final DocumentManager documentManager) { super(documentManager);}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeWhenPart: " + this);
		oupt.writeKind(ObjectKind.ConnectionWhenPart);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		
		// *** ConnectionWhenPart
		oupt.writeIdentifier(classIdentifier);
		oupt.writeObj(connectionStatement);
		oupt.writeObj(connectionBlock);
	}
	
	/// Read and return a ConnectionDoPart object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the ConnectionDoPart object read from the stream.
	/// @throws IOException if something went wrong.
	public static ConnectionDoPart readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		ConnectionWhenPart whn = new ConnectionWhenPart(documentManager);
		whn.OBJECT_SEQU = inpt.readSEQU(whn);
		// *** SyntaxElement

		// *** ConnectionDoPart
		whn.classIdentifier = inpt.readIdentifier();
		whn.connectionStatement = (ConnectionStatement) inpt.readObj(documentManager);
		whn.connectionBlock = (ConnectionBlock) inpt.readObj(documentManager);
		Util.TRACE_INPUT("ConnectionDoPart: " + whn);
		return(whn);
	}

}
