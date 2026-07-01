package simula.builder;

import java.util.LinkedList;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.token.CharacterConst;
import simula.token.IntegerConst;
import simula.token.KeyWordToken;
import simula.token.LexToken;
import simula.token.LongRealConst;
import simula.token.RealConst;
import simula.token.SimpleString;

public class OLD_Psi_SimulaLexer {
	SimulaBuilder simBuilder;
	
//    private Vector<LexToken> tokens;
//    public Vector<LexToken> getTokens() { return tokens; }

    private CharSequence sourceText;
    private int textEndOffset;

    /// EOF is seen
    public LexToken EOF;
    
    private LexToken prevParserToken;
    private LexToken prevLexerToken;
    private LexToken currentLexerToken;
    private int currentPosition;
//    private int tokenStartOffset;
//    private int tokenEndOffset;
    
    // NYE FOR LANGUAGE SERVER - TESTING_NEW_LEXER
    private int currentLineStartPosition;
//    private int currentLine;   // Zero based
    private int currentColumn; // Zero based pr. line
    private int currentLength;
    
//    LexerState state;

    /// ISO EM(EndMedia) character used to denote end-of-input
    private final static int EOF_MARK=25;

    /// The Token queue. The method 'advance' will pick Tokens from the queue first.
    private LinkedList<LexToken> tokenQueue=new LinkedList<LexToken>();
    
    private int tokenStartLine;
    private int nextLineNumber;
    public int getSourceLineNumber() {
    	return nextLineNumber;
    }
    
	private String getTokenDebugText() {
		int tokenStartOffset = currentLineStartPosition + currentColumn;
		int tokenEndOffset = tokenStartOffset + currentLength;
		CharSequence txt = sourceText.subSequence(tokenStartOffset, tokenEndOffset);
		return txt.toString();
	}

