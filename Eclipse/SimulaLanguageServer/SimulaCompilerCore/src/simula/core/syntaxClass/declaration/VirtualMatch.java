/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.declaration;

import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassFile;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.constant.MethodTypeDesc;

import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.SimulaBuilder;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.RTS;

/// Virtual match.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/declaration/VirtualMatch.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class VirtualMatch extends Declaration {
	
	/// The matching ProcedureDeclaration. Set during doChecking.
	private ProcedureDeclaration match; // Set during doChecking

	/// The virtual specification. Set during doChecking.
	public VirtualSpecification virtualSpec; // Set during doChecking

	/// Create a new VirtualMatch.
	/// @param virtualSpec the virtual specification
	/// @param match a matching ProcedureDeclaration
	VirtualMatch(final SimulaBuilder simBuilder, final VirtualSpecification virtualSpec, final ProcedureDeclaration match) {
		super(simBuilder, virtualSpec.identifier);
		this.declarationKind = ObjectKind.VirtualMatch;
		// NOTE: Called during Checking
		this.virtualSpec = virtualSpec;
		this.match = match;
	}

	@Override
	public void doJavaCoding() {
		String matchCode = "{ throw new RTS_SimulaRuntimeError(\"No Virtual Match: " + identifierValue() + "\"); }";
		if (match != null)
			matchCode = "{ return(new RTS_PRCQNT(this," + match.getJavaIdentifier() + ".class)); }";
		JavaSourceFileCoder.code("    public RTS_PRCQNT " + virtualSpec.getVirtualIdentifier() + " " + matchCode);
	}

	/// Build virtual match method.
	/// @param classBuilder the classBuilder to use.
	public void buildMethod(ClassBuilder classBuilder) {
	    String ident=virtualSpec.getSimpleVirtualIdentifier();
		MethodTypeDesc MTD_STM=MethodTypeDesc.ofDescriptor("()Lsimula/runtime/RTS_PRCQNT;");
		classBuilder
			.withMethodBody(ident, MTD_STM, ClassFile.ACC_PUBLIC,
				codeBuilder -> {
					ConstantPoolBuilder pool=codeBuilder.constantPool();
					codeBuilder
						.new_(RTS.CD.RTS_PRCQNT)
						.dup()
						.aload(0)
						.ldc(pool.classEntry(match.getClassDesc()))
						.invokespecial(RTS.CD.RTS_PRCQNT, "<init>", MethodTypeDesc.ofDescriptor("(Lsimula/runtime/RTS_RTObject;Ljava/lang/Class;)V"))
						.areturn();	
				});
	}

	@Override
	public void printTree(final int indent) {
		IO.println(SyntaxElement.edIndent(indent)+this.getClass().getSimpleName()+"    "+this);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		if (match.type != null)
			s.append(match.type).append(' ');
		s.append("PROCEDURE ").append(match.identifierValue());
		if (virtualSpec != null)
			s.append("[Specified by ").append(virtualSpec.identifierValue()).append(']');
		return (s.toString());
	}

}
