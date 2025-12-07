package simuletta.compiler.declaration.scope;

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
import simuletta.compiler.declaration.Variant;
import simuletta.compiler.parsing.Parser;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

/**
 * Record Declaration.
 * 
 * <pre>
 * Syntax:
 * 
 * 		RecordDeclaration ::= RECORD record'identifier  < : prefix'identifier >  < RecordInfo >?
 * 							BEGIN CommonPart  < VariantPart >*  END
 * 
 * 			RecordInfo ::= INFO "DYNAMIC"  |  INFO "TYPE"
 * 
 * 			CommonPart ::= < AttributeDeclaration  >*
 * 
 * 			VariantPart ::= VARIANT  < AttributeDeclaration  >+
 * 
 * 				AttributeDeclaration ::= Type attrid  < , attrid >*
 * 
 * 					attrid ::= attribute'identifier  < ( repeat'IntegerNumber ) >?
 * 
 * S-Code:
 * 
 *		RecordDescriptor ::= RECORD recordTag:newtag < RecordInfo >?
 *							  < PrefixPart >? CommonPart
 *							  < AlternatePart >* ENDRECORD
 *
 *			RecordInfo ::= info "TYPE"   !   info "DYNAMIC"
 *
 *			PrefixPart ::= PREFIX ResolvedStructure
 *
 *			CommonPart ::= < AttributeDefinition >*
 *
 *			AlternatePart ::= ALT < AttributeDefinition >*
 *
 *			AttributeDefinition ::= ATTR attr:newtag QuantityDescriptor
 *
 *			ResolvedStructure ::= StructuredType < FIXREP count:ordinal >?
 *	
 *			StructuredType ::= recordTag:tag
 * 
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class Record extends DeclarationScope implements Externalizable {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
//	public Vector<Declaration> declarationList // From DeclarationScope
	public boolean used_as_type;
	public boolean dynamic;
	public boolean indefinite;
	public boolean packed;
	public String prefixIdentifier;
	public Vector<Variant> variantset=new Vector<Variant>();
	
	public Record prefix; // Set by doChecking
	public boolean sCodeWritten=false;

    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public Record(final boolean visibleflag,final String identifier) {
    	super(identifier,visibleflag);
    	defTag(visibleflag,identifier);
    }

	public static Record doParse(boolean visibleflag) {
		String symbval;
		Record rec=new Record(visibleflag,Parser.expectIdentifier());
		enterScope(rec);
//			IO.println("Record.doParse: rec.identifier="+rec.identifier+", visibleFlag="+visibleflag);
			rec.variantset=new Vector<Variant>();
			if(Parser.accept(KeyWord.COLON)) rec.prefixIdentifier=Parser.expectIdentifier();
			if(Parser.accept(KeyWord.INFO)) {
				symbval=Parser.expectString();
				if(symbval.equals("DYNAMIC")) rec.dynamic=true;
				else if(symbval.equals("TYPE")) rec.used_as_type=true;
				else if(symbval.equals("PACKED")) rec.packed=true;
				else ERROR("Unknown INFO-string");
			}
			Parser.expect(KeyWord.BEGIN);
			rec.declarationList = new Vector<Declaration>();
//			Parser.TRACE("Record.doParse: ATRSET(1) rec.symbol="+rec.symbol+", currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);
			rec.doParseAttributeDefinitions();
//			Parser.TRACE("Record.doParse: ATRSET(2) rec.symbol="+rec.symbol+", currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);
			while(Parser.accept(KeyWord.VARIANT)) {
				Variant variant=new Variant();
				rec.variantset.add(variant);
				variant.atrset=new Vector<Declaration>();
				Type type;
				while((type=Parser.acceptType()) != null) {
					do { // Parse Quant-List
						VariableDeclaration q=VariableDeclaration.doParse(type,rec.visible,false);
						variant.atrset.add(q);
					} while(Parser.accept(KeyWord.COMMA));
				}
			}
			if(Parser.expect(KeyWord.END));
			if(Option.TRACE_PARSE_BREIF) rec.print("",0);
			Parser.TRACE("Record.doParse: END rec.symbol="+rec.identifier+", currentToken="+Parser.currentToken+", prevToken="+Parser.prevToken);

			currentModule.checkDeclarationList();
//			rec.print(20);//("RESULTING RECORD ",20);
//			Util.BREAK("Record.doParse: rec="+rec);
		exitScope(rec);
		return(rec);
	}

    public void doParseAttributeDefinitions() {
        LOOP: while(true) {
        	Type type=Parser.acceptType();
        	if(type==null) {
        		Parser.TRACE("Grammar.declarations: NO MORE DECLARATIONS");
        		break LOOP;
        	}
        	// Parse AttrId-List
        	do { VariableDeclaration q=VariableDeclaration.doParse(type,visible,false);
        		this.declarationList.add(q);
        	} while(Parser.accept(KeyWord.COMMA));
        }
        }
	
	//%title *****   Access record attribute   *****

    public VariableDeclaration findAttribute(String attrid) {
    	for(Declaration d:declarationList) if(d.identifier.equalsIgnoreCase(attrid)) return((VariableDeclaration)d);
    	for(Variant v:variantset) {
    		//IO.println("Record.findAttribute: Searching variant "+v);
    		for(Declaration d:v.atrset) if(d.identifier.equalsIgnoreCase(attrid)) return((VariableDeclaration)d);
    	}
    	if(prefixIdentifier != null) {
    		Declaration d=Declaration.findMeaning(prefixIdentifier);
    		if(d instanceof Record) {
    			Record r=(Record)d;
    			return(r.findAttribute(attrid));
    		} else ERROR("Unknown prefix: " + prefixIdentifier);
    	}
    	VariableDeclaration q=new VariableDeclaration(false,null,attrid); // Error recovery
    	ERROR(attrid + " is not an attribute of " + this.identifier );
    	return(q);
    } 
	

	// ***********************************************************************************************
	// *** Utility: getPrefixRecord
	// ***********************************************************************************************
	public Record getPrefixRecord() {
//		Util.BREAK("RecordDeclaration("+identifier+").getPrefixRecord: prefix="+prefix);
		if (prefixIdentifier == null || prefixIdentifier.length()==0) return (null);
    	prefix=(Record) Declaration.findMeaning(prefixIdentifier);
		if(prefix == this) Util.ERROR("Record prefix chain loops");
		if(prefix.indefinite) Util.ERROR("Record with indefinite repetition can't be used as Prefix");
		return(prefix);
	}

	// ***********************************************************************************************
	// *** Utility: isSubRecordOf
	// ***********************************************************************************************
	/**
	 * Consider the Record definitions:
	 * 
	 * <pre>
	 *  
	 *      Record A   ......;
	 *      Record B:A ......;
	 *      Record C:B ......;
	 * </pre>
	 * 
	 * Then Record B is a subRecord of Record A, While Record C is subRecord of both B and A.
	 * 
	 * @param other
	 * @return Boolean true iff this Record is a subclass of the 'other' Record.
	 */
	public boolean isSubRecordOf(final Record other) {
//		Util.BREAK("RecordDeclaration.isSubRecordOf: this="+this);
//		Util.BREAK("RecordDeclaration.isSubRecordOf: other="+other);
		if(other==null) return(true); // Every Record is a subRecord of 'Object' 
		Record prefixRecord = getPrefixRecord();
		if (prefixRecord != null)
			do { if (other == prefixRecord) return(true);
			} while ((prefixRecord = prefixRecord.getPrefixRecord()) != null);
		return (false);
	}


	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		SET_SEMANTICS_CHECKED();
		enterScope(this);
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
			if(prefixIdentifier != null && prefixIdentifier.length()>0) {
            	prefix=(Record) Declaration.findMeaning(prefixIdentifier);
        		if(prefix.indefinite) ERROR("Record with Indefinite Attribute can't be used as Prefix "+prefix);
			}
			indefinite=false;
			for(Declaration a:declarationList) {
				VariableDeclaration q=(VariableDeclaration)a;
				VariableDeclaration prev=null;
				//Util.TRACE("Record("+this.identifier()+").doChecking: Attribute="+q+",indefinite="+indefinite);
				if(indefinite) ERROR("Indefinite rep is not ast attr of "+this+", prev="+prev);
				q.doChecking();
				if(q.count==0) indefinite=true;
				prev=q;
			}
			for(Variant v:variantset) {
				for(Declaration a:v.atrset) {
					VariableDeclaration q=(VariableDeclaration)a;
					//Util.TRACE("Record.doChecking(3): Attribute="+q+",indefinite="+indefinite);
					if(indefinite) {
						ERROR("Indefinite rep is not last attr of "+this);
					}
					q.doChecking();
					if(q.count==0) indefinite=true;
				}
			}
			sCodeWritten=true;
		exitScope(this);
		SET_SEMANTICS_CHECKED();
	}
	
	// ***********************************************************************************************
	// *** Coding: prepareSCodeOutput
	// ***********************************************************************************************
	public void prepareSCodeOutput() {
		for(Declaration a:declarationList) a.prepareSCodeOutput();
		for(Variant v:variantset) v.prepareSCodeOutput();
	}
	
	// ***********************************************************************************************
	// *** Coding: doSCodeDeclaration
	// ***********************************************************************************************
	public void doSCodeDeclaration() {
		if(IS_SCODE_EMITTED()) return;
		SET_SCODE_EMITTED();
		ASSERT_SEMANTICS_CHECKED(this);
		enterScope(this);
			output_S_LINE();
			if(prefix!=null) prefix.doSCodeDeclaration();
			prepareSCodeOutput();
			sCode.outinst(S_RECORD); sCode.outtagid(getTag());
			if(used_as_type) {
				sCode.outinst(S_INFO); sCode.outstring("TYPE"); sCode.outcode();
			} else if(packed) {
				sCode.outinst(S_INFO); sCode.outstring("PACKED"); sCode.outcode();
			} if(dynamic) {
				sCode.outinst(S_INFO); sCode.outstring("DYNAMIC"); sCode.outcode();
			}
			if(prefixIdentifier != null) {
		    	prefix=(Record) Declaration.findMeaning(prefixIdentifier);
				sCode.outinst(S_PREFIX); sCode.outtagid(prefix.getTag());
				sCode.outcode();
			}
			for(Declaration a:declarationList) outAttr((VariableDeclaration)a);
			for(Variant v:variantset) {
				sCode.outinst(S_ALT); sCode.outcode();
				for(Declaration a:v.atrset) outAttr((VariableDeclaration)a);
			}
			sCode.outinst(S_ENDRECORD); sCode.outcode();
		exitScope(this);
	}
	

    private void outAttr(VariableDeclaration q) {
        if(q.initval != null) ERROR("Initial value for attribute");
     	 q.doOutput(S_ATTR);
    }

	
    public void print(final String lead,final int indent) {
    	StringBuilder s=new StringBuilder(Util.edIndent(indent));
    	s.append("Line "+lineNumber+": ");
    	if(visible) s.append("VISIBLE ");
    	if(global) s.append("GLOBAL ");
    	s.append("RECORD ").append(identifier);
    	s.append(", tag=").append(getTag()).append(" ");
    	if(used_as_type) s.append("used_as_type ");
    	if(dynamic) s.append("dynamic ");
    	if(indefinite) s.append("indefinite ");
    	if(packed) s.append("packed ");
    	if(prefixIdentifier!=null) s.append(", prefixIdentifier=").append(prefixIdentifier).append(" ");
    	IO.println(s.toString()); s=new StringBuilder();
    	if(declarationList!=null) {
    		for(Declaration d:declarationList) d.print("",indent+1);
    	}
    	if(variantset!=null) {
    		for(Variant v:variantset) {
    			for(Declaration d:v.atrset)	d.print("VARIANT: ",indent+2);
    		}
    	}
    }
	
    public String toString() {
    	StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": ");
    	if(visible) s.append("VISIBLE ");
    	if(global) s.append("GLOBAL ");
    	s.append("record ").append(identifier);
    	s.append(", tag=").append(getTag()).append(" ");
    	if(used_as_type) s.append("used_as_type ");
    	if(dynamic) s.append("dynamic ");
    	if(indefinite) s.append("indefinite ");
    	if(packed) s.append("packed ");
    	if(prefixIdentifier!=null) s.append(", prefixIdentifier=").append(prefixIdentifier).append(" ");
    	if(declarationList!=null) {
    		s.append(" Atributes: ");
    		for(Declaration d:declarationList) s.append(d).append("  ");
    	}
    	if(variantset!=null) {
    		for(Variant v:variantset) {
    			s.append(" Variant: ");
    			for(Declaration d:v.atrset) s.append(d).append("  ");
    		}
    	}
    	return(s.toString());
    }

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	
    public Record() { }

