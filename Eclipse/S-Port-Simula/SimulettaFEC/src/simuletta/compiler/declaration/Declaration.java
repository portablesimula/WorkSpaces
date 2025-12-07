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

import java.util.Vector;

import simuletta.compiler.SyntaxClass;
import simuletta.compiler.declaration.scope.InsertedModule;
import simuletta.compiler.declaration.scope.Record;
import simuletta.compiler.declaration.scope.RoutineDeclaration;
import simuletta.compiler.declaration.scope.DeclaredBody;
import simuletta.compiler.parsing.LiteralMnemonic;
import simuletta.compiler.parsing.MacroDefinition;
import simuletta.compiler.parsing.Parser;
import simuletta.compiler.parsing.SimulettaScanner;
import simuletta.type.Type;
import simuletta.utilities.KeyWord;
import simuletta.utilities.Option;
import simuletta.utilities.Tag;
import simuletta.utilities.Token;
import simuletta.utilities.Util;

public abstract class Declaration extends SyntaxClass {
//	public int lineNumber;  // From SyntaxClass
	public String identifier;
	private Tag tag;
	public boolean global;
	public boolean visible;
	
	public Declaration( ) {
		//Util.TRACE("currentScope="+currentScope);
	    this.global=currentScope==null || !currentScope.isRoutine();
	}
    
    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public Declaration(final String identifier,final boolean visible) {
	    this.identifier=identifier;
	    this.visible=visible;
	    this.global=currentScope==null || !currentScope.isRoutine();
    }

    public Tag getTag() { return(tag); }
    public void setTag(Tag tag) { this.tag=tag; }
	protected void defTag(boolean visible,String identifier) {
		if(tag!=null) Util.FATAL_ERROR("Tag already set: "+identifier);
		setTag(Tag.newTag(visible,identifier,""));
	}

    //%title ******   P A R S E R :   declarations   ******
    public static void doParse(Vector<Declaration> declset) {
    	Type type;
        boolean constflag=false;
        boolean visibleflag=false;
//      int lastLineEmitted=0;
        
        LOOP: while(true) {
        	if(Parser.accept(KeyWord.VISIBLE)) {
        		Parser.TRACE("Grammar.declarations: VISIBLE");
        		if(currentScope.isRoutine()) ERROR("Visible illegal in routine");
        		else if(currentModule.isMainProgram()) WARNING("Misplaced VISIBLE in Main Program - IGNORED");  // in main
        		else visibleflag = !currentModule.isMainProgram(); // not in main
        		continue LOOP;
        	} else if(Parser.accept(KeyWord.CONST)) {
        		Parser.TRACE("Grammar.declarations: CONST");
            	constflag=true;
            	continue LOOP;
        	} else if(Parser.accept(KeyWord.DEFINE)) {
        		Parser.TRACE("Grammar.declarations: DEFINE");
            	do { String ident=Parser.expectIdentifier();
            		Parser.expect(KeyWord.EQ);
            		Token value=Parser.getSymb();
            		SimulettaScanner.defineMnemonic(ident,new LiteralMnemonic(ident,value));
            	} while(Parser.accept(KeyWord.COMMA));
    		} else if(Parser.accept(KeyWord.MACRO)) {
    			Parser.TRACE("Grammar.declarations: MACRO");
    			new MacroDefinition(visibleflag);
        	} else if(Parser.accept(KeyWord.RECORD)) {
        		Parser.TRACE("Grammar.declarations: RECORD");
        		Record record=Record.doParse(visibleflag);
        		if(currentScope.isRecord()) ERROR("Misplaced record declaration");
        		else declset.add(record);
        	} else if(Parser.accept(KeyWord.PROFILE,KeyWord.GLOBAL)) {
        		Profile profile=Profile.doParse(visibleflag);
        		declset.add(profile);
        	}
        	 else if(Parser.accept(KeyWord.BODY)) {
        		 DeclaredBody body=DeclaredBody.doParse(visibleflag);
        		 declset.add(body);
        	 }
        	 else if(Parser.accept(KeyWord.SYSROUTINE,KeyWord.KNOWN,KeyWord.EXTERNAL,KeyWord.ROUTINE)) {
        		 RoutineDeclaration routine=RoutineDeclaration.doParse(declset,visibleflag,Parser.prevToken.getKeyWord());
        		 declset.add(routine);
        	 }
        	 else if(Parser.accept(KeyWord.INSERT,KeyWord.SYSINSERT)) {
        		 Parser.TRACE("Grammar.declarations: INSERT/SYSINSERT");
        		 if(currentScope.isRecord()) ERROR("Misplaced insert");
        		 boolean sys=(Parser.prevToken.getKeyWord() == KeyWord.SYSINSERT);
        		 do { String modident=Parser.expectIdentifier();
        		 	  String externalId="";
        		 	  if(Parser.accept(KeyWord.EQ)) externalId=Parser.expectString();
        			  InsertedModule module=new InsertedModule(modident,externalId);
        			  module.doInsertModule(sys);
        			  //modset.add(module);
        		 } while(Parser.accept(KeyWord.COMMA));
        	} else {
        		type=Parser.acceptType();
        		if(type==null) {
            		Parser.TRACE("Grammar.declarations: NO MORE DECLARATIONS");
            		break LOOP;
        		}
                do { VariableDeclaration q=VariableDeclaration.doParse(type,visibleflag,constflag);
                	 declset.add(q);
                } while(Parser.accept(KeyWord.COMMA));
        	}
            constflag=false;
            visibleflag=false;
        }
    }
	
	
	public Declaration findLocalMeaning(String ident) {
		return(null);
	}
	
