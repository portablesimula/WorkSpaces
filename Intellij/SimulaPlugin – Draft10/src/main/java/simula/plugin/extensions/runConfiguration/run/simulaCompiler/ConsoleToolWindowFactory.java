package simula.plugin.extensions.runConfiguration.run.simulaCompiler;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class ConsoleToolWindowFactory implements ToolWindowFactory {
//    @Override
//    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
//
//    }

//    @Override
//    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
//        // Create your UI component here (e.g., a JPanel)
//        MyToolWindowUI myToolWindowUI = new MyToolWindowUI(project);
//        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
//        Content content = contentFactory.createContent(myToolWindowUI.getContentPanel(), "", false);
//        toolWindow.getContentManager().addContent(content);
//    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Instantiate your custom UI component class
        ConsoleToolWindowUI consoleToolWindowUI = new ConsoleToolWindowUI(project);

        // Get the ContentFactory instance
        ContentFactory contentFactory = ContentFactory.getInstance();

        // Create a Content object from your UI component's main JPanel
        Content content = contentFactory.createContent(consoleToolWindowUI.getContentPanel(), "", false);

        // Add the content to the tool window's content manager
        toolWindow.getContentManager().addContent(content);
    }

}