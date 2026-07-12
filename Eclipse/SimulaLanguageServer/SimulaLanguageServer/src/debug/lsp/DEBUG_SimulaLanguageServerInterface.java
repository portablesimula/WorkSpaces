package debug.lsp;

import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.eclipse.lsp4j.services.LanguageServer;

/// The interfaces LanguageServer and LanguageClient implement the Endpoint interface.
/// In this way the underhood communication layer from clients to the server is already managed.
/// 
/// See: https://andzac.github.io/anwn/Development%20docs/Language%20Server/lsp4j/
@JsonSegment("server")
public interface DEBUG_SimulaLanguageServerInterface extends LanguageServer {}