    public void snapShot(String title) {
    	IO.println("############################### LEXER SNAPSHOT - " + title + " ######################################");
    	IO.println("sourceText:        0         10        20        30        40        50        60        70        80        90");
    	IO.println("sourceText:        0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
//    	IO.println("sourceText:        " + (""+sourceText).replace("\r", "\\r").replace("\n", "\\n"));
    	IO.println("sourceText:        " + (""+sourceText).replace("\r", "¤").replace("\n", "¤"));
    	IO.println("textEndOffset:     " + textEndOffset);
    	IO.println("currentLexerToken: " + currentLexerToken);
    	IO.println("currentPosition:   " + currentPosition);
//    	IO.println("tokenStartOffset:  " + tokenStartOffset);
//    	IO.println("tokenEndOffset:    " + tokenEndOffset);
    	IO.println("currentColumn:     " + currentColumn);
    	IO.println("currentLength:     " + currentLength);
    	IO.println("tokenStartLine:    " + tokenStartLine);
    	IO.println("nextLineNumber:    " + nextLineNumber);
    	IO.println("tokenQueue:        " + tokenQueue);
    	IO.println("############################### END LEXER SNAPSHOT - " + title + " ######################################");
    }
    
    public OLD_Psi_SimulaLexer(SimulaBuilder simBuilder) {
    	this.simBuilder = simBuilder;
    }
    
    public int getCurrentPosition() {
    	return currentPosition;
    }
    
    public void printState(String title) {
    	IO.println("==== LEXER STATE: " + title + "  " + currentLexerToken
    			+ "currentPosition=" + currentPosition+",currentColumn=" + currentColumn+", currentLength="+currentLength+", nextLineNumber"+nextLineNumber);
    }
    
//    public void rollBack(LexToken checkPoint, String debugName) {
//    	printState("LexerState.rollBack: "+debugName);
//	  	currentPosition   = checkPoint.endOffset;
//	  	tokenStartOffset  = checkPoint.startOffset;
//	  	tokenEndOffset    = checkPoint.endOffset;
//	  	nextLineNumber    = checkPoint.lineNumber;
//    	printState("LexerState.rollBack: "+debugName);
//		IO.println("SimulaLexer.ROLLbACK: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
//
////    	Util.STOP();
//    }
    
    public void start(CharSequence sourceText) {
		if(! Option.TESTING_WITHOUT_PSI) Util.IERR("SimulaLexer.start: Skal burukes");
    	if(Option.internal.TRACE_LEXER > 0) IO.println(("SimulaLexer.start: " + sourceText).replace("\r", "\\r").replace("\n", "\\n"));
        this.sourceText = sourceText;
	        nextLineNumber = 0;
	        
	        currentLineStartPosition = 0;
	        textEndOffset = sourceText.length();;
	        currentPosition = 0;
	        currentColumn = 0;
	        currentLength = 0;
	        advance();
//			IO.println("SimulaLexer.START: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
    }
    
//    public void startPsi(CharSequence buffer, int startOffset, int endOffset) {
//		if(Option.TESTING_WITHOUT_PSI) Util.IERR("SimulaLexer.startPsi: Skal ikke burukes");
//    	if(Option.internal.TRACE_LEXER > 0) IO.println(("SimulaLexer.start: " + buffer).replace("\r", "\\r").replace("\n", "\\n"));
//        sourceText = buffer;
//        nextLineNumber = 1;
//        textEndOffset = endOffset;
//        currentPosition = startOffset;
//        tokenStartOffset = startOffset;
//        tokenEndOffset = startOffset;
//        advance();
////		IO.println("SimulaLexer.START: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
//    }

    public void advance() {
    	prevLexerToken = currentLexerToken;
    	if(currentLexerToken != null && currentLexerToken.isParserToken()) prevParserToken = currentLexerToken;
//        IO.println("SimulaLexer.advance: BEGIN -----------------------------------------------------------------------");
//        printQueue();
//        IO.println("SimulaLexer.advance: BEGIN -----------------------------------------------------------------------");
    	if(! tokenQueue.isEmpty()) {
    		LexToken qtoken = popToken();
    		// IO.println("\nSimulaLexer.advance: POP OFF QUEUED TOKEN: "+qtoken+" ################################################################################");
    		// snapShot("BEGIN POP OFF QUEUED TOKEN: "+qtoken);
    		nextLineNumber = qtoken.lineNumber;
    		currentColumn = qtoken.column;
    		currentLength = qtoken.length;
    		
//    		Util.IERR("DETTE MÅ SKRIVES OM - BRUK:  NYE FOR LANGUAGE SERVER");
    		
    		currentLexerToken = qtoken;
    		
    		if(qtoken.keyWord == KeyWord.NEWLINE) {
    			nextLineNumber++;
    			tokenStartLine = nextLineNumber;
    		}

//    		if(Option.internal.TRACE_ADVANCE_LEXER)
//    			IO.println("SimulaLexer.advance: QLINE "+currentLexerToken.lineNumber+"                      NEW QUEUED CURRENT: "+currentLexerToken);
    		// return;
//    		IO.println("SimulaLexer.advance(from queue): nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
    	} else {
    		if (currentPosition >= textEndOffset) {
    			currentLexerToken = new KeyWordToken(tokenStartLine, sourceText, currentPosition, currentPosition, KeyWord.EOF, "");
    			if(EOF != null) {
    				IO.println("SimulaLexer.advance: Incomplete program, prevParserToken="+prevParserToken);
    				Util.syntaxError(simBuilder, prevParserToken, "Incomplete program: Attempt to advance Lexer beyond end of source text");
    			}
    			EOF = prevParserToken;
    			// return;
    		} else {
    			tokenStartLine = nextLineNumber;
    			if(Option.internal.TRACE_LEXER > 2)
    				IO.println("SimulaLexer.advance: ============================================================================= tokenStartLine="+tokenStartLine);
    			currentColumn = currentPosition - currentLineStartPosition;
    			currentLexerToken = scanBasic();
    			currentLength = currentPosition - currentLineStartPosition - currentColumn;

//    			if(Option.internal.TRACE_ADVANCE_LEXER)
//    				IO.println("SimulaLexer.advance: LINE "+currentLexerToken.lineNumber+"                       NEW NORMAL CURRENT: "+currentLexerToken);
    		}
//    		IO.println("SimulaLexer.advance: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
    	}
    }
    

//    public void OLD_advance() {
//    	prevLexerToken = currentLexerToken;
//    	if(currentLexerToken != null && currentLexerToken.isParserToken()) prevParserToken = currentLexerToken;
////        IO.println("SimulaLexer.advance: BEGIN -----------------------------------------------------------------------");
////        printQueue();
////        IO.println("SimulaLexer.advance: BEGIN -----------------------------------------------------------------------");
//    	if(! tokenQueue.isEmpty()) {
//    		LexToken qtoken = popToken();
//    		// IO.println("\nSimulaLexer.advance: POP OFF QUEUED TOKEN: "+qtoken+" ################################################################################");
//    		// snapShot("BEGIN POP OFF QUEUED TOKEN: "+qtoken);
//    		nextLineNumber = qtoken.lineNumber;
//    		tokenStartOffset = qtoken.startOffset;
//    		tokenEndOffset = qtoken.endOffset;
//    		currentLexerToken = qtoken;
//    		
//    		if(qtoken.keyWord == KeyWord.NEWLINE) {
//    			nextLineNumber++;
//    			tokenStartLine = nextLineNumber;
//    		}
//
////    		if(Option.internal.TRACE_ADVANCE_LEXER)
////    			IO.println("SimulaLexer.advance: QLINE "+currentLexerToken.lineNumber+"                      NEW QUEUED CURRENT: "+currentLexerToken);
//    		// return;
////    		IO.println("SimulaLexer.advance(from queue): nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
//    	} else {
//    		if (currentPosition >= textEndOffset) {
//    			currentLexerToken = new KeyWordToken(tokenStartLine, sourceText, currentPosition, currentPosition, KeyWord.EOF);
//    			if(EOF != null) {
//    				IO.println("SimulaLexer.advance: Incomplete program, prevParserToken="+prevParserToken);
//    				Util.syntaxError(simBuilder, prevParserToken, "Incomplete program: Attempt to advance Lexer beyond end of source text");
//    			}
//    			EOF = prevParserToken;
//    			// return;
//    		} else {
//    			tokenStartLine = nextLineNumber;
//    			if(Option.internal.TRACE_LEXER > 2)
//    				IO.println("SimulaLexer.advance: ============================================================================= tokenStartLine="+tokenStartLine);
//    			tokenStartOffset = currentPosition;
//    			currentLexerToken = scanBasic();
//    			tokenEndOffset = currentPosition;
//
////    			if(Option.internal.TRACE_ADVANCE_LEXER)
////    				IO.println("SimulaLexer.advance: LINE "+currentLexerToken.lineNumber+"                       NEW NORMAL CURRENT: "+currentLexerToken);
//    		}
////    		IO.println("SimulaLexer.advance: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
//    	}
//    }
    
	public void setParsingBoundPairList(boolean parsingBoundPairList) {
		this.parsingBoundPairList = parsingBoundPairList;
	}

	public void rollBackToBefore(LexToken prev, String debugInfo) {
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) {
			IO.println("\nSimulaLexer.rollBackToBefore: "+prev+debugInfo+", currentPosition="+currentPosition+" CALLED FROM: "+Util.calledFrom(6,7)+"\n");
			IO.println("\nSimulaLexer.rollBackToBefore: prev.startOffset="+prev.startOffset);
		}
//		Thread.dumpStack();
		currentPosition = prev.startOffset;
		nextLineNumber = prev.lineNumber;
//		IO.println("SimulaLexer.rollBackToBefore: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
		
        LOOP: while(! tokenQueue.isEmpty()) {
//        	IO.println("SimulaLexer.rollBackToBefore: tokenQueue: " + tokenQueue);
        	LexToken token = tokenQueue.getLast();
//        	IO.println("SimulaLexer.rollBackToBefore: tokenQueue.last: " + token);
        	if(token.startOffset > currentPosition) {
//            	IO.println("SimulaLexer.rollBackToBefore: POP OFF TOKEN: " + token);
            	tokenQueue.removeLast();
        	} else {
            	IO.println("SimulaLexer.rollBackToBefore: KEEP QUEUED TOKEN: " + token);
            	Util.IERR("DETTE TILFELLE TROR JEG IKKE VIL FOREKOMME: LOOP KAN FORENKLES TIL 'REMOVE ALL'");
        		break LOOP;
        	}
//        	IO.println("SimulaLexer.rollBackToBefore: NEXT tokenQueue: " + tokenQueue);
//        	Util.STOP();
        }
//    	IO.println("SimulaLexer.rollBackToBefore: FINAL currentPosition=" + currentPosition);
//    	IO.println("SimulaLexer.rollBackToBefore: FINAL nextLineNumber=" + nextLineNumber);
        
        if(Option.LEX_VERIFY) {
        	LexToken.prevToken = null;
        }
		advance();
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

	public LexToken getEOFToken() {
//		int ofst = textEndOffset - 1;
//		LexToken token = new LexToken(tokenStartLine,"ILLEGAL TERMINATION", ofst, ofst, KeyWord.EOF);
		int ofst = textEndOffset - 1 - currentLineStartPosition;
		LexToken token = new LexToken(tokenStartLine,"ILLEGAL TERMINATION", ofst, 0, KeyWord.EOF, "EOF");
        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getEOFToken: "+token);
        return token;
    }

    private boolean isWhitespace(int c) {
    	return Character.isWhitespace(c);
    }
    
    
    
//    private int pardepth;
    private boolean parsingBoundPairList;
    //********************************************************************************
    //**	                                                                 scanBasic
    //********************************************************************************
    /// Scan basic Token
    /// @return next Token
    private LexToken scanBasic() {
        if(Global.TRACE_LEXER) Util.TRACE("SimulaLexer.scanBasic: "+edcurrent());
//        IO.println("SimulaLexer.scanBasic: "+edcurrent());
        while(true)	{
            getNext(); if(current == EOF_MARK) return(null);
            
            if(Character.isLetter(current)) return(scanIdentifier());
            
            if (current == '\n') {
            	nextLineNumber++;
//        		IO.println("SimulaLexer.scanbasic: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
                LexToken token = new LexToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NEWLINE, getTokenDebugText());
                currentLineStartPosition = currentPosition;
                return token;
            }

            if (isWhitespace(current)) {
                while (currentPosition < textEndOffset
                		&& isWhitespace(sourceText.charAt(currentPosition))
                		&& sourceText.charAt(currentPosition) != '\n') {
                    currentPosition++;
                }
//                return TokenType.WHITE_SPACE;
                return new LexToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.WHITESPACES, getTokenDebugText());
            }

            switch(current) {

                case '%': return scanCommentToEndOfLine();
//	            case '%': return scanDirectiveLine();


                case '0':case '1':case '2':case '3':case '4':
                case '5':case '6':case '7':case '8':case '9': return scanNumber();

                case '-':
                    if(getNext() == '-')   return scanCommentToEndOfLine();
                    backStep(1); 	   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.MINUS, getTokenDebugText());

                case '.':
                    if(Character.isDigit(getNext())) { return(scanDotDigit(new StringBuilder())); }
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.DOT, getTokenDebugText());

