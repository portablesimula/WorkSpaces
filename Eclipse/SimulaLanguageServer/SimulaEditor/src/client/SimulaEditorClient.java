package client;

import java.util.List;
import simula.SimulaCoreClient;
import simula.core.builder.export.SimulaDiagnostic;
import simula.editor.SourceModule;
import simula.editor.utilities.Global;
import simula.editor.utilities.Util;

public class SimulaEditorClient implements SimulaCoreClient {

	@Override
	public void publishDiagnostics(String uri, List<SimulaDiagnostic> diagnostics) {
		SourceModule.publishDiagnostics(uri, diagnostics);
	}

	@Override
	public void showMessage(messageType type, String message) {
		IO.println("SimulaEditorClient.showMessage: " + type + "  " + message);
		Util.IERR("");
		write(type, "Server MSG: " + message + '\n');
	}

	@Override
	public void logMessage(messageType type, String message) {
		System.err.println("SimulaEditorClient.logMessage: Type:" + type + "  Message:" + message);	
		IO.println("SimulaEditorClient.logMessage: Global.currentModule=" + Global.currentModule);
		write(type, "Server LOG: " + message + '\n');
	}
	
	private void write(messageType type, String message) {
		System.err.println("SimulaEditorClient.logMessage: Type:" + type + "  Message:" + message);	
		IO.println("SimulaEditorClient.logMessage: Global.currentModule=" + Global.currentModule);
		switch(type) {
			case Debug, Error: Global.console.writeError(message); break;
			case Warning:      Global.console.writeWarning(message); break;
			default:           Global.console.write(message);
		}
	}

	@Override
	public void initialized() {
		messageType type = SimulaCoreClient.messageType.Debug;
		write(type, "Simula LanguageServer Initialized !");
	}

}
