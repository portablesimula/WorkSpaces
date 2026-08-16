package simula.core.coder;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.CoreGlobal;
import simula.core.CoreGlobal2;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.SimulaBuilder;
import simula.core.syntaxClass.statement.ProgramModule;
import simula.core.utilities.LOG;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

public class SimulaCoder {

	final public DocumentManager documentManager;

	// ***************************************************************
	// *** Static variables used during Code Generation
	// ***************************************************************

	/// The .jar File Builder
	public static JarFileBuilder jarFileBuilder;

	public File generatedJarFile;

	// Specifies where to place generated executable .jar file;
	public static File outputDir = null;
	
	// Specifies where to search for precompiled classes and procedures
	// If not found, output directory is also searched
	public static File extLib = null;

	/// Compiler state: True while generating STM code
	public static boolean duringSTM_Coding;

	/// The Simula temp directory
	public File simulaTempDir;
	
	/// Temp directory for generated .java files
	public static File tempJavaFileDir;
	
	/// Temp directory for generated .class files
	public File tempClassFileDir;
	
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

	public SimulaCoder(DocumentManager documentManager) {
		this.documentManager = documentManager;
		documentManager.simCoder = this;
    	// INIT:

		// Get an empty Temp Directory:
		simulaTempDir = CoreGlobal.getTempFileDir("simula/");
		deleteTempFiles(simulaTempDir);

		// Create temp .class-Files Directory:
		File tmpClassDir = new File(simulaTempDir, "classes/" + CoreGlobal2.packetName);
		tmpClassDir.mkdirs();
		tempClassFileDir = tmpClassDir.getParentFile();
		LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempClassFileDir="+tempClassFileDir);
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
						IO.println("Delete: " + f);
				}
				if (f.isDirectory())
					deleteTempFiles(f);
				f.delete();
			}
		} catch (Exception e) {
			Util.IERR("SimulaBuilder.deleteFiles FAILED: ", e);
			e.printStackTrace();
		}
	}
	
	// ***************************************************************
	// *** Code Generation
	// ***************************************************************
	public void doCodeGeneration(ProgramModule  programModule) throws IOException {
//		Option.print("SimulaCompiler.doCodeGeneration: ");
//		ProgramModule  programModule = simBuilder.documentManager.getSyntaxTree();
		switch(CoreGlobal2.compilerMode) {
			case directClassFiles:
				break;
			case viaJavaSource:
				SimulaCoder.javaSourceFileCoders = new Vector<JavaSourceFileCoder>();
				// Create Temp .java-Files Directory:
				File javatmp = Option.internal.keepJava;
				if (javatmp == null)
					javatmp = simulaTempDir;
				File tmpJavaDir = new File(javatmp, "src/" + CoreGlobal2.packetName);
				tmpJavaDir.mkdirs();
				SimulaCoder.tempJavaFileDir = tmpJavaDir;
		    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempJavaFileDir="+SimulaCoder.tempJavaFileDir);
				break;
			default:
				break;
		}
		
//		IO.println("SimulaCoder.doCodeGeneration: externalJarFiles: " + DocumentManager.externalJarFileNames);
		for (String jarFileName : DocumentManager.externalJarFileNames) {
			if (Option.internal.DEBUGGING) {
				File jarFile = new File(jarFileName);
				boolean exist = jarFile.exists();
				boolean cread = jarFile.canRead();
				IO.println("Precompiled Library:      \"" + jarFile + "\", exists=" + exist + ", canRead=" + cread);
				JarFileBuilder.listJarFile("SimulaCoder.doCodeGeneration: ",jarFile);
			}
			JarFileBuilder.writeJarEntriesToTempClassFiles(this, jarFileName);
		}
		
//    	Util.doListDirectory("SimulaCoder.doCodeGeneration: ", ""+SimulaCoder.tempClassFileDir);
//    	Util.doListDirectory("SimulaCoder.doCodeGeneration: ", ""+SimulaCoder.tempClassFileDir + "/" + CoreGlobal2.packetName);
//		Util.IERR(""+classPath);


    	// Create output .jar-files Directory
//		IO.println("SimulaCompiler.setOutputDir: sourceFileDir=" + documentManager.sourceFileDir);
//		IO.println("SimulaCompiler.setOutputDir: outputDir=" + SimulaCoder.outputDir);
    	if(SimulaCoder.outputDir == null) {
    		File userDir = new File(System.getProperty("user.dir"));
    		SimulaCoder.outputDir = new File(userDir,"bin");
    	}
    	LOG.info("SimulaCoder.doCodeGeneration: outputDir=" + SimulaCoder.outputDir);
    	SimulaCoder.outputDir.mkdirs();
    	if (! SimulaCoder.outputDir.canWrite()) {
    		Util.IERR("SimulaCompiler.setOutputDir: Unable to write to " + SimulaCoder.outputDir);
    	}


    	
    	
		
		if (CoreGlobal2.compilerMode != CoreGlobal2.CompilerMode.viaJavaSource) {
			if (Option.internal.TRACING)
				IO.println("BEGIN Generate .class Output Code");
			// *** Generate .class files
			programModule.createJavaClassFile(this);
			if(CoreGlobal2.verbose) IO.println(DocumentManager.sourceName + ": Class Files Generated - Directly");
		} else {
			if (Option.internal.TRACING)
				IO.println("BEGIN Generate .java Output Code");
			// *** Generate .java intermediate code
			programModule.doJavaCoding();
			if(CoreGlobal2.verbose) IO.println("SimulaCompiler.doCompile: " + DocumentManager.sourceName + ": Java Source Files Generated");
			if (Option.internal.TRACING) {
				IO.println("END Generate .java Output Code");
				for (JavaSourceFileCoder javaClass : SimulaCoder.javaSourceFileCoders)
					IO.println(javaClass.javaOutputFile.toString());
			}
		}
		if (Util.nError > 0) {
			String msg="Compiler terminate " + DocumentManager.sourceName + " after " + Util.nError + " errors during code generation";
			IO.println(msg);
			throw new RuntimeException(msg);
		}

//		if (CoreGlobal2.verbose)
//			fileSummary(simBuilder);
		if (Option.internal.DEBUGGING) {
			IO.println("------------  CLASSPATH DETAILS  ------------");
			IO.println("Java PathSeparator " + System.getProperty("path.separator"));
			IO.println("Java ClassPath     " + System.getProperty("java.class.path"));
		}

		if(CoreGlobal2.compilerMode == CoreGlobal2.CompilerMode.viaJavaSource) {
			// ***************************************************************
			// *** CALL JAVA COMPILER
			// *** POSSIBLE -- DO BYTE_CODE_ENGINEERING
			// *** POSSIBLE - LIST GENERATED .class FILES
			// ***************************************************************
			JavaCoding.doCallJavaCompiler(this);
			ByteCodeEngineering.doByteCodeEngineering(this);
			if(Option.internal.LIST_GENERATED_CLASS_FILES)
				listGeneratedClassFiles();
		}
		
//		// ***************************************************************
//		// *** CRERATE AND WRITE ATTRIBUTE .jar FILE INLINE
//		// ***************************************************************
		this.generatedJarFile = JarFileBuilder.writeAttributeFile(this, programModule);
		
//		if (CoreGlobal2.verbose) printSummary(simBuilder);
//		deleteTempFiles(SimulaCoder.tempClassFileDir);
	}


	/// Debug utility: listGeneratedClassFiles.
	private void listGeneratedClassFiles() {
		File classFiles = new File(tempClassFileDir, CoreGlobal2.packetName);
		for (File classFile : classFiles.listFiles()) {
			if(classFile.getName().endsWith(".class"))
				Util.doListClassFile("" + classFile); // List generated .class file
		}
	}

	/// File Summary
	private void fileSummary(final SimulaBuilder simBuilder) {
		IO.println("------------  FILE SUMMARY  ------------");
		IO.println("Package Name:    \"" + CoreGlobal2.packetName + "\"");
		IO.println("SourceFile Name: \"" + DocumentManager.sourceName + "\"");
		IO.println("SourceFile Dir:  \"" + simBuilder.documentManager.sourceFileDir + "\"");
		IO.println("TempDir .java:   \"" + SimulaCoder.tempJavaFileDir + "\"");
		IO.println("TempDir .class:  \"" + tempClassFileDir + "\"");
		IO.println("SimulaRtsLib:    \"" + CoreGlobal2.simulaRtsLib + "\"");
		IO.println("OutputDir:       \"" + SimulaCoder.outputDir + "\"");
	}

	// ***************************************************************
	// *** PRINT SUMMARY
	// ***************************************************************
	/// Print summary at program end.
	private static void printSummary(final SimulaBuilder simBuilder) {
		ProgramModule programModule = simBuilder.syntaxTree;
		JarFileBuilder jarFileBuilder = SimulaCoder.jarFileBuilder;
		File outputJarFile = jarFileBuilder.outputJarFile;
		IO.println("------------  COMPILATION SUMMARY  ------------");
		IO.println("Compiler Mode:   \"" + CoreGlobal2.compilerMode + "\"");
		if (!programModule.isExecutable()) {
			IO.println("Separate Compiled " + ObjectKind.edit(programModule.mainModule.declarationKind)
			                   + " " + programModule  + " is written to: \"" + outputJarFile + "\"");
			IO.println("Rel Attr.File:   \"" + programModule.getRelativeAttributeFileName() + "\"");
		} else {
    		if(outputJarFile != null) {
    			IO.println("Resulting File:  \"" + outputJarFile.getAbsolutePath() + "\"");
    			IO.println("Main Entry:      \"" + SimulaCoder.jarFileBuilder.mainEntry + "\"");
    		} else {
    			IO.println("No executable jar-file is generated");    			
    		}
		}
	}

}
