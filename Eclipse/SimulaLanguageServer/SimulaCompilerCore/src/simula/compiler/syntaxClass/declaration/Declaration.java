/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.declaration;

import java.lang.classfile.ClassBuilder;
import java.lang.classfile.CodeBuilder;
import java.util.Vector;

import simula.builder.Parse;
import simula.builder.SimulaBuilder;
import simula.compiler.syntaxClass.ProtectedSpecification;
import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.syntaxClass.Type;
import simula.compiler.utilities.DeclarationList;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.token.LexToken;

/// Declaration.
///  
/// <pre>
///  
/// Simula Standard: Chapter 5. Declarations
///  
///    declaration
///       = simple-variable-declaration
///       | array-declaration
///       | switch-declaration
///       | procedure-declaration
///       | class-declaration
///       | external-declaration
///  </pre>
/// This class is prefix to DeclarationScope, ExternalDeclaration, Parameter,
/// SimpleVariableDeclaration, VirtualSpecification, VirtualMatch, ArrayDeclaration
///  
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaCompiler2/Simula/src/simula/compiler/syntaxClass/declaration/Declaration.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public abstract class Declaration extends SyntaxClass {

	/// The type
	public Type type = null;

	/// Indicates that this declaration is protected.
	public ProtectedSpecification isProtected; // Set during Checking

	/// Simula Identifier from Source Text
	public String identifier;

	/// External Identifier set by doChecking
	public String externalIdent;

	/// The DeclarationScope in which this Declaration is defined.
	public DeclarationScope declaredIn;

	/// The declarationKind.
	public int declarationKind;


	// ***********************************************************************************************
	// *** Constructor
	// ***********************************************************************************************
	/// Create a new Declaration.
	/// @param identifier the given identifier
	protected Declaration(final SimulaBuilder simBuilder, final String identifier) {
		super(simBuilder);
		this.identifier = identifier;
		this.externalIdent = identifier; // May be overwritten
		declaredIn = CoreGlobal.getCurrentScope();
		checkAlreadyDefined();
	}

	/// Returns the Java identifier for this declaration.
	/// @return the Java identifier for this declaration
	public final String getJavaIdentifier() {
		return (this.externalIdent);
	} // May be redefined

	/// Check if a declaration with this identifier is already defined.
	protected void checkAlreadyDefined() {
		boolean error = false;
		boolean warning = false;
		if (identifier == null)
			return;
		if (identifier.equals("_RESULT"))
			return;
		if (declaredIn == null)
			return;
		if (declaredIn instanceof StandardClass)
			return;
		if (declaredIn instanceof StandardProcedure)
			return;
		Vector<Parameter> parameterList;
		if (declaredIn instanceof ProcedureDeclaration proc)
			parameterList = proc.parameterList;
		else if (declaredIn instanceof ClassDeclaration cls)
			parameterList = cls.parameterList;
		else
			parameterList = null; // No parameters

		if (parameterList != null) {
			for (Declaration decl : parameterList)
				if (Util.equals(decl.identifier, identifier)) {
					warning = true;
					break;
				}
		}
		LOOP: for (Declaration decl : declaredIn.declarationList) {
			if (decl == null)
				return; // Error recovery
			if (decl.identifier == null)
				return; // Error recovery
			if (Util.equals(decl.identifier, identifier)) {
				error = true;
				break LOOP;
			}
		}
		if (error)
			Util.syntaxError(simBuilder, identifier + " is alrerady defined in " + declaredIn.identifier);
		else if (warning)
			Util.warning(simBuilder, identifier + " is alrerady defined in " + declaredIn.identifier);
	}

	/// Parse a declaration and add it to the given declaration list.
	/// @param enclosure the owning block.
	/// @return true if a declaration was found, false otherwise
//	protected static boolean acceptDeclaration(final BlockDeclaration enclosure) {
//		if (Option.internal.TRACE_PARSE)
//			Parse.TRACE("Parse Declaration");
//		DeclarationList declarationList=enclosure.declarationList;
//		String prefix = Parse.acceptIdentifier(simBuilder);
//		if (prefix != null) {
//			if (Parse.accept(simBuilder, KeyWord.CLASS))
//				declarationList.add(ClassDeclaration.expectClassDeclaration(prefix));
//			else {
//				Parse.saveCurrentToken(); // Identifier is NOT a class prefix.
//				return (false);
//			}
//		} else if (Parse.accept(simBuilder, KeyWord.ARRAY))
//			ArrayDeclaration.expectArrayDeclaration(Type.Real, declarationList); // Default type real for arrays
//		else if (Parse.accept(simBuilder, KeyWord.PROCEDURE))
//			declarationList.add(ProcedureDeclaration.expectProcedureDeclaration(null));
//		else if (Parse.accept(simBuilder, KeyWord.PRIOR)) {
//			Util.warning("Keyword 'prior' ignored - prior procedure is not implemented");
//			Type type = Parse.acceptType(simBuilder);
//			Parse.expect(simBuilder, KeyWord.PROCEDURE);
//			declarationList.add(ProcedureDeclaration.expectProcedureDeclaration(type));
//		} else if (Parse.accept(simBuilder, KeyWord.CLASS))
//			declarationList.add(ClassDeclaration.expectClassDeclaration(null));
//		else if (Parse.accept(simBuilder, KeyWord.SWITCH)) {
//			String ident = Parse.acceptIdentifier(simBuilder);
//			if (ident == null) {
//				// Switch Statement
//				Parse.saveCurrentToken();
//				return (false);
//			}
//			declarationList.add(new SwitchDeclaration(ident));
//		} else if (Parse.accept(simBuilder, KeyWord.EXTERNAL))
//			ExternalDeclaration.expectExternalHead(enclosure);
//		else {
//			Type type = Parse.acceptType(simBuilder);
//			if (type == null)
//				return (false);
//			if (Parse.accept(simBuilder, KeyWord.PROCEDURE))
//				declarationList.add(ProcedureDeclaration.expectProcedureDeclaration(type));
//			else if (Parse.accept(simBuilder, KeyWord.ARRAY))
//				ArrayDeclaration.expectArrayDeclaration(type, declarationList);
//			else 
//				SimpleVariableDeclaration.expectSimpleVariable(type, declarationList);
//			
//			if (Option.internal.TRACE_PARSE)
//				Parse.TRACE("Parse Declaration(2)");
//		}
//		return (true);
//	}

	/// Parse a declaration and add it to the given declaration list.
	/// @param enclosure the owning block.
	/// @return true if a declaration was found, false otherwise
//	protected static boolean acceptDeclaration(final PsiBuilder simBuilder, final BlockDeclaration enclosure) {
	protected static Vector<SyntaxClass> acceptDeclaration(final SimulaBuilder simBuilder) {
		if (Option.internal.TRACE_PARSE)
			Parse.TRACE("Parse Declaration");
		Declaration decl = null;
		IO.println("Declaration.acceptDeclaration: BEFORE acceptIdentifier");
		LexToken maybePrefix = Parse.acceptIdentifier(simBuilder);
		IO.println("Declaration.acceptDeclaration: AFTER acceptIdentifier");
		if (maybePrefix != null) {
			IO.println("Declaration.acceptDeclaration: maybePrefix="+maybePrefix);
			if (Parse.accept(simBuilder, KeyWord.CLASS)) {
				IO.println("Declaration.acceptDeclaration: CLASS");
				decl = ClassDeclaration.expectClassDeclaration(simBuilder, maybePrefix.edText());
			} else {
				IO.println("Declaration.acceptDeclaration: NOT CLASS");
				
				Parse.saveCurrentToken(simBuilder); // Identifier is NOT a class prefix.
				return null;
			}
		} else if (Parse.accept(simBuilder, KeyWord.ARRAY))
			return ArrayDeclaration.expectArrayDeclaration(simBuilder, Type.Real); // Default type real for arrays
		else if (Parse.accept(simBuilder, KeyWord.PROCEDURE))
			decl = ProcedureDeclaration.expectProcedureDeclaration(simBuilder, null);
		else if (Parse.accept(simBuilder, KeyWord.PRIOR)) {
			Util.warning(simBuilder, "Keyword 'prior' ignored - prior procedure is not implemented");
			Type type = Parse.acceptType(simBuilder);
			Parse.expect(simBuilder, KeyWord.PROCEDURE);
			decl = ProcedureDeclaration.expectProcedureDeclaration(simBuilder, type);
		} else if (Parse.accept(simBuilder, KeyWord.CLASS))
			decl = ClassDeclaration.expectClassDeclaration(simBuilder, null);
		else if (Parse.accept(simBuilder, KeyWord.SWITCH)) {
			LexToken ident = Parse.acceptIdentifier(simBuilder);
			if (ident == null) {
				// Switch Statement
				return null;
			}
			decl = new SwitchDeclaration(simBuilder, ident.edText());
		} else if (Parse.accept(simBuilder, KeyWord.EXTERNAL)) {
			Vector<SyntaxElement> ext = ExternalDeclaration.expectExternalDeclaration(simBuilder);	
			return ext;
		} else {
			Type type = Parse.acceptType(simBuilder);
			if (type == null) return null;
			
			if (Parse.accept(simBuilder, KeyWord.PROCEDURE)) {
				decl = ProcedureDeclaration.expectProcedureDeclaration(simBuilder, type);
			}
			else if (Parse.accept(simBuilder, KeyWord.ARRAY)) {
				return ArrayDeclaration.expectArrayDeclaration(simBuilder, type);
			}
			else 
				return SimpleVariableDeclaration.expectSimpleVariable(simBuilder, type);
			
			if (Option.internal.TRACE_PARSE)
				Parse.TRACE("Parse Declaration(2)");
		}
		Vector<SyntaxElement> declarations = new Vector<SyntaxElement>();
		declarations.add(decl);
		return declarations;
	}


	// ***********************************************************************************************
	// *** Utility: isCompatibleClasses -- Used by IS/IN/QUA-checking and Inspect WHEN
	// ***********************************************************************************************
	/// Check if these classes are compatible.
	/// @param other the other ClassDeclaration
	/// @return the resulting boolean value
	public boolean isCompatibleClasses(final Declaration other) {
		if (!(this instanceof ClassDeclaration))
			Util.error("" + this + " is not a class");
		if (!(other instanceof ClassDeclaration))
			Util.error("" + other + " is not a class");

		if (((ClassDeclaration) this).isSubClassOf((ClassDeclaration) other))
			return (true);
		return (((ClassDeclaration) other).isSubClassOf((ClassDeclaration) this));
	}

	/// Output Java ByteCode. Treat Declaration.
	/// @param classBuilder the classBuilder to use.
	/// @param encloser the owning block.
	public void buildDeclaration(ClassBuilder classBuilder,BlockDeclaration encloser) {
		Util.IERR("Method buildDeclaration need a redefinition in "+this.getClass().getSimpleName());
	}

	/// ClassFile coding utility: get getFieldIdentifier.
	/// @return the resulting String.
	public String getFieldIdentifier() {
		Util.IERR("Method getFieldIdentifier need a redefinition in "+this.getClass().getSimpleName());
		return(null);
	}

	/// Output Java ByteCode. Build init code for an Attribute.
	/// @param codeBuilder the codeBuilder to use.
	public void buildInitAttribute(CodeBuilder codeBuilder) {
		CoreGlobal.sourceLineNumber = lineNumber;
		Util.IERR("Method buildInitAttribute need a redefinition in "+this.getClass().getSimpleName());
	}

	/// Output Java ByteCode. Build declaration code for an Attribute.
	/// @param codeBuilder the codeBuilder to use.
	public void buildDeclarationCode(CodeBuilder codeBuilder) {
		CoreGlobal.sourceLineNumber = lineNumber;
		// Default: No code
	}
	
	/// Debug utility: verifyTree.
	/// @param head of the tree.
	protected void verifyTree(final Object head) {
		if(head instanceof Declaration decl) {
			if(! decl.identifier.equals(this.declaredIn.identifier)) {
				IO.println("\nDeclaration.verifyTree: Treating: "+this);
				IO.println("Declaration.verifyTree: "+this.getClass().getSimpleName()+".head="+head);
				IO.println("Declaration.verifyTree: head.identifier=" + decl.identifier);
				IO.println("Declaration.verifyTree: declaredIn.identifier=" + declaredIn.identifier);
				Util.IERR("verifyTree FAILED");
			}
		}
	}

}
