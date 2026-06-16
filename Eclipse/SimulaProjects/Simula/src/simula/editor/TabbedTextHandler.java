package simula.editor;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simula.compiler.SourceModule;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.editor.SimulaEditor.Language;
import simula.psi.PsiTree;

/// @author Google AI
/// @author Øystein Myhre Andersen
public class TabbedTextHandler {
    
	/// The tabbed pane.
	static JTabbedPane tabbedPane;

//	/// The current SourceTextPanel
//	static SourceTextPanel currentTextPanel;
//	static JPanel currentTextPanel;

	/// The current SourceModule
//	static SourceModuleTextPanel currentTextPanel;
//	static JPanel currentTextPanel;
    
    // ****************************************************************
    // *** setSelectedTabTitle  /  removeSelectedTab
    // ****************************************************************
    /// Set selected tab's title
    /// @param title the new title
    static void setSelectedTabTitle(String title) { tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),title); }
    
    /// Remove selected tab.
    static void removeSelectedTab() {
		
		Util.IERR("SJEKK DETTE");
		Util.STOP();
    	tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
//        currentTextPanel=(SourceTextPanel)tabbedPane.getSelectedComponent();
    	int index = tabbedPane.getSelectedIndex();
        String tabName = tabbedPane.getTitleAt(index);
        SourceModule current = Global.moduleMap.get(tabName);
        Global.currentModule = current;
        SimulaEditor.menuBar.updateMenuItems();
    }
    
    // ****************************************************************
    // *** doNewTabbedPanel
    // ****************************************************************
    /// Create a new Tab with text generated from the given file.
    /// @param file the file
    /// @param lang the language
    static void doNewTabbedPanel(File file, String prefix) {
    	if(tabbedPane == null) doOpenTabbedPane();
    	SwingUtilities.invokeLater(() -> {
    		SourceModule currentModule = Global.currentModule;
    		SimulaEditor.Language lang = currentModule.lang;
    		SourceTextPanel currentTextPanel = new SourceTextPanel(currentModule, SimulaEditor.menuBar.popupMenu);
    		currentModule.setTextPanel(currentTextPanel);
    		String tabName = prefix + Global.currentModule.getTabName();

    		tabbedPane.addTab(null, currentTextPanel); // Add content first, will be replaced
    		int index = tabbedPane.getTabCount() - 1;
    		tabbedPane.setTabComponentAt(index, new ClosableTabPanel("ZZ_"+tabName, tabbedPane, currentTextPanel));
    		tabbedPane.setSelectedIndex(index);
    		
    		currentModule.fileChanged=false;
    		if(file == null) {
    			currentTextPanel.fillTextPane(new StringReader(SourceModule.emptyProgram),0);
    		} else {
    			switch(lang) {
					case Simula:
	    				try { Reader reader=new InputStreamReader(new FileInputStream(file),Global._CHARSET);
	    				currentTextPanel.fillTextPane(reader,0);
	    				} catch(IOException e) { Util.IERR("Impossible",e); }
						break;
					case Jar:
	    				currentTextPanel.fillTextPane(getJarFileReader(file),0);
						break;
					case Text:
	    				try { Reader reader=new InputStreamReader(new FileInputStream(file),Global._CHARSET);
	    				currentTextPanel.fillTextPane(reader,0);
	    				} catch(IOException e) { Util.IERR("Impossible",e); }
						break;
					case Other:
					default:
	    				currentTextPanel.fillTextPane(getHexFileReader(file),0);
    			}
    		}
    		SimulaEditor.menuBar.updateMenuItems();
    	});
    }

    // ****************************************************************
    // *** doNewTabbedPsiPanel
    // ****************************************************************
    /// Create a new Tab with text generated from the given psi tree.
    /// @param file the file
    /// @param lang the language
    static void doNewTabbedPsiPanel(PsiTree psiTree, String prefix) {
    	if(tabbedPane == null) doOpenTabbedPane();
    	SwingUtilities.invokeLater(() -> {
    		PsiTextPanel psiTextPanel=new PsiTextPanel(Global.currentModule, SimulaEditor.menuBar.popupMenu);
    		String tabName = prefix + Global.currentModule.getTabName();

			tabbedPane.addTab(null, psiTextPanel); // Add content first, will be replaced
			int index = tabbedPane.getTabCount() - 1;
			tabbedPane.setTabComponentAt(index, new ClosableTabPanel("WW_"+tabName, tabbedPane, psiTextPanel));
			tabbedPane.setSelectedIndex(index);

    		Global.currentModule.fileChanged=false;
    		psiTextPanel.fillTextPane(0, psiTree);
    		SimulaEditor.menuBar.updateMenuItems();
    	});
    }

    
    /// Utility: Get HexFile Reader.
    /// @param file the file to read
    /// @return The resulting reader
    private static Reader getHexFileReader(File file) {
    	StringBuilder sb=new StringBuilder();
    	String hexPart="",charPart="";
    	FileInputStream inpt;
    	try { inpt=new FileInputStream(file);
    		int b;
    		while((b=inpt.read()) != -1) {
    			hexPart=hexPart+' '+AppendLeadingZeroes(Integer.toHexString(b),2);
    			charPart=charPart+((b>31 && b<128)?((char)b):'.');
    			if((charPart.length())>15) {
    				sb.append("  "+hexPart+"  "+charPart+"\n");
    				hexPart=""; charPart="";
    			}
    		}
    		if((charPart.length())>0) {
    	    	while(hexPart.length()<(16*3)) hexPart=hexPart+" ";
				sb.append("  "+hexPart+"  "+charPart+"\n");    			
    		}
    	} catch(IOException e) { Util.IERR("Impossible",e); }
    	return(new StringReader(sb.toString()));
    }
    
    /// Utility: Append leading zeroes.
    /// @param s the input string
    /// @param n the expected length
    /// @return the resulting string
    private static String AppendLeadingZeroes(String s,int n) {
    	while(s.length()<n) s="0"+s;
    	return(s.toUpperCase());
    }
   
    /// Get .jar file Reader
    /// @param file the file
    /// @return a .jar file Reader
    private static Reader getJarFileReader(File file) {
    	StringBuilder sb=new StringBuilder();
    	sb.append("File: "+file).append("\n");
    	if(!(file.exists() && file.canRead())) {
    		sb.append("Can't read .jar file: "+file).append("\n");
    	} else {
    		JarFile jarFile=null;
    		try {
    			jarFile=new JarFile(file);
    			Manifest manifest=jarFile.getManifest();
    			Attributes mainAttributes=manifest.getMainAttributes();
    			Set<Object> keys=mainAttributes.keySet();
    			for(Object key:keys) {
    				String val=mainAttributes.getValue(key.toString());
    				sb.append(key.toString()+"=\""+val+"\"").append("\n");
    			}

    			Enumeration<JarEntry> entries=jarFile.entries();
    			while(entries.hasMoreElements()) {
    				JarEntry entry=entries.nextElement();
    				String size=""+entry.getSize();
    				while(size.length()<6) size=" "+size;
    				FileTime fileTime=entry.getLastModifiedTime();
    				String date = DateTimeFormatter.ofPattern("uuuu-MMM-dd HH:mm:ss", Locale.getDefault())
    						.withZone(ZoneId.systemDefault()).format(fileTime.toInstant());
    				sb.append("Jar-Entry: "+size+"  "+date+"  \""+entry+"\"").append("\n");
    			}
    		} catch(IOException e) {
    			Util.IERR("Caused by:",e);
    		} finally {
    			if(jarFile!=null)
    				try { jarFile.close(); } catch (IOException e) { e.printStackTrace(); }
    		}
    	}
    	return(new StringReader(sb.toString()));
    }