                case '=':
                    if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.EQR, getTokenDebugText());
                    if(current == '/')
                        if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NER, getTokenDebugText());
                    backStep(1);
                    return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.EQ, getTokenDebugText());

                case '>':
                    if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.GE, getTokenDebugText());
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.GT, getTokenDebugText());

                case '<':
                    if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.LE, getTokenDebugText());
                    if(current   == '>')   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NE, getTokenDebugText());
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.LT, getTokenDebugText());

	            case '*':
		            if(getNext() == '*')   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.EXP, getTokenDebugText());
		            backStep(1); 	   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.MUL, getTokenDebugText());

                case '/':
                    if(getNext() == '/')   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.INTDIV, getTokenDebugText());
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.DIV, getTokenDebugText());

                case ':':
                    if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ASSIGNVALUE, getTokenDebugText());
//                    if(getNext() == '-' && pardepth == 0) return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ASSIGNREF, getTokenDebugText());
//                    if(current == '-' && pardepth == 0) return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ASSIGNREF, getTokenDebugText());
                    if(current == '-' && !parsingBoundPairList) return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ASSIGNREF, getTokenDebugText());
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.COLON, getTokenDebugText());

                case '&':
                    if(getNext()=='&' || current=='-' || current=='+' || Character.isDigit(current))
                        return (scanDigitsExp(null));
                    backStep(1); return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.AMPERSAND, getTokenDebugText());
                    
                case '!':  return(scanDirectComment());
                case '\'': return(scanCharacterConstant());
                case '"':  return(scanSimpleString());

                case '+': return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.PLUS, getTokenDebugText());
                case ',': return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.COMMA, getTokenDebugText());
                
