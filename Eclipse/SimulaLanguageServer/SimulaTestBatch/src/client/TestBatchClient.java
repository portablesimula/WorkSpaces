package client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.LogTraceParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.services.LanguageClient;

public class TestBatchClient implements LanguageClient {

//	@Override
//	public void initialized() {
//		// TODO Auto-generated method stub
//		IO.println("TestBatchClient.initialized: ");		
//	}

	/// The telemetry notification is sent from the server to the client to ask
	/// the client to log a telemetry event.
	@Override
	public void telemetryEvent(Object object) {
		// TODO Auto-generated method stub
		
	}

	/// Diagnostics notifications are sent from the server to the client to
	/// signal results of validation runs.
	@Override
	public void publishDiagnostics(PublishDiagnosticsParams params) {
		IO.println("TestBatchClient.publishDiagnostics: for " + params.getUri());
		List<Diagnostic> diagnostics = params.getDiagnostics();
		for(Diagnostic diag:diagnostics) {
			IO.println("TestBatchClient.publishDiagnostics: - " + diag);			
		}
	}

	/// The show message notification is sent from a server to a client to ask
	/// the client to display a particular message in the user interface.
	@Override
	public void showMessage(MessageParams params) {
		IO.println("TestBatchClient.showMessage: " + params.getType() + "  " + params.getMessage());
	}

	/// The show message request is sent from a server to a client to ask the
	/// client to display a particular message in the user interface. In addition
	/// to the show message notification the request allows to pass actions and
	/// to wait for an answer from the client.
	@Override
	public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
		return null;
	}

	/// The log message notification is sent from the server to the client to ask
	/// the client to log a particular message.
	@Override
	public void logMessage(MessageParams params) {
		System.err.println("TestBatchClient.logMessage: " + params.getType() + "  " + params.getMessage());
	}

	/// A notification to log the trace of the server's execution. The amount and content of these
	/// notifications depends on the current trace configuration. If trace is 'off', the server
	/// should not send any logTrace notification. If trace is 'message', the server should not
	/// add the 'verbose' field in the LogTraceParams.
	/// <p>
	/// {@code $/logTrace} should be used for systematic trace reporting. For single debugging messages,
	/// the server should send window/logMessage notifications.
	@Override
	public void logTrace(LogTraceParams params) {
//		throw new UnsupportedOperationException();
	}

}
