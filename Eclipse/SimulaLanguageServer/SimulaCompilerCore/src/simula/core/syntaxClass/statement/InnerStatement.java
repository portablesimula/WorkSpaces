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
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.SimulaBuilder;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.ObjectList;
import simula.core.utilities.Util;

/// Inner Statement.
/// 
/// <pre>
/// 
/// Syntax:
/// 
///   inner-statement = INNER
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/InnerStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class InnerStatement extends Statement {

	/// Create a new InnerStatement.
	/// @param line the source line number
	public InnerStatement(final SimulaBuilder simBuilder, boolean explicit) {
		super(simBuilder);
		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": InnerStatement: "+this);
//		IO.println("NEW InnerStatement: Line "+firstLineNumber()+": InnerStatement: "+this+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//		Thread.dumpStack();
		
		if(explicit) simBuilder.consume(KeyWord.INNER); //  (add it to tokenList)
		
		if(CoreGlobal.getCurrentScope() instanceof ClassDeclaration cls) {
			if(cls.statements1 != null) {
				Util.semanticError(this, "Multiple Inner Statements");
			} else {
				cls.statements1 = cls.statements;
				cls.statements = new ObjectList<Statement>();
			}
		} else Util.semanticError(this, "Missplaced Inner Statement");
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		CoreGlobal.sourceLineNumber=firstLineNumber();
		SET_SEMANTICS_CHECKED();
	}
	
	@Override
	public void doJavaCoding() {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		// No code !
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		// No code !
	}

	@Override
	public void print(final int indent) {
    	String spc=edIndent(indent);
		IO.println(spc+"inner"); 
	}
	
	@Override
	public void printTree(final int indent) {
		IO.println(edTreeIndent(indent)+"INNER ");
	}

	@Override
	public String toString() {
		return "INNER";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	public InnerStatement() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeInnerStatement: " + this);
		oupt.writeKind(ObjectKind.InnerStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
//		Util.IERR("");
	}

	/// Read and return an InnerStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the InnerStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static InnerStatement readObject(AttributeInputStream inpt) throws IOException {
		InnerStatement stm = new InnerStatement();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		stm.astData = readAstData(inpt);
		Util.TRACE_INPUT("InnerStatement: " + stm);
//		Util.IERR("");
		return(stm);
	}	

}
