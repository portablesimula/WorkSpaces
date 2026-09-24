package simula.core.coder;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.utilities.LOG;
import simula.core.utilities.Util;

public class JavaCoding {

	/// Call Java compiler 'javac'
	/// @throws IOException if something went wrong.
	static void doCallJavaCompiler(SimulaCoder simCoder) throws IOException {
		String classPath = DocumentManager.simulaRtsLib.toString();
		File rtsLib = new File(DocumentManager.simulaRtsLib, "simula/runtime");
		boolean rtsExist = rtsLib.exists();
		boolean rtsCread = rtsLib.canRead();
		if (!(rtsExist && rtsCread)) {
			Util.generalError("Unable to access the Runtime System at:" + rtsLib
					+ "\nCheck the installation and consider to Download it again.");
		}
		if (Option.internal.DEBUGGING) {
			Util.println("Simula Runtime System:    \"" + rtsLib + "\", exists=" + rtsExist + ", canRead=" + rtsCread);
			String[] list = rtsLib.list();
			if (list != null) {
				Util.println("Simula Runtime System:    \"" + rtsLib + "\", exists=" + rtsExist + ", canRead=" + rtsCread + ", size=" + list.length);
				for (int i = 0; i < list.length; i++) {
					Util.println("       " + i + ": \"" + list[i] + "\"");
				}
			}
		}
		
		int exitValue = -1;
		String msg = "Commandline";
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler != null) {
			exitValue = callJavaSystemCompiler(simCoder, compiler, classPath);
			msg = "System";
			if (exitValue != 0) {
				Util.generalError("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
				msg = "Commandline"; // Try use CommandLine Compiler
				exitValue = callJavacCompiler(simCoder, classPath);
			}
		} else
			exitValue = callJavacCompiler(simCoder, classPath);
		if (Option.internal.DEBUGGING) {
			Util.println("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
			for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders)
				Util.println(javaClass.getClassOutputFileName(simCoder));
//			list(SimulaCoder.tempClassFileDir);
		}
		if(DocumentManager.verbose) Util.println("JavaCoding.doCompile: " + simCoder.documentManager.sourceName + ": Class Files Generated - From Java Source");
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
	private static int callJavaSystemCompiler(final SimulaCoder simCoder, final JavaCompiler compiler, final String classPath) throws IOException {
//    	Util.println("JavaCoding.callJavaSystemCompiler: sourceFileDir=" + documentManager.sourceFileDir);
//    	Util.println("JavaCoding.callJavaSystemCompiler: jarFileDir=" + SimulaCoder.jarFileDir);
//    	Util.println("JavaCoding.callJavaSystemCompiler: tempClassFileDir=" + SimulaCoder.tempClassFileDir);
//    	Util.println("JavaCoding.callJavaSystemCompiler: tempClassFileDir=" + JavaCoding.simulaRtsLib);
//    	Util.println("JavaCoding.callJavaSystemCompiler: userHome=" + System.getProperty("user.home"));
//    	Util.println("JavaCoding.callJavaSystemCompiler: userDir=" + System.getProperty("user.dir"));
//    	Util.println("JavaCoding.callJavaSystemCompiler: javaClassPath=" + System.getProperty("java.class.path"));
//    	
//    	Util.doListDirectory(""+SimulaCoder.tempClassFileDir);
//    	Util.doListDirectory(""+SimulaCoder.tempClassFileDir + "/" + packetName);
    	
		Vector<String> arguments = new Vector<String>();
		if (Option.internal.DEBUGGING) {
			arguments.add("-version");
		}
		LOG.info("JavaCoding.callJavaSystemCompiler: classPath=\"" + classPath + "\"");
		
		boolean TESTING = false;//true;
		String clazzPath = null;
		if(TESTING) {
			clazzPath = classPath;
		} else {
			clazzPath = simCoder.tempClassFileDir.toString();
		}
		

		String rtsLib = DocumentManager.simulaRtsLib.toString();
		clazzPath = clazzPath + ';' + rtsLib;
		arguments.add("-classpath");
		arguments.add(clazzPath);
		arguments.add("-d");
		arguments.add(simCoder.tempClassFileDir.toString()); // Specifies output directory.
		if (!DocumentManager.WARNINGS)
			arguments.add("-nowarn");
		for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders)
			arguments.add(javaClass.javaOutputFile.toString()); // Add .java Files
		int nArg = arguments.size();
		String[] args = new String[nArg];
		arguments.toArray(args);

//		if (JavaCoding.verbose) {
//			Util.println("------------  Call Java System Compiler  ------------");
//			Util.println("System Compiler supports " + compiler.getSourceVersions());
//			for (int i = 0; i < args.length; i++)
//				Util.println("Compiler'args[" + i + "]=" + args[i]);
//		}
		int exitValue = compiler.run(System.in, System.out, System.err, args);
		return (exitValue);			
	}

	/// Call Java command line compiler.
	/// @param classPath the classPath
	/// @return return value from the Java compiler
	private static  int callJavacCompiler(final SimulaCoder simCoder, final String classPath) {
		Vector<String> cmds = new Vector<String>();
		cmds.add("javac");
		if (Option.internal.DEBUGGING) {
			cmds.add("-version");
		}
		if (Option.internal.TRACING)
			Util.println("JavaCoding.callJavacCompiler: classPath=\"" + classPath + "\"");
		String clazzPath = simCoder.tempClassFileDir.toString();
		String rtsLib = DocumentManager.simulaRtsLib.toString();
		clazzPath = clazzPath + ';' + rtsLib;
		cmds.add("-classpath");
		cmds.add(clazzPath);

		cmds.add("-d");
		cmds.add(simCoder.tempClassFileDir.toString()); // Specifies output directory.
		if (!DocumentManager.WARNINGS)
			cmds.add("-nowarn");
		for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders) {
			cmds.add(javaClass.javaOutputFile.toString()); // Add .java Files
		}
		int exitValue = Util.execute(cmds);
		if (Option.internal.TRACING) {
			Util.println("END Generate .class Output Code. Exit value=" + exitValue);
			for (JavaSourceFileCoder javaClass : simCoder.javaSourceFileCoders)
				Util.println(javaClass.getClassOutputFileName(simCoder));
		}
		return (exitValue);
	}
	

}
