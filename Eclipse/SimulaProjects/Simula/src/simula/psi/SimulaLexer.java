package simula.psi;

import java.util.LinkedList;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.token.CharacterConst;
import simula.token.IntegerConst;
import simula.token.KeyWordToken;
import simula.token.LongRealConst;
import simula.token.RealConst;
import simula.token.SimpleString;

public class SimulaLexer {
    
//    private Vector<LexToken> tokens;
//    public Vector<LexToken> getTokens() { return tokens; }

    private CharSequence sourceText;
    private int textEndOffset;

//    private LexToken prevParserToken;
    private LexToken currentLexerToken;
    private int currentPosition;
    private int tokenStartOffset;
    private int tokenEndOffset;
    
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
    
    
	public void resetCheckPoint(String debugName, PsiTree psiTree) {
		IO.println("\n\nSimulaLexer.resetCheckPoint: psiTree: " + psiTree);
		IO.println("SimulaLexer.resetCheckPoint: psiTree.startOffset: " + psiTree.startOffset);
	    nextLineNumber = psiTree.lineNumber;
	    
	    currentLexerToken = psiTree.checkPoint;
		tokenStartOffset = currentLexerToken.startOffset;
		IO.println("SimulaLexer.resetCheckPoint: psiTree.checkPoint'startOffset: " + tokenStartOffset);
		tokenEndOffset = currentPosition = currentLexerToken.endOffset;
		tokenStartLine = nextLineNumber;
		
//	    if(! tokenQueue.isEmpty()){
//	    	LexToken token = tokenQueue.peekFirst();
//			IO.println("SimulaLexer.resetCheckPoint: REMOVE from tokenQueue: currentPosition: " + currentPosition + " MayRemove: "+token);
//			if(token.startOffset > currentPosition) {
//				tokenQueue.removeFirst();
//			}
//	    }
	    tokenQueue=new LinkedList<LexToken>();
	    LexToken.prevToken = currentLexerToken;
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
    	IO.println("tokenStartOffset:  " + tokenStartOffset);
    	IO.println("tokenEndOffset:    " + tokenEndOffset);
    	IO.println("tokenStartLine:    " + tokenStartLine);
    	IO.println("nextLineNumber:    " + nextLineNumber);
    	IO.println("tokenQueue:        " + tokenQueue);
    	IO.println("############################### END LEXER SNAPSHOT - " + title + " ######################################");
    }
    
    public SimulaLexer() {
//    	tokens = new Vector<LexToken>();
//    	state = new LexerState();
    }
    
    public int getCurrentPosition() {
    	return currentPosition;
    }
    
    public void printState(String title) {
    	IO.println("==== LEXER STATE: " + title + "  " + currentLexerToken
    			+ "currentPosition=" + currentPosition+",tokenStartOffset=" + tokenStartOffset+", tokenEndOffset="+tokenEndOffset+", nextLineNumber"+nextLineNumber);
    }
    
    public void rollBack(LexToken checkPoint, String debugName) {
    	printState("LexerState.rollBack: "+debugName);
	  	currentPosition   = checkPoint.endOffset;
	  	tokenStartOffset  = checkPoint.startOffset;
	  	tokenEndOffset    = checkPoint.endOffset;
	  	nextLineNumber    = checkPoint.lineNumber;
    	printState("LexerState.rollBack: "+debugName);

//    	Util.STOP();
    }
    
    public void start(CharSequence buffer, int startOffset, int endOffset) {
    	if(Option.internal.TRACE_LEXER > 0) IO.println(("SimulaLexer.start: " + buffer).replace("\r", "\\r").replace("\n", "\\n"));
        sourceText = buffer;
        nextLineNumber = 1;
        textEndOffset = endOffset;
        currentPosition = startOffset;
        tokenStartOffset = startOffset;
        tokenEndOffset = startOffset;
        advance();
    }

