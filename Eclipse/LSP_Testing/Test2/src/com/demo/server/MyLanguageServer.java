package com.demo.server;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import simula.lsp.SimulaTextDocumentService;
import simula.lsp.SimulaWorkspaceService;

import java.util.concurrent.CompletableFuture;

public class MyLanguageServer implements LanguageServer, LanguageClientAware {
    private final TextDocumentService textDocumentService = new SimulaTextDocumentService();
    private final WorkspaceService workspaceService = new SimulaWorkspaceService();
    private LanguageClient client;

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        InitializeResult result = new InitializeResult(new ServerCapabilities());
        // Fortell klienten at denne serveren støtter auto-fullføring
        result.getCapabilities().setCompletionProvider(new CompletionOptions());
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.completedFuture(new Object());
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public TextDocumentService getTextDocumentService() { return textDocumentService; }

    @Override
    public WorkspaceService getWorkspaceService() { return workspaceService; }

    @Override
    public void connect(LanguageClient client) {
        this.client = client;
    }
}
