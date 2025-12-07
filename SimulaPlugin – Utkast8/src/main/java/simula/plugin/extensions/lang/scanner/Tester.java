package simula.plugin.extensions.lang.scanner;

import com.intellij.psi.tree.IElementType;
import simula.plugin.extensions.lang.psi.SimulaTokenType;
import org.jetbrains.annotations.NotNull;

public class Tester {
    static CharSequence source; // character data for lexing.
    static int start;     // offset to start lexing from
    static int end;       // offset to stop lexing at
    int state;           // the initial state of the lexer.

    static int SEQU;

    public static IElementType getElement() {
        if (SEQU++ > 2) return null;
        IElementType elt = new SimulaTokenType("TESTING");
        return elt;
    }

    public static TestToken nextToken() {
        if (SEQU++ > 3) return null;
        IElementType elt = new SimulaTokenType("TESTING");
        int lng = 6;
        TestToken token = new TestToken(elt, start, start + lng);
        start += lng;
//        start++;
        return token;
    }

    public static TestToken nullToken() {
        IElementType elt = new SimulaTokenType("TESTING");
        TestToken token = new TestToken(elt, 0, 0);
        return token;
    }

    public static void addSource(@NotNull CharSequence buffer, int startOffset, int endOffset) {
        System.out.println("SimulaLexer.start: start=" + startOffset + ", end=" + endOffset + ", chars=" + buffer);
        source = buffer;
        start = startOffset;
        end = endOffset;
//        state = initialState;

    }

}
