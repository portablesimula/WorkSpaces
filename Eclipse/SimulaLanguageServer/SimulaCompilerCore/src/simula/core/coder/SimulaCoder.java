package simula.core.coder;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
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
	public JarFileBuilder jarFileBuilder;

	public File generatedJarFile;

	/// Compiler state: True while generating STM code
	public boolean duringSTM_Coding;

	/// The Simula temp directory
	public File simulaTempDir;
	
	/// Temp directory for generated .java files
	public File tempJavaFileDir;
	
	/// Temp directory for generated .class files
	public File tempClassFileDir;
	
	/// The set of Java SourceFile Coders.
	public Vector<JavaSourceFileCoder> javaSourceFileCoders;

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
		File tmpClassDir = new File(simulaTempDir, "classes/" + DocumentManager.packetName);
		tmpClassDir.mkdirs();
		tempClassFileDir = tmpClassDir.getParentFile();
		LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempClassFileDir="+tempClassFileDir);

		if(documentManager.compileViaJavaSource) {
			this.javaSourceFileCoders = new Vector<JavaSourceFileCoder>();
			// Create Temp .java-Files Directory:
			File javatmp = Option.internal.keepJava;
			if (javatmp == null)
				javatmp = simulaTempDir;
			File tmpJavaDir = new File(javatmp, "src/" + DocumentManager.packetName);
			tmpJavaDir.mkdirs();
			this.tempJavaFileDir = tmpJavaDir;
	    	LOG.info("SimulaCompiler.doCodeGeneration: BEGIN: tempJavaFileDir="+this.tempJavaFileDir);
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
		
//		IO.println("SimulaCoder.doCodeGeneration: externalJarFiles: " + DocumentManager.externalJarFileNames);
		for (String jarFileName : documentManager.externalJarFileNames) {
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
//    	Util.doListDirectory("SimulaCoder.doCodeGeneration: ", ""+SimulaCoder.tempClassFileDir + "/" + DocumentManager.packetName);
		
		if ((! documentManager.compileViaJavaSource)) {
			if (Option.internal.TRACING) IO.println("BEGIN Generate .class Output Code");
			// *** Generate .class files
			programModule.createJavaClassFile(this);
			if(DocumentManager.verbose) IO.println(documentManager.sourceName + ": Class Files Generated - Directly");
		} else {
			if (Option.internal.TRACING) IO.println("BEGIN Generate .java Output Code");
			// *** Generate .java intermediate code
			programModule.doJavaCoding(this);
			if(DocumentManager.verbose) IO.println("SimulaCompiler.doCompile: " + documentManager.sourceName + ": Java Source Files Generated");
			if (Option.internal.TRACING) {
				IO.println("END Generate .java Output Code");
				for (JavaSourceFileCoder javaClass : this.javaSourceFileCoders)
					IO.println(javaClass.javaOutputFile.toString());
			}
		}
		if (Util.nError > 0) {
			String msg="Compiler terminate " + documentManager.sourceName + " after " + Util.nError + " errors during code generation";
			IO.println(msg);
			throw new RuntimeException(msg);
		}

		if (DocumentManager.verbose) fileSummary();
		if (Option.internal.DEBUGGING) {
			IO.println("------------  CLASSPATH DETAILS  ------------");
			IO.println("Java PathSeparator " + System.getProperty("path.separator"));
			IO.println("Java ClassPath     " + System.getProperty("java.class.path"));
		}

		if(documentManager.compileViaJavaSource) {
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
		
//		if (DocumentManager.verbose) printSummary(simBuilder);
//		deleteTempFiles(SimulaCoder.tempClassFileDir);
	}


	/// Debug utility: listGeneratedClassFiles.
	private void listGeneratedClassFiles() {
		File classFiles = new File(tempClassFileDir, DocumentManager.packetName);
		for (File classFile : classFiles.listFiles()) {
			if(classFile.getName().endsWith(".class"))
				Util.doListClassFile("" + classFile); // List generated .class file
		}
	}

	/// File Summary
	private void fileSummary() {
		IO.println("------------  CODER FILE SUMMARY  ------------");
		IO.println("Package Name:    \"" + DocumentManager.packetName + "\"");
		IO.println("SourceFile Name: \"" + documentManager.sourceName + "\"");
		IO.println("SourceFile Dir:  \"" + documentManager.sourceFileDir + "\"");
		IO.println("TempDir .java:   \"" + tempJavaFileDir + "\"");
		IO.println("TempDir .class:  \"" + tempClassFileDir + "\"");
		IO.println("SimulaRtsLib:    \"" + DocumentManager.simulaRtsLib + "\"");
		IO.println("OutputDir:       \"" + documentManager.jarFileDir + "\"");
	}

	// ***************************************************************
	// *** PRINT SUMMARY
	// ***************************************************************
	/// Print summary at program end.
	private void printSummary(final SimulaBuilder simBuilder) {
		ProgramModule programModule = simBuilder.syntaxTree;
		File outputJarFile = jarFileBuilder.outputJarFile;
		IO.println("------------  COMPILATION SUMMARY  ------------");
		IO.println("compileViaJavaSource:   \"" + documentManager.compileViaJavaSource + "\"");
		if (!programModule.isExecutable()) {
			IO.println("Separate Compiled " + ObjectKind.edit(programModule.mainModule.declarationKind)
			                   + " " + programModule  + " is written to: \"" + outputJarFile + "\"");
			IO.println("Rel Attr.File:   \"" + programModule.getRelativeAttributeFileName() + "\"");
		} else {
    		if(outputJarFile != null) {
    			IO.println("Resulting File:  \"" + outputJarFile.getAbsolutePath() + "\"");
    			IO.println("Main Entry:      \"" + jarFileBuilder.mainEntry + "\"");
    		} else {
    			IO.println("No executable jar-file is generated");    			
    		}
		}
	}

}