//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Record: "+identifier);
//		oupt.writeByte(Kind.kRecord);
//		oupt.writeShort(lineNumber);
//		oupt.writeIdent(identifier);
//		getTag().writeTag(oupt);
//		oupt.writeBoolean(indefinite);
//		oupt.writeIdent(prefixIdentifier);
//		oupt.writeShort(declarationList.size()); // nDecl
//		for(Declaration decl:declarationList) decl.write(oupt);
//		oupt.writeShort(variantset.size()); // nVariant
//		for(Variant var:variantset) var.writeVariant(oupt);
//		//Util.TRACE_OUTPUT("END Write Record: "+identifier);
//	}
//
//	public static Record createAndReadRecord(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		Record rec=new Record();
//		rec.lineNumber=inpt.readShort();
//		rec.identifier=inpt.readIdent();
//		rec.setTag(inpt.readTag());
//		rec.indefinite=inpt.readBoolean();
//		rec.prefixIdentifier=inpt.readIdent();
//		int nDecl=inpt.readShort();
//		for(int i=0;i<nDecl;i++) rec.declarationList.add(Declaration.createAndRead(inpt));
//		int nVariant=inpt.readShort();
//		for(int i=0;i<nVariant;i++) rec.variantset.add(Variant.createAndReadVariant(inpt));
//		rec.SET_SCODE_EMITTED();
//		Util.TRACE_INPUT("END Read Record: "+rec);
//		return(rec);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Record: "+identifier);
		out.writeShort(lineNumber);
		out.writeObject(identifier);
		getTag().writeTag(out);
		out.writeBoolean(indefinite);
		out.writeObject(prefixIdentifier);
		out.writeObject(declarationList);
		out.writeObject(variantset);
		//Util.TRACE_OUTPUT("END Write Record: "+identifier);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		lineNumber=in.readShort();
		identifier=(String) in.readObject();
		setTag(Tag.readTag(in));
		indefinite=in.readBoolean();
		prefixIdentifier=(String) in.readObject();
		declarationList=(Vector<Declaration>) in.readObject();
		variantset=(Vector<Variant>) in.readObject();
		SET_SCODE_EMITTED();
		Util.TRACE_INPUT("END Read Record: "+this);
	}

}
