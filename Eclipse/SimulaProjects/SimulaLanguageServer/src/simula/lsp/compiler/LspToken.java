package simula.lsp.compiler;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class LspToken {
    int line;      // 0-based
    int character; // 0-based
    int length;
    int tokenTypeIndex;
    int tokenModifiersBitmask;

    // Helper class to hold absolute parsed token data
    LspToken(int line, int character, int length, int type, int mod) {
        this.line = line;
        this.character = character;
        this.length = length;
        this.tokenTypeIndex = type;
        this.tokenModifiersBitmask = mod;
    }

}
