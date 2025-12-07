/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.type;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.VariableDeclaration;
import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.expression.value.AttributeValue;
import simuletta.compiler.expression.value.StructuredConst;
import simuletta.compiler.parsing.Parser;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * 
 * @author Øystein Myhre Andersen
 *
 */
public class Type implements Externalizable {
	protected int keyWord;
	public int getKeyWord() { return(keyWord); }

//	public static final Type Void = new Type(0);
	public static final Type Integer = new Type(KeyWord.INTEGER);
//	public static final Type Range = new Type(KeyWord.RANGE);
	public static final Type Range(int lower,int upper) { return (new RangeType(KeyWord.RANGE,lower,upper)); }
	public static final Type Real = new Type(KeyWord.REAL);
	public static final Type LongReal = new Type(KeyWord.LONG);
	public static final Type Boolean = new Type(KeyWord.BOOLEAN);
	public static final Type Character = new Type(KeyWord.CHARACTER);
	public static final Type Label = new Type(KeyWord.LABEL);
	public static final Type Destination = new Type(KeyWord.LABEL);
	public static final Type Size = new Type(KeyWord.SIZE);
	
	public static final Type Entry = new Type(KeyWord.ENTRY);
	public static final Type Entry(String qual) { return (new EntryType(qual)); }
	public static final Type Field = new FieldType(null);
	public static final Type Field(Type type) { return (new FieldType(type)); }
//	public static final Type Infix = new Type(KeyWord.INFIX);
	public static final Type Infix(String qual,int repCount) { return (new InfixType(qual,repCount)); }
	public static final Type Ref = new RefType(null);
	public static final Type Ref(String qual) { return (new RefType(qual)); }
	public static final Type Name = new NameType(null);
	public static final Type Name(Type type) { return (new NameType(type)); }
	
	public static final Type StringType=Infix("string",1);


	// ***********************************************************************************************
	// *** Parsing: Type.parse
	// ***********************************************************************************************
	/**
	 * <pre>
	 * Syntax:
	 * 
	 * 		Type
	 * 		   ::= INTEGER 
	 * 		   ::= SHORT INTEGER 
	 * 		   ::= RANGE ( lower'IntegerNumber  :  upper'IntegerNumber ) 
	 * 		   ::= REAL 
	 * 		   ::= LONG REAL 
	 * 		   ::= SIZE 
	 * 		   ::= BOOLEAN 
	 * 		   ::= CHARACTER 
	 * 		   ::= LABEL 
	 * 		   ::= ENTRY ( <Profile'Identifier>? ) 
	 * 		   ::= INFIX ( Record'identifier  < : fixrep'IntegerNumber >? ) 
	 * 		   ::= REF ( Record'identifier ) 
	 * 		   ::= FIELD ( < qualifying'Type >? ) 
	 * 		   ::= NAME ( < qualifying'Type >? ) 
	 * 		   ::= Record'Identifier
	 * 
	 * </pre>
	 */
	public static Type parse() {
    	Type type=null; //Type.Notype;
//    	Util.BREAK("Type.parse: currentToken="+Parser.currentToken);
    	if(Parser.accept(KeyWord.BOOLEAN)) type=Type.Boolean;
    	else if(Parser.accept(KeyWord.CHARACTER)) type=Type.Character;
    	else if(Parser.accept(KeyWord.INTEGER)) type=Type.Integer;
		else if(Parser.accept(KeyWord.SHORT)) { Parser.expect(KeyWord.INTEGER); type=Type.Integer; }
		else if(Parser.accept(KeyWord.REAL))  type=Type.Real;
		else if(Parser.accept(KeyWord.LONG))  { Parser.expect(KeyWord.REAL); type=Type.LongReal; }
		else if(Parser.accept(KeyWord.SIZE))  type=Type.Size;
		else if(Parser.accept(KeyWord.LABEL)) type=Type.Label;
		else if(Parser.accept(KeyWord.REF))   type=Type.Ref(Parser.expectParantesedIdentifier()); 
		else if(Parser.accept(KeyWord.ENTRY)) type=Type.Entry(Parser.expectParantesedIdentifier()); 
		else if(Parser.accept(KeyWord.NAME))  type=Type.Name(acceptParantesedType()); 
		else if(Parser.accept(KeyWord.FIELD)) type=Type.Field(acceptParantesedType()); 
		else if(Parser.accept(KeyWord.INFIX))	{
			Parser.expect(KeyWord.BEGPAR);
			String classIdentifier=Parser.acceptIdentifier();
			int repCount=1;
			if(Parser.accept(KeyWord.COLON)) {
				repCount=Parser.expectIntegerConst().intValue()+1;
			}
			Parser.expect(KeyWord.ENDPAR); 
			type=Type.Infix(classIdentifier,repCount); 
		}
		else if(Parser.accept(KeyWord.RANGE))	{
			Parser.expect(KeyWord.BEGPAR);
			int lower=Parser.expectIntegerConst().intValue();
			Parser.expect(KeyWord.COLON); 
			int upper=Parser.expectIntegerConst().intValue();
			Parser.expect(KeyWord.ENDPAR); 
			type=Type.Range(lower,upper); 
		}
//		IO.println("Type.parse: "+type);
		return(type);  
    }

