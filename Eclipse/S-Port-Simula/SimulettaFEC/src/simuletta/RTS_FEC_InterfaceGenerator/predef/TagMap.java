package simuletta.RTS_FEC_InterfaceGenerator.predef;

import static simuletta.compiler.Global.modset;

import java.util.HashMap;
import java.util.Set;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.declaration.scope.InsertedModule;
import simuletta.compiler.declaration.scope.Record;
import simuletta.utilities.Util;

/**
INSERT RT,KNWN,CENT,CINT,ARR,FIL,EDIT,FORM,LIBR,SMST,SML,MNTR"));
DEFINE
       -----------------------------------------------------------------
       ---                                                           ---
       ---                                                           ---
       ---                 P O R T A B L E     S I M U L A           ---
       ---                  R U N T I M E     S Y S T E M            ---
       ---                                                           ---
       ---               I n t e r f a c e    W i t h    F E C       ---
       ---                                                           ---
       -----------------------------------------------------------------

*/
public class TagMap {
	public static HashMap<String,Integer> tagMap = new HashMap<String,Integer>();
	
	public static void define() {
	    // ---    T h e   T y p e   T E X T    ---
		tagMap.put("T_CONSTANT1",find("TXTCNS"));	tagMap.put("T_CONSTANT2",find("TXTCNS:body"));
//	--  tagMap.put("T_START1",find("START"));       tagMap.put("T_START2",find("START:body"));      
	    tagMap.put("T_SUB1",find("SUBATR"));        tagMap.put("T_SUB2",find("SUBATR:body"));
	    tagMap.put("T_SUBV1",find("SUBTMP"));       tagMap.put("T_SUBV2",find("SUBTMP:body"));
	    tagMap.put("T_MAIN1",find("MAIN"));         tagMap.put("T_MAIN2",find("MAIN:body"));
	    tagMap.put("T_STRIP1",find("STRIP"));       tagMap.put("T_STRIP2",find("STRIP:body"));
//	--  tagMap.put("T_LENGTH1",find("LENGTH"));     tagMap.put("T_LENGTH2",find("LENGTH:body"));
//	--  tagMap.put("T_POS1",find("POS"));           tagMap.put("T_POS2",find("POS:body"));
//	--  tagMap.put("T_MORE1",find("MORE"));         tagMap.put("T_MORE2",find("MORE:body"));
	    tagMap.put("T_SETPOS1",find("SETPOS"));     tagMap.put("T_SETPOS2",find("SETPOS:body"));
	    tagMap.put("T_SETPOSV1",find("SETPOT"));    tagMap.put("T_SETPOSV2",find("SETPOT:body"));

	    tagMap.put("T_GETCHAR1",find("gtChaA"));    tagMap.put("T_GETCHAR2",find("gtChaA:body"));
	    tagMap.put("T_GETCHARV1",find("gtChaT"));   tagMap.put("T_GETCHARV2",find("gtChaT:body"));
	    tagMap.put("T_GETINT1",find("gtIntA"));     tagMap.put("T_GETINT2",find("gtIntA:body"));
	    tagMap.put("T_GETINTV1",find("gtIntT"));    tagMap.put("T_GETINTV2",find("gtIntT:body"));
	    tagMap.put("T_GETREAL1",find("gtReaA"));    tagMap.put("T_GETREAL2",find("gtReaA:body"));
	    tagMap.put("T_GETREALV1",find("gtReaT"));   tagMap.put("T_GETREALV2",find("gtReaT:body"));
	    tagMap.put("T_GETFRAC1",find("gtFraA"));    tagMap.put("T_GETFRAC2",find("gtFraA:body"));
	    tagMap.put("T_GETFRACV1",find("gtFraT"));   tagMap.put("T_GETFRACV2",find("gtFraT:body"));

	    tagMap.put("T_PUTCHAR1",find("ptChaA"));    tagMap.put("T_PUTCHAR2",find("ptChaA:body"));
	    tagMap.put("T_PUTCHARV1",find("ptChaT"));   tagMap.put("T_PUTCHARV2",find("ptChaT:body"));
	    tagMap.put("T_PUTINT1",find("ptIntA"));     tagMap.put("T_PUTINT2",find("ptIntA:body"));
	    tagMap.put("T_PUTINTV1",find("ptIntT"));    tagMap.put("T_PUTINTV2",find("ptIntT:body"));
	    tagMap.put("T_PUTREAL1",find("ptReaA"));    tagMap.put("T_PUTREAL2",find("ptReaA:body"));
	    tagMap.put("T_PUTREALV1",find("ptReaT"));   tagMap.put("T_PUTREALV2",find("ptReaT:body"));
	    tagMap.put("T_PUTLREAL1",find("ptLrlA"));   tagMap.put("T_PUTLREAL2",find("ptLrlA:body"));
	    tagMap.put("T_PUTLREALV1",find("ptLrlT"));  tagMap.put("T_PUTLREALV2",find("ptLrlT:body"));
	    tagMap.put("T_PUTFRAC1",find("ptFraA"));    tagMap.put("T_PUTFRAC2",find("ptFraA:body"));
	    tagMap.put("T_PUTFRACV1",find("ptFraT"));   tagMap.put("T_PUTFRACV2",find("ptFraT:body"));
	    tagMap.put("T_PUTFIX1",find("ptFixA"));     tagMap.put("T_PUTFIX2",find("ptFixA:body"));
	    tagMap.put("T_PUTFIXV1",find("ptFixT"));    tagMap.put("T_PUTFIXV2",find("ptFixT:body"));
	    tagMap.put("T_PUTLFIX1",find("ptLfxA"));    tagMap.put("T_PUTLFIX2",find("ptLfxA:body"));
	    tagMap.put("T_PUTLFIXV1",find("ptLfxT"));   tagMap.put("T_PUTLFIXV2",find("ptLfxT:body"));

	    // ---     F i l e   C l a s s e s     ---

	    tagMap.put("F_FI_DESC",find("filRec"));
	    tagMap.put("F_IMF_DESC",find("imfRec"));
	    tagMap.put("F_IF_DESC",find("iflRec"));
	    tagMap.put("F_OF_DESC",find("oflRec"));
	    tagMap.put("F_DF_DESC",find("dflRec"));
	    tagMap.put("F_NAME",find("FILENT.NAM"));
	    tagMap.put("F_IMAGE",find("FILENT.IMG"));
	    tagMap.put("F_ENDFILE",find("FILENT.EOF"));
	    tagMap.put("F_LOCATION",find("FILENT.LOC"));
	    tagMap.put("F_LOCKED",find("FILENT.LOCKED"));                   
	    tagMap.put("B_BSIZE",find("FILENT.BSIZE"));                   

	    tagMap.put("F_PF_DESC",find("pflRec"));
	    tagMap.put("F_SPACINGV",find("pflRec.SPC"));
	    tagMap.put("F_LINESPPV",find("pflRec.LPP"));
	    tagMap.put("F_LINEV",find("pflRec.LIN"));
	    tagMap.put("F_LINE",find("pflRec.LIN"));
	    tagMap.put("F_PAGE",find("pflRec.PAG"));                   



	    tagMap.put("F_FI_PROT",find("FILPTP"));
	    tagMap.put("F_IMF_PROT",find("IMFPTP"));                            
	    tagMap.put("F_IF_PROT",find("iflPtp"));
	    tagMap.put("F_OF_PROT",find("oflPtp"));
	    tagMap.put("F_DF_PROT",find("dflPtp"));
	    tagMap.put("F_PF_PROT",find("pflPtp"));

	    tagMap.put("F_BF_DESC",find("btfRec"));
	    tagMap.put("F_BF_PROT",find("BTFPTP"));   

	    tagMap.put("F_OB_DESC",find("obfRec"));
	    tagMap.put("F_OB_PROT",find("obfPtp"));

	    tagMap.put("F_IB_DESC",find("ibfRec"));
	    tagMap.put("F_IB_PROT",find("ibfPtp"));

	    tagMap.put("F_DBF_DESC",find("dbfRec"));
	    tagMap.put("F_DBF_PROT",find("dbfPtp"));   

	    // ---   F I L E    R o u t i n e s   ---

	    tagMap.put("B_OUTBYTE1",find("utbyte"));	tagMap.put("B_OUTBYTE2",find("utbyte:body"));
	    tagMap.put("B_OUT2BYTE1",find("ut2byt"));   tagMap.put("B_OUT2BYTE2",find("ut2byt:body"));
	    tagMap.put("B_OUTTEXT1",find("boutex"));    tagMap.put("B_OUTTEXT2",find("boutex:body"));

	    tagMap.put("B_INBYTE1",find("INBYTE"));     tagMap.put("B_INBYTE2",find("INBYTE:body"));
	    tagMap.put("B_IN2BYTE1",find("IN2BYT"));    tagMap.put("B_IN2BYTE2",find("IN2BYT:body"));
	    tagMap.put("B_INTEXT1",find("BINTEX"));     tagMap.put("B_INTEXT2",find("BINTEX:body"));

	    tagMap.put("B_DINBYTE1",find("DINBYT"));    tagMap.put("B_DINBYTE2",find("DINBYT:body"));
	    tagMap.put("B_DIN2BYTE1",find("DIN2BY"));   tagMap.put("B_DIN2BYTE2",find("DIN2BY:body"));
	    tagMap.put("B_DOUTBYTE1",find("DUTBYT"));   tagMap.put("B_DOUTBYTE2",find("DUTBYT:body"));
	    tagMap.put("B_DOUT2BYTE1",find("DUT2BY"));  tagMap.put("B_DOUT2BYTE2",find("DUT2BY:body"));
	    tagMap.put("B_DBENDF1",find("DBENDF"));     tagMap.put("B_DBENDF2",find("DBENDF:body"));   

	    tagMap.put("F_FILNAM1",find("FILNAM"));     tagMap.put("F_FILNAM2",find("FILNAM:body"));  
	    tagMap.put("F_FILSET1",find("FILSET"));     tagMap.put("F_FILSET2",find("FILSET:body"));  
	    tagMap.put("F_ISOPEN1",find("ISOPEN"));     tagMap.put("F_ISOPEN2",find("ISOPEN:body")); 
	    tagMap.put("F_SETACC1",find("SETACC"));     tagMap.put("F_SETACC2",find("SETACC:body"));
	    tagMap.put("F_CHKPNT1",find("CHKPNT"));     tagMap.put("F_CHKPNT2",find("CHKPNT:body"));
	    tagMap.put("F_LOCK1",find("LOCK"));         tagMap.put("F_LOCK2",find("LOCK:body"));  
	    tagMap.put("F_UNLOCK1",find("UNLOCK"));     tagMap.put("F_UNLOCK2",find("UNLOCK:body"));
	    tagMap.put("F_LASTLOC1",find("LASTLC"));    tagMap.put("F_LASTLOC2",find("LASTLC:body")); 
	    tagMap.put("F_MAXLOC1",find("MAXLOC"));     tagMap.put("F_MAXLOC2",find("MAXLOC:body")); 
	    tagMap.put("F_OPEN1",find("OPEN"));         tagMap.put("F_OPEN2",find("OPEN:body"));
	    tagMap.put("B_OPEN1",find("BOPN"));         tagMap.put("B_OPEN2",find("BOPN:body"));
	    tagMap.put("F_CLOSE1",find("CLOSE"));       tagMap.put("F_CLOSE2",find("CLOSE:body"));
	    tagMap.put("F_INIMAGE1",find("INIMAG"));    tagMap.put("F_INIMAGE2",find("INIMAG:body"));
	    tagMap.put("F_INRECORD1",find("INREC"));    tagMap.put("F_INRECORD2",find("INREC:body"));
	    tagMap.put("F_OUTIMAGE1",find("OUTIM"));    tagMap.put("F_OUTIMAGE2",find("OUTIM:body"));
	    tagMap.put("F_OUTRECORD1",find("OUTREC"));  tagMap.put("F_OUTRECORD2",find("OUTREC:body"));
	    tagMap.put("F_BREAKOUT1",find("BRKOUT"));   tagMap.put("F_BREAKOUT2",find("BRKOUT:body"));

	    tagMap.put("F_LOCATE1",find("LOCATE"));     tagMap.put("F_LOCATE2",find("LOCATE:body"));
	    tagMap.put("F_DELIMAGE1",find("DELIM"));    tagMap.put("F_DELIMAGE2",find("DELIM:body"));
	    tagMap.put("F_LASTITEM1",find("LAST"));     tagMap.put("F_LASTITEM2",find("LAST:body"));

	    tagMap.put("F_LINESPP1",find("LPP"));       tagMap.put("F_LINESPP2",find("LPP:body"));
	    tagMap.put("F_SPACING1",find("SPACIN"));    tagMap.put("F_SPACING2",find("SPACIN:body"));
	    tagMap.put("F_EJECT1",find("EJECT"));       tagMap.put("F_EJECT2",find("EJECT:body"));

	    tagMap.put("F_INCHAR1",find("INCHAR"));     tagMap.put("F_INCHAR2",find("INCHAR:body"));
	    tagMap.put("F_ININT1",find("ININT"));       tagMap.put("F_ININT2",find("ININT:body"));
	    tagMap.put("F_INREAL1",find("INREAL"));     tagMap.put("F_INREAL2",find("INREAL:body"));
	    tagMap.put("F_INFRAC1",find("INFRAC"));     tagMap.put("F_INFRAC2",find("INFRAC:body"));
	    tagMap.put("F_INTEXT1",find("INTEXT"));     tagMap.put("F_INTEXT2",find("INTEXT:body"));


	    tagMap.put("F_OUTCHAR1",find("OUTCHA"));    tagMap.put("F_OUTCHAR2",find("OUTCHA:body"));
	    tagMap.put("F_OUTINT1",find("OUTINT"));     tagMap.put("F_OUTINT2",find("OUTINT:body"));
	    tagMap.put("F_OUTFIX1",find("OUTFIX"));     tagMap.put("F_OUTFIX2",find("OUTFIX:body"));
	    tagMap.put("F_OUTLFIX1",find("OUTLFX"));    tagMap.put("F_OUTLFIX2",find("OUTLFX:body"));
	    tagMap.put("F_OUTREAL1",find("OUTREA"));    tagMap.put("F_OUTREAL2",find("OUTREA:body"));
	    tagMap.put("F_OUTLREAL1",find("OUTLRL"));   tagMap.put("F_OUTLREAL2",find("OUTLRL:body"));
	    tagMap.put("F_OUTFRAC1",find("OUTFRC"));    tagMap.put("F_OUTFRAC2",find("OUTFRC:body"));
	    tagMap.put("F_OUTTEXT1",find("OUTTXT"));    tagMap.put("F_OUTTEXT2",find("OUTTXT:body"));


	    // ------  S  I  M  S  E  T  ------

	    tagMap.put("S_SMST_DESC",find("smsRec"));
	    tagMap.put("S_SMST_PROT",find("smsPtp"));

	    tagMap.put("S_LAGE_DESC",find("linkag"));
	    tagMap.put("S_LAGE_PROT",find("lkaPtp"));

	    tagMap.put("S_LAGE_SUC1",find("sucSS"));       tagMap.put("S_LAGE_SUC2",find("sucSS:body"));
	    tagMap.put("S_LAGE_PRED1",find("predSS"));     tagMap.put("S_LAGE_PRED2",find("predSS:body"));
	    tagMap.put("S_LAGE_PREV1",find("prevSS"));     tagMap.put("S_LAGE_PREV2",find("prevSS:body"));

	    tagMap.put("S_HEAD_DESC",find("hedRec"));
	    tagMap.put("S_HEAD_PROT",find("hedPtp"));

	    tagMap.put("S_HEAD_FIRS1",find("sucSS"));      tagMap.put("S_HEAD_FIRS2",find("sucSS:body"));
	    tagMap.put("S_HEAD_LAST1",find("predSS"));     tagMap.put("S_HEAD_LAST2",find("predSS:body"));
	    tagMap.put("S_HEAD_EMPT1",find("emptSS"));     tagMap.put("S_HEAD_EMPT2",find("emptSS:body"));
	    tagMap.put("S_HEAD_CARD1",find("cardSS"));     tagMap.put("S_HEAD_CARD2",find("cardSS:body"));
	    tagMap.put("S_HEAD_CLEA1",find("cleaSS"));     tagMap.put("S_HEAD_CLEA2",find("cleaSS:body"));


	    tagMap.put("S_LINK_DESC",find("lnkRec"));
	    tagMap.put("S_LINK_PROT",find("lnkPtp"));

	    tagMap.put("S_LINK_OUT1",find("outSS"));       tagMap.put("S_LINK_OUT2",find("outSS:body"));
	    tagMap.put("S_LINK_FOLW1",find("FOLLOW"));     tagMap.put("S_LINK_FOLW2",find("FOLLOW:body"));
	    tagMap.put("S_LINK_PREC1",find("precSS"));     tagMap.put("S_LINK_PREC2",find("precSS:body"));
	    tagMap.put("S_LINK_INTO1",find("intoSS"));     tagMap.put("S_LINK_INTO2",find("intoSS:body"));

	    // ------  S  I  M  U  L  A  T  I  O  N  ------

	    tagMap.put("S_RNK_DESC",find("rankin"));
	    tagMap.put("S_RNK_PROT",find("rnkPtp"));
	    tagMap.put("S_RNK",find("rankin.rnk"));

	    tagMap.put("S_SMLN_DESC",find("simltn"));
	    tagMap.put("S_SMLN_PROT",find("SMLPTP"));
	    tagMap.put("S_CURRENT",find("simltn.cur"));
	    tagMap.put("S_MAIN",find("simltn.main"));

	    tagMap.put("S_PROC_DESC",find("proces"));
	    tagMap.put("S_PROC_PROT",find("prcPtp"));
	    tagMap.put("S_PROC_TAIL",find("PCSINR")); // -- Note!!!

	    tagMap.put("S_PROC_IDLE1",find("Idle"));       tagMap.put("S_PROC_IDLE2",find("Idle:body"));
	    tagMap.put("S_PROC_TERM1",find("term_d"));     tagMap.put("S_PROC_TERM2",find("Term_d:body"));
	    tagMap.put("S_PROC_EVTM1",find("Evtime"));     tagMap.put("S_PROC_EVTM2",find("Evtime:body"));
	    tagMap.put("S_PROC_NXEV1",find("Nextev"));     tagMap.put("S_PROC_NXEV2",find("Nextev:body"));
	    tagMap.put("S_HOLD1",find("Hold"));            tagMap.put("S_HOLD2",find("Hold:body"));
	    tagMap.put("S_PASSIVATE1",find("PASSIV"));     tagMap.put("S_PASSIVATE2",find("PASSIV:body"));
	    tagMap.put("S_WAIT1",find("Wait"));            tagMap.put("S_WAIT2",find("Wait:body"));
	    tagMap.put("S_CANCEL1",find("CANCEL"));        tagMap.put("S_CANCEL2",find("CANCEL:body"));
	    tagMap.put("S_ACT11",find("Activ1"));          tagMap.put("S_ACT12",find("Activ1:body"));
	    tagMap.put("S_ACT21",find("Activ2"));          tagMap.put("S_ACT22",find("Activ2:body"));
	    tagMap.put("S_ACT31",find("Activ3"));          tagMap.put("S_ACT32",find("Activ3:body"));
	    tagMap.put("S_ACCUM1",find("Accum"));          tagMap.put("S_ACCUM2",find("Accum:body"));
	    
	    
	    // ------  S t a n d a r d    P r o c e d u r e s  ------

	    //---------------  BASIC OPERATIONS  ---------------

	    tagMap.put("P_MOD1",find("MOD"));          tagMap.put("P_MOD2",find("MOD:body"));
//	--  tagMap.put("P_REM1",find("REMAIND"));      tagMap.put("P_REM2",find("REMAIND:body")); 
//	--  tagMap.put("P_IABS1",find("IABS"));         tagMap.put("P_IABS2",find("IABS:body"));
//	--  tagMap.put("P_RABS1",find("RABS"));         tagMap.put("P_RABS2",find("RABS:body"));
//	--  tagMap.put("P_DABS1",find("DABS"));         tagMap.put("P_DABS2",find("DABS:body"));
//	--  tagMap.put("P_RSIGN1",find("RSIGN"));        tagMap.put("P_RSIGN2",find("RSIGN:body"));
//	--  tagMap.put("P_DSIGN1",find("DSIGN"));        tagMap.put("P_DSIGN2",find("DSIGN:body"));
	    tagMap.put("P_RENTIER1",find("RENTI"));        tagMap.put("P_RENTIER2",find("RENTI:body"));
	    tagMap.put("P_DENTIER1",find("DENTI"));        tagMap.put("P_DENTIER2",find("DENTI:body"));
	    tagMap.put("P_RADDEPS1",find("RADDEP"));       tagMap.put("P_RADDEPS2",find("RADDEP:body"));  
	    tagMap.put("P_DADDEPS1",find("DADDEP"));       tagMap.put("P_DADDEPS2",find("DADDEP:body")); 
	    tagMap.put("P_RSUBEPS1",find("RSUBEP"));       tagMap.put("P_RSUBEPS2",find("RSUBEP:body")); 
	    tagMap.put("P_DSUBEPS1",find("DSUBEP"));       tagMap.put("P_DSUBEPS2",find("DSUBEP:body")); 

	    // ---------------  TEXT UTILITIES  ---------------

	    tagMap.put("T_BLANKS1",find("BLANKS"));         tagMap.put("T_BLANKS2",find("BLANKS:body"));
	    tagMap.put("T_COPY1",find("COPY"));           tagMap.put("T_COPY2",find("COPY:body"));
	    tagMap.put("P_LETTER1",find("LETTER"));         tagMap.put("P_LETTER2",find("LETTER:body"));
	    tagMap.put("P_DIGIT1",find("DIGIT"));          tagMap.put("P_DIGIT2",find("DIGIT:body"));
	    tagMap.put("T_LOWTEN1",find("LOWTEN"));         tagMap.put("T_LOWTEN2",find("LOWTEN:body"));
	    tagMap.put("T_DCMARK1",find("DCMARK"));         tagMap.put("T_DCMARK2",find("DCMARK:body")); 
	    tagMap.put("T_UPTX1",find("UPTX"));           tagMap.put("T_UPTX2",find("UPTX:body"));  
	    tagMap.put("T_LWTX1",find("LWTX"));           tagMap.put("T_LWTX2",find("LWTX:body")); 

	    // ---------------  SCHEDULING  ---------------

	    tagMap.put("Q_DETACH1",find("DETACH"));         tagMap.put("Q_DETACH2",find("DETACH:body"));
	    tagMap.put("Q_RESUME1",find("RESUME"));         tagMap.put("Q_RESUME2",find("RESUME:body"));
	    tagMap.put("Q_CALL1",find("ATTACH"));         tagMap.put("Q_CALL2",find("ATTACH:body"));

	    // ---------------  MATHEMATICAL FUNCTIONS  ---------------

	    tagMap.put("P_RSQRT1",find("RSQRT"));          tagMap.put("P_RSQRT2",find("RSQRT:body"));
	    tagMap.put("P_DSQRT1",find("DSQRT"));          tagMap.put("P_DSQRT2",find("DSQRT:body"));
	    tagMap.put("P_RSIN1",find("RSIN"));           tagMap.put("P_RSIN2",find("RSIN:body"));
	    tagMap.put("P_DSIN1",find("DSIN"));           tagMap.put("P_DSIN2",find("DSIN:body"));
	    tagMap.put("P_RCOS1",find("RCOS"));           tagMap.put("P_RCOS2",find("RCOS:body"));
	    tagMap.put("P_DCOS1",find("DCOS"));           tagMap.put("P_DCOS2",find("DCOS:body"));
	    tagMap.put("P_RTAN1",find("RTAN"));           tagMap.put("P_RTAN2",find("RTAN:body"));
	    tagMap.put("P_DTAN1",find("DTAN"));           tagMap.put("P_DTAN2",find("DTAN:body"));
	    tagMap.put("P_RCOTAN1",find("RCOTAN"));         tagMap.put("P_RCOTAN2",find("RCOTAN:body"));  
	    tagMap.put("P_DCOTAN1",find("DCOTAN"));         tagMap.put("P_DCOTAN2",find("DCOTAN:body")); 
	    tagMap.put("P_RARCSIN1",find("ARSINR"));        tagMap.put("P_RARCSIN2",find("ARSINR:body"));
	    tagMap.put("P_DARCSIN1",find("ARSIND"));        tagMap.put("P_DARCSIN2",find("ARSIND:body"));
	    tagMap.put("P_RARCCOS1",find("ARCOSR"));        tagMap.put("P_RARCCOS2",find("ARCOSR:body"));
	    tagMap.put("P_DARCCOS1",find("ARCOSD"));        tagMap.put("P_DARCCOS2",find("ARCOSD:body"));
	    tagMap.put("P_RARCTAN1",find("ARTANR"));        tagMap.put("P_RARCTAN2",find("ARTANR:body"));
	    tagMap.put("P_DARCTAN1",find("ARTAND"));        tagMap.put("P_DARCTAN2",find("ARTAND:body"));
	    tagMap.put("P_RATAN21",find("RATAN2"));         tagMap.put("P_RATAN22",find("RATAN2:body"));   
	    tagMap.put("P_DATAN21",find("DATAN2"));         tagMap.put("P_DATAN22",find("DATAN2:body"));  

	    tagMap.put("P_RSINH1",find("RSINH"));          tagMap.put("P_RSINH2",find("RSINH:body")); 
	    tagMap.put("P_DSINH1",find("DSINH"));          tagMap.put("P_DSINH2",find("DSINH:body"));    
	    tagMap.put("P_RCOSH1",find("RCOSH"));          tagMap.put("P_RCOSH2",find("RCOSH:body"));  
	    tagMap.put("P_DCOSH1",find("DCOSH"));          tagMap.put("P_DCOSH2",find("DCOSH:body"));
	    tagMap.put("P_RTANH1",find("RTANH"));          tagMap.put("P_RTANH2",find("RTANH:body")); 
	    tagMap.put("P_DTANH1",find("DTANH"));          tagMap.put("P_DTANH2",find("DTANH:body"));
	    tagMap.put("P_RLN1",find("RLN"));            tagMap.put("P_RLN2",find("RLN:body"));
	    tagMap.put("P_DLN1",find("DLN"));            tagMap.put("P_DLN2",find("DLN:body"));
	    tagMap.put("P_RLOG1",find("RLOG"));           tagMap.put("P_RLOG2",find("RLOG:body"));
	    tagMap.put("P_DLOG1",find("DLOG"));           tagMap.put("P_DLOG2",find("DLOG:body"));
	    tagMap.put("P_REXP1",find("REXP"));           tagMap.put("P_REXP2",find("REXP:body"));
	    tagMap.put("P_DEXP1",find("DEXP"));           tagMap.put("P_DEXP2",find("DEXP:body"));

	    // ---------------  EXTREMUM FUNCTIONS  ---------------

//	--  tagMap.put("P_RMIN1",find("RMIN"));           tagMap.put("P_RMIN2",find("RMIN:body")); 
//	--  tagMap.put("P_DMIN1",find("DMIN"));           tagMap.put("P_DMIN2",find("DMIN:body")); 
	    tagMap.put("P_TMIN1",find("TXTMIN"));         tagMap.put("P_TMIN2",find("TXTMIN:body")); 
//	--  tagMap.put("P_RMAX1",find("RMAX"));           tagMap.put("P_RMAX2",find("RMAX:body")); 
//	--  tagMap.put("P_DMAX1",find("DMAX"));           tagMap.put("P_DMAX2",find("DMAX:body")); 
	    tagMap.put("P_TMAX1",find("TXTMAX"));         tagMap.put("P_TMAX2",find("TXTMAX:body")); 

	    // ---------------  ENVIRONMENTAL ENQUIRIES  ---------------

//	--  tagMap.put("P_SRCLIN1",find("SRCLIN"));         tagMap.put("P_SRCLIN2",find("SRCLIN:body")); 
	    tagMap.put("G_SYSINVAR",find("bioIns.SYSIN"));   // --- TEMP!!!!
	    tagMap.put("G_SYSOUTVAR",find("bioIns.SYSOUT"));  // --- TEMP!!!!
	    tagMap.put("G_MAXLREAL",find("MAXLRL"));
	    tagMap.put("G_MINLREAL",find("MINLRL"));
	    tagMap.put("G_MAXREAL",find("MAXREA"));
	    tagMap.put("G_MINREAL",find("MINREA"));
	    tagMap.put("G_MAXRANK",find("MAXRNK"));
	    tagMap.put("G_MAXINT",find("MAXINT"));
	    tagMap.put("G_MININT",find("MININT"));
	    tagMap.put("G_SIMID",find("bioIns.SIMID"));

	    // ---------------  TERMINATION CONTROL  ---------------

	    tagMap.put("P_TRMP1",find("TRMP"));           tagMap.put("P_TRMP2",find("TRMP:body")); 
	    tagMap.put("P_ERRX1",find("ERRX"));           tagMap.put("P_ERRX2",find("ERRX:body"));

	    // ---------------  ARRAY QUANTITIES  ---------------

	    tagMap.put("P_LOWBND1",find("LOWBND"));         tagMap.put("P_LOWBND2",find("LOWBND:body"));  
	    tagMap.put("P_UPPBND1",find("UPPBND"));         tagMap.put("P_UPPBND2",find("UPPBND:body")); 
	    tagMap.put("P_ARRDIM1",find("ARRDIM"));         tagMap.put("P_ARRDIM2",find("ARRDIM:body")); 

	    // ---------------  RANDOM DRAWING  ---------------

	    tagMap.put("D_DRAW1",find("DRAW"));      tagMap.put("D_DRAW2",find("DRAW:body"));
	    tagMap.put("D_RANDINT1",find("RANDI"));     tagMap.put("D_RANDINT2",find("RANDI:body"));
	    tagMap.put("D_UNIFORM1",find("UNIFRM"));    tagMap.put("D_UNIFORM2",find("UNIFRM:body"));
	    tagMap.put("D_NORMAL1",find("NORMAL"));    tagMap.put("D_NORMAL2",find("NORMAL:body"));
	    tagMap.put("D_NEGEXP1",find("NEGEXP"));    tagMap.put("D_NEGEXP2",find("NEGEXP:body"));
	    tagMap.put("D_POISSON1",find("POISSN"));    tagMap.put("D_POISSON2",find("POISSN:body"));
	    tagMap.put("D_ERLANG1",find("ERLANG"));    tagMap.put("D_ERLANG2",find("ERLANG:body"));
	    tagMap.put("D_DISCRETE1",find("discrR"));    tagMap.put("D_DISCRETE2",find("discrR:body"));
	    tagMap.put("D_DDISCRETE1",find("discrD"));    tagMap.put("D_DDISCRETE2",find("discrD:body"));
	    tagMap.put("D_LINEAR1",find("lineaR"));    tagMap.put("D_LINEAR2",find("lineaR:body"));
	    tagMap.put("D_DLINEAR1",find("lineaD"));    tagMap.put("D_DLINEAR2",find("lineaD:body"));
	    tagMap.put("D_HISTD1",find("HISTD"));    tagMap.put("D_HISTD2",find("HISTD:body"));
	    tagMap.put("D_DHISTD1",find("DHISTD"));    tagMap.put("D_DHISTD2",find("DHISTD:body"));

	    // ---------------  CALENDAR AND TIMING  ---------------

	    tagMap.put("U_DANDT1",find("timdat"));    tagMap.put("U_DANDT2",find("timdat:body"));
	    tagMap.put("U_TIMEUSED1",find("Tused"));     tagMap.put("U_TIMEUSED2",find("Tused:body"));
	    tagMap.put("U_CLOCKTIME1",find("Tclock"));    tagMap.put("U_CLOCKTIME2",find("Tclock:body")); 

	    // ---------------  MISCELLANEOUS  ---------------

	    tagMap.put("D_HISTO1",find("HISTO"));     tagMap.put("D_HISTO2",find("HISTO:body"));
	    tagMap.put("E_HASH1",find("HASH"));      tagMap.put("E_HASH2",find("HASH:body"));
	    tagMap.put("E_GETIINF1",find("getII"));     tagMap.put("E_GETIINF2",find("getII:body"));
	    tagMap.put("E_GETTINF1",find("getTI"));     tagMap.put("E_GETTINF2",find("getTI:body"));
	    tagMap.put("E_GIVEIINF1",find("giveII"));    tagMap.put("E_GIVEIINF2",find("giveII:body"));
	    tagMap.put("E_GIVETINF1",find("giveTI"));    tagMap.put("E_GIVETINF2",find("giveTI:body"));
	    tagMap.put("R_RTS_UTIL1",find("RTutil"));    tagMap.put("R_RTS_UTIL2",find("RTutil:body"));
	    tagMap.put("R_ERR_LAB1",find("errLab"));    tagMap.put("R_ERR_LAB2",find("errLab:body")); 

//	END
		
	}
	

