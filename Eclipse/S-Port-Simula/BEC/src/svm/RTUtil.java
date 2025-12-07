/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package svm;

import bec.Option;
import bec.scode.Type;
import bec.util.Util;
import svm.segment.DataSegment;
import svm.segment.Segment;
import svm.value.IntegerValue;
import svm.value.ObjectAddress;
import svm.value.Value;

/// Runtime Utilities.
///
///
/// Link to GitHub: <a href="https://github.com/portablesimula/EclipseWorkSpaces/blob/main/S-Port-Simula/BEC/src/svm/RTUtil.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class RTUtil {

	/** Default Constructor */ public RTUtil() {} 

	/// Points to RTS-Module RT's DataSegment
	private static DataSegment DSEG_RT;
	
	/// Current Decimal mark character
	public static int CURRENTDECIMALMARK;
	
	/// Current lowten character
	public static int CURRENTLOWTEN;
	
	/// Initiate local data
	public static void init() {
		DSEG_RT = (DataSegment) Segment.lookup("DSEG_RT");
		CURRENTDECIMALMARK = '.';
		CURRENTLOWTEN = '&';
	}
	
	/// Update the 'itemSize' variable in RTS-Module RT.
	/// @param itemSize the new value
	public static void set_ITEM_SIZE(int itemSize) {
		int offset_ITSIZE = 2;
		DSEG_RT.store(offset_ITSIZE, IntegerValue.of(Type.T_SIZE, itemSize));	
	}
	
	/// Update the 'status' variable in RTS-Module RT.
	/// @param status the new value
	public static void set_STATUS(int status) {
		if(Option.verbose) IO.println("RTUtil.set_STATUS: " + status + "  " + edStatus(status));
		int offset_STATUS = 1;
		DSEG_RT.store(offset_STATUS, IntegerValue.of(Type.T_INT, status));	
	}
	
	/// Returns the String description of the given 'status'
	/// @param status the status code
	/// @return the String description of the given 'status'
	public static String edStatus(int status) {
		switch(status) {
			case 0: return"(not used)";
					// Zero can never be returned from a routine. In case everything is OK the value of
					// status is not changed.
			case 1: return"Invalid filekey";
					// The key is within the interval 1..255, but no data set is associated with the key.
					// The file may have been closed, and consequently the filekey is again undefined.
			case 2: return"File not defined";
					// No real file associated with local name. The data set specification does not
					// correspond to either a descriptor name or a data set name.
			case 3: return"File does not exist";
					// The file association has been given, but the fysical file specified does not exist.
					// The data set specification refers a descriptor but this descriptor does not identify
					// a data set.
			case 4: return"File already exists";
					// An attempt has been made to create a file which already exists.
					// Some systems could allow you to define several files with the same
					// name, e.g. scratch files. This should not occur.
			case 5: return"File not open";
					// An operation on a file is asked for, but the file is not open.
			case 6: return"File already open";
					// A request for file opening has been made on a file which is already open.
			case 7: return"File already closed";
					// For some exterior reason the data set has been closed outside the control of the
					// Simula system, (e.g. a tape has been dismounted by the operator).
			case 8: return"Illegal use of file";
					// The data set organisation is incompatible with the wanted usage as given in
					// filetype, e.g. an attempt to read from an outfile.
			case 9: return"Illegal record format for directfile";
					// The external record format is not compatible with the directfile definition.
			case 10: return"Illegal filename";
					// The string specified does not follow the syntax of a file name in this system.
			case 11: return"Output image too long";
					// The image length is longer than the file record on an attempt to write on the file.
			case 12: return"Input image too short";
					// When reading from a file, the image is not large enough to hold the complete
					// record to be read.
			case 13: return"End of file on input";
					// When reading from a file, the end of file record was read.
			case 14: return"Not enough space available";
					// When work space is asked for, and the specified amount of storage cannot be
					// allocated.
			case 15: return"File full on output";
					// When writing to a file, the space allocated to the file is exhausted, and no more
					// space can be furnished.
			case 16: return"Location out of range";
					// When reading from a file, the specified record in the directfile has never been
					// written. When writing or positioning in a file, the specified location will bring
					// us outside the area reserved for the file.
			case 17: return"I/O error, e.g. hardware fault";
					// Any hardware detected error which does not refer to an error done by the user.
			case 18: return"Specified action cannot be performed";
					// The specified action for open file or close file has not been implemented,
					// and consequently cannot be performed.
			case 19: return"Impossible";
					// This will mean that it is impossible to implement the the specified effect, or that the
					// request has been defined illegal. This status is returned as a signal to the run time
					// system that it need not bother to try recovery, the program should be aborted.
					// There will normally be a separate specification of the interpretation of this code
					// under each routine that can give this return value.
			case 20: return"No write access to this file";
					// Writing has been requested to a file that has been protected against writing.
			case 21: return"Non-numeric item as first character";
					// The de-editing of a string to a numeric item has been requested, but the string does
					// not start according to the syntax of a numeric item.
			case 22: return"Value out of range";
					// The de-editing of a string to a numeric item has been requested, but the result is a
					// numeric item that is to large to be represented in the specified type.
			case 23: return"Incomplete syntax";
					// The de-editing of a string to a real item has been requested, but the string does not
					// complete a real item according to the syntax of a real item.
			case 24: return"Text string too short";
					// The editing of a numeric item into a string has been requested, but the string is
					// too short to contain the result of the editing operation.
			case 25: return"Fraction part less than zero";
					// The editing of a real as a floating point or fixed point real has been requested,
					// but the fraction part has been specified with a negative length.
			case 26: return"No read access to this file.";
					// Reading has been attempted on a read-protected file.
			case 27: return"Argument out of range for system routine";
					// The code refers mainly to the matematical library routines, and indicate that one
					// of the arguments were out of range.
			case 28: return"Key previously defined";
					// This specifies that the generation of a key has been made for a file which already
					// has a key referencing it.
			case 29: return"Maximum number of keys exceeded";
					// The S-port system restricts the number of files that may be open simultaneously
					// to 255, it is however expected that the target system's limit is lower.
					// If any of these limits are exceeded this status is returned from open.
			case 30: return"This service function is not implemented";
					// One of the give_ or get_ routines have been called with an index which is not known
					// in this implementation, or which has not been implemented. Some default value will
					// be assumed.
			case 31: return"Syntax error in dsetspec";
			case 32: return"No read access";
			case 33: return"Illegal action";
			case 34: return"Partial record read.";
			case 35: return"Undefined record (on directfile).";
			case 36: return"Maximum number of breakpoints set";
			default: return"UNDEFINED";
		}
	}

	// Instance sorts:  instances must be first
	/** Instance sort code */ public static final int S_NOSORT =  0; //  no sort
	/** Instance sort code */ public static final int S_SUB    =  1; //  Sub-Block
	/** Instance sort code */ public static final int S_PRO    =  2; //  Procedure
	/** Instance sort code */ public static final int S_ATT    =  3; //  Attached Class
	/** Instance sort code */ public static final int S_DET    =  4; //  Detached Class
	/** Instance sort code */ public static final int S_RES    =  5; //  Resumed Class
	/** Instance sort code */ public static final int S_TRM    =  6; //  Terminated Class
	/** Instance sort code */ public static final int S_PRE    =  7; //  Prefixed Block
	/** Instance sort code */ public static final int S_THK    =  8; //  Thunk
	/** Instance sort code */ public static final int S_SAV    =  9; //  Save Object
	/** Instance sort code */ public static final int S_ALLOC  = 10; //  object allocated on request (not SIMULA)
	// Special entity sorts:
	/** Instance sort code */ public static final int S_GAP    = 11; //  Dynamic Storage Gap
	/** Instance sort code */ public static final int S_TXTENT = 12; //  Text Entity
	/** Instance sort code */ public static final int S_ARHEAD = 13; //  Array Head Entity
	/** Instance sort code */ public static final int S_ARBODY = 14; //  Array Body Entity      (3 or more dimensions)
	/** Instance sort code */ public static final int S_ARBREF = 15; //  ref-Array Body Entity  (3 or more dimensions)
	/** Instance sort code */ public static final int S_ARBTXT = 16; //  text-Array Body Entity (3 or more dimensions)
	/** Instance sort code */ public static final int S_ARENT2 = 17; //  Array Body Entity      (two dimensions)
	/** Instance sort code */ public static final int S_ARREF2 = 18; //  ref-Array Body Entity  (two dimensions)
	/** Instance sort code */ public static final int S_ARTXT2 = 19; //  text-Array Body Entity (two dimensions)
	/** Instance sort code */ public static final int S_ARENT1 = 20; //  Array Body Entity      (one dimension)
	/** Instance sort code */ public static final int S_ARREF1 = 21; //  ref-Array Body Entity  (one dimension)
	/** Instance sort code */ public static final int S_ARTXT1 = 22; //  text-Array Body Entity (one dimension)
	/** Instance sort code */ public static final int MAX_SORT = 22;

