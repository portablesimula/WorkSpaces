/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.parsing;

import static simuletta.utilities.Util.*;

import java.io.Reader;

import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Token;
import simuletta.utilities.Util;

/**
 * All static utilities for parsing Simuletta Syntax
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class Parser {
	public static Token prevToken;
	public static Token currentToken;
	private static Token savedToken; // Used by 'pushBack'
	private static boolean endOfFileErrorGiven;
	private static SimulettaScanner simulettaScaner;

	// *********************************************************************************
	// *** Open
	// *********************************************************************************
	public static void open(final String name,final Reader reader) {
		simulettaScaner = new SimulettaScanner(name,reader,false);
		prevToken = null;
		currentToken = null;
		savedToken = null; // Used by 'pushBack'
		endOfFileErrorGiven=false;

		nextSymb();
	}

	// *********************************************************************************
	// *** Close
	// *********************************************************************************
	public static void close() {
		simulettaScaner.close();
		simulettaScaner = null;
	}

	// *********************************************************************************
	// *** Parser Utilities
	// *********************************************************************************
	public static void saveCurrentToken() {
		//Util.BREAK("Parser.saveCurrentToken: currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);
		if (savedToken != null) Util.FATAL_ERROR("saveCurrentToken: Already called");
		savedToken = Parser.currentToken;
		Parser.currentToken = Parser.prevToken;
		Parser.prevToken = null;
	}

	public static Token getSymb() {
		Token token=Parser.currentToken; nextSymb();
//		Util.TRACE("Parser.getSymb: "+token);
		return(token);
	}

	public static boolean accept(final int ... s) {
		for(int i=0;i<s.length;i++) {
			if (Parser.currentToken.getKeyWord() == s[i]) {
				if(Option.TRACE_PARSE) Util.TRACE("Parser.accept("+KeyWord.ed(s[i])+") == True  "+currentToken);
				nextSymb();
				return (true);
			}
		}
//		if (Option.TRACING) {
//			String str=s[0].toString();
//			int i=1; while(i++ < s.length-1) str=str+','+s[i].toString();
//			Util.TRACE("Parser.accept("+str+") == False  "+currentToken);
//		}
		return (false);
	}

	public static boolean acceptAnyExcept(final int ... s) {
		for(int i=0;i<s.length;i++) {
			if (Parser.currentToken.getKeyWord() == s[i]) {
				if(Option.TRACE_PARSE) Util.TRACE("Parser.accept("+KeyWord.ed(s[i])+") == false  "+currentToken);
				return (false);
			}
		}
//		if (Option.TRACING) {
//			String str=s[0].toString();
//			int i=1; while(i++ < s.length-1) str=str+','+s[i].toString();
//			Util.TRACE("Parser.acceptAnyExcept("+str+") == False  "+currentToken);
//		}
		nextSymb();
		return (true);
	}

	public static boolean expect(final int s) {
		if (accept(s)) return (true);
		Util.ERROR("Got symbol '" + Parser.currentToken + "' while expecting Symbol " + KeyWord.ed(s));
		return (false);
	}

	public static boolean checkNext(final int s) {
		if (Parser.currentToken.getKeyWord() == s) {
			if(Option.TRACE_PARSE) Util.TRACE("Parser.checkNext("+s+") == True  "+currentToken);
			return (true);
		}
		Util.ERROR("Got symbol '" + Parser.currentToken + "' while expecting Next Symbol " + KeyWord.ed(s));
		return (false);
	}

	public static boolean skipMissplacedSymbol(final int s) {
		if (Parser.accept(s)) {
			Util.ERROR("Misplaced symbol: "+KeyWord.ed(s)+" -- Ignored");
			return (true);
		}
		return (false);
	}

	public static void skipMissplacedCurrentSymbol() {
		Util.ERROR("Misplaced symbol: "+Parser.currentToken+" -- Ignored");
		nextSymb();
	}
	
	public static int lastKeyWord()
	{ return(Parser.prevToken.getKeyWord()); }

	
//	public static Token acceptRelationalOperator()   // TODO:
//	{ // value-relational-operator
//	  //     =  <  |  <=  |  =  |  >=  |  >  |  <> | == | =/=
//		Token res=Parser.currentToken;
//	  if(accept(KeyWord.LT)) return(res);
//	  if(accept(KeyWord.LE)) return(res);
//	  if(accept(KeyWord.EQ)) return(res);
//	  if(accept(KeyWord.GE)) return(res);
//	  if(accept(KeyWord.GT)) return(res);
//	  if(accept(KeyWord.NE)) return(res);
//	  return(null);
//	}

	public static Long acceptIntegerConst() {
		if(Parser.accept(KeyWord.SIMPLEVALUE)) {
			Token token = Parser.prevToken;
			Object value=token.getValue();
			if(value instanceof Long) return((Long)value);
		} return(null);
	}

	public static Long expectIntegerConst() {
		Long val=acceptIntegerConst();
		if (val!=null) return (val);
		Util.ERROR("Got symbol '" + Parser.currentToken + "' while expecting Integer Constant");
		return (null);
	}

	public static String acceptString() {
		if(Parser.accept(KeyWord.SIMPLEVALUE)) {
			Token token = Parser.prevToken;
			Object value=token.getValue();
			if(value instanceof String) return((String)value);
		} return(null);
	}

	public static String expectString() {
		String val=acceptString();
		if (val!=null) return (val);
		Util.ERROR("Got symbol '" + Parser.currentToken + "' while expecting String Constant");
		return (null);
	}

	public static String acceptIdentifier() {
		Token token = Parser.currentToken;
		if (Parser.accept(KeyWord.IDENTIFIER))
			return (token.getIdentifier().toString());
		return (null);
	}

	public static String expectIdentifier() {
		Token token = Parser.currentToken;
		if (acceptIdentifier() != null)
			return (token.getIdentifier().toString());
		Util.ERROR("Got symbol " + token + " while expecting an Identifier");
		return (null);
	}  

	public static String expectParantesedIdentifier() {
		Parser.expect(KeyWord.BEGPAR);
		String id=acceptIdentifier(); // May be missing
		Parser.expect(KeyWord.ENDPAR); 
		return(id);
	}

    public static Type acceptType() {
    	return(Type.parse());
    }

	public static Type expectType() {
		Token token = Parser.currentToken;
		Type type=acceptType();
		if (type != null) return (type);
		Util.ERROR("Got symbol " + token + " while expecting a Type");
		return (null);
	}  

	public static void TRACE(final String msg) {
		if(Option.TRACE_PARSE) Util.TRACE(msg + ", current=" + Parser.currentToken + ", prev=" + Parser.prevToken);
	}

	public static void BREAK(final String msg) {
		Util.BREAK(msg + ", current=" + Parser.currentToken + ", prev=" + Parser.prevToken);
	}
	

    //********************************************************************************
    //**	                                                                  nextSymb 
    //********************************************************************************
	public static void nextSymb() {
		Parser.prevToken = Parser.currentToken;
		if (savedToken != null) {
			Parser.currentToken = savedToken;
			savedToken = null;
			return;
		}
		while(MacroScanner.currentMacroScanner!=null) {
			Parser.currentToken=MacroScanner.currentMacroScanner.nextToken();
//			if(Option.TRACE_PARSE) println("Parser.nextSymb: "+Parser.currentToken+"                 (via macroScanner "+currentMacroScanner.identifier()+" 1)"+", prevToken="+Parser.prevToken);
			if(Parser.currentToken!=null) return;
		}
		Parser.currentToken = simulettaScaner.nextToken();
		if (Parser.currentToken == null) {
			if (!endOfFileErrorGiven) {
				//Util.warning("Possible scanning past END-OF-FILE");
			}
			endOfFileErrorGiven = true;
			Parser.currentToken = new Token(KeyWord.END);
		}
		if(Parser.currentToken.getKeyWord()==KeyWord.EXPAND) {
			MacroScanner.currentMacroScanner=new MacroScanner((MacroDefinition)Parser.currentToken.getValue());
			Parser.currentToken=MacroScanner.currentMacroScanner.nextToken();
//			if(Option.TRACE_PARSE)println("Parser.nextSymb: "+Parser.currentToken+"                 (via macroScanner "+MacroScanner.currentMacroScanner.identifier()+" 2)"+", prevToken="+Parser.prevToken);
			return;
		}
		if(Option.TRACE_PARSE || Option.TRACE_MACRO_SCAN) println("Parser.nextSymb: "+Parser.currentToken+", prevToken="+Parser.prevToken);
	}
	
	
}
