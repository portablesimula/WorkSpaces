package simula.core.debug;

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

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.SimulaBuilder;
import simula.core.coder.SimulaCoder;
import simula.core.coder.SimulaExec;
import simula.core.utilities.LOG;
import simula.core.utilities.Util;
import simula.server.SimulaTextDocumentService;

public class RunDebugManager {

	public static void launch(Map<String, Object> launchArgs) {
        // 1. Extract and validate parameters from client configuration
        String programPath = (String) launchArgs.get("program");
        @SuppressWarnings("unchecked")
		List<String> args = (List<String>) launchArgs.get("args");
        Boolean stopOnEntry = (Boolean) launchArgs.getOrDefault("stopOnEntry", false);
        
        LOG.info("RunDebugManager.launch: DETTE MÅ FORSTÅS OG IMPLEMENTERES");
        LOG.info("RunDebugManager.launch: programPath: " + programPath);
        LOG.info("RunDebugManager.launch: stopOnEntry: " + stopOnEntry);

//        if (programPath == null || programPath.isEmpty()) {
//            throw new IllegalArgumentException("The 'program' path must be specified.");
//        }
//
//        // 2. Start your target process (the "debuggee")
//        ProcessBuilder builder = new ProcessBuilder();
//        builder.command(programPath);
//        if (args != null) {
//            builder.command().addAll(args);
//        }
//        
//        // Spawn the process
//        this.debuggeeProcess = builder.start();
//
//        // 3. Connect your internal debugger engine to this process
//        // (e.g., attach a JVM debugger via JDWP, LLDB, or an interpreter loop)
//        myDebuggerEngine.attachToProcess(this.debuggeeProcess, stopOnEntry);
        
        String documentUri = "?????";
        run(documentUri, args);
	}
	
	public static void doOpen(String documentUri) {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

//		// Parse runtime arguments.
//		String[] args = argv.toArray(new String[0]);
//		Option.decodeArguments(args);

		// Start SimulaLanguageServer and Connect
		CoreGlobal.initiate();
		CoreGlobal.INLINE_CONNECTED = true;
//		Util.println("TestBatchLauncher.run: before CoreGlobal.simulaLanguageServer.connect");
		CoreGlobal.simulaLanguageServer.connect(new TestBatchClient());
			
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
		InitializeResult result = CoreGlobal.simulaLanguageServer.initialize_local(initializeParams);
			
//		SimulaCoreInitialize.initiate(argv);
		
//		String documentUri = documentUri;
		int version = 1;
		try {
			String content = Files.readString(Path.of(documentUri));
			SimulaTextDocumentService simulaTextDocumentService = CoreGlobal.getSimulaTextDocumentService();
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
	// Called from: RunDebugManager.launch     when SimulaDebugAdapter is launched
	public static void run(final String documentUri, List<String> argv) {
//		Util.println("SimulaCoreExports.run: " + documentUri);
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
    	SimulaBuilder simBuilder = documentManager.simBuilder;

    	String[] args = argv.toArray(new String[0]);
		Option.decodeArguments(args);

//    	SimulaCompiler simulaCompiler = new SimulaCompiler(documentManager);
//    	simulaCompiler.doCompile();
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
		SimulaTextDocumentService simulaTextDocumentService = CoreGlobal.getSimulaTextDocumentService();
		TextDocumentIdentifier textDocument = new TextDocumentIdentifier(documentUri);
		DidCloseTextDocumentParams params = new DidCloseTextDocumentParams(textDocument);		
		simulaTextDocumentService.didClose(params);
	}

}
