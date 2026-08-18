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
import java.lang.constant.MethodTypeDesc;

import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.token.Identifier;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.ProtectedSpecification;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.expression.Constant;
import simula.core.syntaxClass.expression.Expression;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.RTS;
import simula.core.utilities.Util;

/// Label Declaration.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/declaration/LabelDeclaration.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class LabelDeclaration extends SimpleVariableDeclaration {
	/// The label index. Set by BlockDeclaration.doAccumLabels.
	public int index;

	/// Special case: Labels in a CompoundStatement or ConnectionBlock are moved to
	/// nearest enclosing Block which is not a CompoundStatement or ConnectionBlock.
	public DeclarationScope movedTo;
	
	/// Indicates that codeBuilder.labelBinding is called.
	public boolean isBinded;
	

	/// Create a new Label Declaration.
	/// 
	/// @param identifier label identifier
	public LabelDeclaration(final DocumentManager documentManager, final Identifier identifier) {
		super(documentManager, Type.Label, identifier);
		this.externalIdent = "_LABEL_" + identifierValue();
		this.declarationKind = ObjectKind.LabelDeclaration;
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		DeclarationScope declaredIn = CoreGlobal.getCurrentScope();
		this.externalIdent = "_LABEL_" + declaredIn.externalIdent + '_' + identifierValue() + '_' + declaredIn.prefixLevel();
		type.doChecking(declaredIn, this);
		VirtualSpecification virtSpec = VirtualSpecification.getVirtualSpecification(this);
		if (virtSpec == null) {
			// Label attributes are implicit specified 'protected'
			if (declaredIn.declarationKind == ObjectKind.Class)
				((ClassDeclaration) declaredIn).protectedList
						.add(new ProtectedSpecification(documentManager, (ClassDeclaration) declaredIn, identifier));
		} else {
			// This Label is a Virtual Match
			ClassDeclaration decl = (ClassDeclaration) declaredIn;
			if (decl == virtSpec.declaredIn)
				virtSpec.hasDefaultMatch = true;
		}
		SET_SEMANTICS_CHECKED();
	}
	
	/// Declare a local Label.
	/// @param encloser the BlockDeclaration to update.
	public void declareLocalLabel(final SimulaCoder simCoder, final BlockDeclaration encloser) {
		CoreGlobal.sourceLineNumber = firstLineNumber();
		String ident = getJavaIdentifier();
		int prefixLevel=0;
		if(movedTo != null) {
			if(movedTo instanceof ClassDeclaration cls) prefixLevel=cls.prefixLevel();
		} else {
			if(declaredIn instanceof ClassDeclaration cls) prefixLevel=cls.prefixLevel();			
		}
		VirtualSpecification virtSpec = VirtualSpecification.getVirtualSpecification(this);
		if (virtSpec != null) {
			if(this.isLatestVirtualLabel(encloser)) {
				JavaSourceFileCoder.code(simCoder,"    public RTS_LABEL " + virtSpec.getVirtualIdentifier()
					+ " { return(new RTS_LABEL(this," + prefixLevel + ',' + index + ",\"" + identifierValue() + "\")); }",
					" // Virtual Label #" + index + '=' + identifierValue() + " At PrefixLevel " + prefixLevel);
			}
		} else {
			JavaSourceFileCoder.code(simCoder,
					"final RTS_LABEL " + ident + "=new RTS_LABEL(this," +prefixLevel + ',' + index + ",\"" + identifierValue() + "\");",
					"Local Label #" + index + '=' + identifierValue() + " At PrefixLevel " + prefixLevel);
		}
	}

	@Override
	public void buildDeclaration(final SimulaCoder simCoder, final ClassBuilder classBuilder, final BlockDeclaration encloser) {
		String ident = getFieldIdentifier();
		int prefixLevel = getPrefixLevel();
		
		VirtualSpecification virtSpec = VirtualSpecification.getVirtualSpecification(this);
		if (virtSpec != null) {
			if(this.isLatestVirtualLabel(encloser)) {
				MethodTypeDesc MTD_STM=MethodTypeDesc.ofDescriptor("()Lsimula/runtime/RTS_LABEL;");
				classBuilder
					.withMethodBody(virtSpec.getSimpleVirtualIdentifier(), MTD_STM, ClassFile.ACC_PUBLIC,
						codeBuilder -> buildVirtualMatchMethodBody(prefixLevel,codeBuilder));
			}
		} else {
			classBuilder.withField(ident, RTS.CD.RTS_LABEL, ClassFile.ACC_PUBLIC);
		}
	}
	
	/// Check if this label is the last label in the owner's label list.
	/// @param encloser the owner.
	/// @return true: if this label is the last label in the owner's label list.
	private boolean isLatestVirtualLabel(DeclarationScope encloser) {
		LabelDeclaration last = encloser.labelList.getLastDeclaredLabel(this.identifierValue());
		if(this.index == last.index) {
			return true;
		}
		return false;
	}
	
	/// Returns the prefix level.
	/// @return the prefix level.
	private int getPrefixLevel() {
		int prefixLevel=0;
		if(movedTo != null) {
			if(movedTo instanceof ClassDeclaration cls) prefixLevel=cls.prefixLevel();
		} else {
			if(declaredIn instanceof ClassDeclaration cls) prefixLevel=cls.prefixLevel();			
		}
		return prefixLevel;
	}
	
	/// ClassFile coding utility: Build Virtual Match Method Body.
	/// @param prefixLevel the prefix level.
	/// @param codeBuilder the codeBuilder to use.
	private void buildVirtualMatchMethodBody(int prefixLevel,CodeBuilder codeBuilder) {
		ConstantPoolBuilder pool=codeBuilder.constantPool();
		// Build virtual match method:
		// public RTS_LABEL " + virtSpec.getVirtualIdentifier()
		// { return(new RTS_LABEL(this, prefixLevel, index, "identifier")); }
		codeBuilder
			.new_(RTS.CD.RTS_LABEL)
			.dup()
			.aload(0); // this
		Constant.buildIntConst(codeBuilder, prefixLevel);
		Constant.buildIntConst(codeBuilder, index);
		codeBuilder.ldc(pool.stringEntry(this.identifierValue()));
		codeBuilder
			.invokespecial(RTS.CD.RTS_LABEL, "<init>", MethodTypeDesc.ofDescriptor("(Lsimula/runtime/RTS_RTObject;IILjava/lang/String;)V"))
			.areturn();
	}


	@Override
	public FieldRefEntry getFieldRefEntry(ConstantPoolBuilder pool) {
		DeclarationScope declaredIn = (movedTo != null)? movedTo : this.declaredIn;
		ClassDesc owner=declaredIn.getClassDesc();
		return(pool.fieldRefEntry(owner, getFieldIdentifier(), RTS.CD.RTS_LABEL));
	}
	
	@Override
	public String getFieldIdentifier() {
		return(this.externalIdent);
	}

	public void buildInitAttribute(CodeBuilder codeBuilder) {
		VirtualSpecification virtSpec = VirtualSpecification.getVirtualSpecification(this);
		if (virtSpec == null) {
			ConstantPoolBuilder pool=codeBuilder.constantPool();
			buildLabelQuant(codeBuilder);
			codeBuilder.putfield(getFieldRefEntry(pool));
		}
	}
	
	/// Build binding for this Label.
	/// @param codeBuilder the codeBuilder to use.
	public void doBind(CodeBuilder codeBuilder) {
		if(isBinded) Util.IERR();
		BlockDeclaration labelContext = BlockDeclaration.labelContext;
		labelContext.labelList.labelBinding(this,codeBuilder);
		isBinded = true;
	}
	
	/// Build Label Quantity
	/// @param codeBuilder the codeBuilder to use
	public void buildLabelQuant(CodeBuilder codeBuilder) {
		int prefixLevel=0;
		if(movedTo != null) {
			if(movedTo instanceof ClassDeclaration cls) prefixLevel=cls.prefixLevel();
		} else {
			if(declaredIn instanceof ClassDeclaration cls) prefixLevel=cls.prefixLevel();			
		}

		// new RTS_LABEL(this,0,1,"L1"); // Local Label #1=L1 At PrefixLevel 0
		codeBuilder
			.aload(0)
			.new_(RTS.CD.RTS_LABEL)
			.dup()
			.aload(0); // this
		Constant.buildIntConst(codeBuilder, prefixLevel);
		Constant.buildIntConst(codeBuilder, index);
		codeBuilder
			.ldc(codeBuilder.constantPool().stringEntry(identifierValue()))
			.invokespecial(RTS.CD.RTS_LABEL, "<init>", MethodTypeDesc.ofDescriptor("(Lsimula/runtime/RTS_RTObject;IILjava/lang/String;)V"));
	}

	@Override
	public String toString() {
		String comment = "DeclaredIn: "+declaredIn.identifier;
		if(movedTo != null) comment = comment+" -> "+movedTo;
		return ("LABEL " + identifierValue() + '[' + externalIdent + ']' + ", index=" + index + " IN " + comment);
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeLabelDeclaration: " + identifierValue());
		oupt.writeKind(declarationKind);
		oupt.writeIdentifier(identifier);
		oupt.writeShort(OBJECT_SEQU);

		// *** SyntaxElement
		writeAstData(oupt);

		// *** Declaration
		oupt.writeIdentifier(identifier);
		oupt.writeString(externalIdent);
		oupt.writeType(type);// Declaration
		
		// *** SimpleVariableDeclaration
		oupt.writeBoolean(constant);
		oupt.writeObj(constantElement);

		// *** LabelDeclaration
		oupt.writeShort(index);
		oupt.writeObj(movedTo);
	}
	
	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static LabelDeclaration readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		Identifier identifier = inpt.readIdentifier();
		LabelDeclaration lab = new LabelDeclaration(documentManager, identifier);
		lab.OBJECT_SEQU = inpt.readSEQU(lab);

		// *** SyntaxElement
		lab.astData = readAstData(inpt);

		// *** Declaration
		lab.identifier = inpt.readIdentifier();
		lab.externalIdent = inpt.readString();
		lab.type = inpt.readType();
		
		// *** SimpleVariableDeclaration
		lab.constant = inpt.readBoolean();
		lab.constantElement = (Expression) inpt.readObj(documentManager);

		// *** LabelDeclaration
		lab.index = inpt.readShort();
		lab.movedTo = (DeclarationScope) inpt.readObj(documentManager);
		Util.TRACE_INPUT("readLabelDeclaration: " + lab);
		return(lab);
	}

}
