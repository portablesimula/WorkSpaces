package simula.plugin.extensions.runConfigurationExtension;

import com.intellij.openapi.options.SettingsEditor;
import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulaSettingsEditor extends SettingsEditor<SimulaRunConfiguration> {
    private final JPanel myPanel;
//    private final JTextField scriptPathField;

    public SimulaSettingsEditor(SimulaRunConfiguration settings) {
        myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        myPanel.add(new JLabel("Edit: com.simula.extensions.test1.SimulaRunConfigurationEditor"));
//        myPanel.add(paramPanel());
        JButton button = new JButton("Select more options");
        // Add an ActionListener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("SimulaSettingsEditor'Button clicked!");
                // You can add more complex logic here, like updating a label, opening a new window, etc.
                settings.rtOption.selectRuntimeOptions(settings);
            }
        });
        myPanel.add(button);
        //        resetEditorFrom(settings);

    }

    @Override
    protected void resetEditorFrom(@NotNull SimulaRunConfiguration settings) {
        // Update UI from configuration settings
        // scriptPathField.setText(s.getOptions().getScriptPath());
    }

    @Override
    protected void applyEditorTo(@NotNull SimulaRunConfiguration settings) {
        // Update configuration settings from UI
        // s.getOptions().setScriptPath(scriptPathField.getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        System.out.println("MySettingsEditor.createEditor: ");
//        if(true) throw new RuntimeException("MySettingsEditor.createEditor: ");
        return myPanel;
    }
}
