package simula.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import simula.Option;
import simula.core.builder.SimulaBuilder;
import simula.core.coder.SimulaCoder;
import simula.core.coder.SimulaExec;
import simula.core.utilities.Util;

public class RunDebugManager {

	public static void launch(Map<String, Object> launchArgs) {
        // 1. Extract and validate parameters from client configuration
        String programPath = (String) launchArgs.get("program");
        @SuppressWarnings("unchecked")
		List<String> args = (List<String>) launchArgs.get("args");
        Boolean stopOnEntry = (Boolean) launchArgs.getOrDefault("stopOnEntry", false);

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
	

	// Generate code and execute simula program
	public static void run(final String documentUri, List<String> argv) {
//		Util.println("SimulaCoreExports.run: " + documentUri);
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
    	SimulaBuilder simBuilder = documentManager.simBuilder;

    	String[] args = argv.toArray(new String[0]);
		Option.decodeArguments2(documentManager, args);

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

}
