package simula.plugin.extensions.start_close;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.scopeView.ScopeViewPane;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.newvfs.events.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
//        unregisterIdeActions();
//        updateAllConfigurations(project);
//        initProject(project);
//       System.out.println("Global.consoleView="+Global.consoleView);
        System.out.println("SimulaStartupActivity Completed !\n");
//        if(Global.WITH_TRACE_WINDOW) openToolWindow(project);

//        VirtualFileManager.getInstance().asyncRefresh(null);
        ProjectView projectView = ProjectView.getInstance(project);

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
//        projectView.changeView("ProjectPane", "Project Files");
//        projectView.changeView("Scope", "Project Files");

        switchToProjectFilesView(project);
    }

    private static void switchToProjectFilesView(Project project) {
        ProjectView projectView = ProjectView.getInstance(project);
        ApplicationManager.getApplication().invokeLater(() -> {
            // "Project Files" is a specific scope within the ScopeViewPane
            // The ID for the Scope view pane is typically "Scope"
            projectView.changeView(ScopeViewPane.ID, "Project Files");
        });

     }

    public static void unregisterIdeActions() {
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

}
