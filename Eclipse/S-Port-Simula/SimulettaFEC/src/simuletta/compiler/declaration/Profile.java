/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.declaration;

import static simuletta.compiler.Global.*;
import static simuletta.utilities.Util.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
 * 		ProfileDeclaration ::= < GLOBAL < ( identifying'StringValue ) >? >?
 *                          PROFILE profile'identifier  ParameterSpecification  END
 * 
 * 			ParameterSpecification ::= < IMPORT < Type localid < , localid >* >+ >?  < EXPORT Type Identifier >?
 * 			                         | < IMPORT < Type localid < , localid >* >+ >?  < EXIT LABEL Identifier >?
 * 
 * S-Code:
 * 
 * 
 *		RoutineProfile ::= PROFILE profile:newtag
 *							< ImportDefinition >* < Export  |  Exit >?
 *						   ENDPROFILE
 *
 *		Peculiar ::= KNOWN body:newtag kid:string
 *				  |  SYSTEM body:newtag sid:string
 *				  |  EXTERNAL body:newtag nature:string xid:string
 *				  |  INTERFACE pid:string
 *  
 *		ImportDefinition ::= IMPORT parm:newtag QuantityDescriptor
 *  
 *		Export ::= EXPORT parm:newtag ResolvedType
 *		Exit   ::= EXIT return:newtag
 * 
 * </pre>
 */
public class Profile extends Declaration implements Externalizable {
//public class Profile extends Scope implements Externalizable {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration   tag of descr - must be zero until Pass2;
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
	public Signatur signatur;
	private boolean interFace;
	
//	public Tag bodyTag; // NOTE: Simula FEC assumes that  bodyTag.code = profileTag.code    // TESTING BODYTAG
	
    // ***********************************************************************************************
    // *** Constructor  new Profile
    // ***********************************************************************************************
    public Profile(final boolean visibleflag,final String identifier) {
    	super(identifier,visibleflag);
    	defTag(visibleflag,identifier);
//    	bodyTag=new Tag(visible,identifier,""); // TESTING BODYTAG
    }
	
    public static Profile doParse(boolean visibleflag) {
		Parser.TRACE("Profile.parse: PROFILE/GLOBAL");
        if(!currentScope.isCurrentModule()) ERROR("Misplaced profile declaration");
        boolean interFace=false;
        if(Parser.prevToken.getKeyWord() == KeyWord.GLOBAL) {
        	interFace=true;
            Parser.expect(KeyWord.PROFILE);
        }
        String identifier=Parser.expectIdentifier();
        Profile prf=new Profile(visibleflag,identifier);
    	prf.interFace=interFace;
        prf.signatur=Signatur.doParse(visibleflag);
        Parser.expect(KeyWord.END);
        if(Option.TRACE_PARSE_BREIF) prf.print("",0);
		currentModule.checkDeclarationList();
        return(prf);
    }

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterLine();//Global.sourceLineNumber = lineNumber;
//			Comn.enterScope(this);
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
			signatur.doChecking();
//			Comn.exitScope();
        exitLine();
		SET_SEMANTICS_CHECKED();
	}

	public Declaration findLocalMeaning(String ident) {
		if(Option.TRACE_FIND_MEANING) Util.TRACE("Profile.findLocalMeaning: "+ident);
		return(signatur.findLocalMeaning(ident));
	}
	
	// ***********************************************************************************************
	// *** Coding: prepareSCodeOutput
	// ***********************************************************************************************
	public void prepareSCodeOutput() {
		signatur.prepareSCodeOutput();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeDeclaration
	// ***********************************************************************************************
	public void doSCodeDeclaration() {
		ASSERT_SEMANTICS_CHECKED(this);
		if(IS_SCODE_EMITTED()) return;
//		Comn.enterScope(this);
		enterLine();
			output_S_LINE();
			sCode.outinst(S_PROFILE); sCode.outtagid(getTag());
			sCode.outcode(+1);
			if(interFace) {
//				Util.IERR("SJEKK DETTE !!! "+this.identifier);
//				sCode.outinst(S_INTERFACE); sCode.outstring("INTRHA");
				sCode.outinst(S_INTERFACE); sCode.outstring(this.identifier);
				sCode.outcode();
			}
			signatur.doSCoding();
			sCode.outinst(S_ENDPROFILE);
			sCode.outcode(-1);
		exitLine();
//		Comn.exitScope();
	}


	public void print(final String lead,final int indent) {
    	StringBuilder s=new StringBuilder(Util.edIndent(indent));
    	s.append("Line "+lineNumber+": ");
    	if(visible) s.append("VISIBLE ");
    	if(interFace) s.append("GLOBAL ");
    	s.append("PROFILE ");
    	s.append(identifier).append(';');
    	IO.println(s.toString());
    	signatur.print("",indent);
    }

    public String toString() {
    	StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": ");
    	if(visible) s.append("VISIBLE ");
    	if(interFace) s.append("GLOBAL ");
    	s.append("PROFILE ");
    	s.append(identifier).append("; ");
    	return(s.toString()+signatur);
    }

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************

    public Profile() { }

//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write Record: "+identifier);
//		oupt.writeByte(Kind.kProfile);
//		oupt.writeShort(lineNumber);
//		oupt.writeIdent(identifier);
//		getTag().writeTag(oupt);
//		signatur.writeSignatur(oupt);
//		oupt.writeBoolean(interFace);
//		//Util.TRACE_OUTPUT("END Write Record: "+identifier);
//	}
//
//	public static Profile createAndReadProfile(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		Profile prf=new Profile();
//		prf.lineNumber=inpt.readShort();
//		prf.identifier=inpt.readIdent();
//		prf.setTag(inpt.readTag());
//		prf.signatur=Signatur.createAndReadSignatur(inpt);
//		prf.interFace=inpt.readBoolean();
//		Util.TRACE_INPUT("END Read Profile: "+prf);
//		return(prf);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write Record: "+identifier);
		out.writeShort(lineNumber);
		out.writeObject(identifier);
		getTag().writeTag(out);
		out.writeObject(signatur);
		out.writeBoolean(interFace);
		//Util.TRACE_OUTPUT("END Write Record: "+identifier);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		lineNumber=in.readShort();
		identifier=(String) in.readObject();
		setTag(Tag.readTag(in));
		signatur=(Signatur) in.readObject();
		interFace=in.readBoolean();
		Util.TRACE_INPUT("END Read Profile: "+this);
	}

}