//	// ****************************************************************
//	// *** MouseListener
//	// ****************************************************************
//	/// The MouseListener.
//    private static MouseListener mouseListener = new MouseListener() {
//		public void mousePressed(MouseEvent e) {}
//		public void mouseReleased(MouseEvent e) {}
//		public void mouseEntered(MouseEvent e) {}
//		public void mouseExited(MouseEvent e) {}
//		public void mouseClicked(MouseEvent e) {
//    	    if(e.getButton()==3) SimulaEditor.menuBar.popupMenu.show(TabbedTextHandler.tabbedPane,e.getX(),e.getY());
//    	}
//    };
	
    // ****************************************************************
    // *** doOpenTabbedPane
    // ****************************************************************
	/// Open file action
	public static void doOpenTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent e) {
        	    if(e.getButton()==3) SimulaEditor.menuBar.popupMenu.show(TabbedTextHandler.tabbedPane,e.getX(),e.getY());
        	}        	
        });
        tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Component selected=tabbedPane.getSelectedComponent();
				IO.println("SimulaEditor'changeSelectedComponent: " + selected);
				if(selected == null) {
					SimulaEditor.reopenWelcomePane();
					return;
				}

				IO.println("SimulaEditor'changeSelectedComponent: " + selected.getClass());
				Global.console.write("SimulaEditor'changeSelectedComponent: " + selected.getClass()+"\n");
