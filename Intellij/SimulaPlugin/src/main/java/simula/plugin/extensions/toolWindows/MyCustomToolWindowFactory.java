package simula.plugin.extensions.toolWindows;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class MyCustomToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Create the UI component (e.g., a JPanel with a JTree)
        MyCustomToolWindowContent myContent = new MyCustomToolWindowContent(project);

        // Get the ContentFactory instance
//       ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        ContentFactory contentFactory = ContentFactory.getInstance();

        // Create the content object
        Content content = contentFactory.createContent(myContent.getContentPanel(), "", false);

        // Add the content to the tool window
        toolWindow.getContentManager().addContent(content);
    }
}
