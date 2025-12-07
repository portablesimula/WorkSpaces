package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.configurations.RunConfigurationOptions;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import simula.plugin.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Set;

public final class SimOption extends RunConfigurationOptions {

    private final Project project;
    private final SimulaRunConfiguration runConfiguration;
    private Properties properties;

    // Used by SimulaLexer
    public static boolean TRACE_SCAN = false;
    public static boolean TRACE_COMMENTS = false;
	public static boolean CaseSensitive=false;

    public SimOption(@NotNull Project project, @NotNull SimulaRunConfiguration runConfiguration) {
        this.project = project;
        this.runConfiguration = runConfiguration;
    }


    public void init() {
        properties = new Properties();
        String runConfigName = runConfiguration.getName();
        String dir = project.getBasePath() + "/.idea";
        File propertiesFile = new File(dir,"runconf_" + runConfiguration.getName() + ".xml");
        try { properties.loadFromXML(new FileInputStream(propertiesFile));
        } catch (Exception e) {
            // Set defaults
            properties.setProperty("simula.compiler.compilerMode", "directClassFiles");
            properties.setProperty("simula.compiler.caseSensitive", "false");
            properties.setProperty("simula.compiler.verbose", "true");
            properties.setProperty("simula.compiler.noExecution", "false");
            properties.setProperty("simula.compiler.warnings", "false");
            properties.setProperty("simula.compiler.extensions", "true");
            properties.setProperty("simula.runtime.verbose", "true");
            properties.setProperty("simula.runtime.blockTracing", "false");
            properties.setProperty("simula.runtime.gotoTracing", "false");
            properties.setProperty("simula.runtime.qpsTracing", "false");
            properties.setProperty("simula.runtime.smlTracing", "false");
        }
   }

    public void printOptions(String title) {
        System.out.println("============= " + title + "=================");
        getProperties().list(System.out);
    }

    private Properties getProperties() {
        if(properties == null) {
            init();
         }
        return properties;
    }

    public void writeConfiguration(String runConfigName) {
        String dir = project.getBasePath() + "/.idea";
        Util.TRACE("SimOption.writeConfiguration: " + runConfigName + " Dir=" + dir);
        File propertiesFile = new File(dir, "runconf_" + runConfigName + ".xml");
        try {
            Util.TRACE("SimOption.writeConfiguration: storeToXML: " + propertiesFile);
            getProperties().storeToXML(new FileOutputStream(propertiesFile), "Simula RunConfiguration " + runConfigName);
        } catch (Exception e) {
            Util.TRACE("SimOption.writeConfiguration: storeToXML: " + e);
            Util.IERR();
        }
    }

    /// Returns the Compiler option name 'id'
    /// @param id option id
    /// @return the option name 'id'
    public boolean getCTOption(String id) {
        String val = getProperties().getProperty("simula.compiler." + id, "false");
        return val.equals("true");
    }

    /// Set the Compiler option named 'id' to the given value
    /// @param id option id
    /// @param val new option value
    public void setCTOption(String id, boolean val) {
        getProperties().setProperty(id, ""+val);
    }

    /// Set the Runtime option named 'id' to the given value
    /// @param id option id
    /// @param val new option value
    public void setRTOption(String id, boolean val) {
        getProperties().setProperty(id, ""+val);
    }

    /// Editor Utility: Set Compiler Mode.
    /// @param id the mode String.
    public void setCompilerMode(String id) {
        Util.TRACE("SimOption.setCompilerMode: "+id);
        getProperties().setProperty("simula.compiler.compilerMode", id);
    }

    /// Returns the Runtime option name 'id'
    /// @param id option id
    /// @return the option name 'id'
    public boolean getROption(String id) {
        String val = getProperties().getProperty("simula.runtime." + id, "false");
        return val.equals("true");
    }


