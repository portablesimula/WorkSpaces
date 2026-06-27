/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.declaration.Declaration;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Html;
import simula.compiler.utilities.Util;
import simula.lsp.util.AstData;
import simula.psi.LexTokenRange;
import simula.psi.LexToken;
import simula.psi.AstBuilder;

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
	public AstData astData; // DETTE MÅ SKRIVES

	/// The associated AST Builder
	public AstBuilder astBuilder;
	
//	/// Set by PsiBuilder.doneSubtree
//	/// The associated PSI Tree
//	public PsiTree psiTree;
	
	/// Set by PsiBuilder.doneSubtree
	/// The associated lexTokenRange
	public LexTokenRange lexTokenRange; // TESTING_WITHOUT_PSI

//	/// All errors associated with this SyntaxElement
//	public Vector<String> errors;

	/// Returns the associated PSI Tree
	/// This method may be redefined.
	/// @return the associated PSI Tree
//	public PsiTree getPsiTree() {
//		return psiTree;
//	}

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
	protected SyntaxElement(AstBuilder astBuilder) {
//		OLD_lineNumber = Global.sourceLineNumber;
//		this.psiTree = (psiTree != null)? psiTree : PsiTree.dummyTree;
		this.astBuilder= astBuilder;
//		if(astBuilder == null) { Util.IERR("NEW SyntaxElement: "+this.getClass().getSimpleName() + "  astBuilder == null");
//		this.psiTree = (astBuilder == null)? PsiTree.dummyTree : astBuilder.psiTree;
	}

//	protected void startParse(final LexToken firstLexToken) { // TESTING_WITHOUT_PSI
//		IO.println("SyntaxElement.startParse: " + this.getClass().getSimpleName() + " with token: " + firstLexToken);
//		lexTokenRange.firstLexToken = firstLexToken;
//		lexTokenRange.lastLexToken = firstLexToken;
//	}
//
//	protected void doneParse(final LexToken lastLexToken) { // TESTING_WITHOUT_PSI
//		lexTokenRange.lastLexToken = lastLexToken;
//	}

	/// The first source line number
	public LexToken getFirstLexToken() {
//		if(Option.TESTING_WITHOUT_PSI) {
			return lexTokenRange.getFirstLexToken();			
//		} else {
//			Util.IERR("");
//			return null;
//		}
	}

	/// The last source line number
	public LexToken getLastLexToken() {
//		if(Option.TESTING_WITHOUT_PSI) {
			return lexTokenRange.getLastLexToken();			
//		} else {
//			Util.IERR("");
//			return null;
//		}
	}

	/// The first source line number
	public int firstLineNumber() {
//		if(Option.TESTING_WITHOUT_PSI) {
			if(lexTokenRange == null) return -99;
			return lexTokenRange.getFirstLexToken().firstLineNumber();			
//		} else {
//			return psiTree.firstLineNumber();
//		}
	}

	/// The last source line number
	public int lastLineNumber() {
//		if(Option.TESTING_WITHOUT_PSI) {
			if(lexTokenRange == null) return +99;
			return lexTokenRange.getLastLexToken().lastLineNumber();			
//		} else {
//			return psiTree.lastLineNumber();
//		}
	}

	
	public void addError(String err) {
//		if(Option.TESTING_WITHOUT_PSI) {
			Util.IERR("SKAL IKKE BRUKES");
//		} else {
//			for(PsiElement elt : psiTree.getChildren()) {
//				if(elt instanceof LexToken token) {
//					switch(token.keyWord) {
//						case KeyWord.NEWLINE:
//						case KeyWord.WHITESPACES:
//						case KeyWord.COMMENT_TEXT:
//							break;
//						default:
//							IO.println("SyntaxElement.addError: ADD TO " + token);
//						    token.addError(err);
//					}
//				}
//			}
//		}
	}
	
	/// Perform semantic checking.
	/// 
	/// This must be redefined in every subclass.
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		Global.sourceLineNumber = firstLineNumber();
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
	public void doDeclarationCoding() {}

	/// Output Java code.
	public void doJavaCoding() {
		Global.sourceLineNumber = firstLineNumber();
		JavaSourceFileCoder.code(toJavaCode());
	}

	/// Generate Java code for this Syntax Class.
	/// 
	/// @return Java code
	public String toJavaCode() {
		return (toString());
	}

	/// Build Java ClassFile ByteCode.
	/// @param codeBuilder the codeBuilder to use.
	public void buildByteCode(CodeBuilder codeBuilder) {
		Util.IERR("Method buildByteCode need a redefinition in "+this.getClass().getSimpleName()
				+"\n\n            MAYBE: Use buildEvaluation(boolean destination,CodeBuilder codeBuilder)\n");
	}

	/// Utility print method.
	/// 
	/// @param indent number of spaces leading the line
	public void print(final int indent) {
		Util.println(edIndent(indent) + this);
	}

	/// Utility print syntax tree method.
	/// 
	/// @param indent number of spaces leading the lines
	/// @param head the head of the tree.
	public void printTree(final int indent, final Object head) {
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

//	public void writePsiTree(AttributeOutputStream oupt) throws IOException {
//		oupt.writeShort(firstLineNumber());
//		oupt.writeShort(lastLineNumber());
//	}
//
//	public static PsiTree readPsiTree(AttributeInputStream inpt) throws IOException {
//		int firstLineNumber = inpt.readShort();
//		int lastLineNumber = inpt.readShort();
//		return new ExternalPsiTree("ExternalClass", firstLineNumber, lastLineNumber);
//	}

	public void writeAstData(AttributeOutputStream oupt) throws IOException {
		oupt.writeShort(firstLineNumber());
		oupt.writeShort(lastLineNumber());
	}

	public static AstData readAstData(AttributeInputStream inpt) throws IOException {
		int firstLineNumber = inpt.readShort();
		int lastLineNumber = inpt.readShort();
		return new AstData("ExternalClass", firstLineNumber, lastLineNumber);
	}

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
	public static SyntaxElement readObject(AttributeInputStream inpt) throws IOException {
		Util.IERR("Method 'readObject' needs a redefiniton");
		return(null);
	}

}
