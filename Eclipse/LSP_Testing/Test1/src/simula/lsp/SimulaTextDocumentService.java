package simula.lsp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.TextDocumentService;

import simula.compiler.ParseResult;
import simula.compiler.SimulaCompilerFrontend;
import simula.compiler.SimulaError;

public class SimulaTextDocumentService implements TextDocumentService {
	private static final Logger logger = Logger.getLogger(SimulaTextDocumentService.class.getName());

	LanguageClient client;

    // Kalles når et dokument åpnes i editoren
    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        String uri = params.getTextDocument().getUri();
        String text = params.getTextDocument().getText();
        logger.info("Dokument åpnet: " + uri);
        // Her kan du lagre dokumentets innhold i en lokal buffer eller kjøre en parser
    }

    // Kalles hver gang brukeren gjør endringer i dokumentet
    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        String documentUri = params.getTextDocument().getUri();
        
        // Hent den siste endringen (avhengig av om du har konfigurert Full eller Incremental synkronisering)
        List<TextDocumentContentChangeEvent> changes = params.getContentChanges();
        if (!changes.isEmpty()) {
            String latestText = changes.get(0).getText();
            logger.info("Dokument endret: " + documentUri);
            // Oppdater din interne representasjon av koden her
        }
//    }
//
//    @Override
//    public void didChange(DidChangeTextDocumentParams params) {
        // 1. Hent den oppdaterte koden fra VS Code
        String simulaCode = params.getContentChanges().get(0).getText();
//        String documentUri = params.getTextDocument().getUri();

        // 2. Kjør din egen Simula Lexer og Parser
        SimulaCompilerFrontend compiler = new SimulaCompilerFrontend();
        ParseResult result = compiler.analyze(simulaCode); 

        // 3. Konverter kompilatorfeil til LSP4J Diagnostics
        List<Diagnostic> diagnostics = new ArrayList<>();
        for (SimulaError error : result.getErrors()) {
//            Range range = new Range(
//                new Position(error.getLine() - 1, error.getCharPosition()),
//                new Position(error.getLine() - 1, error.getCharPosition() + error.getLength())
//            );
//            diagnostics.add(new Diagnostic(range, error.getMessage(), DiagnosticSeverity.Error, "SimulaParser"));
        	diagnostics.add(error.getDiagnostic());
        }

        // 4. Send feilmeldinger direkte tilbake til VS Code
        client.publishDiagnostics(new PublishDiagnosticsParams(documentUri, diagnostics));
    }

    // Kalles når et dokument lukkes i editoren
    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        String uri = params.getTextDocument().getUri();
        logger.info("Dokument lukket: " + uri);
        // Rydd opp i eventuelle ressurser knyttet til denne filen
    }

    // Kalles når dokumentet lagres manuelt eller automatisk
    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        String uri = params.getTextDocument().getUri();
        logger.info("Dokument lagret: " + uri);
    }

	public void setClient(LanguageClient client) {
		// TODO Auto-generated method stub
		this.client = client;
	}

//    // Eksempel på en forespørsel (Request) for kode-autofullføring (IntelliSense)
//    @Override
//    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
//        return CompletableFuture.supplyAsync(() -> {
//            List<CompletionItem> completionItems = new ArrayList<>();
//
//            // Eksempel på et forslag som dukker opp i editoren
//            CompletionItem item = new CompletionItem();
//            item.setLabel("helloWorld");
//            item.setKind(CompletionItemKind.Function);
//            item.setInsertText("helloWorld()");
//            item.setDetail("Min egendefinerte funksjon");
//
//            completionItems.add(item);
//
//            // Returner enten en liste eller et CompletionList-objekt
//            return Either.forLeft(completionItems);
//        });
//    }
    
}
