package simula.parser;

import com.intellij.lang.PsiBuilder;

import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.LOG;
import simula.lexer.Identifier;
import simula.lexer.SimulaToken;

public class Parse {

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
////				IO.println("Line "+ Global.sourceLineNumber+": Parse.accept: " + KeyWord.edit(key) + " accepted, nextToken: " + Parse.currentToken);
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
//				IO.println("Line "+ Global.sourceLineNumber+": Parse.accept: " + KeyWord.edit(key) + " accepted, nextToken: " + Parse.currentToken);
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

//	/// Test to accept a Type.
//	/// @return the type or null
//	public static Type acceptType(final PsiBuilder simBuilder) {
//		Type type=null; //Type.Notype;
//		if(accept(simBuilder, KeyWord.BOOLEAN)) type=Type.Boolean;
//		else if(accept(simBuilder, KeyWord.CHARACTER)) type=Type.Character;
//		else if(accept(simBuilder, KeyWord.INTEGER)) type=Type.Integer;
//		else if(accept(simBuilder, KeyWord.SHORT)) { Parse.expect(simBuilder, KeyWord.INTEGER); type=Type.Integer; }
//		else if(accept(simBuilder, KeyWord.REAL)) type=Type.Real;
//		else if(accept(simBuilder, KeyWord.LONG)) { Parse.expect(simBuilder, KeyWord.REAL); type=Type.LongReal; }
//		else if(accept(simBuilder, KeyWord.TEXT)) type=Type.Text;
//		else if(accept(simBuilder, KeyWord.REF))	{
//			Parse.expect(simBuilder, KeyWord.BEGPAR); Token classIdentifier=Parse.currentToken;
//			Parse.expect(simBuilder, KeyWord.IDENTIFIER); Parse.expect(simBuilder, KeyWord.ENDPAR); 
//			type=Type.Ref(classIdentifier.toString()); 
//		}
//		return(type);  
//	}
	
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

	
//	/// Debug utility: Utility TRACE.
//	/// @param msg a message
//	public static void TRACE(final String msg) {
//		Util.TRACE(msg + ", current=" + Parse.currentToken + ", prev=" + Parse.prevToken);
//	}

}
