/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.declaration;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.classfile.constantpool.FieldRefEntry;
import java.lang.constant.ClassDesc;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.Type;
import simula.compiler.syntaxClass.expression.Constant;
import simula.compiler.syntaxClass.expression.Expression;
import simula.compiler.syntaxClass.expression.TypeConversion;
import simula.compiler.utilities.DeclarationList;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.LexToken;
import simula.psi.PsiBuilder;
import simula.psi.PsiParse;
import simula.psi.PsiTree;
import simula.psi.SyntaxTree;
import simula.psi.TreeNodeIdent;

/// Simple Variable Declaration.
/// 
/// <pre>
/// 
/// Simula Standard: 5.1 Simple variable declarations
/// 
///  simple-variable-declaration
///        =  type  type-list
/// 
///    type-list
///        =  type-list-element  { , type-list-element }
/// 
///    type-list-element
///        =  identifier
///        |  constant-element 
/// 
///    constant-element
///        =  identifier  "="  value-expression
///        |  identifier  "="  text-expression
///   
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/declaration/SimpleVariableDeclaration.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public class SimpleVariableDeclaration extends Declaration {
	/// Constant indicator. Is used to prevent assignment of a new value.
	protected boolean constant;

	/// The constant initial value.
	public Expression constantElement;

	/// Create a new SimpleVariableDeclaration.
	/// 
	/// @param type       the variable type
	/// @param identifier the variable identifier
	public SimpleVariableDeclaration(final PsiTree psiTree, final Type type, final String identifier) {
		super(psiTree, identifier);
		this.declarationKind = ObjectKind.SimpleVariableDeclaration;
		this.type = type;
	}

	/// Create a new SimpleVariableDeclaration.
	/// @param type            the variable type
	/// @param identifier      the variable identifier
	/// @param constant        the constant indicator
	/// @param constantElement a constant initial value
	SimpleVariableDeclaration(final PsiTree psiTree, final Type type, final String identifier, final boolean constant, final Constant constantElement) {
		this(psiTree, type, identifier);
		this.constant = constant;
		this.constantElement = constantElement;
	}

	/// Constant indicator. Is used to prevent assignment of a new value.
	/// @return the constant indicator
	public boolean isConstant() {
		return (constant || constantElement != null);
	}

	/// Parse a simple variable declaration.
	/// <pre>
	/// 
	/// Syntax:
	/// 
	///  simple-variable-declaration
	///        =  type  type-list
	/// 
	///    type-list
	///        =  type-list-element  { , type-list-element }
	/// 
	///    type-list-element
	///        =  identifier
	///        |  constant-element 
	/// 
	///    constant-element
	///        =  identifier  "="  value-expression
	///        |  identifier  "="  text-expression
	///   
	/// </pre>
	/// Precondition: Type  is already read.
	/// Endcondition: The terminating semicolon is read.
	/// @param type the variable type
	/// @param declarationList the declaration list to update
	static void ZZ_expectSimpleVariable(final PsiBuilder psiBuilder, final Type type, final DeclarationList declarationList) {
		// identifier-list = identifier { , identifier }
		if (Option.internal.TRACE_PARSE)
			PsiParse.TRACE("Parse IdentifierList");
		LexToken sep = null;
		do {
//			String ident = PsiParse.expectIdentifier(psiBuilder).edText();
			String ident = PsiParse.expectIdentifierText(psiBuilder);
			if(ident == null) {
				ident = "undefined";
			}
			SimpleVariableDeclaration typeDeclaration = new SimpleVariableDeclaration(psiBuilder.psiTree, type, ident);
			if (PsiParse.accept(psiBuilder, KeyWord.EQ))
				typeDeclaration.constantElement = Expression.expectExpression(psiBuilder);
			
			sep = PsiParse.acceptParserToken(psiBuilder, KeyWord.SEMICOLON, KeyWord.COMMA); 
			if(sep == null) {
				LexToken current = PsiParse.currentLexToken(psiBuilder);
				Util.error(current, "Got symbol " + KeyWord.edit(current.keyWord) + " while expecting KeyWord COMMA or SEMICOLON");
				sep = PsiParse.skipUntil(psiBuilder, KeyWord.SEMICOLON);
			}
			
			declarationList.add(typeDeclaration);
			psiBuilder.doneSubtree(PsiTree.Kind.declaration, typeDeclaration);
			psiBuilder.startSubtree(PsiTree.Kind.declaration, "May be NextDeclaration");
//		} while (PsiParse.accept(psiBuilder, KeyWord.COMMA));
		} while (sep.keyWord == KeyWord.COMMA);
	}
	static void expectSimpleVariable(final PsiBuilder psiBuilder, final Type type, final DeclarationList declarationList) {
		// identifier-list = identifier { , identifier }
		Vector<SyntaxElement> syntaxElements = new Vector<SyntaxElement>();
		if (Option.internal.TRACE_PARSE)
			PsiParse.TRACE("Parse IdentifierList");
		do {
			String ident = PsiParse.expectIdentifier(psiBuilder).edText();
			SimpleVariableDeclaration typeDeclaration = new SimpleVariableDeclaration(psiBuilder.psiTree, type, ident);
			if (PsiParse.accept(psiBuilder, KeyWord.EQ))
				typeDeclaration.constantElement = Expression.expectExpression(psiBuilder);
			syntaxElements.add(typeDeclaration);
			declarationList.add(typeDeclaration);
//			psiBuilder.doneSubtree(PsiTree.Kind.declaration, typeDeclaration);
//			psiBuilder.startSubtree(PsiTree.Kind.declaration, "NextDeclaration");
		} while (PsiParse.accept(psiBuilder, KeyWord.COMMA));

		PsiParse.expect(psiBuilder, KeyWord.SEMICOLON);
		psiBuilder.doneSubtree(PsiTree.Kind.declaration, syntaxElements);
		psiBuilder.startSubtree(PsiTree.Kind.declaration, "NextDeclaration");
	}


	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		Global.sourceLineNumber = firstLineNumber();
		type.doChecking(Global.getCurrentScope());
		if (constantElement != null) {
			constantElement.doChecking();
			constantElement = TypeConversion.testAndCreate(type, constantElement);
			constantElement.type = type;
			constantElement.backLink = this;
		}
		
		if (Global.getCurrentScope() instanceof ClassDeclaration cls) {
			if (cls.prefixLevel() > 0)
				externalIdent = identifier + '_' + cls.prefixLevel();
			else
				externalIdent = identifier;
		}
		
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doDeclarationCoding() {
		if (constantElement != null && !(constantElement instanceof Constant)) {
			// Initiate Final Variable
			String value = constantElement.toJavaCode();
			JavaSourceFileCoder.code(getJavaIdentifier() + '=' + value + ';');
		}
	}

	@Override
	public String toJavaCode() {
		ASSERT_SEMANTICS_CHECKED();
		String modifier = "public ";
		if (this.isConstant())
			modifier = modifier + "final ";
		if (constantElement != null) {
			constantElement = TypeConversion.testAndCreate(type, constantElement.evaluate());
			constantElement.doChecking();
			if (constantElement instanceof Constant) {
				String value = constantElement.toJavaCode();
				String putValue = TypeConversion.mayBeConvert(constantElement.type, type, value);
				return (modifier + type.toJavaType() + ' ' + getJavaIdentifier() + putValue);
			} else {
				return (modifier + type.toJavaType() + ' ' + getJavaIdentifier() + ';');
			}
		}
		String value = type.edDefaultValue();
		return (modifier + type.toJavaType() + ' ' + getJavaIdentifier() + '=' + value + ';');
	}

	
	/// ClassFile coding utility: get FieldRefEntry of this SimpleVariable.
	/// @param pool the ConstantPoolBuilder to use.
	/// @return the FieldRefEntry of this SimpleVariable.
	public FieldRefEntry getFieldRefEntry(ConstantPoolBuilder pool) {
		ClassDesc owner=declaredIn.getClassDesc();
		return(pool.fieldRefEntry(owner, getFieldIdentifier(), type.toClassDesc()));
	}
	
	@Override
	public String getFieldIdentifier() {
		return(this.externalIdent);
	}

	@Override
	public void buildDeclaration(ClassBuilder classBuilder,BlockDeclaration encloser) {
		ClassDesc CD=type.toClassDesc();
		classBuilder.withField(getFieldIdentifier(), CD, ClassFile.ACC_PUBLIC);
	}

	@Override
	public void buildInitAttribute(CodeBuilder codeBuilder) {
		codeBuilder.aload(0);
		switch(type.keyWord) {
			case Type.T_BOOLEAN:
			case Type.T_CHARACTER:
			case Type.T_INTEGER:	codeBuilder.iconst_0(); break;
			case Type.T_LONG_REAL:	codeBuilder.dconst_0(); break;
			case Type.T_REAL:		codeBuilder.fconst_0(); break;
			case Type.T_TEXT:
			case Type.T_REF:
			case Type.T_PROCEDURE:
			case Type.T_LABEL:		codeBuilder.aconst_null(); break;
			default: Util.IERR();
		}
		codeBuilder
			.putfield(codeBuilder.constantPool().fieldRefEntry(BlockDeclaration.currentClassDesc(),this.getFieldIdentifier(), type.toClassDesc()));
	}

	@Override
	public void printTree(final int indent, final Object head) {
		verifyTree(head);
		IO.println(edTreeIndent(indent)+this);
	}

	@Override
    public void addSyntaxNodes(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {
//        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(edPsi(edHtml()));
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new TreeNodeIdent(this, edPsi(edHtml())));
        model.insertNodeInto(newNode, parent, parent.getChildCount());
        
		type.addSyntaxNodes(tree, model, newNode);
		SyntaxTree.addIdentifier(tree, model, newNode, identifier);
		if (constantElement != null) {
    		SyntaxTree.addKeyWordNode(tree, model, newNode, KeyWord.EQ);
    		constantElement.addSyntaxNodes(tree, model, newNode);
		}
    }

	public String edHtml() {
		String s = type.edHtml() + " " + identifier;
		if (constantElement != null)
			s = s + "=" + constantElement.toString();
		return (s);
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
				// *** SimpleVariableDeclaration
				"  constant:",			""+constant,
				"  constantElement: ",	""+constantElement
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
//		String s = identifier + " Type=" + type;
//		if (constantElement != null)
//			s = s + ", constantElement=" + constantElement.toString();
		String s = type + " " + identifier;
		if (constantElement != null)
			s = s + "=" + constantElement.toString();
		return (s);
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	public SimpleVariableDeclaration() {
		super(null, null);
		this.declarationKind = ObjectKind.SimpleVariableDeclaration;
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("Variable: " + this);
		oupt.writeKind(declarationKind);
		oupt.writeShort(OBJECT_SEQU);

		// *** SyntaxElement
		writePsiTree(oupt);

		// *** Declaration
		oupt.writeString(identifier);
		oupt.writeString(externalIdent);
		oupt.writeType(type);// Declaration
		oupt.writeObj(declaredIn);// Declaration
		
		// *** SimpleVariableDeclaration
		oupt.writeBoolean(constant);
		oupt.writeObj(constantElement);
	}
	
	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static SimpleVariableDeclaration readObject(AttributeInputStream inpt) throws IOException {
		SimpleVariableDeclaration var = new SimpleVariableDeclaration();
		var.OBJECT_SEQU = inpt.readSEQU(var);

		// *** SyntaxElement
		var.psiTree = readPsiTree(inpt);

		// *** Declaration
		var.identifier = inpt.readString();
		var.externalIdent = inpt.readString();
		var.type = inpt.readType();
		var.declaredIn = (DeclarationScope) inpt.readObj();
		
		// *** SimpleVariableDeclaration
		var.constant = inpt.readBoolean();
		var.constantElement = (Expression) inpt.readObj();
		Util.TRACE_INPUT("Variable: " + var.OBJECT_SEQU + " " + var);
		return(var);
	}

}
