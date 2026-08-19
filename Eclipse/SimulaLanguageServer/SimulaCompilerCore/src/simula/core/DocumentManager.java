package simula.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import simula.SimTextDocumentContentChangeEvent;
import simula.core.builder.DocumentTextUpdater;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.util.LexToken;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.declaration.StandardClass;
import simula.core.syntaxClass.statement.ProgramModule;
import simula.core.utilities.LOG;
import simula.core.utilities.SimulaDiagnostic;
import simula.core.utilities.Util;

/// Vi må lagre innholdet til dokumentene som er åpne i editoren.
/// Siden editoren kan ha endringer som ikke er lagret til disken ennå,
/// må serveren stole på teksten den får tilsendt over LSP
/// 
/// @author Øystein Myhre Andersen
/// @author Google AI
public class DocumentManager {

	final public String documentUri;
	final public File sourceFileDir;
	public int documentVersion;
	public String sourceCode;
	
	public SimulaBuilder simBuilder;
	public SimulaCoder simCoder;
	

	/// The Compiler mode.
//	public CompilerMode compilerMode;
	public boolean compileViaJavaSource;

	/// The source file name.
	public String sourceFileName;

	/// The source file name without .sim
	public String sourceName;
	
	/// The set of external .jar files.
	public Vector<String> externalJarFileNames;

	// Specifies where to place generated executable .jar file;
	// Also used to search for precompiled .jar attribute files
	public File jarFileDir = null;
	
	// Specifies where to search for precompiled classes and procedures
	// If not found, jarFileDir is then searched
	public File extLib = null;


	// ***************************************************************
	// *** Static variables
	// ***************************************************************

	/// The Simula release identification.
	/// 
	/// NOTE: When updating release id, change version in SimulaExtractor and RuntimeSystem
	public static final String simulaReleaseID = "Simula-2.0";

	/// Packet name used in generated .java files.
	/// NOTE: Must be a single identifier.
	public static String packetName = "simprog";

	/// Where to find the Simula Runtime System.
	public static File simulaRtsLib;
	
	/// Source file is case sensitive.
	public static boolean CaseSensitive=false;
	
	/// Output messages about what the compiler is doing.
	public static boolean verbose = false; 
	
	/// Generate warning messages
	public static boolean WARNINGS=true;

	/// TRUE:Do not create popUps at runtime
	public static boolean noPopup = false; 
	
	/// true: Don't execute generated .jar file
	public static boolean noExecution = false;
	
	/// false: Disable all language extensions. In other words,
	/// follow the Simula Standard literally
	public static boolean EXTENSIONS=true;
	
    // Nøkkelen er filens URI (f.eks. file:///path/to/file.txt)
//    private final ConcurrentHashMap<String, SourceDocumentItem> openDocuments = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, DocumentManager> openDocuments = new ConcurrentHashMap<>();

    public DocumentManager(String documentUri, int documentVersion, String sourceCode) {
    	this.documentUri = documentUri;
    	this.sourceFileDir = new File(documentUri).getParentFile();
    	this.documentVersion = documentVersion;
    	this.sourceCode = sourceCode;
    	sourceFileName = this.documentUri;
		sourceName = getSourceName(this.documentUri);
		externalJarFileNames = new Vector<String>();
		compileViaJavaSource = false;
		StandardClass.INITIATE(this);
		createJarFilesDirectory();
    }
    
    private void createJarFilesDirectory() {
    	// Create output .jar-files Directory
//		IO.println("DocumentManager.createJarFilesDirectory: sourceFileDir=" + documentManager.sourceFileDir);
//		IO.println("DocumentManager.createJarFilesDirectory: jarFileDir=" + SimulaCoder.jarFileDir);
    	if(this.jarFileDir == null) {
    		File userDir = new File(System.getProperty("user.dir"));
    		this.jarFileDir = new File(userDir,"bin");
    	}
    	LOG.info("DocumentManager.createJarFilesDirectory: jarFileDir=" + this.jarFileDir);
    	this.jarFileDir.mkdirs();
    	if (! this.jarFileDir.canWrite()) {
    		Util.IERR("SimulaCompiler.setOutputDir: Unable to write to " + this.jarFileDir);
    	}    	
    }
    private String getSourceName(String documentUri) {
    	String sourceName = Util.getBaseName(documentUri);
		if (!Util.isJavaIdentifier(sourceName)) {
			String prevName = sourceName;
			sourceName = Util.makeJavaIdentifier(sourceName);
			Util.generalWarning("The source file name '" + prevName + "' is not a legal class identifier. Modified to: " + sourceName);
		}
    	return sourceName;
    }

