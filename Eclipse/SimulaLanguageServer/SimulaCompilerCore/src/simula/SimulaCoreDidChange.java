package simula;

import java.util.List;

import org.eclipse.lsp4j.TextDocumentContentChangeEvent;

import simula.core.DocumentManager;
import simula.core.utilities.LOG;

public class SimulaCoreDidChange {
    
    /// The document change notification is sent from the client to the server to
	/// signal changes to a text document.
	public static void didChange(final String documentUri, final List<TextDocumentContentChangeEvent> changes) {
    	LOG.info("got Notification: didChange: " + documentUri);
    	LOG.severe("SimulaCoreExports.didChange NOT IMPL");
    	DocumentManager.didChange(documentUri, changes);
	}

}