    public void advance() {
//    	if(currentLexerToken != null && currentLexerToken.isParserToken()) prevParserToken = currentLexerToken;
//        IO.println("SimulaLexer.advance: BEGIN -----------------------------------------------------------------------");
//        printQueue();
//        IO.println("SimulaLexer.advance: BEGIN -----------------------------------------------------------------------");
        if(! tokenQueue.isEmpty()) {
            LexToken qtoken = popToken();
//        	IO.println("\nSimulaLexer.advance: POP OFF QUEUED TOKEN: "+qtoken+" ################################################################################");
//            snapShot("BEGIN POP OFF QUEUED TOKEN: "+qtoken);
        	nextLineNumber = qtoken.lineNumber;
        	tokenStartOffset = qtoken.startOffset;
        	tokenEndOffset = qtoken.endOffset;
        	currentLexerToken = qtoken;
//        	if(qtoken.keyWord == KeyWord.NEWLINE) nextLineNumber--;
        	
            if(Option.internal.TRACE_LEXER > 2)
            	IO.println("SimulaLexer.advance: QLINE "+currentLexerToken.lineNumber+"                      NEW QUEUED CURRENT: "+currentLexerToken);
//            tokens.add(currentLexerToken);
            return;
        }
        if (currentPosition >= textEndOffset) {
            currentLexerToken = null;
//            IO.println("SimulaLexer.advance: EOF ");
//            Thread.dumpStack();
            return;
        }
        tokenStartLine = nextLineNumber;
        if(Option.internal.TRACE_LEXER > 2)
        	IO.println("SimulaLexer.advance: ============================================================================= tokenStartLine="+tokenStartLine);
        tokenStartOffset = currentPosition;
        currentLexerToken = scanBasic();
        tokenEndOffset = currentPosition;
//        tokens.add(currentLexerToken);
        
        if(Option.internal.TRACE_LEXER > 2)
        	IO.println("SimulaLexer.advance: LINE "+currentLexerToken.lineNumber+"                       NEW NORMAL CURRENT: "+currentLexerToken);
        	
//        if(tokenStartOffset == tokenEndOffset) {
//            CharSequence xxx = sourceText.subSequence(tokenStartOffset-10, tokenStartOffset);
////            Util.TRACE("SimulaLexer.advance: start="+tokenStartOffset+", end="+tokenEndOffset+", type="+currentLexerToken);
//            throw new RuntimeException("SimulaLexer.advance: start="+tokenStartOffset+", end="+tokenEndOffset+", type="+currentLexerToken+"  "+xxx);
//        }
    }
    
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
        
		advance();
	}

//	public LexToken getPrevParserToken() {
//        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getPrevParserToken: "+prevParserToken);
//        return prevParserToken;
//    }

	public LexToken getCurrentLexerToken() {
        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getCurrentLexerToken: "+currentLexerToken);
        return currentLexerToken;
    }

//	/// Return current 'Parser' token.
//	/// Skip Comment, Whitespace and Newline tokens.
//	/// Concatenate successive Simple Strings into a single token.
//    public LexToken getCurrentParserToken(PsiTree psiTree) {
//    	// if(DEBUG > 1) IO.println("PsiBuilder.getCurrentParserToken: "+currentLexerToken);
//        while(true) {
//    		LexToken token = getCurrentLexerToken();
////        	IO.println("PsiBuilder.getCurrentParserToken: "+token);
//    		if(token == null) {
////    			public LexToken(int tokenStartLine, CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
//    			token = getEOFToken();
////    			return null;
//    		}
//        	if(token.isParserToken()) return token;
////        	IO.println("PsiBuilder.getCurrentParserToken: SKIP TOKEN: "+token);
//        	psiTree.addChild(token);
//        	advance();
//		}
//    }

	public LexToken getEOFToken() {
		int ofst = textEndOffset - 1;
		LexToken token = new LexToken(tokenStartLine,"ILLEGAL TERMINATION", ofst, ofst, KeyWord.EOF);
        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.getEOFToken: "+token);
        return token;
    }

//    public LexToken nextToken() {
//    	Util.IERR("GAMMEL: BLE BRUKT AV DefaultScanner OG SimulaScanner");
//        LexToken next = currentLexerToken;
//        advance();
//        if(Option.internal.TRACE_LEXER > 1) IO.println("SimulaLexer.nextToken: "+next);
//        return next;
//    }

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
                return new LexToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NEWLINE);        	
            }

            if (isWhitespace(current)) {
                while (currentPosition < textEndOffset
                		&& isWhitespace(sourceText.charAt(currentPosition))
                		&& sourceText.charAt(currentPosition) != '\n') {
                    currentPosition++;
                }
//                return TokenType.WHITE_SPACE;
                return new LexToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.WHITESPACES);
            }

            switch(current) {

                case '%': return scanCommentToEndOfLine();
//	            case '%': return scanDirectiveLine();


                case '0':case '1':case '2':case '3':case '4':
                case '5':case '6':case '7':case '8':case '9': return scanNumber();

                case '-':
                    if(getNext() == '-')   return scanCommentToEndOfLine();
                    backStep(1); 	   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.MINUS);

                case '.':
                    if(Character.isDigit(getNext())) { return(scanDotDigit(new StringBuilder())); }
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.DOT);

                case '=':
                    if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.EQR);
                    if(current == '/')
                        if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NER);
                        else Util.error("Illegal character combination ="+(char)current);
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.EQ);

                case '>':
                    if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.GE);
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.GT);

                case '<':
                    if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.LE);
                    if(current   == '>')   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NE);
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.LT);

	            case '*':
		            if(getNext() == '*')   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.EXP);
		            backStep(1); 	   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.MUL);

                case '/':
                    if(getNext() == '/')   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.INTDIV);
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.DIV);

                case ':':
                    if(getNext() == '=')   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ASSIGNVALUE);