    /// Debug Utility
    public static DocumentManager getDocumentManager(String documentUri)  {
    	return openDocuments.get(documentUri);
    }

	/// Get the text document's Token List.
	public List<LexToken> getTokenList() {
		return simBuilder.tokenList;
	}

	/// Get the text document's getDiagnostic List.
	public List<SimulaDiagnostic> getDiagnostics() {
		return simBuilder.diagnostics;
	}

	/// Get the text document's SyntaxTree.
	public ProgramModule getSyntaxTree() {
		return simBuilder.syntaxTree;
	}

//	/// Set the text document's SyntaxTree.
//	public void setSyntaxTree(final ProgramModule syntaxTree) {
//		this.syntaxTree = syntaxTree;
//	}

	/// Get the text document's diagnostics.
	public List<SimulaDiagnostic> getDiagnostis() {
		return simBuilder.diagnostics;
	}
	
//	public void addDiagnostic(SimulaDiagnostic diagnostic) {
//		simBuilder.diagnostics.add(diagnostic);
//	}

//	/// Set the text document's diagnostics.
//	public void setDiagnostics(final List<SimulaDiagnostic> diagnostics) {
//		this.diagnostics = diagnostics;
//	}

	/// Get the text document's uri.
	public String getUri() {
		return documentUri;
	}

//	/// Set the text document's uri.
//	public void setUri(final String uri) {
//		documentUri = uri;
//	}

	/// Get the version number of this document (it will strictly increase after each change, including undo/redo).
	public int getVersion() {
		return documentVersion;
	}

	/// Set the version number of this document (it will strictly increase after each change, including undo/redo).
	public void setVersion(final int documentVersion) {
		this.documentVersion = documentVersion;
	}

	/// Get the content of the opened text document.
	public String getText() {
		return sourceCode;
	}

	/// Set the content of the opened text document.
	public void setText(final String text) {
		sourceCode = text;
	}
    
	/// The document open notification is sent from the client to the server to
	/// signal newly opened text documents. The document's truth is now managed
	/// by the client and the server must not try to read the document's truth
	/// using the document's uri.
//    public static void didOpen(DidOpenTextDocumentParams params, SimulaLanguageServer server) {
    public static void didOpen(final String documentUri, final int version, final String sourceCode) {
//        public DocumentManager documentManager = DocumentManager.get(documentUri);
    	LOG.info("DocumentManager.didOpen: BEGIN");
    	if(openDocuments.get(documentUri) != null) {
    		Util.IERR("DOKUMENTET FINNES FRA FØR");
    	}
    	DocumentManager documentManager = new DocumentManager(documentUri, version, sourceCode);
    	openDocuments.put(documentUri, documentManager);
    	
    	documentManager.tryCreateBuilder();

    	LOG.info("DocumentManager.didOpen: RETURNS");
    }
    

    /// The document change notification is sent from the client to the server to
	/// signal changes to a text document.
//	public static void didChange(DidChangeTextDocumentParams params, SimulaLanguageServer server) {
	public static void didChange(final String documentUri, final List<SimTextDocumentContentChangeEvent> changes) {
    	LOG.info("DocumentManager.didChange: BEGIN");
    	DocumentManager documentManager = openDocuments.get(documentUri);
		
//		String currentText = sourceItem.getText();
		String currentText = documentManager.getText();
    	LOG.info("DocumentManager.didChange: Current Text: " + currentText);

//    	String updatedText = DocumentTextUpdater.applyChanges(currentText, params.getContentChanges());
    	String updatedText = DocumentTextUpdater.applyChanges(currentText, changes);
    	LOG.info("DocumentManager.didChange: Updated Text: " + updatedText);
    	documentManager.setText(updatedText);
    	
    	documentManager.tryCreateBuilder();

    	LOG.info("DocumentManager.didChange: RETURNS");
	}

	
	///The document will save notification is sent from the client to the server before the document is actually saved.
//	public static void willSave(WillSaveTextDocumentParams params, SimulaLanguageServer server) {
	public static void willSave(final String documentUri, final String reason) {
//    	DocumentManager documentManager = openDocuments.get(documentUri);

		// 2. Add your pre-save routine here
		LOG.info("Document is about to save: " + documentUri + " due to reason: " + reason);

		// Since TextDocumentSyncKind is 'Full', you already have the latest text state 
		// synchronized via your prior 'didChange' notification handlers.
		performPreSaveCleanup(documentUri);
	}

