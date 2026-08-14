/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import simula.Option;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.Parameter;
import simula.core.syntaxClass.expression.Expression;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.utilities.CoreGlobal;
import simula.core.utilities.KeyWord;
import simula.core.utilities.Meaning;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.RTS;
import simula.core.utilities.Util;

/// Goto Statement.
/// 
/// <pre>
/// 
/// Simula Standard: 4.5 Goto-statement
/// 
///  goto-statement = GOTO designational-expression
///                 | GO TO designational-expression
/// 
/// </pre>
/// Java does not support labels like Simula. The Java Virtual Machine (JVM), however, has labels.
/// A JVM-label is simply a relative byte-address within the byte-code of a method. We will use Java's
/// exception handling together with byte code engineering to re-introduce goto in the Java Language.
/// This is done by generating Java-code which is prepared for Byte Code Engineering.
/// 
/// See <a href="https://portablesimula.github.io/github.io/doc/SimulaRTS.pdf">Mapping Simula to Java (runtime design)</a> 
/// Sect. 6.1 Goto Statement
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/GotoStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class GotoStatement extends Statement {
	/// The target label.
	private Expression label;

	/// Create a new GotoStatement.
	/// @param line source line
	GotoStatement(final SimulaBuilder simBuilder, final int keyWord) {
		super(simBuilder);
		simBuilder.consume(KeyWord.GOTO, KeyWord.GO); //  (add it to tokenList)
		if(keyWord != KeyWord.GOTO) {
	        if (!Parse.accept(simBuilder, KeyWord.TO))
	        	Util.syntaxError(simBuilder, "Missing 'TO' after 'GO'");
		}
		label = Expression.expectExpression(simBuilder, "designational");
		if (Option.internal.TRACE_PARSE) Util.TRACE("Line "+this.firstLineNumber()+": GotoStatement: "+this);
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		label.doChecking();
		IF_TEST:if (label.type == null) {
			if(label.type.keyWord == Type.T_LABEL) break IF_TEST;
			if(label.type.keyWord == Type.T_UNDEF) break IF_TEST;
			Util.semanticError(this, "Goto " + label + ", " + label + " is not a Label");
		}
		label.backLink = this; // To ensure _RESULT from functions
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doJavaCoding() {
		CoreGlobal.sourceLineNumber = firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
  		Type type = label.type;
		Util.ASSERT(type.keyWord == Type.T_LABEL, "Invariant");
		JavaSourceFileCoder.code("_GOTO(" + label.toJavaCode() + ");","GOTO EVALUATED LABEL");
	}
	
	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		if(! labelIsParameterProcedure()) codeBuilder.aload(0);
		label.buildEvaluation(null,codeBuilder);
		RTS.invokevirtual_RTS_GOTO(codeBuilder);
	}
	
	/// Check if label is a parameter procedure.
	/// @return true: if label is a parameter procedure.
	private boolean labelIsParameterProcedure() {
		if(label instanceof VariableExpression var) {
			Meaning meaning = var.meaning;
			if(meaning.declaredAs instanceof Parameter par) {
				if(par.kind == Parameter.Kind.Procedure)
					return true;
			}
		}
		return false;
	}

	@Override
	public void printTree(final int indent) {
		IO.println(edTreeIndent(indent)+"GOTO "+label);
		label.printTree(indent + 1);
	}

	@Override
	public String toString() {
		return "GOTO " + label;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	public GotoStatement() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeGotoStatement: " + this);
		oupt.writeKind(ObjectKind.GotoStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** GotoStatement
		oupt.writeObj(label);
	}

	/// Read and return a GotoStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the GotoStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static GotoStatement readObject(AttributeInputStream inpt) throws IOException {
		GotoStatement stm = new GotoStatement();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		stm.astData = readAstData(inpt);
		// *** GotoStatement
		stm.label = (Expression) inpt.readObj();
		Util.TRACE_INPUT("GotoStatement: " + stm);
		return(stm);
	}

}
