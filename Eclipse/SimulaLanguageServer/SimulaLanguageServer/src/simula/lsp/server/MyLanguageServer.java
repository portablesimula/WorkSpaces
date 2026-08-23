package simula.lsp.server;

import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.SemanticTokensLegend;
import org.eclipse.lsp4j.SemanticTokensWithRegistrationOptions;
import org.eclipse.lsp4j.SemanticTokensServerFull;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyLanguageServer implements LanguageServer {

    private final MyTextDocumentService textDocumentService = new MyTextDocumentService();
    private final MyWorkspaceService workspaceService = new MyWorkspaceService();

    // 1. Define the ordered array of Token Types. 
    // The index positions (0, 1, 2...) are what the server will transmit later.
    private static final List<String> SUPPORTED_TOKEN_TYPES = Arrays.asList(
    		SemanticTokenTypes.Namespace,
    		"namespace", // Index 0
        "type",      // Index 1
        "class",     // Index 2
        "enum",      // Index 3
        "interface", // Index 4
        "struct",    // Index 5
        "typeParameter", // Index 6
        "parameter", // Index 7
        "variable",  // Index 8
        "property",  // Index 9
        "macro",     // Index 10
        "function",  // Index 11
        "method"     // Index 12
    );

    // Leave modifiers empty for this baseline configuration
    private static final List<String> SUPPORTED_TOKEN_MODIFIERS = Arrays.asList();

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        // 2. Instantiate the server capabilities tree
        ServerCapabilities capabilities = new ServerCapabilities();

        // 3. Create the Legend containing our custom/standard token types list
        SemanticTokensLegend legend = new SemanticTokensLegend(
            SUPPORTED_TOKEN_TYPES, 
            SUPPORTED_TOKEN_MODIFIERS
        );

        // 4. Configure options to inform the client we support Full document tokens
        SemanticTokensWithRegistrationOptions semanticTokensOptions = new SemanticTokensWithRegistrationOptions();
        semanticTokensOptions.setLegend(legend);
        semanticTokensOptions.setFull(new SemanticTokensServerFull(true)); // Supports full document token passes

        // 5. Attach the semantic tokens configuration to the global server capabilities
        capabilities.setSemanticTokensProvider(semanticTokensOptions);

        // 6. Complete the handshake lifecycle with the client
        InitializeResult result = new InitializeResult(capabilities);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.completedFuture(new Object());
    }

    @Override
    public void exit() {}

    @Override
    public TextDocumentService getTextDocumentService() {
        return this.textDocumentService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return this.workspaceService;
    }
}
