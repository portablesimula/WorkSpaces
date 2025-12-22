package simula.plugin.extensions.start_close;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.scopeView.ScopeViewPane;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteIntentReadAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.events.*;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.search.scope.ProjectFilesScope;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;
import simula.plugin.util.VFS;
import simula.plugin.util.Global;
import simula.plugin.util.Util;

import javax.swing.*;
import java.io.IOException;

// simula.plugin.extensions.start_close.SimulaStartupActivity' is not assignable to 'com.intellij.openapi.startup.ProjectActivity'
public class SimulaStartupActivity implements StartupActivity {
//public class SimulaStartupActivity implements ProjectActivity {

//    @Override
//    public @Nullable Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
//        return null;
//    }

    @Override
    public void runActivity(@NotNull Project project) {
        SimulaStartupActivity.run(project);
    }

    public static void run(@NotNull Project project) {

        // This code is executed after the project has been opened.
        // Place your startup logic here (e.g., showing a notification, running a check)
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++         Simula Project Startup          +++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("A Project was started: " + project.getName());

//       hideProjectWindow(project);
        unregisterIdeActions();
//        updateAllConfigurations(project);
//        initProject(project);
//       System.out.println("Global.consoleView="+Global.consoleView);
        System.out.println("SimulaStartupActivity Completed !\n");
//        if(Global.WITH_TRACE_WINDOW) openToolWindow(project);

//        VirtualFileManager.getInstance().asyncRefresh(null);
//        ProjectView projectView = ProjectView.getInstance(project);

//      ProjectPane.ID: The standard "Project" view.
//      PackagesPane.ID: The "Packages" view.
//      ProjectFilesPane.ID: The "Project Files" view.
//      ScopeViewPane.ID: The "Scopes" or "Problems" view.
//        projectView.changeView(ProjectFilesPane.ID);
//       projectView.changeView("Scope", "Project Files");

        // 2. Change the view to "Project Files"
        // Pane ID: ScopeViewPane.ID (usually "Scope")
        // Subview ID: ProjectFilesScope.INSTANCE.getName() (usually "Project Files")
//       projectView.changeView(ScopeViewPane.ID, ProjectFilesScope.INSTANCE.getName());
        //projectView.refresh();

//        VfsUtil.markDirtyAndRefresh(true, true, true, Util.getBaseDir(project));

    }


    private static void hideProjectWindow(Project project) {
        System.out.println("SimulaStartupActivity.hideProjectWindow: "+project);
       if (project == null) return;

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

    // Several implementations of ConsoleView
    // BuildTreeConsoleView
    // BuildView
    // ConsoleViewImpl **
    // ConsoleViewWrapperBase
    // DuplexConsoleView
    // SMTRunnerConsoleView
    // TerminalExecutionConsole

    private static void unregisterIdeActions() {
        System.out.println("SimulaStartupActivity.unregisterIdeActions: ");
//        IdeActions
        ActionManager actionManager = ActionManager.getInstance();
//        actionManager.unregisterAction("CompileProject");
//        actionManager.unregisterAction("ViewMenu");
//        actionManager.unregisterAction("NavigateMenu");
        actionManager.unregisterAction("CodeMenu");
//        actionManager.unregisterAction("RefactorMenu");
        actionManager.unregisterAction("BuildMenu");
        actionManager.unregisterAction("ToolsMenu");
//        actionManager.unregisterAction("GitMenu");
    }

    //    private void updateAllConfigurations(Project project) {
//        // Get the singleton instance of RunManager for the current project
//        RunManager runManager = RunManager.getInstance(project);
//
//        // Get all run configurations and their associated settings
//        List<RunnerAndConfigurationSettings> allConfigurations = runManager.getAllSettings();
//
//        // Iterate over the configurations and process them
//        for (RunnerAndConfigurationSettings settings : allConfigurations) {
//            //            Util.TRACE("Found configuration: " + settings.getName() + ", Type: " + settings.getType().getDisplayName());
//            if (settings instanceof SimulaRunConfiguration simOption) {
//                Util.TRACE("Startup Found SimulaRunConfiguration: " + simOption.getName());
////                simOption.readConfiguration(project, simOption.getName());
//            }
//        }
//    }

}
