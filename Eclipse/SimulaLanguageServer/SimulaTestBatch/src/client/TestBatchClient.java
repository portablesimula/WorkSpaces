package client;

import java.util.List;

import simula.SimulaCoreClient;
import simula.core.builder.export.SimulaDiagnostic;

public class TestBatchClient implements SimulaCoreClient {

	@Override
	public void showMessage(messageType type, String message) {
		IO.println("TestBatchClient.showMessage: " + type + "  " + message);
	}

	@Override
	public void logMessage(messageType type, String message) {
		System.err.println("TestBatchClient.logMessage: " + type + "  " + message);		
	}

	@Override
	public void initialized() {
		// TODO Auto-generated method stub
		IO.println("TestBatchClient.initialized: ");		
	}

	@Override
	public void publishDiagnostics(String uri, List<SimulaDiagnostic> diagnostics) {
		// TODO Auto-generated method stub
		IO.println("TestBatchClient.publishDiagnostics: for " + uri);
		for(SimulaDiagnostic diag:diagnostics) {
			IO.println("TestBatchClient.publishDiagnostics: - " + diag);			
		}
	}

}
