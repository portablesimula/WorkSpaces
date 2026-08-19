/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.declaration;

import java.io.IOException;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.classfile.constantpool.FieldRefEntry;
import java.lang.constant.ClassDesc;

import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.util.Identifier;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.Type;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// Undefined Declaration.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/declaration/UndefinedDeclaration.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public class UndefinedDeclaration extends Declaration {

	/// Create a new UndefinedDeclaration.
	/// 
	/// @param identifier the variable identifier
	public UndefinedDeclaration(final DocumentManager documentManager, final Identifier identifier) {
		super(documentManager, identifier);
		this.declarationKind = ObjectKind.UndefinedDeclaration;
		this.type = Type.Undef;
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		if(type != null) type.doChecking(CoreGlobal.getCurrentScope(), this);
		
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doDeclarationCoding(final SimulaCoder simCoder) {
		// NOTHING
	}

	@Override
	public String toJavaCode() {
		ASSERT_SEMANTICS_CHECKED();
		String value = type.edDefaultValue();
		return ("public " + type.toJavaType() + ' ' + getJavaIdentifier() + '=' + value + ';');
	}

	
	/// ClassFile coding utility: get FieldRefEntry of this SimpleVariable.
	/// @param pool the ConstantPoolBuilder to use.
	/// @return the FieldRefEntry of this SimpleVariable.
	public FieldRefEntry getFieldRefEntry(ConstantPoolBuilder pool) {
		ClassDesc owner=declaredIn.getClassDesc();
		return(pool.fieldRefEntry(owner, getFieldIdentifier(), type.toClassDesc()));
	}
	
	@Override
	public String getFieldIdentifier() {
		return(this.externalIdent);
	}

	@Override
	public void buildDeclaration(final SimulaCoder simCoder, final ClassBuilder classBuilder, final BlockDeclaration encloser) {
		ClassDesc CD=type.toClassDesc();
		classBuilder.withField(getFieldIdentifier(), CD, ClassFile.ACC_PUBLIC);
	}

	@Override
	public void buildInitAttribute(CodeBuilder codeBuilder) {
		codeBuilder.aload(0);
		switch(type.keyWord) {
			case Type.T_BOOLEAN:
			case Type.T_CHARACTER:
			case Type.T_INTEGER:	codeBuilder.iconst_0(); break;
			case Type.T_LONG_REAL:	codeBuilder.dconst_0(); break;
			case Type.T_REAL:		codeBuilder.fconst_0(); break;
			case Type.T_TEXT:
			case Type.T_REF:
			case Type.T_PROCEDURE:
			case Type.T_LABEL:		codeBuilder.aconst_null(); break;
			default: Util.IERR();
		}
		codeBuilder
			.putfield(codeBuilder.constantPool().fieldRefEntry(BlockDeclaration.currentClassDesc(),this.getFieldIdentifier(), type.toClassDesc()));
	}

	@Override
	public void printTree(final int indent) {
		// verifyTree(head);
		IO.println(edTreeIndent(indent)+this);
	}

	@Override
	public String toString() {
		return "Undefined " + identifierValue() + " Type=" + type;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	public UndefinedDeclaration(final DocumentManager documentManager) {
		super(documentManager, null);
		this.declarationKind = ObjectKind.SimpleVariableDeclaration;
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("Undefined Variable: " + this);
		oupt.writeKind(declarationKind);
		oupt.writeShort(OBJECT_SEQU);

		// *** SyntaxElement
		

		// *** Declaration
		oupt.writeIdentifier(identifier);
		oupt.writeString(externalIdent);
		oupt.writeType(type);// Declaration
		oupt.writeObj(declaredIn);// Declaration
		
//		// *** SimpleVariableDeclaration
//		oupt.writeBoolean(constant);
//		oupt.writeObj(constantElement);
	}
	
	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static UndefinedDeclaration readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		UndefinedDeclaration var = new UndefinedDeclaration(documentManager);
		var.OBJECT_SEQU = inpt.readSEQU(var);

		// *** SyntaxElement
		

		// *** Declaration
		var.identifier = inpt.readIdentifier();
		var.externalIdent = inpt.readString();
		var.type = inpt.readType();
		var.declaredIn = (DeclarationScope) inpt.readObj(documentManager);
		
//		// *** SimpleVariableDeclaration
//		var.constant = inpt.readBoolean();
//		var.constantElement = (Expression) inpt.readObj();
		Util.TRACE_INPUT("Undefined Variable: " + var.OBJECT_SEQU + " " + var);
		return(var);
	}

}
