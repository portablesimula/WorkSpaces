package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import simula.SimulaCoreClient;
import simula.SimulaCoreExports;

public class SimulaEditorClient implements SimulaCoreClient {

	@Override
	public void showMessage(messageType type, String message) {
		IO.println("SimulaEditorClient.showMessage: " + type + "  " + message);
	}

	@Override
	public void logMessage(messageType type, String message) {
		System.err.println("SimulaEditorClient.logMessage: " + type + "  " + message);		
	}

	public static void doOpen(String fileName, Vector<String> argv) {
//		try {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");
		
		SimulaCoreExports.initiate(new SimulaEditorClient(), argv);
		String uri = fileName;
		int version = 1;
        try {
            String content = Files.readString(Path.of(fileName));
//            content = content.replace("\r\n", "\n");
    	    SimulaCoreExports.didOpen(uri, version, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
//	} catch (IOException e) {
//		Util.generalError("can't open " + fileName + ", reason: " + e);
//	}

	}

}
