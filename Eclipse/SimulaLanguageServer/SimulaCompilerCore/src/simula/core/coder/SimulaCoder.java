package simula.core.coder;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.CoreGlobal2;
import simula.core.builder.AttributeFileIO;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.SimulaBuilder;
import simula.core.syntaxClass.statement.ProgramModule;
import simula.core.utilities.LOG;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;

public class SimulaCoder {

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
	// *** Code Generation
	// ***************************************************************
	public static void doCodeGeneration(SimulaBuilder simBuilder) throws IOException {
		ProgramModule  programModule = simBuilder.documentManager.getSyntaxTree();
		switch(CoreGlobal2.compilerMode) {
			case directClassFiles:
				break;
			case viaJavaSource:
				SimulaCoder.javaSourceFileCoders = new Vector<JavaSourceFileCoder>();
				// Create Temp .java-Files Directory:
				File javatmp = Option.internal.keepJava;
				if (javatmp == null)
					javatmp = SimulaCoder.simulaTempDir;
				File tmpJavaDir = new File(javatmp, "src/" + CoreGlobal2.packetName);
				tmpJavaDir.mkdirs();
				SimulaCoder.tempJavaFileDir = tmpJavaDir;
		    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempJavaFileDir="+SimulaCoder.tempJavaFileDir);
				break;
			default:
				break;
		}
		
//		// Create Temp .class-Files Directory:
//		File tmpClassDir = new File(SimulaCoder.simulaTempDir, "classes");
//		tmpClassDir.mkdirs();
//		SimulaCoder.tempClassFileDir = tmpClassDir;
//    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempClassFileDir="+SimulaCoder.tempClassFileDir);

//		Option.print("SimulaCompiler.doCodeGeneration: ");
		if(SimulaCoder.jarFileBuilder == null) {
			SimulaCoder.jarFileBuilder = new JarFileBuilder();
		}
		try {
			SimulaCoder.jarFileBuilder.open(programModule);
//			SimulaCoder.jarFileBuilder.expandIncludeQueue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Util.IERR("jarFileBuilder.open: FAILED !");
		}
		if (CoreGlobal2.compilerMode != CoreGlobal2.CompilerMode.viaJavaSource) {
			if (Option.internal.TRACING)
				IO.println("BEGIN Generate .class Output Code");
			// *** Generate .class files
			programModule.createJavaClassFile();
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

		if (CoreGlobal2.verbose)
			fileSummary(simBuilder);
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
			JavaCoding.doCallJavaCompiler(simBuilder);
			ByteCodeEngineering.doByteCodeEngineering(simBuilder);
			if(Option.internal.LIST_GENERATED_CLASS_FILES)
				listGeneratedClassFiles(simBuilder);
		}
		AttributeFileIO.writeAttributeFile(programModule);

		// ***************************************************************
		// *** CRERATE .jar FILE INLINE
		// ***************************************************************
		simBuilder.generatedJarFile = SimulaCoder.jarFileBuilder.close();
		
		if (CoreGlobal2.verbose) printSummary(simBuilder);
//		deleteTempFiles(SimulaCoder.tempClassFileDir);
	}


	/// Debug utility: listGeneratedClassFiles.
	private static void listGeneratedClassFiles(final SimulaBuilder simBuilder) {
		File classFiles = new File(SimulaCoder.tempClassFileDir, CoreGlobal2.packetName);
		for (File classFile : classFiles.listFiles()) {
			if(classFile.getName().endsWith(".class"))
				Util.doListClassFile("" + classFile); // List generated .class file
		}
	}

	/// File Summary
	private static void fileSummary(final SimulaBuilder simBuilder) {
		IO.println("------------  FILE SUMMARY  ------------");
		IO.println("Package Name:    \"" + CoreGlobal2.packetName + "\"");
		IO.println("SourceFile Name: \"" + DocumentManager.sourceName + "\"");
		IO.println("SourceFile Dir:  \"" + simBuilder.documentManager.sourceFileDir + "\"");
		IO.println("TempDir .java:   \"" + SimulaCoder.tempJavaFileDir + "\"");
		IO.println("TempDir .class:  \"" + SimulaCoder.tempClassFileDir + "\"");
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
