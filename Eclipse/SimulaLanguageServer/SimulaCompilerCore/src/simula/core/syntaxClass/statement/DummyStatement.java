/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.SimulaBuilder;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// Dummy Statement.
/// 
/// <pre>
/// 
/// Simula Standard: 4.11 Dummy statement
/// 
///   dummy-statement = empty
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/DummyStatement.java"><b>Source File
/// </b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class DummyStatement extends Statement {
	
	/// Create a new DummyStatement.
	/// @param line the source line number
//	private DummyStatement(final int line) {
//		super(line);
//		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": DummyStatement: "+this);
//	}
	private DummyStatement(final SimulaBuilder simBuilder) {
		super(simBuilder);
	}

	public static DummyStatement ofExplicit(final SimulaBuilder simBuilder) {
		simBuilder.consume(KeyWord.SEMICOLON); //  (add it to tokenList)
		DummyStatement dummyStatement = new DummyStatement(simBuilder);		
		return dummyStatement;
	}

	public static DummyStatement ofImplicit(final SimulaBuilder simBuilder) {
		DummyStatement dummyStatement = new DummyStatement(simBuilder);		
		return dummyStatement;
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		// No Checking
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doJavaCoding() { /* No Coding */
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(";");
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
	}

	@Override
	public void print(final int indent) {
	}
	
	@Override
	public void printTree(final int indent) {
	}

	@Override
	public String toString() {
		return ";";
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
//	private DummyStatement() { super(0); }

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeDummyStatement: " + this);
		oupt.writeKind(ObjectKind.DummyStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
	}

	/// Read and return a DummyStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the DummyStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static DummyStatement readObject(AttributeInputStream inpt) throws IOException {
		DummyStatement stm = new DummyStatement(null);
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		stm.astData = readAstData(inpt);
		Util.TRACE_INPUT("DummyStatement: " + stm);
		return(stm);
	}

}
