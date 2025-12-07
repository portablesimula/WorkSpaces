/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.RTS_FEC_InterfaceGenerator.predef.util;

import simuletta.RTS_FEC_InterfaceGenerator.predef.parser.Parser;

/**
 * 
 * @author Øystein Myhre Andersen
 *
 */
public class Type {
	public int code;
	public String prefqual; // Prefix or Qual
	public boolean isArray;
	
	// TYPE:
	public static final int INOTY = 14; // no type
	public static final int IINTG =  4; // integer
	public static final int ISHOR =  3; // short integer;
	public static final int IREAL =  5; // real;
	public static final int ILONG =  6; // long real;
	public static final int IBOOL =  1; // boolean;
	public static final int ICHAR =  2; // character;
	public static final int ILABE = 11; // label (switch);
	public static final int ITEXT =  8; // text;
	public static final int IREF  =  7; // ref;
	public static final int IPTR  =  9; //  pointer to record;
	public static final int IELSE = 15; //  universal



	// ***********************************************************************************************
	// *** Parsing: Type.parse
	// ***********************************************************************************************
	/**
	 * <pre>
	 * Syntax:
	 * 
	 * 		Type
	 * 		   ::= INTEGER 
	 * 		   ::= SHORT INTEGER 
	 * 		   ::= REAL 
	 * 		   ::= LONG REAL 
	 * 		   ::= BOOLEAN 
	 * 		   ::= CHARACTER 
	 * 		   ::= LABEL 
	 * 		   ::= TEXT 
	 * 		   ::= REF ( Class'identifier ) 
	 * 		   ::= REF ( Record'Identifier )
	 * 
	 * </pre>
	 */
	public static Type parse() {
    	int code=Type.INOTY;
    	String qual=null;
//    	Util.BREAK("Type.parse: currentToken="+Parser.currentToken);
    	if(Parser.accept(KeyWord.BOOLEAN)) code=Type.IBOOL;
    	else if(Parser.accept(KeyWord.CHARACTER)) code=Type.ICHAR;
    	else if(Parser.accept(KeyWord.INTEGER)) code=Type.IINTG;
		else if(Parser.accept(KeyWord.SHORT)) { Parser.expect(KeyWord.INTEGER); code=Type.ISHOR; }
		else if(Parser.accept(KeyWord.REAL))  code=Type.IREAL;
		else if(Parser.accept(KeyWord.TEXT))  code=Type.ITEXT;
		else if(Parser.accept(KeyWord.LONG))  { Parser.expect(KeyWord.REAL); code=Type.ILONG; }
		else if(Parser.accept(KeyWord.LABEL)) code=Type.ILABE;
		else if(Parser.accept(KeyWord.REF))   { code=Type.IREF; qual=Parser.expectParantesedIdentifier(); } 
		else return(null);
    	Type type=new Type(code,qual);
    	if(Parser.accept(KeyWord.ARRAY)) type.isArray=true;
    	//IO.println("Type.parse: "+type);
    	if(code==Type.INOTY) return(null);
		return(type);  
    }
	
	public static final Type noType=new Type(Type.INOTY,null);
	
	// ***********************************************************************************************
	// *** Constructors
	// ***********************************************************************************************

    public Type(int code, String prefqual) {
    	this.code=code;
    	this.prefqual=prefqual;
	}
	  
	@Override
	public String toString() {
		String tp=toString1();
		if(isArray) tp=tp+" array";
		return(tp);
	}
	public String toString1() {
	switch(code) {
		case INOTY: if(prefqual==null) return("NOTYPE"); else return("prefix("+prefqual+')');
		case IINTG: return("integer");
		case ISHOR: return("short integer");
		case IREAL: return("real");
		case ILONG: return("long real");
		case IBOOL: return("boolean");
		case ICHAR: return("character");
		case ILABE: return("label (switch)");
		case ITEXT: return("text");
		case IREF:  return("ref("+prefqual+')');
		case IPTR:  return("pointer to record");
		case IELSE: return("universal");
		default:    return("illegal");
	}
}

	
	
}
