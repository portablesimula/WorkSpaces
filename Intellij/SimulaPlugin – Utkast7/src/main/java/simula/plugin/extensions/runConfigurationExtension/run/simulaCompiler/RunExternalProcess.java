package simula.plugin.extensions.runConfigurationExtension.run.simulaCompiler;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
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
import simula.plugin.util.Util;

public class RunExternalProcess {

    public static void exec(Project project, GeneralCommandLine commandLine) throws ExecutionException {

        // 1. Create ProcessHandler
        // Use ColoredProcessHandler for ANSI color code support
//        ProcessHandler processHandler = new ColoredProcessHandler(commandLine);
        ProcessHandler processHandler = new OSProcessHandler(commandLine);

        // Optional: Attach a listener to show the exit code when terminated
        ProcessTerminatedListener.attach(processHandler);

        // 2. Create ConsoleView
        TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        ConsoleView consoleView = builder.getConsole();

        // 3. Attach the console to the process handler
        consoleView.attachToProcess(processHandler);

        // 4. Display the console in a tool window
        // You need to define a tool window in your plugin.xml (e.g., id="MyCustomPluginConsole")
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
//        ToolWindow toolWindow = toolWindowManager.getToolWindow("MyCustomPluginConsole"); // Replace with your tool window ID
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Simula"); // Replace with your tool window ID

        Util.TRACE("RunExternalProcess.exec: toolWindow="+toolWindow);
        if (toolWindow != null) {
            ContentFactory contentFactory = ContentFactory.getInstance();
            Content content = contentFactory.createContent(consoleView.getComponent(), "Process Output", false);
            toolWindow.getContentManager().removeAllContents(true); // Clear previous content
            toolWindow.getContentManager().addContent(content);
            toolWindow.activate(null); // Show the tool window
        }

        // 5. Start the process
        processHandler.startNotify();
    }
}
