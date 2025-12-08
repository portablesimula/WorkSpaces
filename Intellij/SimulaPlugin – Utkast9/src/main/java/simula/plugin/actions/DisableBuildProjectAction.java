package simula.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DisableBuildProjectAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // This method is called when the action is invoked.
        // Since we are disabling it, this code might not be reached.
        // If you want to perform some custom logic when the action is "invoked"
        // even if disabled, you can add it here.
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // This method is called to update the state of the action.
        // Here, we disable the action.
        Project project = e.getProject();
        if (project != null) {
            e.getPresentation().setEnabledAndVisible(false); // Disables and hides the action
            // Or, to only disable but keep visible:
            // e.getPresentation().setEnabled(false);
        } else {
            // If no project is open, you might want to hide or disable it as well.
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}