/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass;

import java.awt.BorderLayout;
import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.declaration.Declaration;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Html;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Util;
import simula.psi.ExternalPsiTree;
import simula.psi.LexToken;
import simula.psi.PsiBuilder;
import simula.psi.PsiElement;
import simula.psi.PsiTree;

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

	/// The associated PSI Builder
	public PsiBuilder psiBuilder;
	
	/// Set by PsiBuilder.doneSubtree
	/// The associated PSI Tree
	public PsiTree psiTree;

//	/// All errors associated with this SyntaxElement
//	public Vector<String> errors;

	/// Returns the associated PSI Tree
	/// This method may be redefined.
	/// @return the associated PSI Tree
	public PsiTree getPsiTree() {
//		if(psiBuilder == null) return PsiTree.dummyTree;
//		PsiTree psiTree = psiBuilder.psiTree;
//		return (psiTree != null)? psiTree : PsiTree.dummyTree;
		return psiTree;
	}

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
	protected SyntaxElement(PsiBuilder psiBuilder) {
//		OLD_lineNumber = Global.sourceLineNumber;
//		this.psiTree = (psiTree != null)? psiTree : PsiTree.dummyTree;
		this.psiBuilder= psiBuilder;
//		if(psiBuilder == null) { Util.IERR("NEW SyntaxElement: "+this.getClass().getSimpleName() + "  psiBuilder == null");
		this.psiTree = (psiBuilder == null)? PsiTree.dummyTree : psiBuilder.psiTree;
	}

	/// The first source line number
	public int firstLineNumber() {
		int lno = psiTree.firstLineNumber();
//		if(lno < 0) System.err.println("Illegal LintNumber: " + lno + " IN " + this.getClass().getSimpleName()+" "+this);
		return lno;
	}

	/// The last source line number
	public int lastLineNumber() {
		return psiTree.lastLineNumber();
	}

	
	public void addError(String err) {
//		if(errors == null) errors = new Vector<String>();
//		errors.add(err);
//		IO.println("SyntaxElement.addError: TREATING " + this.getClass().getSimpleName() + " " + this + "  ERR="+err);
		for(PsiElement elt : psiTree.getChildren()) {
			if(elt instanceof LexToken token) {
				switch(token.keyWord) {
					case KeyWord.NEWLINE:
					case KeyWord.WHITESPACES:
					case KeyWord.COMMENT_TEXT:
						break;
					default:
//						IO.println("SyntaxElement.addError: ADD TO " + token);
					    token.addError(err);
				}
			}
		}
	}
	
    public void addSyntaxNodes(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {
		IO.println("Method addSyntaxNodes need a redefinition in "+this.getClass().getSimpleName());
		Util.IERR("Method addSyntaxNodes need a redefinition in "+this.getClass().getSimpleName());
    }


	public void popUpSyntaxPanel() {
		JPanel panel = getSyntaxPanel();
//		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Syntax Info");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        try { frame.setIconImage(Global.favicon.getImage()); } 
	        catch (Exception e) {}// Util.IERR("Impossible",e); }

			JScrollPane scrollPane = new JScrollPane(panel);
			frame.add(scrollPane, BorderLayout.CENTER);

			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
//		});
	}

	/// Redefined in all subclasses
	public JPanel getSyntaxPanel() {
		IO.println("Method getSyntaxPanel need a redefinition in "+this.getClass().getSimpleName());
		Util.IERR("Method getSyntaxPanel need a redefinition in "+this.getClass().getSimpleName());
		return null;
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

	public void writePsiTree(AttributeOutputStream oupt) throws IOException {
		oupt.writeShort(firstLineNumber());
		oupt.writeShort(lastLineNumber());
	}

	public static PsiTree readPsiTree(AttributeInputStream inpt) throws IOException {
		int firstLineNumber = inpt.readShort();
		int lastLineNumber = inpt.readShort();
		return new ExternalPsiTree("ExternalClass", firstLineNumber, lastLineNumber);
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
