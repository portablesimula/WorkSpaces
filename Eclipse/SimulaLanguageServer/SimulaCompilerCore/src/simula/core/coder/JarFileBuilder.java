/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.coder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import simula.Option;
import simula.core.DocumentManager;
import simula.core.builder.AttributeFileIO;
import simula.core.builder.SimulaBuilder;
import simula.core.CoreGlobal2;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.statement.ProgramModule;
import simula.core.utilities.LOG;
import simula.core.utilities.Util;

/// Utilities to build and manipulate jarFiles.
///
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/JarFileBuilder.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public class JarFileBuilder {
	
	/// The ProgramModule.
	private ProgramModule programModule;
	
	/// The output .jar file
	public File outputJarFile;
	
	/// Main entry name.
	String mainEntry;

	/// The Jar files queued for later inclusion.
//	public List<JarFile> includeQueue;
	public Map<String, JarFile> includeQueue;

	/// The target JarOutputStream.
	private JarOutputStream jarOutputStream;

	/// Debug utility.
	private final static boolean TESTING = false;//true;//false;
	
	/// Construct a new JarFileBuilder.
	private JarFileBuilder() {
		if(TESTING) IO.println("\nNEW JarFileBuilder");
	}
	
	// ***************************************************************
	// *** CRERATE AND WRITE ATTRIBUTE .jar FILE INLINE
	// ***************************************************************
	public static File writeAttributeFile(final SimulaCoder simCoder, final ProgramModule programModule) throws IOException {
		simCoder.jarFileBuilder = new JarFileBuilder();
		simCoder.jarFileBuilder.open(simCoder, programModule);
		String entryName = programModule.getRelativeAttributeFileName();
		if (entryName != null) {
			byte[] bytes = AttributeFileIO.buildAttrEntry(programModule);
			simCoder.jarFileBuilder.writeEntryToJarOutput(entryName, bytes);
		}

		// ***************************************************************
		// *** ADD CLASS FILES PART OF OUTPUT .jar FILE INLINE
		// ***************************************************************
		return simCoder.jarFileBuilder.close(simCoder);
	}
	
	/// Open the JarFileBuilder.
	/// @param programModule the relevant ProgramModule
	/// @throws IOException if something went wrong
	private void open(final SimulaCoder simCoder, final ProgramModule programModule) throws IOException {
		if(TESTING) IO.println("JarFileBuilder.open: " + programModule);
//		if(jarOutputStream != null) Util.IERR();
		this.programModule = programModule;
		if (Option.internal.TRACING)
			IO.println("BEGIN Create .jar File");
		outputJarFile = new File(simCoder.documentManager.jarFileDir, programModule.getIdentifier().value + ".jar");
		outputJarFile.getParentFile().mkdirs();

		if(outputJarFile.exists()) {
			boolean done = outputJarFile.delete();
			if(TESTING) IO.println("JarFileBuilder.open: outputJarFile.delete() ==> " + done);
		}

		Manifest manifest = new Manifest();
		String packetName = CoreGlobal2.packetName;
		mainEntry = packetName + '/' + programModule.getIdentifier().value;
		mainEntry = mainEntry.replace('/', '.');
		if (Option.internal.TRACING)
			IO.println("Output " + outputJarFile + " MANIFEST'mainEntry=\"" + mainEntry + "\"");
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().putValue("Created-By", CoreGlobal2.simulaReleaseID + " (Portable Simula)");
		if (programModule.isExecutable()) {
			manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, mainEntry);
		} else {
			String relativeAttributeFileName = programModule.getRelativeAttributeFileName();
			if (relativeAttributeFileName != null)
				manifest.getMainAttributes().putValue("SIMULA-INFO", relativeAttributeFileName);
		}
		jarOutputStream = new JarOutputStream(new FileOutputStream(outputJarFile), manifest);
		if(TESTING) IO.println("JarFileBuilder.open: "+jarOutputStream);
		
		if(! simCoder.documentManager.compileViaJavaSource) {
			// Add initial entry: 
			String entryName = packetName + '/';
			writeEntryToJarOutput(entryName, null);
		}
	}
	
	/// Write a JarEntry to the JarOutputStream.
	/// @param entryName the entry name
	/// @param bytes the bytes, may be null
	/// @throws IOException if something went wrong
	public void writeEntryToJarOutput(String entryName, byte[] bytes) throws IOException {
		if(TESTING) IO.println("JarFileBuilder.writeEntryToJarOutput: "+entryName);
		JarEntry entry = new JarEntry(entryName);
		jarOutputStream.putNextEntry(entry);
		if(bytes != null) jarOutputStream.write(bytes);
		jarOutputStream.closeEntry();
//		Thread.dumpStack();
	}
	
	/// Close the JarFileBuilder by writing the .jar file.
	/// @return the outputJarFile
	/// @throws IOException if something went wrong
	private File close(final SimulaCoder simCoder) throws IOException {
		if(TESTING) {
			IO.println("JarFileBuilder.close: BEGIN: ");
	    	Util.doListDirectory("JarFileBuilder.close: BEGIN: ", ""+simCoder.tempClassFileDir + "/" + CoreGlobal2.packetName);
		}
		writeFileToJarFile(new File(simCoder.tempClassFileDir, CoreGlobal2.packetName), simCoder.tempClassFileDir.toString().length());			
		if (programModule.isExecutable()) {
			if(TESTING) IO.println("JarFileBuilder.close: Executable "+programModule);
			// Add the Runtime System
			File rtsHome = new File(CoreGlobal2.simulaRtsLib, "simula/runtime");
			writeFileToJarFile(rtsHome, CoreGlobal2.simulaRtsLib.toString().length());
		} else {
			String id = programModule.getIdentifier().value;
			String kind = (programModule.mainModule instanceof ClassDeclaration) ? "Class " : "Procedure ";
			Util.generalWarning("No execution - Separate Compiled " + kind + id + " is written to: \"" + outputJarFile + "\"");
		}
        
        jarOutputStream.close();
		if(TESTING) IO.println("JarFileBuilder.close: "+jarOutputStream);
		if(CoreGlobal2.verbose) IO.println("JarFileBuilder.close: " + simCoder.documentManager.sourceName + ": JarFile " + outputJarFile);
		
		if(TESTING) {
			IO.println("JarFileBuilder.close: END: ");
			listJarFile("JarFileBuilder.close: END: ", outputJarFile);
	    	Util.doListDirectory("JarFileBuilder.close: END: ", ""+simCoder.tempClassFileDir + "/" + CoreGlobal2.packetName);
		}

		LOG.info("END Create .jar File: " + outputJarFile);
		return (outputJarFile);
	}

	/// Add the jarFile entries to the temp ClassFile directory.
	/// @param jarFile the jarFile to be added
	/// @throws IOException if something went wrong
