/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.util.Iterator;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.export.LexToken;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.BlockDeclaration;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.Declaration;
import simula.core.syntaxClass.declaration.Parameter;
import simula.core.syntaxClass.expression.Constant;
import simula.core.syntaxClass.expression.Expression;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.ObjectList;
import simula.core.utilities.RTS;
import simula.core.utilities.Util;

/// For Statement.
/// 
/// <pre>
/// 
/// Simula Standard: 4.4 For-statement
/// 
///  for-statement = FOR variable :- reference-list DO statement
///                | FOR variable := value-list DO statement
///               
///      reference-list = reference-list-element { , reference-list-element }
/// 
///          reference-list-element = reference-expression [ WHILE Boolean-expression ]
/// 
///      value-list = value-list-element { , value-list-element }
/// 
///          value-list-element = value-expression [ WHILE Boolean-expression ]
///                             | arithmetic-expression STEP arithmetic-expression UNTIL arithmetic-expression
/// 
/// </pre>
/// The Implementation of the for-statement is a bit tricky. The basic idea is to create a
/// ForList iterator that iterates over a set of FOR_Element iterators. The following subclasses of
/// FOR_Element are defined:
/// <pre>
///                - FOR_SingleELT<T>    for basic types T control variable
///                - FOR_SingleTValElt   for Text type control variable
///                - FOR_StepUntil       for numeric types
///                - FOR_WhileElt<T>     for basic types T control variable
///                - FOR_WhileTValElt    representing For t:= <TextExpr> while <Cond>
///                                      with text value assignment
/// </pre>
/// Each of which deliver a boolean value 'CB' used to indicate whether this for-element is
/// exhausted. All parameters to these classes are transferred 'by name'. This is done to
/// ensure that all expressions are evaluated in the right order. The assignment to the
/// 'control variable' is done within the various for-elements when the 'next' method is
/// invoked. To get a full overview of all the details you are encouraged to study the
/// generated code together with the 'FRAMEWORK for for-list iteration' found in the
/// runtime class RTS_RTObject.
/// 
/// Example, the following for-statement:
/// <pre>
///           for i:=1,6,13 step 6 until 66,i+1 while i < 80 do j:=j+i;
/// </pre>
/// Is compiled to:
/// <pre>
///           for(boolean CB:new ForList(
///               new FOR_SingleELT<Number>(...)
///              ,new FOR_SingleELT<Number>(...)
///              ,new FOR_StepUntil(...)
///              ,new FOR_WhileElt<Number>(...)
///           )) { if(!CB) continue;
///                j=j+i;
///              }
///              </pre>
/// Another example with control variable of type Text:
/// <pre>
///           for t:="one",other while k < 7 do <statement>
/// </pre>
/// Where 'other' is a text procedure, is compiled to:
/// <pre>
///           for(boolean CB:new ForList(
///               new FOR_SingleTValElt(...)
///              ,new FOR_WhileTValElt(...)
///            )) { if(!CB) continue;
///                 … // Statement
///               }
/// </pre>
/// 
/// <h2>Optimized For-Statement</h2>
/// However; most of the for-statements with only one for-list element are optimized.
/// 
/// Single for step-until statements are optimized when the step-expression is constant.
/// I.e. the following for-statements:
/// <pre>
///           for i:=<expr-1> step 1  until <expr-2> do <statements>
///           for i:=<expr-1> step -1 until <expr-2> do <statements>
///           for i:=<expr-1> step 6  until <expr-2> do <statements>
///           for i:=<expr-1> step -6 until <expr-2> do <statements>
/// </pre>
/// are compiled to:
/// <pre>
///           for(i = <expr-1>; i <= <expr-2>; i++) { <statements> }
///           for(i = <expr-1>; i >= <expr-2>; i--) { <statements> }
///           for(i = <expr-1>; i <= <expr-2>; i=i+6) { <statements> }
///           for(i = <expr-1>; i >= <expr-2>; i=i-6) { <statements> }
/// </pre>
/// The other kinds of single elements are optimized in these ways:
/// <pre>
///           for i:=<expr> do <statements>
///           for i:=<expr> while <cond> do <statements>
/// </pre>
/// are compiled to:
/// <pre>
///           i = <expr>; { <statements> }
///           
///           i = <expr>;
///           While( <cond> ) {
///                  <statements>;
///                  i = <expr>;
///           }
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/ForStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class ForStatement extends Statement {
	
	/// The control variable
	VariableExpression controlVariable;
	
	/// Assignment operator  := or :-
	int assignmentOperator; // KeyWord := or :-
	
	/// The list of ForList elements.
	private ObjectList<ForListElement> forList = new ObjectList<ForListElement>();
	
	/// The statement after DO.
	Statement doStatement;

	/// Create a new ForStatement.
	/// @param line the source line number
	private ForStatement(final DocumentManager documentManager) {
		super(documentManager);
	}
	
	static ForStatement of(final DocumentManager documentManager) {
		ForStatement stm = new ForStatement(documentManager);
		SimulaBuilder simBuilder = documentManager.simBuilder;
		simBuilder.consume(KeyWord.FOR); //  (add it to tokenList)

		if (Option.internal.TRACE_PARSE)
			Parse.TRACE("Parse ForStatement");
		stm.controlVariable = new VariableExpression(documentManager, Parse.expectIdentifier(simBuilder));
		LexToken prevToken = Parse.getCurrentParserToken(simBuilder);
		if (!Parse.accept(simBuilder, KeyWord.ASSIGNVALUE))
			Parse.expect(simBuilder, KeyWord.ASSIGNREF);
		stm.assignmentOperator = prevToken.keyWord;
		do {
			stm.forList.add(stm.expectForListElement(simBuilder));
		} while (Parse.accept(simBuilder, KeyWord.COMMA));
		Parse.expect(simBuilder, KeyWord.DO);
		Statement doStatement = Statement.acceptStatement(simBuilder);
		if (doStatement == null) {
			Util.syntaxError(simBuilder, "No statement following DO in For statement");
			doStatement = DummyStatement.ofImplicit(simBuilder);
		}
		stm.doStatement = doStatement;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Line " + stm.firstLineNumber() + ": ForStatement: " + stm);
		return stm;
	}

	/// Parse a for-list element.
	/// @return the resulting ForListElement
	private ForListElement expectForListElement(SimulaBuilder simBuilder) {
		if (Option.internal.TRACE_PARSE)
			Parse.TRACE("Parse ForListElement");
		Expression expr1 = Expression.expectExpression(simBuilder, "for list");
		if (Parse.accept(simBuilder, KeyWord.WHILE))
			return (new ForWhileElement(documentManager, this, expr1, Expression.expectExpression(simBuilder, "for while")));
		if (Parse.accept(simBuilder, KeyWord.STEP)) {
			Expression expr2 = Expression.expectExpression(simBuilder, "for step");
			Parse.expect(simBuilder, KeyWord.UNTIL);
			return (new StepUntilElement(documentManager, this, expr1, expr2, Expression.expectExpression(simBuilder, "for until")));
		} else
			return (new ForListElement(documentManager, this, expr1));
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		controlVariable.doChecking();
		Declaration decl = controlVariable.meaning.declaredAs;
		if (decl instanceof Parameter par && par.mode == Parameter.Mode.name)
			Util.semanticError(this, "For-Statement's Controled Variable(" + controlVariable + ") can't be a formal parameter by Name");
		Type type = controlVariable.type;
		if (type.keyWord != Type.T_TEXT && assignmentOperator == KeyWord.ASSIGNVALUE && type.isReferenceType())
			Util.semanticError(this, "Illegal For-Statement with object value assignment ( := )");
		Iterator<ForListElement> iterator = forList.iterator();
		while (iterator.hasNext()) {
			iterator.next().doChecking();
		}
		doStatement.doChecking();
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doJavaCoding(final SimulaCoder simCoder) {
		ForListElement singleElement = getSingleOptimizableElement();
		if (singleElement != null) {
			singleElement.doSimplifiedJavaCoding(simCoder);
			return;
		}

		// ------------------------------------------------------------
		// Example:
		//
		// for(boolean CB_:new FOR_List(
		//     new FOR_SingleELT<Number>(n1)
		//    ,new FOR_SingleELT<Number>(n3)
		//    ,new FOR_StepUntil(n5,n3,n201)
		//    ,new FOR_WhileElt<Number>(n4,b1)
		// )) { if(!CB_) continue;
		//      // Statements ...
		// }
		// ------------------------------------------------------------
		CoreGlobal.sourceLineNumber = firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		boolean refType = controlVariable.type.isReferenceType();
		String CB = "CB_" + firstLineNumber();
		JavaSourceFileCoder.code(simCoder,"for(boolean " + CB + ":new FOR_List(");
		char del = ' ';
		for (ForListElement elt : forList) {
			String classIdent = (refType) ? elt.expr1.type.getJavaRefIdent(elt.expr1) : "Number";
			switch(elt.expr1.type.keyWord) {
				case Type.T_CHARACTER -> classIdent = "Character"; // AD'HOC
				case Type.T_BOOLEAN -> classIdent = "Boolean"; // AD'HOC
				case Type.T_TEXT -> classIdent = "RTS_TXT"; // AD'HOC
			}
			JavaSourceFileCoder.code(simCoder,"   " + del + elt.edCode(classIdent, elt.expr1.type));
			del = ',';
		}
		JavaSourceFileCoder.code(simCoder,"   )) { if(!" + CB + ") continue;");
		doStatement.doJavaCoding(simCoder);
		JavaSourceFileCoder.code(simCoder,"}");
	}

	/// Check if this ForListElement is a single optimizable element.
	/// @return a single optimizable element or null
	private ForListElement getSingleOptimizableElement() {
		if (forList.size() != 1)
			return (null);
		ForListElement elt = forList.firstElement();
		return (elt.isOptimisable());
	}

	/// Coding Utility: Edit control variable by name.
	/// @param classIdent Java class identifier
	/// @param xType control variable's type
	/// @return the resulting Java source code for this ForListElement
	String edControlVariableByName(final String classIdent, Type xType) {
		String cv = controlVariable.toJavaCode();
		String castVar = "x_;";
		switch (controlVariable.type.keyWord) {
			case Type.T_INTEGER -> castVar = "x_.intValue();";
			case Type.T_REAL -> castVar = "x_.floatValue();";
			case Type.T_LONG_REAL -> castVar = "x_.doubleValue();";
			default -> {
				if (controlVariable.type.isReferenceType()) {
					ClassDeclaration qual = controlVariable.type.getQual();
					if (!(controlVariable.type.equals(xType)))
						castVar = "(" + qual.getJavaIdentifier() + ")x_;";
				}
			}
		}
		String cvName = "new RTS_NAME<" + classIdent + ">()" + "{ public " + classIdent + " put(" + classIdent + " x_){"
				+ cv + "=" + castVar + " return(x_);};" + "  public " + classIdent + " get(){return((" + classIdent
				+ ")" + cv + "); }	}";
		return (cvName);
	}

	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		if (forList.size() == 1) {
			ForListElement singleElement = forList.firstElement();
			singleElement.doSingleElementByteCoding(simCoder, codeBuilder);
			return;
		}

		// ------------------------------------------------------------
		// Example:
		//
		// for(boolean CB_:new ForList(
		//     new FOR_SingleELT<Number>(n1)
		//    ,new FOR_SingleELT<Number>(n3)
		//    ,new FOR_StepUntil(n5,n3,n201)
		//    ,new FOR_WhileElt<Number>(n4,b1)
		// )) { if(!CB_) continue;
		//      // Statements ...
		// }
		// ------------------------------------------------------------
		CoreGlobal.sourceLineNumber = firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		
		codeBuilder
			.new_(RTS.CD.FOR_List)
			.dup();
		Constant.buildIntConst(codeBuilder, this.forList.size());
		codeBuilder
			.anewarray(RTS.CD.FOR_Element)
			.dup();

		int n = this.forList.size();
		for(int i=0;i<n;i++) {
			Constant.buildIntConst(codeBuilder, i); // Index in 
			forList.get(i).buildByteCode(simCoder, codeBuilder,controlVariable);
		    //   aastore
		    //   dup        // ONLY IF i < (n-1)
			codeBuilder.aastore();
			if(i<(n-1)) codeBuilder.dup();
		}
		MethodTypeDesc MTD = MethodTypeDesc.ofDescriptor("([Lsimula/runtime/FOR_Element;)V");
		codeBuilder.invokespecial(RTS.CD.FOR_List, "<init>", MTD); // Invoke ForList'Constructor
		
		Label contLabel = codeBuilder.newLabel();
		Label stmLabel = codeBuilder.newLabel();
		Label endLabel = codeBuilder.newLabel();
		int index1 = BlockDeclaration.currentBlock.allocateLocalVariable(Type.Ref);
		RTS.invokevirtual_FOR_List_iterator(codeBuilder);
		codeBuilder
			.astore(index1)
			.labelBinding(contLabel)
			.aload(index1)
			.invokeinterface(RTS.CD.JAVA_UTIL_ITERATOR, "hasNext", MethodTypeDesc.ofDescriptor("()Z"))
			.ifeq(endLabel)
			.aload(index1)
			.invokeinterface(RTS.CD.JAVA_UTIL_ITERATOR, "next", MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;"))
			.checkcast(ConstantDescs.CD_Boolean)
			.invokevirtual(ConstantDescs.CD_Boolean, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"))
			.ifne(stmLabel)
			.goto_(contLabel)
			.labelBinding(stmLabel);
		doStatement.buildByteCode(simCoder, codeBuilder);
		codeBuilder
			.goto_(contLabel)
			.labelBinding(endLabel);
	}

	@Override
	public void print(final int indent) {
		String spc = edIndent(indent);
		String fl = forList.toString().replace('[', ' ').replace(']', ' ');
		IO.println(spc + "FOR " + controlVariable + " " + KeyWord.edit(assignmentOperator) + fl + "DO");
		if (doStatement != null)
			doStatement.print(indent);
	}

	@Override
	public void printTree(final int indent) {
		String fl = forList.toString().replace('[', ' ').replace(']', ' ');
		IO.println(edTreeIndent(indent)+"FOR " + controlVariable + " " + KeyWord.edit(assignmentOperator) + fl + " DO ");
		doStatement.printTree(indent + 1);
	}
	
	@Override
	public String toString() {
		String fl = forList.toString().replace('[', ' ').replace(']', ' ');
//		return ("FOR " + controlVariable + " " + KeyWord.edit(assignmentOperator) + fl + " DO " + doStatement);
		return "FOR " + controlVariable + " " + KeyWord.edit(assignmentOperator) + fl + " DO " + doStatement;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeForStatement: " + this);
		oupt.writeKind(ObjectKind.ForStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		
		// *** ForStatement
		oupt.writeObj(controlVariable);
		oupt.writeShort(assignmentOperator);
		oupt.writeObjectList(forList);
		oupt.writeObj(doStatement);
	}

	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	@SuppressWarnings("unchecked")
	public static ForStatement readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		ForStatement stm = new ForStatement(documentManager);
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		
		// *** ForStatement
		stm.controlVariable = (VariableExpression) inpt.readObj(documentManager);
		stm.assignmentOperator = inpt.readShort();
		stm.forList = (ObjectList<ForListElement>) inpt.readObjectList(documentManager);
		stm.doStatement = (Statement) inpt.readObj(documentManager);
		Util.TRACE_INPUT("ForStatement: " + stm);
		return(stm);
	}

}
