package simula.lsp.builder;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;

import simula.lsp.utilities.Comn;
import simula.lsp.utilities.LOG;
import simula.lsp.utilities.Util;

/// @author Øystein Myhre Andersen
public class DocumentTextUpdater {

	/// Applies a list of LSP change events sequentially to the existing source text.
    /// 
    /// @param currentText The current complete source code text.
    /// @param changes     The list of changes sent by the client.
    /// @return The updated source code text.
//    public static String OLD_applyChanges(String currentText, List<TextDocumentContentChangeEvent> changes) {
//        String updatedText = currentText;
//        for (TextDocumentContentChangeEvent change : changes) {
//            updatedText = OLD_applySingleChange(updatedText, change);
//        }
//        return updatedText;
//    }
    public static List<String> applyChanges(List<String> sourceLines, List<TextDocumentContentChangeEvent> changes) {
        for (TextDocumentContentChangeEvent change : changes) {
        	sourceLines = applySingleChange(sourceLines, change);
        }
        return sourceLines;
    }

    private static String edChange(TextDocumentContentChangeEvent change) {
    	return Util.edRange(change.getRange()) + ", text=|" + Comn.printable(change.getText() + '|');
    }
    
    private static boolean TRACE_applySingleChange = true;
    private static List<String> applySingleChange(List<String> sourceLines, TextDocumentContentChangeEvent change) {
    	if(TRACE_applySingleChange) LOG.trace("DocumentTextUpdater.applySingleChange: " + edChange(change));
        // If range is null, it means TextDocumentSyncKind.Full is used.
        // The server must replace the entire content.
        if (change.getRange() == null) {
    		List<String> newLines = change.getText().lines()
                    .map(line -> line.stripTrailing() + "\n")
                    .collect(Collectors.toList());
    		
    		if(TRACE_applySingleChange) {
    			for(String line:newLines) {
    		    	LOG.trace("DocumentTextUpdater.applySingleChange: newLine|" + Comn.printable(line) + '|');			
    			}
    		}
    		
            return newLines;
        }

        // If a range is provided, it is an incremental update.
        Range range = change.getRange();
        int startLine = range.getStart().getLine();
        int startChar = range.getStart().getCharacter();
        int endLine = range.getEnd().getLine();
        int endChar = range.getEnd().getCharacter();
        int nLines = endLine - startLine + 1;
        
        // Head is leading part of first to keep
        String head = sourceLines.get(startLine).substring(0, startChar);
        
        // Tail is tailing part of last line to keep
        String tail = sourceLines.get(endLine).substring(endChar);   
        
        StringBuilder sb = new StringBuilder();
//        sb.append(head).append(change.getText()).append(tail);
        String chgtext = change.getText().replaceAll("\r\n", "\n");
        sb.append(head).append(chgtext).append(tail);
        String text = sb.toString();
        
        if(TRACE_applySingleChange) {
	    	LOG.trace("DocumentTextUpdater.applySingleChange: head|" + Comn.printable(head) + '|');
	    	LOG.trace("DocumentTextUpdater.applySingleChange: tail|" + Comn.printable(tail) + '|');
	    	LOG.trace("DocumentTextUpdater.applySingleChange: text|" + Comn.printable(text) + "|\n");
        }
        
        while((nLines--) > 0) {
        	sourceLines.remove(startLine);
        }
        
		List<String> newLines = sb.toString().lines()
                .map(line -> line.stripTrailing() + "\n")
                .collect(Collectors.toList());
		sourceLines.addAll(startLine, newLines);
		
		if(TRACE_applySingleChange) {
			for(String line:sourceLines) {
		    	LOG.trace("DocumentTextUpdater.applySingleChange: line|" + Comn.printable(line) + '|');			
			}
		}
		
        return sourceLines;
    }

}
