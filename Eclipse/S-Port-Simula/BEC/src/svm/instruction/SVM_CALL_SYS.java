/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.instruction;

import java.io.IOException;
import bec.Global;
import bec.Option;
import bec.util.AttributeInputStream;
import bec.util.AttributeOutputStream;
import bec.util.Util;
import svm.CallStackFrame;
import svm.RTStack;
import svm.env.SysDeEdit;
import svm.env.SysDraw;
import svm.env.SysEdit;
import svm.env.SysFile;
import svm.env.SysInfo;
import svm.env.SysMath;
import svm.env.SysBase;
import svm.value.BooleanValue;
import svm.value.ProgramAddress;

/// SVM-INSTRUCTION: SVM_CALL_SYS
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/instruction/SVM_CALL_SYS.java"><b>Source File</b></a>.
/// 
/// @author S-Port: Definition of S-code
/// @author Øystein Myhre Andersen
public class SVM_CALL_SYS extends SVM_Instruction {
	
	/// The routine kind code
	private final int kind;

	/// Construct a new SVM_CALL_SYS instruction
	/// @param kind the routine kind code
	public SVM_CALL_SYS(int kind) {
		this.opcode = SVM_Instruction.iCALLSYS;
		if(kind == 0) Util.IERR("Undefined System Routine: " + kind);
		this.kind = kind;
	}

	@Override
	public void execute() {
		switch(kind) {
			case P_VERBOSE:  SysBase.verbose(); break;
			case P_INITIA:   SysBase.initia(); break;
			case P_DWAREA:   SysBase.dwarea(); break;
			case P_ZEROAREA: SysBase.zeroarea(); break;
			case P_DATTIM:   SysBase.dattim(); break;
			case P_CPUTIM:   SysBase.cputime(); break;
			case P_TERMIN:   SysBase.terminate(); break;
			case P_STREQL:   SysBase.stringEqual(); break;
			case P_MOVEIN:   SysBase.movein(); break;

			case P_DMPENT:   SysBase.dmpent(); break;
			
			case P_GINTIN:   SysInfo.getIntInfo(); break;
			case P_SIZEIN:   SysInfo.getSizeInfo(); break;
			case P_GVIINF:   SysInfo.giveIntInfo(); break;

			case P_GDSPEC:   SysFile.gdspec(); break;
			case P_GETLPP:   SysFile.getlpp(); break;
			case P_OPFILE:   SysFile.opfile(); break;
			case P_NEWPAG:   SysFile.newpag(); break;
			case P_CLFILE:   SysFile.clfile(); break;
			case P_PRINTO:   SysFile.printo(); break;
			case P_INIMAG:   SysFile.INIMAG(); break;
			case P_OUTIMA:   SysFile.OUTIMA(); break;
			case P_BREAKO:   SysFile.BREAKO(); break;
			case P_INBYTE:   SysFile.INBYTE(); break;
			case P_OUTBYT:   SysFile.OUTBYT(); break;
			case P_LOCATE:   SysFile.LOCATE(); break;
			case P_MAXLOC:   SysFile.MXLOC();  break;
			case P_LSTLOC:   SysFile.LSTLOC(); break;

			case P_GETINT:   SysDeEdit.getint(); break;
			case P_GTFRAC:   SysDeEdit.getfrac(); break;
			case P_GTREAL:   SysDeEdit.getreal(); break;

			case P_PUTSTR:   SysEdit.putstr(); break;
			case P_PUTINT:   SysEdit.putint(); break;
			case P_PUTINT2:  SysEdit.putint2(); break;
			case P_PTFRAC:   SysEdit.putfrac(); break;
			case P_PTREAL:   SysEdit.putreal(); break;
			case P_PTREAL2:  SysEdit.putreal2(); break;
			case P_PLREAL:   SysEdit.putlreal(); break;
			case P_PLREAL2:  SysEdit.putlreal2(); break;
			case P_PUTFIX:   SysEdit.putfix(); break;
			case P_PUTFIX2:  SysEdit.putfix2(); break;
			case P_PTLFIX:   SysEdit.putlfix(); break;
			case P_PTLFIX2:  SysEdit.putlfix2(); break;
			case P_PUTHEX:   SysEdit.puthex(); break;
			case P_PUTSIZE:  SysEdit.putsize(); break;
			case P_PTOADR2:  SysEdit.ptoadr2(); break;
			case P_PTPADR2:  SysEdit.ptpadr2(); break;
			case P_PTRADR2:  SysEdit.ptradr2(); break;
			case P_PTAADR2:  SysEdit.ptaadr2(); break;
			case P_PTGADR2:  SysEdit.ptgadr2(); break;
			
			case P_RADDEP:   SysMath.raddep(); break;
			case P_RSUBEP:   SysMath.rsubep(); break;
			case P_DADDEP:   SysMath.daddep(); break;
			case P_DSUBEP:   SysMath.dsubep(); break;
			case P_MODULO:   SysMath.modulo(); break;
			case P_IIPOWR:   SysMath.iipowr(); break;
			case P_RIPOWR:   SysMath.ripowr(); break;
			case P_DIPOWR:   SysMath.dipowr(); break;
			case P_RRPOWR:   SysMath.rrpowr(); break;
			case P_DDPOWR:   SysMath.ddpowr(); break;
			
			case P_RSQROO:   SysMath.rsqroo(); break;
			case P_SQROOT:   SysMath.sqroot(); break;
			case P_RLOGAR:   SysMath.rlogar(); break;
			case P_LOGARI:   SysMath.logari(); break;
			case P_REXPON:   SysMath.rexpon(); break;
			case P_EXPONE:   SysMath.expone(); break;

			case P_RSINUS:   SysMath.rsinus(); break;
			case P_SINUSR:   SysMath.sinusr(); break;
			case P_RCOSIN:   SysMath.rcosin(); break;
			case P_COSINU:   SysMath.cosinu(); break;
			case P_RARTAN:   SysMath.rartan(); break;
			case P_ARCTAN:   SysMath.arctan(); break;

			case P_DRAWRP:   SysDraw.drawrp(); break;

			case P_CMOVE:    SysBase.cmove(); break;
			case P_CBLNK:    SysBase.cblnk(); break;
			default: Util.IERR("SVM_SYSCALL: Unknown System Routine " + edKind(kind));
		}
		Global.PSC.ofst++;
	}
	
