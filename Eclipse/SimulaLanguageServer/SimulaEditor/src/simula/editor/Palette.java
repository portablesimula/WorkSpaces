package simula.editor;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.SwingUtilities;

import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;

public class Palette {
	
	public static String currentThemeName;

    public static Color TextPaneForeground;
    public static Color TextPaneBackground;
    public static Color LineNumberForeground;
    public static Color LineNumberBackground;
    public static Color ClassIdentForeground;
    public static Color ProcedureForeground;
    public static Color CommentForeground;
    public static Color KeywordForeground;
    public static Color ConstantForeground;
    public static Color ErrorForeground;
    public static Color ErrorBackground;
    public static Color HoverForeground;
    public static Color HoverBackground;

	/// The color themes used by Simula Editor.
	public static String[] themeNames = {" Atom Light", " Atom Dark", " Light Mode", " Dark Mode", " Old SimEditor"};

	public static int getThemeIndex(String themeName) {
    	for(int i=0;i<themeNames.length;i++) {
    		if(themeNames[i].equals(themeName)) return i;
    	}
    	Util.STOP();
        return 0;
    }
    
    public static void init() {
//    	IO.println("Palette.init: ");
//    	Thread.dumpStack();
//    	loadAndRenderPalette(null, false);
    	loadAndRenderPalette(Option.selectedTheme, false);
    }

	public static void loadAndRenderPalette(String themeName, boolean reset) {
		currentThemeName = (themeName != null)? themeName : themeNames[0];
		IO.println("Palette.loadAndRenderPalette: " + currentThemeName);
		loadThemeProperties(currentThemeName, reset);
//		printColors("Palette.loadAndRenderPalette: " + currentThemeName); // DEBUG info
	}

	/// The current color theme
//	ColorTheme colorTheme;
	String colorTheme;
	
	public static final String[] NAMES = {
    "Foreground TextPane",   "Background TextPane",
    "Foreground LineNumber", "Background LineNumber",
    "Foreground ClassIdent",
    "Foreground Procedure",
    "Foreground Comment",    // "CommentBackground",
    "Foreground Keyword",    // "KeywordBackground",
    "Foreground Constant",   // "ConstantBackground",
    "Foreground Error",      "Background Error",
    "Foreground Hover",      "Background Hover"
	 };
	
    public static int nColors = NAMES.length; // 13;

    public static void setColor(int index, Color color) {
    	switch(index) {
	    	case 0 ->  TextPaneForeground = color;
	        case 1 ->  TextPaneBackground = color;
	        case 2 ->  LineNumberForeground = color;
	        case 3 ->  LineNumberBackground = color;
	        case 4 ->  ClassIdentForeground = color;
	        case 5 ->  ProcedureForeground = color;
	        case 6 ->  CommentForeground = color;
	        case 7 ->  KeywordForeground = color;
	        case 8 ->  ConstantForeground = color;
	        case 9 ->  ErrorForeground = color;
	        case 10 -> ErrorBackground = color;
	        case 11 -> HoverForeground = color;
	        case 12 -> HoverBackground = color;
    	}
   }

    public static Color getColor(int index) {
    	switch(index) {
	    	case 0: return TextPaneForeground;
	        case 1: return TextPaneBackground;
	        case 2: return LineNumberForeground;
	        case 3: return LineNumberBackground;
	        case 4: return ClassIdentForeground;
	        case 5: return ProcedureForeground;
	        case 6: return CommentForeground;
	        case 7: return KeywordForeground;
	        case 8: return ConstantForeground;
	        case 9: return ErrorForeground;
	        case 10: return ErrorBackground;
	        case 11: return HoverForeground;
	        case 12: return HoverBackground;
    	}
        return null;
    }
    
    public static void printColors(String title) {
    	IO.println("++++++++++++++++++++ Current Color Palette: "+currentThemeName +"  WITH TITLE: "+ title + " ++++++++++++++++++++++++");
    	for(int i=0;i<nColors;i++) {
    		IO.println(""+NAMES[i]+"  "+toHex(getColor(i)));
    	}
    }

