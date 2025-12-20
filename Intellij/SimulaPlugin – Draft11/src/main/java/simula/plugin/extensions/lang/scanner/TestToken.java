package simula.plugin.extensions.lang.scanner;

import com.intellij.psi.tree.IElementType;

public class TestToken {
    public IElementType type;
    public int start;
    public int end;

    public TestToken(IElementType type, int start, int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return "Token["+type+", start="+start+", end="+end+']';
    }
}