//	 Visible record entity;  info "DYNAMIC";
//	 begin ref(inst)                sl;   -- during GC used as GCL!!!!
//	       range(0:MAX_BYT)         sort;
//	       range(0:MAX_BYT)         misc;
//	       variant ref(ptp)         pp;   -- used for instances
//	       variant range(0:MAX_TXT) ncha; -- used for text entities
//	       variant size             lng;  -- used for other entities
//	 end;
//
//	 Visible record inst:entity;
//	 begin ref(entity)              gcl;
//	       variant ref(inst)        dl;
//	               label            lsc;
//	       variant entry(Pmovit)    moveIt;
//	 end;

//	%title ******   P r o t o t y p e s   ******
//	 Visible record ptp;
//	 begin ref(pntvec) refVec; -- pnt_vec
//	       ref(rptvec) repVec;
//	       ref(ptpExt) xpp;
//	       size lng;
//	 end;
	
	/// Returns the length of an Entity
	/// @param ent the Entity
	/// @return the length of an Entity
	public static int length(ObjectAddress ent) {
		IntegerValue sort = (IntegerValue) ent.addOffset(1).load(0);
		Value variant = ent.addOffset(3).load(0);
		switch(sort.value) {
			case S_SUB, S_PRO, S_ATT, S_DET, S_RES, S_TRM, S_PRE:
				ObjectAddress pp = (ObjectAddress) variant;
				IntegerValue lngVal= (IntegerValue) pp.addOffset(3).load(0);
				return lngVal.value;
			case S_TXTENT:
				lngVal = (IntegerValue) variant;
				return lngVal.value + 4;
			default:
				lngVal = (IntegerValue) variant;
				return lngVal.value;
		}
	}
	
	/// Debug utility: Dump an Entity
	/// @param ent the Entity
	public static void dumpEntity(ObjectAddress ent) {
		IntegerValue sort = (IntegerValue) ent.addOffset(1).load(0);
		int lng = length(ent);
		DataSegment dseg = ent.segment();
		int from = ent.ofst;
		int to = from + lng;
		dseg.dump("Entity: " + edSort(sort.value) + ": ", from, to);
	}
	
