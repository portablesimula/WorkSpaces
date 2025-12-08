package simula.plugin.extensions.runConfiguration;

import com.intellij.openapi.components.StoredProperty;
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
    Map<String, String> options;

    public SelectOptionsPanel(Map<String, String> options) {
        this.options = options;

        JButton modeButton = new JButton("Set Compiler Mode");
        modeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectModeOption();
            }
        });
        this.add(modeButton);

        JButton ctButton = new JButton("Set Compiler Options");
        ctButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Util.TRACE("BUTTON PRESSED: CT-OPTION");
                selectCTOptions();
            }
        });
        this.add(ctButton);

        JButton rtButton = new JButton("Set Runtime Options");
        rtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectRTOptions();
            }
        });
        this.add(rtButton);
    }


    /// Editor Utility: Set Compiler Mode.
    public JPanel selectModeOption() {
        JPanel panel=new JPanel();
        panel.setBackground(Color.white);
        JCheckBox but1 = checkBox("viaJavaSource");
        JCheckBox but2 = checkBox("directClassFiles");
        JCheckBox but3 = checkBox("simulaClassLoader");

        String compilerMode = options.get("simula.compiler.compilerMode");

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

//        Set<String> names = getProperties().stringPropertyNames();
//        Map<String, String> options = getOptionsMap();
        Set<String> names = options.keySet();
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

//        Set<String> names = getProperties().stringPropertyNames();
//        Map<String, String> options = getOptionsMap();
        Set<String> names = options.keySet();
        for(String name:names)
            if(name.startsWith("simula.runtime.")) panel.add(checkBox(name));
        Util.optionDialog(panel,"Select Runtime Options",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,"Ok");
        printOptions("SimOption.selectRTOptions: DONE");
//    	Global.storeWorkspaceProperties();
        return panel;
    }

    /// Editor Utility: Create a custom checkBox.
    /// @param id option id
    /// @return the resulting check box
    private JCheckBox checkBox(String id) {
        String val = options.get(id);
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

    public void printOptions(String title) {
        System.out.println("============= " + title + "=================");
//        getProperties().list(System.out);
//        Map<String, String> options = getOptionsMap();
    }

    /// Returns the Compiler option name 'id'
    /// @param id option id
    /// @return the option name 'id'
    public boolean getCTOption(String id) {
        String val = options.get("simula.compiler." + id);
        return val.equals("true");
    }

    /// Set the Compiler option named 'id' to the given value
    /// @param id option id
    /// @param val new option value
    public void setCTOption(String id, boolean val) {
        options.put(id, ""+val);
    }

    /// Set the Runtime option named 'id' to the given value
    /// @param id option id
    /// @param val new option value
    public void setRTOption(String id, boolean val) {
        options.put(id, ""+val);
    }

    /// Editor Utility: Set Compiler Mode.
    /// @param id the mode String.
    public void setCompilerMode(String id) {
        Util.TRACE("SimOption.setCompilerMode: "+id);
        options.put("simula.compiler.compilerMode", id);
    }

    /// Returns the Runtime option name 'id'
    /// @param id option id
    /// @return the option name 'id'
    public boolean getROption(String id) {
        String val = options.get("simula.runtime." + id);
        return val.equals("true");
    }

}
