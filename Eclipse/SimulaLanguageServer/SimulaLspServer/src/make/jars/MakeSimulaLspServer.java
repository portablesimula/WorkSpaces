package make.jars;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
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
import java.util.jar.Manifest;

/// Dependency Tree:
/// org.eclipse.lsp4j:org.eclipse.lsp4j (compile)
///  └── org.eclipse.lsp4j:org.eclipse.lsp4j.jsonrpc (compile)
///       └── com.google.code.gson:gson (compile)
///
/// Eclipse LSP Libraries resides in C:/Eclipse_LSP:
/// - C:/Eclipse_LSP/org.eclipse.lsp4j-1.0.0.jar
/// - "C:/Eclipse_LSP/org.eclipse.lsp4j.jsonrpc-0.24.0.jar
/// - "C:/Eclipse_LSP/gson-2.9.0.jar
/// 
/// They will be unpacked to directories:
/// - C:/Eclipse_LSP/org.eclipse.lsp4j-1.0.0
/// - "C:/Eclipse_LSP/org.eclipse.lsp4j.jsonrpc-0.24.0
/// - "C:/Eclipse_LSP/gson-2.9.0
///
public class MakeSimulaLspServer {
	private static boolean DEBUG = true;
	
	private final static String SETUP_ROOT = "C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaLspServer";
	private final static String SERVER_ROOT = "C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaLspServer";
	private final static String SERVER_BIN = SERVER_ROOT+"/bin";
	private final static String LIBRARY_DIR = "C:/Eclipse_LSP/UnpackedLibraries";

	private static String installParentDirectory = System.getProperty("user.home");
	private static File INSTALL_DIR = new File(installParentDirectory, "Simula");
	
	private static String ECLIPSE_SERVER_DIR = "C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaLspPlugin/server";
	private static String VSCODE_SERVER_DIR = "C:/GitHub/WorkSpaces/VScode/simulaplugin/server";
	private static String INTELLIJ_SERVER_DIR = "C:/GitHub/WorkSpaces/Intellij/SimulaPlugin/src/main/resources/server";

