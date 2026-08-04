package simula.compiler;

import simula.SimTextDocumentContentChangeEvent;
import simula.lsp.util.SimPosition;
import simula.lsp.util.SimRange;

import java.util.List;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class DocumentTextUpdater {

	/// Applies a list of LSP change events sequentially to the existing source text.
    /// 
    /// @param currentText The current complete source code text.
    /// @param changes     The list of changes sent by the client.
    /// @return The updated source code text.
    public static String applyChanges(String currentText, List<SimTextDocumentContentChangeEvent> changes) {
        String updatedText = currentText;
        for (SimTextDocumentContentChangeEvent change : changes) {
            updatedText = applySingleChange(updatedText, change);
        }
        return updatedText;
    }

    private static String applySingleChange(String text, SimTextDocumentContentChangeEvent change) {
        // If range is null, it means TextDocumentSyncKind.Full is used.
        // The server must replace the entire content.
        if (change.getRange() == null) {
            return change.getText();
        }

        // If a range is provided, it is an incremental update.
        SimRange range = change.getRange();
        int startOffset = getOffset(text, range.getStart());
        int endOffset = getOffset(text, range.getEnd());

        // Reconstruct the string by swapping the old range with the new text chunk
        StringBuilder sb = new StringBuilder(text);
        sb.replace(startOffset, endOffset, change.getText());
        return sb.toString();
    }

    /// Translates an LSP Line/Character position into a 0-indexed flat string index.
    private static int getOffset(String text, SimPosition position) {
        int targetLine = position.getLine();
        int targetChar = position.getCharacter();
        
        int currentLine = 0;
        int offset = 0;
        int textLength = text.length();

        // Scan characters sequentially to find line boundaries
        while (currentLine < targetLine && offset < textLength) {
            char c = text.charAt(offset);
            if (c == '\r') {
                // Handle CRLF newlines safely
                if (offset + 1 < textLength && text.charAt(offset + 1) == '\n') {
                    offset++;
                }
                currentLine++;
            } else if (c == '\n') {
                currentLine++;
            }
            offset++;
        }

        // Add the character column position to find the final target index
        return Math.min(offset + targetChar, textLength);
    }
}