    /// Editor Utility: Set Compiler Mode.
    public JPanel selectModeOption() {
        JPanel panel=new JPanel();
        panel.setBackground(Color.white);
        JCheckBox but1 = checkBox("viaJavaSource");
        JCheckBox but2 = checkBox("directClassFiles");
        JCheckBox but3 = checkBox("simulaClassLoader");

        String compilerMode = getProperties().getProperty("simula.compiler.compilerMode", "directClassFiles");

        if(compilerMode.equals("viaJavaSource")) but1.setSelected(true);
        else if(compilerMode.equals("directClassFiles")) but2.setSelected(true);
        else if(compilerMode.equals("simulaClassLoader")) but3.setSelected(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        panel.add(but1); buttonGroup.add(but1);
        panel.add(new JLabel("   The Simula Compiler will generate Java source files and use"));
        panel.add(new JLabel("   the Java compiler to generate JavaClass files which in turn"));
        panel.add(new JLabel("   are collected together with the Runtime System into the"));
        panel.add(new JLabel("   resulting executable jar-file."));
        panel.add(new JLabel(" "));
        panel.add(but2); buttonGroup.add(but2);
        panel.add(new JLabel("   The Simula Compiler will generate JavaClass files directly"));
        panel.add(new JLabel("   which in turn are collected together with the Runtime System"));
        panel.add(new JLabel("   into the resulting executable jar-file."));
        panel.add(new JLabel("   No Java source files are generated."));
        panel.add(new JLabel(" "));
        panel.add(but3); buttonGroup.add(but3);
        panel.add(new JLabel("   The Simula Compiler will generate ClassFile byte array and"));
        panel.add(new JLabel("   load it directly. No intermediate files are created."));
        panel.add(new JLabel(" "));
        panel.add(new JLabel("   NOTE:   In this mode, the editor will terminate after the first"));
        panel.add(new JLabel("                  program execution"));
        panel.add(new JLabel(" "));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
       Util.optionDialog(panel,"Select Compiler Mode",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Ok");
//        Global.storeWorkspaceProperties();
        return panel;
    }

    public JPanel selectCTOptions() {
        JPanel panel=new JPanel();
//        panel.setBackground(Color.white);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Compiler Options:"));
//        Util.TRACE("SimOption.selectCTOptions: "+properties);
        Set<String> names = getProperties().stringPropertyNames();
        for(String name:names) {
//            Util.TRACE("SimOption.selectCTOptions: name="+name);
            if(! name.equals("simula.compiler.compilerMode"))
                if(name.startsWith("simula.compiler.")) panel.add(checkBox(name));
        }
        Util.optionDialog(panel,"Select Compiler Options",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Ok");
        printOptions("SimOption.selectCTOptions: DONE");
//    	Global.storeWorkspaceProperties();
        return panel;
    }

    public JPanel selectRTOptions() {
        JPanel panel=new JPanel();
//        panel.setBackground(Color.white);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Runtime Options:"));
        Set<String> names = getProperties().stringPropertyNames();
        for(String name:names)
            if(name.startsWith("simula.runtime.")) panel.add(checkBox(name));
        Util.optionDialog(panel,"Select Runtime Options",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Ok");
        printOptions("SimOption.selectRTOptions: DONE");
//    	Global.storeWorkspaceProperties();
        return panel;
    }

    public JPanel selectOptionsPanel() {
        JPanel panel = new JPanel();

        JButton modeButton = new JButton("Set Compiler Mode");
        modeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectModeOption();
            }
        });
        panel.add(modeButton);

        JButton ctButton = new JButton("Set Compiler Options");
        ctButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Util.TRACE("BUTTON PRESSED: CT-OPTION");
                selectCTOptions();
            }
        });
        panel.add(ctButton);

        JButton rtButton = new JButton("Set Runtime Options");
        rtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               selectRTOptions();
            }
        });
        panel.add(rtButton);

        return panel;
    }

    /// Editor Utility: Create a custom checkBox.
    /// @param id option id
    /// @return the resulting check box
    private JCheckBox checkBox(String id) {
        String val = getProperties().getProperty(id, "false");
        int p = id.lastIndexOf('.');
        boolean selected = val.equals("true");
        JCheckBox item = new JCheckBox(id.substring(p+1));
        item.setBackground(Color.white);
        item.setSelected(selected);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(id.equals("viaJavaSource") || id.equals("directClassFiles") || id.equals("simulaClassLoader")) {
//                    if(Option.verbose) Util.println("Compiler Mode: "+id);
                    setCompilerMode(id);
                } else {
                    setCTOption(id,item.isSelected());
                }
            }});
        item.addMouseListener(new MouseAdapter() {
            Color color = item.getBackground();
            @Override
            public void mouseEntered(MouseEvent me) {
                color = item.getBackground();
                item.setBackground(Color.lightGray); // change the color to lightGray when mouse over a button
            }
            @Override
            public void mouseExited(MouseEvent me) {
                item.setBackground(color);
            }
        });
        return(item);
    }
}