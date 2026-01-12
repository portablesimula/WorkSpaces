package simula.lexer;

import java.util.LinkedList;


import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import simula.lang.SimulaLanguage;
import simula.plugin.util.Global;
import simula.plugin.util.Util;

public class SimulaLexer extends LexerBase {

    // Define custom token types (for a real plugin, these would be in a custom *TokenTypes class)
    public static final IElementType WORD = new IElementType("WORD", SimulaLanguage.INSTANCE);

    private static final int DEBUG = 0;// 1;// 2;

    private CharSequence sourceText;
    private int textEndOffset;
    private int currentPosition;

    private IElementType tokenElementType;
    private int tokenStartOffset;
    private int tokenEndOffset;

    /// ISO EM(EndMedia) character used to denote end-of-input
    private final static int EOF_MARK=25;

    /// The Token queue. The method nextToken will pick Tokens from the queue first.
    private LinkedList<IElementType> tokenQueue=new LinkedList<IElementType>();

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
        if(DEBUG > 1) System.out.println("\nSimulaLexer.advance:"+" currentPosition="+currentPosition
                +", textEndOffset="+textEndOffset+", tokenStartOffset="+tokenStartOffset+", tokenEndOffset="+tokenEndOffset);
        if(DEBUG > 0)  {
            CharSequence txt = sourceText.subSequence(tokenStartOffset, tokenEndOffset);
            System.out.println("SimulaLexer.advance: "+tokenElementType+'['+tokenStartOffset+':'+tokenEndOffset+"]=\""+txt+"\"\n");
        }
        if (currentPosition >= textEndOffset) {
            tokenElementType = null;
            return;
        }
        tokenStartOffset = currentPosition;
        tokenElementType = scanBasic();
        tokenEndOffset = currentPosition;
        if(tokenStartOffset == tokenEndOffset) {
            CharSequence xxx = sourceText.subSequence(tokenStartOffset-300, tokenStartOffset);
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

    private int pardepth;
    //********************************************************************************
    //**	                                                                 scanBasic
    //********************************************************************************
    /// Scan basic Token
    /// @return next Token
    private IElementType scanBasic() {
        if(Global.TRACE_SCAN) Util.TRACE("SimulaScanner.scanBasic, "+edcurrent());
        while(true)	{
            getNext(); if(current == EOF_MARK) return(null);

            if(Character.isLetter(current)) return(scanIdentifier());

            if (Character.isWhitespace(current)) {
                while (currentPosition < textEndOffset && Character.isWhitespace(sourceText.charAt(currentPosition))) {
                    currentPosition++;
                }
                return TokenType.WHITE_SPACE;
            }

            switch(current) {

                case '%': return(scanCommentToEndOfLine());


                case '0':case '1':case '2':case '3':case '4':
                case '5':case '6':case '7':case '8':case '9': return scanNumber();

                case '-':
                    if(getNext() == '-')   return(scanCommentToEndOfLine());
                    pushBack(current); 	   return(KeyWordToken.MINUS);

                case '.':
                    if(Character.isDigit(getNext())) { return(scanDotDigit(new StringBuilder())); }
                    pushBack(current);     return KeyWordToken.DOT;

                case '=':
                    if(getNext() == '=')   return KeyWordToken.EQR;
                    if(current == '/')
                        if(getNext() == '=')   return KeyWordToken.NER;
                        else Util.error("Illegal character combination ="+(char)current);
                    pushBack(current);     return KeyWordToken.EQ;

                case '>':
                    if(getNext() == '=')   return KeyWordToken.GE;
                    pushBack(current);     return KeyWordToken.GT;

                case '<':
                    if(getNext() == '=')   return KeyWordToken.LE;
                    pushBack(current);     return KeyWordToken.LT;

                case '/':
                    if(getNext() == '/')   return KeyWordToken.INTDIV;
                    pushBack(current);     return KeyWordToken.DIV;

                case ':':
                    if(getNext() == '=')   return KeyWordToken.ASSIGNVALUE;
                    if(getNext() == '-' && pardepth == 0) return KeyWordToken.ASSIGNREF;
                    pushBack(current);     return KeyWordToken.COLON;

                case '&':
                    if(getNext()=='&' || current=='-' || current=='+' || Character.isDigit(current))
                        return (scanDigitsExp(null));
                    pushBack(current); return KeyWordToken.AMPERSAND;
                case '!':  return(scanComment());
                case '\'': return(scanCharacterConstant());
                case '"': return(scanTextConstant());

                case '+': return KeyWordToken.PLUS;
                case '*': return KeyWordToken.MUL;
                case ',': return KeyWordToken.COMMA;
                case ';': return KeyWordToken.SEMICOLON;
                case '(': pardepth++; return KeyWordToken.BEGPAR;
                case ')': pardepth--; return KeyWordToken.ENDPAR;
                case '[': return KeyWordToken.BEGBRACKET;
                case ']': return KeyWordToken.ENDBRACKET;

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
                if(ident.equals("activate"))     return(KeyWordToken.ACTIVATE);
                if(ident.equals("after"))	     return(KeyWordToken.AFTER);
                if(ident.equals("and"))			 return(KeyWordToken.AND);
                if(ident.equals("and_then"))	 return(KeyWordToken.AND_THEN);
                if(ident.equals("array"))	     return(KeyWordToken.ARRAY);
                if(ident.equals("at"))		     return(KeyWordToken.AT);
                break;
            case 'b':
                if(ident.equals("before"))       return(KeyWordToken.BEFORE);
                if(ident.equals("begin"))        return(KeyWordToken.BEGIN);
                if(ident.equals("boolean"))      return(KeyWordToken.BOOLEAN);
                break;
            case 'c':
                if(ident.equals("character"))	 return(KeyWordToken.CHARACTER);
                if(ident.equals("class"))        return(KeyWordToken.CLASS);
                if(ident.equals("comment"))      return(scanComment());
                break;
            case 'd':
                if(ident.equals("delay"))   	 return(KeyWordToken.DELAY);
                if(ident.equals("do")) 	    	 return(KeyWordToken.DO);
                break;
            case 'e':
                if(ident.equals("else"))         return(KeyWordToken.ELSE);

	        	if(ident.equals("end"))   	     return(scanEndComment());
//                if(ident.equals("end"))   	     return(KeyWord.END);

                if(ident.equals("eq"))	         return(KeyWordToken.EQ);
                if(ident.equals("eqv"))	         return(KeyWordToken.EQV);
                if(ident.equals("external"))     return(KeyWordToken.EXTERNAL);
                break;
            case 'f':
                if(ident.equals("false"))  	     return(KeyWordToken.FALSE);
                if(ident.equals("for"))    	     return(KeyWordToken.FOR);
                break;
            case 'g':
                if(ident.equals("ge"))           return(KeyWordToken.GE);
                if(ident.equals("go"))           return(KeyWordToken.GO);
                if(ident.equals("goto"))         return(KeyWordToken.GOTO);
                if(ident.equals("gt"))           return(KeyWordToken.GT);
                break;
            case 'h':
                if(ident.equals("hidden"))       return(KeyWordToken.HIDDEN);
                break;
            case 'i':
                if(ident.equals("if"))	         return(KeyWordToken.IF);
                if(ident.equals("imp"))   	     return(KeyWordToken.IMP);
                if(ident.equals("in"))   	     return(KeyWordToken.IN);
                if(ident.equals("inner"))	     return(KeyWordToken.INNER);
                if(ident.equals("inspect")) 	 return(KeyWordToken.INSPECT);
                if(ident.equals("integer"))	     return(KeyWordToken.INTEGER);
                if(ident.equals("is"))           return(KeyWordToken.IS);
                break;
            case 'l':
                if(ident.equals("label"))        return(KeyWordToken.LABEL);
                if(ident.equals("le"))           return(KeyWordToken.LE);
                if(ident.equals("long"))         return(KeyWordToken.LONG);
                if(ident.equals("lt"))           return(KeyWordToken.LT);
                break;
            case 'n':
                if(ident.equals("name"))         return(KeyWordToken.NAME);
                if(ident.equals("ne"))           return(KeyWordToken.NE);
                if(ident.equals("new"))          return(KeyWordToken.NEW);
                if(ident.equals("none"))         return(KeyWordToken.NONE);
                if(ident.equals("not"))          return(KeyWordToken.NOT);
                if(ident.equals("notext"))       return(KeyWordToken.NOTEXT);
                break;
            case 'o':
                if(ident.equals("or"))           return(KeyWordToken.OR);
                if(ident.equals("or_else"))      return(KeyWordToken.OR_ELSE);
                if(ident.equals("otherwise"))    return(KeyWordToken.OTHERWISE);
                break;
            case 'p':
                if(ident.equals("prior"))        return(KeyWordToken.PRIOR);
                if(ident.equals("procedure"))    return(KeyWordToken.PROCEDURE);
                if(ident.equals("protected"))    return(KeyWordToken.PROTECTED);
                break;
            case 'q':
                if(ident.equals("qua"))          return(KeyWordToken.QUA);
                break;
            case 'r':
                if(ident.equals("reactivate"))   return(KeyWordToken.REACTIVATE);
                if(ident.equals("real"))         return(KeyWordToken.REAL);
                if(ident.equals("ref"))          return(KeyWordToken.REF);
                break;
            case 's':
                if(ident.equals("short"))  		 return(KeyWordToken.SHORT);
                if(ident.equals("step"))   		 return(KeyWordToken.STEP);
                if(ident.equals("switch")) 		 return(KeyWordToken.SWITCH);
                break;
            case 't':
                if(ident.equals("text"))  	     return(KeyWordToken.TEXT);
                if(ident.equals("then"))  	     return(KeyWordToken.THEN);
                if(ident.equals("this"))   	     return(KeyWordToken.THIS);
                if(ident.equals("to"))           return(KeyWordToken.TO);
                if(ident.equals("true"))   	     return(KeyWordToken.TRUE);
                break;
            case 'u':
                if(ident.equals("until"))        return(KeyWordToken.UNTIL);
                break;
            case 'v':
                if(ident.equals("value"))        return(KeyWordToken.VALUE);
                if(ident.equals("virtual"))      return(KeyWordToken.VIRTUAL);
                break;
            case 'w':
                if(ident.equals("when"))         return(KeyWordToken.WHEN);
                if(ident.equals("while"))        return(KeyWordToken.WHILE);
                break;
        }
        return(new Identifier(ident, ident));
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
        return(new IntegerConst(result, res));
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
            return new RealConst(result, Float.parseFloat(result));
        } catch(NumberFormatException e) {
            Util.error("Illegal number: "+result);
            return new RealConst(result,0);
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
            if(doubleAmpersand) return new RealConst(result, Double.parseDouble(result));
            return new RealConst(result, Float.parseFloat(result));
        } catch(NumberFormatException e) {
            Util.error("Illegal number: "+result);
            return new RealConst(result,0);
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
        return new CharacterConst(""+((char)result), result);
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
        StringBuilder accumulatedTextConstant=new StringBuilder();
        LOOP:while(true) {
            int firstLine= Global.sourceLineNumber;
            int lastLine=firstLine;
            // Scan simple-string:
            while(getNext() != '"') {
                if(current=='!') {
                    int code=scanPossibleIsoCode();
                    accumulatedTextConstant.append((char)code);
                }
                else if(current == EOF_MARK) {
                    Util.error("Text constant is not terminated.");
                    String result=accumulatedTextConstant.toString(); accumulatedTextConstant=null;
                    if(Global.TRACE_SCAN) Util.TRACE("scanTextConstant(1): Result=\""+result+"\", "+edcurrent());
//                    tokenQueue.add(SimulaElementTypes.TEXTCONST);
                    tokenQueue.add(new TextConst(result, result));
                    break LOOP;
                } else accumulatedTextConstant.append((char)current);
            }
            tokenQueue.add(SimulaElementTypes.STRING);
//            tokenQueue.add(new TextConst(result, result));
            lastLine=Global.sourceLineNumber;
            if(getNext() == '"') {
                accumulatedTextConstant.append('"');
                lastLine=Global.sourceLineNumber;
            } else {
                // Skip string-separator
                while(currentIsStringSeparator()) getNext();
                if(Global.TRACE_SCAN) Util.TRACE("scanTextConstant(2): "+edcurrent());
                if(current!='"') {
                    pushBack(current);
                    String result=accumulatedTextConstant.toString(); accumulatedTextConstant=null;
                    if(Global.TRACE_SCAN) Util.TRACE("scanTextConstant(2): Result=\""+result+"\", "+edcurrent());
                    if(firstLine<lastLine)
                        Util.warning("Illegal Text constant. Simple string span mutiple source lines ("+firstLine+':'+lastLine+"). See Simula Standard 1.6");
//                    tokenQueue.add(SimulaElementTypes.TEXTCONST);
                    tokenQueue.add(new TextConst(result, result));
                    break LOOP;
                }
            }
        }
        IElementType result=tokenQueue.remove();
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
            IElementType cc=scanComment();
            tokenQueue.add(cc);
            current=' '; return(true);
        } else if(Character.isLetter((char)current)) {
            String name=scanName();
            if(name.equalsIgnoreCase("COMMENT")) {
                IElementType cc=scanComment();
                tokenQueue.add(cc);
                current=' '; return(true);
            } else pushBack(name);
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
    private IElementType scanComment() {
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
        return KeyWordToken.COMMENT;
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
        return KeyWordToken.COMMENT;
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
        //Util.println("SimulaScanner.scanEndComment");
        tokenQueue.add(KeyWordToken.COMMENT);
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
                tokenQueue.add(KeyWordToken.COMMENT);
                tokenQueue.add(SimulaElementTypes.TEGN);
                break LOOP;
            } else if (Character.isLetter(current)) {
                String name = scanName();
                if (Util.equals(name, "end") || Util.equals(name, "else")
                        || Util.equals(name, "when") || Util.equals(name, "otherwise")) {
                    pushBack(name);
                    if (Global.TRACE_COMMENTS) Util.TRACE("END-COMMENT:\"" + skipped + '"');
                    if (firstLine < lastLine && (skipped.length() > 0))
                        Util.warning("END-Comment span mutiple source lines");
                    tokenQueue.add(KeyWordToken.COMMENT); break LOOP;
                }
                skipped.append(name); // lastLine=Global.sourceLineNumber;
            } else if (!isWhiteSpace(current)) {
                skipped.append((char) current);
                lastLine = Global.sourceLineNumber;
            }
        }
        tokenQueue.add(KeyWordToken.COMMENT);

        if (Global.TRACE_COMMENTS)
            Util.TRACE("ENDCOMMENT:\"" + skipped + '"');
        IElementType res=tokenQueue.remove();
        return(res);
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

}
