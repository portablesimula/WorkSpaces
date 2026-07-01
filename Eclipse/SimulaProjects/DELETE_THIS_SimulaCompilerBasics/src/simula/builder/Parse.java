package simula.builder;

import java.io.Reader;

import simula.compiler.syntaxClass.Type;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.Util;
import simula.token.LexToken;


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
public class Parse {

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

	public static LexToken prevToken(final SimulaBuilder simBuilder) {
		return simBuilder.prevToken();
	}

	public static LexToken prevParserToken(final SimulaBuilder simBuilder) {
		return simBuilder.prevParserToken();
	}

	/// Return the current Token.
	public static LexToken currentLexToken(final SimulaBuilder simBuilder) {
        return (LexToken)simBuilder.getCurrentLexerToken();
	}

	/// Return the prev Token.
	public static LexToken getPrevLexToken(final SimulaBuilder simBuilder) {
        return (LexToken)simBuilder.getPrevLexerToken();
	}

	/// Return the Parser current Token.
	public static LexToken getCurrentParserToken(final SimulaBuilder simBuilder) {
        return simBuilder.getCurrentParserToken();
	}
	
	/// Advance to next Token.
	public static void nextToken(final SimulaBuilder simBuilder) {
		simBuilder.advanceLexer();
	}

	/// Expect the given KeyWord.
	/// 
	/// If it is not accepted an error message is given.
	/// @param key a keyword
	/// @return true if the keyword was accepted, otherwise false
	public static boolean expect(final SimulaBuilder simBuilder, final int key) {
		if (accept(simBuilder, key)) {
			return (true);
		}
		Util.syntaxError(simBuilder, simBuilder.getCurrentParserToken(), 
				"Got symbol '" + Parse.currentLexToken(simBuilder).edText() + "' while expecting KeyWord " + KeyWord.edit(key).toLowerCase());
		return (false);
	}

	/// Test to accept a KeyWord.
	/// 
	/// Test currentToken against each given key.
	/// If accepted 'nextToken' is called,
	/// thus setting prevToken.
	/// @param keys t the given keywords
	/// @return true if a keyword is accepted, false otherwise.
	public static boolean accept(final SimulaBuilder simBuilder, final int... keywords) {
		LexToken token = acceptParserToken(simBuilder, keywords); 
		return token != null;
	}

	public static LexToken acceptParserToken(final SimulaBuilder simBuilder, final int... keywords) {
        LexToken currentToken = getCurrentParserToken(simBuilder);
        int currentKeyWord = (currentToken == null)? KeyWord.EOF : currentToken.keyWord;
		for (int keyword : keywords)
			if (currentKeyWord == keyword) {
				nextToken(simBuilder);
//				IO.println("PsiParse.accept: " + KeyWord.edit(keyword) + " accepted, nextToken: " + PsiParse.currentLexToken(simBuilder));
				return currentToken;
			}
		return null;
	}
	
	public static boolean accept_AND_THEN(final SimulaBuilder simBuilder) {
//		Util.IERR("WARNING: SKAL FLYTTES TIL LEXER: accept_AND_THEN");
//		IO.println("\nPsiParse.accept_AND_THEN: BEGIN ======================================================================");
		if(accept(simBuilder, KeyWord.AND_THEN)) {
//			IO.println("PsiParse.accept_AND_THEN: GOT: AND_THEN");
			return true;
		}
		if(accept(simBuilder, KeyWord.AND)) {
			LexToken prv = simBuilder.getCurrentLexerToken();
//			IO.println("PsiParse.accept_AND_THEN: MAYBE AND THEN prv="+prv);
			if(accept(simBuilder, KeyWord.THEN)) {
//				IO.println("PsiParse.accept_AND_THEN: GOT: AND THEN prv="+prv);
				return true;
			}
//			IO.println("PsiParse.accept_AND_THEN: FAILED --> ROLLBACK prv="+prv);
			simBuilder.rollBackTo(prv, " is not part of AND THEN");
		}
		return false;
	}

	public static boolean accept_AND_ONLY(final SimulaBuilder simBuilder) {
//		Util.IERR("WARNING: SKAL FLYTTES TIL LEXER: accept_AND_ONLY");
//		IO.println("\nPsiParse.accept_AND_THEN: BEGIN ======================================================================");
		LexToken prv = simBuilder.getCurrentLexerToken();
		if(accept(simBuilder, KeyWord.AND)) {
//			IO.println("PsiParse.accept_AND_ONLY: MAYBE AND THEN prv="+prv);
			if(accept(simBuilder, KeyWord.THEN)) {
//			IO.println("PsiParse.accept_AND_ONLY: GOT: AND THEN prv="+prv);
				simBuilder.rollBackTo(prv, " is not a single AND without THEN");
				return false;
			}
//			IO.println("PsiParse.accept_AND_ONLY: prv="+prv);
			return true;
		}
		return false;
	}
	
	public static boolean accept_OR_ELSE(final SimulaBuilder simBuilder) {
//		Util.IERR("WARNING: SKAL FLYTTES TIL LEXER: accept_OR_ELSE");
//		IO.println("\nPsiParse.accept_OR_ELSE: BEGIN ======================================================================");
		if(accept(simBuilder, KeyWord.OR_ELSE)) {
//			IO.println("PsiParse.accept_OR_ELSE: GOT: OR_ELSE");
			return true;
		}
		if(accept(simBuilder, KeyWord.OR)) {
			LexToken prv = simBuilder.getCurrentLexerToken();
//			IO.println("PsiParse.accept_OR_ELSE: MAYBE OR ELSE prv="+prv);
			if(accept(simBuilder, KeyWord.ELSE)) {
//				IO.println("PsiParse.accept_OR_ELSE: GOT: OR ELSE prv="+prv);
				return true;
			}
//			IO.println("PsiParse.accept_OR_ELSE: FAILED --> ROLLBACK prv="+prv);
			simBuilder.rollBackTo(prv, " is not part of OR ELSE");
		}
		return false;
	}

