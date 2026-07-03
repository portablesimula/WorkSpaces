/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.builder;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.token.KeyWordToken;
import simula.token.LexToken;
import simula.token.LongRealConst;
import simula.token.RealConst;
import simula.token.SimpleString;
import simula.token.WhiteSpaceToken;
import simula.token.IdentifierToken;
import simula.token.IntegerConst;

/// The Simula Scanner.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/parsing/SimulaScanner.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class SimulaLexer {
	
	private SimulaBuilder simulaBuilder;
    private CharSequence sourceText;
    private int textEndOffset;
    private int currentPosition;
    private int currentLineNumber;
    private int currentColumn;
    private int tokenStartPos; // Used to calculate length
	
    /// Only when Option LEX_VERIFY = true
    public void verifyToken(int lineNumber, int column, int length) {
		int line = getLineStartPos(lineNumber);
		int check = line + column + length;
		if(check > textEndOffset) {
			System.err.println("NEW LexToken: LEX_VERIFY FAILED: lineStartPos("+lineNumber+")=" + line + ", column=" + column + ", length=" + length
					+ "  SUM=" + check + " > lexer.textEndOffset=" + textEndOffset
					+ "\n" + " ".repeat(33) + "Remaining SourceText("+ line +", ...)=\"" + sourceText.subSequence(line, textEndOffset) + '"');
			Util.IERR("LEX_VERIFY FAILED: ");
		}
    }
    
    private LexToken prevParserToken;
    private LexToken prevLexerToken;
    private LexToken currentLexerToken;
    
    private List<Integer> lineStartPos = new ArrayList<>();
    public int getLineStartPos(int lineNumber) {
    	int pos = lineStartPos.get(lineNumber);
       	if(Option.LEX_VERIFY) {
//           	IO.println("SimulaLexer.getLineStartPos: "+lineNumber+": "+pos+" TABLE="+lineStartPos);
       		if(currentPosition > textEndOffset) Util.IERR("IMPOSSIBLE");
       	}
       	return pos;
    }
    public void addLineStartPos() {
       	lineStartPos.add(currentPosition);    	
       	if(Option.LEX_VERIFY) {
//           	IO.println("SimulaLexer.addLineStartPos: "+(lineStartPos.size()-1)+": "+currentPosition+" TABLE="+lineStartPos);
       		if(currentPosition > textEndOffset) Util.IERR("IMPOSSIBLE");
           	if(lineStartPos.size() != (currentLineNumber+1)) {
        		IO.println("SimulaLexer.scanbasic: currentLineNumber: " + currentLineNumber + ", lineStartPos.size(): " + lineStartPos.size());
           		Util.IERR("IMPOSSIBLE");
           	}
       	}
    }

    /// Debug utility
    public String edChar(char c) {
    	String curval = "" + (int)c + ':' + c;
    	curval = curval.replace("\r", "\\r").replace("\n", "\\n").replace(" ", "_");
    	return curval;
    }

    /// Debug utility
    public String edCurrent() {
    	char curChar = sourceText.charAt(currentPosition);
    	return edChar(curChar);
    }

    /// Debug utility
    public void snapShot(String title) {
    	IO.println("############################### LEXER SNAPSHOT - " + title + " ######################################");
    	IO.println("sourceText:        0         10        20        30        40        50        60        70        80        90");
    	IO.println("sourceText:        0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
//    	IO.println("sourceText:        " + (""+sourceText).replace("\r", "\\r").replace("\n", "\\n"));
    	IO.println("sourceText:        " + (""+sourceText).replace("\r", "¤").replace("\n", "¤"));
    	IO.println("textEndOffset:     " + textEndOffset);
    	IO.println("currentLexerToken: " + currentLexerToken);
    	IO.println("currentPosition:   " + currentPosition + "  With value: " + edCurrent());
//    	IO.println("tokenStartOffset:  " + tokenStartOffset);
//    	IO.println("tokenEndOffset:    " + tokenEndOffset);
    	IO.println("currentColumn:     " + currentColumn);
//    	IO.println("currentLength:     " + currentLength);
    	IO.println("tokenStartPos:     " + tokenStartPos);
    	IO.println("currentLineNumber: " + currentLineNumber);
    	IO.println("tokenQueue:        " + tokenQueue);
    	IO.println("############################### END LEXER SNAPSHOT - " + title + " ######################################");
    }
    
    /// Debug utility
    public void printState(String title) {
    	IO.println("==== LEXER STATE: " + title + "  " + currentLexerToken
    			+ "currentPosition=" + currentPosition+",currentColumn=" + currentColumn+", currentLineNumber"+currentLineNumber);
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

//    /// The pushBack stack
//    private Stack<Character> puchBackStack=new Stack<Character>();

    /// The Token queue. The method nextToken will pick Tokens from the queue first.
    private LinkedList<LexToken> tokenQueue=new LinkedList<LexToken>();

//    /// The current source file reader;
//    SourceFileReader sourceFileReader;
    
    /// The selector array.
    public static boolean selector[]=new boolean[256];

	/// The depth of nested parentheses (round brackets).
	/// 
	/// NOTE: An initial "-" in array upper bound may follow directly after : (cf. 1.3).
	/// 
	/// The scanner will treat ":-" within parentheses as two
	/// separate symbols ":" and "-" thus solving this ambiguity in the syntax.
	/// 
	/// This variable is used to cover such situations.
	private int pardepth = 0;

	/// Constructs a new SimulaScanner that produces Items scanned from the specified source.
	/// @param reader The character source to scan
	/// @param editorMode true: delivers tokens to the SimulaEditor
//	public SimulaScanner(final Reader reader,final boolean editorMode) {
//		this.sourceFileReader=new SourceFileReader(reader);
//		this.editorMode=editorMode;
//		Global.sourceLineNumber=1;
//	}
	public SimulaLexer(final SimulaBuilder simulaBuilder, final CharSequence sourceText) {
		this.simulaBuilder = simulaBuilder;
		this.sourceText = sourceText;
        
       	this.sourceText = ((String) this.sourceText).replace("\r\n", "\n");
       	
		this.textEndOffset = this.sourceText.length();
		currentPosition = 0;
		currentLineNumber = 0;
       	addLineStartPos();
		Global.sourceLineNumber=1;
		nextToken();
	}


	public LexToken getPrevLexerToken() {
        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getPrevLexerToken: "+prevLexerToken);
        return prevLexerToken;
    }

	public LexToken getPrevParserToken() {
        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getPrevParserToken: "+prevParserToken);
        return prevParserToken;
    }

	public LexToken getCurrentLexerToken() {
        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getCurrentLexerToken: "+currentLexerToken);
        return currentLexerToken;
    }

    //********************************************************************************
    //**	                                                                 nextToken 
    //********************************************************************************
	public LexToken nextToken() {
    	prevLexerToken = currentLexerToken;
    	if(currentLexerToken != null) {
    		if(prevLexerToken.keyWord != KeyWord.NEWLINE)
    			currentColumn = currentColumn + prevLexerToken.length;
    		if(currentLexerToken.isParserToken()) prevParserToken = currentLexerToken;
    	}
    	tokenStartPos = currentPosition;
    	
    	LexToken token;
		if(tokenQueue.size()>0) { 
		    token=tokenQueue.remove();
		    IO.println("POP LexToken: " + token);
			IO.println("SimulaLexer.nextToken: currentColumn="+currentColumn+", currentPosition=" + currentPosition + ", tokenStartPos="+tokenStartPos);
			currentColumn = token.column;
		} else token = scanToken();

		if (token != null) {
			if (token.keyWord == KeyWord.AND) {
				LexToken maybeThen = scanToken();
				if (maybeThen.keyWord == KeyWord.THEN) {
					LexToken andThen=newKeyWordToken(KeyWord.AND_THEN);
//					andThen.setText(token.getText()+maybeThen.getText());
					Util.IERR("DETTE MÅ RETTES");
					return(andThen);
				}
				tokenQueue.add(maybeThen);
			} else if (token.keyWord == KeyWord.OR) {
				LexToken maybeElse = scanToken();
				if (maybeElse.keyWord == KeyWord.ELSE) {
					LexToken orElse=newKeyWordToken(KeyWord.OR_ELSE);
//					orElse.setText(token.getText()+maybeElse.getText());
					Util.IERR("DETTE MÅ RETTES");
					return(orElse);
				}
				tokenQueue.add(maybeElse);
			}
		}
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("Item.nextToken, " + edcurrent());
		currentLexerToken = token;
	    IO.println("GOT LexToken: " + token);
		IO.println("SimulaLexer.nextToken: currentColumn="+currentColumn+", currentPosition=" + currentPosition + ", tokenStartPos="+tokenStartPos);
		
		simulaBuilder.tokenList.add(token);
		
		return (token);
	}
	
    //********************************************************************************
    //**	                                                                 scanToken 
    //********************************************************************************
	/// Scan and return a Token.
	/// <pre>
    /// FØR:End-Condition: current is last character of construct
    /// FØR:               getNext will return first character after construct
    /// 
    /// Beg-Condition: current is first character of construct
    /// End-Condition: current is first character after construct
    ///                getNext will return first thereafter (second character after construct)
    /// </pre>
	/// @return next Token
    private LexToken scanToken() {
//		snapShot("SimulaLexer.scanToken: BEGIN");
//		IO.println("\n\nSimulaLexer.scanToken: BEGIN currentPosition: " + currentPosition + " with value: " + edCurrent());
    	LexToken token = scanBasic();    
//		snapShot("SimulaLexer.scanToken: END");
//		IO.println("SimulaLexer.scanToken: ENDOF currentPosition: " + currentPosition + " with value: " + edCurrent());
		return token;
    }
    
    //********************************************************************************
    //**	                                                                 scanBasic 
    //********************************************************************************
    /// Scan basic Token
    /// @return next Token
    private LexToken scanBasic() {
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("SimulaScanner.scanBasic, "+edcurrent());
    	while(true)	{
    		LexToken.lineNumberBeforeScanBasic = Global.sourceLineNumber;

    		if(Character.isLetter(getNext())) return(scanIdentifier());

    		switch(current) {
    			case EOF_MARK: return(null);
    		    case '=':
		            if(getNext() == '=')   return(newKeyWordToken(KeyWord.EQR));
		            if(current == '/')
		            if(getNext() == '=')   return(newKeyWordToken(KeyWord.NER));
		            else Util.generalError("Illegal character combination ="+(char)current);
		            pushBackPos(1);        return(newKeyWordToken(KeyWord.EQ));
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
		            if(current == '-' && pardepth == 0) return(newKeyWordToken(KeyWord.ASSIGNREF));
		            pushBackPos(1);                  return(newKeyWordToken(KeyWord.COLON));
	            case ';':	pardepth=0; return(newKeyWordToken(KeyWord.SEMICOLON));
	            case '(':	pardepth++; return(newKeyWordToken(KeyWord.BEGPAR));
	            case ')':	pardepth--; return(newKeyWordToken(KeyWord.ENDPAR));
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
	    	  
//	            case '\r': if(getNext()=='\n') return (scanNewLine());
//				    pushBackPos(1); return (scanWhiteSpace());
		    	  
	            case '\n': return(scanNewLine());            	

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
    //**	                                                            scanNewLine 
    //********************************************************************************
    /// Scan and return a NewLine Token.
    /// <pre>
    /// FØR:End-Condition: current is last character of construct
    /// FØR:               getNext will return first character after construct
    /// 
    /// Beg-Condition: current is first character of construct
    /// End-Condition: current is first character after construct
    ///                getNext will return first thereafter (second character after construct)
    /// </pre>
    /// @return next Token
	private LexToken scanNewLine() {
		LexToken newlineTokem = newKeyWordToken(KeyWord.NEWLINE);
    	currentLineNumber++;
    	currentColumn = 0;
       	addLineStartPos();
        return newlineTokem;
    }

    
    //********************************************************************************
    //**	                                                            scanWhiteSpace 
    //********************************************************************************
    /// Scan and return a WhiteSpace Token.
    /// <pre>
    /// FØR:End-Condition: current is last character of construct
    /// FØR:               getNext will return first character after construct
    /// 
    /// Beg-Condition: current is first character of construct
    /// End-Condition: current is first character after construct
    ///                getNext will return first thereafter (second character after construct)
    /// </pre>
    /// @return next Token
	private LexToken scanWhiteSpace() {
//		snapShot("SimulaLexer.scanWhiteSpace: BEGIN");
//		IO.println("\n\nSimulaLexer.scanWhiteSpace: BEGIN currentPosition: " + currentPosition + " with value: " + edCurrent());
    	LOOP:while(true) {
    		getNext();
//    		IO.println("SimulaLexer.scanWhiteSpace: currentColumn: " + currentColumn);
    		if(current == '\n') break LOOP;
    		if(Character.isWhitespace(current)) continue LOOP;
    		break LOOP;
    	}
    	pushBackPos(1);
//		snapShot("SimulaLexer.scanWhiteSpace: END");
//		IO.println("SimulaLexer.scanWhiteSpace: END Current: " + edCurrent());
    	return(newWhiteSpaceToken());
    }

    
    //********************************************************************************
    //**	                                                            scanIdentifier 
    //********************************************************************************
    /// Scan and return an identifier Token.
    /// <pre>
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return next Token
	private LexToken scanIdentifier() {
		String name=scanName();
	    if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanIdentifier: name=\""+name+"\"");
	    String ident=(Option.CaseSensitive)?name:name.toLowerCase();
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
    		Util.generalError("Integer number out of range: "+result);
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
    		Util.generalError("Illegal number: "+result);
//    		return newRealToken(KeyWord.REALKONST,null));
    		return newRealToken(0);
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
//    		if(doubleAmpersand) return(newKeyWordToken(KeyWord.REALKONST,Double.parseDouble(result)));
//    		return(newKeyWordToken(KeyWord.REALKONST,Float.parseFloat(result)));
    		if(doubleAmpersand) return newRealToken(Double.parseDouble(result));
    		return newRealToken(Float.parseFloat(result));
    	} catch(NumberFormatException e) {
    		Util.generalError("Illegal number: "+result);
//    		return(newKeyWordToken(KeyWord.REALKONST,null));
    		return newRealToken(0);
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
    		IO.println("SimulaLexer.scanName: GOT " + current);
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
    	char result=0;
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanCharacterConstant, "+edcurrent());
    	Util.ASSERT((char)(current)=='\'',"Expecting a character quote '");
    	if((isPrintable(getNext())) && current != '!') {
    		result=(char)current; getNext();
    	} else if(current == '!') {
    		result=(char)scanPossibleIsoCode(); getNext();
    	} else Util.generalError("Illegal character constant. "+edcurrent());
    	
    	if(current != '\'') {
    		Util.generalError("Character constant is not terminated. "+edcurrent());
    		pushBackPos(1);
    	}
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("END scanCharacterConstant, result='"+result+"', "+edcurrent());
//    	return(newKeyWordToken(KeyWord.CHARACTERKONST,Character.valueOf(result)));
    	return newCharacterToken(result);
    }  
    
    
    //********************************************************************************
    //**	                                                          scanTextConstant
    //********************************************************************************
    /// Scan and deliver a Text constant.
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
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return next Token
    private LexToken scanTextConstant() {
    	if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanTextConstant, "+edcurrent());
    	StringBuilder accumulatedTextConstant=new StringBuilder();
    	LOOP:while(true) {
    		int firstLine=Global.sourceLineNumber;
        	int lastLine=firstLine;
    		// Scan simple-string:
    		while(getNext() != '"') {
    			if(current=='!') {
    				int code=scanPossibleIsoCode();
    				accumulatedTextConstant.append((char)code);
    			}
    			else if(current == EOF_MARK) {
    				Util.generalError("Text constant is not terminated.");
    				String result=accumulatedTextConstant.toString(); accumulatedTextConstant=null;
    				if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanTextConstant(1): Result=\""+result+"\", "+edcurrent());
    				tokenQueue.add(newTextToken(result));
    				break LOOP;
    			} else accumulatedTextConstant.append((char)current);
    		}
    		tokenQueue.add(newKeyWordToken(KeyWord.STRING));
    		lastLine=Global.sourceLineNumber;
    		if(getNext() == '"') {
    			accumulatedTextConstant.append('"');
    			lastLine=Global.sourceLineNumber;
    		} else {
    			// Skip string-separator
    			while(currentIsStringSeparator()) getNext();
    			if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanTextConstant(2): "+edcurrent());
    			if(current!='"') {
    				pushBackPos(1);
    				String result=accumulatedTextConstant.toString(); accumulatedTextConstant=null;
    				if(Option.internal.TRACE_LEXER > 0) Util.TRACE("scanTextConstant(2): Result=\""+result+"\", "+edcurrent());
    				if(firstLine<lastLine)
    					Util.warning("Illegal Text constant. Simple string span mutiple source lines ("+firstLine+':'+lastLine+"). See Simula Standard 1.6");
    				tokenQueue.add(newTextToken(result));
    				break LOOP;
    			}
    		}
    	}
    	LexToken result=tokenQueue.remove();
    	return(result);
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
    ///        
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return true if current is a string separator
    private boolean currentIsStringSeparator() {
    	if(current=='!') {
    		LexToken cc=scanComment();
    		tokenQueue.add(cc);
    		current=' '; return(true);
    	} else if(Character.isLetter((char)current)) {
    		String name=scanName();
    		if(name.equalsIgnoreCase("COMMENT")) {
        		LexToken cc=scanComment();
        		tokenQueue.add(cc);
    			current=' '; return(true);
    		} else pushBackPos(name.length());
    		return(false);
		}
    	return(isWhiteSpace(current));
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
						Util.warning("ISO-Code " + value + " is out of range (0:255)"
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
	/// Scan a Comment.
	/// <pre>
	/// Reference-Syntax:
	/// 
	///       comment = COMMENT { any character except semicolon } ;
	///       
	///       
	/// End-Condition: current is last character of construct 
	///                getNext will return first character after construct
	/// </pre>
	/// @return a Comment Token
	private LexToken scanComment() {
		StringBuilder skipped = new StringBuilder();
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("BEGIN scanComment, " + edcurrent());
		while ((getNext() != ';') && current != EOF_MARK)
			skipped.append((char) current);
		skipped.append((char) current);
		if (current == ';')
			current = ' '; // getNext();
		else {
			Util.generalError("Comment is not terminated with ';'.");
			pushBackPos(1);
		}
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("END scanComment: " + edcurrent() + "  skipped=\"" + skipped + '"');
		if (Option.internal.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
		return (newKeyWordToken(KeyWord.COMMENT_KEY));
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
	///       
	/// End-Condition: current is last character of construct 
	///                getNext will return first character after construct
	/// </pre>
	/// @return a Comment Token
	private LexToken scanCommentToEndOfLine() {
		StringBuilder skipped = new StringBuilder();
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("BEGIN scanCommentToEndOfLine, " + edcurrent());
		while ((getNext() != '\n') && current != EOF_MARK)
			skipped.append((char) current);
		skipped.append((char) current);
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("END scanCommentToEndOfLine: " + edcurrent() + "  skipped=\"" + skipped + '"');
		if (Option.internal.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
		return (newKeyWordToken(KeyWord.COMMENT_KEY));
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
	///          END { any sequence of printable characters not containing END, ELSE, WHEN, OTHERWISE or ; }
	///          
	///       is equivalent to:
	///       
	///          END
	/// 
	/// 
	/// End-Condition: current is last character of construct
	///                getNext will return first character after construct
	/// </pre>
	/// @return next Token
	private LexToken OLD_scanEndComment() {
		//Util.println("SimulaScanner.scanEndComment");
		tokenQueue.add(newKeyWordToken(KeyWord.END));				   
		StringBuilder skipped = new StringBuilder();
		if (Option.internal.TRACE_LEXER > 0) Util.TRACE("scanEndComment, " + edcurrent());
		int firstLine = Global.sourceLineNumber;
		int lastLine = firstLine;
   LOOP:while (getNext() != EOF_MARK) {
//			if(prevChar=='\n') {
//				Util.warning("END-Comment span mutiple source lines");
//			}
			if (current == ';') {
				if (Option.internal.TRACE_COMMENTS) Util.TRACE("ENDCOMMENT:\"" + skipped + '"');
				if (firstLine < lastLine && (skipped.length() > 0))
					Util.warning("END-Comment span mutiple source lines");
//				if(accum.length()>0) tokenQueue.add(newKeyWordToken(KeyWord.COMMENT_KEY));
		   		Util.IERR("DETTE MÅ RETTES");
				tokenQueue.add(newKeyWordToken(KeyWord.SEMICOLON)); break LOOP;  
			} else if (Character.isLetter(current)) {
				String name = scanName();
				if (Util.equals(name, "end") || Util.equals(name, "else")
				|| Util.equals(name, "when") || Util.equals(name, "otherwise")) {
					pushBackPos(name.length());
					if (Option.internal.TRACE_COMMENTS) Util.TRACE("END-COMMENT:\"" + skipped + '"');
					if (firstLine < lastLine && (skipped.length() > 0))
						Util.warning("END-Comment span mutiple source lines");
					tokenQueue.add(newKeyWordToken(KeyWord.COMMENT_KEY)); break LOOP;		   
				}
				skipped.append(name); // lastLine=Global.sourceLineNumber;
			} else if (!isWhiteSpace(current)) {
				skipped.append((char) current);
				lastLine = Global.sourceLineNumber;
			}
		}
		
//		if(accum.length()>0) tokenQueue.add(newKeyWordToken(KeyWord.COMMENT_KEY));
   		Util.IERR("DETTE MÅ RETTES");
		if (Option.internal.TRACE_COMMENTS)
			Util.TRACE("ENDCOMMENT:\"" + skipped + '"');
		LexToken res=tokenQueue.remove();
		return(res);
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
    ///          END { any sequence of printable characters not containing END, ELSE, WHEN, OTHERWISE or ; }
    ///
    ///       is equivalent to:
    ///
    ///          END
    ///
    ///
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return next Token
   private static boolean TESTING_SCAN_END = false;//true;
   private static int SEQU = 0;
    private LexToken scanEndComment() {
//        LexToken endToken = new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.END, getTokenDebugText());
        LexToken endToken = newKeyWordToken(KeyWord.END);
        if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: endToken="+endToken);
		currentColumn = currentColumn + endToken.length;
    	tokenStartPos = currentPosition;
        
//        int commentStartPos = currentPosition;
//        IO.println("LexToken.scanEndComment: currentPosition="+currentPosition+", commentStartPos="+commentStartPos);
//        IO.println("LexToken.scanEndComment: currentPosition="+currentPosition+", nextCommentCol="+nextCommentCol);
//        Util.STOP();
        
        if (Global.TRACE_LEXER) Util.TRACE("scanEndComment, " + edcurrent());
        int nPhrase = 0; // Number of comment phrases
        
//        LOOP:while (getNext() != EOF_MARK) {
        LOOP:while (current != EOF_MARK) {
        	getNext();
        	if(current == EOF_MARK) break LOOP;
        	
        	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: current="+current+":'"+(""+(char)current).replace("\r", "\\r").replace("\n", "\\n")+"'");
            if (current == '\n') {
            	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT NEWLINE");
                IO.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT NEWLINE: currentPosition="+currentPosition+", tokenStartPos="+tokenStartPos);
                
                int lng = currentPosition - tokenStartPos - 1;
//                IO.println("LexToken.scanEndComment: AT NEWLINE: lng="+lng);
                if(lng > 0) {
//                    this.snapShot("BEGIN TREAT NEWLINE: lng=" + lng);
                	nPhrase++;
                    pushBackPos(1);
//                    IO.println("LexToken.scanEndComment: AT NEWLINE:TEXT: currentColumn: " + currentColumn);
//                    IO.println("LexToken.scanEndComment: AT NEWLINE:TEXT: currentPosition: " + currentPosition);
//                    IO.println("LexToken.scanEndComment: AT NEWLINE:TEXT: tokenStartPos: " + tokenStartPos);
//                    IO.println("LexToken.scanEndComment: AT NEWLINE:TEXT: currentPosition - tokenStartPos: " + (currentPosition - tokenStartPos));
//                    IO.println("LexToken.scanEndComment: AT NEWLINE:TEXT: commentText: \"" + sourceText.subSequence(tokenStartPos, tokenStartPos + lng)+'"');
//                    IO.println("LexToken.scanEndComment: AT NEWLINE:TEXT: lng=" + lng +", currentPosition - tokenStartPos: " + (currentPosition - tokenStartPos));
                    if(Option.LEX_VERIFY) {
	                    if(lng != (currentPosition - tokenStartPos)) Util.IERR("IMPOSSIBLE: lng=" + lng +", currentPosition - tokenStartPos: " + (currentPosition - tokenStartPos));
	                    if(sourceText.charAt(currentPosition) != '\n') Util.IERR("IMPOSSIBLE");
                    }
                    // Har nådd fram til NEWLINE som avslutter litt comment
                    //      tokenStartPos er nå starten på denne comment
                    //      currentPosition er posisjonen til NEWLINE tegnet altså første tegn etter comment (den ble dekrementert ovenfor)
                    // tokenQueueAdd 
                    // Lager Først token:
            		//      KeyWordToken(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, keyWord, this);
                    // Til slutt:
                    //		currentColumn = currentColumn + token.length;
                	//      tokenStartPos = currentPosition;
                    //
                    // NOTE: currentPosition endres IKKE
                    // NOTE: Ved NEWLINE - currentColumn = 0  Gjøres av 'scanNewLine'
                    
                    tokenQueueAdd("scanEndComment-NEWLINE", newKeyWordToken(tokenStartPos, KeyWord.COMMENT_TEXT));
//                    this.snapShot("");
        			
                    getNext(); // Leser første tegn etter comment, altså et NEWLINE tegn
                    if(Option.LEX_VERIFY) {
	                    if(current != '\n') Util.IERR("IMPOSSIBLE");
	                    if(sourceText.charAt(tokenStartPos) != '\n') Util.IERR("IMPOSSIBLE");
                    }
                }
                if(current != '\n') Util.IERR("IMPOSSIBLE");
        	    tokenQueueAdd("scanEndComment - NEWLINE", scanNewLine());
        	    currentColumn = 0;
            } else if (current == ';') {
                IO.println("\n\n\n\nLexToken.scanEndComment: BEGIN TREAT SEMICOLON: currentPosition="+currentPosition+", tokenStartPos="+tokenStartPos);
                IO.println("LexToken.scanEndComment: AT SEMICOLON: currentPosition="+currentPosition+", tokenStartPos="+tokenStartPos);
                int lng = currentPosition - tokenStartPos - 1;
                IO.println("LexToken.scanEndComment: AT SEMICOLON: lng="+lng);
                if(lng > 0) {
                	nPhrase++;
                    pushBackPos(1);
                    if(sourceText.charAt(currentPosition) != ';') Util.IERR("IMPOSSIBLE");
                    
                    // Har nådd fram til SEMICOLON som avslutter hele comment
                    //      tokenStartPos er nå starten på den siste comment
                    //      currentPosition er posisjonen til SEMICOLON tegnet altså første tegn etter comment (den ble dekrementert ovenfor)
                    // tokenQueueAdd 
                    // Lager Først token:
            		//      KeyWordToken(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, keyWord, this);
                    // Til slutt:
                    //		currentColumn = currentColumn + token.length;
                	//      tokenStartPos = currentPosition;
                    //
                    // NOTE: currentPosition endres IKKE
                    // NOTE: Ved NEWLINE - currentColumn = 0  Gjøres av 'scanNewLine'
                    tokenQueueAdd("scanEndComment-SEMICOLON", newKeyWordToken(tokenStartPos, KeyWord.COMMENT_TEXT));
        			
                    getNext(); // Leser første tegn etter comment, altså et SEMICOLON tegn
                    if(Option.LEX_VERIFY) {
	                    if(current != ';') Util.IERR("IMPOSSIBLE");
	                    if(sourceText.charAt(tokenStartPos) != ';') Util.IERR("IMPOSSIBLE");
                    }
//                    this.snapShot("scanEndComment-SEMICOLON");
//                	Util.STOP();
                }
                tokenQueueAdd("scanEndComment-SEMICOLON", newKeyWordToken(tokenStartPos, KeyWord.SEMICOLON));
                break LOOP;
            } else if (Character.isLetter(current)) {
                IO.println("LexToken.scanEndComment: GOT letter="+current+':'+(char)current);
                String name = scanName();
                if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT name="+name);
                if (Util.equals(name, "end") || Util.equals(name, "else")
                        || Util.equals(name, "when") || Util.equals(name, "otherwise")) {
                	currentPosition = currentPosition - name.length();
                    
                    Util.IERR("DETTE MÅ RETTES");
//                    if(currentPosition > tokenStartOffset) {
//                    	tokenQueueAdd("scanEndComment - NAME", KeyWord.COMMENT_TEXT);
//                    }
                    break LOOP;
                }
            } else {
            	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT OTHER="+current+":'"+(""+(char)current).replace("\r", "\\r").replace("\n", "\\n")+"'");
//                lastLine = nextLineNumber;
            }
        }
////        nPhrase = mayBe_AddCommentToken_ToTokenQueue(nPhrase);
//        IO.println("LexToken.scanEndComment: AT SEMICOLON: currentPosition="+currentPosition+", commentStartPos="+commentStartPos);
//        int lng = currentPosition - commentStartPos - 1;
//        IO.println("LexToken.scanEndComment: AT SEMICOLON: lng="+lng);
//        if(lng > 0) {
//            LexToken token = newKeyWordToken(tokenStartPos, KeyWord.COMMENT_TEXT);
//    	    tokenQueueAdd("scanEndComment - AT END", token);
////        	commentStartPos = commentStartPos + token.length;
//        }

        if(TESTING_SCAN_END) {
	        IO.println("SimulaLexer.scanEndComment: endToken: " + endToken);
	        IO.println("SimulaLexer.scanEndComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
	        IO.println("SimulaLexer.scanEndComment: END TOKEN: " + endToken);
	        printQueue();
	        IO.println("SimulaLexer.mayBe_AddCommentToken_ToTokenQueue: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
        }
        
//        Util.IERR("DETTE MÅ RETTES");
//        tokenStartOffset = endToken.endOffset;
        
        return endToken;
    }
    
	private void tokenQueueAdd(String debugName, LexToken token) {
//		IO.println("SimulaLexer.tokenQueueAdd: "+debugName+" "+token);
	    tokenQueue.add(token);
		currentColumn = currentColumn + token.length;
    	tokenStartPos = currentPosition;
	    currentLexerToken = token;
	}
    
    private void printQueue() {
    	for(LexToken token:tokenQueue) {
        	IO.println("SimulaLexer.printQueue: token="+token);
    	}
    }

        

    //********************************************************************************
    //**	                                                                 UTILITIES 
    //********************************************************************************
	
//	/// The previous character read.
//    private int prevChar;
	
	/// The current character read.
    private int current;
    
    /// Returns next input character.
    /// @return next input character
    private int getNext() {
    	if(currentPosition >= textEndOffset) {
    		if(EOF_SEEN) {
    			Util.IERR("Attempt to scan beyond EOF");
//    			throw new EOFException("");
    			current = EOF_MARK;
    		} else {
    			EOF_SEEN = true; current = EOF_MARK;
    	    	IO.println("SimulaLexer.getNext: EOF_MARK: " + current);
    	    	Thread.dumpStack();
    		}
    	} else {
    		current = sourceText.charAt(currentPosition++);
    	}
//    	IO.println("SimulaLexer.getNext: " + current);
		IO.println("SimulaLexer.getNext(currentPosition: " + (currentPosition - 1)
				+ ") ==> currentPosition: " + currentPosition + " current: " + edChar((char) current)
				+"  CALLED FROM: " + Util.calledFrom(3, 25));
    	return(current);
    }

	private void pushBackPos(int count) {
		currentPosition = currentPosition - count;
		current = sourceText.charAt(currentPosition - 1);
//		IO.println("SimulaLexer.pushBackPos("+ count + "): ==> currentPosition=" + currentPosition + ", current=" + edCurrent());
	}  

    /// Create a new keyWord Token
    /// @param keyWord the KeyWord
    /// @return the newly created Token
	private LexToken newKeyWordToken(final int keyWord) {
		return new KeyWordToken(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, keyWord, this);
	}
	private LexToken newKeyWordToken(final int tokenStartPos, final int keyWord) {
		return new KeyWordToken(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, keyWord, this);
	}
	  
    /// Create a new Integer Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newIntegerToken(final long value) {
		return new IntegerConst(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, value, this);
	}
	  
    /// Create a new Character Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newCharacterToken(final char value) {
		return new IntegerConst(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, value, this);
	}
	  
    /// Create a new Text Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newTextToken(final String value) {
//		return new StringToken(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, value, this);
		return new SimpleString(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, value, this);
	}

    /// Create a new Real Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newRealToken(final double value) {
//		return new RealConst(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, value, this);
		return new LongRealConst(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, value, this);
	}

    /// Create a new WhiteSpace Token
    /// @param keyWord the KeyWord
    /// @param value the value
    /// @return the newly created Token
	private LexToken newWhiteSpaceToken() {
		IO.println("\n\nSimulaLexer.newWhiteSpaceToken: currentColumn="+currentColumn+", currentPosition - tokenStartPos="+(currentPosition - tokenStartPos));
		return new WhiteSpaceToken(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, this);
	}
    
    //********************************************************************************
    //**	                                                           identifierToken 
    //********************************************************************************
    /// Create a new identifier Token.
    /// @param ident the Token's identifier
    /// @return an identifier Token
    private LexToken identifierToken(final String ident) {
    	return new IdentifierToken(currentLineNumber, sourceText, currentColumn, currentPosition - tokenStartPos, this);
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

}