//	 Visible record ptpExt;  --- Prototype extension
//	 begin ref(idfier)          idt;
//	       ref(modinf)       modulI;
//	       ref(atrvec)        attrV; -- List of attributes (or none).
//	       range(0:MAX_BLK)  blkTyp; -- Block type (SUB/PRO/FNC/CLA/PRE)
//	 end;
//
//	 Visible record idfier;   -- identifier
//	 begin range(0:MAX_BYT)   ncha;
//	       character          cha(0);
//	 end;
	/// Returns the Entity's ident
	/// @param ent the Entity
	/// @return the Entity's ident
	public static String entID(ObjectAddress ent) {
		try {
		IntegerValue sort = (IntegerValue) ent.addOffset(1).load(0);
		switch(sort.value) {
			case S_SUB, S_PRO, S_ATT, S_DET, S_RES, S_TRM, S_PRE:
				ObjectAddress pp = (ObjectAddress) ent.addOffset(3).load(0);
				ObjectAddress xpp = (ObjectAddress) pp.addOffset(2).load(0);
				if(xpp == null) return "Instance " + " " + edSort(sort.value);
				ObjectAddress idt = (ObjectAddress) xpp.load(0);
				IntegerValue ncha = (IntegerValue) idt.load(0);
				return edIDT(idt.addOffset(1), ncha.value) + " " + edSort(sort.value);
			default: return edSort(sort.value);
		}
		} catch(Exception e) {
			return "UNKNOWN";
		}
	}
	
	/// Returns an 'idfier' String
	/// @param idt address to the first character
	/// @param ncha the number of characters
	/// @return an 'idfier' String
	private static String edIDT(ObjectAddress idt, int ncha) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<ncha;i++) {
			IntegerValue cha = (IntegerValue) idt.addOffset(i).load(0);
			sb.append((char)((cha==null)?0:cha.value));
		}
		return sb.toString();		
	}
	
	/// Debug utility: Print an Entity
	/// @param ent the Entity
	/// @return false: no more to print
	public static boolean printEntity(ObjectAddress ent) {
		ObjectAddress sl = (ObjectAddress) ent.load(0);
		IntegerValue sort = (IntegerValue) ent.addOffset(1).load(0);
		if(sort == null) return false;
		IntegerValue misc = (IntegerValue) ent.addOffset(2).load(0);
		Value variant = ent.addOffset(3).load(0);
		IO.println("============ Entity " + entID(ent) + " ============");
		IO.println(""+ent +              ": SL    " + sl);
		IO.println(""+ent.addOffset(1) + ": SORT  " + sort);
		IO.println(""+ent.addOffset(2) + ": MISC  " + misc);
		int bdx = 0;
		int lng = length(ent);
		switch(sort.value) {
			case S_SUB, S_PRO, S_ATT, S_DET, S_RES, S_TRM, S_PRE:
				ObjectAddress pp = (ObjectAddress) variant;
				ObjectAddress gcl = (ObjectAddress) ent.addOffset(4).load(0);
				Value var1 = ent.addOffset(5).load(0);
				Value var2 = ent.addOffset(6).load(0);
				IO.println(""+ent.addOffset(3) + ": PP    " + pp);
				IO.println(""+ent.addOffset(4) + ": GCL   " + gcl);
				IO.println(""+ent.addOffset(5) + ": VAR   " + var1);
				IO.println(""+ent.addOffset(6) + ": VAR   " + var2);
				IntegerValue lngVal= (IntegerValue) pp.addOffset(3).load(0);
				lng = lngVal.value;
				bdx = 7;
				break;
			case S_TXTENT:
				lngVal = (IntegerValue) variant;
				IO.println(""+ent.addOffset(3) + ": LNG   " + lngVal);
				IO.println(""+ent.addOffset(4) + ": TXT   \"" + edIDT(ent.addOffset(4), lngVal.value) + '"');
				lng = 0;
				break;
			default:
				lngVal = (IntegerValue) variant;
				lng = lngVal.value;
				IO.println(""+ent.addOffset(3) + ": LNG   " + lngVal);
				bdx = 4;
		}
		
		
		for(int i=bdx;i<lng;i++) {
			IO.println(""+ent.addOffset(i) + ": BODY  " + ent.addOffset(i).load(0));
		}
		return true;
	}
	
	/// Debug utility: Dump current instance
	public static void dumpCurins() {
		DataSegment rt = (DataSegment) Segment.lookup("DSEG_RT");
		ObjectAddress curins = (ObjectAddress) rt.load(0);
		IO.println("RTUtil.dumpCurins: curins=" + curins);
		if(curins != null) RTUtil.printEntity(curins);
	}
	
	/// Debug utility: Print current instance
	public static void printCurins() {
		DataSegment rt = (DataSegment) Segment.lookup("DSEG_RT");
		ObjectAddress curins = (ObjectAddress) rt.load(0);
		RTUtil.printEntity(curins);
		IO.println("==================================");
	}
	
