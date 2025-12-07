/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package make.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static java.nio.file.StandardCopyOption.*;

//import simula.compiler.utilities.Global;
//import simula.compiler.utilities.Util;

/**
 * Updates are written to:  C://GitHub/github.io/setup
 * 
 * NOTE: Remember to Update Global.simulaReleaseID
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class MakeSetup {
	public static final String RELEASE_ID = "SPort-1.0";
	private final static int REVISION = 4;
	
	private final static boolean EARLY_ACCESS = true;   // Used to produce an Early Access
//	private final static boolean EARLY_ACCESS = false;  // Used to produce a Release
	
	private final static String SETUP_TEMPS="C:/GitHub/MakeSetup_Temps";
	private final static String RELEASE_HOME=SETUP_TEMPS+"/"+RELEASE_ID;
	private final static String SPORT_HOME="C:/SPORT";

	private final static String RELEASE_SAMPLES=RELEASE_HOME+"/samples";
	private final static String GITHUB_ROOT="C:/GitHub";
	private final static String SETUP_ROOT="C:/GitHub/EclipseWorkSpaces/S-Port-Simula/MakeSetup";
	private final static String INSTALLER_BIN=SETUP_ROOT+"/bin";

	
	private static String SETUP_IDENT;
	private static void setSetupIdent() {
		if(EARLY_ACCESS)
			 SETUP_IDENT="SPortSetupEA";  // Used to produce a TestSetup
		else SETUP_IDENT="SPortSetup";    // Used to produce a Release
	}

	public static void main(String[] args) {
        //System.setProperty("file.encoding","UTF-8");
		printHeading("Make Simula Compiler, SETUP_TEMPS="+SETUP_TEMPS);
		try {
			setSetupIdent();
			deleteFiles(SETUP_TEMPS);
			updateSetupProperties();
			copySimulaRuntimeSystem();
			copyFECandBEC();
			copySimulaIconFiles();
			copySimulaReleaseTests();
			
			makeSimulaInstaller();

			executeSPortSetup();
		} catch(Exception e) { e.printStackTrace(); }
	}

	// ***************************************************************
	// *** UPDATE SETUP PROPERTIES
	// ***************************************************************
	private static void updateSetupProperties() {
		String setupDated=""+new Date();
		setProperty("sPort.setup.dated",setupDated);
		setProperty("sPort.version",""+RELEASE_ID);
		setProperty("sPort.revision",""+REVISION);
		try { // also update 'sPort-Revision' and 'sPort-Setup-Dated' in InstallerManifest.MF
//		   String SETUP_SRC=SIMULA_ROOT+"\\src\\make\\setup";
		   String SETUP_SRC=SETUP_ROOT+"\\src\\make\\setup";
		   File installerManifestFile=new File(SETUP_SRC+"\\InstallerManifest.MF");
		   IO.println("installerManifestFile: "+installerManifestFile);
		   Manifest manifest=new Manifest();
		   InputStream inputStream=new FileInputStream(installerManifestFile);
		   manifest.read(inputStream);
		   Attributes main=manifest.getMainAttributes();
		   IO.println("Main-Class: "+main.getValue("Main-Class"));
		   IO.println("sPort-Revision: "+main.getValue("sPort-Revision"));
		   main.putValue("sPort-Revision",""+REVISION);
		   main.putValue("sPort-Setup-Dated",""+setupDated);
		   IO.println("sPort-Revision: "+main.getValue("sPort-Revision"));
		   IO.println("sPort-Setup-Dated: "+main.getValue("sPort-Setup-Dated"));
		   OutputStream outputStream=new FileOutputStream(installerManifestFile);
		   manifest.write(outputStream);
		} catch(Exception e) { e.printStackTrace(); }
	}
    private static File setupPropertiesFile;
    private static Properties setupProperties;
	public static String getProperty(String key,String defaultValue) {
		if(setupPropertiesFile==null) loadProperties();
		return(setupProperties.getProperty(key,defaultValue));
	}
	
	public static void setProperty(String key,String value) {
		if(setupPropertiesFile==null) loadProperties();
		setupProperties.setProperty(key,value);
		storeProperties();
	}
	
	private static void loadProperties() {
		String USER_HOME=System.getProperty("user.home");
		IO.println("USER_HOME="+USER_HOME);
//		File setupPropertiesDir=new File(USER_HOME+File.separatorChar+".simula");
		File setupPropertiesDir=new File(GITHUB_ROOT+"\\github.io\\setup");
		IO.println("setupPropertiesDir="+setupPropertiesDir);
		setupPropertiesDir.mkdirs();
		setupPropertiesFile=new File(setupPropertiesDir,"setupProperties.xml");
		setupProperties = new Properties();
		try { setupProperties.loadFromXML(new FileInputStream(setupPropertiesFile));
		} catch(Exception e) {} // e.printStackTrace(); }
	}
	
	private static void storeProperties() {
		if(! EARLY_ACCESS) {
			setupProperties.list(System.out);
			try { setupProperties.storeToXML(new FileOutputStream(setupPropertiesFile),"Setup Properties");
			} catch(Exception e) { e.printStackTrace(); }
		}
	}

	// ***************************************************************
	// *** COPY SIMULA RUNTIME SYSTEM
	// ***************************************************************
	private static void copySimulaRuntimeSystem() throws IOException {
		File source=new File(SPORT_HOME+"/RTS");
		String target=RELEASE_HOME+"/RTS";
		printHeading("Copy Simula RuntimeSystem "+source+" ===> "+target);
        IO.println("MakeCompiler.copySimulaRuntimeSystem: target="+target);
		copyFolder(source,new File(target),true);
		list(source);
	}

	// ***************************************************************
	// *** COPY FEC and BEC
	// ***************************************************************
	private static void copyFECandBEC() throws IOException	{
		printHeading("Copy SIM, FEC and BEC into "+RELEASE_HOME);
		copyFile("SIM.jar");
		copyFile("SimulaFEC.jar");
		copyFile("CommonBEC.jar");
		copyFile("runSimulaEditor.bat");
	}
	private static void copyFile(String fileName) throws IOException	{
		File source=new File(SPORT_HOME+"/" + fileName);
		File target=new File(RELEASE_HOME+"/" + fileName);
		target.mkdirs();
		IO.println("source="+source);
		IO.println("target="+target);
		Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
	}

	// ***************************************************************
	// *** COPY SIMULA ICON FILES
	// ***************************************************************
	private static void copySimulaIconFiles() throws IOException	{
		printHeading("Copy Simula Icons .png's into "+RELEASE_HOME);
		copyImageFile("sim.ico");
		copyImageFile("sim.png");
		copyImageFile("sim2.png");
		copyImageFile("simula.png");
	}
	private static void copyImageFile(String fileName) throws IOException	{
		File source=new File(SPORT_HOME+"/icons/"+fileName);
		File target=new File(RELEASE_HOME+"/icons/"+fileName);
		target.mkdirs();
		IO.println("source="+source);
		IO.println("target="+target);
		Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
	}
	
	// ***************************************************************
	// *** COPY SIMULA RELEASE TEST PROGRAMS
	// ***************************************************************
	private static void copySimulaReleaseTests() throws IOException	{
		printHeading("Copy Simula Sample Programs into "+RELEASE_SAMPLES);
		deleteFiles(RELEASE_SAMPLES);
		copyFolder(new File(SPORT_HOME+"/Samples"), new File(RELEASE_SAMPLES), false);
		list(new File(SPORT_HOME+"/Samples"));
		list(new File(RELEASE_SAMPLES));
		copyFolder(new File(SPORT_HOME+"/Samples/data"), new File(RELEASE_SAMPLES+"/data"), false);
	}
	
	// ***************************************************************
	// *** DELETE FILES
	// ***************************************************************
	private static void deleteFiles(final String dirName) {
		try { File tmpClass = new File(dirName);
			  File[] elt = tmpClass.listFiles();
			  if(elt==null) return; 
			  for (File f : elt) {
				  if(f.isDirectory()) deleteFiles(f.getPath());
				  f.delete();
			  }
		} catch (Exception e) { e.printStackTrace(); }
	}

	// ***************************************************************
	// *** COPY FOLDER
	// ***************************************************************
	static private void copyFolder(File source, File target,boolean copySubFolders) throws IOException {
		IO.println("COPY: "+source+" ==> "+target);
		target.mkdirs();
	    for(File file: source.listFiles()) {
	        File fileDest = new File(target, file.getName());
	        //IO.println(fileDest.getAbsolutePath());
	        if(file.isDirectory()) {
	            if(copySubFolders) copyFolder(file, fileDest, copySubFolders);
	        } else {
	            //if(fileDest.exists()) continue;
	        	Files.copy(file.toPath(), fileDest.toPath());
	        }
	    }
	}	
	// ***************************************************************
	// *** LIST FILES
	// ***************************************************************
//	private static void list(final String dirName) { list(new File(dirName)); }
	private static void list(final File dir) {
		try { Util.println("------------  LIST "+dir+"  ------------");
			  list("",dir);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private static void list(String indent,final File dir) {
		try {
			//Util.println("tmpClass: "+dir);
			File[] elt = dir.listFiles();
			if(elt==null || elt.length==0) {
				Util.println("Empty Directory: "+dir);
				return; 
			}
			Util.println("Elements: "+elt.length);
			for (File f : elt) {
				Util.println(indent+"- "+getModifiedTime(f)+"  "+f);
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
	// *** MAKE SIMULA INSTALLER JAR
	// ***************************************************************
	private static void makeSimulaInstaller() throws IOException	{
		printHeading("Make Simula Setup.jar in "+SETUP_TEMPS);
		File releaseHome=new File(RELEASE_HOME);
		releaseHome.mkdirs();
//		String SETUP_SRC=SIMULA_ROOT+"/src/make/setup";
		String SETUP_SRC=SETUP_ROOT+"/src/make/setup";
		
		// CopySimulaIcon  --> INSTALLER_BIN;
		File source=new File(SETUP_SRC+"/sim.png");
		File target=new File(INSTALLER_BIN+"/make/setup/sim.png");
		target.mkdirs();
		IO.println("source="+source);
		IO.println("target="+target);
		Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
			
		String installerManifest=SETUP_SRC+"/InstallerManifest.MF";
		
		String files=" -C "+RELEASE_HOME+"."  // Complete Simula Release
				    +" -C "+INSTALLER_BIN+" ./make/setup";
		IO.println("jar cmf "+installerManifest+" "+SETUP_TEMPS+"/"+SETUP_IDENT+".jar"+files);
		
		exec("jar", "cmf", installerManifest, SETUP_TEMPS+"/"+SETUP_IDENT+".jar",
				"-C",RELEASE_HOME, ".",  // Complete Simula Release
			    "-C",INSTALLER_BIN, "./make/setup");
		printHeading("BEGIN -- List Simula Setup.jar in "+SETUP_TEMPS);
		exec("jar","-tvf",SETUP_TEMPS+"/"+SETUP_IDENT+".jar");
		printHeading("END -- List Simula Setup.jar in "+SETUP_TEMPS);
		copySetupJAR();
	}
	
	// ***************************************************************
	// *** COPY SIMULA INSTALLER JAR
	// ***************************************************************
	private static void copySetupJAR() throws IOException	{
		File source=new File(SETUP_TEMPS+"/"+SETUP_IDENT+".jar");
		File target2=new File(GITHUB_ROOT+"/github.io/setup/"+SETUP_IDENT+".jar");
		IO.println("source="+source);
		IO.println("target2="+target2);
		Files.copy(source.toPath(), target2.toPath(), REPLACE_EXISTING);
		if(! EARLY_ACCESS) {
			String SETUP_IDENT_WITH_REVISION=SETUP_IDENT+"-R"+REVISION+".jar"; // E.g: simula-setup-r28.jar
			File target1=new File(GITHUB_ROOT+"/github.io/setup/"+SETUP_IDENT_WITH_REVISION);
			IO.println("target1="+target1);
			Files.copy(source.toPath(), target1.toPath(), REPLACE_EXISTING);

		}
		//updateSetupProperties();
	}
	
	// ***************************************************************
	// *** EXECUTE SIMULA SETUP
	// ***************************************************************
	private static void executeSPortSetup() throws IOException	{
		String SETUP_JAR=GITHUB_ROOT+"/github.io/setup/"+SETUP_IDENT+".jar";
		printHeading("Execute SPortSetup: "+SETUP_JAR);
		exec("java","-jar",SETUP_JAR);
	}
	
	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	public static int exec(String... cmd) throws IOException {
		String cmdLine="";
		for(int i=0;i<cmd.length;i++) cmdLine=cmdLine+" "+cmd[i];
        IO.println("SIM.Util.exec: command ="+cmdLine);
		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		IO.println("SIM.Util.exec: processBuilder = "+processBuilder);
		processBuilder.inheritIO();
		processBuilder.redirectErrorStream();
		try {
			Process process = processBuilder.start();
			IO.println("SIM.Util.exec: process = "+process);
			int exitCode = process.waitFor();
			IO.println("SIM.Util.exec: exitCode = "+exitCode);
			return exitCode;
		} catch(Exception e) {
			e.printStackTrace();
			Util.IERR("SIM.Util.exec: Process Execution failed: " + cmdLine + "  " + e);
			return -1;
		}
	}

	
	private static void printHeading(String heading) {
		IO.println("************************************************************************************************************************************");
		IO.println("*** "+heading);
		IO.println("************************************************************************************************************************************");
	}
	
}
