package test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

public class Util {

	// ***************************************************************
	// *** LIST .class file
	// ***************************************************************
	/// Print a .class file listing.
	/// @param classFileName the .class file name
	public static void doListClassFile(final String classFileName) {
		IO.println("\n\n******** BEGIN List ClassFile: "+classFileName + " *****************************************************");
		try {
			execute("javap", "-c", "-l", "-p", "-s", "-verbose", classFileName);
		} catch (Exception e) {
			IO.println("Impossible" + e);
		}
		IO.println("******** ENDOF List ClassFile: "+classFileName + " *****************************************************\n\n");
	}

	// ***************************************************************
	// *** LIST .jar file
	// ***************************************************************
	/// Debug utility: List .jar file
	/// @param file the .jar file
	public static void listJarFile(final File file) {
		IO.println("---------  LIST .jar File: " + file + "  ---------");
		if (!(file.exists() && file.canRead())) {
			IO.println("Can't read .jar file: " + file);
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
			IO.println("Caused by:" + e);
		} finally {
			if (jarFile != null)
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}


	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	/// Execute OS Command
	/// @param cmd command vector
	/// @return return value from the OS
	public static int execute(final Vector<String> cmd) {
		String[] cmds = new String[cmd.size()];
		cmd.copyInto(cmds);
		return (execute(cmds));
	}

	/// Execute an OS command
	/// @param cmdarray command array
	/// @return exit value
	public static int execute(final String... cmdarray) {
		if(true) {// (Option.verbose) {
			String line = "";
			for (int i = 0; i < cmdarray.length; i++)
				line = line + " " + cmdarray[i];
			IO.println("Execute: " + line);
		}
		ProcessBuilder processBuilder = new ProcessBuilder(cmdarray);
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();		
			InputStream output = process.getInputStream();  // Process' output
//			if (Global.console != null) {
//				while (process.isAlive()) {
//					while (output.available() > 0) {
//						Global.console.write("" + (char) output.read());
//					}
//				}
//			} else {
				while (process.isAlive()) {
					while (output.available() > 0) {
						System.out.append((char) output.read());
					}
				}
//			}
			return (process.exitValue());

		} catch(Exception e) {
			throw new RuntimeException("Process Execution failed: " + cmdarray[0], e);
		}
	}
  

}
