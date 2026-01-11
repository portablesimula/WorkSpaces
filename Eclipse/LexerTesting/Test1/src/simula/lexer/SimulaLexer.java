package simula.lexer;

import java.util.LinkedList;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import simula.compiler.utilities.KeyWord;
import simula.lang.SimulaLanguage;
import testing.util.Global;
import testing.util.Util;



public class SimulaLexer extends LexerBase {
    // Define custom token types (for a real plugin, these would be in a custom *TokenTypes class)
    public static final IElementType WORD = new IElementType("WORD", SimulaLanguage.INSTANCE);

    private static final int DEBUG = 1;// 2;

    private CharSequence sourceText;
    private int textEndOffset;
    private int currentPosition;

    private IElementType tokenElementType;
    private int tokenStartOffset;
    private int tokenEndOffset;

    /// ISO EM(EndMedia) character used to denote end-of-input
    private final static int EOF_MARK=25;

    /// The Token queue. The method nextToken will pick Tokens from the queue first.
//    private LinkedList<IElementType> tokenQueue=new LinkedList<IElementType>();
//    private LinkedList<QueueToken> tokenQueue=new LinkedList<QueueToken>();
    private LinkedList<SimulaToken> tokenQueue=new LinkedList<SimulaToken>();

    @Override
    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        sourceText = buffer;
        textEndOffset = endOffset;
        currentPosition = startOffset;
        tokenStartOffset = startOffset;
        tokenEndOffset = startOffset;
        // In a real incremental lexer, initialState would be used to restore context
        // For this simple example, we assume we always start from the beginning (initialState 0)
        advance();
    }

    @Override
    public void advance() {

        printQueue();
        
        if(! tokenQueue.isEmpty()) {
//
//            printQueue();
//            
//        	QueueToken token = tokenQueue.pop();
//        	System.out.println("SimulaLexer.scanBasic: QUEUED: "+token);
//        	Util.IERR();
            IElementType qtoken = popToken();
        	System.out.println("SimulaLexer.advance: QUEUED: "+qtoken);
            return;
        }
//        if(DEBUG > 1) System.out.println("\nSimulaLexer.advance:"+" currentPosition="+currentPosition
//                +", textEndOffset="+textEndOffset+", tokenStartOffset="+tokenStartOffset+", tokenEndOffset="+tokenEndOffset);
//        if(DEBUG > 0)  {
//            CharSequence txt = sourceText.subSequence(tokenStartOffset, tokenEndOffset);
//            System.out.println("SimulaLexer.advance: BEFORE "+tokenElementType+'['+tokenStartOffset+':'+tokenEndOffset+"]=\""+txt+"\"\n");
////            Thread.dumpStack();
//        }
        if (currentPosition >= textEndOffset) {
            tokenElementType = null;
            System.out.println("SimulaLexer.advance: EOF ");
            return;
        }
        tokenStartOffset = currentPosition;
        tokenElementType = scanBasic();
        tokenEndOffset = currentPosition;
        
        if(DEBUG > 0)  {
            CharSequence txt = sourceText.subSequence(tokenStartOffset, tokenEndOffset);
            txt = txt.toString().replace("\n","\\n").replace("\r","");
//            txt = txt.toString().replace("\n","..");
            System.out.println("SimulaLexer.advance: LINE "+Global.sourceLineNumber+" AFTER "+tokenElementType+'['+tokenStartOffset+':'+tokenEndOffset+"]=\""+txt+"\"");
//            Thread.dumpStack();
        }
       
        if(tokenStartOffset == tokenEndOffset) {
            CharSequence xxx = sourceText.subSequence(tokenStartOffset-10, tokenStartOffset);
//            Util.TRACE("SimulaLexer.advance: start="+tokenStartOffset+", end="+tokenEndOffset+", type="+tokenElementType);
            throw new RuntimeException("SimulaLexer.advance: start="+tokenStartOffset+", end="+tokenEndOffset+", type="+tokenElementType+"  "+xxx);
        }
    }

    /**
     * Returns the buffer sequence over which the lexer is running. This method should return the
     * same buffer instance which was passed to the {@code start()} method.
     *
     * @return the lexer buffer.
     */
    @Override
    public CharSequence getBufferSequence() {
        throw new RuntimeException("SimulaLexer.getBufferSequence: \""+sourceText+'"');
//        return sourceText;
    }

    /**
     * Returns the offset at which the lexer will stop lexing. This method should return
     * the length of the buffer or the value passed in the {@code endOffset} parameter
     * to the {@code start()} method.
     *
     * @return the lexing end offset
     */
    @Override
    public int getBufferEnd() {
        throw new RuntimeException("SimulaLexer.getBufferEnd: ");
//        return textEndOffset;
    }

    @Override
    public IElementType getTokenType() {
        if(DEBUG > 1) System.out.println("SimulaLexer.getTokenType: "+tokenElementType);
        return tokenElementType;
    }

    @Override
    public int getTokenStart() {
        if(DEBUG > 1) System.out.println("SimulaLexer.getTokenStart: "+tokenStartOffset);
        return tokenStartOffset;
    }

    @Override
    public int getTokenEnd() {
        if(DEBUG > 1) System.out.println("SimulaLexer.getTokenEnd: "+tokenEndOffset);
        return tokenEndOffset;
    }

    @Override
    public int getState() {
        // State is represented by a single integer number
        // For this simple, stateless lexer, we return 0
        return 0;
    }

    private boolean isWhitespace(int c) {
    	if(Character.isWhitespace(c)) {
    		if(c == '\n') {
    			Global.sourceLineNumber++;
    			System.out.println("SimulaLexer.isWhitespace: NEW LINE "+Global.sourceLineNumber+" ......................................................");
//    			Util.IERR();
    		}
    		return true;
    	}
    	return false;
    }
    private int pardepth;
    //********************************************************************************
    //**	                                                                 scanBasic
    //********************************************************************************
    /// Scan basic Token
    /// @return next Token
    private IElementType scanBasic() {
        if(Global.TRACE_SCAN) Util.TRACE("SimulaLexer.scanBasic: "+edcurrent());
//        System.out.println("SimulaLexer.scanBasic: "+edcurrent());
        while(true)	{
            getNext(); if(current == EOF_MARK) return(null);
            
            if(Character.isLetter(current)) return(scanIdentifier());

            if (isWhitespace(current)) {
                while (currentPosition < textEndOffset && isWhitespace(sourceText.charAt(currentPosition))) {
                    currentPosition++;
                }
                return TokenType.WHITE_SPACE;
            }

            switch(current) {

                case '%': return scanCommentToEndOfLine();
//	            case '%': return scanDirectiveLine();


                case '0':case '1':case '2':case '3':case '4':
                case '5':case '6':case '7':case '8':case '9': return scanNumber();

                case '-':
                    if(getNext() == '-')   return scanCommentToEndOfLine();
                    pushBack(current); 	   return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.MINUS);

                case '.':
                    if(Character.isDigit(getNext())) { return(scanDotDigit(new StringBuilder())); }
                    pushBack(current);     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.DOT);

                case '=':
                    if(getNext() == '=')   return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.EQR);
                    if(current == '/')
                        if(getNext() == '=')   return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.NER);
                        else Util.error("Illegal character combination ="+(char)current);
                    pushBack(current);     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.EQ);

                case '>':
                    if(getNext() == '=')   return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.GE);
                    pushBack(current);     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.GT);

                case '<':
                    if(getNext() == '=')   return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.LE);
                    pushBack(current);     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.LT);

                case '/':
                    if(getNext() == '/')   return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.INTDIV);
                    pushBack(current);     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.DIV);

                case ':':
                    if(getNext() == '=')   return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.ASSIGNVALUE);
                    if(getNext() == '-' && pardepth == 0) return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.ASSIGNREF);
                    pushBack(current);     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.COLON);

                case '&':
                    if(getNext()=='&' || current=='-' || current=='+' || Character.isDigit(current))
                        return (scanDigitsExp(null));
                    pushBack(current); return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.AMPERSAND);
                case '!':  return(scanComment());
                case '\'': return(scanCharacterConstant());
                case '"': return(scanTextConstant());

                case '+': return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.PLUS);
                case '*': return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.MUL);
                case ',': return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.COMMA);
                case ';': return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.SEMICOLON);
                case '(': pardepth++; return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.BEGPAR);
                case ')': pardepth--; return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.ENDPAR);
                case '[': return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.BEGBRACKET);
                case ']': return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.ENDBRACKET);

