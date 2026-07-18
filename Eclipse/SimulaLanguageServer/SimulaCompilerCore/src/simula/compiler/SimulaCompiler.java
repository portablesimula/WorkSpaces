package simula.compiler;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import simula.Option;
import simula.builder.SimulaBuilder;
import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.transform.ClassFileTransform;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Util;
import simula.exception.EOTException;
import simula.lsp.compiler.DocumentManager;

public class SimulaCompiler {
	DocumentManager documentManager;
	
	public SimulaCompiler(DocumentManager documentManager) {
		this.documentManager = documentManager;
		StandardClass.INITIATE();
	}

	// ***************************************************************
	// *** Scanning and Parsing
	// ***************************************************************
	public boolean doParsing(SimulaBuilder simBuilder) {
		boolean builderTerminateNormally = false;
		simBuilder.duringParsing = true;
    	LOG.info("SimulaCompiler.doParsing: BEGIN");
        // Do the actual Building
		simBuilder.getNextParserToken();
		simBuilder.syntaxTree = new ProgramModule(simBuilder);
        try {
        	simBuilder.syntaxTree.doBuild();
        	builderTerminateNormally = true;
        } catch(EOTException e) {
			System.err.println("SimulaBuilder: GOT EXCEPTION: " + e.getMessage());
//			e.printStackTrace();
			simBuilder.lexer.flush();
        }

    	LOG.info("SimulaBuilder: syntaxTree, tokenList and diagnostics DONE");
    	IO.println("SimulaBuilder: this.syntaxTree: "+simBuilder.syntaxTree); // Root of Syntax Tree
    	IO.println("SimulaBuilder: this.diagnostics: "+simBuilder.diagnostics);
    	IO.println("SimulaBuilder: this.tokenList: "+simBuilder.tokenList);
		
    	simBuilder.printAll(" AFTER NEW SimulaBuilder: ");
		
    	return builderTerminateNormally;
	}

	// ***************************************************************
	// *** Semantic Checker
	// ***************************************************************
	public void doChecking(SimulaBuilder simBuilder) {
		if (Option.internal.TRACING)
			Util.println("BEGIN Semantic Checker");
		simBuilder.duringParsing = false;
		simBuilder.duringChecking = true;
    	LOG.info("SimulaCompiler.doChecking: BEGIN");
		StandardClass.ENVIRONMENT.doChecking();
		ProgramModule programModule = simBuilder.syntaxTree;
		programModule.doChecking();
		
//		programModule.doChecking();
		if (Option.internal.TRACING) {
			Util.println("END Semantic Checker: \"" + programModule + "\"");
			if (Option.internal.TRACE_CHECKER_OUTPUT && programModule != null)
				programModule.print(0);
		}
		if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + simBuilder.documentManager.sourceName + ": Semantic Checker completed");
		simBuilder.duringChecking = false;
		if(Option.internal.PRINT_SYNTAX_TREE > 0) {
			IO.println("\nSimulaCompiler.doCompile: =========== Resulting Syntax Tree after Checking ================");
			programModule.printTree(1,this);
		}
		
