package simula.server;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.ArrayList;

import simula.core.DocumentManager;
import simula.core.utilities.LOG;

public class SimulaTextDocumentService implements TextDocumentService {

    public SimulaTextDocumentService(SimulaLanguageServer myLanguageServer) {
		// TODO Auto-generated constructor stub
    	semanticTokensFull(null);
	}

    
	/// The document open notification is sent from the client to the server to signal
	/// newly opened text documents. The document's truth is now managed by the client
	/// and the server must not try to read the document's truth using the document's uri.
    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
    	TextDocumentItem itm = params.getTextDocument();
    	String documentUri = itm.getUri();
    	String sourceCode = itm.getText();
    	// TODO: Store file content in an internal cache and trigger validations
    	int version = itm.getVersion();
    	LOG.info("got Notification: didOpen: " + documentUri);
    	DocumentManager.didOpen(documentUri, version, sourceCode);
    }

    /// The document change notification is sent from the client to the server to
	/// signal changes to a text document.
    @Override
    public void didChange(DidChangeTextDocumentParams params) {
    	String documentUri = params.getTextDocument().getUri();
    	// The changes array contains the delta updates or full text
    	List<TextDocumentContentChangeEvent> changes = params.getContentChanges();
    	// TODO: Update your internal document cache
    	LOG.info("got Notification: didChange: " + documentUri);
    	LOG.severe("SimulaTextDocumentService.didChange NOT IMPL");
    	DocumentManager.didChange(documentUri, changes);
    }

	/// The document save notification is sent from the client to the server when
	/// the document is saved in the client.
    @Override
    public void didSave(DidSaveTextDocumentParams params) {
    	String documentUri = params.getTextDocument().getUri();
    	// Handle post-save hooks if necessary
    	LOG.info("got Notification: didSave: " + documentUri);
    	LOG.severe("SimulaCoreExports.didSave NOT IMPL");
    	DocumentManager.didSave(documentUri);
    }

	/// The document close notification is sent from the client to the server
	/// when the document got closed in the client. The document's truth now
	/// exists where the document's uri points to (e.g. if the document's uri is
	/// a file uri the truth now exists on disk).
    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        String documentUri = params.getTextDocument().getUri();
        // TODO: Clean up cache for this specific document
        	LOG.info("got Notification: didClose: " + documentUri);
//        	LOG.severe("SimulaCoreExports.didClose NOT IMPL");
    		DocumentManager.didClose(documentUri);
    	}

    
    
	/// The textDocument/semanticTokens/full request is sent from the client
	/// to the server to return the semantic tokens for a whole file.
    @Override
	public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
        return CompletableFuture.supplyAsync(() -> {
        	return semanticTokensFull_Local(params);
        });
	}
    public SemanticTokens semanticTokensFull_Local(SemanticTokensParams params) {
    	String documentUri = params.getTextDocument().getUri();
    	LOG.info("got Request: semanticTokensFull: " + documentUri);
    	LOG.severe("SimulaCoreExports.semanticTokensFull NOT IMPL");
    	List<Integer> tokens = DocumentManager.semanticTokensFull(documentUri);
    	return new SemanticTokens(tokens);
    }

    
    // Request: Provide auto-completion suggestions
    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        return CompletableFuture.supplyAsync(() -> {
            List<CompletionItem> items = new ArrayList<>();
            
            // Example completion item
            CompletionItem item = new CompletionItem();
            item.setLabel("HelloWorld");
            item.setKind(CompletionItemKind.Keyword);
            item.setInsertText("Hello World!");
            items.add(item);
            
            return Either.forLeft(items);
        });
    }
}
