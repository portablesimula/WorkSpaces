/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.declaration;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.attribute.SourceFileAttribute;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.CoreGlobal2;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.token.Identifier;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.HiddenSpecification;
import simula.core.syntaxClass.ProtectedSpecification;
import simula.core.syntaxClass.expression.Expression;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.syntaxClass.statement.Statement;
import simula.core.utilities.ClassHierarchy;
import simula.core.utilities.DeclarationList;
import simula.core.utilities.LabelList;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.ObjectList;
import simula.core.utilities.RTS;
import simula.core.utilities.Util;

/// Prefixed Block Declaration.
/// <pre>
/// Simula Standard: 4.10.1 Prefixed blocks
/// 
///  prefixed-block = block-prefix main-block
///  
///     block-prefix = class-identifier [ actual-parameter-part ]
///     
///     main-block
///        = block
///        | compound-statement
///        
///       actual-parameter-part = "(" actual-parameter { , actual-parameter } ")"
///       
///          actual-parameter = expression
///                           | array-identifier-1
///                           | switch-identifier
///                           | procedure-identifier-1
///          
///          compound-statement = BEGIN statement { ; statement } END
/// 
/// </pre>
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/declaration/PrefixedBlockDeclaration.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class PrefixedBlockDeclaration extends ClassDeclaration {
	
	/// The block prefix.
	public VariableExpression blockPrefix;

	// ***********************************************************************************************
	// *** CONSTRUCTOR
	// ***********************************************************************************************
	/// PrefixedBlock.
	/// @param isMainModule true: this is the main module.
	private PrefixedBlockDeclaration(final DocumentManager documentManager, final boolean isMainModule) {
		super(documentManager, null);
//		if(isMainModule)
//			modifyIdentifier(Global.sourceName);
//		else modifyIdentifier("PBLK" + firstLineNumber());
	}

	// ***********************************************************************************************
	// *** Expect Prefixed Block
	// ***********************************************************************************************
	/// Parse Utility: Expect PrefixedBlockDeclaration
	/// @param blockPrefix the block prefix
	/// @param isMainModule true if main module
	/// @return the resulting PrefixedBlockDeclaration
	public static PrefixedBlockDeclaration expectPrefixedBlock(final SimulaBuilder simBuilder, final VariableExpression blockPrefix,boolean isMainModule) {
		PrefixedBlockDeclaration block=new PrefixedBlockDeclaration(simBuilder.documentManager, isMainModule);
		block.declarationKind=ObjectKind.PrefixedBlock;
		Util.ASSERT(blockPrefix != null,"blockPrefix == null");
		block.blockPrefix = blockPrefix;
		block.prefix = blockPrefix.identifier;
		block.isMainModule=isMainModule;
		String ID = (isMainModule)? simBuilder.documentManager.sourceName : block.prefix.value + "_Begin";
		block.modifyIdentifier(new Identifier(ID));
		if (Option.internal.TRACE_PARSE) Parse.TRACE("Parse PrefixedBlock");
		
		block.parseBlock(simBuilder);
		
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Line "+block.firstLineNumber()+": PrefixedBlockDeclaration: "+block);
		CoreGlobal.setScope(block.declaredIn);
		return block;
	}

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		CoreGlobal.enterScope(this.declaredIn);
			blockPrefix.doChecking();
			prefix = blockPrefix.identifier;
//			getPrefixClass().doChecking();
			prefixClass = getPrefixClass();
			if(prefixClass == null) {
				Util.semanticError(this, "Prefix " + prefix.value + " is not a Class");
			} else {
				prefixClass.doChecking();
			}
			LabelList.accumLabelList(this);
		CoreGlobal.exitScope();
		
		CoreGlobal.enterScope(this);
			Util.ASSERT(parameterList.isEmpty(), "Invariant");
			Util.ASSERT(virtualSpecList.isEmpty(), "Invariant");
			Util.ASSERT(hiddenList.isEmpty(), "Invariant");
			Util.ASSERT(protectedList.isEmpty(), "Invariant");
	
			for (Declaration dcl : declarationList)	dcl.doChecking();
			for (Statement stm : statements) stm.doChecking();
		CoreGlobal.exitScope();
		SET_SEMANTICS_CHECKED();
	}

	// ***********************************************************************************************
	// *** Coding: doJavaCoding
	// ***********************************************************************************************
	@Override
	public void doJavaCoding(final SimulaCoder simCoder) {
		CoreGlobal.sourceLineNumber = firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		if (this.isPreCompiledFromFile != null) {
			if(CoreGlobal2.verbose) IO.println("Skip  doJavaCoding: " + this.identifierValue() + " -- It is read from " + isPreCompiledFromFile);	
			return;
		}
		JavaSourceFileCoder javaCoder = new JavaSourceFileCoder(simCoder, this);
		CoreGlobal.enterScope(this);
			labelList.setLabelIdexes();
			boolean duringSTM_Coding=simCoder.duringSTM_Coding;
			simCoder.duringSTM_Coding=false;
			JavaSourceFileCoder.code(simCoder,"@SuppressWarnings(\"unchecked\")");
			String line = "public final class " + getJavaIdentifier();
			if (prefix != null) {
				ClassDeclaration prefixClass = getPrefixClass();
				if(prefixClass != null) line = line + " extends " +prefixClass.getJavaIdentifier();
			}
			else line = line + " extends RTS_BASICIO";
			JavaSourceFileCoder.code(simCoder,line + " {");
			JavaSourceFileCoder.debug(simCoder,"// PrefixedBlockDeclaration: Kind=" + declarationKind + ", BlockLevel=" + getRTBlockLevel()
					+ ", firstLine=" + firstLineNumber() + ", lastLine=" + lastLineNumber() + ", hasLocalClasses="
					+ ((hasLocalClasses) ? "true" : "false") + ", System=" + ((isQPSystemBlock()) ? "true" : "false")
					+ ", detachUsed=" + ((detachUsed) ? "true" : "false"));
			if (isQPSystemBlock())
				JavaSourceFileCoder.code(simCoder,"public boolean isQPSystemBlock() { return(true); }");
			if (isDetachUsed())
				JavaSourceFileCoder.code(simCoder,"public boolean isDetachUsed() { return(true); }");
			JavaSourceFileCoder.debug(simCoder,"// Declare parameters as attributes");
			for (Parameter par : parameterList) {
				String tp = par.toJavaType();
				JavaSourceFileCoder.code(simCoder,"public " + tp + ' ' + par.externalIdent + ';');
			}
			if(this.hasAccumLabel()) {
				JavaSourceFileCoder.debug(simCoder,"// Declare local labels");
				for (LabelDeclaration lab : labelList.getAccumLabels())
					lab.declareLocalLabel(simCoder, this);
			}
			JavaSourceFileCoder.debug(simCoder,"// Declare locals as attributes");
			for (Declaration decl : declarationList) decl.doJavaCoding(simCoder);
			for (VirtualMatch match : virtualMatchList)	match.doJavaCoding(simCoder);
			doCodeConstructor(simCoder);
			simCoder.duringSTM_Coding=true;
			codeClassStatements(simCoder);
			simCoder.duringSTM_Coding=duringSTM_Coding;
	
			if (this.isMainModule) codeMethodMain(simCoder);
			
			javaCoder.codeProgramInfo(simCoder);
			JavaSourceFileCoder.code(simCoder,"}", "End of Class");
		CoreGlobal.exitScope();
		javaCoder.closeJavaOutput();
	}

	// ***********************************************************************************************
	// *** Coding Utility: doCodeConstructor
	// ***********************************************************************************************
	/// Coding Utility: Code the constructor.
	private void doCodeConstructor(final SimulaCoder simCoder) {
		JavaSourceFileCoder.debug(simCoder,"// Normal Constructor");
		JavaSourceFileCoder.code(simCoder,"public " + getJavaIdentifier() + edFormalParameterList());
		if (prefix != null) {
			ClassDeclaration prefixClass = this.getPrefixClass();
			if(prefixClass != null) JavaSourceFileCoder.code(simCoder,"super" + prefixClass.edCompleteParameterList());
		} else JavaSourceFileCoder.code(simCoder,"super(staticLink);");
		JavaSourceFileCoder.debug(simCoder,"// Parameter assignment to locals");
		for (Parameter par : parameterList)
			JavaSourceFileCoder.code(simCoder,"this." + par.externalIdent + " = s" + par.externalIdent + ';');
		JavaSourceFileCoder.debug(simCoder,"// Declaration Code");
		for (Declaration decl : declarationList) decl.doDeclarationCoding(simCoder);
		JavaSourceFileCoder.code(simCoder,"}");
	}

	
	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		if (this.isPreCompiledFromFile != null) {
			if(CoreGlobal2.verbose)
				IO.println("Skip  buildClassFile: " + this.identifierValue()+" extends " + this.prefix + " -- It is read from " + isPreCompiledFromFile);		
		} else {
			try { createJavaClassFile(simCoder); } catch (IOException e) { e.printStackTrace(); }
		}

		// ===================================================
		//  new adHoc05_PBLK14((_CUR), par1, ...)._STM();
		// ===================================================
		ConstantPoolBuilder pool=codeBuilder.constantPool();
		ClassDesc CD_pblk=this.getClassDesc();
		codeBuilder
			.new_(CD_pblk)
			.dup()
			.getstatic(RTS.FRE.RTObject_CUR(pool));

		// Push parameters
		if(blockPrefix.checkedParams != null)
			for(Expression expr:blockPrefix.checkedParams)
				expr.buildEvaluation(simCoder, null, codeBuilder);

		codeBuilder.invokespecial(CD_pblk, "<init>", this.getConstructorMethodTypeDesc());

		// _STM();
		//         new adHoc00_PBLK4((_CUR))._START();
		if(isDetachUsed()) {
			RTS.invokevirtual_CLASS_START(codeBuilder);
		} else {
			codeBuilder.invokevirtual(CD_pblk,"_STM", MethodTypeDesc.ofDescriptor("()Lsimula/runtime/RTS_RTObject;"));
		}
		codeBuilder.pop();			
	}

	// ***********************************************************************************************
	// *** ByteCoding: buildClassFile
	// ***********************************************************************************************
	@Override
	public byte[] buildClassFile(final SimulaCoder simCoder) {
		labelList.setLabelIdexes();
		ClassDesc CD_ThisClass = currentClassDesc();
		ClassDesc CD_SuperClass = superClassDesc();
		if(CoreGlobal2.verbose) IO.println("Begin buildClassFile: PrefixecBlock " + CD_ThisClass + " extends " + CD_SuperClass);
		
		ClassHierarchy.addClassToSuperClass(CD_ThisClass, this.superClassDesc());
		
		byte[] bytes = ClassFile.of(ClassFile.ClassHierarchyResolverOption.of(ClassHierarchy.getResolver())).build(CD_ThisClass,
				classBuilder -> {
					classBuilder
						.with(SourceFileAttribute.of(simCoder.documentManager.sourceFileName))
						.withFlags(ClassFile.ACC_PUBLIC + ClassFile.ACC_SUPER)
						.withSuperclass(this.superClassDesc());

					if(this.hasAccumLabel())
						for (LabelDeclaration lab : labelList.getAccumLabels())
							lab.buildDeclaration(simCoder, classBuilder,this);
					
					for (Declaration decl : declarationList)
						decl.buildDeclaration(simCoder, classBuilder,this);
					
					for(Parameter par:parameterList)
						par.buildDeclaration(simCoder, classBuilder,this);
					
					for (VirtualSpecification virtual : virtualSpecList)
						if (!virtual.hasDefaultMatch)
							virtual.buildMethod(classBuilder);
					
					for (VirtualMatch match : virtualMatchList)
						match.buildMethod(classBuilder);

					classBuilder
						.withMethodBody("<init>", MethodTypeDesc.ofDescriptor(edConstructorSignature()), ClassFile.ACC_PUBLIC,
							codeBuilder -> buildConstructor(simCoder, codeBuilder))
						.withMethodBody("_STM", MethodTypeDesc.ofDescriptor("()Lsimula/runtime/RTS_RTObject;"), ClassFile.ACC_PUBLIC,
							codeBuilder -> buildMethod_STM(simCoder, codeBuilder) );
					
					if (isQPSystemBlock())
						classBuilder
							.withMethodBody("isQPSystemBlock", MethodTypeDesc.ofDescriptor("()Z"), ClassFile.ACC_PUBLIC,
								codeBuilder -> buildIsQPSystemBlock(codeBuilder));
					
					if (isDetachUsed())
						classBuilder
							.withMethodBody("isDetachUsed", MethodTypeDesc.ofDescriptor("()Z"), ClassFile.ACC_PUBLIC,
								codeBuilder -> buildIsMethodDetachUsed(codeBuilder));
					
					if (this.isMainModule)
						classBuilder
							.withMethodBody("main", MethodTypeDesc.ofDescriptor("([Ljava/lang/String;)V"),
									ClassFile.ACC_PUBLIC + ClassFile.ACC_STATIC + ClassFile.ACC_VARARGS,
								codeBuilder -> buildMethodMain(simCoder, codeBuilder));
				}
		);
		return(bytes);
	}


	// ***********************************************************************************************
	// *** Printing Utility: print
	// ***********************************************************************************************
	@Override
	public void print(final int indent) {
    	String spc=edIndent(indent);
		StringBuilder s = new StringBuilder(spc);
		s.append('[').append(sourceBlockLevel).append(':').append(getRTBlockLevel()).append("] ");
		if (prefix != null)	s.append(prefix).append(' ');
		s.append(ObjectKind.edit(declarationKind)).append(' ').append(identifierValue());
		s.append('[').append(externalIdent).append("] ");
		s.append(Parameter.editParameterList(parameterList));
		IO.println(s.toString());
		String beg = "begin[" + edScopeChain() + ']';
		IO.println(spc + beg);
		for (Declaration decl : declarationList) decl.print(indent + 1);
		for (Statement stm : statements) stm.print(indent + 1);
		IO.println(spc + "end[" + edScopeChain() + ']');
	}
	
	@Override
	public void printTree(final int indent) {
		// verifyTree(head);
		String tail = (IS_SEMANTICS_CHECKED()) ? "  BL=" + getRTBlockLevel() : "";
		if(isPreCompiledFromFile != null) tail = tail + " From: " + isPreCompiledFromFile;
		IO.println(edTreeIndent(indent) + blockPrefix + " begin" + tail + " nParam=" + parameterList.size());
		if(labelList != null) labelList.printTree(indent + 1);
		for(Parameter p : parameterList) p.printTree(indent + 1);
		printDeclarationList(indent + 1);
		printStatementList(indent + 1);
	}

	@Override
	public String toString() {
		return ("PrefixedBlockDeclaration: " + identifierValue() + '[' + externalIdent + "] Kind=" + declarationKind + ", BlockPrefix=" + blockPrefix);
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Private Constructor used by Attribute File I/O.
	private PrefixedBlockDeclaration(final DocumentManager documentManager) {
		super(documentManager, null);
	}

	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("PrefixedBlockDeclaration: " + identifierValue() + ", Declared in: " + declaredIn);
		oupt.writeKind(declarationKind); // Mark: This is a PrefixedBlockDeclaration
		oupt.writeIdentifier(identifier);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		
		// *** Declaration
		//oupt.writeIdentifier(identifier);
		oupt.writeString(externalIdent);
		oupt.writeType(type);// Declaration
		
		// *** DeclarationScope
		oupt.writeString(sourceFileName);
		oupt.writeBoolean(hasLocalClasses);
		LabelList.writeLabelList(labelList, oupt);
		DeclarationList decls = prep(declarationList);
		decls.writeObject(oupt);

		// *** BlockDeclaration
		oupt.writeBoolean(isMainModule);
		oupt.writeObjectList(statements);
		
		// *** ClassDeclaration
		oupt.writeIdentifier(prefix);
		oupt.writeBoolean(detachUsed);
		oupt.writeObjectList(parameterList);
		oupt.writeObjectList(virtualSpecList);
		oupt.writeObjectList(hiddenList);
		oupt.writeObjectList(protectedList);
		oupt.writeObjectList(statements1);
		
		// *** PrefixedBlockDeclaration
		oupt.writeObj(blockPrefix);
	}

	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	@SuppressWarnings("unchecked")
	public static PrefixedBlockDeclaration readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		PrefixedBlockDeclaration pbl = new PrefixedBlockDeclaration(documentManager);
		pbl.identifier = inpt.readIdentifier();
		pbl.declarationKind = ObjectKind.Class;
		pbl.OBJECT_SEQU = inpt.readSEQU(pbl);
		// *** SyntaxElement
		pbl.astData = readAstData(inpt);

		// *** Declaration
		//pbl.identifier = inpt.readIdentifier();
		pbl.externalIdent = inpt.readString();
		pbl.type = inpt.readType();

		// *** DeclarationScope
		pbl.sourceFileName = inpt.readString();
		pbl.hasLocalClasses = inpt.readBoolean();
		pbl.labelList = LabelList.readLabelList(documentManager, inpt);
		pbl.declarationList = DeclarationList.readObject(documentManager, inpt);

		// *** BlockDeclaration
		pbl.isMainModule = inpt.readBoolean();
		pbl.statements = (ObjectList<Statement>) inpt.readObjectList(documentManager);
		
		// *** ClassDeclaration
		pbl.prefix = inpt.readIdentifier();
		pbl.detachUsed = inpt.readBoolean();
		pbl.parameterList = (ObjectList<Parameter>) inpt.readObjectList(documentManager);
		pbl.virtualSpecList = (ObjectList<VirtualSpecification>) inpt.readObjectList(documentManager);
		pbl.hiddenList = (ObjectList<HiddenSpecification>) inpt.readObjectList(documentManager);
		pbl.protectedList = (ObjectList<ProtectedSpecification>) inpt.readObjectList(documentManager);
		pbl.statements1 = (ObjectList<Statement>) inpt.readObjectList(documentManager);
		
		// *** PrefixedBlockDeclaration
		pbl.blockPrefix = (VariableExpression) inpt.readObj(documentManager);
		
		pbl.isPreCompiledFromFile = inpt.jarFileName;
		Util.TRACE_INPUT("END Read PrefixedBlockDeclaration: " + pbl.identifierValue() + ", Declared in: " + pbl.declaredIn);
		CoreGlobal.setScope(pbl.declaredIn);
		return(pbl);
	}

}
