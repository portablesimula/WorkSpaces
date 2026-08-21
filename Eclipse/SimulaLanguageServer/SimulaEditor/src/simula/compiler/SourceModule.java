package simula.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.undo.UndoManager;

import client.SimulaEditorClient;
import simula.core.builder.export.SimulaDiagnostic;
import simula.core.builder.export.TokenManager;
import simula.SimulaCoreExports;
//import simula.compiler.syntaxClass.declaration.StandardClass;
//import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.editor.TabTextPanel;
import simula.psi.PsiBuilder;
import simula.psi.TokenList;
import simula.editor.SimulaEditor.Language;
//import simula.psi.PsiBuilder;
//import simula.psi.PsiTree;

public class SourceModule {
	
    // Nøkkelen er filens URI (f.eks. file:///path/to/file.txt)
    private static final ConcurrentHashMap<String, SourceModule> openDocuments = new ConcurrentHashMap<>();
    public static SourceModule getSourceModule(String documentUri)  {
    	return openDocuments.get(documentUri);
    }
	
	String documentUri;
	
	public File sourceFile;
	private String sourceText;
//	private String tabName;
	
//	TokenList psiTree;
//	private ProgramModule syntaxTree; // Root of Syntax Tree
	TokenList tokenList;
	
	List<SimulaDiagnostic> diagnostics;
	public static void publishDiagnostics(String uri, List<SimulaDiagnostic> diagnostics) {
		SourceModule sourceModule = SourceModule.getSourceModule(uri);
		sourceModule.diagnostics = diagnostics;
//		Util.IERR("NOT IMPL");
	}

	/// Current language.
    public Language lang;

//	private SourceTextPanel textPanel;
//	private PsiTextPanel psiTextPanel;
	public TabTextPanel textPanel; // OLD or PsiText
	
    /// Indicates that the source file has changed.
    public boolean fileChanged = false;
	
	/// Signals auto refresh.
    public boolean AUTO_REFRESH=true;//false;
    
    /// Indicates that refresh is needed.
    public boolean refreshNeeded = false;

	// ****************************************************************
	// *** doRefresh
	// ****************************************************************
    /// Do refresh action.
	public void doRefresh() {
//		if(textPanel instanceof PsiTextPanel psiPanel) psiPanel.doRefresh();
//		if(textPanel instanceof SourceTextPanel oldPanel) oldPanel.doRefresh();
		IO.println("SourceModule.doRefresh: " + this.getTabName() + "  " + textPanel.getClass().getSimpleName());
		textPanel.doRefresh();
	}

	/// The undo manager.
	public UndoManager undoManager = new UndoManager();
	
//	/// Returns the undo manager.
//	/// @return the undo manager
//	UndoManager getUndoManager() { return(undoManager); }
	

//	public SourceModule(String sourceText) {
//		this.sourceText = sourceText;
//		Global.currentModule = this;
//	}
	
