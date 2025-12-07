package simuletta.RTS_FEC_InterfaceGenerator.predef.checker;

import java.io.IOException;
import simuletta.RTS_FEC_InterfaceGenerator.predef.AttrFile;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Category;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Key;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Kind;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Type;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

public class Checker {
    private static final boolean TESTING=false;//true;//false;
	public static InputFile inpt;
	public static String checkCode;
	public static String attrmod;
	
	
	public static boolean forced=false;
	public static boolean fetchQuantResult=false;

	public static void doVerify() throws IOException {
		inpt=new InputFile();
		inpt.openattributefile();
		readFileHead();
		int key=inpt.getKey("doVerify");
		if(TESTING) IO.println("Checker.doVerify: Quant-start byte="+key); // TESTING
		fetchquant("",key);

		Util.STOP();
	}

	// ***********************************************************************************************
	// *** readFileHead
	// ***********************************************************************************************
	/**
     *	FileHead ::=  "checkcode"   "modid"  "S-port yyy.x"  < mainKey > <layout index=!1!>1B
     *      			 -- yyy is release, x is subvers, the dot may be ':' (sysattr)
     */
	public static void readFileHead() throws IOException { //(q); ref(quantity) q;
//  !*** returns: moduleident in attrmodhi,lo and in simsymbol,
//                checkcode   in attrckhi,lo ;
        //!must start "S-port 108.1"
    	String symbol=inpt.readChars(12);
		if(TESTING) IO.println("Checker.readFileHead: \""+symbol+'"'); // TESTING
		if(!symbol.substring(0,12).equals("S-port 108.1")) Util.IERR("Wrong Layout");
		checkCode=inpt.gettext();
		if(TESTING) IO.println("Checker.readFileHead: checkCode="+checkCode); // TESTING
		attrmod=inpt.gettext();
		if(TESTING) IO.println("Checker.readFileHead: attrmod="+attrmod); // TESTING
		
		if(inpt.getKey("readFileHead")!=Key.mainKey) Util.IERR("Wrong Layout");
	}
	
	// ***********************************************************************************************
	// *** readQuantHead
	// ***********************************************************************************************
	/**
	 *      The format of a quantity head descriptor is
	 *
	 *      <kind*8 + categ>1B  -- NOTE: this byte must be less than lowKey
	 *      <type + (if prefix or qual then 128 else 0)>1B
	 *      if prefix/qual marked then "qualification identifier"
	 *      <clf + (if procedure parameter then 128 else 0)>1B
	 *  if NOT standard procedure parameter then
	 *    ? if sysattrfile then <RTStagbase of quantity + 1>I
	 *    ? else                <exttag of quantity + 1>I
	 *  endif
	 */
	public void readQuantHead(String indent,AttrFile oupt) throws IOException {
		//*** basic quantity descriptor, size computed above ***;
		// - assume that categ <8 and kind < 16  ALWAYS ***;
//		if(TESTING) IO.println("writeQuant: "+indent+identifier+", exttag="+xtag+", type="+type+", kind="+Kind.edKind(kind)+", categ="+Category.edCateg(categ)+", clf="+clf);
//		oupt.storebyte((char)( (kind*8) + categ ));
//		if(TESTING) IO.println("writeQuant: PACKED="+((kind*8) + categ)+", kind="+kind+", categ="+categ);
//
//		// - assume that type < 128 ALWAYS ***;
//		String prefqual=this.type.prefqual;
//		if(prefqual != null) {
//			oupt.storebyte((char)(type.code+128));
//			oupt.puttext(this.type.prefqual);
//		} else oupt.storebyte((char)type.code);
//
//		// - assume that clf < 128 ALWAYS ***;
//		oupt.storebyte((char) clf);
//		oupt.putNumber(xtag+1);
//		oupt.puttext(identifier);
	}

	
	//%title ********   procedure  FETCHQUANT   *******
	//
	//%      Read one quantity-description from attribute file.
	//%      The result is true if the quantity-identifier did occur in source
	//%      or if the force-mark is read.
	//%      The resulting descriptor is stored in the "x-variables".
	//%      Since the procedure is called both from normal- and from
	//%      recompattrfile, all direct reading must be done from CURF