	/// System Routine prolog
	/// @param ident the Routine ident
	/// @param exportSize the Export size
	/// @param importSize the Import size
	public static void ENTER(String ident, int exportSize, int importSize) {
		if(Option.EXEC_TRACE > 4)
			RTStack.dumpRTStack(ident+"ENTER: ");
		int rtStackIndex = RTStack.size() - (exportSize + importSize);
		CallStackFrame callStackFrame = new CallStackFrame(ident, rtStackIndex, exportSize, importSize);
		RTStack.callStack.push(callStackFrame);
		
		if(Option.EXEC_TRACE > 0) {
			ProgramAddress.printInstr("CALLSYS  " + ident,false);
			if(Option.EXEC_TRACE > 2)
				RTStack.callStack_TOP().dump(ident+"ENTER: ");
		}

		if(Option.CALL_TRACE_LEVEL > 0)
			RTStack.printCallTrace("SVM_CALLSYS.ENTER: ");
	}
	
	/// System Routine epilog
	/// @param ident the Routine ident
	public static void EXIT(String ident) {
		if(Option.CALL_TRACE_LEVEL > 0)
			RTStack.printCallTrace("SVM_CALLSYS.EXIT: ");
		CallStackFrame top = RTStack.callStack.pop();
		if(Option.EXEC_TRACE > 2) {
			RTStack.callStack_TOP().dump(ident+"RETURN: Called from " + top.rutAddr);		
		}
	}
	
	@Override	
	public String toString() {
		return "CALLSYS  " + edKind(kind);
	}
	