    // Atom One Light
    // Element / Kodefunksjon                   HEX-kode    Visuell farge
    // Bakgrunn (Editor)						#fafafa		Tilnærmet hvit
    // Bakgrunn (Sidepaneler/UI)				#eaeaea		Lys grå
    // Normal tekst / Foreground				#383a42		Mørk koksgrå
    // Kommentarer (Comments)					#a0a1a7		Lys sølvgrå
    // Nøkkelord (Keywords)						#a626a4		Dyp lilla
    // Funksjoner & Definisjoner				#4078f2		Klar kongeblå
    // Strenger (Strings)						#50a14f		Mørkegrønn
    // Tall & Konstanter						#986801		Okergul / Brun
    // Innebygde elementer (Built-ins)			#0184bc		Mørk cyan
    // Klasse-variabler / Variabler				#e45649		Terracotta / Rød
    
    public static void initAtomLight(Properties properties) {
    	TextPaneForeground   = Color.decode(properties.getProperty("textPane.foreground",   "#383a42"));
    	TextPaneBackground   = Color.decode(properties.getProperty("textPane.background",   "#fafafa"));
    	LineNumberForeground = Color.decode(properties.getProperty("lineNumber.foreground", "#fafafa"));
    	LineNumberBackground = Color.decode(properties.getProperty("lineNumber.background", "#eaeaea"));
    	ClassIdentForeground = Color.decode(properties.getProperty("classIdent.foreground", "#e45649"));
    	ProcedureForeground  = Color.decode(properties.getProperty("procedure.foreground",  "#4078f2"));
    	CommentForeground    = Color.decode(properties.getProperty("comment.foreground",    "#a0a1a7"));
    	KeywordForeground    = Color.decode(properties.getProperty("keyword.foreground",    "#a626a4"));
    	ConstantForeground   = Color.decode(properties.getProperty("constant.foreground",   "#986801"));        
    	ErrorForeground      = Color.decode(properties.getProperty("error.foreground",      "#FFFFFF"));
    	ErrorBackground      = Color.decode(properties.getProperty("error.background",      "#FF0000"));
    	HoverForeground      = Color.decode(properties.getProperty("hover.foreground",      "#FF0000"));
    	HoverBackground      = Color.decode(properties.getProperty("hover.background",      "#FFFF00"));
    }

    // Atom One Dark
    // Element / Kodefunksjon                   HEX-kode    Visuell farge
    // Bakgrunn (Editor)						#282c34		Mørk gråblå
    // Bakgrunn (Sidepaneler/UI)				#21252b		Enda mørkere grå
    // Normal tekst / Foreground				#abb2bf		Lys gråhvit
    // Kommentarer (Comments)					#5c6370		Dempet grågrønn
    // Nøkkelord (Keywords / Syntax)			#c678dd		Lilla / Magenta
    // Funksjoner & Definisjoner				#61afef		Klar blå
    // Strenger (Strings)						#98c379		Dus grønn
    // Tall & Konstanter						#d19a66		Lys oransje / Brun
    
    // Innebygde elementer (Built-ins) / RegEx	#56b6c2		Cyan / Turkis
    // Klasse-variabler / Preprosessering		#e06c75		Svak rød / Korall
    // Søk- / Valgmarkeringsbakgrunn			#3e4451		Medium gråblå
	private static void initAtomDark(Properties properties) {
    	TextPaneForeground   = Color.decode(properties.getProperty("textPane.foreground",   "#abb2bf"));
    	TextPaneBackground   = Color.decode(properties.getProperty("textPane.background",   "#282c34"));
    	LineNumberForeground = Color.decode(properties.getProperty("lineNumber.foreground", "#abb2bf"));
    	LineNumberBackground = Color.decode(properties.getProperty("lineNumber.background", "#21252b"));
    	ClassIdentForeground = Color.decode(properties.getProperty("classIdent.foreground", "#e06c75"));
    	ProcedureForeground  = Color.decode(properties.getProperty("procedure.foreground",  "#61afef"));
    	CommentForeground    = Color.decode(properties.getProperty("comment.foreground",    "#33FF00")); // #5c6370	
    	KeywordForeground    = Color.decode(properties.getProperty("keyword.foreground",    "#c678dd"));
    	ConstantForeground   = Color.decode(properties.getProperty("constant.foreground",   "#d19a66"));        
    	ErrorForeground      = Color.decode(properties.getProperty("error.foreground",      "#FFFFFF"));
    	ErrorBackground      = Color.decode(properties.getProperty("error.background",      "#FF0000"));
    	HoverForeground      = Color.decode(properties.getProperty("hover.foreground",      "#FF0000"));
    	HoverBackground      = Color.decode(properties.getProperty("hover.background",      "#FFFF00"));
	}
	
