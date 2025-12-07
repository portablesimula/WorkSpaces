/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
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
import simuletta.compiler.declaration.Signatur;
import simuletta.compiler.parsing.Parser;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Util;

/**
 * Routine Declaration.
 * 
 * <pre>
 * Syntax:
 * 
 * 		SingularRoutine ::= ROUTINE Routine'Identifier ParameterSpecification  RoutineBody
 * 
 * 			RoutineBody ::= BEGIN <LocalVariable>*  <Statement>* END
 * 
 * 				LocalVariable ::= Type localid < , localid >*
 * 
 * 
 * S-Code:
 * 
 * 		PeculiarDeclaration ::= SingularRoutine  |  PeculiarRoutineSpecification
 * 
 * 			SingularRoutine ::= RoutineProfile  RoutineSpecification
 *
 *				RoutineProfile ::= PROFILE profile:newtag
 *									< ImportDefinition >* < Export  |  Exit >?
 *								   ENDPROFILE
 *
 *				RoutineSpecification ::= ROUTINESPEC body:newtag profile:tag
 * 
 * 
 * 
 * 			PeculiarRoutineSpecification ::= RoutineProfile
 * 
 *			PeculiarProfile ::= PROFILE profile:newtag < Peculiar >?
 *								< ImportDefinition >* < Export  |  Exit >?
 *							   ENDPROFILE
 *
 *			Peculiar ::= KNOWN body:newtag kid:string
 *					  |  SYSTEM body:newtag sid:string
 *					  |  EXTERNAL body:newtag nature:string xid:string
 *					  |  INTERFACE pid:string
 *  
 *			ImportDefinition ::= IMPORT parm:newtag QuantityDescriptor
 *  
 *			Export ::= EXPORT parm:newtag ResolvedType
 *			Exit   ::= EXIT return:newtag
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class RoutineDeclaration extends DeclarationScope implements Externalizable {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
//	public Vector<Declaration> declarationList // From DeclarationScope
	public int kind; // SYSROUTINE/KNOWN/EXTERNAL/ROUTINE
    public Signatur signatur;
    public String info;
	public Tag bodyTag; // NOTE: Simula FEC assumes that  bodyTag.code = profileTag.code + 1    // TESTING BODYTAG
    public RoutineBody body; // NOT saved in AttributeFile

    public RoutineDeclaration(final boolean visibleflag,final int kind,final String identifier) {
    	super(identifier,visibleflag);
    	this.kind=kind;
    	defTag(visibleflag,identifier);
    	bodyTag=Tag.newTag(visible,identifier+":body",""); // TESTING BODYTAG
    }
    
    public static RoutineDeclaration doParse(Vector<Declaration> declset,boolean visibleflag,int kind) {
        if(!currentScope.isCurrentModule()) ERROR("Misplaced routine declaration");
        String info="";
		if(Parser.accept(KeyWord.BEGPAR)) {
        	info=Parser.expectString();
        	Parser.expect(KeyWord.ENDPAR);
        }
		String identifier=Parser.expectIdentifier();
        RoutineDeclaration routine=new RoutineDeclaration(visibleflag,kind,identifier);
        
        routine.info=info;
		enterScope(routine);
			routine.signatur=Signatur.doParse(visibleflag);
			if(routine.kind==KeyWord.KNOWN || routine.kind==KeyWord.ROUTINE) {
				routine.body=RoutineBody.doParse(visibleflag,routine);
			} else {
//				routine.body=new RoutineBody(visibleflag,routine); // Empty body
				routine.body=new RoutineBody(visibleflag,routine,routine.bodyTag); // Empty body
			}
			declset.add(routine.body);
			routine.bodyTag=routine.body.getTag();
			Parser.expect(KeyWord.END);
			if(Option.TRACE_PARSE_BREIF) routine.print("",0);
			currentModule.checkDeclarationList();
		exitScope(routine);
        return(routine);
    }

    public Tag getBodyTag() {
    	return(bodyTag);
    }

	public Declaration findLocalMeaning(String ident) {
		if(Option.TRACE_FIND_MEANING) Util.TRACE("Routine.findLocalMeaning: "+ident);
		if(declarationList!=null) for(Declaration d:declarationList) {
			if(Option.TRACE_FIND_MEANING) Util.TRACE("Routine.findLocalMeaning: CHECKING "+d);
			if(d.identifier.equalsIgnoreCase(ident)) {
				if(Option.TRACE_FIND_MEANING) Util.TRACE("Routine.findLocalMeaning: "+ident+"   FOUND in "+this+": "+d);
				return(d);
			}
		}
		return(signatur.findLocalMeaning(ident));
	}

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterScope(this);
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
			signatur.doChecking();
			if(declarationList!=null) for(Declaration d:declarationList) d.doChecking();
			if(body!=null) body.doChecking();
		exitScope(this);
		SET_SEMANTICS_CHECKED();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeSpecCode
	// ***********************************************************************************************
    public void doSCodeSpecCode() {
		ASSERT_SEMANTICS_CHECKED(this);
		enterScope(this);
		if(kind==KeyWord.ROUTINE) {
			sCode.outinst(S_ROUTINESPEC); sCode.outtagid(getBodyTag());
			sCode.outtagid(getTag());
			sCode.outcode();
		}
		exitScope(this);
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeDeclaration
	// ***********************************************************************************************
	public void doSCodeDeclaration() {
		ASSERT_SEMANTICS_CHECKED(this);
		if(IS_SCODE_EMITTED()) return;
		enterScope(this);
			sCode.outinst(S_PROFILE); sCode.outtagid(getTag());
			if(kind==KeyWord.KNOWN) { sCode.outinst(S_KNOWN); sCode.outtagid(getBodyTag()); sCode.outstring(info); }
			else if(kind==KeyWord.SYSROUTINE) { sCode.outinst(S_SYSTEM); sCode.outtagid(getBodyTag()); sCode.outstring(info); }
			else if(kind==KeyWord.EXTERNAL) {
				sCode.outinst(S_EXTERNAL); sCode.outtagid(getBodyTag());
				sCode.outstring(info); //!*** nature ***;
				sCode.outstring(identifier); //!*** xid==id ***;
			}
			sCode.outcode(+1);
			signatur.doSCoding();
			sCode.outinst(S_ENDPROFILE);
			sCode.outcode(-1);
//			if(kind==KeyWord.ROUTINE) {
//				sCode.outinst(S_ROUTINESPEC); sCode.outtagid(getBodyTag());
//				sCode.outtagid(getTag());
//				sCode.outcode();
//			}
		exitScope(this);
	}
	
	public void print(final String lead,final int indent) {
    	StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": ");
    	if(visible) s.append("VISIBLE ");
    	if(kind==KeyWord.ROUTINE) s.append("ROUTINE ");
    	else {
    		if(kind==KeyWord.SYSROUTINE) s.append("SYSTEM(\"");
    		else if(kind==KeyWord.KNOWN) s.append("KNOWN(\"");
    		else if(kind==KeyWord.EXTERNAL) s.append("EXTERNAL(\"");
    		else IERR();
    		s.append(info); s.append("\") ");
    	}
    	s.append(identifier).append("; tag="+this.getTag());
    	if(bodyTag!=null) s.append(", bodyTag="+bodyTag);
    	IO.println(s.toString());
    	signatur.print(lead,indent);
    	if(declarationList!=null) for(Declaration d:declarationList) d.print("",indent);
    }


	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	public RoutineDeclaration() { }

//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write RoutineDeclaration: "+identifier);
//		oupt.writeByte(Kind.kRoutine);
//		oupt.writeShort(lineNumber);
//		oupt.writeIdent(identifier);
//		getTag().writeTag(oupt);
//		oupt.writeByte(kind);
//		signatur.writeSignatur(oupt);
//		bodyTag.writeTag(oupt);
//		oupt.writeShort(declarationList.size()); // nDecl
//		for(Declaration decl:declarationList) decl.write(oupt);
//		//Util.TRACE_OUTPUT("END Write RoutineDeclaration: "+identifier);
//	}
//
//	public static RoutineDeclaration createAndReadRoutine(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		RoutineDeclaration rut=new RoutineDeclaration();
//		rut.lineNumber=inpt.readShort();
//		rut.identifier=inpt.readIdent();
//		rut.setTag(inpt.readTag());
//		rut.kind=inpt.readUnsignedByte();
//		rut.signatur=Signatur.createAndReadSignatur(inpt);
//		rut.bodyTag=inpt.readTag();
//		int nDecl=inpt.readShort();
//		for(int i=0;i<nDecl;i++) rut.declarationList.add(Declaration.createAndRead(inpt));
//		
//		rut.SET_SCODE_EMITTED();
//		Util.TRACE_INPUT("END Read RoutineDeclaration: "+rut);
//		return(rut);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write RoutineDeclaration: "+identifier);
		out.writeShort(lineNumber);
		out.writeObject(identifier);
		getTag().writeTag(out);
		out.writeByte(kind);
//		signatur.writeSignatur(oupt);
		out.writeObject(signatur);
		out.writeObject(declarationList);
		bodyTag.writeTag(out);
		out.writeObject(body);
//		out.writeShort(declarationList.size()); // nDecl
//		for(Declaration decl:declarationList) decl.write(oupt);
		//Util.TRACE_OUTPUT("END Write RoutineDeclaration: "+identifier);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		lineNumber=in.readShort();
		identifier=(String) in.readObject();
		setTag(Tag.readTag(in));
		kind=in.readUnsignedByte();
//		signatur=Signatur.createAndReadSignatur(inpt);
		signatur=(Signatur) in.readObject();
		declarationList=(Vector<Declaration>) in.readObject();
		bodyTag=Tag.readTag(in);
		body=(RoutineBody) in.readObject();
//		int nDecl=inpt.readShort();
//		for(int i=0;i<nDecl;i++) rut.declarationList.add(Declaration.createAndRead(inpt));
		
		SET_SCODE_EMITTED();
		Util.TRACE_INPUT("END Read RoutineDeclaration: "+this);
	}
    
}
