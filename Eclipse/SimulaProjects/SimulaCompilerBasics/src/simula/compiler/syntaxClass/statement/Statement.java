/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.lang.classfile.CodeBuilder;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.declaration.DeclarationScope;
import simula.compiler.syntaxClass.declaration.LabelDeclaration;
import simula.compiler.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.compiler.syntaxClass.declaration.PrefixedBlockDeclaration;
import simula.compiler.syntaxClass.expression.Expression;
import simula.compiler.syntaxClass.expression.VariableExpression;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LabelList;
import simula.compiler.utilities.ObjectList;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.LexToken;
import simula.psi.AstBuilder;
import simula.psi.PsiParse;
import simula.psi.PsiTree;

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
	protected Statement(final AstBuilder astBuilder) {
		super(astBuilder);
	}
	
	/// Parse a statement.
	/// @return the statement
	public static Statement acceptStatement(AstBuilder astBuilder) {
		ObjectList<LabelDeclaration> labels = null;
		if (Option.internal.TRACE_PARSE) {
			int lineNumber=PsiParse.getCurrentParserToken(astBuilder).lineNumber;
			Util.TRACE("Statement.acceptStatement: LabeledStatement: lineNumber="+lineNumber+", current=" + PsiParse.getCurrentParserToken(astBuilder));//	+ ", prev=" + PsiParse.prevToken);
		}
		astBuilder.startSubtree(PsiTree.Kind.label, "May be LabeledStatement");
		Statement statement = null;
		LexToken mayBeLabel = PsiParse.acceptIdentifier(astBuilder);
		LOOP:while (mayBeLabel != null) {
//			IO.println("\n\nStatement.acceptStatement: MAYBE LABEL IDENTIFIER: " + mayBeLabel);
			if(PsiParse.accept(astBuilder, KeyWord.COLON)) {
				if (labels == null)	labels = new ObjectList<LabelDeclaration>();
				LabelDeclaration label = new LabelDeclaration(astBuilder, mayBeLabel.edText());
				labels.add(label);
				DeclarationScope scope = Global.getCurrentScope();
				if(scope.labelList == null) scope.labelList = new LabelList(scope); 
				scope.labelList.add(label);
			} else break LOOP;
			mayBeLabel = PsiParse.acceptIdentifier(astBuilder);
		}
		if (labels != null) {
//			IO.println("Statement.acceptStatement: LABELS: " + labels);
//			IO.println("Statement.acceptStatement: prevParserToken: " + astBuilder.prevParserToken());
			if(mayBeLabel != null) {
				astBuilder.rollBackTo(astBuilder.prevParserToken(), "RollBack to Statement after labels start");
//				IO.println("Statement.acceptStatement: ROLLBACK DONE: ");	
			}
			statement = acceptUnlabeledStatement(astBuilder);
			statement = new LabeledStatement(astBuilder, labels, statement);
//			IO.println("Statement.acceptStatement: DONE LabeledStatement: " + statement);				
			astBuilder.doneSubtree(PsiTree.Kind.label, statement);
		} else {
			astBuilder.dropSubtree(PsiTree.Kind.label, " is not a label");
			statement = acceptUnlabeledStatement(astBuilder);
		}
//		IO.println("Statement.acceptStatement: DONE: " + statement);
		return (statement);
	}

	/// Invariant: All Statement handlers starts with 'startSubtree' and ends with 'doneSubtree'.
	private static Statement acceptUnlabeledStatement(AstBuilder astBuilder) {
		astBuilder.startSubtree(PsiTree.Kind.statement, "May be UnlabeledStatement");
		LexToken simToken = PsiParse.getCurrentParserToken(astBuilder);
		if(Option.TRACE_ACCEPT_STATEMENT > 1) IO.println("\nStatement.acceptUnlabeledStatement: "+simToken);
		Statement statement = null;
		int keyWord = simToken.keyWord;
		
		switch(keyWord) {
			case KeyWord.BEGIN:
				// case KeyWord.BEGIN: PsiParse.nextToken(); return (new MaybeBlockDeclaration(null).expectMaybeBlock(lineNumber));
				if(Option.TRACE_ACCEPT_STATEMENT > 1)
					IO.println("\nStatement.acceptUnlabeledStatement: BEGIN ==> parseBlock");
					
//				astBuilder.getRoot().printPsiTree("============================ startSubtree: ACCEPT UnlabeledStatement");
//				Util.STOP();

				MaybeBlockDeclaration block = new MaybeBlockDeclaration(astBuilder, null);
				block.expectMaybeBlock(astBuilder);
				statement = new BlockStatement(astBuilder, block, "Statement.acceptUnlabeledStatement: BEGIN ==> parseBlock");
				break;
				
			case KeyWord.IF:		 statement = new ConditionalStatement(astBuilder); break;
		    case KeyWord.GO,
		         KeyWord.GOTO:		 statement = new GotoStatement(astBuilder, keyWord); break;
		    case KeyWord.FOR:		 statement = new ForStatement(astBuilder); break;
		    case KeyWord.WHILE:		 statement = new WhileStatement(astBuilder); break;
		    case KeyWord.INSPECT:	 statement = new ConnectionStatement(astBuilder); break;
		    case KeyWord.SWITCH:	 if(Option.EXTENSIONS) {
		    							 statement = new SwitchStatement(astBuilder);
		    						 } break;
		    case KeyWord.ACTIVATE,
		         KeyWord.REACTIVATE: statement = new ActivationStatement(astBuilder); break;
		    case KeyWord.INNER:		 statement = new InnerStatement(astBuilder, true); break;
		    case KeyWord.SEMICOLON:	 statement = DummyStatement.ofExplicit(astBuilder); break;
		    case KeyWord.END:	  // statement = DummyStatement.ofImplicit(astBuilder); break; // Dummy Statement, keep END
		    case KeyWord.EOF:		 statement = DummyStatement.ofImplicit(astBuilder); break; // Dummy Statement, keep EOF

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
					astBuilder.printPSI("Statement.acceptUnlabeledStatement: IDENTIFIER");
				}
				
				Expression expr = Expression.acceptExpression(astBuilder);
//				IO.println("\n\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
				
//				if(Option.TRACE_ACCEPT_STATEMENT > 2) {
//					IO.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr);
//					expr.getPsiTree().printPsiTree("Statement.acceptUnlabeledStatement: IDENTIFIER");
//					astBuilder.printPSI("Statement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr.getClass().getSimpleName()+" "+expr);
//				}
				
				if(expr!=null) {
					if(expr instanceof VariableExpression var) {
						if(Option.TRACE_ACCEPT_STATEMENT > 1)
							IO.println("Statement.acceptUnlabeledStatement: GOT VariableExpression: "+var);
							
						if (PsiParse.accept(astBuilder, KeyWord.BEGIN)) {
							PrefixedBlockDeclaration prfblk = PrefixedBlockDeclaration.expectPrefixedBlock(astBuilder, var,false);
							statement = new BlockStatement(astBuilder, prfblk, "Statement.acceptIdentifierStatement: GOT VariableExpression: "+var);
//							IO.println("Statement.acceptUnlabeledStatement: GOT BlockStatement: "+statement);
							break;
		      			} else {
		      				statement = new StandaloneExpression(astBuilder, expr);
//							IO.println("Statement.acceptUnlabeledStatement: GOT StandaloneExpression(1): "+statement);
		      				break;
	      				}
	      			} else {
	      				statement = new StandaloneExpression(astBuilder, expr);
//						IO.println("Statement.acceptUnlabeledStatement: GOT StandaloneExpression(2): "+statement);
	      				break;
	      			}
//					IO.println("\nStatement.acceptUnlabeledStatement: GOT STATEMENT: "+statement);
	      		}
				break;
				
			default:
		        // Error handling and consuming unknown tokens
		        Util.syntaxError(astBuilder, simToken, "Misplaced symbol: '" + simToken.edText() + "' ignored");
		        astBuilder.advanceLexer();
		        statement = DummyStatement.ofImplicit(astBuilder);
			break;
		}
		if(statement != null)
			 astBuilder.doneSubtree(PsiTree.Kind.statement, statement);
		else astBuilder.dropSubtree(PsiTree.Kind.statement, simToken + " is not a Statement");
		return statement;
	}

	@Override
	public void doJavaCoding() {
		Global.sourceLineNumber=firstLineNumber();
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(toJavaCode() + ';');
	}

	/// Build Java ByteCode.
	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		Util.IERR("Method buildByteCode need a redefinition in "+this.getClass().getSimpleName());
	}

}