	private static void initLightMode(Properties properties) {
//		Light VSC
//
//		De standard fargekodene for syntaksheving i VS Code Light Modern og Light+ (Default Light) bruker hovedsakelig følgende heksadesimale verdier for å skille mellom ulike kodeelementer: 
//
//		Syntaks-fargekoder (Token Colors)
//		Kommandoer og nøkkelord: #0000FF (Mørkeblå) – Brukes for ord som if, for, switch, return og new.
//		Funksjoner og metoder: #7A5726 (Brun/mørk gul) – Brukes for funksjonsnavn og metodepåkallinger som console.log() eller init().
//		Tekststrenger (Strings): #A31515 (Mørkerød/vinrød) – Brukes for all tekst i anførselstegn som "hello world" eller 'test'.
//		Kommentarer: #008000 (Grønn) – Brukes for kodelinjer som starter med // eller blocksomgivelser /* ... */.
//
//		Tall og konstanter: #098658 (Mørkegrønn/teal) – Brukes for siffer som 0, 42, samt boolske verdier som true og false.
//		Typer og klasser: #267F99 (Cyan/blågrønn) – Brukes for klassenavn, grensesnitt (interfaces) og innebygde typer som String, User eller HTMLElement.
//		Variabler: #001080 (Mørk marinblå) – Brukes for standard variabler, egenskaper (properties) og parametere. 
    	TextPaneForeground   = Color.decode(properties.getProperty("textPane.foreground",   "#000002")); // Black
    	TextPaneBackground   = Color.decode(properties.getProperty("textPane.background",   "#FFFFFF")); // White
    	LineNumberForeground = Color.decode(properties.getProperty("lineNumber.foreground", "#8C8C8C")); // Gray
    	LineNumberBackground = Color.decode(properties.getProperty("lineNumber.background", "#FFFFFF")); // White
    	ClassIdentForeground = Color.decode(properties.getProperty("classIdent.foreground", "#267F99")); // Cyan/blågrønn - Type identifiers and declarations
    	ProcedureForeground  = Color.decode(properties.getProperty("procedure.foreground",  "#7A5726")); // Brun/mørk gul - Functions and methods being defined
    	CommentForeground    = Color.decode(properties.getProperty("comment.foreground",    "#008000")); // Green
    	KeywordForeground    = Color.decode(properties.getProperty("keyword.foreground",    "#0000FF")); // Dark blue
    	ConstantForeground   = Color.decode(properties.getProperty("constant.foreground",   "#A31515")); // Mørkerød/vinrød        
    	ErrorForeground      = Color.decode(properties.getProperty("error.foreground",      "#FFFFFF")); // White
    	ErrorBackground      = Color.decode(properties.getProperty("error.background",      "#FF0000")); // Red
    	HoverForeground      = Color.decode(properties.getProperty("hover.foreground",      "#FF0000")); // Red
    	HoverBackground      = Color.decode(properties.getProperty("hover.background",      "#FFFF00")); // Yellow
	}
	

