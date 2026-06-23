package test2;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.services.LanguageClient;

import simula.lsp.SimulaLanguageServer;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class MyLanguageClient implements LanguageClient {
	private static final Logger logger = Logger.getLogger(MyLanguageClient.class.getName());

    @Override
    public void telemetryEvent(Object object) {
        // Handle telemetry diagnostics telemetry metrics
    }

    @Override
    public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {
        // Intercept compilation or semantic errors sent from the server
        logger.info("Diagnostics received for: " + diagnostics.getUri());
        diagnostics.getDiagnostics().forEach(d -> logger.info("- " + d.getMessage()));
    }

    @Override
    public void showMessage(MessageParams messageParams) {
        logger.info("[" + messageParams.getType() + "] " + messageParams.getMessage());
    }

    @Override
    public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
        // Returns UI interaction choices back to the server
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void logMessage(MessageParams message) {
        logger.info("[Log] " + message.getMessage());
    }
}