		if (Util.nError > 0) {
			String msg="Compiler terminate " + simBuilder.documentManager.sourceName + " after " + Util.nError + " errors during semantic checking";
			Util.println(msg);
//			Thread.dumpStack();
			throw new RuntimeException(msg);
		}

	}

	// ***************************************************************
	// *** Code Generation
	// ***************************************************************
	public void doCodeGeneration(SimulaBuilder simBuilder) throws IOException {
		ProgramModule  programModule = documentManager.getSyntaxTree();
		switch(Option.compilerMode) {
			case directClassFiles:
	
				// Create Temp .class-Files Directory:
				File tmpClassDir = new File(CoreGlobal.simulaTempDir, "classes");
				tmpClassDir.mkdirs();
				CoreGlobal.tempClassFileDir = tmpClassDir;
		    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempClassFileDir="+CoreGlobal.tempClassFileDir);
				break;
//			case simulaClassLoader:
//				break;
			case viaJavaSource:
				CoreGlobal.javaSourceFileCoders = new Vector<JavaSourceFileCoder>();
				// Create Temp .java-Files Directory:
				File javatmp = Option.internal.keepJava;
				if (javatmp == null)
					javatmp = CoreGlobal.simulaTempDir;
				File tmpJavaDir = new File(javatmp, "src/" + Option.packetName);
				tmpJavaDir.mkdirs();
				CoreGlobal.tempJavaFileDir = tmpJavaDir;
		    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempJavaFileDir="+CoreGlobal.tempJavaFileDir);
				break;
			default:
				break;
		}
		Option.print("SimulaCompiler.doCodeGeneration: ");
		simBuilder.jarFileBuilder = new JarFileBuilder();
		try {
			simBuilder.jarFileBuilder.open(programModule);
			simBuilder.jarFileBuilder.addIncludeQueue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Option.compilerMode != Option.CompilerMode.viaJavaSource) {
			if (Option.internal.TRACING)
				Util.println("BEGIN Generate .class Output Code");
			// *** Generate .class files
			programModule.createJavaClassFile();
			if(Option.verbose) Util.println(simBuilder.documentManager.sourceName + ": Class Files Generated - Directly");
		} else {
			if (Option.internal.TRACING)
				Util.println("BEGIN Generate .java Output Code");
			// *** Generate .java intermediate code
			programModule.doJavaCoding();
			if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + simBuilder.documentManager.sourceName + ": Java Source Files Generated");
			if (Option.internal.TRACING) {
				Util.println("END Generate .java Output Code");
				for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders)
					Util.println(javaClass.javaOutputFile.toString());
			}
		}
		if (Util.nError > 0) {
			String msg="Compiler terminate " + simBuilder.documentManager.sourceName + " after " + Util.nError + " errors during code generation";
			Util.println(msg);
			throw new RuntimeException(msg);
		}

		if (Option.verbose)
			fileSummary(simBuilder);
		if (Option.internal.DEBUGGING) {
			Util.println("------------  CLASSPATH DETAILS  ------------");
			Util.println("Java PathSeparator " + System.getProperty("path.separator"));
			Util.println("Java ClassPath     " + System.getProperty("java.class.path"));
		}

		if(Option.compilerMode == Option.CompilerMode.viaJavaSource) {
			// ***************************************************************
			// *** CALL JAVA COMPILER
			// *** POSSIBLE -- DO BYTE_CODE_ENGINEERING
			// *** POSSIBLE - LIST GENERATED .class FILES
			// ***************************************************************
			doCallJavaCompiler(simBuilder);
			doByteCodeEngineering(simBuilder);
			if(Option.internal.LIST_GENERATED_CLASS_FILES)
				listGeneratedClassFiles(simBuilder);
		}
		AttributeFileIO.writeAttributeFile(programModule);

		// ***************************************************************
		// *** CRERATE .jar FILE INLINE
		// ***************************************************************
