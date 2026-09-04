package simula.core.utilities;

import java.util.logging.Logger;

import simula.core.DocumentManager;

public class LOG {
//	private static final Logger logger = Logger.getLogger(SimulaServer.class.getName());
	private static final Logger logger = Logger.getAnonymousLogger();

	public static void error(String message) {
		logger.severe(message);
		DocumentManager.simulaCoreClient.logError(message);
	}

	public static void severe(String message) {
		logger.severe(message);
		DocumentManager.simulaCoreClient.logError(message);
	}

	public static void warning(String message) {
		logger.warning(message);
		DocumentManager.simulaCoreClient.logWarning(message);
	}

	public static void trace(String message) {
		if(DocumentManager.verbose) {
			logger.info(message);
			DocumentManager.simulaCoreClient.logInfo(message);
		}
	}

	public static void info(String message) {
//		if(DocumentManager.verbose) {
//			logger.info(message);
			DocumentManager.simulaCoreClient.logInfo(message);
//		}
	}
}
