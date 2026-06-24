package simula.editor;

import javax.swing.*;

import simula.compiler.SourceModule;
import simula.compiler.syntaxClass.statement.ProgramModule;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.psi.PsiBuilder;
import simula.psi.PsiTree;

import java.awt.*;

/// @author Google AI
/// @author Øystein Myhre Andersen
@SuppressWarnings("serial")
public class PaletteChooser extends JDialog {
	JPanel topPanel;
	
    private CardLayout cardLayout = new CardLayout();
    private JPanel demoContainer = new JPanel(cardLayout);
    private static int DEMO_SEQU = 1;

    // --- BOTTOM PANEL (Demo text and Reset-button) ---
    JPanel bottomPanel = new JPanel(new BorderLayout());
    PsiTextPanel demoPanel;

    // Theme Choosing
    private JComboBox<String> themeDropdown;
    private String selectedTheme() { return (String) themeDropdown.getSelectedItem(); }
    
    // UI-komponenter for fargelinjene
    private JPanel palettePanel;
    private JPanel[] colorPanels;
    private JLabel[] colorLabels;
    
    public PaletteChooser(Frame owner) {
    	super(owner, "Select Color Theme", true);
        try { setIconImage(Global.favicon.getImage()); } 
        catch (Exception e) {}// Util.IERR("Impossible",e); }
//        setTitle("Select Color Theme");
        setSize(500, 650);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        themeDropdown.setSelectedItem(Option.selectedTheme);
        Palette.loadAndRenderPalette(selectedTheme(), false);
        updateThemeColors();
    }

    private void initComponents() {
        // --- TOP PANEL (Theme Chooser) ---
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        themeDropdown = new JComboBox<String>(Palette.themeNames);
        themeDropdown.setBackground(Color.WHITE);
        themeDropdown.setMaximumSize(new Dimension(150, 30));
        themeDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeDropdown.addActionListener(e -> {
        	IO.println("PaletteChooser'themeDropdown: e=" + e);
        	IO.println("PaletteChooser'themeDropdown: selectedTheme=" + selectedTheme());
	        Palette.loadAndRenderPalette(selectedTheme(), false);
        	updateThemeColors();
        	IO.println("PaletteChooser'themeDropdown: DONE: selectedTheme=" + selectedTheme());
        	Option.selectedTheme = selectedTheme();
        	Global.storeWorkspaceProperties();
        });
//        themeDropdown.setSelectedItem(Option.selectedTheme);
        topPanel.add(themeDropdown);
        add(topPanel, BorderLayout.NORTH);

        // --- MIDTPANEL (x Linjer med Label og Farge) ---
        palettePanel = new JPanel(new GridLayout(Palette.nColors, 1, 5, 5));
        palettePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        IO.println("PaletteChooser.initComponents: Palette.nColors=" + Palette.nColors);
        colorPanels = new JPanel[Palette.nColors];
        colorLabels = new JLabel[Palette.nColors];

        for (int i = 0; i < Palette.nColors; i++) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            
            // Tekstforklaring
            colorLabels[i] = new JLabel(Palette.NAMES[i]);
            colorLabels[i].setFont(new Font("Arial", Font.PLAIN, 14));
            row.add(colorLabels[i], BorderLayout.WEST);

            // Fargevisning og knapp i ett
            colorPanels[i] = new JPanel();
            colorPanels[i].setPreferredSize(new Dimension(150, 20));
            colorPanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            JButton changeBtn = new JButton("Change ...");
            changeBtn.setPreferredSize(new Dimension(90, 20));
            final int index = i;
            changeBtn.addActionListener(_ -> openColorChooser(index));
            
            JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            rightGroup.add(colorPanels[i]);
            rightGroup.add(changeBtn);
            
            row.add(rightGroup, BorderLayout.EAST);
            palettePanel.add(row);
        }
        add(palettePanel, BorderLayout.CENTER);
        // Add Demo panel within the demo Container
        demoContainer.add(getDemoPanel(), "Screen1");
       	bottomPanel.add(demoContainer, BorderLayout.CENTER);        	
        // Add Done and Reset Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton doneBtn = new JButton("Done");
        doneBtn.addActionListener(_ -> dispose());
        buttonPanel.add(doneBtn);
        JButton resetBtn = new JButton("Reset to Standard");
        resetBtn.addActionListener(_ -> resetCurrentTheme());
        buttonPanel.add(resetBtn);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private static int SEQU = 1;
    private PsiTextPanel getDemoPanel() {
//    	Option.internal.TRACE_NEW_LEXTOKEN = 1;
        String sourceText =
        		  " class Demo; begin\n"
        		+ "    comment demo mockup: "+SEQU+";\n"
        		+ "    procedure p(x); integer x; begin\n"
        		+ "       real pi = 3.14;\n"
        		+ "       outtext\"line with error\");\n"
        		+ "    end;\n"
        		+ " end\n";
    	PsiTree psiTree = getPsiTree(sourceText);
//		demoPanel = new PsiTextPanel(SimulaEditor. Language.Simula, null);
		demoPanel = new PsiTextPanel(new SourceModule(sourceText), null);
		demoPanel.fillTextPane(0, psiTree);
		return demoPanel;
    }

	public PsiTree getPsiTree(String sourceText) {
		PsiBuilder psiBuilder = new PsiBuilder();
		psiBuilder.start(sourceText);
		@SuppressWarnings("unused")
		ProgramModule programModule = new ProgramModule(psiBuilder);
		return psiBuilder.getRoot();
	}

    // Oppdaterer visningen når du bytter tema i dropdownmenyen
    private void updateThemeColors() {
//    	IO.println("PaletteChooser.updateThemeColors: Palette.nColors=" + Palette.nColors + ", colorPanels=" + colorPanels.length);
        for (int i = 0; i < Palette.nColors; i++) {
//        	IO.println("PaletteChooser.updateThemeColors: i=" + i);
            colorPanels[i].setBackground(Palette.getColor(i));
        }
        
        if(demoPanel != null) {
    		PsiTextPanel prevCard = demoPanel;
    		DEMO_SEQU++;
    		String ScreenID = "Screen" + DEMO_SEQU;
            // Flip to the specific panel instantly
            demoContainer.add(getDemoPanel(), ScreenID);
            cardLayout.show(demoContainer, ScreenID); 
            
            // Remove previous card from the container
            demoContainer.remove(prevCard);
        }
        palettePanel.repaint();
    }

    // Open JColorChooser and save the new color in the current theme. 
    private void openColorChooser(int index) {
        Color currentColor = Palette.getColor(index);
        Color newColor = JColorChooser.showDialog(this, "Choose color for " + Palette.NAMES[index], currentColor);
        if (newColor != null) {
            Palette.setColor(index, newColor);
            colorPanels[index].setBackground(newColor);
            updateThemeColors();
            Palette.storeCurrentThemeProperties();
        }
    }

    // Resets the current theme back to the default colors.
    private void resetCurrentTheme() {
        Palette.loadAndRenderPalette(selectedTheme(), true);
        updateThemeColors();
        Palette.storeCurrentThemeProperties();
    }

//    public static void main(String[] Array) {
//    	Global.initiate();
////    	Palette.init();
//        SwingUtilities.invokeLater(() -> {
//        	Option.selectedTheme = Palette.themeNames[0];
//            new PaletteChooser(null).setVisible(true);
//        });
//    }
    
}
