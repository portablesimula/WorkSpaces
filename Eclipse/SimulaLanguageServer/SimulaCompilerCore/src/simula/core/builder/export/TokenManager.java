package simula.core.builder.export;

import java.util.Arrays;
import java.util.List;

import simula.core.DocumentManager;
import simula.core.builder.SimulaTokenType;

/// @author Øystein Myhre Andersen
/// @author Google AI
public class TokenManager {

    public static List<String> tokenTypes = Arrays.asList("class", "procedure", "parameter", "identifier", "variable",
			  "keyword", "comment", "string", "constant", "symbol", "whiyeSpaces");

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

	public static List<LexToken> getTokenList(String documentUri) {
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
		return documentManager.getTokenList();
	}

	public static List<SimulaDiagnostic> getDiagnostics(String documentUri) {
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
		return documentManager.getDiagnostics();
	}


////    @Override
//    public static SemanticTokens getAllSemanticTokens(SemanticTokensParams params, SimulaLanguageServer server) {
//        LOG.info("TokenManager.semanticTokensFullBody: BEGIN");
//            String documentUri = params.getTextDocument().getUri();
//            
//            // 1. Fetch your document state (since Sync is FULL, look up your latest document text cache)
////            String documentText = MyDocumentTracker.get(uri); 
//    		DocumentManager documentManager = server.getDocumentManager();
//    		SourceDocumentItem sourceItem = documentManager.get(documentUri);
//    		String documentText = sourceItem.getText();
//
//            // 2. Parse text and extract tokens in absolute positions
////            List<LspToken> lspTokens = parseTokens(documentText);
//    		sourceItem.createTokenList();
//            List<LspToken> lspTokens = sourceItem.tokenList;
//            LOG.info("TokenManager.semanticTokensFull: lspTokens: " + lspTokens);
//            IO.println("TokenManager.semanticTokensFull: lspTokens: " + lspTokens);
//            
//            
//
//            // 3. Sort tokens sequentially (Line first, then Character position)
//            lspTokens.sort((t1, t2) -> {
//                if (t1.line != t2.line) return Integer.compare(t1.line, t2.line);
//                return Integer.compare(t1.column, t2.column);
//            });
//
//            // 4. Compress absolute data into LSP delta format
//            List<Integer> encodedData = new ArrayList<>();
//            int prevLine = 0;
//            int prevChar = 0;
//
//            for (LspToken token : lspTokens) {
//                int deltaLine = token.line - prevLine;
//                // If it is on the same line, char offset is relative to the previous token's start char
//                int deltaChar = (deltaLine == 0) ? (token.column - prevChar) : token.column;
//                
//                LOG.info("TokenManager.semanticTokensFull: " + token
//                		+ " ==> deltaLine:" + deltaLine
//                		+ ", deltaChar: " + deltaChar
//                		+ ", length:" + token.length
//                		+ ", type:" + token.tokenTypeIndex + ':' + edSimulaTokenType(token.tokenTypeIndex));
//
//                encodedData.add(deltaLine);
//                encodedData.add(deltaChar);
//                encodedData.add(token.length);
//                encodedData.add(token.tokenTypeIndex);
//                encodedData.add(token.tokenModifiersBitmask);
//
//                // Update trackers for next iteration
//                prevLine = token.line;
//                prevChar = token.column;
//            }
//
//            return new SemanticTokens(encodedData);
//    }
    // SLIK GJØRES DET I BallerinaLang:
	// public SemanticToken processSemanticToken(List<Integer> data, SemanticToken previousToken) {
	//    int line = this.getLine();
	//    int column = this.getColumn();
	//    int prevTokenLine = line;
	//    int prevTokenColumn = column;
	//
	//    if (previousToken != null) {
	//        if (line == previousToken.getLine()) {
	//            column -= previousToken.getColumn();
	//        }
	//        line -= previousToken.getLine();
	//    }
	//    data.add(line);
	//    data.add(column);
	//    data.add(this.getLength());
	//    data.add(this.getType());
	//    data.add(this.getModifiers());
	//    return new SemanticToken(prevTokenLine, prevTokenColumn);
	// }


}