	private static Type acceptParantesedType() {
		Parser.expect(KeyWord.BEGPAR);
		Type type=Parser.acceptType();
		Parser.expect(KeyWord.ENDPAR); 
		return(type);
	}
	
	// ***********************************************************************************************
	// *** Constructors
	// ***********************************************************************************************
	public Type() {} // Externalization
	  
	public Type(int keyWord) {
		this.keyWord=keyWord;
//		if(keyWord==KeyWord.INFIX) Util.IERR("");
	}
	
	private boolean CHECKED=false; // Set true when doChecking is called
	public void doChecking() {
		if (CHECKED) return;
		if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
		if(this.isReferenceType()) {
	    	Record rec=this.getQualifyingRecord();
	    	if(rec!=null) rec.doChecking();
		} else if(this.isNameType() || this.isFieldType()) {
			Type type=qualifyingType();
			if(type!=null) type.doChecking();
		}
		CHECKED=true;
	}
	

	// ***********************************************************************************************
	// *** getQualifyingRecord
	// ***********************************************************************************************
//	public RecordDeclaration getQualifyingRecord() {
//		RecordDeclaration qualifyingRecord=null; // Qual in case of ref(Qual) type; or infix(Qual) type;
//		if(isReferenceType()) {
//			String refIdent=getRefIdent();
//			if(refIdent!=null) {
//				if(!refIdent.equals("LABQNT$") && !refIdent.equals("Object")) {
//					Declaration decl=Global.getCurrentScope().find(refIdent);
//					if(decl instanceof RecordDeclaration) {
//						qualifyingRecord=(RecordDeclaration)decl;
//					} else Util.error("Illegal Type: "+this.toString()+" - "+refIdent+" is not a Record");
//				}
//			}
//		}
//		return (qualifyingRecord);
//	}
	public Record getQualifyingRecord() {
		Record qualifyingRecord=null; // Qual in case of ref(Qual) type; or infix(Qual) type;
		if(isReferenceType()) {
			String refIdent=getRefIdent();
			if(refIdent==null) {
				Util.BREAK("Record.getQualifyingRecord: refIdent="+refIdent);
			}
			if(refIdent!=null && refIdent.length()>0) {
				if(Option.TRACE_FIND_MEANING) Util.BREAK("Record.getQualifyingRecord: refIdent="+refIdent);
				Declaration dentry=Declaration.findMeaning(refIdent);
				if(dentry==null) IERR("Missing declaration of "+refIdent);
				if(Option.TRACE_FIND_MEANING) Util.BREAK("Record.getQualifyingRecord: dentry="+dentry);//+", QUAL="+dentry.getClass().getSimpleName());
				if(dentry instanceof VariableDeclaration) {
					VariableDeclaration q=(VariableDeclaration)dentry;
					Type tt=q.type;
					if(tt.isInfixType()) {
//						Util.BREAK("Record.getQualifyingRecord: infixType tt="+tt);
						Record rec=tt.getQualifyingRecord();
//						Util.BREAK("Record.getQualifyingRecord: infixType rec="+rec);
						return(rec);
					}
				}
//				if(!(dentry instanceof Record)) dentry=get_rec(idx);
//				if(!(dentry instanceof Record)) dentry=Declaration.getRecord(refIdent);
//				qualifyingRecord=(Record)dentry;
				if(dentry instanceof Record rec) qualifyingRecord=rec;
				else IERR("");
			}
		} else if(this.isNameType()) {
			Util.TRACE("Record.getQualifyingRecord: NameType="+this);
//			Util.STOP();
		}
			
		Util.TRACE("Record.getQualifyingRecord: "+this+"  -->  "+qualifyingRecord);
		return (qualifyingRecord);
	}

