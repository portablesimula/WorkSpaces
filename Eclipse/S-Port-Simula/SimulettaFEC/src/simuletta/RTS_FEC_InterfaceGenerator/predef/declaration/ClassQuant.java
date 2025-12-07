package simuletta.RTS_FEC_InterfaceGenerator.predef.declaration;

import static simuletta.RTS_FEC_InterfaceGenerator.predef.util.PredefGlobal.*;

import java.io.IOException;
import java.util.Vector;

import simuletta.RTS_FEC_InterfaceGenerator.predef.AttrFile;
import simuletta.RTS_FEC_InterfaceGenerator.predef.parser.Parser;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Category;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Key;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.KeyWord;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Kind;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.RTS_FEC_Interface_Option;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Type;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

/**
 * Class Quantity.
 * 
 * <pre>
 * Syntax:
 * 
 * 		ClassQuant ::=  < prefix'identifier >?  CLASS  class'identifier ==  < QuantInfo >  < ParameterSpec >?
 * 							BEGIN < Quantity >*  END
 * 
 * 			QuantInfo ::= "...INFO..."      e.g. "!06! 2 *T_CONSTANT1 *T_CONSTANT2";
 * 
 * 			ParamSpec ::= ( < paramList > ) < modeList >?  < typeList >?
 * 
 * 				paramList ::= param  < , param >*
 * 
 * 					param ::= identifier  ==  " < paramInfo'String > "
 * 
 * 				modeList ::= mode identifier ;  < mode identifier ; >*
 * 
 * 					mode ::= VALUE | NAME
 * 
 * 				typeList ::= type identifier ;  < type identifier ; >*
 * 
 * 
 * 			Quantity ::= ClassQuant  |  ProcedureDeclaration  |  VariableDeclaration
 * 
 * 				VariableDeclaration ::= Type identifier ==  < QuantInfo >
 * 
 * A-Code:
 * 
 * 
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class ClassQuant extends Quantity {
//	public int lineNumber;    // From Quantity
//	public int xtag;          // From Quantity
//	public String identifier; // From Quantity
//  public Type type;         // From Quantity
//  public int kind;          // From Quantity
//  public int categ;         // From Quantity
//	public int clf;           // From Quantity

	public Vector<Quantity> QuantityList=new Vector<Quantity>();
;
	public String prefixIdentifier;
	ParameterList parameterList;

    private static final boolean TESTING=false;

    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public ClassQuant(final String identifier) {
    	super(identifier);
    	this.kind=Kind.K_class;
    	this.categ=Category.C_local; //C_block; //C_extnal;
    }

	public static ClassQuant doParseClass(String prefixIdentifier) {
		if(TESTING) IO.println("ClassQuant.doParse: BEGIN");
		String ident=Parser.expectIdentifier();
		if(TESTING) IO.println("ClassQuant.doParse: rec.identifier="+ident);
		ClassQuant rec=new ClassQuant(ident);
		if(prefixIdentifier!=null) {
			rec.prefixIdentifier=prefixIdentifier;
			rec.type=new Type(Type.INOTY,prefixIdentifier);
		}
		if(TESTING) IO.println("ClassQuant.doParse: rec.identifier="+rec.identifier);
		Parser.expect(KeyWord.DEFINE);
		rec.putQuantInfo(Parser.expectString());
		if(Parser.accept(KeyWord.BEGPAR)) {
			rec.parameterList=ParameterList.parseParameters();
			if(TESTING) IO.println("procedure.doParse: params="+rec.parameterList);
		} else {
			Parser.expect(KeyWord.SEMICOLON); // SKIP IT
		}
		if(TESTING) IO.println("ClassQuant.doParse: "+rec);
		if(Parser.accept(KeyWord.SEMICOLON)) {} // Nothing
		else {
			Parser.expect(KeyWord.BEGIN);
//			rec.QuantityList = new Vector<Quantity>();
			rec.doParseQuantities();
			if(Parser.expect(KeyWord.END));
		}
			if(RTS_FEC_Interface_Option.TRACE_PARSE_BREIF) rec.print("",0);
			Parser.TRACE("Record.doParse: END rec.symbol="+rec.identifier+", currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);
		return(rec);
	}

	//
	//   < prefix'identifier >?  CLASS  class'identifier ==  < ClassInfo > BEGIN < Quantity >*  END	
	//   Type identifier ==  < QuantInfo >
	//   < Type >?  PROCEDURE  procedure'identifier ==  < QuantInfo >
    public void doParseQuantities() {
    	Quantity.doParse(QuantityList);
    }

    // ***********************************************************************************************
    // *** writeQuant
    // ***********************************************************************************************
    public void writeQuant(String indent,AttrFile oupt) throws IOException {
        print("writeQuant",20);
        writeQuantHead(indent,oupt);
        oupt.putKey(Key.forcMark);

        boolean inrflag=quantInfo.nIdent==3;
        
		if( (clf==Clf004 || clf==Clf009) && inrflag) { //!set inrtag for sys class;
			IO.println("thisMark=" + Key.thisMark);
            oupt.putKey(Key.thisMark); oupt.putByte(2); }

//        if(parameterList!=null) parameterList.writeQuantList(indent,oupt);
        writeQuantList(indent,oupt);
//    	Util.IERR("Missing redefinition of writeQuant in "+this.getClass().getSimpleName());
    }
    
    // ***********************************************************************************************
    // *** writeQuantList
    // ***********************************************************************************************
    //    ?    begList
    //    ?    <quantity descriptor>* -- for each new virtual in virt-list
    //    ?                           -- NOTE: the virtuals MUST be first
    //    ?    <quantity descriptor>* -- for each elt in fpar-list for which:
    //    ?                   --  descr is not extbrecord OR descr.status='S'
    //    ?    endList
    public void writeQuantList(String indent,AttrFile oupt) throws IOException {
    //!***********  output quant list  ****;
    	int npar=(parameterList==null)?0:parameterList.params.size();
    	if(npar==0 && QuantityList.size()==0) return;
    	oupt.putKey(Key.begList);
    	
    	if(parameterList!=null) {
    		for(Parameter p:parameterList.params) {
        		oupt.checkBufferSwap();
        		p.classParameter=true;
        		p.writeQuant(indent+"   ",oupt);
//            	oupt.putKey(Key.forcMark);
    		}
    	}
    	
    	
    	for(Quantity decl:QuantityList) {
    		oupt.checkBufferSwap();
    		decl.writeQuant(indent+"   ",oupt);
		}
    	oupt.putKey(Key.endlist);
    }

	public void printDeclarationList() {
		if(QuantityList!=null) for(Quantity d:QuantityList) {
			Util.println("DECLARED: "+d.getClass().getSimpleName()+" "+d);
		}
	}
	
    public void print(final String lead,final int indent) {
    	StringBuilder s=new StringBuilder(Util.edIndent(indent));
    	s.append("Line "+lineNumber+": ");
    	if(prefixIdentifier!=null) s.append(prefixIdentifier).append(" ");
    	s.append("CLASS ").append(identifier).append(" == ").append(edQuantInfo());
    	if(TESTING) IO.println(s.toString()); s=new StringBuilder();
    	if(QuantityList!=null) {
    		for(Quantity d:QuantityList) d.print("",indent+1);
    	}
    }
	
    public String toString() {
    	StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": ");
    	if(prefixIdentifier!=null) s.append(prefixIdentifier).append(" ");
    	s.append("CLASS ").append(identifier).append(" == ").append(edQuantInfo());
//    	if(QuantityList!=null) {
//    		s.append(" Atributes: ");
//    		for(Quantity d:QuantityList) s.append(d).append("  ");
//    	}
    	return(s.toString());
    }

	public static void writePredef(ClassQuant q) throws IOException {
		
	}

}
