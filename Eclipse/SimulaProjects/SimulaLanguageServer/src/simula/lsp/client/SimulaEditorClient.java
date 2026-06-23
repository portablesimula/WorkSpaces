package simula.lsp.client;

import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.services.LanguageClient;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class SimulaEditorClient implements LanguageClient {
//	private static final Logger logger = Logger.getLogger(SimulaEditorClient.class.getName());
	private static final Logger logger = Logger.getAnonymousLogger();

//	@Override
	public void start() {
		// TODO Auto-generated method stub
		logger.info("SimulaEditorClient.start: Client is starting ...");
	}

    // Du må også implementere de obligatoriske standardmetodene fra LanguageClient:
    @Override public void telemetryEvent(Object object) {
    	logger.info("SimulaEditorClient.telemetryEvent: HURRA !!!");
    	
    }
    @Override public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {
    	logger.info("SimulaEditorClient.publishDiagnostics: HURRA !!!");
    	
    }
    @Override public void showMessage(MessageParams messageParams) {
    	logger.info("SimulaEditorClient.showMessage: HURRA !!!");
    	
    }
    @Override public void logMessage(MessageParams messageParams) {
    	logger.info("SimulaEditorClient.logMessage: HURRA !!!");
    }

	@Override
	public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
		// TODO Auto-generated method stub
		return null;
	}
}
