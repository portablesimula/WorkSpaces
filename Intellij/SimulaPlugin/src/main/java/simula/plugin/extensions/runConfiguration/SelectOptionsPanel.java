package simula.plugin.extensions.runConfiguration;

import simula.plugin.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Set;

public class SelectOptionsPanel extends JPanel {
    DemoSettingsEditor settingsEditor;
    Map<String, String> getOptions() {
        return settingsEditor.optionsMap;
    }

    public SelectOptionsPanel(DemoSettingsEditor settingsEditor) {
        this.settingsEditor = settingsEditor;
        JButton modeButton = new JButton("Set Compiler Mode");
        modeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { selectModeOption(); }
        });
        this.add(modeButton);

        JButton ctButton = new JButton("Set Compiler Options");
        ctButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { selectCTOptions(); }
        });
        this.add(ctButton);

        JButton rtButton = new JButton("Set Runtime Options");
        rtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { selectRTOptions(); }
        });
        this.add(rtButton);
    }

    private void setDefaults() {
        setDefaultOption("simula.compiler.compilerMode", "directClassFiles");
        setDefaultOption("simula.compiler.caseSensitive", "false");
        setDefaultOption("simula.compiler.verbose", "true");
        setDefaultOption("simula.compiler.noExecution", "false");
        setDefaultOption("simula.compiler.warnings", "false");
        setDefaultOption("simula.compiler.extensions", "true");
        setDefaultOption("simula.runtime.verbose", "true");
        setDefaultOption("simula.runtime.blockTracing", "false");
        setDefaultOption("simula.runtime.gotoTracing", "false");
        setDefaultOption("simula.runtime.qpsTracing", "false");
        setDefaultOption("simula.runtime.smlTracing", "false");
    }

    private void setDefaultOption(String id, String val){
        String prev = getOptions().get(id);
        if(prev == null) getOptions().put(id, val);
    }

    /// Editor Utility: Set Compiler Mode.
    public JPanel selectModeOption() {
        setDefaults();
        JPanel panel=new JPanel();
        panel.setBackground(Color.white);
        JCheckBox but1 = checkBox("viaJavaSource");
        JCheckBox but2 = checkBox("directClassFiles");
        JCheckBox but3 = checkBox("simulaClassLoader");

        String compilerMode = getOptions().get("simula.compiler.compilerMode");

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
        return panel;
    }

    public JPanel selectCTOptions() {
        setDefaults();
        JPanel panel=new JPanel();
//        panel.setBackground(Color.white);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Compiler Options:"));
        Set<String> names = getOptions().keySet();
        for(String name:names) {
           if(! name.equals("simula.compiler.compilerMode"))
                if(name.startsWith("simula.compiler.")) panel.add(checkBox(name));
        }
        Util.optionDialog(panel,"Select Compiler Options",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Ok");
        return panel;
    }

    public JPanel selectRTOptions() {
        setDefaults();
        JPanel panel=new JPanel();
//        panel.setBackground(Color.white);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Runtime Options:"));
        Set<String> names = getOptions().keySet();
        for(String name:names)
            if(name.startsWith("simula.runtime.")) panel.add(checkBox(name));
        Util.optionDialog(panel,"Select Runtime Options",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Ok");
        return panel;
    }

    /// Editor Utility: Create a custom checkBox.
    /// @param id option id
    /// @return the resulting check box
    private JCheckBox checkBox(String id) {
        String val = getOptions().get(id);
        int p = id.lastIndexOf('.');
        boolean selected = "true".equals(val);
        JCheckBox item = new JCheckBox(id.substring(p+1));
        item.setBackground(Color.white);
        item.setSelected(selected);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(id.equals("viaJavaSource") || id.equals("directClassFiles") || id.equals("simulaClassLoader")) {
                    setCompilerMode(id);
                } else {
                    setOption(id,item.isSelected());
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

    /// Set the Compiler option named 'id' to the given value
    /// @param id option id
    /// @param val new option value
    public void setOption(String id, boolean val) {
        Util.TRACE("SelectOptionsPanel.setOption: "+id+" := "+val);
        getOptions().put(id, ""+val);
//        settingsEditor.???
    }

    /// Editor Utility: Set Compiler Mode.
    /// @param id the mode String.
    public void setCompilerMode(String id) {
        Util.TRACE("SimOption.setCompilerMode: "+id);
        getOptions().put("simula.compiler.compilerMode", id);
    }

}