	// ***********************************************************************************************
	// *** qualifyingType
	// ***********************************************************************************************
	public Type qualifyingType() {
	    if(this instanceof FieldType f) return(f.type);
	    if(this instanceof NameType  n) return(n.type);
		return (null);
	}
	
	public String getRefIdent() {
	    if(this instanceof RefType r) return(r.qual);
	    if(this instanceof InfixType x) return(x.qual);
		return(null); 
	}

	public boolean isIntegerType() {
		return(keyWord==KeyWord.INTEGER||keyWord==KeyWord.RANGE); }

	public boolean isRealType() {
		return(keyWord==KeyWord.REAL||keyWord==KeyWord.LONG); }

	public boolean isArithmeticType() {
		return(keyWord==KeyWord.INTEGER||keyWord==KeyWord.REAL
			 ||keyWord==KeyWord.LONG||keyWord==KeyWord.RANGE); }

    public Type arith_type() {
    	int key=getKeyWord();
    	if(key==KeyWord.INTEGER) return(Type.Integer);
    	if(key==KeyWord.RANGE) return(Type.Integer);
    	if(key==KeyWord.REAL) return(Type.Real);
    	if(key==KeyWord.LONG) return(Type.LongReal);
    	ERROR("Non-arithmetic type: " + this);
    	Type.tstconv(this,Type.Integer);
    	return(Type.Integer);
    }


	public boolean isCharacterType() {
		return(keyWord==KeyWord.CHARACTER); }

	public boolean isObjectReferenceType() {
		if(keyWord==KeyWord.REF) return(true);
		return(getRefIdent()!=null);
	}
	  
	public boolean isRefType() {
		if(keyWord==KeyWord.REF) return(true);
		return(false);
	}
  
	public boolean isReferenceType() {
		if(keyWord==KeyWord.REF) return(true);
		if(keyWord==KeyWord.INFIX) return(true);
//		if(this.equals(Type.String)) return(true);
//		return(getRefIdent()!=null);
		return(false);
	}
	  
	public boolean isInfixType() {
		if(keyWord==KeyWord.INFIX) return(true);
		return(false);
	}
	  
	public boolean isSizeType() {
		if(keyWord==KeyWord.SIZE) return(true);
		return(false);
	}
	  
	public boolean isFieldType() {
		if(keyWord==KeyWord.FIELD) return(true);
		//return(getRefIdent()!=null);
		return(false);
	}
	  
	public boolean isNameType() {
		if(keyWord==KeyWord.NAME) return(true);
		//return(getRefIdent()!=null);
		return(false);
	}
	  
	public boolean isEntryType() {
		if(keyWord==KeyWord.ENTRY) return(true);
		//return(getRefIdent()!=null);
		return(false);
	}
  
	@Override
	public boolean equals(final Object other) {
		if(other==null) return(false);
		int otherKey=((Type)other).keyWord;  
		return(keyWord==otherKey);
	}
	  
//	public boolean equals(Object other) {
//		if(other !=null && other instanceof Type tp2) {
//			return(key.equals(tp2.key));
//		} else return(false);
//	}
  
  
	// ***********************************************************************************************
	// *** isConvertableTo
	// ***********************************************************************************************
	/**
     * Checks if a type-conversion is legal.
     * <p>
     * The possible return values are:
     * <ul>
     * <li>DirectAssignable - No type-conversion is necessary. E.g. integer to integer
     * <li>ConvertValue - Type-conversion with possible Runtime check is necessary. E.g. real to integer.
     * <li>ConvertRef - Type-conversion with Runtime check is necessary. E.g. ref(A) to ref(B) where B is a subclass of A.
     * <li>Illegal - Conversion is illegal
     */
    public enum ConversionKind { Illegal, DirectAssignable, ConvertValue, ConvertRef }
    public ConversionKind isConvertableTo(final Type to) {
//    	Util.BREAK("Type.isConvertableTo: this="+this+", to="+to);
	    ConversionKind result;
	    if(to==null) result=ConversionKind.Illegal;
	    else if(to.isNameType()) result=ConversionKind.ConvertRef;
	    else if(to.isEntryType()) result=ConversionKind.ConvertRef;
	    else if(to.isFieldType()) result=ConversionKind.ConvertRef;
	    else if(this.equals(to)) result=ConversionKind.DirectAssignable;
	    else if(this.isArithmeticType() && to.isArithmeticType()) result=ConversionKind.ConvertValue;
//	    else if(this==Type.String && to.isReferenceType()) result=ConversionKind.ConvertValue;
//	    else if(this.isSubReferenceOf(to)) result=ConversionKind.DirectAssignable;  
	    else if(this.isSubReferenceOf(to)) result=ConversionKind.ConvertRef;  
	    else if(to.isSubReferenceOf(this)) result=ConversionKind.ConvertRef; // Needs Runtime-Check
//	    else if(to.isNameType()) result=ConversionKind.ConvertRef;
//	    else if(to.isEntryType()) result=ConversionKind.ConvertRef;
	    else result=ConversionKind.Illegal;
	    return(result); 
    }
  
