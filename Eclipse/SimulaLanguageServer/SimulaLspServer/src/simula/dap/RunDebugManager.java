package simula.dap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.eclipse.lsp4j.ClientInfo;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.TraceValue;

import simula.lsp.DocumentManager;
import simula.lsp.SimulaTextDocumentService;
import simula.lsp.builder.SimulaBuilder;
import simula.lsp.coder.SimulaCoder;
import simula.lsp.coder.SimulaExec;
import simula.lsp.utilities.LOG;
import simula.lsp.utilities.LspGlobal;
import simula.lsp.utilities.Option;
import simula.lsp.utilities.Util;

public class RunDebugManager {

	public static void launch(Map<String, Object> launchArgs) {
        // 1. Extract and validate parameters from client configuration
//        String programPath = (String) launchArgs.get("program");
//        @SuppressWarnings("unchecked")
//		List<String> args = (List<String>) launchArgs.get("args");
        Boolean stopOnEntry = (Boolean) launchArgs.getOrDefault("stopOnEntry", false);
        String simulaRtsLib = (String) launchArgs.get("simulaRtsLib");

//        launchArgs.put("documentUri", documentUri.toString());
//        launchArgs.put("mode", mode);
        String documentUri = (String) launchArgs.get("documentUri");
        String mode = (String) launchArgs.get("mode");

        
        LOG.info("RunDebugManager.launch: DETTE MÅ FORSTÅS OG IMPLEMENTERES");
//        LOG.info("RunDebugManager.launch: programPath: " + programPath);
        LOG.info("RunDebugManager.launch: documentUri: " + documentUri);
        LOG.info("RunDebugManager.launch: mode:        " + mode);
        LOG.info("RunDebugManager.launch: stopOnEntry: " + stopOnEntry);
        LOG.info("RunDebugManager.launch: simulaRtsLib: " + simulaRtsLib);
        
        launchArgs.forEach((key, value) -> {
        	System.out.println(key + ": " + value);
            LOG.info("RunDebugManager.launch: " + key + ": " + value);
            switch(key) {
	            case "verbose":      SimulaCoder.RTOption_VERBOSE = true; break;
	            case "blockTracing": SimulaCoder.RTOption_BLOCK_TRACING = true; break;
	            case "gotoTracing":  SimulaCoder.RTOption_GOTO_TRACING = true; break;
	            case "qpsTracing":   SimulaCoder.RTOption_QPS_TRACING = true; break;
	            case "smlTracing":   SimulaCoder.RTOption_SML_TRACING = true; break;
            }
        });
        
//		if(SimulaCoder.RTOption_VERBOSE) args.add("-verbose");
//		if(SimulaCoder.RTOption_BLOCK_TRACING) args.add("-blockTracing");
//		if(SimulaCoder.RTOption_GOTO_TRACING) args.add("-gotoTracing");
//		if(SimulaCoder.RTOption_QPS_TRACING) args.add("-qpsTracing");
//		if(SimulaCoder.RTOption_SML_TRACING) args.add("-smlTracing");

        DocumentManager.simulaRtsLib = new File(simulaRtsLib);
        LOG.info("RunDebugManager.launch: DocumentManager.simulaRtsLib: " + DocumentManager.simulaRtsLib);

    	doRun(documentUri);
	}
	
	public static void doOpen(String documentUri) {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

//		// Parse runtime arguments.
//		String[] args = argv.toArray(new String[0]);
//		Option.decodeArguments(args);

		// Start SimulaLanguageServer and Connect
		LspGlobal.initiate();
		LspGlobal.INLINE_CONNECTED = true;
//		Util.println("TestBatchLauncher.run: before CoreGlobal.simulaLanguageServer.connect");
		LspGlobal.simulaLanguageServer.connect(new TestBatchClient());
			
		// Initialize SimulaLanguageServer
//		ClientCapabilities capabilities = null;
		ClientInfo clientInfo = new ClientInfo("SimulaEditor");
		String trace = TraceValue.Off;      // No Tracing
//		String trace = TraceValue.Messages; // Single message trace
//		String trace = TraceValue.Verbose;  // Full systematic trace

		InitializeParams initializeParams = new InitializeParams();
//		initializeParams.setCapabilities(capabilities);
		initializeParams.setClientInfo(clientInfo);
		initializeParams.setTrace(trace);
		@SuppressWarnings("unused")
		InitializeResult result = LspGlobal.simulaLanguageServer.initialize_local(initializeParams);
			
//		SimulaCoreInitialize.initiate(argv);
		
//		String documentUri = documentUri;
		int version = 1;
		try {
			String content = Files.readString(Path.of(documentUri));
			SimulaTextDocumentService simulaTextDocumentService = LspGlobal.getSimulaTextDocumentService();
			TextDocumentItem textDocument = new TextDocumentItem(documentUri, "Simula", version, content);

			DidOpenTextDocumentParams params = new DidOpenTextDocumentParams();
			params.setTextDocument(textDocument);
//			IO.println("SourceModule.doOpenSimulaModule: " + params);
			simulaTextDocumentService.didOpen(params);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	// Generate code and execute simula program
	// Called from: SimulaLanguageServer.main  when --debugRun
	public static void run(final String documentUri, List<String> argv) {
//		Util.println("RunDebugManager.run(2): documentUri: " + documentUri);
//		Util.println("RunDebugManager.run(2): DocumentManager.simulaRtsLib: " + DocumentManager.simulaRtsLib);
    	if(argv != null) {
	    	String[] args = argv.toArray(new String[0]);
			Option.decodeArguments(args);
    	}
    	doRun(documentUri);
	}

	private static void doRun(final String documentUri) {
		Util.println("RunDebugManager.doRun: documentUri: " + documentUri);
//		Util.println("RunDebugManager.doRun: DocumentManager.simulaRtsLib: " + DocumentManager.simulaRtsLib);
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
		@SuppressWarnings("unused")
		List<Integer> semtokens = DocumentManager.semanticTokensFull(documentUri);
		
    	SimulaBuilder simBuilder = documentManager.simBuilder;
    	if(simBuilder.nErrors != 0) {
    		Util.IERR("Can't generate code due to " + simBuilder.nErrors + " errors.");
    	}
    	SimulaCoder simCoder = null;
    	try {
			simCoder = new SimulaCoder(documentManager);
			simCoder.doCodeGeneration(simBuilder.documentManager.getSyntaxTree());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(simBuilder.nErrors != 0) {
    		Util.IERR("Can't run due to " + simBuilder.nErrors + " errors.");
    	}
    	try {
			SimulaExec.doRun(simCoder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void doClose(String documentUri) {
		SimulaTextDocumentService simulaTextDocumentService = LspGlobal.getSimulaTextDocumentService();
		TextDocumentIdentifier textDocument = new TextDocumentIdentifier(documentUri);
		DidCloseTextDocumentParams params = new DidCloseTextDocumentParams(textDocument);		
		simulaTextDocumentService.didClose(params);
	}

}