    public static Declaration findGlobalMeaning(String symb) {
    	if(Option.TRACE_FIND_MEANING) Util.TRACE("Declaration.findGlobalMeaning: "+symb);
		currentModule.checkDeclarationList();
		for(Declaration d:currentModule.declarationList) {
	    	if(!(d instanceof Record)) if(Option.TRACE_FIND_MEANING) Util.TRACE("Declaration.findGlobalMeaning: LOCAL CHECKING "+d);
			if(d.identifier.equalsIgnoreCase(symb)) return(d);
		}
    	
    	for(InsertedModule m:modset) {
    		if(Option.TRACE_FIND_MEANING) Util.TRACE("Declaration.findGlobalMeaning: SEARCHING "+m.identifier);
        	for(Declaration d:m.declarationList) {
    	    	if(!(d instanceof Record)) if(Option.TRACE_FIND_MEANING) Util.TRACE("Declaration.findGlobalMeaning: CHECKING "+d);
    			if(d.identifier.equalsIgnoreCase(symb)) {
    				if(Option.TRACE_FIND_MEANING) Util.TRACE("Declaration.findGlobalMeaning: "+symb+"   FOUND in "+m.identifier+": "+d);
    				return(d);
    			}
    		}
    	}
    	return(null);
    }
	
    public static Declaration findMeaning(String ident) {
    	if(Option.TRACE_FIND_MEANING) Util.TRACE("Declaration.findMeaning: "+ident+", currentScope="+currentScope);
    	// Søk lokalt først
    	if(currentScope!=null) {
        	currentScope.checkDeclarationList();
    		Declaration fd=currentScope.findLocalMeaning(ident);
    		if(fd!=null) return(fd);
    	}
    	return(findGlobalMeaning(ident));
    }

    public void prepareSCodeOutput() {
		Util.FATAL_ERROR("prepareSCodeOutput should been REDEFINED !  QUAL="+this.getClass().getSimpleName());
	}

    public void doSCodeSpecCode() {
//		Util.FATAL_ERROR("doSCodeSpecCode should been REDEFINED !  QUAL="+this.getClass().getSimpleName());
	}

    public void doSCodeDeclaration() {
		Util.FATAL_ERROR("doSCodeDeclaration should been REDEFINED !  QUAL="+this.getClass().getSimpleName());
	}
	
	public String toString() {
		StringBuilder s=new StringBuilder();
		if(visible) s.append("VISIBLE ");
		if(global) s.append("GLOBAL ");
		s.append(identifier);
		s.append("(tag=").append(tag);
		s.append(')');
		return(s.toString());
	}
	
	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	
//	public void write(AttributeOutput oupt) throws IOException {
//		Util.IERR("Missing redefinition of 'write' in "+this.getClass().getSimpleName());
//	}
//
//	public static Declaration createAndRead(AttributeInput inpt) throws ClassNotFoundException, IOException {
//		int kind=inpt.readUnsignedByte();
//		switch(kind) {
//			case Kind.kNull:         return(null);
//			case Kind.kRecord:       return(Record.createAndReadRecord(inpt));
//			case Kind.kVariable:     return(VariableDeclaration.createAndReadVariable(inpt));
//			case Kind.kProfile:      return(Profile.createAndReadProfile(inpt));
//			case Kind.kRoutine:      return(RoutineDeclaration.createAndReadRoutine(inpt));
//			case Kind.kRoutineBody:  return(RoutineBody.createAndReadRoutineBody(inpt));
//			case Kind.kLabel:        return(LabelDeclaration.createAndReadLabel(inpt));
//			case Kind.kDeclaredBody: return(DeclaredBody.createAndReadDeclaredBody(inpt));
//		}
//		Util.IERR("");
//		return(null);
//	}
	
}
