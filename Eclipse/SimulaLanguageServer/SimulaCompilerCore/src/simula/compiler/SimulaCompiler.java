package simula.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Vector;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import simula.Option;
import simula.builder.SimulaBuilder;
import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.transform.ClassFileTransform;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Util;
import simula.exception.EOTException;

public class SimulaCompiler {
	DocumentManager documentManager;

	/// The Compiler Modes.
	public enum CompilerMode { 
    	/** Generate Java source and use Java compiler to generate JavaClass files. */					viaJavaSource,
    	/** Generate JavaClass files directly. No Java source files are generated. */ 					directClassFiles,
    }

	// ***************************************************************
	// *** Static variables
	// ***************************************************************

	/// The Simula release identification.
	/// 
	/// NOTE: When updating release id, change version in SimulaExtractor and RuntimeSystem
	public static final String simulaReleaseID = "Simula-2.0";

	/// The Compiler mode.
	public static CompilerMode compilerMode;
//	
//	/// The source file name.
//	public static String sourceFileName;
//
//	/// The source file name without .sim
//	public static String sourceName;

	/// Packet name used in generated .java files.
	/// NOTE: Must be a single identifier.
	public static String packetName = "simprog";

	/// Where to find the Simula Runtime System.
	public static File simulaRtsLib;
	
	/// Source file is case sensitive.
	public static boolean CaseSensitive=false;
	
	/// Output messages about what the compiler is doing.
	public static boolean verbose = false; 
	
	/// Generate warning messages
	public static boolean WARNINGS=true;

	/// TRUE:Do not create popUps at runtime
	public static boolean noPopup = false; 
	
	/// true: Don't execute generated .jar file
	public static boolean noExecution = false;
	
	/// false: Disable all language extensions. In other words,
	/// follow the Simula Standard literally
	public static boolean EXTENSIONS=true;

	// ***************************************************************
	// *** Static variables used during Code Generation
	// ***************************************************************

	/// The .jar File Builder
	public static JarFileBuilder jarFileBuilder;

	// Specifies where to place generated executable .jar file;
	public static File outputDir = null;
	
	// Specifies where to search for precompiled classes and procedures
	// If not found, output directory is also searched
	public static File extLib = null;

	/// Compiler state: True while generating STM code
	public static boolean duringSTM_Coding;

	/// The Simula temp directory
	public static File simulaTempDir;
	
	/// Temp directory for generated .java files
	public static File tempJavaFileDir;
	
	/// Temp directory for generated .class files
	public static File tempClassFileDir;
//	
//	/// The set of external .jar files.
//	public static Vector<File> externalJarFiles;
	
	/// The set of Java SourceFile Coders.
	public static Vector<JavaSourceFileCoder> javaSourceFileCoders;

	// ***************************************************************
	// *** Static variables used during Code Generation
	// ***************************************************************

