package com.simula.client.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;

public class NOT_USED_SimulaLanguagePresentationReconciler extends PresentationReconciler {

    public NOT_USED_SimulaLanguagePresentationReconciler() {
        // 1. Create your rule-based code scanner (defined in Step 3)
    	NOT_USED_SimulaRuleBasedScanner scanner = new NOT_USED_SimulaRuleBasedScanner();

        // 2. Set up the Damager-Repairer for the default text partition
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
        this.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        this.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
    }
}
