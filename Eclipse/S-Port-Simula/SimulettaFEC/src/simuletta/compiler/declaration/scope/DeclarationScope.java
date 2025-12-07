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

import java.util.Stack;
import java.util.Vector;

import simuletta.compiler.declaration.Declaration;
import simuletta.utilities.Option;
import simuletta.utilities.Util;

/**
 * 
 * @author Øystein Myhre Andersen
 * 
 */
public abstract class DeclarationScope extends Declaration {
//	public int lineNumber;    // From SyntaxClass
//	public String identifier; // From Declaration
//	public Tag tag;  		  // From Declaration
//	public boolean global;    // From Declaration
//	public boolean visible;   // From Declaration
	public Vector<Declaration> declarationList = new Vector<Declaration>();

    // ***********************************************************************************************
    // *** Constructor
    // ***********************************************************************************************
    public DeclarationScope(final String identifier,final boolean visible) {
    	super(identifier,visible);
    }

	public void add(Declaration d) {
		Util.ASSERT(d.identifier!=null,"Missing identifier");
    	if(lookup(d.identifier)!=null) ERROR("Redefinition of "+d);
		declarationList.add(d);
		if(currentScope!=null) currentScope.checkDeclarationList();
		currentModule.checkDeclarationList();
	}

	public void addIfNotPresent(Declaration d) {
		Util.ASSERT(d.identifier!=null,"Missing identifier");
    	if(lookup(d.identifier)!=null) return;
		declarationList.add(d);
		if(currentScope!=null) currentScope.checkDeclarationList();
		currentModule.checkDeclarationList();
	}
	
	public boolean isRoutine() {
		return(this instanceof RoutineBody || this instanceof DeclaredBody);
	}
	
	public boolean isRecord() {
		return(this instanceof Record);
	}
	
	public boolean isCurrentModule() {
		return(this instanceof ProgramModule);
	}

	public Declaration lookup(String ident) {
		if(Option.TRACE_FIND_MEANING) Util.TRACE(ident);
		if(declarationList!=null) for(Declaration d:declarationList) {
			if(Option.TRACE_FIND_MEANING) Util.TRACE("CHECKING "+d);
			if(d.identifier.equalsIgnoreCase(ident)) {
				if(Option.TRACE_FIND_MEANING) Util.TRACE(ident+"   FOUND in "+this+": "+d);
				return(d);
			}
		}
		return(null);
	}

	public void checkDeclarationList() {
		if(declarationList!=null) for(Declaration d:declarationList) {
			Util.ASSERT(d.identifier!=null,"Inconsistent DeclarationList");
		}
	}

	public void printDeclarationList() {
		if(declarationList!=null) for(Declaration d:declarationList) {
			Util.println("DECLARED: "+d.getClass().getSimpleName()+" "+d);
		}
	}

	private static Stack<DeclarationScope> scopeStack=new Stack<DeclarationScope>(); // Current Scope. Maintained during Checking and Coding
	
	protected static void enterScope(DeclarationScope scope) {
//		Util.BREAK("Global.enterScope: currentScope <== "+scope);
		scopeStack.push(currentScope); currentScope=scope;
		scope.enterLine();
	}
	protected static void exitScope(DeclarationScope scope) {
		Util.ASSERT(currentScope==scope,"Impossible");
		currentScope=scopeStack.pop();
		scope.exitLine();
//		Util.BREAK("Global.exitScope: currentScope <== "+currentScope);
	}

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	public DeclarationScope() { }

}