	// ref(B) is a sub-reference of ref(A) iff B is a subclass of A
	// any ref is a sub-reference of NONE, NONAME, ...
	public boolean isSubReferenceOf(final Type other) {
		String thisRef=this.getRefIdent(); // May be null for NONE
		String otherRef=other.getRefIdent(); // May be null for NONE
		boolean result=false;
//		Util.BREAK("Type.isSubReferenceOf: thisType="+this+", otherType="+other);
//		Util.BREAK("Type.isSubReferenceOf: thisRef="+thisRef+", otherRef="+otherRef);
		if(otherRef==null) result=false;  // No ref is a super-reference of NONE
		else if(thisRef==null) result=true; // Any ref is a sub-reference of NONE
		else {
			Record thisQual=this.getQualifyingRecord();
			Record otherQual=other.getQualifyingRecord();
			if(thisQual==null) result=false; // Error Recovery
			else result=(thisQual).isSubRecordOf(otherQual);
		}
		return(result); 
	}
  
	
    public static void convert(Type t1,Type t2) {
//    	tstconv(t1,t2);
//    	if(checkCompatible(t1,t2)!=null) {
    	if(!t1.equals(t2)) {
    		sCode.outinst(S_CONVERT); sCode.outtype(t2);
    		sCode.outcode();
    	}
    } //*** convert ***;

    public static void tstconv(Type t1,Type t2) {
    	if(checkCompatible(t1,t2)==null)
    		ERROR("Missing type conversion: " + t1 + " => " + t2);
    } // *** tstconv ***;

	
	public static Type checkCompatible(final Type type1,final Type type2) {
		Util.TRACE("Type.checkCompatible: type1="+type1+", type2="+type2);
		if(type1==null) {
//			if(type2.isRefType()) return(type2);
//			if(type2.isNameType()) return(type2);
//			if(type2.isSizeType()) return(type2);
//			return(null); // NUL Type 
			return(type2); // NUL Type 
		}
//		if(type2==null) return(null); // NUL Type 
		int key1=type1.keyWord;
		int key2=type2.keyWord;
		if(key1==KeyWord.INTEGER)	{
			if(key2==KeyWord.INTEGER) return(Type.Integer); 
			if(key2==KeyWord.RANGE) return(Type.Integer); 
		}
		else if(key1==KeyWord.RANGE) {
			if(key2==KeyWord.INTEGER) return(Type.Integer); 
			if(key2==KeyWord.RANGE) return(Type.Integer); 
		}
		else if(key1==KeyWord.REAL) {
			if(key2==KeyWord.REAL) return(Type.Real);
			if(key2==KeyWord.LONG) return(Type.LongReal);
		}
		else if(key1==KeyWord.LONG) {
			if(key2==KeyWord.REAL) return(Type.LongReal);
			if(key2==KeyWord.LONG) return(Type.LongReal);
		}
		else if(key1==KeyWord.CHARACTER) {
			if(key2==KeyWord.CHARACTER) return(Type.Character);
		}
		else if(type1 instanceof InfixType infx) {
//			IO.println("Type.checkCompatible: infx="+infx);
			if(infx.qual.equals("STRING")) {
				if(key2==KeyWord.CHARACTER) return(Type.Character);
			}
		}
		else if(type1.equals(type2)) return(type1);

//		if(type1.isInfixType() && type2.isInfixType()) {
//			Util.BREAK("Type.checkCompatible: InfixType:  type1="+type1+", type2="+type2);
//			if(type1.isSubReferenceOf(type2)) return(type2);
//		    if(type2.isSubReferenceOf(type1)) return(type1);
//		    return(type1); // All  ref(record'identifier)  are compatible in Simuletta
//		}

		if(type1.isReferenceType() && type2.isReferenceType()) {
			if(type1.isSubReferenceOf(type2)) return(type2);
		    if(type2.isSubReferenceOf(type1)) return(type1);
		    return(type1); // All  ref(record'identifier)  are compatible in Simuletta
		}

		if(type1.isFieldType() && type2.isFieldType()) {
			Type qType1=((FieldType)type1).type;
			Type qType2=((FieldType)type2).type;
			if(qType1==null) return(type2);
			if(qType2==null) return(type1);
			return(new FieldType(checkCompatible(qType1,qType2)));
		}

		if(type1.isNameType() && type2.isNameType()) {
			Type qType1=((NameType)type1).type;
			Type qType2=((NameType)type2).type;
			if(qType1==null) return(type2);
			if(qType2==null) return(type1);
			return(new NameType(checkCompatible(qType1,qType2)));
		}


		if(type1.isEntryType() && type2.isEntryType()) {
//			String q1=((EntryType)type1).qual;
//			String q2=((EntryType)type2).qual;
			String q1=type1.getQual();
			String q2=type2.getQual();
			if(q1==null) return(type2);
			if(q2==null) return(type1);
			if(q1.equals(q2)) return(type1);
		}

		return(null);  
	}
	
