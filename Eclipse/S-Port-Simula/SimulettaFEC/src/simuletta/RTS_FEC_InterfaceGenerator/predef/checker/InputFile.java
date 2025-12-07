package simuletta.RTS_FEC_InterfaceGenerator.predef.checker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Key;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

/**
 * 
 *** PHYSICAL Format of attribute files ***
 * 
 * The attribute file is split into a number of buffers. The format of this
 * buffering is superimposed upon the logical format as follows:
 * 
 * Each buffer or "very long string" is output as - a marker byte
 * (longSwap,bufSwap) followed by the length of the buffer or string (short
 * integer packed into 2 bytes)
 * 
 * A buffer may be output - at the beginning of outquantwlist - whenever a
 * string (as defined under the logical layout) is output - whenever a key is
 * output
 * 
 * During reading, the buffers are skewed so that the length bytes of the
 * FOLLOWING buffer is included as the last bytes of any buffer read. This means
 * that after reading the first two length bytes, all reading is done thru
 * 'intext', with the following exception:
 * 
 * Very long strings (of length larger than the buffer size) are output just as
 * buffers. They will almost certainly cause the buffer sizes to vary far from
 * the ideal, if they occur. S-port does, however, permit text constants of up
 * to 32000 bytes in length as attributes to separately compiled classes.
 * Therefore this facility must be included. On input, such strings will of
 * necessity cause inbyte to be called to get in phase again.
 *
 */
public class InputFile {
    private static final int layoutindex = 2; //!change if attr file changed;
    private static final boolean TESTING=false;

    //!*** bufsize-bufmax must allow for max number of bytes to be
    //output from a quantity descriptor without strings ;
    private static final int bufsize=2048;//, bufmax = bufsize - 25;
	
	public FileInputStream CURF;
    private  byte[] bufferBytes;
    private  ByteBuffer attrbuffer;
    private  int p;

    /**
     * 
     *      <layout index=!2!>1B  <first buffer size>I  < buffer bytes ... >   ...
     */
	public void openattributefile() throws IOException { //(q); ref(quantity) q;
//  !*** returns: moduleident in attrmodhi,lo and in simsymbol,
//                checkcode   in attrckhi,lo ;
		String relativeAttributeFileName="Attrs/FEC/"+PredefGlobal.sourceName+".atr";
		PredefGlobal.attributeFile = new File(PredefGlobal.outputDir,relativeAttributeFileName);
		if (RTS_FEC_Interface_Option.verbose) IO.println("************************** BEGIN READ ATTRIBUTE FILE: "+PredefGlobal.attributeFile+" **************************");
		if (RTS_FEC_Interface_Option.verbose)	Util.println("AttrFile.open: \"" + PredefGlobal.attributeFile+"\"");
		CURF = new FileInputStream(PredefGlobal.attributeFile);
		int layout=CURF.read();
		if(TESTING) IO.println("InputFile.openattributefile: LAYOUT="+layout); // TESTING
		if(layout != layoutindex) Util.IERR("Wrong Layout");
           
		int firstbufsize=in2byte();
		if(TESTING) IO.println("InputFile.openattributefile: firstbufsize="+firstbufsize); // TESTING
        bufferBytes = new byte[bufsize];
        attrbuffer = ByteBuffer.wrap(bufferBytes);
        CURF.read(bufferBytes, 0, firstbufsize);
	} // end openattributefile;

//	private String substring(int offset, int length) {
//		return(new String(bufferBytes,offset,length));
//	}
	protected String edBuffer() {
//		if(p<30)
//			 return(substring(0,30)+"\" "+edhex(0,30));
//		else return(substring(p-20,p+20)+"\" "+edhex(p-20,p-1)+" | "+edhex(p,p+20));
		return("");
	}

    public final short in2byte() throws IOException {
        int ch1 = CURF.read();
        int ch2 = CURF.read();
        return (short)((ch1 << 8) + (ch2 << 0));
    }
	
	public String readChars(int n) {
    	byte[] bytes=new byte[n];
    	attrbuffer.get(bytes, 0, n);
    	String symbol=new String(bytes); p=p+n;
		if(TESTING) IO.println("InputFile.readChars: n="+n+", symbol=\""+symbol+'"'); // TESTING
		return(symbol);
	}
	
