package client;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.services.LanguageClient;
import java.util.concurrent.CompletableFuture;

public class MyLanguageClient implements LanguageClient {

    @Override
    public void telemetryEvent(Object object) {
        System.out.println("Telemetry mottatt: " + object);
    }

    @Override
    public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {
        System.out.println("Diagnostikk mottatt for " + diagnostics.getUri());
        diagnostics.getDiagnostics().forEach(d -> 
            System.out.printf("[%s] %s: %s\n", d.getSeverity(), d.getRange().getStart(), d.getMessage())
        );
    }

    @Override
    public void showMessage(MessageParams messageParams) {
        System.out.printf("Melding fra server [%s]: %s\n", messageParams.getType(), messageParams.getMessage());
    }

    @Override
    public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
        System.out.println("Forespørsel fra server: " + requestParams.getMessage());
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void logMessage(MessageParams message) {
        System.out.println("Logg fra server: " + message.getMessage());
    }
}
