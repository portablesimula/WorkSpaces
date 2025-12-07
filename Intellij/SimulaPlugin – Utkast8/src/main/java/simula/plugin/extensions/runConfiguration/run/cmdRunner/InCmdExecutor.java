package simula.plugin.extensions.runConfiguration.run.cmdRunner;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import com.intellij.execution.Executor;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindowId;

/**
 * @author Andrey Turbanov
 */
public class InCmdExecutor extends Executor {
    public static final Icon cmdExecutorIcon = IconLoader.getIcon("/cmd.png");
    public static final String executorId = "RunInCmdExecutor";

    @NotNull
    @Override
    public String getToolWindowId() {
        System.out.println("InCmdExecutor.getToolWindowId: ");
//        if(true) throw new RuntimeException("InCmdExecutor.getToolWindowId: ");
    return ToolWindowId.DEBUG;
    }

    @NotNull
    @Override
    public Icon getToolWindowIcon() {
//        if(true) throw new RuntimeException("InCmdExecutor.getToolWindowIcon: ");
        return cmdExecutorIcon;
    }

    @NotNull
    @Override
    public Icon getIcon() {
//        if(true) throw new RuntimeException("InCmdExecutor.getIcon: ");
        return cmdExecutorIcon;
    }

    @Override
    public Icon getDisabledIcon() {
//        if(true) throw new RuntimeException("InCmdExecutor.getDisabledIcon: ");
       return cmdExecutorIcon;
    }

    @Override
    public String getDescription()
    {
        if(true) throw new RuntimeException("InCmdExecutor.getDescription: ");
        return "Run program in cmd.exe instead of internal console";
    }

    @NotNull
    @Override
    public String getActionName() {
        if(true) throw new RuntimeException("InCmdExecutor.getActionName: ");
        return "Run in cmd";
    }

    @NotNull
    @Override
    public String getId() {
        System.out.println("InCmdExecutor.getId: ");
//        if(true) throw new RuntimeException("InCmdExecutor.getId: ");
        return executorId;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        System.out.println("InCmdExecutor.getStartActionText: ");
//        if(true) throw new RuntimeException("InCmdExecutor.getStartActionText: ");
        return "Run in cmd";
    }

    @Override
    public String getContextActionId() {
        System.out.println("InCmdExecutor.getContextActionId: ");
//        if(true) throw new RuntimeException("InCmdExecutor.getContextActionId: ");
        return "RunInCmd";
    }

    @Override
    public String getHelpId() {
        if(true) throw new RuntimeException("InCmdExecutor.getHelpId: ");
        return null;
    }
}