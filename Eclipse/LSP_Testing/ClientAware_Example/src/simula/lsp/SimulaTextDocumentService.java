package simula.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.messages.Either3;
import org.eclipse.lsp4j.services.TextDocumentService;

import simula.lsp.compiler.DocumentManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SimulaTextDocumentService implements TextDocumentService {

    private final SimulaLanguageServer server;

    public SimulaTextDocumentService(SimulaLanguageServer server) {
        this.server = server;
    }


	/**
	 * The document open notification is sent from the client to the server to
	 * signal newly opened text documents. The document's truth is now managed
	 * by the client and the server must not try to read the document's truth
	 * using the document's uri.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
//	@JsonNotification
    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
    	DocumentManager.didOpen(params, server);
    }

	/**
	 * The document change notification is sent from the client to the server to
	 * signal changes to a text document.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentChangeRegistrationOptions}
	 */
//	@JsonNotification
	@Override
	public void didChange(DidChangeTextDocumentParams params) {
    	DocumentManager.didChange(params, server);
	}

	/**
	 * The document close notification is sent from the client to the server
	 * when the document got closed in the client. The document's truth now
	 * exists where the document's uri points to (e.g. if the document's uri is
	 * a file uri the truth now exists on disk).
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
//	@JsonNotification
	@Override
	public void didClose(DidCloseTextDocumentParams params) {
    	DocumentManager.didClose(params, server);
	}

	/**
	 * The document save notification is sent from the client to the server when
	 * the document is saved in the client.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentSaveRegistrationOptions}
	 */
//	@JsonNotification
	@Override
	public void didSave(DidSaveTextDocumentParams params) {
    	DocumentManager.didSave(params, server);
	}

	/**
	 * The document will save notification is sent from the client to the server before the document is actually saved.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
//	@JsonNotification
	@Override
	public void willSave(WillSaveTextDocumentParams params) {
    	DocumentManager.willSave(params, server);		
	}
    

	/**
	 * The {@code textDocument/semanticTokens/full} request is sent from the client to the server to return
	 * the semantic tokens for a whole file.
	 */
	@Override
	public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The {@code textDocument/semanticTokens/full/delta} request is sent from the client to the server to return
	 * the semantic tokens delta for a whole file.
	 */
	@Override
	public CompletableFuture<Either<SemanticTokens, SemanticTokensDelta>> semanticTokensFullDelta(SemanticTokensDeltaParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The {@code textDocument/semanticTokens/range} request is sent from the client to the server to return
	 * the semantic tokens delta for a range.
	 * <p>
	 * When a user opens a file it can be beneficial to only compute the semantic tokens for the visible range
	 * (faster rendering of the tokens in the user interface). If a server can compute these tokens faster than
	 * for the whole file it can provide a handler for the textDocument/semanticTokens/range request to handle
	 * this case special. Please note that if a client also announces that it will send the
	 * textDocument/semanticTokens/range server should implement this request as well to allow for flicker free
	 * scrolling and semantic coloring of a minimap.
	 */
	@Override
	public CompletableFuture<SemanticTokens> semanticTokensRange(SemanticTokensRangeParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The go to declaration request is sent from the client to the server to resolve
	 * the declaration location of a symbol at a given text document position.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.DeclarationRegistrationOptions}
	 */
    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> declaration(DeclarationParams params) {
        return CompletableFuture.supplyAsync(() -> {
            // 1. Hent URI og posisjon fra VS Code-forespørselen
            String uri = params.getTextDocument().getUri();
            int line = params.getPosition().getLine();
            int character = params.getPosition().getCharacter();

            // 2. Egen logg/logikk for Simula-kompilatoren
            // Her slår du opp i Simulas Abstract Syntax Tree (AST) for å finne
            // hvor identifikatoren (f.eks. en klasse eller klasseprosedyre) ble deklarert.
            Location declarationLocation = findSimulaDeclaration(uri, line, character);

            // 3. Returner lokasjonen tilbake til VS Code
            return Either.forLeft(List.of(declarationLocation));
        });
    }
    
    private Location findSimulaDeclaration(String uri, int line, int character) {
        // Simula-spesifikk parsing og AST-oppslag skjer her
    	int startLine = 44;
    	int startPos = 13;
    	int endLine = 46;
    	int endPos = 7;
    	Position start = new Position(startLine, startPos);
    	Position end = new Position(endLine, endPos);
        return new Location(uri, new org.eclipse.lsp4j.Range(start, end));
    }

    
    
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++ RESTEN +++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++ RESTEN +++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++ RESTEN +++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++ RESTEN +++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++ RESTEN +++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++ RESTEN +++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++ RESTEN +++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++ RESTEN +++++++++++++++++++++++++++++++++++++++++++++++
    
	@Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The request is sent from the client to the server to resolve additional
	 * information for a given completion item.
	 */
	@Override
	public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The hover request is sent from the client to the server to request hover
	 * information at a given text document position.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.HoverRegistrationOptions}
	 */
	@Override
	public CompletableFuture<Hover> hover(HoverParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The signature help request is sent from the client to the server to
	 * request signature information at a given cursor position.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.SignatureHelpRegistrationOptions}
	 */
	@Override
	public CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The goto definition request is sent from the client to the server to resolve
	 * the definition location of a symbol at a given text document position.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.DefinitionRegistrationOptions}
	 */
	@Override
	public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(DefinitionParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The goto type definition request is sent from the client to the server to resolve
	 * the type definition location of a symbol at a given text document position.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TypeDefinitionRegistrationOptions}
	 */
	@Override
	public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> typeDefinition(TypeDefinitionParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The goto implementation request is sent from the client to the server to resolve
	 * the implementation location of a symbol at a given text document position.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.ImplementationRegistrationOptions}
	 */
	@Override
	public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> implementation(ImplementationParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The references request is sent from the client to the server to resolve
	 * project-wide references for the symbol denoted by the given text document
	 * position.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.ReferenceRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document highlight request is sent from the client to the server to
	 * resolve document highlights for a given text document position.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.DocumentHighlightRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(DocumentHighlightParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document symbol request is sent from the client to the server to list all
	 * symbols found in a given text document.
	 * <p>
	 * Registration Options: {@link TextDocumentRegistrationOptions}
	 * <p>
	 * <b>Caveat</b>: although the return type allows mixing the
	 * {@link DocumentSymbol} and {@link SymbolInformation} instances into a list do
	 * not do it because the clients cannot accept a heterogeneous list. A list of
	 * {@code DocumentSymbol} instances is only a valid return value if the
	 * {@link DocumentSymbolCapabilities#getHierarchicalDocumentSymbolSupport()
	 * textDocument.documentSymbol.hierarchicalDocumentSymbolSupport} is
	 * {@code true}. More details on this difference between the LSP and the LSP4J
	 * can be found <a href="https://github.com/eclipse-lsp4j/lsp4j/issues/252">here</a>.
	 * </p>
	 * Servers should whenever possible return {@link DocumentSymbol} since it is the richer data structure.
	 */
	@Override
	public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(DocumentSymbolParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The code action request is sent from the client to the server to compute
	 * commands for a given text document and range. These commands are
	 * typically code fixes to either fix problems or to beautify/refactor code.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.CodeActionRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The request is sent from the client to the server to resolve additional information for a given code action. This is usually used to compute
	 * the `edit` property of a code action to avoid its unnecessary computation during the `textDocument/codeAction` request.
	 */
	@Override
	public CompletableFuture<CodeAction> resolveCodeAction(CodeAction unresolved) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The code lens request is sent from the client to the server to compute
	 * code lenses for a given text document.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.CodeLensRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The code lens resolve request is sent from the client to the server to
	 * resolve the command for a given code lens item.
	 */
	@Override
	public CompletableFuture<CodeLens> resolveCodeLens(CodeLens unresolved) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document formatting request is sent from the client to the server to
	 * format a whole document.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.DocumentFormattingRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document range formatting request is sent from the client to the
	 * server to format a given range in a document.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.DocumentRangeFormattingRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document ranges formatting request is sent from the client to the
	 * server to format multiple ranges at once in a document.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.DocumentRangeFormattingRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<? extends TextEdit>> rangesFormatting(DocumentRangesFormattingParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document on type formatting request is sent from the client to the
	 * server to format parts of the document during typing.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.DocumentOnTypeFormattingRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The rename request is sent from the client to the server to do a
	 * workspace wide rename of a symbol.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.RenameOptions RenameRegistrationOptions}
	 */
	@Override
	public CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The linked editing range request is sent from the client to the server to return
	 * for a given position in a document the range of the symbol at the position
	 * and all ranges that have the same content. Optionally a word pattern can be
	 * returned to describe valid contents. A rename to one of the ranges can be
	 * applied to all other ranges if the new content is valid. If no result-specific
	 * word pattern is provided, the word pattern from the client's language configuration
	 * is used.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.LinkedEditingRangeRegistrationOptions}
	 */
	@Override
	public CompletableFuture<LinkedEditingRanges> linkedEditingRange(LinkedEditingRangeParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document will save request is sent from the client to the server before the document is actually saved.
	 * The request can return an array of TextEdits which will be applied to the text document before it is saved.
	 * Please note that clients might drop results if computing the text edits took too long or if a server constantly fails on this request.
	 * This is done to keep the save fast and reliable.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.TextDocumentRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<TextEdit>> willSaveWaitUntil(WillSaveTextDocumentParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document links request is sent from the client to the server to request the location of links in a document.
	 * <p>
	 * Registration Options: {@link org.eclipse.lsp4j.DocumentLinkRegistrationOptions}
	 */
	@Override
	public CompletableFuture<List<DocumentLink>> documentLink(DocumentLinkParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document link resolve request is sent from the client to the server to resolve the target of a given document link.
	 */
	@Override
	public CompletableFuture<DocumentLink> documentLinkResolve(DocumentLink params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The document color request is sent from the client to the server to list all color references found in a given text
	 * document. Along with the range, a color value in RGB is returned.
	 * <p>
	 * Clients can use the result to decorate color references in an editor. For example:
	 * <p><ul>
	 * <li>Color boxes showing the actual color next to the reference
	 * <li>Show a color picker when a color reference is edited
	 * </ul>
	 */
	@Override
	public CompletableFuture<List<ColorInformation>> documentColor(DocumentColorParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The color presentation request is sent from the client to the server to obtain a list of presentations for a color
	 * value at a given location. Clients can use the result to
	 * <p><ul>
	 * <li>modify a color reference.
	 * <li>show in a color picker and let users pick one of the presentations
	 * </ul>
	 */
	@Override
	public CompletableFuture<List<ColorPresentation>> colorPresentation(ColorPresentationParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The folding range request is sent from the client to the server to return all folding
	 * ranges found in a given text document.
	 */
	@Override
	public CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The prepare rename request is sent from the client to the server to setup and test the validity of a rename
	 * operation at a given location.
	 */
	@Override
	public CompletableFuture<Either3<Range, PrepareRenameResult, PrepareRenameDefaultBehavior>> prepareRename(PrepareRenameParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The type hierarchy request is sent from the client to the server to return a type hierarchy for
	 * the language element of given text document positions. Will return {@code null} if the server
	 * couldn't infer a valid type from the position. The type hierarchy requests are executed in two steps:
	 * <p><ol>
	 * <li>first a type hierarchy item is prepared for the given text document position.
	 * <li>for a type hierarchy item the supertype or subtype type hierarchy items are resolved.
	 * </ol>
	 */
	@Override
	public CompletableFuture<List<TypeHierarchyItem>> prepareTypeHierarchy(TypeHierarchyPrepareParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The request is sent from the client to the server to resolve the supertypes for
	 * a given type hierarchy item. Will return {@code null} if the server couldn't infer
	 * a valid type from {@link TypeHierarchySupertypesParams#getItem()
	 * TypeHierarchySupertypesParams.item}. The request doesn't define
	 * its own client and server capabilities. It is only issued if a server registers for the
	 * {@code textDocument/prepareTypeHierarchy} request.
	 */
	@Override
	public CompletableFuture<List<TypeHierarchyItem>> typeHierarchySupertypes(TypeHierarchySupertypesParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The request is sent from the client to the server to resolve the subtypes for
	 * a given type hierarchy item. Will return {@code null} if the server couldn't infer
	 * a valid type from {@link TypeHierarchySupertypesParams#getItem()
	 * TypeHierarchySupertypesParams.item}. The request doesn't define
	 * its own client and server capabilities. It is only issued if a server registers for the
	 * {@code textDocument/prepareTypeHierarchy} request.
	 */
	@Override
	public CompletableFuture<List<TypeHierarchyItem>> typeHierarchySubtypes(TypeHierarchySubtypesParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Bootstraps call hierarchy by returning the item that is denoted by the given document
	 * and position. This item will be used as entry into the call graph. Providers should
	 * return null when there is no item at the given location.
	 */
	@Override
	public CompletableFuture<List<CallHierarchyItem>> prepareCallHierarchy(CallHierarchyPrepareParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Provide all incoming calls for an item, e.g all callers for a method. In graph terms this describes directed
	 * and annotated edges inside the call graph, e.g the given item is the starting node and the result is the nodes
	 * that can be reached.
	*/
	@Override
	public CompletableFuture<List<CallHierarchyIncomingCall>> callHierarchyIncomingCalls(CallHierarchyIncomingCallsParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	* Provide all outgoing calls for an item, e.g call calls to functions, methods, or constructors from the given item. In
	* graph terms this describes directed and annotated edges inside the call graph, e.g the given item is the starting
	* node and the result is the nodes that can be reached.
	*/
	@Override
	public CompletableFuture<List<CallHierarchyOutgoingCall>> callHierarchyOutgoingCalls(CallHierarchyOutgoingCallsParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The {@code textDocument/selectionRange} request is sent from the client to the server to return
	 * suggested selection ranges at an array of given positions. A selection range is a range around
	 * the cursor position which the user might be interested in selecting.
	 */
	@Override
	public CompletableFuture<List<SelectionRange>> selectionRange(SelectionRangeParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Language Server Index Format (LSIF) introduced the concept of symbol monikers to help associate symbols across
	 * different indexes. This request adds capability for LSP server implementations to provide the same symbol moniker
	 * information given a text document position. Clients can utilize this method to get the moniker at the current
	 * location in a file user is editing and do further code navigation queries in other services that rely on LSIF indexes
	 * and link symbols together.
	 * <p>
	 * The {@code textDocument/moniker} request is sent from the client to the server to get the symbol monikers for a given
	 * text document position. An array of Moniker types is returned as response to indicate possible monikers at the given location.
	 * If no monikers can be calculated, an empty array or null should be returned.
	 */
	@Override
	public CompletableFuture<List<Moniker>> moniker(MonikerParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The inlay hints request is sent from the client to the server to compute inlay hints for a given [text document, range]
	 * tuple that may be rendered in the editor in place with other text.
	 */
	@Override
	public CompletableFuture<List<InlayHint>> inlayHint(InlayHintParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The request is sent from the client to the server to resolve additional information for a given inlay hint.
	 * This is usually used to compute the {@code tooltip}, {@code location} or {@code command} properties of an
	 * inlay hint's label part to avoid its unnecessary computation during the {@code textDocument/inlayHint} request.
	 */
	@Override
	public CompletableFuture<InlayHint> resolveInlayHint(InlayHint unresolved) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The inline value request is sent from the client to the server to compute inline values for a given text document
	 * that may be rendered in the editor at the end of lines.
	 */
	@Override
	public CompletableFuture<List<InlineValue>> inlineValue(InlineValueParams params) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The text document diagnostic request is sent from the client to the server to ask the server to compute the diagnostics
	 * for a given document. As with other pull requests the server is asked to compute the diagnostics for the currently
	 * synced version of the document.
	 */
	@Override
	public CompletableFuture<DocumentDiagnosticReport> diagnostic(DocumentDiagnosticParams params) {
//       String uri = params.getTextDocument().getUri();
//        String previousResultId = params.getPreviousResultId();
//
//        // 1. If the client passes a matching previous result ID and document hasn't changed, 
//        // return an unchanged report to optimize performance.
//        if (previousResultId != null && previousResultId.equals(this.lastResultId) && !isDocumentDirty(uri)) {
//            RelatedUnchangedDocumentDiagnosticReport unchangedReport = new RelatedUnchangedDocumentDiagnosticReport();
//            unchangedReport.setResultId(this.lastResultId);
//            
////            DocumentDiagnosticReport report = new DocumentDiagnosticReport(Either.forRight(unchangedReport));
//            RelatedUnchangedDocumentDiagnosticReport relatedUnchangedDocumentDiagnosticReport = null;
//            DocumentDiagnosticReport report = new DocumentDiagnosticReport(relatedUnchangedDocumentDiagnosticReport);
//            NOT IMPL - SJEKK DETTE
//            return CompletableFuture.completedFuture(report);
//        }
//
//        // 2. Otherwise, compute fresh diagnostics
//        List<Diagnostic> diagnostics = computeDiagnosticsFor(uri);
//
//        // 3. Build a full diagnostic report
//        RelatedFullDocumentDiagnosticReport fullReport = new RelatedFullDocumentDiagnosticReport();
//        fullReport.setItems(diagnostics);
//        
//        // Generate a unique result ID for this state (e.g., hash or an incremental counter)
//        this.lastResultId = "diagnostic-id-" + System.currentTimeMillis();
//        fullReport.setResultId(this.lastResultId);
//
//        //public class DocumentDiagnosticReport extends Either<RelatedFullDocumentDiagnosticReport, RelatedUnchangedDocumentDiagnosticReport> {
////        DocumentDiagnosticReport report = new DocumentDiagnosticReport(Either.forLeft(fullReport));
//        DocumentDiagnosticReport report = new DocumentDiagnosticReport(fullReport);
//        return CompletableFuture.completedFuture(report);
		DocumentDiagnosticReport report = DocumentManager.diagnostic(params, server);
		return CompletableFuture.completedFuture(report);
    }

//    private List<Diagnostic> computeDiagnosticsFor(String uri) {
//        List<Diagnostic> diagnostics = new ArrayList<>();
//        // Your parsing and validation logic goes here...
//        // Diagnostic diagnostic = new Diagnostic(range, message, severity, source);
//        return diagnostics;
//    }
//
//    private boolean isDocumentDirty(String uri) {
//        // Implement logic checking if your internal abstract syntax tree (AST) 
//        // or document tracking has been modified since the last validation loop.
//        return false; 
//    }
    
    // Remember to implement mandatory lifecycle overrides like didOpen, didChange, didSave!

	/**
	 * The inline completion request is sent from the client to the server to compute inline completions for a given text document
	 * either explicitly by a user gesture or implicitly when typing.
	 * <p>
	 * Inline completion items usually complete bigger portions of text (e.g., whole methods) and in contrast to completions, items
	 * can complete code that might be syntactically or semantically incorrect.
	 * <p>
	 * Due to this, inline completion items are usually not suited to be presented in normal code completion widgets like a list of
	 * items. One possible approach can be to present the information inline in the editor with lower contrast.
	 * <p>
	 * When multiple inline completion items are returned, the client may decide whether the user can cycle through them or if they,
	 * along with their filterText, are merely for filtering if the user continues to type without yet accepting the inline
	 * completion item.
	 * <p>
	 * Clients may choose to send information about the user’s current completion selection via context if completions are visible at
	 * the same time. In this case, returned inline completions should extend the text of the provided completion.
	 */
	@Override
	public CompletableFuture<Either<List<InlineCompletionItem>, InlineCompletionList>> inlineCompletion(InlineCompletionParams params) {
		throw new UnsupportedOperationException();
	}

}
