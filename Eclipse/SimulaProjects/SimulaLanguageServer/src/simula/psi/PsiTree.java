package simula.psi;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Html;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;

public class PsiTree extends PsiElement {

//	public SyntaxElement syntaxClass;
	/// The SyntaxElement elements connected to this PsiTree
	Vector<SyntaxElement> syntaxElements = new Vector<SyntaxElement>();

	protected final List<PsiElement> children = new ArrayList<>();
	
	public LexToken checkPoint;
	
    public enum Kind { any,
    	// Declarations
//    	programModule,
    	mainModule, label, declaration, block,
//    	externalDeclaration,
//    	typeDeclaration, arrayDeclaration, externalDeclaration, classDeclaration, procedureDeclaration,
    	virtualSpecification,
    	// Statements
    	statement,
    	activationStatement, conditionalStatement, connectionStatement,
    	forStatement,gotoStatement, innerStatement, whileStatement, switchStatement, blockStatement,
    	// Expressions
    	expression, simpleExpression, booleanExpression, unaryOperation, textExpression,
    	relationalOperation, arithmeticExpression, constant, postfixExpression,
    	// textAfterProgramEnd
    	textAfterProgramEnd
    }
	public Kind kind;
	
//	public static PsiTree dummyTree = new LocalPsiTree("dymmyTree", null) {
//		@Override public int firstLineNumber() { return -24; }
//		@Override public int lastLineNumber()  { return -25; }
//	};
//	public static PsiTree dummyTree = new PsiTree("dymmyTree", -23, -25, null);
	public static PsiTree dummyTree = new PsiTree(null, -23, "", -25, "dymmyTree");
	
	public PsiTree(PsiTree parent, int tokenStartLine, CharSequence sourceText, int startOffset, String debugName) {
		super(debugName, sourceText);
		this.startOffset = startOffset;
		this.lineNumber = tokenStartLine;
		this.parent = parent;
	}
    
	/// EndOffset is not set until 'doneSubtree'.
	/// Use this in debug traces
	public int getEndOffset() {
		if(children.isEmpty()) return startOffset;
		return children.getLast().endOffset;
	}

	public void addChild(PsiElement child) {
		if(child == null) {
			Util.IERR("addChild NULL !!");
			return;
		}
		if(Option.internal.TRACE_PSITREE_GROW)
			IO.println("PsiTree.addChild: " + debugName + ": " + edChildrenText());
		children.add(child);
		child.parent = this;
	}
	
	public void addTree(PsiTree psiTree) {
		children.addAll(psiTree.children);
	}
	
	public boolean isEmpty() {
		return children.isEmpty();
	}
	
//	public LexToken removeLastChild() {
//		return (LexToken) children.removeLast();
	public PsiElement removeLastChild() {
		return children.removeLast();
	}


	public LexToken getLastChild() {
		if(! children.isEmpty() && children.getLast() instanceof LexToken token)
			return token;
		return null;
	}
	
	public LexToken getLastParserChild() {
		int n = children.size();
		if(n > 0) {
			for(int i=n-1;n>=0;i--) {
				PsiElement elt = children.get(i);
//				IO.println("PsiTree.getLastParserChild: CHECK elt "+i+": "+elt);
				if(elt instanceof LexToken token && token.isParserToken()) return token;
//				if(elt instanceof PsiTree psiTree) return psiTree.getLastParserChild();
			}
		}
		IO.println("PsiTree.getLastParserChild: children.size="+n);
		this.printPsiTree("PsiTree.getLastParserChild: ");
		IO.println("PsiTree.getLastParserChild: parent="+parent);
		parent.printPsiTree("PsiTree.getLastParserChild: PARENT TREE: ");
		if(parent != null) return parent.getLastParserChild();
//			Util.STOP();			
		return null;
	}

