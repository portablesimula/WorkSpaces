package com.vogella.lsp.asciidoc.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AsciidocDocumentModel {

    // A single line of the document
    public record DocumentLine(int line, String text) {
    }

    private final List<DocumentLine> lines = new ArrayList<>();

    public AsciidocDocumentModel(String text) {
        int lineNumber = 0;
        for (String lineText : text.lines().toList()) {
            lines.add(new DocumentLine(lineNumber++, lineText));
        }
    }

    // Returns the content of the given line or null if the line does not exist
    public String getLineContent(int lineNumber) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return null;
        }
        return lines.get(lineNumber).text();
    }

    public List<DocumentLine> getResolvedLines() {
        return Collections.unmodifiableList(this.lines);
    }
}