/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.lsp.syntaxClass.expression;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;

import simula.lsp.DocumentManager;
import simula.lsp.builder.AttributeInputStream;
import simula.lsp.builder.AttributeOutputStream;
import simula.lsp.builder.SimulaBuilder;
import simula.lsp.coder.SimulaCoder;
import simula.lsp.syntaxClass.SyntaxElement;
import simula.lsp.syntaxClass.Type;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.LspGlobal;
import simula.lsp.utilities.ObjectKind;
import simula.lsp.utilities.Option;
import simula.lsp.utilities.Util;

/// Unary Operation.
/// 
/// <pre>
/// 
/// Syntax:
/// 
///   unary-operation =  unary-operator  Expression
///   
///      unary-operator = NOT | + | -
/// </pre>
/// Link to GitHub: <a href="https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/expression/UnaryOperation.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class UnaryOperation extends Expression {
	
	/// The unary operator.
	int oprator;
	
	/// The operand Expression.
	Expression operand;

	/// Create a new UnaryOperation.
	/// @param oprator the unary operator.
	/// @param operand the operand Expression
	private UnaryOperation(final DocumentManager documentManager, final int oprator,final Expression operand) {
		super(documentManager);
		SimulaBuilder simBuilder = documentManager.simBuilder;
		this.oprator = oprator; this.operand = operand;
		if(this.operand==null)
		{ Util.syntaxError(simBuilder, "Missing operand after unary "+KeyWord.edit(oprator));
		  this.operand=new MissingExpression(documentManager);
		}
		this.operand.backLink=this;
	}

	/// Create a new UnaryOperation.
	/// @param oprator the unary operator.
	/// @param operand the operand Expression
	/// @return the newly created UnaryOperation
	static Expression create(final SimulaBuilder simBuilder, final int oprator,final Expression operand) {
		if (oprator == KeyWord.PLUS || oprator == KeyWord.MINUS) {
			try { // Try to Compile-time Evaluate this expression
				Number rhn=operand.getNumber();
				if(rhn!=null) {
					return(Constant.evaluate(simBuilder, oprator, rhn));
				}  
			} catch(Exception e) {}
		}
		return(new UnaryOperation(simBuilder.documentManager, oprator, operand));
	}
	
//	@Override
//	public Expression evaluate(final PsiBuilder simBuilder) {
//		// Try to Compile-time Evaluate this expression
//		if (oprator == KeyWord.PLUS || oprator == KeyWord.MINUS) {
//			Number rhn=operand.getNumber();
//			if(rhn!=null) {
//				return(Constant.evaluate(null, oprator,rhn));
//			}  
//		}
//		return(this);
//	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		LspGlobal.sourceLineNumber=firstLineNumber();
		if (Option.internal.TRACE_CHECKER)
			Util.TRACE("BEGIN UnaryOperation" + toString() + ".doChecking - Current Scope Chain: " + LspGlobal.getCurrentScope().edScopeChain());
		operand.doChecking();
		if (oprator == KeyWord.NOT) {
			this.type=Type.Boolean;
		}
		else if (oprator == KeyWord.PLUS || oprator == KeyWord.MINUS) {
			this.type=operand.type;
		}
		SET_SEMANTICS_CHECKED();
	}

	// Returns true if this expression may be used as a statement.
	@Override
	public boolean maybeStatement() {
		ASSERT_SEMANTICS_CHECKED();
		return (false);
	}

	@Override
	public void buildEvaluation(final SimulaCoder simCoder, final Expression rightPart, final CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		operand.buildEvaluation(simCoder, null, codeBuilder);
		if (oprator == KeyWord.PLUS) ; // NOTHING
		else if (oprator == KeyWord.NOT) {
			buildNOT(codeBuilder);
		} else if (oprator == KeyWord.MINUS) {
			switch(type.keyWord) {
				case Type.T_INTEGER   -> codeBuilder.ineg();
				case Type.T_REAL      -> codeBuilder.fneg();
				case Type.T_LONG_REAL -> codeBuilder.dneg();
				default -> Util.IERR();
			}
		}
	}

	/// Build code for the NOT operation.
	/// @param codeBuilder the codeBuilder to use.
	public static void buildNOT(CodeBuilder codeBuilder) {
		//    ifne  L1
		//    iconst_1
		//    goto  L2
		//L1: iconst_0
		//L2:
		Label L1 = codeBuilder.newLabel();
		Label L2 = codeBuilder.newLabel();
		codeBuilder
			.ifne(L1)
			.iconst_1()
			.goto_(L2)
			.labelBinding(L1)
			.iconst_0()
			.labelBinding(L2);
	}

	@Override
	public String toJavaCode() {
		ASSERT_SEMANTICS_CHECKED();
		return ("(" + KeyWord.toJavaCode(oprator) + "(" + operand.toJavaCode() + "))");
	}

	@Override
	public String toString() {
		return ("(UNARY:" + KeyWord.edit(oprator) + ' ' + operand + ")");
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private UnaryOperation(final DocumentManager documentManager) {
		super(documentManager);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeUnaryOperation: " + this);
		oupt.writeKind(ObjectKind.UnaryOperation);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		
		// *** Expression
		oupt.writeType(type);
		oupt.writeObj(backLink);
		// *** UnaryOperation
		oupt.writeShort(oprator);
		oupt.writeObj(operand);
	}
	
	/// Read and return an UnaryOperation object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the UnaryOperation object read from the stream.
	/// @throws IOException if something went wrong.
	public static UnaryOperation readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		UnaryOperation expr = new UnaryOperation(documentManager);
		expr.OBJECT_SEQU = inpt.readSEQU(expr);
		// *** SyntaxElement
		
		// *** Expression
		expr.type = inpt.readType();
		expr.backLink = (SyntaxElement) inpt.readObj(documentManager);
		// *** UnaryOperation
		expr.oprator = inpt.readShort();
		expr.operand = (Expression) inpt.readObj(documentManager);
		Util.TRACE_INPUT("readUnaryOperation: " + expr);
		return(expr);
	}

}
