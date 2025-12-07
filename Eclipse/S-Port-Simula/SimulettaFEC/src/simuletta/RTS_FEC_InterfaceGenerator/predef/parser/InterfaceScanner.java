/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.RTS_FEC_InterfaceGenerator.predef.parser;

import static simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util.*;

import java.io.Reader;
import java.util.LinkedList;
import java.util.Stack;

import simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.KeyWord;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

/**
 * A simuletta Scanner
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class InterfaceScanner {
    private static final int EOF_MARK=25; // ISO EM(EndMedia) character used to denote end-of-input
    public boolean EOF_SEEN=false;        // Set 'true' when EOF-character ( -1 ) was read.
    private SourceFileReader sourceFileReader;      // The source file reader;
    private Stack<Character> puchBackStack=new Stack<Character>();
    private boolean selector[]=new boolean[256];
    
    private StringBuilder accum;
    private final boolean editorMode;  // TODO: REMOVE ?

    private LinkedList<Token> tokenQueue=new LinkedList<Token>();

    //********************************************************************************
    //*** CONSTRUCTORS: SimulettaScanner
    //********************************************************************************
	/**
	 * Constructs a new SimulettaScanner that produces Items scanned from the specified
	 * source.
	 * 
	 * @param reader The character source to scan
	 * @param editorMode true: delivers tokens to the SimulaEditor
	 */
	public InterfaceScanner(final String name,final Reader reader,final boolean editorMode) {
		this.sourceFileReader=new SourceFileReader(name,reader);
		this.editorMode=editorMode;
		PredefGlobal.sourceLineNumber=1;
		if(RTS_FEC_Interface_Option.SELECT!=null) this.setSelectors(RTS_FEC_Interface_Option.SELECT);
	}

    //********************************************************************************
    //*** Insert
    //********************************************************************************
	void insert(Reader reader) {
		this.sourceFileReader.insert(reader);
	}


    //********************************************************************************
    //*** Close
    //********************************************************************************
	void close() {
		SEARCH:while(!EOF_SEEN) {
			int c=readNextCharacter();
			if(!EOF_SEEN && !isWhiteSpace(c)) {
				Util.ERROR("Text after final END");
				break SEARCH;
			}
		}
		sourceFileReader.close();
		sourceFileReader=null;
	}

    //********************************************************************************
    //**	                                                                 nextToken 
    //********************************************************************************
	public Token nextToken() {
    	Token token;
		if(tokenQueue.size()>0) { 
		    token=tokenQueue.remove();
		} else token = scanToken();
		if (RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("Item.nextToken, " + edcurrent());
//		IO.println("Scanner.nextToken - TOKEN: "+token);
		return (token);
	}
	
    //********************************************************************************
    //**	                                                                 scanToken 
    //********************************************************************************
    //** End-Condition: current is last character of construct
    //**                getNext will return first character after construct
    //********************************************************************************
    private Token scanToken() {
  	  Token token;
  	  if (editorMode) {
  			  token = scanBasic();    
  	  } else {
  		do token = scanBasic();
  			while (token!=null && ( token.getKeyWord() == KeyWord.COMMENT));
  	  }
  	  return(token);
    }
    
    
    //********************************************************************************
    //**	                                                                 scanBasic 
    //********************************************************************************
    private Token scanBasic() {
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("SimulettaScanner.scanBasic, "+edcurrent());
    	while(true)	{
    		if(Character.isLetter(getNext())) {
    			Token id=scanIdentifier();
    			//println("SimulettaScanner.scanBasic: id="+id);
//       			if(duringMacroDefinition && id.getKeyWord()==KeyWord.ENDMACRO) duringMacroDefinition=false;
//       			else if(!duringMacroDefinition && id.getKeyWord()==KeyWord.IDENTIFIER) {
//    				Object val=mnemonics.get(id.getIdentifier().toUpperCase());
//        			//println("SimulettaScanner.scanBasic: val="+val);
//    				if(val!=null) {
////    					if(val instanceof Token) return((Token)val);
//    					if(val instanceof LiteralMnemonic lit) return(lit.token);
//    					else return(new Token(KeyWord.EXPAND,val));
//    				}
//    			}
				//Util.BREAK("SimulettaScanner.scanBasic: RETURN IDENT: "+id);
    			return(id);
    		}
    		switch(current) {
    			case '_':				   return(scanIdentifier());
    		    case '=':	               
    		    	if(getNext() == '=')   return(newToken(KeyWord.DEFINE));
    		    	pushBack(current);     return(newToken(KeyWord.EQ));
	            case '>':
		            if(getNext() == '=')   return(newToken(KeyWord.GE));
		            pushBack(current);     return(newToken(KeyWord.GT));
	            case '<':
	                if(getNext() == '=')   return(newToken(KeyWord.LE));
		            if(current == '>')     return(newToken(KeyWord.NE));
		            pushBack(current);     return(newToken(KeyWord.LT));
	            case '+':                  return(newToken(KeyWord.PLUS));
	            case '-':
	            	if(getNext() == '-')   return(scanCommentToEndOfLine());
	                pushBack(current); 	   return(newToken(KeyWord.MINUS));
	            case '*':		           return(newToken(KeyWord.MUL));
	            case '/':		           return(newToken(KeyWord.DIV));
	            case '.':
		            if(Character.isDigit(getNext())) { return(scanDotDigit(new StringBuilder())); }
		            pushBack(current);     return(newToken(KeyWord.DOT));
	            case ',':	               return(newToken(KeyWord.COMMA));
	            case ':':
		            if(getNext() == '=')   return(newToken(KeyWord.ASSIGN));
		            pushBack(current);     return(newToken(KeyWord.COLON));
	            case '(':				   return(newToken(KeyWord.BEGPAR));
	            case ')':				   return(newToken(KeyWord.ENDPAR));
	            case '@':				   return(newToken(KeyWord.ADDR));
	            case '?':				   return(newToken(KeyWord.INDEFINITE));
	            case '&':
				    if(getNext()=='&' || current=='-' || current=='+' || Character.isDigit(current)) 
					return (scanDigitsExp(null));
				
				    pushBack(current); return (newToken(KeyWord.CONC));
	            case '!':  return(scanComment());
	            case '\'': return(scanCharacterConstant());
	            case '\"': return(scanTextConstant());
	            case '0':case '1':case '2':case '3':case '4':
	            case '5':case '6':case '7':case '8':case '9':return(scanNumber());
	   
	            case EOF_MARK:
	            	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("GOT END-OF-FILE");
	            	return(null);
	    	  
	            case '%': return(newToken(KeyWord.PERCENT));
	            case ';':           return(newToken(KeyWord.SEMICOLON));
	    	
	            case '\n':			/* NL (LF) */
	            	if(getNext() == '%') return(scanDirectiveLine());
	            	pushBack(current);
	    	      if (editorMode) return(newToken(KeyWord.NEWLINE,PredefGlobal.sourceLineNumber+1));
	            case ' ':
	            case '\b':			/* BS */
	            case '\t':			/* HT */
	           	//case '\v':		/* VT */
	            case '\f':			/* FF */
	            case '\r':			/* CR */
	            	break;
	            default: Util.ERROR("Illegal character: "+(char)current+", Value="+(int)current);
	                break;
    		}
    	}
    }
  
    //********************************************************************************
    //**	                                                            scanIdentifier 
    //********************************************************************************
    //** End-Condition: current is last character of construct
    //**                getNext will return first character after construct
    //********************************************************************************
    private Token javaKeyword(final String name) {
//    	return(identifierToken(name+'$'));
    	return(identifierToken(name));
    }
    private Token identifierToken(final String name) {
    	Token token=newToken(KeyWord.IDENTIFIER,name);
//    	if(Parser.prevToken.getKeyWord()==KeyWord.IDENTIFIER) Util.ERROR("Misplaced identifier "+name+" directly after "+Parser.prevToken);
    	return(token);
    }
    	
	private Token scanIdentifier() {
		String name=scanName();
	    if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("Token.scanIdentifier: name=\""+name+"\"");
	    switch(name.toUpperCase().charAt(0)) {
	        case 'A':
		        if(name.equalsIgnoreCase("ABSTRACT"))	return(javaKeyword(name)); // Java KeyWord
		        if(name.equalsIgnoreCase("AND"))		return(Token.AND);
		        if(name.equalsIgnoreCase("ARRAY"))	    return(Token.ARRAY);
		        break;
	        case 'B':
	        	if(name.equalsIgnoreCase("BEGIN"))      return(Token.BEGIN);
	        	if(name.equalsIgnoreCase("BODY")) 	    return(Token.BODY);
	        	if(name.equalsIgnoreCase("BOOLEAN"))    return(Token.BOOLEAN);
	        	if(name.equalsIgnoreCase("BREAK"))	    return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("BYTE"))	    return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'C':
//	        	if(name.equalsIgnoreCase("CALL"))	    return(Token.CALL);
	        	if(name.equalsIgnoreCase("CASE"))		return(Token.CASE);
	        	if(name.equalsIgnoreCase("CATCH"))	    return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("CLASS"))      return(Token.CLASS);
	        	if(name.equalsIgnoreCase("CHAR"))  	    return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("CHARACTER"))  return(Token.CHARACTER);
	        	if(name.equalsIgnoreCase("COMMENT"))    return(scanComment());
	        	if(name.equalsIgnoreCase("CONST"))	    return(Token.CONST);
	        	if(name.equalsIgnoreCase("CONTINUE"))	return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'D':
	        	if(name.equalsIgnoreCase("DEFAULT"))	return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("DEFINE"))     return(Token.DEFINE);
	        	if(name.equalsIgnoreCase("DO")) 	    return(Token.DO);
	        	if(name.equalsIgnoreCase("DOUBLE"))	    return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'E':
	        	if(name.equalsIgnoreCase("ELSE"))       return(Token.ELSE);
	        	if(name.equalsIgnoreCase("ELSIF"))      return(Token.ELSIF);
	        	if(name.equalsIgnoreCase("END"))   	    return(Token.END);
	        	if(name.equalsIgnoreCase("ENDCASE"))    return(Token.ENDCASE);
	        	if(name.equalsIgnoreCase("ENDIF")) 	    return(Token.ENDIF);
	        	if(name.equalsIgnoreCase("ENDMACRO"))   return(Token.ENDMACRO);
	        	if(name.equalsIgnoreCase("ENDREPEAT"))  return(Token.ENDREPEAT);
	        	if(name.equalsIgnoreCase("ENDSKIP"))    return(Token.ENDSKIP);
	        	if(name.equalsIgnoreCase("ENTRY")) 	    return(Token.ENTRY);
	        	if(name.equalsIgnoreCase("ENUM"))		return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("EQ"))	        return(Token.EQ);
	        	if(name.equalsIgnoreCase("EXIT"))	    return(Token.EXIT);
	        	if(name.equalsIgnoreCase("EXPORT"))	    return(Token.EXPORT);
	        	if(name.equalsIgnoreCase("EXTENDS"))	return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("EXTERNAL"))   return(Token.EXTERNAL);
	        	break;
	        case 'F':
	        	if(name.equalsIgnoreCase("false"))  	return(Token.FALSE);
	        	if(name.equalsIgnoreCase("field"))  	return(Token.FIELD);
	        	if(name.equalsIgnoreCase("FINAL"))  	return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("FINALLY"))	return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("FLOAT"))	    return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'G':
	        	if(name.equalsIgnoreCase("GE"))     return(Token.GE);
	        	if(name.equalsIgnoreCase("GLOBAL")) return(Token.GLOBAL);
	        	if(name.equalsIgnoreCase("GOTO"))   return(Token.GOTO);
	        	if(name.equalsIgnoreCase("GT"))     return(Token.GT);
	        	break;
	        case 'I':
	        	if(name.equalsIgnoreCase("IF"))	        return(Token.IF);
	        	if(name.equalsIgnoreCase("IMPLEMENTS")) return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("IMPORT"))	    return(Token.IMPORT);
	        	if(name.equalsIgnoreCase("INFIX"))   	return(Token.INFIX);
	        	if(name.equalsIgnoreCase("INFO"))   	return(Token.INFO);
	        	if(name.equalsIgnoreCase("INSERT")) 	return(Token.INSERT);
	        	if(name.equalsIgnoreCase("INSTANCEOF")) return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("INT"))		return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("INTEGER"))	return(Token.INTEGER);
	        	if(name.equalsIgnoreCase("INTERFACE"))  return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'K':
	        	if(name.equalsIgnoreCase("KNOWN"))		return(Token.KNOWN);
	        	break;
	        case 'L':
	        	if(name.equalsIgnoreCase("LABEL")) return(Token.LABEL);
	        	if(name.equalsIgnoreCase("LE"))    return(Token.LE);
	        	if(name.equalsIgnoreCase("LONG"))  return(Token.LONG);
	        	if(name.equalsIgnoreCase("LT"))    return(Token.LT);
	        	break;
	        case 'M':
	        	if(name.equalsIgnoreCase("MACRO"))   return(Token.MACRO);
	        	if(name.equalsIgnoreCase("MODULE"))  return(Token.MODULE);
	        	break;
	        case 'N':
	        	if(name.equalsIgnoreCase("NAME"))   return(Token.NAME);
	        	if(name.equalsIgnoreCase("NATIVE")) return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("NE"))     return(Token.NE);
	        	if(name.equalsIgnoreCase("NEW"))    return(javaKeyword(name)); // Java KeyWord	        	
	        	if(name.equalsIgnoreCase("NOBODY")) return(Token.NOBODY);
	        	if(name.equalsIgnoreCase("NOFIELD")) return(Token.NOFIELD);
	        	if(name.equalsIgnoreCase("NONAME")) return(Token.NONAME);
	        	if(name.equalsIgnoreCase("NONE"))   return(Token.NONE);
	        	if(name.equalsIgnoreCase("NOSIZE")) return(Token.NOSIZE);
	        	if(name.equalsIgnoreCase("NOT"))    return(Token.NOT);
	        	if(name.equalsIgnoreCase("NOWHERE")) return(Token.NOWHERE);
	        	if(name.equalsIgnoreCase("NULL"))   return(javaKeyword(name)); // Java NullLiteral
	        	break;
	        case 'O':
	        	if(name.equalsIgnoreCase("OR"))         return(Token.OR);
	        	if(name.equalsIgnoreCase("OTHERWISE"))  return(Token.OTHERWISE);
	        	break;
	        case 'P':
	        	if(name.equalsIgnoreCase("PACKAGE"))	return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("PRIVATE"))	return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("PROFILE"))	return(Token.PROFILE);
	        	if(name.equalsIgnoreCase("PROCEDURE"))    return(Token.PROCEDURE);
	        	if(name.equalsIgnoreCase("PUBLIC"))	    return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'Q':
	        	if(name.equalsIgnoreCase("QUA"))        return(Token.QUA);
	        	break;
	        case 'R':
	        	if(name.equalsIgnoreCase("RANGE"))		return(Token.RANGE);
	        	if(name.equalsIgnoreCase("REAL"))       return(Token.REAL);
	        	if(name.equalsIgnoreCase("REF"))        return(Token.REF);
