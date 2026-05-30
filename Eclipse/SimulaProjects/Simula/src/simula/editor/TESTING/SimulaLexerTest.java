package simula.editor.TESTING;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimulaLexerTest {

    // Definerer de ulike tokentypene i Simula
    public enum TokenType {
        KEYWORD, IDENTIFIER, INTEGER, REAL, STRING, CHARACTER,
        ASSIGN, REF_ASSIGN, EQ, NE, LT, LE, GT, GE,
        PLUS, MINUS, MULT, DIV, INT_DIV,
        LPAREN, RPAREN, LBRACKET, RBRACKET, SEMICOLON, COLON, COMMA, DOT,
        EOF, ERROR
    }

    public static class Token {
        public final TokenType type;
        public final String value;
        public final int line;

        public Token(TokenType type, String value, int line) {
            this.type = type;
            this.value = value;
            this.line = line;
        }

        @Override
        public String toString() {
            return String.format("Linje %d: [%s] '%s'", line, type, value);
        }
    }

    private final String source;
    private final int length;
    private int pos = 0;
    private int line = 1;

    private static final Set<String> KEYWORDS = new HashSet<>();
    static {
        // Standard Simula 67 nøkkelord (case-insensitive)
        String[] kws = {
            "BEGIN", "END", "COMMENT", "TRUE", "FALSE", "IF", "THEN", "ELSE",
            "FOR", "DO", "WHILE", "UNTIL", "STEP", "SWITCH", "PROCEDURE",
            "CLASS", "REF", "NEW", "NONE", "INNER", "IN", "IS", "QUA",
            "INTEGER", "REAL", "BOOLEAN", "CHARACTER", "TEXT", "ARRAY",
            "VALUE", "NAME", "EXTERNAL", "INSPECT", "WHEN"
        };
        for (String kw : kws) {
            KEYWORDS.add(kw.toUpperCase());
        }
    }

    public SimulaLexerTest(String source) {
        this.source = source;
        this.length = source.length();
    }

    private char peek() {
        if (pos >= length) return '\0';
        return source.charAt(pos);
    }

    private char peekNext() {
        if (pos + 1 >= length) return '\0';
        return source.charAt(pos + 1);
    }

    private char advance() {
        if (pos >= length) return '\0';
        char c = source.charAt(pos++);
        if (c == '\n') line++;
        return c;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        
        while (pos < length) {
            char c = peek();

            // 1. Håndter mellomrom og linjeskift
            if (Character.isWhitespace(c)) {
                advance();
                continue;
            }

            // 2. Tall (Integer og Real)
            if (Character.isDigit(c)) {
                tokens.add(readNumber());
                continue;
            }

            // 3. Identifikatorer og Nøkkelord
            if (Character.isLetter(c) || c == '_') {
                Token t = readIdentifierOrKeyword();
                tokens.add(t);

                // Spesialhåndtering for "COMMENT" i Simula
                if (t.type == TokenType.KEYWORD && t.value.equalsIgnoreCase("COMMENT")) {
                    skipCommentKeyword();
                }
                // Spesialhåndtering for "END" kommentarer (alt etter END fram til ';' eller 'END'/'ELSE')
                if (t.type == TokenType.KEYWORD && t.value.equalsIgnoreCase("END")) {
                    skipEndComment();
                }
                continue;
            }

            // 4. Tekststrenger (Strings)
            if (c == '"') {
                tokens.add(readString());
                continue;
            }

            // 5. Karakterer (Character)
            if (c == '\'') {
                tokens.add(readCharacter());
                continue;
            }

            // 6. Operatorer og Skilletegn
            advance(); // Konsumer tegnet
            switch (c) {
                // Sammensatte operatorer
                case ':':
                    if (peek() == '=') { advance(); tokens.add(new Token(TokenType.ASSIGN, ":=", line)); }
                    else if (peek() == '-') { advance(); tokens.add(new Token(TokenType.REF_ASSIGN, ":-", line)); }
                    else { tokens.add(new Token(TokenType.COLON, ":", line)); }
                    break;
                case '=':
                    if (peek() == '=') { advance(); tokens.add(new Token(TokenType.EQ, "==", line)); }
                    else { tokens.add(new Token(TokenType.EQ, "=", line)); } // Simula bruker ofte enkelt '=' for likhet
                    break;
                case '<':
                    if (peek() == '=') { advance(); tokens.add(new Token(TokenType.LE, "<=", line)); }
                    else { tokens.add(new Token(TokenType.LT, "<", line)); }
                    break;
                case '>':
                    if (peek() == '=') { advance(); tokens.add(new Token(TokenType.GE, ">=", line)); }
                    else { tokens.add(new Token(TokenType.GT, ">", line)); }
                    break;
                case '/':
                    if (peek() == '/') { advance(); tokens.add(new Token(TokenType.INT_DIV, "//", line)); }
                    else if (peek() == '=') { 
                        advance(); 
                        if (peek() == '=') { advance(); tokens.add(new Token(TokenType.NE, "=/=", line)); }
                        else { tokens.add(new Token(TokenType.ERROR, "=/", line)); }
                    }
                    else { tokens.add(new Token(TokenType.DIV, "/", line)); }
                    break;
                
                // Enkle tegn
                case '+': tokens.add(new Token(TokenType.PLUS, "+", line)); break;
                case '-': tokens.add(new Token(TokenType.MINUS, "-", line)); break;
                case '*': tokens.add(new Token(TokenType.MULT, "*", line)); break;
                case '(': tokens.add(new Token(TokenType.LPAREN, "(", line)); break;
                case ')': tokens.add(new Token(TokenType.RPAREN, ")", line)); break;
                case '[': tokens.add(new Token(TokenType.LBRACKET, "[", line)); break;
                case ']': tokens.add(new Token(TokenType.RBRACKET, "]", line)); break;
                case ';': tokens.add(new Token(TokenType.SEMICOLON, ";", line)); break;
                case ',': tokens.add(new Token(TokenType.COMMA, ",", line)); break;
                case '.': tokens.add(new Token(TokenType.DOT, ".", line)); break;
                
                default:
                    tokens.add(new Token(TokenType.ERROR, String.valueOf(c), line));
                    break;
            }
        }
        
        tokens.add(new Token(TokenType.EOF, "EOF", line));
        return tokens;
    }

    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        boolean isReal = false;

        while (Character.isDigit(peek())) {
            sb.append(advance());
        }

        // Håndterer desimaltall (f.eks. 3.14)
        if (peek() == '.' && Character.isDigit(peekNext())) {
            isReal = true;
            sb.append(advance()); // '.'
            while (Character.isDigit(peek())) {
                sb.append(advance());
            }
        }

        // Håndterer eksponentform (f.eks. 10&6 eller 2.5&(-2) i Simula standarden, ofte '&' eller 'E')
        if (peek() == '&' || peek() == 'E' || peek() == 'e') {
            isReal = true;
            sb.append(advance());
            if (peek() == '+' || peek() == '-') {
                sb.append(advance());
            }
            while (Character.isDigit(peek())) {
                sb.append(advance());
            }
        }

        return new Token(isReal ? TokenType.REAL : TokenType.INTEGER, sb.toString(), line);
    }

    private Token readIdentifierOrKeyword() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetterOrDigit(peek()) || peek() == '_') {
            sb.append(advance());
        }
        String value = sb.toString();
        if (KEYWORDS.contains(value.toUpperCase())) {
            return new Token(TokenType.KEYWORD, value, line);
        }
        return new Token(TokenType.IDENTIFIER, value, line);
    }

    private Token readString() {
        StringBuilder sb = new StringBuilder();
        advance(); // Konsumer start-sitattegn "
        while (peek() != '"' && pos < length) {
            sb.append(advance());
        }
        if (pos >= length) return new Token(TokenType.ERROR, "Uavsluttet streng: " + sb.toString(), line);
        advance(); // Konsumer slutt-sitattegn "
        return new Token(TokenType.STRING, sb.toString(), line);
    }

    private Token readCharacter() {
        advance(); // Konsumer start '
        StringBuilder sb = new StringBuilder();
        if (peek() != '\'' && pos < length) {
            sb.append(advance());
        }
        if (peek() != '\'') return new Token(TokenType.ERROR, "Ugyldig karakterkonstant", line);
        advance(); // Konsumer slutt '
        return new Token(TokenType.CHARACTER, sb.toString(), line);
    }

    // Skipper alt etter "COMMENT" fram til neste semikolon
    private void skipCommentKeyword() {
        while (peek() != ';' && pos < length) {
            advance();
        }
    }

    // I Simula er tekst etter "END" regnet som kommentar fram til neste semikolon, END, ELSE eller WHEN
    private void skipEndComment() {
        StringBuilder sb = new StringBuilder();
        while (pos < length) {
            char c = peek();
            if (c == ';') {
                break;
            }
            // Sjekk om vi treffer et nytt nøkkelord som avslutter end-kommentaren
            if (Character.isLetter(c)) {
                int savedPos = pos;
                Token nextT = readIdentifierOrKeyword();
                pos = savedPos; // Rull tilbake slik at hovedloopen kan lese det ordentlig
                String val = nextT.value.toUpperCase();
                if (val.equals("END") || val.equals("ELSE") || val.equals("WHEN")) {
                    break;
                }
            }
            advance();
        }
    }

    // Test-kjøring av Lexeren
    public static void main(String[] args) {
        String simulaCode = """
            BEGIN
                ARRAY ARR(-20:-5);
                INTEGER i, j;
                REAL x;
                COMMENT Dette er en kommentar;
                i := 10;
                x := 3.14&2;
                IF i == 10 THEN
                    j :- NONE;
                END kommentar etter end her;
            END
            """;

        SimulaLexerTest lexer = new SimulaLexerTest(simulaCode);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
