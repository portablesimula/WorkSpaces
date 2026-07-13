/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.lang.classfile.CodeBuilder;

import simula.builder.Parse;
import simula.builder.SimulaBuilder;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.SyntaxClass;
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
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
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
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaCompiler2/Simula/src/simula/compiler/syntaxClass/statement/Statement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public abstract class Statement extends SyntaxClass {
	
	/// Create a new Statement.
	/// @param line the source line number
	/// Create a new Statement.
	protected Statement(final SimulaBuilder simBuilder) {
		super(simBuilder);
	}

	/// Parse a statement.
	/// @return the statement
	public static Statement acceptStatement(SimulaBuilder simBuilder) {
		ObjectList<LabelDeclaration> labels = null;
//		int lineNumber=Parse.getCurrentParserToken(simBuilder).lineNumber;
		int lineNumber=Parse.getSourceLineNumber(simBuilder);
		if (Option.internal.TRACE_PARSE) {
			Util.TRACE("Statement.acceptStatement: LabeledStatement: lineNumber="+lineNumber+", current=" + Parse.getCurrentParserToken(simBuilder));//	+ ", prev=" + PsiParse.prevToken);
		}
		LexToken ident = Parse.acceptIdentifier(simBuilder);
		while (Parse.accept(simBuilder, KeyWord.COLON)) {
			if (ident != null) {
				if (labels == null)	labels = new ObjectList<LabelDeclaration>();
				LabelDeclaration label = new LabelDeclaration(ident);
				labels.add(label);
				DeclarationScope scope = CoreGlobal.getCurrentScope();
				if(scope.labelList == null) scope.labelList = new LabelList(scope); 
				scope.labelList.add(label);
			} else Util.error("Missplaced ':'");
			ident = Parse.acceptIdentifier(simBuilder);
		}
		if(ident!=null) Parse.saveCurrentToken(); // Not Label: Pushback
		Statement statement = acceptUnlabeledStatement(simBuilder);
		if (labels != null && statement != null)
			statement = new LabeledStatement(lineNumber,labels, statement);
		return (statement);
	}

	/// Parse Utility: Expect an unlabeled statement.
	/// @return the resulting statement
	private static Statement acceptUnlabeledStatement(SimulaBuilder simBuilder) {
//		int lineNumber=Parse.getCurrentParserToken(simBuilder).lineNumber;
		int lineNumber=Parse.getSourceLineNumber(simBuilder);
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Statement.doUnlabeledStatement: lineNumber="+lineNumber+", current=" + Parse.getCurrentParserToken(simBuilder)	+ ", prev=" + Parse.prevToken);
		switch(Parse.getCurrentParserToken(simBuilder).getKeyWord()) {
		    case KeyWord.BEGIN: Parse.nextToken(simBuilder); return (new MaybeBlockDeclaration(null).expectMaybeBlock(lineNumber));
		    case KeyWord.IF:    Parse.nextToken(simBuilder); return (new ConditionalStatement(lineNumber));
		    case KeyWord.GOTO:  Parse.nextToken(simBuilder); return (new GotoStatement(lineNumber));
		    case KeyWord.GO:    Parse.nextToken(simBuilder); 
				        if (!Parse.accept(simBuilder, KeyWord.TO))	Util.error("Missing 'TO' after 'GO'");
				        return (new GotoStatement(lineNumber));
		    case KeyWord.FOR:        Parse.nextToken(simBuilder); return (new ForStatement(lineNumber));
		    case KeyWord.WHILE:      Parse.nextToken(simBuilder); return (new WhileStatement(lineNumber));
		    case KeyWord.INSPECT:    Parse.nextToken(simBuilder); return (new ConnectionStatement(lineNumber));
		    case KeyWord.SWITCH:	 if(Option.EXTENSIONS) {
		    							 Parse.nextToken(simBuilder); return (new SwitchStatement(lineNumber));
		    						 } break;
		    case KeyWord.ACTIVATE:   Parse.nextToken(simBuilder); return (new ActivationStatement(lineNumber));
		    case KeyWord.REACTIVATE: Parse.nextToken(simBuilder); return (new ActivationStatement(lineNumber));
		    case KeyWord.INNER:      Parse.nextToken(simBuilder); return (new InnerStatement(lineNumber));
		    case KeyWord.SEMICOLON:  Parse.nextToken(simBuilder); return (new DummyStatement(lineNumber)); // Dummy Statement
		    case KeyWord.END:        return (new DummyStatement(lineNumber)); // Dummy Statement, keep END
		    case KeyWord.EOF:    	 return (new DummyStatement(lineNumber)); // Dummy Statement, keep EOF
		
		    case KeyWord.IDENTIFIER: case KeyWord.NEW: case KeyWord.THIS: case KeyWord.BEGPAR:
		         Expression expr = Expression.acceptExpression();
		         if(expr!=null) {
		        	 if(expr instanceof VariableExpression var) {
		        		 if (Parse.accept(simBuilder, KeyWord.BEGIN))
		        			 return new BlockStatement(PrefixedBlockDeclaration.expectPrefixedBlock(var,false));
		        	 }
		        	 return (new StandaloneExpression(lineNumber,expr));
		         }
		}
    	Parse.skipMisplacedCurrentSymbol();
    	return(new DummyStatement(lineNumber));
	}

	@Override
	public void doJavaCoding() {
		CoreGlobal.sourceLineNumber=lineNumber;
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(toJavaCode() + ';');
	}

	/// Build Java ByteCode.
	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		Util.IERR("Method buildByteCode need a redefinition in "+this.getClass().getSimpleName());
	}

}
