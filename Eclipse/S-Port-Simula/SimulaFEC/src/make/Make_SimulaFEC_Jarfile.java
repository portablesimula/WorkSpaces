package make;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Make_SimulaFEC_Jarfile {

	
	public static void main(String[] argv) {
		compile("CLASS_COMMON.sim");   // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_COMMON.jar
		compile("CLASS_ERRMSG.sim");   // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_ERRMSG.jar
		compile("CLASS_SCANNER.sim");  // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCANNER.jar
		compile("CLASS_SCANINP.sim");  // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCANINP.jar
		compile("CLASS_PARSER.sim");   // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_PARSER.jar
		compile("CLASS_PAS1INIT.sim"); // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_PAS1INIT.jar
		compile("CLASS_BUILDER1.sim"); // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_BUILDER1.jar
		compile("CLASS_BUILDER2.sim"); // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_BUILDER2.jar
		compile("CLASS_CHECKER1.sim"); // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_CHECKER1.jar
		compile("CLASS_CHECKER2.sim"); // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_CHECKER2.jar
		
		compile("CLASS_SCODER0.sim");  // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCODER0.jar
		compile("CLASS_SCODER1.sim");  // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCODER1.jar
		compile("CLASS_SCODER1E.sim"); // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCODER1E.jar	
		compile("CLASS_SCODER2.sim");  // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCODER2.jar
		compile("CLASS_SCODER3.sim");  // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCODER3.jar
		compile("CLASS_SCODER4.sim");  // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCODER4.jar
		
		compile("CLASS_PAS2INIT.sim"); // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_PAS2INIT.jar
		compile("CLASS_SCODMAIN.sim"); // Output -> C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/CLASS_SCODMAIN.jar

//		compile("GENMSG.sim");            // WILL GENERATE FILE MessageGenerator.sim
//		compile("MessageGenerator.sim");  // WILL GENERATE ERROR MESSAGE FILE FECERROR.txt
		
		compile("FEC.sim");  // WILL CREATE THE S-PORT COMPILER:  C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/FEC.jar
		copyFECtoSPORT_HOME();

		System.out.append("END MakeFEC");
	}
	
	private static void copyFECtoSPORT_HOME() {
		File source=new File("C:/GitHub/EclipseWorkSpaces/S-Port-Simula/SimulaFEC/src/fec/source/bin/FEC.jar");
		File target=new File("C:/SPORT/SimulaFEC.jar");
		target.mkdirs();
		IO.println("source="+source);
		IO.println("target="+target);
		try {
			Files.copy(source.toPath(), target.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void compile(String name) {
		String fileName = "C:\\GitHub\\EclipseWorkSpaces\\S-Port-Simula\\SimulaFEC\\src\\fec\\source\\"+name;
		
		Vector<String> cmds = new Vector<String>();
		cmds.add("java");
		cmds.add("-jar");
		cmds.add("C:\\Users\\omyhr\\Simula\\Simula-2.0\\simula.jar");
//		cmds.add("-verbose");
		cmds.add("-compilerMode"); cmds.add("directClassFiles");
//		cmds.add("-compilerMode"); cmds.add("simulaClassLoader");
//		cmds.add("-compilerMode"); cmds.add("viaJavaSource");
		cmds.add("-noexec");
		cmds.add("-nowarn");
		cmds.add("-select"); cmds.add("ZDTW");
//		cmds.add("-select"); cmds.add("ZDW");
		cmds.add(fileName);

		try {
			exec(cmds);
		} catch (IOException e) {
			IO.println("Make_SimulaFEC_Jarfile.compile - Exit: ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	public static int exec(final Vector<String> cmd) throws IOException {
		String[] cmds = new String[cmd.size()];
		cmd.copyInto(cmds);
		return (exec(cmds));
	}

	public static int exec(String... cmd) throws IOException {
		String line="";
		for(int i=0;i<cmd.length;i++) line=line+" "+cmd[i];
        IO.println("MakeCompiler.execute: command="+line);
		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();		
			InputStream output = process.getInputStream();  // Process' output
			while (process.isAlive()) {
				while (output.available() > 0)
					System.out.append((char) output.read());
			}
			return (process.exitValue());

		} catch(Exception e) {
			IO.println("ERROR: "+e);
			throw new RuntimeException("Process Execution failed: " + line, e);
		}
	}

	
	// ***************************************************************
	// *** LIST .jar file
	// ***************************************************************
	/**
	 * List .jar file
	 * @param file the .jar file
	 */
	public static void listJarFile(final File file) {
		IO.println("---------  LIST .jar File: " + file + "  ---------");
		if (!(file.exists() && file.canRead())) {
			IO.println("ERROR: Can't read .jar file: " + file);
			return;
		}
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);
			Manifest manifest = jarFile.getManifest();
			Attributes mainAttributes = manifest.getMainAttributes();
			Set<Object> keys = mainAttributes.keySet();
			for (Object key : keys) {
				String val = mainAttributes.getValue(key.toString());
				IO.println(key.toString() + "=\"" + val + "\"");
			}

			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String size = "" + entry.getSize();
				while (size.length() < 6)
					size = " " + size;
				FileTime fileTime = entry.getLastModifiedTime();
				String date = DateTimeFormatter.ofPattern("uuuu-MMM-dd HH:mm:ss", Locale.getDefault())
						.withZone(ZoneId.systemDefault()).format(fileTime.toInstant());
				IO.println("Jar-Entry: " + size + "  " + date + "  \"" + entry + "\"");
			}
		} catch (IOException e) {
			IO.println("IERR: Caused by: " + e);
		} finally {
			if (jarFile != null)
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}


}
