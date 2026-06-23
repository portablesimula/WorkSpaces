package simula.lsp;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

import simula.lsp.client.SimulaEditorClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

public class ServerLauncher {

    private static boolean DEBUG_MODE = true;
	
    public static void main(String[] args) {
    	//Rydd opp LOGGER OUTPUT: SEE Bookmark Java/Logger
    	System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");

    	// Instansier vår server-implementasjon
    	SimulaLanguageServer server = new SimulaLanguageServer();

    	if(DEBUG_MODE) {
    		LanguageClient client = new SimulaEditorClient();
    		server.connect(client);
    	} else {
    		try {

    			// Bruker standard input/output-strømmer for kommunikasjon med IDE-klienten
    			InputStream input = System.in;
    			OutputStream output = System.out;

    			// Opprett en server-launcher ved hjelp av LSPLauncher
    			Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(server, input, output);

    			// Siden serveren vår implementerer LanguageClientAware, henter vi ut fjernkontrollen (proxy)
    			// til klienten og sender den manuelt inn i serveren vår.
    			LanguageClient client = launcher.getRemoteProxy();
    			server.connect(client);

    			// Start lytting på bakgrunnsstrømmen
    			Future<Void> listeningFuture = launcher.startListening();

    			// Hold applikasjonen i gang så lenge forbindelsen eksisterer  ?????
    			listeningFuture.get();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}

    }
}
