/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.declaration;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simula.compiler.AttributeFileIO;
import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.JarFileBuilder;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.Type;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Util;
import simula.psi.LexToken;
import simula.psi.PsiBuilder;
import simula.psi.PsiParse;
import simula.token.SimpleString;

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
	private ExternalDeclaration(final PsiBuilder psiBuilder, String identifier,String extIdentitier) {
		super(psiBuilder.psiTree, identifier);
		this.declarationKind = ObjectKind.ExternalDeclaration;
		this.externalIdent = extIdentitier;
	}
	
	/// Private Constructor used by Attribute File I/O.
	private ExternalDeclaration() {
		super(null, null);
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
	public static Vector<SyntaxElement> expectExternalDeclaration(final PsiBuilder psiBuilder) {
		LexToken kind = PsiParse.acceptIdentifier(psiBuilder);
		if (kind != null)
			Util.error("*** NOT IMPLEMENTED: " + "External " + kind + " Procedure");
		Type expectedType = PsiParse.acceptType(psiBuilder);
		if (!(PsiParse.accept(psiBuilder, KeyWord.CLASS) || PsiParse.accept(psiBuilder, KeyWord.PROCEDURE)))
			Util.error("parseExternalDeclaration: Expecting CLASS or PROCEDURE");

		Vector<SyntaxElement> declarations = new Vector<SyntaxElement>();
		String identifier = PsiParse.expectIdentifier(psiBuilder).edText();
		LOOP: while (true) {
//			IO.println("ExternalDeclaration.expectExternalDeclaration: identifier=" + identifier);
			String externalIdentifier = null;
			if (PsiParse.accept(psiBuilder, KeyWord.EQ)) {
//				externalIdentifier = Parse.currentToken;
//				Parse.expect(KeyWord.TEXTKONST);
				LexToken token = PsiParse.getCurrentParserToken(psiBuilder);
//				IO.println("ExternalDeclaration.expectExternalDeclaration: " + token.getClass().getSimpleName());
				if(token instanceof SimpleString xident) {
					String extIdentifier = xident.value;
//					IO.println("ExternalDeclaration.expectExternalDeclaration: extIdentifier" + extIdentifier);
				} else {
					Util.error(token, "Expecting external identifier string");
				}
			}
			ExternalDeclaration externalDeclaration = new ExternalDeclaration(psiBuilder, identifier, externalIdentifier);
			externalDeclaration.type = expectedType;
			declarations.add(externalDeclaration);
//			IO.println("ExternalDeclaration.expectExternalDeclaration: externalDeclaration" + externalDeclaration);

			File jarFile = JarFileBuilder.findJarFile(identifier, externalIdentifier);
			if(jarFile == null) {
				Util.IERR(""+jarFile);
			}
			if (jarFile != null) {
				if(checkJarFiles(jarFile)) {
					DeclarationScope scope = Global.getCurrentScope();
					Type moduleType = AttributeFileIO.readAttributeFile(identifier, jarFile, scope.getEnclosingBlock());
					if(moduleType == null) {
						if (expectedType != null) Util.error("Missing external type: "+expectedType);
					} else if(expectedType == null) {
						// NOTHING
					} else if (!moduleType.equals(expectedType)) {
						if (expectedType != null)
							Util.error("Wrong external type: "+moduleType+". Expected type: "+expectedType);
					}
				}
			}

			if (PsiParse.accept(psiBuilder, KeyWord.IS)) {
				Util.error("*** NOT IMPLEMENTED: " + "External non-Simula Procedure");
				break LOOP;
			}
			if (!PsiParse.accept(psiBuilder, KeyWord.COMMA))
				break LOOP;
			identifier = PsiParse.expectIdentifier(psiBuilder).edText();
		}

		return declarations;
	}


	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		Global.sourceLineNumber = firstLineNumber();
		DeclarationScope declaredIn = Global.getCurrentScope();
		// ...
		
		IO.println("\n\nExternalDeclaration.doChecking: " + this);
		IO.println("ExternalDeclaration.doChecking: +++++++++++++++++ BASICIO ++++++++++++++++++++++");
//		StandardClass.BASICIO.printTree(0, decl);
		for(Declaration memb:StandardClass.BASICIO.declarationList) {
			IO.println("ExternalDeclaration.doChecking: " + memb);
		}
		IO.println("ExternalDeclaration.doChecking: +++++++++++++++++ CURRENT SCOPE "+Global.getCurrentScope().identifier +" ++++++++++++++++++++++");
		for(Declaration z:Global.getCurrentScope().declarationList) {
			IO.println("ExternalDeclaration.doChecking: " + z);
			if(z instanceof ExternalDeclaration ext) {
				
			}
		}

		
		IO.println("ExternalDeclaration.doChecking: " + this);
		SET_SEMANTICS_CHECKED();
	}

	/// Check if the jarFile is already included.
	/// @param jarFile the jarFile.
	/// @return false: if the jarFile is already included.
	private static boolean checkJarFiles(File jarFile) {
		for(File f:Global.externalJarFiles) if(f.equals(jarFile)) {
			Util.warning("External already included: "+jarFile.getName());
			return(false);
		}
		return true;
	}

	/// Read external Attribute file.
	public void readExternalAttributeFile() {
		File jarFile = JarFileBuilder.findJarFile(identifier, externalIdent);
		if (jarFile != null) {
			if(checkJarFiles(jarFile)) {
				BlockDeclaration enclosure = StandardClass.BASICIO;
				AttributeFileIO.readAttributeFile(identifier, jarFile, enclosure);
			}
		}		
	}


	@Override
	public JPanel getSyntaxPanel() {
		String[] table = {
				"  declarationKind:",	""+declarationKind+":"+ObjectKind.edit(declarationKind),
				"  declarationClass:",	""+this.getClass().getSimpleName(),
				"  lines:",				""+this.firstLineNumber() + " ==> " + this.lastLineNumber(),
				// *** Declaration
				"  identifier:",		""+identifier,
				"  externalIdent:",	    ""+externalIdent,
				"  type:",			    ""+type,
				"  declaredIn:",		""+declaredIn.identifier,
//				// *** SimpleVariableDeclaration
//				"  constant:",			""+constant,
//				"  constantElement: ",	""+constantElement
		};
		JPanel panel = new JPanel(new GridLayout(table.length/2, 2));
		Font monoFont = new Font(Font.MONOSPACED, Font.BOLD, 12);
		for(String s:table) {
			JLabel lab = new JLabel(s);
			lab.setFont(monoFont);
			panel.add(lab);
		}
		return panel;
	}

	public String toString() {
		return "ExternalDeclaration: identifier=" + identifier + ", externalIdent=" + externalIdent;
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
		writePsiTree(oupt);

		// *** Declaration
		oupt.writeString(identifier);
		oupt.writeString(externalIdent);
		oupt.writeType(type);// Declaration
	}
	
	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static ExternalDeclaration readObject(AttributeInputStream inpt) throws IOException {
		ExternalDeclaration ext = new ExternalDeclaration();
		ext.OBJECT_SEQU = inpt.readSEQU(ext);

		// *** SyntaxElement
		ext.psiTree = readPsiTree(inpt);

		// *** Declaration
		ext.identifier = inpt.readString();
		ext.externalIdent = inpt.readString();
		ext.type = inpt.readType();

		Util.TRACE_INPUT("readExternalDeclaration: " + ext);
		return(ext);
	}
	
}
