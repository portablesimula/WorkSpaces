package sim.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayDeque;
import java.util.Properties;

import javax.swing.ImageIcon;

import sim.editor.ConsolePanel;

public class Global {

	/// Default constructor.
	Global() {}

	/// Initiate Global variables.
	public static void initiate() {
		Option.INIT();
//		Global.consolePanel = new ConsolePanel();	
       	Global.loadSPortSetupProperties(); 
		String SPORT_HOME = Global.sPortSetupProperties.getProperty("sPort.home","?");
		if (SPORT_HOME != null) {
//			if(Option.verbose) Util.println("Global.initiate: SPORT_HOME="+SPORT_HOME);
			String SPORT_VERSION = Global.sPortSetupProperties.getProperty("sPort.version","?");
//			if(Option.verbose) Util.println("Global.initiate: SPORT_VERSION="+SPORT_VERSION);
			if (SPORT_VERSION != null) {
				simdir = new File(SPORT_HOME, SPORT_VERSION);
//				if(Option.verbose) Util.println("Global.initiate: simdir="+simdir);
				simIcon = new ImageIcon(new File(simdir, "icons/sim2.png").toString());
				sIcon = new ImageIcon(new File(simdir, "icons/sim.png").toString());
			}
		}
    	String revision = Global.sPortSetupProperties.getProperty("sPort.revision","?");
    	String dated = Global.sPortSetupProperties.getProperty("sPort.setup.dated","?");
    	String releaseID=Global.sPortReleaseID+'R'+revision;
		Global.sPortVersion = "S-Port Editor ("+releaseID+ " built "+dated+" using "+getJavaID()+")";
//       	Global.loadSPortEditorProperties();
	}
	
	/// Utility: get Java ID
	/// @return the Java ID string
	private static String getJavaID() {
		String javaID="Java version "+System.getProperty("java.version");
        return(javaID);
	}

	public static String selectors;
	public static String sourceFileName;
	public static String sCodeFileName;

	/// The Simula release identification.
	/// 
	/// NOTE: When updating release id, change version in SimulaExtractor and RuntimeSystem
	public static final String sPortReleaseID = "S-Port-1.0";
	
	public static File simdir; // E.g: ../user/Simula/SPort-1.0

	/// Indicates that the S-Port editor is running.
	public static boolean inEditor;
	
	/// A Simula icon
	public static ImageIcon simIcon;
	/// A small Simula icon
	static ImageIcon sIcon;

	/// The SPort Home directory.
	public static File sPortHome;

	/// The SPort Release Home directory.
	public static File releaseHome;
	
	/// The SPort Version
	public static String sPortVersion;

	/// The SPort properties file
	public static File sPortPropertiesFile;
	
	/// The SPort properties
	private static Properties sPortProperties;
	
	
	/// The SPort properties
	public static Properties sPortSetupProperties;
	
//	/// The sample source directory. Where to find sample SPort files
//	public static File sampleSourceDir;
	
	/// Current workspace. Where to find .sim source files
	public static File currentWorkspace;
	
//	/// The output directory. Used by Java-Coding to save the generated .jar files.
//	public static File outputDir;

	/// The set of workspaces
	public static ArrayDeque<File> workspaces;

	/// The current source line number.
	public static int sourceLineNumber;
	
	/// The insert name.
	public static String insertName;

	/// The Editor and FEC console
	public static ConsolePanel consolePanel;


	/// Returns a temp file directory.
	/// @param subDir the wanted sub-directory name
	/// @return a temp file directory
	public static File getTempFileDir(String subDir) {
		String tmp = System.getProperty("java.io.tmpdir");
		File tempFileDir = new File(tmp, subDir);
		tempFileDir.mkdirs();
		setAccessRWX(tempFileDir);
		return (tempFileDir);
	}

