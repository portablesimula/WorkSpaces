package simula.lsp.server;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.ArrayList;

import simula.SimTextDocumentContentChangeEvent;
import simula.SimulaCoreExports;

public class SimulaTextDocumentService implements TextDocumentService {

    public SimulaTextDocumentService(SimulaLanguageServer myLanguageServer) {
		// TODO Auto-generated constructor stub
	}

	// Document lifecycle notification: Client opened a file
    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
    	TextDocumentItem itm = params.getTextDocument();
        String uri = itm.getUri();
        String text = itm.getText();
        // TODO: Store file content in an internal cache and trigger validations
        int version = itm.getVersion();
        SimulaCoreExports.didOpen(uri, version, text);
    }

    // Document lifecycle notification: Client changed a file
    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        String uri = params.getTextDocument().getUri();
        // The changes array contains the delta updates or full text
        List<TextDocumentContentChangeEvent> changes = params.getContentChanges();
        // TODO: Update your internal document cache
    	//public static void didChange(final String documentUri, final List<SimTextDocumentContentChangeEvent> changes)
        List<SimTextDocumentContentChangeEvent> simChanges = SimulaCoreClientProxy.convert(changes);
        SimulaCoreExports.didChange(uri, simChanges);
    }

	// Document lifecycle notification: Client saved a file
    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        String uri = params.getTextDocument().getUri();
        // Handle post-save hooks if necessary
    	SimulaCoreExports.didSave(uri);
    }

    // Document lifecycle notification: Client closed a file
    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        String uri = params.getTextDocument().getUri();
        // TODO: Clean up cache for this specific document
        SimulaCoreExports.didClose(uri);
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
