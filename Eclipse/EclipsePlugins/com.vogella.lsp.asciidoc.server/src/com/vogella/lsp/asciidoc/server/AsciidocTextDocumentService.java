package com.vogella.lsp.asciidoc.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

public class AsciidocTextDocumentService implements TextDocumentService {

    private final Map<String, AsciidocDocumentModel> docs = new ConcurrentHashMap<>();

    private final AsciidocLanguageServer languageServer;

    public AsciidocTextDocumentService(AsciidocLanguageServer languageServer) {
        this.languageServer = languageServer;
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        // Example: provide completions for AsciiDoc elements
        CompletionItem image = new CompletionItem("image::");
        CompletionItem include = new CompletionItem("include::");
        return CompletableFuture.completedFuture(Either.forLeft(List.of(image, include)));
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        AsciidocDocumentModel model = new AsciidocDocumentModel(params.getTextDocument().getText());
        docs.put(params.getTextDocument().getUri(), model);
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        AsciidocDocumentModel model = new AsciidocDocumentModel(params.getContentChanges().get(0).getText());
        docs.put(params.getTextDocument().getUri(), model);
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        docs.remove(params.getTextDocument().getUri());
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {
    }
}