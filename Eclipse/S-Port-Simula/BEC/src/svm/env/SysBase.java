/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm.env;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bec.Global;
import bec.Option;
import bec.descriptor.Kind;
import bec.scode.Type;
import bec.util.EndProgram;
import bec.util.Util;
import svm.RTStack;
import svm.RTUtil;
import svm.instruction.SVM_CALL_SYS;
import svm.segment.DataSegment;
import svm.value.BooleanValue;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;
import svm.value.ProgramAddress;
import svm.value.Value;

/// Basic System Routines
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/env/SysBase.java"><b>Source File</b></a>.
/// 
/// @author Simula Standard
/// @author S-Port: The Environment Interface
/// @author Øystein Myhre Andersen
public abstract class SysBase {
	
	/** Default Constructor */ public SysBase() {} 

	/// Initialisation of the environment
	///
	///		Visible sysroutine("INITIA") INITIA;
	///		import entry(PXCHDL) exchdl  end;
	///
	/// 	Runtime Stack
	/// 	   ..., exchdl →
	/// 	   ...
	///
	/// Initialisation of the environment.
	///
	///	The import parameter 'exchdl' will be the RADDR of the routine that is to
	/// be called if the environment gets control after an interrupt.
	public static void initia() {
		SVM_CALL_SYS.ENTER("INITIA: ", 0, 1); // exportSize, importSize
		ProgramAddress exchdl = (ProgramAddress) RTStack.pop();
		if(Option.verbose) IO.println("SVM_SYSCALL.initia: "+exchdl);
		SVM_CALL_SYS.EXIT("INITIA: ");
	}
	
	/// Terminate program
	///
	///		Visible sysroutine("TERMIN") TERMIN;
	///		import range(0:3) code; infix(string) msg  end;
	///
	/// 	Runtime Stack
	/// 	   ..., code, msg'addr, msg'ofst, msg'nchr →
	/// 	   ...
	///
	/// Execution of a Simula program will be concluded by a call on terminate.
	/// <pre>
	///	code: Completion code:
	///		0 - normal termination
	///		1 - user requested termination
	///		2 - termination after error in user program
	///		3 - termination after Simula system error
	///
	///	msg: Possible message to the user.
	/// </pre>
	public static void terminate() {
		SVM_CALL_SYS.ENTER("TERMIN: ", 0, 4); // exportSize, importSize
		String str = RTStack.popString();
		int code = RTStack.popInt();
		if(Option.DUMPS_AT_EXIT) {
//			Segment.lookup("DSEG_ADHOC02").dump("SVM_SYSCALL.terminate: ");
			Global.DSEG.dump("SVM_SYSCALL.terminate: FINAL DATA SEGMENT ");
			Global.CSEG.dump("SVM_SYSCALL.terminate: FINAL CONSTANT SEGMENT ");
			Global.TSEG.dump("SVM_SYSCALL.terminate: FINAL CONSTANT TEXT SEGMENT ");
//			Segment.lookup("DSEG_RT").dump("SVM_SYSCALL.terminate: BIOINS", 30, 82);
//			Segment.lookup("POOL_1").dump("SVM_SYSCALL.terminate: FINAL POOL_1", 0, 20);
			RTUtil.printPool("POOL_1");
		}

		throw new EndProgram(code,"SVM_SYSCALL.terminate: "+str+" with exit code " + code);
	}
	
	/// Test strings for equality
	///
	///		Visible sysroutine("STREQL") STREQL;
	///		import infix(string) str1,str2; export boolean res  end;
	///
	/// 	Runtime Stack
	/// 	   ..., str1'oaddr, str1'ofst, str1'nchr, str2'oaddr, str2'ofst, str2'nchr →
	/// 	   ..., res
	///
	/// First: Pop the two strings off the Runtime stack
	/// <br> Then test for equality, 'res' true if equals
	/// <br> Finally: push the 'res' onto the Runtime stack.
	public static void stringEqual() {
		SVM_CALL_SYS.ENTER("STREQL: ", 1, 6); // exportSize, importSize
		String str1 = RTStack.popString();
		String str2 = RTStack.popString();
		boolean result = str1.equals(str2);
		RTStack.push(BooleanValue.of(result));
		SVM_CALL_SYS.EXIT("STREQL: " + result);
	}

