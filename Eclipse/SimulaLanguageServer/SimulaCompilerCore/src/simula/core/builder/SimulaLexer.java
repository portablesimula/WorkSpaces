/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.builder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import simula.Comn;
import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.export.LexToken;
import simula.core.builder.util.CharacterConst;
import simula.core.builder.util.Identifier;
import simula.core.builder.util.IntegerConst;
import simula.core.builder.util.KeyWordToken;
import simula.core.builder.util.LongRealConst;
import simula.core.builder.util.RealConst;
import simula.core.builder.util.SimpleString;
import simula.core.builder.util.TabToken;
import simula.core.builder.util.WhiteSpaceToken;
import simula.core.utilities.KeyWord;
import simula.core.utilities.Util;
import simula.exception.EOTException;

/// The Simula Scanner.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/parsing/SimulaScanner.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class SimulaLexer {
	private boolean TRACE_CURRENT_COLUMN = false;// true;//false;
	
	private SimulaBuilder simBuilder;
    private CharSequence sourceText;
    private int textEndOffset;
    private int nextPos;
    private int currentLineNumber;
    private int currentColumn;
    private int tokenStartPos; // Used to calculate length
    
    private LexToken prevParserToken;
    private LexToken prevLexerToken;
    private LexToken currentLexerToken;
    
    private List<Integer> lineStartPos = new ArrayList<>();
    public int getLineStartPos(int lineNumber) {
    	int pos = lineStartPos.get(lineNumber);
       	if(Option.LEX_VERIFY) {
//           	IO.println("SimulaLexer.getLineStartPos: "+lineNumber+": "+pos+" TABLE="+lineStartPos);
       		if(nextPos > textEndOffset) Util.IERR("IMPOSSIBLE");
       	}
       	return pos;
    }
    public void update_LineStartPos_List() {
       	lineStartPos.add(nextPos);    	
       	if(Option.LEX_VERIFY) {
//           	IO.println("SimulaLexer.update_LineStartPos_List: "+(lineStartPos.size()-1)+": "+nextPos+" TABLE="+lineStartPos);
       		if(nextPos > textEndOffset) Util.IERR("IMPOSSIBLE");
           	if(lineStartPos.size() != (currentLineNumber+1)) {
        		IO.println("SimulaLexer.scanbasic: currentLineNumber: " + currentLineNumber + ", lineStartPos.size(): " + lineStartPos.size());
           		Util.IERR("IMPOSSIBLE");
           	}
       	}
    }

    public int getSourceLineNumber() {
    	return currentLineNumber;
    }

	/// ISO EM(EndMedia) character used to denote end-of-input
    private static final int EOF_MARK=25;

//    /// EOF is seen
//    public LexToken EOF;
    
    /// Set 'true' when EOF-character ( -1 ) was read.
    /// Set 'true' when EOF-character EOF_MARK was read.
    private boolean EOF_SEEN=false;
 
	public boolean eof() {
		return EOF_SEEN;
	}
	
	public void flush() {
		IO.println("SimulaLexer.close: ");
		while(tokenQueue.size()>0) { 
			nextToken();
		}
//		Util.STOP();
	}

//    /// The pushBack stack
//    private Stack<Character> puchBackStack=new Stack<Character>();

    /// The Token queue. The method nextToken will pick Tokens from the queue first.
    private LinkedList<LexToken> tokenQueue=new LinkedList<LexToken>();

//    /// The current source file reader;
//    SourceFileReader sourceFileReader;
    
    /// The selector array.
    public static boolean selector[]=new boolean[256];

	/// NOTE: An initial "-" in array upper bound may follow directly after : (cf. 1.3).
	/// 
	/// The scanner will treat ":-" within BoundPairList as two
	/// separate symbols ":" and "-" thus solving this ambiguity in the syntax.
	/// 
	/// This variable is used to cover such situations.
//	private int pardepth = 0;
    private boolean parsingBoundPairList;
    
	public void setParsingBoundPairList(boolean parsingBoundPairList) {
		this.parsingBoundPairList = parsingBoundPairList;
	}
	
	
	/// Constructs a new SimulaScanner that produces Items scanned from the specified source.
	/// @param reader The character source to scan
	/// @param editorMode true: delivers tokens to the SimulaEditor
	public SimulaLexer(final SimulaBuilder simBuilder, final CharSequence sourceText) {
//		IO.println("NEW SimulaLexer: sourceText(lng:"+sourceText.length()+")" + Comn.printable((String) sourceText));
		this.simBuilder = simBuilder;
		this.sourceText = sourceText;
		this.textEndOffset = sourceText.length();
		nextPos = 0;
		currentLineNumber = 0;
       	update_LineStartPos_List();
		CoreGlobal.sourceLineNumber=1;
//		nextToken();+
	}