	/// Utility: Set read-write-execute access on a directory
	/// @param dir the directory
	private static void setAccessRWX(File dir) {
		dir.setReadable(true, false); // Readable for all users
		dir.setWritable(true, false); // Writable for all users
		dir.setExecutable(true, false); // Executable for all users
	}

//	/// Try set Global.outputDir
//	/// @param dir a directory
//	public static void trySetOutputDir(File dir) {
//		dir.mkdirs();
//		if (dir.canWrite())
//			Global.outputDir = dir;
//		else {
//			Global.outputDir = getTempFileDir("simulaEditor/bin");
//		}
//	}

//	/// Initiate Simula properties.
//	public static void initSimulaProperties() {
//		if (sPortProperties == null)
//			loadProperties();
//	}
//
//	/// Returns a Simula property.
//	/// @param key          property key
//	/// @param defaultValue default value
//	/// @return a Simula property
//	public static String getSPortProperty(String key, String defaultValue) {
//		if (sPortProperties == null)
//			loadProperties();
//		return (sPortProperties.getProperty(key, defaultValue));
//	}
//
//	/// Load Simula properties.
//	private static void loadProperties() {
//		String USER_HOME=System.getProperty("user.home");
//		sPortPropertiesFile=new File(USER_HOME,".simula/sPortProperties.xml");			
//		sPortProperties = new Properties();
//		try {
//			sPortProperties.loadFromXML(new FileInputStream(sPortPropertiesFile));
//		} catch (Exception e) {
//			Util.popUpError("Can't load: " + sPortPropertiesFile + "\nGot error: " + e);
////			Thread.dumpStack();
//		}
//		sPortHome = new File(sPortProperties.getProperty("s-port.home"));
//		String version = sPortProperties.getProperty("s-port.version");
//		releaseHome = new File(sPortHome, "/"+version);
//		IO.println("Global.loadProperties: sPortHome="+sPortHome);
//		IO.println("Global.loadProperties: Version="+version);
//		IO.println("Global.loadProperties: releaseHome="+releaseHome);
////		Util.IERR("");
//	}

	// **********************************************************
	// *** S-PORT PROPERTIES
	// **********************************************************

	/// Load SPort properties.
	public static void loadSPortSetupProperties() {
		sPortSetupProperties = new Properties();
		String USER_HOME = System.getProperty("user.home");
		File simulaPropertiesDir = new File(USER_HOME, ".simula");
		File sPortSetupFile = new File(simulaPropertiesDir, "sPortSetup.xml");
		if (sPortSetupFile.exists()) {
			try {
				sPortSetupProperties.loadFromXML(new FileInputStream(sPortSetupFile));
			} catch (Exception e) {
				e.printStackTrace();
				Util.popUpError("Can't load: " + sPortSetupFile + "\nGot error: " + e);
			}
		}
	}

	/// Load SPort Editor properties.
	public static void loadSPortEditorProperties() {
		sPortProperties = new Properties();
		String USER_HOME = System.getProperty("user.home");
		File simulaPropertiesDir = new File(USER_HOME, ".simula");
		sPortPropertiesFile = new File(simulaPropertiesDir, "sPortEditor.xml");
		workspaces = new ArrayDeque<File>();
		if (sPortPropertiesFile.exists()) {
			try {
				Option.getCompilerOptions(sPortProperties);
				sPortProperties.loadFromXML(new FileInputStream(sPortPropertiesFile));
				
				Option.getCompilerOptions(sPortProperties);
				String count = sPortProperties.getProperty("sPort.workspace.count","0");
				IO.println("Global.loadSPortEditorProperties: count="+count);
				int n =  Integer.decode(count).intValue();
				for(int i=0;i<n;i++) {
					String ws = sPortProperties.getProperty("sPort.workspace." + (i+1));
					IO.println("Global.loadSPortEditorProperties: workspace="+ws);
					if(ws != null) {
						File workspace = new File(ws);
						if(workspace.exists()) workspaces.add(new File(ws));
					}
				}
			} catch (Exception e) {
				Util.popUpError("Can't load: " + sPortPropertiesFile + "\nGot error: " + e);
			}
		}
		if (workspaces.isEmpty()) {
			File samples = new File(simdir + "/samples");
			Util.println("Adding default workspace: " + samples);
			workspaces.add(samples);
		}
		currentWorkspace = workspaces.getFirst();
	}

	/// Set current Workspace.
	/// @param workspace the workspace
	public static void setCurrentWorkspace(File workspace) {
		if (!workspace.equals(Global.currentWorkspace)) {
			workspaces.remove(workspace);
			workspaces.addFirst(workspace);
			storeSPortEditorProperties();
		}
	}

	/// Store Workspace and Options properties.
	public static void storeSPortEditorProperties() {
		IO.println("Global.storeSPortEditorProperties: workspaces.size()="+workspaces.size());
		Option.setCompilerOptions(sPortProperties);
		sPortProperties.setProperty("sPort.workspace.count", ""+workspaces.size());
		int i = 1;
		for (File ws : workspaces) {
			sPortProperties.setProperty("sPort.workspace." + (i++), ws.toString());
		}
		Global.currentWorkspace = workspaces.getFirst();
		sPortPropertiesFile.getParentFile().mkdirs();
		try {
			sPortProperties.storeToXML(new FileOutputStream(sPortPropertiesFile), "S-Port Editor Properties");
		} catch (Exception e) {
			Util.IERR();
		}
	}


}
