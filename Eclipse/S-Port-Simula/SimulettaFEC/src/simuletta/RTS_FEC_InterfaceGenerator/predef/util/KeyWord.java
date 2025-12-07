package simuletta.RTS_FEC_InterfaceGenerator.predef.util;

public class KeyWord {
	public static final int AND=1;
	public static final int ARRAY=2;
	public static final int	BEGIN=3;
	public static final int BODY=4;
	public static final int BOOLEAN=5;
//	public static final int	CALL=6;
	public static final int CASE=7;
	public static final int CHARACTER=8;
	public static final int CONST=9;
	public static final int	DEFINE=10;
	public static final int DO=11;
	public static final int	ELSE=12;
	public static final int ELSIF=13;
	public static final int END=14;
	public static final int ENDCASE=15;
	public static final int ENDIF=16;
	public static final int ENDMACRO=17;
	public static final int ENDREPEAT=18;
	public static final int ENDSKIP=19;
	public static final int ENTRY=20;
	public static final int EXIT=21;
	public static final int EXPORT=22;
	public static final int EXTERNAL=23;
	public static final int	FALSE=24;
	public static final int FIELD=25;
	public static final int	GLOBAL=26;
	public static final int GOTO=27;
	public static final int	IF=28;
	public static final int IMPORT=29;
	public static final int INFIX=30;
	public static final int INFO=31;
	public static final int INSERT=32;
	public static final int INTEGER=33;
	public static final int	KNOWN=34;
	public static final int	LABEL=35;
	public static final int LONG=36;
//	public static final int	REM=37;
	public static final int MACRO=39;
	public static final int MODULE=40;
	public static final int	NAME=41;
	public static final int NOBODY=42;
	public static final int NOFIELD=43;
	public static final int NONAME=44;
	public static final int NONE=45;
	public static final int NOSIZE=46;
	public static final int NOT=47;
	public static final int NOWHERE=48;
	public static final int	OR=49;
	public static final int OTHERWISE=50;
	public static final int	PROFILE=51;
	public static final int	QUA=52;
	public static final int	RANGE=53;
	public static final int REAL=54;
	public static final int CLASS=55;
	public static final int REPEAT=56;
	public static final int REF=57;
	public static final int PROCEDURE=58;
	public static final int	SYSINSERT=59;
	public static final int SHORT=60;
	public static final int SEMICOLON=61;
	public static final int SKIP=62;
	public static final int SYSROUTINE=63;
	public static final int SYSTEM=64;
	public static final int	TEXT=65;
	public static final int	THEN=66;
	public static final int TRUE=67;
	public static final int VALUE=68;
	public static final int VISIBLE=69;
	public static final int	WHEN=70;
	public static final int WHILE=71;
	public static final int	XOR=72;
	
    // Other Symbols
	public static final int EXPAND=73;  // Expand Macro
	public static final int PERCENT=74;
	public static final int	CONC=75;    // Simula Text Concatenation
	public static final int	COMMENT=76; // Ad'Hoc
	public static final int	EQ=77;
	public static final int GE=78;
	public static final int GT=79;
	public static final int LE=80;
	public static final int LT=81;
	public static final int NE=82;
	public static final int	ADDR=83; // Address of

	// Other Symbols
	public static final int ASSIGN=84;
	public static final int	COMMA=85;
	public static final int COLON=86;
	public static final int INDEFINITE=87;
	public static final int	BEGPAR=88;
	public static final int ENDPAR=89;
	public static final int	PLUS=90;
	public static final int MINUS=91;
	public static final int MUL=92;
	public static final int DIV=93;
	public static final int	IDENTIFIER=94;
	public static final int	SIMPLEVALUE=95;
	public static final int	DOT=96;
	public static final int NEWLINE=97;

