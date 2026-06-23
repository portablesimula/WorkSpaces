package simula.compiler;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

public class SimulaParser {
    private final List<SimulaLexer.Token> tokens;
    private int ptr = 0;
    public final List<Diagnostic> diagnostics = new ArrayList<>();

    public SimulaParser(List<SimulaLexer.Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (peek().type != SimulaLexer.TokenType.EOF) {
            if (peek().type == SimulaLexer.TokenType.KEYWORD && peek().value.equalsIgnoreCase("INTEGER")) {
                parseVariableDeclaration();
            } else {
                consume(); // Hopper videre ved ukjent kode
            }
        }
    }

    private void parseVariableDeclaration() {
        SimulaLexer.Token keyword = consume(); // INTEGER
        if (peek().type != SimulaLexer.TokenType.IDENTIFIER) {
            reportError(keyword, "Forventet variabelnavn etter 'INTEGER'");
            return;
        }
        consume(); // ID

        if (peek().type != SimulaLexer.TokenType.SEMICOLON) {
            reportError(peek(), "Forventet ';' etter variabeldeklarasjon");
            return;
        }
        consume(); // ;
    }

    private SimulaLexer.Token peek() { return tokens.get(ptr); }
    private SimulaLexer.Token consume() { return tokens.get(ptr++); }

    private void reportError(SimulaLexer.Token token, String message) {
        Diagnostic d = new Diagnostic();
        d.setSeverity(DiagnosticSeverity.Error);
        d.setMessage(message);
        d.setRange(new Range(new Position(token.line, token.character), new Position(token.line, token.character + token.value.length())));
        diagnostics.add(d);
    }
}
