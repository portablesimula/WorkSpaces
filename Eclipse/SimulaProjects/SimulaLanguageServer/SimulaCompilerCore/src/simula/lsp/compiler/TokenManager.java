package simula.lsp.compiler;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class TokenManager {
	
	// Standard Token Types defined by VSCode:
	//
	// TokenType		Description
	//
	// namespace		Packages, namespaces, or modules
	// type				General types (e.g., classes, structs) that don't have a more specific type
	// class			Class types
	// enum				Enumeration types
	// interface		Interface definitions
	// struct			Data structure types
	// typeParameter	Generic type parameters
	// parameter		Function or method parameters
	// variable			Local or global variables
	// property			Member variables or object fields
	// enumMember		Individual enum values/variants
	// function			Standalone function names
	// member			Class method or member function names
	// macro			Macro invocations
	// label			Code labels (e.g., jump labels)
	// keyword			Language keywords
	// string			Text strings
	// number			Numeric literals
	// regexp			Regular expressions
	// operator			Math/logic operators
	
//	class SimulaTokenType {
//		String VSCode_TokenType;
//		int index;
//		
//		public SimulaTokenType(String VSCode_TokenType, int index) {
//			this.VSCode_TokenType = VSCode_TokenType;
//			this.index = index;
//		}
//	}
	
	// Simula Token Types mapped to Standard Token Types
	// KEYWORD, IDENTIFIER, NUMBER_LITERAL, TEXT_LITERAL, OPERATOR, COMMENT, WHITESPACE, UNKNOWN
	public static final SimulaTokenType KEYWORD =	new SimulaTokenType( "keyword",  1 );
	public static final SimulaTokenType STRING =	new SimulaTokenType( "string",   2 );
	public static final SimulaTokenType NUMBER =	new SimulaTokenType( "number",   3 );
	public static final SimulaTokenType COMMENT =	new SimulaTokenType( "macro",    4 );
	public static final SimulaTokenType OTHER =		new SimulaTokenType( "variable", 5 );
	
	public static String edSimulaTokenType(int index) {
		switch(index) {
			case 1: return "keyword";
			case 2: return "string";
			case 3: return "number";
			case 4: return "comment";
			case 5: return "other";
		}
		return "UNKNOWN";
	}
	
}
