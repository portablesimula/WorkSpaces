package sim.compiler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Util {
	
	public static void warning(final String msg) {
		IO.println("WARNING: " + msg);
	}
	
	public static void error(final String msg) {
		IO.println("ERROR: " + msg);
	}

	/// Utility method: ASSERT
	/// @param test this test must be true
	/// @param msg the message when test = false
	public static void ASSERT(final boolean test, final String msg) {
		if (!test) {
			IERR("ASSERT(" + msg + ") -- FAILED");
		}
	}

	/// Print a string.
	/// @param s the string
	public static void println(final String s) {
		if (Global.consolePanel != null) {
			if(s != null) {
				String u = s.replace('\r', (char) 0);
				u = u.replace('\n', (char) 0);
				Global.consolePanel.write(u + '\n');
//				Thread.dumpStack();
			}
		}
//		else
		IO.println("Util.println: " + s);
	}  

	/// Print the internal error message: IMPOSSIBLE.
	public static void IERR() {
		IERR("IMPOSSIBLE");
	}

	/// Print a internal error message.
	/// @param msg the message
	public static void IERR(final String msg) {
//		String err = edLINE(": Internal error - " + msg);
//		nError++;
//		printError(err);
		IO.println("Internal error: " + msg);
		Thread.dumpStack();
//		FORCED_EXIT();
		System.exit(-1);
	}

	/// Print a internal error message.
	/// @param msg the message
	/// @param e any Throwable
	public static void IERR(final String msg,final Throwable e) {
		IERR(msg +"  "+ e);
	}


	/// Utility method: TRACE
	/// @param msg the message to print
	public static void TRACE(final String msg) {
//		if (Option.TRACING)
			IO.println("TRACE " + Global.sourceLineNumber + ": " + msg);
	}
	
	public static boolean getBoolProperty(Properties properties, String key, boolean defaultValue) {
		String prop = properties.getProperty(key);
		if(prop == null) return defaultValue;
		return (prop.equalsIgnoreCase("true"))? true : false;
	}
	
	public static int getIntProperty(Properties properties, String key, int defaultValue) {
		String prop = properties.getProperty(key);
		if(prop == null) return defaultValue;
		return Integer.valueOf(prop);
	}
	  
    //*******************************************************************************
    //*** 
    //*******************************************************************************
	/// Returns true if the two specified strings are equal to one another.
	/// @param s1 argument string
	/// @param s2 argument string
	/// @return true if the two specified strings are equal to one another
	public static boolean equals(String s1,String s2) {
		if(Option.CaseSensitive)
			 return(s1.equals(s2));			
		else return(s1.equalsIgnoreCase(s2));
	}
	
	/// Pop up an error message box.
	/// @param msg the error message
	public static void popUpError(final String msg) {
		int res=Util.optionDialog(msg+"\nDo you want to continue ?","Error",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, "Yes", "No");
		if(res!=JOptionPane.YES_OPTION) System.exit(0);
	}

	/// Brings up an option dialog.
	/// @param msg the message to display
	/// @param title the title string for the dialog
	/// @param optionType an integer designating the options available on the dialog
	/// @param messageType an integer designating the kind of message this is
	/// @param option an array of objects indicating the possible choices the user can make
	/// @return an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
	public static int optionDialog(final Object msg, final String title, final int optionType, final int messageType, final String... option) {
		Object OptionPaneBackground = UIManager.get("OptionPane.background");
		Object PanelBackground = UIManager.get("Panel.background");
		UIManager.put("OptionPane.background", Color.WHITE);
		UIManager.put("Panel.background", Color.WHITE);
		int answer = JOptionPane.showOptionDialog(null, msg, title, optionType, messageType, Global.sIcon, option, option[0]);
		// IO.println("doClose.saveDialog: answer="+answer);
		UIManager.put("OptionPane.background", OptionPaneBackground);
		UIManager.put("Panel.background", PanelBackground);
		return (answer);
	}

	/// Editor Utility: Create a checkBox with tooltips.
	/// @param id option id
	/// @param tooltip option's tooltip or null
	/// @return the resulting check box
	public static JCheckBox checkBox(String id,String tooltip) {
		return checkBox(id, tooltip,Option.getOption(id));
	}

	/// Editor Utility: Create a checkBox with tooltips.
	/// @param id option id.
	/// @param tooltip option's tooltip or null.
	/// @param selected true: this checkBox is selected.
	/// @return the resulting check box.
	public static JCheckBox checkBox(String id,String tooltip,boolean selected) {
		JCheckBox item = new JCheckBox(id);
		item.setBackground(Color.white);
        item.setSelected(selected);
        item.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Option.setOption(id,item.isSelected());
		}});
        if(tooltip != null) item.setToolTipText(tooltip);
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

	
	// ***************************************************************
	// *** LIST .jar file
	// ***************************************************************
	/**
	 * List .jar file
	 * @param file the .jar file
	 */
	public static void listJarFile(final File file) {
		IO.println("---------  LIST .jar File: " + file + "  ---------");
		if (!(file.exists() && file.canRead())) {
			IO.println("ERROR: Can't read .jar file: " + file);
			return;
		}
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(file);
			Manifest manifest = jarFile.getManifest();
			Attributes mainAttributes = manifest.getMainAttributes();
			Set<Object> keys = mainAttributes.keySet();
			for (Object key : keys) {
				String val = mainAttributes.getValue(key.toString());
				IO.println(key.toString() + "=\"" + val + "\"");
			}

			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String size = "" + entry.getSize();
				while (size.length() < 6)
					size = " " + size;
				FileTime fileTime = entry.getLastModifiedTime();
				String date = DateTimeFormatter.ofPattern("uuuu-MMM-dd HH:mm:ss", Locale.getDefault())
						.withZone(ZoneId.systemDefault()).format(fileTime.toInstant());
				IO.println("Jar-Entry: " + size + "  " + date + "  \"" + entry + "\"");
			}
		} catch (IOException e) {
			IO.println("IERR: Caused by: " + e);
		} finally {
			if (jarFile != null)
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	
	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	public static int exec(final Vector<String> cmd) throws IOException {
		String[] cmds = new String[cmd.size()];
		cmd.copyInto(cmds);
		return (exec(cmds));
	}

	public static int exec(String... cmd) throws IOException {
		String cmdLine="";
		for(int i=0;i<cmd.length;i++) cmdLine=cmdLine+" "+cmd[i];
        if(Option.verbose) IO.println("SIM.Util.exec: command ="+cmdLine);
		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		processBuilder.inheritIO();
		try {
			Process process = processBuilder.start();
			int exitCode = process.waitFor();
			if(Option.verbose) IO.println("SIM.Util.exec: exitCode = "+exitCode);
			return exitCode;
		} catch(Exception e) {
			e.printStackTrace();
			Util.IERR("SIM.Util.exec: Process Execution failed: " + cmdLine, e);
			return -1;
		}
	}

	public static int exec1(String... cmd) throws IOException {
		String cmdLine="";
		for(int i=0;i<cmd.length;i++) cmdLine=cmdLine+" "+cmd[i];
        if(Option.verbose) IO.println("SIM.Util.exec: command ="+cmdLine);
		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
//		processBuilder.inheritIO();
//		processBuilder.redirectErrorStream();
		try {
			Process process = processBuilder.start();
			BufferedReader reader = process.inputReader(); // Process' output
			BufferedReader errOut = process.errorReader(); // Process' error output
			String line = null;
			while((line = reader.readLine()) != null) {
			    IO.println(line);
				if(Global.consolePanel != null) {
					Global.consolePanel.write(line + '\n');
				}
			}
			while((line = errOut.readLine()) != null) {
			    IO.println(line);
				if(Global.consolePanel != null) {
					Global.consolePanel.write(line + '\n');
				}
			}
			
//			boolean terminated = process.waitFor(5, TimeUnit.MINUTES);
//			if(! terminated) Util.IERR("SIM.Util.exec: Process Execution didn't terminate: " + cmdLine);
//			int exitCode = process.exitValue();
			
			int exitCode = process.waitFor();
			
			if(Option.verbose) IO.println("SIM.Util.exec: exitCode = "+exitCode);
			return exitCode;
		} catch(Exception e) {
			e.printStackTrace();
			Util.IERR("SIM.Util.exec: Process Execution failed: " + cmdLine, e);
			return -1;
		}
	}

}
