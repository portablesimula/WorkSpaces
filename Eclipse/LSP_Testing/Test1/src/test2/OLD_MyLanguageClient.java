package test2;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;
import java.util.concurrent.CompletableFuture;

public class OLD_MyLanguageClient implements LanguageClient {

    private LanguageServer serverProxy;

    // Setter to store the server proxy for outgoing calls
    public void setServerProxy(LanguageServer serverProxy) {
        this.serverProxy = serverProxy;
    }

    @Override
    public void telemetryEvent(Object object) {
        logger.info("Telemetry received: " + object);
    }

    @Override
    public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {
        logger.info("Diagnostics received for: " + diagnostics.getUri());
        diagnostics.getDiagnostics().forEach(d -> logger.info("- " + d.getMessage()));
    }

    @Override
    public void showMessage(MessageParams messageParams) {
        logger.info("[" + messageParams.getType() + "] " + messageParams.getMessage());
    }

    @Override
    public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
        // Handle interactive UI dialog prompts from the server here
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void logMessage(MessageParams message) {
        logger.info("[Log] " + message.getMessage());
    }
}
