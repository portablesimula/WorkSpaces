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
import simula.core.builder.token.CharacterConst;
import simula.core.builder.token.CommentToken;
import simula.core.builder.token.Identifier;
import simula.core.builder.token.IntegerConst;
import simula.core.builder.token.KeyWordToken;
import simula.core.builder.token.LexToken;
import simula.core.builder.token.LongRealConst;
import simula.core.builder.token.RealConst;
import simula.core.builder.token.SimpleString;
import simula.core.builder.token.WhiteSpaceToken;
import simula.core.utilities.KeyWord;
import simula.core.utilities.Util;

/// The Simula Scanner.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/parsing/SimulaScanner.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class SimulaLexer {
	private boolean TRACE_CURRENT_COLUMN = false;// true;//false;
	private SourceTextReader reader;
	
	private SimulaBuilder simBuilder;
//    private CharSequence sourceText;
    private List<String> sourceLines;
//    private int textEndOffset;
//    private int reader.nextPos();
//    private int currentLineNumber;
    private int currentColumn;
    private int tokenStartPos; // Used to calculate length
    
//    private LexToken prevParserToken;
    private LexToken prevLexerToken;
    private LexToken currentLexerToken;
    
//    private List<Integer> lineStartPos = new ArrayList<>();
//    public int getLineStartPos(int lineNumber) {
//    	int pos = lineStartPos.get(lineNumber);
//       	if(Option.LEX_VERIFY) {
////           	Util.println("SimulaLexer.getLineStartPos: "+lineNumber+": "+pos+" TABLE="+lineStartPos);
//       		if(reader.nextPos() > textEndOffset) Util.IERR("IMPOSSIBLE");
//       	}
//       	return pos;
//    }
//    public void update_LineStartPos_List() {
//       	lineStartPos.add(reader.nextPos());    	
//       	if(Option.LEX_VERIFY) {
////           	Util.println("SimulaLexer.update_LineStartPos_List: "+(lineStartPos.size()-1)+": "+reader.nextPos()+" TABLE="+lineStartPos);
//       		if(reader.nextPos() > textEndOffset) Util.IERR("IMPOSSIBLE");
//           	if(lineStartPos.size() != (currentLineNumber+1)) {
//        		Util.println("SimulaLexer.scanbasic: currentLineNumber: " + currentLineNumber + ", lineStartPos.size(): " + lineStartPos.size());
//           		Util.IERR("IMPOSSIBLE");
//           	}
//       	}
//    }

    public int getSourceLineNumber() {
    	return reader.currentLineNumber();
    }

    public boolean eof() {
		return reader.eof();
	}
	
	public void flush() {
		Util.println("SimulaLexer.close: ");
		while(tokenQueue.size()>0) { 
			nextToken();
		}
//		Util.STOP();
	}

//    /// The pushBack stack
//    private Stack<Character> puchBackStack=new Stack<Character>();

    /// The Token queue. The method nextToken will pick Tokens from the queue first.
    private LinkedList<LexToken> tokenQueue=new LinkedList<LexToken>();

//    /// The reader.getCurrent() source file reader;
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
//	public SimulaLexer(final SimulaBuilder simBuilder, final CharSequence sourceText) {
	public SimulaLexer(final SimulaBuilder simBuilder, final List<String> sourceLines) {
//		Util.println("NEW SimulaLexer: sourceText(lng:"+sourceText.length()+")" + Comn.printable((String) sourceText));
		this.simBuilder = simBuilder;
		this.reader = new SourceTextReader(sourceLines);
//		this.sourceText = sourceText;
//		this.textEndOffset = sourceText.length();
		this.sourceLines = sourceLines;
//		reader.nextPos() = 0;
//		currentLineNumber = 0;
//       	update_LineStartPos_List();
		CoreGlobal.sourceLineNumber=1;
//		nextToken();+
	}


//	private LexToken getPrevLexerToken() {
//        if(Option.internal.TRACE_LEXER > 1) Util.println("SimulaLexer.getPrevLexerToken: "+prevLexerToken);
//        return prevLexerToken;
//    }
//
//	private LexToken getCurrentLexerToken() {
//        if(Option.internal.TRACE_LEXER > 1) Util.println("SimulaLexer.getCurrentLexerToken: "+currentLexerToken);
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
    			if(TRACE_CURRENT_COLUMN) Util.println("SimulaLexer.nextToken(1): currentColumn="+currentColumn+", prevLexerToken: "+prevLexerToken);
    		}
//    		if(prevLexerToken.isParserToken()) prevParserToken = prevLexerToken;
    	}
    	tokenStartPos = reader.nextPos();
    	
    	LexToken lexToken;
    	if(tokenQueue.size()>0) { 
		    lexToken=tokenQueue.remove();
		    if(Option.internal.TRACE_NEW_LEXTOKEN > 0) Util.println("POP LexToken: " + lexToken);
//			Util.println("SimulaLexer.nextToken: currentColumn="+currentColumn+", reader.nextPos()=" + reader.nextPos() + ", tokenStartPos="+tokenStartPos);
//			Util.println("SimulaLexer.nextToken: currentColumn="+lexToken.column+" FROM POP TOKEN");
		    if(lexToken.keyWord == KeyWord.EOF) {
//		    	Util.IERR("SJEKK DETTE: GOT EOF");
		    }
		    
			currentColumn = (lexToken.keyWord == KeyWord.NEWLINE)? 0 : lexToken.column;
			if(TRACE_CURRENT_COLUMN) Util.println("SimulaLexer.nextToken(2): currentColumn="+currentColumn);
		} else lexToken = scanToken();
		
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("Item.nextToken, " + edcurrent());
		currentLexerToken = lexToken;
