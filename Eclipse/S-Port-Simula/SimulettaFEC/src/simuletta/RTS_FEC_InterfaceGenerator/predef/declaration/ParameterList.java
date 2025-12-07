package simuletta.RTS_FEC_InterfaceGenerator.predef.declaration;

import java.io.IOException;
import java.util.Vector;

import simuletta.RTS_FEC_InterfaceGenerator.predef.AttrFile;
import simuletta.RTS_FEC_InterfaceGenerator.predef.parser.Parser;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Category;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Key;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.KeyWord;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Type;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

public class ParameterList {
	public Vector<Parameter> params=new Vector<Parameter>();


	public static ParameterList parseParameters() {
		ParameterList pList=new ParameterList();
		do { pList.readParameter(); } while(Parser.accept(KeyWord.COMMA));
		if(!Parser.expect(KeyWord.ENDPAR)) Util.IERR("");
		Parser.expect(KeyWord.SEMICOLON);
		pList.readModes();
		pList.readTypes();
//		Parser.expect(KeyWord.SEMICOLON);
		return(pList);
	}
	
	private  void readParameter() {
		Parameter par=new Parameter(Parser.expectIdentifier());
		Parser.expect(KeyWord.DEFINE);
//		par.symbval=Parser.expectString();
		par.putQuantInfo(Parser.expectString());
		params.add(par);
		//IO.println("ParameterList.readParameter: "+par);
		//	Util.STOP();
	}
	
	private void readModes() {
		while(true) {
			int categ=Category.C_unknwn; // Default
        	if(Parser.accept(KeyWord.VALUE)) categ=Category.C_value;
        	else if(Parser.accept(KeyWord.NAME)) categ=Category.C_name;
        	else return;
			String ident=Parser.expectIdentifier();
			Parser.expect(KeyWord.SEMICOLON);
			Parameter spc=lookup(ident);
			spc.categ=categ;
			//IO.println("ParameterList.readModes: "+mode+"  ==>  "+spc);
		}
	}
	
	private void readTypes() {
		while(true) {
			Type type=Parser.acceptType();
			if(type==null) return;
			String ident=Parser.expectIdentifier();
			Parser.expect(KeyWord.SEMICOLON);
			Parameter spc=lookup(ident);
			spc.type=type;
			//IO.println("ParameterList.readTypes: "+type+"  ==>  "+spc);
		}
	}
	
	private Parameter lookup(String ident) {
		for(Parameter spc:params) if(spc.identifier.equalsIgnoreCase(ident)) return(spc);
		Util.IERR("");
		return(null);
	}

    public String toString() {
    	StringBuilder s=new StringBuilder();
    	s.append("("); boolean first=true;
    	for(Parameter spc:params) {
    		if(!first) s.append(',');
    		s.append(spc); first=false;
    	}
    	s.append(')');
    	return(s.toString());
    }

    // ***********************************************************************************************
    // *** writeQuantList
    // ***********************************************************************************************
    //    ?    begList
    //    ?       <quantity descriptor>* -- for each elt in fpar-list for which:
    //    ?                              --  descr is not extbrecord OR descr.status='S'
    //    ?    endList
    public void writeQuantList(String indent,AttrFile oupt) throws IOException {
    //!***********  output quant list  ****;
    	oupt.putKey(Key.begList);
    	for(Parameter par:params) {
    		oupt.checkBufferSwap();
    		par.writeQuant(indent+"   ",oupt);
		}
    	oupt.putKey(Key.endlist);
    }

	public void print() {
		if(params!=null) for(Parameter d:params) {
			Util.println("DECLARED: "+d.getClass().getSimpleName()+" "+d);
		}
	}

}
