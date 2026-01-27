package simula.editor;

import java.util.Vector;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Util;
import simula.parser.SimulaParser;
import simula.psi.PsiTree;
import simula.psi.LexToken;
import simula.psi.SimulaLexer;

public class PsiBuilder {
	
    SimulaLexer lexer;
    public PsiTree psiRoot;
    public PsiTree psiTree;

	public PsiBuilder() {
		psiTree = psiRoot = new PsiTree("ROOT", null);
	}


	public void start(CharSequence txt) {
        lexer = new SimulaLexer();
		CharSequence buffer = txt;
		int startOffset = 0;
		int endOffset = buffer.length();
		int initialState = 0;
	    lexer.start(buffer, startOffset, endOffset, initialState);
	}
	
	public void doParse() {
//	    firstTEST();		
		Global.initiate();
		SimulaParser parser = new SimulaParser();
//		IElementType root = FILE;
//	    ASTNode tree = parser.parse(root, builder);
	    parser.parse(null, this);
	    Util.IERR("HVA SÅ ?");
//		System.out.println("Main.tester3: AST-tree: "+tree);
//	    printAST(tree, 4);
	}
	
	public void setPsiTree(PsiTree psiTree) {
		this.psiTree = psiTree;
	}
	
	public void startSubtree(String debugName) {
//        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
		psiTree = new PsiTree(debugName, psiTree);
		psiTree.parent.addChild(psiTree);
//        IO.println("PsiBuilder.startSubtree: ============================ startSubtree: " + psiTree.debugName + ", parent =" + ((psiTree == null)?"null":psiTree.parent));
//        psiRoot.printTree("============================ startSubtree: " + psiTree.debugName + " ROOT " + psiRoot.debugName);
	}
	
	public void doneSubtree(SyntaxClass element) {
//        psiTree.debugName = element.getClass().getSimpleName();
        psiTree.debugName = psiTree.debugName + " ==> " + element.getClass().getSimpleName();
        element.psiTree = psiTree;
//        psiTree.printTree("============================ doneSubtree: " + psiTree.debugName);
//        psiRoot.printTree("============================ doneSubtree: ROOT " + psiRoot.debugName);
        
        psiTree = psiTree.parent;
	}

//	public void advanceLexer(PsiTree compositeElement) {
//		compositeElement.addChild(getTokenType());
	public void advanceLexer() {
		psiTree.addChild(getTokenType());
		IO.println("PsiBuilder.advanceLexer: " + getTokenType() + " ==> " + psiTree);
		lexer.advance();
	}

	public boolean eof() {
		// TODO Auto-generated method stub
		return getTokenType() == null;
	}

	public LexToken getTokenType() {
		// TODO Auto-generated method stub
		return lexer.getTokenType();
	}
	
	public void printPSI(String title) {
		IO.println("printPSI: BEGIN *** "+title+" ***");
		psiRoot.printTree(title);
		IO.println("printPSI: ENDOF *** "+title+" ***");
	}

}