	public String getQual() {
		if(this instanceof RefType ref) return(ref.qual);
		if(this instanceof EntryType entr) return(entr.qual);
		return(null);
	}
	

	public String edDefaultValue() {
		switch(getKeyWord()) {
			case KeyWord.INTEGER:
			case KeyWord.RANGE:		return("0");
			case KeyWord.SIZE:		return("0");
			case KeyWord.CHARACTER: return("0");
			case KeyWord.REAL:		return("0.0f");
			case KeyWord.LONG:		return("0.0d");
			case KeyWord.BOOLEAN:	return("false");
			case KeyWord.INFIX:		return("new "+((InfixType)this).qual+"()");
//			case KeyWord.STRING:	return("null");
			case KeyWord.LABEL:		return("null");
			case KeyWord.REF:		return("null");
			case KeyWord.NAME:		return("null");
			case KeyWord.FIELD:		return("null");
			case KeyWord.ENTRY:		return("null");
		default: 
			Util.FATAL_ERROR("NullValue.edValue: IMPOSIBLE !!! "+this);
			return("null"); // All other cases			
		}
	}

  
	public String toJavaType() {
		if(keyWord==0) return("void");
	    //if(this.equals(Array)) return("array"); // ARRAY Elements 
		if(this.equals(LongReal)) return("double");
		if(this.equals(Real)) return("float");
		if(this.equals(Integer)) return("int");
		if(this.equals(Boolean)) return("boolean");
		if(this.equals(Character)) return("char");
//		if(this.equals(String)) return("TXT$");
//		if(this.equals(String)) return("String");
		if(this.equals(Size)) return("int");
		if(this.equals(Label)) return("LABQNT$");
		if(keyWord==KeyWord.ENTRY) return("ENTRY");
		if(keyWord==KeyWord.RANGE) return("int");
		if(keyWord==KeyWord.REF)   return(((RefType)this).qual);
		if(keyWord==KeyWord.INFIX) return(((InfixType)this).qual);
		if(keyWord==KeyWord.NAME) {
//			Object val=key.getValue();
//			if(val==null) return("Name"); // Generic Type Name
//			Type type=(Type)val;
//			String res="Name<"+type.toJavaTypeClass()+">";
			String res="Name<?>";
			return(res);
		}
		if(keyWord==KeyWord.FIELD) {
			if(this instanceof FieldType field)
				 return("Field<"+field.type.toJavaTypeClass()+">");
			else return("Field"); // Generic Type Field
		}
		return(this.toString());
	}
	 