	public String readString() {
		int n=inByte();
		String res=null;
		if(n!=0) res=readChars(n);
    	if(RTS_FEC_Interface_Option.TRACE_CODING>1) INPUT_TRACE("readString="+res);
		return(res);
	}

	public String gettext() throws IOException {
//  begin short integer tlength;
//  REP:  
	    int key=getKey("GetText");
        if(key < Key.lowKey) {
//           simsymbol:-attrbuffer.sub(p+1,rank(key)); p:=p+rank(key);
        	String simsymbol=readChars(key);
    		if(TESTING) IO.println("InputFile.gettext(Verifier.key<lowKey): key="+key+", simsymbol=\""+simsymbol+'"'); // TESTING
        	if(RTS_FEC_Interface_Option.TRACE_CODING>1) INPUT_TRACE("gettext="+simsymbol);
//    		Util.STOP();
        	return(simsymbol);
        }
//        else ! if key >= lowkey then ; begin
//           if key=longText then begin
//              tlength:=nextNumber;
//              simsymbol:-attrbuffer.sub(p+1,tlength);
//              p:=p+tlength;
//           end
//      else if key=longSwap then begin
//            inspect CURF do begin
//              tlength:=nextNumber; simsymbol:-blanks(tlength);
//              simsymbol:-intext(simsymbol);
//              tlength:=inbyte*256 + inbyte;
//              intext(attrbuffer.sub(1,tlength)); p:=0;
//           end end
        else Util.IERR("Wrong Layout");
        return(null);
//        end;
	} // gettext

	public void swapIbuffer() throws IOException {
		int bufsize=getNumber("swapIbuffer");
        CURF.read(bufferBytes, 0, bufsize);
        attrbuffer.rewind(); p=0;
	}
	
	public int getKey(String where) throws IOException {
		int key;
    REP:while(true) {
    		key=inByte();
			if(TESTING) IO.println("InputFile.nextKey: key="+key); // TESTING
			if(key == Key.bufSwap) { swapIbuffer(); continue REP; }
			break REP;
		}
    	String sequ="";

    	if(RTS_FEC_Interface_Option.TRACE_CODING>0) {
    		if(key==Key.endlist) INDENT=INDENT-3;
    		INPUT_TRACE("nextKey="+Key.edKey(key)+'['+where+']'+sequ);
    		if(key==Key.begList) INDENT=INDENT+3;
    	}
    	return(key);
	}
	
	public int getByte(String where) {
		int b=inByte();
		if(TESTING) IO.println("InputFile.getByte: byte="+b); // TESTING
    	if(RTS_FEC_Interface_Option.TRACE_CODING>1) INPUT_TRACE("getByte="+b+'['+where+']');
    	return(b);
	}
	
	private int inByte() {
		int b=Byte.toUnsignedInt(attrbuffer.get()); p++;
    	return(b);
	}

	public int getNumber(String where) {
		int b1=inByte();
		int b2=inByte();
		int n=b1*256+b2;
    	if(RTS_FEC_Interface_Option.TRACE_CODING>1) INPUT_TRACE("nextNumber="+n+'['+where+']');
		return(n);
	}

//%+Z       text procedure nextitemoft;
//%+Z       begin short integer i,j;
//%+Z             while permt.more do if permt.getchar <> ' ' then goto ubl;
//%+Z             nextitemoft:-notext; goto E;
//%+Z        ubl: i:=permt.pos-1; j:=1;
//%+Z             while permt.more
//%+Z             do if permt.getchar=' ' then goto bl else j:=j+1;
//%+Z         bl: nextitemoft:-permt.sub(i,j);
//%+Z             if permt.sub(i,j) = "*" then nextitemoft:-notext;
//%+Z   E:  end of nextitemoft;

	
	private static int INDENT=10;
	public static void INPUT_TRACE(String mss) {
//		String line=mss+"  -- CallChain: "+Util.getCallChain();
       	System.out.print(mss.indent(INDENT));
	}

}
