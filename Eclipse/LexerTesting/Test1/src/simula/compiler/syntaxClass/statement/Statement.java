/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

// import java.lang.classfile.CodeBuilder;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

import simula.compiler.JavaSourceFileCoder;
import simula.compiler.parsing.Parse;
import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.syntaxClass.declaration.DeclarationScope;
import simula.compiler.syntaxClass.declaration.LabelDeclaration;
import simula.compiler.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.compiler.syntaxClass.declaration.PrefixedBlockDeclaration;
import simula.compiler.syntaxClass.expression.AssignmentOperation;
import simula.compiler.syntaxClass.expression.Expression;
import simula.compiler.syntaxClass.expression.VariableExpression;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LabelList;
import simula.compiler.utilities.ObjectList;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.lexer.SimulaElementTypes;
import simula.lexer.SimulaToken;

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
/// "https://github.com/portablesimula/WorkSpaces/Eclipse/blob/main/SimulaCompiler2/Simula/src/simula/compiler/syntaxClass/statement/Statement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public abstract class Statement extends SyntaxClass {
	
	/// Create a new Statement.
	/// @param line the source line number
	protected Statement(String debugName, int line) {
		super(debugName);
		lineNumber=line;
	}

	/// Parse a statement.
	/// @return the statement
	public static Statement expectStatement(PsiBuilder simBuilder) {
		ObjectList<LabelDeclaration> labels = null;
		int lineNumber=Parse.currentToken(simBuilder).lineNumber;
//		if (Option.internal.TRACE_PARSE)
//			Util.TRACE("Statement.doParse: LabeledStatement: lineNumber="+lineNumber+", current=" + Parse.currentToken	+ ", prev=" + Parse.prevToken);
		String ident = Parse.acceptIdentifier(simBuilder);
		while (Parse.accept(simBuilder, KeyWord.COLON)) {
			if (ident != null) {
				if (labels == null)	labels = new ObjectList<LabelDeclaration>();
				LabelDeclaration label = new LabelDeclaration(ident);
				labels.add(label);
				DeclarationScope scope = Global.getCurrentScope();
				if(scope.labelList == null) scope.labelList = new LabelList(scope); 
				scope.labelList.add(label);
			} else Util.error("Missplaced ':'");
			ident = Parse.acceptIdentifier(simBuilder);
		}
		if(ident!=null) Parse.saveCurrentToken(); // Not Label: Pushback
		Statement statement = expectUnlabeledStatement(simBuilder);
		if (labels != null && statement != null)
			statement = new LabeledStatement(lineNumber,labels, statement);
		return (statement);
	}


//  public static void parseStatement(SimPsiBuilder simBuilder) {
  public static Statement expectUnlabeledStatement(PsiBuilder simBuilder) {
      IElementType tokenType = simBuilder.getTokenType();
//      System.out.println("Statement.parseStatement: tokenType="+tokenType.getClass().getSimpleName()+" "+tokenType+" "+simBuilder.getTokenText());
      final PsiBuilder.Marker statementMarker = simBuilder.mark();
      System.out.println("Statement.parseStatement: statementMarker="+statementMarker);

      SimulaToken simToken = getSimToken(simBuilder);
      switch(simToken.keyWord) {
      case KeyWord.BEGIN:
          System.out.println("\nStatement.parseStatement: BEGIN ==> parseBlock");
//          Util.IERR("Statement.BEGIN");
          MaybeBlockDeclaration.parseBlock(simBuilder);
          System.out.println("\nStatement.parseStatement: END BLOCK_ELEMENT ==> CALL assignMarker.done: "+statementMarker);
//          statementMarker.done(SimulaElementTypes.BLOCK_ELEMENT);
          statementMarker.done(new MaybeBlockDeclaration("BlockIDENT"));
          break;
//	    case KeyWord.SEMICOLON:
//	    	// Parse.nextToken(); return (new DummyStatement(lineNumber)); // Dummy Statement
//	    	DummyStatement.parseDummyStatement(simBuilder);
//	    	break;
      case KeyWord.IDENTIFIER, KeyWord.NEW, KeyWord.THIS, KeyWord.BEGPAR:
          System.out.println("\nStatement.parseStatement: IDENTIFIER ==> parseAssignment");
//          Util.IERR("Statement.IDENTIFIER");
      	
      		Expression expr = Expression.acceptExpression(simBuilder);
      		if(expr!=null) {
      			if(expr instanceof VariableExpression var) {
      				if (Parse.accept(simBuilder, KeyWord.BEGIN)) {
      					Util.IERR("Statement.expectUnlabeledStatement: NOT IMPL");
//      					//return new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(var,false));
//      					Statement prfblk = new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(var,false));
//      					statementMarker.done(prfblk);
//      					return prfblk;
      				}
      			}
//      			Statement statement = new StandaloneExpression(lineNumber,expr);
      			Statement statement = new StandaloneExpression(simBuilder, Global.sourceLineNumber, expr);
      			statementMarker.done(statement);
      			return statement;
      		}
      	

//          statementMarker.done(SimulaElementTypes.STATEMENT);
      	break;
		default:
//	        Util.IERR("Statement.default");
	        // Error handling or consuming unknown tokens
          System.out.println("\nSimulaParser.parseAssignment: CALL statementMarker.done: "+statementMarker);
          System.out.println("\nStatement.parseStatement: default " + simToken);
          statementMarker.error("Statement.parseStatement: default " + simToken);
	        simBuilder.advanceLexer();
			break;
      }
      return null;
  }


