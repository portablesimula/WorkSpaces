package simula.lsp.server;

import java.util.List;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.services.LanguageClient;

import simula.SimulaCoreClient;

public class SimulaCoreClientProxy implements SimulaCoreClient {
	
	LanguageClient lspClient;
	
	
	public SimulaCoreClientProxy(LanguageClient lspClient) {
		// TODO Auto-generated constructor stub
		this.lspClient = lspClient;
	}

	@Override
	public void showMessage(messageType type, String message) {
		// TODO Auto-generated method stub
		MessageParams params = null;
		lspClient.showMessage(params);
	}

	@Override
	public void logMessage(messageType type, String message) {
		// TODO Auto-generated method stub
		MessageParams params = null;
		lspClient.logMessage(params);
	}


    public static List<TextDocumentContentChangeEvent> convert(List<TextDocumentContentChangeEvent> changes) {
		// TODO Auto-generated method stub
    	throw new RuntimeException("NOT IMPL");
//		return null;
	}

	@Override
	public void initialized() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publishDiagnostics(String uri, List<Diagnostic> diagnostics) {
		// TODO Auto-generated method stub
		
	}

}
