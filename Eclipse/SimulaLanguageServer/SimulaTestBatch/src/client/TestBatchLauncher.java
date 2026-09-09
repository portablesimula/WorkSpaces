package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentItem;

import simula.SimulaCoreInitialize;
import simula.core.CoreGlobal;
import simula.lsp.server.SimulaExecutor;
import simula.lsp.server.SimulaTextDocumentService;

public class TestBatchLauncher {

	public static void run(String fileName, Vector<String> argv, Vector<String> argv2) {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

		SimulaCoreInitialize.connect(new TestBatchClient());
		SimulaCoreInitialize.initiate(argv);
		String documentUri = fileName;
		int version = 1;
		try {
			String content = Files.readString(Path.of(fileName));
			SimulaTextDocumentService simulaTextDocumentService = CoreGlobal.getSimulaTextDocumentService();
			TextDocumentItem textDocument = new TextDocumentItem(documentUri, "Simula", version, content);

			DidOpenTextDocumentParams params = new DidOpenTextDocumentParams();
			params.setTextDocument(textDocument);
//			IO.println("SourceModule.doOpenSimulaModule: " + params);
			simulaTextDocumentService.didOpen(params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SimulaExecutor.run(documentUri, argv2);
	}
}