	public String toJavaTypeClass() {
		if(keyWord==0) return("void");
	    //Util.BREAK("Type.toJavaTypeClass: key="+key);
		if(this.equals(LongReal)) return("Double");
		if(this.equals(Real)) return("Float");
		if(this.equals(Integer)) return("Integer");
		if(this.equals(Boolean)) return("Boolean");
		if(this.equals(Character)) return("Character");
//		if(this.equals(String)) return("TXT$");
//		if(this.equals(String)) return("String");
		if(keyWord==KeyWord.RANGE) return("Integer");
		if(keyWord==KeyWord.REF)   return(((RefType)this).qual);
		if(keyWord==KeyWord.INFIX) return(((InfixType)this).qual);
		return(this.toString());
	}
	
	
	public static final int TAG_BOOL=  1, TAG_CHAR=  2, TAG_INT=   3,
		      TAG_SINT=  4, TAG_REAL=  5, TAG_LREAL= 6,
		      TAG_AADDR= 7, TAG_OADDR= 8, TAG_GADDR= 9,
		      TAG_PADDR=10, TAG_RADDR=11, TAG_SIZE= 12;

	public void toSCode() {
		//if(key==null) return("void");
		//if(this.equals(Array)) return("array"); // ARRAY Elements 
		if(this.equals(Type.LongReal)) sCode.uttag(TAG_LREAL);
		if(this.equals(Type.Real)) sCode.uttag(TAG_REAL);
		if(this.equals(Type.Integer)) sCode.uttag(TAG_INT);
		if(this.equals(Type.Boolean)) sCode.uttag(TAG_BOOL);
		if(this.equals(Type.Character)) sCode.uttag(TAG_CHAR);
		//			if(this.equals(Type.String)) return("TXT$");
		//			if(this.equals(Type.String)) return("String");
		if(this.equals(Type.Size)) sCode.uttag(TAG_SIZE);
		if(this.equals(Type.Label)) sCode.uttag(TAG_PADDR);
		if(this.getKeyWord()==KeyWord.ENTRY) sCode.uttag(TAG_RADDR);
		if(this.getKeyWord()==KeyWord.RANGE) {
			RangeType range=(RangeType)this;
			sCode.uttag(TAG_INT);	sCode.outinst(S_RANGE);
			sCode.outnumber(range.lower);
			sCode.outnumber(range.upper);
		}
		if(this.getKeyWord()==KeyWord.REF) sCode.uttag(TAG_OADDR);
		if(this.getKeyWord()==KeyWord.INFIX) {
			InfixType infix=(InfixType)this;
			Record rec=(Record) Declaration.findMeaning(infix.qual);
			if(!rec.IS_SCODE_EMITTED()) ERROR("Infix record is not written yet: "+rec.identifier);
			sCode.outtag(rec.getTag());
			//IO.println("Type.toSCode: INFIX qual="+qual+", value="+key.getValue()+", value2="+key.getVal2());
			//Util.BREAK("Type.toSCode: INFIX qual="+qual+", value="+key.getValue()+", value2="+key.getVal2());
			int info1=(int)infix.repCount;
			if(info1>1) { sCode.outinst(S_FIXREP); sCode.outnumber(info1-1); }
		}
		if(this.getKeyWord()==KeyWord.NAME) sCode.uttag(TAG_GADDR);
		if(this.getKeyWord()==KeyWord.FIELD) sCode.uttag(TAG_AADDR);
	}
	
	public void toDefaultSCode() {
		if(this.equals(Type.LongReal)) { sCode.outinst(S_C_LREAL); sCode.outstring("0"); }
		if(this.equals(Type.Real))     { sCode.outinst(S_C_REAL); sCode.outstring("0"); }
		if(this.equals(Type.Integer))  { sCode.outinst(S_C_INT); sCode.outstring("0"); }
		if(this.equals(Type.Boolean)) { sCode.outinst(S_FALSE); }
//		if(this.equals(Type.Character)) { sCode.outinst(S_C_CHAR); sCode.outbyt((int)(' ')); }
		if(this.equals(Type.Character)) { sCode.outinst(S_C_CHAR); sCode.outbyt(0); }
		if(this.equals(Type.Size)) { sCode.outinst(S_NOSIZE); }
		if(this.equals(Type.Label)) { sCode.outinst(S_NOWHERE); }
		if(this.getKeyWord()==KeyWord.ENTRY) { sCode.outinst(S_NOBODY); }
		if(this.getKeyWord()==KeyWord.RANGE)  { sCode.outinst(S_C_INT); sCode.outstring("0"); }
		if(this.getKeyWord()==KeyWord.REF) { sCode.outinst(S_ONONE); }
		if(this.getKeyWord()==KeyWord.INFIX) {

			String qual=this.getRefIdent();
//			Record rec=getRecord(qual);
			Record rec=(Record) Declaration.findMeaning(qual);

//			IO.println("Type.toDefaultSCode: code="+rec.getTag().getCode());
//			if(rec.getTag().getCode()==94) {
//				Util.STOP();
//			}
			sCode.outinst(S_C_RECORD);
			sCode.outtag(rec.getTag());
			sCode.outcode();
			StructuredConst.outstruct(rec,new Vector<AttributeValue>());
			sCode.outinst(S_ENDRECORD);
		}
		if(this.getKeyWord()==KeyWord.NAME) { sCode.outinst(S_GNONE); }
		if(this.getKeyWord()==KeyWord.FIELD) { sCode.outinst(S_ANONE); }
	}
	  
