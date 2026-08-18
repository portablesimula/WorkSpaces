/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.lang.classfile.CodeBuilder;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.DocumentManager;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.token.Identifier;
import simula.core.builder.token.LexToken;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.syntaxClass.declaration.DeclarationScope;
import simula.core.syntaxClass.declaration.LabelDeclaration;
import simula.core.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.core.syntaxClass.declaration.PrefixedBlockDeclaration;
import simula.core.syntaxClass.expression.Expression;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.utilities.KeyWord;
import simula.core.utilities.LabelList;
import simula.core.utilities.ObjectList;
import simula.core.utilities.Util;

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
	protected Statement(final DocumentManager documentManager) {
		super(documentManager);
	}
	
	/// Parse a statement.
	/// @return the statement
	public static Statement acceptStatement(SimulaBuilder simBuilder) {
		ObjectList<LabelDeclaration> labels = null;
		if (Option.internal.TRACE_PARSE) {
			int lineNumber=Parse.getCurrentParserToken(simBuilder).lineNumber;
			Util.TRACE("Statement.acceptStatement: LabeledStatement: lineNumber="+lineNumber+", current=" + Parse.getCurrentParserToken(simBuilder));//	+ ", prev=" + PsiParse.prevToken);
		}
//		IO.println("Statement.acceptStatement: LabeledStatement: current=" + Parse.getCurrentParserToken(simBuilder));//	+ ", prev=" + PsiParse.prevToken);
		Statement statement = null;
		Identifier mayBeLabel = Parse.acceptIdentifier(simBuilder);
		LOOP:while (mayBeLabel != null) {
//			IO.println("\n\nStatement.acceptStatement: MAYBE LABEL IDENTIFIER: " + mayBeLabel);
			if(Parse.accept(simBuilder, KeyWord.COLON)) {
				if (labels == null)	labels = new ObjectList<LabelDeclaration>();
				LabelDeclaration label = new LabelDeclaration(simBuilder.documentManager, mayBeLabel);
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
			statement = new LabeledStatement(simBuilder.documentManager, labels, statement);
//			IO.println("Statement.acceptStatement: DONE LabeledStatement: " + statement);				
		}
//		IO.println("Statement.acceptStatement: DONE: " + statement);
		return (statement);
	}

	private static Statement acceptUnlabeledStatement(SimulaBuilder simBuilder) {
		LexToken simToken = Parse.getCurrentParserToken(simBuilder);
		if(Option.TRACE_ACCEPT_STATEMENT > 1) IO.println("\nStatement.acceptUnlabeledStatement: "+simToken);
		Statement statement = null;
		int keyWord = simToken.keyWord;
		
//		IO.println("\nStatement.acceptUnlabeledStatement(1): keyWord=" + KeyWord.edit(keyWord) + " " + simToken);
		switch(keyWord) {
			case KeyWord.BEGIN:
				// case KeyWord.BEGIN: PsiParse.nextToken(); return (new MaybeBlockDeclaration(null).expectMaybeBlock(lineNumber));
				if(Option.TRACE_ACCEPT_STATEMENT > 1)
					IO.println("\nStatement.acceptUnlabeledStatement: BEGIN ==> parseBlock");

				MaybeBlockDeclaration block = new MaybeBlockDeclaration(simBuilder.documentManager, null);
				block.expectMaybeBlock(simBuilder);
				statement = new BlockStatement(simBuilder.documentManager, block, "Statement.acceptUnlabeledStatement: BEGIN ==> parseBlock");
				break;
				
			case KeyWord.IF:		 statement = ConditionalStatement.of(simBuilder.documentManager); break;
		    case KeyWord.GO,
		         KeyWord.GOTO:		 statement = new GotoStatement(simBuilder.documentManager, keyWord); break;
		    case KeyWord.FOR:		 statement = ForStatement.of(simBuilder.documentManager); break;
		    case KeyWord.WHILE:		 statement = WhileStatement.of(simBuilder.documentManager); break;
		    case KeyWord.INSPECT:	 statement = ConnectionStatement.of(simBuilder.documentManager); break;
		    case KeyWord.SWITCH:	 if(DocumentManager.EXTENSIONS) {
		    							 statement = SwitchStatement.of(simBuilder.documentManager);
		    						 } break;
		    case KeyWord.ACTIVATE,
		         KeyWord.REACTIVATE: statement = ActivationStatement.of(simBuilder.documentManager); break;
		    case KeyWord.INNER:		 statement = new InnerStatement(simBuilder.documentManager, true); break;
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
//					IO.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER");
//					simBuilder.printPSI("Statement.acceptUnlabeledStatement: IDENTIFIER");
				}
				
				Expression expr = Expression.acceptExpression(simBuilder);
//				IO.println("\n\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr.getClass().getSimpleName()+" "+expr+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
//				Thread.dumpStack();
				
//				if(Option.TRACE_ACCEPT_STATEMENT > 2) {
//					IO.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr);
//					expr.getPsiTree().printPsiTree("Statement.acceptUnlabeledStatement: IDENTIFIER");
//					simBuilder.printPSI("Statement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr.getClass().getSimpleName()+" "+expr);
//				}
				
				if(expr!=null) {
					if(expr instanceof VariableExpression var) {
						if(Option.TRACE_ACCEPT_STATEMENT > 1) {
							IO.println("Statement.acceptUnlabeledStatement: GOT VariableExpression: "+var);
						}
							
						if (Parse.accept(simBuilder, KeyWord.BEGIN)) {
							PrefixedBlockDeclaration prfblk = PrefixedBlockDeclaration.expectPrefixedBlock(simBuilder, var,false);
							statement = new BlockStatement(simBuilder.documentManager, prfblk, "Statement.acceptIdentifierStatement: GOT VariableExpression: "+var);
//							IO.println("Statement.acceptUnlabeledStatement: GOT BlockStatement: "+statement);
							break;
		      			} else {
		      				statement = new StandaloneExpression(simBuilder.documentManager, expr);
//							IO.println("Statement.acceptUnlabeledStatement: GOT StandaloneExpression(1): "+statement);
		      				break;
	      				}
	      			} else {
	      				statement = new StandaloneExpression(simBuilder.documentManager, expr);
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
		return statement;
	}

	@Override
	public void doJavaCoding(final SimulaCoder simCoder) {
		CoreGlobal.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(simCoder,toJavaCode() + ';');
	}

	/// Build Java ByteCode.
	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		Util.IERR("Method buildByteCode need a redefinition in "+this.getClass().getSimpleName());
	}

}
