package simula.plugin.extensions.start_close;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import simula.plugin.extensions.runConfiguration.SimulaRunConfiguration;
import simula.plugin.util.Util;

import java.util.List;

//public class SimulaStartupActivity implements ProjectActivity {
//    @Override
//    public @Nullable Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
//        return null;
//    }
//}

public class SimulaStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        // This code is executed after the project has been opened.
        // Place your startup logic here (e.g., showing a notification, running a check)

        hideProjectWindow(project);

//        Util.logActionList("B");
//        Util.logActionList("G");
//        Util.logActionList("N");

//        Util.logActionGroupList();

//        IdeActions
        ActionManager actionManager = ActionManager.getInstance();
//        actionManager.unregisterAction("CompileProject");
        actionManager.unregisterAction("ViewMenu");
//        actionManager.unregisterAction("NavigateMenu");
        actionManager.unregisterAction("CodeMenu");
//        actionManager.unregisterAction("RefactorMenu");
        actionManager.unregisterAction("BuildMenu");
        actionManager.unregisterAction("ToolsMenu");
//        actionManager.unregisterAction("GitMenu");

        updateAllConfigurations(project);
    }

    private void updateAllConfigurations(Project project) {
        // Get the singleton instance of RunManager for the current project
        RunManager runManager = RunManager.getInstance(project);

        // Get all run configurations and their associated settings
        List<RunnerAndConfigurationSettings> allConfigurations = runManager.getAllSettings();

        // Iterate over the configurations and process them
        for (RunnerAndConfigurationSettings settings : allConfigurations) {
            //            Util.TRACE("Found configuration: " + settings.getName() + ", Type: " + settings.getType().getDisplayName());
            if (settings instanceof SimulaRunConfiguration simOption) {
                Util.TRACE("Startup Found SimulaRunConfiguration: " + simOption.getName());
//                simOption.readConfiguration(project, simOption.getName());
            }
        }
    }

    private static void hideProjectWindow(Project project) {
        if (project == null) {
            return;
        }

        // Ensure UI operations run on the Event Dispatch Thread (EDT)
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

            // The ID for the "Project" tool window is "Project"
            ToolWindow projectToolWindow = toolWindowManager.getToolWindow("Project");

            if (projectToolWindow != null) {
                // Hide the tool window
                projectToolWindow.hide(null); // The 'null' callback is optional

                // You can also make it unavailable so its button disappears
                // projectToolWindow.setAvailable(false, null);
            }
        });
    }
}