	/// Search for System Routine kind code
	/// @param rutID the routine ident
	/// @return the kind code found or zero
	public static int getSysKind(String rutID) {
		if(rutID.equalsIgnoreCase("TERMIN")) return SVM_CALL_SYS.P_TERMIN;
		if(rutID.equalsIgnoreCase("INTRHA")) return SVM_CALL_SYS.P_INTRHA;
		if(rutID.equalsIgnoreCase("PXCHDL")) return SVM_CALL_SYS.P_PXCHDL;
//		if(rutID.equalsIgnoreCase("PEXERR")) return SVM_CALL_SYS.P_PEXERR;
		if(rutID.equalsIgnoreCase("PSIMOB")) return SVM_CALL_SYS.P_PSIMOB;
		if(rutID.equalsIgnoreCase("PobSML")) return SVM_CALL_SYS.P_PobSML;
		if(rutID.equalsIgnoreCase("Palloc")) return SVM_CALL_SYS.P_Palloc;
		if(rutID.equalsIgnoreCase("Pfree"))  return SVM_CALL_SYS.P_Pfree;
		if(rutID.equalsIgnoreCase("Pmovit")) return SVM_CALL_SYS.P_Pmovit;

		if(rutID.equalsIgnoreCase("STREQL")) return SVM_CALL_SYS.P_STREQL;
		if(rutID.equalsIgnoreCase("PRINTO")) return SVM_CALL_SYS.P_PRINTO;
		if(rutID.equalsIgnoreCase("INITIA")) return SVM_CALL_SYS.P_INITIA;
		if(rutID.equalsIgnoreCase("SETOPT")) return SVM_CALL_SYS.P_SETOPT;
		if(rutID.equalsIgnoreCase("DMPSEG")) return SVM_CALL_SYS.P_DMPSEG;
		if(rutID.equalsIgnoreCase("DMPENT")) return SVM_CALL_SYS.P_DMPENT;
		if(rutID.equalsIgnoreCase("DMPOOL")) return SVM_CALL_SYS.P_DMPOOL;
		if(rutID.equalsIgnoreCase("VERBOSE")) return SVM_CALL_SYS.P_VERBOSE;
		if(rutID.equalsIgnoreCase("GINTIN")) return SVM_CALL_SYS.P_GINTIN;
		if(rutID.equalsIgnoreCase("GTEXIN")) return SVM_CALL_SYS.P_GTEXIN;
		if(rutID.equalsIgnoreCase("SIZEIN")) return SVM_CALL_SYS.P_SIZEIN;
		if(rutID.equalsIgnoreCase("GVIINF")) return SVM_CALL_SYS.P_GVIINF;
		if(rutID.equalsIgnoreCase("GIVINF")) return SVM_CALL_SYS.P_GIVINF;
		if(rutID.equalsIgnoreCase("CPUTIM")) return SVM_CALL_SYS.P_CPUTIM;
		if(rutID.equalsIgnoreCase("DWAREA")) return SVM_CALL_SYS.P_DWAREA;
		if(rutID.equalsIgnoreCase("MOVEIN")) return SVM_CALL_SYS.P_MOVEIN;
		if(rutID.equalsIgnoreCase("OPFILE")) return SVM_CALL_SYS.P_OPFILE;
		if(rutID.equalsIgnoreCase("CLFILE")) return SVM_CALL_SYS.P_CLFILE;
		if(rutID.equalsIgnoreCase("LSTLOC")) return SVM_CALL_SYS.P_LSTLOC;
		if(rutID.equalsIgnoreCase("MAXLOC")) return SVM_CALL_SYS.P_MAXLOC;
		if(rutID.equalsIgnoreCase("CHKPNT")) return SVM_CALL_SYS.P_CHKPNT;
		if(rutID.equalsIgnoreCase("LOCKFI")) return SVM_CALL_SYS.P_LOCKFI;
		if(rutID.equalsIgnoreCase("UNLOCK")) return SVM_CALL_SYS.P_UNLOCK;
		if(rutID.equalsIgnoreCase("INIMAG")) return SVM_CALL_SYS.P_INIMAG;
		if(rutID.equalsIgnoreCase("OUTIMA")) return SVM_CALL_SYS.P_OUTIMA;
		if(rutID.equalsIgnoreCase("BREAKO")) return SVM_CALL_SYS.P_BREAKO;
		if(rutID.equalsIgnoreCase("LOCATE")) return SVM_CALL_SYS.P_LOCATE;
		if(rutID.equalsIgnoreCase("DELETE")) return SVM_CALL_SYS.P_DELETE;
		if(rutID.equalsIgnoreCase("GDSNAM")) return SVM_CALL_SYS.P_GDSNAM;
		if(rutID.equalsIgnoreCase("GDSPEC")) return SVM_CALL_SYS.P_GDSPEC;
		if(rutID.equalsIgnoreCase("GETLPP")) return SVM_CALL_SYS.P_GETLPP;
		if(rutID.equalsIgnoreCase("NEWPAG")) return SVM_CALL_SYS.P_NEWPAG;
		if(rutID.equalsIgnoreCase("PRINTO")) return SVM_CALL_SYS.P_PRINTO;
		if(rutID.equalsIgnoreCase("STREQL")) return SVM_CALL_SYS.P_STREQL;
		if(rutID.equalsIgnoreCase("INBYTE")) return SVM_CALL_SYS.P_INBYTE;
		if(rutID.equalsIgnoreCase("OUTBYT")) return SVM_CALL_SYS.P_OUTBYT;
		if(rutID.equalsIgnoreCase("GETINT")) return SVM_CALL_SYS.P_GETINT;
		if(rutID.equalsIgnoreCase("GTREAL")) return SVM_CALL_SYS.P_GTREAL;
		if(rutID.equalsIgnoreCase("GTFRAC")) return SVM_CALL_SYS.P_GTFRAC;
		if(rutID.equalsIgnoreCase("PUTSTR")) return SVM_CALL_SYS.P_PUTSTR;
		if(rutID.equalsIgnoreCase("PUTINT")) return SVM_CALL_SYS.P_PUTINT;
		if(rutID.equalsIgnoreCase("PUTINT2")) return SVM_CALL_SYS.P_PUTINT2;
		if(rutID.equalsIgnoreCase("PUTSIZE")) return SVM_CALL_SYS.P_PUTSIZE;
		if(rutID.equalsIgnoreCase("PUTHEX")) return SVM_CALL_SYS.P_PUTHEX;
		if(rutID.equalsIgnoreCase("PUTFIX")) return SVM_CALL_SYS.P_PUTFIX;
		if(rutID.equalsIgnoreCase("PUTFIX2")) return SVM_CALL_SYS.P_PUTFIX2;
		if(rutID.equalsIgnoreCase("PTLFIX")) return SVM_CALL_SYS.P_PTLFIX;
		if(rutID.equalsIgnoreCase("PTLFIX2")) return SVM_CALL_SYS.P_PTLFIX2;
		if(rutID.equalsIgnoreCase("PTREAL")) return SVM_CALL_SYS.P_PTREAL;
		if(rutID.equalsIgnoreCase("PTREAL2")) return SVM_CALL_SYS.P_PTREAL2;
		if(rutID.equalsIgnoreCase("PLREAL")) return SVM_CALL_SYS.P_PLREAL;
		if(rutID.equalsIgnoreCase("PLREAL2")) return SVM_CALL_SYS.P_PLREAL2;
		if(rutID.equalsIgnoreCase("PTFRAC")) return SVM_CALL_SYS.P_PTFRAC;
		if(rutID.equalsIgnoreCase("PTSIZE")) return SVM_CALL_SYS.P_PTSIZE;
		if(rutID.equalsIgnoreCase("PTOADR")) return SVM_CALL_SYS.P_PTOADR;
		if(rutID.equalsIgnoreCase("PTOADR2")) return SVM_CALL_SYS.P_PTOADR2;
		if(rutID.equalsIgnoreCase("PTAADR")) return SVM_CALL_SYS.P_PTAADR;
		if(rutID.equalsIgnoreCase("PTAADR2")) return SVM_CALL_SYS.P_PTAADR2;
		if(rutID.equalsIgnoreCase("PTGADR")) return SVM_CALL_SYS.P_PTGADR;
		if(rutID.equalsIgnoreCase("PTGADR2")) return SVM_CALL_SYS.P_PTGADR2;
		if(rutID.equalsIgnoreCase("PTPADR")) return SVM_CALL_SYS.P_PTPADR;
		if(rutID.equalsIgnoreCase("PTPADR2")) return SVM_CALL_SYS.P_PTPADR2;
		if(rutID.equalsIgnoreCase("PTRADR")) return SVM_CALL_SYS.P_PTRADR;
		if(rutID.equalsIgnoreCase("PTRADR2")) return SVM_CALL_SYS.P_PTRADR2;
		if(rutID.equalsIgnoreCase("DRAWRP")) return SVM_CALL_SYS.P_DRAWRP;
		if(rutID.equalsIgnoreCase("DATTIM")) return SVM_CALL_SYS.P_DATTIM;
		if(rutID.equalsIgnoreCase("LOWTEN")) return SVM_CALL_SYS.P_LOWTEN;
		if(rutID.equalsIgnoreCase("DCMARK")) return SVM_CALL_SYS.P_DCMARK;
		if(rutID.equalsIgnoreCase("RSQROO")) return SVM_CALL_SYS.P_RSQROO;
		if(rutID.equalsIgnoreCase("SQROOT")) return SVM_CALL_SYS.P_SQROOT;
		if(rutID.equalsIgnoreCase("RLOGAR")) return SVM_CALL_SYS.P_RLOGAR;
		if(rutID.equalsIgnoreCase("LOGARI")) return SVM_CALL_SYS.P_LOGARI;
		if(rutID.equalsIgnoreCase("RLOG10")) return SVM_CALL_SYS.P_RLOG10;
		if(rutID.equalsIgnoreCase("DLOG10")) return SVM_CALL_SYS.P_DLOG10;
		if(rutID.equalsIgnoreCase("REXPON")) return SVM_CALL_SYS.P_REXPON;
		if(rutID.equalsIgnoreCase("EXPONE")) return SVM_CALL_SYS.P_EXPONE;
		if(rutID.equalsIgnoreCase("RSINUS")) return SVM_CALL_SYS.P_RSINUS;
		if(rutID.equalsIgnoreCase("SINUSR")) return SVM_CALL_SYS.P_SINUSR;
		if(rutID.equalsIgnoreCase("RCOSIN")) return SVM_CALL_SYS.P_RCOSIN;
		if(rutID.equalsIgnoreCase("COSINU")) return SVM_CALL_SYS.P_COSINU;
		if(rutID.equalsIgnoreCase("RTANGN")) return SVM_CALL_SYS.P_RTANGN;
		if(rutID.equalsIgnoreCase("TANGEN")) return SVM_CALL_SYS.P_TANGEN;
		if(rutID.equalsIgnoreCase("RCOTAN")) return SVM_CALL_SYS.P_RCOTAN;
		if(rutID.equalsIgnoreCase("COTANG")) return SVM_CALL_SYS.P_COTANG;
		if(rutID.equalsIgnoreCase("RARTAN")) return SVM_CALL_SYS.P_RARTAN;
		if(rutID.equalsIgnoreCase("ARCTAN")) return SVM_CALL_SYS.P_ARCTAN;
		if(rutID.equalsIgnoreCase("RARCOS")) return SVM_CALL_SYS.P_RARCOS;
		if(rutID.equalsIgnoreCase("ARCCOS")) return SVM_CALL_SYS.P_ARCCOS;
		if(rutID.equalsIgnoreCase("RARSIN")) return SVM_CALL_SYS.P_RARSIN;
		if(rutID.equalsIgnoreCase("ARCSIN")) return SVM_CALL_SYS.P_ARCSIN;
		if(rutID.equalsIgnoreCase("RATAN2")) return SVM_CALL_SYS.P_RATAN2;
		if(rutID.equalsIgnoreCase("ATAN2")) return SVM_CALL_SYS.P_ATAN2;
		if(rutID.equalsIgnoreCase("RSINH")) return SVM_CALL_SYS.P_RSINH;
		if(rutID.equalsIgnoreCase("SINH")) return SVM_CALL_SYS.P_SINH;
		if(rutID.equalsIgnoreCase("RCOSH")) return SVM_CALL_SYS.P_RCOSH;
		if(rutID.equalsIgnoreCase("COSH")) return SVM_CALL_SYS.P_COSH;
		if(rutID.equalsIgnoreCase("RTANH")) return SVM_CALL_SYS.P_RTANH;
		if(rutID.equalsIgnoreCase("TANH")) return SVM_CALL_SYS.P_TANH;
		if(rutID.equalsIgnoreCase("BEGDEB")) return SVM_CALL_SYS.P_BEGDEB;
		if(rutID.equalsIgnoreCase("ENDDEB")) return SVM_CALL_SYS.P_ENDDEB;
		if(rutID.equalsIgnoreCase("BEGTRP")) return SVM_CALL_SYS.P_BEGTRP;
		if(rutID.equalsIgnoreCase("ENDTRP")) return SVM_CALL_SYS.P_ENDTRP;
		if(rutID.equalsIgnoreCase("GTPADR")) return SVM_CALL_SYS.P_GTPADR;
		if(rutID.equalsIgnoreCase("GTOUTM")) return SVM_CALL_SYS.P_GTOUTM;
		if(rutID.equalsIgnoreCase("GTLNID")) return SVM_CALL_SYS.P_GTLNID;
		if(rutID.equalsIgnoreCase("GTLNO")) return SVM_CALL_SYS.P_GTLNO;
		if(rutID.equalsIgnoreCase("BRKPNT")) return SVM_CALL_SYS.P_BRKPNT;
		if(rutID.equalsIgnoreCase("STMNOT")) return SVM_CALL_SYS.P_STMNOT;
		if(rutID.equalsIgnoreCase("DMPOBJ")) return SVM_CALL_SYS.P_DMPOBJ;

		// KNOWN 
		if(rutID.equalsIgnoreCase("MODULO")) return SVM_CALL_SYS.P_MODULO;
		if(rutID.equalsIgnoreCase("RADDEP")) return SVM_CALL_SYS.P_RADDEP;
		if(rutID.equalsIgnoreCase("DADDEP")) return SVM_CALL_SYS.P_DADDEP;
		if(rutID.equalsIgnoreCase("RSUBEP")) return SVM_CALL_SYS.P_RSUBEP;
		if(rutID.equalsIgnoreCase("DSUBEP")) return SVM_CALL_SYS.P_DSUBEP;
		if(rutID.equalsIgnoreCase("IIPOWR")) return SVM_CALL_SYS.P_IIPOWR;
		if(rutID.equalsIgnoreCase("RIPOWR")) return SVM_CALL_SYS.P_RIPOWR;
		if(rutID.equalsIgnoreCase("RRPOWR")) return SVM_CALL_SYS.P_RRPOWR;
		if(rutID.equalsIgnoreCase("RDPOWR")) return SVM_CALL_SYS.P_RDPOWR;
		if(rutID.equalsIgnoreCase("DIPOWR")) return SVM_CALL_SYS.P_DIPOWR;
		if(rutID.equalsIgnoreCase("DRPOWR")) return SVM_CALL_SYS.P_DRPOWR;
		if(rutID.equalsIgnoreCase("DDPOWR")) return SVM_CALL_SYS.P_DDPOWR;

		return 0;
	}