	public static boolean RTOption_VERBOSE;
	public static boolean RTOption_BLOCK_TRACING;
	public static boolean RTOption_GOTO_TRACING;
	public static boolean RTOption_QPS_TRACING;
	public static boolean RTOption_SML_TRACING;

	
	// ***************************************************************
	// *** Constructor: SimulaCompiler
	// ***************************************************************
	public SimulaCompiler(DocumentManager documentManager) {
		this.documentManager = documentManager;
//		DocumentManager.sourceFileName = documentManager.documentUri;
//		SimulaCompiler.sourceName = getSourceName(documentManager.documentUri);
//		externalJarFiles = new Vector<File>();
//		StandardClass.INITIATE();
	}
//    
//    private String getSourceName(String documentUri) {
//    	String sourceName = Util.getBaseName(documentUri);
//		if (!Util.isJavaIdentifier(sourceName)) {
//			String prevName = sourceName;
//			sourceName = Util.makeJavaIdentifier(sourceName);
//			Util.generalWarning("The source file name '" + prevName + "' is not a legal class identifier. Modified to: " + sourceName);
//		}
//    	return sourceName;
//    }

//    private void setTmpClassDir() {
//		// Create Temp .class-Files Directory:
//		File tmpClassDir = new File(SimulaCompiler.simulaTempDir, "classes");
//		tmpClassDir.mkdirs();
//		SimulaCompiler.tempClassFileDir = tmpClassDir;
//    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempClassFileDir="+SimulaCompiler.tempClassFileDir);
////    	Util.IERR("");
////    	Thread.dumpStack();
//    }
//    
//    private void setOutputDir() {
////    	IO.println("SimulaCompiler.setOutputDir: sourceFileDir=" + documentManager.sourceFileDir);
////    	IO.println("SimulaCompiler.setOutputDir: outputDir=" + SimulaCompiler.outputDir);
//    	if(SimulaCompiler.outputDir == null) {
////    		SimulaCompiler.outputDir = new File(documentManager.sourceFileDir,"bin");
//        	File userDir = new File(System.getProperty("user.dir"));
//    		SimulaCompiler.outputDir = new File(userDir,"bin");
//    	}
//    	LOG.info("SimulaCompiler.setOutputDir: outputDir=" + SimulaCompiler.outputDir);
//    	SimulaCompiler.outputDir.mkdirs();
//    	if (! SimulaCompiler.outputDir.canWrite()) {
//    		Util.IERR("SimulaCompiler.setOutputDir: Unable to write to " + SimulaCompiler.outputDir);
//    	}
//    }
    
//	// ***************************************************************
//	// *** Scanning and Parsing
//	// ***************************************************************
//	public boolean doParsing(SimulaBuilder simBuilder) {
//		boolean builderTerminateNormally = false;
//		simBuilder.duringParsing = true;
//    	LOG.info("SimulaCompiler.doParsing: BEGIN");
//    	
//    	setTmpClassDir(); // Neccessary because external decclaration reads .jar
//    	setOutputDir();   // Neccessary because external decclaration reads .jar
//    	
//        // Do the actual Building
//		simBuilder.getNextParserToken();
//		simBuilder.syntaxTree = new ProgramModule(simBuilder);
//        try {
//        	simBuilder.syntaxTree.doBuild();
//        	builderTerminateNormally = true;
//        } catch(EOTException e) {
//			System.err.println("SimulaBuilder: GOT EXCEPTION: " + e.getMessage());
////			e.printStackTrace();
//			simBuilder.lexer.flush();
//        }
//
//    	LOG.info("SimulaBuilder: syntaxTree, tokenList and diagnostics DONE");
////    	IO.println("SimulaBuilder: this.syntaxTree: "+simBuilder.syntaxTree); // Root of Syntax Tree
////    	IO.println("SimulaBuilder: this.diagnostics: "+simBuilder.diagnostics);
////    	IO.println("SimulaBuilder: this.tokenList: "+simBuilder.tokenList);
////		
////    	simBuilder.printAll(" AFTER NEW SimulaBuilder: ");
//		
//    	return builderTerminateNormally;
//	}
//
//	// ***************************************************************
//	// *** Semantic Checker
//	// ***************************************************************
//	public void doChecking(SimulaBuilder simBuilder) {
//		if (Option.internal.TRACING)
//			IO.println("BEGIN Semantic Checker");
//		simBuilder.duringParsing = false;
//		simBuilder.duringChecking = true;
//    	LOG.info("SimulaCompiler.doChecking: BEGIN");
//		StandardClass.ENVIRONMENT.doChecking();
//		ProgramModule programModule = simBuilder.syntaxTree;
//		programModule.doChecking();
//		
////		programModule.doChecking();
//		if (Option.internal.TRACING) {
//			IO.println("END Semantic Checker: \"" + programModule + "\"");
//			if (Option.internal.TRACE_CHECKER_OUTPUT && programModule != null)
//				programModule.print(0);
//		}
//		if(SimulaCompiler.verbose) IO.println("SimulaCompiler.doCompile: " + SimulaCompiler.sourceName + ": Semantic Checker completed");
//		simBuilder.duringChecking = false;
//		if(Option.internal.PRINT_SYNTAX_TREE > 0) {
//			IO.println("\nSimulaCompiler.doCompile: =========== Resulting Syntax Tree after Checking ================");
//			programModule.printTree(1);
//		}
//		
//		if (Util.nError > 0) {
//			String msg="Compiler terminate " + SimulaCompiler.sourceName + " after " + Util.nError + " errors during semantic checking";
//			IO.println(msg);
////			Thread.dumpStack();
//			throw new RuntimeException(msg);
//		}
//
//	}