//	private LexToken getPrevLexerToken() {
//        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getPrevLexerToken: "+prevLexerToken);
//        return prevLexerToken;
//    }
//
//	private LexToken getCurrentLexerToken() {
//        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getCurrentLexerToken: "+currentLexerToken);
//        return currentLexerToken;
//    }

	/// Return next 'Parser' token.
	/// Skip Comment, Whitespace and Newline tokens.
	public LexToken getNextParserToken() {
//		lexer.nextToken();                               // And then advance the lexer.				
//		lexer.getNextParserToken();                      // And then advance the lexer.				
        while(true) {
    		LexToken lexToken = nextToken();
    		if(lexToken == null) {
    			lexToken = getEOFToken();
    		}
        	if(lexToken.isParserToken()) return lexToken;
		}
	}

    //********************************************************************************
    //**	                                                                 nextToken 
    //********************************************************************************
	public LexToken nextToken() {
    	prevLexerToken = currentLexerToken;
    	if(prevLexerToken != null) {
    		if(prevLexerToken.keyWord != KeyWord.NEWLINE) {
//    			currentColumn = currentColumn + prevLexerToken.length;
    			currentColumn = prevLexerToken.column + prevLexerToken.length;
    			if(TRACE_CURRENT_COLUMN) IO.println("SimulaLexer.nextToken(1): currentColumn="+currentColumn+", prevLexerToken: "+prevLexerToken);
    		}
    		if(prevLexerToken.isParserToken()) prevParserToken = prevLexerToken;
    	}
    	tokenStartPos = nextPos;
    	
    	LexToken lexToken;
    	if(tokenQueue.size()>0) { 
		    lexToken=tokenQueue.remove();
		    if(Option.internal.TRACE_NEW_LEXTOKEN > 0) IO.println("POP LexToken: " + lexToken);
//			IO.println("SimulaLexer.nextToken: currentColumn="+currentColumn+", nextPos=" + nextPos + ", tokenStartPos="+tokenStartPos);
//			IO.println("SimulaLexer.nextToken: currentColumn="+lexToken.column+" FROM POP TOKEN");
		    if(lexToken.keyWord == KeyWord.EOF) {
//		    	Util.IERR("SJEKK DETTE: GOT EOF");
		    }
		    
			currentColumn = (lexToken.keyWord == KeyWord.NEWLINE)? 0 : lexToken.column;
			if(TRACE_CURRENT_COLUMN) IO.println("SimulaLexer.nextToken(2): currentColumn="+currentColumn);
		} else lexToken = scanToken();
		
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("Item.nextToken, " + edcurrent());
		currentLexerToken = lexToken;
//	    IO.println("GOT LexToken: " + lexToken);
//		IO.println("SimulaLexer.nextToken: currentColumn="+currentColumn+", nextPos=" + nextPos + ", tokenStartPos="+tokenStartPos);
		
		if((lexToken.keyWord != KeyWord.NEWLINE) && (lexToken.keyWord != KeyWord.WHITESPACES))
			simBuilder.lexTokenList.add(lexToken);
		
		return (lexToken);
	}
	
    //********************************************************************************
    //**	                                                                 scanToken 
    //********************************************************************************
	/// Scan and return a Token.
	/// <pre>
    /// Pre-Condition: current is first character of construct.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
	/// @return next Token
    private LexToken scanToken() {
//		snapShot("SimulaLexer.scanToken: BEGIN");
//		IO.println("\n\nSimulaLexer.scanToken: BEGIN nextPos: " + nextPos + " with value: " + edCurrent());
    	LexToken lexToken = scanBasic();    
//		snapShot("SimulaLexer.scanToken: END");
//		IO.println("SimulaLexer.scanToken: ENDOF nextPos: " + nextPos + " with value: " + edCurrent());
    	
		return lexToken;
    }
    
    //********************************************************************************
    //**	                                                                 scanBasic 
    //********************************************************************************
    /// Scan basic Token
    /// Pre-Condition: current is first character of construct.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// @return next Token
    private LexToken scanBasic() {
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("SimulaScanner.scanBasic, "+edcurrent());
    	while(true)	{
    		LexToken.lineNumberBeforeScanBasic = CoreGlobal.sourceLineNumber;
    		
//    		if(current == EOF_MARK) {
////				LexToken EOFToken = new KeyWordToken(tokenStartLine, sourceText, nextPos, nextPos, KeyWord.EOF, "");
//				LexToken EOFToken = newKeyWordToken(KeyWord.EOF);
//				IO.println("SimulaLexer.scanBasic: EOFToken: " + EOFToken);
//				return EOFToken;
//    		}

    		if(Character.isLetter(getNext())) {
    			return(scanIdentifier());
    		}

    		switch(current) {
    			case EOF_MARK:             return newKeyWordToken(KeyWord.EOF);
    			case '%':                  LexToken dirToken = scanCommentToEndOfLine();
    			               			   Directive.treatDirective(simBuilder, dirToken, dirToken.getText());
    			                           return dirToken;
    		    case '=':
		            if(getNext() == '=')   return(newKeyWordToken(KeyWord.EQR));
		            if(current == '/') {
		            	if(getNext() == '=')   return(newKeyWordToken(KeyWord.NER));
		                else {
			            	String error = "Illegal character combination ="+(char)current;
			            	LexToken lexToken = newKeyWordToken(KeyWord.BAD_CHARACTERS);
			        		Util.syntaxError(simBuilder, lexToken, error);
			            	return lexToken;
		                }
		            }
		            pushBackPos(1);        return newKeyWordToken(KeyWord.EQ);
	            case '>':
		            if(getNext() == '=')   return(newKeyWordToken(KeyWord.GE));
		            pushBackPos(1);        return(newKeyWordToken(KeyWord.GT));
	            case '<':
	                if(getNext() == '=')   return(newKeyWordToken(KeyWord.LE));
		            if(current == '>')     return(newKeyWordToken(KeyWord.NE));
		            pushBackPos(1);        return(newKeyWordToken(KeyWord.LT));
	            case '+':                  return(newKeyWordToken(KeyWord.PLUS));
	            case '-':
	            	if(getNext() == '-')   return(scanCommentToEndOfLine());
	                pushBackPos(1); 	   return(newKeyWordToken(KeyWord.MINUS));
	            case '*':
		            if(getNext() == '*')   return(newKeyWordToken(KeyWord.EXP));
		            pushBackPos(1); 	   return(newKeyWordToken(KeyWord.MUL));
	            case '/':
		            if(getNext() == '/')   return(newKeyWordToken(KeyWord.INTDIV));
		            pushBackPos(1); 	   return(newKeyWordToken(KeyWord.DIV));
	            case '.':
		            if(Character.isDigit(getNext())) { return(scanDotDigit(new StringBuilder())); }
		            pushBackPos(1);        return(newKeyWordToken(KeyWord.DOT));
	            case ',':	               return(newKeyWordToken(KeyWord.COMMA));
	            case ':':
		            if(getNext() == '=')                return(newKeyWordToken(KeyWord.ASSIGNVALUE));
//		            if(current == '-' && pardepth == 0) return(newKeyWordToken(KeyWord.ASSIGNREF));
                    if(current == '-' && !parsingBoundPairList) return newKeyWordToken(KeyWord.ASSIGNREF);
		            pushBackPos(1);                  return(newKeyWordToken(KeyWord.COLON));
	            case ';':	return(newKeyWordToken(KeyWord.SEMICOLON));
	            case '(':	return(newKeyWordToken(KeyWord.BEGPAR));
	            case ')':	return(newKeyWordToken(KeyWord.ENDPAR));
	            case '[':	return(newKeyWordToken(KeyWord.BEGBRACKET));
	            case ']':	return(newKeyWordToken(KeyWord.ENDBRACKET));
	            case '&':
				    if(getNext()=='&' || current=='-' || current=='+' || Character.isDigit(current)) 
				    	return (scanDigitsExp(null));
				    pushBackPos(1); return (newKeyWordToken(KeyWord.AMPERSAND));
	            case '!':  return(scanComment());
	            case '\'': return(scanCharacterConstant());
	            case '\"': return(scanTextConstant());
	            case '0':case '1':case '2':case '3':case '4':
	            case '5':case '6':case '7':case '8':case '9':return(scanNumber());
	            
	            case '\t': return(newTabToken());
		    	  
	            case '\n': return(newNewlineToken());

	            case '\r': if(getNext()=='\n') return (newNewlineToken());
				    pushBackPos(1); // NOTE: No break or return ==> default
	            default: if(Character.isWhitespace(current)) return(scanWhiteSpace());
	        		return newKeyWordToken(KeyWord.BAD_CHARACTERS);
    		}
    	}
    }
  
    //********************************************************************************
    //**	                                                               javaKeyword 
    //********************************************************************************
    /// Scanner Utility: Create a Java-name Token.
    /// @param name the Token's Java-name
    /// @return an identifier Token
    private LexToken javaKeyword(final String name) {
    	return(identifierToken('_'+name));
    }

    
    //********************************************************************************
    //**	                                                            scanWhiteSpace 
    //********************************************************************************
    /// Scan and return a WhiteSpace Token.
    /// <pre>
    /// Pre-Condition: current is first character of construct.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
	private LexToken scanWhiteSpace() {
//		snapShot("SimulaLexer.scanWhiteSpace: BEGIN");
//		IO.println("\n\nSimulaLexer.scanWhiteSpace: BEGIN nextPos: " + nextPos + " with value: " + edCurrent());
    	LOOP:while(true) {
    		getNext();
//    		IO.println("SimulaLexer.scanWhiteSpace: currentColumn: " + currentColumn);
			if(current == '\r' && nextCharIs('\n')) break LOOP;
    		if(current == '\n') break LOOP;
    		if(current == '\t') break LOOP;
    		if(Character.isWhitespace(current)) continue LOOP;
    		break LOOP;
    	}
    	pushBackPos(1);
//		snapShot("SimulaLexer.scanWhiteSpace: END");
//		IO.println("SimulaLexer.scanWhiteSpace: END Current: " + edCurrent());
//    	return(newWhiteSpaceToken());
   		return new WhiteSpaceToken(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, this);
     }

    
    //********************************************************************************
    //**	                                                            scanIdentifier 
    //********************************************************************************
    /// Scan and return an identifier Token.
    /// <pre>
    /// Pre-Condition: current is first character of construct.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
	private LexToken scanIdentifier() {
		String name=scanName();
	    if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanIdentifier: name=\""+name+"\"");
	    String ident=(DocumentManager.CaseSensitive)?name:name.toLowerCase();
	    switch(Character.toLowerCase(ident.charAt(0))) {
	        case 'a':
		        if(ident.equals("abstract"))	 return(javaKeyword(name)); // Java KeyWord
		        if(ident.equals("activate"))     return(newKeyWordToken(KeyWord.ACTIVATE));
		        if(ident.equals("after"))	     return(newKeyWordToken(KeyWord.AFTER));
		        if(ident.equals("and"))			 return(newKeyWordToken(KeyWord.AND));
		        if(ident.equals("and_then"))	 return(newKeyWordToken(KeyWord.AND_THEN));
		        if(ident.equals("array"))	     return(newKeyWordToken(KeyWord.ARRAY));
		        if(ident.equals("assert"))	     return(javaKeyword(name)); // Java KeyWord
		        if(ident.equals("at"))		     return(newKeyWordToken(KeyWord.AT));
		        break;
	        case 'b':
	        	if(ident.equals("before"))       return(newKeyWordToken(KeyWord.BEFORE));
	        	if(ident.equals("begin"))        return(newKeyWordToken(KeyWord.BEGIN));
	        	if(ident.equals("boolean"))      return(newKeyWordToken(KeyWord.BOOLEAN));
	        	if(ident.equals("break"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("byte"))	     return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'c':
	        	if(ident.equals("case"))		 return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("catch"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("char"))  	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("character"))	 return(newKeyWordToken(KeyWord.CHARACTER));
	        	if(ident.equals("class"))        return(newKeyWordToken(KeyWord.CLASS));
	        	if(ident.equals("comment"))      return(scanComment());
	        	if(ident.equals("const"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("continue"))	 return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'd':
	        	if(ident.equals("default"))		 return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("delay"))   	 return(newKeyWordToken(KeyWord.DELAY));
	        	if(ident.equals("do")) 	    	 return(newKeyWordToken(KeyWord.DO));
	        	if(ident.equals("double"))	     return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'e':
	        	if(ident.equals("else"))         return(newKeyWordToken(KeyWord.ELSE));
	        	if(ident.equals("end"))   	     return(scanEndComment());
	        	if(ident.equals("enum"))		 return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("eq"))	         return(newKeyWordToken(KeyWord.EQ));
	        	if(ident.equals("eqv"))	         return(newKeyWordToken(KeyWord.EQV));
	        	if(ident.equals("extends"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("external"))     return(newKeyWordToken(KeyWord.EXTERNAL));
	        	break;
	        case 'f':
	        	if(ident.equals("false"))  	     return(newKeyWordToken(KeyWord.FALSE));
	        	if(ident.equals("final"))  	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("finally"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("float"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("for"))    	     return(newKeyWordToken(KeyWord.FOR));
	        	break;
	        case 'g':
	        	if(ident.equals("ge"))           return(newKeyWordToken(KeyWord.GE));
	        	if(ident.equals("go"))           return(newKeyWordToken(KeyWord.GO));
	        	if(ident.equals("goto"))         return(newKeyWordToken(KeyWord.GOTO));
	        	if(ident.equals("gt"))           return(newKeyWordToken(KeyWord.GT));
	        	break;
	        case 'h':
	        	if(ident.equals("hidden"))       return(newKeyWordToken(KeyWord.HIDDEN));
	        	break;
	        case 'i':
	        	if(ident.equals("if"))	         return(newKeyWordToken(KeyWord.IF));
	        	if(ident.equals("imp"))   	     return(newKeyWordToken(KeyWord.IMP));
	        	if(ident.equals("implements"))   return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("import"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("in"))   	     return(newKeyWordToken(KeyWord.IN));
	        	if(ident.equals("inner"))	     return(newKeyWordToken(KeyWord.INNER));
	        	if(ident.equals("inspect")) 	 return(newKeyWordToken(KeyWord.INSPECT));
	        	if(ident.equals("instanceOf"))   return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("int"))		     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("integer"))	     return(newKeyWordToken(KeyWord.INTEGER));
	        	if(ident.equals("interface"))    return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("is"))           return(newKeyWordToken(KeyWord.IS));
	        	break;
	        case 'l':
	        	if(ident.equals("label"))        return(newKeyWordToken(KeyWord.LABEL));
	        	if(ident.equals("le"))           return(newKeyWordToken(KeyWord.LE));
	        	if(ident.equals("long"))         return(newKeyWordToken(KeyWord.LONG));
	        	if(ident.equals("lt"))           return(newKeyWordToken(KeyWord.LT));
	        	break;
	        case 'n':
	        	if(ident.equals("name"))         return(newKeyWordToken(KeyWord.NAME));
	        	if(ident.equals("native"))       return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("ne"))           return(newKeyWordToken(KeyWord.NE));
	        	if(ident.equals("new"))          return(newKeyWordToken(KeyWord.NEW));
	        	if(ident.equals("none"))         return(newKeyWordToken(KeyWord.NONE));
	        	if(ident.equals("not"))          return(newKeyWordToken(KeyWord.NOT));
	        	if(ident.equals("notext"))       return(newKeyWordToken(KeyWord.NOTEXT));
	        	if(ident.equals("null"))         return(javaKeyword(name)); // Java NullLiteral
	        	break;
	        case 'o':
	        	if(ident.equals("or"))           return(newKeyWordToken(KeyWord.OR));
	        	if(ident.equals("or_else"))      return(newKeyWordToken(KeyWord.OR_ELSE));
	        	if(ident.equals("otherwise"))    return(newKeyWordToken(KeyWord.OTHERWISE));
	        	break;
	        case 'p':
	        	if(ident.equals("package"))      return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("prior"))        return(newKeyWordToken(KeyWord.PRIOR));
	        	if(ident.equals("private"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("procedure"))    return(newKeyWordToken(KeyWord.PROCEDURE));
	        	if(ident.equals("protected"))    return(newKeyWordToken(KeyWord.PROTECTED));
	        	if(ident.equals("public"))	     return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'q':
	        	if(ident.equals("qua"))          return(newKeyWordToken(KeyWord.QUA));
	        	break;
	        case 'r':
	        	if(ident.equals("reactivate"))   return(newKeyWordToken(KeyWord.REACTIVATE));
	        	if(ident.equals("real"))         return(newKeyWordToken(KeyWord.REAL));
	        	if(ident.equals("ref"))          return(newKeyWordToken(KeyWord.REF));
	        	if(ident.equals("return"))	     return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 's':
	        	if(ident.equals("short"))  		 return(newKeyWordToken(KeyWord.SHORT));
	        	if(ident.equals("static"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("step"))   		 return(newKeyWordToken(KeyWord.STEP));
	        	if(ident.equals("strictfp"))	 return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("super"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("switch")) 		 return(newKeyWordToken(KeyWord.SWITCH));
	        	if(ident.equals("synchronized")) return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 't':
	        	if(ident.equals("text"))  	     return(newKeyWordToken(KeyWord.TEXT));
	        	if(ident.equals("then"))  	     return(newKeyWordToken(KeyWord.THEN));
	        	if(ident.equals("this"))   	     return(newKeyWordToken(KeyWord.THIS));
	        	if(ident.equals("throw"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("throws"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("to"))           return(newKeyWordToken(KeyWord.TO));
	        	if(ident.equals("transient"))    return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("true"))   	     return(newKeyWordToken(KeyWord.TRUE));
	        	if(ident.equals("try"))	  	     return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'u':
	        	if(ident.equals("until"))        return(newKeyWordToken(KeyWord.UNTIL));
	        	break;
	        case 'v':
	        	if(ident.equals("value"))        return(newKeyWordToken(KeyWord.VALUE));
	        	if(ident.equals("virtual"))      return(newKeyWordToken(KeyWord.VIRTUAL));
	        	if(ident.equals("void"))	     return(javaKeyword(name)); // Java KeyWord
	        	if(ident.equals("volatile"))     return(javaKeyword(name)); // Java KeyWord
	        	break;
	        case 'w':
	        	if(ident.equals("when"))         return(newKeyWordToken(KeyWord.WHEN));
	        	if(ident.equals("while"))        return(newKeyWordToken(KeyWord.WHILE));
	        	break;
	    }
//	    IO.println("SimulaLexer.scanIdentifier: " + name + " currentColumn=" + currentColumn);
	    return(identifierToken(name));
	}
	
	//********************************************************************************
	//**	                                                                scanNumber 
	//********************************************************************************
	/// Scan a unsigned number.
	/// <pre>
	///  Reference-Syntax:
	///      unsigned-number
	///        = decimal-number  [  exponent-part  ]
	///        | exponent-part
	///      decimal-number
	///        = unsigned-integer  [  decimal-fraction  ]
	///        | decimal-fraction
	///      decimal-fraction
	///        = .  unsigned-integer
	///      exponent-part
	///        =  ( & | && )  [ + | - ]  unsigned-integer
	///      unsigned-integer
	///        =  digit  {  digit  |  _  }
	///        |  radix  R  radix-digit  {  radix-digit  |  _  radix-digit  }
	///      radix
	///        =  2  |  4  |  8  |  16
	///      radix-digit
	///        =  digit  |  A  |  B  |  C  |  D  |  E  |  F
	/// </pre>
	/// <b>End-Condition:</b>
	/// 
	///  - current is last character of construct
	///  - getNext will return first character after construct
	/// 
	/// @return A Token representing a unsigned number.
    private LexToken scanNumber() {
    	int radix=10;
    	char firstChar=(char)current;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanNumber, "+edcurrent());
    	Util.ASSERT(Character.isDigit((char)(current)),"scanNumber:Expecting a Digit");
    	StringBuilder number=new StringBuilder();
	
    	number.append((char)current);
    	if(getNext() == 'R' && (firstChar == '2' | firstChar == '4' | firstChar == '8')) {
    		radix=firstChar - '0';
    		if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanNumber, radix="+radix);
    		number.setLength(0);
    	} else if(firstChar == '1' && current == '6') { 
    		number.append((char)current);
    		if(getNext() == 'R') {
    			radix=16;
    			if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanNumber, radix="+radix);
    			number.setLength(0);
    		} else pushBackPos(1);
    	} else pushBackPos(1);
    
    	while ((radix==16 ? isHexDigit(getNext()) : Character.isDigit(getNext())) || current=='_')
    		if(current!='_') number.append((char)current);
    
    	if(current == '.' && radix == 10) { getNext(); return(scanDotDigit(number)); }
    
    	if(current == '&' && radix == 10) { getNext(); return(scanDigitsExp(number)); }
      
    	String result=number.toString(); number=null;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanNumber, result='"+result+"' radix="+radix);

    	pushBackPos(1);
    	long res = 0;
    	try {
    		res=Integer.parseInt(result,radix);
    	} catch (NumberFormatException e) {
    		LexToken lexToken = newIntegerToken(res);
    		Util.syntaxError(simBuilder, lexToken, "Integer number out of range: "+result);
    		return lexToken;
    	}
    	return(newIntegerToken(res));
    }


    //********************************************************************************
    //**	                                                              scanDotDigit 
    //********************************************************************************
    /// Scan decimal-fraction possibly followed by an exponent-part.
    /// And append it to the given number.
    /// <pre>
    /// Reference-Syntax:
    /// 
    ///      decimal-fraction =  .  unsigned-integer
    ///      
    ///      
    /// End-Condition: current is last character of construct                 
    ///                getNext will return first character after construct
    /// </pre>
    /// @param number The edited number so far
    /// @return next Token
    private LexToken scanDotDigit(StringBuilder number) {
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanDotDigit, "+edcurrent());
    	number.append('.');
    	if(Character.isDigit(current)) number.append((char)current);
    	while(Character.isDigit(getNext()) || current == '_')
    		if(current != '_') number.append((char)current);

    	if(current == '&') { getNext(); return(scanDigitsExp(number)); }
    
    	String result=number.toString(); number=null;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanDotDigit, result='"+result);
    	pushBackPos(1);
    	try {
    		return newRealToken(Float.parseFloat(result));
    	} catch(NumberFormatException e) {
    		LexToken lexToken = newRealToken(0);
    		Util.syntaxError(simBuilder, lexToken, "Illegal number: "+result);
    		return lexToken;
    	}
    }
	
    //********************************************************************************
    //**	                                                             scanDigitsExp 
    //********************************************************************************
    /// Scan exponent-part. And append it to the given number.
    /// <pre>
    /// Reference-Syntax:
    /// 
    ///      exponent-part =  ( & | && )  [ + | - ]  unsigned-integer
    /// </pre>
    /// Pre-Condition: First & is already read
    /// 
    /// End-Condition: current is last character of construct                 
    ///                getNext will return first character after construct
    ///                
    /// @param number The edited number so far
    /// @return next Token
    private LexToken scanDigitsExp(StringBuilder number) {
    	String result;
    	boolean doubleAmpersand=false;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanDigitsExp, "+edcurrent());
    	if(number==null) { number=new StringBuilder(); number.append('1'); }
    	if(current == '&') { getNext(); doubleAmpersand=true; }
    	number.append('e');
    	if(current == '-') { number.append('-'); getNext(); }
    	else if(current == '+') getNext();
    	if(Character.isDigit(current)) number.append((char)current);
    	while(Character.isDigit(getNext()) || current == '_') number.append((char)current);
	      
    	result=number.toString(); number=null;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanDigitsExp, result='"+result);
    	pushBackPos(1);
    	try {
    		if(doubleAmpersand) return newLongRealToken(Double.parseDouble(result));
    		return newRealToken(Float.parseFloat(result));
    	} catch(NumberFormatException e) {
    		LexToken lexToken = newRealToken(0);
    		Util.syntaxError(simBuilder, lexToken, "Illegal number: "+result);
    		return lexToken;
    	}
    }
	

    //********************************************************************************
    //**					                                                  scanName
    //********************************************************************************
    /// Scan identifier or reserved name.
    /// <pre>
    /// Reference-Syntax:
    /// 
    ///    identifier = letter  { letter  |  digit  |  _  }
    ///    
    ///    
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return the resulting identifier
    private String scanName() {
    	StringBuilder name=new StringBuilder();
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanName, "+edcurrent());
    	Util.ASSERT(Character.isLetter((char)(current)),"Expecting a Letter");
    	name.append((char)current);
//    	while ((Character.isLetter(getNext()) || Character.isDigit(current) || current == '_'))
//    		name.append((char)current);
    	LOOP:while(true) {
    		getNext();
//    		IO.println("SimulaLexer.scanName: GOT " + current);
    		if(current == EOF_MARK) break LOOP;
    		if(Character.isLetter(current)) ; // OK
    		else if( Character.isDigit(current)) ; // OK
    		else if( current == '_') ; // OK
    		else break LOOP;
    		name.append((char)current);
    	}
    	if(current != EOF_MARK) pushBackPos(1);
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanName, name="+name+",current="+edcurrent());
    	return(name.toString());
    }
	
    
    //********************************************************************************
    //**	                                                     scanCharacterConstant
    //********************************************************************************
    /// Scan and deliver a Character constant.
    /// <pre>
    ///  Reference-Syntax:   
    ///                                                   
    ///      character-constant  = '  character-designator  '
    ///      
    ///      character-designator
    ///         = iso-code
    ///         |  non-quote-character
    ///         |  "
    ///         
    ///         iso-code =  ! digit  [ digit ]  [ digit ]  !
    ///       
    ///       
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return next Token
    private LexToken scanCharacterConstant() {
    	List<String> errors = null;
    	char result=0;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanCharacterConstant, "+edcurrent());
    	Util.ASSERT((char)(current)=='\'',"Expecting a character quote '");
    	if((isPrintable(getNext())) && current != '!') {
    		result=(char)current; getNext();
    	} else if(current == '!') {
    		result=(char)scanPossibleIsoCode(); getNext();
    	} else {
    		if(errors == null) errors = new ArrayList<>();
    		errors.add("Illegal character constant. "+edcurrent());
    	}
    	
    	if(current != '\'') {
    		if(errors == null) errors = new ArrayList<>();
    		errors.add("Character constant is not terminated. "+edcurrent());
    		pushBackPos(1);
    	}
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("END scanCharacterConstant, result='"+result+"', "+edcurrent());
//    	return(newKeyWordToken(KeyWord.CHARACTERKONST,Character.valueOf(result)));
    	
    	LexToken lexToken = newCharacterToken(result);
    	if(errors != null) for(String error : errors) {
    		Util.syntaxError(simBuilder, lexToken, error);
    	}
    	return lexToken;
    }  
    
    
    //********************************************************************************
    //**	                                                          scanTextConstant
    //********************************************************************************
    /// Scan and deliver a Text constant as a sequence of queued simple strings.
    /// <pre>
    ///  Reference-Syntax:   
    ///                                                   
    ///      string = simple-string  {  string-separator  simple-string  }
    ///      
    ///         simple-string = " { iso-code |  non-quote-character  |  ""  }  "
    ///         
    ///            iso-code = ! digit  [ digit ]  [ digit ]  !
    ///            
    ///         string-separator = token-separator  {  token-separator  }
    ///         
    ///            token-separator
    ///                = a direct comment
    ///                | a space  { except in simple strings and character constants }
    ///                | a format effector  { except as noted for spaces }
    ///                | the separation of consecutive lines
    ///        
    /// 
    /// Pre-Condition: current is first character of construct.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct. I.e. '"' or last string separator
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
	private boolean TRACE_TEXTCONST = false;//true;
    private LexToken scanTextConstant() {
    	if(Option.LEX_VERIFY) {
    		if(current != '"') Util.IERR(""+edCurrent());
    	}
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanTextConstant, "+edcurrent());
    	
    	do { scanSimpleString();
    		 getNext();
    	} while(moreSimpleString());
    	pushBackPos(1);
    	
//		simBuilder.printTokenList("END scanTextConstant");
//		printQueue("SimulaLexer.scanTextConstant: ");
//		Util.STOP();

    	LexToken result=tokenQueue.remove();
    	return(result);
    }
    
    //********************************************************************************
    //**	                                                       moreSimpleString
    //********************************************************************************
    /// Scan string separator, if any. Then test if current is a string quote '"'.
    /// <pre>
    ///  Reference-Syntax:   
    ///                                                   
    ///      string = simple-string  {  string-separator  simple-string  }
    /// 
    ///         string-separator = token-separator  {  token-separator  }
    ///         
    ///            token-separator
    ///                = a direct comment
    ///                | a space  { except in simple strings and character constants }
    ///                | a format effector  { except as noted for spaces }
    ///                | the separation of consecutive lines
    ///        
    /// Pre-Condition: current is first character of String Separator.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is first character after String Separator. I.e. '"' if a Simple String follows
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// @Return true: if a Simple String follows 
    /// </pre>
	private boolean moreSimpleString() {
		boolean TRACE_SKIP_SEP = false;//true;
		if(TRACE_SKIP_SEP) IO.println("\nSimulaLexer.moreSimpleString: BEFORE current: " + edChar((char) current));

		// First: Skip Token separators
		while(currentIsTokenSeparator()) {
			if(Option.LEX_VERIFY) {
				if(current == '\n' || current == ';' || isWhiteSpace(current)) ; // OK
				else Util.IERR("SimulaLexer.moreSimpleString: TokenSeparator End-Condition Failed: current = "+edCurrent());
			}
			getNext();
		}
		if(TRACE_SKIP_SEP) IO.println("\nSimulaLexer.moreSimpleString: AFTER current: " + edChar((char) current));

		pushBackPos(1);
		if(TRACE_SKIP_SEP) IO.println("SimulaLexer.moreSimpleString(2): "+edChar((char) current));
		
		if(nextPos > tokenStartPos) {
			LexToken lexToken = (newKeyWordToken(KeyWord.COMMENT_KEY));
			tokenQueueAdd("scanTextConstant - StringSeparator", lexToken);
		}
		
		getNext();
		if(TRACE_SKIP_SEP) IO.println("SimulaLexer.moreSimpleString: current: " + edChar((char) current) );
		return current == '"';
	}
    
    //********************************************************************************
    //**	                                                          scanSimpleString
    //********************************************************************************
    /// Scan and queue a Simple String as a sequence of queued tokens.
    /// In the normal case, only a single Simple String is queued.
    /// 
    /// However; if the simple string contains NEWLINE characters, a sequence of 
    /// tokens are queued.
    /// <pre>
    ///  Reference-Syntax:   
    ///      
    ///      simple-string = " { iso-code |  non-quote-character  |  ""  }  "
    ///         
    ///         iso-code = ! digit  [ digit ]  [ digit ]  !
    ///            
    /// Pre-Condition: current is first character of construct. I.e. '"'
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.  I.e. '"' or EOF_MARK
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
    private void scanSimpleString() {
    	if(Option.LEX_VERIFY) {
    		if(current != '"') Util.IERR(""+edCurrent());
    	}
    	StringBuilder sb=new StringBuilder();
		// Scan simple-string:
		if(TRACE_TEXTCONST) IO.println("SimulaLexer.scanSimpleString: BEGIN Scan simple-string: ");
		getNext();
		LOOP:while(true) {
//			IO.println("SimulaLexer.scanSimpleString: CHECK line: " + currentLineNumber + ", currrent=" + edChar((char) current));
			switch(current) {
			case '"':
				if(nextCharIs('"')) {
					sb.append('"');
					getNext(); getNext();
					continue LOOP;
				}
				if(nextPos > tokenStartPos) {
					tokenQueueAdd("scanSimpleString - TOKEN-2", newSimpleStringToken(sb.toString()));
				}
				break LOOP;
			case '!':
				int code=scanPossibleIsoCode();
				sb.append((char)code);
				break;
			case '\r':
				IO.println("\nSimulaLexer.scanSimpleString: GOT NEWLINE(CRLF) length: " + (nextPos - tokenStartPos));
				if(! nextCharIs('\n')) Util.IERR("");
				pushBackPos(1);
				if(TRACE_TEXTCONST) IO.println("\nSimulaLexer.scanSimpleString: GOT NEWLINE(CRLF) length: " + (nextPos - tokenStartPos));
				if(nextPos > tokenStartPos) {
					LexToken lexToken = newSimpleStringToken(sb.toString());
					Util.warning(simBuilder, lexToken, "Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6");
		    	    tokenQueueAdd("scanSimpleString - CRLF", lexToken);
				}
	    		
				getNext(); getNext(); // Consume CRLF
        	    tokenQueueAdd("scanSimpleString - CRLF", newNewlineToken());
				sb = new StringBuilder();
//				getNext();
				break;
			case '\n':
				pushBackPos(1);
				if(TRACE_TEXTCONST) IO.println("\nSimulaLexer.scanSimpleString: GOT NEWLINE(LF) length: " + (nextPos - tokenStartPos));
				if(nextPos > tokenStartPos) {
					LexToken lexToken = newSimpleStringToken(sb.toString());
					Util.warning(simBuilder, lexToken, "Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6");
		    	    tokenQueueAdd("scanSimpleString - LF", lexToken);
				}
	    		
				getNext();
        	    tokenQueueAdd("scanSimpleString - CRLF", newNewlineToken());
				sb = new StringBuilder();
				break;
			case EOF_MARK:
				if(TRACE_TEXTCONST) IO.println("\nSimulaLexer.scanSimpleString: GOT EOF_MARK length: " + (nextPos - tokenStartPos));
				if(nextPos > tokenStartPos) {
					LexToken lexToken = newSimpleStringToken(sb.toString());
					Util.warning(simBuilder, lexToken, "Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6");
		    	    tokenQueueAdd("scanSimpleString - EOF_MARK", lexToken);
				}
				tokenQueueAdd("scanSimpleString - EOF-TOKEN", newKeyWordToken(KeyWord.EOF));
				
				break LOOP;
				
			default: sb.append((char)current);
			}
			getNext();
		}
		if(TRACE_TEXTCONST) IO.println("\nSimulaLexer.scanSimpleString: ENDOF Scan simple-string: " + (nextPos - tokenStartPos));

//       	simBuilder.printTokenList("END scanSimpleString");
//        printQueue("SimulaLexer.scanSimpleString: ");

    	if(Option.LEX_VERIFY) {
            /// End-Condition: current is last character of construct.  I.e. '"' or EOF_MARK
    		if(current == '"') ; // OK
    		else if(current == EOF_MARK) ; // OK
    		else Util.IERR("SimulaLexer.scanSimpleString: End-Condition Failed: current = "+edCurrent());
    	}
    }

    //********************************************************************************
  	//**	                                                  currentIsStringSeparator
    //********************************************************************************
    /// Scanner Utility: Check if current is a string separator.
    /// <pre>
    ///  Reference-Syntax:
    ///  
    ///      string-separator = token-separator  {  token-separator  }
    ///      
    ///         token-separator
    ///            = a direct comment
    ///            | a space  { except in simple strings and character constants }
    ///            | a format effector  { except as noted for spaces }
    ///            | the separation of consecutive lines
    ///        
    /// Pre-Condition: current is first character of construct.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.
    ///                I.e: LF, ';' or a whitespace 
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return true if current is a string separator
    private boolean currentIsTokenSeparator() {
    	boolean TRACE_TOKEN_SEP = false; // true;
		if(TRACE_TOKEN_SEP) IO.println("\nSimulaLexer.currentIsTokenSeparator: BEGIN current: " + edChar((char) current) );

		if(current == '\r' && nextCharIs('\n')) {
    		if((nextPos-1) > tokenStartPos) {
	    		pushBackPos(1);
	    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", newKeyWordToken(KeyWord.COMMENT_KEY));
//	    		pushBackPos(-1);
	    	    getNext();
    		}
    		getNext();
    		    	    
			if(TRACE_TOKEN_SEP) IO.println("SimulaLexer.currentIsTokenSeparator: NEW NEWLINE");
    	    tokenQueueAdd("currentIsTokenSeparator - NEWLINE", newNewlineToken());
    	    if(Option.LEX_VERIFY) {
    	    	if(current != '\n')
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: current = "+edCurrent());
    	    }
    	    return true;    
		}
		
    	if(current == '\n') {
    		IO.println("SimulaLexer.currentIsTokenSeparator: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
    		if((nextPos-1) > tokenStartPos) {
	    		pushBackPos(1);
	    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", newKeyWordToken(KeyWord.COMMENT_KEY));
//	    		pushBackPos(-1);
	    	    getNext();
    		}    		    	    
			if(TRACE_TOKEN_SEP) IO.println("SimulaLexer.currentIsTokenSeparator: NEW NEWLINE");
    	    tokenQueueAdd("currentIsTokenSeparator - NEWLINE", newNewlineToken());
    	    if(Option.LEX_VERIFY) {
    	    	if(current != '\n')
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: current = "+edCurrent());
    	    }
    	    return true;    
    	}
    	
    	if(current == '!') {
			if(TRACE_TOKEN_SEP) IO.println("SimulaLexer.currentIsTokenSeparator: NEW COMMENT-1");
//			Util.IERR("SJEKK DETTE");
    	    tokenQueueAddCommentTokens();
    	    if(Option.LEX_VERIFY) {
    	    	if(current != ';')
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: current = "+edCurrent());
    	    }
    		return true;	
    	}
    	
    	if(current == '-' && nextCharIs('-')) {
 			if(TRACE_TOKEN_SEP) IO.println("SimulaLexer.currentIsTokenSeparator: currentColumn="+currentColumn);
//			Util.IERR("SJEKK DETTE");
    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", scanCommentToEndOfLine());
//			this.snapShot("SimulaLexer.currentIsTokenSeparator: ");
    	    if(Option.LEX_VERIFY) {
    	    	if(! (nextCharIs('\r') || nextCharIs('\n')))
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: current = "+edCurrent());
    	    }
			// Consume CRLF or LF
    	    if(nextCharIs('\r')) getNext();
			getNext();
    	    tokenQueueAdd("currentIsTokenSeparator - NEWLINE", newNewlineToken());
			
    		return true;	
    	}
    	
    	if(current == '%' && currentColumn == 0) {
 			if(TRACE_TOKEN_SEP) IO.println("SimulaLexer.currentIsTokenSeparator: currentColumn="+currentColumn);
//			Util.IERR("SJEKK DETTE");
			LexToken lexToken = scanCommentToEndOfLine();
			Directive.treatDirective(simBuilder, lexToken, lexToken.getText());
    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", lexToken);
    	    if(Option.LEX_VERIFY) {
    	    	if(! (nextCharIs('\r') || nextCharIs('\n')))
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: current = "+edCurrent());
    	    }
			// Consume CRLF or LF
    	    if(nextCharIs('\r')) getNext();
			getNext();
    	    tokenQueueAdd("currentIsTokenSeparator - NEWLINE", newNewlineToken());

    	    return true;	
    	}
    	
    	if(Character.isLetter((char)current)) {
//        	IO.println("SimulaLexer.currentIsTokenSeparator: Current isLetter:" + (char)current);
    		if((nextPos-1) > tokenStartPos) {
	    		pushBackPos(1);
	    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", newKeyWordToken(KeyWord.COMMENT_KEY));
//	    		pushBackPos(-1);
	    	    getNext();
    		}    		    	    
    		String name=scanName();
    		if(name.equalsIgnoreCase("COMMENT")) {
    			if(TRACE_TOKEN_SEP) IO.println("SimulaLexer.currentIsTokenSeparator: NEW COMMENT-2");
        	    tokenQueueAddCommentTokens();
        	    if(Option.LEX_VERIFY) {
        	    	if(current != ';')
    		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: nextPos="+nextPos+", current = "+edCurrent());
        	    }
    			return true;
    		} else {
    			pushBackPos(name.length() - 1);
    		}
    		return false;
		}
    	
    	boolean res = isWhiteSpace(current);
//    	IO.println("SimulaLexer.currentIsTokenSeparator: isWhiteSpace("+current+")=" + isWhiteSpace(current));
    	return res;
    }
  

    //********************************************************************************
    //**	                                                       scanPossibleIsoCode
    //********************************************************************************
    /// Scanner Utility: Scan possible iso-code.
    /// <pre>
    ///  Reference-Syntax:
    ///  
    ///      iso-code =  ! digit  [ digit ]  [ digit ]  !
    ///       
    /// 
    /// Pre-Condition: The leading character ! is already read
    /// 
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return the resulting iso-code
    private int scanPossibleIsoCode() {
		char firstchar, secondchar, thirdchar;
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("scanPossibleIsoCode, " + edcurrent());
		Util.ASSERT((char) (current) == '!', "Expecting a character !");
		if (Character.isDigit(getNext())) {
			firstchar = (char) current;
			if (Character.isDigit(getNext())) {
				secondchar = (char) current;
				if (Character.isDigit(getNext())) {
					thirdchar = (char) current;
					if (getNext() == '!') { // ! digit digit digit ! Found
						int value = (((firstchar - '0') * 10 + secondchar - '0') * 10 + thirdchar - '0');
						if (Option.internal.TRACE_LEXER > 0)
							Util.TRACE("scanPossibleIsoCode:Got three digits: "+(char)firstchar+(char)secondchar+(char)thirdchar+"value="+value);
						if (value < 256)
							return (value);
						Util.warning(simBuilder, "ISO-Code " + value + " is out of range (0:255)"
							+" interpreted as an ordinary sequence of characters: !" +value + "!  See Simula Standard 1.6");
						pushBackPos(4);
						return ('!');
					} else {
						pushBackPos(4);
						return ('!');
					}
				} else if (current == '!') { // ! digit digit ! Found
					return ((char) ((firstchar - '0') * 10 + secondchar - '0'));
				} else {
					pushBackPos(3);
					return ('!');
				}
			} else if (current == '!') { // ! digit ! Found
				return ((char) (firstchar - '0'));
			} else {
				pushBackPos(2);
				return ('!');
			}
		} else {
			pushBackPos(1);
			return ('!');
		}
	}
  
	// ********************************************************************************
	// ** scanComment
	// ********************************************************************************
	/// Scan a Comment. Multiple tokens may be queued
	/// <pre>
	/// Reference-Syntax:
	/// 
	///       comment = COMMENT { any character except semicolon } ;
	///               | ! { any character except semicolon } ;
	///       
	///       
    /// Pre-Condition: current is first character of construct.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
	/// </pre>
	/// @return a Comment Token
    private static boolean TRACE_SCAN_COMMENT = false;//true;
    private LexToken scanComment() {
		tokenQueueAddCommentTokens();
	    LexToken lexToken=tokenQueue.remove();
		return lexToken;
    }
    private void tokenQueueAddCommentTokens() {
//    	this.snapShot("BEGIN scanComment");	 
    	
    	LexToken commentToken = newKeyWordToken(KeyWord.COMMENT_KEY);
    	tokenQueueAdd("scanComment-START", commentToken);

//    	if (CoreGlobal.TRACE_LEXER) Util.TRACE("scanComment, " + edcurrent());
    	int nPhrase = 0; // Number of comment phrases

    	LOOP:while (true) {
    		if(current == EOF_MARK) {
//    			IO.println("\n\n\n\nLexToken.scanComment: BEGIN TREAT EOF_MARK: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
//    			IO.println("LexToken.scanComment: AT EOF_MARK: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
    			int lng = nextPos - tokenStartPos - 1;
//    			IO.println("LexToken.scanComment: AT EOF_MARK: lng="+lng);
    			if(lng > 0) {
    				nPhrase++;
    				if(nextPos != textEndOffset) Util.IERR("IMPOSSIBLE");
    				LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
    				if(nPhrase > 1) Util.warning(simBuilder, lexToken, "Comment spans multiple lines");
    				tokenQueueAdd("scanComment-EOF_MARK", lexToken);
    			}
    			break LOOP;
    		}

    		getNext();
    		if(TRACE_SCAN_COMMENT) IO.println("LexToken.scanComment: current="+current+":'"+Comn.printable(""+(char)current)+"'");

    		if(current == '\r' && nextCharIs('\n')) {
    			if(TRACE_SCAN_COMMENT) IO.println("LexToken.scanComment: GOT CRLF");
//    			IO.println("\n\n\n\nLexToken.scanComment: BEGIN TREAT NEWLINE: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);

    			boolean TESTING = true;
    			if(TESTING) {
                    int lng = nextPos - tokenStartPos - 1;
//    	        	IO.println("LexToken.scanComment: lng="+lng);
                    if(lng > 0) {
                    	nPhrase++;
                        pushBackPos(1);
                        if(Option.LEX_VERIFY) {
    	                    if(lng != (nextPos - tokenStartPos)) Util.IERR("IMPOSSIBLE: lng=" + lng +", nextPos - tokenStartPos: " + (nextPos - tokenStartPos));
    	                    if(sourceText.charAt(nextPos) != '\r') Util.IERR("IMPOSSIBLE");
                        }
                        LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
                        if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                        tokenQueueAdd("scanComment-COMMENT", lexToken);
                        getNext(); // Reads the first character after the comment. I.e. CR character.
                        if(Option.LEX_VERIFY) {
    	                    if(current != '\r') Util.IERR("IMPOSSIBLE");
    	                    if(sourceText.charAt(tokenStartPos) != '\r') Util.IERR("IMPOSSIBLE");
                        }
                    }
                    if(Option.LEX_VERIFY) {
                    	if(current != '\r') Util.IERR("IMPOSSIBLE: " + current);
                    }
                    getNext(); // current = LF
            	    tokenQueueAdd("scanComment - NEWLINE", newNewlineToken());
    			} else {
	    			int lng = nextPos - tokenStartPos - 1;
	    			if(lng > 0) {
	    				nPhrase++;
	    				pushBackPos(1);
	    				if(Option.LEX_VERIFY) {
	    					if(lng != (nextPos - tokenStartPos)) Util.IERR("IMPOSSIBLE: lng=" + lng +", nextPos - tokenStartPos: " + (nextPos - tokenStartPos));
	    					if(sourceText.charAt(nextPos) != '\r') Util.IERR("IMPOSSIBLE: " + edCurrent());
	    				}
	    				LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
	    				if(nPhrase > 1) Util.warning(simBuilder, lexToken, "Comment spans multiple lines");
	    				tokenQueueAdd("scanComment-NEWLINE", lexToken);
	
	    				getNext(); getNext(); // Skip NEWLINE(CRLF)
	    				if(Option.LEX_VERIFY) {
	    					if(current != '\n') Util.IERR("IMPOSSIBLE");
	    					if(sourceText.charAt(tokenStartPos) != '\r') Util.IERR("IMPOSSIBLE: ");
	    				}
	    			}
	    			if(current != '\n') Util.IERR("IMPOSSIBLE");
	    			tokenQueueAdd("scanComment - CRLF", newNewlineToken());
	
	    			this.snapShot("SimulaLexer.scanTextConstant: ");
    			}
    		}
    		else if (current == '\n') {
    			if(TRACE_SCAN_COMMENT) IO.println("LexToken.scanComment: GOT NEWLINE");
//    			IO.println("\n\n\n\nLexToken.scanComment: BEGIN TREAT NEWLINE: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);

    			int lng = nextPos - tokenStartPos - 1;
    			if(lng > 0) {
    				nPhrase++;
    				pushBackPos(1);
    				if(Option.LEX_VERIFY) {
    					if(lng != (nextPos - tokenStartPos)) Util.IERR("IMPOSSIBLE: lng=" + lng +", nextPos - tokenStartPos: " + (nextPos - tokenStartPos));
    					if(sourceText.charAt(nextPos) != '\n') Util.IERR("IMPOSSIBLE");
    				}

    				LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
    				if(nPhrase > 1) Util.warning(simBuilder, lexToken, "Comment spans multiple lines");
    				tokenQueueAdd("scanComment-NEWLINE", lexToken);

    				getNext(); // Skip NEWLINE(LF)
    				if(Option.LEX_VERIFY) {
    					if(current != '\n') Util.IERR("IMPOSSIBLE");
    					if(sourceText.charAt(tokenStartPos) != '\n') Util.IERR("IMPOSSIBLE");
    				}
    			}
    			if(current != '\n') Util.IERR("IMPOSSIBLE");
    			tokenQueueAdd("scanComment - NEWLINE", newNewlineToken());
    		} else if (current == ';') {
//    			IO.println("\n\n\n\nLexToken.scanComment: BEGIN TREAT SEMICOLON: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
//    			IO.println("LexToken.scanComment: AT SEMICOLON: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
    			int lng = nextPos - tokenStartPos;
//    			IO.println("LexToken.scanComment: AT SEMICOLON: lng="+lng);
				LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
				if(nPhrase > 1) Util.warning(simBuilder, lexToken, "Comment spans multiple lines");
				tokenQueueAdd("scanComment-SEMICOLON", lexToken);
    			break LOOP;
    		} else {
    			if(TRACE_SCAN_COMMENT) IO.println("LexToken.scanComment: GOT OTHER="+current+":'"+Comn.printable(""+(char)current)+"'");
    		}
    	}

    	if(TRACE_SCAN_COMMENT) {
    		IO.println("SimulaLexer.scanComment: commentToken: " + commentToken);
    		IO.println("SimulaLexer.scanComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
    		IO.println("SimulaLexer.scanComment: COMMENT TOKEN: " + commentToken);
    		printQueue("SimulaLexer.scanComment: ");
    		IO.println("SimulaLexer.scanComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
    	}
    }

	  
	// ********************************************************************************
	// ** scanCommentToEndOfLine
	// ********************************************************************************
	/// Scan Comment to end-of-line.
	/// <pre>
	/// Reference-Syntax:
	/// 
	///       comment = -- { any character until end-of-line }
	///       
    /// Pre-Condition: current is first character of construct. I.e. '%' or "--"
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
	/// </pre>
	/// @return a Comment Token
	private static boolean TESTING_SCAN_END_LINE = false;//true;
	private LexToken scanCommentToEndOfLine() {
        while (true) {
        	getNext();
        	if(current == EOF_MARK) {
        		if(TESTING_SCAN_END_LINE) IO.println("LexToken.scanCommentToEndOfLine: GOT EOF_MARK");
        		if(Option.LEX_VERIFY) {
        			if(nextPos == tokenStartPos) Util.IERR("IMPOSSIBLE");
        		}
        		return newKeyWordToken(KeyWord.COMMENT_TEXT);
        	}
        	if(current == '\n' || (current == '\r' && nextCharIs('\n'))) {
        		if(TESTING_SCAN_END_LINE) IO.println("LexToken.scanCommentToEndOfLine: GOT NEWLINE(LF or CRLF)");
        		pushBackPos(1);
        		if(Option.LEX_VERIFY) {
        			if(nextPos == tokenStartPos) Util.IERR("IMPOSSIBLE");
        		}
        		return newKeyWordToken(KeyWord.COMMENT_TEXT);
        	}
        }
	}

	// ********************************************************************************
    // ** scanEndComment
    // ********************************************************************************
    /// Scan end-comment.
    /// <pre>
    /// reference-Syntax:
    ///
    ///       The sequence:
    ///
    ///          END { any sequence of printable characters not containing END, ELSE, WHEN, OTHERWISE, EOF_MARK or ; }
    ///
    ///       is equivalent to:
    ///
    ///          END
    ///
    ///
    /// Pre-Condition: current is first character of construct.
    ///                nextPos points to second character of construct.
    /// End-Condition: current is last character of construct.
    ///                nextPos points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
	private static boolean TESTING_SCAN_END = false;
    private LexToken scanEndComment() {
        LexToken endToken = newKeyWordToken(KeyWord.END);
        if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: endToken="+endToken);
		currentColumn = currentColumn + endToken.length;
		if(TRACE_CURRENT_COLUMN) IO.println("SimulaLexer.scanEndComment(1): currentColumn="+currentColumn);
    	tokenStartPos = nextPos;
        
//        if (CoreGlobal.TRACE_LEXER) Util.TRACE("scanEndComment, " + edcurrent());
        int nPhrase = 0; // Number of comment phrases
        
        LOOP:while (true) {
        	if(current == EOF_MARK) {
        		if(TESTING_SCAN_END) IO.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT EOF_MARK: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
                int lng = nextPos - tokenStartPos - 1;
                if(lng > 0) {
                	nPhrase++;
                    if(nextPos != textEndOffset) Util.IERR("IMPOSSIBLE");
                    LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
                    if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                    tokenQueueAdd("scanEndComment-EOF_TEXT", lexToken);
                }
                tokenQueueAdd("scanEndComment-EOF_TEXT", newKeyWordToken(KeyWord.EOF));
        	    currentColumn = 0;
        	    if(TRACE_CURRENT_COLUMN) IO.println("SimulaLexer.scanEndComment(2): currentColumn="+currentColumn);
        		break LOOP;
        	}
        	
        	getNext();
        	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: current="+current+":'"+Comn.printable(""+(char)current)+"'");
    		
    		if(current == '\r' && nextCharIs('\n')) {
            	if(TESTING_SCAN_END) IO.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT NEWLINE(CRLF): nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
                
                int lng = nextPos - tokenStartPos - 1;
//	        	IO.println("LexToken.scanEndComment: lng="+lng);
                if(lng > 0) {
                	nPhrase++;
                    pushBackPos(1);
                    if(Option.LEX_VERIFY) {
	                    if(lng != (nextPos - tokenStartPos)) Util.IERR("IMPOSSIBLE: lng=" + lng +", nextPos - tokenStartPos: " + (nextPos - tokenStartPos));
	                    if(sourceText.charAt(nextPos) != '\r') Util.IERR("IMPOSSIBLE");
                    }
                    LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
                    if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                    tokenQueueAdd("scanEndComment-COMMENT", lexToken);
                    getNext(); // Reads the first character after the comment. I.e. CR character.
                    if(Option.LEX_VERIFY) {
	                    if(current != '\r') Util.IERR("IMPOSSIBLE");
	                    if(sourceText.charAt(tokenStartPos) != '\r') Util.IERR("IMPOSSIBLE");
                    }
                }
                if(Option.LEX_VERIFY) {
                	if(current != '\r') Util.IERR("IMPOSSIBLE: " + current);
                }
                getNext(); // current = LF
        	    tokenQueueAdd("scanEndComment - NEWLINE", newNewlineToken());
    		} else if (current == '\n') {
            	if(TESTING_SCAN_END) IO.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT NEWLINE(LF): nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
                
                int lng = nextPos - tokenStartPos - 1;
                if(lng > 0) {
                	nPhrase++;
                    pushBackPos(1);
                    if(Option.LEX_VERIFY) {
	                    if(lng != (nextPos - tokenStartPos)) Util.IERR("IMPOSSIBLE: lng=" + lng +", nextPos - tokenStartPos: " + (nextPos - tokenStartPos));
	                    if(sourceText.charAt(nextPos) != '\n') Util.IERR("IMPOSSIBLE");
                    }
                    LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
                    if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                    tokenQueueAdd("scanEndComment-NEWLINE", lexToken);
        			
                    getNext(); // Reads the first character after the comment. I.e. CR character.
                    if(Option.LEX_VERIFY) {
	                    if(current != '\n') Util.IERR("IMPOSSIBLE");
	                    if(sourceText.charAt(tokenStartPos) != '\n') Util.IERR("IMPOSSIBLE");
                    }
                }
                if(current != '\n') Util.IERR("IMPOSSIBLE");
        	    tokenQueueAdd("scanEndComment - NEWLINE", newNewlineToken());
            } else if (current == ';') {
            	if(TESTING_SCAN_END) IO.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT SEMICOLON: nextPos="+nextPos+", tokenStartPos="+tokenStartPos);
                int lng = nextPos - tokenStartPos - 1;
                if(lng > 0) {
                	nPhrase++;
                    pushBackPos(1);
                    if(sourceText.charAt(nextPos) != ';') Util.IERR("IMPOSSIBLE");
                    
                    LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
                    if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                    tokenQueueAdd("scanEndComment-SEMICOLON", lexToken);
        			
                    getNext(); // Leser første tegn etter comment, altså et SEMICOLON tegn
                    if(Option.LEX_VERIFY) {
	                    if(current != ';') Util.IERR("IMPOSSIBLE");
	                    if(sourceText.charAt(tokenStartPos) != ';') Util.IERR("IMPOSSIBLE");
                    }
                }
                tokenQueueAdd("scanEndComment-SEMICOLON", newKeyWordToken(KeyWord.SEMICOLON));
                break LOOP;
            } else if (Character.isLetter(current)) {
                String name = scanName();
                if(TESTING_SCAN_END) IO.println("\n\nLexToken.scanEndComment: GOT name="+name);
                if (Util.equals(name, "end") || Util.equals(name, "else")
                        || Util.equals(name, "when") || Util.equals(name, "otherwise")) {
                	nextPos = nextPos - name.length();
                	EOF_SEEN=false;
//                	current = 0;
                	
                    if(nextPos > tokenStartPos) {
                        LexToken lexToken = newKeyWordToken(KeyWord.COMMENT_TEXT);
                        if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                        tokenQueueAdd("scanEndComment-NAME", lexToken);
                    }
//                    this.snapShot("GOT name="+name);
//                    IO.println("LexToken.scanEndComment: GOT name="+name+" break LOOP\n\n");
                    break LOOP;
                }
            } else {
            	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT OTHER="+current+":'"+Comn.printable(""+(char)current)+"'");
            }
        }

        if(TESTING_SCAN_END) {
	        IO.println("SimulaLexer.scanEndComment: endToken: " + endToken);
	        IO.println("SimulaLexer.scanEndComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
	        IO.println("SimulaLexer.scanEndComment: END TOKEN: " + endToken);
	        printQueue("SimulaLexer.scanEndComment: ");
	        IO.println("SimulaLexer.scanEndComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
        }
        
        return endToken;
    }
    
	private void tokenQueueAdd(String debugName, LexToken lexToken) {
//		if(lexToken.length == 0) return;
//		IO.println("SimulaLexer.tokenQueueAdd: "+debugName+" "+lexToken);
	    tokenQueue.add(lexToken);
//		IO.println("SimulaLexer.tokenQueueAdd: "+debugName+" currentColumn = "+currentColumn+" + "+lexToken.length + " = "+(currentColumn + lexToken.length));
	    
	    if(lexToken.keyWord != KeyWord.NEWLINE)
	    	currentColumn = currentColumn + lexToken.length;
		
		if(TRACE_CURRENT_COLUMN) IO.println("SimulaLexer.tokenQueueAdd: currentColumn="+currentColumn);
    	tokenStartPos = nextPos;
	    currentLexerToken = lexToken;
	}
    
    private void printQueue(String title) {
    	IO.println("================================= BEGIN TOKEN-QUEUE " +title + " =================================");
    	for(LexToken lexToken:tokenQueue) {
        	IO.println("SimulaLexer.printQueue: lexToken="+lexToken);
    	}
    	IO.println("================================= ENDOF TOKEN-QUEUE " +title + " =================================");
    }

        

    //********************************************************************************
    //**	                                                                 UTILITIES 
    //********************************************************************************
	
//	/// The previous character read.
//    private int prevChar;
	
	/// The current character read.
    private int current;
    
    /// Returns next input character.
    /// nextPos is incremented to point to the next character
    /// @return next input character
    private int getNext() {
    	if(nextPos >= textEndOffset) {
    		if(EOF_SEEN) {
    			Util.syntaxError(simBuilder, prevLexerToken, "Attempt to scan beyond EOF");
//    			throw new EOFException("");
    			current = EOF_MARK;
    			throw new EOTException("Attempt to scan beyond EOF");
    		} else {
    			EOF_SEEN = true; current = EOF_MARK;
//    	    	IO.println("SimulaLexer.getNext: EOF_MARK: " + current);
//    	    	Thread.dumpStack();
    		}
    	} else {
    		current = sourceText.charAt(nextPos++);
    	}
//    	IO.println("SimulaLexer.getNext: " + current);
//		IO.println("SimulaLexer.getNext(nextPos: " + (nextPos - 1)
//				+ ") ==> nextPos: " + nextPos + " current: " + edChar((char) current)
//				+"  CALLED FROM: " + Util.calledFrom(3, 25));
    	return(current);
    }
    
    private boolean nextCharIs(int c) {
    	int next = 0;
    	if(nextPos >= textEndOffset) {
    		next = EOF_MARK;
     	} else {
    		next = sourceText.charAt(nextPos);
    	}
//		IO.println("SimulaLexer.nextCharIs("+c+") ==> current: " + edChar((char) current) + ", next: " + edChar((char) next) + " ==> " + (next == c));
//    	IO.println("SimulaLexer.getNext: " + current);
//		IO.println("SimulaLexer.getNext(nextPos: " + (nextPos - 1)
//				+ ") ==> nextPos: " + nextPos + " current: " + edChar((char) current)
//				+"  CALLED FROM: " + Util.calledFrom(3, 25));
    	return(next == c);
    	
    }

	private void pushBackPos(int count) {
		nextPos = nextPos - count;
		current = sourceText.charAt(nextPos - 1);
//		IO.println("SimulaLexer.pushBackPos("+ count + "): ==> nextPos=" + nextPos + ", current=" + edCurrent());
	}  

    /// Create a new keyWord Token
    /// @param keyWord the KeyWord
    /// @return the newly created Token
	private LexToken newKeyWordToken(final int keyWord) {
		return new KeyWordToken(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, keyWord, this);
	}
//	private LexToken newKeyWordToken(final int tokenStartPos, final int length, final int keyWord) {
//		return new KeyWordToken(currentLineNumber, sourceText, currentColumn, length, keyWord, this);
//	}
//	
//	/// SKAL FJERNES
//	private LexToken newKeyWordToken(final int tokenStartPos, final int keyWord) {
//		return new KeyWordToken(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, keyWord, this);
//	}

    /// Create a new keyWord Token
    /// @param keyWord the KeyWord
    /// @return the newly created Token
	private LexToken newNewlineToken() {
		LexToken newlineToken = newKeyWordToken(KeyWord.NEWLINE);
    	currentLineNumber++;
    	currentColumn = 0;
    	if(TRACE_CURRENT_COLUMN) IO.println("SimulaLexer.newNewlineToken: currentColumn="+currentColumn);
       	update_LineStartPos_List();
       	if(Option.LEX_VERIFY) {
       		String text = newlineToken.getText();
       		int lng = text.length();
       		boolean ok;
       		switch(lng){
	       		case 1: ok = text.equals("\n"); break;
	       		case 2: ok = text.equals("\r\n"); break;
	       		default: ok = false;
       		}
       		if(! ok) {
       			Util.IERR("SimulaLexer.newNewlineToken: LEX_VERIFY Failed: Illegal content: " + Comn.printable(text));
       		}
       	}
        return newlineToken;
    }
	  
    /// Create a new Tab \t Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newTabToken() {
		return new TabToken(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, this);
	}
	  
    /// Create a new Integer Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newIntegerToken(final long value) {
		return new IntegerConst(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, value, this);
	}
	  
    /// Create a new Character Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newCharacterToken(final char value) {
		return new CharacterConst(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, value, this);
	}
	  
    /// Create a new Simple String Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newSimpleStringToken(final String value) {
		return new SimpleString(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, value, this);
	}

    /// Create a new Real Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newRealToken(final float value) {
		return new RealConst(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, value, this);
	}

    /// Create a new Long Real Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newLongRealToken(final double value) {
		return new LongRealConst(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, value, this);
	}
	
    /// Only when Option LEX_VERIFY = true
    public void verifyToken(LexToken lexToken, int lineNumber, int column, int length) {
		int line = getLineStartPos(lineNumber);
		int check = line + column + length;
		if(length == 0) {
			Util.IERR("LEX_VERIFY FAILED: Token length is Zero: " + lexToken);
//			System.err.println("LEX_VERIFY FAILED: Token length is Zero: " + lexToken);
		} else
			if(check > textEndOffset || length == 0) {
				System.err.println("LEX_VERIFY FAILED: " + lexToken);
			System.err.println("LEX_VERIFY FAILED: lineStartPos("+lineNumber+")=" + line + ", column=" + column + ", length=" + length
					+ "  SUM=" + check + " > lexer.textEndOffset=" + textEndOffset
					+ "\n" + " ".repeat(33) + "Remaining SourceText("+ line +", ...)=\"" + sourceText.subSequence(line, textEndOffset) + '"');
			Util.IERR("LEX_VERIFY FAILED: ");
		}
    }
    
    //********************************************************************************
    //**	                                                           identifierToken 
    //********************************************************************************
    /// Create a new identifier Token.
    /// @param ident the Token's identifier
    /// @return an identifier Token
    private LexToken identifierToken(final String ident) {
    	return new Identifier(currentLineNumber, sourceText, currentColumn, nextPos - tokenStartPos, this);
    }

	/// Utility: Edit current character.
	/// @return edited current character
	private String edcurrent() {
		if (current < 32)
			return ("Current code=" + current);
		return ("Current='" + (char) current + "' value=" + current);
	}
	
	/// Utility: Check if a character is a hex digit.
	/// @param c the character
	/// @return true if character c is a hex digit
    private boolean isHexDigit(final int c) {
	    switch(c) {
	        case '0':case '1':case '2':case '3':case '4':
	        case '5':case '6':case '7':case '8':case '9':
	        case 'A':case 'B':case 'C':case 'D':case 'E':case 'F':
	        case 'a':case 'b':case 'c':case 'd':case 'e':case 'f': return(true);
	        default: return(false);
	    }
    }
	
	/// Utility: Check if a character is printable.
	/// @param c the character
	/// @return true if character c is printable
	private boolean isPrintable(final int c) {
		if (c < 32) return (false);
		if (c > 126) return (false);
		return (true);
	}

	/// Utility: Check if a character is a whiteSpace.
	/// @param c the character
	/// @return true if character c is a whiteSpace
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


	public LexToken getEOFToken() {
		// TODO Auto-generated method stub
		Util.IERR("MÅ SKRIVES");
		return null;
	}

    /// Debug utility
    public String edChar(char c) {
    	String curval = "" + (int)c + ':' + c;
    	curval = curval.replace("\t", "\\t").replace("\r", "\\r").replace("\n", "\\n").replace(" ", "_");
    	return curval;
    }

    /// Debug utility
    public String edCurrent() {
    	return edChar((char) current);
    }

    /// Debug utility
    public String edNext() {
   		if(nextPos >= textEndOffset) return "EOF_MARK";
    	char curChar = sourceText.charAt(nextPos);
    	return edChar(curChar);
    }

    /// Debug utility
    public void snapShot(String title) {
    	int beg = Math.max(0, nextPos - 50); beg = beg - beg%10;
    	int end = Math.min(beg + 100, textEndOffset);
    	CharSequence text = sourceText.subSequence(beg, end);
    	IO.println("SimulaLexer.snapShot: beg: " + beg + ", end: " + end);
    	IO.println("############################### LEXER SNAPSHOT["+beg+':'+end+") - " + title + " ######################################");
    	IO.println("sourceText:        0         10        20        30        40        50        60        70        80        90");
    	IO.println("sourceText:        0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
    	IO.println("sourceText:        " + (""+text).replace("\t", "¤").replace("\r", "¤").replace("\n", "¤"));
    	IO.println("sourceText(esc):   " + (""+text).replace("\t", "\\t").replace("\r", "\\r").replace("\n", "\\n"));
    	IO.println("textEndOffset:     " + textEndOffset + '(' + (textEndOffset-beg) + ')');
    	IO.println("currentLexerToken: " + currentLexerToken);
    	IO.println("nextPos:           " + nextPos + '(' + (nextPos-beg) + ")  With value: " + edNext());
//    	IO.println("tokenStartOffset:  " + tokenStartOffset);
//    	IO.println("tokenEndOffset:    " + tokenEndOffset);
    	IO.println("currentColumn:     " + currentColumn);
//    	IO.println("currentLength:     " + currentLength);
    	IO.println("tokenStartPos:     " + tokenStartPos + '(' + (tokenStartPos-beg) + ')');
    	IO.println("currentLineNumber: " + currentLineNumber);
    	IO.println("tokenQueue:        " + tokenQueue);
    	printLines();
    	IO.println("############################### END LEXER SNAPSHOT - " + title + " ######################################");
    }
    
    /// Debug utility
    public void printState(String title) {
    	IO.println("==== LEXER STATE: " + title + "  " + currentLexerToken
    			+ "nextPos=" + nextPos+",currentColumn=" + currentColumn+", currentLineNumber"+currentLineNumber);
    }
    
    /// Debug utility
    public void printLines() {
        int nLines = lineStartPos.size();
        for(int i=0;i<nLines;i++) {
        	int beg = getLineStartPos(i);
//        	IO.println("Line " + i + ": starts " + getLineStartPos(i));
        	int end = ((i+1) < nLines)?getLineStartPos(i+1) : textEndOffset;
//        	IO.println("Line " + i + ": start: " + getLineStartPos(i) + ", end: " + end);
        	CharSequence text = sourceText.subSequence(beg, end);
        	String line = "Line " + i +"["+beg+':'+end+"): ";
        	while(line.length() < 19) line = line + " ";
        	IO.println(line + '|' + (""+text).replace("\t", "¤").replace("\r", "¤").replace("\n", "¤") + '|');
        }
    }

}
