package simula.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.Util;
import simula.editor.SourceTextPanel;
import simula.psi.PsiBuilder;
import simula.psi.PsiTree;

public class ModuleManager {
	
	public File sourceFile;
	
	PsiTree psiTree;
	private ProgramModule programModule; // Root of Syntax Tree
	
	SourceTextPanel textPanel;
	
	public ModuleManager(SourceTextPanel textPanel, File sourceFile) {
		this.textPanel = textPanel;
		this.sourceFile = sourceFile;
	}
	
	public ModuleManager(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public String getName() {
		if(sourceFile != null) return sourceFile.getName();
		return "Unnamed.sim";
	}
	
	public String getSourceText() throws IOException {
		if(textPanel != null) return textPanel.getText();
		return Files.readString(sourceFile.toPath());
	}

	public PsiTree getPsiTree() {
		if(psiTree == null) {
			PsiBuilder psiBuilder = new PsiBuilder();
//			psiBuilder.start(textPanel.getText());
			try {
				psiBuilder.start(getSourceText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			programModule = new ProgramModule(psiBuilder);
			programModule.doChecking();
			psiTree = psiBuilder.getRoot();
			checkPsiText();
		}
		return psiTree;
	}
	
	public ProgramModule getProgramModule() {
		if(programModule == null) {
//			Global.programModule.printTree(1, this);
			getPsiTree();
		}
		return programModule;
	}
	
	private void checkPsiText() {
//		if(! psiTree.getText().equals(textPanel.getText())) {
		try {
			if(! psiTree.getText().equals(getSourceText())) {
				String curTxt = (""+textPanel.getText()).replace("\r", "\\r").replace("\n", "\\n");
				String psiTxt = (psiTree.getText()).replace("\r", "\\r").replace("\n", "\\n");
				IO.println("EditorMenues.doRenderSyntaxTreeAction: curTxt: "+curTxt);
				IO.println("EditorMenues.doRenderSyntaxTreeAction: psiTxt: "+psiTxt);
				Util.IERR("Resulting text differ from original text");
//				Util.STOP();
			}
//			else IO.println("EditorMenues.doRenderSyntaxTreeAction: DONE - OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
