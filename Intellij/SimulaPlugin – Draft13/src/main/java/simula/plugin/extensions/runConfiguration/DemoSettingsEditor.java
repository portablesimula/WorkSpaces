package simula.plugin.extensions.runConfiguration;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Map;

public class DemoSettingsEditor extends SettingsEditor<DemoRunConfiguration> {

    private final JPanel myPanel;
    private final TextFieldWithBrowseButton scriptPathField;
    Map<String, String> optionsMap;

    private SelectOptionsPanel selectOptionsPanel;

    public DemoSettingsEditor() {
        scriptPathField = new TextFieldWithBrowseButton();
        scriptPathField.addBrowseFolderListener(null,
                FileChooserDescriptorFactory.createSingleFileDescriptor().withTitle("Select .jar Directory"));
        selectOptionsPanel = new SelectOptionsPanel(this);
        myPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(".jar Dir", scriptPathField)
                .addComponent(selectOptionsPanel)
                .getPanel();
    }

    @Override
    protected void resetEditorFrom(DemoRunConfiguration demoRunConfiguration) {
//        Util.TRACE("DemoSettingsEditor.resetEditorFrom: "+demoRunConfiguration);
        scriptPathField.setText(demoRunConfiguration.getJarFileDirName());
        optionsMap = demoRunConfiguration.getOptionsMap();
    }

    @Override
    protected void applyEditorTo(@NotNull DemoRunConfiguration demoRunConfiguration) {
//        Util.TRACE("DemoSettingsEditor.applyEditorTo: "+demoRunConfiguration);
        demoRunConfiguration.setJarFileDirName(scriptPathField.getText());
        demoRunConfiguration.setOptionsMap(optionsMap);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

}