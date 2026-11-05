/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.lsp.syntaxClass;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;

import org.eclipse.lsp4j.Range;

import simula.lsp.DocumentManager;
import simula.lsp.builder.AttributeInputStream;
import simula.lsp.builder.AttributeOutputStream;
import simula.lsp.builder.JavaSourceFileCoder;
import simula.lsp.builder.SimulaBuilder;
import simula.lsp.builder.token.LexToken;
import simula.lsp.builder.token.LexTokenRange;
import simula.lsp.coder.SimulaCoder;
import simula.lsp.syntaxClass.declaration.Declaration;
import simula.lsp.utilities.LspGlobal;
import simula.lsp.utilities.Util;

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
//	public final DocumentManager documentManager;
	
	/// Not present for System or External modules
	public LexTokenRange tokenRange = new LexTokenRange();;

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
//	protected SyntaxElement(DocumentManager documentManager) {
//		this.documentManager= documentManager;
	
	public DocumentManager getDocumentManager() {
		if(tokenRange == null) return null;
		return tokenRange.getDocumentManager();
	}
	
	public SimulaBuilder getSimBuilder() {
		if(tokenRange == null) return null;
		return tokenRange.getSimBuilder();
	}

	public SimulaCoder getSimCoder() {
		if(tokenRange == null) return null;
		return tokenRange.getSimCoder();
	}

	public void setFirstParserToken(LexToken lexToken) {
		tokenRange.setFirstParserToken(lexToken);
	}
	public void setLastParserToken(LexToken lexToken) {
		tokenRange.setLastParserToken(lexToken);
	}
	public LexToken getFirstParserToken() {
		if(tokenRange == null) return null;
		return tokenRange.getFirstParserToken();
	}
	public LexToken getLastParserToken() {
		if(tokenRange == null) return null;
		return tokenRange.getLastParserToken();
	}
	
	public Range getLexRange() {
		if(tokenRange == null) return null;
		return tokenRange.getLexRange();
	}

	/// The first source line number (one based)
	public int sourceLineNumber() {
		return firstLineNumber() + 1;			
	}

	/// The first source line number (zero based)
	public int firstLineNumber() {
		if(tokenRange == null) return 0;
		return tokenRange.firstLineNumber();			
	}

	/// The last source line number (zero based)
	public int lastLineNumber() {
		if(tokenRange == null) return 0;
		return tokenRange.lastLineNumber();			
	}
	
	/// Perform semantic checking.
	/// 
	/// This must be redefined in every subclass.
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		LspGlobal.sourceLineNumber = firstLineNumber();
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
			Util.println("FATAL error - THE Semantic checker not called: " + this.getClass().getName() + ", " + this);
			Util.IERR("");
//			System.exit(-1);
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
		LspGlobal.sourceLineNumber = firstLineNumber();
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
		Util.println(edIndent(indent) + this.getClass().getSimpleName() + ": " + this);
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
	public static SyntaxElement readObject( final AttributeInputStream inpt) throws IOException {
		Util.IERR("Method 'readObject' needs a redefiniton");
		return(null);
	}

}