	private static void performPreSaveCleanup(String documentUri) {
		// Implement server-side logic here (e.g., locking states, clearing stale caches)
	}

	
	/// The document save notification is sent from the client to the server when
	/// the document is saved in the client.
//	public static void didSave(DidSaveTextDocumentParams params, SimulaLanguageServer server) {
	public static void didSave(final String documentUri) {
		
    	DocumentManager documentManager = openDocuments.get(documentUri);
		
		String fullText =  documentManager.getText();

		if (fullText == null) {
			// Fallback: If cache missed, you could read directly from the URI/disk
			return;
		}

		// 3. Trigger server-side logic (e.g., Compilation, Re-indexing, Validation)
		try {
//			DocumentManager.runCompilerOrValidator(documentUri, fullText);
	    	documentManager.tryCreateBuilder();
		} catch (Exception e) {
			// Log your errors appropriately
			e.printStackTrace();
		}
	}
	

	/// The document close notification is sent from the client to the server
	/// when the document got closed in the client. The document's truth now
	/// exists where the document's uri points to (e.g. if the document's uri is
	/// a file uri the truth now exists on disk).
//	public static void didClose(DidCloseTextDocumentParams params, SimulaLanguageServer server) {
	public static void didClose(final String documentUri) {
		
//    	DocumentManager documentManager = openDocuments.get(documentUri);
        openDocuments.remove(documentUri);

		System.out.println("Document closed on client side: " + documentUri);
	}
	

	public void tryCreateBuilder() {
    	LOG.info("DocumentManager.tryCreateBuilder: BEGIN");
//		String sourceText = this.getText();
		SimulaBuilder newSimBuilder = new SimulaBuilder(this);
		// TRY BUILD SYNTAX TREE ...
		try {
			newSimBuilder.doBuilding();
//    		IO.println("DocumentManager.tryCreateBuilder: " + Util.printable(sourceText));
    		simBuilder = newSimBuilder;
		} catch (Exception e) {
			IO.println("DocumentManager.tryCreateBuilder: GOT EXCEPTION: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// ***************************************************************
	// *** Semantic Checker
	// ***************************************************************
//	public void semanticChecker() {
//		ProgramModule  programModule = this.getSyntaxTree();
//		if (Option.internal.TRACING)
//			IO.println("BEGIN Semantic Checker");
//		documentManager.simBuilder.duringChecking = true;
//		programModule.doChecking();
//		if (Option.internal.TRACING) {
//			IO.println("END Semantic Checker: \"" + programModule + "\"");
//			if (Option.internal.TRACE_CHECKER_OUTPUT && programModule != null)
//				programModule.print(0);
//		}
//		if(SimulaCompiler.verbose) IO.println("SimulaCompiler.doCompile: " + SimulaCompiler.sourceName + ": Semantic Checker completed");
//		documentManager.simBuilder.duringChecking = false;
//		if(Option.internal.PRINT_SYNTAX_TREE > 0) {
//			IO.println("\nSimulaCompiler.doCompile: =========== Resulting Syntax Tree after Checking ================");
//			programModule.printTree(1);
//		}
//		
//		if (Util.nError > 0) {
//			String msg="Compiler terminate " + SimulaCompiler.sourceName + " after " + Util.nError + " errors during semantic checking";
//			IO.println(msg);
////			Thread.dumpStack();
//			throw new RuntimeException(msg);
//		}
//		
//	}


	/// Delete temporary .class files.
	/// @param dir temporary .class directory
	public static void deleteTempFiles(final File dir) {
		if(DocumentManager.verbose) {
			IO.println("SimulaCompiler.deleteTempFiles:  Delete: " + dir);
//			Thread.dumpStack();
		}
        if (! dir.exists()) {
            Util.IERR("File does not exist: " + dir);
            return;
        }
		Path path = dir.toPath();
        try { Files.walk(path)
	             // Sorts in reverse order (subfolders and files first)
	             .sorted(Comparator.reverseOrder())
	             .forEach(p -> {
	                 try {
	             		if(DocumentManager.verbose) {
	             			IO.println("SimulaCompiler.deleteTempFiles: Delete: " + p);
	             		}
	                     Files.delete(p);
	                 } catch (IOException e) {
	                     Util.IERR("Could not delete: " + p + " - " + e.getMessage());
	                 }
	             });
		} catch (Exception e) {
			Util.IERR("SimulaCompiler.deleteFiles FAILED: ", e);
			e.printStackTrace();
		}
    }

}
