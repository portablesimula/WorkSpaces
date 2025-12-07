/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package sim.editor;

import static sim.compiler.Global.sCodeFileName;
import static sim.compiler.Global.sourceFileName;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

import sim.compiler.Global;
import sim.compiler.Option;
import sim.compiler.CommonBEC;
import sim.compiler.SimulaFEC;
import sim.compiler.Util;

/// The editor's menues.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/EclipseWorkSpaces/blob/main/SimulaCompiler2/Simula/src/simula/editor/EditorMenues.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class EditorMenues extends JMenuBar {
	
	/// Create a Menu item with toolTip.
	/// @param ident item identifier
	/// @param toolTipText the tooltip text
	/// @return the resulting MenuItem
	private JMenuItem defJMenuItem(String ident,String toolTipText) {
		JMenuItem item = new JMenuItem(ident);
		if(toolTipText!=null) item.setToolTipText(toolTipText);
		return(item);
	}
    
    /** Menu */ private JMenu fileMenu=new JMenu("File");
    /** Menu item */ private JMenuItem newFile = defJMenuItem("New","Will open a new Simula file for editing");
    /** Menu item */ private JMenuItem openFile = new JMenuItem("Open");
    /** Menu item */ private JMenuItem saveFile = new JMenuItem("Save");
    /** Menu item */ private JMenuItem saveAs = new JMenuItem("Save As...");
    /** Menu item */ private JMenuItem close = new JMenuItem("Close");
    /** Menu item */ private JMenuItem closeAll = new JMenuItem("Close All");
    /** Menu item */ private JMenuItem exit = new JMenuItem("Exit");
    
    /** Menu */ private JMenu editMenu=new JMenu("Edit");
    
    /// Refresh item.
 	JMenuItem refresh=new JMenuItem("Refresh");
	/** Menu item */ private JMenuItem cut=new JMenuItem(new DefaultEditorKit.CutAction());
	/** Menu item */ private JMenuItem copy=new JMenuItem(new DefaultEditorKit.CopyAction());
	/** Menu item */ private JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
	/** Menu item */ private JMenuItem undo=new JMenuItem("Undo");
	///** Menu item */ private JMenuItem redo=new JMenuItem("Redo");
    
    /** Menu */ private JMenu runMenu=new JMenu("Run");
    /** Menu item */ private JMenuItem run = new JMenuItem("Run");
    
    /** Menu */ private JMenu settings=new JMenu("Settings");
	/** CheckBox */ private JCheckBox autoRefresh=new JCheckBox("AutoRefresh");
    /** Menu item */ private JMenuItem setWorkSpace = new JMenuItem("Select WorkSpace");
    /** Menu item */ private JMenuItem workSpaces = new JMenuItem("Remove WorkSpaces");
    /** Menu item */ private JMenuItem EDTOption = new JMenuItem("Options");

    /** Menu */ private JMenu helpMenu=new JMenu("Help");
    /** Menu item */ private JMenuItem about = new JMenuItem("About S-Port Simula");
    /** Menu item */ private JMenuItem more = new JMenuItem("More Info");
    
    /// The popup menu.
    JPopupMenu popupMenu;
    /** Popup Menu item */ private JMenuItem newFile2 = new JMenuItem("New");
    /** Popup Menu item */ private JMenuItem openFile2 = new JMenuItem("Open");
    /** Popup Menu item */ private JMenuItem saveFile2 = new JMenuItem("Save");
    /** Popup Menu item */ private JMenuItem saveAs2 = new JMenuItem("Save As...");
    /** Popup Menu item */ private JMenuItem close2 = new JMenuItem("Close");
    /** Popup Menu item */ private JMenuItem closeAll2 = new JMenuItem("Close All");
    /** Popup Menu item */ private JMenuItem exit2 = new JMenuItem("Exit");
	/** Popup Menu item */ private JMenuItem refresh2=new JMenuItem("Refresh");
	/** Popup Menu item */ private JMenuItem cut2=new JMenuItem(new DefaultEditorKit.CutAction());
	/** Popup Menu item */ private JMenuItem copy2=new JMenuItem(new DefaultEditorKit.CopyAction());
	/** Popup Menu item */ private JMenuItem paste2=new JMenuItem(new DefaultEditorKit.PasteAction());
	/** Popup Menu item */ private JMenuItem undo2=new JMenuItem("Undo");
	///** Popup Menu item */ private JMenuItem redo2=new JMenuItem("Redo");
    /** Popup Menu item */ private JMenuItem run2 = new JMenuItem("Run");
	/** Popup Menu item */ private JCheckBox autoRefresh2=new JCheckBox("AutoRefresh");
    /** Popup Menu item */ private JMenuItem setWorkSpace2 = new JMenuItem("Select WorkSpace");
    /** Popup Menu item */ private JMenuItem workSpaces2 = new JMenuItem("Remove WorkSpaces");
    /** Popup Menu item */ private JMenuItem EDTOption2 = new JMenuItem("Options");
    /** Popup Menu item */ private JMenuItem about2 = new JMenuItem("About S-Port Simula");
    /** Popup Menu item */ private JMenuItem more2 = new JMenuItem("More Info");

	
	// ****************************************************************
	// *** Constructor
	// ****************************************************************
    /// Create a new instance of EditorMenues.
 	EditorMenues() {
    	fileMenu.add(newFile); newFile.addActionListener(actionListener);
    	fileMenu.add(openFile); openFile.addActionListener(actionListener);
    	fileMenu.addSeparator();
    	fileMenu.add(saveFile); saveFile.setEnabled(false); saveFile.addActionListener(actionListener);
    	fileMenu.add(saveAs); saveAs.setEnabled(false); saveAs.addActionListener(actionListener);
    	fileMenu.addSeparator();
    	fileMenu.add(close); close.setEnabled(false); close.addActionListener(actionListener);
    	fileMenu.addSeparator();
    	fileMenu.add(closeAll); closeAll.setEnabled(false); closeAll.addActionListener(actionListener);
    	fileMenu.addSeparator();
    	fileMenu.add(exit); exit.addActionListener(actionListener);
		this.add(fileMenu);
		editMenu.add(undo); undo.setEnabled(false); undo.addActionListener(actionListener);
		//editMenu.add(redo); redo.setEnabled(false); redo.addActionListener(actionListener);
		editMenu.addSeparator();
		editMenu.add(cut); cut.setEnabled(false); cut.setText("Cut");     
		editMenu.add(copy); copy.setEnabled(false); copy.setText("Copy");   
		editMenu.add(paste); paste.setEnabled(false); paste.setText("Paste"); 
        editMenu.addSeparator();
        editMenu.add(refresh); refresh.setEnabled(false); refresh.addActionListener(actionListener);
		this.add(editMenu);
		runMenu.add(run); run.setEnabled(false); run.addActionListener(actionListener);
		this.add(runMenu);
		settings.add(autoRefresh); autoRefresh.setEnabled(false); autoRefresh.addActionListener(actionListener);
        settings.add(setWorkSpace); setWorkSpace.addActionListener(actionListener);
        settings.add(workSpaces); workSpaces.addActionListener(actionListener);
        settings.addSeparator();
        settings.add(EDTOption); EDTOption.addActionListener(actionListener);
		this.add(settings);
		helpMenu.add(about); about.addActionListener(actionListener);
		helpMenu.add(more); more.addActionListener(actionListener);
		this.add(helpMenu);
		
	    addPopupMenuItems();
	    setAccelerators();
	}

	// ****************************************************************
	// *** HelpMenu: setAccelerators
	// ****************************************************************
	/// HelpMenu: setAccelerators
	private void setAccelerators() {
		newFile.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
	    openFile.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
		saveFile.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
	    close.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
		//closeAll.setAccelerator(KeyStroke.getKeyStroke('W', (InputEvent.CTRL_DOWN_MASK)|InputEvent.SHIFT_DOWN_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_DOWN_MASK));
		refresh.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
		undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
		//redo.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
	    run.setAccelerator(KeyStroke.getKeyStroke('B', InputEvent.CTRL_DOWN_MASK));
	    about.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_DOWN_MASK));
		newFile2.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
	    openFile2.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
		saveFile2.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
	    close2.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
		//closeAll2.setAccelerator(KeyStroke.getKeyStroke('W', (InputEvent.CTRL_DOWN_MASK)|InputEvent.SHIFT_DOWN_MASK));
		cut2.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK));
		copy2.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
		paste2.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_DOWN_MASK));
		refresh2.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
		undo2.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
		//redo2.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
	    run2.setAccelerator(KeyStroke.getKeyStroke('B', InputEvent.CTRL_DOWN_MASK));
	    about2.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_DOWN_MASK));
	}
	
	// ****************************************************************
	// *** HelpMenu: addPopupMenuItems
	// ****************************************************************
	/// Add popup menu items,
	private void addPopupMenuItems() {
	    popupMenu=new JPopupMenu();
        popupMenu.add(newFile2); newFile2.addActionListener(actionListener);
		popupMenu.add(openFile2); openFile2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(run2); run2.setEnabled(false); run2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(saveFile2); saveFile2.setEnabled(false); saveFile2.addActionListener(actionListener);
        popupMenu.add(saveAs2); saveAs2.setEnabled(false); saveAs2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(close2); close2.setEnabled(false); close2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(closeAll2); closeAll2.setEnabled(false); closeAll2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(exit2); exit2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(undo2); undo2.setEnabled(false); undo2.addActionListener(actionListener);
        //popupMenu.add(redo2); redo2.setEnabled(false); redo2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(cut2); cut2.setEnabled(false); cut2.setText("Cut");
        popupMenu.add(copy2); copy2.setEnabled(false); copy2.setText("Copy");
        popupMenu.add(paste2); paste2.setEnabled(false); paste2.setText("Paste");
        popupMenu.addSeparator();
        popupMenu.add(refresh2); refresh2.setEnabled(false); refresh2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(autoRefresh2); autoRefresh2.setEnabled(false); autoRefresh2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(setWorkSpace2); setWorkSpace2.addActionListener(actionListener);
        popupMenu.add(workSpaces2); workSpaces2.addActionListener(actionListener);
        popupMenu.add(EDTOption2); EDTOption2.addActionListener(actionListener);
        popupMenu.addSeparator();
        popupMenu.add(about2); about2.addActionListener(actionListener);
        popupMenu.add(more2); more2.addActionListener(actionListener);
	}
	
	// ****************************************************************
	// *** EditMenu: UpdateMenuItems
	// ****************************************************************
	/// Update menu items.
	void updateMenuItems() {
		SourceTextPanel current=SPortEditor.current;
		boolean source=false;
		boolean text=false;
		boolean mayRun=false;
		boolean fileChanged=false;
		boolean auto=false;
		boolean canUndo=false;
		if(current!=null) {
			source=true;
			String editText=current.editTextPane.getText();
			if(editText!=null && editText.trim().length()!=0) text=true; 
			if(current.lang==SPortEditor.Language.Simula && text) mayRun=true;
			if(current.lang==SPortEditor.Language.Simula && editText!=null && editText.trim().length()!=0) text=true; 
			fileChanged=current.fileChanged;
			auto=source && current.AUTO_REFRESH;
			UndoManager undoManager = current.getUndoManager();
			canUndo=undoManager.canUndo();
		}
		saveFile.setEnabled(fileChanged); saveFile2.setEnabled(fileChanged);
		saveAs.setEnabled(mayRun);        saveAs2.setEnabled(mayRun);
		close.setEnabled(source);         close2.setEnabled(source);
		closeAll.setEnabled(source);      closeAll2.setEnabled(source);
		cut.setEnabled(text);             cut2.setEnabled(text);
		copy.setEnabled(text);            copy2.setEnabled(text); 
		paste.setEnabled(source);         paste2.setEnabled(source);
//		search.setEnabled(text);          search2.setEnabled(text);
		refresh.setEnabled(text);         refresh2.setEnabled(text);
		run.setEnabled(mayRun);           run2.setEnabled(mayRun);
		autoRefresh.setSelected(auto);    autoRefresh2.setSelected(auto);
		autoRefresh.setEnabled(source);   autoRefresh2.setEnabled(source);
		undo.setEnabled(canUndo);         undo2.setEnabled(canUndo);
//		redo.setEnabled(canRedo);         redo2.setEnabled(canRedo);
		SPortEditor.autoRefresher.reset();
	}	
	
	// ****************************************************************
	// *** HelpMenu: ActionListener
	// ****************************************************************
	/// the ActionListener
	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Object item=e.getSource();
			SourceTextPanel current=SPortEditor.current;
			if(item==newFile || item==newFile2) SPortEditor.doNewTabbedPanel(null,SPortEditor.Language.Simula);
			else if(item==openFile || item==openFile2) doOpenFileAction();
			else if(item==saveFile || item==saveFile2) doSaveCurrentFile(false);
			else if(item==saveAs   || item==saveAs2) doSaveCurrentFile(true);
			else if(item==close    || item==close2) doCloseCurrentFileAction();
			else if(item==closeAll || item==closeAll2) doCloseAllAction();
			else if(item==exit     || item==exit2) doExitAction();
			else if(item==undo || item==undo2) undoAction();