//	            case '\n':			/* NL (LF) */
//	    	      if (editorMode) return SimulaTokenType.NEWLINE,Global.sourceLineNumber+1));
//	            case ' ':
//	            case '\b':			/* BS */
//	            case '\t':			/* HT */
//	           	//case '\v':		/* VT */
//	            case '\f':			/* FF */
//	            case '\r':			/* CR */
//	            	break;

                default:
                    return TokenType.BAD_CHARACTER;

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
    private IElementType scanIdentifier() {
        String name=scanName();
        if(Global.TRACE_SCAN) Util.TRACE("scanIdentifier: name=\""+name+"\"");
        String ident=(Global.CaseSensitive)?name:name.toLowerCase();
        switch(Character.toLowerCase(ident.charAt(0))) {
            case 'a':
                if(ident.equals("activate"))     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.ACTIVATE);
                if(ident.equals("after"))	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.AFTER);
                if(ident.equals("and"))			 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.AND);
                if(ident.equals("and_then"))	 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.AND_THEN);
                if(ident.equals("array"))	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.ARRAY);
                if(ident.equals("at"))		     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.AT);
                break;
            case 'b':
                if(ident.equals("before"))       return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.BEFORE);
                if(ident.equals("begin"))        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.BEGIN);
                if(ident.equals("boolean"))      return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.BOOLEAN);
                break;
            case 'c':
                if(ident.equals("character"))	 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.CHARACTER);
                if(ident.equals("class"))        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.CLASS);
                if(ident.equals("comment"))      return scanComment();
                break;
            case 'd':
                if(ident.equals("delay"))   	 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.DELAY);
                if(ident.equals("do")) 	    	 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.DO);
                break;
            case 'e':
                if(ident.equals("else"))         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.ELSE);

	        	if(ident.equals("end"))   	     return scanEndComment();