//		String jarFile = null;
			if(Option.compilerMode == Option.CompilerMode.viaJavaSource) {
				simBuilder.jarFileBuilder.addTempClassFiles();
			}
			simBuilder.generatedJarFile = simBuilder.jarFileBuilder.close();
		
		if (Option.verbose) printSummary();
		Util.IERR("NOT IMPL");
	}

	// ***************************************************************
	// *** Run generated .jar File
	// ***************************************************************
	public void doRun(SimulaBuilder simBuilder) throws IOException {
		Vector<String> cmds = new Vector<String>();
		String jarFile = simBuilder.generatedJarFile.toString();
		cmds.add("java");
   		if(Option.compilerMode != Option.CompilerMode.simulaClassLoader) {
			if(Option.editorUIScale != null  && !Option.editorUIScale.equals("1")) {
				// java -Dsun.java2d.uiScale=2 -jar application.jar
				String uiScaleOption = "-Dsun.java2d.uiScale=" + Option.editorUIScale;
				cmds.add(uiScaleOption);
			}
			cmds.add("-jar");
			cmds.add(jarFile);
		}
		if (Option.internal.RUNTIME_USER_DIR.length() > 0) {
			cmds.add("-userDir");
			cmds.add(Option.internal.RUNTIME_USER_DIR);
//		} else {
//			cmds.add("-userDir");
//			cmds.add(Global.outputDir.getParentFile().getAbsolutePath());
		}
		
		Util.IERR("SJEKK DETTE");
		//RTOption.addRTArguments(cmds);
		
		
		if(Option.noPopup) {
			cmds.add("-noPopup");			
		}
		if (Option.internal.SOURCE_FILE != null) {
			cmds.add(Option.internal.SOURCE_FILE);
		}
		doExecuteJarFile(jarFile,cmds);
		
		if (Option.internal.DEBUGGING)
			Util.println("------------  CLEANING UP TEMP FILES  ------------");
		deleteTempFiles(simBuilder.simulaTempDir);
		Util.IERR("NOT IMPL");
	}


	/// Delete temporary .class files.
	/// @param dir temporary .class directory
	private void deleteTempFiles(final File dir) {
		try {
			File[] elt = dir.listFiles();
			if (elt == null)
				return;
			for (File f : elt) {
				if (Option.internal.DEBUGGING) {
					if (f.isFile())
						Util.println("Delete: " + f);
				}
				if (f.isDirectory())
					deleteTempFiles(f);
				f.delete();
			}
		} catch (Exception e) {
			Util.IERR("SimulaCompiler.deleteFiles FAILED: ", e);
			e.printStackTrace();
		}
	}

	/// Execute JarFile.
	/// @param jarFile a jarFile
	/// @param arg the arguments
	/// @throws IOException if something went wrong.
	private void doExecuteJarFile(String jarFile,Vector<String> arg) throws IOException {
		if (!programModule.isExecutable()) {
			if (Option.verbose)
				Util.println("Separate Compilation - No Execution of .jar File: " + jarFile);
		} else if (Option.noExecution) {
			if (Option.verbose)
				Util.println("Option 'noexec' ==> No Execution of .jar File: " + jarFile);
		} else {
			if (Option.verbose)
				Util.println("------------  EXECUTION SUMMARY  ------------");
			if (Option.internal.TRACING)
				Util.println("Execute .jar File");
			int exitValue3 = Util.execute(arg);
			if (Option.verbose)
				Util.println("END Execute .jar File. Exit value=" + exitValue3);
			if(exitValue3 != 0) {
				IO.println("SimulaCompiler.doCompile: Exit value = " + exitValue3);
				throw new RuntimeException("Execution of "+jarFile+" failed. ExitValue = "+exitValue3);
			}
		}
	}

	/// Call Java compiler 'javac'
	/// @throws IOException if something went wrong.
	private void doCallJavaCompiler(SimulaBuilder simBuilder) throws IOException {
		String classPath = simBuilder.simulaRtsLib.toString();
		File rtsLib = new File(simBuilder.simulaRtsLib, "simula/runtime");
		boolean rtsExist = rtsLib.exists();
		boolean rtsCread = rtsLib.canRead();
		if (!(rtsExist && rtsCread)) {
			Util.IERR("DETTE MÅ RETTES");
//			Util.popUpError("Unable to access the Runtime System at:" + "\n" + rtsLib
//					+ "\nCheck the installation and consider" + "\nto Download it again.\n");
		}
		if (Option.internal.DEBUGGING) {
			Util.println(
					"Simula Runtime System:    \"" + rtsLib + "\", exists=" + rtsExist + ", canRead=" + rtsCread);
			String[] list = rtsLib.list();
			if (list != null) {
				Util.println("Simula Runtime System:    \"" + rtsLib + "\", exists=" + rtsExist + ", canRead="
						+ rtsCread + ", size=" + list.length);
				for (int i = 0; i < list.length; i++) {
					Util.println("       " + i + ": \"" + list[i] + "\"");
				}
			}
		}
		String pathSeparator = System.getProperty("path.separator");
		for (File jarFile : simBuilder.externalJarFiles) {
			if (Option.internal.DEBUGGING) {
				boolean exist = jarFile.exists();
				boolean cread = jarFile.canRead();
				Util.println(
						"Precompiled Library:      \"" + jarFile + "\", exists=" + exist + ", canRead=" + cread);
				JarFileBuilder.listJarFile(jarFile);
			}
			classPath = classPath + pathSeparator + (jarFile.toString().trim());
		}
		int exitValue = -1;
		String msg = "Commandline";
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler != null) {
			exitValue = callJavaSystemCompiler(compiler, classPath);
			msg = "System";
			if (exitValue != 0) {
				Util.generalError("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
				msg = "Commandline"; // Try use CommandLine Compiler
				exitValue = callJavacCompiler(classPath);
			}
		} else
			exitValue = callJavacCompiler(classPath);
		if (Option.internal.DEBUGGING) {
			Util.println("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
			for (JavaSourceFileCoder javaClass : simBuilder.javaSourceFileCoders)
				Util.println(javaClass.getClassOutputFileName());
			list(simBuilder.tempClassFileDir);
		}
		if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + simBuilder.documentManager.sourceName + ": Class Files Generated - From Java Source");
		if (exitValue != 0) {
			Util.generalError("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
			Util.println("\nCompiler terminated after error(s) during Java Compilation");
			return;
		}
	}


	/// Call Java system compiler
	/// @param compiler the Java compiler
	/// @param classPath the classPath
	/// @return return value from the Java compiler
	/// @throws IOException if something went wrong
	private int callJavaSystemCompiler(final SimulaBuilder simBuilder, final JavaCompiler compiler, final String classPath) throws IOException {
		Vector<String> arguments = new Vector<String>();
		if (Option.internal.DEBUGGING) {
			arguments.add("-version");
		}
		if (Option.internal.TRACING)
			Util.println("SimulaCompiler.callJavaSystemCompiler: classPath=\"" + classPath + "\"");
		arguments.add("-classpath");
		arguments.add(classPath);
		arguments.add("-d");
		arguments.add(simBuilder.tempClassFileDir.toString()); // Specifies output directory.
		if (!Option.WARNINGS)
			arguments.add("-nowarn");
		for (JavaSourceFileCoder javaClass : simBuilder.javaSourceFileCoders)
			arguments.add(javaClass.javaOutputFile.toString()); // Add .java Files
		int nArg = arguments.size();
		String[] args = new String[nArg];
		arguments.toArray(args);

		if (Option.internal.DEBUGGING) {
			Util.println("------------  Call Java System Compiler  ------------");
			Util.println("System Compiler supports " + compiler.getSourceVersions());
			for (int i = 0; i < args.length; i++)
				Util.println("Compiler'args[" + i + "]=" + args[i]);
		}
		int exitValue = compiler.run(System.in, System.out, System.err, args);
		return (exitValue);			
	}

	/// Call Java command line compiler.
	/// @param classPath the classPath
	/// @return return value from the Java compiler
	private int callJavacCompiler(final SimulaBuilder simBuilder, final String classPath) {
		Vector<String> cmds = new Vector<String>();
		cmds.add("javac");
		if (Option.internal.DEBUGGING) {
			cmds.add("-version");
		}
		if (Option.internal.TRACING)
			Util.println("SimulaCompiler.callJavacCompiler: classPath=\"" + classPath + "\"");
		cmds.add("-classpath");
		cmds.add(classPath);
		cmds.add("-d");
		cmds.add(simBuilder.tempClassFileDir.toString()); // Specifies output directory.
		if (!Option.WARNINGS)
			cmds.add("-nowarn");
		for (JavaSourceFileCoder javaClass : simBuilder.javaSourceFileCoders) {
			cmds.add(javaClass.javaOutputFile.toString()); // Add .java Files
		}
		int exitValue = Util.execute(cmds);
		if (Option.internal.TRACING) {
			Util.println("END Generate .class Output Code. Exit value=" + exitValue);
			for (JavaSourceFileCoder javaClass : simBuilder.javaSourceFileCoders)
				Util.println(javaClass.getClassOutputFileName());
		}
		return (exitValue);
	}
	
	
	/// Possible doByteCodeEngineering reintroducing labels and goto.
	/// @throws IOException if something went wrong.
	private void doByteCodeEngineering(final SimulaBuilder simBuilder) throws IOException {
		if (Option.internal.keepJava == null) {
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				Util.println("------------  LIST ByteCode Before Engineering  ------------");
				for (JavaSourceFileCoder javaClass : simBuilder.javaSourceFileCoders) {
					String classFile = javaClass.getClassOutputFileName();
					Util.doListClassFile(classFile);
				}
			}
			for (JavaSourceFileCoder javaClass : simBuilder.javaSourceFileCoders) {
				if (javaClass.mustDoByteCodeEngineering) {
					String classFileName = javaClass.getClassOutputFileName();
					ClassFileTransform.doRepairSingleByteCode(classFileName,classFileName);
					if(Option.verbose) Util.println("SimulaCompiler.doByteCodeEngineering: " + simBuilder.documentManager.sourceName + ": Class File " + classFileName + " is repaired");
				}
			}
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				Util.println("------------  LIST ByteCode After Engineering  ------------");
				for (JavaSourceFileCoder javaClass : simBuilder.javaSourceFileCoders) {
					String classFile = javaClass.getClassOutputFileName();
					Util.doListClassFile(classFile);
				}
			}
		} else {
			Util.generalWarning("Option.internal.keepJava set: No ByteCode Engineering is performed");
		}
	}

	/// Debug utility: listGeneratedClassFiles.
	private void listGeneratedClassFiles(final SimulaBuilder simBuilder) {
		File classFiles = new File(simBuilder.tempClassFileDir, DocumentManager.packetName);
		for (File classFile : classFiles.listFiles()) {
			if(classFile.getName().endsWith(".class"))
				Util.doListClassFile("" + classFile); // List generated .class file
		}
	}

	/// File Summary
	private void fileSummary(final SimulaBuilder simBuilder) {
		Util.println("------------  FILE SUMMARY  ------------");
		Util.println("Package Name:    \"" + DocumentManager.packetName + "\"");
		Util.println("SourceFile Name: \"" + simBuilder.sourceName + "\"");
		Util.println("SourceFile Dir:  \"" + simBuilder.sourceFileDir + "\"");
		if (simBuilder.currentWorkspace != null)
			Util.println("CurrentWorkspace \"" + simBuilder.currentWorkspace + "\"");
		Util.println("TempDir .java:   \"" + simBuilder.tempJavaFileDir + "\"");
		Util.println("TempDir .class:  \"" + simBuilder.tempClassFileDir + "\"");
		Util.println("SimulaRtsLib:    \"" + simBuilder.simulaRtsLib + "\"");
		Util.println("OutputDir:       \"" + simBuilder.outputDir + "\"");
	}

	// ***************************************************************
	// *** PRINT SUMMARY
	// ***************************************************************
	/// Print summary at program end.
	private void printSummary() {
		Util.println("------------  COMPILATION SUMMARY  ------------");
		Util.println("Compiler Mode:   \"" + Option.compilerMode + "\"");
		if (!programModule.isExecutable()) {
			Util.println("Separate Compiled " + ObjectKind.edit(programModule.mainModule.declarationKind)
			                   + " " + programModule  + " is written to: \"" + outputJarFile + "\"");
			Util.println("Rel Attr.File:   \"" + programModule.getRelativeAttributeFileName() + "\"");
		} else {
    		if(outputJarFile != null) {
    			Util.println("Resulting File:  \"" + outputJarFile.getAbsolutePath() + "\"");
    			Util.println("Main Entry:      \"" + CoreGlobal.jarFileBuilder.mainEntry + "\"");
    		} else {
    			Util.println("No executable jar-file is generated");    			
    		}
		}
	}


}
