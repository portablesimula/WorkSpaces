package simula.compiler;

import java.io.IOException;
import java.util.Vector;

import simula.Option;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.SimulaClassLoader;
import simula.compiler.utilities.Util;
import simula.lsp.compiler.DocumentManager;

public class OLD_SimulaCompiler {
	DocumentManager documentManager;
	
	public OLD_SimulaCompiler(DocumentManager documentManager) {
		this.documentManager = documentManager;
	}
	
	/// Do Compile
	/// @throws IOException when it fails
//	public void doCompile() throws IOException {
	public void doCompile() {//throws IOException {
		if(Option.verbose) Util.println("SimulaCompiler.doCompile: " + CoreGlobal.sourceName + ": Start Simula Compiler");
		Util.nError = 0;
		String sourceName = documentManager.sourceName;
		if (!Util.isJavaIdentifier(sourceName)) {
			documentManager.sourceName = Util.makeJavaIdentifier(sourceName);
			Util.generalWarning("The source file name '" + sourceName + "' is not a legal class identifier. Modified to: " + documentManager.sourceName);
			sourceName = documentManager.sourceName;
		}
		Util.IERR("STOP HER INTILL VIDERE");	
		
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
		
		ProgramModule  programModule = documentManager.getSyntaxTree();
		
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
//   		if(Option.compilerMode == Option.CompilerMode.simulaClassLoader) {
//			if (!programModule.isExecutable()) {
//				// Separate Compilation
//				if(CoreGlobal.jarFileBuilder == null)
//					CoreGlobal.jarFileBuilder = new JarFileBuilder();
//				CoreGlobal.jarFileBuilder.open(programModule);
//			} else {
//				CoreGlobal.simulaClassLoader = new SimulaClassLoader();
//				if(! Option.internal.INLINE_TESTING)
//					JarFileBuilder.loadRuntimeSystem();
//				JarFileBuilder.loadIncludeQueue();
//			}
//		} else {
			CoreGlobal.jarFileBuilder.open(programModule);
			CoreGlobal.jarFileBuilder.addIncludeQueue();
//		}
		
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
		
		// ***************************************************************
		// *** Code Generation
		// ***************************************************************
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



}
