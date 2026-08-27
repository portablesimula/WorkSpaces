package simula.core.builder.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simula.Comn;
import simula.Option;
import simula.core.DocumentManager;
import simula.core.builder.SimulaTokenType;
import simula.core.utilities.KeyWord;
import simula.core.utilities.Util;

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
	

//    public static List<String> tokenTypes = Arrays.asList("class", "procedure", "parameter", "identifier", "variable",
//			  "keyword", "comment", "string", "constant", "symbol", "whiyeSpaces");

	/// NOTE: SEE: simula.editor.LspTextPanel
	public static List<String> tokenTypes = Arrays.asList(
        SimulaTokenTypes.Keyword,    // Index: 0
        SimulaTokenTypes.Class,      // Index: 1  Class identifier
        SimulaTokenTypes.Attribute,  // Index: 2  Class attribute
        SimulaTokenTypes.Procedure,  // Index: 3  Procedure identifier
        SimulaTokenTypes.Variable,   // Index: 4  Variable identifier
        SimulaTokenTypes.Parameter,  // Index: 5  Class/Procedure Parameter identifier
        SimulaTokenTypes.String,     // Index: 6  String constant
        SimulaTokenTypes.Character,  // Index: 7  Character constant
        SimulaTokenTypes.Number,     // Index: 8
        SimulaTokenTypes.Operator,   // Index: 9  LT, EQ, ...
        SimulaTokenTypes.Label,      // Index: 10
        SimulaTokenTypes.Comment,    // Index: 11
        SimulaTokenTypes.WhiteSpace, // Index: 12
        SimulaTokenTypes.Symbol      // Index: 13
    );

    public static final int SimulaTokenKeyword    = 0;
    public static final int SimulaTokenClass      = 1; //  Class identifier
    public static final int SimulaTokenAttribute  = 2; //  Class attribute
    public static final int SimulaTokenProcedure  = 3; //  Procedure identifier
    public static final int SimulaTokenVariable   = 4; //  Variable identifier
    public static final int SimulaTokenParameter  = 5; //  Class/Procedure Parameter identifier
    public static final int SimulaTokenString     = 6; //  String constant
    public static final int SimulaTokenCharacter  = 7; //  Character constant
    public static final int SimulaTokenNumber     = 8; //
    public static final int SimulaTokenOperator   = 9; //  LT, EQ, ...
    public static final int SimulaTokenLabel      = 10; //
    public static final int SimulaTokenComment    = 11; //
    public static final int SimulaTokenWhiteSpace = 12; //
    public static final int SimulaTokenSymbol     = 13; //

    public static String getTokenType(int tokenTypeIndex) {
    	String tokenType = TokenManager.tokenTypes.get(tokenTypeIndex);
    	return tokenType;
     }
	
    public static int getTokenTypeIndex(String tokenType) {
    	int tokenTypeIndex = TokenManager.tokenTypes.indexOf(tokenType);
    	if(tokenTypeIndex < 0) Util.IERR("Undefined token type: "+tokenType);
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

	public static List<SimulaDiagnostic> getDiagnostics(String documentUri) {
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
		return documentManager.getDiagnostics();
	}


	private final static boolean TESTING = true;
	
	public static List<Integer> generateSemanticTokens(List<LexToken> lexTokenList) {
		IO.println("DocumentManager.generateSemanticTokens: " + lexTokenList.size());
        List<Integer> encodedData = new ArrayList<>();
        
        int currentLine = 0;
        int prevTokenLine = 0;
        int prevTokenColumn = 0;
        
        LOOP:for (LexToken lexToken : lexTokenList) {
            // Beregn relative verdier (deltas)
            int deltaLine = 0;  // Number of lines down from the start of the previous token.
            int deltaStart = 0; // Number of characters to the right from the start of the previous token
                                // or from the start of the line if deltaLine > 0.
            
            if(Option.TESTING_VERIFY) {
//	            if (lexToken.keyWord == KeyWord.WHITESPACES) { continue LOOP; }
//	            if (lexToken.keyWord == KeyWord.NEWLINE) { continue LOOP; }
	            currentLine = lexToken.lineNumber;
            } else {
	            if (lexToken.keyWord == KeyWord.WHITESPACES) { continue LOOP; }
	            if (lexToken.keyWord == KeyWord.NEWLINE) {
	            	currentLine++;
	            	continue LOOP;
	            }
            }
            
            deltaLine = currentLine - prevTokenLine;
        	if(deltaLine == 0) {
        		// Fortsett på samme linje
        		// meaning the current token is on the same line as the previous token),
        		// deltaStart is relative to the start character (column offset) of the previous token.
        		//
        		// |  token  token   token    | lexToken.column = 17, prevTokenColumn = 9
        		// |         ------->         | deltaStart = lexToken.column - prevTokenColumn = 17 - 9 = 8
        		deltaStart = lexToken.column - prevTokenColumn;
        		IO.println("\nFortsett på samme linje: deltaStart = lexToken.column - lastDeltaStart: " + deltaStart);
        	} else {
        		// Start NEWLINE
            	// meaning the current token is on a new line relative to the previous token),
            	// deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
        		//
        		// |  token  token   token    | lexToken.column = 17, prevTokenColumn = 9
        		// |         ------->         | deltaStart = lexToken.column - prevTokenColumn = 17 - 9 = 8
        		deltaStart = lexToken.column;
        		IO.println("\nStart NEWLINE: deltaStart = lexToken.column: " + deltaStart);
        	}

            // Legg til det semantiske tokenet
            encodedData.add(deltaLine);
            encodedData.add(deltaStart);
            encodedData.add(lexToken.length);
            encodedData.add(lexToken.tokenTypeIndex);
            
//          encodedData.add(lexToken.tokenModifiersBitmask);
            encodedData.add(0);
            
            if(TESTING) {
            	IO.println(""+lexToken);
 	    		String str = Comn.printable(lexToken.tokenText);
 	    		IO.println("==> DeltaLine " + deltaLine + ": " + TokenManager.tokenTypes.get(lexToken.tokenTypeIndex)
 	    		+ "[deltaStart:" + deltaStart + ", lng:" + lexToken.length + "] Text: \"" + str + '"');
             }
            
            // Oppdater historikk for neste iterasjon
            prevTokenLine = currentLine;
            prevTokenColumn = lexToken.column;
        	IO.println("Fortsett: prevTokenColumn: " + prevTokenColumn);
        }

        return encodedData;
    }


}
