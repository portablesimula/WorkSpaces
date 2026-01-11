package simula.lexer;

import simula.compiler.utilities.KeyWord;

public class KeyWordToken extends SimulaToken {
	
	public KeyWordToken(CharSequence sourceText, int startOffset, int endOffset, int keyWord) {
		super(sourceText, startOffset, endOffset, keyWord, KeyWord.edit(keyWord));
	}

//	private KeyWordToken(int keyWord, String debugName) { super(keyWord, debugName);	}
//
//	/** Simula Keyword */ public static final KeyWordToken ACTIVATE     = new KeyWordToken(KeyWord.ACTIVATE,   "ACTIVATE");
//	/** Simula Keyword */ public static final KeyWordToken AND          = new KeyWordToken(KeyWord.AND,        "AND");
//	/** Simula Keyword */ public static final KeyWordToken AND_THEN     = new KeyWordToken(KeyWord.AND_THEN,   "AND_THEN");
//	/** Simula Keyword */ public static final KeyWordToken AFTER        = new KeyWordToken(KeyWord.AFTER,      "AFTER");
//	/** Simula Keyword */ public static final KeyWordToken ARRAY        = new KeyWordToken(KeyWord.ARRAY,      "ARRAY");
//	/** Simula Keyword */ public static final KeyWordToken AT           = new KeyWordToken(KeyWord.AT,         "AT");
//	/** Simula Keyword */ public static final KeyWordToken BEFORE       = new KeyWordToken(KeyWord.BEFORE,     "BEFORE");
//	/** Simula Keyword */ public static final KeyWordToken BEGIN        = new KeyWordToken(KeyWord.BEGIN,      "BEGIN");
//	/** Simula Keyword */ public static final KeyWordToken BOOLEAN      = new KeyWordToken(KeyWord.BOOLEAN,    "BOOLEAN");
//	/** Simula Keyword */ public static final KeyWordToken CHARACTER    = new KeyWordToken(KeyWord.CHARACTER,  "CHARACTER");
//	/** Simula Keyword */ public static final KeyWordToken CLASS        = new KeyWordToken(KeyWord.CLASS,      "CLASS");
//	/** Simula Keyword */ public static final KeyWordToken COMMENT      = new KeyWordToken(KeyWord.COMMENT,    "COMMENT");
//	/** Simula Keyword */ public static final KeyWordToken CONC         = new KeyWordToken(KeyWord.CONC,       "CONC");
//	/** Simula Keyword */ public static final KeyWordToken DELAY        = new KeyWordToken(KeyWord.DELAY,      "DELAY");
//	/** Simula Keyword */ public static final KeyWordToken DO           = new KeyWordToken(KeyWord.DO,         "DO");
//	/** Simula Keyword */ public static final KeyWordToken ELSE         = new KeyWordToken(KeyWord.ELSE,       "ELSE");
//	/** Simula Keyword */ public static final KeyWordToken END          = new KeyWordToken(KeyWord.END,        "END");
//	/** Simula Keyword */ public static final KeyWordToken EQ           = new KeyWordToken(KeyWord.EQ,         "EQ");
//	/** Simula Keyword */ public static final KeyWordToken EQV          = new KeyWordToken(KeyWord.EQV,        "EQV");
//	/** Simula Keyword */ public static final KeyWordToken EXTERNAL     = new KeyWordToken(KeyWord.EXTERNAL,   "EXTERNAL");
//	/** Simula Keyword */ public static final KeyWordToken FALSE        = new KeyWordToken(KeyWord.FALSE,      "FALSE");
//	/** Simula Keyword */ public static final KeyWordToken FOR          = new KeyWordToken(KeyWord.FOR,        "FOR");
//	/** Simula Keyword */ public static final KeyWordToken GE           = new KeyWordToken(KeyWord.GE,         "GE");
//	/** Simula Keyword */ public static final KeyWordToken GO           = new KeyWordToken(KeyWord.GO,         "GO");
//	/** Simula Keyword */ public static final KeyWordToken GOTO         = new KeyWordToken(KeyWord.GOTO,       "GOTO");
//	/** Simula Keyword */ public static final KeyWordToken GT           = new KeyWordToken(KeyWord.GT,         "GT");
//	/** Simula Keyword */ public static final KeyWordToken HIDDEN       = new KeyWordToken(KeyWord.HIDDEN,     "HIDDEN");
//	/** Simula Keyword */ public static final KeyWordToken IF           = new KeyWordToken(KeyWord.IF,         "IF");
//	/** Simula Keyword */ public static final KeyWordToken IMP          = new KeyWordToken(KeyWord.IMP,        "IMP");
//	/** Simula Keyword */ public static final KeyWordToken IN           = new KeyWordToken(KeyWord.IN,         "IN");
//	/** Simula Keyword */ public static final KeyWordToken INNER        = new KeyWordToken(KeyWord.INNER,      "INNER");
//	/** Simula Keyword */ public static final KeyWordToken INSPECT      = new KeyWordToken(KeyWord.INSPECT,    "INSPECT");
//	/** Simula Keyword */ public static final KeyWordToken INTEGER      = new KeyWordToken(KeyWord.INTEGER,    "INTEGER");
//	/** Simula Keyword */ public static final KeyWordToken IS           = new KeyWordToken(KeyWord.IS,         "IS");
//	/** Simula Keyword */ public static final KeyWordToken LABEL        = new KeyWordToken(KeyWord.LABEL,      "LABEL");
//	/** Simula Keyword */ public static final KeyWordToken LE           = new KeyWordToken(KeyWord.LE,         "LE");
//	/** Simula Keyword */ public static final KeyWordToken LONG         = new KeyWordToken(KeyWord.LONG,       "LONG");
//	/** Simula Keyword */ public static final KeyWordToken LT           = new KeyWordToken(KeyWord.LT,         "LT");
//	/** Simula Keyword */ public static final KeyWordToken NAME         = new KeyWordToken(KeyWord.NAME,       "NAME");
//	/** Simula Keyword */ public static final KeyWordToken NE           = new KeyWordToken(KeyWord.NE,         "NE");
//	/** Simula Keyword */ public static final KeyWordToken NEW          = new KeyWordToken(KeyWord.NEW,        "NEW");
//	/** Simula Keyword */ public static final KeyWordToken NONE         = new KeyWordToken(KeyWord.NONE,       "NONE");
//	/** Simula Keyword */ public static final KeyWordToken NOT          = new KeyWordToken(KeyWord.NOT,        "NOT");
//	/** Simula Keyword */ public static final KeyWordToken NOTEXT       = new KeyWordToken(KeyWord.NOTEXT,     "NOTEXT");
//	/** Simula Keyword */ public static final KeyWordToken OR           = new KeyWordToken(KeyWord.OR,         "OR");
//	/** Simula Keyword */ public static final KeyWordToken OR_ELSE      = new KeyWordToken(KeyWord.OR_ELSE,    "OR_ELSE");
//	/** Simula Keyword */ public static final KeyWordToken OTHERWISE    = new KeyWordToken(KeyWord.OTHERWISE,  "OTHERWISE");
//	/** Simula Keyword */ public static final KeyWordToken PRIOR        = new KeyWordToken(KeyWord.PRIOR,      "PRIOR");
//	/** Simula Keyword */ public static final KeyWordToken PROCEDURE    = new KeyWordToken(KeyWord.PROCEDURE,  "PROCEDURE");
//	/** Simula Keyword */ public static final KeyWordToken PROTECTED    = new KeyWordToken(KeyWord.PROTECTED,  "PROTECTED");
//	/** Simula Keyword */ public static final KeyWordToken QUA          = new KeyWordToken(KeyWord.QUA,        "QUA");
//	/** Simula Keyword */ public static final KeyWordToken REACTIVATE   = new KeyWordToken(KeyWord.REACTIVATE, "REACTIVATE");
//	/** Simula Keyword */ public static final KeyWordToken REAL         = new KeyWordToken(KeyWord.REAL,       "REAL");
//	/** Simula Keyword */ public static final KeyWordToken REF          = new KeyWordToken(KeyWord.REF,        "REF");
//	/** Simula Keyword */ public static final KeyWordToken SHORT        = new KeyWordToken(KeyWord.SHORT,      "SHORT");
//	/** Simula Keyword */ public static final KeyWordToken STEP         = new KeyWordToken(KeyWord.STEP,       "STEP");
//	/** Simula Keyword */ public static final KeyWordToken SWITCH       = new KeyWordToken(KeyWord.SWITCH,     "SWITCH");
//	/** Simula Keyword */ public static final KeyWordToken TEXT         = new KeyWordToken(KeyWord.TEXT,       "TEXT");
//	/** Simula Keyword */ public static final KeyWordToken THEN         = new KeyWordToken(KeyWord.THEN,       "THEN");
//	/** Simula Keyword */ public static final KeyWordToken THIS         = new KeyWordToken(KeyWord.THIS,       "THIS");
//	/** Simula Keyword */ public static final KeyWordToken TO           = new KeyWordToken(KeyWord.TO,         "TO");
//	/** Simula Keyword */ public static final KeyWordToken TRUE         = new KeyWordToken(KeyWord.TRUE,       "TRUE");
//	/** Simula Keyword */ public static final KeyWordToken UNTIL        = new KeyWordToken(KeyWord.UNTIL,      "UNTIL");
//	/** Simula Keyword */ public static final KeyWordToken VALUE        = new KeyWordToken(KeyWord.VALUE,      "VALUE");
//	/** Simula Keyword */ public static final KeyWordToken VIRTUAL      = new KeyWordToken(KeyWord.VIRTUAL,    "VIRTUAL");
//	/** Simula Keyword */ public static final KeyWordToken WHEN         = new KeyWordToken(KeyWord.WHEN,       "WHEN");
//	/** Simula Keyword */ public static final KeyWordToken WHILE        = new KeyWordToken(KeyWord.WHILE,      "WHILE");
//	// Other Symbols
//	/** Other Symbol */ public static final KeyWordToken ASSIGNVALUE    = new KeyWordToken(KeyWord.ASSIGNVALUE, "ASSIGNVALUE");
//	/** Other Symbol */ public static final KeyWordToken ASSIGNREF      = new KeyWordToken(KeyWord.ASSIGNREF,   "ASSIGNREF");
//	/** Other Symbol */ public static final KeyWordToken COMMA          = new KeyWordToken(KeyWord.COMMA,       "COMMA");
//	/** Other Symbol */ public static final KeyWordToken COLON          = new KeyWordToken(KeyWord.COLON,       "COLON");
//	/** Other Symbol */ public static final KeyWordToken SEMICOLON      = new KeyWordToken(KeyWord.SEMICOLON,   "SEMICOLON");
//	/** Other Symbol */ public static final KeyWordToken BEGPAR         = new KeyWordToken(KeyWord.BEGPAR,      "BEGPAR");
//	/** Other Symbol */ public static final KeyWordToken ENDPAR         = new KeyWordToken(KeyWord.END,         "ENDPAR");
//	/** Other Symbol */ public static final KeyWordToken BEGBRACKET     = new KeyWordToken(KeyWord.BEGBRACKET,  "BEGBRACKET");
//	/** Other Symbol */ public static final KeyWordToken ENDBRACKET     = new KeyWordToken(KeyWord.ENDBRACKET,  "ENDBRACKET");
//	/** Other Symbol */ public static final KeyWordToken EQR            = new KeyWordToken(KeyWord.EQR, "EQR");
//	/** Other Symbol */ public static final KeyWordToken NER            = new KeyWordToken(KeyWord.NER, "NER");
//	/** Other Symbol */ public static final KeyWordToken PLUS           = new KeyWordToken(KeyWord.PLUS, "PLUS");
//	/** Other Symbol */ public static final KeyWordToken MINUS          = new KeyWordToken(KeyWord.MINUS, "MINUS");
//	/** Other Symbol */ public static final KeyWordToken MUL            = new KeyWordToken(KeyWord.MUL, "MUL");
//	/** Other Symbol */ public static final KeyWordToken DIV            = new KeyWordToken(KeyWord.DIV, "DIV");
//	/** Other Symbol */ public static final KeyWordToken INTDIV         = new KeyWordToken(KeyWord.INTDIV, "INTDIV");
//	/** Other Symbol */ public static final KeyWordToken EXP            = new KeyWordToken(KeyWord.EXP, "EXP");
//	/** Other Symbol */ public static final KeyWordToken AMPERSAND      = new KeyWordToken(KeyWord.AMPERSAND, "AMPERSAND");
//	/** Other Symbol */ public static final KeyWordToken IDENTIFIER     = new KeyWordToken(KeyWord.IDENTIFIER, "IDENTIFIER");
////	/** Other Symbol */ public static final KeyWordToken BOOLEANKONST   = new KeyWordToken(KeyWord., "");
////	/** Other Symbol */ public static final KeyWordToken INTEGERKONST   = new KeyWordToken(KeyWord., "");
////	/** Other Symbol */ public static final KeyWordToken CHARACTERKONST = new KeyWordToken(KeyWord., "");
////	/** Other Symbol */ public static final KeyWordToken REALKONST      = new KeyWordToken(KeyWord., "");
////	/** Other Symbol */ public static final KeyWordToken TEXTKONST      = new KeyWordToken(KeyWord., "");
//	/** Other Symbol */ public static final KeyWordToken DOT            = new KeyWordToken(KeyWord.DOT, "DOT");
//	/** Other Symbol */ public static final KeyWordToken NEWLINE        = new KeyWordToken(KeyWord.NEWLINE, "NEWLINE");
//	/** Other Symbol */ public static final KeyWordToken STRING         = new KeyWordToken(KeyWord.STRING, "STRING");
//	/** Other Symbol */ public static final KeyWordToken EOF	        = new KeyWordToken(KeyWord.EOF, "EOF");

}