	public static boolean accept_OR_ONLY(final SimulaBuilder simBuilder) {
//		Util.IERR("WARNING: SKAL FLYTTES TIL LEXER: accept_OR_ONLY");
//		IO.println("\nPsiParse.accept_OR_THEN: BEGIN ======================================================================");
		LexToken prv = simBuilder.getCurrentLexerToken();
		if(accept(simBuilder, KeyWord.OR)) {
//			IO.println("PsiParse.accept_OR_ONLY: MAYBE OR ELSE prv="+prv);
			if(accept(simBuilder, KeyWord.ELSE)) {
//				IO.println("PsiParse.accept_OR_ONLY: GOT: OR ELSE prv="+prv);
				simBuilder.rollBackTo(prv, " is not a single OR without ELSE");
				return false;
			}
//			IO.println("PsiParse.accept_OR_ONLY: prv="+prv);
			return true;
		}
		return false;
	}


	/// Skip misplaced current symbol.
	public static void skipMisplacedCurrentSymbol(final SimulaBuilder simBuilder) {
		Util.syntaxError(simBuilder, "Misplaced symbol: "+Parse.currentLexToken(simBuilder)+" -- Ignored");
		nextToken(simBuilder);
	}

	/// Skip until the given symbol.
	public static LexToken skipUntil(final SimulaBuilder simBuilder, int keyWord) {
		LexToken token = null;
		do { nextToken(simBuilder);
			 token = simBuilder.getCurrentLexerToken();
		} while (token.keyWord != keyWord);
		simBuilder.advanceLexer();
		return token;
	}
	
	/// Test to accept an identifier.
	/// @return the identifier or null
	public static LexToken acceptIdentifier(final SimulaBuilder simBuilder, String styleName) {
		LexToken token = Parse.acceptParserToken(simBuilder, KeyWord.IDENTIFIER);
		token.styleName = styleName;
		return (token);
	}

	public static LexToken acceptIdentifier(final SimulaBuilder simBuilder) {
		LexToken token = Parse.acceptParserToken(simBuilder, KeyWord.IDENTIFIER);
		return (token);
	}
	
	/// Test to expect an identifier.
	/// 
	/// If failing to do so, an error is printed.
	/// @return the identifier or null
	public static LexToken expectIdentifier(final SimulaBuilder simBuilder, String styleName) {
        LexToken ident = acceptIdentifier(simBuilder, styleName);
		return (ident);
	} 
	
	public static LexToken expectIdentifier(final SimulaBuilder simBuilder) {
        LexToken ident = acceptIdentifier(simBuilder);
		return (ident);
	}  

	/// Test to accept a Type.
	/// @return the type or null
	public static Type acceptType(final SimulaBuilder simBuilder) {
		Type type=null; //Type.Notype;
		if(accept(simBuilder, KeyWord.BOOLEAN)) type=Type.Boolean;
		else if(accept(simBuilder, KeyWord.CHARACTER)) type=Type.Character;
		else if(accept(simBuilder, KeyWord.INTEGER)) type=Type.Integer;
		else if(accept(simBuilder, KeyWord.SHORT)) { Parse.expect(simBuilder, KeyWord.INTEGER); type=Type.Integer; }
		else if(accept(simBuilder, KeyWord.REAL)) type=Type.Real;
		else if(accept(simBuilder, KeyWord.LONG)) { Parse.expect(simBuilder, KeyWord.REAL); type=Type.LongReal; }
		else if(accept(simBuilder, KeyWord.TEXT)) type=Type.Text;
		else if(accept(simBuilder, KeyWord.REF))	{
			Parse.expect(simBuilder, KeyWord.BEGPAR); LexToken classIdentifier=Parse.getCurrentParserToken(simBuilder);
			Parse.expect(simBuilder, KeyWord.IDENTIFIER); Parse.expect(simBuilder, KeyWord.ENDPAR); 
//			type=Type.Ref(classIdentifier.toString()); 
			type=Type.Ref(classIdentifier.getText()); 
		}
		return(type);  
	}
	
	/// Test to accept a postfix operator ( DOT, IS, IN, QUA).
	/// @return true if the keyword is accepted, false otherwise.
	public static LexToken acceptPostfixOprator(final SimulaBuilder simBuilder) {
		//   DOT | IS | IN | QUA
		LexToken prevToken = null;
		if((prevToken = acceptParserToken(simBuilder, KeyWord.DOT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.IS)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.IN)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.QUA)) != null) return(prevToken);
		return(prevToken);
	}
	
	/// Test to accept a relational operator.
	/// <pre>
	///	 value-relational-operator
	///	     =  <  |  <=  |  =  |  >=  |  >  |  <> | == | =/=
	/// </pre>
	/// @return true if the keyword is accepted, false otherwise.
	public static LexToken acceptRelationalOperator(final SimulaBuilder simBuilder) {
		LexToken prevToken = null;
		if((prevToken = acceptParserToken(simBuilder, KeyWord.LT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.LE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.EQ)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.GE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.GT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.NE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.NER)) != null) return(prevToken);
		if((prevToken = acceptParserToken(simBuilder, KeyWord.EQR)) != null) return(prevToken);
		return(prevToken);
	}

	
	/// Debug utility: Utility TRACE.
	/// @param msg a message
	public static void TRACE(final String msg) {
		LOG.info(msg);
	}

}