//                    if(getNext() == '-' && pardepth == 0) return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ASSIGNREF);
//                    if(current == '-' && pardepth == 0) return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ASSIGNREF);
                    if(current == '-' && !parsingBoundPairList) return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ASSIGNREF);
                    backStep(1);     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.COLON);

                case '&':
                    if(getNext()=='&' || current=='-' || current=='+' || Character.isDigit(current))
                        return (scanDigitsExp(null));
                    backStep(1); return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.AMPERSAND);
                    
                case '!':  return(scanComment());
                case '\'': return(scanCharacterConstant());
                case '"':  return(scanSimpleString());

                case '+': return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.PLUS);
                case ',': return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.COMMA);
                
//                case ';': pardepth=0; return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.SEMICOLON);
//                case '(': pardepth++; return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.BEGPAR);
//                case ')': pardepth--; return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ENDPAR);

                case ';': return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.SEMICOLON);
                case '(': return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.BEGPAR);
                case ')': return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ENDPAR);

                case '[': return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.BEGBRACKET);
                case ']': return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ENDBRACKET);

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
                    return new LexToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.BAD_CHARACTERS);        	

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
                if(ident.equals("activate"))     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ACTIVATE);
                if(ident.equals("after"))	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.AFTER);
                if(ident.equals("and"))			 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.AND);
                if(ident.equals("and_then"))	 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.AND_THEN);
                if(ident.equals("array"))	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ARRAY);
                if(ident.equals("at"))		     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.AT);
                break;
            case 'b':
                if(ident.equals("before"))       return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.BEFORE);
                if(ident.equals("begin"))        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.BEGIN);
                if(ident.equals("boolean"))      return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.BOOLEAN);
                break;
            case 'c':
                if(ident.equals("character"))	 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.CHARACTER);
                if(ident.equals("class"))        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.CLASS);
                if(ident.equals("comment"))      return scanComment();
                break;
            case 'd':
                if(ident.equals("delay"))   	 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.DELAY);
                if(ident.equals("do")) 	    	 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.DO);
                break;
            case 'e':
                if(ident.equals("else"))         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.ELSE);

	        	if(ident.equals("end"))   	     return scanEndComment();
