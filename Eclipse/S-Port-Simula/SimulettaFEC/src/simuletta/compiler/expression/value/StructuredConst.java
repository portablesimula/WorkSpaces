/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.expression.value;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;
import java.util.Vector;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.declaration.Variant;
import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.expression.Expression;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Util;

/**
 * StructuredConst.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 *		StructuredConst ::= RECORD : StructuredValue'Identifier ( AttrValue < , AttrValue >* )
 *			AttrValue ::= attribute'Identifier = value
 *						| attribute'Identifier = ( value < , value >* )
 *
 * S-Code:
 * 
 *		RecordValue
 *			::= C-RECORD StructuredType
 *				< AttributeValue >+ ENDRECORD
 *
 *		AttributeValue ::= ATTR attribute:tag Type RepetitionValue
 *
 *			repetition_value
 *				::= <boolean_value>+
 *				::= <character_value>+ ! text_value
 *				::= <integer_value>+ ! <size_value>+
 *				::= <real_value>+ ! <longreal_value>+
 *				::= <attribute_address>+ ! <object_address>+
 *				::= <general_address>+ ! <program_address>+
 *				::= <routine_address>+ ! <record_value>+
 *
 *				text_value      ::= text long_string
 *				boolean_value   ::= true ! false
 *				character_value ::= c-char byte
 *				integer_value   ::= c-int integer_literal:string
 *
 *				attribute_address ::= anone   |  < c-dot attribute:tag >* c-aaddr attribute:tag
 *				object_address	  ::= onone   |  c-oaddr global_or_const:tag
 *				general_address	  ::= gnone   |  < c-dot attr:tag >* c-gaddr global_or_const:tag
 *				program_address   ::= nowhere |  c-paddr label:tag
 *				routine_address   ::= nobody  |  c-raddr body:tag
 *
 *				record_value
 *					::= c-record structured_type  <attribute_value>+  endrecord
 *
 *					attribute_value ::= attr attribute:tag type repetition_value
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class StructuredConst extends Value {
	public String ident;
	public Vector<AttributeValue> attributeValues;
	
	public StructuredConst() {}
	
	public static StructuredConst parse(boolean constflag) {
		StructuredConst strcns=new StructuredConst();
		strcns.attributeValues=new Vector<AttributeValue>();
		Parser.expect(KeyWord.COLON);
		strcns.ident=Parser.expectIdentifier();
		Parser.expect(KeyWord.BEGPAR);
		do {
			String ident=Parser.expectIdentifier();
			Parser.expect(KeyWord.EQ);
			Expression elt;
			if(Parser.accept(KeyWord.BEGPAR)) {
				RepeatedConst rep=new RepeatedConst();
				elt=rep;
				rep.values=new Vector<Value>();
				do {
					Value val=parseValue(constflag);
//			    	IO.println("StructuredConst.parse(0): val="+val);
					rep.values.add(val);
				} while(Parser.accept(KeyWord.COMMA));
				Parser.expect(KeyWord.ENDPAR);
//		    	IO.println("StructuredConst.parse(1): elt="+elt);
			} else {
				elt=parseValue(constflag);
		    	//IO.println("StructuredConst.parse(2): elt="+elt.getClass().getSimpleName()+"  "+elt);
			}
	    	//IO.println("StructuredConst.parse: elt="+elt);
	    	if(elt==null) Util.STOP();
			strcns.attributeValues.add(new AttributeValue(ident,elt));
		} while(Parser.accept(KeyWord.COMMA)); // then goto L;
		Parser.expect(KeyWord.ENDPAR);
		Parser.TRACE("StructuredConst.parse: "+strcns);
		return(strcns);
	}
	
	
	// ***********************************************************************************************
	// *** Coding: doOutConst
	// ***********************************************************************************************
//	private boolean DONE;
	public Type doOutConst() {
//		if(DONE) Util.IERR(""); DONE=true;
		Type cnstype;
		int fixrep;
		enterLine();
//			IO.println("StructuredConst.doOutConst: "+this);
			sCode.outinst(S_C_RECORD);
			Record rec=(Record) Declaration.findMeaning(this.ident);
			sCode.outtag(rec.getTag());
			sCode.outcode(+1);
			fixrep=StructuredConst.outstruct(rec,this.attributeValues);
			sCode.outinst(S_ENDRECORD);
			sCode.outcode(-1);
			StructuredConst.checkAndClearMatched(rec,this.attributeValues);
			cnstype=Type.Infix(this.ident,fixrep);
		exitLine();
		return(cnstype);
	}

	// ***********************************************************************************************
	// *** Coding: outstruct  also used by Type.outdefault
	// ***********************************************************************************************

	public static int outstruct(Record rec,Vector<AttributeValue> set) {
//		IO.println("StructuredConst.outstruct: QUAL="+rec.getClass().getSimpleName()+", set.size="+set.size()+", rec="+rec);
//		for(AttributeValue atr:set) IO.println("StructuredConst.outstruct: atr="+atr);
		int result=0;
		if(rec.prefixIdentifier != null && rec.prefixIdentifier.length()>0) {
			Record prefix=(Record) Declaration.findMeaning(rec.prefixIdentifier);
			result=outstruct(prefix,set);
		}
		AttributeValue n=outAtrset(rec,rec.declarationList,set);
		for(Variant v:rec.variantset) {
			outAtrset(rec,v.atrset,set);
		}
		if(rec.indefinite) {
			result=1;
			if(n != null)
				//inspect n.elt when repeated_const do
				if(n.value instanceof RepeatedConst) {
					RepeatedConst rc=(RepeatedConst)n.value;
					result=rc.values.size()+1;
				} else result=2;
		}
		return(result);
	}

	private static AttributeValue outAtrset(Record rec,Vector<Declaration> atrset,Vector<AttributeValue> set) {
//		for(AttributeValue val:set) val.matched=false;
//		printAttrValueSet("StructuredConst.outAtrset: BEFORE OUTPUT",set);

		AttributeValue attrValue=null;
		Type t2;
		for(Declaration d:atrset) {
			VariableDeclaration q=(VariableDeclaration)d;
			attrValue=getAttributeValue(q.identifier,set);
			if(attrValue!=null) {
				sCode.outinst(S_ATTR); sCode.outtag(q.getTag());
				q.type.toSCode(); //OldType.outtype(q.type);
//				attrValue=getAttributeValue(q.identifier,set);
//				IO.println("StructuredConst.outAtrset: symbol="+q.identifier+"  ==>  "+attrValue+" =============================================================");
//				if(attrValue==null) q.type.toDefaultSCode();
//				else
				if(attrValue.value==null) {
					q.type.toDefaultSCode(); attrValue.matched=true;
				} else {
					t2= ((Value)attrValue.value).doOutConst();
					if(t2 != q.type) {
						Type res=Type.checkCompatible(t2,q.type);
						if(res==null) ERROR("Missing type conversion: "+t2+" --> "+q.type);
					}
//					set.remove(attrValue); //n.out; ala'Simset !!!
					attrValue.matched=true;
				}
				sCode.outcode();
			}
//			IO.println("StructuredConst.outAtrset: AFTER symbol="+q.identifier+"  ==>  "+attrValue+" =============================================================");
		}
//		printAttrValueSet("StructuredConst.outAtrset: AFTER OUTPUT",set);
//		checkAndClearMatched(rec,set);
		return(attrValue);
	}

	private static void checkAndClearMatched(Record rec,Vector<AttributeValue> set) {
//		printAttrValueSet("StructuredConst.checkAndClearMatched: ",set);
		for(AttributeValue val:set) {
			if(!val.matched) {
				Util.println("Unmatched: "+val);
				IO.println("XX-Extra unmatched element(s) in constant "+val+" in RECORD "+rec);
				ERROR("XX-Extra unmatched element(s) in constant "+val+" in RECORD "+rec.identifier);
			}
			val.matched=false;
		}		
	}

	private static AttributeValue getAttributeValue(String attrid,Vector<AttributeValue> set) {
		if(set.isEmpty()) return(null);
		for(AttributeValue n:set) {
//			Util.TRACE("StructuredConst.outAtrset: Compere  "+n.ebox+"  <==>  "+symbol);
			if(n.ident.equalsIgnoreCase(attrid)) {
//				Util.TRACE("StructuredConst.outAtrset: GOT IT !!!  "+n.ebox+"  ==  "+symbol);
				return(n);
			}
		}
		Util.TRACE("StructuredConst.outAtrset: NOT FOUND !!!  "+attrid);
		return(null);
	}

	// ***********************************************************************************************
	// *** Editing:
	// ***********************************************************************************************

	public static void printAttrValueSet(String title,Vector<AttributeValue> set) {
		Util.println(title);
		for(AttributeValue val:set) {
			Util.println(val.toString());
		}
	}
	
	public String edAttrValues() {
		StringBuilder s=new StringBuilder();
		char dlm='(';
		if(attributeValues!=null) for(AttributeValue v:attributeValues) {
			s.append(dlm); dlm=',';
			s.append(v);
		}
		s.append(')');
		return(s.toString());
	}

	public String toString() {
		return ("StructuredConst: RECORD " + ident + '(' + edAttrValues() + ")");
	}


}
