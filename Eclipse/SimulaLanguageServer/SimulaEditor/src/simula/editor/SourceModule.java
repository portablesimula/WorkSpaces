package simula.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.undo.UndoManager;

import simula.core.builder.export.SimulaDiagnostic;
import simula.Comn;
import simula.SimulaCoreExports;
import simula.editor.SimulaEditor.Language;
import simula.editor.text.TabTextPanel;
import simula.editor.utilities.Global;
import simula.editor.utilities.Util;

public class SourceModule {
	
    // Nøkkelen er filens URI (f.eks. file:///path/to/file.txt)
    private static final ConcurrentHashMap<String, SourceModule> openModules = new ConcurrentHashMap<>();
    public static SourceModule getSourceModule(String documentUri)  {
    	SourceModule res = openModules.get(documentUri);
    	if(res == null) Util.IERR("No such Module: " + documentUri);
    	return res;
    }
	
	String documentUri;
	
	public File sourceFile;
	private String sourceText;
//	private String tabName;
	
	List<Integer> semTokens;
	
	public DiagnosticHandler diagnosticHandler;
//	List<SimulaDiagnostic> diagnostics;
	public static void publishDiagnostics(String uri, List<SimulaDiagnostic> diagnostics) {
		IO.println("SourceModule.publishDiagnostics: " + uri + " " + diagnostics);
		SourceModule sourceModule = SourceModule.getSourceModule(uri);
    	IO.println("SourceModule.publishDiagnostics: openModules: " + openModules);
//		sourceModule.diagnostics = diagnostics;
    	sourceModule.diagnosticHandler = new DiagnosticHandler(sourceModule, diagnostics);
//		Util.IERR("NOT IMPL");
	}

	/// Current language.
    public Language lang;

//	private SourceTextPanel textPanel;
//	private SimulaTextPanel simTextPanel;
	public TabTextPanel textPanel; // OLD or PsiText
	
    /// Indicates that the source file has changed.
    private boolean fileChanged = false;
    public boolean getFileChanged() { return fileChanged; }
    public void setFileChanged(boolean on) {
//    	fileChanged = on;
    	if(on) Thread.dumpStack();
    }
	
	/// Signals auto refresh.
    public boolean AUTO_REFRESH=true;//false;
    
    /// Indicates that refresh is needed.
    public boolean refreshNeeded = false;
    
//    /// Used by PaletteChooser
//    public SourceModule(String sourceText) {
//    	this.sourceText = sourceText;
//		Global.currentModule = this;
//    	this.getTokenList();
//    }

	// ****************************************************************
	// *** doRefresh
	// ****************************************************************
    /// Do refresh action.
	public void doRefresh() {
		IO.println("SourceModule.doRefresh: " + this.getTabName() + "  " + textPanel.getClass().getSimpleName());
		Thread.dumpStack();
		textPanel.doRefresh();
	}

	/// The undo manager.
	public UndoManager undoManager = new UndoManager();
	
//	/// Returns the undo manager.
//	/// @return the undo manager
//	UndoManager getUndoManager() { return(undoManager); }
	

//	public SourceModule(String sourceText) {
//	}
	
	public SourceModule(String documentUri, String sourceText) {
		this.documentUri = documentUri;
		this.sourceText = sourceText;
    	openModules.put(documentUri, this);
		Global.currentModule = this;
    	this.lang = Language.Simula;
	}
	
	public SourceModule(File sourceFile) {
		this.sourceFile = sourceFile;
		this.documentUri = sourceFile.toString();
    	openModules.put(documentUri, this);
		Global.currentModule = this;
		
		if(sourceFile != null) {
			// Set Module language based on file type
			String lowName=sourceFile.getName().toLowerCase();
			if(lowName.endsWith(".sim"))      this.lang = Language.Simula;
			else if(lowName.endsWith(".jar")) this.lang = Language.Jar;
			else if(isTextFile(lowName))      this.lang = Language.Text;
			else                              this.lang = Language.Other;
			// Read the text
			try {
				this.sourceText = Files.readString(sourceFile.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			Vector<String> args = new Vector<String>();
//			SimulaEditorClient.doOpen(documentUri, args);

		}
	}

	public void doOpenSimulaModule() {
		try {
//			String uri = sourceFile.toString();
			String uri = documentUri;
			int version = 1;
			String content = getModifiedText();
			SimulaCoreExports.didOpen(uri, version, content);
			IO.println("SourceModule.doOpenSimulaModule: " + getUpdatedText().replace("\n", "\\n").replace("\r", "\\r"));
			this.semTokens = SimulaCoreExports.semanticTokensFull(documentUri);
		} catch (Exception e) {
			IO.println("SourceModule.doOpenSimulaModule: GOT EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void doCloseSimulaModule() {
		IO.println("SourceModule.doCloseSimulaModule: " + openModules);
        openModules.remove(documentUri);
		IO.println("SourceModule.doCloseSimulaModule: " + openModules);
//		Util.STOP();
	}

		
	public List<Integer> getSemTokens() {
		if(semTokens == null) doOpenSimulaModule();
		return semTokens;
	}

	/// Test if a file is a text file
	/// @param lowName the ident after .
	/// @return true if it is a text file
	private static boolean isTextFile(String lowName) {
		String[] kind= {".java", ".txt", ".bat", ".sh", ".md", ".html", ".xml" }; // TODO: More ?
		for(String k:kind) if(lowName.endsWith(k)) return(true);
		return(false);
	}

	public void setTextPanel(TabTextPanel textPanel) {
		this.textPanel = textPanel;
	}
	
	public String getName() {
		if(sourceFile != null) return sourceFile.getName();
		return "Unnamed.sim";
	}
	
	public String getTabName() {
		return getName();
	}
	
	public String getUri() {
		return this.documentUri;
	}
	
	public String getUpdatedText() {
		if(textPanel != null) {
			return textPanel.getText();
		}
		return this.sourceText;
	}
	
	public String getOriginalText() throws IOException {
		return this.sourceText;
	}
	
	public String getModifiedText() throws IOException {
		IO.println("\nSourceModule.getModifiedText: ========================================");
		return Comn.modifySourceCode(this.sourceText);
	}
    
    public String toString() {
    	return "SourceModule: " + getTabName() + " Language: " + lang;
    }

}
