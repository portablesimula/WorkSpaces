package simula.plugin.extensions.runConfigurationExtension.run.simulaCompiler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class MyUpdateAction_TEST extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // 1. Get the ToolWindow instance using its registered ID
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Your_ToolWindow_ID");

        if (toolWindow != null) {
            // Optional: Activate the tool window to bring it to the foreground
            toolWindow.activate(null);

            // 2. Access the components within the tool window
            ContentManager contentManager = toolWindow.getContentManager();
            Content[] contents = contentManager.getContents();
            if (contents.length > 0) {
                // Get the first content/tab (index 0)
                // You will need to cast the component to your custom UI class
//                MyCustomToolWindowUI customUI = (MyCustomToolWindowUI) contents[0].getComponent();
//
//                // Now you can call methods on your custom UI component, e.g., to update a JList
//                customUI.updateData("New data to display");
            }
        }
    }
}