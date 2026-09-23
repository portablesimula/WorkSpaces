package simula.extensions;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IFolderLayout;

public class SimulaPerspectiveFactory implements IPerspectiveFactory {

    @Override
    public void createInitialLayout(IPageLayout layout) {
        // Get the editor area string id (center)
        String editorArea = layout.getEditorArea();

        // 1. Left Folder: Navigation (Simula Project Explorer)
        IFolderLayout leftFolder = layout.createFolder("left", IPageLayout.LEFT, 0.25f, editorArea);
        leftFolder.addView("org.eclipse.ui.navigator.ProjectExplorer");

        // 2. Bottom Folder: Console, Problems, and Execution Outputs
        IFolderLayout bottomFolder = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.70f, editorArea);
        bottomFolder.addView(IPageLayout.ID_PROBLEM_VIEW);
        bottomFolder.addView("org.eclipse.ui.console.ConsoleView");

        // 3. Right Folder: Simula Class/Object Outline
        IFolderLayout rightFolder = layout.createFolder("right", IPageLayout.RIGHT, 0.75f, editorArea);
        rightFolder.addView(IPageLayout.ID_OUTLINE);

        // 4. Add Shortcuts for "Window -> Show View" and "New" Wizards
        layout.addShowViewShortcut("org.eclipse.ui.navigator.ProjectExplorer");
        layout.addShowViewShortcut("org.eclipse.ui.console.ConsoleView");
        
        // Add shortcuts for custom Simula New Wizards
        layout.addNewWizardShortcut("com.simula.ide.ui.wizards.NewSimulaClassWizard");
        layout.addNewWizardShortcut("com.simula.ide.ui.wizards.NewSimulaProjectWizard");
    }
}
