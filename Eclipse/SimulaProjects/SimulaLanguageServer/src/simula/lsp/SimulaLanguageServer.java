package simula.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import simula.compiler.utilities.LOG;
import simula.lsp.client.SimulaDebugClient;
import simula.lsp.compiler.DocumentManager;
import simula.lsp.compiler.SourceDocumentItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class SimulaLanguageServer implements LanguageServer, LanguageClientAware {

    private LanguageClient client;
    private final TextDocumentService textDocumentService;
    private final WorkspaceService workspaceService;
    private final DocumentManager documentManager;

//  public MyTextDocumentService(DocumentManager documentManager) {
//      this.documentManager = documentManager;
//  }

    public SimulaLanguageServer() {
        this.textDocumentService = new SimulaTextDocumentService(this);
        this.workspaceService = new SimulaWorkspaceService();
        this.documentManager = new DocumentManager();
    }

    // Definert i LanguageClientAware: Mottar klienten fra launcheren
    @Override
    public void connect(LanguageClient client) {
        this.client = client;
        // Valgfritt: Send en infomelding til klienten med en gang vi er koblet til
        MessageParams message = new MessageParams(MessageType.Info, "Serveren er koblet til klienten! " + client.getClass());
        client.logMessage(message);
        if(client instanceof SimulaDebugClient extendedLanguageClient) {
        	extendedLanguageClient.start(this);
        }
    }

    // Gjør klienten tilgjengelig for undertjenestene (f.eks. for å publisere feilmeldinger)
    public LanguageClient getClient() {
        return this.client;
    }
    
    public DocumentManager getDocumentManager() {
        return this.documentManager;
    }

    /// I din LanguageServer-implementasjon (i initialize-metoden),
    /// må du returnere TextDocumentSyncKind.Full eller Incremental:
    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
    	LOG.info("SimulaLanguageServer.initialize: BEGIN");
        ServerCapabilities capabilities = new ServerCapabilities();
        
//        // Fortell klient VSCode/Eclipse/IntelliJ ... at vi vil ha beskjed når filer åpnes, endres og lukkes
//        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
        
        // Set sync kind to Full
        TextDocumentSyncOptions syncOptions = new TextDocumentSyncOptions();
        syncOptions.setChange(TextDocumentSyncKind.Full);
        syncOptions.setOpenClose(true);
        
        // Explicitly configure Save Options so didSave is sent
        SaveOptions saveOptions = new SaveOptions();
        saveOptions.setIncludeText(false); // False because you already get full text via didChange
        syncOptions.setSave(saveOptions);
        syncOptions.setWillSave(true); // <--- Critical flag
        
        capabilities.setTextDocumentSync(syncOptions);

    	LOG.info("SimulaLanguageServer.initialize: RETURNS");
        return CompletableFuture.completedFuture(new InitializeResult(capabilities));
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.completedFuture(new Object());
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return this.textDocumentService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return this.workspaceService;
    }
    
    public List<Diagnostic> getDiagnostics(String documentUri) {
		SourceDocumentItem sourceItem = documentManager.get(documentUri);
		return sourceItem.getDiagnostis();
    }

}