	/// Search for Known Routine kind code
	/// @param rutID the routine ident
	/// @return the kind code found or zero
	public static int getKnownKind(String rutID) {
		if(rutID.equalsIgnoreCase("CBLNK")) return SVM_CALL_SYS.P_CBLNK;
		if(rutID.equalsIgnoreCase("CMOVE")) return SVM_CALL_SYS.P_CMOVE;

//		Util.IERR(""+s);
		return 0;
	}

//	---------     S y s t e m    K i n d    C o d e s      ---------
	
	/// Edit system routine kind
	/// @param kind the kind code
	/// @return the edited string
	public static String edKind(int kind) {
		switch(kind) {
		case P_TERMIN: return "TERMIN";
		case P_INTRHA: return "INTRHA";
		case P_PXCHDL: return "PXCHDL";
		case P_PEXERR: return "PEXERR";
		case P_PSIMOB: return "PSIMOB";
		case P_PobSML: return "PobSML";
		case P_Palloc: return "Palloc";
		case P_Pfree:  return "Pfree";
		case P_Pmovit: return "Pmovit";
		case P_STREQL: return "STREQL";
		case P_PRINTO: return "PRINTO";
		case P_INITIA: return "INITIA";
		case P_SETOPT: return "SETOPT";
		case P_DMPSEG: return "DMPSEG";
		case P_DMPENT: return "DMPENT";
		case P_DMPOOL: return "DMPOOL";
		case P_VERBOSE: return "VERBOSE";
		case P_GINTIN: return "GINTIN";
		case P_GTEXIN: return "GTEXIN";
		case P_SIZEIN: return "SIZEIN";
		case P_GVIINF: return "GVIINF";
		case P_GIVINF: return "GIVINF";
		case P_CPUTIM: return "CPUTIM";
		case P_DWAREA: return "DWAREA";
		case P_ZEROAREA: return "ZEROAREA";
		case P_MOVEIN: return "MOVEIN";
//
//		case P_STRIP:  return  "STRIP";  // Known
//		case P_TRFREL: return "TRFREL"; // Known
		
		case P_OPFILE: return "OPFILE";
		case P_CLFILE: return "CLFILE";
		case P_LSTLOC: return "LSTLOC";
		case P_MAXLOC: return "MXLOC";
		case P_CHKPNT: return "CHECKP";
		case P_LOCKFI: return "LOCKFI";
		case P_UNLOCK: return "UPLOCK";
		case P_INIMAG: return "INIMAG";
		case P_OUTIMA: return "OUTIMA";
		case P_BREAKO: return "BREAKO";
		case P_LOCATE: return "LOCATE";
		case P_DELETE: return "DELETE";
		case P_GDSNAM: return "GDSNAM";
		case P_GDSPEC: return "GDSPEC";
		case P_GETLPP: return "GETLPP";
		case P_NEWPAG: return "NEWPAG";
		case P_INBYTE: return "INBYTE";
		case P_OUTBYT: return "OUTBYT";
		case P_GETINT: return "GETINT";
		case P_GTREAL: return "GTREAL";
		case P_GTFRAC: return "GTFRAC";
		case P_PUTSTR: return "PUTSTR";
		case P_PUTINT: return "PUTINT";
		case P_PUTINT2: return "PUTINT2";
		case P_PUTSIZE: return "PUTSIZE";
		case P_PUTHEX: return "PUTHEX";
		case P_PUTFIX: return "PUTFIX";
		case P_PUTFIX2: return "PUTFIX2";
		case P_PTLFIX: return "PTLFIX";
		case P_PTLFIX2: return "PTLFIX2";
		case P_PTREAL: return "PTREAL";
		case P_PTREAL2: return "PTREAL2";
		case P_PLREAL: return "PLREAL";
		case P_PLREAL2: return "PLREAL2";
		case P_PTFRAC: return "PTFRAC";
		case P_PTSIZE: return "PTSIZE";
		case P_PTOADR: return "PTOADR";
		case P_PTOADR2: return "PTOADR2";
		case P_PTAADR: return "PTAADR";
		case P_PTAADR2: return "PTAADR2";
		case P_PTGADR: return "PTGADR";
		case P_PTGADR2: return "PTGADR2";
		case P_PTPADR: return "PTPADR";
		case P_PTPADR2: return "PTPADR2";
		case P_PTRADR: return "PTRADR";
		case P_PTRADR2: return "PTRADR2";
		case P_DRAWRP: return "DRAWRP";
		case P_DATTIM: return "DATTIM";
		case P_LOWTEN: return "LTEN";
		case P_DCMARK: return "DECMRK";
		case P_RSQROO: return "RSQROO";
		case P_SQROOT: return "SQROOT";
		case P_RLOGAR: return "RLOGAR";
		case P_LOGARI: return "LOGARI";
		case P_RLOG10: return "RLOG10";
		case P_DLOG10: return "DLOG10";
		case P_REXPON: return "REXPON";
		case P_EXPONE: return "EXPONE";
		case P_RSINUS: return "RSINUS";
		case P_SINUSR: return "SINUSR";
		case P_RCOSIN: return "RCOSIN";
		case P_COSINU: return "COSINU";
		case P_RTANGN: return "RTANGN";
		case P_TANGEN: return "TANGEN";
		case P_RCOTAN: return "COTANR";
		case P_COTANG: return "COTANG";
		case P_RARTAN: return "RARTAN";
		case P_ARCTAN: return "ARCTAN";
		case P_RARCOS: return "RARCOS";
		case P_ARCCOS: return "ARCCOS";
		case P_RARSIN: return "RARSIN";
		case P_ARCSIN: return "ARCSIN";
		case P_RATAN2: return "ATAN2R";
		case P_ATAN2: return "ATAN2";
		case P_RSINH: return "SINHR";
		case P_SINH: return "SINH";
		case P_RCOSH: return "COSHR";
		case P_COSH: return "COSH";
		case P_RTANH: return "TANHR";
		case P_TANH: return "TANH";
		case P_BEGDEB: return "BEGDEB";
		case P_ENDDEB: return "ENDDEB";
		case P_BEGTRP: return "BEGTRP";
		case P_ENDTRP: return "ENDTRP";
		case P_GTPADR: return "GTPADR";
		case P_GTOUTM: return "GTOUTM";
		case P_GTLNID: return "GTLNID";
		case P_GTLNO: return "GTLNO";
		case P_BRKPNT: return "BRKPNT";
		case P_STMNOT: return "STMNOT";
		case P_DMPOBJ: return "DMPOBJ";

		// KNOWN
		case P_MODULO: return "MODULO";
		case P_RADDEP: return "RADDEP";
		case P_DADDEP: return "DADDEP";
		case P_RSUBEP: return "RSUBEP";
		case P_DSUBEP: return "DSUBEP";
		case P_IIPOWR: return "IIPOWR";
		case P_RIPOWR: return "RIPOWR";
		case P_RRPOWR: return "RRPOWR";
		case P_RDPOWR: return "RDPOWR";
		case P_DIPOWR: return "DIPOWR";
		case P_DRPOWR: return "DRPOWR";
		case P_DDPOWR: return "DDPOWR";

		case P_CBLNK: return "CBLNK";
		case P_CMOVE: return "CMOVE";
}
		return "UNKNOWN:" + kind;
	}

