package client;

import java.util.List;
import simula.SimulaCoreClient;
import simula.compiler.SourceModule;
import simula.core.builder.export.SimulaDiagnostic;

public class SimulaEditorClient implements SimulaCoreClient {

	@Override
	public void publishDiagnostics(String uri, List<SimulaDiagnostic> diagnostics) {
		SourceModule.publishDiagnostics(uri, diagnostics);
//		Util.IERR("NOT IMPL");
	}

	@Override
	public void showMessage(messageType type, String message) {
		IO.println("SimulaEditorClient.showMessage: " + type + "  " + message);
	}

	@Override
	public void logMessage(messageType type, String message) {
		System.err.println("SimulaEditorClient.logMessage: " + type + "  " + message);		
	}

	@Override
	public void initialized() {
		// TODO Auto-generated method stub
		
	}

}
