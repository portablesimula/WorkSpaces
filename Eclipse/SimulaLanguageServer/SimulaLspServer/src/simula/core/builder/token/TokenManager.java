package simula.core.builder.token;

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
import simula.core.utilities.KeyWord;
import simula.core.utilities.LOG;
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

    public static SemanticTokensWithRegistrationOptions getSemanticOptions() {
    	// Set up semantic tokens options with your token types and modifiers legend
        SemanticTokensWithRegistrationOptions semanticOptions = new SemanticTokensWithRegistrationOptions();
        SemanticTokensLegend legend = new SemanticTokensLegend(
                STANDARD_TOKEN_TYPES, 
                STANDARD_TOKEN_MODIFIERS
            );
        semanticOptions.setLegend(legend);
        semanticOptions.setFull(true); // Enable full document semantic tokens
        // Optionally enable delta tracking if supported by your server logic
        // semanticOptions.setFull(new SemanticTokensServerFull(true)); 
        return semanticOptions;
    }

    public static String getTokenType(int tokenTypeIndex) {
    	String tokenType = TokenManager.STANDARD_TOKEN_TYPES.get(tokenTypeIndex);
    	return tokenType;
     }
	
    public static int getTokenTypeIndex(String tokenType) {
    	int tokenTypeIndex = TokenManager.STANDARD_TOKEN_TYPES.indexOf(tokenType);
    	return tokenTypeIndex;
     }
    
	public static List<LexToken> getTokenList(String documentUri) {
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
		return documentManager.getTokenList();
	}

	public static List<Diagnostic> getDiagnostics(String documentUri) {
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
		return documentManager.getDiagnostics();
	}


	/// Detailed Specification of deltaStart
	/// 
	/// - Definition: At index 5*i + 1, deltaStart represents the token's start character
	///   offset relative to the start of the previous token.
	///
	/// - The Rule for Line Changes:
	/// 
	///   - If deltaLine is 0 (meaning the current token is on the same line as the previous token),
	///     deltaStart is relative to the start character (column offset) of the previous token.
	/// 
	///   - If deltaLine is greater than 0 (meaning the current token is on a new line relative to
	///     the previous token), deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
	/// 
	/// - For the First Token (i = 0): deltaLine is relative to the start of the file/document (line 0),
	///   and deltaStart is relative to column 0.
	/// 
	public static List<Integer> generateSemanticTokens(List<LexToken> lexTokenList) {
		
//		Option.internal.TRACE_NEW_SEMTOKEN = 1;
		
		if(Option.internal.TRACE_NEW_SEMTOKEN > 0) Util.println("DocumentManager.generateSemanticTokens: " + lexTokenList.size());
        List<Integer> encodedData = new ArrayList<>();
        
        int currentLine = 0;
        int prevTokenLine = 0;
        int prevTokenColumn = 0;
        
        LOOP:for (LexToken lexToken : lexTokenList) {
        	
        	if(lexToken.keyWord == KeyWord.COMMENT_TEXT || lexToken.keyWord == KeyWord.COMMENT_KEY) {
        		if(lexToken.tokenText.stripTrailing() == "") {
        			continue LOOP;
        		}
        	}
        	currentLine = lexToken.lineNumber;

        	// Beregn relative verdier (deltas)
            int deltaLine = 0;  // Number of lines down from the start of the previous token.
            int deltaStart = 0; // Number of characters to the right from the start of the previous token
                                // or from the start of the line if deltaLine > 0.
            deltaLine = currentLine - prevTokenLine;
            if(deltaLine > 0) {
        		// Start NEWLINE,
            	// meaning the current token is on a new line relative to the previous token),
            	// deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
        		deltaStart = lexToken.column;
        		if(Option.internal.TRACE_NEW_SEMTOKEN > 1) Util.println("\nStart NEWLINE: deltaStart = lexToken.column: " + deltaStart);            	
            } else {
        		// Continue on current line,
        		// meaning the current token is on the same line as the previous token),
        		// deltaStart is relative to the start character (column offset) of the previous token.
        		deltaStart = lexToken.column - prevTokenColumn;
        		if(Option.internal.TRACE_NEW_SEMTOKEN > 1) Util.println("\nCONTINUE LINE: deltaStart = lexToken.column - lastDeltaStart: " + deltaStart);
        	}
            
            if(Option.LEX_VERIFY) {
            	if(deltaStart < 0) Util.IERR(""+lexToken);
            	if(lexToken.tokenTypeIndex < 0) Util.IERR(""+lexToken);
            	if(lexToken.tokenText == null || lexToken.tokenText.isEmpty()) Util.IERR(""+lexToken);
                if(lexToken.length < 1 ) {
                	Util.IERR("TokenManager.generateSemanticTokens: FAILED: lexToken.length < 1  " + lexToken);
                	System.exit(0);
                }
            }
            
            String tokenText = lexToken.tokenText;
            prevTokenColumn = lexToken.column;
            int length = lexToken.length;
            LOOP1:while(length > 0) {
            	char c = tokenText.charAt(0);
            	if(! Character.isWhitespace(c)) break LOOP1;
        		tokenText = tokenText.substring(1);
        		prevTokenColumn++;
        		deltaStart++;
        		length--;
            }
            tokenText = tokenText.stripTrailing();
            length = tokenText.length();
            if(length != lexToken.length)
            Util.println("TokenManager.generateSemanticTokens: T"+lexToken.length+'|'+Comn.printable(lexToken.tokenText)+ "| ==> T"
            		+length+'|'+Comn.printable(tokenText)+ "| ==>"
            		);

            // Add the semantic token
            encodedData.add(deltaLine);
            encodedData.add(deltaStart);
//            encodedData.add(lexToken.semTokenLength());
            encodedData.add(length);
            encodedData.add(lexToken.tokenTypeIndex);
            
//          encodedData.add(lexToken.tokenModifiersBitmask);
            encodedData.add(0);
            
//            Option.internal.TRACE_NEW_SEMTOKEN = 1;
            if(Option.internal.TRACE_NEW_SEMTOKEN > 0) {
        		String str = Comn.printable(tokenText);
        		String sem = ("DeltaLine " + deltaLine + ": " + TokenManager.STANDARD_TOKEN_TYPES.get(lexToken.tokenTypeIndex)
        					+ "[deltaStart:" + deltaStart + ", lng:" + length + "] Text: \"" + str + '"');
            	if(Option.internal.TRACE_NEW_SEMTOKEN > 1) {
            		Util.println(""+lexToken);
            		Util.println("==> " + sem);
            	} else {
            		Util.println("NEW SemToken: " + sem);
            	}
            }
            
            // Oppdater historikk for neste iterasjon
            prevTokenLine = currentLine;
            if(Option.internal.TRACE_NEW_SEMTOKEN > 1) Util.println("Fortsett: prevTokenColumn: " + prevTokenColumn);
        }
        return encodedData;
    }



	// ****************************************************************
	// *** TokenListVerifyer  -- SEE: LspTextPanel.fillTextPane
	// ****************************************************************
	/// Validates whether a list of encoded data complies with the LSP specification for Semantic Tokens.
	///
	/// @param semanticTokens The list of integers to be validated.
	public static void tokenListVerifyer(List<String> sourceLines, List<Integer> semanticTokens) {
		
//		Option.internal.TRACE_VERIFY_TOKEN = 1;
		boolean TRACE_RECONSTR = false;// true;
		
		int maxTokenType = STANDARD_TOKEN_TYPES.size() - 1;

		if (semanticTokens == null) {
			LOG.error("TokenManager.tokenListVerifyer: VERIFIER FAILED: encodedData == null.");
		}

		int semSize = semanticTokens.size();
		if (semSize % 5 != 0) {
			LOG.error("TokenManager.tokenListVerifyer: VERIFIER FAILED: encodedData of wrong size (" + semSize + "). Must be a multiple of 5.");
		}

		if(Option.internal.TRACE_VERIFY_TOKEN > 0) {
			int i = 1;
			for(String line:sourceLines) {
				Util.println("Line " + i++ + ": |" + Comn.printable(line) + '|');
			}
		}
		StringBuilder reconstr = new StringBuilder();
		int sourcePos = 0;
		int lineNumber = 0;
		int prevTextLength = 0;
		String originalLine = sourceLines.get(lineNumber);

		int x = 0;
		while(x < semanticTokens.size()) {
			int deltaLine = semanticTokens.get(x++);
			int deltaStartChar = semanticTokens.get(x++);
			int length = semanticTokens.get(x++);
			int tokenTypeIndex = semanticTokens.get(x++);
			int tokenModifiersBitmask = semanticTokens.get(x++);

			if(Option.internal.TRACE_VERIFY_TOKEN > 0) {
				Util.println("TokenManager.tokenListVerifyer: SEM_TOKEN: semToken: deltaLine=" + deltaLine + ", deltaStartChar="+deltaStartChar
						+ ", length="+length+", tokenTypeIndex=" + tokenTypeIndex+", tokenModifiersBitmask=" + tokenModifiersBitmask);
			}

			// 1. Calculate absolute positions based on LSP delta rules
			if (deltaLine > 0) {
				checkEqual("case 1", tokenTypeIndex, lineNumber, sourceLines.get(lineNumber++), reconstr.toString());
				// Start NEWLINE
				// meaning the current token is on a new line relative to the previous token),
				// deltaStart is relative to 0 (the absolute beginning/left margin of that new line).
				while((deltaLine--) > 1) {
					// Empty line
					checkEqual("case 2", tokenTypeIndex, lineNumber, sourceLines.get(lineNumber++), "");
				}
				prevTextLength = 0;
				sourcePos = 0;
				originalLine = sourceLines.get(lineNumber);
				reconstr = new StringBuilder();
				if(TRACE_RECONSTR) Util.println("CASE 1: NEW RECONSTR: Line "+lineNumber+" |" + Comn.printable(originalLine) + '|');
			} else if (deltaLine < 0) {
				VERIFIER_FAILED(lineNumber, sourcePos, x - 5, "deltaLine is negative (" + deltaLine + ").");
			}
			
			// DeltaStartChar can't be negative
			if (deltaStartChar < 0) {
				VERIFIER_FAILED(lineNumber, sourcePos, x - 4, "deltaStartChar is negative (" + deltaStartChar + ").");
			}

			// Validate tokenType against the range of allowed types (if provided)
			if (maxTokenType >= 0 && tokenTypeIndex >= maxTokenType) {
				VERIFIER_FAILED(lineNumber, sourcePos, x - 2, "tokenTypeIndex (" + tokenTypeIndex + ") is outside legal range[0:" + maxTokenType + "]");
			}

			if (tokenTypeIndex < 0)	VERIFIER_FAILED(lineNumber, sourcePos, x - 2, "tokenTypeIndex can't be negative.");

			// tokenModifiers can't be negative (bitmask)
			if (tokenModifiersBitmask < 0) {
				VERIFIER_FAILED(lineNumber, sourcePos, x - 1, "tokenModifiers is negative (" + tokenModifiersBitmask + ").");
			}

			// 3. Pad missing characters on the current line
			int gap = deltaStartChar - prevTextLength;
			if(gap < 0) {
				VERIFIER_FAILED(lineNumber, sourcePos, x - 1, "Illegal gap between tokens: " + gap);
			}
			if(gap != 0) {
				reconstr.append(" ".repeat(gap));
				if(TRACE_RECONSTR) Util.println("CASE 2: ADD GAP RECONSTR|" + reconstr + '|');
				sourcePos += gap;
				if(Option.internal.TRACE_VERIFY_TOKEN > 0) Util.println("LINE " + lineNumber + ": PAD SPACE: gap = " + gap + " ==> LINE|" + reconstr + '|');
			}

			// 4. Insert the token text
			if(length > 0) {
				String tokenText = originalLine.substring(sourcePos, sourcePos + length);
				if(hasTrailingBlanks(tokenText)) {
					VERIFIER_FAILED(lineNumber, sourcePos, x - 1, "Token text has trailing blanks|" + tokenText + '|');					
				}
				reconstr.append(tokenText);
				if(TRACE_RECONSTR) Util.println("CASE 3: INSERT  RECONSTR|" + Comn.printable(reconstr.toString()) + '|');
				if(Option.internal.TRACE_VERIFY_TOKEN > 0) Util.println("LINE " + lineNumber + ": APPEND TEXT: length = " + length + ", TEXT|" + tokenText + "| ==> LINE|" + reconstr + '|');
				sourcePos += length;
			} else if (length <= 0)	VERIFIER_FAILED(lineNumber, sourcePos, x - 3, "length must be greater then 0 (" + length + ").");
			prevTextLength = length;
		}
		String reconstrLine = reconstr.toString();
		while(lineNumber < (sourceLines.size()-1)) {
			checkEqual("case 3", 0, lineNumber, sourceLines.get(lineNumber++), reconstrLine);
			reconstrLine = "";
		}
		if(TRACE_RECONSTR) Util.println("TokenManager.tokenListVerifyer: DONE");
	}
	
	private static void VERIFIER_FAILED(int lineNumber, int sourcePos, int index, String mss) {
		LOG.error("TokenManager.tokenListVerifyer: VERIFIER FAILED: at index " + index + ", line " + lineNumber + ", pos=" + sourcePos  + ": " + mss);
	}

	private static boolean hasTrailingBlanks(String str) {
	    if (str == null || str.isEmpty()) return false;
	    char lastChar = str.charAt(str.length() - 1);
	    return Character.isWhitespace(lastChar);
	}

	private static void checkEqual(String debugName, int tokenTypeIndex, int lineNumber, String original, String reconstr) {
		String originalLine = original.replace("\t", " ");
		String reconstrLine = reconstr.replace("\t", " ") + '\n';
		if(Option.internal.TRACE_VERIFY_TOKEN > 0) {
			Util.println("LINE " + lineNumber + ": RECONSTR: |" + Comn.printable(reconstrLine) + '|');
			Util.println("LINE " + lineNumber + ": ORIGINAL: |" + Comn.printable(originalLine) + '|');
		}
		if(! reconstrLine.equals(originalLine)) {
			LOG.error("TokenManager.tokenListVerifyer: VERIFIER FAILED(" + debugName + "): Reconstructed text differ from original text on line " + lineNumber);
			Util.println("LINE " + lineNumber + ": RECONSTR_LINE: |" + Comn.printable(reconstrLine) + '|');
			Util.println("LINE " + lineNumber + ": ORIGINAL_LINE: |" + Comn.printable(originalLine) + '|');
			int lng1 = original.length();
			int lng2 = reconstr.length();
			LOG.error("Original Text(lng:"+lng1+"): |" + Comn.printable(original) + '|');
			LOG.error("Reconstr Text(lng:"+lng2+"): |" + Comn.printable(reconstr) + '|');
			System.exit(-1);
		}
	}

}