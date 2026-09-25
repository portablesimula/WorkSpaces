package com.simula.client.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class MyLanguageCodeScanner extends RuleBasedScanner {

    public MyLanguageCodeScanner() {
        // Define your colors (Ensure management/disposal of colors in real plugins)
        Color keywordColor = new Color(Display.getCurrent(), 127, 0, 85); // Purple
        Color stringColor = new Color(Display.getCurrent(), 42, 0, 255);  // Blue

        // Define Tokens linked to TextAttributes
        Token keywordToken = new Token(new TextAttribute(keywordColor, null, SWT.BOLD));
        Token stringToken = new Token(new TextAttribute(stringColor));

        IRule[] rules = new IRule[2];

        // Rule for Strings (e.g., "text" or 'text')
        rules[0] = new SingleLineRule("\"", "\"", stringToken, '\\');

        // Rule for Keywords
        WordRule wordRule = new WordRule(new Token(null)); // Default text token
        wordRule.addWord("if", keywordToken);
        wordRule.addWord("else", keywordToken);
        wordRule.addWord("while", keywordToken);
        wordRule.addWord("function", keywordToken);
        rules[1] = wordRule;

        setRules(rules);
    }
}