//	    Util.println("GOT LexToken: " + lexToken);
//		Util.println("SimulaLexer.nextToken: currentColumn="+currentColumn+", reader.nextPos()=" + reader.nextPos() + ", tokenStartPos="+tokenStartPos);
		
		if((lexToken.keyWord != KeyWord.NEWLINE) && (lexToken.keyWord != KeyWord.WHITESPACES))
			simBuilder.lexTokenList.add(lexToken);
		
		return (lexToken);
	}
	
    //********************************************************************************
    //**	                                                                 scanToken 
    //********************************************************************************
	/// Scan and return a Token.
	/// <pre>
    /// Pre-Condition: reader.getCurrent() is first character of construct.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
	/// @return next Token
    private LexToken scanToken() {
//		snapShot("SimulaLexer.scanToken: BEGIN");
//		Util.println("\n\nSimulaLexer.scanToken: BEGIN reader.nextPos(): " + reader.nextPos() + " with value: " + edCurrent());
    	LexToken lexToken = scanBasic();    
//		snapShot("SimulaLexer.scanToken: END");
//		Util.println("SimulaLexer.scanToken: ENDOF reader.nextPos(): " + reader.nextPos() + " with value: " + edCurrent());
    	
		return lexToken;
    }
    
    //********************************************************************************
    //**	                                                                 scanBasic 
    //********************************************************************************
    /// Scan basic Token
    /// Pre-Condition: reader.getCurrent() is first character of construct.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
    /// @return next Token
    private LexToken scanBasic() {
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("SimulaScanner.scanBasic, "+edcurrent());
    	while(true)	{
    		LexToken.lineNumberBeforeScanBasic = CoreGlobal.sourceLineNumber;
    		
//    		if(reader.getCurrent() == EOF_MARK) {
////				LexToken EOFToken = new KeyWordToken(tokenStartLine, sourceText, reader.nextPos(), reader.nextPos(), KeyWord.EOF, "");
//				LexToken EOFToken = newKeyWordToken(KeyWord.EOF);
//				Util.println("SimulaLexer.scanBasic: EOFToken: " + EOFToken);
//				return EOFToken;
//    		}

    		if(Character.isLetter(reader.getNext())) {
    			return(scanIdentifier());
    		}

    		switch(reader.getCurrent()) {
    			case SourceTextReader.EOF_MARK: return newKeyWordToken(KeyWord.EOF);
    			case '%':                  LexToken dirToken = scanCommentToEndOfLine();
    			               			   Directive.treatDirective(simBuilder, dirToken, dirToken.getText());
    			                           return dirToken;
    		    case '=':
		            if(reader.getNext() == '=')   return(newKeyWordToken(KeyWord.EQR));
		            if(reader.getCurrent() == '/') {
		            	if(reader.getNext() == '=')   return(newKeyWordToken(KeyWord.NER));
		                else {
			            	String error = "Illegal character combination ="+(char)reader.getCurrent();
			            	LexToken lexToken = newKeyWordToken(KeyWord.BAD_CHARACTERS);
			        		Util.syntaxError(simBuilder, lexToken, error);
			            	return lexToken;
		                }
		            }
		            reader.pushBackPos(1);        return newKeyWordToken(KeyWord.EQ);
	            case '>':
		            if(reader.getNext() == '=')   return(newKeyWordToken(KeyWord.GE));
		            reader.pushBackPos(1);        return(newKeyWordToken(KeyWord.GT));
	            case '<':
	                if(reader.getNext() == '=')   return(newKeyWordToken(KeyWord.LE));
		            if(reader.getCurrent() == '>')     return(newKeyWordToken(KeyWord.NE));
		            reader.pushBackPos(1);        return(newKeyWordToken(KeyWord.LT));
	            case '+':                  return(newKeyWordToken(KeyWord.PLUS));
	            case '-':
	            	if(reader.getNext() == '-')   return(scanCommentToEndOfLine());
	                reader.pushBackPos(1); 	   return(newKeyWordToken(KeyWord.MINUS));
	            case '*':
		            if(reader.getNext() == '*')   return(newKeyWordToken(KeyWord.EXP));
		            reader.pushBackPos(1); 	   return(newKeyWordToken(KeyWord.MUL));
	            case '/':
		            if(reader.getNext() == '/')   return(newKeyWordToken(KeyWord.INTDIV));
		            reader.pushBackPos(1); 	   return(newKeyWordToken(KeyWord.DIV));
	            case '.':
		            if(Character.isDigit(reader.getNext())) { return(scanDotDigit(new StringBuilder())); }
		            reader.pushBackPos(1);        return(newKeyWordToken(KeyWord.DOT));
	            case ',':	               return(newKeyWordToken(KeyWord.COMMA));
	            case ':':
		            if(reader.getNext() == '=')                return(newKeyWordToken(KeyWord.ASSIGNVALUE));
//		            if(reader.getCurrent() == '-' && pardepth == 0) return(newKeyWordToken(KeyWord.ASSIGNREF));
                    if(reader.getCurrent() == '-' && !parsingBoundPairList) return newKeyWordToken(KeyWord.ASSIGNREF);
		            reader.pushBackPos(1);                  return(newKeyWordToken(KeyWord.COLON));
	            case ';':	return(newKeyWordToken(KeyWord.SEMICOLON));
	            case '(':	return(newKeyWordToken(KeyWord.BEGPAR));
	            case ')':	return(newKeyWordToken(KeyWord.ENDPAR));
	            case '[':	return(newKeyWordToken(KeyWord.BEGBRACKET));
	            case ']':	return(newKeyWordToken(KeyWord.ENDBRACKET));
	            case '&':
				    if(reader.getNext()=='&' || reader.getCurrent()=='-' || reader.getCurrent()=='+' || Character.isDigit(reader.getCurrent())) 
				    	return (scanDigitsExp(null));
				    reader.pushBackPos(1); return (newKeyWordToken(KeyWord.AMPERSAND));
	            case '!':  return(scanComment());
	            case '\'': return(scanCharacterConstant());
	            case '\"': return(scanTextConstant());
	            case '0':case '1':case '2':case '3':case '4':
	            case '5':case '6':case '7':case '8':case '9':return(scanNumber());
		    	  
	            case '\n': return(newNewlineToken());

	            case '\r': if(reader.getNext()=='\n') return (newNewlineToken());
				    reader.pushBackPos(1); // NOTE: No break or return ==> default
				    
	            default: if(Character.isWhitespace(reader.getCurrent())) return(scanWhiteSpace());
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
    /// Pre-Condition: reader.getCurrent() is first character of construct.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
	private LexToken scanWhiteSpace() {
//		snapShot("SimulaLexer.scanWhiteSpace: BEGIN");
//		Util.println("SimulaLexer.scanWhiteSpace: BEGIN reader.nextPos(): " + reader.nextPos() + " with value: " + edCurrent());
    	LOOP:while(true) {
    		reader.getNext();
//    		Util.println("SimulaLexer.scanWhiteSpace: currentColumn: " + currentColumn);
			if(reader.getCurrent() == '\r' && reader.nextCharIs('\n')) break LOOP;
    		if(reader.getCurrent() == '\n') break LOOP;
//    		if(! Option.TESTING_TABS) {
//    			if(reader.getCurrent() == '\t') break LOOP;
//    		}
    		if(Character.isWhitespace(reader.getCurrent())) continue LOOP;
    		break LOOP;
    	}
    	reader.pushBackPos(1);
    	return new WhiteSpaceToken(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, this);
     }

    
    //********************************************************************************
    //**	                                                            scanIdentifier 
    //********************************************************************************
    /// Scan and return an identifier Token.
    /// <pre>
    /// Pre-Condition: reader.getCurrent() is first character of construct.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.
    ///                reader.nextPos() points to first character after construct.
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
//	    Util.println("SimulaLexer.scanIdentifier: " + name + " currentColumn=" + currentColumn);
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
	///  - reader.getCurrent() is last character of construct
	///  - getNext will return first character after construct
	/// 
	/// @return A Token representing a unsigned number.
    private LexToken scanNumber() {
    	int radix=10;
    	char firstChar=(char)reader.getCurrent();
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanNumber, "+edcurrent());
    	Util.ASSERT(Character.isDigit((char)(reader.getCurrent())),"scanNumber:Expecting a Digit");
    	StringBuilder number=new StringBuilder();
	
    	number.append((char)reader.getCurrent());
    	if(reader.getNext() == 'R' && (firstChar == '2' | firstChar == '4' | firstChar == '8')) {
    		radix=firstChar - '0';
    		if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanNumber, radix="+radix);
    		number.setLength(0);
    	} else if(firstChar == '1' && reader.getCurrent() == '6') { 
    		number.append((char)reader.getCurrent());
    		if(reader.getNext() == 'R') {
    			radix=16;
    			if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanNumber, radix="+radix);
    			number.setLength(0);
    		} else reader.pushBackPos(1);
    	} else reader.pushBackPos(1);
    
    	while ((radix==16 ? isHexDigit(reader.getNext()) : Character.isDigit(reader.getNext())) || reader.getCurrent()=='_')
    		if(reader.getCurrent()!='_') number.append((char)reader.getCurrent());
    
    	if(reader.getCurrent() == '.' && radix == 10) { reader.getNext(); return(scanDotDigit(number)); }
    
    	if(reader.getCurrent() == '&' && radix == 10) { reader.getNext(); return(scanDigitsExp(number)); }
      
    	String result=number.toString(); number=null;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanNumber, result='"+result+"' radix="+radix);

    	reader.pushBackPos(1);
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
    /// End-Condition: reader.getCurrent() is last character of construct                 
    ///                getNext will return first character after construct
    /// </pre>
    /// @param number The edited number so far
    /// @return next Token
    private LexToken scanDotDigit(StringBuilder number) {
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanDotDigit, "+edcurrent());
    	number.append('.');
    	if(Character.isDigit(reader.getCurrent())) number.append((char)reader.getCurrent());
    	while(Character.isDigit(reader.getNext()) || reader.getCurrent() == '_')
    		if(reader.getCurrent() != '_') number.append((char)reader.getCurrent());

    	if(reader.getCurrent() == '&') { reader.getNext(); return(scanDigitsExp(number)); }
    
    	String result=number.toString(); number=null;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanDotDigit, result='"+result);
    	reader.pushBackPos(1);
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
    /// End-Condition: reader.getCurrent() is last character of construct                 
    ///                getNext will return first character after construct
    ///                
    /// @param number The edited number so far
    /// @return next Token
    private LexToken scanDigitsExp(StringBuilder number) {
    	String result;
    	boolean doubleAmpersand=false;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanDigitsExp, "+edcurrent());
    	if(number==null) { number=new StringBuilder(); number.append('1'); }
    	if(reader.getCurrent() == '&') { reader.getNext(); doubleAmpersand=true; }
    	number.append('e');
    	if(reader.getCurrent() == '-') { number.append('-'); reader.getNext(); }
    	else if(reader.getCurrent() == '+') reader.getNext();
    	if(Character.isDigit(reader.getCurrent())) number.append((char)reader.getCurrent());
    	while(Character.isDigit(reader.getNext()) || reader.getCurrent() == '_') number.append((char)reader.getCurrent());
	      
    	result=number.toString(); number=null;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanDigitsExp, result='"+result);
    	reader.pushBackPos(1);
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
    /// End-Condition: reader.getCurrent() is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return the resulting identifier
    private String scanName() {
    	StringBuilder name=new StringBuilder();
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanName, "+edcurrent());
    	Util.ASSERT(Character.isLetter((char)(reader.getCurrent())),"Expecting a Letter");
    	name.append((char)reader.getCurrent());
//    	while ((Character.isLetter(reader.getNext()) || Character.isDigit(reader.getCurrent()) || reader.getCurrent() == '_'))
//    		name.append((char)reader.getCurrent());
    	LOOP:while(true) {
    		reader.getNext();
//    		Util.println("SimulaLexer.scanName: GOT " + reader.getCurrent());
    		if(reader.getCurrent() == SourceTextReader.EOF_MARK) break LOOP;
    		if(Character.isLetter(reader.getCurrent())) ; // OK
    		else if( Character.isDigit(reader.getCurrent())) ; // OK
    		else if( reader.getCurrent() == '_') ; // OK
    		else break LOOP;
    		name.append((char)reader.getCurrent());
    	}
    	if(reader.getCurrent() != SourceTextReader.EOF_MARK) reader.pushBackPos(1);
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanName, name="+name+",reader.getCurrent()="+edcurrent());
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
    /// End-Condition: reader.getCurrent() is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return next Token
    private LexToken scanCharacterConstant() {
    	List<String> errors = null;
    	char result=0;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanCharacterConstant, "+edcurrent());
    	Util.ASSERT((char)(reader.getCurrent())=='\'',"Expecting a character quote '");
    	if((isPrintable(reader.getNext())) && reader.getCurrent() != '!') {
    		result=(char)reader.getCurrent(); reader.getNext();
    	} else if(reader.getCurrent() == '!') {
    		result=(char)scanPossibleIsoCode(); reader.getNext();
    	} else {
    		if(errors == null) errors = new ArrayList<>();
    		errors.add("Illegal character constant. "+edcurrent());
    	}
    	
    	if(reader.getCurrent() != '\'') {
    		if(errors == null) errors = new ArrayList<>();
    		errors.add("Character constant is not terminated. "+edcurrent());
    		reader.pushBackPos(1);
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
    /// Pre-Condition: reader.getCurrent() is first character of construct.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct. I.e. '"' or last string separator
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
	private boolean TRACE_TEXTCONST = false;//true;
    private LexToken scanTextConstant() {
    	if(Option.LEX_VERIFY) {
    		if(reader.getCurrent() != '"') Util.IERR(""+edCurrent());
    	}
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanTextConstant, "+edcurrent());
    	
    	do { scanSimpleString();
    		 reader.getNext();
    	} while(moreSimpleString());
    	reader.pushBackPos(1);
    	
//		simBuilder.printTokenList("END scanTextConstant");
//		printQueue("SimulaLexer.scanTextConstant: ");
//		Util.STOP();

    	LexToken result=tokenQueue.remove();
    	return(result);
    }
    
    //********************************************************************************
    //**	                                                       moreSimpleString
    //********************************************************************************
    /// Scan string separator, if any. Then test if reader.getCurrent() is a string quote '"'.
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
    /// Pre-Condition: reader.getCurrent() is first character of String Separator.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is first character after String Separator. I.e. '"' if a Simple String follows
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
    /// @Return true: if a Simple String follows 
    /// </pre>
	private boolean moreSimpleString() {
		boolean TRACE_SKIP_SEP = false;//true;
		if(TRACE_SKIP_SEP) Util.println("\nSimulaLexer.moreSimpleString: BEFORE reader.getCurrent(): " + edChar((char) reader.getCurrent()));

		// First: Skip Token separators
		while(currentIsTokenSeparator()) {
			if(Option.LEX_VERIFY) {
//				if(reader.getCurrent() == '\n' || reader.getCurrent() == ';' || isWhiteSpace(reader.getCurrent())) ; // OK
				if(reader.getCurrent() == '\n' || reader.getCurrent() == ';' || Character.isWhitespace((char)reader.getCurrent())) ; // OK
				else Util.IERR("SimulaLexer.moreSimpleString: TokenSeparator End-Condition Failed: reader.getCurrent() = "+edCurrent());
			}
			reader.getNext();
		}
		if(TRACE_SKIP_SEP) Util.println("\nSimulaLexer.moreSimpleString: AFTER reader.getCurrent(): " + edChar((char) reader.getCurrent()));

		reader.pushBackPos(1);
		if(TRACE_SKIP_SEP) Util.println("SimulaLexer.moreSimpleString(2): "+edChar((char) reader.getCurrent()));
		
		if(reader.nextPos() > tokenStartPos) {
			LexToken lexToken = (newKeyWordToken(KeyWord.COMMENT_KEY));
			tokenQueueAdd("scanTextConstant - StringSeparator", lexToken);
		}
		
		reader.getNext();
		if(TRACE_SKIP_SEP) Util.println("SimulaLexer.moreSimpleString: reader.getCurrent(): " + edChar((char) reader.getCurrent()) );
		return reader.getCurrent() == '"';
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
    /// Pre-Condition: reader.getCurrent() is first character of construct. I.e. '"'
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.  I.e. '"' or EOF_MARK
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
    private void scanSimpleString() {
    	if(Option.LEX_VERIFY) {
    		if(reader.getCurrent() != '"') Util.IERR(""+edCurrent());
    	}
    	StringBuilder sb=new StringBuilder();
		// Scan simple-string:
		if(TRACE_TEXTCONST) Util.println("SimulaLexer.scanSimpleString: BEGIN Scan simple-string: ");
		reader.getNext();
		LOOP:while(true) {
//			Util.println("SimulaLexer.scanSimpleString: CHECK line: " + currentLineNumber + ", currrent=" + edChar((char) reader.getCurrent()));
			switch(reader.getCurrent()) {
			case '"':
				if(reader.nextCharIs('"')) {
					sb.append('"');
					reader.getNext(); reader.getNext();
					continue LOOP;
				}
				if(reader.nextPos() > tokenStartPos) {
					tokenQueueAdd("scanSimpleString - TOKEN-2", newSimpleStringToken(sb.toString()));
				}
				break LOOP;
			case '!':
				int code=scanPossibleIsoCode();
				sb.append((char)code);
				break;
			case '\r':
				Util.println("\nSimulaLexer.scanSimpleString: GOT NEWLINE(CRLF) length: " + (reader.nextPos() - tokenStartPos));
				if(! reader.nextCharIs('\n')) Util.IERR("");
				reader.pushBackPos(1);
				if(TRACE_TEXTCONST) Util.println("\nSimulaLexer.scanSimpleString: GOT NEWLINE(CRLF) length: " + (reader.nextPos() - tokenStartPos));
				if(reader.nextPos() > tokenStartPos) {
					LexToken lexToken = newSimpleStringToken(sb.toString());
					Util.warning(simBuilder, lexToken, "Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6");
		    	    tokenQueueAdd("scanSimpleString - CRLF", lexToken);
				}
	    		
				reader.getNext(); reader.getNext(); // Consume CRLF
        	    tokenQueueAdd("scanSimpleString - CRLF", newNewlineToken());
				sb = new StringBuilder();
//				reader.getNext();
				break;
			case '\n':
				reader.pushBackPos(1);
				if(TRACE_TEXTCONST) Util.println("\nSimulaLexer.scanSimpleString: GOT NEWLINE(LF) length: " + (reader.nextPos() - tokenStartPos));
				if(reader.nextPos() > tokenStartPos) {
					LexToken lexToken = newSimpleStringToken(sb.toString());
					Util.warning(simBuilder, lexToken, "Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6");
		    	    tokenQueueAdd("scanSimpleString - LF", lexToken);
				}
	    		
				reader.getNext();
        	    tokenQueueAdd("scanSimpleString - CRLF", newNewlineToken());
				sb = new StringBuilder();
				break;
			case SourceTextReader.EOF_MARK:
				if(TRACE_TEXTCONST) Util.println("\nSimulaLexer.scanSimpleString: GOT EOF_MARK length: " + (reader.nextPos() - tokenStartPos));
				if(reader.nextPos() > tokenStartPos) {
					LexToken lexToken = newSimpleStringToken(sb.toString());
					Util.warning(simBuilder, lexToken, "Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6");
		    	    tokenQueueAdd("scanSimpleString - EOF_MARK", lexToken);
				}
				tokenQueueAdd("scanSimpleString - EOF-TOKEN", newKeyWordToken(KeyWord.EOF));
				
				break LOOP;
				
			default: sb.append((char)reader.getCurrent());
			}
			reader.getNext();
		}
		if(TRACE_TEXTCONST) Util.println("\nSimulaLexer.scanSimpleString: ENDOF Scan simple-string: " + (reader.nextPos() - tokenStartPos));

//       	simBuilder.printTokenList("END scanSimpleString");
//        printQueue("SimulaLexer.scanSimpleString: ");

    	if(Option.LEX_VERIFY) {
            /// End-Condition: reader.getCurrent() is last character of construct.  I.e. '"' or EOF_MARK
    		if(reader.getCurrent() == '"') ; // OK
    		else if(reader.getCurrent() == SourceTextReader.EOF_MARK) ; // OK
    		else Util.IERR("SimulaLexer.scanSimpleString: End-Condition Failed: reader.getCurrent() = "+edCurrent());
    	}
    }

    //********************************************************************************
  	//**	                                                  currentIsStringSeparator
    //********************************************************************************
    /// Scanner Utility: Check if reader.getCurrent() is a string separator.
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
    /// Pre-Condition: reader.getCurrent() is first character of construct.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.
    ///                I.e: LF, ';' or a whitespace 
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return true if reader.getCurrent() is a string separator
    private boolean currentIsTokenSeparator() {
    	boolean TRACE_TOKEN_SEP = false; // true;
		if(TRACE_TOKEN_SEP) Util.println("\nSimulaLexer.currentIsTokenSeparator: BEGIN reader.getCurrent(): " + edChar((char) reader.getCurrent()) );

		if(reader.getCurrent() == '\r' && reader.nextCharIs('\n')) {
    		if((reader.nextPos()-1) > tokenStartPos) {
	    		reader.pushBackPos(1);
	    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", newKeyWordToken(KeyWord.COMMENT_KEY));
//	    		reader.pushBackPos(-1);
	    	    reader.getNext();
    		}
    		reader.getNext();
    		    	    
			if(TRACE_TOKEN_SEP) Util.println("SimulaLexer.currentIsTokenSeparator: NEW NEWLINE");
    	    tokenQueueAdd("currentIsTokenSeparator - NEWLINE", newNewlineToken());
    	    if(Option.LEX_VERIFY) {
    	    	if(reader.getCurrent() != '\n')
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: reader.getCurrent() = "+edCurrent());
    	    }
    	    return true;    
		}
		
    	if(reader.getCurrent() == '\n') {
    		Util.println("SimulaLexer.currentIsTokenSeparator: reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
    		if((reader.nextPos()-1) > tokenStartPos) {
	    		reader.pushBackPos(1);
	    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", newKeyWordToken(KeyWord.COMMENT_KEY));
//	    		reader.pushBackPos(-1);
	    	    reader.getNext();
    		}    		    	    
			if(TRACE_TOKEN_SEP) Util.println("SimulaLexer.currentIsTokenSeparator: NEW NEWLINE");
    	    tokenQueueAdd("currentIsTokenSeparator - NEWLINE", newNewlineToken());
    	    if(Option.LEX_VERIFY) {
    	    	if(reader.getCurrent() != '\n')
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: reader.getCurrent() = "+edCurrent());
    	    }
    	    return true;    
    	}
    	
    	if(reader.getCurrent() == '!') {
			if(TRACE_TOKEN_SEP) Util.println("SimulaLexer.currentIsTokenSeparator: NEW COMMENT-1");
//			Util.IERR("SJEKK DETTE");
    	    tokenQueueAddCommentTokens();
    	    if(Option.LEX_VERIFY) {
    	    	if(reader.getCurrent() != ';')
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: reader.getCurrent() = "+edCurrent());
    	    }
    		return true;	
    	}
    	
    	if(reader.getCurrent() == '-' && reader.nextCharIs('-')) {
 			if(TRACE_TOKEN_SEP) Util.println("SimulaLexer.currentIsTokenSeparator: currentColumn="+currentColumn);
//			Util.IERR("SJEKK DETTE");
    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", scanCommentToEndOfLine());
//			this.snapShot("SimulaLexer.currentIsTokenSeparator: ");
    	    if(Option.LEX_VERIFY) {
    	    	if(! (reader.nextCharIs('\r') || reader.nextCharIs('\n')))
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: reader.getCurrent() = "+edCurrent());
    	    }
			// Consume CRLF or LF
    	    if(reader.nextCharIs('\r')) reader.getNext();
			reader.getNext();
    	    tokenQueueAdd("currentIsTokenSeparator - NEWLINE", newNewlineToken());
			
    		return true;	
    	}
    	
    	if(reader.getCurrent() == '%' && currentColumn == 0) {
 			if(TRACE_TOKEN_SEP) Util.println("SimulaLexer.currentIsTokenSeparator: currentColumn="+currentColumn);
//			Util.IERR("SJEKK DETTE");
			LexToken lexToken = scanCommentToEndOfLine();
			Directive.treatDirective(simBuilder, lexToken, lexToken.getText());
    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", lexToken);
    	    if(Option.LEX_VERIFY) {
    	    	if(! (reader.nextCharIs('\r') || reader.nextCharIs('\n')))
		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: reader.getCurrent() = "+edCurrent());
    	    }
			// Consume CRLF or LF
    	    if(reader.nextCharIs('\r')) reader.getNext();
			reader.getNext();
    	    tokenQueueAdd("currentIsTokenSeparator - NEWLINE", newNewlineToken());

    	    return true;	
    	}
    	
    	if(Character.isLetter((char)reader.getCurrent())) {
//        	Util.println("SimulaLexer.currentIsTokenSeparator: Current isLetter:" + (char)reader.getCurrent());
    		if((reader.nextPos()-1) > tokenStartPos) {
	    		reader.pushBackPos(1);
	    	    tokenQueueAdd("currentIsTokenSeparator - COMMENT-0", newKeyWordToken(KeyWord.COMMENT_KEY));
//	    		reader.pushBackPos(-1);
	    	    reader.getNext();
    		}    		    	    
    		String name=scanName();
    		if(name.equalsIgnoreCase("COMMENT")) {
    			if(TRACE_TOKEN_SEP) Util.println("SimulaLexer.currentIsTokenSeparator: NEW COMMENT-2");
        	    tokenQueueAddCommentTokens();
        	    if(Option.LEX_VERIFY) {
        	    	if(reader.getCurrent() != ';')
    		    		Util.IERR("SimulaLexer.currentIsTokenSeparator: End-Condition Failed: reader.nextPos()="+reader.nextPos()+", reader.getCurrent() = "+edCurrent());
        	    }
    			return true;
    		} else {
    			reader.pushBackPos(name.length() - 1);
    		}
    		return false;
		}
    	
//    	boolean res = isWhiteSpace(reader.getCurrent());
    	boolean res = Character.isWhitespace((char)reader.getCurrent());
//    	Util.println("SimulaLexer.currentIsTokenSeparator: isWhiteSpace("+reader.getCurrent()+")=" + isWhiteSpace(reader.getCurrent()));
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
    /// End-Condition: reader.getCurrent() is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return the resulting iso-code
    private int scanPossibleIsoCode() {
		char firstchar, secondchar, thirdchar;
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("scanPossibleIsoCode, " + edcurrent());
		Util.ASSERT((char) (reader.getCurrent()) == '!', "Expecting a character !");
		if (Character.isDigit(reader.getNext())) {
			firstchar = (char) reader.getCurrent();
			if (Character.isDigit(reader.getNext())) {
				secondchar = (char) reader.getCurrent();
				if (Character.isDigit(reader.getNext())) {
					thirdchar = (char) reader.getCurrent();
					if (reader.getNext() == '!') { // ! digit digit digit ! Found
						int value = (((firstchar - '0') * 10 + secondchar - '0') * 10 + thirdchar - '0');
						if (Option.internal.TRACE_LEXER > 0)
							Util.TRACE("scanPossibleIsoCode:Got three digits: "+(char)firstchar+(char)secondchar+(char)thirdchar+"value="+value);
						if (value < 256)
							return (value);
						Util.warning(simBuilder, "ISO-Code " + value + " is out of range (0:255)"
							+" interpreted as an ordinary sequence of characters: !" +value + "!  See Simula Standard 1.6");
						reader.pushBackPos(4);
						return ('!');
					} else {
						reader.pushBackPos(4);
						return ('!');
					}
				} else if (reader.getCurrent() == '!') { // ! digit digit ! Found
					return ((char) ((firstchar - '0') * 10 + secondchar - '0'));
				} else {
					reader.pushBackPos(3);
					return ('!');
				}
			} else if (reader.getCurrent() == '!') { // ! digit ! Found
				return ((char) (firstchar - '0'));
			} else {
				reader.pushBackPos(2);
				return ('!');
			}
		} else {
			reader.pushBackPos(1);
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
    /// Pre-Condition: reader.getCurrent() is first character of construct.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.
    ///                reader.nextPos() points to first character after construct.
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
    		if(reader.getCurrent() == SourceTextReader.EOF_MARK) {
    			Util.println("\n\nLexToken.scanComment: BEGIN TREAT EOF_MARK: reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
    			Util.println("LexToken.scanComment: AT EOF_MARK: reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
    			int lng = reader.nextPos() - tokenStartPos - 1;
    			Util.println("LexToken.scanComment: AT EOF_MARK: lng="+lng);
    			if(lng > 0) {
    				nPhrase++;
//    				if(reader.nextPos() != textEndOffset) Util.IERR("IMPOSSIBLE");
    				LexToken lexToken = newCommentToken(KeyWord.COMMENT_TEXT);
    				if(nPhrase > 1) Util.warning(simBuilder, lexToken, "Comment spans multiple lines");
    				tokenQueueAdd("scanComment-EOF_MARK", lexToken);
    			}
    			break LOOP;
    		}

    		reader.getNext();
    		if(TRACE_SCAN_COMMENT) Util.println("LexToken.scanComment: reader.getCurrent()="+reader.getCurrent()+":'"+Comn.printable(""+(char)reader.getCurrent())+"'");

    		if (reader.getCurrent() == '\n') {
    			if(TRACE_SCAN_COMMENT) Util.println("LexToken.scanComment: GOT NEWLINE");
//    			Util.println("\n\n\n\nLexToken.scanComment: BEGIN TREAT NEWLINE: reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);

//    			int lng = reader.nextPos() - tokenStartPos - 1;
    			int lng = reader.prevLineLength() - tokenStartPos - 1;
    			if(lng > 0) {
    				nPhrase++;
    				reader.pushBackPos(1);
    				if(Option.LEX_VERIFY) {
    					if(lng != (reader.nextPos() - tokenStartPos)) Util.IERR("IMPOSSIBLE: lng=" + lng +", reader.nextPos() - tokenStartPos: " + (reader.nextPos() - tokenStartPos));
//    					if(sourceText.charAt(reader.nextPos()) != '\n') Util.IERR("IMPOSSIBLE");
    				}

    				LexToken lexToken = newCommentToken(KeyWord.COMMENT_TEXT);
    				if(nPhrase > 1) Util.warning(simBuilder, lexToken, "Comment spans multiple lines");
    				tokenQueueAdd("scanComment-NEWLINE", lexToken);

    				reader.getNext(); // Skip NEWLINE(LF)
    				if(Option.LEX_VERIFY) {
    					if(reader.getCurrent() != '\n') Util.IERR("IMPOSSIBLE");
//    					if(sourceText.charAt(tokenStartPos) != '\n') Util.IERR("IMPOSSIBLE");
    				}
    			}
    			if(reader.getCurrent() != '\n') Util.IERR("IMPOSSIBLE");
    			tokenQueueAdd("scanComment - NEWLINE", newNewlineToken());
    		} else if (reader.getCurrent() == ';') {
//    			Util.println("\n\n\n\nLexToken.scanComment: BEGIN TREAT SEMICOLON: reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
//    			Util.println("LexToken.scanComment: AT SEMICOLON: reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
//    			int lng = reader.nextPos() - tokenStartPos;
//    			Util.println("LexToken.scanComment: AT SEMICOLON: lng="+lng);
				LexToken lexToken = newCommentToken(KeyWord.COMMENT_TEXT);
				if(nPhrase > 1) Util.warning(simBuilder, lexToken, "Comment spans multiple lines");
				tokenQueueAdd("scanComment-SEMICOLON", lexToken);
    			break LOOP;
    		} else {
    			if(TRACE_SCAN_COMMENT) Util.println("LexToken.scanComment: GOT OTHER="+reader.getCurrent()+":'"+Comn.printable(""+(char)reader.getCurrent())+"'");
    		}
    	}

    	if(TRACE_SCAN_COMMENT) {
    		Util.println("SimulaLexer.scanComment: commentToken: " + commentToken);
    		Util.println("SimulaLexer.scanComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
    		Util.println("SimulaLexer.scanComment: COMMENT TOKEN: " + commentToken);
    		printQueue("SimulaLexer.scanComment: ");
    		Util.println("SimulaLexer.scanComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
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
    /// Pre-Condition: reader.getCurrent() is first character of construct. I.e. '%' or "--"
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
	/// </pre>
	/// @return a Comment Token
	private static boolean TESTING_SCAN_END_LINE = false;//true;
	private LexToken scanCommentToEndOfLine() {
        while (true) {
        	reader.getNext();
        	if(reader.getCurrent() == SourceTextReader.EOF_MARK) {
        		if(TESTING_SCAN_END_LINE) Util.println("LexToken.scanCommentToEndOfLine: GOT EOF_MARK");
        		if(Option.LEX_VERIFY) {
        			if(reader.nextPos() == tokenStartPos) Util.IERR("IMPOSSIBLE");
        		}
//        		return newCommentToken(KeyWord.COMMENT_TEXT);
        		return newCommentToken(KeyWord.COMMENT_TEXT);
        	}
        	if(reader.getCurrent() == '\n' || (reader.getCurrent() == '\r' && reader.nextCharIs('\n'))) {
        		if(TESTING_SCAN_END_LINE) Util.println("LexToken.scanCommentToEndOfLine: GOT NEWLINE(LF or CRLF)");
        		reader.pushBackPos(1);
        		if(Option.LEX_VERIFY) {
        			if(reader.nextPos() == tokenStartPos) Util.IERR("IMPOSSIBLE");
        		}
//        		return newCommentToken(KeyWord.COMMENT_TEXT);
        		return newCommentToken(KeyWord.COMMENT_TEXT);
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
    /// Pre-Condition: reader.getCurrent() is first character of construct.
    ///                reader.nextPos() points to second character of construct.
    /// End-Condition: reader.getCurrent() is last character of construct.
    ///                reader.nextPos() points to first character after construct.
    ///                getNext will return first character after construct.
    /// </pre>
    /// @return next Token
	private static boolean TESTING_SCAN_END = false;
    private LexToken scanEndComment() {
        LexToken endToken = newKeyWordToken(KeyWord.END);
        if(TESTING_SCAN_END) Util.println("LexToken.scanEndComment: endToken="+endToken);
		currentColumn = currentColumn + endToken.length;
		if(TRACE_CURRENT_COLUMN) Util.println("SimulaLexer.scanEndComment(1): currentColumn="+currentColumn);
    	tokenStartPos = reader.nextPos();
        
//        if (CoreGlobal.TRACE_LEXER) Util.TRACE("scanEndComment, " + edcurrent());
        int nPhrase = 0; // Number of comment phrases
        
        LOOP:while (true) {
        	if(reader.getCurrent() == SourceTextReader.EOF_MARK) {
        		if(TESTING_SCAN_END) Util.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT EOF_MARK: reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
                int lng = reader.nextPos() - tokenStartPos - 1;
//    			int lng = reader.prevLineLength() - tokenStartPos - 1;
//    			Util.IERR("DETTE MÅ TESTES");
                if(lng > 0) {
                	nPhrase++;
//                    if(reader.nextPos() != textEndOffset) Util.IERR("IMPOSSIBLE");
                    LexToken lexToken = newCommentToken(KeyWord.COMMENT_TEXT);
                    if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                    tokenQueueAdd("scanEndComment-EOF_TEXT", lexToken);
                }
                tokenQueueAdd("scanEndComment-EOF_TEXT", newKeyWordToken(KeyWord.EOF));
        	    currentColumn = 0;
        	    if(TRACE_CURRENT_COLUMN) Util.println("SimulaLexer.scanEndComment(2): currentColumn="+currentColumn);
        		break LOOP;
        	}
        	
        	reader.getNext();
        	if(TESTING_SCAN_END) Util.println("LexToken.scanEndComment: reader.getCurrent()="+reader.getCurrent()+":'"+Comn.printable(""+(char)reader.getCurrent())+"'");
    		
    		if (reader.getCurrent() == '\n') {
            	if(TESTING_SCAN_END) Util.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT NEWLINE(LF): reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
                
        		int lng = reader.prevLineLength() - tokenStartPos - 1;
                if(lng > 0) {
                	nPhrase++;
                    reader.pushBackPos(1);
                    if(Option.LEX_VERIFY) {
	                    if(lng != (reader.nextPos() - tokenStartPos)) Util.IERR("IMPOSSIBLE: lng=" + lng +", reader.nextPos() - tokenStartPos: " + (reader.nextPos() - tokenStartPos));
//	                    if(sourceText.charAt(reader.nextPos()) != '\n') Util.IERR("IMPOSSIBLE");
                    }
                    LexToken lexToken = newCommentToken(KeyWord.COMMENT_TEXT);
                    if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                    tokenQueueAdd("scanEndComment-NEWLINE", lexToken);
        			
                    reader.getNext(); // Reads the first character after the comment. I.e. LF character.
                    if(Option.LEX_VERIFY) {
	                    if(reader.getCurrent() != '\n') Util.IERR("IMPOSSIBLE");
//	                    if(sourceText.charAt(tokenStartPos) != '\n') Util.IERR("IMPOSSIBLE");
                    }
                }
                if(reader.getCurrent() != '\n') Util.IERR("IMPOSSIBLE");
        	    tokenQueueAdd("scanEndComment - NEWLINE", newNewlineToken());
            } else if (reader.getCurrent() == ';') {
            	if(TESTING_SCAN_END) Util.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT SEMICOLON: reader.nextPos()="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
                int lng = reader.nextPos() - tokenStartPos - 1;
                if(lng > 0) {
                	nPhrase++;
                    reader.pushBackPos(1);
//                    if(sourceText.charAt(reader.nextPos()) != ';') Util.IERR("IMPOSSIBLE");
                    
                    LexToken lexToken = newCommentToken(KeyWord.COMMENT_TEXT);
                    if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                    tokenQueueAdd("scanEndComment-SEMICOLON", lexToken);
        			
                    reader.getNext(); // Leser første tegn etter comment, altså et SEMICOLON tegn
                    if(Option.LEX_VERIFY) {
	                    if(reader.getCurrent() != ';') Util.IERR("IMPOSSIBLE");
//	                    if(sourceText.charAt(tokenStartPos) != ';') Util.IERR("IMPOSSIBLE");
                    }
                }
                tokenQueueAdd("scanEndComment-SEMICOLON", newKeyWordToken(KeyWord.SEMICOLON));
                break LOOP;
            } else if (Character.isLetter(reader.getCurrent())) {
                String name = scanName();
                if(TESTING_SCAN_END) Util.println("\n\nLexToken.scanEndComment: GOT name="+name);
                if (Util.equals(name, "end") || Util.equals(name, "else")
                        || Util.equals(name, "when") || Util.equals(name, "otherwise")) {
                	
//                	reader.nextPos() = reader.nextPos() - name.length();
//                	EOF_SEEN=false;
//                	reader.getCurrent() = 0;
                	reader.pushBackPos(name.length());
                	
                    if(reader.nextPos() > tokenStartPos) {
                        LexToken lexToken = newCommentToken(KeyWord.COMMENT_TEXT);
                        if(nPhrase > 1) Util.warning(simBuilder, lexToken, "END comment spans multiple lines");
                        tokenQueueAdd("scanEndComment-NAME", lexToken);
                    }
//                    this.snapShot("GOT name="+name);
//                    Util.println("LexToken.scanEndComment: GOT name="+name+" break LOOP\n\n");
                    break LOOP;
                }
            } else {
            	if(TESTING_SCAN_END) Util.println("LexToken.scanEndComment: GOT OTHER="+reader.getCurrent()+":'"+Comn.printable(""+(char)reader.getCurrent())+"'");
            }
        }

        if(TESTING_SCAN_END) {
	        Util.println("SimulaLexer.scanEndComment: endToken: " + endToken);
	        Util.println("SimulaLexer.scanEndComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
	        Util.println("SimulaLexer.scanEndComment: END TOKEN: " + endToken);
	        printQueue("SimulaLexer.scanEndComment: ");
	        Util.println("SimulaLexer.scanEndComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
        }
        
        return endToken;
    }
    
	private void tokenQueueAdd(String debugName, LexToken lexToken) {
//		if(lexToken.length == 0) return;
//		Util.println("SimulaLexer.tokenQueueAdd: "+debugName+" "+lexToken);
	    tokenQueue.add(lexToken);
//		Util.println("SimulaLexer.tokenQueueAdd: "+debugName+" currentColumn = "+currentColumn+" + "+lexToken.length + " = "+(currentColumn + lexToken.length));
	    
	    if(lexToken.keyWord != KeyWord.NEWLINE)
	    	currentColumn = currentColumn + lexToken.length;
		
		if(TRACE_CURRENT_COLUMN) Util.println("SimulaLexer.tokenQueueAdd: currentColumn="+currentColumn);
    	tokenStartPos = reader.nextPos();
	    currentLexerToken = lexToken;
	}
    
    private void printQueue(String title) {
    	Util.println("================================= BEGIN TOKEN-QUEUE " +title + " =================================");
    	for(LexToken lexToken:tokenQueue) {
        	Util.println("SimulaLexer.printQueue: lexToken="+lexToken);
    	}
    	Util.println("================================= ENDOF TOKEN-QUEUE " +title + " =================================");
    }

        

    //********************************************************************************
    //**	                                                                 UTILITIES 
    //********************************************************************************

    /// Create a new keyWord Token
    /// @param keyWord the KeyWord
    /// @return the newly created Token
	private LexToken newKeyWordToken(final int keyWord) {
//		Util.println("SimulaLexer.newKeyWordToken: "+KeyWord.edit(keyWord)+", currentColumn="+currentColumn+", reader.nextPos="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
		return new KeyWordToken(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, keyWord, this);
	}

    /// Create a new keyWord Token
    /// @param keyWord the KeyWord
    /// @return the newly created Token
	private LexToken newCommentToken(final int keyWord) {
//		Util.println("SimulaLexer.newKeyWordToken: "+KeyWord.edit(keyWord)+", currentColumn="+currentColumn+", reader.nextPos="+reader.nextPos()+", tokenStartPos="+tokenStartPos);
		return new CommentToken(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, this);
	}
	
//	private String edTokenText(List<String> sourceLines, int lineNumber, int column, int length) {
////		int startOfLine = getLineStartPos(lineNumber);
////		int tokenStartPos = startOfLine + column;
////		CharSequence txt = sourceText.subSequence(tokenStartPos, tokenStartPos + length);
////		String debugText=txt.toString();
////		return debugText;
//		try {
//			String sourceLine = sourceLines.get(lineNumber);
//			Util.println("LexToken.edTokenText: Line "+lineNumber+": |"+sourceLine+"| column="+column+", length="+length);
//			String txt = sourceLine.substring(column, column + length);
//			Util.println("LexToken.edTokenText: Line "+lineNumber+": |"+sourceLine+"| column="+column+", length="+length+" ==> |" + txt +'|');
//		return txt;
//		} catch(Exception e) {
//			return "";
//		}
//	}
//
//	private LexToken newKeyWordToken(final int tokenStartPos, final int length, final int keyWord) {
//		return new KeyWordToken(reader.currentLineNumber(), sourceLines, currentColumn, length, keyWord, this);
//	}
//	
//	/// SKAL FJERNES
//	private LexToken newKeyWordToken(final int tokenStartPos, final int keyWord) {
//		return new KeyWordToken(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, keyWord, this);
//	}

    /// Create a new keyWord Token
    /// @param keyWord the KeyWord
    /// @return the newly created Token
	private LexToken newNewlineToken() {
//		LexToken newlineToken = newKeyWordToken(KeyWord.NEWLINE);
		int line = reader.currentLineNumber() - 1;
		int lngt = reader.prevLineLength() - 1;
//		lngt = currentColumn;
    	Util.println("SimulaLexer.newNewlineToken: currentColumn="+currentColumn + "  Line|" + Comn.printable(sourceLines.get(line)) + '|');
		LexToken newlineToken = new KeyWordToken(line, sourceLines, lngt, 1, KeyWord.NEWLINE, this);
//    	currentLineNumber++;
    	currentColumn = 0;
    	if(TRACE_CURRENT_COLUMN) Util.println("SimulaLexer.newNewlineToken: currentColumn="+currentColumn);
       	if(Option.LEX_VERIFY) {
       		String text = newlineToken.getText();
       		if(! text.equals("\n")) {
       			Util.IERR("SimulaLexer.newNewlineToken: LEX_VERIFY Failed: Illegal content: |" + Comn.printable(text) + '|');
       		}
       	}
        return newlineToken;
    }
	  
    /// Create a new Integer Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newIntegerToken(final long value) {
		return new IntegerConst(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, value, this);
	}
	  
    /// Create a new Character Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newCharacterToken(final char value) {
		return new CharacterConst(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, value, this);
	}
	  
    /// Create a new Simple String Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newSimpleStringToken(final String value) {
		return new SimpleString(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, value, this);
	}

    /// Create a new Real Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newRealToken(final float value) {
		return new RealConst(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, value, this);
	}

    /// Create a new Long Real Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newLongRealToken(final double value) {
		return new LongRealConst(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, value, this);
	}
	
    /// Only when Option LEX_VERIFY = true
    public void verifyToken(LexToken lexToken, int lineNumber, int column, int length) {
//		int line = getLineStartPos(lineNumber);
//		int check = line + column + length;
    	String sourceLine = sourceLines.get(lineNumber);
		int check = column + length;
		if(length == 0) {
			Util.IERR("LEX_VERIFY FAILED: Token length is Zero: " + lexToken);
//			System.err.println("LEX_VERIFY FAILED: Token length is Zero: " + lexToken);
		} else
			if(check > (sourceLine.length()) || length == 0) {
//				System.err.println("LEX_VERIFY FAILED: " + lexToken);
				
//			System.err.println("LEX_VERIFY FAILED: lineStartPos("+lineNumber+")=" + line + ", column=" + column + ", length=" + length
//					+ "  SUM=" + check + " > lexer.lineEndOffset=" + lineEndOffset
//					+ "\n" + " ".repeat(33) + "Remaining SourceText("+ line +", ...)=\"" + sourceText.subSequence(line, lineEndOffset) + '"');
				System.err.println("LEX_VERIFY FAILED: lineNumber=" + lineNumber + ", column=" + column + ", length=" + length
						+ ", check=" + check + " |" + Comn.printable(sourceLine) + '|');
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
    	return new Identifier(reader.currentLineNumber(), sourceLines, currentColumn, reader.nextPos() - tokenStartPos, this);
    }

	/// Utility: Edit reader.getCurrent() character.
	/// @return edited reader.getCurrent() character
	private String edcurrent() {
		if (reader.getCurrent() < 32)
			return ("Current code=" + reader.getCurrent());
		return ("Current='" + (char) reader.getCurrent() + "' value=" + reader.getCurrent());
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
//	private boolean isWhiteSpace(final int c) {
//		switch(c) {
//		    case '\n':	/* NL (LF) */
//		    case 32:    /* SPACE */
//		    case '\b':	/* BS */
//		    case '\t':	/* HT */
//		    case 11:	/* VT */
//		    case '\f':	/* FF */
//		    case '\r':	/* CR */
//			         return(true);
//		    default: return(false);
//		}  
//	}


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
    	return edChar((char) reader.getCurrent());
    }

//    /// Debug utility
//    public String edNext() {
//   		if(reader.nextPos() >= textEndOffset) return "EOF_MARK";
//    	char curChar = sourceText.charAt(reader.nextPos());
//    	return edChar(curChar);
//    }
//
//    /// Debug utility
//    public void snapShot(String title) {
//    	int beg = Math.max(0, reader.nextPos() - 50); beg = beg - beg%10;
//    	int end = Math.min(beg + 100, textEndOffset);
//    	CharSequence text = sourceText.subSequence(beg, end);
//    	Util.println("SimulaLexer.snapShot: beg: " + beg + ", end: " + end);
//    	Util.println("############################### LEXER SNAPSHOT["+beg+':'+end+") - " + title + " ######################################");
//    	Util.println("sourceText:        0         10        20        30        40        50        60        70        80        90");
//    	Util.println("sourceText:        0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
//    	Util.println("sourceText:        " + (""+text).replace("\t", "¤").replace("\r", "¤").replace("\n", "¤"));
//    	Util.println("sourceText(esc):   " + (""+text).replace("\t", "\\t").replace("\r", "\\r").replace("\n", "\\n"));
//    	Util.println("textEndOffset:     " + textEndOffset + '(' + (textEndOffset-beg) + ')');
//    	Util.println("currentLexerToken: " + currentLexerToken);
//    	Util.println("reader.nextPos():           " + reader.nextPos() + '(' + (reader.nextPos()-beg) + ")  With value: " + edNext());
////    	Util.println("tokenStartOffset:  " + tokenStartOffset);
////    	Util.println("tokenEndOffset:    " + tokenEndOffset);
//    	Util.println("currentColumn:     " + currentColumn);
////    	Util.println("currentLength:     " + currentLength);
//    	Util.println("tokenStartPos:     " + tokenStartPos + '(' + (tokenStartPos-beg) + ')');
//    	Util.println("currentLineNumber: " + currentLineNumber);
//    	Util.println("tokenQueue:        " + tokenQueue);
//    	printLines();
//    	Util.println("############################### END LEXER SNAPSHOT - " + title + " ######################################");
//    }
//    
//    /// Debug utility
//    public void printState(String title) {
//    	Util.println("==== LEXER STATE: " + title + "  " + currentLexerToken
//    			+ "reader.nextPos()=" + reader.nextPos()+",currentColumn=" + currentColumn+", currentLineNumber"+currentLineNumber);
//    }
//    
//    /// Debug utility
//    public void printLines() {
//        int nLines = lineStartPos.size();
//        for(int i=0;i<nLines;i++) {
//        	int beg = getLineStartPos(i);
////        	Util.println("Line " + i + ": starts " + getLineStartPos(i));
//        	int end = ((i+1) < nLines)?getLineStartPos(i+1) : textEndOffset;
////        	Util.println("Line " + i + ": start: " + getLineStartPos(i) + ", end: " + end);
//        	CharSequence text = sourceText.subSequence(beg, end);
//        	String line = "Line " + i +"["+beg+':'+end+"): ";
//        	while(line.length() < 19) line = line + " ";
//        	Util.println(line + '|' + (""+text).replace("\t", "¤").replace("\r", "¤").replace("\n", "¤") + '|');
//        }
//    }

}