//    record area;                 -- Definition of storage pool
//    begin ref(area) suc;         -- Used to organize the pool list
//          ref(entity) nxt,lim;   -- Boundary pointers within the pool
//          ref(entity) startgc;   -- "freeze-address" for the pool
//          size stepsize;         -- extend/contract step
//          size mingap;           -- for this pool
//          short integer sequ;    -- Sequence number (1,2, ... )
//    end;

	/// Debug utility: Print a complete Pool
	/// @param segID the Segment ident of the Pool
	public static void printPool(String segID) {
		DataSegment dseg = (DataSegment) Segment.lookup(segID);
		dseg.dump("POOL_1: " , 0, 40);

		ObjectAddress pool = ObjectAddress.ofSegAddr(dseg, 0);
		ObjectAddress suc = (ObjectAddress) pool.load(0);
		ObjectAddress nxt = (ObjectAddress) pool.addOffset(1).load(0);
		ObjectAddress lim = (ObjectAddress) pool.addOffset(2).load(0);
		ObjectAddress startgc = (ObjectAddress) pool.addOffset(3).load(0);
		Value stepsize = pool.addOffset(4).load(0);
		Value mingap = pool.addOffset(5).load(0);
		Value sequ = pool.addOffset(6).load(0);
		IO.println("=============== " + segID + " ===============");
		IO.println(""+pool +              ": SUC      " + suc);
		IO.println(""+pool.addOffset(1) + ": NXT      " + nxt);
		IO.println(""+pool.addOffset(2) + ": LIM      " + lim);
		IO.println(""+pool.addOffset(3) + ": STARTGC  " + startgc);
		IO.println(""+pool.addOffset(4) + ": STEPSIZE " + stepsize);
		IO.println(""+pool.addOffset(5) + ": MINGAP   " + mingap);
		IO.println(""+pool.addOffset(6) + ": SEQU     " + sequ);
		
		ObjectAddress ent = ObjectAddress.ofSegAddr(dseg, 7);
		
		try {
			while(RTUtil.printEntity(ent)) {
				int lng = length(ent);
				ent = ent.addOffset(lng);				
			}
		} catch(Exception e) {
			e.printStackTrace();
			Util.IERR("");
		}
		IO.println("=============== END " + segID + " ===============");
	}

	
