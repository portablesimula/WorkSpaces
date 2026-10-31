package simula.lsp.utilities;

import java.io.PrintStream;

import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;

import simula.lsp.DocumentManager;
import simula.lsp.SimulaLanguageServer;

public class LOG {
//	private static final Logger logger = Logger.getLogger(SimulaServer.class.getName());
//	private static final Logger logger = Logger.getAnonymousLogger();

	public static void error(String message) {
//		logger.severe(message);
		if(LspGlobal.INLINE_CONNECTED)
			System.err.println(message);
		else if(LspGlobal.debugConsole != null) {
			LspGlobal.debugConsole.writeError(message+"\n");
//			PrintStream prt = LspGlobal.debugConsole.getOutputStream();
//	        new Exception("LOG: Stack trace: ").printStackTrace(prt);
		} else if(SimulaLanguageServer.languageClient != null) {
			MessageParams params = new MessageParams(MessageType.Error, message);
			SimulaLanguageServer.languageClient.logMessage(params);
		}
	}

	public static void severe(String message) {
//		logger.severe(message);
//		SimulaLanguageServer.languageClient.logError(message);
		if(LspGlobal.INLINE_CONNECTED)
			System.err.println(message);
		else if(LspGlobal.debugConsole != null) {
			LspGlobal.debugConsole.writeError(message+"\n");
//			PrintStream prt = LspGlobal.debugConsole.getErrorStream();
//			new Exception("LOG: Stack trace: ").printStackTrace(prt);
		} else if(SimulaLanguageServer.languageClient != null) {
			MessageParams params = new MessageParams(MessageType.Error, message);
			SimulaLanguageServer.languageClient.logMessage(params);
		}
	}

	public static void warning(String message) {
//		logger.warning(message);
//		SimulaLanguageServer.languageClient.logWarning(message);
		if(LspGlobal.INLINE_CONNECTED)
			IO.println(message);
		else if(LspGlobal.debugConsole != null)
			LspGlobal.debugConsole.writeWarning(message+"\n");
		else if(SimulaLanguageServer.languageClient != null) {
			MessageParams params = new MessageParams(MessageType.Warning, message);
			SimulaLanguageServer.languageClient.logMessage(params);
		}
	}

	public static void trace(String message) {
		if(DocumentManager.verbose) {
//			logger.info(message);
//			SimulaLanguageServer.languageClient.logInfo(message);
			if(LspGlobal.INLINE_CONNECTED)
				IO.println(message);
			else if(LspGlobal.debugConsole != null)
				LspGlobal.debugConsole.write(message+"\n");
			else if(SimulaLanguageServer.languageClient != null) {
				MessageParams params = new MessageParams(MessageType.Info, message);
				SimulaLanguageServer.languageClient.logMessage(params);
			}
		}
	}

	public static void info(String message) {
//		if(DocumentManager.verbose) {
//			logger.info(message);
//			SimulaLanguageServer.languageClient.logInfo(message);
		if(LspGlobal.INLINE_CONNECTED)
			IO.println(message);
		else if(LspGlobal.debugConsole != null)
			LspGlobal.debugConsole.write(message+"\n");
		else if(SimulaLanguageServer.languageClient != null) {
			MessageParams params = new MessageParams(MessageType.Info, message);
			SimulaLanguageServer.languageClient.logMessage(params);
		}
	}
}
