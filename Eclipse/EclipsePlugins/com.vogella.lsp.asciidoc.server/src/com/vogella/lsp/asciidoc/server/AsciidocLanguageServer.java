package com.vogella.lsp.asciidoc.server;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

public class AsciidocLanguageServer implements LanguageServer {

    private final TextDocumentService textService;
    private final WorkspaceService workspaceService;
    private volatile boolean shutdownRequested;
    LanguageClient client;

    public AsciidocLanguageServer() {
        textService = new AsciidocTextDocumentService(this);
        workspaceService = new AsciidocWorkspaceService();
    }

    /**
     * Tells the client which functionality this server supports
     */
    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        ServerCapabilities capabilities = new ServerCapabilities();
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
        capabilities.setCompletionProvider(new CompletionOptions());
        return CompletableFuture.completedFuture(new InitializeResult(capabilities));
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        // The client announces that it will exit soon, free resources here
        shutdownRequested = true;
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void exit() {
        // A standalone server would terminate its process here, for example via
        // System.exit(shutdownRequested ? 0 : 1);
        // This server runs inside the IDE process, so the connection provider
        // stops the launcher instead
        if (!shutdownRequested) {
            System.err.println("exit received without a previous shutdown request");
        }
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return textService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return workspaceService;
    }

    public void setRemoteProxy(LanguageClient remoteProxy) {
        this.client = remoteProxy;
    }
}