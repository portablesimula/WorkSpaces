/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.declaration;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeFileIO;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.export.LexToken;
import simula.core.builder.util.Identifier;
import simula.core.builder.util.SimpleString;
import simula.core.coder.JarFileBuilder;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.syntaxClass.Type;
import simula.core.utilities.KeyWord;
import simula.core.utilities.LOG;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

/// External Declaration.
/// <pre>
/// Simula Standard: 6.1 External declarations
/// 
///   external-head = external-declaration ; { external-declaration ; }
///   
///   external-declaration
///      = external-procedure-declaration | external-class-declaration
/// </pre>
/// An external declaration is a substitute for a complete introduction of the
/// corresponding source module referred to, including its external head. In the
/// case where multiple but identical external declarations occur as a
/// consequence of this rule, this declaration will be incorporated only once.
/// 
/// 
/// External Class Declaration
/// 
/// <pre>
///    external-class-declaration
///        =  EXTERNAL  CLASS  external-list
/// </pre>
/// 
/// An implementation may restrict the number of block levels at which an
/// external class declaration may occur.
/// 
/// Note: As a consequence of 5.5.1 all classes belonging to the prefix chain of
/// a separately compiled class must be declared in the same block as this class.
/// However, this need not be done explicitly; an external declaration of a
/// separately compiled class implicitly declares all classes in its prefix chain
/// (since these will be declared in the external head of the class in question).
/// 
/// 
/// 
/// 
/// External procedure declaration
/// 
/// <pre>
/// 
/// external-procedure-declaration
///         = EXTERNAL [ kind ] [ type ] PROCEDURE external-list
///         | EXTERNAL kind PROCEDURE external-item  IS procedure-declaration
///         
///    external-list = external-item { , external-item }
/// 	  external-item = identifier [ "=" external-identification ]
/// 
/// 		 kind  =  identifier  // E.g. FORTRAN, JAVA, ...
/// 		 external-identification = string   // E.g  a file-name
/// 
/// </pre>
/// 
/// The kind of an external procedure declaration may indicate the source
/// language in which the separately compiled procedure is written (e.g assembly,
/// Cobol, Fortran, PL1 etc.). The kind must be empty if this language is Simula.
/// The interpretation of kind (if given) is implementation-dependent.
/// 
/// If an external procedure declaration contains a procedure specification, the
/// procedure body of the procedure declaration must be empty. This specifies a
/// procedure whose actual body, which embodies the algorithm required, is
/// supplied in a separate (non-Simula) module. The procedure heading of the
/// procedure declaration will determine the procedure identifier (function
/// designator) to be used within the source module in which the external
/// declaration occurs, as well as the type, order, and transmission mode of the
/// parameters.
/// 
/// A non-Simula procedure cannot be used as an actual parameter corresponding to
/// a formal procedure.
///  
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/declaration/ExternalDeclaration.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class ExternalDeclaration extends Declaration {
	
	/// Create a new ExternalDeclaration.
	/// @param identifier the identifier.
	/// @param extIdentitier the external identifier.
	private ExternalDeclaration(final DocumentManager documentManager, Identifier identifier,String extIdentitier) {
		super(documentManager, identifier);
		this.declarationKind = ObjectKind.ExternalDeclaration;
		this.externalIdent = extIdentitier;
	}
	
	/// Private Constructor used by Attribute File I/O.
	private ExternalDeclaration(final DocumentManager documentManager) {
		super(documentManager, null);
		this.declarationKind = ObjectKind.ExternalDeclaration;
	}

	/// Parse an external declaration updating a declaration list.
	/// <pre>
	///    external-head = external-declaration ; { external-declaration ; }
	/// 
	///       external-class-declaration
	///            =  EXTERNAL  CLASS  external-list
	///        
	///       external-procedure-declaration
	///            = EXTERNAL [ kind ] [ type ] PROCEDURE external-list
	///            | EXTERNAL kind PROCEDURE external-item  IS procedure-declaration
	/// 
	///			 external-list = external-item { , external-item }
	/// 
	/// 			external-item = identifier [ = external-identification ]
	/// 
	/// 				external-identification = string
	/// </pre>
	/// Precondition: EXTERNAL  is already read.
	/// @param enclosure the BlockDeclaration which is updated
	/// @return a Vector of ExternalDeclaration
	public static Vector<SyntaxElement> expectExternalDeclaration(final SimulaBuilder simBuilder) {
		LexToken kind = Parse.acceptIdentifier(simBuilder);
		if (kind != null)
			Util.syntaxError(simBuilder, "*** NOT IMPLEMENTED: " + "External " + kind + " Procedure");
		Type expectedType = Parse.acceptType(simBuilder);
//		if (!(PsiParse.accept(simBuilder, KeyWord.CLASS) || PsiParse.accept(simBuilder, KeyWord.PROCEDURE)))
		if (!(Parse.accept(simBuilder, KeyWord.CLASS, KeyWord.PROCEDURE)))
//			Util.syntaxError(simBuilder.getCurrentParserToken(), "parseExternalDeclaration: Expecting CLASS or PROCEDURE");
			Util.syntaxError(simBuilder, "parseExternalDeclaration: Expecting CLASS or PROCEDURE");

		Vector<SyntaxElement> declarations = new Vector<SyntaxElement>();
		Identifier identifier = Parse.expectIdentifier(simBuilder);
		LOOP: while (true) {
//			IO.println("ExternalDeclaration.expectExternalDeclaration: identifier=" + identifierValue());
			String externalIdentifier = null;
			if (Parse.accept(simBuilder, KeyWord.EQ)) {
//				externalIdentifier = Parse.currentToken;
//				Parse.expect(KeyWord.TEXTKONST);
				LexToken token = Parse.getCurrentParserToken(simBuilder);
//				IO.println("ExternalDeclaration.expectExternalDeclaration: " + token.getClass().getSimpleName());
				if(token instanceof SimpleString xident) {
					externalIdentifier = xident.value;
					LOG.info("ExternalDeclaration.expectExternalDeclaration: extIdentifier" + externalIdentifier);
				} else {
					Util.syntaxError(simBuilder, token, "Expecting external identifier string");
				}
				simBuilder.getNextParserToken();
			}
			ExternalDeclaration externalDeclaration = new ExternalDeclaration(simBuilder.documentManager, identifier, externalIdentifier);
			externalDeclaration.type = expectedType;
			declarations.add(externalDeclaration);
//			IO.println("ExternalDeclaration.expectExternalDeclaration: externalDeclaration: " + externalDeclaration);

			File jarFile = JarFileBuilder.findJarFile(simBuilder, identifier.value, externalIdentifier);
//			IO.println("ExternalDeclaration.expectExternalDeclaration: jarFile: " + jarFile);
			if(jarFile == null) {
				Util.syntaxError(simBuilder, "Can't find attribute file: " + identifier.value + '[' + externalIdentifier + ']');
			}
			if (jarFile != null) {
				if(AttributeFileIO.checkJarFiles(simBuilder, jarFile.toString())) {
					DeclarationScope scope = CoreGlobal.getCurrentScope();
					Type moduleType = AttributeFileIO.readAttributeEntry(simBuilder, identifier.value, jarFile, scope.getEnclosingBlock());
					if(moduleType == null) {
						if (expectedType != null) Util.syntaxError(simBuilder, "Missing external type: "+expectedType);
					} else if(expectedType == null) {
						// NOTHING
					} else if (!moduleType.equals(expectedType)) {
						if (expectedType != null)
							Util.syntaxError(simBuilder, "Wrong external type: "+moduleType+". Expected type: "+expectedType);
					}
				}
			}

			if (Parse.accept(simBuilder, KeyWord.IS)) {
				Util.syntaxError(simBuilder, "*** NOT IMPLEMENTED: " + "External non-Simula Procedure");
				break LOOP;
			}
			if (!Parse.accept(simBuilder, KeyWord.COMMA))
				break LOOP;
			identifier = Parse.expectIdentifier(simBuilder);
		}

		return declarations;
	}


	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		DeclarationScope declaredIn = CoreGlobal.getCurrentScope();
		// ...
		
		IO.println("\n\nExternalDeclaration.doChecking: " + this);
		IO.println("ExternalDeclaration.doChecking: +++++++++++++++++ BASICIO ++++++++++++++++++++++");
//		StandardClass.BASICIO.printTree(0, decl);
		for(Declaration memb:StandardClass.BASICIO.declarationList) {
			IO.println("ExternalDeclaration.doChecking: " + memb);
		}
		IO.println("ExternalDeclaration.doChecking: +++++++++++++++++ CURRENT SCOPE "+CoreGlobal.getCurrentScope().identifierValue() +" ++++++++++++++++++++++");
		for(Declaration z:CoreGlobal.getCurrentScope().declarationList) {
			IO.println("ExternalDeclaration.doChecking: " + z);
			if(z instanceof ExternalDeclaration ext) {
				
			}
		}

		
		IO.println("ExternalDeclaration.doChecking: " + this);
		SET_SEMANTICS_CHECKED();
	}