//                if(ident.equals("end"))   	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.END;

                if(ident.equals("eq"))	         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.EQ);
                if(ident.equals("eqv"))	         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.EQV);
                if(ident.equals("external"))     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.EXTERNAL);
                break;
            case 'f':
                if(ident.equals("false"))  	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.FALSE);
                if(ident.equals("for"))    	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.FOR);
                break;
            case 'g':
                if(ident.equals("ge"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.GE);
                if(ident.equals("go"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.GO);
                if(ident.equals("goto"))         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.GOTO);
                if(ident.equals("gt"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.GT);
                break;
            case 'h':
                if(ident.equals("hidden"))       return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.HIDDEN);
                break;
            case 'i':
                if(ident.equals("if"))	         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.IF);
                if(ident.equals("imp"))   	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.IMP);
                if(ident.equals("in"))   	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.IN);
                if(ident.equals("inner"))	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.INNER);
                if(ident.equals("inspect")) 	 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.INSPECT);
                if(ident.equals("integer"))	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.INTEGER);
                if(ident.equals("is"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.IS);
                break;
            case 'l':
                if(ident.equals("label"))        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.LABEL);
                if(ident.equals("le"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.LE);
                if(ident.equals("long"))         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.LONG);
                if(ident.equals("lt"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.LT);
                break;
            case 'n':
                if(ident.equals("name"))         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NAME);
                if(ident.equals("ne"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NE);
                if(ident.equals("new"))          return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NEW);
                if(ident.equals("none"))         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NONE);
                if(ident.equals("not"))          return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NOT);
                if(ident.equals("notext"))       return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.NOTEXT);
                break;
            case 'o':
                if(ident.equals("or"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.OR);
                if(ident.equals("or_else"))      return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.OR_ELSE);
                if(ident.equals("otherwise"))    return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.OTHERWISE);
                break;
            case 'p':
                if(ident.equals("prior"))        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.PRIOR);
                if(ident.equals("procedure"))    return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.PROCEDURE);
                if(ident.equals("protected"))    return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.PROTECTED);
                break;
            case 'q':
                if(ident.equals("qua"))          return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.QUA);
                break;
            case 'r':
                if(ident.equals("reactivate"))   return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.REACTIVATE);
                if(ident.equals("real"))         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.REAL);
                if(ident.equals("ref"))          return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.REF);
                break;
            case 's':
                if(ident.equals("short"))  		 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.SHORT);
                if(ident.equals("step"))   		 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.STEP);
                if(ident.equals("switch")) 		 return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.SWITCH);
                break;
            case 't':
                if(ident.equals("text"))  	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.TEXT);
                if(ident.equals("then"))  	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.THEN);
                if(ident.equals("this"))   	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.THIS);
                if(ident.equals("to"))           return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.TO);
                if(ident.equals("true"))   	     return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.TRUE);
                break;
            case 'u':
                if(ident.equals("until"))        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.UNTIL);
                break;
            case 'v':
                if(ident.equals("value"))        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.VALUE);
                if(ident.equals("virtual"))      return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.VIRTUAL);
                break;
            case 'w':
                if(ident.equals("when"))         return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.WHEN);
                if(ident.equals("while"))        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.WHILE);
                break;
        }