//	public static void writeJarEntriesToTempClassFiles(final String jarFileName, final JarFile jarFile) throws IOException {
	public static void writeJarEntriesToTempClassFiles(final SimulaCoder simCoder, final String jarFileName) throws IOException {
		try (JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFileName))) {
			String tempClassFileDirName = simCoder.tempClassFileDir.toString();
			JarEntry entry;
			// Loop through all entries in the source JAR
			LOOP2:while ((entry = jarInputStream.getNextJarEntry()) != null) {

				String entryName = entry.getName();
				if (!entryName.startsWith(CoreGlobal2.packetName)) continue LOOP2;
				if (!entryName.endsWith(".class"))				      continue LOOP2;

				// Write entry to tempClassFileDir
				byte[] bytes = jarInputStream.readAllBytes();
		        Path path = Paths.get(tempClassFileDirName + '/' + entryName);
	            Files.createDirectories(path.getParent());
//	            IO.println("JarFileBuilder.writeJarEntriesToTempClassFiles: " + path);
		        Files.write(path, bytes);
			}
		}
	}
	
	/// Add directory or a file to a JarOutputStream.
	/// @param source source file or directory
	/// @param pathSize the path size
	/// @throws IOException if something went wrong
	private void writeFileToJarFile(final File source, final int pathSize) throws IOException {
		if(TESTING) IO.println("JarFileBuilder.writeFileToJarFile: WRITE: " + source);
		if(!source.exists()) {
//			Util.IERR("JarFileBuilder.writeFileToJarFile: source="+source+", exists="+source.exists());
			return;
		}
		if (source.isDirectory()) {
			String name = source.getPath().replace("\\", "/");
			if (!name.isEmpty()) {
				if (!name.endsWith("/")) name += "/";
				name = name.substring(pathSize);
				if (name.startsWith("/")) name = name.substring(1);
//				writeEntryToJarOutput(name, null);
			}
			for (File nestedFile : source.listFiles())
				writeFileToJarFile(nestedFile, pathSize);
			return;
		}
		String entryName = source.getPath().replace("\\", "/");
		if (!entryName.endsWith(".jasm")) {
			entryName = entryName.substring(pathSize);
			if (entryName.startsWith("/"))
				entryName = entryName.substring(1);

			try (InputStream inpt = new FileInputStream(source)) {
				byte[] bytes = inpt.readAllBytes();
				writeEntryToJarOutput(entryName, bytes);
			}
		}
	}

	/// Find the .jar file containing an external class or procedure.
	/// @param identifier class or procedure identifier
	/// @param externalIdentifier the external identifier if any
	/// @return the resulting File or null
	public static File findJarFile(final SimulaBuilder simBuilder, final String identifier, final String externalIdentifier) {
		DocumentManager documentManager = simBuilder.documentManager;
		File jarFile = null;
		try {
			if (externalIdentifier == null || externalIdentifier.length() == 0) {
				// If present search extLib
				if (documentManager.extLib != null) {
					jarFile = new File(documentManager.extLib, identifier + ".jar");
					if (jarFile.exists()) {
//						IO.println("JarFileBuilder.findJarFile: FOUND in extlib: " + jarFile);
						return (jarFile);
					}
//					IO.println("JarFileBuilder.findJarFile: NOT FOUND in extlib: " + jarFile);
				}
				jarFile = new File(documentManager.jarFileDir, identifier + ".jar");
//				IO.println("JarFileBuilder.findJarFile: SimulaCoder.jarFileDir: " + documentManager.jarFileDir);
				if (jarFile.exists()) {
//					IO.println("JarFileBuilder.findJarFile: FOUND in jarFileDir: " + jarFile);
					return (jarFile);
				}
//				IO.println("JarFileBuilder.findJarFile: NOT FOUND in jarFileDir: " + jarFile);
			} else {
				jarFile = new File(externalIdentifier);
				if (jarFile.exists()) {
//					IO.println("JarFileBuilder.findJarFile: FOUND using externalIdentifier: " + jarFile);
					return (jarFile);
				}
//				IO.println("JarFileBuilder.findJarFile: NOT FOUND using externalIdentifier: " + jarFile);
			}
		} catch (Exception e) {}
		return null;
	}
	
	// ***************************************************************
	// *** LIST .jar file
	// ***************************************************************
	/// Debug utility: List .jar file
	/// @param file the .jar file
	public static void listJarFile(final String title, final File file) {
		IO.println("\n--------- " + title + " LIST .jar File: " + file + "  ---------");
		if (!(file.exists() && file.canRead())) {
			Util.generalError("Can't read .jar file: " + file);
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
			Util.IERR("Caused by:", e);
		} finally {
			if (jarFile != null)
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		IO.println();
	}


}
