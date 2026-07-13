/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import simula.Option;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.transform.ClassFileTransform;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.Meaning;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.SimulaClassLoader;
import simula.compiler.utilities.Util;
import simula.lsp.compiler.DocumentManager;

/// The Simula Compiler.
/// 
/// The compiler consists of the following steps:
///
/// 	- Initiate global variables.
/// 	- Do Parsing: Read source file through the scanner building program syntax tree.
/// 	- Do Checking: Traverse the syntax tree performing semantic checking.
/// 	- Do Coding dependent on the CompilerMode:
/// 		-  CompilerMode = viaJavaSource:
/// 			- Do JavaCoding: Traverse the syntax tree generating .java code.
/// 			- Call Java Compiler to generate .class files.
/// 			- Do ByteCodeEngineering updating .class files.
/// 			- Create executable .jar of program.
/// 			- Execute .jar file.
/// 		-  CompilerMode = directClassFiles:
/// 			- Traverse the syntax tree generating ClassFile code.
/// 			- Create executable .jar of program.
/// 			- Execute .jar file.
/// 		-  CompilerMode = simulaClassLoader:
/// 			- Traverse the syntax tree generate and load ClassFile code.
/// 			- Run the loaded program
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/SimulaCompiler.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class SimulaCompiler {
	
	/// The Reader in case of SimulaEditor.
	private Reader reader;

	/// The ProgramModule.
	private ProgramModule programModule;
	
	/// The output .jar file
	private File outputJarFile;

//	/// Create a new SimulaCompiler.
//	/// @param sourceFileName the source file name
//	public SimulaCompiler(final String sourceFileName) {
//		this(sourceFileName, null);
//	}

	/// Create a new SimulaCompiler.
	/// @param inputFileName the source file name
	/// @param reader        Reader in case of SimulaEditor
	public SimulaCompiler(final String inputFileName, final Reader reader) {
		IO.println("NEW SimulaCompiler(final String sourceFileName, final Reader reader)");
		CoreGlobal.initiate();
		this.reader = reader;
		if(Option.verbose) Util.println("Input Source File: " + inputFileName);
		if (!inputFileName.toLowerCase().endsWith(".sim"))
			Util.generalWarning("Simula source file should, by convention be extended by .sim: " + inputFileName);

		File inputFile = new File(inputFileName);

		CoreGlobal.sourceFileName = inputFile.getName();
		CoreGlobal.sourceName = Util.getBaseName(inputFile.getName());
		CoreGlobal.sourceFileDir = inputFile.getParentFile();
		if(CoreGlobal.sourceFileDir == null) CoreGlobal.sourceFileDir = new File(System.getProperty("user.dir"));
		
		if (Option.internal.TRACING)
			Util.println("Compiling: \"" + inputFileName + "\"");

		if (CoreGlobal.outputDir == null) {
			CoreGlobal.trySetOutputDir(new File(CoreGlobal.sourceFileDir, "bin"));
		}

		// Get Temp Directory:
		CoreGlobal.simulaTempDir = CoreGlobal.getTempFileDir("simula/");
		deleteTempFiles(CoreGlobal.simulaTempDir);

		// Create Temp .java-Files Directory:
		File javatmp = Option.internal.keepJava;
		if (javatmp == null)
			javatmp = CoreGlobal.simulaTempDir;
		File tmpJavaDir = new File(javatmp, "src/" + CoreGlobal.packetName);
		tmpJavaDir.mkdirs();
		CoreGlobal.tempJavaFileDir = tmpJavaDir;

		// Create Temp .class-Files Directory:
		File tmpClassDir = new File(CoreGlobal.simulaTempDir, "classes");
		tmpClassDir.mkdirs();
		CoreGlobal.tempClassFileDir = tmpClassDir;

		File desktop = new File(System.getProperty("user.home"), "Desktop");
		if (Option.verbose) {
			// https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
			Util.println("------------  SIMULA ENVIRONMENT SUMMARY  ------------");
			Util.println("Simula Properties    " + CoreGlobal.simulaPropertiesFile);
			Util.println("Simula Home          " + CoreGlobal.simulaHome);
			Util.println("Simula Home (prev)   " + CoreGlobal.getSimulaProperty("simula.home", null));
			Util.println("Java Home            " + System.getProperty("java.home"));
			Util.println("User Home            " + System.getProperty("user.home"));
			Util.println("Working Directory    " + System.getProperty("user.dir"));
			String s = (desktop.exists()) ? "true " : "false";
			Util.println("Desktop Exists=" + s + " " + desktop.toString());
			Util.println("Java Class Path      " + System.getProperty("java.class.path"));
			Util.println("Java Class Version   " + System.getProperty("java.class.version"));
			Util.println("Java Version         " + System.getProperty("java.version"));
			Util.println("Java VM Spec Version " + System.getProperty("java.vm.specification.version"));
			Util.println("Java Vendor          " + System.getProperty("java.vendor"));
			Util.println("OS name              " + System.getProperty("os.name"));
			Util.println("OS architecture      " + System.getProperty("os.arch"));
			Util.println("OS version           " + System.getProperty("os.version"));
			Util.println("file.encoding        " + System.getProperty("file.encoding"));
			Util.println("defaultCharset       " + Charset.defaultCharset());
			Util.println("compilerMode         " + Option.compilerMode);

			// This will list the current system properties
			// System.getProperties().list(System.out);

		}
	}
	public SimulaCompiler(final String sourceFileName) {
		IO.println("NEW SimulaCompiler(final String sourceFileName)");
//		Global.initiate();
//		this.reader = reader;
		
		if(Option.verbose) Util.println("Input Source File: " + sourceFileName);
		if (!sourceFileName.toLowerCase().endsWith(".sim"))
			Util.generalWarning("Simula source file should, by convention be extended by .sim: " + sourceFileName);

		File inputFile = new File(sourceFileName);

		CoreGlobal.sourceFileName = inputFile.getName();
		CoreGlobal.sourceName = Util.getBaseName(inputFile.getName());
		CoreGlobal.sourceFileDir = inputFile.getParentFile();
		if(CoreGlobal.sourceFileDir == null) CoreGlobal.sourceFileDir = new File(System.getProperty("user.dir"));
		
		if (Option.internal.TRACING)
			Util.println("Compiling: \"" + sourceFileName + "\"");

		if (CoreGlobal.outputDir == null) {
			CoreGlobal.trySetOutputDir(new File(CoreGlobal.sourceFileDir, "bin"));
		}

		// Get Temp Directory:
		CoreGlobal.simulaTempDir = CoreGlobal.getTempFileDir("simula/");
		deleteTempFiles(CoreGlobal.simulaTempDir);

		// Create Temp .java-Files Directory:
		File javatmp = Option.internal.keepJava;
		if (javatmp == null)
			javatmp = CoreGlobal.simulaTempDir;
		File tmpJavaDir = new File(javatmp, "src/" + CoreGlobal.packetName);
		tmpJavaDir.mkdirs();
		CoreGlobal.tempJavaFileDir = tmpJavaDir;

		// Create Temp .class-Files Directory:
		File tmpClassDir = new File(CoreGlobal.simulaTempDir, "classes");
		tmpClassDir.mkdirs();
		CoreGlobal.tempClassFileDir = tmpClassDir;

		File desktop = new File(System.getProperty("user.home"), "Desktop");
		if (Option.verbose) {
			// https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
			Util.println("------------  SIMULA ENVIRONMENT SUMMARY  ------------");
			Util.println("Simula Properties    " + CoreGlobal.simulaPropertiesFile);
			Util.println("Simula Home          " + CoreGlobal.simulaHome);
			Util.println("Simula Home (prev)   " + CoreGlobal.getSimulaProperty("simula.home", null));
			Util.println("Java Home            " + System.getProperty("java.home"));
			Util.println("User Home            " + System.getProperty("user.home"));
			Util.println("Working Directory    " + System.getProperty("user.dir"));
			String s = (desktop.exists()) ? "true " : "false";
			Util.println("Desktop Exists=" + s + " " + desktop.toString());
			Util.println("Java Class Path      " + System.getProperty("java.class.path"));
			Util.println("Java Class Version   " + System.getProperty("java.class.version"));
			Util.println("Java Version         " + System.getProperty("java.version"));
			Util.println("Java VM Spec Version " + System.getProperty("java.vm.specification.version"));
			Util.println("Java Vendor          " + System.getProperty("java.vendor"));
			Util.println("OS name              " + System.getProperty("os.name"));
			Util.println("OS architecture      " + System.getProperty("os.arch"));
			Util.println("OS version           " + System.getProperty("os.version"));
			Util.println("file.encoding        " + System.getProperty("file.encoding"));
			Util.println("defaultCharset       " + Charset.defaultCharset());
			Util.println("compilerMode         " + Option.compilerMode);

			// This will list the current system properties
			// System.getProperties().list(System.out);

		}
	}

	/// List temp class file directory tree
	/// @param dir tempClassFileDir
	private void list(final File dir) {
		try {
			Util.println("------------ BEGIN LIST tempClassFileDir: " + dir + "  ------------");
			list("", dir);
			Util.println("------------ ENDOF LIST tempClassFileDir: " + dir + "  ------------");
		} catch (Exception e) {
			Util.IERR("SimulaCompiler.listFiles FAILED: ", e);
			e.printStackTrace();
		}
	}

	/// List a directory tree.
	/// @param indent the indentation
	/// @param dir the directory
	private void list(String indent, final File dir) {
		try {
			File[] elt = dir.listFiles();
			if (elt == null || elt.length == 0) {
				Util.println("Empty Directory: " + dir);
				return;
			}
			for (File f : elt) {
				Util.println(indent + "- " + f);
				if (f.isDirectory())
					list(indent + "   ", f);
			}
		} catch (Exception e) {
			Util.IERR("SimulaCompiler.listFiles FAILED: ", e);
			e.printStackTrace();
		}
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

	/// Do Compile
	/// @throws IOException when it fails
//	public void doCompile() throws IOException {
	public void doCompile() throws IOException {
		IO.println("SimulaTestBatch: SimulaCompiler.doCompile");
		throw new IOException("ZZZZZZ");
	}
	
	/// Do Compile
	/// @throws IOException when it fails
//	public void doCompile() throws IOException {
	public void doCompile(ProgramModule programModule) throws IOException {
		this.programModule = programModule;
		if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + CoreGlobal.sourceName + ": Start Simula Compiler");
		Util.nError = 0;
		if (!Util.isJavaIdentifier(CoreGlobal.sourceName)) {
			String sourceName = CoreGlobal.sourceName;
			CoreGlobal.sourceName = Util.makeJavaIdentifier(sourceName);
			Util.warning("The source file name '" + sourceName + "' is not a legal class identifier. Modified to: "
					+ CoreGlobal.sourceName);
		}
		
		if(CoreGlobal.jarFileBuilder == null) {
	   		if(Option.compilerMode != Option.CompilerMode.simulaClassLoader) {
				CoreGlobal.jarFileBuilder = new JarFileBuilder();
			}
		}
		
		// ***************************************************************
		// *** Scanning and Parsing
		// ***************************************************************
		CoreGlobal.javaSourceFileCoders = new Vector<JavaSourceFileCoder>();
//		Parse.initiateParser(reader);
		
//		Util.IERR("DETTE MÅ RETTES");
//		programModule = new ProgramModule(null);
//		Util.IERR("DETTE MÅ RETTES");
//		Global.programModule = programModule;
		
//		programModule = Global.currentModule.programModule;
		
//		IO.println("SimulaCompiler.doCompile: mainModule"+programModule.mainModule);
//		IO.println("SimulaCompiler.doCompile: mainModule"+programModule.mainModule.declaredIn);
//		IO.println("SimulaCompiler.doCompile: mainModule"+programModule.mainModule.declaredIn.declaredIn);
//		Option.internal.TRACE_FIND_MEANING = 1;
//		Meaning meaning = programModule.mainModule.findMeaning("sysin");
//		IO.println("SimulaCompiler.doCompile: meaning="+meaning);
//		Option.internal.TRACE_FIND_MEANING = 0;
//		Util.STOP();

		
		if (Option.internal.TRACING) {
			Util.println("END Parsing, resulting Program: \"" + programModule + "\"");
			if (Option.internal.TRACE_PARSE && programModule != null)
				programModule.print(0);
		}
		if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + CoreGlobal.sourceName + ": Parsing completed");
//		Parse.close();
		CoreGlobal.duringParsing = false;
		if(Option.internal.PRINT_SYNTAX_TREE > 1) {
			IO.println("\nSimulaCompiler.doCompile: =========== Resulting Syntax Tree after Parsing ================");
			programModule.printTree(1,this);
		}
		if (Util.nError > 0) {
			String msg="Compiler terminate " + CoreGlobal.sourceName + " after " + Util.nError + " errors during parsing";
			Util.println(msg);
			throw new RuntimeException(msg);
		}
		
		// ***************************************************************
		// *** Generate .java files or ClassFileBuilder -> jarFile
		// ***************************************************************
   		if(Option.compilerMode == Option.CompilerMode.simulaClassLoader) {
			if (!programModule.isExecutable()) {
				// Separate Compilation
				if(CoreGlobal.jarFileBuilder == null)
					CoreGlobal.jarFileBuilder = new JarFileBuilder();
				CoreGlobal.jarFileBuilder.open(programModule);
			} else {
				CoreGlobal.simulaClassLoader = new SimulaClassLoader();
				if(! Option.internal.INLINE_TESTING)
					JarFileBuilder.loadRuntimeSystem();
				JarFileBuilder.loadIncludeQueue();
			}
		} else {
			CoreGlobal.jarFileBuilder.open(programModule);
			CoreGlobal.jarFileBuilder.addIncludeQueue();
		}
		
		if (Option.internal.TRACING)
			Util.println("BEGIN Possible Generate AttributeFile");
		
		// ***************************************************************
		// *** Semantic Checker
		// ***************************************************************
		if (Option.internal.TRACING)
			Util.println("BEGIN Semantic Checker");
		CoreGlobal.duringChecking = true;
		programModule.doChecking();
		if (Option.internal.TRACING) {
			Util.println("END Semantic Checker: \"" + programModule + "\"");
			if (Option.internal.TRACE_CHECKER_OUTPUT && programModule != null)
				programModule.print(0);
		}
		if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + CoreGlobal.sourceName + ": Semantic Checker completed");
		CoreGlobal.duringChecking = false;
		if(Option.internal.PRINT_SYNTAX_TREE > 0) {
			IO.println("\nSimulaCompiler.doCompile: =========== Resulting Syntax Tree after Checking ================");
			programModule.printTree(1,this);
		}
		
		if (Util.nError > 0) {
			String msg="Compiler terminate " + CoreGlobal.sourceName + " after " + Util.nError + " errors during semantic checking";
			Util.println(msg);
//			Thread.dumpStack();
			throw new RuntimeException(msg);
		}
		
		if (Option.compilerMode != Option.CompilerMode.viaJavaSource) {
			if (Option.internal.TRACING)
				Util.println("BEGIN Generate .class Output Code");
			// *** Generate .class files
			programModule.createJavaClassFile();
			if(Option.verbose) Util.println(CoreGlobal.sourceName + ": Class Files Generated - Directly");
		} else {
			if (Option.internal.TRACING)
				Util.println("BEGIN Generate .java Output Code");
			// *** Generate .java intermediate code
			programModule.doJavaCoding();
			if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + CoreGlobal.sourceName + ": Java Source Files Generated");
			if (Option.internal.TRACING) {
				Util.println("END Generate .java Output Code");
				for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders)
					Util.println(javaClass.javaOutputFile.toString());
			}
		}
		if (Util.nError > 0) {
			String msg="Compiler terminate " + CoreGlobal.sourceName + " after " + Util.nError + " errors during code generation";
			Util.println(msg);
			throw new RuntimeException(msg);
		}

		if (Option.verbose)
			fileSummary();
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
			doCallJavaCompiler();
			doByteCodeEngineering();
			if(Option.internal.LIST_GENERATED_CLASS_FILES)
				listGeneratedClassFiles();
		}
		AttributeFileIO.writeAttributeFile(programModule);

		// ***************************************************************
		// *** CRERATE .jar FILE INLINE
		// ***************************************************************
		String jarFile = null;
   		if(Option.compilerMode == Option.CompilerMode.simulaClassLoader) {
			if(CoreGlobal.jarFileBuilder != null) {
				if(Option.compilerMode == Option.CompilerMode.viaJavaSource) {
					CoreGlobal.jarFileBuilder.addTempClassFiles();
				}
				outputJarFile = CoreGlobal.jarFileBuilder.close();
				jarFile = outputJarFile.toString(); 				
			}
		} else {
			if(Option.compilerMode == Option.CompilerMode.viaJavaSource) {
				CoreGlobal.jarFileBuilder.addTempClassFiles();
			}
			outputJarFile = CoreGlobal.jarFileBuilder.close();
			jarFile = outputJarFile.toString();
		}
		
		if (Option.verbose) printSummary();

		// ***************************************************************
		// *** EXECUTE .jar FILE
		// ***************************************************************
		Vector<String> cmds = new Vector<String>();
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
   		if(Option.compilerMode == Option.CompilerMode.simulaClassLoader) {
			if(CoreGlobal.simulaClassLoader != null) {
				String name = CoreGlobal.packetName + '.' + programModule.getIdentifier();
				CoreGlobal.simulaClassLoader.runClass(name, cmds);
			} else {
				if(CoreGlobal.jarFileBuilder != null) {
	    			doExecuteJarFile(jarFile,cmds);    					
				} else
				Util.IERR();
			}
		} else {
			doExecuteJarFile(jarFile,cmds);
		}
		
		if (Option.internal.DEBUGGING)
			Util.println("------------  CLEANING UP TEMP FILES  ------------");
		deleteTempFiles(CoreGlobal.simulaTempDir);
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
	private void doCallJavaCompiler() throws IOException {
		String classPath = CoreGlobal.simulaRtsLib.toString();
		File rtsLib = new File(CoreGlobal.simulaRtsLib, "simula/runtime");
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
		for (File jarFile : CoreGlobal.externalJarFiles) {
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
			for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders)
				Util.println(javaClass.getClassOutputFileName());
			list(CoreGlobal.tempClassFileDir);
		}
		if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + CoreGlobal.sourceName + ": Class Files Generated - From Java Source");
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
	private int callJavaSystemCompiler(final JavaCompiler compiler, final String classPath) throws IOException {
		Vector<String> arguments = new Vector<String>();
		if (Option.internal.DEBUGGING) {
			arguments.add("-version");
		}
		if (Option.internal.TRACING)
			Util.println("SimulaCompiler.callJavaSystemCompiler: classPath=\"" + classPath + "\"");
		arguments.add("-classpath");
		arguments.add(classPath);
		arguments.add("-d");
		arguments.add(CoreGlobal.tempClassFileDir.toString()); // Specifies output directory.
		if (!Option.WARNINGS)
			arguments.add("-nowarn");
		for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders)
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
	private int callJavacCompiler(final String classPath) {
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
		cmds.add(CoreGlobal.tempClassFileDir.toString()); // Specifies output directory.
		if (!Option.WARNINGS)
			cmds.add("-nowarn");
		for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders) {
			cmds.add(javaClass.javaOutputFile.toString()); // Add .java Files
		}
		int exitValue = Util.execute(cmds);
		if (Option.internal.TRACING) {
			Util.println("END Generate .class Output Code. Exit value=" + exitValue);
			for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders)
				Util.println(javaClass.getClassOutputFileName());
		}
		return (exitValue);
	}
	
	/// Possible doByteCodeEngineering reintroducing labels and goto.
	/// @throws IOException if something went wrong.
	private void doByteCodeEngineering() throws IOException {
		if (Option.internal.keepJava == null) {
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				Util.println("------------  LIST ByteCode Before Engineering  ------------");
				for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders) {
					String classFile = javaClass.getClassOutputFileName();
					Util.doListClassFile(classFile);
				}
			}
			for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders) {
				if (javaClass.mustDoByteCodeEngineering) {
					String classFileName = javaClass.getClassOutputFileName();
					ClassFileTransform.doRepairSingleByteCode(classFileName,classFileName);
					if(Option.verbose) Util.println("SimulaCompiler.doByteCodeEngineering: " + CoreGlobal.sourceName + ": Class File " + classFileName + " is repaired");
				}
			}
			if (Option.internal.TRACE_BYTECODE_OUTPUT) {
				Util.println("------------  LIST ByteCode After Engineering  ------------");
				for (JavaSourceFileCoder javaClass : CoreGlobal.javaSourceFileCoders) {
					String classFile = javaClass.getClassOutputFileName();
					Util.doListClassFile(classFile);
				}
			}
		} else {
			Util.warning("Option.internal.keepJava set: No ByteCode Engineering is performed");
		}
	}

	/// Debug utility: listGeneratedClassFiles.
	private void listGeneratedClassFiles() {
		File classFiles = new File(CoreGlobal.tempClassFileDir, CoreGlobal.packetName);
		for (File classFile : classFiles.listFiles()) {
			if(classFile.getName().endsWith(".class"))
				Util.doListClassFile("" + classFile); // List generated .class file
		}
	}

	/// File Summary
	private void fileSummary() {
		Util.println("------------  FILE SUMMARY  ------------");
		Util.println("Package Name:    \"" + CoreGlobal.packetName + "\"");
		Util.println("SourceFile Name: \"" + CoreGlobal.sourceName + "\"");
		Util.println("SourceFile Dir:  \"" + CoreGlobal.sourceFileDir + "\"");
		if (CoreGlobal.currentWorkspace != null)
			Util.println("CurrentWorkspace \"" + CoreGlobal.currentWorkspace + "\"");
		Util.println("TempDir .java:   \"" + CoreGlobal.tempJavaFileDir + "\"");
		Util.println("TempDir .class:  \"" + CoreGlobal.tempClassFileDir + "\"");
		Util.println("SimulaRtsLib:    \"" + CoreGlobal.simulaRtsLib + "\"");
		Util.println("OutputDir:       \"" + CoreGlobal.outputDir + "\"");
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