	/// Move information
	///
	///		Visible sysroutine("MOVEIN") MOVEIN;
	///		import ref() from,to; size length  end;
	///
	/// 	Runtime Stack
	/// 	   ..., from'oaddr, to'ofst, length'size →
	/// 	   ...
	///
	///	An implementation will be safe (with regards to overlap) if the area
	/// is moved sequentially starting with the object unit identified by "from"
	/// (this unit must be moved to the unit identified by "to").
	///
	///		From:   The "lowest" object address of the area to be moved.
	///		To:     The "lowest" object address of the destination area.
	///		Length: The size of the area to be moved.
	public static void movein() {
		SVM_CALL_SYS.ENTER("MOVEIN: ", 0, 5); // exportSize, importSize		
		int lng = RTStack.popInt();
		ObjectAddress to = RTStack.popOADDR();
		ObjectAddress from = RTStack.popOADDR();
		for(int i=0;i<lng;i++) {
			Value val = from.load(i);
			to.store(i, val);
		}
		SVM_CALL_SYS.EXIT("MOVEIN: ");
	}
	
	/// Zero fill Area
	///
	///		Visible inline'routine ("ZEROAREA")  ZEROAREA;
	///		import ref() fromAddr, toAddr; end;
	///
	/// 	Runtime Stack
	/// 	   ..., from'oaddr, to'oaddr →
	/// 	   ...
	///
	/// The area between fromAddr and toAddr (from included, toAddr not) is to be zero-filled.
	///
	/// In this implementation the area is null-filled.
	public static void zeroarea() {
		SVM_CALL_SYS.ENTER("ZEROAREA: ", 0, 2); // exportSize, importSize
		ObjectAddress toAddr = RTStack.popOADDR();
		ObjectAddress fromAddr = RTStack.popOADDR();
		ObjectAddress from = fromAddr.addOffset(0);
		ObjectAddress to = toAddr.addOffset(0);
		if(! from.segID.equals(to.segID)) Util.IERR("");
		DataSegment dseg = from.segment();
		int idxFrom = from.ofst;
		int idxTo = to.ofst;
		for(int i=idxFrom; i<idxTo;i++) {
			dseg.store(i, null);
		}
		RTStack.push(null); // TODO: ?????
		SVM_CALL_SYS.EXIT("ZEROAREA: ");
	}
	
	/// Define Work Area
	///
	///		Visible sysroutine ("DWAREA")  DWAREA;
	///		import size lng; range(0:255) ano;
	///		export ref() pool  end;
	///
	/// 	Runtime Stack
	/// 	   ..., lng, ano →
	/// 	   ..., pool
	///
	/// The routine def work area must return a reference to a contiguous area.
	///
	///	lng: The wanted size of the work area.
	///
	/// ano: Identification of the work area in question.
	///
	/// The result 'pool', which is pushed onto the Runtime stack, is the
	/// object address of the first allocation point within the reserved area.
	public static void dwarea() {
		SVM_CALL_SYS.ENTER("DWAREA: ", 1, 2); // exportSize, importSize
		int warea = RTStack.popInt();
		int lng = RTStack.popInt();
		DataSegment pool = new DataSegment("POOL_" + warea, Kind.K_SEG_DATA);
		pool.emitDefaultValue(1, lng);
		RTStack.push(ObjectAddress.ofSegAddr(pool, 0));
		SVM_CALL_SYS.EXIT("DWAREA: ");
	}

	/// Processor Time Usage
	///
	///		Visible sysroutine("CPUTIM") CPUTIM;
	///		export long real sec  end;
	///
	/// 	Runtime Stack
	/// 	   ... →
	/// 	   ..., sec
	///
	/// The elapsed cpu-time measured in seconds is pushed onto the Runtime stack.
	public static void cputime() {
		SVM_CALL_SYS.ENTER("CPUTIM: ", 0, 1); // exportSize, importSize
		RTStack.push(null);
		SVM_CALL_SYS.EXIT("CPUTIM: ");
	}
	
	/// Date and time
	///
	///		Visible sysroutine("DATTIM") DATTIM;
	///		import infix(string) item; export integer filled  end;
	///
	/// 	Runtime Stack
	/// 	   ..., item'oaddr, item'ofst, item'nchr →
	/// 	   ..., filled
	///
	///
	/// The result of a call on the routine will be filled into the string 'item'.
	/// <br>The length of the filled part is pushed onto the Runtime stack.
	///
	/// The string should have the following syntax:
	///
	///		 "yyyy-mm-dd hh:nn:ss.ppp"
	public static void dattim() {
		SVM_CALL_SYS.ENTER("DATTIM: ", 1, 3); // exportSize, importSize
		int nchr = RTStack.popInt();
		ObjectAddress oaddr = RTStack.popGADDRasOADDR();
		
		// String s = "2025-06-27 08:22:13.123";
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String s = LocalDateTime.now().format(form);
		int length = s.length();
		if(nchr < length) {
			RTUtil.set_STATUS(24);
			length = 0;
		} else {
			for(int i=0;i<length;i++) {
				Value val = IntegerValue.of(Type.T_CHAR, s.charAt(i));
				oaddr.store(i, val);
			}
		}
		RTStack.push(IntegerValue.of(Type.T_INT, length));
		SVM_CALL_SYS.EXIT("DATTIM: ");
	}
	
