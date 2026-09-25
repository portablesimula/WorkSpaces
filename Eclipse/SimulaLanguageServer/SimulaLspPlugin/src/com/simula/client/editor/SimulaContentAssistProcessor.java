package com.simula.client.editor;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import java.util.ArrayList;
import java.util.List;

public class SimulaContentAssistProcessor implements IContentAssistProcessor {

    private static final String[] KEYWORDS = { "select", "where", "insert", "delete" };

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        List<ICompletionProposal> proposals = new ArrayList<>();
        
        // Simple example: suggest all keywords regardless of prefix
        for (String keyword : KEYWORDS) {
            // Replacement string, replacement offset, replacement length, cursor position after replacement
            proposals.add(new CompletionProposal(keyword, offset, 0, keyword.length()));
        }
        
        return proposals.toArray(new ICompletionProposal[0]);
    }

    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return null; // Return context hints if needed
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return new char[] { '.' }; // Trigger autocomplete automatically when '.' is typed
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }
}