//	 Visible record bioIns:inst;
//	 begin ref(entity)            nxtAdr;   -- NOT in bioptp'refvec
//	       ref(entity)            lstAdr;   -- NOT in bioptp'refvec
//	       ref(prtEnt)            sysout;
//	       ref(filent)            sysin;
//	       ref(filent)            files;
//	       ref(filent)            opfil;    -- USED DURING OPEN
//	       ref(txtent)            opimg;    -- USED DURING OPEN
//	       ref(thunk)             thunks;
//	       ref(txtAr1)            conc;     -- For text conc. - pje sep 87
//	       ref(entity)            smbP1;    -- SIMOB parameter
//	       ref(entity)            smbP2;    -- SIMOB extra param (detach)
//	       integer                edOflo;
//	       long real              initim;
//	       range(0:MAX_ACT)       actLvl;
//	       range(0:MAX_EVT)       obsEvt;
//	       range(0:MAX_BYT)       pgleft; -- SIMOB-page lines left to write
//	       range(0:MAX_BYT)       pgsize; -- SIMOB-page lines per page
//	       range(0:MAX_BYT)       utpos;  -- Current output pos (0..utlng-1)
//	       range(0:MAX_KEY)       logfile; -- 0: no logfile attached
//	       boolean                logging; -- true: logfile att. and active
//	       boolean                stp;
//	       boolean                trc;
//	       boolean                realAr;  -- true if real arithm. possible
//	       character              lwten;
//	       character              dcmrk;
//	       character        utbuff(utlng);    -- The output buffer
//	       --- inbuff moved to MNTR, dumbuf moved to UTIL
//	       character           ebuf(600); -- edit buffer (leftadj/exactfit)
//	       short integer          GCval;  -- GCutil 2'param and return val
//	       infix(txtqnt)          simid;
//	       infix(labqnt)          erh;      -- Current error return label
//	       infix(quant)           ern;      -- Current error return variable
//	       ref(entity)            globalI;
//	       infix(string)          errmsg;   -- NOT in bioptp'refvec
//	 end;
	
	/// Debug utility: Print BASICIO
	public static void printBasicIO() {
		DataSegment dseg = (DataSegment) Segment.lookup("DSEG_RT");
		ObjectAddress ent = ObjectAddress.ofSegAddr(dseg, 0);
		IO.println("============ print BasicIO ============ " + dseg.size());
		int idx = 30;
		prt(dseg, ent, " SL      ", idx++);
		prt(dseg, ent, " SORT    ", idx++);
		prt(dseg, ent, " MISC    ", idx++);
		prt(dseg, ent, " PP      ", idx++);
		prt(dseg, ent, " GCL     ", idx++);
		prt(dseg, ent, " VAR     ", idx++);
		prt(dseg, ent, " VAR     ", idx++);
		prt(dseg, ent, " nxtAdr  ", idx++);
		prt(dseg, ent, " lstAdr  ", idx++);

		prt(dseg, ent, " sysout  ", idx++);
		prt(dseg, ent, " sysin   ", idx++);
		prt(dseg, ent, " files   ", idx++);
		prt(dseg, ent, " opfil   ", idx++);
		prt(dseg, ent, " opimg   ", idx++);
		prt(dseg, ent, " thunks  ", idx++);
		prt(dseg, ent, " conc    ", idx++);
		prt(dseg, ent, " smbP1   ", idx++);
		prt(dseg, ent, " smbP2   ", idx++);
		prt(dseg, ent, " edOflo  ", idx++);
		
		prt(dseg, ent, " initim  ", idx++);
		prt(dseg, ent, " actLvl  ", idx++);
		prt(dseg, ent, " obsEvt  ", idx++);
		prt(dseg, ent, " pgleft  ", idx++);
		prt(dseg, ent, " pgsize  ", idx++);
		prt(dseg, ent, " utpos   ", idx++);
		prt(dseg, ent, " logfile ", idx++);
		prt(dseg, ent, " logging ", idx++);
		prt(dseg, ent, " stp     ", idx++);
		prt(dseg, ent, " trc     ", idx++);

		prt(dseg, ent, " realAr  ", idx++);
		prt(dseg, ent, " lwten   ", idx++);
		prt(dseg, ent, " dcmrk   ", idx++); // 61
		
	    //   character        utbuff(utlng);    -- The output buffer
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<200;i++) {
			IntegerValue value = (IntegerValue) dseg.load(62+i);
			sb.append((char)((value==null)?0:value.value));
		}
		IO.println(""+ent.addOffset(62) + ":  utbuff   \"" + sb + '"');
		
	    //   character           ebuf(600); -- edit buffer (leftadj/exactfit)
		sb = new StringBuilder();
		for(int i=0;i<600;i++) {
			IntegerValue value = (IntegerValue) dseg.load(200+62+i);
			sb.append((char)((value==null)?0:value.value));
		}
		IO.println(""+ent.addOffset(200+62) + ": ebuff    \"" + sb + '"');
		idx = idx + 200 + 600;
		
		prt(dseg, ent, "GCval   ", idx++);
		prt(dseg, ent, "simid   ", idx++);
		prt(dseg, ent, "simid   ", idx++);
		prt(dseg, ent, "simid   ", idx++);
		prt(dseg, ent, "simid   ", idx++);
		prt(dseg, ent, "erh     ", idx++);
		prt(dseg, ent, "erh     ", idx++);
		prt(dseg, ent, "erh     ", idx++);
		prt(dseg, ent, "ern     ", idx++);
		prt(dseg, ent, "ern     ", idx++);
		prt(dseg, ent, "ern     ", idx++);
		prt(dseg, ent, "ern     ", idx++);
		prt(dseg, ent, "globalI ", idx++);
		prt(dseg, ent, "errmsg  ", idx++);
		prt(dseg, ent, "errmsg  ", idx++);
		prt(dseg, ent, "errmsg  ", idx++);
		IO.println("============ endof BasicIO ============");
	}
	
	/// Internal utility: prt
	/// @param dseg the DataSegment
	/// @param ent the Enity
	/// @param ident the ident
	/// @param ofst the offset
	private static void prt(DataSegment dseg, ObjectAddress ent, String ident, int ofst) {
		Value value = dseg.load(ofst);
		IO.println(""+ent.addOffset(ofst) + ": " + ident + " " + value);
		
	}
	
	/// Debug utility: Print DSEG_RRT
	public static void printDSEG_RT() {
		DataSegment dseg = (DataSegment) Segment.lookup("DSEG_RT");
//		ObjectAddress ent = new ObjectAddress("DSEG_RT", 0);
		ObjectAddress ent = ObjectAddress.ofSegAddr(dseg, 0);
		
//		dseg.dump("DSEG_RT ", 0, 60);
		
		IO.println("============ print DSEG_RT ============");
		prt(dseg, ent, " curins ", 0);
		prt(dseg, ent, " status ", 1);
		prt(dseg, ent, " itsize ", 2);
		prt(dseg, ent, " maxlen ", 3);
		prt(dseg, ent, " inplth ", 4);
		prt(dseg, ent, " outlth ", 5);
		prt(dseg, ent, " outlth ", 6);
		prt(dseg, ent, " tmp    ", 7);
		prt(dseg, ent, " tmp    ", 8);
		prt(dseg, ent, " tmp    ", 9);
		prt(dseg, ent, "tmp    ", 10);
		prt(dseg, ent, "maxint ", 11);
		prt(dseg, ent, "minint ", 12);
		prt(dseg, ent, "maxrnk ", 13);
		prt(dseg, ent, "maxrea ", 14);
		prt(dseg, ent, "minrea ", 15);
		prt(dseg, ent, "maxlrl ", 16);
		prt(dseg, ent, "minlrl ", 17);
		prt(dseg, ent, "inierr ", 18);
		prt(dseg, ent, "alloco ", 19);
		prt(dseg, ent, "freeob ", 20);
		prt(dseg, ent, "smb    ", 21);
		prt(dseg, ent, "obSML  ", 22);
		prt(dseg, ent, "actlvl ", 23);
		prt(dseg, ent, "txttmp ", 24);
		prt(dseg, ent, "txttmp ", 25);
		prt(dseg, ent, "txttmp ", 26);
		prt(dseg, ent, "txttmp ", 27);
		prt(dseg, ent, "rstr   ", 28);
		prt(dseg, ent, "rstr_x ", 29);
		prt(dseg, ent, "bio    ", 30);
		prt(dseg, ent, "bio    ", 31);
		IO.println("          ... rest of bio truncated");
		IO.println("============ endof DSEG_RT ============");
	}
	
	/// Edit and return the entity sort
	/// @param sort the entity sort
	/// @return the entity sort
	public static String edSort(int sort) {
		switch(sort) {
			case S_NOSORT: return("S_NOSORT"); //  no sort
			case S_SUB:    return("S_SUB"); //  Sub-Block
			case S_PRO:    return("S_PRO"); //  Procedure
			case S_ATT:    return("S_ATT"); //  Attached Class
			case S_DET:    return("S_DET"); //  Detached Class
			case S_RES:    return("S_RES"); //  Resumed Class
			case S_TRM:    return("S_TRM"); //  Terminated Class
			case S_PRE:    return("S_PRE"); //  Prefixed Block
			case S_THK:    return("S_THK"); //  Thunk
			case S_SAV:    return("S_SAV"); //  Save Object
			case S_ALLOC:  return("S_ALLOC"); //  object allocated on request (not SIMULA)
			case S_GAP:    return("S_GAP"); //  Dynamic Storage Gap
			case S_TXTENT: return("S_TXTENT"); //  Text Entity
			case S_ARHEAD: return("S_ARHEAD"); //  Array Head Entity
			case S_ARBODY: return("S_ARBODY"); //  Array Body Entity      (3 or more dimensions)
			case S_ARBREF: return("S_ARBREF"); //  ref-Array Body Entity  (3 or more dimensions)
			case S_ARBTXT: return("S_ARBTXT"); //  text-Array Body Entity (3 or more dimensions)
			case S_ARENT2: return("S_ARENT2"); //  Array Body Entity      (two dimensions)
			case S_ARREF2: return("S_ARREF2"); //  ref-Array Body Entity  (two dimensions)
			case S_ARTXT2: return("S_ARTXT2"); //  text-Array Body Entity (two dimensions)
			case S_ARENT1: return("S_ARENT1"); //  Array Body Entity      (one dimension)
			case S_ARREF1: return("S_ARREF1"); //  ref-Array Body Entity  (one dimension)
			case S_ARTXT1: return("S_ARTXT1"); //  text-Array Body Entity (one dimension)
		}
		return("UNKNOWN");
	}
	
	/// Move a String's characters to an area starting at 'dst'
	/// @param src the source String
	/// @param dst the start of the destination area
	public static void move(String src, ObjectAddress dst) {
		int lng = src.length();
		for(int i=0;i<lng;i++) {
			Value x = IntegerValue.of(Type.T_CHAR, src.charAt(i));
			dst.store(i, x); //into.incrOffset();
		}
	}
	
	/// Move values from one area to another.
	/// @param src the start of the source area
	/// @param dst the start of the destination area
	/// @param lng the length of the areas
	public static void move(ObjectAddress src, ObjectAddress dst, int lng) {
		for(int i=0;i<lng;i++) {
			Value x = src.load(i);
			dst.store(i, x); //into.incrOffset();
		}
	}

}