	/// Blank fill
	///
	///		Visible known("CBLNK") C_BLNK; import infix(string) str;
	///
	/// 	Runtime Stack
	/// 	   ..., str'addr, str'ofst, str'nchr →
	/// 	   ...
	///
	/// Blankfill the area specified by the argument 'str'.
	///
	public static void cblnk() {
		SVM_CALL_SYS.ENTER("CBLNK: ", 0, 3); // exportSize, importSize
		int nchr = RTStack.popInt();
		ObjectAddress addr = RTStack.popGADDRasOADDR();
		DataSegment dseg = addr.segment();
		int idx = addr.ofst;
		IntegerValue blnk = IntegerValue.of(Type.T_CHAR, ' ');
		for(int i=0;i<nchr;i++) {
			dseg.store(idx + i, blnk);		
		}
		SVM_CALL_SYS.EXIT("CBLNK: ");
	}
	
	/// Move characters
	///
	///		Visible known("CMOVE") C_MOVE; import infix(string) src,dst;
	///
	/// 	Runtime Stack
	/// 	   ..., src'addr, src'ofst, src'nchr, dst'addr, dst'ofst, dst'nchr →
	/// 	   ...
	///
	/// Transfer all the characters in the source 'src' to the destination 'dst'.
	/// <br>If source is longer than destination, the remaining characters are ignored.
	/// <br>Blankfill any remaining characters of the destination.
	///
	public static void cmove() {
		SVM_CALL_SYS.ENTER("CMOVE: ", 0, 6); // exportSize, importSize
		int dstNchr = RTStack.popInt();
		ObjectAddress dstAddr = RTStack.popGADDRasOADDR();
		int srcNchr = RTStack.popInt();
		ObjectAddress srcAddr = RTStack.popGADDRasOADDR();
		int n = srcNchr; int rst = dstNchr-n;
		if(rst < 0) { n = dstNchr; rst = 0; };
		DataSegment dstSeg = dstAddr.segment();
		int dstIdx = dstAddr.ofst;
		
		if(srcAddr != null) { // Move characters
			DataSegment srcSeg = srcAddr.segment();
			int srcIdx = srcAddr.ofst;
			for(int i=0;i<n;i++) {
				Value val = srcSeg.load(srcIdx + i);
				dstSeg.store(dstIdx + i, val);
			}
		}
		
		if(rst > 0) { // Blank Fill
			IntegerValue blnk = IntegerValue.of(Type.T_CHAR, ' ');
			for(int i=0;i<rst;i++) {
				dstSeg.store(dstIdx + (n++), blnk);
			}
		}
		SVM_CALL_SYS.EXIT("CMOVE: ");
	}

	/// Verbose
	///
	///		Visible sysroutine("VERBOSE") VERBOSE;
	///		export integer result  end;
	///
	/// 	Runtime Stack
	/// 	   ... →
	/// 	   ..., result
	///
	public static void verbose() {
		SVM_CALL_SYS.ENTER("VERBOSE: ", 1, 0); // exportSize, importSize
		boolean RUNTIME_VERBOSE = false;
		RTStack.push(BooleanValue.of(RUNTIME_VERBOSE));
		SVM_CALL_SYS.EXIT("VERBOSE: ");
	}

	/// Visible sysroutine("DMPSEG") DMPSEG;
	/// import infix(string) segnam; integer start,lng  end;

	/// Dump an Entity
	///
	///		Visible sysroutine("DMPENT") DMPENT;
	///		import ref() rtAddr;  end;
	///
	/// 	Runtime Stack
	/// 	   ..., rtAddr →
	/// 	   ...
	///
	/// Dump an Entity on Standard output.
	public static void dmpent() {
		SVM_CALL_SYS.ENTER("DMPENT: ", 0, 1); // exportSize, importSize
		ObjectAddress oaddr = RTStack.popOADDR();
		IO.println("SVM_SYSCALL.dmpent: "+oaddr);
		RTUtil.dumpEntity(oaddr);
		SVM_CALL_SYS.EXIT("DMPENT: ");
	}

	/// Visible sysroutine("DMPOOL") DMPOOL; -- Dump POOL_n
	/// import integer n;  end;


}
