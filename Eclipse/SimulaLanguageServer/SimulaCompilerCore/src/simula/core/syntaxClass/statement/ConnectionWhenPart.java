/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;

import simula.Option;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.token.Identifier;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.ConnectionBlock;
import simula.core.syntaxClass.expression.AssignmentOperation;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

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
	public ConnectionWhenPart(final SimulaBuilder simBuilder, final ConnectionStatement connectionStatement, final Identifier classIdentifier,final ConnectionBlock connectionBlock,final Statement statement) {
		super(simBuilder, connectionStatement, connectionBlock, statement);
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
			if(classDeclaration == null) Util.semanticError(this, "Illegal WHEN part: " + classIdentifier + " is not a class");
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
	public void doCoding(final boolean first) {
		ASSERT_SEMANTICS_CHECKED();
		String prfx = (first) ? "" : "else ";
		String cid = classDeclaration.getJavaIdentifier();
		if (impossibleWhenPart) {
			JavaSourceFileCoder.code(prfx,"WHEN " + cid + " DO -- IMPOSSIBLE REMOVED");
		} else {
			String cvar = this.connectionBlock.connID;
			JavaSourceFileCoder.code(prfx + "if(" + connectionStatement.inspectedVariable.toJavaCode() + " instanceof " + cid + "  " + cvar + ") {","WHEN "	+ cid + " DO ");
			connectionBlock.doJavaCoding();
			JavaSourceFileCoder.code("}");				
		}
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		if (!impossibleWhenPart) {
			connectionStatement.inspectedVariable.buildEvaluation(null, codeBuilder);
			Label elseLabel = codeBuilder.newLabel();
			codeBuilder
				.instanceOf(classDeclaration.getClassDesc())
				.ifeq(elseLabel);
			
			connectionBlock.buildByteCode(codeBuilder);
			
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
	private ConnectionWhenPart() {}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeWhenPart: " + this);
		oupt.writeKind(ObjectKind.ConnectionWhenPart);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** ConnectionWhenPart
		oupt.writeIdentifier(classIdentifier);
		oupt.writeObj(connectionStatement);
		oupt.writeObj(connectionBlock);
	}
	
	/// Read and return a ConnectionDoPart object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the ConnectionDoPart object read from the stream.
	/// @throws IOException if something went wrong.
	public static ConnectionDoPart readObject(AttributeInputStream inpt) throws IOException {
		ConnectionWhenPart whn = new ConnectionWhenPart();
		whn.OBJECT_SEQU = inpt.readSEQU(whn);
		// *** SyntaxElement
		whn.astData = readAstData(inpt);
		// *** ConnectionDoPart
		whn.classIdentifier = inpt.readIdentifier();
		whn.connectionStatement = (ConnectionStatement) inpt.readObj();
		whn.connectionBlock = (ConnectionBlock) inpt.readObj();
		Util.TRACE_INPUT("ConnectionDoPart: " + whn);
		return(whn);
	}

}