//	        	if(name.equalsIgnoreCase("REM"))        return(Token.REM);
	        	if(name.equalsIgnoreCase("REPEAT"))     return(Token.REPEAT);
	        	if(name.equalsIgnoreCase("RETURN"))	    return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'S':
	        	if(name.equalsIgnoreCase("SHORT"))  		return(Token.SHORT);
//	        	if(name.equals("sourceline"))  		    	return(newToken(KeyWord.SIMPLEVALUE,Global.sourceLineNumber));
//	        	if(name.equalsIgnoreCase("SIZE"))  			return(Token.SIZE);
	        	if(name.equalsIgnoreCase("SKIP"))  			return(Token.SKIP);
	        	if(name.equalsIgnoreCase("STATIC"))	        return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("STRICTFP"))	    return(javaKeyword(name)); // Java KeyWord
//	        	if(name.equalsIgnoreCase("STRING"))    		return(identifierToken("String")); // Special Case
//	        	if(name.equals("String"))    				return(Token.STRING); // Special Case
	        	if(name.equalsIgnoreCase("SUPER"))	        return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("SYNCHRONIZED"))	return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("SYSINSERT"))		return(Token.SYSINSERT);
	        	if(name.equalsIgnoreCase("SYSROUTINE")) 	return(Token.SYSROUTINE);
	        	if(name.equalsIgnoreCase("SYSTEM")) 		return(Token.SYSTEM);
	        	break;
	        case 'T':
	        	if(name.equalsIgnoreCase("TEXT"))	    return(Token.TEXT);
	        	if(name.equalsIgnoreCase("THEN"))  	    return(Token.THEN);
	        	if(name.equalsIgnoreCase("THIS"))   	return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("THROW"))	    return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("THROWS"))	    return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("TRANSIENT"))  return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("true"))   	return(Token.TRUE);
	        	if(name.equalsIgnoreCase("TRY"))	  	return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'V':
	        	if(name.equalsIgnoreCase("VALUE"))     return(Token.VALUE);
	        	if(name.equalsIgnoreCase("VISIBLE"))   return(Token.VISIBLE);
	        	if(name.equalsIgnoreCase("VOID"))	   return(javaKeyword(name)); // Java KeyWord
	        	if(name.equalsIgnoreCase("VOLATILE"))  return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'W':
	        	if(name.equalsIgnoreCase("WHEN"))  return(Token.WHEN);
	        	if(name.equalsIgnoreCase("WHILE")) return(Token.WHILE);
	        case 'X':
	        	if(name.equalsIgnoreCase("XOR"))  return(Token.XOR);
	        	break;
	    }
	    return(identifierToken(name));
	}
	
    //********************************************************************************
    //**	                                                                scanNumber 
    //********************************************************************************
    /**
    * Scan a unsigned number.
    * <pre>
    *  Reference-Syntax:
    *      unsigned-number
    *        = decimal-number  [  exponent-part  ]
    *        | exponent-part
    *      decimal-number
    *        = unsigned-integer  [  decimal-fraction  ]
    *        | decimal-fraction
    *      decimal-fraction
    *        = .  unsigned-integer
    *      exponent-part
    *        =  ( & | && )  [ + | - ]  unsigned-integer
    *      unsigned-integer
    *        =  digit  {  digit  |  _  }
    *        |  radix  R  radix-digit  {  radix-digit  |  _  radix-digit  }
    *      radix
    *        =  2  |  4  |  8  |  16
    *      radix-digit
    *        =  digit  |  A  |  B  |  C  |  D  |  E  |  F
    * </pre>
    * <b>End-Condition:</b>
    *  <ul>
    *  <li>current is last character of construct</li>
    *  <li>getNext will return first character after construct</li>
    * </ul>
    * @return A Item representing a constant number.
    */
    private Token scanNumber() {
    	int radix=10;
    	char firstChar=(char)current;
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanNumber, "+edcurrent());
    	Util.ASSERT(Character.isDigit((char)(current)),"scanNumber:Expecting a Digit");
    	StringBuilder number=new StringBuilder();
	
    	number.append((char)current);
    	if(getNext() == 'R' && (firstChar == '2' | firstChar == '4' | firstChar == '8')) {
    		radix=firstChar - '0';
    		if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanNumber, radix="+radix);
    		number.setLength(0);
    	} else if(firstChar == '1' && current == '6') { 
    		number.append((char)current);
    		if(getNext() == 'R') {
    			radix=16;
    			if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanNumber, radix="+radix);
    			number.setLength(0);
    		} else pushBack(current);
    	} else pushBack (current);
    
    	while ((radix==16 ? isHexDigit(getNext()) : Character.isDigit(getNext())) || current=='_')
    		if(current!='_') number.append((char)current);
    
    	if(current == '.' && radix == 10) { getNext(); return(scanDotDigit(number)); }
    
    	if(current == '&' && radix == 10) { getNext(); return(scanDigitsExp(number)); }
      
    	String result=number.toString(); number=null;
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanNumber, result='"+result+"' radix="+radix);

    	pushBack(current);
    	return(newToken(KeyWord.SIMPLEVALUE,Long.parseLong(result,radix)));
    }


    //********************************************************************************
    //**	                                                              scanDotDigit 
    //********************************************************************************
    //** End-Condition: current is last character of construct                 CHECKED
    //**                getNext will return first character after construct
    //********************************************************************************
    private Token scanDotDigit(StringBuilder number) {
    	/* Behandling av tall som starter med tegnet '.' */
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanDotDigit, "+edcurrent());
    	number.append('.');
    	if(Character.isDigit(current)) number.append((char)current);
    	while(Character.isDigit(getNext()) || current == '_')
    		if(current != '_') number.append((char)current);

    	if(current == '&') { getNext(); return(scanDigitsExp(number)); }
    
    	String result=number.toString(); number=null;
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanDotDigit, result='"+result);
    	pushBack(current);
    	try {
    		return(newToken(KeyWord.SIMPLEVALUE,Float.parseFloat(result)));
    	} catch(NumberFormatException e) {
    		Util.ERROR("Illegal number: "+result);
    		return(newToken(KeyWord.SIMPLEVALUE,null));
    	}
    }
	
    //********************************************************************************
    //**	                                                             scanDigitsExp 
    //********************************************************************************
    //** End-Condition: current is last character of construct                 CHECKED
    //**                getNext will return first character after construct
    //********************************************************************************
    private Token scanDigitsExp(StringBuilder number) {
    	String result;
    	boolean doubleAmpersand=false;
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanDigitsExp, "+edcurrent());
    	if(number==null) { number=new StringBuilder(); number.append('1'); }
    	if(current == '&') { getNext(); doubleAmpersand=true; }
    	number.append('e');
    	if(current == '-') { number.append('-'); getNext(); }
    	else if(current == '+') getNext();
    	if(Character.isDigit(current)) number.append((char)current);
    	while(Character.isDigit(getNext()) || current == '_') number.append((char)current);
	      
    	result=number.toString(); number=null;
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanDigitsExp, result='"+result);
    	pushBack(current);
    	try {
    		if(doubleAmpersand) return(newToken(KeyWord.SIMPLEVALUE,Double.parseDouble(result)));
    		return(newToken(KeyWord.SIMPLEVALUE,Float.parseFloat(result)));
    	} catch(NumberFormatException e) {
    		Util.ERROR("Illegal number: "+result);
    		return(newToken(KeyWord.SIMPLEVALUE,null));
    	}
    }
	

    //********************************************************************************
    //**					                                                  scanName
    //********************************************************************************
    //**  Reference-Syntax:
    //**      identifier
    //**       = letter  { letter  |  digit  |  _  }
    //********************************************************************************
    // Scan identifier or reserved name.                                       CHECKED
    //** End-Condition: current is last character of construct
    //**                getNext will return first character after construct
    //********************************************************************************
    private String scanName() {
    	StringBuilder name=new StringBuilder();
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanName, "+edcurrent());
    	Util.ASSERT(Character.isLetter((char)(current)) || current == '_',"Expecting a Letter");
    	name.append((char)current);
    	while ((Character.isLetter(getNext()) || Character.isDigit(current) || current == '_'))
    		name.append((char)current);
    	pushBack(current);
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanName, name="+name+",current="+edcurrent());
    	return(name.toString());
    }
	

    //********************************************************************************
    //**	                                                     scanCharacterConstant
    //********************************************************************************
    //**  Reference-Syntax:                                                    CHECKED
    //**      character-constant
    //**       = '  character-designator  '
    //**      character-designator
    //**       = iso-code
    //**        |  non-quote-character
    //**        |  "
    //**      iso-code
    //**       =! digit  [ digit ]  [ digit ]  !
    //********************************************************************************
    //** End-Condition: current is last character of construct
    //**                getNext will return first character after construct
    //********************************************************************************
    private Token scanCharacterConstant() {
    	char result=0;
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanCharacterConstant, "+edcurrent());
    	Util.ASSERT((char)(current)=='\'',"Expecting a character quote '");
    	if((isPrintable(getNext())) && current != '!') {
    		result=(char)current; getNext();
    	} else if(current == '!') {
    		result=(char)scanPossibleIsoCode(); getNext();
    	} else Util.ERROR("Illegal character constant. "+edcurrent());
    	
    	if(current != '\'') {
    		Util.ERROR("Character constant is not terminated. "+edcurrent());
    		pushBack(current);
    	}
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("END scanCharacterConstant, result='"+result+"', "+edcurrent());
    	return(newToken(KeyWord.SIMPLEVALUE,Character.valueOf(result)));
    }  
    
    
    //********************************************************************************
    //**	                                                          scanTextConstant
    //********************************************************************************
    //**  Reference-Syntax:                                                    CHECKED
    //**      string
    //**       = simple-string  {  string-separator  simple-string  }
    //**      simple-string
    //**       = " { iso-code |  non-quote-character  |  ""  }  "
    //**      iso-code
    //**       =! digit  [ digit ]  [ digit ]  !
    //**      string-separator
    //**       = token-separator  {  token-separator  }
    //**      token-separator
    //**       = a direct comment
    //**        |  a space  { except in simple strings and character constants }
    //**        |  a format effector  { except as noted for spaces }
    //**        |  the separation of consecutive lines
    //********************************************************************************
    //** End-Condition: current is last character of construct
    //**                getNext will return first character after construct
    //********************************************************************************
    private Token scanTextConstant() {
    	if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanTextConstant, "+edcurrent());
    	StringBuilder accumulatedTextConstant=new StringBuilder();
    	int firstLine=PredefGlobal.sourceLineNumber;
    	int lastLine=firstLine;
    	LOOP:while(true) {
    		// Scan simple-string:
    		while(getNext() != '"') {
    			if(current=='!') accumulatedTextConstant.append((char)scanPossibleIsoCode());
    			else if(current == EOF_MARK) {
    				Util.ERROR("Text constant is not terminated.");
    				String result=accumulatedTextConstant.toString(); accumulatedTextConstant=null;
    				if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanTextConstant(1): Result=\""+result+"\", "+edcurrent());
    				tokenQueue.add(newToken(KeyWord.SIMPLEVALUE,result));
    				break LOOP;
    			} else accumulatedTextConstant.append((char)current);
    		}
//    		if(editorMode) tokenQueue.add(newToken(KeyWord.STRING));
    		lastLine=PredefGlobal.sourceLineNumber;
    		if(getNext() == '"') {
    			accumulatedTextConstant.append('"');
    			lastLine=PredefGlobal.sourceLineNumber;
    		} else {
    			// Skip string-separator
    			while(currentIsStringSeparator()) getNext();
    			if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanTextConstant(2): "+edcurrent());
    			if(current!='"') {
    				pushBack(current);
    				String result=accumulatedTextConstant.toString(); accumulatedTextConstant=null;
    				if(RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanTextConstant(2): Result=\""+result+"\", "+edcurrent());
    				if(firstLine<lastLine) Util.WARNING("Text constant span mutiple source lines");
    				result=result.replace("\n","\\n");
    				tokenQueue.add(newToken(KeyWord.SIMPLEVALUE,result));
    				break LOOP;
    			}
    		}
    	}
    	Token result=tokenQueue.remove();
    	return(result);
    }
	
    //********************************************************************************
  	//**	                                                  currentIsStringSeparator
    //********************************************************************************
    //**  Reference-Syntax:
    //**      string-separator
    //**       = token-separator  {  token-separator  }
    //**      token-separator
    //**       = a direct comment
    //**        |  a space  { except in simple strings and character constants }
    //**        |  a format effector  { except as noted for spaces }
    //**        |  the separation of consecutive lines
    //********************************************************************************
    //** End-Condition: current is last character of construct
    //**                getNext will return first character after construct
    //********************************************************************************
    private boolean currentIsStringSeparator() {
    	if(current=='!') {
    		Token cc=scanComment();
    		if(editorMode) tokenQueue.add(cc);
    		current=' '; return(true);
    	} else if(Character.isLetter((char)current)) {
    		String name=scanName();
    		if(name.equalsIgnoreCase("COMMENT")) {
        		Token cc=scanComment();
        		if(editorMode) tokenQueue.add(cc);
    			current=' '; return(true);
    		} else pushBack(name);
    		return(false);
    	} else if(current=='%' && prevChar=='\n') {
			// Directive inside Text-Constant
    		Token cc=scanDirectiveLine();
    		if(editorMode) tokenQueue.add(cc);
			current=' '; return(true);
		}
    	return(isWhiteSpace(current));
    }
  
  

    //********************************************************************************
    //**	                                                       scanPossibleIsoCode
    //********************************************************************************
    //**  Reference-Syntax:
    //**      iso-code
    //**       =! digit  [ digit ]  [ digit ]  !
    //********************************************************************************
    //** End-Condition: current is last character of construct
    //**                getNext will return first character after construct
    //********************************************************************************
	private int scanPossibleIsoCode() {
		char firstchar, secondchar, thirdchar;
		if (RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanPossibleIsoCode, " + edcurrent());
		Util.ASSERT((char) (current) == '!', "Expecting a character !");
		if (Character.isDigit(getNext())) {
			firstchar = (char) current;
			if (Character.isDigit(getNext())) {
				secondchar = (char) current;
				if (Character.isDigit(getNext())) {
					thirdchar = (char) current;
					if (getNext() == '!') { // ! digit digit digit ! Found
						int value = (((firstchar - '0') * 10 + secondchar - '0') * 10 + thirdchar - '0');
						if (RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("scanPossibleIsoCode:Got three digits: "+(char)firstchar+(char)secondchar+(char)thirdchar+"value="+value);
						if (value < 256)
							return (value);
						Util.WARNING("ISO-Code " + value + " is out of range (0:255)");
						pushBack(current);
						pushBack(thirdchar);
						pushBack(secondchar);
						pushBack(firstchar);
						return ('!');
					} else {
						pushBack(current);
						pushBack(thirdchar);
						pushBack(secondchar);
						pushBack(firstchar);
						return ('!');
					}
				} else if (current == '!') { // ! digit digit ! Found
					return ((char) ((firstchar - '0') * 10 + secondchar - '0'));
				} else {
					pushBack(current);
					pushBack(secondchar);
					pushBack(firstchar);
					return ('!');
				}
			} else if (current == '!') { // ! digit ! Found
				return ((char) (firstchar - '0'));
			} else {
				pushBack(current);
				pushBack(firstchar);
				return ('!');
			}
		} else {
			pushBack(current);
			return ('!');
		}
	}

    //********************************************************************************
    //**	                                                         scanDirectiveLine
    //********************************************************************************
    //**  Reference-Syntax:
    //**      directive
    //**       =  % { any character except end-of-line }
    //********************************************************************************
    //** End-Condition: current is last character of construct               UNCHECKED
    //**                getNext will return first character after construct
    //********************************************************************************
	private Token scanDirectiveLine() {
		getNext();
		if(current==' ') {
			readUntilEndofLine(); // Skip comment line
		    return (newToken(KeyWord.COMMENT));
		} else if(current=='+' || current=='-') {
			if(lineSelected()) {
	        	char next=(char) getNext(); // pushBack(current);
				if(RTS_FEC_Interface_Option.TRACE_SELECTION) Util.println("SimulettaScanner.scanDirectiveLine: SELECTED: next="+next);
				if(next=='%') return(scanDirectiveLine());
				else pushBack(current);
		    } else {
		    	if(RTS_FEC_Interface_Option.TRACE_SELECTION) Util.println("SimulettaScanner.scanDirectiveLine: NOT SELECTED");
				readUntilEndofLine();
		    }
        	if(RTS_FEC_Interface_Option.TRACE_SELECTION) TRACE("SimulettaScanner.scanDirectiveLine: RETURN COMMENT");
		    return (newToken(KeyWord.COMMENT));
		} else if(Character.isLetter(current)) {
			String id=scanName();
			if (id.equalsIgnoreCase("SELECT")) setSelectors();
			else Directive.treatDirectiveLine(this,id,readUntilEndofLine());
		}
	    return (newToken(KeyWord.COMMENT));
	}
	
	private String readUntilEndofLine() {
		StringBuilder line=new StringBuilder();
		while(getNext()!='\n') {
			line.append((char)current);
		}
		pushBack('\n');
		return(line.toString().trim());
	}

    /**
     * %SELECT select-character { select-character }
     * <p>
     * Set selectors for conditional compilation.
     */
    private void setSelectors() {
    	for(int i=0;i<255;i++) selector[i]=false;
    	getNext();
    	while(current==' ') getNext();
    	while(current!=' ' && current!='\n') {
    		selector[current]=true;
    		getNext();
    	}
    }
	private void setSelectors(String set) {
    	for(int i=0;i<255;i++) selector[i]=false;
    	for(int i=0;i<set.length();i++) {
    		int c=set.charAt(i);
    		selector[c]=true;
    	}
    }
    
	private boolean lineSelected() {
		while (true) {
			if (current == '+') {
				getNext();
				if(RTS_FEC_Interface_Option.TRACE_SELECTION) Util.println("SimulettaScanner.lineSelected: CHECK SELECTOR: +"+(char)current);
				while (Character.isLetter(current) | Character.isDigit(current)) {
					if (!selector[current])
						return (false); // then SKIPLINE;
					getNext();
				}
			} else if (current == '-') {
				getNext();
				if(RTS_FEC_Interface_Option.TRACE_SELECTION) Util.println("SimulettaScanner.lineSelected: CHECK SELECTOR: -"+(char)current);
				while (Character.isLetter(current) | Character.isDigit(current)) {
					if (selector[current])
						return (false); // then SKIPLINE;
					getNext();
				}
			} else
				break;
		}
		while (current == ' ') getNext();
		pushBack(current);
		return (true); // Return to scan remainder part of line.
	}
  
	// ********************************************************************************
	// ** scanComment
	// ********************************************************************************
	// ** Reference-Syntax:
	// ** comment
	// ** = COMMENT { any character except semicolon } ;
	// ********************************************************************************
	// ** End-Condition: current is last character of construct CHECKED
	// ** getNext will return first character after construct
	// ********************************************************************************
	private Token scanComment() {
		StringBuilder skipped = new StringBuilder();
		if (RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("BEGIN scanComment, " + edcurrent());
		while ((getNext() != ';') && current != EOF_MARK)
			skipped.append((char) current);
		skipped.append((char) current);
		if (current == ';')
			current = ' '; // getNext();
		else {
			Util.ERROR("Comment is not terminated with ';'.");
			pushBack(current);
		}
		if (RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("END scanComment: " + edcurrent() + "  skipped=\"" + skipped + '"');
		if (RTS_FEC_Interface_Option.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
		return (newToken(KeyWord.COMMENT));
	}
	  
	// ********************************************************************************
	// ** scanCommentToEndOfLine
	// ********************************************************************************
	// ** Reference-Syntax:
	// ** comment
	// ** = -- { any character until end-of-line }
	// ********************************************************************************
	// ** End-Condition: current is last character of construct CHECKED
	// ** getNext will return first character after construct
	// ********************************************************************************
	private Token scanCommentToEndOfLine() {
		StringBuilder skipped = new StringBuilder();
		if (RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("BEGIN scanCommentToEndOfLine, " + edcurrent());
		while ((getNext() != '\n') && current != EOF_MARK)
			skipped.append((char) current);
		skipped.append((char) current);
		pushBack(current);
		if (RTS_FEC_Interface_Option.TRACE_SCAN) Util.TRACE("END scanCommentToEndOfLine: " + edcurrent() + "  skipped=\"" + skipped + '"');
		if (RTS_FEC_Interface_Option.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
		return (newToken(KeyWord.COMMENT));
	}

	
    //********************************************************************************
    //**	                                                                 UTILITIES 
    //********************************************************************************
    private int prevChar;
    private int current;
    private int getNext() {
    	prevChar=current;
	    current=readNextCharacter();
	    if(editorMode) {
	        if(accum==null) accum=new StringBuilder();
	        if(current!='\r' && current!=EOF_MARK) {
	    	    accum.append((char)current);
	        }
	    }
	    return(current);
    }

    private int readNextCharacter() {
    	if(!puchBackStack.empty()) return(puchBackStack.pop());
		int c=sourceFileReader.read();

		if(c<0) { EOF_SEEN=true; return(EOF_MARK); }
		if(c=='\n') PredefGlobal.sourceLineNumber++;
		
		if(c<32 && c!='\n') c=' '; // Whitespace
		return(c);
	}

    private void pushBack(final int chr) {
	    // push given value back into the input stream
    	if(editorMode) {
    		if(current!=EOF_MARK && accum.length()>0)
    		accum.deleteCharAt(accum.length()-1);
    	}
	    puchBackStack.push((char)chr);
	    current=' ';
    }
  
    private void pushBack(final String s) {
	    // put given value back into the input stream
	    int i=s.length();
		while((i--)>0) pushBack(s.charAt(i));
    }
  
	private Token newToken(final int keyWord, final Object value) {
		String text=null;
		if(editorMode) {
	        text=(accum==null)?"":accum.toString();
	        accum=new StringBuilder();
		}
		return(new Token(text,keyWord,value));
	  }

	private Token newToken(final int keyWord) {
		return (newToken(keyWord, null));
	}

	private String edcurrent() {
		if (current < 32)
			return ("Current code=" + current);
		return ("Current='" + (char) current + "' value=" + current);
	}
	
    private boolean isHexDigit(final int c) {
	    switch(c) {
	        case '0':case '1':case '2':case '3':case '4':
	        case '5':case '6':case '7':case '8':case '9':
	        case 'A':case 'B':case 'C':case 'D':case 'E':case 'F':
	        case 'a':case 'b':case 'c':case 'd':case 'e':case 'f': return(true);
	        default: return(false);
	    }
    }
	
	private boolean isPrintable(final int c) {
		if (c < 32) return (false);
		if (c > 126) return (false);
		return (true);
	}

	private boolean isWhiteSpace(final int c) {
		switch(c) {
		    case '\n':	/* NL (LF) */
		    case 32:    /* SPACE */
		    case '\b':	/* BS */
		    case '\t':	/* HT */
		    case 11:	/* VT */
		    case '\f':	/* FF */
		    case '\r':	/* CR */
			         return(true);
		    default: return(false);
		}  
	}

}