//        return(new Identifier(tokenStartLine, sourceText, tokenStartOffset, currentPosition, ident));
//        return new Identifier(tokenStartLine, sourceText, tokenStartOffset, currentPosition);
		return new LexToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.IDENTIFIER);
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
            Util.error("Integer number out of range: "+result);
        }
        return new IntegerConst(tokenStartLine, sourceText, tokenStartOffset, currentPosition, res);
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
            return new RealConst(tokenStartLine, sourceText, tokenStartOffset, currentPosition, Float.parseFloat(result));
        } catch(NumberFormatException e) {
            Util.error("Illegal number: "+result);
            return new RealConst(tokenStartLine, sourceText, tokenStartOffset, currentPosition, 0);
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
            if(doubleAmpersand) return new LongRealConst(tokenStartLine, sourceText, tokenStartOffset, currentPosition, Double.parseDouble(result));
            return new RealConst(tokenStartLine, sourceText, tokenStartOffset, currentPosition, Float.parseFloat(result));
        } catch(NumberFormatException e) {
            Util.error("Illegal number: "+result);
            return new RealConst(tokenStartLine, sourceText, tokenStartOffset, currentPosition, 0);
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
        char result=0;
        if(Global.TRACE_LEXER) Util.TRACE("scanCharacterConstant, "+edcurrent());
        Util.ASSERT((char)(current)=='\'',"Expecting a character quote '");
        if((isPrintable(getNext())) && current != '!') {
            result=(char)current; getNext();
        } else if(current == '!') {
            result=(char)scanPossibleIsoCode(); getNext();
        } else Util.error("Illegal character constant. "+edcurrent());

        if(current != '\'') {
            Util.error("Character constant is not terminated. "+edcurrent());
            backStep(1);
        }
        if(Global.TRACE_LEXER) Util.TRACE("END scanCharacterConstant, result='"+result+"', "+edcurrent());
        return new CharacterConst(tokenStartLine, sourceText, tokenStartOffset, currentPosition, result);
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
    private LexToken OLD_scanTextConstant() {
        if(Global.TRACE_LEXER) Util.TRACE("scanTextConstant, "+edcurrent());
        IO.println("SimulaLexer.scanTextConstant: BEGIN -----------------------------------------------------------------------");
        LOOP:while(true) {
            OLD_scanSimpleString();
            // Skip string-separators
            while(scanStringSeparator());
            if(Global.TRACE_LEXER) Util.TRACE("scanTextConstant(2): "+edcurrent());
            IO.println("scanTextConstant(2): "+edcurrent());
            if(current!='"') {
                backStep(1);
                break LOOP;
            }            
        }
        IO.println("SimulaLexer.scanTextConstant: END -----------------------------------------------------------------------");
        printQueue();
        IO.println("SimulaLexer.scanTextConstant: END -----------------------------------------------------------------------");
        return popToken();
    }
    private LexToken NEW_scanTextConstant() {
        if(Global.TRACE_LEXER) Util.TRACE("scanTextConstant, "+edcurrent());
        IO.println("SimulaLexer.scanTextConstant: BEGIN -----------------------------------------------------------------------");
        SimpleString first = NEW_scanSimpleString();
        String result = first.value;
        SimpleString last = null;
        int textStartOffset = tokenStartOffset;
        int textStartLine = tokenStartLine;
        LOOP:while(true) {
        	while(scanStringSeparator());
        	if(Global.TRACE_LEXER) Util.TRACE("scanTextConstant(2): "+edcurrent());
        	IO.println("scanTextConstant(2): "+edcurrent());
        	if(current!='"') {
        		backStep(1);
        		break LOOP;
        	} 
        	last = NEW_scanSimpleString();
            result += last.value;
        }
        if(last == null) {   	
            IO.println("SimulaLexer.scanTextConstant: END SINGLE STRING "+last+"-----------------------------------------------------------------------");
        	return first;
        }
        return new SimpleString(textStartLine, sourceText, textStartOffset, currentPosition, result);
        
//        LOOP:while(true) {
//            NEW_scanSimpleString();
//            // Skip string-separators
//            while(scanStringSeparator());
//            if(Global.TRACE_LEXER) Util.TRACE("scanTextConstant(2): "+edcurrent());
//            IO.println("scanTextConstant(2): "+edcurrent());
//            if(current!='"') {
//                backStep(1);
//                break LOOP;
//            }            
//        }
//        IO.println("SimulaLexer.scanTextConstant: END -----------------------------------------------------------------------");
//        printQueue();
//        IO.println("SimulaLexer.scanTextConstant: END -----------------------------------------------------------------------");
//        return popToken();
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
    private SimpleString NEW_scanSimpleString() {
        StringBuilder sb=new StringBuilder();
//        LOOP:while(getNext() != '"') {
//	    	switch(current) {
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
	                Util.warning("Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6"); 
	                break LOOP;
	        	case EOF_MARK:
	                Util.error("Text constant is not terminated.");
	                break LOOP;
                default:
                	if(! isWhitespace(current)) sb.append((char)current);
        	}
        }
        String result=sb.toString();
        if(Global.TRACE_LEXER) Util.TRACE("scanSimpleString: Result=\""+result+"\", "+edcurrent());
        return new SimpleString(tokenStartLine, sourceText, tokenStartOffset, currentPosition, result);
    }
    private void OLD_scanSimpleString() {
        StringBuilder sb=new StringBuilder();
//        LOOP:while(getNext() != '"') {
//	    	switch(current) {
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
	                Util.warning("Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6"); 
	                break LOOP;
	        	case EOF_MARK:
	                Util.error("Text constant is not terminated.");
	                break LOOP;
                default:
                	if(! isWhitespace(current)) sb.append((char)current);
        	}
        }
        String result=sb.toString();
        if(Global.TRACE_LEXER) Util.TRACE("scanSimpleString: Result=\""+result+"\", "+edcurrent());
        tokenQueueAdd("scanTextConstant", new SimpleString(tokenStartLine, sourceText, tokenStartOffset, currentPosition, result));
    }
    private LexToken scanSimpleString() {
        StringBuilder sb=new StringBuilder();
//      LOOP:while(getNext() != '"') {
//	    	switch(current) {
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
	                Util.warning("Illegal Text constant. Simple string span mutiple source lines. See Simula Standard 1.6"); 
	                break LOOP;
	        	case EOF_MARK:
	                Util.error("Text constant is not terminated.");
	                break LOOP;
              default:
              	if(! isWhitespace(current)) sb.append((char)current);
      	}
      }
      String result=sb.toString();
      if(Global.TRACE_LEXER) Util.TRACE("scanSimpleString: Result=\""+result+"\", "+edcurrent());
      return new SimpleString(tokenStartLine, sourceText, tokenStartOffset, currentPosition, result);
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
            LexToken cc=scanComment();
            tokenQueueAdd("scanStringSeparator", cc);