	private static void initDarkMode(Properties properties) {
//		Dark VSC
//		Her er listen over de mest sentrale fargekodene (HEX) som brukes i standard VS Code Default Dark+ tema for syntaksutheving:
//		Sentrale syntaksfarger
//		Funksjoner: #DCDCAA (Lys gul/grønn)
//		Nøkkelord: #569CD6 (Lys blå, f.eks. if, return, const)
//		Strenger: #CE9178 (Terrakotta / lakserosa)
//		Klasser/Typer: #4EC9B0 (Turkis / sjøgrønn)
//		Variabler: #9CDCFE (Himmelblå)
//		Tall: #B5CEA8 (Lys olivengrønn)
//		Kommentarer: #6A9955 (Gressgrønn)
//		Regulære uttrykk: #D16969 (Dempet rød)
//
//		Grensesnitt og bakgrunn
//		Hovedbakgrunn: #1E1E1E (Mørk grå)
//		Sidelinje (Sidebar): #252526 (Litt lysere mørk grå)
//		Aktiv fane: #1E1E1E (Matcher editorbakgrunnen)
//		Inaktiv fane: #2D2D2D (Mørkere grå)
//		Statuslinje: #007ACC (Klassisk VS Code blå)
//		Tekstmarkering: #264F78 (Mørk blå)
		IO.println("Palette.initDarkMode: ");
    	TextPaneForeground   = Color.decode(properties.getProperty("textPane.foreground",   "#FFFFFF"));
    	TextPaneBackground   = Color.decode(properties.getProperty("textPane.background",   "#282c34"));
    	LineNumberForeground = Color.decode(properties.getProperty("lineNumber.foreground", "#abb2bf"));
    	LineNumberBackground = Color.decode(properties.getProperty("lineNumber.background", "#FFFFFF"));
    	ClassIdentForeground = Color.decode(properties.getProperty("classIdent.foreground", "#FFCC00"));
    	ProcedureForeground  = Color.decode(properties.getProperty("procedure.foreground",  "#DCDCAA"));
    	CommentForeground    = Color.decode(properties.getProperty("comment.foreground",    "#33FF00"));
    	KeywordForeground    = Color.decode(properties.getProperty("keyword.foreground",    "#569CD6"));
    	ConstantForeground   = Color.decode(properties.getProperty("constant.foreground",   "#CCCC00"));        
    	ErrorForeground      = Color.decode(properties.getProperty("error.foreground",      "#FFFFFF"));
    	ErrorBackground      = Color.decode(properties.getProperty("error.background",      "#FF0000"));
    	HoverForeground      = Color.decode(properties.getProperty("hover.foreground",      "#FF0000"));
    	HoverBackground      = Color.decode(properties.getProperty("hover.background",      "#FFFF00"));
	}

	public static void initOldSimulaEditor(Properties properties) {
    	TextPaneForeground   = Color.decode(properties.getProperty("textPane.foreground",   "#000003")); // Black
    	TextPaneBackground   = Color.decode(properties.getProperty("textPane.background",   "#FFFFFF")); // White
    	LineNumberForeground = Color.decode(properties.getProperty("lineNumber.foreground", "#CCCCFF")); // 
    	LineNumberBackground = Color.decode(properties.getProperty("lineNumber.background", "#FFFFFF")); // White
    	ClassIdentForeground = Color.decode(properties.getProperty("classIdent.foreground", "#267F99")); // Cyan/blågrønn - Type identifiers and declarations
    	ProcedureForeground  = Color.decode(properties.getProperty("procedure.foreground",  "#7A5726")); // Brun/mørk gul - Functions and methods being defined
    	CommentForeground    = Color.decode(properties.getProperty("comment.foreground",    "#009999")); // 
    	KeywordForeground    = Color.decode(properties.getProperty("keyword.foreground",    "#990033")); //
    	ConstantForeground   = Color.decode(properties.getProperty("constant.foreground",   "#CC9900")); //    
    	ErrorForeground      = Color.decode(properties.getProperty("error.foreground",      "#FFFFFF")); // White
    	ErrorBackground      = Color.decode(properties.getProperty("error.background",      "#FF0000")); // Red
    	HoverForeground      = Color.decode(properties.getProperty("hover.foreground",      "#FF0000")); // Red
    	HoverBackground      = Color.decode(properties.getProperty("hover.background",      "#FFFF00")); // Yellow

//		TextPaneForeground = Color.BLACK;
//        TextPaneBackground = Color.WHITE;
//		LineNumberForeground = new Color(204,204,255);
//        LineNumberBackground = Color.WHITE;
//		ClassIdentForeground = new Color(0x267F99); // Cyan/blågrønn - Type identifiers and declarations  SOM VSC
//		ProcedureForeground  = new Color(0x7A5726); // Brun/mørk gul - Functions and methods being defined  SOM VSC
//		CommentForeground = new Color(0,153,153);
//		KeywordForeground = new Color(153,0,51);
//		ConstantForeground = new Color(204,153,0);
	}

