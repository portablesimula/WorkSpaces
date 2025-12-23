package simula.plugin.actions.toolsMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

public class SimulaOptions extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // Implement your plugin's logic here
        Messages.showMessageDialog(e.getProject(), "Hello from My Plugin!", "My Plugin", Messages.getInformationIcon());
    }
}