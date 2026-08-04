package simula.compiler.utilities;

import java.util.logging.Logger;

import simula.compiler.SimulaCompiler;

public class LOG {
//	private static final Logger logger = Logger.getLogger(SimulaServer.class.getName());
	private static final Logger logger = Logger.getAnonymousLogger();

	public static void error(String message) {
		logger.severe(message);
	}

	public static void severe(String message) {
		logger.severe(message);
	}

	public static void warning(String message) {
		logger.warning(message);
	}

	public static void trace(String message) {
		if(SimulaCompiler.verbose)
			logger.info(message);
	}

	public static void info(String message) {
		if(SimulaCompiler.verbose)
			logger.info(message);
	}
}
