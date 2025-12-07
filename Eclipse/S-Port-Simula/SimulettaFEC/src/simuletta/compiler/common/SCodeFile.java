/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.common;

import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import simuletta.compiler.Global;
import simuletta.type.Type;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

public class SCodeFile {
	//      ----------------------------------------------------------------
	//      ---                                                          ---
	//      ---                 P O R T A B L E     S I M U L A          ---
	//      ---                                                          ---
	//      ---              S I M U L E T T A    C O M P I L E R        ---
	//      ---                                                          ---
	//      ---                                                          ---
	//      ---                 C l a s s    S C o d e F i l e           ---
	//      ---                                                          ---
	//      ----------------------------------------------------------------
    public boolean lowercase;
    String ident;
    private BufferedOutputStream scode;
    
    public SCodeFile(String ident,File outputFile) {
    	this.ident=ident;
    	createFile(outputFile);
    	try {
    		//BufferedOutputStream uses a default buffer size of 512 bytes
    		scode=new BufferedOutputStream(new FileOutputStream(outputFile),10*512);
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
    	}
    }
    
    public void close() {
    	try {
			scode.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    	
    private void outbyte(int i) {
    	try { scode.write(i); } catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void outtype(Type type) {
    	if(Option.TRACE_CODING > 0) TRC("Outtype",""+type);
    	type.toSCode();
    }
    
    private void out2byte(int n) {
		outbyte((n>>8)&255);
		outbyte(n&255);
	}


    public void outinst(int i) {
    	if(Option.TRACE_CODING > 0) TRC("Outinstr",edSymbol(i).toUpperCase());
        outbyte(i);
    }

	public void outbyt(int i) {
       if(Option.TRACE_CODING > 0) TRC("Outbyt",""+i);
       outbyte(i);
    }

	public void outbyt(int i,String info) {
       if(Option.TRACE_CODING > 0) TRC("Outbyt",""+i+':'+info);
       outbyte(i);
    }
    
    public void outnumber(int n) {
       if(Option.TRACE_CODING > 0) TRC("Outnumber",""+n);
       out2byte(n);
    }

    public void uttag(int t) { // !*** output known tag ***;
       if(t == 0) WARNING("UTTAG = ZERO???");
       if(Option.TRACE_CODING > 0) TRC("Uttag",edKnownTag(t));
       out2byte(t);   
    }
    
    private String edKnownTag(int t) {
    	switch(t) {
    	case Type.TAG_BOOL: return("BOOL");
    	case Type.TAG_CHAR: return("CHAR");
    	case Type.TAG_INT:  return("INT");
    	case Type.TAG_SINT: return("SINT");
    	case Type.TAG_REAL: return("REAL");
    	case Type.TAG_LREAL: return("LREAL");
    	case Type.TAG_AADDR: return("AADDR");
    	case Type.TAG_OADDR: return("OADDR");
    	case Type.TAG_GADDR: return("GADDR");
    	case Type.TAG_PADDR: return("PADDR");
    	case Type.TAG_RADDR: return("RADDR");
    	case Type.TAG_SIZE:  return("SIZE");
    	default: return("UNKNOWN:"+t);
    	}
    }

    public void outtag(Tag t) {
       if(Option.TRACE_CODING > 0) TRC("Outtag",""+t);
       outTagWithIdent(t);
    }

    public void outtagid(Tag t) {
    	if(Option.TRACE_CODING > 0) TRC("OuttagId",""+t);
    	outTagWithIdent(t);
    }

    private void outTagWithIdent(Tag t) {
    	if(t == null) ERROR("outtag: Illegal output tag: NULL");
    	int i=t.getCode();
    	if(i == 0) WARNING("outtagid: Illegal output tag: ZERO");
    	String id=t.getIdent();
    	Util.ASSERT(id!=null,"Illegal output tag: "+t);
    	out2byte(0); out2byte(t.getCode());
    	id=id.toUpperCase();
    	byte[] b=id.getBytes();
    	outbyte(b.length); outbytes(b);
    }

    public void outstring(String t) {
    	if(Option.TRACE_CODING > 0) TRC("Outstring","\"" + t + "\"");
    	byte[] b=t.getBytes();
    	outbyte(b.length); outbytes(b);
    }

    public void outlongstring(String t) {
        if(Option.TRACE_CODING > 0) TRC("Outstring","\"" + t + "\"");
    	byte[] b=t.getBytes();
    	out2byte(b.length); outbytes(b);
    }
    
    private void outbytes(byte[] b) {
    	try { scode.write(b);	} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public void outcode(int inc) {
		if(inc<0) {	indent=indent+inc; outcode(); }
		else { outcode(); indent=indent+inc; }
		if(indent<0) indent=0;
	}

    public void outcode() {
    	if(Option.TRACE_CODING > 0) {
    		if(TRCBUFF.length() > 0) {
    			IO.println("Line "+Global.sourceLineNumber+": "+ident+".Output: "+Util.edIndent(indent)+TRCBUFF.toString());
    		}
    	}
		TRCBUFF=new StringBuilder();
    }

    private StringBuilder TRCBUFF=new StringBuilder();
    private int indent=0;

    public void TRC(String m, String v) {
    	if(Option.TRACE_CODING > 1) {
             IO.println("SCode.Line "+Global.sourceLineNumber+": "+m+":  "+v);
    	}
    	else if(Option.TRACE_CODING > 0) {
    		TRCBUFF.append(v).append(' ');
    	}
    }

}