	public static void doUpdatePalette() {
        SwingUtilities.invokeLater(() -> {
            new PaletteChooser(null).setVisible(true);
        });
	}

    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

	// **********************************************************
	// *** USER SETTINGS
	// **********************************************************

	/// Load Workspace properties.
	public static void loadThemeProperties(String themeName, boolean reset) {
		Properties properties = new Properties();
		if(! reset) {
			File themeSettingsFile = getThemeSettingsFile(themeName);
			if (themeSettingsFile.exists()) {
				try (FileInputStream inpt = new FileInputStream(getThemeSettingsFile(themeName))) {
					properties.loadFromXML(inpt);
		        } catch (IOException e) {}
			}
		}
		Util.IERR("loadThemeProperties: " + themeName);
		if(themeName.equals(themeNames[0])) initAtomLight(properties);
		else if(themeName.equals(themeNames[1])) initAtomDark(properties);
		else if(themeName.equals(themeNames[2])) initLightMode(properties);
		else if(themeName.equals(themeNames[3])) initDarkMode(properties);
		else if(themeName.equals(themeNames[4])) initOldSimulaEditor(properties);
	}
	
	private static File getThemeSettingsFile(String themeName) {
		String USER_HOME = System.getProperty("user.home");
		File simulaPropertiesDir = new File(USER_HOME, ".simula");
		String fileName = ("simulaEditorTheme"+themeName+".xml").replace(' ', '_');
		File themeSettingsFile = new File(simulaPropertiesDir, fileName);
    	IO.println("Palette.getThemeSettingsFile: " + themeSettingsFile);
		return themeSettingsFile;
	}

	/// Store theme properties.
	public static void storeCurrentThemeProperties() {
		String themeName = currentThemeName;
		Properties properties = new Properties();
    	properties.setProperty("textPane.foreground", toHex(TextPaneForeground));
    	properties.setProperty("textPane.background", toHex(TextPaneBackground));
    	properties.setProperty("lineNumber.foreground", toHex(LineNumberForeground));
    	properties.setProperty("lineNumber.background", toHex(LineNumberBackground));
    	properties.setProperty("classIdent.foreground", toHex(ClassIdentForeground));
    	properties.setProperty("procedure.foreground", toHex(ProcedureForeground));
    	properties.setProperty("comment.foreground", toHex(CommentForeground));
    	properties.setProperty("keyword.foreground", toHex(KeywordForeground));
    	properties.setProperty("constant.foreground", toHex(ConstantForeground));
    	properties.setProperty("error.foreground", toHex(ErrorForeground));
    	properties.setProperty("error.background", toHex(ErrorBackground));
    	properties.setProperty("hover.foreground", toHex(HoverForeground));
    	properties.setProperty("hover.background", toHex(HoverBackground));
        try (FileOutputStream oupt = new FileOutputStream(getThemeSettingsFile(themeName))) {
        	IO.println("Palette.storeCurrentThemeProperties: ");
        	properties.storeToXML(oupt, "Simula Editor Theme " + themeName + " Properties", "UTF-8");
        } catch (IOException e) {}
		
	}

}
