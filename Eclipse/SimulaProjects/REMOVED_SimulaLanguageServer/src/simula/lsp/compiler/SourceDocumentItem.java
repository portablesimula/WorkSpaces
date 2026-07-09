package simula.lsp.compiler;

import java.util.List;
import java.util.Vector;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.util.ToStringBuilder;

import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.LOG;
import simula.psi.PsiElement;
import simula.psi.PsiTree;
import simula.psi.PsiTreeIterator;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class SourceDocumentItem {

	private TextDocumentItem textDocumentItem;
	private List<Diagnostic> diagnostics;
	private PsiTree psiTree;
	public List<LspToken> tokenList;
	private ProgramModule syntaxTree; // Root of Syntax Tree

	public SourceDocumentItem(final TextDocumentItem textDocumentItem) {
		this.textDocumentItem = textDocumentItem;
	}

	public void printAll(String title) {
		printSyntaxTree(title);
		printDiagnostics(title);
		printTokenList(title);
	}
	
	public void printSyntaxTree(String title) {
		IO.println("======================================== BEGIN SYNTAX TREE: " + title + " ============================ ");
		syntaxTree.print(0);
		IO.println("======================================== ENDOF SYNTAX TREE: " + title + " ============================ ");
	}

	public void printDiagnostics(String title) {
		LOG.info("++++++++++++++++ BEGIN DIAGNOSTICS: " + title + " ++++++++++++++++++");
		boolean detailed = false;//true;
		if(detailed) {
			for(Diagnostic diagnostic:diagnostics) LOG.info(diagnostic.toString());			
		} else {
			for(Diagnostic diagnostic:diagnostics) LOG.info(edDiagnostic(diagnostic));
		}
		LOG.info("++++++++++++++++ ENDOF DIAGNOSTICS: " + title + " ++++++++++++++++++");
	}
	
	public void printTokenList(String title) {
		IO.println("======================================== BEGIN TOKEN LIST: " + title + " ============================ ");
		for(LspToken token:tokenList) {
			IO.println(""+token);
		}
		IO.println("======================================== ENDOF TOKEN LIST: " + title + " ============================ ");		
	}

	public String edDiagnostic(Diagnostic diagnostic) {
		StringBuilder sb = new StringBuilder();
		sb.append(diagnostic.getSeverity()+" ");
		Range range = diagnostic.getRange();
		sb.append(edRange(range)+" ");
		Either<String, MarkupContent> msg = diagnostic.getMessage();
		sb.append(msg.getLeft());
		return sb.toString();
	}
	
	public String edRange(Range range) {
		Position start = range.getStart();
		Position end = range.getEnd();
		return "Range[start:" +edPosition(start) + ", end:" + edPosition(end)+ "]";
	}
	
	public String edPosition(Position pos) {
		return "[line:" + pos.getLine() +", char:" + pos.getCharacter() + "]";
	}
	
	// ****************************************************************
	// *** createTokenList
	// ****************************************************************
    /// Create the tokenList with tokens delivered from the psiTree.
    public void createTokenList() {
    	this.tokenList = new Vector<LspToken>();
		boolean TESTING = true;
		if(TESTING) {
//			PsiTreeIterator.TRACING = true;
			psiTree.printPsiTree("++++++++++++++++ SourceDocumentItem.createTokenList: PSI TREE: " + psiTree + " ++++++++++++++++++");
			Thread.dumpStack();
		}
		PsiTreeIterator itr = new PsiTreeIterator(psiTree);
		while (itr.hasNext()) {
			PsiElement elt = itr.next();
			if(TESTING) {
//				IO.println("PsiTextPanel.fillTextPane: GOT NEXT: " + elt.edText());
			IO.println("PsiTextPanel.fillTextPane: GOT NEXT: " + elt);
			}
				
		    int line = elt.lineNumber;
		    int character = elt.startOffset;
		    int length = elt.endOffset - elt.startOffset;
		    int type = elt.getLspTokenType();
		    int mod = 0; //????;

			LspToken lspToken = new LspToken(line, character, length, type, mod);

			tokenList.add(lspToken);
		}
		PsiTreeIterator.TRACING = false;
	}


	/// Get the text document's SyntaxTree.
	public ProgramModule getSyntaxTree() {
		return syntaxTree;
	}

	/// Set the text document's SyntaxTree.
	public void setSyntaxTree(final ProgramModule syntaxTree) {
		this.syntaxTree = syntaxTree;
	}

	/// Get the text document's PsiTree.
	public PsiTree getPsiTree() {
		return psiTree;
	}

	/// Set the text document's PsiTree.
	public void setPsiTree(final PsiTree psiTree) {
		this.psiTree = psiTree;
	}

	/// Get the text document's diagnostics.
	public List<Diagnostic> getDiagnostis() {
		return diagnostics;
	}

	public void initDiagnostics() {
		diagnostics = new Vector<Diagnostic>();
	}
	
	public void addDiagnostic(Diagnostic diagnostic) {
		diagnostics.add(diagnostic);
	}

//	/// Set the text document's diagnostics.
//	public void setDiagnostics(final List<Diagnostic> diagnostics) {
//		this.diagnostics = diagnostics;
//	}

	/// Get the text document's uri.
	public String getUri() {
		return textDocumentItem.getUri();
	}

	/// Set the text document's uri.
	public void setUri(final String uri) {
		textDocumentItem.setUri(uri);
	}

	/// Get the text document's language identifier
	public String getLanguageId() {
		return textDocumentItem.getLanguageId();
	}

	/// Set the text document's language identifier
	public void setLanguageId(final String languageId) {
		textDocumentItem.setLanguageId(languageId);
	}

	/// Get the version number of this document (it will strictly increase after each change, including undo/redo).
	public int getVersion() {
		return textDocumentItem.getVersion();
	}

	/// Set the version number of this document (it will strictly increase after each change, including undo/redo).
	public void setVersion(final int version) {
		textDocumentItem.setVersion(version);
	}

	/// Get the content of the opened text document.
	public String getText() {
		return textDocumentItem.getText();
	}

	/// Set the content of the opened text document.
	public void setText(final String text) {
		textDocumentItem.setText(text);
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this);
		b.add("uri", textDocumentItem.getUri());
		b.add("languageId", textDocumentItem.getLanguageId());
		b.add("version", textDocumentItem.getVersion());
		b.add("text", textDocumentItem.getText());
		return b.toString();
	}

}
