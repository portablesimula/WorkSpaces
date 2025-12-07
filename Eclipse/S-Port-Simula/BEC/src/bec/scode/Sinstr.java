/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package bec.scode;

/// S-Instructions.
/// 
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/bec/scode/Sinstr.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class Sinstr {
	
	/** Default Constructor */ public Sinstr() {} 

	//  ------   S  -  I  N  S  T  R  U  C  T  I  O  N  S   ------
	
	/** S-Instruction */ public final static int S_LSHIFTL=2;   // Extension to S-Code:  Left shift logical
	/** S-Instruction */ public final static int S_LSHIFTA=5;   // Extension to S-Code:  Left shift arithmetical
	/** S-Instruction */ public final static int S_RSHIFTL=66;  // Extension to S-Code:  Right shift logical
	/** S-Instruction */ public final static int S_RSHIFTA=129; // Extension to S-Code:  Right shift arithmetical
	
	/** S-Instruction */ public final static int S_NULL=0;
	/** S-Instruction */ public final static int S_RECORD=1;
	/** S-Instruction */ public final static int S_PREFIX=3;
	/** S-Instruction */ public final static int S_ATTR=4;
	/** S-Instruction */ public final static int S_REP=6;
	/** S-Instruction */ public final static int S_ALT=7;
	/** S-Instruction */ public final static int S_FIXREP=8;
	/** S-Instruction */ public final static int S_ENDRECORD=9;
	/** S-Instruction */ public final static int S_C_RECORD=10;
	/** S-Instruction */ public final static int S_TEXT=11;
	/** S-Instruction */ public final static int S_C_CHAR=12;
	/** S-Instruction */ public final static int S_C_INT=13;
	/** S-Instruction */ public final static int S_C_SIZE=14;
	/** S-Instruction */ public final static int S_C_REAL=15;
	/** S-Instruction */ public final static int S_C_LREAL=16;
	/** S-Instruction */ public final static int S_C_AADDR=17;
	/** S-Instruction */ public final static int S_C_OADDR=18;
	/** S-Instruction */ public final static int S_C_GADDR=19;
	/** S-Instruction */ public final static int S_C_PADDR=20;
	/** S-Instruction */ public final static int S_C_DOT=21;
	/** S-Instruction */ public final static int S_C_RADDR=22;
	/** S-Instruction */ public final static int S_NOBODY=23;
	/** S-Instruction */ public final static int S_ANONE=24;
	/** S-Instruction */ public final static int S_ONONE=25;
	/** S-Instruction */ public final static int S_GNONE=26;
	/** S-Instruction */ public final static int S_NOWHERE=27;
	/** S-Instruction */ public final static int S_TRUE=28;
	/** S-Instruction */ public final static int S_FALSE=29;
	/** S-Instruction */ public final static int S_PROFILE=30;
	/** S-Instruction */ public final static int S_KNOWN=31;
	/** S-Instruction */ public final static int S_SYSTEM=32;
	/** S-Instruction */ public final static int S_EXTERNAL=33;
	/** S-Instruction */ public final static int S_IMPORT=34;
	/** S-Instruction */ public final static int S_EXPORT=35;
	/** S-Instruction */ public final static int S_EXIT=36;
	/** S-Instruction */ public final static int S_ENDPROFILE=37;
	/** S-Instruction */ public final static int S_ROUTINESPEC=38;
	/** S-Instruction */ public final static int S_ROUTINE=39;
	/** S-Instruction */ public final static int S_LOCAL=40;
	/** S-Instruction */ public final static int S_ENDROUTINE=41;
	/** S-Instruction */ public final static int S_MODULE=42;
	/** S-Instruction */ public final static int S_EXISTING=43;
	/** S-Instruction */ public final static int S_TAG=44;
	/** S-Instruction */ public final static int S_BODY=45;
	/** S-Instruction */ public final static int S_ENDMODULE=46;
	/** S-Instruction */ public final static int S_LABELSPEC=47;
	/** S-Instruction */ public final static int S_LABEL=48;
	/** S-Instruction */ public final static int S_RANGE=49;
	/** S-Instruction */ public final static int S_GLOBAL=50;
	/** S-Instruction */ public final static int S_INIT=51;
	/** S-Instruction */ public final static int S_CONSTSPEC=52;
	/** S-Instruction */ public final static int S_CONST=53;
	/** S-Instruction */ public final static int S_DELETE=54;
	/** S-Instruction */ public final static int S_FDEST=55;
	/** S-Instruction */ public final static int S_BDEST=56;
	/** S-Instruction */ public final static int S_SAVE=57;
	/** S-Instruction */ public final static int S_RESTORE=58;
	/** S-Instruction */ public final static int S_BSEG=59;
	/** S-Instruction */ public final static int S_ESEG=60;
	/** S-Instruction */ public final static int S_SKIPIF=61;
	/** S-Instruction */ public final static int S_ENDSKIP=62;
	/** S-Instruction */ public final static int S_IF=63;
	/** S-Instruction */ public final static int S_ELSE=64;
	/** S-Instruction */ public final static int S_ENDIF=65;
	/** S-Instruction */ public final static int S_PRECALL=67;
	/** S-Instruction */ public final static int S_ASSPAR=68;
	/** S-Instruction */ public final static int S_ASSREP=69;
	/** S-Instruction */ public final static int S_CALL=70;
	/** S-Instruction */ public final static int S_FETCH=71;
	/** S-Instruction */ public final static int S_REFER=72;
	/** S-Instruction */ public final static int S_DEREF=73;
	/** S-Instruction */ public final static int S_SELECT=74;
	/** S-Instruction */ public final static int S_REMOTE=75;
	/** S-Instruction */ public final static int S_LOCATE=76;
	/** S-Instruction */ public final static int S_INDEX=77;
	/** S-Instruction */ public final static int S_INCO=78;
	/** S-Instruction */ public final static int S_DECO=79;
	/** S-Instruction */ public final static int S_PUSH=80;
	/** S-Instruction */ public final static int S_PUSHC=81;
	/** S-Instruction */ public final static int S_PUSHLEN=82;
	/** S-Instruction */ public final static int S_DUP=83;
	/** S-Instruction */ public final static int S_POP=84;
	/** S-Instruction */ public final static int S_EMPTY=85;
	/** S-Instruction */ public final static int S_SETOBJ=86;
	/** S-Instruction */ public final static int S_GETOBJ=87;
	/** S-Instruction */ public final static int S_ACCESS=88;
	/** S-Instruction */ public final static int S_FJUMP=89;
	/** S-Instruction */ public final static int S_BJUMP=90;
	/** S-Instruction */ public final static int S_FJUMPIF=91;
	/** S-Instruction */ public final static int S_BJUMPIF=92;
	/** S-Instruction */ public final static int S_SWITCH=93;
	/** S-Instruction */ public final static int S_GOTO=94;
	/** S-Instruction */ public final static int S_T_INITO=95;
	/** S-Instruction */ public final static int S_T_GETO=96;
	/** S-Instruction */ public final static int S_T_SETO=97;
	/** S-Instruction */ public final static int S_ADD=98;
	/** S-Instruction */ public final static int S_SUB=99;
	/** S-Instruction */ public final static int S_MULT=100;
	/** S-Instruction */ public final static int S_DIV=101;
	/** S-Instruction */ public final static int S_REM=102;
	/** S-Instruction */ public final static int S_NEG=103;
	/** S-Instruction */ public final static int S_AND=104;
	/** S-Instruction */ public final static int S_OR =105;
	/** S-Instruction */ public final static int S_XOR=106; 
	/** S-Instruction */ public final static int S_IMP=107;
	/** S-Instruction */ public final static int S_EQV=108; 
	/** S-Instruction */ public final static int S_NOT=109;
	/** S-Instruction */ public final static int S_DIST=110; 
	/** S-Instruction */ public final static int S_ASSIGN=111;
	/** S-Instruction */ public final static int S_UPDATE=112;
	/** S-Instruction */ public final static int S_CONVERT=113;
	/** S-Instruction */ public final static int S_SYSINSERT=114;
	/** S-Instruction */ public final static int S_INSERT=115;
	/** S-Instruction */ public final static int S_ZEROAREA=116;
	/** S-Instruction */ public final static int S_INITAREA=117;
	/** S-Instruction */ public final static int S_COMPARE=118;
	/** S-Instruction */ public final static int S_LT=119;
	/** S-Instruction */ public final static int S_LE=120;
	/** S-Instruction */ public final static int S_EQ=121;
	/** S-Instruction */ public final static int S_GE=122;
	/** S-Instruction */ public final static int S_GT=123;
	/** S-Instruction */ public final static int S_NE=124;
	/** S-Instruction */ public final static int S_EVAL=125;
	/** S-Instruction */ public final static int S_INFO=126;
	/** S-Instruction */ public final static int S_LINE=127;
	/** S-Instruction */ public final static int S_SETSWITCH=128;
	/** S-Instruction */ public final static int S_PROGRAM=130;
	/** S-Instruction */ public final static int S_MAIN=131;
	/** S-Instruction */ public final static int S_ENDPROGRAM=132;
	/** S-Instruction */ public final static int S_DSIZE=133;
	/** S-Instruction */ public final static int S_SDEST=134;
	/** S-Instruction */ public final static int S_RUPDATE=135;
	/** S-Instruction */ public final static int S_ASSCALL=136;
	/** S-Instruction */ public final static int S_CALL_TOS=137;
	/** S-Instruction */ public final static int S_DINITAREA=138;
	/** S-Instruction */ public final static int S_NOSIZE=139;
	/** S-Instruction */ public final static int S_POPALL=140;
	/** S-Instruction */ public final static int S_REPCALL=141;
	/** S-Instruction */ public final static int S_INTERFACE=142;
	/** S-Instruction */ public final static int S_MACRO=143;
	/** S-Instruction */ public final static int S_MARK=144;
	/** S-Instruction */ public final static int S_MPAR=145;
	/** S-Instruction */ public final static int S_ENDMACRO=146;
	/** S-Instruction */ public final static int S_MCALL=147;
	/** S-Instruction */ public final static int S_PUSHV=148;
	/** S-Instruction */ public final static int S_SELECTV=149;
	/** S-Instruction */ public final static int S_REMOTEV=150;
	/** S-Instruction */ public final static int S_INDEXV=151;
	/** S-Instruction */ public final static int S_ACCESSV=152;
	/** S-Instruction */ public final static int S_DECL=153;
	/** S-Instruction */ public final static int S_STMT=154;
	
	/** S-Instruction */ public final static int S_STRING=155;
	
	/** S-Instruction */ public final static int S_max=155;  // Max value of S-Instruction codes

	/// Returns a edited S-Instruction 
	/// @param i the instruction index
	/// @return the edited S-Instruction 
	public static String edInstr(final int i) {
		switch(i) {
			case S_LSHIFTL: return("LSHIFTL");   // Extension to S-Code:  Left shift logical
			case S_LSHIFTA: return("LSHIFTA");   // Extension to S-Code:  Left shift arithmetical
			case S_RSHIFTL: return("RSHIFTL");  // Extension to S-Code:  Right shift logical
			case S_RSHIFTA: return("RSHIFTA"); // Extension to S-Code:  Right shift arithmetical
		
			case S_NULL: return("NULL");
			case S_RECORD: return("RECORD");
			case S_PREFIX: return("PREFIX");
			case S_ATTR: return("ATTR");
			case S_REP: return("REP");
			case S_ALT: return("ALT");
			case S_FIXREP: return("FIXREP");
			case S_ENDRECORD: return("ENDRECORD");
			case S_C_RECORD: return("C-RECORD"); 
			case S_TEXT: return("TEXT");
			case S_C_CHAR: return("C-CHAR");
			case S_C_INT: return("C-INT");
			case S_C_SIZE: return("C-SIZE");
			case S_C_REAL: return("C-REAL");
			case S_C_LREAL: return("C-LREAL");
			case S_C_AADDR: return("C-AADDR");
			case S_C_OADDR: return("C-OADDR");
			case S_C_GADDR: return("C-GADDR");
			case S_C_PADDR: return("C-PADDR");
			case S_C_DOT: return("C-DOT");
			case S_C_RADDR: return("C-RADDR");
			case S_NOBODY: return("NOBODY");
			case S_ANONE: return("ANONE");
			case S_ONONE: return("ONONE");
			case S_GNONE: return("GNONE");
			case S_NOWHERE: return("NOWHERE");
			case S_TRUE: return("TRUE");
			case S_FALSE: return("FALSE");
			case S_PROFILE: return("PROFILE");
			case S_KNOWN: return("KNOWN");
			case S_SYSTEM: return("SYSTEM");
			case S_EXTERNAL: return("EXTERNAL");
			case S_IMPORT: return("IMPORT");
			case S_EXPORT: return("EXPORT");
			case S_EXIT: return("EXIT");
			case S_ENDPROFILE: return("ENDPROFILE");
			case S_ROUTINESPEC: return("ROUTINESPEC");
			case S_ROUTINE: return("ROUTINE");
			case S_LOCAL: return("LOCAL");
			case S_ENDROUTINE: return("ENDROUTINE");
			case S_MODULE: return("MODULE");
			case S_EXISTING: return("EXISTING");
			case S_TAG: return("TAG");
			case S_BODY: return("BODY");
			case S_ENDMODULE: return("ENDMODULE");
			case S_LABELSPEC: return("LABELSPEC");
			case S_LABEL: return("LABEL");
			case S_RANGE: return("RANGE");
			case S_GLOBAL: return("GLOBAL");
			case S_INIT: return("INIT");
			case S_CONSTSPEC: return("CONSTSPEC");
			case S_CONST: return("CONST");
			case S_DELETE: return("DELETE");
			case S_FDEST: return("FDEST");
			case S_BDEST: return("BDEST");
			case S_SAVE: return("SAVE");
			case S_RESTORE: return("RESTORE");
			case S_BSEG: return("BSEG");
			case S_ESEG: return("ESEG");
			case S_SKIPIF: return("SKIPIF");
			case S_ENDSKIP: return("ENDSKIP");
			case S_IF: return("IF");
			case S_ELSE: return("ELSE");
			case S_ENDIF: return("ENDIF");
			case S_PRECALL: return("PRECALL");
			case S_ASSPAR: return("ASSPAR");
			case S_ASSREP: return("ASSREP");
			case S_CALL: return("CALL");
			case S_FETCH: return("FETCH");
			case S_REFER: return("REFER");
			case S_DEREF: return("DEREF");
			case S_SELECT: return("SELECT");
			case S_REMOTE: return("REMOTE");
			case S_LOCATE: return("LOCATE");
			case S_INDEX: return("INDEX");
			case S_INCO: return("INCO");
			case S_DECO: return("DECO");
			case S_PUSH: return("PUSH");
			case S_PUSHC: return("PUSHC");
			case S_PUSHLEN: return("PUSHLEN");
			case S_DUP: return("DUP");
			case S_POP: return("POP");
			case S_EMPTY: return("EMPTY");
			case S_SETOBJ: return("SETOBJ");
			case S_GETOBJ: return("GETOBJ");
			case S_ACCESS: return("ACCESS");
			case S_FJUMP: return("FJUMP");
			case S_BJUMP: return("BJUMP");
			case S_FJUMPIF: return("FJUMPIF");
			case S_BJUMPIF: return("BJUMPIF");
			case S_SWITCH: return("SWITCH");
			case S_GOTO: return("GOTO");
			case S_T_INITO: return("INITO"); 
			case S_T_GETO: return("T-GETO");
			case S_T_SETO: return("T-SETO");
			case S_ADD: return("ADD");
			case S_SUB: return("SUB"); 
			case S_MULT: return("MULT");
			case S_DIV: return("DIV");
			case S_REM: return("REM");
			case S_NEG: return("NEG"); 
			case S_AND: return("AND");
			case S_OR : return("OR");
			case S_XOR: return("XOR");
			case S_IMP: return("IMP");
			case S_EQV: return("EQV");
			case S_NOT: return("NOT");
			case S_DIST: return("DIST");
			case S_ASSIGN: return("ASSIGN");
			case S_UPDATE: return("UPDATE");
			case S_CONVERT: return("CONVERT");
			case S_SYSINSERT: return("SYSINSERT"); 
			case S_INSERT: return("INSERT");
			case S_ZEROAREA: return("ZEROAREA");
			case S_INITAREA: return("INITAREA");
			case S_COMPARE: return("COMPARE");
			case S_LT: return("LT");
			case S_LE: return("LE");
			case S_EQ: return("EQ");
			case S_GE: return("GE");
			case S_GT: return("GT");
			case S_NE: return("NE");
			case S_EVAL: return("EVAL");
			case S_INFO: return("INFO");
			case S_LINE: return("LINE");
			case S_SETSWITCH: return("SETSWITCH");
			case S_PROGRAM: return("PROGRAM");
			case S_MAIN: return("MAIN");
			case S_ENDPROGRAM: return("ENDPROGRAM");
			case S_DSIZE: return("DSIZE");
			case S_SDEST: return("SDEST");
			case S_RUPDATE: return("RUPDATE");
			case S_ASSCALL: return("ASSCALL");
			case S_CALL_TOS: return("CALL_TOS");
			case S_DINITAREA: return("DINITAREA");
			case S_NOSIZE: return("NOSIZE");
			case S_POPALL: return("POPALL");
			case S_REPCALL: return("REPCALL"); 
			case S_INTERFACE: return("INTERFACE");
			case S_MACRO: return("MACRO");
			case S_MARK: return("MARK");
			case S_MPAR: return("MPAR");
			case S_ENDMACRO: return("ENDMACRO");
			case S_MCALL: return("MCALL");
			case S_PUSHV: return("PUSHV");
			case S_SELECTV: return("SELECTV");
			case S_REMOTEV: return("REMOTEV");
			case S_INDEXV: return("INDEXV");
			case S_ACCESSV: return("ACCESSV");
			case S_DECL: return("DECL");
			case S_STMT: return("STMT");
			default: return("UNKNOWN:"+i);
		}
	}

}