//			else if(item==redo || item==redo2) redoAction();
			else if(item==refresh || item==refresh2) current.doRefresh();
			else if(item==run   || item==run2) doRunAction();
			else if(item==autoRefresh) current.AUTO_REFRESH=autoRefresh.isSelected();
			else if(item==autoRefresh2) current.AUTO_REFRESH=autoRefresh2.isSelected();
			else if(item==setWorkSpace || item==setWorkSpace2) selectWorkspaceAction();
			else if(item==workSpaces || item==workSpaces2) removeWorkspacesAction();
			else if(item==EDTOption || item==EDTOption2) Option.selectCompilerOptions();
			else if(item==about || item==about2) doAboutAction();
			else if(item==more || item==more2) doMoreAction();
		}
	};
	
    // ****************************************************************
    // *** doOpenFileAction
    // ****************************************************************
	/// Open file action
	private void doOpenFileAction() {
        JFileChooser fileChooser = new JFileChooser(Global.currentWorkspace);
        if (fileChooser.showOpenDialog(SPortEditor.tabbedPane)==JFileChooser.APPROVE_OPTION) {
        	File file=fileChooser.getSelectedFile();
    		if(!file.exists()) { Util.popUpError("Can't open file\n"+file); return; }
    		String lowName=file.getName().toLowerCase();
    		if(lowName.endsWith(".sim")) {
    			SPortEditor.doNewTabbedPanel(file,SPortEditor.Language.Simula);
            	Global.setCurrentWorkspace(fileChooser.getCurrentDirectory());
    		} else if(isTextFile(lowName)) SPortEditor.doNewTabbedPanel(file,SPortEditor.Language.Text);
    		else SPortEditor.doNewTabbedPanel(file,SPortEditor.Language.Other);
    		
        }
	}
	
	/// Test if a file is a text file
	/// @param lowName the ident after .
	/// @return true if it is a text file
	private boolean isTextFile(String lowName) {
		String[] kind= {".java", ".txt", ".bat", ".sh", ".md", ".html", ".xml" }; // TODO: More ?
		for(String k:kind) if(lowName.endsWith(k)) return(true);
		return(false);
	}
	
    // ****************************************************************
    // *** doSaveCurrentFile
    // ****************************************************************
	/// Do save current source file.
	/// @param saveAs true if a file chooser is wanted
	void doSaveCurrentFile(boolean saveAs) {
		SourceTextPanel current=SPortEditor.current;
		if(saveAs || current.sourceFile==null) {
	        JFileChooser fileChooser = new JFileChooser(Global.currentWorkspace);
	        if (fileChooser.showSaveDialog(SPortEditor.tabbedPane)!=JFileChooser.APPROVE_OPTION) return; // Do Nothing
	        File file=fileChooser.getSelectedFile();
	        Global.setCurrentWorkspace(fileChooser.getCurrentDirectory());
	        if(file.exists() && overwriteDialog(file)!=JOptionPane.YES_OPTION) return; // Do Nothing
	        if(!file.getName().toLowerCase().endsWith(".sim")) {
	        	if(noSimTypeDialog(file)!=JOptionPane.OK_OPTION) return; // Do Nothing
	        }
	        current.sourceFile=file;
	        SPortEditor.setSelectedTabTitle(file.getName());
	        current.fileChanged=true;
		}
    	if(current.fileChanged)	try {
    		Writer writer=new OutputStreamWriter(new FileOutputStream(current.sourceFile.getPath()));
    		BufferedWriter out = new BufferedWriter(writer);
    		String text=current.editTextPane.getText();
    		out.write(text); out.close();
    		current.fileChanged = false;
    	} catch (Exception e) { Util.IERR(e.getMessage()); }
    }
	
    // ****************************************************************
    // *** doCloseCurrentFileAction
    // ****************************************************************
	/// Close current file acation.
	private void doCloseCurrentFileAction() {
			maybeSaveCurrentFile();
			SPortEditor.removeSelectedTab();
	}
	
    // ****************************************************************
    // *** doCloseAllAction
    // ****************************************************************
	/// Close action.
	void doCloseAllAction() {
		while(SPortEditor.tabbedPane.getSelectedIndex()>=0)
		    doCloseCurrentFileAction();
	}
	
    // ****************************************************************
    // *** doExitAction
    // ****************************************************************
	/// Exit action.
	void doExitAction() {
		doCloseAllAction();
		IO.println("EditorMenues.doExitAction - Exit: ");
		System.exit(0);
	}

    // ****************************************************************
    // *** maybeSaveCurrentFile
    // ****************************************************************
	/// Maybe save current source file.
	/// 
	/// Also used by RunMeny.
	void maybeSaveCurrentFile() {
		SourceTextPanel current=SPortEditor.current;
		if(current==null) return; if(!current.fileChanged) return;
		if(saveDialog(current.sourceFile)==JOptionPane.YES_OPTION) doSaveCurrentFile(false);
	}

	/// Popup a warning: The file: 'name' Already exists - Do you want to overwrite it ?
	/// @param file the file
	/// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
	private int overwriteDialog(File file) {
 		String msg="The file: \n"+file+"\nAlready exists - Do you want to overwrite it ?";
 		return(Util.optionDialog(msg,"Warning",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,"Yes","No"));
	}

	/// Popup a warning: The file 'name' Does not end with the recomended .sim
	/// @param file the file
	/// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
	private int noSimTypeDialog(File file) {
        String msg="The file name\n"+file+"\nDoes not end with the recomended \".sim\"";
		return(Util.optionDialog(msg,"Warning",JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,"Ok","Cancel"));
	}
	
	/// Save file dialog
	/// @param file the file
	/// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
	private int saveDialog(File file) {
		String msg=(file==null)?"The source text has unsaved changes.\nDo you want to save it in a file ?"
		                       :"The file: \n"+file+"\nHas changed - do you want to save it ?";
		return(Util.optionDialog(msg,"Question",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,"Yes","No"));
	}
	
	// ****************************************************************
	// *** undoAction
	// ****************************************************************
	/// The undo action
	private void undoAction() {
		SourceTextPanel current=SPortEditor.current;
		current.getUndoManager().undo();
		current.fileChanged=true; current.refreshNeeded=true;
		updateMenuItems();
	}
	
