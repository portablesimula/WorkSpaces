package debug.lsp;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.ServerInfo;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import simula.lsp.server.SimulaTextDocumentService;
import simula.lsp.server.SimulaWorkspaceService;

//public class SimulaLanguageServerImpl implements SimulaLanguageServer {
	
public class DEBUG_SimulaLanguageServerImpl implements LanguageServer, LanguageClientAware {

	
	private final List<LanguageClient> clients = new CopyOnWriteArrayList<>();
	private TextDocumentService textService = new SimulaTextDocumentService(null);
    private final WorkspaceService workspaceService = new SimulaWorkspaceService();

	public DEBUG_SimulaLanguageServerImpl() {
		//initialize textDocumentService and workspaceService
	}
	

//	public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	@Override
	public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
	    // 1. Opprett et resultat-objekt
	    InitializeResult result = new InitializeResult();
	    
	    // 2. Definer hva serveren din skal klare (Capabilities)
	    ServerCapabilities capabilities = new ServerCapabilities();
	    
	    // Fortell klienten hvordan filer skal synkroniseres (Fullstendig overskriving er enklest)
	    capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
	    
	    // Aktiver funksjoner du vil støtte
	    capabilities.setCompletionProvider(new org.eclipse.lsp4j.CompletionOptions());
	    capabilities.setHoverProvider(true);
	    capabilities.setDefinitionProvider(true);
	    
	    result.setCapabilities(capabilities);
	    
	    // 3. Legg til informasjon om serveren din
	    ServerInfo serverInfo = new ServerInfo("Simula Eclipse LSP-Server", "1.0.0");
	    result.setServerInfo(serverInfo);
	    
	    // 4. Returner som en CompletableFuture
	    return CompletableFuture.completedFuture(result);
	}

	public CompletableFuture<Object> shutdown() {
		// TODO Auto-generated method stub
		return null;
	}

	public void exit() {
		// TODO Auto-generated method stub

	}

	public TextDocumentService getTextDocumentService() {
		return textService;
	}

	public WorkspaceService getWorkspaceService() {
		return workspaceService;
	}

	public Runnable setRemoteProxy(LanguageClient languageClient) {
		this.clients.add(languageClient);
		return () -> this.clients.remove(languageClient);
	}

	public List<LanguageClient> getClientList() {
		return clients;
	}


	@Override
	public void connect(LanguageClient client) {
		// TODO Auto-generated method stub
		
	}
}
