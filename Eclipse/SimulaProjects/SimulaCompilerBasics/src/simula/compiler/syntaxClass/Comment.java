/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass;

import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.AstBuilder;

/// Comment or White Spaces.
/// 
/// <pre>
/// 
/// Simula Standard: 4.11 Dummy statement
/// 
///   dummy-statement = empty
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/Eclipse/blob/main/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/DummyStatement.java"><b>Source File
/// </b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class Comment extends SyntaxElement {
	
	/// Create a new DummyStatement.
	/// @param line the source line number
	public Comment(AstBuilder astBuilder) {
		super(astBuilder);
		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+firstLineNumber()+": DummyStatement: "+this);
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
//		JavaSourceFileCoder.code(";");
	}

//	@Override
//	public void buildByteCode(CodeBuilder codeBuilder) {
//	}
//
//	@Override
//	public void print(final int indent) {
//	}
	
	@Override
	public void printTree(final int indent, final Object head) {
	}

	@Override
	public String toString() {
		return ("DUMMY at Line "+firstLineNumber()+" in "+Global.getCurrentScope().identifier);
	}

//	// ***********************************************************************************************
//	// *** Attribute File I/O
//	// ***********************************************************************************************
//	/// Default constructor used by Attribute File I/O
//	private Comment() { super("DummyStatement", 0); }
//
//	@Override
//	public void writeObject(AttributeOutputStream oupt) throws IOException {
//		Util.TRACE_OUTPUT("writeDummyStatement: " + this);
//		oupt.writeKind(ObjectKind.DummyStatement);
//		oupt.writeShort(OBJECT_SEQU);
//		// *** SyntaxElement
//		writeAstData(oupt);
//	}
//
//	/// Read and return a DummyStatement object.
//	/// @param inpt the AttributeInputStream to read from
//	/// @return the DummyStatement object read from the stream.
//	/// @throws IOException if something went wrong.
//	public static Comment readObject(AttributeInputStream inpt) throws IOException {
//		Comment stm = new Comment();
//		stm.OBJECT_SEQU = inpt.readSEQU(stm);
//		// *** SyntaxElement
//		stm.astData = readAstData(inpt);
//		Util.TRACE_INPUT("DummyStatement: " + stm);
//		return(stm);
//	}

}
