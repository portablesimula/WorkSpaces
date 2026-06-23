package simula.lsp.compiler;

import java.util.List;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.util.ToStringBuilder;

import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.psi.PsiTree;

public class SourceDocumentItem {

	private TextDocumentItem textDocumentItem;
	private List<Diagnostic> diagnostics;
	private PsiTree psiTree;
	private ProgramModule syntaxTree; // Root of Syntax Tree

	public SourceDocumentItem(final TextDocumentItem textDocumentItem) {
		this.textDocumentItem = textDocumentItem;
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

	public void addDiagnostic(Diagnostic diagnostic) {
		diagnostics.add(diagnostic);
	}

	/// Set the text document's diagnostics.
	public void setDiagnostics(final List<Diagnostic> diagnostics) {
		this.diagnostics = diagnostics;
	}

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
