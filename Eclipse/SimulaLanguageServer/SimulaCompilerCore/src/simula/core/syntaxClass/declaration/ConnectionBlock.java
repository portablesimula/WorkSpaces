/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.declaration;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.constant.ClassDesc;

import simula.Option;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.token.Identifier;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.expression.Expression;
import simula.core.syntaxClass.expression.TypeConversion;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.syntaxClass.statement.ConnectionStatement;
import simula.core.syntaxClass.statement.Statement;
import simula.core.utilities.CoreGlobal;
import simula.core.utilities.DeclarationList;
import simula.core.utilities.LOG;
import simula.core.utilities.LabelList;
import simula.core.utilities.Meaning;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// Connection Block.
/// 
/// A Connection Block is a Statement within a Connection Statement.
/// It acts as a block, whether it takes the form of a block or not.
/// It further acts as if enclosed by a second fictitious block, called a
/// "connection block". During the execution of a connection block the object X is said to be
/// "connected".
/// 
/// See Simula Standard 4.8 Connection statement.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/declaration/ConnectionBlock.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class ConnectionBlock extends DeclarationScope {
	/// The Connection Statement.
	public Statement statement;
	
	/// When clause class identifier.
	private Identifier whenClassIdentifier;
	
	/// When clause class Declaration. Set during checking.
	private Declaration whenClassDeclaration; // Set during checking

	/// The inspected variable.
	public VariableExpression inspectedVariable;

	/// The connected ident.
	/// E.g: the ident in the Java statement:
	/// if(_inspect_7 instanceof RTS_Infile connID) 
	public String connID;

	/// The when class declaration. Set during checking.
	public ClassDeclaration classDeclaration;

	/// Create a new ConnectionBlock.
	/// @param inspectedVariable   the inspected variable
	/// @param whenClassIdentifier the when class identifier
	public ConnectionBlock(final SimulaBuilder simBuilder, final VariableExpression inspectedVariable, final Identifier whenClassIdentifier) {
//		super("Connection block at line " + (Global.sourceLineNumber - 1));
		super(simBuilder, new Identifier("Inspect " + inspectedVariable));
		declarationKind = ObjectKind.ConnectionBlock;
		this.inspectedVariable = inspectedVariable;
		this.whenClassIdentifier = whenClassIdentifier;
		// Set External Identifier
		externalIdent = inspectedVariable.identifier.value;
	}

	/// Get inspected variable.
	/// @return inspected variable.
	public Expression getTypedInspectedVariable() {
		Type type = classDeclaration.type;
		return ((Expression) TypeConversion.testAndCreate(type, inspectedVariable));
	}

	/// Connection block end.
	public void end() {
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("END ConnectionBlock: " + this.edScopeChain());
		if (labelList != null && labelList.declaredLabelSize() != 0)
			MaybeBlockDeclaration.moveLabelsFrom(this); // Label is also declaration
		CoreGlobal.setScope(declaredIn);
	}

	/// Set the Connected ClassDeclaration.
	/// @param classDeclaration the Connected ClassDeclaration.
	public void setClassDeclaration(final ClassDeclaration classDeclaration) {
		this.classDeclaration = classDeclaration;
	}

	/// Set the Connection Statement.
	/// @param statement the Connection Statement
	public void setStatement(final Statement statement) {
		this.statement = statement;
	}

	@Override
	public Meaning findMeaning(final Identifier identifier) {
		if(Option.internal.TRACE_FIND_MEANING > 0)
			LOG.trace("ConnectionBlock.findMeaning("+identifierValue()+"): BEGIN Search "+identifierValue());
		if (classDeclaration == null && simBuilder.duringParsing) {
			return (null); // Still in Pass1(Parser)
		}
		Meaning result = null;
		if (classDeclaration != null) {
			if(Option.internal.TRACE_FIND_MEANING > 0)
				LOG.trace("ConnectionBlock.findMeaning: BEFORE Search " + classDeclaration);
			result = classDeclaration.findRemoteAttributeMeaning(identifier);
			if(Option.internal.TRACE_FIND_MEANING > 0)
				LOG.trace("ConnectionBlock.findMeaning: AFTER Search " + classDeclaration + "  RESULT="+result);
		}
		if (result != null) {
			result.declaredIn = this;
		} else if (declaredIn != null) {
			if(Option.internal.TRACE_FIND_MEANING > 0)
				LOG.trace("ConnectionBlock.findMeaning: BEFORE Search " + declaredIn);
			result = declaredIn.findMeaning(identifier);
			if(Option.internal.TRACE_FIND_MEANING > 0)
				LOG.trace("ConnectionBlock.findMeaning: AFTER Search " + declaredIn + "  RESULT="+result);
		}
		if (result == null) {
//			Util.error("Undefined variable: " + identifierValue());
			UndefinedDeclaration undef = new UndefinedDeclaration(null, identifier);
			result = new Meaning(undef, this); // Error Recovery
		}
		if (Option.internal.TRACE_FIND_MEANING > 0)
			LOG.trace("ConnectionBlock.findVisibleAttributeMeaning("+identifierValue()+"): ENDOF Search "+identifierValue()+" ========= RESULT: " + result);
		return (result);
	}

	// ***********************************************************************************************
	// *** Utility: findVisibleAttributeMeaning
	// ***********************************************************************************************
	@Override
	public Meaning findVisibleAttributeMeaning(final Identifier ident) {
		if(Option.internal.TRACE_FIND_MEANING > 1)
			LOG.trace("ConnectionBlock.findVisibleAttributeMeaning: BEGIN Search "+identifierValue()+" for "+ident.value+" ================================== "+identifierValue()+" ==================================");
		for (Declaration declaration : declarationList) {
			if(Option.internal.TRACE_FIND_MEANING > 2) LOG.trace("ConnectionBlock.findVisibleAttributeMeaning: Checking Local " + declaration);
			if (Util.equals(ident, declaration.identifier))
				return (new Meaning(declaration, this, this, false));
		}
		if(labelList != null) for (LabelDeclaration label : labelList.getDeclaredLabels()) {
			if(Option.internal.TRACE_FIND_MEANING > 2) LOG.trace("ConnectionBlock.findVisibleAttributeMeaning: Checking Label " + label);
			if (Util.equals(ident, label.identifier))
				return (new Meaning(label, this, this, false));
		}
		if(Option.internal.TRACE_FIND_MEANING > 1)
			LOG.trace("ConnectionBlock.findVisibleAttributeMeaning: ENDOF Search "+ident.value+" ========== NOT FOUND ============= "+identifierValue()+" ==================================");
		return (null);
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		CoreGlobal.enterScope(this);
		if (whenClassIdentifier != null) {
			Meaning meaning = findMeaning(whenClassIdentifier);
			whenClassDeclaration = meaning.declaredAs;
			connID = ConnectionStatement.getUniqueConnID();
		}
		statement.doChecking();
		CoreGlobal.exitScope();
		SET_SEMANTICS_CHECKED();
	}
	
	@Override
	public int getRTBlockLevel() {
//		ASSERT_SEMANTICS_CHECKED();
		int rtBlockLevel = declaredIn.getRTBlockLevel();
		return rtBlockLevel;
	}

	@Override
	public void doJavaCoding() {
		CoreGlobal.sourceLineNumber = firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		CoreGlobal.enterScope(this);
		JavaSourceFileCoder.code("{");
		statement.doJavaCoding();
		JavaSourceFileCoder.code("}");
		CoreGlobal.exitScope();
	}

	@Override
	public String toJavaCode() {
		Declaration when = whenClassDeclaration;
		if (when == null)
			return (inspectedVariable.toJavaCode());
		return (connID);
	}
	
	@Override
	public ClassDesc getClassDesc() {
		return(inspectedVariable.type.toClassDesc());
	}

	public void buildByteCode(CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		CoreGlobal.enterScope(this);
		statement.buildByteCode(codeBuilder);
		CoreGlobal.exitScope();
	}

	// ***********************************************************************************************
	// *** Printing Utility: print
	// ***********************************************************************************************
	@Override
	public void print(final int indent) {
		String spc = edIndent(indent);
		StringBuilder s = new StringBuilder(indent);
		s.append('[').append(sourceBlockLevel).append(':').append(getRTBlockLevel()).append("] ");
		s.append(ObjectKind.edit(declarationKind)).append(' ').append(identifierValue());
		IO.println(s.toString());
		String beg = "begin[" + edScopeChain() + ']';
		IO.println(spc + beg);
		for (Declaration decl : declarationList)
			decl.print(indent + 1);
		if(statement != null) statement.print(indent + 1);
		IO.println(spc + "end[" + edScopeChain() + ']');
	}

	// ***********************************************************************************************
	// *** Printing Utility: printTree
	// ***********************************************************************************************
	@Override
	public void printTree(final int indent) {
		// verifyTree(head);
		String tail = (IS_SEMANTICS_CHECKED()) ? "  BL=" + getRTBlockLevel() : "";
		if(isPreCompiledFromFile != null) tail = tail + " From: " + isPreCompiledFromFile;
		IO.println(edTreeIndent(indent) + "CONNECTION " + identifierValue() + tail + "  PrefixLevel=" + prefixLevel() + "  declaredIn="+this.declaredIn);
		printDeclarationList(indent + 1);
		statement.printTree(indent + 1);
		IO.println(edTreeIndent(indent)+"END CONNECTION "+identifierValue());
	}

	@Override
	public String toString() {
//		return ("ConnectionBlock: Inspect(" + inspectedVariable + ") do " + statement);
		return ("ConnectionBlock: " + inspectedVariable);
	}

	@Override
	public byte[] buildClassFile() {
		return null;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	/// @param identifier the block identifier.
	public ConnectionBlock(Identifier identifier) {
		super(null, identifier);
		declarationKind = ObjectKind.ConnectionBlock;
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write ConnectionBlock: "+identifierValue());
		oupt.writeKind(declarationKind); // Mark: This is a ConnectionBlock
		oupt.writeIdentifier(identifier);
		oupt.writeShort(OBJECT_SEQU);
		
		// *** SyntaxElement
		writeAstData(oupt);
		
		// *** Declaration
		//oupt.writeIdentifier(identifier);
		oupt.writeString(externalIdent);
		oupt.writeType(type);
		oupt.writeObj(declaredIn);
		
		// *** DeclarationScope
		oupt.writeString(sourceFileName);
		oupt.writeBoolean(hasLocalClasses);
		LabelList.writeLabelList(labelList, oupt);
		DeclarationList decls = prep(declarationList);
		decls.writeObject(oupt);
		
		// *** ConnectionBlock
		oupt.writeObj(statement);
		oupt.writeIdentifier(whenClassIdentifier);
		oupt.writeObj(inspectedVariable);

		Util.TRACE_OUTPUT("END Write ConnectionBlock: "+identifierValue());
	}

	/// Read and return a ConnectionBlock object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static ConnectionBlock readObject(AttributeInputStream inpt) throws IOException {
		Identifier identifier = inpt.readIdentifier();
		ConnectionBlock blk = new ConnectionBlock(identifier);
		blk.OBJECT_SEQU = inpt.readSEQU(blk);
		
		// *** SyntaxElement
		blk.astData = readAstData(inpt);

		// *** Declaration
		//blk.identifier = inpt.readIdentifier();
		blk.externalIdent = inpt.readString();
		blk.type = inpt.readType();
		blk.declaredIn = (DeclarationScope) inpt.readObj();

		// *** DeclarationScope
		blk.sourceFileName = inpt.readString();
		blk.hasLocalClasses = inpt.readBoolean();
		blk.labelList = LabelList.readLabelList(inpt);
		blk.declarationList = DeclarationList.readObject(inpt);
		
		// *** ConnectionBlock
		blk.statement = (Statement) inpt.readObj();
		blk.whenClassIdentifier = inpt.readIdentifier();
		blk.inspectedVariable = (VariableExpression) inpt.readObj();

		blk.isPreCompiledFromFile = inpt.jarFileName;
		Util.TRACE_INPUT("END Read ConnectionBlock: " + identifier.value + ", Declared in: "+blk.declaredIn);
		CoreGlobal.setScope(blk.declaredIn);
		return(blk);
	}


}