//                if(ident.equals("end"))   	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.END;

                if(ident.equals("eq"))	         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.EQ);
                if(ident.equals("eqv"))	         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.EQV);
                if(ident.equals("external"))     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.EXTERNAL);
                break;
            case 'f':
                if(ident.equals("false"))  	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.FALSE);
                if(ident.equals("for"))    	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.FOR);
                break;
            case 'g':
                if(ident.equals("ge"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.GE);
                if(ident.equals("go"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.GO);
                if(ident.equals("goto"))         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.GOTO);
                if(ident.equals("gt"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.GT);
                break;
            case 'h':
                if(ident.equals("hidden"))       return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.HIDDEN);
                break;
            case 'i':
                if(ident.equals("if"))	         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.IF);
                if(ident.equals("imp"))   	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.IMP);
                if(ident.equals("in"))   	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.IN);
                if(ident.equals("inner"))	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.INNER);
                if(ident.equals("inspect")) 	 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.INSPECT);
                if(ident.equals("integer"))	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.INTEGER);
                if(ident.equals("is"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.IS);
                break;
            case 'l':
                if(ident.equals("label"))        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.LABEL);
                if(ident.equals("le"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.LE);
                if(ident.equals("long"))         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.LONG);
                if(ident.equals("lt"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.LT);
                break;
            case 'n':
                if(ident.equals("name"))         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.NAME);
                if(ident.equals("ne"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.NE);
                if(ident.equals("new"))          return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.NEW);
                if(ident.equals("none"))         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.NONE);
                if(ident.equals("not"))          return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.NOT);
                if(ident.equals("notext"))       return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.NOTEXT);
                break;
            case 'o':
                if(ident.equals("or"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.OR);
                if(ident.equals("or_else"))      return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.OR_ELSE);
                if(ident.equals("otherwise"))    return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.OTHERWISE);
                break;
            case 'p':
                if(ident.equals("prior"))        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.PRIOR);
                if(ident.equals("procedure"))    return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.PROCEDURE);
                if(ident.equals("protected"))    return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.PROTECTED);
                break;
            case 'q':
                if(ident.equals("qua"))          return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.QUA);
                break;
            case 'r':
                if(ident.equals("reactivate"))   return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.REACTIVATE);
                if(ident.equals("real"))         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.REAL);
                if(ident.equals("ref"))          return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.REF);
                break;
            case 's':
                if(ident.equals("short"))  		 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.SHORT);
                if(ident.equals("step"))   		 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.STEP);
                if(ident.equals("switch")) 		 return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.SWITCH);
                break;
            case 't':
                if(ident.equals("text"))  	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.TEXT);
                if(ident.equals("then"))  	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.THEN);
                if(ident.equals("this"))   	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.THIS);
                if(ident.equals("to"))           return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.TO);
                if(ident.equals("true"))   	     return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.TRUE);
                break;
            case 'u':
                if(ident.equals("until"))        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.UNTIL);
                break;
            case 'v':
                if(ident.equals("value"))        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.VALUE);
                if(ident.equals("virtual"))      return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.VIRTUAL);
                break;
            case 'w':
                if(ident.equals("when"))         return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.WHEN);
                if(ident.equals("while"))        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.WHILE);
                break;
        }
        return(new Identifier(sourceText, tokenStartOffset, currentPosition, ident));
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
    private IElementType scanNumber() {
        int radix=10;
        char firstChar=(char)current;
        if(Global.TRACE_SCAN) Util.TRACE("scanNumber, "+edcurrent());
        Util.ASSERT(Character.isDigit((char)(current)),"scanNumber:Expecting a Digit");
        StringBuilder number=new StringBuilder();

        number.append((char)current);
        if(getNext() == 'R' && (firstChar == '2' | firstChar == '4' | firstChar == '8')) {
            radix=firstChar - '0';
            if(Global.TRACE_SCAN) Util.TRACE("scanNumber, radix="+radix);
            number.setLength(0);
        } else if(firstChar == '1' && current == '6') {
            number.append((char)current);
            if(getNext() == 'R') {
                radix=16;
                if(Global.TRACE_SCAN) Util.TRACE("scanNumber, radix="+radix);
                number.setLength(0);
            } else pushBack(current);
        } else pushBack (current);

        while ((radix==16 ? isHexDigit(getNext()) : Character.isDigit(getNext())) || current=='_')
            if(current!='_') number.append((char)current);

        if(current == '.' && radix == 10) { getNext(); return(scanDotDigit(number)); }

        if(current == '&' && radix == 10) { getNext(); return(scanDigitsExp(number)); }

        String result=number.toString(); number=null;
        if(Global.TRACE_SCAN) Util.TRACE("scanNumber, result='"+result+"' radix="+radix);

        pushBack(current);
        @SuppressWarnings("unused")
        long res = 0;
        try {
            res=Integer.parseInt(result,radix);
        } catch (NumberFormatException e) {
            Util.error("Integer number out of range: "+result);
        }
        return new IntegerConst(sourceText, tokenStartOffset, currentPosition, res);
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
    private IElementType scanDotDigit(StringBuilder number) {
        if(Global.TRACE_SCAN) Util.TRACE("scanDotDigit, "+edcurrent());
        number.append('.');
        if(Character.isDigit(current)) number.append((char)current);
        while(Character.isDigit(getNext()) || current == '_')
            if(current != '_') number.append((char)current);

        if(current == '&') { getNext(); return(scanDigitsExp(number)); }

        String result=number.toString(); number=null;
        if(Global.TRACE_SCAN) Util.TRACE("scanDotDigit, result='"+result);
        pushBack(current);
        try {
            return new RealConst(sourceText, tokenStartOffset, currentPosition, Float.parseFloat(result));
        } catch(NumberFormatException e) {
            Util.error("Illegal number: "+result);
            return new RealConst(sourceText, tokenStartOffset, currentPosition, 0);
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
    private IElementType scanDigitsExp(StringBuilder number) {
        String result;
        @SuppressWarnings("unused")
        boolean doubleAmpersand=false;
        if(Global.TRACE_SCAN) Util.TRACE("scanDigitsExp, "+edcurrent());
        if(number==null) { number=new StringBuilder(); number.append('1'); }
        if(current == '&') { getNext(); doubleAmpersand=true; }
        number.append('e');
        if(current == '-') { number.append('-'); getNext(); }
        else if(current == '+') getNext();
        if(Character.isDigit(current)) number.append((char)current);
        while(Character.isDigit(getNext()) || current == '_') number.append((char)current);

        result=number.toString(); number=null;
        if(Global.TRACE_SCAN) Util.TRACE("scanDigitsExp, result='"+result);
        pushBack(current);
        try {
            if(doubleAmpersand) return new RealConst(sourceText, tokenStartOffset, currentPosition, Double.parseDouble(result));
            return new RealConst(sourceText, tokenStartOffset, currentPosition, Float.parseFloat(result));
        } catch(NumberFormatException e) {
            Util.error("Illegal number: "+result);
            return new RealConst(sourceText, tokenStartOffset, currentPosition, 0);
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
        if(Global.TRACE_SCAN) Util.TRACE("scanName, "+edcurrent());
        Util.ASSERT(Character.isLetter((char)(current)),"Expecting a Letter");
        name.append((char)current);
        while ((Character.isLetter(getNext()) || Character.isDigit(current) || current == '_'))
            name.append((char)current);
        pushBack(current);
        if(Global.TRACE_SCAN) Util.TRACE("scanName, name="+name+",current="+edcurrent());
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
    private IElementType scanCharacterConstant() {
        char result=0;
        if(Global.TRACE_SCAN) Util.TRACE("scanCharacterConstant, "+edcurrent());
        Util.ASSERT((char)(current)=='\'',"Expecting a character quote '");
        if((isPrintable(getNext())) && current != '!') {
            result=(char)current; getNext();
        } else if(current == '!') {
            result=(char)scanPossibleIsoCode(); getNext();
        } else Util.error("Illegal character constant. "+edcurrent());

        if(current != '\'') {
            Util.error("Character constant is not terminated. "+edcurrent());
            pushBack(current);
        }
        if(Global.TRACE_SCAN) Util.TRACE("END scanCharacterConstant, result='"+result+"', "+edcurrent());
        return new CharacterConst(sourceText, tokenStartOffset, currentPosition, result);
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
    private IElementType scanTextConstant() {
        if(Global.TRACE_SCAN) Util.TRACE("scanTextConstant, "+edcurrent());
        System.out.println("SimulaLexer.scanTextConstant: BEGIN -----------------------------------------------------------------------");
        LOOP:while(true) {
            scanSimpleString();
            // Skip string-separators
            while(scanStringSeparator());
            if(Global.TRACE_SCAN) Util.TRACE("scanTextConstant(2): "+edcurrent());
            System.out.println("scanTextConstant(2): "+edcurrent());
            if(current!='"') {
                pushBack(current);
                break LOOP;
            }            
        }
        System.out.println("SimulaLexer.scanTextConstant: END -----------------------------------------------------------------------");
        printQueue();
        System.out.println("SimulaLexer.scanTextConstant: END -----------------------------------------------------------------------");
        return popToken();
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
    private void scanSimpleString() {
        StringBuilder sb=new StringBuilder();
        LOOP:while(getNext() != '"') {
        	switch(current) {
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
        if(Global.TRACE_SCAN) Util.TRACE("scanSimpleString: Result=\""+result+"\", "+edcurrent());
        tokenQueueAdd("scanTextConstant", new SimpleString(sourceText, tokenStartOffset, currentPosition, result));
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
        System.out.println("SimulaLexer.scanStringSeparator: " + edcurrent());
        if(current=='!') {
            SimulaToken cc=scanComment();
            tokenQueueAdd("scanStringSeparator", cc);
//            current=' ';
            return(true);
        } else if(Character.isLetter((char)current)) {
            String name=scanName();
            System.out.println("SimulaLexer.scanStringSeparator: name=\""+name+'"');
            if(name.equalsIgnoreCase("COMMENT")) {
                SimulaToken cc=scanComment();
                tokenQueueAdd("scanStringSeparator", cc);
//                current=' ';
                return(true);
            } else pushBack(name);
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
        if (Global.TRACE_SCAN) Util.TRACE("scanPossibleIsoCode, " + edcurrent());
        Util.ASSERT((char) (current) == '!', "Expecting a character !");
        if (Character.isDigit(getNext())) {
            firstchar = (char) current;
            if (Character.isDigit(getNext())) {
                secondchar = (char) current;
                if (Character.isDigit(getNext())) {
                    thirdchar = (char) current;
                    if (getNext() == '!') { // ! digit digit digit ! Found
                        int value = (((firstchar - '0') * 10 + secondchar - '0') * 10 + thirdchar - '0');
                        if (Global.TRACE_SCAN)
                            Util.TRACE("scanPossibleIsoCode:Got three digits: "+(char)firstchar+(char)secondchar+(char)thirdchar+"value="+value);
                        if (value < 256)
                            return (value);
                        Util.warning("ISO-Code " + value + " is out of range (0:255)"
                                +" interpreted as an ordinary sequence of characters: !" +value + "!  See Simula Standard 1.6");
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
    private SimulaToken scanComment() {
        StringBuilder skipped = new StringBuilder();
        if (Global.TRACE_SCAN) Util.TRACE("BEGIN scanComment, " + edcurrent());
        while ((getNext() != ';') && current != EOF_MARK)
            skipped.append((char) current);
        skipped.append((char) current);
        if (current == ';')
            current = ' '; // getNext();
        else {
            Util.error("Comment is not terminated with ';'.");
            pushBack(current);
        }
        if (Global.TRACE_SCAN) Util.TRACE("END scanComment: " + edcurrent() + "  skipped=\"" + skipped + '"');
        if (Global.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.COMMENT);
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
    private IElementType scanCommentToEndOfLine() {
        StringBuilder skipped = new StringBuilder();
        if (Global.TRACE_SCAN) Util.TRACE("BEGIN scanCommentToEndOfLine, " + edcurrent());
        while ((getNext() != '\n') && current != EOF_MARK)
            skipped.append((char) current);
        skipped.append((char) current);
        if (Global.TRACE_SCAN) Util.TRACE("END scanCommentToEndOfLine: " + edcurrent() + "  skipped=\"" + skipped + '"');
        if (Global.TRACE_COMMENTS) Util.TRACE("COMMENT:\"" + skipped + "\" Skipped and replaced with a SPACE");
        return new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.COMMENT);
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
    private IElementType scanEndComment() {
        //Util.println("SimulaLexer.scanEndComment");
        tokenQueueAdd("scanEndComment", new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.END));
        
        StringBuilder skipped = new StringBuilder();
        if (Global.TRACE_SCAN) Util.TRACE("scanEndComment, " + edcurrent());
        int firstLine = Global.sourceLineNumber;
        int lastLine = firstLine;
        LOOP:while (getNext() != EOF_MARK) {
            if (current == ';') {
                if (Global.TRACE_COMMENTS) Util.TRACE("ENDCOMMENT:\"" + skipped + '"');
                if (firstLine < lastLine && (skipped.length() > 0))
                    Util.warning("END-Comment span mutiple source lines");
//				if(editorMode && accum.length()>0) tokenQueue.add(SimulaTokenType.COMMENT);
                currentPosition--;
                tokenQueueAdd("scanEndComment", new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.COMMENT));
                currentPosition++;
//                tokenQueueAdd(SimulaElementTypes.TEGN);
                tokenQueueAdd("scanEndComment", new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.SEMICOLON));
                break LOOP;
            } else if (Character.isLetter(current)) {
                String name = scanName();
                if (Util.equals(name, "end") || Util.equals(name, "else")
                        || Util.equals(name, "when") || Util.equals(name, "otherwise")) {
                    pushBack(name);
                    if (Global.TRACE_COMMENTS) Util.TRACE("END-COMMENT:\"" + skipped + '"');
                    if (firstLine < lastLine && (skipped.length() > 0))
                        Util.warning("END-Comment span mutiple source lines");
                    tokenQueueAdd("scanEndComment", new KeyWordToken(sourceText, tokenStartOffset, currentPosition, KeyWord.COMMENT));
                    break LOOP;
                }
                skipped.append(name); // lastLine=Global.sourceLineNumber;
            } else if (!Character.isWhitespace(current)) {
                skipped.append((char) current);
                lastLine = Global.sourceLineNumber;
            }
        }
        if (Global.TRACE_COMMENTS)
            Util.TRACE("ENDCOMMENT:\"" + skipped + '"');
        System.out.println("SimulaLexer.scanEndComment: END -----------------------------------------------------------------------");
        printQueue();
        System.out.println("SimulaLexer.scanEndComment: END -----------------------------------------------------------------------");
        return popToken();
    }
    
    private void tokenQueueAdd(String debugString, SimulaToken token) {
        System.out.println("SimulaLexer.tokenQueueAdd: "+debugString+": "+token);
        tokenQueue.add(token);
        tokenStartOffset = currentPosition;
    }
    
    private SimulaToken popToken() {
    	SimulaToken token = tokenQueue.pop();
        tokenStartOffset = token.startOffset;
        tokenEndOffset = token.endOffset;
        currentPosition = tokenEndOffset;
        System.out.println("SimulaLexer.popToken: "+token);
        return(token);    	
    }
    
    private void printQueue() {
    	for(SimulaToken token:tokenQueue) {
        	System.out.println("SimulaLexer.printQueue: token="+token);
   		
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

    /// Push a character onto the puchBackStack.
    /// @param chr character to be pushed
    private void pushBack(final int chr) {
        if(current != EOF_MARK) currentPosition--;
    }


    /// Push a string onto the puchBackStack.
    /// @param s string to be pushed
    private void pushBack(final String s) {
        // put given value back into the input stream
        int i=s.length();
        while((i--)>0) pushBack(s.charAt(i));
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
