package debug.lsp;

import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;

import simula.InitializeParams;

/// The interfaces LanguageServer and LanguageClient implement the Endpoint interface.
/// In this way the underhood communication layer from clients to the server is already managed.
/// 
/// See: https://andzac.github.io/anwn/Development%20docs/Language%20Server/lsp4j/
@JsonSegment("client")
public interface DEBUG_RemoteLanguageClient extends LanguageClient {
	public void start(LanguageServer serverLauncher);
}