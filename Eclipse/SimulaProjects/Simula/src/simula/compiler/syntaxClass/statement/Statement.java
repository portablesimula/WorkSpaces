/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.lang.classfile.CodeBuilder;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.SyntaxClass;
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
import simula.psi.PsiBuilder;
import simula.psi.PsiParse;

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
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaCompiler2/Simula/src/simula/compiler/syntaxClass/statement/Statement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public abstract class Statement extends SyntaxClass {
	
	/// Create a new Statement.
	/// @param line the source line number
	protected Statement(int line) {
		OLD_lineNumber=line;
	}

	/// Parse a statement.
	/// @return the statement
	public static Statement acceptStatement(PsiBuilder psiBuilder) {
		ObjectList<LabelDeclaration> labels = null;
		int lineNumber=PsiParse.getParserToken(psiBuilder).lineNumber;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Statement.acceptStatement: LabeledStatement: lineNumber="+lineNumber+", current=" + PsiParse.getParserToken(psiBuilder));//	+ ", prev=" + PsiParse.prevToken);
		int labTree = psiBuilder.startSubtree(LabelDeclaration.class, "LabelDeclaration");
		String ident = PsiParse.acceptIdentifier(psiBuilder);
		while (PsiParse.accept(psiBuilder, KeyWord.COLON)) {
			if (ident != null) {
				if (labels == null)	labels = new ObjectList<LabelDeclaration>();
				LabelDeclaration label = new LabelDeclaration(ident);
				psiBuilder.doneSubtree(label, labTree, "LabelDeclaration");
				labTree = psiBuilder.startSubtree(LabelDeclaration.class, "LabelDeclaration");
				labels.add(label);
				DeclarationScope scope = Global.getCurrentScope();
				if(scope.labelList == null) scope.labelList = new LabelList(scope); 
				scope.labelList.add(label);
			} else Util.error("Missplaced ':'");
			ident = PsiParse.acceptIdentifier(psiBuilder);
		}
		psiBuilder.dropSubtree(labTree, "LabelDeclaration");
		if(ident!=null) {
			if(Option.TRACE_ACCEPT_STATEMENT > 0) IO.println("\n\nStatement.acceptStatement: NOT LABEL ==> ROLL BACK: "+ident);
			PsiParse.rollBack(psiBuilder); // Not Label: rollBack
		}
		Statement statement = acceptUnlabeledStatement(psiBuilder);
		if (labels != null && statement != null) {
			statement = new LabeledStatement(lineNumber, labels, statement);
		}
		return (statement);
	}

	/// Invariant: All Statement handlers starts with 'startSubtree' and ends with 'doneSubtree'.
	private static Statement acceptUnlabeledStatement(PsiBuilder psiBuilder) {
		LexToken simToken = PsiParse.getParserToken(psiBuilder);
		if(Option.TRACE_ACCEPT_STATEMENT > 1) IO.println("\nStatement.acceptUnlabeledStatement: "+simToken);
		int lineNumber = simToken.lineNumber;
		Statement statement = null;
		int keyWord = simToken.keyWord;
		
		switch(keyWord) {
			case KeyWord.BEGIN:
				// case KeyWord.BEGIN: PsiParse.nextToken(); return (new MaybeBlockDeclaration(null).expectMaybeBlock(lineNumber));
				if(Option.TRACE_ACCEPT_STATEMENT > 1) IO.println("\nStatement.acceptUnlabeledStatement: BEGIN ==> parseBlock");
				MaybeBlockDeclaration block = new MaybeBlockDeclaration(null);
				block.expectMaybeBlock(psiBuilder, lineNumber);
				statement = new BlockStatement(block); break;
				
			case KeyWord.IF:		 statement = new ConditionalStatement(psiBuilder, lineNumber); break;
		    case KeyWord.GO,
		         KeyWord.GOTO:		 statement = new GotoStatement(psiBuilder, keyWord, lineNumber); break;
		    case KeyWord.FOR:		 statement = new ForStatement(psiBuilder, lineNumber); break;
		    case KeyWord.WHILE:		 statement = new WhileStatement(psiBuilder, lineNumber); break;
		    case KeyWord.INSPECT:	 statement = new ConnectionStatement(psiBuilder, lineNumber); break;
		    case KeyWord.SWITCH:	 if(Option.EXTENSIONS) {
		    							 statement = new SwitchStatement(psiBuilder, lineNumber);
		    						 } break;
		    case KeyWord.ACTIVATE,
		         KeyWord.REACTIVATE: statement = new ActivationStatement(psiBuilder, lineNumber); break;
//		    case KeyWord.INNER:		 statement = new InnerStatement(psiBuilder, false, lineNumber);
		    case KeyWord.INNER:		 statement = InnerStatement.ofExplicit(psiBuilder); break;
		    case KeyWord.SEMICOLON:	 statement = DummyStatement.ofExplicit(psiBuilder, lineNumber); break;
		    case KeyWord.END:	  // statement = DummyStatement.ofImplicit(psiBuilder, lineNumber); break; // Dummy Statement, keep END
		    case KeyWord.EOF:		 statement = DummyStatement.ofImplicit(psiBuilder, lineNumber); break; // Dummy Statement, keep EOF

//			case KeyWord.IDENTIFIER, KeyWord.NEW, KeyWord.THIS, KeyWord.BEGPAR:
			case KeyWord.NEW, KeyWord.THIS, KeyWord.BEGPAR:
				// new classIdentifier ...
				// this classIdentifier ...
				// BEGPAR ????
				Util.IERR("DETTE MÅ SKRIVES");
				Util.STOP();
				break;
			case KeyWord.IDENTIFIER:
				if(Option.TRACE_ACCEPT_STATEMENT > 2) {
					IO.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER");
					psiBuilder.printPSI("Statement.acceptUnlabeledStatement: IDENTIFIER");
				}
				
				Expression expr = Expression.acceptExpression(psiBuilder);
				IO.println("\n\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
				
				if(Option.TRACE_ACCEPT_STATEMENT > 2) {
					IO.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr);
					expr.psiTree.printPsiTree("Statement.acceptUnlabeledStatement: IDENTIFIER");
					psiBuilder.printPSI("Statement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr);
				}
				
				if(expr!=null) {
					if(expr instanceof VariableExpression var) {
						if(Option.TRACE_ACCEPT_STATEMENT > 1) IO.println("Statement.acceptUnlabeledStatement: GOT VariableExpression: "+var);
						if (PsiParse.accept(psiBuilder, KeyWord.BEGIN)) {
//							Util.IERR("Statement.acceptUnlabeledStatement: PREFIXED BLOCK: NOT IMPL");
	      					//return new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(var,false));
	      					Statement prfblk = new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(psiBuilder, var,false));
//	      					statementMarker.done(prfblk);
	      					return prfblk;
	      				}
	      			}
	      			statement = new StandaloneExpression(psiBuilder, Global.sourceLineNumber, expr);
	      		}
				break;
				
			default:
		        Util.IERR("Statement.default: " + KeyWord.edit(simToken.keyWord) + " " + (char)simToken.keyWord);
		        // Error handling or consuming unknown tokens
		        IO.println("\nSimulaParser.parseAssignment: CALL statementMarker.done: ");//+statementMarker);
		        IO.println("\nStatement.parseStatement: default " + simToken);
	//          statementMarker.error("Statement.parseStatement: default " + simToken);
	          
	//          // DETTE MÅ IMPLEMENTERES !!!
	//	        psiBuilder.advanceLexer(); //  (add it to 'blk')
		        Util.STOP();
				break;
		}
//		if(statement != null)
//			 psiBuilder.doneSubtree(statement);
//		else psiBuilder.dropSubtree();
		return statement;
  }



	@Override
	public void doJavaCoding() {
		Global.sourceLineNumber=lineNumber();
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(toJavaCode() + ';');
	}

	/// Build Java ByteCode.
	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		Util.IERR("Method buildByteCode need a redefinition in "+this.getClass().getSimpleName());
	}


	public String edStatement(String phrase) {
//		return new StringBuilder("Line ")
//				.append(lineNumber)
//				.append(' ')
//				.append(getClass().getSimpleName())
//				.append(": ")
//				.append(phrase)
//				.toString();
		return new StringBuilder(getClass().getSimpleName())
				.append("[Line ")
				.append(lineNumber())
				.append(": ")
				.append(phrase)
				.append("]")
				.toString();
	}

}
