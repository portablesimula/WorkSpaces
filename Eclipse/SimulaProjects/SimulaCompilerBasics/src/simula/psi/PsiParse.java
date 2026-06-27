package simula.psi;

import java.io.Reader;

import simula.compiler.syntaxClass.Type;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.Util;


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
/// "https://github.com/portablesimula/WorkSpaces/Eclipse/blob/main/SimulaProjects/Simula/src/simula/compiler/parsing/Parse.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class PsiParse {
	
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

	public static LexToken prevToken(final AstBuilder astBuilder) {
//		Util.IERR("Parse.prevToken: KAN IKKE BRUKES: SKRIV OM KODEN");
		return astBuilder.prevToken();
	}

	public static LexToken prevParserToken(final AstBuilder astBuilder) {
//		Util.IERR("Parse.prevToken: KAN IKKE BRUKES: SKRIV OM KODEN");
		return astBuilder.prevParserToken();
	}

//	/// Return the current Token.
//	public static LexToken currentToken(final PsiBuilder astBuilder) {
//        return (LexToken)astBuilder.getCurrentLexerToken();
//	}

	/// Return the current Token.
	public static LexToken currentLexToken(final AstBuilder astBuilder) {
        return (LexToken)astBuilder.getCurrentLexerToken();
	}

	/// Return the prev Token.
	public static LexToken getPrevLexToken(final AstBuilder astBuilder) {
        return (LexToken)astBuilder.getPrevLexerToken();
	}

	/// Return the Parser current Token.
	public static LexToken getCurrentParserToken(final AstBuilder astBuilder) {
        return astBuilder.getCurrentParserToken();
	}
	
	/// Advance to next Token.
	public static void nextToken(final AstBuilder astBuilder) {
		astBuilder.advanceLexer();
	}

	/// Expect the given KeyWord.
	/// 
	/// If it is not accepted an error message is given.
	/// @param key a keyword
	/// @return true if the keyword was accepted, otherwise false
	public static boolean expect(final AstBuilder astBuilder, final int key) {
		if (accept(astBuilder, key)) {
			return (true);
		}
		Util.syntaxError(astBuilder, astBuilder.getCurrentParserToken(), 
				"Got symbol '" + PsiParse.currentLexToken(astBuilder).edText() + "' while expecting KeyWord " + KeyWord.edit(key).toLowerCase());
		return (false);
	}

	/// Test to accept a KeyWord.
	/// 
	/// Test currentToken against each given key.
	/// If accepted 'nextToken' is called,
	/// thus setting prevToken.
	/// @param keys t the given keywords
	/// @return true if a keyword is accepted, false otherwise.
	public static boolean accept(final AstBuilder astBuilder, final int... keywords) {
		LexToken token = acceptParserToken(astBuilder, keywords); 
		return token != null;
	}

	public static LexToken acceptParserToken(final AstBuilder astBuilder, final int... keywords) {
        LexToken currentToken = getCurrentParserToken(astBuilder);
        int currentKeyWord = (currentToken == null)? KeyWord.EOF : currentToken.keyWord;
		for (int keyword : keywords)
			if (currentKeyWord == keyword) {
				nextToken(astBuilder);
//				IO.println("PsiParse.accept: " + KeyWord.edit(keyword) + " accepted, nextToken: " + PsiParse.currentLexToken(astBuilder));
				return currentToken;
			}
		return null;
	}
	
	public static boolean accept_AND_THEN(final AstBuilder astBuilder) {
//		Util.IERR("WARNING: SKAL FLYTTES TIL LEXER: accept_AND_THEN");
//		IO.println("\nPsiParse.accept_AND_THEN: BEGIN ======================================================================");
		if(accept(astBuilder, KeyWord.AND_THEN)) {
//			IO.println("PsiParse.accept_AND_THEN: GOT: AND_THEN");
			return true;
		}
		if(accept(astBuilder, KeyWord.AND)) {
			LexToken prv = astBuilder.getCurrentLexerToken();
//			IO.println("PsiParse.accept_AND_THEN: MAYBE AND THEN prv="+prv);
			if(accept(astBuilder, KeyWord.THEN)) {
//				IO.println("PsiParse.accept_AND_THEN: GOT: AND THEN prv="+prv);
				return true;
			}
//			IO.println("PsiParse.accept_AND_THEN: FAILED --> ROLLBACK prv="+prv);
			astBuilder.rollBackTo(prv, " is not part of AND THEN");
		}
		return false;
	}

	public static boolean accept_AND_ONLY(final AstBuilder astBuilder) {
//		Util.IERR("WARNING: SKAL FLYTTES TIL LEXER: accept_AND_ONLY");
//		IO.println("\nPsiParse.accept_AND_THEN: BEGIN ======================================================================");
		LexToken prv = astBuilder.getCurrentLexerToken();
		if(accept(astBuilder, KeyWord.AND)) {
//			IO.println("PsiParse.accept_AND_ONLY: MAYBE AND THEN prv="+prv);
			if(accept(astBuilder, KeyWord.THEN)) {
//			IO.println("PsiParse.accept_AND_ONLY: GOT: AND THEN prv="+prv);
				astBuilder.rollBackTo(prv, " is not a single AND without THEN");
				return false;
			}
//			IO.println("PsiParse.accept_AND_ONLY: prv="+prv);
			return true;
		}
		return false;
	}
	
	public static boolean accept_OR_ELSE(final AstBuilder astBuilder) {
//		Util.IERR("WARNING: SKAL FLYTTES TIL LEXER: accept_OR_ELSE");
//		IO.println("\nPsiParse.accept_OR_ELSE: BEGIN ======================================================================");
		if(accept(astBuilder, KeyWord.OR_ELSE)) {
//			IO.println("PsiParse.accept_OR_ELSE: GOT: OR_ELSE");
			return true;
		}
		if(accept(astBuilder, KeyWord.OR)) {
			LexToken prv = astBuilder.getCurrentLexerToken();
//			IO.println("PsiParse.accept_OR_ELSE: MAYBE OR ELSE prv="+prv);
			if(accept(astBuilder, KeyWord.ELSE)) {
//				IO.println("PsiParse.accept_OR_ELSE: GOT: OR ELSE prv="+prv);
				return true;
			}
//			IO.println("PsiParse.accept_OR_ELSE: FAILED --> ROLLBACK prv="+prv);
			astBuilder.rollBackTo(prv, " is not part of OR ELSE");
		}
		return false;
	}

	public static boolean accept_OR_ONLY(final AstBuilder astBuilder) {
//		Util.IERR("WARNING: SKAL FLYTTES TIL LEXER: accept_OR_ONLY");
//		IO.println("\nPsiParse.accept_OR_THEN: BEGIN ======================================================================");
		LexToken prv = astBuilder.getCurrentLexerToken();
		if(accept(astBuilder, KeyWord.OR)) {
//			IO.println("PsiParse.accept_OR_ONLY: MAYBE OR ELSE prv="+prv);
			if(accept(astBuilder, KeyWord.ELSE)) {
//				IO.println("PsiParse.accept_OR_ONLY: GOT: OR ELSE prv="+prv);
				astBuilder.rollBackTo(prv, " is not a single OR without ELSE");
				return false;
			}
//			IO.println("PsiParse.accept_OR_ONLY: prv="+prv);
			return true;
		}
		return false;
	}


	/// Skip misplaced current symbol.
	public static void skipMisplacedCurrentSymbol(final AstBuilder astBuilder) {
		Util.syntaxError(astBuilder, "Misplaced symbol: "+PsiParse.currentLexToken(astBuilder)+" -- Ignored");
		nextToken(astBuilder);
	}

	/// Skip until the given symbol.
	public static LexToken skipUntil(final AstBuilder astBuilder, int keyWord) {
		LexToken token = null;
		do { nextToken(astBuilder);
			 token = astBuilder.getCurrentLexerToken();
		} while (token.keyWord != keyWord);
		astBuilder.advanceLexer();
		return token;
	}
	
	