	public static String ed(int key) {
		switch(key) {
		case AND: return("AND");
		case ARRAY: return("ARRAY");
		case BEGIN: return("BEGIN");
		case BODY: return("BODY");
		case BOOLEAN: return("BOOLEAN");
//		case CALL: return("CALL");
		case CASE: return("CASE");
		case CHARACTER: return("CHARACTER");
		case CONST: return("CONST");
		case DEFINE: return("DEFINE");
		case DO: return("DO");
		case ELSE: return("ELSE");
		case ELSIF: return("ELSIF");
		case END: return("END");
		case ENDCASE: return("ENDCASE");
		case ENDIF: return("ENDIF");
		case ENDMACRO: return("ENDMACRO");
		case ENDREPEAT: return("ENDREPEAT");
		case ENDSKIP: return("ENDSKIP");
		case ENTRY: return("ENTRY");
		case EQ: return("EQ");
		case EXIT: return("EXIT");
		case EXPORT: return("EXPORT");
		case EXTERNAL: return("EXTERNAL");
		case FALSE: return("SIMPLEVALUE,false");
		case FIELD: return("FIELD");
		case GE: return("GE");
		case GLOBAL: return("GLOBAL");
		case GOTO: return("GOTO");
		case GT: return("GT");
		case IF: return("IF");
		case IMPORT: return("IMPORT");
		case INFIX: return("INFIX");
		case INFO: return("INFO");
		case INSERT: return("INSERT");
		case INTEGER: return("INTEGER");
		case KNOWN: return("KNOWN");
		case LABEL: return("LABEL");
		case LE: return("LE");
		case LONG: return("LONG REAL");
		case LT: return("LT");
		case MACRO: return("MACRO");
		case MODULE: return("MODULE");
		case NAME: return("NAME");
		case NE: return("NE");
		case NOBODY: return("NOBODY");
		case NOFIELD: return("NOFIELD");
		case NONAME: return("NONAME");
		case NONE: return("NONE");
		case NOSIZE: return("NOSIZE");
		case NOT: return("NOT");
		case NOWHERE: return("NOWHERE");
		case OR: return("OR");
		case OTHERWISE: return("OTHERWISE");
		case PROFILE: return("PROFILE");
		case QUA: return("QUA");
		case RANGE: return("RANGE");
		case CLASS: return("CLASS");
		case REAL: return("REAL");
		case REF: return("REF");
//		case REM: return("REM");
		case REPEAT: return("REPEAT");
		case PROCEDURE: return("PROCEDURE");
		case SHORT: return("SHORT");
		case SEMICOLON: return("SEMICOLON");
		case SKIP: return("SKIP");
		case SYSINSERT: return("SYSINSERT");
		case SYSROUTINE: return("SYSROUTINE");
		case SYSTEM: return("SYSTEM");
		case THEN: return("THEN");
		case TRUE: return("SIMPLEVALUE,true");
		case TEXT: return("TEXT");
		case VALUE: return("VALUE");
		case VISIBLE: return("VISIBLE");
		case WHEN: return("WHEN");
		case WHILE: return("WHILE");
		case XOR: return("XOR");
		
		case EXPAND: return("EXPAND");  // Expand Macro
		case PERCENT: return("PERCENT");
		case CONC: return("CONC");    // Simula Text Concatenation
		case COMMENT: return("COMMENT"); // Ad'Hoc
//		case EQ: return("EQ");
//		case GE: return("GE");
//		case GT: return("GT");
//		case LE: return("LE");
//		case LT: return("LT");
//		case NE: return("NE");
		case ADDR: return("ADD"); // Address of

		// Other Symbols
		case ASSIGN: return("ASSIGN");
		case COMMA: return("COMMA");
		case COLON: return("COLON");
		case INDEFINITE: return("INDEFINITE");
		case BEGPAR: return("BEGPAR");
		case ENDPAR: return("ENDPAR");
		case PLUS: return("PLUS");
		case MINUS: return("MINUS");
		case MUL: return("MUL");
		case DIV: return("DIV");
		case IDENTIFIER: return("IDENTIFIER");
		case SIMPLEVALUE: return("SIMPLEVALUE");
		case DOT: return("DOT");
		case NEWLINE: return("NEWLINE");

		}
		return("KeyWord_"+key);
	}
}
