package simuletta.RTS_FEC_InterfaceGenerator.predef;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HexFormat;

import simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Key;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

public class AttrFile {
    protected static final int layoutindex = 2; //!change if attr file changed;
//    private static byte[] attrbuffer;
    private  byte[] bufferBytes;
    private  ByteBuffer attrbuffer;
    private  final byte NUL = 0;
    private  int p;
    private  OutputStream oupt;
    
    //!*** bufsize-bufmax must allow for max number of bytes to be
    //output from a quantity descriptor without strings ;
    private static final int bufsize=2048, bufmax = bufsize - 25;


	protected void open() throws IOException {
		String relativeAttributeFileName="Attrs/FEC/"+PredefGlobal.sourceName+".atr";
		PredefGlobal.attributeFile = new File(PredefGlobal.outputDir,relativeAttributeFileName);
		if (RTS_FEC_Interface_Option.verbose) IO.println("************************** BEGIN WRITE ATTRIBUTE FILE: "+PredefGlobal.attributeFile+" **************************");
		if (RTS_FEC_Interface_Option.verbose)	Util.println("AttrFile.open: \"" + PredefGlobal.attributeFile+"\"");
    	Util.createFile(PredefGlobal.attributeFile);
		FileOutputStream fileOutputStream = new FileOutputStream(PredefGlobal.attributeFile);
		// BufferedOutputStream uses a default buffer size of 512 bytes
		oupt = new BufferedOutputStream(fileOutputStream);
        oupt.write(layoutindex);
	
		//Util.BREAK("Module.writeModule: File="+Global.attributeFile);

        // must start "S-port 108.1x" where 'x' is layout index;
        //attrbuffer=new StringBuffer(" ".repeat(bufsize));
        bufferBytes = new byte[bufsize];
        attrbuffer = ByteBuffer.wrap(bufferBytes);
        attrbuffer.put(NUL); attrbuffer.put(NUL); p=2;
        //        attrbuffer.replace(0, 12, "S-port 108.1"); p=12;
//        IO.println("writeCombinedAttrFile: HEAD: \""+edBuffer());
	
	}
	
	public void checkBufferSwap() throws IOException {
		if(p > bufmax-500 ) swapObuffer();
	}

    protected void close() throws IOException {
        swapObuffer(); //!output last buffer;
    	oupt.flush(); oupt.close(); oupt = null;
    	if (RTS_FEC_Interface_Option.verbose)	IO.println("************************** END WRITE ATTRIBUTE FILE: "+PredefGlobal.attributeFile+" **************************");
    }
    
    private void storebyte(int ch) {
    	if(RTS_FEC_Interface_Option.TRACE_CODING>3) OUPUT_TRACE("   ==> storebyte("+ch+")");
    	attrbuffer.put((byte) ch); p=p+1;
    }
    
    public void putKey(int key) {
    	if(RTS_FEC_Interface_Option.TRACE_CODING>0) {
    		if(key==Key.endlist) INDENT=INDENT-3;
    		OUPUT_TRACE("putKey("+Key.edKey(key)+")");
    		if(key==Key.begList) INDENT=INDENT+3;
    	}
    	storebyte(key);
    }
    
    public void putByte(int b) {
    	if(RTS_FEC_Interface_Option.TRACE_CODING>1) OUPUT_TRACE("   putByte("+b+")");
    	storebyte(b);
    }

    public void putText(String t) throws IOException {
    	if(RTS_FEC_Interface_Option.TRACE_CODING>1) OUPUT_TRACE("   putText("+t+")");
    	int tlength=t.length();
    	if(tlength==0) storebyte(0);
    	else {
    		if( p+tlength > bufmax ) {
    			if( tlength > bufmax ) {
    				storebyte(Key.longSwap); putNumber(tlength);
    				swapObuffer();
    				//oupt.writeIdent(t); // oupt.outtext(t);
    				oupt.write(t.getBytes()); // oupt.outtext(t);
    				Util.NOTIMPL("putText; "+t);
    				return; //goto EX
    			}
    			swapObuffer();
    		}
    		if( tlength >= Key.lowKey ) {
    			storebyte(Key.longText); putNumber(tlength);
    		} else {
    			storebyte(tlength);
    		}
//    		OUPUT_TRACE("puttext("+t+"): p="+p+", attrbuffer.position="+attrbuffer.position()+"  MIDLE:  \""+edBuffer());
    		storeText(t);
    		if( p > bufsize ) Util.IERR("internerr('!7!',sourceline");
//    		OUPUT_TRACE("puttext("+t+"): p="+p+", attrbuffer.position="+attrbuffer.position()+"  AFTER:  \""+edBuffer());
    	}
    }
    
    protected void storeText(String t) {
    	int lng=t.length();
    	byte[] bytes=t.getBytes();
    	for(int i=0;i<lng;i++) {
        	if(RTS_FEC_Interface_Option.TRACE_CODING>4) OUPUT_TRACE("   ==> storebyte("+bytes[i]+")");
    		attrbuffer.put(bytes[i]);
    	}
        p=p+lng; 
        Util.ASSERT(p==attrbuffer.position(),"storeText "+t+": p="+p+", attrbuffer.position="+attrbuffer.position());
    }

    public void putNumber(int i) throws IOException {
    	if(RTS_FEC_Interface_Option.TRACE_CODING>1) OUPUT_TRACE("   putNumber("+i+")");
     	storebyte((char)((i>>8)&0xff));
     	storebyte((char)(i&0xff));
     	//p=p+2;
//        OUPUT_TRACE("putNumber("+i+"): p="+p+", attrbuffer.position="+attrbuffer.position()+"  AFTER:  \""+edBuffer());
    }

	protected void swapObuffer() throws IOException {
    	storebyte((char)Key.bufSwap);
    	byte b1=(byte) ((p>>8)&0xff);
    	byte b2=(byte) (p&0xff);
    	attrbuffer.rewind();
    	attrbuffer.put(b1);
    	attrbuffer.put(b2);
//        OUPUT_TRACE("swapObuffer: b1="+b1+", b2="+b2+", p="+p+"  AFTER:  \""+edBuffer());
      	oupt.write(bufferBytes, 0, p);
      	p=2; //!leave room for actual size;
    }

	protected String edhex(int i,int j) {
		HexFormat formatFingerprint = HexFormat.ofDelimiter(":").withUpperCase();
	    // byte[] bytes = {0, 1, 2, 3, 124, 125, 126, 127};
//		byte[] bytes=attrbuffer.toString().getBytes();
	    String str = formatFingerprint.formatHex(bufferBytes,i,j);
	    return(str);
	}

//	private String substring(int offset, int length) {
//		return(new String(bufferBytes,offset,length));
//	}
	protected String edBuffer() {
//		if(p<30)
//			 return(substring(0,30)+"\" "+edhex(0,30));
//		else return(substring(p-20,p+20)+"\" "+edhex(p-20,p-1)+" | "+edhex(p,p+20));
		return("");
	}
	
	private static int INDENT=10;
	public static void OUPUT_TRACE(String mss) {
       	System.out.print(mss.indent(INDENT));
	}
}
