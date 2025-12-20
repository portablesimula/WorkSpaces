/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.plugin.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;

/// Runtime Options
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/EclipseWorkSpaces/blob/main/SimulaCompiler2/Simula/src/simula/editor/RTOption.java"><b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class RTOption {
	/** Runtime Option */ public boolean VERBOSE = false;
	/** Runtime Option */ public boolean BLOCK_TRACING = false;
	/** Runtime Option */ public boolean GOTO_TRACING = false;
	/** Runtime Option */ public boolean QPS_TRACING = false;
	/** Runtime Option */ public boolean SML_TRACING = false;


	/// The default constructor
    public RTOption() {}

	/// Initiate Runtime options with default values.
    public void InitRuntimeOptions() {
		VERBOSE = false;
//		USE_CONSOLE=true;
		BLOCK_TRACING = false;
		GOTO_TRACING = false;
		QPS_TRACING = false;
		SML_TRACING = false;
	}

    /// Add Runtime options to the argument vector.
    /// @param args the argument vector
	public void addRTArguments(Vector<String> args) {
		if(VERBOSE) args.add("-verbose");
		if(BLOCK_TRACING) args.add("-blockTracing");
		if(GOTO_TRACING) args.add("-gotoTracing");
		if(QPS_TRACING) args.add("-qpsTracing");
		if(SML_TRACING) args.add("-smlTracing");
	}
	
//	/// Get Compiler options from property file.
//	/// @param properties the properties to decode.
//	public void getRuntimeOptions(Properties properties) {
//		VERBOSE = properties.getProperty("simula.runtime.option.VERBOSE", "false").equalsIgnoreCase("true");
//		BLOCK_TRACING = properties.getProperty("simula.runtime.option.BLOCK_TRACING", "false").equalsIgnoreCase("true");
//		GOTO_TRACING = properties.getProperty("simula.runtime.option.GOTO_TRACING", "false").equalsIgnoreCase("true");
//		QPS_TRACING = properties.getProperty("simula.runtime.option.QPS_TRACING", "false").equalsIgnoreCase("true");
//		SML_TRACING = properties.getProperty("simula.runtime.option.SML_TRACING", "false").equalsIgnoreCase("true");
//	}
	
//	/// Set Compiler options in property file.
//	/// @param properties the properties to encode.
//	public void setRuntimeOptions(Properties properties) {
//		properties.setProperty("simula.runtime.option.VERBOSE", ""+VERBOSE);
//		properties.setProperty("simula.runtime.option.BLOCK_TRACING", ""+BLOCK_TRACING);
//		properties.setProperty("simula.runtime.option.GOTO_TRACING", ""+GOTO_TRACING);
//		properties.setProperty("simula.runtime.option.QPS_TRACING", ""+QPS_TRACING);
//		properties.setProperty("simula.runtime.option.SML_TRACING", ""+SML_TRACING);
//	}
    
	/// Editor Utility: Select Runtime Options.

//    public static void selectRuntimeOptions(Project project, MyRunConfiguration settings) {
      public JPanel selectRTOptions() {
        JPanel panel=new JPanel();
//        panel.setBackground(Color.white);
        panel.add(new JLabel("ZZZ Runtime Options:"));
        panel.add(checkBox("Verbose"));
        panel.add(checkBox("BLOCK_TRACING"));
        panel.add(checkBox("GOTO_TRACING"));
        panel.add(checkBox("QPS_TRACING"));
        panel.add(checkBox("SML_TRACING"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        Util.optionDialog(panel,"Select Runtime Options",JOptionPane.OK_OPTION,
//                JOptionPane.INFORMATION_MESSAGE,"Ok");
//    	Global.storeWorkspaceProperties();
        return panel;
    }

    /// Editor Utility: Create a checkBox without tooltips.
	/// @param id option id
	/// @return the resulting check box
	public JCheckBox checkBox(String id) {
        JCheckBox item = new JCheckBox(id);
    	item.setBackground(Color.white);
        item.setSelected(getOption(id));
        item.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setOption(id,item.isSelected());
		}});
        return(item);
	}

	/// Returns the option name 'id'
	/// @param id option id
	/// @return the option name 'id'
	private boolean getOption(String id) {
		if(id.equalsIgnoreCase("VERBOSE")) return(VERBOSE);
		if(id.equalsIgnoreCase("BLOCK_TRACING")) return(BLOCK_TRACING);
		if(id.equalsIgnoreCase("GOTO_TRACING")) return(GOTO_TRACING);
		if(id.equalsIgnoreCase("QPS_TRACING")) return(QPS_TRACING);
		if(id.equalsIgnoreCase("SML_TRACING")) return(SML_TRACING);
		return(false);
	}

	/// Set the option named 'id' to the given value
	/// @param id option id
	/// @param val new option value
	private void setOption(String id, boolean val) {
		if(id.equalsIgnoreCase("VERBOSE")) VERBOSE=val;
		if(id.equalsIgnoreCase("BLOCK_TRACING")) BLOCK_TRACING=val;
		if(id.equalsIgnoreCase("GOTO_TRACING")) GOTO_TRACING=val;
		if(id.equalsIgnoreCase("QPS_TRACING")) QPS_TRACING=val;
		if(id.equalsIgnoreCase("SML_TRACING")) SML_TRACING=val;
	}

    public void print(String title) {
        Util.log("====== " + title + " ======");
        Util.log(title + "VERBOSE=" + VERBOSE);
        Util.log(title + "BLOCK_TRACING" + BLOCK_TRACING);
        Util.log(title + "GOTO_TRACING" + GOTO_TRACING);
        Util.log(title + "QPS_TRACING" + QPS_TRACING);
        Util.log(title + "SML_TRACING" + SML_TRACING);
    }

}