//                case ';': pardepth=0; return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.SEMICOLON, getTokenDebugText());
//                case '(': pardepth++; return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.BEGPAR, getTokenDebugText());
//                case ')': pardepth--; return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ENDPAR, getTokenDebugText());

                case ';': return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.SEMICOLON, getTokenDebugText());
                case '(': return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.BEGPAR, getTokenDebugText());
                case ')': return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ENDPAR, getTokenDebugText());

                case '[': return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.BEGBRACKET, getTokenDebugText());
                case ']': return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ENDBRACKET, getTokenDebugText());

//	            case '\n':			/* NL (LF) */
//	    	      if (editorMode) return SyntaxElement.NEWLINE));
//	            case ' ':
//	            case '\b':			/* BS */
//	            case '\t':			/* HT */
//	           	//case '\v':		/* VT */
//	            case '\f':			/* FF */
//	            case '\r':			/* CR */
//	            	break;

                default:
                    return new LexToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.BAD_CHARACTERS, getTokenDebugText());        	

            }
        }
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
        if(Global.TRACE_LEXER) Util.TRACE("scanIdentifier: name=\""+name+"\"");
        String ident=(Global.CaseSensitive)?name:name.toLowerCase();
        switch(Character.toLowerCase(ident.charAt(0))) {
            case 'a':
                if(ident.equals("activate"))     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ACTIVATE, getTokenDebugText());
                if(ident.equals("after"))	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.AFTER, getTokenDebugText());
                if(ident.equals("and"))			 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.AND, getTokenDebugText());
                if(ident.equals("and_then"))	 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.AND_THEN, getTokenDebugText());
                if(ident.equals("array"))	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ARRAY, getTokenDebugText());
                if(ident.equals("at"))		     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.AT, getTokenDebugText());
                break;
            case 'b':
                if(ident.equals("before"))       return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.BEFORE, getTokenDebugText());
                if(ident.equals("begin"))        return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.BEGIN, getTokenDebugText());
                if(ident.equals("boolean"))      return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.BOOLEAN, getTokenDebugText());
                break;
            case 'c':
                if(ident.equals("character"))	 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.CHARACTER, getTokenDebugText());
                if(ident.equals("class"))        return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.CLASS, getTokenDebugText());
                if(ident.equals("comment"))      return scanDirectComment();
                break;
            case 'd':
                if(ident.equals("delay"))   	 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.DELAY, getTokenDebugText());
                if(ident.equals("do")) 	    	 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.DO, getTokenDebugText());
                break;
            case 'e':
                if(ident.equals("else"))         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.ELSE, getTokenDebugText());

	        	if(ident.equals("end"))   	     return scanEndComment();
