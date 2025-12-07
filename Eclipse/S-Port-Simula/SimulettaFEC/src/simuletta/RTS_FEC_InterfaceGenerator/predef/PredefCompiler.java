/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.RTS_FEC_InterfaceGenerator.predef;

import static simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import simuletta.RTS_FEC_InterfaceGenerator.RTS_FEC_InterfaceGenerator;
import simuletta.RTS_FEC_InterfaceGenerator.predef.declaration.ClassQuant;
import simuletta.RTS_FEC_InterfaceGenerator.predef.declaration.Quantity;
import simuletta.RTS_FEC_InterfaceGenerator.predef.parser.Parser;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Key;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.KeyWord;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

/**
 * 
 *title ***  LOGICAL Format of attribute files  ***
 
 *       <...>1B means one byte with the indicated content
 *       <...>I  means doublebyte representing a short integer
 *       "..."   means a string (see procedure puttext for format)
 *       'x'     means one byte with the specified character (ISO!!!)
 *        ?      this part occurs only under the given condition

 *      "checkcode"   "modid"  "S-port yyy.x"  <layout index=!1!>1B
 *       -- yyy is release, x is subvers, the dot may be ':' (sysattr)
 *  ?   <quantity descriptor>*  -- one for each external in module head
 *      mainKey  <quantity descriptor>
 *      mainKey  <number of external tags>I
 *      mainKey  "timestamp"
 *
 *      The format of a quantity descriptor is
 *
 *      <kind*8 + categ>1B  -- NOTE: this byte must be less than lowKey
 *      <type + (if prefix or qual then 128 else 0)>1B
 *      if prefix/qual marked then "qualification identifier"
 *      <clf + (if procedure parameter then 128 else 0)>1B
 *  if NOT standard procedure parameter then
 *    ? if sysattrfile then <RTStagbase of quantity + 1>I
 *    ? else                <exttag of quantity + 1>I
 *  endif
 *  if NOT procedure parameter then
 *    ? "identifier of this quant" --- may be empty, i.e. <0>1B
 *   --- marked part: ---
 *    ?     forcMark -- forced creation of this quantity
 *    ?     protMark -- this quantity is protected
 *    ?     dimMark <dim>1B  -- cannot occur together w/xMark or yMark
 *    ?     overMark <overload indicator>1B
 *    ?     specMark <special>1B  ( <value>1B ! "value" )
 *    ?            constant attribute or array lb. on attr.file:
 *    ?               -- if special > 128:
 *    ?               arithmetic +
 *    ?               text<>notext:            <value string>
 *    ?               -- otherwise:
 *    ?               character                <1 byte rank>
 *    ?               TRUE/FALSE/NONE/NOTEXT:  <1 byte index>
 *    ?        NOTE: if kind=K_rep then special layout for infix array
 *    ?     xMark "modid" "checkcode" "language" "external ident."
 *    ?     yMark                     "language" "external ident."
 *    ?      -- only one of xMark or yMark can occur  --
 *    ?     <connest>1B
 *    ?     thisMark <0,thisused,hasCode,0,0,isGlobal,inrtag,0>1B
 *    ?               -- bit packed
 *    ?   ( hidMark  "identifier" )*
 *   --- end of marked part, terminated by 
 *         1. a key not mentioned above (must be begList,endList,mainKey)
 *         2. a character < lowKey (start of next quant)
 *  end NOT procedure parameter
 *
 *  if not special and then
 *     ( descr is brecord and (virt-list or fpar-list is non-empty) )
 *     or else this is descr of virt proc w/binding
 *  then --- list of the relevant quantities follows:
 *       --- (this list is empty for virt proc w/binding and no param)
 *    ?    begList
 *    ?    <quantity descriptor>* -- for each new virtual in virt-list
 *    ?                           -- NOTE: the virtuals MUST be first
 *    ?    <quantity descriptor>* -- for each elt in fpar-list for which:
 *    ?                   --  descr is not extbrecord OR descr.status='S'
 *    ?    endList
 *  end output of local quantity descriptors
 *
 */
public class PredefCompiler {
	//      -----------------------------------------------------------------
	//      ---                                                           ---
	//      ---                 P O R T A B L E     S I M U L A           ---
	//      ---                                                           ---
	//      ---                 P R E D E F     C O M P I L E R           ---
	//      ---                                                           ---
	//      -----------------------------------------------------------------
    private static final String sysSportid="S-port 108.1";

    public final String inputFileName;
	public final File outputFile;
	ClassQuant predefModule;
	
