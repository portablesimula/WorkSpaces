package simula.plugin.extensions.start_close;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectCloseListener;
import org.jetbrains.annotations.NotNull;
import simula.plugin.extensions.runConfiguration.SimulaRunConfiguration;
import simula.plugin.util.Util;

import java.util.List;

public class SimulaProjectCloseListener implements ProjectCloseListener {

    @Override
    public void projectClosing(@NotNull Project project) {
        // This method is called when a project is about to be closed.
        // Place your code here to write the "closed" status.
        System.out.println("Project is closing: " + project.getName());
        // Log the status or perform cleanup operations
        Util.TRACE("SimulaProjectCloseListener.projectClosing: " + project);

        List<RunConfiguration> configurations = getAllRunConfigurations(project);
        for (RunConfiguration settings : configurations) {
//            Util.TRACE("Found configuration: " + settings.getName() + ", Type: " + settings.getType().getDisplayName());
            if(settings instanceof SimulaRunConfiguration simOption) {
//                Util.TRACE("Found SimulaRunConfiguration: " + simOption.getName());
                simOption.writeConfiguration(project, simOption.getName());
            }
        }
    }

//    @Override
//    public boolean canCloseProject(@NotNull Project project) {
//        // This method is called before projectClosing().
//        // You can use this to perform checks and prevent closing if necessary (e.g., if a process is running).
//        return true;
//    }

    public static List<RunConfiguration> getAllRunConfigurations(Project project) {
        RunManager runManager = RunManager.getInstance(project);
        return runManager.getAllConfigurationsList();
    }

}