	// ***************************************************************
	// *** Code Generation
	// ***************************************************************
	public void doCodeGeneration(SimulaBuilder simBuilder) throws IOException {
		ProgramModule  programModule = documentManager.getSyntaxTree();
		switch(SimulaCompiler.compilerMode) {
			case directClassFiles:
				break;
			case viaJavaSource:
				SimulaCompiler.javaSourceFileCoders = new Vector<JavaSourceFileCoder>();
				// Create Temp .java-Files Directory:
				File javatmp = Option.internal.keepJava;
				if (javatmp == null)
					javatmp = SimulaCompiler.simulaTempDir;
				File tmpJavaDir = new File(javatmp, "src/" + SimulaCompiler.packetName);
				tmpJavaDir.mkdirs();
				SimulaCompiler.tempJavaFileDir = tmpJavaDir;
		    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempJavaFileDir="+SimulaCompiler.tempJavaFileDir);
				break;
			default:
				break;
		}
		
//		// Create Temp .class-Files Directory:
//		File tmpClassDir = new File(SimulaCompiler.simulaTempDir, "classes");
//		tmpClassDir.mkdirs();
//		SimulaCompiler.tempClassFileDir = tmpClassDir;
//    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempClassFileDir="+SimulaCompiler.tempClassFileDir);

//		Option.print("SimulaCompiler.doCodeGeneration: ");
		if(SimulaCompiler.jarFileBuilder == null) {
			SimulaCompiler.jarFileBuilder = new JarFileBuilder();
		}
		try {
			SimulaCompiler.jarFileBuilder.open(programModule);
//			SimulaCompiler.jarFileBuilder.expandIncludeQueue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Util.IERR("jarFileBuilder.open: FAILED !");
		}
		if (SimulaCompiler.compilerMode != SimulaCompiler.CompilerMode.viaJavaSource) {
			if (Option.internal.TRACING)
				IO.println("BEGIN Generate .class Output Code");
			// *** Generate .class files
			programModule.createJavaClassFile();
			if(SimulaCompiler.verbose) IO.println(DocumentManager.sourceName + ": Class Files Generated - Directly");
		} else {
			if (Option.internal.TRACING)
				IO.println("BEGIN Generate .java Output Code");
			// *** Generate .java intermediate code
			programModule.doJavaCoding();
			if(SimulaCompiler.verbose) IO.println("SimulaCompiler.doCompile: " + DocumentManager.sourceName + ": Java Source Files Generated");
			if (Option.internal.TRACING) {
				IO.println("END Generate .java Output Code");
				for (JavaSourceFileCoder javaClass : SimulaCompiler.javaSourceFileCoders)
					IO.println(javaClass.javaOutputFile.toString());
			}
		}
		if (Util.nError > 0) {
			String msg="Compiler terminate " + DocumentManager.sourceName + " after " + Util.nError + " errors during code generation";
			IO.println(msg);
			throw new RuntimeException(msg);
		}

		if (SimulaCompiler.verbose)
			fileSummary(simBuilder);
		if (Option.internal.DEBUGGING) {
			IO.println("------------  CLASSPATH DETAILS  ------------");
			IO.println("Java PathSeparator " + System.getProperty("path.separator"));
			IO.println("Java ClassPath     " + System.getProperty("java.class.path"));
		}

		if(SimulaCompiler.compilerMode == SimulaCompiler.CompilerMode.viaJavaSource) {
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
		simBuilder.generatedJarFile = SimulaCompiler.jarFileBuilder.close();
		
		if (SimulaCompiler.verbose) printSummary(simBuilder);
//		deleteTempFiles(SimulaCompiler.tempClassFileDir);
	}

	// ***************************************************************
	// *** Run generated .jar File
	// ***************************************************************
	public void doRun(SimulaBuilder simBuilder) throws IOException {
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
		
		if(SimulaCompiler.noPopup) {
			cmds.add("-noPopup");			
		}
		if (Option.internal.SOURCE_FILE != null) {
			cmds.add(Option.internal.SOURCE_FILE);
		}
		doExecuteJarFile(jarFile,cmds);
		
		if (Option.internal.DEBUGGING)
			IO.println("------------  CLEANING UP TEMP FILES  ------------");
		deleteTempFiles(SimulaCompiler.simulaTempDir);
	}

    /// Add Runtime options to the argument vector.
    /// @param args the argument vector
	public static void addRTArguments(Vector<String> args) {
		if(RTOption_VERBOSE) args.add("-verbose");
		if(RTOption_BLOCK_TRACING) args.add("-blockTracing");
		if(RTOption_GOTO_TRACING) args.add("-gotoTracing");
		if(RTOption_QPS_TRACING) args.add("-qpsTracing");
		if(RTOption_SML_TRACING) args.add("-smlTracing");
	}


	/// Delete temporary .class files.
	/// @param dir temporary .class directory
//	private void deleteTempFiles(final File dir) {
//		IO.println("SimulaCompiler.deleteTempFiles:  Delete: " + dir);
//		Thread.dumpStack();
//		Option.internal.DEBUGGING = true;
//		try {
//			File[] elt = dir.listFiles();
//			if (elt == null)
//				return;
//			for (File f : elt) {
//				if (Option.internal.DEBUGGING) {
//					if (f.isFile())
//						IO.println("SimulaCompiler.deleteTempFiles:  Delete: " + f);
//				}
//				if (f.isDirectory())
//					deleteTempFiles(f);
//				f.delete();
//			}
//		} catch (Exception e) {
//			Util.IERR("SimulaCompiler.deleteFiles FAILED: ", e);
//			e.printStackTrace();
//		}
//	}

	public static void deleteTempFiles(final File dir) {
		if(SimulaCompiler.verbose) {
		IO.println("SimulaCompiler.deleteTempFiles:  Delete: " + dir);
//			Thread.dumpStack();
		}
        if (! dir.exists()) {
            Util.IERR("File does not exist: " + dir);
            return;
        }
		Path path = dir.toPath();
        try { Files.walk(path)
	             // Sorts in reverse order (subfolders and files first)
	             .sorted(Comparator.reverseOrder())
	             .forEach(p -> {
	                 try {
	             		if(SimulaCompiler.verbose) {
	             			IO.println("SimulaCompiler.deleteTempFiles: Delete: " + p);
	             		}
	                     Files.delete(p);
	                 } catch (IOException e) {
	                     Util.IERR("Could not delete: " + p + " - " + e.getMessage());
	                 }
	             });
		} catch (Exception e) {
			Util.IERR("SimulaCompiler.deleteFiles FAILED: ", e);
			e.printStackTrace();
		}
    }
	
	/// Execute JarFile.
	/// @param jarFile a jarFile
	/// @param arg the arguments
	/// @throws IOException if something went wrong.
	private void doExecuteJarFile(String jarFile, Vector<String> arg) throws IOException {
		ProgramModule programModule = documentManager.simBuilder.syntaxTree;
		if (!programModule.isExecutable()) {
			if (SimulaCompiler.verbose)
				IO.println("Separate Compilation - No Execution of .jar File: " + jarFile);
		} else if (SimulaCompiler.noExecution) {
			if (SimulaCompiler.verbose)
				IO.println("Option 'noexec' ==> No Execution of .jar File: " + jarFile);
		} else {
			if (SimulaCompiler.verbose) {
				IO.println("------------  EXECUTION SUMMARY  ------------");
				IO.println("Execute .jar File");
			}
			int exitValue3 = Util.execute(arg);
			if (SimulaCompiler.verbose)
				IO.println("END Execute .jar File. Exit value=" + exitValue3);
			if(exitValue3 != 0) {
				IO.println("SimulaCompiler.doCompile: Exit value = " + exitValue3);
		    	Util.doListDirectory("SimulaCompiler.doExecuteJarFile: ", ""+SimulaCompiler.tempClassFileDir);
		    	Util.doListDirectory("SimulaCompiler.doExecuteJarFile: ", ""+SimulaCompiler.tempClassFileDir + "/" + packetName);
		    	JarFileBuilder.listJarFile("SimulaCompiler.doExecuteJarFile: ",new File(jarFile));
				throw new RuntimeException("Execution of "+jarFile+" failed. ExitValue = "+exitValue3);
			}
		}
	}

	/// Call Java compiler 'javac'
	/// @throws IOException if something went wrong.
	private void doCallJavaCompiler(SimulaBuilder simBuilder) throws IOException {
		String classPath = SimulaCompiler.simulaRtsLib.toString();
		File rtsLib = new File(SimulaCompiler.simulaRtsLib, "simula/runtime");
		boolean rtsExist = rtsLib.exists();
		boolean rtsCread = rtsLib.canRead();
		if (!(rtsExist && rtsCread)) {
			Util.IERR("DETTE MÅ RETTES");
//			Util.popUpError("Unable to access the Runtime System at:" + "\n" + rtsLib
//					+ "\nCheck the installation and consider" + "\nto Download it again.\n");
		}
		if (Option.internal.DEBUGGING) {
			IO.println(
					"Simula Runtime System:    \"" + rtsLib + "\", exists=" + rtsExist + ", canRead=" + rtsCread);
			String[] list = rtsLib.list();
			if (list != null) {
				IO.println("Simula Runtime System:    \"" + rtsLib + "\", exists=" + rtsExist + ", canRead="
						+ rtsCread + ", size=" + list.length);
				for (int i = 0; i < list.length; i++) {
					IO.println("       " + i + ": \"" + list[i] + "\"");
				}
			}
		}
		
		IO.println("SimulaCompiler.doCallJavaCompiler: externalJarFiles: " + DocumentManager.externalJarFiles);
		String pathSeparator = System.getProperty("path.separator");
		for (File jarFile : DocumentManager.externalJarFiles) {
//			if (Option.internal.DEBUGGING) {
				boolean exist = jarFile.exists();
				boolean cread = jarFile.canRead();
				IO.println("Precompiled Library:      \"" + jarFile + "\", exists=" + exist + ", canRead=" + cread);
				JarFileBuilder.listJarFile("SimulaCompiler.doCallJavaCompiler: ",jarFile);
//			}
			classPath = classPath + pathSeparator + (jarFile.toString().trim());
//			Util.IERR(""+classPath);
		}
//		Util.IERR("");
		
		int exitValue = -1;
		String msg = "Commandline";
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler != null) {
			exitValue = callJavaSystemCompiler(simBuilder, compiler, classPath);
			msg = "System";
			if (exitValue != 0) {
				Util.generalError("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
				msg = "Commandline"; // Try use CommandLine Compiler
				exitValue = callJavacCompiler(simBuilder, classPath);
			}
		} else
			exitValue = callJavacCompiler(simBuilder, classPath);
		if (Option.internal.DEBUGGING) {
			IO.println("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
			for (JavaSourceFileCoder javaClass : SimulaCompiler.javaSourceFileCoders)
				IO.println(javaClass.getClassOutputFileName());
//			list(SimulaCompiler.tempClassFileDir);
		}
		if(SimulaCompiler.verbose) IO.println("SimulaCompiler.doCompile: " + DocumentManager.sourceName + ": Class Files Generated - From Java Source");
		if (exitValue != 0) {
			Util.generalError("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
			IO.println("\nCompiler terminated after error(s) during Java Compilation");
			return;
		}
	}


	/// Call Java system compiler
	/// @param compiler the Java compiler
	/// @param classPath the classPath
	/// @return return value from the Java compiler
	/// @throws IOException if something went wrong
	private int callJavaSystemCompiler(final SimulaBuilder simBuilder, final JavaCompiler compiler, final String classPath) throws IOException {
//    	IO.println("SimulaCompiler.callJavaSystemCompiler: sourceFileDir=" + documentManager.sourceFileDir);
//    	IO.println("SimulaCompiler.callJavaSystemCompiler: outputDir=" + SimulaCompiler.outputDir);
//    	IO.println("SimulaCompiler.callJavaSystemCompiler: tempClassFileDir=" + SimulaCompiler.tempClassFileDir);
//    	IO.println("SimulaCompiler.callJavaSystemCompiler: tempClassFileDir=" + SimulaCompiler.simulaRtsLib);
//    	IO.println("SimulaCompiler.callJavaSystemCompiler: userHome=" + System.getProperty("user.home"));
//    	IO.println("SimulaCompiler.callJavaSystemCompiler: userDir=" + System.getProperty("user.dir"));
//    	IO.println("SimulaCompiler.callJavaSystemCompiler: javaClassPath=" + System.getProperty("java.class.path"));
//    	
//    	Util.doListDirectory(""+SimulaCompiler.tempClassFileDir);
//    	Util.doListDirectory(""+SimulaCompiler.tempClassFileDir + "/" + packetName);
    	
		Vector<String> arguments = new Vector<String>();
		if (Option.internal.DEBUGGING) {
			arguments.add("-version");
		}
		LOG.info("SimulaCompiler.callJavaSystemCompiler: classPath=\"" + classPath + "\"");
		
		boolean TESTING = false;//true;
		String clazzPath = null;
		if(TESTING) {
			clazzPath = classPath;
		} else {
			clazzPath = SimulaCompiler.tempClassFileDir.toString();
		}
		

		String rtsLib = SimulaCompiler.simulaRtsLib.toString();
		clazzPath = clazzPath + ';' + rtsLib;
		arguments.add("-classpath");
		arguments.add(clazzPath);
		arguments.add("-d");
		arguments.add(SimulaCompiler.tempClassFileDir.toString()); // Specifies output directory.
		if (!SimulaCompiler.WARNINGS)
			arguments.add("-nowarn");
		for (JavaSourceFileCoder javaClass : SimulaCompiler.javaSourceFileCoders)
			arguments.add(javaClass.javaOutputFile.toString()); // Add .java Files
		int nArg = arguments.size();
		String[] args = new String[nArg];
		arguments.toArray(args);

//		if (SimulaCompiler.verbose) {
			IO.println("------------  Call Java System Compiler  ------------");
			IO.println("System Compiler supports " + compiler.getSourceVersions());
			for (int i = 0; i < args.length; i++)
				IO.println("Compiler'args[" + i + "]=" + args[i]);
//		}
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
			IO.println("SimulaCompiler.callJavacCompiler: classPath=\"" + classPath + "\"");
		String clazzPath = SimulaCompiler.tempClassFileDir.toString();
		String rtsLib = SimulaCompiler.simulaRtsLib.toString();
		clazzPath = clazzPath + ';' + rtsLib;
		cmds.add("-classpath");
		cmds.add(clazzPath);

		cmds.add("-d");
		cmds.add(SimulaCompiler.tempClassFileDir.toString()); // Specifies output directory.
		if (!SimulaCompiler.WARNINGS)
			cmds.add("-nowarn");
		for (JavaSourceFileCoder javaClass : SimulaCompiler.javaSourceFileCoders) {
			cmds.add(javaClass.javaOutputFile.toString()); // Add .java Files
		}
		int exitValue = Util.execute(cmds);
		if (Option.internal.TRACING) {
			IO.println("END Generate .class Output Code. Exit value=" + exitValue);
			for (JavaSourceFileCoder javaClass : SimulaCompiler.javaSourceFileCoders)
				IO.println(javaClass.getClassOutputFileName());
		}
		return (exitValue);
	}
	
	
	/// Possible doByteCodeEngineering reintroducing labels and goto.
	/// @throws IOException if something went wrong.
	private void doByteCodeEngineering(final SimulaBuilder simBuilder) throws IOException {
		if (Option.internal.keepJava == null) {
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				IO.println("------------  LIST ByteCode Before Engineering  ------------");
				for (JavaSourceFileCoder javaClass : SimulaCompiler.javaSourceFileCoders) {
					String classFile = javaClass.getClassOutputFileName();
					Util.doListClassFile(classFile);
				}
			}
			for (JavaSourceFileCoder javaClass : SimulaCompiler.javaSourceFileCoders) {
				if (javaClass.mustDoByteCodeEngineering) {
					String classFileName = javaClass.getClassOutputFileName();
					ClassFileTransform.doRepairSingleByteCode(classFileName,classFileName);
					if(SimulaCompiler.verbose) IO.println("SimulaCompiler.doByteCodeEngineering: " + DocumentManager.sourceName + ": Class File " + classFileName + " is repaired");
				}
			}
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				IO.println("------------  LIST ByteCode After Engineering  ------------");
				for (JavaSourceFileCoder javaClass : SimulaCompiler.javaSourceFileCoders) {
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
		File classFiles = new File(SimulaCompiler.tempClassFileDir, SimulaCompiler.packetName);
		for (File classFile : classFiles.listFiles()) {
			if(classFile.getName().endsWith(".class"))
				Util.doListClassFile("" + classFile); // List generated .class file
		}
	}

	/// File Summary
	private void fileSummary(final SimulaBuilder simBuilder) {
		IO.println("------------  FILE SUMMARY  ------------");
		IO.println("Package Name:    \"" + SimulaCompiler.packetName + "\"");
		IO.println("SourceFile Name: \"" + DocumentManager.sourceName + "\"");
		IO.println("SourceFile Dir:  \"" + simBuilder.documentManager.sourceFileDir + "\"");
		IO.println("TempDir .java:   \"" + SimulaCompiler.tempJavaFileDir + "\"");
		IO.println("TempDir .class:  \"" + SimulaCompiler.tempClassFileDir + "\"");
		IO.println("SimulaRtsLib:    \"" + SimulaCompiler.simulaRtsLib + "\"");
		IO.println("OutputDir:       \"" + SimulaCompiler.outputDir + "\"");
	}

	// ***************************************************************
	// *** PRINT SUMMARY
	// ***************************************************************
	/// Print summary at program end.
	private void printSummary(final SimulaBuilder simBuilder) {
		ProgramModule programModule = simBuilder.syntaxTree;
		JarFileBuilder jarFileBuilder = SimulaCompiler.jarFileBuilder;
		File outputJarFile = jarFileBuilder.outputJarFile;
		IO.println("------------  COMPILATION SUMMARY  ------------");
		IO.println("Compiler Mode:   \"" + SimulaCompiler.compilerMode + "\"");
		if (!programModule.isExecutable()) {
			IO.println("Separate Compiled " + ObjectKind.edit(programModule.mainModule.declarationKind)
			                   + " " + programModule  + " is written to: \"" + outputJarFile + "\"");
			IO.println("Rel Attr.File:   \"" + programModule.getRelativeAttributeFileName() + "\"");
		} else {
    		if(outputJarFile != null) {
    			IO.println("Resulting File:  \"" + outputJarFile.getAbsolutePath() + "\"");
    			IO.println("Main Entry:      \"" + SimulaCompiler.jarFileBuilder.mainEntry + "\"");
    		} else {
    			IO.println("No executable jar-file is generated");    			
    		}
		}
	}


}
