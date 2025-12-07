/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simuletta.compiler.declaration.scope;

import static simuletta.compiler.Global.*;
import static simuletta.compiler.common.S_Instructions.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

import simuletta.compiler.common.ProgramPoint;
import simuletta.compiler.declaration.Declaration;
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
public class RoutineBody extends DeclarationScope implements Externalizable {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
//	public Vector<Declaration> declarationList // From DeclarationScope
	public final Vector<Statement> statements = new Vector<Statement>();
	
	public RoutineDeclaration routine;
//	String routineID;
//	public RoutineDeclaration getRoutine() {
//		RoutineDeclaration rut=(RoutineDeclaration) Global.currentModule.lookup(routineID);
//		//IO.println("RoutineBody.getRoutine: "+routineID+"  ==>  "+rut);
//		return(rut);
//	}
	

    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
//    public RoutineBody(final boolean visibleflag,final RoutineDeclaration routine) {
    public RoutineBody(final boolean visibleflag,final RoutineDeclaration routine,Tag bodyTag) { // TESTING BODYTAG
    	super(routine.identifier+":body",visibleflag);
//    	this.routineID=routine.identifier;
    	this.routine=routine;
    	
    	//defTag(visibleflag,identifier);  // TESTING BODYTAG
    	setTag(bodyTag);                   // TESTING BODYTAG
    }

    public static RoutineBody doParse(boolean visibleflag,RoutineDeclaration routine) {
		Parser.TRACE("Grammar.declarations: ROUTINE-BODY");
		//IO.println("Grammar.declarations: BODY");
//    	RoutineBody body=new RoutineBody(visibleflag,routine);
    	RoutineBody body=new RoutineBody(visibleflag,routine,routine.bodyTag); // TESTING BODYTAG
		enterScope(body);
			Parser.expect(KeyWord.BEGIN);
			Declaration.doParse(body.declarationList);
			Statement.parseStatements(body.statements);
        
			if(Option.TRACE_PARSE_BREIF) body.print("",20);
			bodyList.add(body);
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
		Util.BREAK("RoutineBody.findLocalMeaning: Then search  imports, export, exit "+ident);
		return(routine.findLocalMeaning(ident));
//		return(getRoutine().findLocalMeaning(ident));
	}
	
	// ***********************************************************************************************
	// *** Coding: prepareSCodeOutput
	// ***********************************************************************************************
	public void prepareSCodeOutput() {
		for(Declaration a:declarationList) a.prepareSCodeOutput();
	}

	// ***********************************************************************************************
	// *** Coding: doSCodeDeclaration
	// ***********************************************************************************************
	public void doSCodeDeclaration() {
		ASSERT_SEMANTICS_CHECKED(this);
	}
	
	// ***********************************************************************************************
	// *** Coding: doSCodeStatement  -- Called via bodyList
	// ***********************************************************************************************
	public void doSCodeStatement() {
		enterScope(this);
			sCode.outinst(S_ROUTINE); sCode.outtagid(this.getTag());
//			sCode.outtagid(getRoutine().getTag());
			sCode.outtagid(routine.getTag());

			sCode.outcode(+1);
			for(Declaration d:declarationList) d.doSCodeDeclaration();
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
		//	spec.prt(pos);
		for(Declaration d:declarationList) d.print("",indent);
	}


	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	public RoutineBody() { }

//	@Override
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.TRACE_OUTPUT("BEGIN Write RoutineBody: ");
//		oupt.writeByte(Kind.kRoutineBody);
//		oupt.writeShort(lineNumber);
//		oupt.writeIdent(identifier);
//		getTag().writeTag(oupt);
//		oupt.writeShort(declarationList.size()); // nDecl
//		for(Declaration decl:declarationList) decl.write(oupt);
////		oupt.writeIdent(routineID);
//		oupt.writeObject(routine);
//		//Util.TRACE_OUTPUT("END Write RoutineBody: "+identifier);
//	}
//
//	public static RoutineBody createAndReadRoutineBody(AttributeInput inpt) throws IOException, ClassNotFoundException {
//		RoutineBody body=new RoutineBody();
//		body.lineNumber=inpt.readShort();
//		body.identifier=inpt.readIdent();
//		body.setTag(inpt.readTag());
//		int nDecl=inpt.readShort();
//		for(int i=0;i<nDecl;i++) body.declarationList.add(Declaration.createAndRead(inpt));
////		body.routineID=inpt.readIdent();
//		body.routine=(RoutineDeclaration)inpt.readObject();
//		body.SET_SCODE_EMITTED();
//		Util.TRACE_INPUT("END Read RoutineBody: "+body);
//		return(body);
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write RoutineBody: ");
		out.writeShort(lineNumber);
		out.writeObject(identifier);
		getTag().writeTag(out);
//		out.writeShort(declarationList.size()); // nDecl
//		for(Declaration decl:declarationList) decl.write(oupt);
		out.writeObject(declarationList);
//		oupt.writeIdent(routineID);
		out.writeObject(routine);
		//Util.TRACE_OUTPUT("END Write RoutineBody: "+identifier);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		lineNumber=in.readShort();
		identifier=(String) in.readObject();
//		setTag(inpt.readTag());
		setTag(Tag.readTag(in));
//		int nDecl=in.readShort();
//		for(int i=0;i<nDecl;i++) declarationList.add(Declaration.createAndRead(inpt));
		declarationList=(Vector<Declaration>) in.readObject();
//		body.routineID=inpt.readIdent();
		routine=(RoutineDeclaration)in.readObject();
		SET_SCODE_EMITTED();
		Util.TRACE_INPUT("END Read RoutineBody: "+this);
		
	}

}
