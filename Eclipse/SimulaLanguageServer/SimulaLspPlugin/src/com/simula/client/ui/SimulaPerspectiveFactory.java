package com.simula.client.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class SimulaPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.25f, editorArea);
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, 0.70f, editorArea);
		layout.addView(IPageLayout.ID_PROBLEM_VIEW, IPageLayout.BOTTOM, 0.75f, editorArea);
		layout.setEditorOnboardingText("Welcome to Simula IDE\n\n• Press Ctrl+N to create a new file\n• ...");
        layout.setEditorOnboardingImageUri("platform:/plugin/com.simula.ide/icons/sim.png");
	}
}
