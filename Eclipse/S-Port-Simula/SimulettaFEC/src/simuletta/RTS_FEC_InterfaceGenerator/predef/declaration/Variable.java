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

import simuletta.RTS_FEC_InterfaceGenerator.predef.AttrFile;
import simuletta.RTS_FEC_InterfaceGenerator.predef.parser.Parser;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.KeyWord;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Kind;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Type;
import simuletta.RTS_FEC_InterfaceGenerator.predef.util.Util;

/**
 * <pre>
 * 
 * Syntax:
 * 
 *  	Variable ::= Type identifier ==  < QuantInfo >
 *  
 * 			QuantInfo ::= "...INFO..."      e.g. "!06! 2 *T_CONSTANT1 *T_CONSTANT2";
 *   
 * </pre>
 * 
 * @see Type
 * @author Øystein Myhre Andersen
 */
public class Variable extends Quantity {
//	public int lineNumber;    // From Quantity
//	public String identifier; // From Quantity
//	public String quantInfo;  // From Quantity

	
	public Variable(final Type type,final String identifier) {
		super(identifier);
		this.type=type;
		this.kind=Kind.K_ident;
		this.categ=C_local;
	}

    public static Quantity doParse(Type type,String identifier,boolean constflag) {
    	Variable quant=new Variable(type,identifier);
//    	IO.println("VariableDeclaration doParse: "+quant);
		Parser.expect(KeyWord.DEFINE);
		quant.putQuantInfo(Parser.expectString());
//		IO.println("procedure.doParse: quantInfo="+quant.quantInfo);
		Parser.expect(KeyWord.SEMICOLON); // SKIP IT
    	
//		Util.println("Variable.doParse: " + quant);
//    	Util.STOP();
		return(quant);
    }

    // ***********************************************************************************************
    // *** writeQuant
    // ***********************************************************************************************
    public void writeQuant(String indent,AttrFile oupt) throws IOException {
        writeQuantHead(indent,oupt);
//    	Util.IERR("Missing redefinition of writeQuant in "+this.getClass().getSimpleName());
    }

	public String toString() {
    	StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": VARIABLE ");
    	if(type!=null) s.append(""+type+" ");
    	s.append(identifier+" == "+edQuantInfo());
		return(s.toString());
	}
	


}
