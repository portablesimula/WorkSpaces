package simula.plugin.extensions.runConfigurationExtension.run.simulaCompiler;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import simula.plugin.util.RTS_ConsolePanel;

import javax.swing.*;

public class ConsoleToolWindowUI extends JWindow {
    public ConsoleToolWindowUI(@NotNull Project project) {
    }

    public @Nullable JComponent getContentPanel() {
        return new RTS_ConsolePanel();
    }
}
