/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.value;

import java.util.Vector;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Tag;

/**
 * AttributeAddress.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 * 		AttributeReferenceValue ::= NOFIELD  |  FieldConstant
 * 
 * 			FieldConstant ::= FIELD ( record'Identifier < . Identifier >+ )
 * 
 * 
 *  S-Code:
 *  
 *  	AttributeAddress ::= ANONE  |  < DOT attribute:tag >* AADDR attribute:tag
 * </pre>
 * 
 * The value of an attribute address is the OFFSET of the attribute.
 * This may be computed relative to any surrounding record by means of the c-dot construction.
 * 
 * ANONE is an empty OFFSET, refering to no attribute.
 * <p>
 * The interpretation of the construction
 * <p>
 * 			c-dot T1 c-dot T2 ... c-aaddr LT   is    "T1.T2.. .LT".AADDR
 * 
 * @author Øystein Myhre Andersen
 */
public class AttributeAddress extends Value {
	public String offset;
	public Vector<String> dotList=new Vector<String>();


	public AttributeAddress() {}
	
	public static AttributeAddress parse() {
		AttributeAddress aaddr=new AttributeAddress();
        Parser.expect(KeyWord.BEGPAR);
        aaddr.offset=Parser.expectIdentifier();
        while(Parser.accept(KeyWord.DOT)) {
        	String ident=Parser.expectIdentifier();
//        	IO.println("AttributeAddress.parse: DOT "+ident);
            aaddr.dotList.add(ident);
        }
        Parser.expect(KeyWord.ENDPAR);
//        IO.println("AttributeAddress.parse: "+aaddr);
//        Util.STOP();
        return(aaddr);
	}


	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
	public Type doOutConst() {
		enterLine();
		if(offset==null) {			
			sCode.outinst(S_ANONE); sCode.outcode(); 
			return(Type.Field);
		} 
		Record rec=(Record) Declaration.findMeaning(offset);
//		Tag tag=rec.getTag();
		VariableDeclaration q=null;
//		for(String attr:dotList) {
//            //IO.println("AttributeAddress.doOutConst: DOT "+attr);
////            if(q!=null) {
//            	sCode.outinst(S_C_DOT);
//            	sCode.outtag(tag);
////            }
//			q=rec.findAttribute(attr);
//			tag=q.getTag();
//			rec=q.type.getQualifyingRecord();
//		}
		
//		for(String attr:dotList) {
//            IO.println("AttributeAddress.doOutConst: DOT "+attr);
//		}		
		for(int i=0;i<dotList.size();i++) {
//            IO.println("AttributeAddress.doOutConst: DOT "+attr);
//            if(q!=null) {
//            	sCode.outinst(S_C_DOT);
//            	sCode.outtag(tag);
//            }
			String attr=dotList.elementAt(i);
			q=rec.findAttribute(attr);
			Tag tag=q.getTag();
			
			if(i==dotList.size()-1)
				 sCode.outinst(S_C_AADDR);
			else sCode.outinst(S_C_DOT);
        	sCode.outtag(tag);			
			
			rec=q.type.getQualifyingRecord();
		}
//		sCode.outinst(S_C_AADDR); sCode.outtag(tag);
		sCode.outcode();
		exitLine();
		return(Type.Field(q.type));
	}


	public String toString() {
		if(offset==null) return("ANONE");
		StringBuilder s=new StringBuilder();
		s.append(offset);
        for(String attr:dotList) s.append('.').append(attr);
		return(s.toString());
	}

}
