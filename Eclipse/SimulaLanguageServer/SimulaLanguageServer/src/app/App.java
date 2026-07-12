package app;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

import debug.lsp.DEBUG_SimulaLanguageServerImpl;

public class App {
	//	  static Logger logger = LogManager.getLogger(App.class);
	static Logger logger = Logger.getAnonymousLogger();

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		logger.info("Start debugging");
		startServer(System.in, System.out);
	}

	public static void startServer(InputStream in, OutputStream out)
			throws InterruptedException, ExecutionException {
		DEBUG_SimulaLanguageServerImpl myServer = new DEBUG_SimulaLanguageServerImpl();

		Launcher<LanguageClient> l = LSPLauncher.createServerLauncher(myServer, in, out);
		Future<?> startListening = l.startListening();
		myServer.setRemoteProxy(l.getRemoteProxy());
		startListening.get();
	}
}