package simula.plugin.actions.build;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import simula.plugin.util.Util;

public class TriggerBuildAction_NOT_USED extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Util.log("TriggerBuildAction.actionPerformed: "+e);
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        Util.log("TriggerBuildAction.actionPerformed: "+project);

        // The action ID for "Build Project" is "CompileProject"
        String buildActionId = "CompileProject";
        AnAction buildAction = ActionManager.getInstance().getAction(buildActionId);

        if (buildAction != null) {
            // Create a new AnActionEvent to pass to the existing action
            // It's crucial to pass the current DataContext
            Presentation presentation = buildAction.getTemplatePresentation().clone();
            DataContext context = e.getDataContext();



//  @deprecated Use {@link #createEvent(DataContext, Presentation, String, ActionUiKind, InputEvent)} or
//             * {@link #AnActionEvent(DataContext, Presentation, String, ActionUiKind, InputEvent, int, ActionManager)} instead. */
//  @Deprecated(forRemoval = true)
//  public AnActionEvent(@Nullable InputEvent inputEvent,
//                    @NotNull DataContext dataContext,
//                    @NotNull @NonNls String place,
//                    @NotNull Presentation presentation,
//                    @NotNull ActionManager actionManager,
//            @JdkConstants.InputEventMask int modifiers) {
//                this(dataContext, presentation, place, ActionUiKind.NONE, inputEvent, modifiers, actionManager);
//            }
            AnActionEvent newEvent = AnActionEvent.createEvent(
                    context,
                    presentation,
                    e.getPlace(),
                    ActionUiKind.NONE,
                    e.getInputEvent()
            );

//            AnActionEvent newEvent = new AnActionEvent(
//                    e.getInputEvent(),
//                    context,
//                    e.getPlace(),
//                    presentation,
//                    ActionManager.getInstance(),
//                    0
//            );

            // Execute the action
            buildAction.actionPerformed(newEvent);

        } else {
            Messages.showMessageDialog(project, "The 'Build Project' action could not be found.", "Error", Messages.getErrorIcon());
        }
    }
}