	/** System Routine kind */ public static final int P_TERMIN=1; // System profile
	/** System Routine kind */ public static final int P_INTRHA=2; // System profile
	/** System Routine kind */ public static final int P_PXCHDL=140; // System profile
	/** System Routine kind */ public static final int P_PEXERR=141; // System profile
	/** System Routine kind */ public static final int P_PSIMOB=142; // System profile
	/** System Routine kind */ public static final int P_PobSML=143; // System profile
	/** System Routine kind */ public static final int P_Palloc=144; // System profile
	/** System Routine kind */ public static final int P_Pfree =145; // System profile
	/** System Routine kind */ public static final int P_Pmovit=146; // System profile
	/** System Routine kind */ public static final int P_STREQL=3; // System routine
	/** System Routine kind */ public static final int P_PRINTO=4; // System routine
	/** System Routine kind */ public static final int P_INITIA=5; // System routine
	/** System Routine kind */ public static final int P_SETOPT=6; // System routine
	/** System Routine kind */ public static final int P_DMPSEG=7; // System routine
	/** System Routine kind */ public static final int P_DMPENT=8; // System routine
	/** System Routine kind */ public static final int P_DMPOOL=9; // System routine
	/** System Routine kind */ public static final int P_VERBOSE=10; // System routine
	/** System Routine kind */ public static final int P_GINTIN=11; // System routine
	/** System Routine kind */ public static final int P_GTEXIN=12; // System routine
	/** System Routine kind */ public static final int P_SIZEIN=13; // System routine
	/** System Routine kind */ public static final int P_GVIINF=14; // System routine
	/** System Routine kind */ public static final int P_GIVINF=15; // System routine
	/** System Routine kind */ public static final int P_CPUTIM=16; // System routine
	/** System Routine kind */ public static final int P_DWAREA=17; // System routine
	/** System Routine kind */ public static final int P_MOVEIN=18; // System routine

