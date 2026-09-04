package simula;

import java.util.List;

import simula.core.builder.export.SimulaDiagnostic;
import simula.core.utilities.Util;

public interface SimulaCoreClient {
	/// See: Eclipse LSP API
	/// - org.eclipse.lsp4j.MessageType
	/// - org.eclipse.lsp4j.MessageParams
	/// 
	/// Package org.eclipse.lsp4j.services
	///    Interface LanguageClient
	///    - showMessahe
	///    - logMessage
	///    - logTrace
	///      ...
	
	enum messageType { Debug, Error, Info, Log, Warning }
	
	/// The initialized notification is sent from the client to the server
	/// after the client received the result of the initialize request,
	/// but before the client is sending any other request or notification
	/// to the server. The server can use the initialized notification,
	/// for example, to dynamically register capabilities.
	abstract public void initialized();//(InitializedParams params);
	
	/// Diagnostics notifications are sent from the server to the client to signal results of validation runs.
	abstract public void publishDiagnostics(String uri, List<SimulaDiagnostic> diagnostics);

	/// The show message notification is sent from a server to a client to ask the client to display a particular message in the user interface.
	/// void showMessage(MessageParams messageParams)
	abstract public void showMessage(messageType type, String message);
	
	///	The log message notification is sent from the server to the client to ask the client to log a particular message.
	/// void logMessage(MessageParams message)
	abstract public void logMessage(messageType type, String message);
	
	default public void warning(String message) {
		showMessage(messageType.Warning, message);
	}
	
	default public void error(String message) {
		showMessage(messageType.Error, message);
	}
	
	default public void logInfo(String message) {
		logMessage(messageType.Info, message);
	}
	
	default public void logWarning(String message) {
		logMessage(messageType.Warning, message);
	}
	
	default public void logError(String message) {
		logMessage(messageType.Error, message);
	}
}
