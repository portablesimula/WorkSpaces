package simula.plugin.actions;

import com.intellij.ide.actions.AboutAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.AnActionListener;
import com.intellij.openapi.actionSystem.AnActionResult;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.customFrameDecorations.header.toolbar.MainMenuButton;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.event.InputEvent;


public class MyActionLogger implements AnActionListener {

    @Override
    public void beforeActionPerformed(@NotNull AnAction action, @NotNull AnActionEvent event) {
        // This method is called before an action is performed
        System.out.println("MyActionLogger: Action '"
                + action.getClass().getSimpleName() + "' is about to be performed."
//                + " with event " + event);
                  + " with action " + action.getClass());
        // You can access event details like project, data context, etc.
        // if (event.getProject() != null) {
        //     System.out.println("  in project: " + event.getProject().getName());
        // }

        // Check if the action is the specific one you are interested in
        if (action instanceof AboutAction) { // Example:
//        if (action instanceof MainMenuButton.ShowMenuAction) { // Example:
            System.out.println("AboutAction !");
            // Perform your custom logic here
            throw new RuntimeException("MyActionLogger.beforeActionPerformed: AboutAction");
        }

        // You can also check for actions by their ID using ActionManager.getInstance().getAction(actionId)
        @NonNls @NotNull String actionId = "";
        ActionManager.getInstance().getAction(actionId);

        if(action.getClass().getSimpleName().equals("CompileDirtyAction")) {
            System.out.println("MyActionLogger: TemplateText=" + action.getTemplateText());
            System.out.println("MyActionLogger: Synonyms=" + action.getSynonyms());
            System.out.println("MyActionLogger: event.Place=" + event.getPlace());
            InputEvent ie = event.getInputEvent();
            System.out.println("MyActionLogger: InputEvent=" + ie);
            // Build project menu item was selected
            Project project = event.getProject();
//            throw new RuntimeException("MyActionLogger.beforeActionPerformed: CompileDirtyAction, project="+project);
        }
//        throw new RuntimeException("MyActionLogger.beforeActionPerformed: ");
    }

    @Override
//    public void afterActionPerformed(@NotNull AnAction action, @NotNull AnActionEvent event) {
    public void afterActionPerformed(@NotNull AnAction action, @NotNull AnActionEvent event, @NotNull AnActionResult result) {
        // This method is called after an action has been performed
        ActionManager actionManager = ActionManager.getInstance();
        System.out.println("MyActionLogger: Action '"
                + action.getClass().getSimpleName()
                + "' ActionID='"
                + actionManager.getId(action)
                + "' has been performed.");
//      throw new RuntimeException("MyActionLogger.afterActionPerformed: ");
    }

}