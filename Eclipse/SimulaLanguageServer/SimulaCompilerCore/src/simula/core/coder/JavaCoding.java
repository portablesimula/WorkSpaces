package simula.core.coder;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.CoreGlobal2;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.SimulaBuilder;
import simula.core.utilities.LOG;
import simula.core.utilities.Util;

public class JavaCoding {

	/// Call Java compiler 'javac'
	/// @throws IOException if something went wrong.
	static void doCallJavaCompiler(SimulaCoder simCoder) throws IOException {
		String classPath = CoreGlobal2.simulaRtsLib.toString();
		File rtsLib = new File(CoreGlobal2.simulaRtsLib, "simula/runtime");
		boolean rtsExist = rtsLib.exists();
		boolean rtsCread = rtsLib.canRead();
		if (!(rtsExist && rtsCread)) {
			Util.generalError("Unable to access the Runtime System at:" + rtsLib
					+ "\nCheck the installation and consider to Download it again.");
		}
		if (Option.internal.DEBUGGING) {
			IO.println("Simula Runtime System:    \"" + rtsLib + "\", exists=" + rtsExist + ", canRead=" + rtsCread);
			String[] list = rtsLib.list();
			if (list != null) {
				IO.println("Simula Runtime System:    \"" + rtsLib + "\", exists=" + rtsExist + ", canRead=" + rtsCread + ", size=" + list.length);
				for (int i = 0; i < list.length; i++) {
					IO.println("       " + i + ": \"" + list[i] + "\"");
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
			IO.println("Java " + msg + " Compiler returns exit=" + exitValue + "\n");
			for (JavaSourceFileCoder javaClass : SimulaCoder.javaSourceFileCoders)
				IO.println(javaClass.getClassOutputFileName(simCoder));
//			list(SimulaCoder.tempClassFileDir);
		}
		if(CoreGlobal2.verbose) IO.println("JavaCoding.doCompile: " + DocumentManager.sourceName + ": Class Files Generated - From Java Source");
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
	private static int callJavaSystemCompiler(final SimulaCoder simCoder, final JavaCompiler compiler, final String classPath) throws IOException {
//    	IO.println("JavaCoding.callJavaSystemCompiler: sourceFileDir=" + documentManager.sourceFileDir);
//    	IO.println("JavaCoding.callJavaSystemCompiler: outputDir=" + SimulaCoder.outputDir);
//    	IO.println("JavaCoding.callJavaSystemCompiler: tempClassFileDir=" + SimulaCoder.tempClassFileDir);
//    	IO.println("JavaCoding.callJavaSystemCompiler: tempClassFileDir=" + JavaCoding.simulaRtsLib);
//    	IO.println("JavaCoding.callJavaSystemCompiler: userHome=" + System.getProperty("user.home"));
//    	IO.println("JavaCoding.callJavaSystemCompiler: userDir=" + System.getProperty("user.dir"));
//    	IO.println("JavaCoding.callJavaSystemCompiler: javaClassPath=" + System.getProperty("java.class.path"));
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
		

		String rtsLib = CoreGlobal2.simulaRtsLib.toString();
		clazzPath = clazzPath + ';' + rtsLib;
		arguments.add("-classpath");
		arguments.add(clazzPath);
		arguments.add("-d");
		arguments.add(simCoder.tempClassFileDir.toString()); // Specifies output directory.
		if (!CoreGlobal2.WARNINGS)
			arguments.add("-nowarn");
		for (JavaSourceFileCoder javaClass : SimulaCoder.javaSourceFileCoders)
			arguments.add(javaClass.javaOutputFile.toString()); // Add .java Files
		int nArg = arguments.size();
		String[] args = new String[nArg];
		arguments.toArray(args);

//		if (JavaCoding.verbose) {
//			IO.println("------------  Call Java System Compiler  ------------");
//			IO.println("System Compiler supports " + compiler.getSourceVersions());
//			for (int i = 0; i < args.length; i++)
//				IO.println("Compiler'args[" + i + "]=" + args[i]);
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
			IO.println("JavaCoding.callJavacCompiler: classPath=\"" + classPath + "\"");
		String clazzPath = simCoder.tempClassFileDir.toString();
		String rtsLib = CoreGlobal2.simulaRtsLib.toString();
		clazzPath = clazzPath + ';' + rtsLib;
		cmds.add("-classpath");
		cmds.add(clazzPath);

		cmds.add("-d");
		cmds.add(simCoder.tempClassFileDir.toString()); // Specifies output directory.
		if (!CoreGlobal2.WARNINGS)
			cmds.add("-nowarn");
		for (JavaSourceFileCoder javaClass : SimulaCoder.javaSourceFileCoders) {
			cmds.add(javaClass.javaOutputFile.toString()); // Add .java Files
		}
		int exitValue = Util.execute(cmds);
		if (Option.internal.TRACING) {
			IO.println("END Generate .class Output Code. Exit value=" + exitValue);
			for (JavaSourceFileCoder javaClass : SimulaCoder.javaSourceFileCoders)
				IO.println(javaClass.getClassOutputFileName(simCoder));
		}
		return (exitValue);
	}
	

}
