/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.lang.classfile.CodeBuilder;

import simula.builder.SimulaBuilder;
import simula.Option;
import simula.builder.Parse;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.declaration.DeclarationScope;
import simula.compiler.syntaxClass.declaration.LabelDeclaration;
import simula.compiler.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.compiler.syntaxClass.declaration.PrefixedBlockDeclaration;
import simula.compiler.syntaxClass.expression.Expression;
import simula.compiler.syntaxClass.expression.VariableExpression;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LabelList;
import simula.compiler.utilities.ObjectList;
import simula.compiler.utilities.Util;
import simula.token.Identifier;
import simula.token.LexToken;

/// Statement.
/// 
/// <pre>
/// 
/// Simula Standard: Chapter 4: Statements
/// 
///  Statement
///         =  { label : }  unconditional-statement
///         |  { label : }  conditional-statement
///         |  { label : }  for-statement
/// 
///     Unconditional-statement
///         =  assignment-statement  NOTE: Treated as a binary operation
///         |  while-statement
///         |  goto-statement
///         |  procedure-statement
///         |  object-generator
///         |  connection-statement
///         |  compound-statement
///         |  block
///         |  dummy-statement
///         |  activation-statement
/// 
/// </pre>
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/Statement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public abstract class Statement extends SyntaxElement {
	
	/// Create a new Statement.
	protected Statement(final SimulaBuilder simBuilder) {
		super(simBuilder);
	}
	
	/// Parse a statement.
	/// @return the statement
	public static Statement acceptStatement(SimulaBuilder simBuilder) {
		ObjectList<LabelDeclaration> labels = null;
		if (Option.internal.TRACE_PARSE) {
			int lineNumber=Parse.getCurrentParserToken(simBuilder).lineNumber;
			Util.TRACE("Statement.acceptStatement: LabeledStatement: lineNumber="+lineNumber+", current=" + Parse.getCurrentParserToken(simBuilder));//	+ ", prev=" + PsiParse.prevToken);
		}
		IO.println("Statement.acceptStatement: LabeledStatement: current=" + Parse.getCurrentParserToken(simBuilder));//	+ ", prev=" + PsiParse.prevToken);
		Statement statement = null;
		Identifier mayBeLabel = Parse.acceptIdentifier(simBuilder);
		LOOP:while (mayBeLabel != null) {
//			IO.println("\n\nStatement.acceptStatement: MAYBE LABEL IDENTIFIER: " + mayBeLabel);
			if(Parse.accept(simBuilder, KeyWord.COLON)) {
				if (labels == null)	labels = new ObjectList<LabelDeclaration>();
				LabelDeclaration label = new LabelDeclaration(simBuilder, mayBeLabel);
				labels.add(label);
				DeclarationScope scope = CoreGlobal.getCurrentScope();
				if(scope.labelList == null) scope.labelList = new LabelList(scope); 
				scope.labelList.add(label);
			} else break LOOP;
			mayBeLabel = Parse.acceptIdentifier(simBuilder);
		}
		
		if(mayBeLabel != null) {
			// Got identifier without colon. PushBack the identifier
			Parse.saveCurrentToken(simBuilder);
		}
		statement = acceptUnlabeledStatement(simBuilder);
		
		if (labels != null) {
//			IO.println("Statement.acceptStatement: LABELS: " + labels);
//			IO.println("Statement.acceptStatement: prevParserToken: " + simBuilder.prevParserToken());
			statement = new LabeledStatement(simBuilder, labels, statement);
//			IO.println("Statement.acceptStatement: DONE LabeledStatement: " + statement);				
		}
		IO.println("Statement.acceptStatement: DONE: " + statement);
		return (statement);
	}

	/// Invariant: All Statement handlers starts with 'startTokenRange' and ends with 'doneTokenRange'.
	private static Statement acceptUnlabeledStatement(SimulaBuilder simBuilder) {
		simBuilder.startTokenRange("UnlabeledStatement: ");
		LexToken simToken = Parse.getCurrentParserToken(simBuilder);
		if(Option.TRACE_ACCEPT_STATEMENT > 1) IO.println("\nStatement.acceptUnlabeledStatement: "+simToken);
		Statement statement = null;
		int keyWord = simToken.keyWord;
		
		IO.println("\nStatement.acceptUnlabeledStatement(1): "+simToken);
		switch(keyWord) {
			case KeyWord.BEGIN:
				// case KeyWord.BEGIN: PsiParse.nextToken(); return (new MaybeBlockDeclaration(null).expectMaybeBlock(lineNumber));
				if(Option.TRACE_ACCEPT_STATEMENT > 1)
					IO.println("\nStatement.acceptUnlabeledStatement: BEGIN ==> parseBlock");
					
//				simBuilder.getRoot().printPsiTree("============================ startTokenRange: ACCEPT UnlabeledStatement");
//				Util.STOP();

				MaybeBlockDeclaration block = new MaybeBlockDeclaration(simBuilder, null);
				block.expectMaybeBlock(simBuilder);
				statement = new BlockStatement(simBuilder, block, "Statement.acceptUnlabeledStatement: BEGIN ==> parseBlock");
				break;
				
			case KeyWord.IF:		 statement = new ConditionalStatement(simBuilder); break;
		    case KeyWord.GO,
		         KeyWord.GOTO:		 statement = new GotoStatement(simBuilder, keyWord); break;
		    case KeyWord.FOR:		 statement = new ForStatement(simBuilder); break;
		    case KeyWord.WHILE:		 statement = new WhileStatement(simBuilder); break;
		    case KeyWord.INSPECT:	 statement = new ConnectionStatement(simBuilder); break;
		    case KeyWord.SWITCH:	 if(Option.EXTENSIONS) {
		    							 statement = new SwitchStatement(simBuilder);
		    						 } break;
		    case KeyWord.ACTIVATE,
		         KeyWord.REACTIVATE: statement = new ActivationStatement(simBuilder); break;
		    case KeyWord.INNER:		 statement = new InnerStatement(simBuilder, true); break;
		    case KeyWord.SEMICOLON:	 statement = DummyStatement.ofExplicit(simBuilder); break;
		    case KeyWord.END:	  // statement = DummyStatement.ofImplicit(simBuilder); break; // Dummy Statement, keep END
		    case KeyWord.EOF:		 statement = DummyStatement.ofImplicit(simBuilder); break; // Dummy Statement, keep EOF

//			case KeyWord.IDENTIFIER, KeyWord.NEW, KeyWord.THIS, KeyWord.BEGPAR:
		    case KeyWord.BEGPAR:
				// new classIdentifier ...
				// this classIdentifier ...
				// BEGPAR ????
//				Util.IERR("DETTE MÅ SKRIVES");
//				Util.STOP();
//				break;
			case KeyWord.IDENTIFIER:				
			case KeyWord.NEW:
		    case KeyWord.THIS:
				if(Option.TRACE_ACCEPT_STATEMENT > 2) {
					IO.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER");
//					simBuilder.printPSI("Statement.acceptUnlabeledStatement: IDENTIFIER");
				}
				
				Expression expr = Expression.acceptExpression(simBuilder);
//				IO.println("\n\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
				
//				if(Option.TRACE_ACCEPT_STATEMENT > 2) {
//					IO.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr);
//					expr.getPsiTree().printPsiTree("Statement.acceptUnlabeledStatement: IDENTIFIER");
//					simBuilder.printPSI("Statement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr.getClass().getSimpleName()+" "+expr);
//				}
				
				if(expr!=null) {
					if(expr instanceof VariableExpression var) {
						if(Option.TRACE_ACCEPT_STATEMENT > 1)
							IO.println("Statement.acceptUnlabeledStatement: GOT VariableExpression: "+var);
							
						if (Parse.accept(simBuilder, KeyWord.BEGIN)) {
							PrefixedBlockDeclaration prfblk = PrefixedBlockDeclaration.expectPrefixedBlock(simBuilder, var,false);
							statement = new BlockStatement(simBuilder, prfblk, "Statement.acceptIdentifierStatement: GOT VariableExpression: "+var);
//							IO.println("Statement.acceptUnlabeledStatement: GOT BlockStatement: "+statement);
							break;
		      			} else {
		      				statement = new StandaloneExpression(simBuilder, expr);
//							IO.println("Statement.acceptUnlabeledStatement: GOT StandaloneExpression(1): "+statement);
		      				break;
	      				}
	      			} else {
	      				statement = new StandaloneExpression(simBuilder, expr);
//						IO.println("Statement.acceptUnlabeledStatement: GOT StandaloneExpression(2): "+statement);
	      				break;
	      			}
//					IO.println("\nStatement.acceptUnlabeledStatement: GOT STATEMENT: "+statement);
	      		}
				break;
				
			default:
		        // Error handling and consuming unknown tokens
		        Util.syntaxError(simBuilder, simToken, "Misplaced symbol: '" + simToken.edText() + "' ignored");
		        simBuilder.getNextParserToken();
		        statement = DummyStatement.ofImplicit(simBuilder);
			break;
		}
		if(statement != null)
			 simBuilder.doneTokenRange(statement);
		else simBuilder.dropTokenRange();
		return statement;
	}

	@Override
	public void doJavaCoding() {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(toJavaCode() + ';');
	}

	/// Build Java ByteCode.
	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		Util.IERR("Method buildByteCode need a redefinition in "+this.getClass().getSimpleName());
	}

}