//                if(ident.equals("end"))   	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.END;

                if(ident.equals("eq"))	         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.EQ, getTokenDebugText());
                if(ident.equals("eqv"))	         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.EQV, getTokenDebugText());
                if(ident.equals("external"))     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.EXTERNAL, getTokenDebugText());
                break;
            case 'f':
                if(ident.equals("false"))  	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.FALSE, getTokenDebugText());
                if(ident.equals("for"))    	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.FOR, getTokenDebugText());
                break;
            case 'g':
                if(ident.equals("ge"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.GE, getTokenDebugText());
                if(ident.equals("go"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.GO, getTokenDebugText());
                if(ident.equals("goto"))         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.GOTO, getTokenDebugText());
                if(ident.equals("gt"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.GT, getTokenDebugText());
                break;
            case 'h':
                if(ident.equals("hidden"))       return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.HIDDEN, getTokenDebugText());
                break;
            case 'i':
                if(ident.equals("if"))	         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.IF, getTokenDebugText());
                if(ident.equals("imp"))   	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.IMP, getTokenDebugText());
                if(ident.equals("in"))   	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.IN, getTokenDebugText());
                if(ident.equals("inner"))	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.INNER, getTokenDebugText());
                if(ident.equals("inspect")) 	 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.INSPECT, getTokenDebugText());
                if(ident.equals("integer"))	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.INTEGER, getTokenDebugText());
                if(ident.equals("is"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.IS, getTokenDebugText());
                break;
            case 'l':
                if(ident.equals("label"))        return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.LABEL, getTokenDebugText());
                if(ident.equals("le"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.LE, getTokenDebugText());
                if(ident.equals("long"))         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.LONG, getTokenDebugText());
                if(ident.equals("lt"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.LT, getTokenDebugText());
                break;
            case 'n':
                if(ident.equals("name"))         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NAME, getTokenDebugText());
                if(ident.equals("ne"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NE, getTokenDebugText());
                if(ident.equals("new"))          return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NEW, getTokenDebugText());
                if(ident.equals("none"))         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NONE, getTokenDebugText());
                if(ident.equals("not"))          return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NOT, getTokenDebugText());
                if(ident.equals("notext"))       return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.NOTEXT, getTokenDebugText());
                break;
            case 'o':
                if(ident.equals("or"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.OR, getTokenDebugText());
                if(ident.equals("or_else"))      return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.OR_ELSE, getTokenDebugText());
                if(ident.equals("otherwise"))    return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.OTHERWISE, getTokenDebugText());
                break;
            case 'p':
                if(ident.equals("prior"))        return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.PRIOR, getTokenDebugText());
                if(ident.equals("procedure"))    return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.PROCEDURE, getTokenDebugText());
                if(ident.equals("protected"))    return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.PROTECTED, getTokenDebugText());
                break;
            case 'q':
                if(ident.equals("qua"))          return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.QUA, getTokenDebugText());
                break;
            case 'r':
                if(ident.equals("reactivate"))   return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.REACTIVATE, getTokenDebugText());
                if(ident.equals("real"))         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.REAL, getTokenDebugText());
                if(ident.equals("ref"))          return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.REF, getTokenDebugText());
                break;
            case 's':
                if(ident.equals("short"))  		 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.SHORT, getTokenDebugText());
                if(ident.equals("step"))   		 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.STEP, getTokenDebugText());
                if(ident.equals("switch")) 		 return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.SWITCH, getTokenDebugText());
                break;
            case 't':
                if(ident.equals("text"))  	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.TEXT, getTokenDebugText());
                if(ident.equals("then"))  	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.THEN, getTokenDebugText());
                if(ident.equals("this"))   	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.THIS, getTokenDebugText());
                if(ident.equals("to"))           return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.TO, getTokenDebugText());
                if(ident.equals("true"))   	     return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.TRUE, getTokenDebugText());
                break;
            case 'u':
                if(ident.equals("until"))        return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.UNTIL, getTokenDebugText());
                break;
            case 'v':
                if(ident.equals("value"))        return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.VALUE, getTokenDebugText());
                if(ident.equals("virtual"))      return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.VIRTUAL, getTokenDebugText());
                break;
            case 'w':
                if(ident.equals("when"))         return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.WHEN, getTokenDebugText());
                if(ident.equals("while"))        return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.WHILE, getTokenDebugText());
                break;
        }
