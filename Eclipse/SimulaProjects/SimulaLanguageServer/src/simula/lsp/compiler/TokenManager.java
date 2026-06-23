package simula.lsp.compiler;

import org.eclipse.lsp4j.SemanticTokens;
import org.eclipse.lsp4j.SemanticTokensParams;
import simula.lsp.SimulaLanguageServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
	
	class SimulaTokenType {
		String VSCode_TokenType;
		int index;
		
		public SimulaTokenType(String VSCode_TokenType, int index) {
			this.VSCode_TokenType = VSCode_TokenType;
			this.index = index;
		}
	}
	
	// Simula Token Types mapped to Standard Token Types
	public final SimulaTokenType KEYWORD = new SimulaTokenType( "keyword" , 1 );
	
//	public class MyTextDocumentService implements TextDocumentService {

	    // Helper class to hold absolute parsed token data
	    class AbsoluteToken {
	        int line;      // 0-based
	        int character; // 0-based
	        int length;
	        int tokenTypeIndex;
	        int tokenModifiersBitmask;

	        AbsoluteToken(int line, int character, int length, int type, int mod) {
	            this.line = line;
	            this.character = character;
	            this.length = length;
	            this.tokenTypeIndex = type;
	            this.tokenModifiersBitmask = mod;
	        }
	    }

//	    @Override
	    public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params, SimulaLanguageServer server) {
	        return CompletableFuture.supplyAsync(() -> {
	            String documentUri = params.getTextDocument().getUri();
	            
	            // 1. Fetch your document state (since Sync is FULL, look up your latest document text cache)
//	            String documentText = MyDocumentTracker.get(uri); 
	    		DocumentManager documentManager = server.getDocumentManager();
	    		SourceDocumentItem sourceItem = documentManager.get(documentUri);
	    		String documentText = sourceItem.getText();

	            // 2. Parse text and extract tokens in absolute positions
	            List<AbsoluteToken> absoluteTokens = parseTokens(documentText);

	            // 3. Sort tokens sequentially (Line first, then Character position)
	            absoluteTokens.sort((t1, t2) -> {
	                if (t1.line != t2.line) return Integer.compare(t1.line, t2.line);
	                return Integer.compare(t1.character, t2.character);
	            });

	            // 4. Compress absolute data into LSP delta format
	            List<Integer> encodedData = new ArrayList<>();
	            int prevLine = 0;
	            int prevChar = 0;

	            for (AbsoluteToken token : absoluteTokens) {
	                int deltaLine = token.line - prevLine;
	                // If it is on the same line, char offset is relative to the previous token's start char
	                int deltaChar = (deltaLine == 0) ? (token.character - prevChar) : token.character;

	                encodedData.add(deltaLine);
	                encodedData.add(deltaChar);
	                encodedData.add(token.length);
	                encodedData.add(token.tokenTypeIndex);
	                encodedData.add(token.tokenModifiersBitmask);

	                // Update trackers for next iteration
	                prevLine = token.line;
	                prevChar = token.character;
	            }

	            return new SemanticTokens(encodedData);
	        });
	    }

	    private List<AbsoluteToken> parseTokens(String text) {
	        List<AbsoluteToken> tokens = new ArrayList<>();
	        // TODO: Plug your AST parser / Lexer here. 
	        // Example: If a keyword "class" is at Line 0, Char 5, length 5:
	        // tokens.add(new AbsoluteToken(0, 5, 5, 3, 0)); // index 3 = "class"
	        return tokens;
	    }
	

}