//            current=' ';
            return(true);
        } else if(Character.isLetter((char)current)) {
            String name=scanName();
            IO.println("SimulaLexer.scanStringSeparator: name=\""+name+'"');
            if(name.equalsIgnoreCase("COMMENT")) {
                LexToken cc=scanComment();
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
        if (Global.TRACE_LEXER) Util.TRACE("BEGIN scanComment, " + edcurrent());
        while ((getNext() != ';') && current != EOF_MARK)
            skipped.append((char) current);
        skipped.append((char) current);
        if (current == ';')
            current = ' '; // getNext();
        else {
            Util.error("Comment is not terminated with ';'.");
            backStep(1);
        }
        if (Global.TRACE_LEXER) Util.TRACE("END scanComment: " + edcurrent() + "  skipped=\"" + skipped + '"');
        if (Global.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.COMMENT);
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
        if (Global.TRACE_LEXER) Util.TRACE("BEGIN scanCommentToEndOfLine, " + edcurrent());
        while ((getNext() != '\n') && current != EOF_MARK)
            skipped.append((char) current);
        nextLineNumber++;
        skipped.append((char) current);
        if (Global.TRACE_LEXER) Util.TRACE("END scanCommentToEndOfLine: " + edcurrent() + "  skipped=\"" + skipped + '"');
        if (Global.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
        return new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.COMMENT);
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
    private static int SEQU=1;
    private static boolean TESTING_SCAN_END = false;//true;
    private LexToken scanEndComment() {
    	if(TESTING_SCAN_END) {
	        IO.println("\n\nSimulaLexer.scanEndComment ###########################################################################################");
	        snapShot("BEGIN SCAN_END_COMMENT("+(SEQU++)+")");
    	}
        LexToken endToken = new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.END);
        if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: endToken="+endToken);
        tokenStartOffset = endToken.endOffset;
        
        if (Global.TRACE_LEXER) Util.TRACE("scanEndComment, " + edcurrent());
        int firstLine = nextLineNumber;
        int lastLine = firstLine;
        int nPhrase = 0; // Number of comment phrases
        LOOP:while (getNext() != EOF_MARK) {
        	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: current="+current+":'"+(""+(char)current).replace("\r", "\\r").replace("\n", "\\n")+"'");
            if (current == 10) {
            	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT NEWLINE");
                nPhrase = mayBe_AddCommentToken_ToTokenQueue(nPhrase);
                tokenQueueAdd("scanEndComment - NEWLINE", KeyWord.NEWLINE);
            	tokenStartLine++;
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
                    
                	if(TESTING_SCAN_END) snapShot("LexToken.scanEndComment: AFTER SCAN NAME:");
                    if(currentPosition > tokenStartOffset) {
                    	tokenQueueAdd("scanEndComment - NAME", KeyWord.COMMENT);
                    }
                    break LOOP;
                }
            } else {
            	if(TESTING_SCAN_END) IO.println("LexToken.scanEndComment: GOT OTHER="+current+":'"+(""+(char)current).replace("\r", "\\r").replace("\n", "\\n")+"'");
                lastLine = nextLineNumber;
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
        return endToken;
    }
    
	public void tokenQueueAdd(String debugName, int keyWord) {
		LexToken token = new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, keyWord);
		if(TESTING_SCAN_END) IO.println("SimulaLexer.tokenQueueAdd: "+debugName+" "+token);
	    tokenQueue.add(token);
	    tokenStartOffset = currentPosition;
	    currentLexerToken = token;
	//    advance();
	//    System.err.println("SimulaLexer.tokenQueueAdd: DENNE SKAL IKKE BRUKES - SKRIV OM: " + token);
	}
    
	public void tokenQueueAdd(String debugName, LexToken token) {
		if(TESTING_SCAN_END) IO.println("SimulaLexer.tokenQueueAdd: "+debugName+" "+token);
	    tokenQueue.add(token);
	    tokenStartOffset = currentPosition;
	    currentLexerToken = token;
	//    advance();
	//    System.err.println("SimulaLexer.tokenQueueAdd: DENNE SKAL IKKE BRUKES - SKRIV OM: " + token);
	}
   
    private int mayBe_AddCommentToken_ToTokenQueue(int nPhrase) {
        currentPosition--;
        if(currentPosition > tokenStartOffset) {
        	tokenQueueAdd("scanEndComment - SEMICOLON", new KeyWordToken(tokenStartLine, sourceText, tokenStartOffset, currentPosition, KeyWord.COMMENT));
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
    
    private LexToken popToken() {
    	LexToken token = tokenQueue.pop();
        tokenStartOffset = token.startOffset;
        tokenEndOffset = token.endOffset;
        currentPosition = tokenEndOffset;
//        IO.println("SimulaLexer.popToken: "+token);
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