	/** System Routine kind */ public static final int P_OPFILE=19; // OPFILE;
	/** System Routine kind */ public static final int P_CLFILE=20; // CLFILE;
	/** System Routine kind */ public static final int P_LSTLOC=21; // LSTLOC;
	/** System Routine kind */ public static final int P_MAXLOC=22; // MXLOC;
	/** System Routine kind */ public static final int P_CHKPNT=23; // CHECKP;
	/** System Routine kind */ public static final int P_LOCKFI=24; // LOCKFI;
	/** System Routine kind */ public static final int P_UNLOCK=25; // UPLOCK;
	/** System Routine kind */ public static final int P_INIMAG=26; // INIMAG;
	/** System Routine kind */ public static final int P_OUTIMA=27; // OUTIMA;
	/** System Routine kind */ public static final int P_BREAKO=28; // BREAKO;
	/** System Routine kind */ public static final int P_LOCATE=29; // LOCATE;
	/** System Routine kind */ public static final int P_DELETE=30; // DELETE;
	/** System Routine kind */ public static final int P_GDSNAM=31; // GDSNAM;
	/** System Routine kind */ public static final int P_GDSPEC=32; // GDSPEC;
	/** System Routine kind */ public static final int P_GETLPP=33; // GETLPP;
	/** System Routine kind */ public static final int P_NEWPAG=34; // NEWPAG;
	/** System Routine kind */ public static final int P_INBYTE=35; // INBYTE;
	/** System Routine kind */ public static final int P_OUTBYT=36; // OUTBYT;
	/** System Routine kind */ public static final int P_GETINT=37; // GETINT;
	/** System Routine kind */ public static final int P_GTREAL=38; // GTREAL;
	/** System Routine kind */ public static final int P_GTFRAC=39; // GTFRAC;
	/** System Routine kind */ public static final int P_PUTSTR=40; // PUTSTR;
	/** System Routine kind */ public static final int P_PUTINT=41; // PUTINT;
	/** System Routine kind */ public static final int P_PUTINT2=42; // PUTINT2;
	/** System Routine kind */ public static final int P_PUTSIZE=43; // PUTSIZE;
	/** System Routine kind */ public static final int P_PUTHEX=44; // PUTHEX;
	/** System Routine kind */ public static final int P_PUTFIX=45; // PUTFIX;
	/** System Routine kind */ public static final int P_PUTFIX2=46; // PUTFIX2;
	/** System Routine kind */ public static final int P_PTLFIX=47; // PTLFIX;
	/** System Routine kind */ public static final int P_PTLFIX2=48; // PTLFIX2;
	/** System Routine kind */ public static final int P_PTREAL=49; // PTREAL;
	/** System Routine kind */ public static final int P_PTREAL2=50; // PTREAL2;
	/** System Routine kind */ public static final int P_PLREAL=51; // PLREAL;
	/** System Routine kind */ public static final int P_PLREAL2=52; // PLREAL2;
	/** System Routine kind */ public static final int P_PTFRAC=53; // PTFRAC;
	/** System Routine kind */ public static final int P_PTSIZE=54; // PTSIZE;
	/** System Routine kind */ public static final int P_PTOADR=55; // PTOADR;
	/** System Routine kind */ public static final int P_PTOADR2=56; // PTOADR2;
	/** System Routine kind */ public static final int P_PTAADR=57; // PTAADR;
	/** System Routine kind */ public static final int P_PTAADR2=58; // PTAADR2;
	/** System Routine kind */ public static final int P_PTGADR=59; // PTGADR;
	/** System Routine kind */ public static final int P_PTGADR2=60; // PTGADR2;
	/** System Routine kind */ public static final int P_PTPADR=61; // PTPADR;
	/** System Routine kind */ public static final int P_PTPADR2=62; // PTPADR2;
	/** System Routine kind */ public static final int P_PTRADR=63; // PTRADR;
	/** System Routine kind */ public static final int P_PTRADR2=64; // PTRADR2;
	/** System Routine kind */ public static final int P_DRAWRP=65; // DRAWRP;
	/** System Routine kind */ public static final int P_DATTIM=66; // DATTIM;
	/** System Routine kind */ public static final int P_LOWTEN=67; // LTEN;
	/** System Routine kind */ public static final int P_DCMARK=68; // DECMRK;
	/** System Routine kind */ public static final int P_RSQROO=69; // RSQROO;
	/** System Routine kind */ public static final int P_SQROOT=70; // SQROOT;
	/** System Routine kind */ public static final int P_RLOGAR=71; // RLOGAR;
	/** System Routine kind */ public static final int P_LOGARI=72; // LOGARI;
	/** System Routine kind */ public static final int P_RLOG10=73; // RLOG10;
	/** System Routine kind */ public static final int P_DLOG10=74; // DLOG10;
	/** System Routine kind */ public static final int P_REXPON=75; // REXPON;
	/** System Routine kind */ public static final int P_EXPONE=76; // EXPONE;
	/** System Routine kind */ public static final int P_RSINUS=77; // RSINUS;
	/** System Routine kind */ public static final int P_SINUSR=78; // SINUSR;
	/** System Routine kind */ public static final int P_RCOSIN=79; // RCOSIN;
	/** System Routine kind */ public static final int P_COSINU=80; // COSINU;
	/** System Routine kind */ public static final int P_RTANGN=81; // RTANGN;
	/** System Routine kind */ public static final int P_TANGEN=82; // TANGEN;
	/** System Routine kind */ public static final int P_RCOTAN=83; // COTANR;
	/** System Routine kind */ public static final int P_COTANG=84; // COTANG;
	/** System Routine kind */ public static final int P_RARTAN=85; // RARTAN;
	/** System Routine kind */ public static final int P_ARCTAN=86; // ARCTAN;
	/** System Routine kind */ public static final int P_RARCOS=87; // RARCOS;
	/** System Routine kind */ public static final int P_ARCCOS=88; // ARCCOS;
	/** System Routine kind */ public static final int P_RARSIN=89; // RARSIN;
	/** System Routine kind */ public static final int P_ARCSIN=90; // ARCSIN;
	/** System Routine kind */ public static final int P_RATAN2=91; // ATAN2R;
	/** System Routine kind */ public static final int P_ATAN2=92; // ATAN2;
	/** System Routine kind */ public static final int P_RSINH=93; // SINHR;
	/** System Routine kind */ public static final int P_SINH=94; // SINH;
	/** System Routine kind */ public static final int P_RCOSH=95; // COSHR;
	/** System Routine kind */ public static final int P_COSH=96; // COSH;
	/** System Routine kind */ public static final int P_RTANH=97; // TANHR;
	/** System Routine kind */ public static final int P_TANH=98; // TANH;
	/** System Routine kind */ public static final int P_BEGDEB=99; // BEGDEB;
	/** System Routine kind */ public static final int P_ENDDEB=100; // ENDDEB;
	/** System Routine kind */ public static final int P_BEGTRP=101; // BEGTRP;
	/** System Routine kind */ public static final int P_ENDTRP=102; // ENDTRP;
	/** System Routine kind */ public static final int P_GTPADR=103; // GTPADR;
	/** System Routine kind */ public static final int P_GTOUTM=104; // GTOUTM;
	/** System Routine kind */ public static final int P_GTLNID=105; //  GTLNID;
	/** System Routine kind */ public static final int P_GTLNO=106; // GTLNO;
	/** System Routine kind */ public static final int P_BRKPNT=107; // BRKPNT;
	/** System Routine kind */ public static final int P_STMNOT=108; // STMNOT;
	/** System Routine kind */ public static final int P_DMPOBJ=109; //  DMPOBJ;

