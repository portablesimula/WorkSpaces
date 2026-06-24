package simula.editor;

public class Highlighting {

//	Core Language Defaults HighlightingBelow are the exact default hex codes used for standard language tokens across both major default schemes:Code ElementDarcula (Dark) HexIntelliJ Light HexDescription
//
//	Keywords#CC7832 (Orange)#0033B3 (Blue)public, class, return, if
//
//	Strings#6A8759 (Olive Green)#067D17 (Green)Text wrapped in quotes "..."
//
//	Numbers#6897BB (Light Blue)#1750EB (Blue)Integer and floating-point literals
//
//	Comments#808080 (Gray)#8C8C8C (Gray)Line (//) and block (/*) comments
//
//	Instance Fields#9876AA (Purple)#871094 (Purple)Non-static class variables
//
//	Static Fields#9876AA (Italic Purple)#871094 (Italic Purple)Global class variables
//
//	Method Declarations#FFC66D (Yellow)#00627A (Teal)Functions and methods being defined
//
//	Method Calls#A9B7C6 (Light Gray)#000000 (Black)Executed method names
//
//	Classes/Interfaces#A9B7C6 (Light Gray)#000000 (Black)Type identifiers and declarations
//
//	Annotations#BBB529 (Khaki)#9E880D (Dark Yellow)Metadata markers like @Override
//
//	Type Parameters#507874 (Dark Teal)#007874 (Teal)Generic types like <T>
//
//
//	General Editor Highlighting
//	Beyond source code text, IntelliJ utilizes functional highlighting for text states, focus management, and error tracking:
//
//	Selection Foreground: Transparent text layer utilizing a background block of #214283 (Darcula) or #A6D2FF (Light).
//
//	Identifier under caret: Adds a background block of #344134 (Darcula) or #E2E6D6 (Light) to automatically match all matching variable instances.
//
//	Identifier under caret (write): Highlights re-assigned variables using #403333 (Darcula) or #F2E2E2 (Light).
//
//	Search Results: Found text highlights with an active background of #32593D (Darcula) or #BCE2C3 (Light).
//
//	Errors (Bad Characters): Highlighted with an aggressive red underline or full background block using #BC3F3C.	
	
	interface darkMode {
		int keyWord = 0xCC7832; // Orange
	}
	
	abstract class ColorMode {
		public abstract int KeyWord();
		public abstract int String();
		public abstract int Number();
		public abstract int Comment();
	}
	
	class DarkMode extends ColorMode {
		public int KeyWord() { return 0xCC7832; } // Orange
		public int String() { return 0x6A8759; } // Olive Green)() { return 0x067D17; } //Green)Text wrapped in quotes "..."
		public int Number() { return 0x6897BB; } // Light Blue)() { return 0x1750EB; } //Blue)Integer and floating-point literals
		public int Comment() { return 0x808080; } // Gray)() { return 0x8C8C8C; } //Gray)Line; } ////) and block; } ///*) comments

	}
	
	class LightMode extends ColorMode {
		public int KeyWord() { return 0x0033B3; } // Blue
		public int String() { return 0x067D17; } // Green  Text wrapped in quotes "..."
		public int Number() { return 0x1750EB; } // Blue   Integer and floating-point literals
		public int Comment() { return 0x8C8C8C; } // Gray

	}

}
