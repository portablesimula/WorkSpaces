/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.editor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import simula.editor.text.TabbedTextHandler;
import simula.editor.utilities.ConsolePanel;
import simula.editor.utilities.Global;
import simula.editor.utilities.Option;
import simula.editor.utilities.Util;

/// The SimulaEditor.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/editor/SimulaEditor.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class SimulaEditor extends JFrame {
	
	static CardLayout cardLayout;
	
	/// The main CardPanel 
	static JPanel mainCardPanel;
	
	/// The menu bar.
	public static EditorMenues menuBar;

	/// The autoRefresher
	static AutoRefresher autoRefresher;

	/// Available languages.
	public enum Language { /** Simula */Simula,/** Jar file */Jar,/** Text file */Text,/** other */ Other }


	// ****************************************************************
	// *** SimulaEditor: Main Entry for TESTING ONLY
	// ****************************************************************
	/// SimulaEditor: Main Entry for TESTING ONLY.
	/// @param args the arguments
	public static void main(String[] args) {
		Global.packetName="simprog";
		String userDir="C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/Simula";
		Global.simulaRtsLib=new File(userDir,"bin"); // To use Eclipse Project's simula.runtime  Download
		RTOption.InitRuntimeOptions();
		Option.InitCompilerOptions();
		Global.sampleSourceDir=new File(userDir+"/src/simulaTestPrograms/samples");
		Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable e) {
				System.out.print("SimulaEditor.UncaughtExceptionHandler: GOT Exception: " + e);
				e.printStackTrace();
			}});
		Option.internal.INLINE_TESTING=true;
		setUIScale(); // Must be done before any Swing Component is created.
		SimulaEditor editor=new SimulaEditor();
		editor.setVisible(true);
	}

	/// Set UI-Scale factor
	/// See: https://docs.oracle.com/en/java/javase/25/troubleshoot/java-2d-properties.html
	public static void setUIScale() {
     	Global.loadUserSettings();
		IO.println("SimulaEditor.setUIScale: " + Option.editorUIScale);
		if(! Option.editorUIScale.equals("1")) {
			IO.println("SimulaEditor.setUIScale: setProperty(\"sun.java2d.uiScale\", " + Option.editorUIScale + ')');
			System.setProperty("sun.java2d.uiScale", Option.editorUIScale);
		}
	}
           
	/// Create and add a new Tabbed Pane to 'mainCardPanel'
	public static void addTabbedPaneToCard() {
		IO.println("mainCardPanel.add(TabbedTextHandler.tabbedPane)");
        mainCardPanel.add(TabbedTextHandler.tabbedPane,"TabbedPane");
//        cardLayout.next(TabbedTextHandler.tabbedPane);
        cardLayout.next(mainCardPanel);
	}

	/// Create and add a new Tabbed Pane to 'mainCardPanel'
	public static void reopenWelcomePane() {
		IO.println("mainCardPanel.reopenWelcomePane");
		cardLayout.next(mainCardPanel);
	}

	// ****************************************************************
	// *** Constructor
	// ****************************************************************
    /// Create a new SimulaEditor.
    public SimulaEditor() {
		Global.initiate();
        try { setIconImage(Global.favicon.getImage()); } 
        catch (Exception e) {}// Util.IERR("Impossible",e); }
		Global.console=new ConsolePanel();
    	String revision=Global.getSimulaProperty("simula.revision","?");
    	String dated=Global.getSimulaProperty("simula.setup.dated","?");
        String releaseID=Global.simulaReleaseID+'R'+revision;
		Global.simulaVersion="SimulaEditor ("+releaseID+ " built "+dated+" using "+Util.getJavaID()+")";
        Global.console.write(Global.simulaVersion+"\n");
        
        // Set the initial size of the window
        int frameHeight=800;
        int frameWidth=1000;
        setSize(frameWidth, frameHeight);

        // Set the title of the window
        setTitle(Global.simulaVersion);
//     	Global.loadUserSettings();
    	
        // Set the default close operation (exit when it gets closed)
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null); // center the frame on screen

        getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
    	cardLayout = new CardLayout();
    	mainCardPanel = new JPanel(cardLayout);
        getContentPane().add(mainCardPanel);
        
        WelcomePanel welcomePanel = new WelcomePanel();
        mainCardPanel.add(welcomePanel,"Welcome");

        System.err.println("Dette må endres. Auto refresh må skrives på en annewn måte");
        autoRefresher=new AutoRefresher();
        // autoRefresher.start();

        // Set the Menus
        menuBar=new EditorMenues();
        this.setJMenuBar(menuBar);
        this.setVisible(true);
        
		int javaVersion=getJavaSpecVersion();
		if(javaVersion < 25) {
			String msg = "You have installed Java "+System.getProperty("java.version")+'.'  // TODO: CHECK DETTE
					+"\nWe recommend at least Java 25."
					+"\nCheck the settings and consider"
					+"\ninstalling a newer version.\n"
//					+"\nRemember to set Environment Variables:"
//					+"\n    JAVA_HOME, CLASSPATH, PATH\n"
					+"\nDo you want to continue ?\n\n"
				;
			int result=Util.optionDialog(msg,"Java version Notification",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,"Yes","No","Get Java");
			IO.println("result="+result);
			
			if(result == 1) System.exit(0);
			if(result == 2) {
				Desktop desktop = Desktop.getDesktop();
				try {
//					desktop.browse(new URI("https://portablesimula.github.io/github.io/"));
					desktop.browse(new URI("https://dev.java/download/"));
					System.exit(-1); // Stop the Editor
				} catch (Exception ex) {
					msg="Unable to open Desktop Browser\n\n"
							+"Go to your Browser and open page:\n"
//							+" https://portablesimula.github.io/github.io/\n\n"
							+" https://dev.java/download/\n\n"
							+"Do you want to continue ?";
					result=Util.optionDialog(msg,"Update Notification",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,"Yes","No");
					if(result!=0) System.exit(-1); // Stop the Editor
				}
			}
		}

        doCheckForNewVersion();
        doSelectWorkspace();
    }
    
    /// Utility: getJavaVersion
    /// @return the JavaVersion
	private static int getJavaSpecVersion() {
		String ver = System.getProperty("java.vm.specification.version");
		try {
			return (Integer.parseInt(ver));
		} catch (Exception e) {}
		return (0);
	}
    
    // ****************************************************************
    // *** processWindowEvent
    // ****************************************************************
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        	TabbedTextHandler.doExitAction(); 
        }
    }

    // ****************************************************************
    // *** doSelectWorkspace
    // ****************************************************************
    /// Select Workspace dialog.
    public static void doSelectWorkspace() {
    	if (Option.internal.TRACING) Util.println("SimulaEditor.doSelectWorkspace: ");
    	String text="The Simula Editor uses the directory workspace to "
    			   +"\nretrieve Simula source files and save the results"
    	           +"\n"
    			   +"\nThe default workspace 'Samples' contains a set of"
    	           +"\nSimula sample programs ready to be compiled."
    	           +"\nYou may open them by the menu item [File][Open]"
    	           +"\n"
    			   +"\nExecutable .jar files are normally stored in the"
    	           +"\nsubdirectory <CurrentWorkspace>/bin"
                   +"\n";
    	String browse="Browse for another Workspace Directory";
    	Choice workspaceChooser = new Choice();
		for(File workspace:Global.workspaces) workspaceChooser.add(workspace.toString());			
    	workspaceChooser.add(browse);
    	workspaceChooser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
		        String s=workspaceChooser.getItem(workspaceChooser.getSelectedIndex());  
		        if(s.equals(browse)) {
			        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home",Global.currentWorkspace.toString()));
			        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			        int answer = fileChooser.showOpenDialog(null);
			        if (answer == JFileChooser.APPROVE_OPTION) {
			        	String anotherWorkspace=fileChooser.getSelectedFile().toString();
			        	workspaceChooser.remove(browse);  
			        	workspaceChooser.add(anotherWorkspace);
			        	workspaceChooser.add(browse);
			        	workspaceChooser.select(anotherWorkspace);
			        }
		        }		        	
			}});

		Object PanelBackground=UIManager.get("Panel.background");
        UIManager.put("Panel.background", Color.WHITE);
    	JPanel panel=new JPanel();
    	JTextArea textArea=new JTextArea(text);
    	panel.setLayout(new BorderLayout());
    	panel.add(textArea,BorderLayout.NORTH);
    	panel.add(workspaceChooser,BorderLayout.CENTER);
    	panel.add(new JTextArea(""),BorderLayout.SOUTH);
		Util.optionDialog(panel,"Select Simula Workspace",JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE,"OK");
        UIManager.put("Panel.background",PanelBackground);
        String selected=workspaceChooser.getItem(workspaceChooser.getSelectedIndex());  
    	Global.setCurrentWorkspace(new File(selected));
	    Global.trySetOutputDir(new File(Global.currentWorkspace,"bin"));
    }

    // ****************************************************************
    // *** doSelectJavaDir
    // ****************************************************************
    /// Select Java directory dialog.
    static void doSelectJavaDir() {
    	if (Option.internal.TRACING) Util.println("SimulaEditor.doSelectJavaDir: ");
	    File file=new File(Global.currentWorkspace,"java");
	    file.mkdirs();
        JFileChooser fileChooser = new JFileChooser(file);
        fileChooser.setDialogTitle("Select Java Directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int answer = fileChooser.showOpenDialog(null);
        if (answer == JFileChooser.APPROVE_OPTION) {
        	Option.internal.keepJava=fileChooser.getSelectedFile();
        }
    }

    // ****************************************************************
    // *** doSelectOutputDir
    // ****************************************************************
    /// Select Output directory dialog.
    static void doSelectOutputDir() {
    	if (Option.internal.TRACING) Util.println("SimulaEditor.doSelectOutputDir: ");
        JFileChooser fileChooser = new JFileChooser(Global.outputDir);
        fileChooser.setDialogTitle("Select Output Directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int answer = fileChooser.showOpenDialog(null);
        if (answer == JFileChooser.APPROVE_OPTION) {
        	Global.outputDir=fileChooser.getSelectedFile();
        }
    }

    // ****************************************************************
    // *** doSelectExtLibDir
    // ****************************************************************
    /// Select External Search Library dialog.
    static void doSelectExtLibDir() {
    	if (Option.internal.TRACING) Util.println("SimulaEditor.doSelectExtLibDir: ");
    	File prev=Global.extLib;
    	if(prev==null) prev=Global.outputDir.getParentFile();
        JFileChooser fileChooser = new JFileChooser(prev);
        fileChooser.setDialogTitle("Select External Search Library");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int answer = fileChooser.showOpenDialog(null);
        if (answer == JFileChooser.APPROVE_OPTION) {
        	Global.extLib=fileChooser.getSelectedFile();
        }
    }
    
    // ****************************************************************
    // *** doCheckForNewVersion
    // ****************************************************************
    /// Check for new version of the Simula System.
    /// 
    /// The Simula site at GitHub is contacted and the current Simula Release ID is checked against this version.
    /// If not equal question dialog panel is poped up.
    void doCheckForNewVersion() {
    	if (Option.internal.TRACING) Util.println("SimulaEditor.doCheckForNewVersion: ");
        try {
        	String thisRevision=Global.getSimulaProperty("simula.revision","?");
        	String thisSetupDated=Global.getSimulaProperty("simula.setup.dated","?");
	        String thisReleaseID=Global.simulaReleaseID+'R'+thisRevision;

		    String remoteFileName="https://portablesimula.github.io/github.io/setup/setupProperties.xml";
		    if (Option.internal.TRACING) Util.println("SimulaEditor.doCheckForNewVersion: Load Remote Properties from: "+remoteFileName);
		    URL remote = (new URI(remoteFileName)).toURL();
            Properties remoteProperties=new Properties();
            remoteProperties.loadFromXML(remote.openStream());
            String remoteReleaseID=remoteProperties.getProperty("simula.version")
            		          +'R'+remoteProperties.getProperty("simula.revision");
        	String remoteSetupDated=remoteProperties.getProperty("simula.setup.dated","?");

            if(remoteReleaseID.compareTo(thisReleaseID) > 0) {
    			String msg = "   A newer version of Simula is available:\n\n"
				  	       + "   - Installed version: "+thisReleaseID+"\n"
 				  	       + "        Dated: "+thisSetupDated+"\n\n"
    					   + "   - Available version: "+remoteReleaseID+"\n"
 				  	       + "        Dated: "+remoteSetupDated+"\n\n"
    					   
    					   + "   Do you want to download now ?\n";
    			int result=Util.optionDialog(msg,"Update Notification",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,"Yes","No");
    			if(result==0) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI("https://portablesimula.github.io/github.io/"));
					    System.exit(-1); // Stop the Editor
					} catch (Exception ex) {
						msg="Unable to open Desktop Browser\n\n"
						   +"Go to your Browser and open page:\n"
						   +" https://portablesimula.github.io/github.io/\n\n"
						   +"Do you want to continue ?";
		    			result=Util.optionDialog(msg,"Update Notification",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,"Yes","No");
						if(result!=0) System.exit(-1); // Stop the Editor
					}
    			}
            }
        } catch(Exception e) { }
    }
	
	// ****************************************************************
	// *** doRunJarFile
	// ****************************************************************
    /// Run the given .jar file
    /// @param jarFile the file
	public static void doRunJarFile(File jarFile) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String userDir=jarFile.getParentFile().getParent();
				IO.println("SimulaEditor.doRunJarFile: Option.editorUIScale="+Option.editorUIScale);
				if(!Option.editorUIScale.equals("1")) {
					// java -Dsun.java2d.uiScale=2 -jar application.jar
					String uiScaleOption = "-Dsun.java2d.uiScale=" + Option.editorUIScale;
					String[] cmds= {"java", uiScaleOption, "-jar",jarFile.toString(), "-userDir",userDir};
					Util.execute(cmds);					
				} else {
					String[] cmds= {"java","-jar",jarFile.toString(),"-userDir",userDir};
					Util.execute(cmds);
				}
			}
		}).start();
	}
	    
    // ****************************************************************
    // *** AutoRefresher
    // ****************************************************************
	///  Utility class: AutoRefresher
	class AutoRefresher extends Thread {
		/** counter */ int counter=10;
		/** stoped mark */ boolean stoped=false;
		/** Constructor */ public AutoRefresher() { setPriority(MIN_PRIORITY);	}
		/** terminate */ public void terminate() { stoped=true; }
		/** reset */ public void reset() { counter=10; }

		/// Run the AutoRefresher
		@Override
		public void run() {
			while (!stoped) {
				try { sleep(100); } catch (InterruptedException e) {}
				if ((counter--) <= 0) {
					try {
//						if (TabbedTextHandler.currentTextPanel.AUTO_REFRESH
//								&& TabbedTextHandler.currentTextPanel != null
//								&& TabbedTextHandler.currentTextPanel.refreshNeeded) {
//							TabbedTextHandler.currentTextPanel.refreshNeeded = false;
						if (Global.currentModule.AUTO_REFRESH
								&& Global.currentModule != null
								&& Global.currentModule.refreshNeeded) {
							Global.currentModule.refreshNeeded = false;
							menuBar.refresh.doClick();
						}
					} catch (Throwable t) {}
				}
			}
		}
	}

}