/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.declaration.BlockDeclaration;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.PrefixedBlockDeclaration;
import simula.core.syntaxClass.expression.Expression;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// BlockStatement.
/// <pre>
/// Simula Standard: 4.10 Blocks
/// 
///   block
///      = subblock
///      | prefixed-block
///      
///         subblock = BEGIN declaration { ; declaration } ; statement { ; statement } END
///         
///         prefixed-block
///            = block-prefix main-block
///            
///            block-prefix
///               = class-identifier [ actual-parameter-part ]
///               
///            main-block
///            
///               = block
///               | BEGIN statement { ; statement } END
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/BlockStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class BlockStatement extends Statement {
//	public String debugName;
	
	/// The associated block declaration.
	public BlockDeclaration blockDeclaration;

//	private static int SEQU = 1;
	/// Create a new BlockStatement.
	/// @param blockDeclaration the BlockDeclaration
	public BlockStatement(final DocumentManager documentManager, final BlockDeclaration blockDeclaration, String debugName1) {
		super(documentManager);
//		SimulaBuilder simBuilder = documentManager.simBuilder;
//		debugName = "ZZZ_BlockStatement: "+SEQU++;
		this.blockDeclaration = blockDeclaration;
		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": BlockStatement: "+this);
//		IO.println("NEW BlockStatement: " + debugName + '[' + debugName1 + ']');
	}
	
	public String psiKind() {
		return ObjectKind.edit(blockDeclaration.declarationKind);
	}

	/// Check if this BlockStatement is a CompoundStatement.
	/// @return true if this BlockStatement is a CompoundStatement
	boolean isCompoundStatement() {
		return(blockDeclaration.declarationKind == ObjectKind.CompoundStatement);
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		blockDeclaration.doChecking();
		SET_SEMANTICS_CHECKED();
	}

	/// Add a leading label to this BlockStatement.
	/// @param labelcode the label code
	void addLeadingLabel(String labelcode) {
		blockDeclaration.addLeadingLabel(labelcode);
	}
	
	@Override
	public void doJavaCoding(final SimulaCoder simCoder) {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		if(blockDeclaration.declarationKind!=ObjectKind.CompoundStatement) {
			String staticLink=blockDeclaration.declaredIn.edCTX();
			StringBuilder s = new StringBuilder();
			s.append("new ").append(blockDeclaration.getJavaIdentifier()).append('(');
			s.append(staticLink);
			if(blockDeclaration instanceof PrefixedBlockDeclaration pref) {
				VariableExpression blockPrefix=pref.blockPrefix;
				if(blockPrefix.hasArguments() && blockPrefix.checkedParams != null)
					for (Expression par:blockPrefix.checkedParams) {
						s.append(',').append(par.toJavaCode());
					}
			}
			s.append(')');
			if(blockDeclaration.declarationKind==ObjectKind.PrefixedBlock && ((ClassDeclaration)blockDeclaration).isDetachUsed())
				s.append("._START();");
			else s.append("._STM();");
			JavaSourceFileCoder.code(simCoder,s.toString());
		}
		boolean duringSTM_Coding = simCoder.duringSTM_Coding;
		simCoder.duringSTM_Coding=false;
		blockDeclaration.doJavaCoding(simCoder);
		simCoder.duringSTM_Coding=duringSTM_Coding;
	}

	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		blockDeclaration.buildByteCode(simCoder, codeBuilder);
	}

	@Override
	public void print(final int indent) {
		blockDeclaration.print(indent);
	}
	
	@Override
	public void printTree(final int indent) {
		blockDeclaration.printTree(indent);
	}
	
	@Override
	public String toString() {
		return "BlockStatement: " + blockDeclaration.getClass().getSimpleName() + " " + blockDeclaration;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Default constructor used by Attribute File I/O
	private BlockStatement(final DocumentManager documentManager) {
		super(documentManager);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeBlockStatement: " + this);
		oupt.writeKind(ObjectKind.BlockStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		
		// *** BlockStatement
		oupt.writeObj(blockDeclaration);
	}

	/// Read and return a BlockStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the BlockStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static BlockStatement readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		BlockStatement stm = new BlockStatement(documentManager);
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		
		// *** BlockStatement
		stm.blockDeclaration = (BlockDeclaration) inpt.readObj(documentManager);

		Util.TRACE_INPUT("BlockStatement: " + stm);
		return(stm);
	}

}
