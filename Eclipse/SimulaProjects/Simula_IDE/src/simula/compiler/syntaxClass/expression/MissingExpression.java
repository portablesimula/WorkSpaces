/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.expression;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.Type;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.PsiBuilder;
import simula.psi.SyntaxTree;

/// Unary Operation.
/// 
/// <pre>
/// 
/// Syntax:
/// 
///   unary-operation =  unary-operator  Expression
///   
///      unary-operator = NOT | + | -
/// </pre>
/// Link to GitHub: <a href="https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/expression/UnaryOperation.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class MissingExpression extends Expression {

	/// Create a new MissingExpression.
	public MissingExpression(final PsiBuilder psiBuilder) {
		super(psiBuilder);
		this.type = Type.Undef;
	}

	@Override
	public void doChecking() {
//		if (IS_SEMANTICS_CHECKED())	return;
		SET_SEMANTICS_CHECKED();
	}

	// Returns true if this expression may be used as a statement.
	@Override
	public boolean maybeStatement() {
		ASSERT_SEMANTICS_CHECKED();
		return (false);
	}

	@Override
	public void buildEvaluation(Expression rightPart,CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		Util.IERR("NOT IMPL");
	}

	@Override
	public String toJavaCode() {
		ASSERT_SEMANTICS_CHECKED();
		Util.IERR("NOT IMPL");
		return null;
	}

	@Override
    public void addSyntaxNodes(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(edPsi(toString()));
        model.insertNodeInto(newNode, parent, parent.getChildCount());

//		SyntaxTree.addKeyWordNode(tree, model, newNode, oprator);
//		operand.addSyntaxNodes(tree, model, newNode);
		Util.IERR("NOT IMPL");

    }

	@Override
	public JPanel getSyntaxPanel() {
		String[] table = {
				// *** Expression
				"  type:",			    ""+type,
				"  backLink:",		    ""+backLink,
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

	@Override
	public String toString() {
		return ("MISSING_EXPRESSION");
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private MissingExpression() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeUnaryOperation: " + this);
		oupt.writeKind(ObjectKind.MissingExpression);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writePsiTree(oupt);
		// *** Expression
		oupt.writeType(type);
		oupt.writeObj(backLink);
	}
	
	/// Read and return an UnaryOperation object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the UnaryOperation object read from the stream.
	/// @throws IOException if something went wrong.
	public static MissingExpression readObject(AttributeInputStream inpt) throws IOException {
		MissingExpression expr = new MissingExpression();
		expr.OBJECT_SEQU = inpt.readSEQU(expr);
		// *** SyntaxElement
		expr.psiTree = readPsiTree(inpt);
		// *** Expression
		expr.type = inpt.readType();
		expr.backLink = (SyntaxElement) inpt.readObj();
		// *** UnaryOperation
		Util.TRACE_INPUT("readMissingExpression: " + expr);
		return(expr);
	}

}