//        return(new Identifier(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, ident));
//        return new Identifier(tokenStartLine, sourceText, tokenStartOffset, currentPosition);
		return new LexToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.IDENTIFIER, getTokenDebugText());
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
        if(Global.TRACE_LEXER) Util.TRACE("scanNumber, "+edcurrent());
        Util.ASSERT(Character.isDigit((char)(current)),"scanNumber:Expecting a Digit");
        StringBuilder number=new StringBuilder();

        number.append((char)current);
        if(getNext() == 'R' && (firstChar == '2' | firstChar == '4' | firstChar == '8')) {
            radix=firstChar - '0';
            if(Global.TRACE_LEXER) Util.TRACE("scanNumber, radix="+radix);
            number.setLength(0);
        } else if(firstChar == '1' && current == '6') {
            number.append((char)current);
            if(getNext() == 'R') {
                radix=16;
                if(Global.TRACE_LEXER) Util.TRACE("scanNumber, radix="+radix);
                number.setLength(0);
            } else backStep(1);
        } else backStep(1);

        while ((radix==16 ? isHexDigit(getNext()) : Character.isDigit(getNext())) || current=='_')
            if(current!='_') number.append((char)current);

        if(current == '.' && radix == 10) { getNext(); return(scanDotDigit(number)); }

        if(current == '&' && radix == 10) { getNext(); return(scanDigitsExp(number)); }

        String result=number.toString(); number=null;
        if(Global.TRACE_LEXER) Util.TRACE("scanNumber, result='"+result+"' radix="+radix);

        backStep(1);
        @SuppressWarnings("unused")
        long res = 0;
        try {
            res=Integer.parseInt(result,radix);
        } catch (NumberFormatException e) {
            LexToken token = new IntegerConst(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, 0, getTokenDebugText());
            Util.syntaxError(simBuilder, token, "Integer number out of range: " + token.edText());
            return token;
        }
        return new IntegerConst(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, res, getTokenDebugText());
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
        if(Global.TRACE_LEXER) Util.TRACE("scanDotDigit, "+edcurrent());
        number.append('.');
        if(Character.isDigit(current)) number.append((char)current);
        while(Character.isDigit(getNext()) || current == '_')
            if(current != '_') number.append((char)current);

        if(current == '&') { getNext(); return(scanDigitsExp(number)); }

        String result=number.toString(); number=null;
        if(Global.TRACE_LEXER) Util.TRACE("scanDotDigit, result='"+result);
        backStep(1);
        try {
            return new RealConst(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, Float.parseFloat(result), getTokenDebugText());
        } catch(NumberFormatException e) {
            LexToken token = new RealConst(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, 0, getTokenDebugText());
            Util.syntaxError(simBuilder, token, "Illegal number: " + token.edText());
            return token;
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
        @SuppressWarnings("unused")
        boolean doubleAmpersand=false;
        if(Global.TRACE_LEXER) Util.TRACE("scanDigitsExp, "+edcurrent());
        if(number==null) { number=new StringBuilder(); number.append('1'); }
        if(current == '&') { getNext(); doubleAmpersand=true; }
        number.append('e');
        if(current == '-') { number.append('-'); getNext(); }
        else if(current == '+') getNext();
        if(Character.isDigit(current)) number.append((char)current);
        while(Character.isDigit(getNext()) || current == '_') number.append((char)current);

        result=number.toString(); number=null;
        if(Global.TRACE_LEXER) Util.TRACE("scanDigitsExp, result='"+result);
        backStep(1);
        try {
            if(doubleAmpersand) return new LongRealConst(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, Double.parseDouble(result), getTokenDebugText());
            return new RealConst(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, Float.parseFloat(result), getTokenDebugText());
        } catch(NumberFormatException e) {
            LexToken token = new RealConst(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, 0, getTokenDebugText());
            Util.syntaxError(simBuilder, token, "Illegal number: " + token.edText());
            return token;
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
        if(Global.TRACE_LEXER) Util.TRACE("scanName, "+edcurrent());
        Util.ASSERT(Character.isLetter((char)(current)),"Expecting a Letter");
        name.append((char)current);
        while ((Character.isLetter(getNext()) || Character.isDigit(current) || current == '_'))
            name.append((char)current);
        backStep(1);
        if(Global.TRACE_LEXER) Util.TRACE("scanName, name="+name+",current="+edcurrent());
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
    	String err = null;
        char result=0;
        if(Global.TRACE_LEXER) Util.TRACE("scanCharacterConstant, "+edcurrent());
        Util.ASSERT((char)(current)=='\'',"Expecting a character quote '");
        if((isPrintable(getNext())) && current != '!') {
            result=(char)current; getNext();
        } else if(current == '!') {
            result=(char)scanPossibleIsoCode(); getNext();
        } else err = "Illegal character constant. "+edcurrent();

        if(current != '\'') {
            err = "Character constant is not terminated. "+edcurrent();
            backStep(1);
        }
        if(Global.TRACE_LEXER) Util.TRACE("END scanCharacterConstant, result='"+result+"', "+edcurrent());
        LexToken token = new CharacterConst(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, result, getTokenDebugText());
        if(err != null) Util.syntaxError(simBuilder, token, err);
        return token;
    }
    
    //********************************************************************************
    //**	                                                          scanSimpleString
    //********************************************************************************
    /// Scan and deliver a Simple String.
    /// <pre>
    ///  Reference-Syntax:
    ///
    ///         simple-string = " { iso-code |  non-quote-character  |  ""  }  "
    ///
    ///            iso-code = ! digit  [ digit ]  [ digit ]  !
    ///
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return next Token
    private LexToken scanSimpleString() {
        StringBuilder sb=new StringBuilder();
        String err = null;
   LOOP:while(true) {
	      	switch(getNext()) {
	      		case '"':
                  if(getNext() == '"') {
                  	sb.append('"');
                  } else {
                  	backStep(1);
                  	break LOOP;
                  }
      			break;
	        	case ' ':
	            	sb.append(' ');
	            	break;
	        	case '!':
	                int code=scanPossibleIsoCode();
	                sb.append((char)code);
	                break;
	        	case '\n':
	                err = "Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6"; 
//	                currentPosition--;
	                backStep(1);
	                break LOOP;
	        	case EOF_MARK:
	                err = "Text constant is not terminated.";
	                break LOOP;
              default:
              	if(! isWhitespace(current)) sb.append((char)current);
      	}
      }
      String result=sb.toString();
      if(Global.TRACE_LEXER) Util.TRACE("scanSimpleString: Result=\""+result+"\", "+edcurrent());
      LexToken token = new SimpleString(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, result, getTokenDebugText());
      if(err != null) Util.syntaxError(simBuilder, token, err);
      return token;
    }
    
    //********************************************************************************
    //**	                                                       scanStringSeparator
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
    private boolean scanStringSeparator() {
    	getNext();
        IO.println("SimulaLexer.scanStringSeparator: " + edcurrent());
        if(current=='!') {
            LexToken cc=scanDirectComment();
            tokenQueueAdd("scanStringSeparator", cc);
//            current=' ';
            return(true);
        } else if(Character.isLetter((char)current)) {
            String name=scanName();
            IO.println("SimulaLexer.scanStringSeparator: name=\""+name+'"');
            if(name.equalsIgnoreCase("COMMENT")) {
                LexToken cc=scanDirectComment();
                tokenQueueAdd("scanStringSeparator", cc);
//                current=' ';
                return(true);
            } else backStep(name.length());
            return(false);
        }
        return(Character.isWhitespace(current));
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
        if (Global.TRACE_LEXER) Util.TRACE("scanPossibleIsoCode, " + edcurrent());
        Util.ASSERT((char) (current) == '!', "Expecting a character !");
        if (Character.isDigit(getNext())) {
            firstchar = (char) current;
            if (Character.isDigit(getNext())) {
                secondchar = (char) current;
                if (Character.isDigit(getNext())) {
                    thirdchar = (char) current;
                    if (getNext() == '!') { // ! digit digit digit ! Found
                        int value = (((firstchar - '0') * 10 + secondchar - '0') * 10 + thirdchar - '0');
                        if (Global.TRACE_LEXER)
                            Util.TRACE("scanPossibleIsoCode:Got three digits: "+(char)firstchar+(char)secondchar+(char)thirdchar+"value="+value);
                        if (value < 256)
                            return (value);
                        Util.warning("ISO-Code " + value + " is out of range (0:255)"
                                +" interpreted as an ordinary sequence of characters: !" +value + "!  See Simula Standard 1.6");
//                        pushBack( current);
//                        pushBack(thirdchar);
//                        pushBack(secondchar);
//                        pushBack(firstchar);
                        backStep(4);
                        return ('!');
                    } else {
//                        pushBack( current);
//                        pushBack(thirdchar);
//                        pushBack(secondchar);
//                        pushBack(firstchar);
                        backStep(4);
                        return ('!');
                    }
                } else if (current == '!') { // ! digit digit ! Found
                    return ((char) ((firstchar - '0') * 10 + secondchar - '0'));
                } else {
//                    pushBack( current);
//                    pushBack(secondchar);
//                    pushBack(firstchar);
                    backStep(3);
                    return ('!');
                }
            } else if (current == '!') { // ! digit ! Found
                return ((char) (firstchar - '0'));
            } else {
//                pushBack( current);
//                pushBack(firstchar);
                backStep(2);
                return ('!');
            }
        } else {
//            pushBack( current);
            backStep(1);
            return ('!');
        }
    }

    // ********************************************************************************
    // ** scanDirectComment
    // ********************************************************************************
    /// Scan a Comment.
    /// <pre>
    /// Reference-Syntax:
    ///
    ///       comment = ! { any character except semicolon } ;
    /// 
    ///       comment = COMMENT { any character except semicolon } ;
    ///
    /// PRE-Condition: current is the first character of construct.
    /// 
    /// End-Condition: current is last character of construct
    ///                getNext will return first character after construct
    /// </pre>
    /// @return a Comment Token
    private LexToken scanDirectComment() {
		LexToken commentToken = new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.COMMENT_KEY, getTokenDebugText());
    	tokenStartOffset = currentPosition;
		while(current != ';') {
			if(current == '\n') {
	        	tokenQueueAdd("scanDirectComment - TEXT", new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition-1, KeyWord.COMMENT_TEXT, getTokenDebugText());
	        	tokenQueueAdd("scanDirectComment - NEWLINE", new KeyWordToken(tokenStartLine++, sourceText, currentPosition-1, currentPosition, KeyWord.NEWLINE, getTokenDebugText());
	        	tokenStartOffset = currentPosition;
			}
			getNext(); // Updates current and currentPosition
		}
//    	tokenQueueAdd("scanDirectComment - TEXT", new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.COMMENT_TEXT));
    	tokenQueueAdd("scanDirectComment - TEXT", KeyWord.COMMENT_TEXT);
        tokenStartOffset = commentToken.endOffset;
        return commentToken;
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
    ///                getNext will return first character after construct.  I.E. a newline or EOF character
    /// </pre>
    /// @return a Comment Token
    private LexToken scanCommentToEndOfLine() {
        StringBuilder skipped = new StringBuilder();
        if (Global.TRACE_LEXER) Util.TRACE("BEGIN scanCommentToEndOfLine, " + edcurrent());
        while ((getNext() != '\n') && current != EOF_MARK)
            skipped.append((char) current);
        
        boolean TESTING = true;
        if(TESTING) {
//        	currentPosition--;
        	backStep(1);
        } else {
	        nextLineNumber++;
//			IO.println("SimulaLexer.scanCommentToEndOfLine: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
	        skipped.append((char) current);
        }
        if (Global.TRACE_LEXER) Util.TRACE("END scanCommentToEndOfLine: " + edcurrent() + "  skipped=\"" + skipped + '"');
        if (Global.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
        return new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.COMMENT_TEXT, getTokenDebugText());
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
    private LexToken scanEndComment() {
        LexToken endToken = new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, KeyWord.END, getTokenDebugText());
        if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: endToken="+endToken);
        tokenStartOffset = endToken.endOffset;
        
        if (Global.TRACE_LEXER) Util.TRACE("scanEndComment, " + edcurrent());
        int firstLine = nextLineNumber;
//        int lastLine = firstLine;
        int nPhrase = 0; // Number of comment phrases
        LOOP:while (getNext() != EOF_MARK) {
        	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: current="+current+":'"+(""+(char)current).replace("\r", "\\r").replace("\n", "\\n")+"'");
            if (current == '\n') {
//            	nextLineNumber++;
            	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT NEWLINE");
                nPhrase = mayBe_AddCommentToken_ToTokenQueue(nPhrase);
                
//            	nextLineNumber++;
//            	tokenStartLine++;
                tokenQueueAdd("scanEndComment - NEWLINE", KeyWord.NEWLINE);
            	tokenStartLine++;
//    			IO.println("SimulaLexer.scanCommentToEndOfLine: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
               
            } else if (current == ';') {
                nPhrase = mayBe_AddCommentToken_ToTokenQueue(nPhrase);
                tokenQueueAdd("scanEndComment-SEMICOLON", KeyWord.SEMICOLON);
                break LOOP;
            } else if (Character.isLetter(current)) {
                String name = scanName();
                if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT name="+name);
                if (Util.equals(name, "end") || Util.equals(name, "else")
                        || Util.equals(name, "when") || Util.equals(name, "otherwise")) {
                	currentPosition = currentPosition - name.length();
                    
                    if(currentPosition > tokenStartOffset) {
                    	tokenQueueAdd("scanEndComment - NAME", KeyWord.COMMENT_TEXT);
                    }
                    break LOOP;
                }
            } else {
            	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT OTHER="+current+":'"+(""+(char)current).replace("\r", "\\r").replace("\n", "\\n")+"'");
//                lastLine = nextLineNumber;
            }
        }
        nPhrase = mayBe_AddCommentToken_ToTokenQueue(nPhrase);
        if(TESTING_SCAN_END) {
	        IO.println("SimulaLexer.scanEndComment: endToken: " + endToken);
	        IO.println("SimulaLexer.scanEndComment: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
	        IO.println("SimulaLexer.scanEndComment: END TOKEN: " + endToken);
	        printQueue();
	        IO.println("SimulaLexer.mayBe_AddCommentToken_ToTokenQueue: TOKEN QUEUE AFTER END -----------------------------------------------------------------------");
        }
        tokenStartOffset = endToken.endOffset;
        
//		tokenStartLine = ++endToken.lineNumber;
//        nextLineNumber = endToken.lineNumber;
        return endToken;
    }
    
	private void tokenQueueAdd(String debugName, int keyWord) {
		LexToken token = new KeyWordToken(tokenStartLine, sourceText, currentColumn, currentPosition - currentLineStartPosition, keyWord, getTokenDebugText());
	    tokenQueueAdd(debugName, token);
	}
    
	private void tokenQueueAdd(String debugName, LexToken token) {
//		IO.println("SimulaLexer.tokenQueueAdd: "+debugName+" "+token);
	    tokenQueue.add(token);
	    tokenStartOffset = currentPosition;
	    currentLexerToken = token;
	}
   
    private int mayBe_AddCommentToken_ToTokenQueue(int nPhrase) {
        if(current != EOF_MARK) currentPosition--;
        if(currentPosition > tokenStartOffset) {
        	tokenQueueAdd("scanEndComment - SEMICOLON", KeyWord.COMMENT_TEXT);
            tokenStartOffset = currentPosition;
            if(++nPhrase == 2) {
            	int lno = Global.sourceLineNumber;
            	Global.sourceLineNumber = tokenStartLine;
            	Util.warning("END-Comment span multiple source lines");
            	Global.sourceLineNumber = lno;
            }
        }
        currentPosition++;
        return nPhrase;
    }
    
    private boolean TESTING_LNO = true;
    private LexToken popToken() {
    	LexToken token = tokenQueue.pop();
        tokenStartOffset = token.startOffset;
        tokenEndOffset = token.endOffset;
        currentPosition = tokenEndOffset;

	    if(TESTING_LNO) {
		    nextLineNumber = token.lineNumber + 1;
//		    tokenStartLine = nextLineNumber;
//			IO.println("SimulaLexer.popToken: nextLineNumber: " + nextLineNumber + ", tokenStartLine: " + tokenStartLine);
//	    	IO.println("SimulaLexer.popToken: "+token+"   nextLineNumber="+nextLineNumber + "  tokenQueue: " + tokenQueue.size() + tokenQueue);
	    }
        return(token);    	
    }
    
    private void printQueue() {
    	for(LexToken token:tokenQueue) {
        	IO.println("SimulaLexer.printQueue: token="+token);
    	}
    }



    //********************************************************************************
    //**	                                                                 UTILITIES
    //********************************************************************************

    /// The current character read.
    private int current;

    /// Returns next input character.
    /// @return next input character
    private int getNext() {
        current = (currentPosition >= textEndOffset)? EOF_MARK : sourceText.charAt(currentPosition++);
        return current;
    }

    /// Decrement currentPosition with the given 'n'
    /// @param n backStep count
    private void backStep(final int n) {
        if(current != EOF_MARK)
        	currentPosition = currentPosition - n;
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

}
