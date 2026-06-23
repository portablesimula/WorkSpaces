package simula.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class SimulaLanguageServer implements LanguageServer, LanguageClientAware {
	private static final Logger logger = Logger.getLogger(SimulaLanguageServer.class.getName());

	private final SimulaTextDocumentService textDocumentService = new SimulaTextDocumentService();
	private LanguageClient client;

	@Override
//	public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
//		ServerCapabilities capabilities = new ServerCapabilities();
//		capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
//		return CompletableFuture.completedFuture(new InitializeResult(capabilities));
//	}
//    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        ServerCapabilities capabilities = new ServerCapabilities();
        // Configure the server to accept full text synchronization
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
        
        InitializeResult result = new InitializeResult(capabilities);
        return CompletableFuture.completedFuture(result);
    }

	@Override
	public CompletableFuture<Object> shutdown() {
		return CompletableFuture.completedFuture(new Object());
	}

//	@Override
//	public void exit() {
//	}
    @Override
    public void exit() {
        System.exit(0);
    }

	@Override
	public TextDocumentService getTextDocumentService() {
		return textDocumentService;
	}

//	@Override
//	public WorkspaceService getWorkspaceService() {
//		return new SimulaWorkspaceService();
//	}
    @Override
    public WorkspaceService getWorkspaceService() {
        return new WorkspaceService() {
            @Override public void didChangeConfiguration(DidChangeConfigurationParams p) {}
            @Override public void didChangeWatchedFiles(DidChangeWatchedFilesParams p) {}
        };
    }

	@Override
	public void connect(LanguageClient client) {
		this.client = client;
        logger.info("[Server] Connected to the client proxy.");
		this.textDocumentService.setClient(client);
		logger.info("[Server] textDocumentService.setClient(client)");
	}


}