	// KNOWN
	/** System Routine kind */ public static final int P_MODULO=110; //  KNOWN
	/** System Routine kind */ public static final int P_RADDEP=111; //  KNOWN
	/** System Routine kind */ public static final int P_DADDEP=112; //  KNOWN
	/** System Routine kind */ public static final int P_RSUBEP=113;
	/** System Routine kind */ public static final int P_DSUBEP=114;
	/** System Routine kind */ public static final int P_IIPOWR=115;
	/** System Routine kind */ public static final int P_RIPOWR=116;
	/** System Routine kind */ public static final int P_RRPOWR=117;
	/** System Routine kind */ public static final int P_RDPOWR=118;
	/** System Routine kind */ public static final int P_DIPOWR=119;
	/** System Routine kind */ public static final int P_DRPOWR=120;
	/** System Routine kind */ public static final int P_DDPOWR=121;
	/** System Routine kind */ public static final int P_STRIP=122;  // Known("STRIP")
	/** System Routine kind */ public static final int P_TRFREL=123; // Known("TRFREL")
	/** System Routine kind */ public static final int P_ZEROAREA=124; // Known("TRFREL")
	/** System Routine kind */ public static final int P_CBLNK=125; // Known("CBLNK")
	/** System Routine kind */ public static final int P_CMOVE=126; // Known("CMOVE")

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void write(AttributeOutputStream oupt) throws IOException {
		if(Option.ATTR_OUTPUT_TRACE) IO.println("SVM.Write: " + this);
		oupt.writeByte(opcode);
		oupt.writeByte(kind);
	}

	/// Reads an SVM_CALL_SYS instruction from the given input.
	/// @param inpt the input stream
	/// @return the SVM_CALL_SYS instruction read
	/// @throws IOException if IOException occur
	public static SVM_Instruction read(AttributeInputStream inpt) throws IOException {
		SVM_CALL_SYS instr = new SVM_CALL_SYS(inpt.readUnsignedByte());
		if(Option.ATTR_INPUT_TRACE) IO.println("SVM.Read: " + instr);
		return instr;
	}


}
