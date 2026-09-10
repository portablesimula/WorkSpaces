package client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.LogTraceParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.services.LanguageClient;

import simula.editor.SourceModule;
import simula.editor.utilities.Global;
import simula.editor.utilities.Util;

public class SimulaEditorClient implements LanguageClient {
	
	private void write(MessageType type, String message) {
		System.err.println("SimulaEditorClient.logMessage: Type:" + type + "  Message:" + message);	
		IO.println("SimulaEditorClient.logMessage: Global.currentModule=" + Global.currentModule);
		switch(type) {
			case Debug, Error: Global.console.writeError(message); break;
			case Warning:      Global.console.writeWarning(message); break;
			default:           Global.console.write(message);
		}
	}

//	@Override
//	public void initialized() {
//		messageType type = SimulaCoreClient.messageType.Debug;
//		write(type, "Simula LanguageServer Initialized !");
//	}

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

	public void publishDiagnostics(String uri, List<Diagnostic> diagnostics) {
		Util.IERR("SJEKK DETTE");
		SourceModule.publishDiagnostics(uri, diagnostics);
	}

	/// The show message notification is sent from a server to a client to ask
	/// the client to display a particular message in the user interface.
	@Override
	public void showMessage(MessageParams params) {
		IO.println("TestBatchClient.showMessage: " + params.getType() + "  " + params.getMessage());
	}

	public void showMessage(MessageType type, String message) {
		IO.println("SimulaEditorClient.showMessage: " + type + "  " + message);
		Util.IERR("SJEKK DETTE");
//		write(type, "Server MSG: " + message + '\n');
		showMessage(new MessageParams(type, message));
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

	public void logMessage(MessageType type, String message) {
		System.err.println("SimulaEditorClient.logMessage: Type:" + type + "  Message:" + message);	
		IO.println("SimulaEditorClient.logMessage: Global.currentModule=" + Global.currentModule);
		write(type, "Server LOG: " + message + '\n');
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
