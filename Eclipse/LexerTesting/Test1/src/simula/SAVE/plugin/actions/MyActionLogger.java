package simula.plugin.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.actions.AboutAction;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.AnActionListener;
import com.intellij.openapi.actionSystem.AnActionResult;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.impl.customFrameDecorations.header.toolbar.MainMenuButton;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import simula.plugin.util.Util;

import javax.swing.*;
import java.awt.*;
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
        if (action instanceof AboutAction) {
            doAboutAction(event.getProject());
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

    // ****************************************************************
    // *** doAboutAction
    // ****************************************************************
    /// About action
    private void doAboutAction(Project project) {
        String msg = "   This is a new Simula System created by the\n\n"
                   + "   Open Source Project '<a href=\"https://portablesimula.github.io/github.io/\">Portable Simula Revisited</a>'.\n\n"

                   + "   The project was initiated as a response to the lecture\n"
                   + "   held by James Gosling at the 50th anniversary of Simula\n"
                   + "   in Oslo on 27th September, 2017.\n\n"

                   + "   This Simula is written in Java and compiles to an\n"
                   + "   executable .jar file consisting of some Java ClassFiles.\n\n";
        Messages.showDialog(project, msg, "About Portable Simula", new String[]{"Close"}, 0, Util.getSimulaIcon());
    }


    /// Brings up an option dialog.
    /// @param msg the message to display
    /// @param title the title string for the dialog
    /// @param optionType an integer designating the options available on the dialog
    /// @param messageType an integer designating the kind of message this is
    /// @param option an array of objects indicating the possible choices the user can make
    /// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
    public static int optionDialog(final Object msg, final String title, final int optionType, final int messageType, final String... option) {
        Object OptionPaneBackground = UIManager.get("OptionPane.background");
        Object PanelBackground = UIManager.get("Panel.background");
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        int answer = JOptionPane.showOptionDialog(null, msg, title, optionType, messageType, Util.getSimulaIcon(), option, option[0]);
        // System.out.println("doClose.saveDialog: answer="+answer);
        UIManager.put("OptionPane.background", OptionPaneBackground);
        UIManager.put("Panel.background", PanelBackground);
        return (answer);
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
                + "' has been performed. Event=" + event
                + ", result=" + result.isPerformed()
        );
//      throw new RuntimeException("MyActionLogger.afterActionPerformed: ");

    }

}