	public PredefCompiler(String inputFileName,File outputFile) {
		this.inputFileName=inputFileName;
		this.outputFile=outputFile;
	}

	public void doCompile() {
		PredefGlobal.INIT();
		File file=new File(inputFileName);
		String name=file.getName();
		int i=name.indexOf('.');
		if(i!=0) name=name.substring(0,i);
		Parser.open(name,getReader());
//		currentModule.parse();
		TagMap.define();
		
    	Parser.expect(KeyWord.CLASS);
    	predefModule=ClassQuant.doParseClass(null);
    	predefModule.print("LEAD",2);
    	try { writePredefAttrFile(); } catch (IOException e) { e.printStackTrace(); }
    	Parser.expect(KeyWord.END);

		Parser.close();
		//currentModule.print("(END PARSING)",4);
		
//		try { Verifyer.doVerify(); } catch (IOException e) { e.printStackTrace(); }
//		try { Checker.doVerify(); } catch (IOException e) { e.printStackTrace(); }
		
		if(RTS_FEC_Interface_Option.INTERFACE_TRACE_LEVEL > 2) TagMap.print();

		if(RTS_FEC_Interface_Option.verbose || nError != 0) {
			StringBuilder s=new StringBuilder();
			s.append("End PredefCompiler");
			if(PredefGlobal.sourceLineNumber > 0) s.append(", lines: ").append(PredefGlobal.sourceLineNumber);
			if(nError != 0) { s.append(" , "); s.append(nError); }
			else s.append(" no");
			s.append(" errors.");
			IO.println(s.toString()); // symboltable.close;
		}
		if(nError > 0) IO.println("Program Terminated due to " + nError + " Errors");	            
	}

	private InputStreamReader getReader() {
		InputStreamReader reader = null;
		try { reader=new InputStreamReader(new FileInputStream(inputFileName));
		} catch (IOException e) { Util.ERROR("can't open " + inputFileName+", reason: "+e); }
		if (!inputFileName.toLowerCase().endsWith(".sml"))
			Util.WARNING("Simuletta source file should, by convention be extended by .sml: " + inputFileName);
		File inputFile = new File(inputFileName);
		// Create Output File Path
		String name = inputFile.getName();
		PredefGlobal.sourceFileName=name;
		PredefGlobal.sourceName = name.substring(0, name.length() - 4);
		PredefGlobal.sourceFileDir=inputFile.getParentFile();
		if(RTS_FEC_Interface_Option.TRACING) Util.println("Compiling: \""+inputFileName+"\"");
		return(reader);
	}
	
	public void writePredefAttrFile() throws IOException {
		AttrFile oupt=new AttrFile();
		oupt.open();
		
//        AOF:-new outbytefile(attrfilename);
//        if not AOF.open then openerror(attrfilename);
//	      AOF.outbyte(rank(layoutindex));
//         storebyte((char) layoutindex);		
		
//%+D         if sportid.length<>12 then internerr('!7!',sourceline);
//%+Z         sysattrfile:=(mainqnt.virtno <> 0);
//%+Z         if sysattrfile then begin
//%+Z            t:-copy(sportid); t.sub(11,1):=":"; end else
//        t:-sportid; attrbuffer.sub(3,12):=t; p:=14;
//%           storebyte(layoutindex);
//        puttext(checkcode); puttext(moduleident);
        
		if (RTS_FEC_Interface_Option.verbose)	Util.TRACE("***       Write External " + predefModule.identifier);
        oupt.storeText(sysSportid);
//        IO.println("writePredefAttrFile: AFTER:  \""+oupt.edBuffer());
        oupt.putText("_predefmodule"); // CheckCode
//        IO.println("writePredefAttrFile: AFTER:  \""+oupt.edBuffer());
        oupt.putText("_predefmodule");
//        IO.println("writePredefAttrFile: AFTER:  \""+oupt.edBuffer());
                
        //!***********  output main w/locals and tag count  ****;
        oupt.putKey(Key.mainKey);
        predefModule.writeQuantHead("",oupt);
        predefModule.writeQuantList("",oupt);
        oupt.putKey(Key.mainKey);
        oupt.putByte(Quantity.ntag);
        oupt.putKey(Key.mainKey); // *** terminates reading ***;
        
        oupt.close();
		
		if (RTS_FEC_Interface_Option.verbose)	Util.TRACE("writePredefAttrFile: *** ENDOF Generate AttributeFile: " + PredefGlobal.attributeFile);
		if(RTS_FEC_Interface_Option.INTERFACE_TRACE_LEVEL > 2) HexDump.hexDump(PredefGlobal.attributeFile);
	}

	
	
}

