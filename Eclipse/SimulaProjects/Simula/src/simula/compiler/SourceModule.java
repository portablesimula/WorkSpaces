package simula.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.editor.PsiTextPanel;
import simula.editor.SourceTextPanel;
import simula.psi.PsiBuilder;
import simula.psi.PsiTree;

public class SourceModule {
	public File sourceFile;
	private String tabName;
	
	PsiTree psiTree;
	private ProgramModule syntaxTree; // Root of Syntax Tree
	
	private SourceTextPanel textPanel;
	private PsiTextPanel psiTextPanel;
	
	public SourceModule(File sourceFile) {
		this.sourceFile = sourceFile;
		Global.currentModule = this;

		// Update moduleMap
		String tabName0 = getName();
		IO.println("SourceModule.getTabName: " + tabName0);
		tabName = tabName0;
		int sequ = 1;
		while(Global.moduleMap.containsKey(tabName)) {
			tabName = tabName0 + '(' + (sequ++) + ')';
			IO.println("SourceModule.getTabName: " + tabName0);
		}
		Global.moduleMap.put(tabName, this);
		SourceModule THIS = Global.moduleMap.get(tabName);
		if(THIS != this) {
			Util.STOP();
		}
	}
	
	public void setTextPanel(SourceTextPanel textPanel) {
		this.textPanel = textPanel;
	}
	
	public void setPsiTextPanel(PsiTextPanel psiTextPanel) {
		this.psiTextPanel = psiTextPanel;
	}
	
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
	
	public String getSourceText() throws IOException {
		if(textPanel != null) return textPanel.getText();
		return Files.readString(sourceFile.toPath());
	}
	
	public void dropPsiAndSyntaxTrees() {
		psiTree = null;
		syntaxTree = null;
	}

	public void buildPsiAndSyntaxTrees() {
		PsiBuilder psiBuilder = new PsiBuilder();
		try {
			psiBuilder.start(getSourceText());
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
			String txt2 = getSourceText().replace("\t", "");
			if(! txt1.equals(txt2)) {
				compare(psiTree.getText(), getSourceText());
				if(textPanel != null) {
					String curTxt = (""+textPanel.getText()).replace("\r", "\\r").replace("\n", "\\n");
					String psiTxt = (psiTree.getText()).replace("\r", "\\r").replace("\n", "\\n");
					IO.println("EditorMenues.doRenderSyntaxTreeAction: curTxt: "+curTxt);
					IO.println("EditorMenues.doRenderSyntaxTreeAction: psiTxt: "+psiTxt);
					compare(curTxt, psiTxt);
				} else {
					String curTxt = (getSourceText()).replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
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