//	// ****************************************************************
//	// *** redoAction
//	// ****************************************************************
//	private void redoAction() {
//		SourceTextPanel current=SPortEditor.current;
//		current.getUndoManager().redo();
//		current.fileChanged=true; current.refreshNeeded=true;
//		updateMenuItems();
//	}
	
	// ****************************************************************
	// *** doRunAction
	// ****************************************************************
	/// The run action
	private void doRunAction() {
//		Option.internal.DEBUGGING=false;
		doStartRunning();
	}
	
	// ****************************************************************
	// *** doStartRunning
	// ****************************************************************
	/// Utility: Start running current Simula program.
	private void doStartRunning() {
		maybeSaveCurrentFile();
       	File file=SPortEditor.current.sourceFile;
		if(file==null) {
			file=new File(Global.getTempFileDir("simula/tmp/"),"unnamed.sim");
			file.getParentFile().mkdirs();
		}
		try {
			Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread thread, Throwable e) {
					System.out.print("EditorMenues.UncaughtExceptionHandler: GOT Exception: " + e);
					e.printStackTrace();
			}});
			// Start compiler ....
			Util.ASSERT(SPortEditor.current!=null,"EditorMenues.doRunAction: Invariant-1");
			String text=SPortEditor.current.editTextPane.getText();
