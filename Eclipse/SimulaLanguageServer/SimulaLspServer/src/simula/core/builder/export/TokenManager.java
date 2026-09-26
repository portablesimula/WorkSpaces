package simula.core.builder.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.SemanticTokenModifiers;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.eclipse.lsp4j.SemanticTokensLegend;
import org.eclipse.lsp4j.SemanticTokensWithRegistrationOptions;

import simula.Comn;
import simula.Option;
import simula.core.DocumentManager;
import simula.core.builder.SimulaTokenType;
import simula.core.utilities.KeyWord;
import simula.core.utilities.Util;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class TokenManager {

	// Standard Token Types defined by the Official Language Server Protocol (LSP) Specification.
    public static final List<String> STANDARD_TOKEN_TYPES = Arrays.asList(
        SemanticTokenTypes.Namespace,     // Index 0:  For identifiers that declare or reference a namespace, module, or package.
        SemanticTokenTypes.Type,          // Index 1:  For identifiers that reference a generic type or a type not covered by more precise rules.
        SemanticTokenTypes.Class,         // Index 2:  For identifiers that reference a class type.
        SemanticTokenTypes.Enum,          // Index 3:  For identifiers that reference an enumeration type.
        SemanticTokenTypes.Interface,     // Index 4:  For identifiers that reference an interface type.
        SemanticTokenTypes.Struct,        // Index 5:  For identifiers that reference a struct type.
        SemanticTokenTypes.TypeParameter, // Index 6:  For identifiers that reference a type parameter (e.g., generics).
        SemanticTokenTypes.Parameter,     // Index 7:  For identifiers that reference a function or method parameter.
        SemanticTokenTypes.Variable,      // Index 8:  For identifiers that reference a local or global variable.
        SemanticTokenTypes.Property,      // Index 9:  For identifiers that reference a member variable, property, or field of a class/struct.
        SemanticTokenTypes.EnumMember,    // Index 10: For identifiers that reference an enum constant/variant.
        SemanticTokenTypes.Event,         // Index 11: For identifiers that reference an event object or event structure.
        SemanticTokenTypes.Function,      // Index 12: For identifiers that reference a standalone function.
        SemanticTokenTypes.Method,        // Index 13: For identifiers that reference a member function or class method.
        SemanticTokenTypes.Macro,         // Index 14: For identifiers that reference a macro preprocessor definition.
        SemanticTokenTypes.Keyword,       // Index 15: For tokens that represent language-specific reserved keywords.
        SemanticTokenTypes.Modifier,      // Index 16: For tokens that modify types or declarations (e.g., public, private).
        SemanticTokenTypes.Comment,       // Index 17: For tokens that represent code comments.
        SemanticTokenTypes.String,        // Index 18: For tokens that represent string literals.
        SemanticTokenTypes.Number,        // Index 19: For tokens that represent numeric literals.
        SemanticTokenTypes.Regexp,        // Index 20: For tokens that represent regular expression literals.
        SemanticTokenTypes.Operator,      // Index 21: For tokens that represent expressions or arithmetic operators.
        SemanticTokenTypes.Decorator      // Index 22: For tokens that represent annotations or decorators (added in later LSP specs).
    );

	//
	// Standard Semantic Token Modifiers
    private static final List<String> STANDARD_TOKEN_MODIFIERS = Arrays.asList(
		SemanticTokenModifiers.Declaration,    // Index 0: e.g., where a symbol is declared.
		SemanticTokenModifiers.Definition,     // Index 1: e.g., where a symbol is executed/fully defined)
		SemanticTokenModifiers.Readonly,       // Index 2: e.g., constants, immutable variables)
		SemanticTokenModifiers.Static,         // Index 3: e.g., class-level variables or functions)
		SemanticTokenModifiers.Deprecated,     // Index 4: e.g., out-of-date API endpoints)
		SemanticTokenModifiers.Abstract,       // Index 5: e.g., abstract classes or interfaces)
		SemanticTokenModifiers.Async,          // Index 6: e.g., asynchronous functions or loops)
		SemanticTokenModifiers.Modification,   // Index 7: e.g., a variable being assigned to)
		SemanticTokenModifiers.Documentation,  // Index 8: e.g., part of a JSDoc, Javadoc, or Docstring block)
		SemanticTokenModifiers.DefaultLibrary  // Index 9: e.g., global built-ins like console or window)
	);

    

    // 1. Define the ordered array of Token Types. 
    // The index positions (0, 1, 2...) are what the server will transmit later.
    private static final List<String> SUPPORTED_TOKEN_TYPES = Arrays.asList(
    		SemanticTokenTypes.Namespace,
    		"namespace", // Index 0
        "type",      // Index 1
        "class",     // Index 2
        "enum",      // Index 3
        "interface", // Index 4
        "struct",    // Index 5
        "typeParameter", // Index 6
        "parameter", // Index 7
        "variable",  // Index 8
        "property",  // Index 9
        "macro",     // Index 10
        "function",  // Index 11
        "method"     // Index 12
    );

    // Leave modifiers empty for this baseline configuration
    private static final List<String> SUPPORTED_TOKEN_MODIFIERS = Arrays.asList();

    public static SemanticTokensWithRegistrationOptions getSemanticOptions() {
    	// Set up semantic tokens options with your token types and modifiers legend
        SemanticTokensWithRegistrationOptions semanticOptions = new SemanticTokensWithRegistrationOptions();
//        SemanticTokensLegend legend = new SemanticTokensLegend(
//            Arrays.asList("class", "interface", "variable", "function"), 
//            Arrays.asList("declaration", "readonly")
//        );
        SemanticTokensLegend legend = new SemanticTokensLegend(
//                SUPPORTED_TOKEN_TYPES, 
//                SUPPORTED_TOKEN_MODIFIERS
                STANDARD_TOKEN_TYPES, 
                STANDARD_TOKEN_MODIFIERS
            );
        semanticOptions.setLegend(legend);
        semanticOptions.setFull(true); // Enable full document semantic tokens
        return semanticOptions;
    }
    
    

//    public static List<String> tokenTypes = Arrays.asList("class", "procedure", "parameter", "identifier", "variable",
//			  "keyword", "comment", "string", "constant", "symbol", "whiyeSpaces");

//	/// NOTE: SEE: simula.editor.LspTextPanel
//	public static List<String> tokenTypes = Arrays.asList(
//        SimulaTokenTypes.Keyword,    // Index: 0
//        SimulaTokenTypes.Class,      // Index: 1  Class identifier
//        SimulaTokenTypes.Attribute,  // Index: 2  Class attribute
//        SimulaTokenTypes.Procedure,  // Index: 3  Procedure identifier
//        SimulaTokenTypes.Variable,   // Index: 4  Variable identifier
//        SimulaTokenTypes.Parameter,  // Index: 5  Class/Procedure Parameter identifier
//        SimulaTokenTypes.String,     // Index: 6  String constant
//        SimulaTokenTypes.Character,  // Index: 7  Character constant
//        SimulaTokenTypes.Number,     // Index: 8
//        SimulaTokenTypes.Operator,   // Index: 9  LT, EQ, ...
//        SimulaTokenTypes.Label,      // Index: 10
//        SimulaTokenTypes.Comment,    // Index: 11
//        SimulaTokenTypes.WhiteSpace, // Index: 12
//        SimulaTokenTypes.Symbol      // Index: 13
//    );
//
//    public static final int SimulaTokenKeyword    = 0;
//    public static final int SimulaTokenClass      = 1; //  Class identifier
//    public static final int SimulaTokenAttribute  = 2; //  Class attribute
//    public static final int SimulaTokenProcedure  = 3; //  Procedure identifier
//    public static final int SimulaTokenVariable   = 4; //  Variable identifier
//    public static final int SimulaTokenParameter  = 5; //  Class/Procedure Parameter identifier
//    public static final int SimulaTokenString     = 6; //  String constant
//    public static final int SimulaTokenCharacter  = 7; //  Character constant
//    public static final int SimulaTokenNumber     = 8; //
//    public static final int SimulaTokenOperator   = 9; //  LT, EQ, ...
//    public static final int SimulaTokenLabel      = 10; //
//    public static final int SimulaTokenComment    = 11; //
//    public static final int SimulaTokenWhiteSpace = 12; //
//    public static final int SimulaTokenSymbol     = 13; //

    public static String getTokenType(int tokenTypeIndex) {
//    	String tokenType = TokenManager.tokenTypes.get(tokenTypeIndex);
    	String tokenType = TokenManager.STANDARD_TOKEN_TYPES.get(tokenTypeIndex);
    	return tokenType;
     }
	
    public static int getTokenTypeIndex(String tokenType) {
    	Util.println("TokenManager.getTokenTypeIndex: " + tokenType);
//    	int tokenTypeIndex = TokenManager.tokenTypes.indexOf(tokenType);
    	int tokenTypeIndex = TokenManager.STANDARD_TOKEN_TYPES.indexOf(tokenType);
    	Util.println("TokenManager.getTokenTypeIndex: " + tokenType + " ==> " + tokenTypeIndex);
//    	if(tokenTypeIndex < 0) Util.IERR("Undefined token type: "+tokenType);
    	return tokenTypeIndex;
     }
    
    
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

	public static List<LexToken> getTokenList(String documentUri) {
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
		return documentManager.getTokenList();
	}

	public static List<Diagnostic> getDiagnostics(String documentUri) {
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
		return documentManager.getDiagnostics();
	}


	private final static boolean TESTING = true;
	
	public static List<Integer> generateSemanticTokens(List<LexToken> lexTokenList) {
		if(Option.internal.TRACE_NEW_SEMTOKEN > 0) Util.println("DocumentManager.generateSemanticTokens: " + lexTokenList.size());
        List<Integer> encodedData = new ArrayList<>();
        
        int currentLine = 0;
        int prevTokenLine = 0;
        int prevTokenColumn = 0;
        
        LOOP:for (LexToken lexToken : lexTokenList) {
            // Beregn relative verdier (deltas)
            int deltaLine = 0;  // Number of lines down from the start of the previous token.
            int deltaStart = 0; // Number of characters to the right from the start of the previous token
                                // or from the start of the line if deltaLine > 0.
            
//            if(Option.TESTING_VERIFY) {
//	            if (lexToken.keyWord == KeyWord.WHITESPACES) { continue LOOP; }
//	            if (lexToken.keyWord == KeyWord.NEWLINE) { continue LOOP; }
	            currentLine = lexToken.lineNumber;
//            } else {
//	            if (lexToken.keyWord == KeyWord.WHITESPACES) { continue LOOP; }
//	            if (lexToken.keyWord == KeyWord.NEWLINE) {
//	            	currentLine++;
//	            	continue LOOP;
//	            }
//            }
            
            deltaLine = currentLine - prevTokenLine;
            if(deltaLine > 0) {
        		// Start NEWLINE
            	// meaning the current token is on a new line relative to the previous token),
            	// deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
        		//
        		// |  token  token   token    | lexToken.column = 17, prevTokenColumn = 9
        		// |         ------->         | deltaStart = lexToken.column - prevTokenColumn = 17 - 9 = 8
        		deltaStart = lexToken.column;
        		if(Option.internal.TRACE_NEW_SEMTOKEN > 1) Util.println("\nStart NEWLINE: deltaStart = lexToken.column: " + deltaStart);            	
            } else {
        		// Fortsett på samme linje
        		// meaning the current token is on the same line as the previous token),
        		// deltaStart is relative to the start character (column offset) of the previous token.
        		//
        		// |  token  token   token    | lexToken.column = 17, prevTokenColumn = 9
        		// |         ------->         | deltaStart = lexToken.column - prevTokenColumn = 17 - 9 = 8
        		deltaStart = lexToken.column - prevTokenColumn;
        		if(Option.internal.TRACE_NEW_SEMTOKEN > 1) Util.println("\nCONTINUE LINE: deltaStart = lexToken.column - lastDeltaStart: " + deltaStart);
        	}

            // Legg til det semantiske tokenet
            encodedData.add(deltaLine);
            encodedData.add(deltaStart);
            encodedData.add(lexToken.length);
            encodedData.add(lexToken.tokenTypeIndex);
            
//          encodedData.add(lexToken.tokenModifiersBitmask);
            encodedData.add(0);
            
            if(Option.internal.TRACE_NEW_SEMTOKEN > 0) {
        		String str = Comn.printable(lexToken.tokenText);
        		String sem = ("DeltaLine " + deltaLine + ": " + TokenManager.tokenTypes.get(lexToken.tokenTypeIndex)
        					+ "[deltaStart:" + deltaStart + ", lng:" + lexToken.length + "] Text: \"" + str + '"');
            	if(Option.internal.TRACE_NEW_SEMTOKEN > 1) {
            		Util.println(""+lexToken);
            		Util.println("==> " + sem);
            	} else {
            		Util.println("NEW SemToken: " + sem);
            	}
            }
            
            // Oppdater historikk for neste iterasjon
            prevTokenLine = currentLine;
            prevTokenColumn = lexToken.column;
            if(Option.internal.TRACE_NEW_SEMTOKEN > 1) Util.println("Fortsett: prevTokenColumn: " + prevTokenColumn);
        }

        return encodedData;
    }


}
