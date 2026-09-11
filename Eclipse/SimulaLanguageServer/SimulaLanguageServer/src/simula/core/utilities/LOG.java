package simula.core.utilities;

import java.util.logging.Logger;

import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;

import simula.core.DocumentManager;
import simula.lsp.server.SimulaLanguageServer;

public class LOG {
//	private static final Logger logger = Logger.getLogger(SimulaServer.class.getName());
	private static final Logger logger = Logger.getAnonymousLogger();

	public static void error(String message) {
		logger.severe(message);
		MessageParams params = new MessageParams(MessageType.Error, message);
		SimulaLanguageServer.languageClient.logMessage(params);
	}

	public static void severe(String message) {
		logger.severe(message);
//		SimulaLanguageServer.languageClient.logError(message);
		MessageParams params = new MessageParams(MessageType.Error, message);
		SimulaLanguageServer.languageClient.logMessage(params);
	}

	public static void warning(String message) {
		logger.warning(message);
//		SimulaLanguageServer.languageClient.logWarning(message);
		MessageParams params = new MessageParams(MessageType.Warning, message);
		SimulaLanguageServer.languageClient.logMessage(params);
	}

	public static void trace(String message) {
		if(DocumentManager.verbose) {
			logger.info(message);
//			SimulaLanguageServer.languageClient.logInfo(message);
			MessageParams params = new MessageParams(MessageType.Info, message);
			SimulaLanguageServer.languageClient.logMessage(params);
		}
	}

	public static void info(String message) {
//		if(DocumentManager.verbose) {
//			logger.info(message);
//			SimulaLanguageServer.languageClient.logInfo(message);
		MessageParams params = new MessageParams(MessageType.Info, message);
		SimulaLanguageServer.languageClient.logMessage(params);
//		}
	}
}
