/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.util.LexPosition;
import simula.core.builder.util.LexRange;
import simula.core.builder.util.LexToken;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.declaration.Declaration;
import simula.core.utilities.Html;
import simula.core.utilities.Util;

/// The class SyntaxElement.
/// 
/// The Simula syntax is formally defined in the Simula Standard.
/// Some non-terminal symbols give rise to a Java class with almost the same name.
/// They are all subclasses of the class SyntaxElement.
/// The subclass hierarchy of the Syntax class is described below
/// 
/// <pre>
///            SyntaxElement
///               HiddenSpecification 
///               ProtectedSpecification 
///               Type 
///                  OverLoad
///               Declaration
///                  ArrayDeclaration 
///                  DeclarationScope
///                     BlockDeclaration
///                        ClassDeclaration 
///                           PrefixedBlockDeclaration
///                           StandardClass
///                        MaybeBlockDeclaration
///                        ProcedureDeclaration 
///                           StandardProcedure
///                           SwitchDeclaration
///                     ConnectionBlock 
///                  ExternalDeclaration
///                  Parameter 
///                  SimpleVariableDeclaration 
///                     LabelDeclaration 
///                  UndefinedDeclaration 
///                  VirtualMatch 
///                  VirtualSpecification 
///               Statement
///                  ActivationStatement
///                  BlockStatement
///                  ConditionalStatement
///                  ConnectionStatement
///                  DummyStatement
///                  ForStatement
///                  GotoStatement
///                  InnerStatement
///                  LabeledStatement
///                  ProgramModule
///                  StandaloneExpression
///                  SwitchStatement
///                  WhileStatement
///               Expression
///                  ArithmeticExpression
///                  AssignmentOperation
///                  BooleanExpression
///                  ConditionalExpression
///                  Constant 
///                  LocalObject
///                  ObjectGenerator
///                  ObjectRelation
///                  QualifiedObject
///                  RelationalOperation
///                  RemoteVariable
///                  TextExpression
///                  TypeConversion
///                  UnaryOperation
///                  VariableExpression          
/// </pre>
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/SyntaxElement.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public abstract class SyntaxElement {

	/// The associated Document Manager
//	public SimulaBuilder simBuilder;
	public DocumentManager documentManager;
	
	/// Not present for System or External modules
	public LexRange lexRange;

	/// Controls semantic checking.
	/// 
	/// Set true when the method doChecking() has been completed.
	protected boolean CHECKED = false;

	/// Object sequence number used by Attribute File I/O to fixup object references.
	/// 
	/// During Attribute File Input it is index to the Object Reference Table.
	/// See: Global.Object_SEQU
	public int OBJECT_SEQU;
	
	/// Create a new SyntaxElement.
	protected SyntaxElement(DocumentManager documentManager) {
		if(documentManager == null && (!(this instanceof Type))) Util.IERR("documentManager == null for "+this.getClass());
//		OLD_lineNumber = Global.sourceLineNumber;
//		this.simBuilder= simBuilder;
		this.documentManager= documentManager;
		if(documentManager != null && documentManager.simBuilder != null) {
//			this.firstParserToken = this.lastParserToken = documentManager.simBuilder.getCurrentParserToken();
			LexToken current = documentManager.simBuilder.getCurrentParserToken();
			LexPosition firstLexPosition = new LexPosition(current.lineNumber, current.column);
			LexPosition lastLexPosition = new LexPosition(current.lineNumber, current.column + current.length);
			this.lexRange = new LexRange(firstLexPosition, lastLexPosition);
		}
	}

//	/// The first source line number
//	public LexToken getFirstLexToken() {
////		IO.println("SyntaxElement.getFirstLexToken: "+this.getClass());
////		return lexTokenRange.getFirstLexToken();			
//		return firstParserToken;			
//	}
//
//	/// The last source line number
//	public LexToken getLastLexToken() {
////		return lexTokenRange.getLastLexToken();			
//		return lastParserToken;			
//	}

	/// The first source line number
	public int firstLineNumber() {
//		if(firstParserToken == null) return 0;
//		return firstParserToken.firstLineNumber();			
//		if(firstLexPosition == null) return 0;
//		return firstLexPosition.getLine();			
		if(lexRange == null) return 0;
		return lexRange.getStart().getLine();			
	}

	/// The last source line number
	public int lastLineNumber() {
//		if(lastParserToken == null) return 0;
//		return lastParserToken.lastLineNumber();			
//		if(lastLexPosition == null) return 0;
//		return lastLexPosition.getLine();			
		if(lexRange == null) return 0;
		return lexRange.getEnd().getLine();			
	}
	
	/// Perform semantic checking.
	/// 
	/// This must be redefined in every subclass.
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		Util.IERR("The method 'doChecking' needs a redefinition in "+this.getClass().getSimpleName());
	}

	/// Set semantic checked.
	/// 
	/// Should be called from all doChecking methods to signal that semantic checking is done.
	public void SET_SEMANTICS_CHECKED() {
		CHECKED = true;
	}

	/// Returns true if semantic checking is done.
	/// 
	/// @return true if semantic checking is done
	public boolean IS_SEMANTICS_CHECKED() {
		return (CHECKED);
	}

	/// Assert that semantic checking done.
	protected void ASSERT_SEMANTICS_CHECKED() {
		if (!CHECKED) {
			IO.println("FATAL error - THE Semantic checker not called: " + this.getClass().getName() + ", " + this);
			Util.IERR("");
			System.exit(-1);
		}
		if (this instanceof Declaration decl) {
			if (decl.externalIdent == null) {
				Thread.dumpStack();
				Util.generalError("External Identifier is not set -- "+this.getClass().getSimpleName()+"  "+this);
			}
		}
	}

	/// Output possible declaration Java code.
	public void doDeclarationCoding(final SimulaCoder simCoder) {}

	/// Output Java code.
	public void doJavaCoding(final SimulaCoder simCoder) {
		CoreGlobal.sourceLineNumber = firstLineNumber();
		JavaSourceFileCoder.code(simCoder,toJavaCode());
	}

	/// Generate Java code for this Syntax Class.
	/// 
	/// @return Java code
	public String toJavaCode() {
		return (toString());
	}

	/// Build Java ClassFile ByteCode.
	/// @param codeBuilder the codeBuilder to use.
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		Util.IERR("Method buildByteCode need a redefinition in "+this.getClass().getSimpleName()
				+"\n\n            MAYBE: Use buildEvaluation(boolean destination,CodeBuilder codeBuilder)\n");
	}

	/// Utility print method.
	/// 
	/// @param indent number of spaces leading the line
	public void print(final int indent) {
		IO.println(edIndent(indent) + this.getClass().getSimpleName() + ": " + this);
	}

	/// Utility print syntax tree method.
	/// 
	/// @param indent number of spaces leading the lines 
	/// @param head the head of the tree.
	public void printTree(final int indent) {
		Util.IERR("Method printTree need a redefinition in "+this.getClass().getSimpleName());
	}

	/// Utility: Returns a number of blanks followed by qualification of this
	/// 
	/// @param indent the number of blanks requested
	/// @return the resulting string
	public String edTreeIndent(final int indent) {
		int i = indent;
		String s = "";
		while ((i--) > 0)
			s = s + "    ";
		return (s+"Line "+this.firstLineNumber()+": "+this.getClass().getSimpleName()+"    ");
	}

	/// Utility: Returns a number of blanks.
	/// 
	/// @param indent the number of blanks requested
	/// @return a number of blanks.
	public static String edIndent(final int indent) {
		int i = indent;
		String s = "";
		while ((i--) > 0)
			s = s + "    ";
		return (s);
	}

//	public String edPsi(String phrase) {
//		int lno = firstLineNumber();
//		StringBuilder sb = new StringBuilder("Line ").append(lno);
//		int lastLine = this.lastLineNumber();
//		if(lastLine != lno) sb.append('-').append(lastLine);
//		sb.append(": ").append(getClass().getSimpleName()).append(": ").append(phrase);
//		return sb.toString();
//	}
	public String edPsi(String phrase) {
		int lno = firstLineNumber();
		int lastLine = this.lastLineNumber();
		return Html.edPsi(lno, lastLine, getClass().getSimpleName() + ": " + phrase);
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	/// Write a SyntaxElement object to a AttributeOutputStream.
	/// @param oupt the AttributeOutputStream to write to.
	/// @throws IOException if something went wrong.
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.IERR("Method 'writeObject' needs a redefinition in "+this.getClass().getSimpleName());
	}

	/// Read and return a SyntaxElement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static SyntaxElement readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		Util.IERR("Method 'readObject' needs a redefiniton");
		return(null);
	}

}
