package simula;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.CoreGlobal2;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.TokenManager;
import simula.core.builder.token.LexToken;
import simula.core.coder.SimulaCoder;
import simula.core.coder.SimulaExec;
import simula.core.utilities.LOG;
import simula.core.utilities.SimulaDiagnostic;
import simula.core.utilities.Util;

public class SimulaCoreExports {
	
	// Debug Utility
	// TODO: Innfør Options via argv. Se: SimulaCompiler2'simula.java
	// public static void initiate(SimulaCoreClient client, List<String> argv) {

	public static void initiate(SimulaCoreClient client, Vector<String> argv) {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

		CoreGlobal.initiate();
		CoreGlobal.simulaCoreClient = client;
    	String[] args = argv.toArray(new String[0]);
		Option.decodeArguments(args);
	}

	// Debug Utility
	public static void run(final String documentUri, Vector<String> argv) {
//		IO.println("SimulaCoreExports.run: " + documentUri);
    	DocumentManager documentManager = DocumentManager.getDocumentManager(documentUri);
    	SimulaBuilder simBuilder = documentManager.simBuilder;

    	String[] args = argv.toArray(new String[0]);
		Option.decodeArguments2(args);

//    	SimulaCompiler simulaCompiler = new SimulaCompiler(documentManager);
//    	simulaCompiler.doCompile();
    	if(simBuilder.nErrors != 0) {
    		Util.IERR("Can't generate code due to " + simBuilder.nErrors + " errors.");
    	}
    	try {
			SimulaCoder.doCodeGeneration(simBuilder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(simBuilder.nErrors != 0) {
    		Util.IERR("Can't run due to " + simBuilder.nErrors + " errors.");
    	}
    	try {
			SimulaExec.doRun(simBuilder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ==============================================================================================================
	
	
	/// The document open notification is sent from the client to the server to
	/// signal newly opened text documents. The document's truth is now managed
	/// by the client and the server must not try to read the document's truth
	/// using the document's uri.
    public static void didOpen(final String documentUri, final int version, final String sourceCode) {
//        public DocumentManager documentManager = DocumentManager.get(documentUri);
    	LOG.info("Core.didOpen: BEGIN");
	    DocumentManager.didOpen(documentUri, version, sourceCode);
    }
    
    /// The document change notification is sent from the client to the server to
	/// signal changes to a text document.
	public static void didChange(final String documentUri, final List<SimTextDocumentContentChangeEvent> changes) {
    	LOG.info("DocumentManager.didChange: BEGIN");
		Util.IERR("NOT IMPL");
    	DocumentManager.didChange(documentUri, changes);
	}
	
	/// The document will save notification is sent from the client to the server
	/// before the document is actually saved.
	public static void willSave(final String documentUri, final String reason) {
		LOG.info("Document is about to save: " + documentUri + " due to reason: " + reason);
		Util.IERR("NOT IMPL");
		DocumentManager.willSave(documentUri, reason);
	}
	
	/// The document save notification is sent from the client to the server when
	/// the document is saved in the client.
	public static void didSave(final String documentUri) {
    	LOG.info("DocumentManager.didSave: BEGIN");
		Util.IERR("NOT IMPL");
		DocumentManager.didSave(documentUri);
	}
	
	/// The document close notification is sent from the client to the server
	/// when the document got closed in the client. The document's truth now
	/// exists where the document's uri points to (e.g. if the document's uri is
	/// a file uri the truth now exists on disk).
	public static void didClose(final String documentUri) {
    	LOG.info("DocumentManager.didClose: BEGIN");
		Util.IERR("NOT IMPL");
		DocumentManager.didClose(documentUri);
	}

	
    public static List<LexToken> getTokenList(String documentUri) {
        LOG.info("TokenManager.semanticTokensFullBody: BEGIN");
		Util.IERR("NOT IMPL");
		return TokenManager.getTokenList(documentUri);
    }
	
    public static List<SimulaDiagnostic> getDiagnostics(String documentUri) {
        LOG.info("TokenManager.semanticTokensFullBody: BEGIN");
		Util.IERR("NOT IMPL");
		return TokenManager.getDiagnostics(documentUri);
    }

}