		public static boolean fetchquant(String indent,int leadKey) throws IOException {
			int xkind;
			int xcateg;
			int xclf;
			xcateg=leadKey;
			if(TESTING) IO.println("Checker.fetchquant: xcateg="+xcateg); // TESTING
	    	// BLE SKREVET SÅNN: oupt.storebyte((char)( (kind*8) + categ ));
			if(xcateg >= 8) { // *** not simple;
	               xkind=xcateg/8 ;
	               xcateg=(xcateg & 7);
			} else xkind=0;
			if(TESTING) IO.println("Checker.fetchquant: xkind="+xkind+", xcateg="+xcateg); // TESTING
			if(TESTING) IO.println("Checker.fetchquant: xkind="+Kind.edKind(xkind)+", xcateg="+Category.edCateg(xcateg)); // TESTING
			int xtype=inpt.getByte("xtype");
			String prefix=null;
			if(xtype >= 128) { // *** prefix;
				xtype=xtype-128;
				int n=inpt.getByte("prefix'length");
	        	prefix=inpt.readChars(n);
			}
			Type type=new Type(xtype,prefix);
			if(TESTING) IO.println("Checker.fetchquant: type="+type); // TESTING
			xclf=inpt.getByte("xclf");		
			if(TESTING) IO.println("Checker.fetchquant: xclf="+xclf); // TESTING
			
			InputQuant quant=new InputQuant(type,xkind,xcateg,xclf);

			if(xclf>=128) { //*** procedure parameter;
	//%+D            forced:=
				fetchQuantResult=true;
				xclf=xclf-128;
//	               if sysattrfile then  !no proc param tags;
//	               else xftag:=nextNumber-1;
//	               goto SETDID;
//				Util.NOTIMPL("procedure parameter");
			} else {
				int xftag=inpt.getNumber("xtag")-1;
				if(TESTING) IO.println("Checker.fetchquant: xftag="+xftag); // TESTING
				quant.xidentstring=inpt.readString();
				if(xkind==Kind.K_class) quant.xident=quant.xidentstring;
			}
			
			if(TESTING) IO.println("Checker.fetchquant: xidentstring="+quant.xidentstring); // TESTING
	        	
			int key;
			CHCKMARK:while(true) {
//				key=inpt.getByte();
				key=inpt.getKey("CHCKMARK: "+quant.xidentstring);
				if(key>=Key.lowKey) {
					switch(key) {
					case Key.bufSwap:  inpt.swapIbuffer();              continue CHCKMARK; // *** buffer swap ;
					case Key.forcMark: forced=fetchQuantResult=true;    continue CHCKMARK; // *** force creation ***;
					case Key.protMark: quant.xprotect=inpt.getByte("xprotect");     continue CHCKMARK;
					case Key.dimMark:  quant.xdim=inpt.getByte("xdim");             continue CHCKMARK; // *** dim ***;
					case Key.overMark: quant.xlongindic=inpt.getByte("xlongindic"); continue CHCKMARK; // *** overloaded ***;
					case Key.xMark: {
						Util.NOTIMPL("");
						quant.xmodulid=inpt.gettext();  // moduleid: never notext when of interest;
						quant.xcheck=inpt.gettext();    // checkcode: never notext when of interest;
						quant.xlanguage=inpt.gettext(); // language: zero if no language (i.e. SIMULA for main);
						quant.xextident=inpt.gettext(); // extident: DEFCONST("?") if no extident;
						continue CHCKMARK;
					}

					case Key.yMark: {
						quant.xlanguage=inpt.gettext(); // language: zero if no language (i.e. SIMULA for main);
						quant.xextident=inpt.gettext(); // extident: DEFCONST("?") if no extident;
						continue CHCKMARK;
					}

//					case Key.nestMark: { // *** for/connect vars ;
//						Util.NOTIMPL("");
//						int xconnests=inpt.getByte(); continue CHCKMARK;
//					}
//
//					case Key.thisMark: { // *** inr, hasCode, thisused ;
//						Util.NOTIMPL("");
//						int nxtc=inpt.getByte();
//						if(nxtc >= 64) { xthisused=true; nxtc=nxtc-64; }
//						if(nxtc >= 32) { xhasCode :=true; nxtc=nxtc-32; }
//						//! if nxtc <> '!00!' then xinrtag:='!2!';
//						xinrtag=nxtc; //!NOTE - also isGlobal !!!;
//						continue CHCKMARK;
//					}
//					case Key.specMark: // !*** special ***;
//					if(xkind==Kind.K_rep) {
//						quant.sxlanguage=inpt.nextNumber(); quant.sxextident=inpt.nextNumber();
//						continue CHCKMARK;
//					}
//					quant.xspecial=inpt.getByte();
//					if(quant.xspecial > 128) {
//						quant.xspecial=quant.xspecial-128;
//						int xarrlo=inpt.getByte();
//					} else {
//						Util.NOTIMPL("");
//           GETARR:   gettext; DEFCONST;
//                     xarrhi:=hashhi; xarrlo:=hashlo end;
//                  goto CHCKMARK;
//                  }
//					case Key.hidMark: { // !*** hidden list;
//						Util.NOTIMPL("");
//	                  cvis:-xhidlist:-new idpack;
//	                  while key=hidmark do begin
//	                     cvis:-cvis.next:-new idpack;
//	                     gettext; DEFIDENT;
//	                     cvis.idhi:=hashhi; cvis.idlo:=hashlo;
//	                     cvis.line:=1; !*** must be non-zero;
//	                     getKey;
//	                  end hidmarks;
//	                  xhidlist:-xhidlist.next;
//	                  ! goto NOMORE;
//					}

					default: break CHCKMARK;
					} // end case;
				} // end key>=lowkey;
			} // end while(true);

//	     NOMORE: !*** next key has been read ***;
			
			//IO.println("End fetchquant: "+indent+quant+"   KEY="+Key.edKey(key));
			InputFile.INPUT_TRACE("End fetchquant: "+quant+"   KEY="+Key.edKey(key));

				if(key==Key.begList) {
					key=inpt.getKey("BEGIN LIST: "+quant.xidentstring);
					if(TESTING) IO.println("Checker.fetchquant.begList: "+quant+", key="+key);
//			        LOOP:
					while(key < Key.lowKey) {
			        	fetchquant(indent+"   ",key);
						if(TESTING) IO.println("Checker.fetchquant: CONTINUE LIST: "+quant+", key="+key);
						
						//if(key==Key.endlist) break LOOP;
						
						key=inpt.getKey("CONTINUE LIST"+quant.xidentstring);
//						if(TESTING) IO.println("Checker.fetchquant: CONTINUE LIST: "+quant+", key="+key);
//			        	Util.STOP();
			        }
					if(TESTING) IO.println("Checker.fetchquant: END LIST: "+quant+", key="+key);
					if(TESTING) IO.println("Checker.fetchquant: END LIST: "+quant.xidentstring); // TESTING
					if(key!=Key.endlist) Util.IERR("Wrong Layout: got "+Key.edKey(key)+" while expeting 'endList'");
				}

			return(fetchQuantResult);
		} // end fetchquant;


}
