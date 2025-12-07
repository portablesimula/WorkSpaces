/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.utilities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class Token {
	private String text;
	private int keyWord;
	private Object value;
	
	public final static Token AND       =new Token(KeyWord.AND);
	public final static Token ASSERT    =new Token(KeyWord.ASSERT);
	public final static Token BEGIN     =new Token(KeyWord.BEGIN);
	public final static Token BODY 	    =new Token(KeyWord.BODY);
	public final static Token BOOLEAN   =new Token(KeyWord.BOOLEAN);
	public final static Token CALL	    =new Token(KeyWord.CALL);
	public final static Token CASE		=new Token(KeyWord.CASE);
	public final static Token CHARACTER =new Token(KeyWord.CHARACTER);
	public final static Token CONST	    =new Token(KeyWord.CONST);
	public final static Token DEFINE    =new Token(KeyWord.DEFINE);
	public final static Token DO 	    =new Token(KeyWord.DO);
	public final static Token ELSE      =new Token(KeyWord.ELSE);
	public final static Token ELSIF     =new Token(KeyWord.ELSIF);
	public final static Token END   	=new Token(KeyWord.END);
	public final static Token ENDCASE   =new Token(KeyWord.ENDCASE);
	public final static Token ENDIF 	=new Token(KeyWord.ENDIF);
	public final static Token ENDMACRO  =new Token(KeyWord.ENDMACRO);
	public final static Token ENDREPEAT =new Token(KeyWord.ENDREPEAT);
	public final static Token ENDSKIP   =new Token(KeyWord.ENDSKIP);
	public final static Token ENTRY 	=new Token(KeyWord.ENTRY);
	public final static Token EQ	    =new Token(KeyWord.EQ);
	public final static Token EXIT	    =new Token(KeyWord.EXIT);
	public final static Token EXPORT	=new Token(KeyWord.EXPORT);
	public final static Token EXTERNAL  =new Token(KeyWord.EXTERNAL);
	public final static Token FALSE  	=new Token(KeyWord.SIMPLEVALUE,false);
	public final static Token FIELD  	=new Token(KeyWord.FIELD);
	public final static Token GE        =new Token(KeyWord.GE);
	public final static Token GLOBAL    =new Token(KeyWord.GLOBAL);
	public final static Token GOTO      =new Token(KeyWord.GOTO);
	public final static Token GT        =new Token(KeyWord.GT);
	public final static Token IF	    =new Token(KeyWord.IF);
	public final static Token IMPORT	=new Token(KeyWord.IMPORT);
	public final static Token INFIX   	=new Token(KeyWord.INFIX);
	public final static Token INFO   	=new Token(KeyWord.INFO);
	public final static Token INSERT 	=new Token(KeyWord.INSERT);
	public final static Token INTEGER	=new Token(KeyWord.INTEGER);
	public final static Token KNOWN		=new Token(KeyWord.KNOWN);
	public final static Token LABEL     =new Token(KeyWord.LABEL);
	public final static Token LE        =new Token(KeyWord.LE);
	public final static Token LONG      =new Token(KeyWord.LONG);
	public final static Token LSHIFTA   =new Token(KeyWord.LSHIFTA);
	public final static Token LSHIFTL   =new Token(KeyWord.LSHIFTL);
	public final static Token LT        =new Token(KeyWord.LT);
	public final static Token MACRO     =new Token(KeyWord.MACRO);
	public final static Token MODULE    =new Token(KeyWord.MODULE);
	public final static Token NAME      =new Token(KeyWord.NAME);
	public final static Token NE        =new Token(KeyWord.NE);
	public final static Token NOBODY    =new Token(KeyWord.NOBODY);
	public final static Token NOFIELD   =new Token(KeyWord.NOFIELD);
	public final static Token NONAME    =new Token(KeyWord.NONAME);
	public final static Token NONE      =new Token(KeyWord.NONE);
	public final static Token NOSIZE    =new Token(KeyWord.NOSIZE);
	public final static Token NOT       =new Token(KeyWord.NOT);
	public final static Token NOWHERE   =new Token(KeyWord.NOWHERE);
	public final static Token OR        =new Token(KeyWord.OR);
	public final static Token OTHERWISE =new Token(KeyWord.OTHERWISE);
	public final static Token PROFILE	=new Token(KeyWord.PROFILE);
	public final static Token QUA       =new Token(KeyWord.QUA);
	public final static Token RANGE		=new Token(KeyWord.RANGE);
	public final static Token RECORD    =new Token(KeyWord.RECORD);
	public final static Token REAL      =new Token(KeyWord.REAL);
	public final static Token REF       =new Token(KeyWord.REF);
	public final static Token REM       =new Token(KeyWord.REM);
	public final static Token REPEAT    =new Token(KeyWord.REPEAT);
	public final static Token ROUTINE   =new Token(KeyWord.ROUTINE);
	public final static Token RSHIFTA   =new Token(KeyWord.RSHIFTA);
	public final static Token RSHIFTL   =new Token(KeyWord.RSHIFTL);
	public final static Token SHORT  	=new Token(KeyWord.SHORT);
	public final static Token SIZE  	=new Token(KeyWord.SIZE);
	public final static Token SKIP  	=new Token(KeyWord.SKIP);
	public final static Token SYSINSERT	=new Token(KeyWord.SYSINSERT);
	public final static Token SYSROUTINE=new Token(KeyWord.SYSROUTINE);
	public final static Token SYSTEM 	=new Token(KeyWord.SYSTEM);
	public final static Token THEN  	=new Token(KeyWord.THEN);
	public final static Token TRUE   	=new Token(KeyWord.SIMPLEVALUE,true);
	public final static Token VAR	    =new Token(KeyWord.VAR);
	public final static Token VARIANT   =new Token(KeyWord.VARIANT);
	public final static Token VISIBLE   =new Token(KeyWord.VISIBLE);
	public final static Token WHEN      =new Token(KeyWord.WHEN);
	public final static Token WHILE     =new Token(KeyWord.WHILE);
	public final static Token XOR       =new Token(KeyWord.XOR);
	public final static Token IMP       =new Token(KeyWord.IMP);
	public final static Token EQV       =new Token(KeyWord.EQV);
		
	
	public enum StyleCode {regular,keyword,comment,constant,lineNumber};

	public Token(final String text,final int keyWord,final Object value) {
		this.text = text;
		this.keyWord = keyWord;
		this.value = value;
	}

	public Token(final String text,final int keyWord) {
		this(text, keyWord, null);
	}

	public Token(final int keyWord,final Object value) {
		this(null, keyWord, value);
	}

	public Token(final int keyWord) {
		this(keyWord, null);
	}

	public Token() {} // Externalization
	
	

	public void setText(final String text) {
		this.text = text;
	}

	public String getText() {
		if (text == null)
			return (toString());
		return (text);
	}

	public StyleCode getStyleCode() {
		switch(keyWord) {
		    case KeyWord.ASSIGN: case KeyWord.COMMA: case KeyWord.COLON:
		    case KeyWord.BEGPAR: case KeyWord.ENDPAR:
		    case KeyWord.EQ: case KeyWord.GE: case KeyWord.GT: case KeyWord.LE: case KeyWord.LT: case KeyWord.NE:
		    case KeyWord.PLUS: case KeyWord.MINUS: case KeyWord.MUL: case KeyWord.DIV:
		    case KeyWord.IDENTIFIER: case KeyWord.DOT:
		    	 return(Token.StyleCode.regular);
		    case KeyWord.SIMPLEVALUE:
		    	 return(Token.StyleCode.constant);
		    case KeyWord.COMMENT:
		    	 return(Token.StyleCode.comment);
		    default: return(Token.StyleCode.keyword);
		}
	}

    public String edKeyWord() { return(KeyWord.ed(keyWord)); }
    public int getKeyWord() { return(keyWord); }
    public void putValue(Object value) { this.value=value; }
    public Object getValue() { return(value); }
    public String getIdentifier() { return((String)value); }

	public boolean equals(final Object other) {
		Token othr=(Token)other;
		if(this.keyWord!=othr.keyWord) return(false);
		if(this.value==othr.value) return(true);
		if(this.value==null) return(false);
		if(othr.value==null) return(false);
	    if(!this.value.equals(othr.value)) return(false);
		return(true);
	}

	public String toString() {
		switch (keyWord) {
		    case KeyWord.COMMA: return (","); 
		    case KeyWord.COLON: return (":"); 
		    case KeyWord.PERCENT: return ("%"+value); 
			case KeyWord.BEGPAR: return ("("); 
			case KeyWord.ENDPAR: return (")"); 
			case KeyWord.DOT: return ("."); 
			case KeyWord.CONC: return ("&");
			  
			case KeyWord.EQ: return ("=");
			case KeyWord.GE: return (">=");
			case KeyWord.GT: return (">");
			case KeyWord.LE: return ("<=");
			case KeyWord.LT: return ("<");
			case KeyWord.NE: return ("<>");
			case KeyWord.PLUS: return ("+");
			case KeyWord.MINUS: return ("-");
			case KeyWord.MUL: return ("*");
			case KeyWord.DIV: return ("/");
			case KeyWord.ASSIGN: return (":=");
		
			case KeyWord.SIMPLEVALUE: return(""+value);
			case KeyWord.IDENTIFIER: return("Identifier "+value);
		
			case KeyWord.INTEGER: case KeyWord.REAL: {
				// Possible SHORT  or  LONG  in value part
			    String res=KeyWord.ed(keyWord); if(value!=null) res=value.toString()+' '+res;
			    return(res);
			} 
			default: return("KeyWord "+KeyWord.ed(keyWord));
		}
	}
	
	public String edToken() {
		return("Token: "+KeyWord.ed(keyWord)+", text="+text+", value="+value);
	}
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	private static final int vNull=0,vString=1,vLong=2,vBoolean=3;
	private int getValueKind(Object value) {
		if(value==null) return(vNull);
		if(value instanceof String) return(vString);
		else if(value instanceof Long) return(vLong);
		else if(value instanceof Boolean) return(vBoolean);
		else {
			Util.IERR("Token.getValueKind: Unrecognized Value kind: "+value.getClass().getSimpleName());
			return(0);
		}
	}

	public void writeToken(ObjectOutput oupt) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Token: "+this);
		oupt.writeObject(text);
		oupt.writeByte(keyWord);
		int kind=getValueKind(value);
		oupt.writeByte(kind);
		switch(kind) {
			case vNull: break;
			case vString: oupt.writeObject((String)value); break;
			case vLong: oupt.writeLong((Long)value); break;
		}
//		out.writeObject(val2);
		Util.TRACE_OUTPUT("END Write Token: "+this);
	}
	
	public static Token readToken(ObjectInput inpt) throws IOException, ClassNotFoundException {
		Token token=new Token();
		token.text=(String) inpt.readObject();
		token.keyWord=inpt.readUnsignedByte();
		int kind=inpt.readUnsignedByte();
		switch(kind) {
			case vNull: break;
			case vString: token.value=inpt.readObject(); break;
			case vLong:   token.value=inpt.readLong(); break;
		}
		return(token);
	}

}
