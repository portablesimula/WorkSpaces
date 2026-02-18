/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.lang.classfile.CodeBuilder;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.parsing.Parse;
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
	public static Statement expectStatement() {
		ObjectList<LabelDeclaration> labels = null;
		int lineNumber=Parse.currentToken.lineNumber;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Statement.doParse: LabeledStatement: lineNumber="+lineNumber+", current=" + Parse.currentToken	+ ", prev=" + Parse.prevToken);
		String ident = Parse.acceptIdentifier();
		while (Parse.accept(KeyWord.COLON)) {
			if (ident != null) {
				if (labels == null)	labels = new ObjectList<LabelDeclaration>();
				LabelDeclaration label = new LabelDeclaration(ident);
				labels.add(label);
				DeclarationScope scope = Global.getCurrentScope();
				if(scope.labelList == null) scope.labelList = new LabelList(scope); 
				scope.labelList.add(label);
			} else Util.error("Missplaced ':'");
			ident = Parse.acceptIdentifier();
		}
		if(ident!=null) Parse.saveCurrentToken(); // Not Label: Pushback
		Statement statement = expectUnlabeledStatement();
		if (labels != null && statement != null)
			statement = new LabeledStatement(lineNumber,labels, statement);
		return (statement);
	}

	/// Parse Utility: Expect an unlabeled statement.
	/// @return the resulting statement
	private static Statement expectUnlabeledStatement() {
		int lineNumber=Parse.currentToken.lineNumber;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Statement.doUnlabeledStatement: lineNumber="+lineNumber+", current=" + Parse.currentToken	+ ", prev=" + Parse.prevToken);
		switch(Parse.currentToken.getKeyWord()) {
		    case KeyWord.BEGIN: Parse.nextToken(); return (new MaybeBlockDeclaration(null).expectMaybeBlock(lineNumber));
		    case KeyWord.IF:    Parse.nextToken(); return (new ConditionalStatement(lineNumber));
		    case KeyWord.GOTO:  Parse.nextToken(); return (new GotoStatement(lineNumber));
		    case KeyWord.GO:    Parse.nextToken(); 
				        if (!Parse.accept(KeyWord.TO))	Util.error("Missing 'TO' after 'GO'");
				        return (new GotoStatement(lineNumber));
		    case KeyWord.FOR:        Parse.nextToken(); return (new ForStatement(lineNumber));
		    case KeyWord.WHILE:      Parse.nextToken(); return (new WhileStatement(lineNumber));
		    case KeyWord.INSPECT:    Parse.nextToken(); return (new ConnectionStatement(lineNumber));
		    case KeyWord.SWITCH:	 if(Option.EXTENSIONS) {
		    							 Parse.nextToken(); return (new SwitchStatement(lineNumber));
		    						 } break;
		    case KeyWord.ACTIVATE:   Parse.nextToken(); return (new ActivationStatement(lineNumber));
		    case KeyWord.REACTIVATE: Parse.nextToken(); return (new ActivationStatement(lineNumber));
		    case KeyWord.INNER:      Parse.nextToken(); return (new InnerStatement(lineNumber));
		    case KeyWord.SEMICOLON:  Parse.nextToken(); return (new DummyStatement(lineNumber)); // Dummy Statement
		    case KeyWord.END:        return (new DummyStatement(lineNumber)); // Dummy Statement, keep END
		    case KeyWord.EOF:    	 return (new DummyStatement(lineNumber)); // Dummy Statement, keep EOF
		
		    case KeyWord.IDENTIFIER: case KeyWord.NEW: case KeyWord.THIS: case KeyWord.BEGPAR:
		         Expression expr = Expression.acceptExpression();
		         if(expr!=null) {
		        	 if(expr instanceof VariableExpression var) {
		        		 if (Parse.accept(KeyWord.BEGIN))
		        			 return new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(var,false));
		        	 }
		        	 return (new StandaloneExpression(lineNumber,expr));
		         }
		}
    	Parse.skipMisplacedCurrentSymbol();
    	return(new DummyStatement(lineNumber));
	}


	private static int SEQU = 1;
	/// Parse a statement.
	/// @return the statement
	public static Statement acceptStatement(PsiBuilder simBuilder) {
		PsiTree stmTree = simBuilder.startSubtree(Statement.class, "Statement-"+(SEQU++));
		IO.println("Statement.acceptStatement: stmTree="+stmTree.debugName);
		
		ObjectList<LabelDeclaration> labels = null;
		int lineNumber=PsiParse.getParserToken(simBuilder).lineNumber;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Statement.acceptStatement: LabeledStatement: lineNumber="+lineNumber+", current=" + PsiParse.getParserToken(simBuilder));//	+ ", prev=" + PsiParse.prevToken);
		String ident = PsiParse.acceptIdentifier(simBuilder);
		PsiTree labTree = simBuilder.startSubtree(LabelDeclaration.class, "LabelDeclaration");
		while (PsiParse.accept(simBuilder, KeyWord.COLON)) {
			if (ident != null) {
				if (labels == null)	labels = new ObjectList<LabelDeclaration>();
				LabelDeclaration label = new LabelDeclaration(ident);
				simBuilder.doneSubtree(labTree, label);
				labTree = simBuilder.startSubtree(LabelDeclaration.class, "LabelDeclaration");
				labels.add(label);
				DeclarationScope scope = Global.getCurrentScope();
				if(scope.labelList == null) scope.labelList = new LabelList(scope); 
				scope.labelList.add(label);
			} else Util.error("Missplaced ':'");
			ident = PsiParse.acceptIdentifier(simBuilder);
		}
		simBuilder.dropSubtree(labTree);
		if(ident!=null) {
			IO.println("\n\nStatement.acceptStatement: NOT LABEL ==> ROLL BACK: "+ident);
			PsiParse.rollBack(simBuilder); // Not Label: rollBack
		}
		Statement statement = acceptUnlabeledStatement(simBuilder);
		if (labels != null && statement != null) {
			Util.IERR("RETT OPP MHT PsiTree");
			statement = new LabeledStatement(lineNumber,labels, statement);
		}
		IO.println("Statement.acceptStatement END: stmTree="+stmTree.debugName);
//		simBuilder.doneSubtree(stmTree, statement);
		if(statement != null)
			 simBuilder.doneSubtree(stmTree, statement);
		else simBuilder.dropSubtree();
		return (statement);
	}

	private static Statement acceptUnlabeledStatement(PsiBuilder simBuilder) {
//		LexToken simToken = getSimToken(simBuilder);
		LexToken simToken = PsiParse.getParserToken(simBuilder);
		IO.println("\nStatement.acceptUnlabeledStatement: "+simToken);
		int lineNumber = simToken.lineNumber;
		Statement statement = null;
		int keyWord = simToken.keyWord;
		
//		simBuilder.startSubtree("Statement-"+(SEQU++));
//		simBuilder.advanceLexer(); // consume simToken (add it to 'current tree')

//		switch(simToken.keyWord) {
		switch(keyWord) {
			case KeyWord.BEGIN:
				// case KeyWord.BEGIN: PsiParse.nextToken(); return (new MaybeBlockDeclaration(null).expectMaybeBlock(lineNumber));
				System.out.println("\nStatement.acceptUnlabeledStatement: BEGIN ==> parseBlock");
				MaybeBlockDeclaration block = new MaybeBlockDeclaration(null);
				block.expectMaybeBlock(simBuilder, lineNumber);
				statement = new BlockStatement(block); break;
				
			case KeyWord.IF:		 statement = new ConditionalStatement(simBuilder, lineNumber); break;
		    case KeyWord.GO,
		         KeyWord.GOTO:		 statement = new GotoStatement(simBuilder, keyWord, lineNumber); break;
		    case KeyWord.FOR:		 statement = new ForStatement(simBuilder, lineNumber); break;
		    case KeyWord.WHILE:		 statement = new WhileStatement(simBuilder, lineNumber); break;
		    case KeyWord.INSPECT:	 statement = new ConnectionStatement(simBuilder, lineNumber); break;
		    case KeyWord.SWITCH:	 if(Option.EXTENSIONS) {
		    							 statement = new SwitchStatement(simBuilder, lineNumber);
		    						 } break;
		    case KeyWord.ACTIVATE,
		         KeyWord.REACTIVATE: statement = new ActivationStatement(simBuilder, lineNumber); break;
//		    case KeyWord.INNER:		 statement = new InnerStatement(simBuilder, false, lineNumber);
		    case KeyWord.INNER:		 statement = InnerStatement.ofExplicit(simBuilder);
		    case KeyWord.SEMICOLON:	 statement = new DummyStatement(lineNumber); simBuilder.consume(KeyWord.SEMICOLON); break;
		    case KeyWord.END:		 statement = new DummyStatement(lineNumber); // Dummy Statement, keep END
		    case KeyWord.EOF:		 statement = new DummyStatement(lineNumber); // Dummy Statement, keep EOF

			case KeyWord.IDENTIFIER, KeyWord.NEW, KeyWord.THIS, KeyWord.BEGPAR:
				System.out.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER");
				Expression expr = Expression.acceptExpression(simBuilder);
				System.out.println("\nStatement.acceptUnlabeledStatement: IDENTIFIER: expr="+expr);
				if(expr!=null) {
					if(expr instanceof VariableExpression var) {
						System.out.println("Statement.acceptUnlabeledStatement: GOT VariableExpression: "+var);
						if (PsiParse.accept(simBuilder, KeyWord.BEGIN)) {
//							Util.IERR("Statement.acceptUnlabeledStatement: PREFIXED BLOCK: NOT IMPL");
	      					//return new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(var,false));
	      					Statement prfblk = new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(simBuilder, var,false));
//	      					statementMarker.done(prfblk);
	      					return prfblk;
	      				}
	      			}
	      			statement = new StandaloneExpression(simBuilder, Global.sourceLineNumber, expr);
	      		}
				break;
				
			default:
		        Util.IERR("Statement.default: " + KeyWord.edit(simToken.keyWord) + " " + (char)simToken.keyWord);
		        // Error handling or consuming unknown tokens
	          System.out.println("\nSimulaParser.parseAssignment: CALL statementMarker.done: ");//+statementMarker);
	          System.out.println("\nStatement.parseStatement: default " + simToken);
	//          statementMarker.error("Statement.parseStatement: default " + simToken);
	          
	//          // DETTE MÅ IMPLEMENTERES !!!
	//	        simBuilder.advanceLexer(); //  (add it to 'blk')
				break;
		}
//		if(statement != null)
//			 simBuilder.doneSubtree(statement);
//		else simBuilder.dropSubtree();
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
