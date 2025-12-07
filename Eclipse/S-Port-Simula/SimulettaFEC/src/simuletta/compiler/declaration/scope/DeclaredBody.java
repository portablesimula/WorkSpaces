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

import simuletta.compiler.common.ProgramPoint;
import simuletta.compiler.declaration.Declaration;
import simuletta.compiler.declaration.Profile;
import simuletta.compiler.parsing.Parser;
import simuletta.compiler.statement.Statement;
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
 * 		BodyDeclaration ::= BODY ( Profile'Identifier )
 *                          Routine'Identifier  RoutineBody
 * 
 * 			RoutineBody ::= BEGIN <LocalVariable>*  <Statement>* END
 * 
 * 				LocalVariable ::= Type localid < , localid >*
 * 
 * 
 * S-Code:
 * 
 *		RoutineSpecification ::= ROUTINESPEC body:newtag profile:tag
 *  
 *		RoutineDefinition ::= ROUTINE body:spectag profile:tag
 *								<local_quantity>* < instruction >* ENDROUTINE
 * 
 *			local_quantity ::= local var:newtag quantity_descriptor
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public class DeclaredBody extends DeclarationScope implements Externalizable {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
//	public Vector<Declaration> declarationList // From DeclarationScope
	public String profileIdentifier;
	public final Vector<Statement> statements = new Vector<Statement>();

    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public DeclaredBody(final boolean visibleflag,final String identifier) {
    	super(identifier,visibleflag);
    	defTag(visibleflag,identifier);
    }

    public static DeclaredBody doParse(boolean visibleflag) {
		Parser.TRACE("Grammar.declarations: BODY: visibleflag="+visibleflag);
		//IO.println("Grammar.declarations: BODY");
        if(!currentScope.isCurrentModule()) ERROR("Misplaced Routine Body");
        Parser.expect(KeyWord.BEGPAR);
        String rutProfileIdentifier=Parser.expectIdentifier();
        Parser.expect(KeyWord.ENDPAR);                
        String rutIdentifier=Parser.expectIdentifier();
    	DeclaredBody body=new DeclaredBody(visibleflag,rutIdentifier);
    	body.profileIdentifier=rutProfileIdentifier;
		enterScope(body);
			Parser.expect(KeyWord.BEGIN);
			Declaration.doParse(body.declarationList);
			Statement.parseStatements(body.statements);
			Parser.expect(KeyWord.END);
        
			if(Option.TRACE_PARSE_BREIF) body.print("",20);
		exitScope(body);
        return(body);
    }


	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		enterScope(this);
			if(Option.TRACE_CHECKER) Util.TRACE("Checking "+this);
			for (Declaration dcl : declarationList)	dcl.doChecking();
			for (Statement stm : statements) stm.doChecking();
		exitScope(this);
		SET_SEMANTICS_CHECKED();
	}

	public Declaration findLocalMeaning(String ident) {
		Util.BREAK("RoutineBody.findLocalMeaning: "+ident);
		for(Declaration d:declarationList) {
			Util.BREAK("RoutineBody.findLocalMeaning: CHECKING: "+ident+" <==> "+d);
			if(d.identifier.equalsIgnoreCase(ident)) {
				Util.BREAK("RoutineBody.findLocalMeaning: FOUND="+d);
				return(d);
			}
		}
		Util.BREAK("RoutineBody.findLocalMeaning: Then: Search  imports, export, exit "+ident);
		Profile profile=(Profile)Declaration.findGlobalMeaning(profileIdentifier);
		return(profile.findLocalMeaning(ident));
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeSpecCode
	// ***********************************************************************************************
    public void doSCodeSpecCode() {
		ASSERT_SEMANTICS_CHECKED(this);
		enterScope(this);
			output_S_LINE();
			Profile profile=(Profile)Declaration.findGlobalMeaning(profileIdentifier);
			sCode.outinst(S_ROUTINESPEC); sCode.outtagid(getTag());
			sCode.outtagid(profile.getTag());
			sCode.outcode();
			bodyList.add(this);
		exitScope(this);
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeDeclaration
	// ***********************************************************************************************
	public void doSCodeDeclaration() {
		ASSERT_SEMANTICS_CHECKED(this);
		if(IS_SCODE_EMITTED()) return;
//		enterScope(this);
//			output_S_LINE();
//			Profile profile=(Profile)Declaration.findGlobalMeaning(profileIdentifier);
//			sCode.outinst(S_ROUTINESPEC); sCode.outtagid(getTag());
//			sCode.outtagid(profile.getTag());
//			sCode.outcode();
//			bodyList.add(this);
//		exitScope(this);
	}
    
	// ***********************************************************************************************
	// *** Coding: doSCodeStatement
	// ***********************************************************************************************
	public void doSCodeStatement() {
		enterScope(this);
			Profile prf=(Profile)Declaration.findGlobalMeaning(profileIdentifier);
			sCode.outinst(S_ROUTINE);
			sCode.outtagid(this.getTag()); sCode.outtagid(prf.getTag());
			sCode.outcode(+1);
			if(declarationList!=null) for(Declaration d:declarationList) d.doSCodeDeclaration();
			for(Statement s:statements) s.doSCodeStatement();
			ProgramPoint.clearDestinationTable();
			sCode.outinst(S_ENDROUTINE);
			sCode.outcode(-1);
		exitScope(this);
	}
	
	public void print(final String lead,final int indent) {
		StringBuilder s=new StringBuilder();
    	s.append("Line "+lineNumber+": ");
		if(visible) s.append("visible ");
		s.append("body ");
		s.append(identifier).append(';');
		IO.println(s.toString());
		for(Declaration d:declarationList) d.print("",indent);
	}


	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	public DeclaredBody() { }

//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write DeclaredBody: ");
//		oupt.writeByte(Kind.kDeclaredBody);
//		oupt.writeShort(lineNumber);
//		oupt.writeIdent(identifier);
//		getTag().writeTag(oupt);
//		oupt.writeShort(declarationList.size()); // nDecl
//		for(Declaration decl:declarationList) decl.write(oupt);
//		oupt.writeIdent(profileIdentifier);
//		//Util.TRACE_OUTPUT("END Write DeclaredBody: "+identifier);
//	}
//
//	public static DeclaredBody createAndReadDeclaredBody(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		DeclaredBody body=new DeclaredBody();
//		body.lineNumber=inpt.readShort();
//		body.identifier=inpt.readIdent();
//		body.setTag(inpt.readTag());
//		int nDecl=inpt.readShort();
//		for(int i=0;i<nDecl;i++) body.declarationList.add(Declaration.createAndRead(inpt));
//		body.profileIdentifier=inpt.readIdent();
//		body.SET_SCODE_EMITTED();
//		Util.TRACE_INPUT("END Read DeclaredBody: "+body);
//		return(body);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write DeclaredBody: ");
		out.writeShort(lineNumber);
		out.writeObject(identifier);
		getTag().writeTag(out);
//		out.writeShort(declarationList.size()); // nDecl
//		for(Declaration decl:declarationList) decl.write(oupt);
		out.writeObject(declarationList);
		out.writeObject(profileIdentifier);
		//Util.TRACE_OUTPUT("END Write DeclaredBody: "+identifier);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		lineNumber=in.readShort();
		identifier=(String) in.readObject();
		setTag(Tag.readTag(in));
//		int nDecl=inpt.readShort();
//		for(int i=0;i<nDecl;i++) body.declarationList.add(Declaration.createAndRead(inpt));
		declarationList=(Vector<Declaration>) in.readObject();
		profileIdentifier=(String) in.readObject();
		SET_SCODE_EMITTED();
		Util.TRACE_INPUT("END Read DeclaredBody: "+this);
	}

}
