package simula.lsp.compiler;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class LspToken {
    int line;   // 0-based
    int column; // 0-based
    int length;
    int tokenTypeIndex;
    int tokenModifiersBitmask;

    // Helper class to hold absolute parsed token data
    LspToken(int line, int column, int length, int type, int mod) {
        this.line = line;
        this.column = column;
        this.length = length;
        this.tokenTypeIndex = type;
        this.tokenModifiersBitmask = mod;
    }
    
//    public String getText(String fullText) {
//    	
//    }

    @Override
    public String toString() {
    	return "LspToken[line:" + line +", column:" + column + ", length:" + length + ", type: " + tokenTypeIndex + ", mod: " + tokenModifiersBitmask +"]";
    }
}
