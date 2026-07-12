package simula;

import java.util.List;

import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.SimulaDiagnostic;
import simula.compiler.utilities.Util;
import simula.lsp.compiler.DocumentManager;
import simula.lsp.compiler.TokenManager;
import simula.token.LexToken;

public class SimulaCoreExports {
	
	public static void initiate(SimulaCoreClient client, String packetName) {
		// Remove time, date, and headers from Logger output.
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

		CoreGlobal.initiate();
		CoreGlobal.simulaCoreClient = client;
//		CoreGlobal.packetName="simulaTestBatch";
		CoreGlobal.packetName = packetName;

//		Option.internal.keepJava=userDir; // Generated .java Source is then found in Eclipse Package simulaTestBatch
//		CoreGlobal.simulaRtsLib=new File(simulaDir,"bin"); // To use Eclipse Project's simula.runtime
////		Global.extLib="C:/GitHub/WorkSpaces/Eclipse/SimulaProjects/Simula/src/simulaTestBatch/sim/bin";

	}

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