	@Override
	public String toString() {
		if(keyWord==0) return("null");
		return(KeyWord.ed(keyWord));
	}
	
//	public static Type inType(ObjectInput inpt) throws IOException, ClassNotFoundException {
//		Type tp=(Type)inpt.readObject();
//		if(tp==null) return(null);
//		int key=tp.keyWord;
//		if(key==KeyWord.INTEGER) return(Type.Integer);
//		if(key==KeyWord.REAL) return(Type.Real);
//		if(key==KeyWord.LONG) return(Type.LongReal);
//		if(key==KeyWord.BOOLEAN) return(Type.Boolean);
//		if(key==KeyWord.CHARACTER) return(Type.Character);
////		if(key==KeyWord.STRING) return(Type.String);
//		if(key==KeyWord.LABEL) return(Type.Label);
//		return(tp);
//
//	}
	
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	
//	public static void writeNullType(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write NullType:");
//		oupt.writeByte(Kind.kType);
//		oupt.writeByte(0);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);		
//	}
//	
//	public void writeType(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
//		oupt.writeByte(Kind.kType);
//		oupt.writeByte(keyWord);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//	
//	public static Type readType(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		int kind=inpt.readUnsignedByte();
//		switch(kind) {
//			case Kind.kType:
//				int keyWord=inpt.readUnsignedByte();
//				if(keyWord==0) return(null); 
//				Type type=new Type();
//				type.keyWord=keyWord;
//				return(type);
//			case Kind.kRange: return(RangeType.readRangeType(inpt));
//			case Kind.kRef:   return(RefType.  readRefType(inpt));
//			case Kind.kInfix: return(InfixType.readInfixType(inpt));
//			case Kind.kEntry: return(EntryType.readEntryType(inpt));
//			case Kind.kField: return(FieldType.readFieldType(inpt));
//			case Kind.kName:  return(NameType. readNameType(inpt));
//		}
//		Util.IERR("");
//		return(null);
//	}
//	
//	public static void writeType(Type type,ObjectOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Type: "+type);
//		oupt.writeShort(Kind.kType);
//		if(type!=null) {
//			oupt.writeByte(type.keyWord);	
//			if(type.keyWord==KeyWord.INFIX) Util.IERR("");
//		} else oupt.writeByte(0);
//		//Util.TRACE_OUTPUT("END Write Type: "+this);
//	}
//
//	public void writeType(ObjectOutput oupt) throws IOException {
//		Util.IERR("Missing redefinition of 'writeType' in "+this.getClass().getSimpleName());
//	}
//	
//	public static Type readType(ObjectInput inpt) throws IOException, ClassNotFoundException {
//		int kind=inpt.readShort();
//		switch(kind) {
//			case Kind.kType:
//				int keyWord=inpt.readUnsignedByte();
//				if(keyWord==0) return(null); 
//				Type type=new Type();
//				type.keyWord=keyWord;
//				if(type.keyWord==KeyWord.INFIX) Util.IERR("");
//				return(type);
//			case Kind.kRange: return(RangeType.readRangeType(inpt));
//			case Kind.kRef:   return(RefType.  readRefType(inpt));
//			case Kind.kInfix: return(InfixType.readInfixType(inpt));
//			case Kind.kEntry: return(EntryType.readEntryType(inpt));
//			case Kind.kField: return(FieldType.readFieldType(inpt));
//			case Kind.kName:  return(NameType. readNameType(inpt));
//		}
//		Util.IERR("");
//		return(null);
//	}


	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Type: "+this);
		out.writeByte(keyWord);
		//Util.TRACE_OUTPUT("END Write Type: "+this);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		keyWord=in.readUnsignedByte();
	}

}
