package simula.plugin.util;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class Util {
    static Util INSTANCE = new Util();

    public static Icon getSimulaIcon() {
        String iconName = "favico.png";
//        String iconName = "images/sim.png";
//        String iconName = "images/sim2.png";
//        String iconName = "images/simula.png;
        return getSimulaIcon(iconName);
    }
    public static Icon getSimulaIcon(String iconName) {
        // Assuming your image is in a folder named 'images' within your resources root
       URL imageUrl = INSTANCE.getClass().getClassLoader().getResource("images/" + iconName);
        if (imageUrl != null) {
//            System.out.println("Util.getSimulaIcon: URL="+imageUrl);
            ImageIcon sIcon = new ImageIcon(imageUrl);
            // Now you can use 'myIcon' with a JLabel, JButton, or other Swing components
            // For example: JLabel label = new JLabel(myIcon);
            return sIcon;
        } else {
            throw new RuntimeException("Util.getSimulaIcon: Image not found: " + iconName);
        }
    }

    public static void warning(String msg) {
        System.out.println("[WARNING] " + msg);
    }

    public static void TRACE(String msg) {
//        Messages.showMessageDialog(msg,"TRACE", Util.getSimulaIcon("sim.png"));
        System.out.println("TRACE: " + msg);
    }

    public static void error(String msg) {
//        Messages.showMessageDialog(msg,"ERROR", Util.getSimulaIcon("sim.png"));
        System.out.println("ERROR: " + msg);
    }

    public static void ASSERT(boolean cond, String s) {

    }

    public static boolean equals(String s1, String s2) {
        return false;
    }

    public static void IERR(String msg) {
        Messages.showMessageDialog(msg,"IERR", Util.getSimulaIcon("sim.png"));
    }

    public static void ALERT(String msg) {
        msg += "\n\nDo you want to Continue ?\n\n";
        String finalMsg = msg;
        ApplicationManager.getApplication().invokeLater(() -> {
            WriteAction.run(() -> {
                try {
                    int answer = Util.optionDialog(finalMsg, "ALERT", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "Continue", "Exit");
                    if (answer != JOptionPane.OK_OPTION) {
                        System.exit(-1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
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
        int answer = JOptionPane.showOptionDialog(null, msg, title, optionType, messageType,
                Util.getSimulaIcon("sim.png"), option, option[0]);
        // IO.println("doClose.saveDialog: answer="+answer);
        UIManager.put("OptionPane.background", OptionPaneBackground);
        UIManager.put("Panel.background", PanelBackground);
        return (answer);
    }

    public static String getSimulaProperty(String key) {
        if(simulaProperties == null) {
            loadSimulaProperties();
        }
        return simulaProperties.getProperty(key);
    }
    private static Properties simulaProperties;

    private static void loadSimulaProperties() {
        simulaProperties = new Properties();
        String USER_HOME = System.getProperty("user.home");
        File simulaPropertiesDir = new File(USER_HOME, ".simula");
        File simulaPropertiesFile = new File(simulaPropertiesDir, "simulaProperties.xml");
        try {
            simulaProperties.loadFromXML(new FileInputStream(simulaPropertiesFile));
        } catch (IOException e) {
            Messages.showMessageDialog("Can't load Simula Properties",
                    "ERROR", Util.getSimulaIcon("sim.png"));
            throw new RuntimeException(e);
        }
    }

    public static VirtualFile getSimulaSamplesDir() {
        String simulaHomeDir = Util.getSimulaProperty("simula.home");
        Path path = Path.of(simulaHomeDir, "/Simula-2.0/samples");
        VirtualFile res = VfsUtil.findFile(path, true);
        System.out.println("Util.getSimulaSamplesDir: res=" + res);
        return res;
    }

    public static VirtualFile getBaseDir(Project project) {
        // ASK Goolgle: how to replace Deprecated
        //              public abstract com.intellij.openapi.vfs.VirtualFile getBaseDir
        //              in intellij plugin implementation
        VirtualFile res = ProjectUtil.guessProjectDir(project);
        System.out.println("Util.getBaseDir: res=" + res);
        return res;
    }

    public static void printProject(String title, Project project) {
        String id = "Util.printProject'"+title+": ";
        System.out.println(id+project);
        System.out.println(id+"Name: "+project.getName());
        System.out.println(id+"isOpen: "+project.isOpen());
        System.out.println(id+"isInitialized: "+project.isInitialized());
        System.out.println(id+"isDefault: "+project.isDefault());
        System.out.println(id+"getBasePath: "+project.getBasePath());
    }


}
