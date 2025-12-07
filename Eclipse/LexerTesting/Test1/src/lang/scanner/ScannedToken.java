package lang.scanner;

import lang.IElementType;

public class ScannedToken {
    public IElementType type;
    public int start;
    public int end;

    public ScannedToken(IElementType type, int start, int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return "Token["+type+", start="+start+", end="+end+']';
    }
}
