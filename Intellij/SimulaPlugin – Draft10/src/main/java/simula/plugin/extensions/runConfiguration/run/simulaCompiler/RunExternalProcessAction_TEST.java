package simula.plugin.extensions.runConfiguration.run.simulaCompiler;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

public class RunExternalProcessAction_TEST extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        // 1. Create GeneralCommandLine (example: running 'echo Hello World' in cmd/sh)
        GeneralCommandLine commandLine = new GeneralCommandLine();
        // Adjust command based on OS:
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            commandLine.setExePath("cmd.exe");
            commandLine.addParameters("/c", "echo", "Hello World from Plugin");
        } else {
            commandLine.setExePath("/bin/sh");
            commandLine.addParameters("-c", "echo 'Hello World from Plugin'");
        }
        commandLine.setWorkDirectory(project.getBasePath());

        try {
            // 2. Create ProcessHandler
            // Use ColoredProcessHandler for ANSI color code support
            ProcessHandler processHandler = new ColoredProcessHandler(commandLine);

            // Optional: Attach a listener to show the exit code when terminated
            ProcessTerminatedListener.attach(processHandler);

            // 3. Create ConsoleView
            TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
            ConsoleView consoleView = builder.getConsole();

            // 4. Attach the console to the process handler
            consoleView.attachToProcess(processHandler);

            // 5. Display the console in a tool window
            // You need to define a tool window in your plugin.xml (e.g., id="MyCustomPluginConsole")
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow toolWindow = toolWindowManager.getToolWindow("MyCustomPluginConsole"); // Replace with your tool window ID

            if (toolWindow != null) {
                ContentFactory contentFactory = ContentFactory.getInstance();
                Content content = contentFactory.createContent(consoleView.getComponent(), "Process Output", false);
                toolWindow.getContentManager().removeAllContents(true); // Clear previous content
                toolWindow.getContentManager().addContent(content);
                toolWindow.activate(null); // Show the tool window
            }

            // 6. Start the process
            processHandler.startNotify();
        } catch (ExecutionException ex) {
            Messages.showErrorDialog(project, "Could not start the process: " + ex.getMessage(), "Error");
        }
    }
}
