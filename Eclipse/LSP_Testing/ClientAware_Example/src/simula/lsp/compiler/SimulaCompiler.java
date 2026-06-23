package simula.lsp.compiler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

public class SimulaCompiler {


    public static List<Diagnostic> runCompilerOrValidator(String documentUri, String sourceCode) {
    	// Her legger du koden for å kompilere/parsere teksten
    	// og sende feilmeldinger (PublishDiagnostics) tilbake til klienten om nødvendig.
	    // 2. Kjør din egen Simula Lexer og Parser
	    SimulaLexer lexer = new SimulaLexer(sourceCode);
	    SimulaParser parser = new SimulaParser(lexer);
	    
	    // Bygg syntakstreet
	    SimulaAST ast = parser.parse(); 

	    // 3. Hent ut eventuelle feil funnet av parseren
	    List<Diagnostic> diagnostics = new ArrayList<>();
	    for (SyntaxError error : parser.getErrors()) {
	        Diagnostic diagnostic = new Diagnostic();
	        diagnostic.setSeverity(DiagnosticSeverity.Error);
	        diagnostic.setMessage(error.getMessage());
	        
	        // LSP bruker 0-indekserte linjer og tegn
	        Position start = new Position(error.getLine() - 1, error.getCharPosition());
	        Position end = new Position(error.getLine() - 1, error.getCharPosition() + error.getLength());
	        diagnostic.setRange(new Range(start, end));
	        
	        diagnostics.add(diagnostic);
	    }
	    
	    return diagnostics;
    }

}
