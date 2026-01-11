package com.intellij.openapi.editor.highlighter;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;

public interface HighlighterClient {
    Project getProject();

    void repaint(int var1, int var2);

    Document getDocument();
}
