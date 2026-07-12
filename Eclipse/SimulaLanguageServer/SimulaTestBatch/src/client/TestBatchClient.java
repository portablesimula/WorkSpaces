package client;

import simula.SimulaCoreClient;

public class TestBatchClient implements SimulaCoreClient {

	@Override
	public void showMessage(messageType type, String message) {
		IO.println("TestBatchClient.showMessage: " + type + "  " + message);
	}

	@Override
	public void logMessage(messageType type, String message) {
		System.err.println("TestBatchClient.logMessage: " + type + "  " + message);		
	}

}
