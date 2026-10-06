package com.simula.client.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class SimulaPerspectiveFactory implements IPerspectiveFactory {

    @Override
    public void createInitialLayout(IPageLayout layout) {
        // Hent editorens område (hvor Simula-kildekoden skal skrives)
        String editorArea = layout.getEditorArea();
        
        // Legg til Package Explorer til venstre for editoren (25% av bredden)
        layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.25f, editorArea);
        
        // Legg til Outline-visningen til høyre for editoren (30% av bredden)
        layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, 0.70f, editorArea);
        
        // Legg til Problem-visningen og konsollen i bunnen
        layout.addView(IPageLayout.ID_PROBLEM_VIEW, IPageLayout.BOTTOM, 0.75f, editorArea);
    }

}