//			IO.println("EditorMenu.doStartRunning: " + text);

			SourceTextPanel current=(SourceTextPanel)SPortEditor.tabbedPane.getSelectedComponent();
			String name = "new";
			if(current.sourceFile != null) {
				name = current.sourceFile.getName();
				int pos = name.indexOf('.');
				if(pos > 0) name = name.substring(0, pos);
			}

			File sourceFile=new File(Global.getTempFileDir("sim/tmp/"), name + ".sim");
			sourceFile.getParentFile().mkdirs();
			sourceFileName = sourceFile.toString();
			if(Option.verbose) IO.println("EditorMenues.doStartRunning: name=" + name + ", sourceFileName="+sourceFileName);
			FileOutputStream out = new FileOutputStream(sourceFile);
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(out))) {
				writer.write(text);
				writer.flush();
				writer.close();
			}
			File sCodeFile=new File(Global.getTempFileDir("sim/tmp/"), name + ".scd");
			sCodeFile.getParentFile().mkdirs();
			sCodeFileName = sCodeFile.toString();
			if(Option.verbose) Util.println("\n\nEditorMenues.doStartRunning: CALL FEC: Output ==> sCodeFileName = "+sCodeFileName);
			int execCode = SimulaFEC.callSimulaFEC();
			if(Option.verbose) Util.println("EditorMenues.doStartRunning: RETURN FROM FEC: ExitCode = "+execCode);
			if(execCode == 0) {
				if(Option.verbose)
					IO.println("\n\nCALL BEC: Output ==> SVM Code ==> executed");
				int execCode2 = CommonBEC.callBEC();
				if(Option.verbose)
					IO.println("RETURN FROM BEC: ExitCode = "+execCode2);
			}
		} catch(Exception e) { Util.popUpError("Can't run: "+e);}
	}
    
	// ****************************************************************
	// *** selectWorkspaceAction
	// ****************************************************************
	/// Select Workspace action.
	private void selectWorkspaceAction() {
    	SPortEditor.doSelectWorkspace();
    }	

	// ****************************************************************
	// *** removeWorkspacesAction
	// ****************************************************************
	/// Remove Workspace action.
    private void removeWorkspacesAction() {
    	JPanel panel=new JPanel();
    	panel.setBackground(Color.white);
    	JLabel label=new JLabel("Check Workspaces to be removed:");
    	panel.add(label);
    	ArrayList<JCheckBox> list=new ArrayList<JCheckBox>();
    	for(File workspace:Global.workspaces) {
        	JCheckBox checkbox=new JCheckBox(workspace.toString()); 
        	checkbox.setBackground(Color.white);
        	list.add(checkbox); panel.add(checkbox);  
    	}
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		int res=Util.optionDialog(panel,"Remove Workspaces (no changes to the file system)"
				,JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Update","Cancel");
		if(res==JOptionPane.OK_OPTION) {
			for(JCheckBox box:list) {
				if(box.isSelected()) Global.workspaces.remove(new File(box.getText()));
			}
	    	Global.storeSPortEditorProperties();
		}
    }
	
	// ****************************************************************
	// *** doAboutAction
	// ****************************************************************
    /// About action
	private void doAboutAction() {
		JTextArea msg=new JTextArea(
			"   This is a reconstruction of S-Port Simula System created\n"
	      + "   by the Open Source Project 'Portable Simula Revisited'.\n\n"

		  + "   The system consists of:\n" 
		  + "      - Simuletta compiler which gererates S-Code\n" 
		  + "      - Simula runtime system (RTS) written in Simuletta.\n" 
		  + "      - Simula Front-end compiler (FEC) written in Simula\n" 
		  + "      - S-Code Back-end compiler (BEC) written in Java\n" 
		  + "      - Simula Virtual Macine (SVM)\n\n" 

	   	  + "   BEC is used to compile S-Code from bouth RTS and FEC\n" 
	   	  + "   into code for a Simula Virtual Macine (SVM)\n\n"); 

		Util.optionDialog(msg,"About Portable Simula",JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE,"Ok");
	}
	
	// ****************************************************************
	// *** doMoreAction
	// ****************************************************************
	/// More action
	private void doMoreAction() {
		if(Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try { desktop.browse(new URI("https://portablesimula.github.io/github.io/"));
			} catch (Exception ex) {}
		}
	}

}
