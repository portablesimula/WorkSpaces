package com.simula.extensions.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class SimulaToolWindowContent {

    private final JPanel contentPanel;
    private final JBLabel myLabel = new JBLabel("Welcome to the Custom Project View!");

    public SimulaToolWindowContent(Project project) {
        // You would build your custom JTree structure here
        this.contentPanel = FormBuilder.createFormBuilder()
                .addComponent(myLabel)
                // Add your JTree component here
                .getPanel();
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}