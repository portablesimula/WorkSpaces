package simula.server.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.CompletableFutures;
import org.eclipse.lsp4j.jsonrpc.ResponseErrorException;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;
import org.eclipse.lsp4j.services.TextDocumentService;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.ArrayList;

import simula.core.DocumentManager;
import simula.core.utilities.LOG;
import simula.core.utilities.Util;

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
    	try {
    		TextDocumentItem itm = params.getTextDocument();
    		String documentUri = itm.getUri();
    		String sourceCode = itm.getText();
    		// TODO: Store file content in an internal cache and trigger validations
    		int version = itm.getVersion();
    		LOG.trace("\nGot Notification: didOpen: " + documentUri);
    		DocumentManager.didOpen(documentUri, version, sourceCode);
    	} catch(Throwable e) {
    		LOG.severe("SimulaTextDocumentService.didOpen: Got exception: " + e);
    	}
    }

    /// The document change notification is sent from the client to the server to
	/// signal changes to a text document.
    @Override
    public void didChange(DidChangeTextDocumentParams params) {
    	try {
    		String documentUri = params.getTextDocument().getUri();
    		// The changes array contains the delta updates or full text
    		List<TextDocumentContentChangeEvent> changes = params.getContentChanges();
    		LOG.trace("\nGot Notification: SimulaTextDocumentService.didChange: " + documentUri);
    		DocumentManager.didChange(documentUri, changes);
    	} catch(Throwable e) {
    		LOG.severe("SimulaTextDocumentService.didChange: Got exception: " + e);
    	}
    }

	/// The document save notification is sent from the client to the server when
	/// the document is saved in the client.
    @Override
    public void didSave(DidSaveTextDocumentParams params) {
    	try {
    		String documentUri = params.getTextDocument().getUri();
    		// Handle post-save hooks if necessary
    		LOG.trace("\nGot Notification: didSave: " + documentUri);
    		LOG.severe("SimulaCoreExports.didSave NOT IMPL");
    		DocumentManager.didSave(documentUri);
    	} catch(Throwable e) {
    		LOG.severe("SimulaTextDocumentService.didSave: Got exception: " + e);
    	}
    }

	/// The document close notification is sent from the client to the server
	/// when the document got closed in the client. The document's truth now
	/// exists where the document's uri points to (e.g. if the document's uri is
	/// a file uri the truth now exists on disk).
    @Override
    public void didClose(DidCloseTextDocumentParams params) {
    	try {
    		String documentUri = params.getTextDocument().getUri();
    		LOG.trace("\nGot Notification: didClose: " + documentUri);
    		DocumentManager.didClose(documentUri);
    	} catch(Throwable e) {
    		LOG.severe("SimulaTextDocumentService.didClose: Got exception: " + e);
    	}
    }

    
    
	/// The textDocument/semanticTokens/full request is sent from the client
	/// to the server to return the semantic tokens for a whole file.
//    @Override
//	public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
//        return CompletableFuture.supplyAsync(() -> {
//        	return semanticTokensFull_Local(params);
//        });
//	}
    @Override
    public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
        // 1. Use computeAsync to automatically hook into LSP cancellation tokens
        return CompletableFutures.computeAsync(cancelToken -> {
            // Check if the client canceled the request before starting heavy computation
            cancelToken.checkCanceled();
            try {
                SemanticTokens semanticTokens = semanticTokensFull_Local(params);
                cancelToken.checkCanceled();
                return semanticTokens;
            } catch (Exception e) {
                // 2. Handle internal exceptions and translate them into LSP errors
                ResponseError error = new ResponseError(
                    ResponseErrorCode.InternalError, 
                    "Failed to compute semantic tokens: " + e.getMessage(), 
                    null
                );
                throw new ResponseErrorException(error);
            }
        });
    }
    public SemanticTokens semanticTokensFull_Local(SemanticTokensParams params) {
    	String documentUri = params.getTextDocument().getUri();
    	LOG.trace("\nGot Request: semanticTokensFull: " + documentUri);
//    	LOG.severe("SimulaCoreExports.semanticTokensFull NOT IMPL");
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
