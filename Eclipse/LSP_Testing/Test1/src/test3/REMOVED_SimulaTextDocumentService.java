package test3;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.TextDocumentService;

import simula.compiler.ParseResult;
import simula.compiler.SimulaCompilerFrontend;
import simula.compiler.SimulaError;

public class REMOVED_SimulaTextDocumentService implements TextDocumentService {

    private final LanguageClient client;

    public REMOVED_SimulaTextDocumentService(LanguageClient client) {
        this.client = client;
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        // 1. Hent den oppdaterte koden fra VS Code
        String simulaCode = params.getContentChanges().get(0).getText();
        String documentUri = params.getTextDocument().getUri();

        // 2. Kjør din egen Simula Lexer og Parser
        SimulaCompilerFrontend compiler = new SimulaCompilerFrontend();
        ParseResult result = compiler.analyze(simulaCode); 

        // 3. Konverter kompilatorfeil til LSP4J Diagnostics
        List<Diagnostic> diagnostics = new ArrayList<>();
        for (SimulaError error : result.getErrors()) {
            Range range = new Range(
                new Position(error.getLine() - 1, error.getCharPosition()),
                new Position(error.getLine() - 1, error.getCharPosition() + error.getLength())
            );
            diagnostics.add(new Diagnostic(range, error.getMessage(), DiagnosticSeverity.Error, "SimulaParser"));
        }

        // 4. Send feilmeldinger direkte tilbake til VS Code
        client.publishDiagnostics(new PublishDiagnosticsParams(documentUri, diagnostics));
    }

	@Override
	public void didOpen(DidOpenTextDocumentParams params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
		// TODO Auto-generated method stub
		
	}
    
    // ... andre påkrevde metoder som didOpen, didSave, didClose
}
