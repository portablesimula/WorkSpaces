package simula.psi;

import java.io.Reader;

import simula.compiler.syntaxClass.Type;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LOG;
import simula.token.Identifier;


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
public interface PsiParse {
	
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

	public static void rollBack(final PsiBuilder psiBuilder) {
		psiBuilder.rollBack();
//		Util.IERR("Parse.rollBack: KAN IKKE BRUKES: FORSØK  drop()  eller rollbackTo()");
	}

	public static LexToken prevToken(final PsiBuilder psiBuilder) {
//		Util.IERR("Parse.prevToken: KAN IKKE BRUKES: SKRIV OM KODEN");
		return psiBuilder.prevToken();
	}

	public static LexToken prevParserToken(final PsiBuilder psiBuilder) {
//		Util.IERR("Parse.prevToken: KAN IKKE BRUKES: SKRIV OM KODEN");
		return psiBuilder.prevParserToken();
	}

//	/// Return the current Token.
//	public static LexToken currentToken(final PsiBuilder psiBuilder) {
//        return (LexToken)psiBuilder.getCurrentLexerToken();
//	}

	/// Return the current Token.
	public static LexToken currentLexToken(final PsiBuilder psiBuilder) {
        return (LexToken)psiBuilder.getCurrentLexerToken();
	}

	/// Return the Parser current Token.
	public static LexToken getParserToken(final PsiBuilder psiBuilder) {
        return (LexToken)psiBuilder.getParserToken();
	}
	
	/// Advance to next Token.
	public static void nextToken(final PsiBuilder psiBuilder) {
		psiBuilder.advanceLexer();
	}

	/// Expect the given KeyWord.
	/// 
	/// If it is not accepted an error message is given.
	/// @param key a keyword
	/// @return true if the keyword was accepted, otherwise false
	public static boolean expect(final PsiBuilder psiBuilder, final int key) {
		if (accept(psiBuilder, key)) return (true);
		LOG.error("Got symbol '" + PsiParse.currentLexToken(psiBuilder) + "' while expecting KeyWord " + KeyWord.edit(key).toLowerCase());
		return (false);
	}

	/// Test to accept a KeyWord.
	/// 
	/// Test currentToken against each given key.
	/// If accepted 'nextToken' is called,
	/// thus setting prevToken.
	/// @param keys t the given keywords
	/// @return true if a keyword is accepted, false otherwise.
	public static boolean accept(final PsiBuilder psiBuilder, final int... keywords) {
		LexToken token = acceptParserToken(psiBuilder, keywords); 
		return token != null;
	}

	public static LexToken acceptParserToken(final PsiBuilder psiBuilder, final int... keywords) {
        LexToken currentToken = getParserToken(psiBuilder);
        int currentKeyWord = (currentToken == null)? KeyWord.EOF : currentToken.keyWord;
		for (int keyword : keywords)
			if (currentKeyWord == keyword) {
				nextToken(psiBuilder);
//				IO.println("Line "+ Global.sourceLineNumber+": Parse.accept: " + KeyWord.edit(key) + " accepted, nextToken: " + Parse.currentToken);
//				IO.println("PsiParse.accept: " + KeyWord.edit(keyword) + " accepted, nextToken: " + PsiParse.currentLexToken(psiBuilder));
				return currentToken;
			}
		return null;
	}
	
	public static boolean accept_AND_THEN(final PsiBuilder psiBuilder) {
//		IO.println("\nPsiParse.accept_AND_THEN: BEGIN ======================================================================");
		if(accept(psiBuilder, KeyWord.AND_THEN)) {
			IO.println("PsiParse.accept_AND_THEN: GOT: AND_THEN");
			return true;
		}
		if(accept(psiBuilder, KeyWord.AND)) {
			LexToken prv = psiBuilder.getCurrentLexerToken();
			IO.println("PsiParse.accept_AND_THEN: MAYBE AND THEN prv="+prv);
			if(accept(psiBuilder, KeyWord.THEN)) {
				IO.println("PsiParse.accept_AND_THEN: GOT: AND THEN prv="+prv);
				return true;
			}
			IO.println("PsiParse.accept_AND_THEN: FAILED --> ROLLBACK prv="+prv);
			psiBuilder.rollBackTo(prv);
		}
		return false;
	}

	public static boolean accept_AND_ONLY(final PsiBuilder psiBuilder) {
//		IO.println("\nPsiParse.accept_AND_THEN: BEGIN ======================================================================");
		LexToken prv = psiBuilder.getCurrentLexerToken();
		if(accept(psiBuilder, KeyWord.AND)) {
			IO.println("PsiParse.accept_AND_ONLY: MAYBE AND THEN prv="+prv);
			if(accept(psiBuilder, KeyWord.THEN)) {
				IO.println("PsiParse.accept_AND_ONLY: GOT: AND THEN prv="+prv);
				psiBuilder.rollBackTo(prv);
				return false;
			}
			IO.println("PsiParse.accept_AND_ONLY: prv="+prv);
			return true;
		}
		return false;
	}
	
