package simula.lsp.compiler;

import java.util.List;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.jsonrpc.util.ToStringBuilder;

public class SourceDocumentItem {

	private TextDocumentItem textDocumentItem;
	private List<Diagnostic> diagnostics;

	public SourceDocumentItem(final TextDocumentItem textDocumentItem) {
		this.textDocumentItem = textDocumentItem;
	}

	/// Get the text document's diagnostics.
	public List<Diagnostic> getDiagnostis() {
		return diagnostics;
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