//	/// Parse Utility: Expect an unlabeled statement.
//	/// @return the resulting statement
//	private static Statement expectUnlabeledStatement() {
//		int lineNumber=Parse.currentToken.lineNumber;
//		if (Option.internal.TRACE_PARSE)
//			Util.TRACE("Statement.doUnlabeledStatement: lineNumber="+lineNumber+", current=" + Parse.currentToken	+ ", prev=" + Parse.prevToken);
//		switch(Parse.currentToken.getKeyWord()) {
//		    case KeyWord.BEGIN: Parse.nextToken(); return (new MaybeBlockDeclaration(null).expectMaybeBlock(lineNumber));
//		    case KeyWord.IF:    Parse.nextToken(); return (new ConditionalStatement(lineNumber));
//		    case KeyWord.GOTO:  Parse.nextToken(); return (new GotoStatement(lineNumber));
//		    case KeyWord.GO:    Parse.nextToken(); 
//				        if (!Parse.accept(simBuilder, KeyWord.TO))	Util.error("Missing 'TO' after 'GO'");
//				        return (new GotoStatement(lineNumber));
//		    case KeyWord.FOR:        Parse.nextToken(); return (new ForStatement(lineNumber));
//		    case KeyWord.WHILE:      Parse.nextToken(); return (new WhileStatement(lineNumber));
//		    case KeyWord.INSPECT:    Parse.nextToken(); return (new ConnectionStatement(lineNumber));
//		    case KeyWord.SWITCH:	 if(Option.EXTENSIONS) {
//		    							 Parse.nextToken(); return (new SwitchStatement(lineNumber));
//		    						 } break;
//		    case KeyWord.ACTIVATE:   Parse.nextToken(); return (new ActivationStatement(lineNumber));
//		    case KeyWord.REACTIVATE: Parse.nextToken(); return (new ActivationStatement(lineNumber));
//		    case KeyWord.INNER:      Parse.nextToken(); return (new InnerStatement(lineNumber));
//		    case KeyWord.SEMICOLON:  Parse.nextToken(); return (new DummyStatement(lineNumber)); // Dummy Statement
//		    case KeyWord.END:        return (new DummyStatement(lineNumber)); // Dummy Statement, keep END
//		    case KeyWord.EOF:    	 return (new DummyStatement(lineNumber)); // Dummy Statement, keep EOF
//		
//		    case KeyWord.IDENTIFIER: case KeyWord.NEW: case KeyWord.THIS: case KeyWord.BEGPAR:
//		         Expression expr = Expression.acceptExpression();
//		         if(expr!=null) {
//		        	 if(expr instanceof VariableExpression var) {
//		        		 if (Parse.accept(simBuilder, KeyWord.BEGIN))
//		        			 return new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(var,false));
//		        	 }
//		        	 return (new StandaloneExpression(lineNumber,expr));
//		         }
//		}
//    	Parse.skipMisplacedCurrentSymbol();
//    	return(new DummyStatement(lineNumber));
//	}

	@Override
	public void doJavaCoding() {
		Global.sourceLineNumber=lineNumber;
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(toJavaCode() + ';');
	}

//	/// Build Java ByteCode.
//	@Override
//	public void buildByteCode(CodeBuilder codeBuilder) {
//		Util.IERR("Method buildByteCode need a redefinition in "+this.getClass().getSimpleName());
//	}

}