	@Override
	public List<PsiElement> getChildren() { return children; }
	
//	public LexToken getFirstChild() {
//		if(! children.isEmpty() && children.getFirst() instanceof LexToken token)
//			return token;
//		return null;
//	}
	
	
	@Override public int firstLineNumber() {
		try {
			PsiElement firstChild = children.getFirst();
			if(firstChild != null) return firstChild.firstLineNumber();
		} catch(Exception e) { }
//		IO.println("PsiTree.firstLineNumber: No child: return "+this.debugName+"  "+this.lineNumber);
		return this.lineNumber;
	}
	
	@Override public int lastLineNumber() {
		try {
			PsiElement lastChild = children.getLast();
			if(lastChild != null) return lastChild.lastLineNumber();
		} catch(Exception e) { }
		return firstLineNumber();
	}

	@Override public String getText() {
//		IO.println("PsiTree.getText: " + debugName);
		StringBuilder sb = new StringBuilder();
		for(PsiElement child:children) {
//			IO.println("PsiTree.getText: child: " + child);
			sb.append(child.getText());
		}
//		IO.println(("PsiTree.getText: " + debugName + " ==> \"" + sb + '"').replace("\n", "\\n").replace("\r", "\\r"));
		return sb.toString();
	}
	
	
	public String edChildren() {
		StringBuilder sb = new StringBuilder();
		String sep ="";
        for (PsiElement child : getChildren()) {
            sb.append(sep).append(child);
            sep = ", ";
        }
		return sb.toString();
	}
	
	public String edChildrenText() {
		StringBuilder sb = new StringBuilder("\"");
        for (PsiElement child : getChildren()) {
            sb.append(child.getOriginalText());
        }
		return sb.toString().replace("\r", "\\r").replace("\n", "\\n") + '"';
	}

	public void printAncesterChain(String title) {
    	IO.println("====== BEGIN - printAncesterChain: " + title + " ======");
    	PsiTree x = this;
    	while(x != null) {
    		IO.println("PsiTree: Line:"+x.lineNumber+ " Level: "+ x.level() + " " +x.kind+" "+x.debugName);
    		x = x.parent;
    	}
    	IO.println("====== ENDOF - printAncesterChain: " + title + " ======");
		
	}

	public int level() {
		int res = -1;
    	PsiTree x = this;
    	while(x != null) {
    		res++; x = x.parent;
    	}
    	return res;
	}
	
    public void printPsiTree(String title) {
    	IO.println("====== BEGIN - PrintTree: " + title + " ======");
    	printPsiTree(this, 1);
    	IO.println("====== ENDOF - PrintTree: " + title + " ======");
    }

    private static void printPsiTree(PsiElement element, int depth) {
    	IO.println("  ".repeat(depth) + element.edPsiLine());
        if(element instanceof PsiTree subTree) {
	        for (PsiElement child : subTree.getChildren()) {
	            printPsiTree(child, depth + 1);
	        }
        }
    }

	public String edPsiLine() {
		int lno = this.firstLineNumber();
		int lastLine = this.lastLineNumber();
		
		//String ID1 = (syntaxClass == null)? "" : "==> " + syntaxClass.getClass().getSimpleName();
		
		
//		String ID = "PsiTree(" + debugName + ") " + ID1 + " Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
		String ID = "PsiTree[" + startOffset + ':' + getEndOffset() + "]=" + debugName + " Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
		if(ID.length() > 200) ID = ID.substring(0, 200) + " ... Truncated";
		StringBuilder sb = new StringBuilder();
		if(lno > 0) {
			sb.append("Line ").append(lno);
			if(lastLine > 0 && lastLine != lno) sb.append('-').append(lastLine);
			sb.append(": ");
		}
		sb.append(ID);
		return sb.toString();		
	}
	
	public String edHtmlLine() {
		String ID = "PsiTree[" + startOffset + ':' + getEndOffset() + "]=" + debugName + " Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
		if(ID.length() > 200) ID = ID.substring(0, 200) + " ... Truncated";
		int lno = this.firstLineNumber();
		int lastLine = this.lastLineNumber();
        return Html.edPsi(lno, lastLine, ID);
	}

	@Override public String toString() {
		return edHtmlLine();
	}

}