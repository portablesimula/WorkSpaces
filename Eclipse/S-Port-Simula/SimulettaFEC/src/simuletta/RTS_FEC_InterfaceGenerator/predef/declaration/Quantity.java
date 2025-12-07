/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.RTS_FEC_InterfaceGenerator.predef.declaration;

import static simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal.*;

import java.io.IOException;
import java.util.Vector;

import simuletta.RTS_FEC_InterfaceGenerator.predef.AttrFile;
import simuletta.RTS_FEC_InterfaceGenerator.predef.parser.Parser;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Category;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.KeyWord;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Kind;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Type;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

public abstract class Quantity {
	public int lineNumber;
	//public int xtag;
	public String identifier;
    public Type type;
    public int kind;
    public int categ;
	public int clf; // Classification code
	public QuantInfo quantInfo;
	
	public Object prefqual;
	public int plev;
	
	public static int ntag;

    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public Quantity(final String identifier) {
		lineNumber = PredefGlobal.sourceLineNumber;
	    this.identifier=identifier;
	    this.type=Type.noType;
    }


    //%title ******   P A R S E R :   declarations   ******
    public static void doParse(Vector<Quantity> declset) {        
        LOOP: while(true) {
    		if(Parser.accept(KeyWord.PROCEDURE)) {
        		//IO.println("Declaration.doParse:======================================  Case: PROCEDURE  procedure'identifier ...");
        		declset.add(Procedure.doParse(Type.noType));
    			continue LOOP;
    		}
        	if(Parser.accept(KeyWord.CLASS)) {
        		//IO.println("Declaration.doParse:======================================  Case: CLASS  class'identifier ...");
        		declset.add(ClassQuant.doParseClass(null));
        		continue LOOP;
        	}
        	Type type=Parser.acceptType();
        	if(type!=null) {
        		if(Parser.accept(KeyWord.PROCEDURE)) {
            		//IO.println("Declaration.doParse:======================================  Case: Type  PROCEDURE  procedure'identifier ...");
            		declset.add(Procedure.doParse(type));
        			continue LOOP;
        		} else {
        	    	String identifier=Parser.expectIdentifier();
        	    	//IO.println("Declaration.doParse:======================================  Case: Type identifier ...");
        	    	declset.add(Variable.doParse(type,identifier,false));
        	    	continue LOOP;
         		}
        	}
        	
	    	String identifier=Parser.acceptIdentifier();
	    	if(identifier!=null) {
	    		// Parse prefixed class
	    		if(Parser.accept(KeyWord.CLASS)) {
	    			//IO.println("Declaration.doParse:======================================  Case: prefix'identifier CLASS  class'identifier ...");
	    			declset.add(ClassQuant.doParseClass(identifier));
	    			continue LOOP;
	    		} else {
	    			Util.FORCED_EXIT();
	    		}
	    	}
        	
    		//IO.println("Declaration.doParse:====================================== NO MORE DECLARATIONS");
        	Parser.TRACE("Declaration.doParse: NO MORE DECLARATIONS");
        	break LOOP;
        }
    }

    // ***********************************************************************************************
    // *** writeQuant
    // ***********************************************************************************************
    public void writeQuant(String indent,AttrFile oupt) throws IOException {
    	Util.IERR("Missing redefinition of writeQuant in "+this.getClass().getSimpleName());
    }

    	
    // ***********************************************************************************************
    // *** writeQuantHead
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
	@SuppressWarnings("unused")
	public void writeQuantHead(String indent,AttrFile oupt) throws IOException {
    	//*** basic quantity descriptor, size computed above ***;
    	// - assume that categ <8 and kind < 16  ALWAYS ***;
    	if(RTS_FEC_Interface_Option.INTERFACE_TRACE_LEVEL > 0) IO.println("writeQuant: "+indent+identifier+", type="+type
    			+", kind="+Kind.edKind(kind)+", categ="+Category.edCateg(categ)+", clf="+clf);
    	int xtag=quantInfo.getXtag(0);
//    	IO.println("writeQuant: "+indent+identifier+", exttag="+xtag+", type="+type+", kind="+Kind.edKind(kind)+", categ="+Category.edCateg(categ)+", clf="+clf);
    	if(RTS_FEC_Interface_Option.TRACE_CODING>1) AttrFile.OUPUT_TRACE(Kind.edKind(kind)+": "+identifier+", exttag="+xtag+", type="+type
    			+", kind="+Kind.edKind(kind)+", categ="+Category.edCateg(categ)+", clf="+clf+", "+quantInfo);
    	oupt.putByte((kind*8) + categ);
//    	IO.println("writeQuant: PACKED="+((kind*8) + categ)+", kind="+kind+", categ="+categ);

    	// - assume that type < 128 ALWAYS ***;
    	String prefqual=this.type.prefqual;
    	if(prefqual != null) {
    		oupt.putByte(type.code+128);
    		oupt.putText(prefqual);
    	} else oupt.putByte(type.code);

    	// - assume that clf < 128 ALWAYS ***;
	     oupt.putByte(clf);
	     oupt.putNumber(xtag+1);
	     
//			if(kind != K_class) {
//				// symbols starting with a single underline are skipped ;
//				if(identifier.startsWith("_") && !identifier.startsWith("__")) identifier="";
//			}

	     oupt.putText(identifier);
    }

    
    public void putQuantInfo(String info) {
    	clf=info.charAt(0);
    	if(clf==92) {
    		clf=10;
    		quantInfo=new QuantInfo(info.substring(2,info.length()));
    	} else quantInfo=new QuantInfo(info.substring(1,info.length()));
//    	IO.println("BEFORE: "+quantInfo);
//    	IO.println("AFTER: "+s);
//    	Util.STOP();    	
    }
    
    public String edQuantInfo() {
    	String s="\"!"+clf+"!"+quantInfo+'\"';
//    	IO.println("BEFORE: "+quantInfo);
//    	IO.println("AFTER: "+s);
//    	Util.STOP();
    	return(s);
    }
    
	public void print(final String lead,final int indent) {
		Util.println(Util.edIndent(indent) + lead + this);
	}
	
	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append(identifier);
		s.append(')');
		return(s.toString());
	}
	
}
