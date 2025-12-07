/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.declaration;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * Parameter Specification.
 * 
 * <pre>
 * Syntax:
 * 
 * 		ParameterSpecification ::= < IMPORT < Type localid < , localid >* >+ >?  < EXPORT Type Identifier >?
 * 		                         | < IMPORT < Type localid < , localid >* >+ >?  < EXIT LABEL Identifier >?
 * 
 * 			localid ::= identifier < ( repeat'integer_number ) >?
 * 
 * </pre>
 */
public class Signatur implements Externalizable {
	public Vector<Declaration> imports;
	public Declaration export;
	public Declaration exit;

    public static Signatur doParse(boolean visibleflag) {
    	Signatur ps=new Signatur();
    	if(Parser.accept(KeyWord.IMPORT)) {
    		ps.imports=new Vector<Declaration>();
    		Type type;
    		while((type=Parser.acceptType()) != null) {
    			do { // Parse Quant-List
    				VariableDeclaration q=VariableDeclaration.doParse(type,visibleflag,false);
    				Util.TRACE("Quant="+q);
    				ps.imports.add(q);
    			} while(Parser.accept(KeyWord.COMMA));
    		}
    	}
    	if(Parser.accept(KeyWord.EXPORT)) {
        	Type type=Parser.expectType(); 
        	ps.export=VariableDeclaration.doParse(type,visibleflag,false);
   		
    	}
    	else if(Parser.accept(KeyWord.EXIT)) {
        	Type type=Parser.expectType(); 
        	ps.exit=VariableDeclaration.doParse(type,visibleflag,false);
    	}
		Parser.TRACE("Signatur.parse: "+ps);
		currentModule.checkDeclarationList();
    	return(ps);
    }

    public void doChecking() {
		if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
    	if(imports != null) for(Declaration q:imports) q.doChecking();
    	if(export!=null) export.doChecking();
    	if(exit!=null) exit.doChecking();
    }

	public Declaration findLocalMeaning(String ident) {
		if(Option.TRACE_FIND_MEANING) Util.TRACE("Signatur.findLocalMeaning: "+ident);
		if(imports != null) for(Declaration d:imports) {
			if(Option.TRACE_FIND_MEANING) Util.TRACE("Signatur.findLocalMeaning: CHECKING "+d);
			if(d.identifier.equalsIgnoreCase(ident)) {
				if(Option.TRACE_FIND_MEANING) Util.TRACE("Signatur.findLocalMeaning: "+ident+"   FOUND in "+this+": "+d);
				return(d);
			}
		}
		if(Option.TRACE_FIND_MEANING) Util.TRACE("Signatur.findLocalMeaning: CHECKING EXPORT "+export);
		if(export!=null && export.identifier.equalsIgnoreCase(ident)) {
			if(Option.TRACE_FIND_MEANING) Util.TRACE("Signatur.findLocalMeaning: "+ident+"   FOUND in "+this+": "+export);
			return(export);
		}
		if(Option.TRACE_FIND_MEANING) Util.TRACE("Signatur.findLocalMeaning: CHECKING EXIT "+exit);
		if(exit!=null && exit.identifier.equalsIgnoreCase(ident)) {
			if(Option.TRACE_FIND_MEANING) Util.TRACE("Signatur.findLocalMeaning: "+ident+"   FOUND in "+this+": "+exit);
			return(exit);
		}
		 return(null);
	}

	// ***********************************************************************************************
	// *** Coding: prepareSCodeOutput
	// ***********************************************************************************************
	public void prepareSCodeOutput() {
		for(Declaration a:imports) a.prepareSCodeOutput();
		if(export!=null) export.prepareSCodeOutput();
	}

	// ***********************************************************************************************
	// *** Coding: doSCoding
	// ***********************************************************************************************
    public void doSCoding() {
          if(imports!=null) for(Declaration d:imports) {
        	  ((VariableDeclaration)d).doOutput(S_IMPORT);
          }
          if(export!=null) {
          	  ((VariableDeclaration)export).doOutput(S_EXPORT);
          }
          if(exit!=null) {
                sCode.outinst(S_EXIT); sCode.outtagid(exit.getTag());
                sCode.outcode();
          }
    }

    public void print(final String lead,final int indent) {
    	String spc=Util.edIndent(indent);
    	if(imports != null) {
    		String prfx="   IMPORT ";
    		for(Declaration d:imports) {
    			IO.println(spc+prfx+d);	prfx="          ";
    		}
    	}
    	if(export != null) IO.println(spc+"   EXPORT "+export);
    	if(exit != null) IO.println(spc+"   EXIT "+exit);
    }
    
    public String toString() {
    	StringBuilder s=new StringBuilder();
    	if(imports != null) {
    		s.append("IMPORT ");
    		boolean first=true;
    		for(Declaration d:imports) {
    			if(!first) s.append(','); first=true;
    			s.append(d).append("; ");
    		}
    	}
    	if(export != null) s.append("EXPORT ").append(export).append(';');
    	if(exit != null) s.append("EXIT ").append(exit).append(';');
    	return(s.toString());
    }
    

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

    public Signatur() { }

//	public void writeSignatur(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Signatur: ");
//		if(imports!=null) {
//			oupt.writeShort(imports.size());
//			for(Declaration decl:imports) decl.write(oupt);
//		} else oupt.writeShort(0);
//		if(export==null) oupt.writeByte(Kind.kNull); else export.write(oupt);
//		if(exit==null) oupt.writeByte(Kind.kNull); else exit.write(oupt);
//		//Util.TRACE_OUTPUT("END Write Record: "+identifier);
//	}
//
//	public static Signatur createAndReadSignatur(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		//Util.TRACE_INPUT("BEGIN Read Signatur: "+this);
//		Signatur sig=new Signatur();
//		int nImport=inpt.readShort();
//		if(nImport>0) sig.imports=new Vector<Declaration>();
//		for(int i=0;i<nImport;i++) {
//			sig.imports.add(Declaration.createAndRead(inpt));
//		}
//		sig.export=Declaration.createAndRead(inpt);
//		sig.exit=Declaration.createAndRead(inpt);
//		//Util.TRACE_INPUT("END Read Signatur: "+sig);
//		return(sig);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Signatur: ");
		out.writeObject(imports);
		out.writeObject(export);
		out.writeObject(exit);
		//Util.TRACE_OUTPUT("END Write Record: "+identifier);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		imports=(Vector<Declaration>) in.readObject();
		export=(Declaration) in.readObject();
		exit=(Declaration) in.readObject();
		//Util.TRACE_INPUT("END Read Signatur: "+sig);
	}
    
}
