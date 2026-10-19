package com.simula.client.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;

import java.util.List;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.simula.client.DEF;
import com.simula.client.SimulaPluginActivator;


/// extension point="org.eclipse.ui.preferencePages"
public class SimulaPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public SimulaPreferencePage() {
        // Use GRID layout and tie the page to your plugin's preference store
        super(GRID);
        setPreferenceStore(SimulaPluginActivator.getDefault().getPreferenceStore());
        setDescription("Configure general settings for the Simula language plugin.");
    }

    @Override
    public void init(IWorkbench workbench) {
        // Initialization code if needed
    }

    @Override
    protected void createFieldEditors() {
    	Composite parent = getFieldEditorParent();

    	addField(new BooleanFieldEditor(DEF.P_VERBOSE, "&Produce verbose tracing", parent));
    	addField(new BooleanFieldEditor(DEF.P_CASE_SENSITIVE, "&Case sensitive source files", parent));
    	addField(new BooleanFieldEditor(DEF.P_LANUAGE_EXTENSIONS, "&Add Simula language extensions", parent));
    	addField(new BooleanFieldEditor(DEF.P_WARNINGS, "&Show warning messages", parent));
 
        addCompilerModeChooser();
//        addTraceModeChooser();
//        addKeepJavaFileChooser();
    }
    
    public static void addCommands(List<String> commands) {
	    IPreferenceStore preferenceStore = SimulaPluginActivator.getDefault().getPreferenceStore();
	    if(preferenceStore.getBoolean(DEF.P_VERBOSE))              commands.add("-verbose");
	    if(preferenceStore.getBoolean(DEF.P_CASE_SENSITIVE))       commands.add("-caseSensitive");
	    if(! preferenceStore.getBoolean(DEF.P_LANUAGE_EXTENSIONS)) commands.add("-noextension");
	    if(! preferenceStore.getBoolean(DEF.P_WARNINGS))           commands.add("-nowarn");
	    
	    String compilerMode = preferenceStore.getString(DEF.SIMULA_COMPILER_MODE);
	    if(compilerMode.equals(DEF.CHOICE_DIRECT_CLASS_FILES)) {
	    	commands.add("-compilerMode");
	    	commands.add("directClassFiles");
	    }
	    if(compilerMode.equals(DEF.CHOICE_VIA_JAVA_SOURCES)) {
	    	commands.add("-compilerMode");
	    	commands.add("viaJavaSource");
	    }
    }
    
    private void addCompilerModeChooser() {
    	Composite parent = getFieldEditorParent();
        // Defines the labels and string data mappings for the 2 choices
        String[][] labelAndValues = new String[][] {
            { "Create Class files directly using the ClassFile API", DEF.CHOICE_DIRECT_CLASS_FILES },
            { "Create Java source files and use Javac to create Class files", DEF.CHOICE_VIA_JAVA_SOURCES }
        };

        // Add the chooser editor to the page
        addField(new RadioGroupFieldEditor(
            DEF.SIMULA_COMPILER_MODE,
            "Choose a Simula Backend Compiler Mode:",
            1, // Number of columns to display the radio elements
            labelAndValues,
            parent,
            true // Use a group box enclosure frame
        ));
    }
    
//    public static String getCompilerMode() {
//    	IPreferenceStore store = SimulaPluginActivator.getDefault().getPreferenceStore();
//        return store.getString(DEF.SIMULA_COMPILER_MODE);
//    }
//    
//    private void addTraceModeChooser() {
//    	Composite parent = getFieldEditorParent();
//        // Defines the labels and string data mappings for the 2 choices
//        String[][] labelAndValues = new String[][] {
//            { "LSP Server Trace Off", DEF.CHOICE_TRACE_OFF },
//            { "LSP Server Trace Message", DEF.CHOICE_MESSAGE },
//            { "LSP Server Trace Verbose", DEF.CHOICE_VERBOSE }
//        };
//
//        // Add the chooser editor to the page
//        addField(new RadioGroupFieldEditor(
//            DEF.SIMULA_TRACE_MODE,
//            "Choose a Simula Language Server Trace Mode:",
//            1, // Number of columns to display the radio elements
//            labelAndValues,
//            parent,
//            true // Use a group box enclosure frame
//        ));
//    }
//    
//    public static String getTraceMode() {
//    	IPreferenceStore store = SimulaPluginActivator.getDefault().getPreferenceStore();
//        return store.getString(DEF.SIMULA_TRACE_MODE);
//    }
//
//    private void addKeepJavaFileChooser() {
//        // This adds a label, a default text box, and a File Browser button automatically
//        FileFieldEditor fileChooser = new FileFieldEditor(
//            DEF.SIMULA_KEEP_JAVA_PATH, 
//            "&Keep Java:", 
//            getFieldEditorParent()
//        );
//        
//        // Optional: Filter file extensions if needed (e.g., only executables)
//        // fileChooser.setFileExtensions(new String[] {"*.exe", "*"});
//        
//        addField(fileChooser);
//    }
    
}
