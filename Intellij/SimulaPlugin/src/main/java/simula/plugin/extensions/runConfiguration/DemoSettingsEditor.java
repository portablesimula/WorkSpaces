package simula.plugin.extensions.runConfiguration;

import com.intellij.openapi.components.StoredProperty;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class DemoSettingsEditor extends SettingsEditor<DemoRunConfiguration> {

    private final JPanel myPanel;
    private final TextFieldWithBrowseButton scriptPathField;
    private Map<String, String> options;
    private JPanel selectOptionsPanel;

//    public SimulaSettingsEditor(Project project, SimulaRunConfiguration settings) {
//    public DemoSettingsEditor(DemoRunConfigurationOptions settings) {
    public DemoSettingsEditor() {
        scriptPathField = new TextFieldWithBrowseButton();
        scriptPathField.addBrowseFolderListener(null,
                FileChooserDescriptorFactory.createSingleFileDescriptor().withTitle("Select .jar Directory"));
        options = new HashMap<String, String>();
        setDefaults();

        selectOptionsPanel = new SelectOptionsPanel(options);
        myPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Script file", scriptPathField)
                .addComponent(selectOptionsPanel)
                .getPanel();
    }


    private void setDefaults() {
        options.put("simula.compiler.compilerMode", "directClassFiles");
        options.put("simula.compiler.caseSensitive", "false");
        options.put("simula.compiler.verbose", "true");
        options.put("simula.compiler.noExecution", "false");
        options.put("simula.compiler.warnings", "false");
        options.put("simula.compiler.extensions", "true");
        options.put("simula.runtime.verbose", "true");
        options.put("simula.runtime.blockTracing", "false");
        options.put("simula.runtime.gotoTracing", "false");
        options.put("simula.runtime.qpsTracing", "false");
        options.put("simula.runtime.smlTracing", "false");

    }

    @Override
    protected void resetEditorFrom(DemoRunConfiguration demoRunConfiguration) {
        scriptPathField.setText(demoRunConfiguration.getScriptName());
        options = demoRunConfiguration.getOptionsMap();
    }

    @Override
    protected void applyEditorTo(@NotNull DemoRunConfiguration demoRunConfiguration) {
        demoRunConfiguration.setScriptName(scriptPathField.getText());
        demoRunConfiguration.setOptionsMap(options);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

}