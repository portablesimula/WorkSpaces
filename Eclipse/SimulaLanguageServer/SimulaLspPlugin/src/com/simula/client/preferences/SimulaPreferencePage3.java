package com.simula.client.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;

import java.util.List;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.simula.client.DEF;
import com.simula.client.SimulaPluginActivator;


/// extension point="org.eclipse.ui.preferencePages"
public class SimulaPreferencePage3 extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	   // Define a constant for the preference key
    public static final String P_VERBOSE = "verbosePreference";
    
    public SimulaPreferencePage3() {
        // Use GRID layout and tie the page to your plugin's preference store
        super(GRID);
        setPreferenceStore(SimulaPluginActivator.getDefault().getPreferenceStore());
        setDescription("Configure Options for the Simula language server.");
    }

    @Override
    public void init(IWorkbench workbench) {
        // Initialization code if needed
    }

    
    public static void addCommands(List<String> commands) {
    	IPreferenceStore preferenceStore = SimulaPluginActivator.getDefault().getPreferenceStore();
    	if(preferenceStore.getBoolean(DEF.P_RTS_VERBOSE))       commands.add(DEF.P_RTS_VERBOSE);  // "-RTS-verbose";
    	if(preferenceStore.getBoolean(DEF.P_RTS_NOPOPUP))       commands.add(DEF.P_RTS_NOPOPUP);  // "-RTS-noPopup";
    	if(preferenceStore.getBoolean(DEF.P_RTS_BLOCK_TRACING)) commands.add(DEF.P_RTS_BLOCK_TRACING);  // "-RTS-block-tracing";
    	if(preferenceStore.getBoolean(DEF.P_RTS_GOTO_TRACING))  commands.add(DEF.P_RTS_GOTO_TRACING);  // "-RTS-goto-tracing";
    	if(preferenceStore.getBoolean(DEF.P_RTS_QPS_TRACING))   commands.add(DEF.P_RTS_QPS_TRACING);  // "-RTS-qps-tracing";
    	if(preferenceStore.getBoolean(DEF.P_RTS_SML_TRACING))   commands.add(DEF.P_RTS_SML_TRACING);  // "-RTS-sml-tracing";

//    	if(preferenceStore.getBoolean(DEF. SOURCE_FILE)) commands.add(DEF.SOURCE_FILE);  // "" = "-RTS-source-file";
//    	if(preferenceStore.getBoolean(DEF.RUNTIME_USER_DIR)) commands.add(DEF.RUNTIME_USER_DIR);  // "-RTS-user-dir";
    }
    
    @Override
    protected void createFieldEditors() {
    	Composite parent = getFieldEditorParent();

    	// Runtime Options
        Group runtimeGroup = createGroup(parent, "Runtime Options");
    	addField(new BooleanFieldEditor(DEF.P_RTS_VERBOSE, "verbose", runtimeGroup));
    	addField(new BooleanFieldEditor(DEF.P_RTS_NOPOPUP, "noPopup", runtimeGroup));
    	addField(new BooleanFieldEditor(DEF.P_RTS_BLOCK_TRACING, "block-tracing", runtimeGroup));
    	addField(new BooleanFieldEditor(DEF.P_RTS_GOTO_TRACING, "goto-tracing", runtimeGroup));
    	addField(new BooleanFieldEditor(DEF.P_RTS_QPS_TRACING, "qps-tracing", runtimeGroup));
    	addField(new BooleanFieldEditor(DEF.P_RTS_SML_TRACING, "sml-tracing", runtimeGroup));

//    	/** Runtime Options */ public static final String SOURCE_FILE = "", "source-file", runtimeGroup);
//    	/** Runtime Options */ public static final String RUNTIME_USER_DIR, "user-dir", runtimeGroup);

    	
    }

    /**
     * Helper method to create a titled visual group.
     */
    private Group createGroup(Composite parent, String title) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(title);
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 1;
        group.setLayoutData(gd);
        
        return group;
    }

}