//	private static String pendingIdentifier = null;
//	private static void setPendingIdentifier(String ident) {
//		pendingIdentifier = ident;
//	}
//	
//	private static LexToken pendingIDToken = null;
//	private static void setPendingIdentifier(LexToken ident) {
//		pendingIDToken = ident;
//	}
//
//	public static void rollBackIdentifier(final PsiBuilder astBuilder, String ident, String debugInfo) {
////		astBuilder.rollBack(debugInfo);
//		PsiParse.setPendingIdentifier(ident);
//	}
//
//	public static void rollBackIdentifier(final PsiBuilder astBuilder, LexToken ident, String debugInfo) {
//		boolean TESTING = true;
//		if(TESTING) {
//			IO.println("PsiParse.rollBackIdentifier: " + ident);
//			astBuilder.rollBackTo(ident, debugInfo);
//			PsiParse.setPendingIdentifier(ident);
//		} else {
////			astBuilder.rollBack(debugInfo);
//			PsiParse.setPendingIdentifier(ident);
//		}
//	}
	
	/// Test to accept an identifier.
	/// @return the identifier or null
	public static LexToken acceptIdentifier(final AstBuilder astBuilder, String styleName) {
		LexToken token = PsiParse.acceptParserToken(astBuilder, KeyWord.IDENTIFIER);
		token.styleName = styleName;
		return (token);
	}

	public static LexToken acceptIdentifier(final AstBuilder astBuilder) {
		LexToken token = PsiParse.acceptParserToken(astBuilder, KeyWord.IDENTIFIER);
		return (token);
	}
	
	/// Test to expect an identifier.
	/// 
	/// If failing to do so, an error is printed.
	/// @return the identifier or null
	public static LexToken expectIdentifier(final AstBuilder astBuilder, String styleName) {
        LexToken ident = acceptIdentifier(astBuilder, styleName);
		return (ident);
	} 
	
	public static LexToken expectIdentifier(final AstBuilder astBuilder) {
        LexToken ident = acceptIdentifier(astBuilder);
		return (ident);
	}  

	/// Test to accept a Type.
	/// @return the type or null
	public static Type acceptType(final AstBuilder astBuilder) {
		Type type=null; //Type.Notype;
		if(accept(astBuilder, KeyWord.BOOLEAN)) type=Type.Boolean;
		else if(accept(astBuilder, KeyWord.CHARACTER)) type=Type.Character;
		else if(accept(astBuilder, KeyWord.INTEGER)) type=Type.Integer;
		else if(accept(astBuilder, KeyWord.SHORT)) { PsiParse.expect(astBuilder, KeyWord.INTEGER); type=Type.Integer; }
		else if(accept(astBuilder, KeyWord.REAL)) type=Type.Real;
		else if(accept(astBuilder, KeyWord.LONG)) { PsiParse.expect(astBuilder, KeyWord.REAL); type=Type.LongReal; }
		else if(accept(astBuilder, KeyWord.TEXT)) type=Type.Text;
		else if(accept(astBuilder, KeyWord.REF))	{
			PsiParse.expect(astBuilder, KeyWord.BEGPAR); LexToken classIdentifier=PsiParse.getCurrentParserToken(astBuilder);
			PsiParse.expect(astBuilder, KeyWord.IDENTIFIER); PsiParse.expect(astBuilder, KeyWord.ENDPAR); 
//			type=Type.Ref(classIdentifier.toString()); 
			type=Type.Ref(classIdentifier.getText()); 
		}
		return(type);  
	}
	
	/// Test to accept a postfix operator ( DOT, IS, IN, QUA).
	/// @return true if the keyword is accepted, false otherwise.
	public static LexToken acceptPostfixOprator(final AstBuilder astBuilder) {
		//   DOT | IS | IN | QUA
		LexToken prevToken = null;
		if((prevToken = acceptParserToken(astBuilder, KeyWord.DOT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.IS)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.IN)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.QUA)) != null) return(prevToken);
		return(prevToken);
	}
	
	/// Test to accept a relational operator.
	/// <pre>
	///	 value-relational-operator
	///	     =  <  |  <=  |  =  |  >=  |  >  |  <> | == | =/=
	/// </pre>
	/// @return true if the keyword is accepted, false otherwise.
	public static LexToken acceptRelationalOperator(final AstBuilder astBuilder) {
		LexToken prevToken = null;
		if((prevToken = acceptParserToken(astBuilder, KeyWord.LT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.LE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.EQ)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.GE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.GT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.NE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.NER)) != null) return(prevToken);
		if((prevToken = acceptParserToken(astBuilder, KeyWord.EQR)) != null) return(prevToken);
		return(prevToken);
	}

	
	/// Debug utility: Utility TRACE.
	/// @param msg a message
	public static void TRACE(final String msg) {
		LOG.info(msg);
	}

}