//				if(selected instanceof PsiTextPanel panel) {
//					currentTextPanel=panel;
//					Global.currentModule = currentTextPanel.currentModule;
//					SimulaEditor.menuBar.updateMenuItems();
//				} else
//				if(selected instanceof SourceTextPanel panel) {
//					currentTextPanel=panel;
//					Global.currentModule = currentTextPanel.currentModule;
//					SimulaEditor.menuBar.updateMenuItems();
//				}
				if(selected instanceof TabTextPanel panel) {
				Global.currentModule = panel.sourceModule;
				SimulaEditor.menuBar.updateMenuItems();
			}
				Util.IERR("SJEKK DETTE");
//				Util.STOP();
			}});
        SimulaEditor.addTabbedPaneToCard();
	}
		
    // ****************************************************************
    // *** doOpenFileAction
    // ****************************************************************
	/// Open file action
	public static void doNewFileAction() {
		new SourceModule(null);
		Global.currentModule.lang = SimulaEditor.Language.Simula;
		TabbedTextHandler.doNewTabbedPanel(null, "");		
	}

	// ****************************************************************
	// *** doOpenFileAction
	// ****************************************************************
	/// Open file action
	public static void doOpenFileAction() {
//		if(tabbedPane == null) doOpenTabbedPane();
        JFileChooser fileChooser = new JFileChooser(Global.currentWorkspace);
        if (fileChooser.showOpenDialog(tabbedPane)==JFileChooser.APPROVE_OPTION) {
        	File file=fileChooser.getSelectedFile();
        	
        	doOpenFile(file);
        }
	}
	
    // ****************************************************************
    // *** doOpenFileAction
    // ****************************************************************
	/// Open file action
	public static void doOpenFile(File file) {
		if(tabbedPane == null) doOpenTabbedPane();
		if(!file.exists()) { Util.popUpError("Can't open file\n"+file); return; }
		SourceModule currentModule = new SourceModule(file);
    	switch(Global.currentModule.lang){
		case Simula:
//			SimulaEditor.doNewTabbedPanel(file,SimulaEditor.Language.Simula);
//			SourceModule currentModule = Global.currentModule;
//			SourceModule currentModule = new SourceModule(file, SimulaEditor.Language.Simula); 
			currentModule.buildPsiAndSyntaxTrees();
			PsiTree psiTree = currentModule.getPsiTree();
			doNewTabbedPsiPanel(psiTree, "");
//        	Global.setCurrentWorkspace(fileChooser.getCurrentDirectory());
			break;
		case Jar:
			IO.println("EditorMenues.doOpenFileAction: "+file);
			int res = Util.optionDialog("Executable Jarfile\nDo you want to execute ?",
					"Execute or List Jarfile", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "Yes", "No");
			if (res == JOptionPane.YES_OPTION) {
				SimulaEditor.doRunJarFile(file);
				break;
			}
		default:
			doNewTabbedPanel(file, "");
			break;
    	}
	}
	
    // ****************************************************************
    // *** doSaveCurrentFile
    // ****************************************************************
	/// Do save current source file.
	/// @param saveAs true if a file chooser is wanted
	static void doSaveCurrentFile(boolean saveAs) {
//		SourceTextPanel current=currentTextPanel;
		SourceModule currentModule = Global.currentModule;
		
		Util.IERR("SJEKK DETTE");
		Util.STOP();
		if(saveAs || currentModule.sourceFile==null) {
	        JFileChooser fileChooser = new JFileChooser(Global.currentWorkspace);
	        if (fileChooser.showSaveDialog(tabbedPane)!=JFileChooser.APPROVE_OPTION) return; // Do Nothing
	        File file=fileChooser.getSelectedFile();
	        Global.setCurrentWorkspace(fileChooser.getCurrentDirectory());
	        if(file.exists() && overwriteDialog(file)!=JOptionPane.YES_OPTION) return; // Do Nothing
	        if(!file.getName().toLowerCase().endsWith(".sim")) {
	        	if(noSimTypeDialog(file)!=JOptionPane.OK_OPTION) return; // Do Nothing
	        }
	        currentModule.sourceFile=file;
	        setSelectedTabTitle(file.getName());
	        currentModule.fileChanged=true;
		}
    	if(Global.currentModule.fileChanged) try {
    		Writer writer=new OutputStreamWriter(new FileOutputStream(currentModule.sourceFile.getPath()),Global._CHARSET);
    		BufferedWriter out = new BufferedWriter(writer);
//    		String text=current.editTextPane.getText();
    		String text=currentModule.getUpdatedText();
    		out.write(text); out.close();
    		Global.currentModule.fileChanged = false;
    	} catch (Exception e) { Util.IERR("Internal Error: "+e.getMessage()); }
    }
	
    // ****************************************************************
    // *** doCloseCurrentFileAction
    // ****************************************************************
	/// Close current file acation.
	static void doCloseCurrentFileAction() {
			maybeSaveCurrentFile();
			removeSelectedTab();
	}
	
    // ****************************************************************
    // *** doCloseAllAction
    // ****************************************************************
	/// Close action.
	static void doCloseAllAction() {
		if(tabbedPane != null) {
			while(tabbedPane.getSelectedIndex()>=0)
			    doCloseCurrentFileAction();
		}
	}
	
    // ****************************************************************
    // *** doExitAction
    // ****************************************************************
	/// Exit action.
	static void doExitAction() {
		doCloseAllAction();
		System.exit(0);
	}

    // ****************************************************************
    // *** maybeSaveCurrentFile
    // ****************************************************************
	/// Maybe save current source file.
	/// 
	/// Also used by RunMeny.
	static void maybeSaveCurrentFile() {
//		SourceTextPanel current=currentTextPanel;
//		if(current==null) return; if(!current.fileChanged) return;
		
		Util.IERR("SJEKK DETTE");
		Util.STOP();
		SourceModule current=Global.currentModule;
		if(current==null) return; if(!current.fileChanged) return;
		if(saveDialog(current.sourceFile)==JOptionPane.YES_OPTION) doSaveCurrentFile(false);
	}

	/// Popup a warning: The file: 'name' Already exists - Do you want to overwrite it ?
	/// @param file the file
	/// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
	private static int overwriteDialog(File file) {
 		String msg="The file: \n"+file+"\nAlready exists - Do you want to overwrite it ?";
 		return(Util.optionDialog(msg,"Warning",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,"Yes","No"));
	}

	/// Popup a warning: The file 'name' Does not end with the recomended .sim
	/// @param file the file
	/// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
	private static int noSimTypeDialog(File file) {
        String msg="The file name\n"+file+"\nDoes not end with the recomended \".sim\"";
		return(Util.optionDialog(msg,"Warning",JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,"Ok","Cancel"));
	}
	
	/// Save file dialog
	/// @param file the file
	/// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
	private static int saveDialog(File file) {
		String msg=(file==null)?"The source text has unsaved changes.\nDo you want to save it in a file ?"
		                       :"The file: \n"+file+"\nHas changed - do you want to save it ?";
		return(Util.optionDialog(msg,"Question",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,"Yes","No"));
	}

}