	public static void main(String[] args) {
		try {
//			list(SERVER_BIN);
			
//			File file = new File("C:/Program Files/Eclipse_LSP", "org.eclipse.lsp4j-1.0.0.jar");
//			listJarFile("", file);
////			listManifest(new JarFile(file));

			Path tempDir = Path.of(LIBRARY_DIR);
			unpackJarFile("", tempDir, "C:/Eclipse_LSP/org.eclipse.lsp4j-1.0.0.jar");
			unpackJarFile("", tempDir, "C:/Eclipse_LSP/org.eclipse.lsp4j.jsonrpc-0.24.0.jar");
			unpackJarFile("", tempDir, "C:/Eclipse_LSP/gson-2.9.0.jar");
			
			list(tempDir.toFile());


			
//			File file = new File(INSTALL_DIR, "TestSimulaLspServer.jar");
//			listJarFile("", file);
////			listManifest(new JarFile(file));
			
//			new File(INSTALL_DIR, "SimulaLspServer.jar");

			String INSTALLED = makeSimulaLanguageServer();
			
			listJarFile("", new File(INSTALL_DIR, "SimulaLspServer.jar"));
			listManifest(new JarFile(INSTALLED));

			copyInstalledServer(INSTALLED, ECLIPSE_SERVER_DIR);
			copyInstalledServer(INSTALLED, VSCODE_SERVER_DIR);
			copyInstalledServer(INSTALLED, INTELLIJ_SERVER_DIR);
			
			IO.println("\nSimulaLspServer was created in " + INSTALL_DIR);
			IO.println("                        and in " + ECLIPSE_SERVER_DIR);
			IO.println("                        and in " + VSCODE_SERVER_DIR);
			IO.println("                        and in " + INTELLIJ_SERVER_DIR);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void copyInstalledServer(String INSTALLED, String TARGET_DIR) throws IOException	{
		Path source = Paths.get(INSTALLED);
		Path target = Paths.get(TARGET_DIR);
		IO.println("copyInstalledServerToVSCode: source="+source+"   Exists:"+(new File(INSTALLED)).exists());
		IO.println("copyInstalledServerToVSCode: target="+target+"   Exists:"+(new File(TARGET_DIR)).exists());
	        
	        // Kombinerer mappen og filnavnet til den endelige destinasjonsstien
	        Path destinasjon = target.resolve(source.getFileName());

	        try {
	            // REPLACE_EXISTING tvinger Java til å overskrive filen hvis den finnes
	            Files.copy(source, destinasjon, StandardCopyOption.REPLACE_EXISTING);
	            System.out.println("Filen ble kopiert og overskrevet suksessfullt.");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	
	// ***************************************************************
	// *** MAKE SIMULA LANGUAGE SERVER JAR
	// ***************************************************************
	private static String makeSimulaLanguageServer() throws IOException	{
		IO.println("Make Simula Language Server.jar in "+INSTALL_DIR);
		INSTALL_DIR.mkdirs();
		String INSTALL_FILE = INSTALL_DIR+"/SimulaLspServer.jar";
		IO.println("Make Simula Language Server.jar as "+INSTALL_FILE);
		
		String compilerManifest=SETUP_ROOT+"/src/make/jars/MANIFEST.MF";
		IO.println("makeSimulaLanguageServer: compilerManifest: " + compilerManifest+"  Exists:" + (new File(compilerManifest)).exists());
		execute("jar","cmf", compilerManifest, INSTALL_FILE,
				"-C", SERVER_BIN, "./simula",
				"-C", LIBRARY_DIR, "./com",
				"-C", LIBRARY_DIR, "./org");
		
//		execute("jar", "-tvf", INSTALL_DIR+"/TestSimulaLspServer.jar");
		return INSTALL_FILE;
	}
	
	// ***************************************************************
	// *** LIST FILES
	// ***************************************************************
	@SuppressWarnings("unused")
	private static void list(final String dirName) { list(new File(dirName)); }
	private static void list(final File dir) {
		try { IO.println("------------  LIST "+dir+"  ------------");
			  list("",dir);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private static void list(String indent,final File dir) {
		try {
			//IO.println("tmpClass: "+dir);
			File[] elt = dir.listFiles();
			if(elt==null || elt.length==0) {
				IO.println("Empty Directory: "+dir);
				return; 
			}
			IO.println("Elements: "+elt.length);
			for (File f : elt) {
				IO.println(indent+"- "+getModifiedTime(f)+"  "+f);
				if(f.isDirectory()) list(indent+"   ",f);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	private static String getModifiedTime(File file) {
		try { Path path = Paths.get(file.toString());
			  BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
			  return(attr.lastModifiedTime().toString().substring(0,19).replace('T',' '));
		} catch (IOException e) { e.printStackTrace(); }
		return(null);
	}
	
	// ***************************************************************
	// *** LIST .jar file
	// ***************************************************************
	/// Debug utility: List .jar file
	/// @param file the .jar file
	public static void listJarFile(final String title, final File file) {
		IO.println("\n--------- " + title + " LIST .jar File: " + file + "  ---------");
//		if (!(file.exists() && file.canRead())) {
//			Util.generalError("Can't read .jar file: " + file);
//			return;
//		}
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
			listManifest(jarFile);
		} catch (IOException e) {
			e.printStackTrace();
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

	
	// ***************************************************************
	// *** listManifest
	// ***************************************************************
	private static void listManifest(JarFile jarFile) throws IOException  {
		Manifest manifest = jarFile.getManifest();
		Attributes mainAttributes=manifest.getMainAttributes();
		if(DEBUG) IO.println("SimulaExtractor.loadManifest: Main-Class="+mainAttributes.getValue("Main-Class"));
		printAttributes(mainAttributes);
	}

    // Helper method to loop through and print a set of Attributes
    private static void printAttributes(Attributes attributes) {
        for (Map.Entry<Object, Object> attribute : attributes.entrySet()) {
            Attributes.Name key = (Attributes.Name) attribute.getKey(); //
            String value = (String) attribute.getValue(); //
            System.out.println("  " + key + ": " + value);
        }
    }
	
	// ***************************************************************
	// *** UNPACK .jar file
	// ***************************************************************
	/// Debug utility: List .jar file
	/// @param file the .jar file
	public static void OLD_unpackJarFile(final String title, final File tempFile, final File file) {
		IO.println("\n--------- " + title + " UNPACK .jar File: " + file + "  ---------");
//		if (!(file.exists() && file.canRead())) {
//			Util.generalError("Can't read .jar file: " + file);
//			return;
//		}
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
			listManifest(jarFile);
		} catch (IOException e) {
			e.printStackTrace();
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

    public static void unpackJarFile(final String title, final Path tempDir, final String jarPathName) throws IOException {
        // 1. Create a secure system temporary directory
//        Path tempDir = Files.createTempDirectory(tempDirPrefix);
    	Path jarPath = Path.of(jarPathName);

    	
        // 2. Open the JAR file
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            Enumeration<JarEntry> entries = jar.entries();
            
            // 3. Iterate through every file and directory in the JAR
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                
                // Resolve the entry path relative to our target temporary directory
                Path targetPath = tempDir.resolve(entry.getName()).normalize();
                
                // Security Check: Guard against Zip Slip vulnerability (directory traversal)
                if (!targetPath.startsWith(tempDir)) {
                    throw new IOException("Malicious JAR entry detected outside target directory: " + entry.getName());
                }
                
                if (entry.isDirectory()) {
                    // Create the nested subdirectory structural layout
                    Files.createDirectories(targetPath);
                } else {
                    // Ensure the parent directories exist (in case the JAR structure is implicit)
                    if (targetPath.getParent() != null) {
                        Files.createDirectories(targetPath.getParent());
                    }
                    
                    // Extract and write the file
                    try (InputStream is = jar.getInputStream(entry)) {
                        Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }

	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	private static int execute(String... cmd) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		String line="";
		for(int i=0;i<cmd.length;i++) line=line+" "+cmd[i];
        IO.println("MakeCompiler.execute: command="+line);
//	    String cmd=command.trim()+'\n';
		Process process = runtime.exec(cmd);
		//try
		{ InputStream err=process.getErrorStream();
		  InputStream inp=process.getInputStream();
		  while(process.isAlive())
		  { while(err.available()>0) System.err.append((char)err.read());
		    while(inp.available()>0) System.out.append((char)inp.read());
			
		  }
		  // process.waitFor();
		} //catch(InterruptedException e) { e.printStackTrace(); }
		return(process.exitValue());
	}

	
}
