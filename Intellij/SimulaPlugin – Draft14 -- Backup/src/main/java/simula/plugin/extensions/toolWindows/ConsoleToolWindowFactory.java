package simula.plugin.extensions.toolWindows;

import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import simula.plugin.util.Util;

public class ConsoleToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Use the factory to create a ConsoleView instance
        TextConsoleBuilder textConsoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        ConsoleView consoleView = textConsoleBuilder.getConsole();

        ApplicationManager.getApplication().invokeLater(() -> {
            // Copy file within a WriteAction
            ReadAction.run(() -> {
                try {

                    // Get the ContentFactory instance to create content for the tool window
                    ContentFactory contentFactory = ContentFactory.getInstance();
                    Content content = contentFactory.createContent(consoleView.getComponent(), "Output", false);
                    toolWindow.getContentManager().addContent(content);

                    // Add the content to the tool window
                    toolWindow.getContentManager().addContent(content);

                    // Optional: Print an initial message to the console
                    consoleView.print("Simula Console Initialized!\n", ConsoleViewContentType.NORMAL_OUTPUT);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

    }
}