    private static int find(String identifier) {
		String attribute=null;
    	int p=identifier.indexOf('.');
    	if(p>0) {
    		//IO.println("TagMap.find: identifier\""+identifier+"\"");
    		attribute=identifier.substring(p+1);
    		identifier=identifier.substring(0, p);
    		//IO.println("TagMap.find: identifier\""+identifier+"\"");
    		//IO.println("TagMap.find: attribute\""+attribute+"\"");
    		//Util.IERR();
    	}
    		
    	for(InsertedModule m:modset) {
    		//IO.println("TagMap.findGlobalMeaning: SEARCHING"+identifier+"in"+m.identifier);
    		for(Declaration d:m.declarationList) {
    			//if(!(d instanceof Record)) IO.println("TagMap.findGlobalMeaning: CHECKING"+d);
    			if(d.identifier.equalsIgnoreCase(identifier)) {
    				//IO.println("TagMap.findGlobalMeaning:"+identifier+" FOUND in"+m.identifier+":"+d);
    				if(attribute==null) return(d.getTag().getCode());
    				return(find((Record) d,attribute));
    			}
    		}
    	}
    	Util.IERR("TagMap.find: Can't find "+identifier);
    	return(0);
    }

    private static int find(Record rec,String attrid) {
    	VariableDeclaration var=rec.findAttribute(attrid);
    	return(var.getTag().getCode());
    }
    
    public static void print() {
		IO.println("*************** TAGMAP ***************");
    	Set<String> keys=tagMap.keySet();
    	for(String key:keys) {
    		int xtag=tagMap.get(key);
    		IO.println(key+" = "+xtag);
    	}
    }
}
