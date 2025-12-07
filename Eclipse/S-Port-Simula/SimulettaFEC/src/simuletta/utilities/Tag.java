/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.utilities;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import simuletta.compiler.declaration.scope.InsertedModule;

/**
 * 
 * @author Øystein Myhre Andersen
 *
 */
public class Tag {
	public final static int FirstUserTag=32;   // First user tag
	public static int lastTag=FirstUserTag-1;  // Last used tag number
	public static int nxtag;  // Number of external tag numbers
	public static InsertedModule currentInsert;
	public static HashMap<String,Tag> predefined=new HashMap<String,Tag>();
	
	private int sequ;          // Tag sequence number
	private int xtag;          // External Tag number
	private String ident;
	
	public static void preDefine(String ident) {
		Tag tag=new Tag(true,ident,"");
//		IO.println("Tag.preDefine: "+ident+"'tag="+tag);
		predefined.put(ident.toUpperCase(),tag);
	}
	
	public static void checkPredefinedEmpty() {
		int size=predefined.size();
		if(size!=0) {
			IO.println("\n\nTag.checkPredefinedEmpty: FAILED: size="+size);
			Set<String> keys=predefined.keySet();
			for(String key:keys) {
				IO.println("Tag.checkPredefinedEmpty: predefined("+key+")="+predefined.get(key));
			}
			Util.IERR("Tag.checkPredefinedEmpty: FAILED: size="+size);
		}
	}
	
	public static Tag newTag(boolean visible,String identifier,String subid) {
		String key=(identifier+subid).toUpperCase();
//    	Tag pretag=predefined.get(key);
    	Tag pretag=predefined.remove(key);
    	if(pretag!=null) {
//    		IO.println("Tag.preDefined: "+identifier+subid+"'tag="+pretag+", predefined.size="+predefined.size());
    		if(pretag.xtag<0) Util.STOP();
    		return(pretag);
    	}
		if(identifier==null) Util.STOP();
		return(new Tag(visible,identifier,subid));
	}
	
	private Tag(boolean visible,String identifier,String subid) {
		if(identifier==null) Util.STOP();
		this.ident=identifier+subid;
		lastTag=lastTag+1;
		this.sequ=lastTag;
        if(visible) {
        	xtag=nxtag++; // get new external tag
        	xTAGTAB.add(this);
        } else xtag= -1;
	}
    
    public int getCode() { return(sequ); }
    public String getIdent() { return(ident); }
    public int getXtag() { return(xtag); }
	
//	"********************************** xTAGTABLE **********************************
    public static Vector<Tag> xTAGTAB;//=new Vector<Tag>();
    
	public static void INIT() {
		lastTag=FirstUserTag-1;  // Last used tag number
		nxtag=0; 				 // Number of external tag numbers
		xTAGTAB=new Vector<Tag>();
		currentInsert=null;
	}
	
	public static void outputTAGTAB() {
		//IO.println("Tag.outputTAGTAB: nxtag="+nxtag+", xTAGTAB.size="+xTAGTAB.size());
		//printTagTable();
		for(int i=0;i<nxtag;i++) {
			Tag tag=xTAGTAB.elementAt(i);
			Util.ASSERT(tag.xtag >= 0,"Illegal xtag");
			sCode.outinst(S_TAG);
			sCode.outtag(tag);
			sCode.outnumber(tag.xtag); sCode.outcode();
		}
	}

    public static void printTagTable() {
    	IO.println("****** BEGIN LISTING OF xTAGTABLE: nxtag= "+nxtag+" ******");
    	for(int i=0;i<nxtag;i++) {
    		Tag d=xTAGTAB.elementAt(i);
    		if(d!=null) {
    			IO.println("xTAGTAB["+i+"] = "+"  "+d);
    		}
    	}
    	IO.println("****** ENDOF LISTING OF xTAGTABLE ******");
    }

	
	public String toString() {
        if(xtag >= 0) return("T" + sequ + "X" + (xtag) + ":" + ident);
		return("T" + sequ + ':' + ident);
	}

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

	public Tag() {}
	
	public void writeTag(ObjectOutput oupt) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Tag: "+this);
		oupt.writeObject(ident);
		oupt.writeShort(xtag);
		//Util.TRACE_OUTPUT("END Write Tag: "+this);
	}
	
	public static Tag readTag(ObjectInput inpt) throws IOException, ClassNotFoundException {
		Tag tag=new Tag();
		tag.ident=(String) inpt.readObject();
		tag.xtag=inpt.readShort();
		tag.sequ=tag.xtag+currentInsert.tagbase;
		if(tag.sequ>lastTag) lastTag=tag.sequ;
//		Util.BREAK("Tag.readExternal: tagbase="+currentInsert.tagbase+"   TAG="+this);
//		Util.println("Tag.readExternal: tagbase="+currentInsert.tagbase+", xtag="+xtag+"   TAG="+this);
		return(tag);
	}

}
