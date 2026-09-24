package simula.core.coder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.eclipse.lsp4j.Diagnostic;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.CoreGlobal;
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

	public int nErrors;
	public List<Diagnostic> diagnostics;

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


	public void addError(Diagnostic diagnostic) {
		diagnostics.add(diagnostic);
		nErrors++;
	}

	public void addDiagnostic(Diagnostic diagnostic) {
		diagnostics.add(diagnostic);
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
			Util.IERR("SimulaBuilder.deleteFiles FAILED: ", e);
			e.printStackTrace();
		}
	}
	
	// ***************************************************************
	// *** Code Generation
	// ***************************************************************
	public void doCodeGeneration(ProgramModule  programModule) throws IOException {
//		Option.print("SimulaCompiler.doCodeGeneration: ");
		
//		Util.println("SimulaCoder.doCodeGeneration: externalJarFiles: " + DocumentManager.externalJarFileNames);
		for (String jarFileName : documentManager.externalJarFileNames) {
			if (Option.internal.DEBUGGING) {
				File jarFile = new File(jarFileName);
				boolean exist = jarFile.exists();
				boolean cread = jarFile.canRead();
				Util.println("Precompiled Library:      \"" + jarFile + "\", exists=" + exist + ", canRead=" + cread);
				JarFileBuilder.listJarFile("SimulaCoder.doCodeGeneration: ",jarFile);
			}
			JarFileBuilder.writeJarEntriesToTempClassFiles(this, jarFileName);
		}
		
//    	Util.doListDirectory("SimulaCoder.doCodeGeneration: ", ""+SimulaCoder.tempClassFileDir);
//    	Util.doListDirectory("SimulaCoder.doCodeGeneration: ", ""+SimulaCoder.tempClassFileDir + "/" + DocumentManager.packetName);
		
		if ((! documentManager.compileViaJavaSource)) {
			if (Option.internal.TRACING) Util.println("BEGIN Generate .class Output Code");
			// *** Generate .class files
			programModule.createJavaClassFile(this);
			if(DocumentManager.verbose) Util.println(documentManager.sourceName + ": Class Files Generated - Directly");
		} else {
			if (Option.internal.TRACING) Util.println("BEGIN Generate .java Output Code");
			// *** Generate .java intermediate code
			programModule.doJavaCoding(this);
			if(DocumentManager.verbose) Util.println("SimulaCompiler.doCompile: " + documentManager.sourceName + ": Java Source Files Generated");
			if (Option.internal.TRACING) {
				Util.println("END Generate .java Output Code");
				for (JavaSourceFileCoder javaClass : this.javaSourceFileCoders)
					Util.println(javaClass.javaOutputFile.toString());
			}
		}
		if (nErrors > 0) {
			String msg="Compiler terminate " + documentManager.sourceName + " after " + nErrors + " errors during code generation";
			Util.println(msg);
			throw new RuntimeException(msg);
		}

		if (DocumentManager.verbose) fileSummary();
		if (Option.internal.DEBUGGING) {
			Util.println("------------  CLASSPATH DETAILS  ------------");
			Util.println("Java PathSeparator " + System.getProperty("path.separator"));
			Util.println("Java ClassPath     " + System.getProperty("java.class.path"));
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
		Util.println("------------  CODER FILE SUMMARY  ------------");
		Util.println("Package Name:    \"" + DocumentManager.packetName + "\"");
		Util.println("SourceFile Name: \"" + documentManager.sourceName + "\"");
		Util.println("SourceFile Dir:  \"" + documentManager.sourceFileDir + "\"");
		Util.println("TempDir .java:   \"" + tempJavaFileDir + "\"");
		Util.println("TempDir .class:  \"" + tempClassFileDir + "\"");
		Util.println("SimulaRtsLib:    \"" + DocumentManager.simulaRtsLib + "\"");
		Util.println("OutputDir:       \"" + documentManager.jarFileDir + "\"");
	}

	// ***************************************************************
	// *** PRINT SUMMARY
	// ***************************************************************
	/// Print summary at program end.
	private void printSummary(final SimulaBuilder simBuilder) {
		ProgramModule programModule = simBuilder.syntaxTree;
		File outputJarFile = jarFileBuilder.outputJarFile;
		Util.println("------------  COMPILATION SUMMARY  ------------");
		Util.println("compileViaJavaSource:   \"" + documentManager.compileViaJavaSource + "\"");
		if (!programModule.isExecutable()) {
			Util.println("Separate Compiled " + ObjectKind.edit(programModule.mainModule.declarationKind)
			                   + " " + programModule  + " is written to: \"" + outputJarFile + "\"");
			Util.println("Rel Attr.File:   \"" + programModule.getRelativeAttributeFileName() + "\"");
		} else {
    		if(outputJarFile != null) {
    			Util.println("Resulting File:  \"" + outputJarFile.getAbsolutePath() + "\"");
    			Util.println("Main Entry:      \"" + jarFileBuilder.mainEntry + "\"");
    		} else {
    			Util.println("No executable jar-file is generated");    			
    		}
		}
	}

}
