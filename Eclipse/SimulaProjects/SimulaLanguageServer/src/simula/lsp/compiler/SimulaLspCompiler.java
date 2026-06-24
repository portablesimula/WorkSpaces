package simula.lsp.compiler;

import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.lsp.SimulaLanguageServer;
import simula.psi.PsiBuilder;
import simula.psi.PsiTree;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class SimulaLspCompiler {


    public static void runCompilerOrValidator(String documentUri, SimulaLanguageServer server) {
    	DocumentManager documentManager = server.getDocumentManager();
    	SourceDocumentItem sourceItem = documentManager.get(documentUri);
    	
    	
    	// Her legger du koden for å kompilere/parsere teksten
    	// og sende feilmeldinger (PublishDiagnostics) tilbake til klienten om nødvendig.
	    // 2. Kjør din egen Simula Lexer og Parser
//	    SimulaLexer lexer = new SimulaLexer(sourceCode);
//	    SimulaParser parser = new SimulaParser(lexer);
//	    // Bygg syntakstreet
//	    SimulaAST ast = parser.parse(); 
	    
    	buildPsiAndSyntaxTrees(sourceItem);
	    

//	    // 3. Hent ut eventuelle feil funnet av parseren
//	    List<Diagnostic> diagnostics = new ArrayList<>();
//	    for (SyntaxError error : parser.getErrors()) {
//	        Diagnostic diagnostic = new Diagnostic();
//	        diagnostic.setSeverity(DiagnosticSeverity.Error);
//	        diagnostic.setMessage(error.getMessage());
//	        
//	        // LSP bruker 0-indekserte linjer og tegn
//	        Position start = new Position(error.getLine() - 1, error.getCharPosition());
//	        Position end = new Position(error.getLine() - 1, error.getCharPosition() + error.getLength());
//	        diagnostic.setRange(new Range(start, end));
//	        
//	        diagnostics.add(diagnostic);
//	    }
//	    
//	    return diagnostics;
	    
    	// MERK: Alle meldinger legges direkte inn i SourceDocumentItem.diagnostics  ON THE FLY !!!!!
    	sourceItem.printDiagnostics();
    }


	public static void buildPsiAndSyntaxTrees(SourceDocumentItem sourceDocumentItem) {
    	LOG.info("SimulaLspCompiler.buildPsiAndSyntaxTrees: BEGIN");
    	sourceDocumentItem.initDiagnostics();
		String sourceText = sourceDocumentItem.getText();
		PsiBuilder psiBuilder = new PsiBuilder(sourceDocumentItem);
		ProgramModule syntaxTree = null;
		try {
			psiBuilder.start(sourceText);
    		IO.println("SourceModule.buildPsiAndSyntaxTrees: " + sourceText.replace("\n", "\\n").replace("\r", "\\r"));
    		syntaxTree = new ProgramModule(psiBuilder);
		} catch (Exception e) {
			IO.println("SourceModule.buildPsiAndSyntaxTrees: GOT EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		}
		
		sourceDocumentItem.setSyntaxTree(syntaxTree);
			
		StandardClass.ENVIRONMENT.doChecking();
		Global.duringParsing = false;
		syntaxTree.doChecking();
		PsiTree psiTree = psiBuilder.getRoot();
		if(Option.PSI_VERIFY) {
			checkPsiText(sourceText, psiTree);
		}
		sourceDocumentItem.setPsiTree(psiTree);
	}
	
	private static void checkPsiText(String sourceText, PsiTree psiTree) {
//		if(! psiTree.getText().equals(textPanel.getText())) {
//		try {
			String txt1 = psiTree.getText().replace("\t", "");
			String txt2 = sourceText.replace("\t", "");
			if(! txt1.equals(txt2)) {
				compare(psiTree.getText(), sourceText);
				String curTxt = (sourceText).replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
				String psiTxt = (psiTree.getText()).replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
				IO.println("EditorMenues.doRenderSyntaxTreeAction: curTxt: ]"+curTxt+'[');
				IO.println("EditorMenues.doRenderSyntaxTreeAction: psiTxt: ]"+psiTxt+'[');
				compare(curTxt, psiTxt);
				Util.IERR("Resulting text differ from original text");
//				Util.STOP();
			}
//			else IO.println("EditorMenues.doRenderSyntaxTreeAction: DONE - OK");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private static void compare(String s1, String s2) {
		if(s1.length() != s2.length()) IO.println("EditorMenues.doRenderSyntaxTreeAction: Different length: "+s1.length()+" "+s2.length());
		int n = Math.min(s1.length(), s2.length());
		for(int i=0;i<n;i++) {
			if(s1.charAt(i) != s2.charAt(i)) IO.println("EditorMenues.doRenderSyntaxTreeAction: Diff at pos: "+i+ "" +s1.charAt(i)+" "+s2.charAt(i));
		}
	}

}
