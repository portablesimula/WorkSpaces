package simula.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JPanel;
import javax.swing.undo.UndoManager;

import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.editor.PsiTextPanel;
import simula.editor.SourceTextPanel;
import simula.editor.TabTextPanel;
import simula.editor.SimulaEditor.Language;
import simula.psi.PsiBuilder;
import simula.psi.PsiTree;

public class SourceModule {
	public File sourceFile;
	private String tabName;
	
	PsiTree psiTree;
	private ProgramModule syntaxTree; // Root of Syntax Tree
	
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
		if(textPanel instanceof PsiTextPanel psiPanel) psiPanel.doRefresh();
		if(textPanel instanceof SourceTextPanel oldPanel) oldPanel.doRefresh();
	}

	/// The undo manager.
	public UndoManager undoManager = new UndoManager();
	
//	/// Returns the undo manager.
//	/// @return the undo manager
//	UndoManager getUndoManager() { return(undoManager); }
	

	public SourceModule(File sourceFile) {
		this.sourceFile = sourceFile;
		Global.currentModule = this;
		
		if(sourceFile != null) {
			// Set Module language based on file type
			String lowName=sourceFile.getName().toLowerCase();
			if(lowName.endsWith(".sim"))      this.lang = Language.Simula;
			else if(lowName.endsWith(".jar")) this.lang = Language.Jar;
			else if(isTextFile(lowName))      this.lang = Language.Text;
			else                              this.lang = Language.Other;
		}

		// Update moduleMap
		String tabName0 = getName();
//		IO.println("SourceModule.getTabName: " + tabName0);
		tabName = tabName0;
		int sequ = 1;
		while(Global.moduleMap.containsKey(tabName)) {
			tabName = tabName0 + '(' + (sequ++) + ')';
//			IO.println("SourceModule.getTabName: " + tabName0);
		}
		Global.moduleMap.put(tabName, this);
		SourceModule THIS = Global.moduleMap.get(tabName);
		if(THIS != this) {
			Util.STOP();
		}
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
		return tabName;
	}
	
	public static String emptyProgram = "begin\n\nend;\n";
	
//	public String getSourceText() throws IOException {
	public String getUpdatedText() {
		if(textPanel != null) {
			if(textPanel instanceof PsiTextPanel psiPanel) {
				return psiPanel.getText();
			}
			if(textPanel instanceof SourceTextPanel oldPanel) {
				return oldPanel.getText();
			}
		}
		try {
			return Files.readString(sourceFile.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			return null;
		}
		return emptyProgram;
	}
	public String getOriginalText() throws IOException {
		return Files.readString(sourceFile.toPath());
	}
	
	public void dropPsiAndSyntaxTrees() {
		psiTree = null;
		syntaxTree = null;
	}

	public void buildPsiAndSyntaxTrees() {
		PsiBuilder psiBuilder = new PsiBuilder();
		try {
//			psiBuilder.start(getSourceText());
			psiBuilder.start(getUpdatedText());
			syntaxTree = new ProgramModule(psiBuilder);
		} catch (Exception e) {
			IO.println("SourceModule.buildPsiAndSyntaxTrees: GOT EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		}
			
		StandardClass.ENVIRONMENT.doChecking();
		Global.duringParsing = false;
		syntaxTree.doChecking();
		psiTree = psiBuilder.getRoot();
		if(Option.PSI_VERIFY) {
			checkPsiText(psiTree);
		}
	}

	public PsiTree getPsiTree() {
		if(psiTree == null) buildPsiAndSyntaxTrees();
		return psiTree;
	}
	
//	public ProgramModule getSyntaxTree() {
	public ProgramModule getSyntaxTree() {
		if(syntaxTree == null) buildPsiAndSyntaxTrees();
		return syntaxTree;
	}
	
	private void checkPsiText(PsiTree psiTree) {
//		if(! psiTree.getText().equals(textPanel.getText())) {
		try {
			String txt1 = psiTree.getText().replace("\t", "");
			String txt2 = getOriginalText().replace("\t", "");
			if(! txt1.equals(txt2)) {
				compare(psiTree.getText(), getOriginalText());
				if(textPanel != null) {
					String curTxt = getUpdatedText().replace("\r", "\\r").replace("\n", "\\n");
					String psiTxt = (psiTree.getText()).replace("\r", "\\r").replace("\n", "\\n");
					IO.println("EditorMenues.doRenderSyntaxTreeAction: curTxt: "+curTxt);
					IO.println("EditorMenues.doRenderSyntaxTreeAction: psiTxt: "+psiTxt);
					compare(curTxt, psiTxt);
				} else {
					String curTxt = (getOriginalText()).replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
					String psiTxt = (psiTree.getText()).replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
					IO.println("EditorMenues.doRenderSyntaxTreeAction: curTxt: ]"+curTxt+'[');
					IO.println("EditorMenues.doRenderSyntaxTreeAction: psiTxt: ]"+psiTxt+'[');
					compare(curTxt, psiTxt);
				}
				Util.IERR("Resulting text differ from original text");
//				Util.STOP();
			}
//			else IO.println("EditorMenues.doRenderSyntaxTreeAction: DONE - OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void compare(String s1, String s2) {
		if(s1.length() != s2.length()) IO.println("EditorMenues.doRenderSyntaxTreeAction: Different length: "+s1.length()+" "+s2.length());
		int n = Math.min(s1.length(), s2.length());
		for(int i=0;i<n;i++) {
			if(s1.charAt(i) != s2.charAt(i)) IO.println("EditorMenues.doRenderSyntaxTreeAction: Diff at pos: "+i+ "" +s1.charAt(i)+" "+s2.charAt(i));
		}
	}
	
}
