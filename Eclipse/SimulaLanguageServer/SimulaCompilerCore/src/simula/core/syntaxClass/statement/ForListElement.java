/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.constant.MethodTypeDesc;

import simula.Option;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.SimulaBuilder;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.Parameter;
import simula.core.syntaxClass.declaration.SimpleVariableDeclaration;
import simula.core.syntaxClass.expression.Expression;
import simula.core.syntaxClass.expression.TypeConversion;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.utilities.CoreGlobal;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.RTS;
import simula.core.utilities.Util;

// ************************************************************************************
// *** ForListElement -- Single Value
// ************************************************************************************
/// Utility class ForListElement implementing a single value element.
///
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/ForListElement.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class ForListElement extends SyntaxElement {
	/// The For-statement.
	ForStatement forStatement;

	/// The first expression
	Expression expr1;

	/// Create a new ForListElement.
	/// @param forStatement the ForStatement
	/// @param expr1 The first expression
	public ForListElement(final SimulaBuilder simBuilder, final ForStatement forStatement, final Expression expr1) {
		super(simBuilder);
		this.forStatement = forStatement;
		this.expr1 = expr1;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("NEW ForListElement: " + toString());
	}

	/// Perform semantic checking.
	public void doChecking() {
		if (Option.internal.TRACE_CHECKER)
			Util.TRACE("BEGIN ForListElement(" + this + ").doChecking - Current Scope Chain: "
					+ CoreGlobal.getCurrentScope().edScopeChain());
		expr1.doChecking();
		expr1 = TypeConversion.testAndCreate(forStatement.controlVariable.type, expr1);
//		expr1.doChecking();
		expr1.backLink = forStatement; // To ensure _RESULT from functions
	}

	/// Edit Java source code for this ForListElement.
	/// @param classIdent Java class identifier
	/// @param xType control variable's type
	/// @return the resulting Java source code for this ForListElement
	public String edCode(final String classIdent, Type xType) {
		String forElt = (forStatement.controlVariable.type.keyWord == Type.T_TEXT && forStatement.assignmentOperator == KeyWord.ASSIGNVALUE) ? "TValElt"
				: "Elt<" + classIdent + ">";
		return ("new FOR_Single" + forElt + "(" + forStatement.edControlVariableByName(classIdent, xType) + ",new RTS_NAME<"
				+ classIdent + ">() { public " + classIdent + " get(){return(" + expr1.toJavaCode() + "); }})");
	}

	/// Checks if this ForListElement can be optimized.
	/// @return this ForListElement if it can be optimized
	public ForListElement isOptimisable() {
		return (this);
	}

	/// Perform simplified JavaCoding.
	public void doSimplifiedJavaCoding() {
		String cv = forStatement.controlVariable.toJavaCode();
		String val = this.expr1.toJavaCode();
		if (expr1.type != forStatement.controlVariable.type) {
			switch (forStatement.controlVariable.type.keyWord) {
				case Type.T_INTEGER ->   val = "(int)" + val;
				case Type.T_REAL ->      val = "(float)" + val;
				case Type.T_LONG_REAL -> val = "(double)" + val;
				default -> {
					if (forStatement.controlVariable.type.isReferenceType()) {
						ClassDeclaration qual = forStatement.controlVariable.type.getQual();
						if (!(forStatement.controlVariable.type.equals(this.expr1.type)))
							val = "(" + qual.getJavaIdentifier() + ")" + val;
					}
				}
			}
		}
		JavaSourceFileCoder.code(cv + "=" + val + "; {");
		forStatement.doStatement.doJavaCoding();
		JavaSourceFileCoder.code("}");
	}

	/// Build SingleElement ByteCoding
	/// @param codeBuilder the codeBuilder to use.
	public void doSingleElementByteCoding(CodeBuilder codeBuilder) {
        // 0: aload_0
        // 1: aload_0
        // 2: getfield      #16                 // Field k:I
        // 5: putfield      #19                 // Field l:I
		SimpleVariableDeclaration decl = (SimpleVariableDeclaration)forStatement.controlVariable.meaning.declaredAs;

		// controlVariable := expr1
		forStatement.controlVariable.buildIdentifierAccess(true, codeBuilder);
		expr1.buildEvaluation(null,codeBuilder); // evaluate expr1
		
		// JavaSourceFileCoder.code(cv + "=" + val + "; {");
		codeBuilder.putfield(decl.getFieldRefEntry(codeBuilder.constantPool()));

		forStatement.doStatement.buildByteCode(codeBuilder);
	}

	/// Build byte code.
	/// @param codeBuilder the codeBuilder to use.
	/// @param controlVariable the ForStatement's controlVariable.
	public void buildByteCode(CodeBuilder codeBuilder,VariableExpression controlVariable) {
		codeBuilder
			.new_(RTS.CD.FOR_SingleElt)
			.dup();

		// PARAMETER: RTS_NAME<T> cvar  -- Control Variable
        //   getstatic     #25                 // Field _CUR:Lsimula/runtime/RTS_RTObject;
        //   new           #29                 // class simulaTestPrograms/adHoc13$THUNK
        //   dup
        //   aload_0
        //   invokespecial #31                 // Method simulaTestPrograms/adHoc13$1."<init>":(LsimulaTestPrograms/adHoc13;)V
		Parameter.buildNameParam(codeBuilder,controlVariable);

		// PARAMETER: RTS_NAME<T> value
		Parameter.buildNameParam(codeBuilder,expr1);

		MethodTypeDesc MTD=MethodTypeDesc.ofDescriptor("(Lsimula/runtime/RTS_NAME;Lsimula/runtime/RTS_NAME;)V");
		codeBuilder.invokespecial(RTS.CD.FOR_SingleElt, "<init>", MTD); // Invoke Constructor
	}

	@Override
	public String toString() {
		return ("" + expr1);
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	protected ForListElement() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("ForListElement: " + this);
		oupt.writeKind(ObjectKind.ForListElement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** ForListElement
		oupt.writeObj(forStatement);
		oupt.writeObj(expr1);
	}
	
	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static ForListElement readObject(AttributeInputStream inpt) throws IOException {
		ForListElement elt = new ForListElement();
		elt.OBJECT_SEQU = inpt.readSEQU(elt);
		// *** SyntaxElement
		elt.astData = readAstData(inpt);
		// *** ForListElement
		elt.forStatement = (ForStatement) inpt.readObj();
		elt.expr1 = (Expression) inpt.readObj();
		Util.TRACE_INPUT("ForListElement: " + elt);
		return(elt);
	}

}