	public static boolean accept_OR_ELSE(final PsiBuilder psiBuilder) {
//		IO.println("\nPsiParse.accept_OR_ELSE: BEGIN ======================================================================");
		if(accept(psiBuilder, KeyWord.OR_ELSE)) {
			IO.println("PsiParse.accept_OR_ELSE: GOT: OR_ELSE");
			return true;
		}
		if(accept(psiBuilder, KeyWord.OR)) {
			LexToken prv = psiBuilder.getCurrentLexerToken();
			IO.println("PsiParse.accept_OR_ELSE: MAYBE OR ELSE prv="+prv);
			if(accept(psiBuilder, KeyWord.ELSE)) {
				IO.println("PsiParse.accept_OR_ELSE: GOT: OR ELSE prv="+prv);
				return true;
			}
			IO.println("PsiParse.accept_OR_ELSE: FAILED --> ROLLBACK prv="+prv);
			psiBuilder.rollBackTo(prv);
		}
		return false;
	}

	public static boolean accept_OR_ONLY(final PsiBuilder psiBuilder) {
//		IO.println("\nPsiParse.accept_OR_THEN: BEGIN ======================================================================");
		LexToken prv = psiBuilder.getCurrentLexerToken();
		if(accept(psiBuilder, KeyWord.OR)) {
			IO.println("PsiParse.accept_OR_ONLY: MAYBE OR ELSE prv="+prv);
			if(accept(psiBuilder, KeyWord.ELSE)) {
				IO.println("PsiParse.accept_OR_ONLY: GOT: OR ELSE prv="+prv);
				psiBuilder.rollBackTo(prv);
				return false;
			}
			IO.println("PsiParse.accept_OR_ONLY: prv="+prv);
			return true;
		}
		return false;
	}


	/// Skip misplaced current symbol.
	public static void skipMisplacedCurrentSymbol(final PsiBuilder psiBuilder) {
		LOG.error("Misplaced symbol: "+PsiParse.currentLexToken(psiBuilder)+" -- Ignored");
		nextToken(psiBuilder);
	}
	
	/// Test to accept an identifier.
	/// @return the identifier or null
	public static String acceptIdentifier(final PsiBuilder psiBuilder) {
		LexToken token = null;
		if ((token = PsiParse.acceptParserToken(psiBuilder, KeyWord.IDENTIFIER)) != null)
			return ((Identifier)token).value;
		return (null);
	}

	/// Test to expect an identifier.
	/// 
	/// If failing to do so, an error is printed.
	/// @return the identifier or null
	public static String expectIdentifier(final PsiBuilder psiBuilder) {
        LexToken currentToken = getParserToken(psiBuilder);
		if (acceptIdentifier(psiBuilder) != null)
			return ((Identifier)currentToken).value;
		LOG.error("Got symbol " + currentToken + " while expecting an Identifier");
		return (null);
	}  

	/// Test to accept a Type.
	/// @return the type or null
	public static Type acceptType(final PsiBuilder psiBuilder) {
		Type type=null; //Type.Notype;
		if(accept(psiBuilder, KeyWord.BOOLEAN)) type=Type.Boolean;
		else if(accept(psiBuilder, KeyWord.CHARACTER)) type=Type.Character;
		else if(accept(psiBuilder, KeyWord.INTEGER)) type=Type.Integer;
		else if(accept(psiBuilder, KeyWord.SHORT)) { PsiParse.expect(psiBuilder, KeyWord.INTEGER); type=Type.Integer; }
		else if(accept(psiBuilder, KeyWord.REAL)) type=Type.Real;
		else if(accept(psiBuilder, KeyWord.LONG)) { PsiParse.expect(psiBuilder, KeyWord.REAL); type=Type.LongReal; }
		else if(accept(psiBuilder, KeyWord.TEXT)) type=Type.Text;
		else if(accept(psiBuilder, KeyWord.REF))	{
			PsiParse.expect(psiBuilder, KeyWord.BEGPAR); LexToken classIdentifier=PsiParse.getParserToken(psiBuilder);
			PsiParse.expect(psiBuilder, KeyWord.IDENTIFIER); PsiParse.expect(psiBuilder, KeyWord.ENDPAR); 
			type=Type.Ref(classIdentifier.toString()); 
		}
		return(type);  
	}
	
	/// Test to accept a postfix operator ( DOT, IS, IN, QUA).
	/// @return true if the keyword is accepted, false otherwise.
	public static LexToken acceptPostfixOprator(final PsiBuilder psiBuilder) {
		//   DOT | IS | IN | QUA
		LexToken prevToken = null;
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.DOT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.IS)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.IN)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.QUA)) != null) return(prevToken);
		return(prevToken);
	}
	
	/// Test to accept a relational operator.
	/// <pre>
	///	 value-relational-operator
	///	     =  <  |  <=  |  =  |  >=  |  >  |  <> | == | =/=
	/// </pre>
	/// @return true if the keyword is accepted, false otherwise.
	public static LexToken acceptRelationalOperator(final PsiBuilder psiBuilder) {
		LexToken prevToken = null;
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.LT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.LE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.EQ)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.GE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.GT)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.NE)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.NER)) != null) return(prevToken);
		if((prevToken = acceptParserToken(psiBuilder, KeyWord.EQR)) != null) return(prevToken);
		return(prevToken);
	}

	
	/// Debug utility: Utility TRACE.
	/// @param msg a message
	public static void TRACE(final String msg) {
		LOG.println(msg);
	}

}
