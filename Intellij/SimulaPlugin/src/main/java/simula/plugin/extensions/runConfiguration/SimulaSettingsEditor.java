package simula.plugin.extensions.runConfiguration;

import com.intellij.openapi.options.SettingsEditor;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Map;

public class SimulaSettingsEditor extends SettingsEditor<SimulaRunConfiguration> {

    private final JPanel myPanel;
//    private final TextFieldWithBrowseButton scriptPathField;
    Map<String, String> optionsMap;

    private SelectOptionsPanel selectOptionsPanel;

    public SimulaSettingsEditor() {
//        scriptPathField = new TextFieldWithBrowseButton();
//        scriptPathField.addBrowseFolderListener(null,
//                FileChooserDescriptorFactory.createSingleFileDescriptor().withTitle("Select .jar Directory"));
        selectOptionsPanel = new SelectOptionsPanel(this);
        myPanel = FormBuilder.createFormBuilder()
//                .addLabeledComponent(".jar Dir", scriptPathField)
                .addComponent(selectOptionsPanel)
                .getPanel();
    }

    @Override
    protected void resetEditorFrom(SimulaRunConfiguration simulaRunConfiguration) {
//        scriptPathField.setText(demoRunConfiguration.getJarFileDirName());
        optionsMap = simulaRunConfiguration.getOptionsMap();
    }

    @Override
    protected void applyEditorTo(@NotNull SimulaRunConfiguration simulaRunConfiguration) {
//        demoRunConfiguration.setJarFileDirName(scriptPathField.getText());
        simulaRunConfiguration.setOptionsMap(optionsMap);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

}