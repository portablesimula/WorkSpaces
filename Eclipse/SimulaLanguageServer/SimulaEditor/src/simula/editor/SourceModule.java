package simula.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.undo.UndoManager;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.SemanticTokens;
import org.eclipse.lsp4j.SemanticTokensParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import simula.core.CoreGlobal;
import simula.Comn;
import simula.editor.SimulaEditor.Language;
import simula.editor.text.TabTextPanel;
import simula.editor.utilities.Global;
import simula.editor.utilities.Util;
import simula.lsp.server.SimulaTextDocumentService;

public class SourceModule {
	
    // Nøkkelen er filens URI (f.eks. file:///path/to/file.txt)
    private static final ConcurrentHashMap<String, SourceModule> openModules = new ConcurrentHashMap<>();
    public static SourceModule getSourceModule(String documentUri)  {
    	SourceModule res = openModules.get(documentUri);
    	if(res == null) Util.IERR("No such Module: " + documentUri);
    	return res;
    }
	
	String documentUri;
	TextDocumentIdentifier documentID;
	
	public File sourceFile;
	private String sourceText;
//	private String tabName;
	
	List<Integer> semTokens;
	
	public DiagnosticHandler diagnosticHandler;
//	List<SimulaDiagnostic> diagnostics;
	public static void publishDiagnostics(String uri, List<Diagnostic> diagnostics) {
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
		this.documentID = new TextDocumentIdentifier(documentUri);
		this.sourceText = sourceText;
    	openModules.put(documentUri, this);
		Global.currentModule = this;
    	this.lang = Language.Simula;
	}
	
	public SourceModule(File sourceFile) {
		this.sourceFile = sourceFile;
		this.documentUri = sourceFile.toString();
		this.documentID = new TextDocumentIdentifier(documentUri);
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
			SimulaTextDocumentService simulaTextDocumentService = CoreGlobal.getSimulaTextDocumentService();
			int version = 1;
			String content = getModifiedText();
			TextDocumentItem textDocumentItem = new TextDocumentItem(documentUri, "Simula", version, content);
			
			DidOpenTextDocumentParams params = new DidOpenTextDocumentParams();
			params.setTextDocument(textDocumentItem);
//			IO.println("SourceModule.doOpenSimulaModule: " + params);
			simulaTextDocumentService.didOpen(params);
//			IO.println("SourceModule.doOpenSimulaModule: " + Util.printable(getUpdatedText()));
			
			SemanticTokensParams tokenParams = new SemanticTokensParams(documentID);
//			tokenParams.setTextDocument(documentID);
//			IO.println("SourceModule.doOpenSimulaModule: tokenParams: " + tokenParams);
//			this.semTokens = simulaTextDocumentService.semanticTokensFull(documentUri);
			SemanticTokens semTokens = simulaTextDocumentService.semanticTokensFull_Local(tokenParams);
			this.semTokens = semTokens.getData();
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
