package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.impl.CheckableRunConfigurationEditor;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import simula.plugin.util.Util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimulaSettingsEditor extends SettingsEditor<SimulaRunConfiguration>
//        implements CheckableRunConfigurationEditor<SimulaRunConfiguration>
{
    private final JPanel myPanel;

    public SimulaSettingsEditor(Project project, SimulaRunConfiguration settings) {
        myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        myPanel.add(settings.simOptions.selectOptionsPanel());

//        JBTextField myUsernameText = new JBTextField();
//        JBCheckBox myEnableFeature = new JBCheckBox("Enable cool feature");
//        TextFieldWithBrowseButton scriptPathField = new TextFieldWithBrowseButton();
//        scriptPathField.addBrowseFolderListener(null,
//                FileChooserDescriptorFactory.createSingleFileDescriptor().withTitle("Select Script File"));
//        myPanel = FormBuilder.createFormBuilder()
//                .addLabeledComponent("Script file", settings.simOptions.selectOptionsPanel())
//                .addComponent(myEnableFeature)
//                .addVerticalGap(10)
//                .addSeparator()
////                .addComponentFillVertically(new JPanel(), 0)
//                .addLabeledComponent("Script file", scriptPathField)
//                .addLabeledComponent("Script file", scriptPathField)
//                .addLabeledComponent("Script file", scriptPathField)
//                .getPanel();
    }

    @Override
    protected void resetEditorFrom(@NotNull SimulaRunConfiguration settings) {
        // Update UI from configuration settings
        // scriptPathField.setText(s.getOptions().getScriptPath());

        Util.TRACE("SimulaSettingsEditor.resetEditorFrom: "+settings);
        settings.simOptions.init();
    }

    @Override
    protected void applyEditorTo(@NotNull SimulaRunConfiguration settings) {
        // Update configuration settings from UI
        // s.getOptions().setScriptPath(scriptPathField.getText());

//        Util.TRACE("SimulaSettingsEditor.applyEditorTo: "+settings);
        System.out.println("SimulaSettingsEditor.applyEditorTo: "+settings);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        System.out.println("SimulaSettingsEditor.createEditor: ");
//        if(true) throw new RuntimeException("MySettingsEditor.createEditor: ");
        return myPanel;
    }

//    /**
//     * @param s
//     */
//    @Override
//    public void checkEditorData(SimulaRunConfiguration s) {
//
//    }
}
