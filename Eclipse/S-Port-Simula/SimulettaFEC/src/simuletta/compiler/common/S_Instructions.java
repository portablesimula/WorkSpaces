/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.common;

public class S_Instructions {

	//******    S - I N S T R U C T I O N S    ******;

	public static final int S_NULL = 0,S_RECORD=1,
	S_LSHIFTL=2, // extension to Scode, left shift logical
	S_PREFIX=3,S_ATTR=4,
	S_LSHIFTA=5, // extension to Scode, left shift arithmetical
	S_REP=6,
	S_ALT=7,S_FIXREP=8,S_ENDRECORD=9,S_C_RECORD=10,
	S_TEXT=11,S_C_CHAR=12,S_C_INT=13,S_C_SIZE=14,
	S_C_REAL=15,S_C_LREAL=16,S_C_AADDR=17,S_C_OADDR=18,
	S_C_GADDR=19,S_C_PADDR=20,S_C_DOT=21,S_C_RADDR=22,
	S_NOBODY=23,S_ANONE=24,S_ONONE=25,S_GNONE=26,
	S_NOWHERE=27,S_TRUE=28,S_FALSE=29,S_PROFILE=30,
	S_KNOWN=31,S_SYSTEM=32,S_EXTERNAL=33,S_IMPORT=34,
	S_EXPORT=35,S_EXIT=36,S_ENDPROFILE=37,S_ROUTINESPEC=38,
	S_ROUTINE=39,S_LOCAL=40,S_ENDROUTINE=41,S_MODULE=42,
	S_EXISTING=43,S_TAG=44,S_BODY=45,S_ENDMODULE=46,
	S_LABELSPEC=47,S_LABEL=48,S_RANGE=49,S_GLOBAL=50,
	S_INIT=51,S_CONSTSPEC=52,S_CONST=53,S_DELETE=54,
	S_FDEST=55,S_BDEST=56,S_SAVE=57,S_RESTORE=58,
	S_BSEG=59,S_ESEG=60,S_SKIPIF=61,S_ENDSKIP=62,
	S_IF=63,S_ELSE=64,S_ENDIF=65,
	S_RSHIFTL=66, // extension to Scode, right shift logical
	S_PRECALL=67,S_ASSPAR=68,S_ASSREP=69,S_CALL=70,
	S_FETCH=71,S_REFER=72,S_DEREF=73,S_SELECT=74,
	S_REMOTE=75,S_LOCATE=76,S_INDEX=77,S_INCO=78,
	S_DECO=79,S_PUSH=80,S_PUSHC=81,S_PUSHLEN=82,
	S_DUP=83,S_POP=84,S_EMPTY=85,S_SETOBJ=86,
	S_GETOBJ=87,S_ACCESS=88,S_FJUMP=89,S_BJUMP=90,
	S_FJUMPIF=91,S_BJUMPIF=92,S_SWITCH=93,S_GOTO=94,
	S_T_INITO=95,S_T_GETO=96,S_T_SETO=97,S_ADD=98,
	S_SUB=99,S_MULT=100,S_DIV=101,S_REM=102,
	S_NEG=103,S_AND=104,S_OR=105,S_XOR=106,
	S_IMP=107,S_EQV=108,S_NOT=109,S_DIST=110,
	S_ASSIGN=111,S_UPDATE=112,S_CONVERT=113,S_SYSINSERT=114,
	S_INSERT=115,S_ZEROAREA=116,S_INITAREA=117,S_COMPARE=118,
	S_LT=119,S_LE=120,S_EQ=121,S_GE=122,
	S_GT=123,S_NE=124,S_EVAL=125,S_INFO=126,
	S_LINE=127,S_SETSWITCH=128,
	S_RSHIFTA=129, // extension to Scode, right shift arithmetical
	S_PROGRAM=130,S_MAIN=131,
	S_ENDPROGRAM=132,S_DSIZE=133,S_SDEST=134,S_RUPDATE=135,
	S_ASSCALL=136,S_CALL_TOS=137,S_DINITAREA=138,S_NOSIZE=139,
	S_POPALL=140,S_REPCALL=141,S_INTERFACE=142,S_MACRO=143,
	S_MARK=144,S_MPAR=145,S_ENDMACRO=146,S_MCALL=147,
	S_PUSHV=148,S_SELECTV=149,S_REMOTEV=150,S_INDEXV=151,
	S_ACCESSV=152,S_DECL=153,S_STMT=154,
	S_NAME=156; // Additional S-Instruction used internally for  NAME  and  @-operation



	
	public static String edSymbol(int i) {
		switch(i) {
		case S_NULL: return("NULL");
		case S_RECORD:     return("RECORD");
		case S_LSHIFTL:    return("LSHIFTL"); // extension to Scode, left shift logical
		case S_PREFIX:     return("PREFIX");
		case S_ATTR:       return("ATTR");
		case S_LSHIFTA:    return("LSHIFTA"); // extension to Scode, left shift arithmetical
		case S_REP:        return("REP");
		case S_ALT:        return("ALT");
		case S_FIXREP:     return("FIXREP");
		case S_ENDRECORD:  return("ENDRECORD");
		case S_C_RECORD:   return("C_RECORD");
		case S_TEXT:       return("TEXT");
		case S_C_CHAR:     return("C_CHAR");
		case S_C_INT:      return("C_INT");
		case S_C_SIZE:     return("C_SIZE");

		case S_C_REAL:     return("C_REAL");
		case S_C_LREAL:    return("C_LREAL");
		case S_C_AADDR:    return("C_AADDR");
		case S_C_OADDR:    return("C_OADDR");
	//S_C_REAL=15,S_C_LREAL=16,S_C_AADDR=17,S_C_OADDR=18,
		case S_C_GADDR:    return("C_GADDR");
		case S_C_PADDR:    return("C_PADDR");
		case S_C_DOT:      return("C_DOT");
		case S_C_RADDR:    return("C_RADDR");
	//S_C_GADDR=19,S_C_PADDR=20,S_C_DOT=21,S_C_RADDR=22,
		case S_NOBODY:     return("NOBODY");
		case S_ANONE:      return("ANONE");
		case S_ONONE:      return("ONONE");
		case S_GNONE:      return("GNONE");
	//S_NOBODY=23,S_ANONE=24,S_ONONE=25,S_GNONE=26,
		case S_NOWHERE:    return("NOWHERE");
		case S_TRUE:       return("TRUE");
		case S_FALSE:      return("FALSE");
		case S_PROFILE:    return("PROFILE");
	//S_NOWHERE=27,S_TRUE=28,S_FALSE=29,S_PROFILE=30,
		case S_KNOWN:      return("KNOWN");
		case S_SYSTEM:     return("SYSTEM");
		case S_EXTERNAL:   return("EXTERNAL");
		case S_IMPORT:     return("IMPORT");
	//S_KNOWN=31,S_SYSTEM=32,S_EXTERNAL=33,S_IMPORT=34,
		case S_EXPORT:     return("EXPORT");
		case S_EXIT:       return("EXIT");
		case S_ENDPROFILE:  return("ENDPROFILE");
		case S_ROUTINESPEC:  return("ROUTINESPEC");
	//S_EXPORT=35,S_EXIT=36,S_ENDPROFILE=37,S_ROUTINESPEC=38,
		case S_ROUTINE:    return("ROUTINE");
		case S_LOCAL:      return("LOCAL");
		case S_ENDROUTINE:  return("ENDROUTINE");
		case S_MODULE:     return("MODULE");
	//S_ROUTINE=39,S_LOCAL=40,S_ENDROUTINE=41,S_MODULE=42,
		case S_EXISTING:   return("EXISTING");
		case S_TAG:        return("TAG");
		case S_BODY:       return("BODY");
		case S_ENDMODULE:  return("ENDMODULE");
	//S_EXISTING=43,S_TAG=44,S_BODY=45,S_ENDMODULE=46,
		case S_LABELSPEC:  return("LABELSPEC");
		case S_LABEL:      return("LABEL");
		case S_RANGE:      return("RANGE");
		case S_GLOBAL:     return("GLOBAL");
	//S_LABELSPEC=47,S_LABEL=48,S_RANGE=49,S_GLOBAL=50,
		case S_INIT:       return("INIT");
		case S_CONSTSPEC:  return("CONSTSPEC");
		case S_CONST:      return("CONST");
		case S_DELETE:     return("DELETE");
	//S_INIT=51,S_CONSTSPEC=52,S_CONST=53,S_DELETE=54,
		case S_FDEST:      return("FDEST");
		case S_BDEST:      return("BDEST");
		case S_SAVE:       return("SAVE");
		case S_RESTORE:    return("RESTORE");
	//S_FDEST=55,S_BDEST=56,S_SAVE=57,S_RESTORE=58,
		case S_BSEG:       return("BSEG");
		case S_ESEG:       return("ESEG");
		case S_SKIPIF:     return("SKIPIF");
		case S_ENDSKIP:    return("ENDSKIP");
	//S_BSEG=59,S_ESEG=60,S_SKIPIF=61,S_ENDSKIP=62,
		case S_IF:         return("IF");
		case S_ELSE:       return("ELSE");
		case S_ENDIF:      return("ENDIF");
	//S_IF=63,S_ELSE=64,S_ENDIF=65,
		case S_RSHIFTL:    return("RSHIFTL"); // extension to Scode, right shift logical
	//S_RSHIFTL=66, // extension to Scode, right shift logical
		case S_PRECALL:    return("PRECALL");
		case S_ASSPAR:     return("ASSPAR");
		case S_ASSREP:     return("ASSREP");
		case S_CALL:       return("CALL");
	//S_PRECALL=67,S_ASSPAR=68,S_ASSREP=69,S_CALL=70,
		case S_FETCH:      return("FETCH");
		case S_REFER:      return("REFER");
		case S_DEREF:      return("DEREF");
		case S_SELECT:     return("SELECT");
	//S_FETCH=71,S_REFER=72,S_DEREF=73,S_SELECT=74,
		case S_REMOTE:     return("REMOTE");
		case S_LOCATE:     return("LOCATE");
		case S_INDEX:      return("INDEX");
		case S_INCO:       return("INCO");
	//S_REMOTE=75,S_LOCATE=76,S_INDEX=77,S_INCO=78,
		case S_DECO:       return("DECO");
		case S_PUSH:       return("PUSH");
		case S_PUSHC:      return("PUSHC");
		case S_PUSHLEN:    return("PUSHLEN");
	//S_DECO=79,S_PUSH=80,S_PUSHC=81,S_PUSHLEN=82,
		case S_DUP:        return("DUP");
		case S_POP:        return("POP");
		case S_EMPTY:      return("EMPTY");
		case S_SETOBJ:     return("SETOBJ");
	//S_DUP=83,S_POP=84,S_EMPTY=85,S_SETOBJ=86,
		case S_GETOBJ:     return("GETOBJ");
		case S_ACCESS:     return("ACCESS");
		case S_FJUMP:      return("FJUMP");
		case S_BJUMP:      return("BJUMP");
	//S_GETOBJ=87,S_ACCESS=88,S_FJUMP=89,S_BJUMP=90,
		case S_FJUMPIF:    return("FJUMPIF");
		case S_BJUMPIF:    return("BJUMPIF");
		case S_SWITCH:     return("SWITCH");
		case S_GOTO:       return("GOTO");
	//S_FJUMPIF=91,S_BJUMPIF=92,S_SWITCH=93,S_GOTO=94,
		case S_T_INITO:    return("T_INITO");
		case S_T_GETO:     return("T_GETO");
		case S_T_SETO:     return("T_SETO");
		case S_ADD:        return("ADD");
	//S_T_INITO=95,S_T_GETO=96,S_T_SETO=97,S_ADD=98,
		case S_SUB:        return("SUB");
		case S_MULT:       return("MULT");
		case S_DIV:        return("DIV");
		case S_REM:        return("REM");
	//S_SUB=99,S_MULT=100,S_DIV=101,S_REM=102,
		case S_NEG:        return("NEG");
		case S_AND:        return("AND");
		case S_OR:         return("OR");
		case S_XOR:        return("XOR");
	//S_NEG=103,S_AND=104,S_OR=105,S_XOR=106,
		case S_IMP:        return("IMP");
		case S_EQV:        return("EQV");
		case S_NOT:        return("NOT");
		case S_DIST:       return("DIST");
	//S_IMP=107,S_EQV=108,S_NOT=109,S_DIST=110,
		case S_ASSIGN:     return("ASSIGN");
		case S_UPDATE:     return("UPDATE");
		case S_CONVERT:    return("CONVERT");
		case S_SYSINSERT:  return("SYSINSERT");
	//S_ASSIGN=111,S_UPDATE=112,S_CONVERT=113,S_SYSINSERT=114,
		case S_INSERT:     return("INSERT");
		case S_ZEROAREA:   return("ZEROAREA");
		case S_INITAREA:   return("INITAREA");
		case S_COMPARE:    return("COMPARE");
	//S_INSERT=115,S_ZEROAREA=116,S_INITAREA=117,S_COMPARE=118,
		case S_LT:         return("LT");
		case S_LE:         return("LE");
		case S_EQ:         return("EQ");
		case S_GE:         return("GE");
	//S_LT=119,S_LE=120,S_EQ=121,S_GE=122,
		case S_GT:         return("GT");
		case S_NE:         return("NE");
		case S_EVAL:       return("EVAL");
		case S_INFO:       return("INFO");
	//S_GT=123,S_NE=124,S_EVAL=125,S_INFO=126,
		case S_LINE:       return("LINE");
		case S_SETSWITCH:  return("SETSWITCH");
	//S_LINE=127,S_SETSWITCH=128,
		case S_RSHIFTA:    return("RSHIFTA"); // extension to Scode, right shift arithmetical
	//S_RSHIFTA=129, // extension to Scode, right shift arithmetical
		case S_PROGRAM:    return("PROGRAM");
		case S_MAIN:       return("MAIN");
	//S_PROGRAM=130,S_MAIN=131,
		case S_ENDPROGRAM:  return("ENDPROGRAM");
		case S_DSIZE:      return("DSIZE");
		case S_SDEST:      return("SDEST");
		case S_RUPDATE:    return("RUPDATE");
	//S_ENDPROGRAM=132,S_DSIZE=133,S_SDEST=134,S_RUPDATE=135,
		case S_ASSCALL:    return("ASSCALL");
		case S_CALL_TOS:   return("CALL_TOS");
		case S_DINITAREA:  return("DINITAREA");
		case S_NOSIZE:     return("NOSIZE");
	//S_ASSCALL=136,S_CALL_TOS=137,S_DINITAREA=138,S_NOSIZE=139,
		case S_POPALL:     return("POPALL");
		case S_REPCALL:    return("REPCALL");
		case S_INTERFACE:  return("INTERFACE");
		case S_MACRO:      return("MACRO");
	//S_POPALL=140,S_REPCALL=141,S_INTERFACE=142,S_MACRO=143,
		case S_MARK:       return("MARK");
		case S_MPAR:       return("MPAR");
		case S_ENDMACRO:   return("ENDMACRO");
		case S_MCALL:      return("MCALL");
	//S_MARK=144,S_MPAR=145,S_ENDMACRO=146,S_MCALL=147,
		case S_PUSHV:      return("PUSHV");
		case S_SELECTV:    return("SELECTV");
		case S_REMOTEV:    return("REMOTEV");
		case S_INDEXV:     return("INDEXV");
	//S_PUSHV=148,S_SELECTV=149,S_REMOTEV=150,S_INDEXV=151,
		case S_ACCESSV:    return("ACCESSV");
		case S_DECL:       return("DECL");
		case S_STMT:       return("STMT");
	//S_ACCESSV=152,S_DECL=153,S_STMT=154,
		case S_NAME:       return("NAME");  // Additional S-Instruction used internally for  NAME  and  @-operation
		}
		return(null);
	}

}
