package simula.editor;

import java.awt.Color;

public class Palette {

	/// The color themes used by Simula Editor.
	public enum ColorTheme { lightIntellij, darkIntellij, lightVSC, darkVSC, oldSimEditor };
	
	/// The current color theme
	ColorTheme colorTheme;

    public static Color TextPaneForeground;
    public static Color TextPaneBackground;

    public static Color TooltipForeground;
    public static Color TooltipBackground;
    public static Color TooltipBorder;

    public static Color LineNumberForeground;
    public static Color LineNumberBackground;

    public static Color CommentForeground;
    public static Color CommentBackground;

    public static Color KeywordForeground;
    public static Color KeywordBackground;

    public static Color ConstantForeground;
    public static Color ConstantBackground;

    public static Color ErrorForeground;
    public static Color ErrorBackground;

	public static void init(ColorTheme colorTheme) {
//		TooltipForeground = new Color(0xcccccc);
//		TooltipBackground = new Color(0x2d2d30);
//		TooltipBorder = new Color(0x454545);
		TooltipForeground = Color.RED;
		TooltipBackground = Color.YELLOW;
		TooltipBorder = Color.RED;
		
		ErrorForeground = Color.WHITE;
		ErrorBackground = Color.RED;

        switch(colorTheme) {
			case lightIntellij -> initLightIntellij();
			case darkIntellij  -> initDarkIntellij();
			case lightVSC      -> initLightVSC();
			case darkVSC       -> initDarkVSC();
			case oldSimEditor  -> initOldSimulaEditor();
		}
	}

	private static void initLightIntellij() {
//		Core Language Defaults HighlightingBelow are the exact default hex codes used for standard language tokens across both major default schemes:Code ElementDarcula (Dark) HexIntelliJ Light HexDescription
//			Keywords#CC7832 (Orange)#0033B3 (Blue)public, class, return, if
//			Strings#6A8759 (Olive Green)#067D17 (Green)Text wrapped in quotes "..."
//			Numbers#6897BB (Light Blue)#1750EB (Blue)Integer and floating-point literals
//			Comments#808080 (Gray)#8C8C8C (Gray)Line (//) and block (/*) comments
//			Instance Fields#9876AA (Purple)#871094 (Purple)Non-static class variables
//			Static Fields#9876AA (Italic Purple)#871094 (Italic Purple)Global class variables
//			Method Declarations#FFC66D (Yellow)#00627A (Teal)Functions and methods being defined
//			Method Calls#A9B7C6 (Light Gray)#000000 (Black)Executed method names
//			Classes/Interfaces#A9B7C6 (Light Gray)#000000 (Black)Type identifiers and declarations
//			Annotations#BBB529 (Khaki)#9E880D (Dark Yellow)Metadata markers like @Override
//			Type Parameters#507874 (Dark Teal)#007874 (Teal)Generic types like <T>
//
//
//			General Editor Highlighting
//			Beyond source code text, IntelliJ utilizes functional highlighting for text states, focus management, and error tracking:
//			Selection Foreground: Transparent text layer utilizing a background block of #214283 (Darcula) or #A6D2FF (Light).
//			Identifier under caret: Adds a background block of #344134 (Darcula) or #E2E6D6 (Light) to automatically match all matching variable instances.
//			Identifier under caret (write): Highlights re-assigned variables using #403333 (Darcula) or #F2E2E2 (Light).
//			Search Results: Found text highlights with an active background of #32593D (Darcula) or #BCE2C3 (Light).
//			Errors (Bad Characters): Highlighted with an aggressive red underline or full background block using #BC3F3C.	
        TextPaneForeground = Color.BLACK;
        TextPaneBackground = Color.WHITE;
        
		LineNumberForeground = new Color(0x8C8C8C); // Gray
        LineNumberBackground = Color.WHITE;

		CommentForeground = new Color(0x8C8C8C); // Gray
        CommentBackground = TextPaneBackground;

		KeywordForeground = new Color(0x0033B3); // Gray
        KeywordBackground = TextPaneBackground;

		ConstantForeground = new Color(0x067D17); // Green
        ConstantBackground = TextPaneBackground;
	}

	private static void initDarkIntellij() {
        TextPaneBackground = Color.DARK_GRAY;
        TextPaneForeground = Color.WHITE;
        
		LineNumberForeground = new Color(0x808080); // Gray
        LineNumberBackground = Color.WHITE;

		CommentForeground = new Color(0x808080); // Gray
        CommentBackground = TextPaneBackground;

		KeywordForeground = new Color(0xCC7832); // Gray
        KeywordBackground = TextPaneBackground;

		ConstantForeground = new Color(0x6A8759); // Olive green
        ConstantBackground = TextPaneBackground;
	}
	
	private static void initLightVSC() {
        TextPaneForeground = Color.BLACK;
        TextPaneBackground = Color.WHITE;
        
		LineNumberForeground = new Color(0x8C8C8C); // Gray
		LineNumberBackground = Color.WHITE;

		CommentForeground = new Color(0x008000); // Green
		CommentBackground = TextPaneBackground;

		KeywordForeground = new Color(0x0000FF); // Mørk blå
		KeywordBackground = TextPaneBackground;

		ConstantForeground = new Color(0xA31515); // Mørkerød/vinrød
		ConstantBackground = TextPaneBackground;
	}
	

	private static void initDarkVSC() {
        TextPaneBackground = Color.DARK_GRAY;
        TextPaneForeground = Color.WHITE;
        
		LineNumberForeground = new Color(0x808080); // Gray
        LineNumberBackground = Color.WHITE;

		CommentForeground = new Color(0x6A9955); // Gress grønn
        CommentBackground = TextPaneBackground;

		KeywordForeground = new Color(0x569CD6); // Lys blå
        KeywordBackground = TextPaneBackground;

		ConstantForeground = new Color(0xCE9178); // Terrakotta / lakserosa
        ConstantBackground = TextPaneBackground;
	}

	private static void initOldSimulaEditor() {
        TextPaneForeground = Color.BLACK;
        TextPaneBackground = Color.WHITE;
        
		LineNumberForeground = new Color(204,204,255);
        LineNumberBackground = Color.WHITE;

		CommentForeground = new Color(0,153,153);
        CommentBackground = TextPaneBackground;

		KeywordForeground = new Color(153,0,51);
        KeywordBackground = TextPaneBackground;

		ConstantForeground = new Color(204,153,0);
        ConstantBackground = TextPaneBackground;
	}

}
