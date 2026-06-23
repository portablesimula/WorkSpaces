package simula.compiler;

import java.util.ArrayList;
import java.util.List;

public class SimulaLexer {
    public enum TokenType { KEYWORD, IDENTIFIER, NUMBER, ASSIGN, SEMICOLON, EOF }

    public static class Token {
        public final TokenType type;
        public final String value;
        public final int line;
        public final int character;

        public Token(TokenType type, String value, int line, int character) {
            this.type = type;
            this.value = value;
            this.line = line;
            this.character = character;
        }
    }

    public static List<Token> tokenize(String source) {
        List<Token> tokens = new ArrayList<>();
        String[] lines = source.split("\r?\n");

        for (int l = 0; l < lines.length; l++) {
            String line = lines[l];
            int c = 0;
            while (c < line.length()) {
                char ch = line.charAt(c);
                if (Character.isWhitespace(ch)) { c++; continue; }

                if (ch == ';') {
                    tokens.add(new Token(TokenType.SEMICOLON, ";", l, c));
                    c++;
                } else if (ch == ':' && c + 1 < line.length() && line.charAt(c + 1) == '=') {
                    tokens.add(new Token(TokenType.ASSIGN, ":=", l, c));
                    c += 2;
                } else if (Character.isDigit(ch)) {
                    int start = c;
                    while (c < line.length() && Character.isDigit(line.charAt(c))) c++;
                    tokens.add(new Token(TokenType.NUMBER, line.substring(start, c), l, start));
                } else if (Character.isLetter(ch)) {
                    int start = c;
                    while (c < line.length() && (Character.isLetterOrDigit(line.charAt(c)))) c++;
                    String text = line.substring(start, c);
                    TokenType type = text.equalsIgnoreCase("BEGIN") || text.equalsIgnoreCase("END") || text.equalsIgnoreCase("INTEGER") ? TokenType.KEYWORD : TokenType.IDENTIFIER;
                    tokens.add(new Token(TokenType.KEYWORD, text, l, start));
                } else {
                    c++; // Hopper over ukjente tegn i denne enkle versjonen
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, "", lines.length, 0));
        return tokens;
    }
}
