package com.demo.client;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.LanguageClient;

public class MyLanguageClient implements LanguageClient {
    @Override
//    public void logMessage(LogMessageParams message) {
    public void logMessage(MessageParams message) {
        System.out.println("LOGG FRA SERVER: " + message.getMessage());
    }

//    @Override
//    public void showMessage(ShowMessageParams message) {}
//
//    @Override
//    public void showMessageRequest(ShowMessageRequestParams request) return null;

    @Override
    public void telemetryEvent(Object object) {}

    // +++++++++++++++++++++++++++++
    
	@Override
	public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showMessage(MessageParams messageParams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
		// TODO Auto-generated method stub
		return null;
	}
}
