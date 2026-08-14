package simula.core.coder;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.CoreGlobal2;
import simula.core.builder.SimulaBuilder;
import simula.core.syntaxClass.statement.ProgramModule;
import simula.core.utilities.Util;

public class SimulaExec {

	// ***************************************************************
	// *** Run generated .jar File
	// ***************************************************************
	public static void doRun(SimulaBuilder simBuilder) throws IOException {
		Vector<String> cmds = new Vector<String>();
		String jarFile = simBuilder.generatedJarFile.toString();
		cmds.add("java");
		if(Option.editorUIScale != null  && !Option.editorUIScale.equals("1")) {
			// java -Dsun.java2d.uiScale=2 -jar application.jar
			String uiScaleOption = "-Dsun.java2d.uiScale=" + Option.editorUIScale;
			cmds.add(uiScaleOption);
		}
		cmds.add("-jar");
		cmds.add(jarFile);
		if (Option.internal.RUNTIME_USER_DIR.length() > 0) {
			cmds.add("-userDir");
			cmds.add(Option.internal.RUNTIME_USER_DIR);
		}
		
		addRTArguments(cmds);
		
		if(CoreGlobal2.noPopup) {
			cmds.add("-noPopup");			
		}
		if (Option.internal.SOURCE_FILE != null) {
			cmds.add(Option.internal.SOURCE_FILE);
		}
		doExecuteJarFile(simBuilder, jarFile, cmds);
		
		if (Option.internal.DEBUGGING)
			IO.println("------------  CLEANING UP TEMP FILES  ------------");
		DocumentManager.deleteTempFiles(SimulaCoder.simulaTempDir);
	}

    /// Add Runtime options to the argument vector.
    /// @param args the argument vector
	public static void addRTArguments(Vector<String> args) {
		if(SimulaCoder.RTOption_VERBOSE) args.add("-verbose");
		if(SimulaCoder.RTOption_BLOCK_TRACING) args.add("-blockTracing");
		if(SimulaCoder.RTOption_GOTO_TRACING) args.add("-gotoTracing");
		if(SimulaCoder.RTOption_QPS_TRACING) args.add("-qpsTracing");
		if(SimulaCoder.RTOption_SML_TRACING) args.add("-smlTracing");
	}

	/// Execute JarFile.
	/// @param jarFile a jarFile
	/// @param arg the arguments
	/// @throws IOException if something went wrong.
	private static void doExecuteJarFile(SimulaBuilder simBuilder, String jarFile, Vector<String> arg) throws IOException {
		ProgramModule programModule = simBuilder.syntaxTree;
		if (!programModule.isExecutable()) {
			if (CoreGlobal2.verbose)
				IO.println("Separate Compilation - No Execution of .jar File: " + jarFile);
		} else if (CoreGlobal2.noExecution) {
			if (CoreGlobal2.verbose)
				IO.println("Option 'noexec' ==> No Execution of .jar File: " + jarFile);
		} else {
			if (CoreGlobal2.verbose) {
				IO.println("------------  EXECUTION SUMMARY  ------------");
				IO.println("Execute .jar File");
			}
			int exitValue3 = Util.execute(arg);
			if (CoreGlobal2.verbose)
				IO.println("END Execute .jar File. Exit value=" + exitValue3);
			if(exitValue3 != 0) {
				IO.println("SimulaCompiler.doCompile: Exit value = " + exitValue3);
		    	Util.doListDirectory("SimulaCompiler.doExecuteJarFile: ", ""+SimulaCoder.tempClassFileDir);
		    	Util.doListDirectory("SimulaCompiler.doExecuteJarFile: ", ""+SimulaCoder.tempClassFileDir + "/" + CoreGlobal2.packetName);
		    	JarFileBuilder.listJarFile("SimulaCompiler.doExecuteJarFile: ",new File(jarFile));
				throw new RuntimeException("Execution of "+jarFile+" failed. ExitValue = "+exitValue3);
			}
		}
	}

}
