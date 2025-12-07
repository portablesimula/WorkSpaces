package simuletta.RTS_FEC_InterfaceGenerator.predef.declaration;

import java.io.IOException;

import simuletta.RTS_FEC_InterfaceGenerator.predef.AttrFile;
import simuletta.RTS_FEC_InterfaceGenerator.predef.parser.Parser;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Category;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Key;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.KeyWord;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Kind;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Type;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

/**
 * Procedure Quantity.
 * 
 * <pre>
 * Syntax:
 * 
 * 		Procedure ::=  < Type >?  PROCEDURE  procedure'identifier ==  < QuantInfo >  < paramSpec >? ;
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
 * A-Code:
 * 
 * 
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class Procedure extends Quantity {
//	public int lineNumber;    // From Quantity
//	public String identifier; // From Quantity
//	public String quantInfo;  // From Quantity
//	public Type type;         // From Quantity
	ParameterList parameterList;

    private static final boolean TESTING=false;

    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public Procedure(final String identifier) {
    	super(identifier);
    	this.kind=Kind.K_proc;
    	this.categ=Category.C_local; //C_block; //C_extnal;
    }

    public static Procedure doParse(Type type) {
    	Procedure proc=new Procedure(Parser.expectIdentifier());
    	proc.type=type;
    	//IO.println("procedure.doParse: proc.identifier="+proc.identifier);
    	Parser.expect(KeyWord.DEFINE);
		proc.putQuantInfo(Parser.expectString());
    	//IO.println("procedure.doParse: quantInfo="+proc.quantInfo);
    	if(Parser.accept(KeyWord.BEGPAR)) {
    		proc.parameterList=ParameterList.parseParameters();
    		//IO.println("procedure.doParse: params="+proc.parameterList);
    		Parser.expect(KeyWord.SEMICOLON);
    	} else {
    		Parser.expect(KeyWord.SEMICOLON); // SKIP IT
    		Parser.expect(KeyWord.SEMICOLON); // SKIP IT
    	}
    	if(TESTING) IO.println("Procedure.doParse: "+proc);
    	return(proc);
    }


    // ***********************************************************************************************
    // *** writeQuant
    // ***********************************************************************************************
    public void writeQuant(String indent,AttrFile oupt) throws IOException {
        writeQuantHead(indent,oupt);
        if(quantInfo.ovlkind!=0) {
        	oupt.putKey(Key.overMark);
        	oupt.putByte(quantInfo.ovlkind);
        }
        if(quantInfo.idents.size()==2) {
        	//    ?     xMark "modid" "checkcode" "language" "external ident."
        	//    ?     yMark                     "language" "external ident."
        	//    ?      -- only one of xMark or yMark can occur  --
        	oupt.putKey(Key.yMark);
        	oupt.putText(quantInfo.idents.elementAt(0));
        	oupt.putText(quantInfo.idents.elementAt(1));
        } else if(quantInfo.idents.size()==1) {
        	oupt.putKey(Key.yMark);
        	oupt.putText(quantInfo.idents.elementAt(0));
        	oupt.putText("MISSING");
//        	Util.IERR();
        } else if(quantInfo.idents.size()!=0) {
        	Util.IERR();
        }
        if(parameterList!=null) parameterList.writeQuantList(indent,oupt);
    }
	
    public String toString() {
    	StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": ");
    	if(type!=null) s.append(""+type+" ");
    	s.append("PROCEDURE ").append(identifier).append(" == ").append(edQuantInfo());
    	return(s.toString());
    }

}
