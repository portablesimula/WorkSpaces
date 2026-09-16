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

public class MakeSimulaLspServer {
	private static boolean DEBUG = true;
	
	private final static String SETUP_ROOT="C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/MakeJars";
	private final static String SERVER_ROOT="C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaPlugin";
	private final static String SERVER_BIN=SERVER_ROOT+"/bin";


	private static String installParentDirectory = System.getProperty("user.home");
	private static File INSTALL_DIR = new File(installParentDirectory, "Simula");
	
	private static String VSCODE_SERVER_DIR = "C:/GitHub/WorkSpaces/VScode/simulaplugin/server";
	private static String INTELLIJ_SERVER_DIR = "C:/GitHub/WorkSpaces/Intellij/SimulaPlugin/build/resources/main/server";

	public static void main(String[] args) {
		try {
//			list(SERVER_BIN);
			
//			File file = new File(INSTALL_DIR, "OLD_TestSimulaLspServer.jar");
//			listJarFile("", file);
////			listManifest(new JarFile(file));
			
			new File(INSTALL_DIR, "SimulaLspServer.jar");
//			listJarFile("", INSTALLED);
//			listManifest(new JarFile(INSTALLED));

			String INSTALLED = makeSimulaLanguageServer();
			
			copyInstalledServerToVSCode(INSTALLED, VSCODE_SERVER_DIR);
			copyInstalledServerToVSCode(INSTALLED, INTELLIJ_SERVER_DIR);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void copyInstalledServerToVSCode(String INSTALLED, String TARGET_DIR) throws IOException	{
		Path source = Paths.get(INSTALLED);
		Path target = Paths.get(TARGET_DIR);
		IO.println("copyInstalledServerToVSCode: source="+source);
		IO.println("copyInstalledServerToVSCode: target="+target);
	        
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
	// *** MAKE SIMULA COMPILER JAR
	// ***************************************************************
	private static String makeSimulaLanguageServer() throws IOException	{
		IO.println("Make Simula Language Server.jar in "+INSTALL_DIR);
		INSTALL_DIR.mkdirs();
		String INSTALL_FILE = INSTALL_DIR+"/SimulaLspServer.jar";
		IO.println("Make Simula Language Server.jar as "+INSTALL_FILE);
		String compilerManifest=SETUP_ROOT+"/src/make/jars/ServerManifest.MF";
		execute("jar","cmf",compilerManifest,INSTALL_FILE,
				"-C", SERVER_BIN, "./simula");
//		execute("jar", "-tvf", INSTALL_DIR+"/TestSimulaLspServer.jar");
		return INSTALL_FILE;
	}
	
	// ***************************************************************
	// *** LIST FILES
	// ***************************************************************
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
