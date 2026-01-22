package simula.compiler.parsing;

import java.io.Reader;

import simula.compiler.syntaxClass.Type;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.Util;
import simula.editor.PsiBuilder;
import simula.lexer.Identifier;
import simula.lexer.SimulaToken;


/// The Simula Parser Utilities.
/// 
/// It contains all static utilities for parsing Simula Syntax.
/// 
/// The Simula Compiler uses Recursive Descent Parsing. Each syntax class is a
/// subclass of this class.
/// 
/// A NonTerminal object represents non terminal symbol in the formal syntax.
/// 
/// Parsing descends in a top-down manner, until the final nonterminal has been
/// processed. The parsing process depends on a global variable, currentToken,
/// which contains the current symbol from the input, and the function nextToken,
/// which updates currentToken when called.
/// 
/// For further description of Recursive Descent Parsing see <a href=
/// "https://en.wikipedia.org/wiki/Recursive_descent_parser">Wikipedia</a>.
/// 
///  META-SYNTAX:
///  
///       MetaSymbol                    Meaning
///       
///           =                     is defined to be
///           |                     alternatively
///         [ x ]                   0 or 1 instance of x
///         { x }                   0 or more instances of x
///       ( x | y )                 grouping: either x or y
///          xyz                    the terminal symbol xyz
///     MetaIdentifier              a non terminal symbol
/// 
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/Eclipse/blob/main/SimulaCompiler2/Simula/src/simula/compiler/parsing/Parse.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public interface Parse {
	
//	/// The saved Token used by 'pushBack'
//	private static Token savedToken; // Used by 'pushBack'
//	
//	/// The SimulaScanner
//	private static SimulaScanner simulaScanner;
//	
//	/// The previous Token.
//	public static Token prevToken;
//	
//	/// The current Token.
//	public static Token currentToken;
//	
//	/// Default constructor.
//	Parse(){}

	/// Close the Simula parser.
	public static void close() {
//		simulaScanner.close();
//		simulaScanner = null;
	}

	/// Initiate Parsing utilities.
	/// @param reader the source file reader.
	public static void initiateParser(final Reader reader) {
//		simulaScanner = new SimulaScanner(reader,false);
//		prevToken = null;
//		currentToken = null;
//		savedToken = null; // Used by 'pushBack'
//
//		nextToken();
	}

	public static void saveCurrentToken() {
		Util.IERR("Parse.saveCurrentToken: KAN IKKE BRUKES: FORSØK  drop()  eller rollbackTo()");
	}

	public static SimulaToken prevToken() {
		Util.IERR("Parse.prevToken: KAN IKKE BRUKES: SKRIV OM KODEN");
		return null;
	}

	/// Return the current Token.
	public static SimulaToken currentToken(final PsiBuilder simBuilder) {
        return (SimulaToken)simBuilder.getTokenType();
	}
	
	/// Advance to next Token.
	public static void nextToken(final PsiBuilder simBuilder) {
		simBuilder.advanceLexer();
	}

	/// Expect the given KeyWord.
	/// 
	/// If it is not accepted an error message is given.
	/// @param key a keyword
	/// @return true if the keyword was accepted, otherwise false
	public static boolean expect(final PsiBuilder simBuilder, final int key) {
		if (accept(simBuilder, key)) return (true);
		LOG.error("Got symbol '" + Parse.currentToken(simBuilder) + "' while expecting KeyWord " + KeyWord.edit(key).toLowerCase());
		return (false);
	}

	/// Test to accept a KeyWord.
	/// 
	/// Test currentToken against each given key.
	/// If accepted 'nextToken' is called,
	/// thus setting prevToken.
	/// @param keys t the given keywords
	/// @return true if a keyword is accepted, false otherwise.
	public static boolean accept(final PsiBuilder simBuilder, final int... keys) {
//        SimulaToken currentToken = currentToken(simBuilder);
//		for (int key : keys)
////			if (Parse.currentToken.getKeyWord() == key) {
//			if (currentToken.keyWord == key) {
//				nextToken(simBuilder);
////				System.out.println("Line "+ Global.sourceLineNumber+": Parse.accept: " + KeyWord.edit(key) + " accepted, nextToken: " + Parse.currentToken);
////				acceptTrace(key, keys);
//				return true;
//			}
////		acceptTrace(0, keys);
//		return false;
		SimulaToken token = acceptToken(simBuilder, keys); 
		return token != null;
	}

	public static SimulaToken acceptToken(final PsiBuilder simBuilder, final int... keys) {
        SimulaToken currentToken = currentToken(simBuilder);
		for (int key : keys)
//			if (Parse.currentToken.getKeyWord() == key) {
			if (currentToken.keyWord == key) {
				nextToken(simBuilder);
//				System.out.println("Line "+ Global.sourceLineNumber+": Parse.accept: " + KeyWord.edit(key) + " accepted, nextToken: " + Parse.currentToken);
//				acceptTrace(key, keys);
				return currentToken;
			}
//		acceptTrace(0, keys);
		return null;
	}


	/// Skip misplaced current symbol.
	public static void skipMisplacedCurrentSymbol(final PsiBuilder simBuilder) {
		LOG.error("Misplaced symbol: "+Parse.currentToken(simBuilder)+" -- Ignored");
		nextToken(simBuilder);
	}
	
	/// Test to accept an identifier.
	/// @return the identifier or null
	public static String acceptIdentifier(final PsiBuilder simBuilder) {
		SimulaToken token = null;
		if ((token = Parse.acceptToken(simBuilder, KeyWord.IDENTIFIER)) != null)
			return (((Identifier)token).value);
		return (null);
	}

	/// Test to expect an identifier.
	/// 
	/// If failing to do so, an error is printed.
	/// @return the identifier or null
	public static String expectIdentifier(final PsiBuilder simBuilder) {
        SimulaToken currentToken = currentToken(simBuilder);
		if (acceptIdentifier(simBuilder) != null)
			return (((Identifier)currentToken).value);
		LOG.error("Got symbol " + currentToken + " while expecting an Identifier");
		return (null);
	}  

	/// Test to accept a Type.
	/// @return the type or null
	public static Type acceptType(final PsiBuilder simBuilder) {
		Type type=null; //Type.Notype;
		if(accept(simBuilder, KeyWord.BOOLEAN)) type=Type.Boolean;
		else if(accept(simBuilder, KeyWord.CHARACTER)) type=Type.Character;
		else if(accept(simBuilder, KeyWord.INTEGER)) type=Type.Integer;
		else if(accept(simBuilder, KeyWord.SHORT)) { Parse.expect(simBuilder, KeyWord.INTEGER); type=Type.Integer; }
		else if(accept(simBuilder, KeyWord.REAL)) type=Type.Real;
		else if(accept(simBuilder, KeyWord.LONG)) { Parse.expect(simBuilder, KeyWord.REAL); type=Type.LongReal; }
		else if(accept(simBuilder, KeyWord.TEXT)) type=Type.Text;
		else if(accept(simBuilder, KeyWord.REF))	{
			Parse.expect(simBuilder, KeyWord.BEGPAR); SimulaToken classIdentifier=Parse.currentToken(simBuilder);
			Parse.expect(simBuilder, KeyWord.IDENTIFIER); Parse.expect(simBuilder, KeyWord.ENDPAR); 
			type=Type.Ref(classIdentifier.toString()); 
		}
		return(type);  
	}
	
	/// Test to accept a postfix operator ( DOT, IS, IN, QUA).
	/// @return true if the keyword is accepted, false otherwise.
	public static SimulaToken acceptPostfixOprator(final PsiBuilder simBuilder) {
		//   DOT | IS | IN | QUA
		SimulaToken prevToken = null;
		if((prevToken = acceptToken(simBuilder, KeyWord.DOT)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.IS)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.IN)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.QUA)) != null) return(prevToken);
		return(prevToken);
	}
	
	/// Test to accept a relational operator.
	/// <pre>
	///	 value-relational-operator
	///	     =  <  |  <=  |  =  |  >=  |  >  |  <> | == | =/=
	/// </pre>
	/// @return true if the keyword is accepted, false otherwise.
	public static SimulaToken acceptRelationalOperator(final PsiBuilder simBuilder) {
		SimulaToken prevToken = null;
		if((prevToken = acceptToken(simBuilder, KeyWord.LT)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.LE)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.EQ)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.GE)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.GT)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.NE)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.NER)) != null) return(prevToken);
		if((prevToken = acceptToken(simBuilder, KeyWord.EQR)) != null) return(prevToken);
		return(prevToken);
	}

	
	/// Debug utility: Utility TRACE.
	/// @param msg a message
	public static void TRACE(final String msg) {
		LOG.println(msg);
	}

}
