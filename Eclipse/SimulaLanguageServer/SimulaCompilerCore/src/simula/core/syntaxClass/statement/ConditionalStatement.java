/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.expression.Expression;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// Conditional Statement.
/// 
/// <pre>
/// 
/// Simula Standard: 4.2 Conditional statement
/// 
///   conditional-statement = if-clause { Label : } for-statement
///                         | if-clause { Label : } unconditional-statement  [ ELSE statement ]
///                         
///     if-clause = IF boolean-expression THEN
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/ConditionalStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class ConditionalStatement extends Statement {
	
	/// The if-clause condition
	private Expression condition;
	
	/// The then-statement
	private Statement thenStatement;
	
	/// The else-statement
	private Statement elseStatement;

	/// Create a new ConditionalStatement.
	/// @param line the source line number
	ConditionalStatement(SimulaBuilder simBuilder) {
		super(simBuilder);
		int lno = simBuilder.getSourceLineNumber();
//		IO.println("NEW ConditionalStatement: "+simBuilder.getSourceLineNumber());
		if (Option.internal.TRACE_PARSE) Util.TRACE("Line " + lno + ": BEGIN IfStatement: ");
		simBuilder.consume(KeyWord.IF); //  (add it to tokenList)

		condition = Expression.expectExpression(simBuilder, "if-condition");
		Parse.expect(simBuilder, KeyWord.THEN);
		Statement elseStatement = null;
		if (Parse.accept(simBuilder, KeyWord.ELSE)) {
			thenStatement = DummyStatement.ofImplicit(simBuilder);
			elseStatement = Statement.acceptStatement(simBuilder);
		} else {
		    thenStatement = Statement.acceptStatement(simBuilder);
		    if (Parse.accept(simBuilder, KeyWord.ELSE)) {
			    elseStatement = Statement.acceptStatement(simBuilder);
		    }
		}
		this.elseStatement=elseStatement;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Line " + simBuilder.getSourceLineNumber() + ": DONE IfStatement started at line: " + lno + ": " + this);
	}

	@Override
	public void print(final int indent) {
    	String spc=edIndent(indent);
		StringBuilder s = new StringBuilder(spc);
		s.append("IF ").append(condition);
		IO.println(s.toString());
		IO.println(spc + "THEN ");
		thenStatement.print(indent + 1);
		if (elseStatement != null) {
			IO.println(spc + "ELSE ");
			elseStatement.print(indent + 1);
		}
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
//		IO.println("ConditionalStatement.doChecking: " + condition.getClass().getSimpleName() + "  " + this);
		condition.doChecking();
		condition.backLink=this; // To ensure _RESULT from functions
		if (condition.type == null || condition.type.keyWord != Type.T_BOOLEAN) {
			if(condition.type != Type.Undef )
				Util.semanticError(this, "ConditionalStatement.doChecking: Condition is not of Type Boolean, but: " + condition.type);
		}
		thenStatement.doChecking();
		if (elseStatement != null) {
			elseStatement.doChecking();
		}
		SET_SEMANTICS_CHECKED();
	}
	
	@Override
	public void doJavaCoding() {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code("if(_VALUE(" + condition.toJavaCode() + ")) {");
		thenStatement.doJavaCoding();
		if (elseStatement != null) {
			JavaSourceFileCoder.code("} else {");
			elseStatement.doJavaCoding();
			JavaSourceFileCoder.code("}");
		} else
			JavaSourceFileCoder.code("}");
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		condition.buildEvaluation(null,codeBuilder);
		Label elseLabel = codeBuilder.newLabel();
		codeBuilder.ifeq(elseLabel);
		thenStatement.buildByteCode(codeBuilder);
		if(elseStatement != null) {
			Label endLabel = codeBuilder.newLabel();
			codeBuilder.goto_(endLabel);
			codeBuilder.labelBinding(elseLabel);
			elseStatement.buildByteCode(codeBuilder);
			codeBuilder.labelBinding(endLabel);
		} else codeBuilder.labelBinding(elseLabel);
	}
	
	@Override
	public void printTree(final int indent) {
		IO.println(edTreeIndent(indent)+"IF " + condition + " THEN");
		thenStatement.printTree(indent + 1);
		if(elseStatement != null) {
			IO.println(edTreeIndent(indent)+"ELSE");
			elseStatement.printTree(indent + 1);
		}
	}

	@Override
	public String toString() {
		return "IF " + condition + " THEN " + thenStatement + " ELSE "	+ elseStatement + ';';
	}
	

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private ConditionalStatement() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeConditionalStatement: " + this);
		oupt.writeKind(ObjectKind.ConditionalStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** ConditionalStatement
		oupt.writeObj(condition);
		oupt.writeObj(thenStatement);
		oupt.writeObj(elseStatement);
	}

	/// Read and return a ConditionalStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the ConditionalStatement object read from the stream.
	/// @throws IOException if something went wrong.
	public static ConditionalStatement readObject(AttributeInputStream inpt) throws IOException {
		ConditionalStatement stm = new ConditionalStatement();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		stm.astData = readAstData(inpt);
		// *** ConditionalStatement
		stm.condition = (Expression) inpt.readObj();
		stm.thenStatement = (Statement) inpt.readObj();
		stm.elseStatement = (Statement) inpt.readObj();

		Util.TRACE_INPUT("ConditionalStatement: " + stm);
		return(stm);
	}

}