	public SourceModule(File sourceFile) {
		this.sourceFile = sourceFile;
		this.documentUri = sourceFile.toString();
    	openDocuments.put(documentUri, this);
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

//		// Update moduleMap
//		String tabName0 = getName();
////		IO.println("SourceModule.getTabName: " + tabName0);
//		tabName = tabName0;
//		int sequ = 1;
//		while(Global.moduleMap.containsKey(tabName)) {
//			tabName = tabName0 + '(' + (sequ++) + ')';
////			IO.println("SourceModule.getTabName: " + tabName0);
//		}
//		Global.moduleMap.put(tabName, this);
//		SourceModule THIS = Global.moduleMap.get(tabName);
//		if(THIS != this) {
//			Util.STOP();
//		}
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
	
//	public void setPsiTextPanel(PsiTextPanel psiTextPanel) {
//		this.psiTextPanel = psiTextPanel;
//	}
	
	public String getName() {
		if(sourceFile != null) return sourceFile.getName();
		return "Unnamed.sim";
	}
	
	public String getTabName() {
//		String tabName0 = getName();
//		IO.println("SourceModule.getTabName: " + tabName0);
//		String tabName = tabName0;
//		int sequ = 1;
//		while(tabNames.contains(tabName)) {
//			tabName = tabName0 + '(' + (sequ++) + ')';
//			IO.println("SourceModule.getTabName: " + tabName0);
//		}
//		tabNames.add(tabName);
//		return tabName;
		return getName();
	}
	
	public static String emptyProgram = "begin\n\nend;\n";
	
	public String getUpdatedText() {
		if(textPanel != null) {
//			if(textPanel instanceof PsiTextPanel psiPanel) {
//				return psiPanel.getText();
//			}
//			if(textPanel instanceof SourceTextPanel oldPanel) {
//				return oldPanel.getText();
//			}
			return textPanel.getText();
		}
		return this.sourceText;
	}
	
	public String getOriginalText() throws IOException {
//		return Files.readString(sourceFile.toPath());
		return this.sourceText;
	}
	
	public void dropPsiAndSyntaxTrees() {
		tokenList = null;
//		syntaxTree = null;
	}

	public void buildInitialTokenList() {
//		PsiBuilder psiBuilder = new PsiBuilder();
		try {
//			psiBuilder.start(getOriginalText());
//			psiBuilder.start(getUpdatedText());
			Vector<String> args = new Vector<String>();
			SimulaEditorClient.doOpen(this.documentUri.toString(), args);
    		IO.println("SourceModule.buildInitialTokenList: " + getUpdatedText().replace("\n", "\\n").replace("\r", "\\r"));
    		this.tokenList = new TokenList(this, SimulaCoreExports.semanticTokensFull(documentUri));
    		if(Option.LSP_VERIFY) {
    			tokenList.verifyTokenList();
    		}
//    		Util.IERR("");
//			syntaxTree = new ProgramModule(psiBuilder);
		} catch (Exception e) {
			IO.println("SourceModule.buildInitialTokenList: GOT EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		}
			
//		StandardClass.ENVIRONMENT.doChecking();
//		Global.duringParsing = false;
//		syntaxTree.doChecking();
//		psiTree = psiBuilder.getRoot();
//		if(Option.LSP_VERIFY) {
//			checkPsiText(psiTree);
//		}
	}

	public TokenList getTokenList() {
		if(tokenList == null) buildInitialTokenList();
		return tokenList;
	}
	
//	public ProgramModule getSyntaxTree() {
//		if(syntaxTree == null) buildInitialTokenList();
//		return syntaxTree;
//	}
	
//	private void checkPsiText(PsiTree psiTree) {
//		String txt1 = psiTree.getText().replace("\t", "");
//		String txt2 = getUpdatedText().replace("\t", "");
//		if(! txt1.equals(txt2)) {
//			compare(psiTree.getText(), getUpdatedText());
//			if(textPanel != null) {
//				String curTxt = getUpdatedText().replace("\r", "\\r").replace("\n", "\\n");
//				String psiTxt = (psiTree.getText()).replace("\r", "\\r").replace("\n", "\\n");
//				IO.println("EditorMenues.doRenderSyntaxTreeAction: curTxt: \""+curTxt+'"');
//				IO.println("EditorMenues.doRenderSyntaxTreeAction: psiTxt: \""+psiTxt+'"');
//				compare(curTxt, psiTxt);
//			} else {
//				String curTxt = (getUpdatedText()).replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
//				String psiTxt = (psiTree.getText()).replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
//				IO.println("EditorMenues.doRenderSyntaxTreeAction: curTxt: ]"+curTxt+'[');
//				IO.println("EditorMenues.doRenderSyntaxTreeAction: psiTxt: ]"+psiTxt+'[');
//				compare(curTxt, psiTxt);
//			}
//			Util.IERR("Resulting text differ from original text");
//		}
//	}
	
	private void compare(String s1, String s2) {
		if(s1.length() != s2.length()) IO.println("EditorMenues.doRenderSyntaxTreeAction: Different length: "+s1.length()+" "+s2.length());
		int n = Math.min(s1.length(), s2.length());
		for(int i=0;i<n;i++) {
			if(s1.charAt(i) != s2.charAt(i)) IO.println("EditorMenues.doRenderSyntaxTreeAction: Diff at pos: "+i+ "" +s1.charAt(i)+" "+s2.charAt(i));
		}
	}
	
    
    public String toString() {
    	return "SourceModule: " + getTabName() + " Language: " + lang;
    }

}
