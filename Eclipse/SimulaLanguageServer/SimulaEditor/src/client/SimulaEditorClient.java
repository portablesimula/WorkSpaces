package client;

import java.util.concurrent.CompletableFuture;
import javax.swing.JOptionPane;
import org.eclipse.lsp4j.LogTraceParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.services.LanguageClient;

import simula.editor.SourceModule;
import simula.editor.utilities.Global;

public class SimulaEditorClient implements LanguageClient {

	/// The telemetry notification is sent from the server to the client to ask the client to log a telemetry event.
	/// I.e: Events of performance metrics, error logs, events, and traces from the server.
	/// 
	/// Should not be used.
	/// See Google AI: how does vscode treat telemetryEvent from my language server
	@Override
	public void telemetryEvent(Object object) {
		throw new UnsupportedOperationException();
	}

	/// Diagnostics notifications are sent from the server to the client to
	/// signal results of validation runs.
	@Override
	public void publishDiagnostics(PublishDiagnosticsParams params) {
		IO.println("TestBatchClient.publishDiagnostics: for " + params.getUri());
		SourceModule.unpackDiagnostics(params.getUri(), params.getDiagnostics());
	}

	/// The show message notification is sent from a server to a client to ask
	/// the client to display a particular message in the user interface.
	@Override
	public void showMessage(MessageParams params) {
		IO.println("TestBatchClient.showMessage: " + params.getType() + "  " + params.getMessage());
		write(params.getType(), "Server LOG: " + params.getMessage() + '\n');
	}

	
	/// The log message notification is sent from the server to the client to ask
	/// the client to log a particular message.
	@Override
	public void logMessage(MessageParams params) {
		IO.println("TestBatchClient.logMessage: " + params.getType() + "  " + params.getMessage());
		write(params.getType(), "Server LOG: " + params.getMessage() + '\n');
	}

//	public void logMessage(MessageType type, String message) {
//		IO.println("SimulaEditorClient.logMessage: Global.currentModule=" + Global.currentModule);
//		write(type, "Server LOG: " + message + '\n');
//	}

	/// A notification to log the trace of the server's execution. The amount and content of these
	/// notifications depends on the current trace configuration. If trace is 'off', the server
	/// should not send any logTrace notification. If trace is 'message', the server should not
	/// add the 'verbose' field in the LogTraceParams.
	/// <p>
	/// logTrace should be used for systematic trace reporting. For single debugging messages,
	/// the server should send window/logMessage notifications.
	@Override
	public void logTrace(LogTraceParams params) {
		IO.println("TestBatchClient.logTrace: " + params.getVerbose() + "  " + params.getMessage());
		MessageType type = MessageType.Info;
		switch(params.getVerbose()) {
			case "off": type = MessageType.Error; break;
			case "message": type = MessageType.Info; break;
			case "verbose": type = MessageType.Log; break;
		}
		write(type, "Server TRC: " + params.getMessage() + '\n');
	}
	
	private void write(MessageType type, String message) {
		System.err.println("SimulaEditorClient.logMessage: Type:" + type + "  Message:" + message);	
		IO.println("SimulaEditorClient.logMessage: Global.currentModule=" + Global.currentModule);
		switch(type) {
			case Debug, Error: Global.console.writeError(message); break;
			case Warning:      Global.console.writeWarning(message); break;
			default:           Global.console.write(message);
		}
	}

	/// The show message request is sent from a server to a client to ask the
	/// client to display a particular message in the user interface. In addition
	/// to the show message notification the request allows to pass actions and
	/// to wait for an answer from the client.
	@Override
    public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
        // Opprett en fremtid (future) som skal returnere svaret
        CompletableFuture<MessageActionItem> future = new CompletableFuture<>();

        // LSP-serveren sender med en liste over mulige handlinger (knapper)
        Object[] options = requestParams.getActions().stream()
                .map(MessageActionItem::getTitle)
                .toArray();

        // MERK: UI-dialoger bør kjøres på UI-tråden (f.eks. via Display.getDefault().asyncExec i Eclipse/SWT)
        // Her bruker vi et enkelt Swing-eksempel for illustrasjon:
        int responseIdx = JOptionPane.showOptionDialog(
                null, 
                requestParams.getMessage(), 
                "SimulaLanguageServer request:", 
                JOptionPane.DEFAULT_OPTION, 
                convertMessageType(requestParams.getType()), 
                null, 
                options, 
                options.length > 0 ? options[0] : null
        );

        if (responseIdx >= 0 && responseIdx < requestParams.getActions().size()) {
            // Returner det spesifikke MessageActionItem-objektet brukeren valgte
            future.complete(requestParams.getActions().get(responseIdx));
        } else {
            // Brukeren lukket vinduet uten å velge noe
            future.complete(null);
        }
        return future;
    }

    // Hjelpemetode for å koble LSP-meldingstyper til UI-ikoner
    private int convertMessageType(org.eclipse.lsp4j.MessageType type) {
        switch (type) {
            case Error: return JOptionPane.ERROR_MESSAGE;
            case Warning: return JOptionPane.WARNING_MESSAGE;
            case Info: return JOptionPane.INFORMATION_MESSAGE;
            default: return JOptionPane.PLAIN_MESSAGE;
        }
    }

}
