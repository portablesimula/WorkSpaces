package com.simula.client.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class MyCustomScanner extends RuleBasedScanner {

    public MyCustomScanner() {
        // 1. Define text attributes (colors & styles)
        Color keywordColor = new Color(Display.getCurrent(), 127, 0, 85); // Purple
        Color stringColor = new Color(Display.getCurrent(), 42, 0, 255);   // Blue
        
        IToken keywordToken = new Token(new TextAttribute(keywordColor, null, org.eclipse.swt.SWT.BOLD));
        IToken stringToken = new Token(new TextAttribute(stringColor));
        IToken defaultToken = new Token(new TextAttribute(null)); // Default text

        // 2. Build your rules array
        IRule[] rules = new IRule[3];

        // Rule for strings (e.g., "text")
        rules[0] = new SingleLineRule("\"", "\"", stringToken, '\\');

        // Rule for whitespace (skips formatting)
        rules[1] = new WhitespaceRule(new IWhitespaceDetector() {
            @Override
            public boolean isWhitespace(char c) {
                return Character.isWhitespace(c);
            }
        });

        // Rule for detecting keywords
        WordRule wordRule = new WordRule(new IWordDetector() {
            @Override
            public boolean isWordStart(char c) {
                return Character.isJavaIdentifierStart(c);
            }
            @Override
            public boolean isWordPart(char c) {
                return Character.isJavaIdentifierPart(c);
            }
        }, defaultToken);

        // Add specific keywords to the word rule
        wordRule.addWord("if", keywordToken);
        wordRule.addWord("else", keywordToken);
        wordRule.addWord("while", keywordToken);
        wordRule.addWord("begin", keywordToken);
        wordRule.addWord("integer", keywordToken);
        wordRule.addWord("end", keywordToken);
        
        rules[2] = wordRule;

        // 3. Register rules and the fallback token
        setRules(rules);
        setDefaultReturnToken(defaultToken);
    }
}
