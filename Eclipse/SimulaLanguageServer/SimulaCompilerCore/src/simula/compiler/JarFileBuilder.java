/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler;

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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import simula.Option;
import simula.compiler.syntaxClass.declaration.ClassDeclaration;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.Util;

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

	/// The intermediate classFileMap.
	private final HashMap<String,byte[]> classFileMap;

	/// The target JarOutputStream.
	private JarOutputStream jarOutputStream;

	/// Debug utility.
	private final static boolean TESTING = true;// false;
	
	/// Construct a new JarFileBuilder.
	public JarFileBuilder() {
		if(TESTING) IO.println("\nNEW JarFileBuilder");
		this.classFileMap = new HashMap<String,byte[]>();
	}
	
	/// Open the JarFileBuilder.
	/// @param programModule the relevant ProgramModule
	/// @throws IOException if something went wrong
	public void open(final ProgramModule programModule) throws IOException {
		if(TESTING) IO.println("JarFileBuilder.open: " + programModule);
		if(jarOutputStream != null) Util.IERR();
		this.programModule = programModule;
		if (Option.internal.TRACING)
			Util.println("BEGIN Create .jar File");
		outputJarFile = new File(SimulaCompiler.outputDir, programModule.getIdentifier().value + ".jar");
		outputJarFile.getParentFile().mkdirs();
		Manifest manifest = new Manifest();
		String packetName = SimulaCompiler.packetName;
		mainEntry = packetName + '/' + programModule.getIdentifier().value;
		mainEntry = mainEntry.replace('/', '.');
		if (Option.internal.TRACING)
			Util.println("Output " + outputJarFile + " MANIFEST'mainEntry=\"" + mainEntry + "\"");
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().putValue("Created-By", SimulaCompiler.simulaReleaseID + " (Portable Simula)");
		if (programModule.isExecutable()) {
			manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, mainEntry);
//			manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, ".");
		} else {
			String relativeAttributeFileName = programModule.getRelativeAttributeFileName();
			if (relativeAttributeFileName != null)
				manifest.getMainAttributes().putValue("SIMULA-INFO", relativeAttributeFileName);
		}
		jarOutputStream = new JarOutputStream(new FileOutputStream(outputJarFile), manifest);
		
		if(SimulaCompiler.compilerMode != SimulaCompiler.CompilerMode.viaJavaSource) {
			// Add initial entry: 
			String entryName = packetName + '/';
			writeJarEntry(entryName, null);
		}
	}
	
	/// Put a JarEntry to the intermediate classFileMap.
	/// @param entryName the entry name
	/// @param bytes the bytes, may be null
	public void putMapEntry(String entryName, byte[] bytes) {
		if(TESTING)	IO.println("JarFileBuilder.putMapEntry: "+entryName);
		byte[] prev = classFileMap.put(entryName,bytes);
		if(prev != null) {
			if(SimulaCompiler.verbose)
				Util.println("JarOutputSet.putMapEntry: "+entryName+" WAS REPLACED");
		}
	}
	
	/// Write a JarEntry to the JarOutputStream.
	/// @param entryName the entry name
	/// @param bytes the bytes, may be null
	/// @throws IOException if something went wrong
	public void writeJarEntry(String entryName, byte[] bytes) throws IOException {
		if(TESTING) IO.println("JarFileBuilder.writeJarEntry: "+entryName);
		JarEntry entry = new JarEntry(entryName);
		jarOutputStream.putNextEntry(entry);
		if(bytes != null) jarOutputStream.write(bytes);
		jarOutputStream.closeEntry();
	}
	
	/// Close the JarFileBuilder by writing the .jar file.
	/// @return the outputJarFile
	/// @throws IOException if something went wrong
	public File close() throws IOException {
		// Write the actual .jar file
		if(TESTING) printClassFileMap("END JarFileBuilder.close");
        for (Entry<String, byte[]> entry : classFileMap.entrySet()) {
            String entryName = entry.getKey();
            byte[] bytes = entry.getValue();
            writeJarEntry(entryName, bytes);
        }
       
		if (programModule.isExecutable()) {
			if(TESTING) IO.println("JarFileBuilder.close: Executable "+programModule);
			// Add the Runtime System
			File rtsHome = new File(SimulaCompiler.simulaRtsLib, "simula/runtime");
			add(false, rtsHome, SimulaCompiler.simulaRtsLib.toString().length());
		} else {
			String id = programModule.getIdentifier().value;
			String kind = (programModule.mainModule instanceof ClassDeclaration) ? "Class " : "Procedure ";
			Util.generalWarning("No execution - Separate Compiled " + kind + id + " is written to: \"" + outputJarFile + "\"");
		}
        
        jarOutputStream.close();
		if(SimulaCompiler.verbose) Util.println("JarFileBuilder.close: " + SimulaCompiler.sourceName + ": JarFile " + outputJarFile);
		
		if(TESTING) {
			IO.println("JarFileBuilder.close: ");
			listJarFile(outputJarFile);
		}

		if (Option.internal.TRACING)
			Util.println("END Create .jar File: " + outputJarFile);
		return (outputJarFile);
	}
	
	/// Add temp .class files to jarOutputStream.
	/// @throws IOException if something went wrong
	public void addTempClassFiles() throws IOException {
		if(SimulaCompiler.compilerMode != SimulaCompiler.CompilerMode.viaJavaSource) Util.IERR();
		add(true, new File(SimulaCompiler.tempClassFileDir, SimulaCompiler.packetName), SimulaCompiler.tempClassFileDir.toString().length());
	}	
	
	/// Add directory or a file to a JarOutputStream, or
	/// Put it into the intermediate classFileMap.
	/// @param doPut true:put it, otherwise add it
	/// @param source source file or directory
	/// @param pathSize the path size
	/// @throws IOException if something went wrong
	private void add(final boolean doPut, final File source, final int pathSize) throws IOException {
		if(!source.exists())
			Util.IERR("SimulaCompiler.add: source="+source+", exists="+source.exists());
		if (source.isDirectory()) {
			String name = source.getPath().replace("\\", "/");
			if (!name.isEmpty()) {
				if (!name.endsWith("/")) name += "/";
				name = name.substring(pathSize);
				if (name.startsWith("/")) name = name.substring(1);
				if(doPut)
					 putMapEntry(name, null);
				else writeJarEntry(name, null);
			}
			for (File nestedFile : source.listFiles())
				add(doPut, nestedFile, pathSize);
			return;
		}
		String entryName = source.getPath().replace("\\", "/");
		if (!entryName.endsWith(".jasm")) {
			entryName = entryName.substring(pathSize);
			if (entryName.startsWith("/"))
				entryName = entryName.substring(1);
			if (Option.internal.TRACING && (!entryName.startsWith("simula/runtime")))
				Util.println("ADD-TO-JAR: "+entryName);

			try (InputStream inpt = new FileInputStream(source)) {
				byte[] bytes = inpt.readAllBytes();
				if(doPut)
					 putMapEntry(entryName, bytes);
				else writeJarEntry(entryName, bytes);
			}
		}
	}
	
	/// Expand .jar file entries into the classFileMap.
	/// @param jarFile the .jar file to read
	/// @throws IOException if something went wrong
	public void expandJarFile(final JarFile jarFile) throws IOException {
		if(TESTING) IO.println("JarFileBuilder.expandJarFile: JarFileName="+jarFile.getName());
		if (SimulaCompiler.verbose)
			Util.println("---------  INCLUDE .jar File: " + jarFile.getName() + "  ---------");
		Enumeration<JarEntry> entries = jarFile.entries();
		LOOP: while (entries.hasMoreElements()) {
			JarEntry inputEntry = entries.nextElement();

			String entryName = inputEntry.getName();
			if (!entryName.startsWith(SimulaCompiler.packetName))	continue LOOP;
			if (!entryName.endsWith(".class"))				continue LOOP;

			InputStream inputStream = null;
			try {
				inputStream = jarFile.getInputStream(inputEntry);
				byte[] bytes = inputStream.readAllBytes();
				putMapEntry(entryName, bytes);
				addToTempClassfiles(entryName, bytes);
			} finally {	if (inputStream != null) inputStream.close(); }
		}
		if(TESTING) printClassFileMap("END JarFileBuilder.expandJarFile");
	}
	
	private void addToTempClassfiles(final String entryName, final byte[] bytes) throws IOException {
		IO.println("JarFileBuilder.addToTempClassfiles: " + entryName);
        Path path = Paths.get(""+SimulaCompiler.tempClassFileDir + '/' + entryName);

        // Oppretter nødvendige mapper hvis de ikke eksisterer (f.eks. com/example/)
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
		IO.println("JarFileBuilder.addToTempClassfiles: path = " + path);

        // Skriver alle bytes til filen i én operasjon
        Files.write(path, bytes);
    	Util.doListDirectory(""+SimulaCompiler.tempClassFileDir);
//    	Util.STOP();
	}
	
    /**
     * Skriver en klassefil til disk basert på klassenavn og bytes.
     *
     * @param className Det fulle navnet på klassen (f.eks. "com.example.MyClass")
     * @param classBytes Den ferdige byte-arrayen som utgjør klassefilen
     * @throws IOException Hvis det oppstår en feil under skriving til fil
     */
    public static void writeClassFile(String className, byte[] classBytes) throws IOException {
        // Konverterer pakkenavn (med punktum) til en gyldig filsti
        // F.eks: "com.example.MyClass" blir til "com/example/MyClass.class"
        String fileName = className.replace('.', '/') + ".class";
        Path path = Paths.get(fileName);

        // Oppretter nødvendige mapper hvis de ikke eksisterer (f.eks. com/example/)
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        // Skriver alle bytes til filen i én operasjon
        Files.write(path, classBytes);
    }

	/// Debug utility: printClassFileMap.
	/// @param title the title String.
	private void printClassFileMap(String title) {
		IO.println("============================== printClassFileMap: "+title+" ==============================");
        for (Entry<String, byte[]> entry : classFileMap.entrySet()) {
            String entryName = entry.getKey();
            byte[] bytes = entry.getValue();
            IO.println("JarFileBuilder.printClassFileMap: "+entryName+"   Size="+((bytes==null)?0:bytes.length));
        }		
		IO.println("END ========================== printClassFileMap: "+title+" ==============================");
	}


	/// Find the .jar file containing an external class or procedure.
	/// @param identifier class or procedure identifier
	/// @param externalIdentifier the external identifier if any
	/// @return the resulting File or null
	public static File findJarFile(final String identifier, final String externalIdentifier) {
		File jarFile = null;
		try {
			if (externalIdentifier == null || externalIdentifier.length() == 0) {
				// If present search extLib
				if (SimulaCompiler.extLib != null) {
					jarFile = new File(SimulaCompiler.extLib, identifier + ".jar");
					if (jarFile.exists()) {
//						IO.println("JarFileBuilder.findJarFile: FOUND in extlib: " + jarFile);
						return (jarFile);
					}
//					IO.println("JarFileBuilder.findJarFile: NOT FOUND in extlib: " + jarFile);
				}
				jarFile = new File(SimulaCompiler.outputDir, identifier + ".jar");
				IO.println("JarFileBuilder.findJarFile: SimulaCompiler.outputDir: " + SimulaCompiler.outputDir);
				if (jarFile.exists()) {
					IO.println("JarFileBuilder.findJarFile: FOUND in outputDir: " + jarFile);
					return (jarFile);
				}
				IO.println("JarFileBuilder.findJarFile: NOT FOUND in outputDir: " + jarFile);
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

	/// Add the jarFile to the includeQueue.
	/// @param jarFile the jarFile to be added
	/// @throws IOException if something went wrong
	public static void addToIncludeQueue(final JarFile jarFile) throws IOException {
		if(CoreGlobal.includeQueue == null) CoreGlobal.includeQueue = new LinkedList<JarFile>();
		CoreGlobal.includeQueue.add(jarFile);			
	}

	/// Add the jarFiles in the includeQueue.
	/// @throws IOException if something went wrong
	public void addIncludeQueue() throws IOException {
		if(CoreGlobal.includeQueue != null) {
			for(JarFile jarFile:CoreGlobal.includeQueue) {
				if(TESTING)
					IO.println("JarFileBuilder.addIncludeQueue: expandJarFile: "+jarFile.getName());
				expandJarFile(jarFile);	
			}
		}
	}

	
	// ***************************************************************
	// *** LIST .jar file
	// ***************************************************************
	/// Debug utility: List .jar file
	/// @param file the .jar file
	public static void listJarFile(final File file) {
		Util.println("---------  LIST .jar File: " + file + "  ---------");
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
				Util.println(key.toString() + "=\"" + val + "\"");
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
				Util.println("Jar-Entry: " + size + "  " + date + "  \"" + entry + "\"");
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
	}


}