//	/// Check if the jarFile is already included.
//	/// @param jarFile the jarFile.
//	/// @return false: if the jarFile is already included.
//	private static boolean checkJarFiles(File jarFile) {
//		for(File f:Global.externalJarFiles) if(f.equals(jarFile)) {
//			Util.warning("External already included: "+jarFile.getName());
//			return(false);
//		}
//		return true;
//	}
//
//	/// Read external Attribute file.
//	public void readExternalAttributeFile() {
//		File jarFile = JarFileBuilder.findJarFile(identifier, externalIdent);
//		if (jarFile != null) {
//			if(checkJarFiles(jarFile)) {
//				BlockDeclaration enclosure = StandardClass.BASICIO;
//				AttributeFileIO.readAttributeFile(identifier, jarFile, enclosure);
//			}
//		}		
//	}


	public String toString() {
		return "ExternalDeclaration: identifier=" + identifierValue() + ", externalIdent=" + externalIdent;
	}


	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeExternalDeclaration: " + this);
		oupt.writeKind(declarationKind);
		oupt.writeShort(OBJECT_SEQU);

		// *** SyntaxElement
		

		// *** Declaration
		oupt.writeIdentifier(identifier);
		oupt.writeString(externalIdent);
		oupt.writeType(type);// Declaration
	}
	
	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static ExternalDeclaration readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		ExternalDeclaration ext = new ExternalDeclaration(documentManager);
		ext.OBJECT_SEQU = inpt.readSEQU(ext);

		// *** SyntaxElement

		// *** Declaration
		ext.identifier = inpt.readIdentifier();
		ext.externalIdent = inpt.readString();
		ext.type = inpt.readType();

		Util.TRACE_INPUT("readExternalDeclaration: " + ext);
		return(ext);
	}
	
}
